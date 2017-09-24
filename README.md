# Waterfall Config Library for Java
> a simplified configuration library for the JVM, heavily-based on *typesafehub Config*, with some additional features and strongly opinionated

# wconf()
Use `wconf()` for configuration properties management in Java project. *wconf* allows for hierarchical (i.e. waterfall) merging of configuration properties with an established precedence between the sources.

## Simple Usage

Modify your `pom.xml` to add a dependency for *wconf*:
```xml
    <dependency>
      <groupId>com.github.sergiofgonzalez</groupId>
      <artifactId>waterfall-config</artifactId>
      <version>1.0.0</version>
    </dependency>
```

Create a file `common.conf` under `src/main/resources/config` with the config properties for your application:
```
message.en: "Hello to Jason Isaacs!"
message.es: "¡Hola a Jason Isaacs!"
```

Include a static import for the `WaterfallConfig` functions and start using your configuration values:

```java
...
import static com.github.sergiofgonzalez.wconf.WaterfallConfig.*;
...

    System.out.println(wconf().get("message.en"));
...
``` 


## Waterfall and Merge
*wconf* defines a hierarchy between the different configuration property sources it supports, so that properties found in a source with a higher precedence will overwrite properties found in sources with lower precedence.

The precedence is established as follows, from lowest to highest:
+ a file `config/common.conf` packaged in the jar
+ a file `config/application.conf` packaged in the jar
+ Java System Properties
+ OS Environment Variables
+ A file `application.conf` found outside the jar

For example, if you have:
+ a file `config/common.conf` within the jar with the line `best_actor=Jason Isaacs`
+ a file `config/application.conf` within the jar with the line `best_actor=Riz Ahmed`
+ an environment variable defined as `best_actor=Idris Elba`

invoking `wconf().get("best_actor")` will render `"Idris Elba"` as result. 

*wconf* also supports merging of configuration properties between sources, so that configuration properties sharing a common parent component will be merged.

For example, if you have:
+ a file `config/common.conf` with the line `message.greeting=Hello to Jason Isaacs`
+ a file `config/application.conf` with the line `message.farewell=Enjoy your evening`

both `message.greeting` and `message.farewell` will be available.

## Customizing Application Configuration Files
As stated above, by default, *wconf* will try to find properties in the following files:
+ A `config/common.conf` within the jar with the common configuration properties
+ A `config/application.conf` within the jar with application-level properties
+ An `application.conf` file with application-level specific properties not packaged in the jar 

While the name for `config/common.conf` cannot be customized *wconf()* allows you to select a custom name for the application configuration by setting the property `application_resource` to a value different from `config/application.conf`.

The config key `wconf_ext_app_properties_paths` can be used to customize the paths that will be scanned while looking for the application-level specific properties not packaged in the jar. The default for this value is `[ "./" ]`. You will typically want to specify such value for your application in `common.conf`:
```
wconf_ext_app_properties_paths=[ "./", "/tmp/", "/shared-volume/" ]
```

but it can also be specified using environment variables and properties using the syntax:
```
-D wconf_ext_app_properties_paths.0="./" \
-D wconf_ext_app_properties_paths.1="./tmp/" \
-D wconf_ext_app_properties_paths.2="./shared-volume/"
```
  
**Note**
Setting the value of `wconf_ext_app_properties_paths` in the application-level property file will have **no effect**, as this value is needed in creation time while the application level property source is being discovered.  
  
