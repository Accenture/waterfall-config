package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.sergiofgonzalez.test.utils.categories.ActiveTest;

/**
 * Test Cases when:
 *   + key is not found in the key store
 * 
 * 
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigEncryptionUnexistingKeyErrorTests {
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		System.clearProperty("wconf_app_properties");
		System.setProperty("wconf_encryption.enabled", "true");
		System.setProperty("wconf_encryption.key_store.key.alias", "unexisting-key");
		System.setProperty("wconf_encryption.algorithm", "unexisting-algorithm");
	}
		
	@Test
	public void testUnexistingKeyThrowsException() {
		assertThatThrownBy(() -> {
			wconf().get("encrypted_value_in_common");
		}).hasRootCauseExactlyInstanceOf(IllegalStateException.class);
	}
}
