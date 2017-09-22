package com.github.sergiofgonzalez.wconf;

/**
 * Enum containing the configuration keys used for the internal configuration of wconf.
 * 
 * Note that keys likely to be overridden with environment variable do not include "."
 * as some OS do not allow that. 
 * 
 * @author sergio.f.gonzalez
 *
 */
public enum MetaConfigKeys {
	META_CONFIG_APP_RESOURCE_KEY("wconf_app_properties"),
	META_CONFIG_EXT_APP_RESOURCE_ADDITIONAL_PATHS("wconf_ext_app_properties_paths"),
	META_CONFIG_ACTIVE_PROFILE_KEY("wconf_active_profile"),
	META_CONFIG_ENCRYPTION_SWITCH_KEY("wconf_encryption.enabled"),
	META_CONFIG_ENCRYPTION_ALGORITHM_KEY("wconf_encryption.algorithm"),
	META_CONFIG_ENCRYPTION_KEY_TYPE_KEY("wconf_encryption.key_type"),
	META_CONFIG_ENCRYPTION_KEY_STORE_PATH_KEY("wconf_encryption.key_store.path"),
	META_CONFIG_ENCRYPTION_KEY_STORE_PASSWORD_KEY("wconf_encryption.key_store.password"),
	META_CONFIG_ENCRYPTION_KEY_STORE_KEY_ALIAS_KEY("wconf_encryption.key_store.key.alias"),
	META_CONFIG_ENCRYPTION_KEY_STORE_KEY_PASSWORD_KEY("wconf_encryption.key_store.key.password"),
	META_CONFIG_ENCRYPTION_IV_KEY("wconf_encryption.iv");	
	;
	
	
	private final String key;
	
	private MetaConfigKeys(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return key;
	}
}
