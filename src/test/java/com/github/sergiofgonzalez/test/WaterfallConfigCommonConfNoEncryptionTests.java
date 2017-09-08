package com.github.sergiofgonzalez.test;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.wconf;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.sergiofgonzalez.test.utils.categories.ActiveTest;

/**
 * Test Cases when:
 *   + only using `config/common.conf` for properties on file (i.e. no application conf, no profiles...)
 *   + not using encryption
 * 
 * Things to check:
 * [X] Read property defined in config/common
 * [X] Property defined in config/common is overridden by env var
 * [X] Property defined in config/common is overridden by Java system property
 * [X] Property not defined in config/common but defined as env var
 * [X] Property not defined in config/common but defined as Java System property
 * [X] Property not defined in config/common but defined as Java System property and Environment Var
 * [X] Encrypted property will throw an exception 
 * 
 * @author sergio.f.gonzalez
 *
 */

@Category(ActiveTest.class)
public class WaterfallConfigCommonConfNoEncryptionTests {
	
	@Test
	public void testReadPropOnlyDefinedInCommon() {
		String value = wconf().get("only_in_common");
		assertThat(value).isEqualTo("This value has been set on common.conf");		
	}
	
	@Test
	public void testPropCanBeOverriddenByEnvironmentVar() {
		String value = wconf().get("in_common_but_overridden_by_env");
		assertThat(value).isEqualTo("This value has been set in an environment variable");				
	}
	
	@Test
	public void testPropCanBeOverriddenByJavaSystemProperty() {
		String value = wconf().get("in_common_but_overridden_by_java_property");
		assertThat(value).isEqualTo("This value has been set in a Java system property");	
	}
	
	@Test
	public void testPropDefinedAsEnvVarTakesPrecedence() {
		String value = wconf().get("in_common_but_overridden_by_env_and_property");
		assertThat(value).isEqualTo("This value has been set in an environment variable");	
	}
	
	@Test(expected = IllegalStateException.class)
	public void testEncryptedPropWithEncryptionDisableShouldFail() {
		wconf().get("encrypted_value_in_common");
	}
}
