package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.sergiofgonzalez.test.utils.categories.ActiveTest;

import static com.github.sergiofgonzalez.test.utils.writers.ConfFileUtils.*;

/**
 * Test Cases when:
 *   + using `config/common.conf` for common properties on file
 *   + using `config/application005.conf` for custom properties on file
 *   + using `../application005.conf` for external custom properties on file
 *   + not using profiles
 *   + not using encryption
 * 
 * Things to check:
 *  ToDo
 *  
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigExtConfNoEncryptionNoProfilesTests {
	
	private static final Path extConfPath = Paths.get("application005.conf").toAbsolutePath();
	
	private static final List<String> EXTERNAL_CONF_CONTENTS = Arrays.asList(
			"only_in_application005=This value has been set on application005.conf",
			"also_in_application005=This value has been set on application005.conf",
			"in_application_ext_and_in_jar=This value has been set on application005.conf",
			"in_app005_and_env_var=This value has been set on application005.conf",
			"in_app005_and_java_property=This value has been set on application005.conf",
			"in_app005_and_env_var_and_property=This value has been set on application005.conf",
			"wconf_encryption {",
			"  enabled: false",
			"}",
			"encrypted_value_in_app005=\"cipher(PiWreyV5lSH8rqPP7/08lu67Lmkqsq0HSlNWImBrXUw=)\""
			);
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		deleteTestResource(extConfPath);
		writeFileBeforeTest(extConfPath, EXTERNAL_CONF_CONTENTS);		
		System.setProperty("wconf_app_properties", "config/application005.conf");		
	}
	
	@AfterClass
	public static void runOnlyOnceOnEnd() {
		deleteTestResource(extConfPath);
	}
	
	@Test
	public void testReadPropOnlyDefinedInCommon() {
		String value = wconf().get("only_in_common");
		assertThat(value).isEqualTo("This value has been set on common.conf");		
	}
	
	@Test
	public void testReadPropOnlyDefinedInAppConf() {
		String value = wconf().get("only_in_application005");
		assertThat(value).isEqualTo("This value has been set on application005.conf");		
	}
	
	@Test
	public void testReadPropDefinedInCommonAndExtAppConf() {
		String value = wconf().get("also_in_application005");
		assertThat(value).isEqualTo("This value has been set on application005.conf");		
	}
	
	@Test
	public void testReadPropDefinedInExtConfAndInternalConf() {
		String value = wconf().get("in_application_ext_and_in_jar");
		assertThat(value).isEqualTo("This value has been set on application005.conf");		
	}
	
	@Test
	public void testPropDefinedInExtConfAndEnvVar() {
		String value = wconf().get("in_app005_and_env_var");
		assertThat(value).isEqualTo("This value has been set on application005.conf");				
	}
	
	@Test
	public void testPropDefinedInExtConfAndSystemProp() {
		String value = wconf().get("in_app005_and_java_property");
		assertThat(value).isEqualTo("This value has been set on application005.conf");	
	}
	
	@Test
	public void testPropDefinedInExtConfAndEnvVarAndSystemProp() {
		String value = wconf().get("in_app005_and_env_var_and_property");
		assertThat(value).isEqualTo("This value has been set on application005.conf");	
	}
	
	@Test(expected = IllegalStateException.class)
	public void testEncryptedPropWithEncryptionDisableShouldFail() {
		wconf().get("encrypted_value_in_app005");
	}	
}
