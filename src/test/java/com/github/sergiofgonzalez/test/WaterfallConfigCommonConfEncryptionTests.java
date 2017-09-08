package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.sergiofgonzalez.test.utils.categories.ActiveTest;

/**
 * Test Cases when:
 *   + only using `config/common.conf` for properties on file (i.e. no application conf, no profiles...)
 *   + using encryption
 * 
 * Things to check:
 * [ ] Encrypted property defined in config/common
 * [ ] Encrypted property defined in config/common and environment variable
 * [ ] Encrypted property defined in config/common and Java system property
 * [ ] Encrypted property defined only in environment variable
 * [ ] Encrypted property defined only in Java system property
 * [ ] Encrypted property defined in environment variable and Java system property
 * 
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigCommonConfEncryptionTests {
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		System.clearProperty("wconf_app_properties");
		System.setProperty("wconf_encryption.enabled", "true");
	}
		
	@Test
	public void testReadEncryptedPropFromConfFile() {
		String value = wconf().get("encrypted_value_in_common");
		assertThat(value).isEqualTo("This value has been encrypted");	
	}
	
	@Test
	public void testReadEncryptedPropFromEnvironmentVarAndDefinedInCommon() {
		String value = wconf().get("encrypted_value_in_common_and_env");
		assertThat(value).isEqualTo("This value has been encrypted and put in an environment variable");	
	}
	
	@Test
	public void testReadEncryptedPropFromSystemPropAndDefinedInCommon() {
		String value = wconf().get("encrypted_value_in_common_and_system_prop");
		assertThat(value).isEqualTo("This value has been encrypted and put in a Java system property");	
	}	
	
	@Test
	public void testReadEncryptedPropFromEnvironmentVar() {
		String value = wconf().get("encrypted_value_in_env_var");
		assertThat(value).isEqualTo("This value has been encrypted and put in an environment variable");	
	}
	
	@Test
	public void testReadEncryptedPropFromJavaSystemProperty() {
		String value = wconf().get("encrypted_value_in_system_prop");
		assertThat(value).isEqualTo("This value has been encrypted and put in a Java system property");	
	}	
	
	@Test
	public void testReadEncryptedPropFromEnvVarAndJavaSystemProperty() {
		String value = wconf().get("encrypted_value_in_env_var_and_system_prop");
		assertThat(value).isEqualTo("This value has been encrypted and put in an environment variable");	
	}		
}
