package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import com.typesafe.config.ConfigException;

/**
 * Test Cases when:
 *   + using `config/common.conf` for common properties on file
 *   + using `config/application003.conf` for custom properties on file
 *   + using profiles
 *   + not using encryption
 * 
 * Things to check:
 * [ ] Read encrypted prop only defined in config/application002.conf
 * [ ] Read encrypted prop defined in config/application002.conf and env var
 * [ ] Read encrypted prop defined only in env var
 * 
 * @author sergio.f.gonzalez
 *
 */

public class WaterfallConfigAppConfNoEncryptionProfilesTests {
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		System.setProperty("application_resource", "config/application003.conf");		
	}
	
	@Test
	public void testReadPropOnlyDefinedInActiveProfile() {
		String value = wconf().get("value_defined_in_test");
		assertThat(value).isEqualTo("This value has been taken from test profile in application003.conf");		
	}
	
	@Test
	public void testReadPropDefinedInAllProfiles() {
		String value = wconf().get("value_defined_in_all_profiles");
		assertThat(value).isEqualTo("This value has been taken from test profile in application003.conf");		
	}	
	
	@Test(expected = ConfigException.class)
	public void testReadPropDefinedOutsideAnyProfile() {
		wconf().get("value_defined_outside_any_profile");
	}
	
	@Test
	public void testReadPropDefinedInCommon() {
		String value = wconf().get("only_in_common");
		assertThat(value).isEqualTo("This value has been set on common.conf");		
	}	
	
	@Test
	public void testReadPropDefinedInEnvironmentVariableOnly() {
		String value = wconf().get("in_env_var");
		assertThat(value).isEqualTo("This value has been set in an environment variable");		
	}
	
	@Test
	public void testReadPropDefinedInEnvironmentVariableAndProfile() {
		String value = wconf().get("in_environment_var_and_profile");
		assertThat(value).isEqualTo("This value has been set in an environment variable");		
	}
	
	@Test
	public void testReadPropDefinedAsJavaPropOnly() {
		String value = wconf().get("in_system_prop");
		assertThat(value).isEqualTo("This value has been set in a Java system prop");		
	}
	
	@Test
	public void testReadPropDefinedAsJavaPropAndProfile() {
		String value = wconf().get("in_java_property_and_profile");
		assertThat(value).isEqualTo("This value has been set in a Java system prop");		
	}
	
	@Test
	public void testReadPropDefinedInEnvVarAndAsJavaPropAndProfile() {
		String value = wconf().get("in_environment_var_and_java_property_and_profile");
		assertThat(value).isEqualTo("This value has been set in an environment variable");		
	}	
}
