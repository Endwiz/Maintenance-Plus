package fr.endwiz.maintenance.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.endwiz.maintenance.main;

public class PlayerLoginListener implements Listener {

	private main config;

	public PlayerLoginListener(main main) {
		this.config = main;
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void OnJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		if (main.MAINTENANCE_ENABLED == true) {
			if (player.isOp() || main.getAUTHORIZED().contains(player.getUniqueId())) {
				return;
			} else {
				player.kickPlayer(config.getConfig().getString("message.maintenance_connect_message").replace("&", "§"));
			}
		}
	}
}
