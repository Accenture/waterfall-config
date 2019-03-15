package com.accenture.wconf.test;

import static com.accenture.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.accenture.wconf.test.utils.categories.ActiveTest;

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


@Category(ActiveTest.class)
public class WaterfallConfigAppConfEncryptionProfilesTests {
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		System.setProperty("wconf_app_properties", "config/application004.conf");		
	}
	
	@Test
	public void testReadEncryptedPropInActiveProfile() {
		String value = wconf().get("encrypted_value_in_test");
		assertThat(value).isEqualTo("This value has been encrypted");		
	}
	
	@Test
	public void testReadEncryptedPropInActiveProfileUsingSafeGet() {
		Optional<String> value = wconf().safeGet("encrypted_value_in_test");
		assertThat(value).isPresent();
		assertThat(value.get()).isEqualTo("This value has been encrypted");		
	}	
	
	@Test
	public void testReadEncryptedPropInActiveProfileUsingGetOrElse() {
		String value = wconf().getOrElse("encrypted_value_in_test", "default-value");
		assertThat(value).isEqualTo("This value has been encrypted");		
	}		
}