## Syntax for Configuration Files
The syntax for configuration files is a JSON superset called *HOCON* that is used by the library doing all the heavy lifting for *wconf* (see [Typesafehub Config](https://github.com/typesafehub/config#user-content-using-hocon-the-json-superset).

*HOCON* allows you to use plain java-style properties if that is what you like:
```
foo1.bar1=foobar11
foo1.bar2=foobar12

foo2.bar1=foobar21
```

but I find much more expressive to use explicit grouping:
```
modes {
  available: [encrypt, decrypt, geniv]
  active: geniv
}

geniv {
  algorithm: "AES/CBC/PKCS5Padding"
}
```

You can find more usage examples in the link above and in the test resources within the project.

## Using Profiles
Another opinionated feature of *wconf* is the profile selection. Using grouping of configuration variables and a configuration property named `wconf_active_profile` lets you activate a portion of the complete application configuration properties.

```
wconf_active_profile: test

dev {
  foo=bar in dev
  message=to be used in dev environment
}

test {
  foo=bar in test
  message=to be used in test environment  
}

production {
  foo=bar in prod
  message=to be used in prod environment
}
```

As `wconf_active_profile` is set to test, `wconf().get(foo)` will render `"bar in test"` as the result.

Note that when using profiles:
+ Only the specific section of the `config/application.conf` and `application.conf` will be enabled. Information found in other profiles or outside any profile will not be visible to *wconf*.
+ You can override values found in the active profile in environment variables and system properties without having to include the profile prefix. That is, the value for `foo` can be overridden defining an environment variable `foo=new value for active profile`.
+ Profile rules do not affect the information in `config/common.conf`. If you wish to use profiles, plan for having application configuration.

You can find examples of profile usage in the test section.

## Encrypted Properties
A recurring requirement for configuration properties is the support of symmetric encryption for sensible data such as datasource passwords.

*wconf* features support for flexible encryption of configuration properties using the Java Cryptography Extension (JCE). Please note that you should be aware of the best practices and recommendations when using this feature, as symmetric encryption is useless is the key is handled carelessly (distributed without proper control or committed to your source code repository).

**Note**
+ The keystore and key found in the test section is provided for **demonstration purposes** and should **not** be used in applications.

In order to enable encryption you will need the following:
1. Create a *JCEKS Key Store* with a secret key
2. Configure in the common or application level properties the encryption details
3. Encrypt the sensitive values using the generated Key Store and encryption details and include them as properties

### Creating a JCEKS Key Store and Adding a Secret Key
A JCEKS Key Store is a Java Key Store that allows you to store secret keys for symmetric encryption (typically AES256). The `keytool` JDK utility lets you generate such a Key Store with a somewhat complicated *spell*:

```bash
$ keytool -genseckey \
-alias wconf-secret-key -keyalg AES -keysize 256 -keypass mykeypasswd \
-storetype JCEKS -storepass mystorepasswd -keystore ./wconf-keystore.jceks
```

The previous command, generates a *JCEKS Key Store* in a file `./wconf-keystore.jceks` with a Key Store password `mystorepasswd` and saves in it an AES256 secret key you can use for symmetric encryption. The key is assigned the alias `wconf-secret-key` and protects the key access (facepalm) with the password `mykeypasswd`. 

### Configuring the Encryption Details in the Configuration
*wconf* expects the following configuration properties to be defined:
```
wconf_encryption {
  enabled: <true|false>
  algorithm: <the symmetric algorithm to use, e.g. AES/CBC/PKCS5Padding>
  key_type: <the type of the secret key, e.g. AES>
  iv: <the initialization vector for the symmetric encryption process>
  key_store {
    path: <the path to the JCEKS key store, to scan the JAR prefix with "classpath://", e.g. classpath://config/wconf-keystore.jceks>
    password: <the keystore password, e.g. mystorepasswd>
    key {
      alias: <the key alias, e.g.wconf-secret-key>
      password: <the password key, e.g. mykeypasswd>
    }
  }
}
```

Note that this configuration can be included in the common or application level properties (or even merged between both common and application level properties).
If using profiles, it is a good practice to use different Key Stores for each profile.

If you don't know how to generate an initialization vector, please keep reading.

### Encrypting Sensitive Properties
In order to specify an encrypted property you have to use the following syntax:
```
key="cipher(<encrypted-then-encoded-in-base64-value>)"
```

Note that the quotes `"` are required because byte sequences encoded in Base64 typically include `=`.

For example:
```
foo="cipher(PiWreyV5lSH8rqPP7/08lu67Lmkqsq0HSlNWImBrXUw=)"
```

If you don't know how to generate an *"encrypted and then encoded in Base64 value"*, please keep reading.

### Cryptools
[*Cryptools*](https://github.com/sergiofgonzalez/cryptools) is a simple Java application that can be used to:
+ Generate an initialization vector for the symmetric encryption process
+ Encrypt and Decrypt values to be used in *wconf*


## Reserved Configuration Property Names
The configuration properties used to configure *wconf* are always prepended with `wconf_` and are reserved.

The current list all these reserved property names and their descriptions:

| Reserved Property Name | Description | Default|
|------------------------|-------------|--------|
| wconf_app_properties | identifies the path and filename of the application level configuration file. | config/application.conf |
| wconf_ext_app_properties_paths | an optional list of paths that will be scanned when looking for the application level configuration file outside the jar | [ "./" ] |
| wconf_active_profile  | identifies the portion of the application level configuration that will be activated | n/a |
| wconf_encryption.enabled | switches encryption support on and off | false |
| wconf_encryption.algorithm | the string representing the symmetric algorithm to use, e.g. "AES/CBC/PKCS5Padding" | n/a |
| wconf_encryption.key_type | the string representing the type of symmetric key found in the key store, e.g. "AES" | n/a |
| wconf_encryption.iv | the initialization vector for the symmetric encryption process, encoded in base 64 | n/a |
| wconf_encryption.key_store.path | the path to the JCEKS key store, which can be a path to a resource within the jar, e.g. config/wconf-keystore.jceks | n/a |
| wconf_encryption.key_store.password | the key store password, e.g. mystorepasswd | n/a |
| wconf_encryption.key_store.key.alias | the key alias, that is the string, used to identify the key to use for the symmetric encryption from the ones defined in the key store,  e.g.wconf-secret-key | n/a |
| wconf_encryption.key_store.key.password | the password key that provides access to the key to use for the symmetric encryption, e.g. mykeypasswd | n/a |

Note that the variables susceptible of being overridden by environment variables do not use `.` as there are operating systems that prevent variables such as `profile.active` from being defined.

## Implementation Details and Recommendations
*wconf* is defined as an eagerly-loaded singleton that loads and preconfigures the *Typesafehub Config* framework according to a set of predefined choices.

*wconf* is targeted for lightweight, non-container based, read-only, non-refreshable use cases. If your use case allows for an IoC container I recommend you to have a look at [Spring Boot](https://projects.spring.io/spring-boot/). 

If the opinionated choices of *wconf* are not acceptable, feel free to fork and modify the project, or fallback to the awesome framework in which *wconf* relies: [Typesafehub Config](https://github.com/typesafehub/config).

## Tests and Examples
Tests can be found under `src/test/resources` and those serve also as usage examples for both accessing config properties and specifying configuration properties.

The [Cryptools](https://github.com/sergiofgonzalez/cryptools) also uses *wconf*.

Note:
+ Encryption feature has only been tested with AES256. Note that to use AES256 encryption you have to enable the JCE Unlimited Strength feature in your JRE/JDK. Failing to enable that feature may raise a `java.security.InvalidKeyException: Illegal key size` exception.

## License
The license is MIT, see [LICENSE](./LICENSE) for the details.

## Contributing
Contributions will be gladly accepted, but this section is currently in progress.
For general information see [general guidelines for contributing on GitHub](https://guides.github.com/activities/contributing-to-open-source/).

