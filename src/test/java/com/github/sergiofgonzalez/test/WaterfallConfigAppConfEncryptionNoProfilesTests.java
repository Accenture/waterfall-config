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
 *   + using `config/application002.conf` for custom properties on file
 *   + not using profiles
 *   + using encryption
 * 
 * Things to check:
 * [ ] Read encrypted prop only defined in config/application002.conf
 * [ ] Read encrypted prop defined in config/application002.conf and env var
 * [ ] Read encrypted prop defined only in env var
 * 
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigAppConfEncryptionNoProfilesTests {
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		System.setProperty("wconf_app_properties", "config/application002.conf");		
		System.setProperty("incorrect_encrypted_value", "cipher(thisisnotacipher)");
	}
	
	@Test
	public void testReadEncryptedPropFromConfFile() {		
		String value = wconf().get("encrypted_value_in_application002");
		assertThat(value).isEqualTo("This value has been encrypted");	
	}
	
	@Test
	public void testReadEncryptedPropFromEnvironmentVarDefinedInConf() {
		String value = wconf().get("encrypted_value_in_application002_and_env");
		assertThat(value).isEqualTo("This value has been encrypted and put in an environment variable");	
	}
	
	@Test
	public void testReadEncryptedPropFromEnvironmentVar() {
		String value = wconf().get("encrypted_value_in_env_var");
		assertThat(value).isEqualTo("This value has been encrypted and put in an environment variable");	
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReadIncorrectCipher() {
		wconf().get("incorrect_encrypted_value");
	}
}
