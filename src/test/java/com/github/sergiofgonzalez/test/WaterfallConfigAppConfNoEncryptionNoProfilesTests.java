package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.sergiofgonzalez.test.utils.categories.ActiveTest;

/**
 * Test Cases when:
 *   + using `config/common.conf` for common properties on file
 *   + using `config/application1.conf` for custom properties on file
 *   + not using profiles
 *   + not using encryption
 * 
 * Things to check:
 * [ ] Read property only defined in config/application001.conf
 * [ ] Read property defined in config/application001.conf and common.conf
 * [ ] Read property defined in config/application001.conf and environment var
 * [ ] Read property defined in config/application001.conf and system prop
 * [ ] Read property defined in config/application001.conf, system prop and environment var
 * [ ] Read property defined as environment var and not defined in config/application001.conf
 * [ ] Read property defined as java system prop and not defined in config/application001.conf
 * [ ] Read property defined as java system prop and environment var and not defined in config/application001.conf
 * [ ] Read encrypted property

 * 
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigAppConfNoEncryptionNoProfilesTests {
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		System.clearProperty("wconf_encryption.enabled");
		System.setProperty("wconf_app_properties", "config/application001.conf");		
	}
		
	@Test
	public void testReadPropOnlyDefinedInCommon() {
		String value = wconf().get("only_in_common");
		assertThat(value).isEqualTo("This value has been set on common.conf");		
	}
	
	@Test
	public void testReadPropOnlyDefinedInAppConf() {
		String value = wconf().get("only_in_application001");
		assertThat(value).isEqualTo("This value has been set on application001.conf");		
	}
	
	@Test
	public void testReadPropDefinedInCommonAndAppConf() {
		String value = wconf().get("in_application001_and_common");
		assertThat(value).isEqualTo("This value has been set on application001.conf");		
	}
	
	
	@Test
	public void testPropCanBeOverriddenByEnvironmentVar() {
		String value = wconf().get("in_application001_but_overridden_by_env");
		assertThat(value).isEqualTo("This value has been set in an environment variable");				
	}
	
	@Test
	public void testPropCanBeOverriddenByJavaSystemProperty() {		
		String value = wconf().get("in_application001_but_overridden_by_java_property");
		assertThat(value).isEqualTo("This value has been set in a Java system property");	
	}
	
	@Test
	public void testPropDefinedAsEnvVarTakesPrecedence() {
		String value = wconf().get("in_application001_but_overridden_by_env_and_property");
		assertThat(value).isEqualTo("This value has been set in an environment variable");	
	}
	
	@Test(expected = IllegalStateException.class)
	public void testEncryptedPropWithEncryptionDisableShouldFail() {
		wconf().get("encrypted_value_in_application001");
	}
	
	@Test
	public void testPropDefinedAsEnvVar() {
		String value = wconf().get("in_env_var");
		assertThat(value).isEqualTo("This value has been set in an environment variable");	
	}

	@Test
	public void testPropDefinedAsSystemProp() {
		String value = wconf().get("in_system_prop");
		assertThat(value).isEqualTo("This value has been set in a Java system prop");	
	}
	
	@Test
	public void testPropDefinedAsEnvVarAndSystemProp() {
		String value = wconf().get("in_env_var_and_system_prop");
		assertThat(value).isEqualTo("This value has been set in an environment variable");	
	}	
}
