package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test Cases when:
 *   + using `config/common.conf` for common properties on file
 *   + using `config/application004.conf` for custom properties on file
 *   + using profiles
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


public class WaterfallConfigAppConfEncryptionProfilesTests {
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		System.setProperty("application_resource", "config/application004.conf");		
	}
	
	@Test
	public void testReadEncryptedPropInActiveProfile() {
		String value = wconf().get("encrypted_value_in_test");
		assertThat(value).isEqualTo("This value has been encrypted");		
	}
}
