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
 *   + using `../application007.conf` for external custom properties on file
 *   + not using profiles
 *   + using encryption
 * 
 * Things to check:
 *   ToDo
 *   
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigExtConfEncryptionNoProfilesTests {
	
	private static final Path EXT_CONF_PATH = Paths.get("application007.conf").toAbsolutePath();
	private static final Path EXT_KEYSTORE_PATH = Paths.get("ext-wconf-keystore.jceks").toAbsolutePath();
	private static final Path INT_KEYSTORE_PATH = Paths.get("src", "test", "resources", "config", "wconf-keystore.jceks");
	
	private static final List<String> EXTERNAL_CONF_CONTENTS = Arrays.asList(
			"wconf_encryption {",
			"  enabled: true",
			"  algorithm: AES/CBC/PKCS5Padding",
			"  key_type: AES",
			"  iv: \"D3IwGkX2iRtIVE46CwdOEg==\"",
			"  key_store {",
			"    path: \"./ext-wconf-keystore.jceks\"",
			"    password: mystorepasswd",
			"    key {",
			"      alias: wconf-secret-key",
			"      password: mykeypasswd",
			"    }",
			"  }",
			"}",			
			"encrypted_value_in_application007=\"cipher(PiWreyV5lSH8rqPP7/08lu67Lmkqsq0HSlNWImBrXUw=)\"",
			"encrypted_value_in_application007_and_env=\"cipher(PiWreyV5lSH8rqPP7/08lu67Lmkqsq0HSlNWImBrXUw=)\""
			);
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		deleteTestResource(EXT_CONF_PATH);
		deleteTestResource(EXT_KEYSTORE_PATH);
		writeFileBeforeTest(EXT_CONF_PATH, EXTERNAL_CONF_CONTENTS);
		copyFileBeforeTest(INT_KEYSTORE_PATH, EXT_KEYSTORE_PATH);
		System.setProperty("wconf_app_properties", "config/application007.conf");
	}
	
	@AfterClass
	public static void runOnlyOnceOnEnd() {
		deleteTestResource(EXT_CONF_PATH);
		deleteTestResource(EXT_KEYSTORE_PATH);		
	}
	
	@Test
	public void testReadEncryptedPropFromConfFile() {		
		String value = wconf().get("encrypted_value_in_application007");
		assertThat(value).isEqualTo("This value has been encrypted");	
	}
	
	@Test
	public void testReadEncryptedPropFromEnvironmentVarDefinedInConf() {
		String value = wconf().get("encrypted_value_in_application007_and_env");
		assertThat(value).isEqualTo("This value has been encrypted");	
	}
	
	@Test
	public void testReadEncryptedPropFromEnvironmentVar() {
		String value = wconf().get("encrypted_value_in_env_var");
		assertThat(value).isEqualTo("This value has been encrypted and put in an environment variable");	
	}	
}
