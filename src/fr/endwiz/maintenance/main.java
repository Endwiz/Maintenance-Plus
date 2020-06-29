package fr.endwiz.maintenance;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import fr.endwiz.maintenance.command.CommandMaintenance;
import fr.endwiz.maintenance.listeners.PlayerLoginListener;
import fr.endwiz.maintenance.utils.YamlAuthorized;
import fr.endwiz.maintenance.utils.YamlConfig;

public class main extends JavaPlugin {

    private static Set<UUID> AUTHORIZED = new HashSet<>();

    public static boolean MAINTENANCE_ENABLED = false;
    public static int DELAY_BEFOR_MAINTENANCING = 0;
    public static int MAINTENANCE_DURATION = 0;

    public void onEnable() {
        readAuthorized();
        saveDefaultConfig();
        getCommand("maintenance").setExecutor(new CommandMaintenance(this));
        getServer().getPluginManager().registerEvents((Listener) new CommandMaintenance(this), this);
        getServer().getPluginManager().registerEvents((Listener) new PlayerLoginListener(this), this);
    }

    public void onDisable() {

    }

    public static Set<UUID> getAUTHORIZED() {

        return AUTHORIZED;
    }

    private void readAuthorized() {
        main.AUTHORIZED = new YamlAuthorized(this).readAuthorized();
    }

    @SuppressWarnings("unused")
    private void readState() {
        new YamlConfig(this).read();
    }
}
