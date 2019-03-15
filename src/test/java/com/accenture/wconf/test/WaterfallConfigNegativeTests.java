package com.accenture.wconf.test;

import static com.accenture.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.accenture.wconf.test.utils.categories.ActiveTest;
import com.typesafe.config.ConfigException;

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
	
	@Test(expected = ConfigException.class)
	public void testWaterfallConfigClassicGetNonExistentKey() {
		wconf().get("this-key-will-not-be-found");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigClassicGetWithNullKey() {
		wconf().get(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigGetWithNullKey() {
		wconf().getOrElse(null, "default-value");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigGetWithEmptyKey() {
		wconf().getOrElse("", "default-Value");
	}
		
	@Test
	public void testWaterfallConfigGetWithPropNotDefinedWithDefaultValue() {
		String value = wconf().getOrElse("prop_not_defined", "default_value");
		assertThat(value).isEqualTo("default_value");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigSafeGet() {
		wconf().safeGet(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigSafeGetWithEmptyKey() {
		wconf().safeGet("");
	}
		
	@Test
	public void testWaterfallConfigSafeGetWithPropNotDefinedWithDefaultValue() {
		Optional<String> value = wconf().safeGet("prop_not_defined");
		assertThat(value).isEmpty();
	}
	
	/* multi-valued */
	@Test(expected = ConfigException.class)
	public void testWaterfallConfigClassicGetMultiNonExistentKey() {
		wconf().get("this-key-will-not-be-found", true);
	}
	
	@Test(expected = NullPointerException.class)
	public void testWaterfallConfigClassicGetMultiWithNullKey() {
		wconf().get(null, true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigGetMultiWithNullKey() {
		wconf().getOrElse(null, Arrays.asList("default-value"), true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigGetMultiWithEmptyKey() {
		wconf().getOrElse("", Arrays.asList("default-Value"), true);
	}
		
	@Test
	public void testWaterfallConfigGetMultiWithPropNotDefinedWithDefaultValue() {
		List<String> values = wconf().getOrElse("prop_not_defined", Arrays.asList("Hello", "to", "Jason", "Isaacs"), true);
		assertThat(values).containsExactly("Hello", "to", "Jason", "Isaacs");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigSafeGetMultivalued() {
		wconf().safeGet(null, true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWaterfallConfigSafeGetMultiValuedWithEmptyKey() {
		wconf().safeGet("", true);
	}
		
	@Test
	public void testWaterfallConfigSafeGetMultiValuedWithPropNotDefinedWithDefaultValue() {
		Optional<List<String>> value = wconf().safeGet("prop_not_defined", true);
		assertThat(value).isEmpty();
	}	
	
}
