package com.accenture.wconf.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.accenture.wconf.test.utils.categories.ActiveTest;

import static com.accenture.wconf.WaterfallConfig.wconf;
import static com.accenture.wconf.test.utils.writers.ConfFileUtils.*;

/**
 * Test Cases validating the scanning of external paths to find application-level properties
 * when only one path is passed (instead of an array)
 *   
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigExtPathSingleValueTests {
	
	private static final UUID uuid = UUID.randomUUID();
	private static final String appConfFilename = String.format("/tmp/application_%s.conf", uuid.toString());
	private static final Path extConfPath = Paths.get(appConfFilename).toAbsolutePath();
	
	private static final List<String> EXTERNAL_CONF_CONTENTS = Arrays.asList(
			"wconf_active_profile: test",
			"dev {",
			"  value_defined_in_dev=This value has been taken from dev profile in application009.conf",
			"  value_defined_in_all_profiles=This value has been taken from dev profile in application009.conf",
			"}",
			"test {",
			"  value_defined_in_test=This value has been taken from test profile in application009.conf",
			"  value_defined_in_all_profiles=This value has been taken from test profile in application009.conf",
			"  in_environment_var_and_profile=This value has been taken from test profile in application009.conf",
			"  in_java_property_and_profile=This value has been taken from test profile in application009.conf",
			"  in_environment_var_and_java_property_and_profile=This value has been taken from test profile in application009.conf",
			"  in_external_and_external_conf=This value has been taken from test profile in application009.conf",
			"}",
			"production {",
			"  value_defined_in_production=This value has been taken from production profile in application009.conf",
			"  value_defined_in_all_profiles=This value has been taken from production profile in application009.conf",  
			"}",
			"value_defined_outside_any_profile=This value has been defined in application009.conf"			
		);
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		deleteTestResource(extConfPath);
		writeFileBeforeTest(extConfPath, EXTERNAL_CONF_CONTENTS);		
		System.setProperty("wconf_app_properties", String.format("config/%s", appConfFilename));

		System.setProperty("wconf_ext_app_properties_paths", "/tmp/");
	}
		
	@AfterClass
	public static void runOnlyOnceOnEnd() {
		deleteTestResource(extConfPath);
	}
	
	@Test
	public void testReadPropOnlyDefinedInActiveProfile() {
		String value = wconf().get("value_defined_in_test");
		assertThat(value).isEqualTo("This value has been taken from test profile in application009.conf");		
	}
}
