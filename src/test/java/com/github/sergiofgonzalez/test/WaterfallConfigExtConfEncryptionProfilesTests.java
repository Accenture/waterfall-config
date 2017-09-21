package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.sergiofgonzalez.test.utils.categories.ActiveTest;

import static com.github.sergiofgonzalez.test.utils.writers.ConfFileUtils.*;

/**
 * Test Cases when:
 *   + using `config/common.conf` for common properties on file
 *   + using `../application007.conf` for external custom properties on file
 *   + using profiles
 *   + using encryption
 * 
 * Things to check:
 *   ToDo
 * 
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigExtConfEncryptionProfilesTests {
	
	private static final Path extConfPath = Paths.get("application008.conf").toAbsolutePath();
	
	private static final List<String> EXTERNAL_CONF_CONTENTS = Arrays.asList(
			"wconf_active_profile: test",
			"}",
			"dev {",
			"  value_defined_in_dev=This value has been taken from dev profile in application008.conf",
			"  value_defined_in_all_profiles=This value has been taken from dev profile in application008.conf",
			"}",
			"test {",
			"  wconf_encryption {",
			"    enabled: true",
			"    algorithm: \"AES/CBC/PKCS5Padding\"",
			"    key_type: AES",
			"    iv: \"D3IwGkX2iRtIVE46CwdOEg==\"",
			"    key_store {",
			"      path: \"classpath://config/wconf-keystore.jceks\"",
			"      password: mystorepasswd",
			"      key {",
			"        alias: wconf-secret-key",
			"        password: mykeypasswd",
			"      }",
			"    }",
			"  }",
			"  encrypted_value_in_test=\"cipher(PiWreyV5lSH8rqPP7/08lu67Lmkqsq0HSlNWImBrXUw=)\"",
			"}",
			"production {",
			"  value_defined_in_production=This value has been taken from production profile in application008.conf",
			"  value_defined_in_all_profiles=This value has been taken from production profile in application008.conf",  
			"}",
			"value_defined_outside_any_profile=This value has been defined in application008.conf"
			);
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		deleteBeforeTest(extConfPath);
		writeFileBeforeTest(extConfPath, EXTERNAL_CONF_CONTENTS);		
		System.setProperty("application_resource", "config/application008.conf");		
	}
	
	
	@Test
	public void testReadEncryptedPropFromConfFile() {		
		String value = wconf().get("encrypted_value_in_application008");
		assertThat(value).isEqualTo("This value has been encrypted");	
	}
	
	@Test
	public void testReadEncryptedPropFromEnvironmentVarDefinedInConf() {
		String value = wconf().get("encrypted_value_in_application008_and_env");
		assertThat(value).isEqualTo("This value has been encrypted");	
	}
	
	@Test
	public void testReadEncryptedPropFromEnvironmentVar() {
		String value = wconf().get("encrypted_value_in_env_var");
		assertThat(value).isEqualTo("This value has been encrypted and put in an environment variable");	
	}	
}
