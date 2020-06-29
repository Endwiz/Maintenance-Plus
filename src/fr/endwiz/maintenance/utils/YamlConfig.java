package fr.endwiz.maintenance.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.endwiz.maintenance.main;

public class YamlConfig {

	private static final String AUTHORIZED_NAME = "maintenance.yml";
	private final File savedFile;
	private final YamlConfiguration configuration;

	public YamlConfig(main Maintenance) {
		this.savedFile = new File(Maintenance.getDataFolder(), AUTHORIZED_NAME);
		this.configuration = YamlConfiguration.loadConfiguration(savedFile);
	}

	public void write() throws IOException {
		configuration.set("maintenance_statut", main.MAINTENANCE_ENABLED);
		configuration.save(savedFile);

	}

	public void read() {
		main.MAINTENANCE_ENABLED = configuration.getBoolean("maintenance_statut");
	}
}
