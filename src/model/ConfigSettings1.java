package model;

import java.io.File;

import javares.files.FileUtils;
import javares.files.config.ConfigSettings;
import javares.files.config.Property;

/**
 * @since 18.11.2013
 * @author Julian Schelker
 */
public class ConfigSettings1 extends ConfigSettings {

	public static Property lastMinimapFolder = new Property("lastMinimapFolder", false,
		null);

	@Override
	public Object getConfigSettingsObject() {
		return this;
	}

	@Override
	public File getFile() {
		return new File("config/mainConfig.txt");
	}

	@Override
	public boolean isRuntimeNewPropertiesAllowed() {
		return true;
	}

	@Override
	public void save() {
		FileUtils.checkOrCreateDir("config");
	}

}
