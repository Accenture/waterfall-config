package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.sergiofgonzalez.test.utils.categories.ActiveTest;

/**
 * Things to check:
 * [ ] Throw IllegalArgumentException if null value is provided in key
 * [ ] Throw IllegalArgumentException if empty string value is provided in key
 * [ ] Return empty value if key is not present in the config/application001.conf & environment variable & system prop
 * [ ] Return default value if key is not present in the config/application001.conf & environment variable & system prop
 * 
 *
 */
@Category(ActiveTest.class)
public class WaterfallConfigNegativeTests {

	@BeforeClass
	public static void runOnlyOnceOnStart() {
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigGetWithNullKey() {
		wconf().getOrElse(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigGetWithEmptyKey() {
		wconf().getOrElse("");
	}
	
	@Test
	public void testWaterfallConfigGetWithPropNotDefined() {
		Optional<String> value = wconf().getOrElse("prop_not_defined");
		assertThat(value).isEmpty();
	}
	
	@Test
	public void testWaterfallConfigGetWithPropNotDefinedWithDefaultValue() {
		Optional<String> value = wconf().getOrElse("prop_not_defined", "default_value");
		assertThat(value.get()).isEqualTo("default_value");
	}
}
