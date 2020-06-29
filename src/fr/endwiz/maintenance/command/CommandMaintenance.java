package fr.endwiz.maintenance.command;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.endwiz.maintenance.main;
import fr.endwiz.maintenance.utils.YamlAuthorized;
import fr.endwiz.maintenance.utils.YamlConfig;

public class CommandMaintenance implements Listener, CommandExecutor {

	private static main config;
	private final main maintenance;

	@SuppressWarnings("static-access")
	public CommandMaintenance(main main) {
		this.config = main;
		this.maintenance = main;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
		if (lable.equalsIgnoreCase("maintenance")) {
			if (args.length == 0) {
				sender.sendMessage("§f/maintenance on                 §cActiver le mode maintenance");
				sender.sendMessage("§f/maintenance off                §cDésactiver le mode maintenance");
				sender.sendMessage(
						"§f/maintenance <dalay> <time>     §cPermet de mettre un delay avant la maintenance et un temps de maintenance");
				sender.sendMessage("§f/maintenance reload             §cReload la configuration");
				sender.sendMessage(
						"§f/maintenance statut             §cPermet de voir si le serveur est en maintenance ou pas");
				sender.sendMessage(
						"§f/maintenance list               §cPermet de voir la liste des joueurs dans  la whitelist de maintenance");
				sender.sendMessage(
						"§f/maintenance add <pseudo>       §cAjouter un joueur a la joueur a la whitelist de maintenance");
				sender.sendMessage(
						"§f/maintenance remove <pseudo>    §cSupprimer un joueur a la whitelist de maintenance");
			}

			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("on")) {
					if (main.MAINTENANCE_ENABLED == false) {

						try {
							main.MAINTENANCE_ENABLED = true;
							CommandMaintenance.kickAllNotAllowed();
							sender.sendMessage("[§4Maintenance§r] "
									+ config.getConfig().getString("message.maintenance_enable").replace("&", "§"));
							new YamlConfig(maintenance).write();
						} catch (IOException e) {
							sender.sendMessage(
									config.getConfig().getString("message.failed_maintenance_state").replace("&", "§"));
							e.printStackTrace();
						}

						Bukkit.broadcastMessage(
								config.getConfig().getString("message.maintenance_brodcast_start").replace("&", "§"));

					} else if (main.MAINTENANCE_ENABLED == true) {
						sender.sendMessage("[§4Maintenance§r] "
								+ config.getConfig().getString("message.erreur_maintenance_on").replace("&", "§"));
					}

				} else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
					config.reloadConfig();
					sender.sendMessage("[§4Maintenance§r] "
							+ config.getConfig().getString("message.reload_message").replace("&", "§"));

				} else if (args[0].equalsIgnoreCase("off")) {
					if (main.MAINTENANCE_ENABLED == true) {

						try {
							main.MAINTENANCE_ENABLED = false;
							sender.sendMessage("[§4Maintenance§r] "
									+ config.getConfig().getString("message.maintenance_disable").replace("&", "§"));
							Bukkit.broadcastMessage(
									config.getConfig().getString("message.maintenance_brodcast_end").replace("&", "§"));
							new YamlConfig(maintenance).write();
						} catch (IOException e) {
							sender.sendMessage(
									config.getConfig().getString("message.failed_maintenance_state").replace("&", "§"));
							e.printStackTrace();
						}

					} else if (main.MAINTENANCE_ENABLED == false) {
						sender.sendMessage("[§4Maintenance§r] "
								+ config.getConfig().getString("message.erreur_maintenance_off").replace("&", "§"));
					}

				} else if (args[0].equalsIgnoreCase("list")) {
					OfflinePlayer player;
					for (UUID uuid : main.getAUTHORIZED()) {
						player = Bukkit.getOfflinePlayer(uuid);

						if (player != null) {
							sender.sendMessage(ChatColor.DARK_RED + player.getName());
						}
					}

				} else if (args[0].equalsIgnoreCase("statut")) {
					if (main.MAINTENANCE_ENABLED == true) {
						sender.sendMessage("§4Maintenance Statut: §r"
								+ config.getConfig().getString("message.statut.maintenance_on").replace("&", "§"));

					} else if (main.MAINTENANCE_ENABLED == false) {
						sender.sendMessage("§4Maintenance Statut: §r"
								+ config.getConfig().getString("message.statut.maintenance_off").replace("&", "§"));
					}

				} else {
					sender.sendMessage("§f/maintenance on                 §cActiver le mode maintenance");
					sender.sendMessage("§f/maintenance off                §cDésactiver le mode maintenance");
					sender.sendMessage(
							"§f/maintenance <dalay> <time>     §cPermet de mettre un delay avant la maintenance et un temps de maintenance");
					sender.sendMessage("§f/maintenance reload             §cReload la configuration");
					sender.sendMessage(
							"§f/maintenance statut             §cPermet de voir si le serveur est en maintenance ou pas");
					sender.sendMessage(
							"§f/maintenance list               §cPermet de voir la liste des joueurs dans  la whitelist de maintenance");
					sender.sendMessage(
							"§f/maintenance add <pseudo>       §cAjouter un joueur a la joueur a la whitelist de maintenance");
					sender.sendMessage(
							"§f/maintenance remove <pseudo>    §cSupprimer un joueur a la whitelist de maintenance");
				}
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("add")) {

					final String PlayerName = args[1];
					final UUID uuid = Bukkit.getPlayer(PlayerName).getUniqueId();

					try {
						main.getAUTHORIZED().add(uuid);
						new YamlAuthorized(maintenance).writeAuthorized();
						sender.sendMessage(
								"[§4Maintenance§r] " + config.getConfig().getString("message.player_add_message")
										.replace("&", "§").replace("{player}", PlayerName));

					} catch (IOException e) {
						sender.sendMessage(
								"[§4Maintenance§r] " + config.getConfig().getString("message.failed_add_list")
										.replace("&", "§").replace("{player}", PlayerName));
						e.printStackTrace();
					}

				} else if (args[0].equalsIgnoreCase("remove")) {

					final String PlayerName = args[1];
					final UUID uuid = Bukkit.getPlayer(PlayerName).getUniqueId();

					try {
						main.getAUTHORIZED().remove(uuid);
						new YamlAuthorized(maintenance).writeAuthorized();
						sender.sendMessage(
								"[§4Maintenance§r] " + config.getConfig().getString("message.player_remove_message")
										.replace("&", "§").replace("{player}", PlayerName));

					} catch (IOException e) {
						sender.sendMessage(
								"[§4Maintenance§r] " + config.getConfig().getString("message.failed_remove_list")
										.replace("&", "§").replace("{player}", PlayerName));
						e.printStackTrace();
					}

				} else if (args[0].equalsIgnoreCase("delay")) {
					final int delay = Integer.parseInt(args[1]);
					final CharSequence delay2 = args[1];
					main.DELAY_BEFOR_MAINTENANCING = delay;
					if (main.MAINTENANCE_ENABLED == false) {
						Bukkit.broadcastMessage(config.getConfig().getString("message.delayed_message_maintenance")
								.replace("&", "§").replace("{delay}", delay2));

						Bukkit.getScheduler().scheduleAsyncDelayedTask(maintenance, () -> {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "maintenance on");

						}, delay * 20);
					} else {
						sender.sendMessage("[§4Maintenance§r] "
								+ config.getConfig().getString("message.erreur_maintenance_on").replace("&", "§"));
					}

				} else if (args[0].equalsIgnoreCase("time")) {
					final int time = Integer.parseInt(args[1]);
					main.MAINTENANCE_DURATION = time;

					if (main.MAINTENANCE_ENABLED == false) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "maintenance on");
						Bukkit.getScheduler().scheduleAsyncRepeatingTask(maintenance, () -> {
							main.MAINTENANCE_DURATION --;
							if(main.MAINTENANCE_DURATION == 0) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "maintenance off");

							}
						}, 20, 20);

					} else {
						sender.sendMessage("[§4Maintenance§r] "
								+ config.getConfig().getString("message.erreur_maintenance_on").replace("&", "§"));
					}
				}
			}
			if (args.length == 4) {
				if (args[0].equalsIgnoreCase("delay")) {
					final int delay1 = Integer.parseInt(args[1]);
					final CharSequence delay2 = args[1];
					main.DELAY_BEFOR_MAINTENANCING = delay1;

					Bukkit.broadcastMessage(config.getConfig().getString("message.delayed_message_maintenance")
							.replace("&", "§").replace("{delay}", delay2));

					if (args[2].equalsIgnoreCase("time")) {
						final int time = Integer.parseInt(args[3]);

						if (main.MAINTENANCE_ENABLED == false) {
							Bukkit.getScheduler().scheduleAsyncDelayedTask(maintenance, () -> {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "maintenance on");

								Bukkit.getScheduler().scheduleAsyncDelayedTask(maintenance, () -> {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "maintenance off");

								}, time * 20);
							}, delay1 * 20);
						} else {
							sender.sendMessage("[§4Maintenance§r] "
									+ config.getConfig().getString("message.erreur_maintenance_on").replace("&", "§"));
						}
					}
				}
			}
		}
		return true;
	}

	private static void kickAllNotAllowed() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.isOp() || main.getAUTHORIZED().contains(player.getUniqueId())) {
				continue;
			} else {
				player.kickPlayer(config.getConfig().getString("message.maintenance_kick_message").replace("&", "§"));
			}
		}
	}
}
