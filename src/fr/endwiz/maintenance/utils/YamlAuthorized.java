package fr.endwiz.maintenance.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.endwiz.maintenance.main;

public class YamlAuthorized {
	
	private static final String AUTHORIZED_NAME = "maintenance.yml";
	private static final String KEY = "authorized";
	private final File savedFile;
	private final YamlConfiguration configuration;
	
	public YamlAuthorized(main Maintenance) {
		this.savedFile = new File(Maintenance.getDataFolder(), AUTHORIZED_NAME);
		this.configuration = YamlConfiguration.loadConfiguration(savedFile);
	}

	public void writeAuthorized() throws IOException {
		final List<String> uuids = new ArrayList<>();
		
		for(UUID uuid : main.getAUTHORIZED()) {
			uuids.add(uuid.toString());
		}

		configuration.set(KEY, uuids);
		configuration.save(savedFile);
	}
	
	@SuppressWarnings("unchecked")
	public HashSet<UUID> readAuthorized() {		
		if(configuration != null) {
			final List<String> uuids = (List<String>)configuration.getList(KEY);
			
			if(uuids != null) {
				final HashSet<UUID> hUUIDs = new HashSet<>();
				
				for(String uuid : uuids) {
					hUUIDs.add(UUID.fromString(uuid));
				}
				
				return hUUIDs;
			}
		}
		return new HashSet<>();
	}
}
