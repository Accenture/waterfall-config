package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import com.typesafe.config.ConfigException;

import static com.github.sergiofgonzalez.test.utils.writers.ConfFileUtils.*;

/**
 * Test Cases when:
 *   + using `config/common.conf` for common properties on file
 *   + using `config/application006.conf` for custom properties on file
 *   + using `../application006.conf` for external custom properties on file
 *   + using profiles
 *   + not using encryption
 * 
 * Things to check:
 *   ToDo
 *   
 * @author sergio.f.gonzalez
 *
 */

public class WaterfallConfigExtConfNoEncryptionProfilesTests {
	
	private static final Path extConfPath = Paths.get("application006.conf").toAbsolutePath();
	
	private static final List<String> EXTERNAL_CONF_CONTENTS = Arrays.asList(
			"profiles {",
			"  available: [dev, test, production]",
			"  active: test",
			"}",
			"dev {",
			"  value_defined_in_dev=This value has been taken from dev profile in application006.conf",
			"  value_defined_in_all_profiles=This value has been taken from dev profile in application006.conf",
			"}",
			"test {",
			"  value_defined_in_test=This value has been taken from test profile in application006.conf",
			"  value_defined_in_all_profiles=This value has been taken from test profile in application006.conf",
			"  in_environment_var_and_profile=This value has been taken from test profile in application006.conf",
			"  in_java_property_and_profile=This value has been taken from test profile in application006.conf",
			"  in_environment_var_and_java_property_and_profile=This value has been taken from test profile in application006.conf",
			"  in_external_and_external_conf=This value has been taken from test profile in application006.conf",
			"}",
			"production {",
			"  value_defined_in_production=This value has been taken from production profile in application006.conf",
			"  value_defined_in_all_profiles=This value has been taken from production profile in application006.conf",  
			"}",
			"value_defined_outside_any_profile=This value has been defined in application006.conf"
		);
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		deleteBeforeTest(extConfPath);
		writeFileBeforeTest(extConfPath, EXTERNAL_CONF_CONTENTS);		
		System.setProperty("application_resource", "config/application006.conf");		
	}
		
	@Test
	public void testReadPropOnlyDefinedInActiveProfile() {
		String value = wconf().get("value_defined_in_test");
		assertThat(value).isEqualTo("This value has been taken from test profile in application006.conf");		
	}
	
	@Test
	public void testReadPropDefinedInAllProfiles() {
		String value = wconf().get("value_defined_in_all_profiles");
		assertThat(value).isEqualTo("This value has been taken from test profile in application006.conf");		
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
		assertThat(value).isEqualTo("This value has been taken from test profile in application006.conf");		
	}
	
	@Test
	public void testReadPropDefinedAsJavaPropOnly() {
		String value = wconf().get("in_system_prop");
		assertThat(value).isEqualTo("This value has been set in a Java system prop");		
	}
	
	@Test
	public void testReadPropDefinedAsJavaPropAndProfile() {
		String value = wconf().get("in_java_property_and_profile");
		assertThat(value).isEqualTo("This value has been taken from test profile in application006.conf");		
	}
	
	@Test
	public void testReadPropDefinedInEnvVarAndAsJavaPropAndProfile() {
		String value = wconf().get("in_environment_var_and_java_property_and_profile");
		assertThat(value).isEqualTo("This value has been taken from test profile in application006.conf");		
	}	
	
	@Test
	public void testReadPropDefinedInExternalConfAndWithinJar() {
		String value = wconf().get("in_external_and_external_conf");
		assertThat(value).isEqualTo("This value has been taken from test profile in application006.conf");		
	}	
}
