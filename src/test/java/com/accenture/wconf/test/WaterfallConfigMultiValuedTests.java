package com.accenture.wconf.test;

import static com.accenture.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.accenture.wconf.test.utils.categories.ActiveTest;

@Category(ActiveTest.class)
public class WaterfallConfigMultiValuedTests {
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		System.setProperty("wconf_app_properties", "config/application008.conf");		
	}
	
	@Test
	public void testWaterfallConfigClassicGetMultiValued() {
		List<String> values = wconf().get("multi_valued", true);
		assertThat(values).containsExactly("uno", "dos", "tres", "catorce");
	}
	
	@Test
	public void testWaterfallConfigSafeGetMultiValued() {
		Optional<List<String>> values = wconf().safeGet("multi_valued", true);
		assertThat(values).isPresent();
		assertThat(values.get()).containsExactly("uno", "dos", "tres", "catorce");
	}
	
	
	@Test
	public void testWaterfallConfigGetOrElseMultiValued() {
		List<String> values = wconf().getOrElse("multi_valued", Arrays.asList("happy", "friday"), true);
		assertThat(values).containsExactly("uno", "dos", "tres", "catorce");
	}
}
