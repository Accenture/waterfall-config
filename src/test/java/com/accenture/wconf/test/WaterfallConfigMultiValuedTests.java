package com.accenture.wconf.test;

import static com.accenture.wconf.WaterfallConfig.wconf;
import static com.accenture.wconf.test.utils.writers.ConfFileUtils.deleteTestResource;
import static com.accenture.wconf.test.utils.writers.ConfFileUtils.writeFileBeforeTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.accenture.wconf.test.utils.categories.ActiveTest;

@Category(ActiveTest.class)
public class WaterfallConfigMultiValuedTests {
	
	private static final UUID uuid = UUID.randomUUID();
	private static final String appConfFilename = String.format("application_%s.conf", uuid.toString());
	private static final Path extConfPath = Paths.get(appConfFilename).toAbsolutePath();
	
	private static final List<String> EXTERNAL_CONF_CONTENTS = Arrays.asList(
			"wconf_active_profile: test",
			"dev {",
			"  multi_valued=[\"a\", \"b\", \"c\", \"d\"]",
			"}",
			"test {",
			"  multi_valued=[\"uno\", \"dos\", \"tres\", \"catorce\"]",
			"}",
			"production {",
			"  multi_valued=[\"1\", \"2\", \"3\", \"14\"]",
			"}",
			"multi_valued=[\"Hello\", \"to\", \"Jason\", \"Isaacs\"]"			
		);
	
	@BeforeClass
	public static void runOnlyOnceOnStart() {
		deleteTestResource(extConfPath);
		writeFileBeforeTest(extConfPath, EXTERNAL_CONF_CONTENTS);		
		System.setProperty("wconf_app_properties", String.format("config/%s", appConfFilename));	
	}
		
	@AfterClass
	public static void runOnlyOnceOnEnd() {
		deleteTestResource(extConfPath);
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
