package com.accenture.wconf.test;

import static com.accenture.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.NoSuchAlgorithmException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.accenture.wconf.test.utils.categories.ActiveTest;

/**
 * Test Cases when:
 *   + the encryption details are not correctly configured
 * 
 * 
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigEncryptionUnexistingAlgorithmErrorTests {
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		System.clearProperty("wconf_app_properties");
		System.setProperty("wconf_encryption.enabled", "true");
		System.setProperty("wconf_encryption.algorithm", "unexisting-algorithm");
	}
		
	@Test
	public void testUnexistingAlgorithmThrowsException() {
		assertThatThrownBy(() -> {
			wconf().get("encrypted_value_in_common");
		}).hasRootCauseExactlyInstanceOf(NoSuchAlgorithmException.class);
	}
}
