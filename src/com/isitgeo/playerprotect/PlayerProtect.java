package com.isitgeo.playerprotect;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerProtect extends JavaPlugin implements Listener {
	
	ArrayList<String> protectedPlayers = new ArrayList<String>();
	String pluginTitle = ChatColor.GREEN + "[PlayerProtect] " + ChatColor.WHITE;
	int maxListPlayers = 10;
	
	@Override
	public void onEnable() {
		getLogger().info("PlayerProtect Enabled");
		getCommand("playerprotect").setExecutor(this);
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	private void load() {
		// TODO fetch saved data
	}
	
	@EventHandler
	private void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			
			Player player = (Player) event.getEntity();
			Player damagerPlayer = (Player) event.getDamager();
			
			if (protectedPlayers.contains(player.getName())) {
				damagerPlayer.kickPlayer(player.getName() + " is protected by PlayerProtect.");
			}			
		}
	}
	
	public boolean onCommand(CommandSender player, Command command, String cmdLabel, String[] args) {
		if(command.getName().equalsIgnoreCase("playerprotect")) {
			
			if (args.length < 1) {
				player.sendMessage(pluginTitle + "INFO");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("list")) {
				String tmpProtectedPlayers = "";
				
				if (protectedPlayers.size() < 1) {
					player.sendMessage(pluginTitle + "List empty!");
					return true;
				}
				
				int tmpPlayerNum = 0;
				
				if (protectedPlayers.size() > maxListPlayers) {
					tmpPlayerNum = maxListPlayers;
				} else {
					tmpPlayerNum = protectedPlayers.size();
				}
				
				boolean firstLoop = true;
				
				for (int i = 0; i < tmpPlayerNum; i++) {
					if (!firstLoop) {
						tmpProtectedPlayers += ", ";
					}
					
					firstLoop = false;
					tmpProtectedPlayers += ChatColor.GRAY + protectedPlayers.get(i) + ChatColor.WHITE;
				}
				
				if (protectedPlayers.size() > maxListPlayers) {					
					tmpProtectedPlayers += " and " + (protectedPlayers.size() - maxListPlayers) + " others.";
				} else {
					tmpProtectedPlayers += ".";
				}
				
				player.sendMessage(pluginTitle + tmpProtectedPlayers);
				
				return true;
			}
			
			
			if (args[0].equalsIgnoreCase("remove")) {
				if (args[1] != null) {
					if (getServer().getPlayer(args[1]) != null) {
						protectedPlayers.remove(args[1]);
						player.sendMessage(pluginTitle + args[1] + " removed.");
					} else {
						player.sendMessage(pluginTitle + "Cannot find that player!");
					}
				} else {
					player.sendMessage(pluginTitle + "You must specify a player!");
				}
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reset")) {
				protectedPlayers.clear();
				player.sendMessage(pluginTitle + "List reset.");
				return true;
			}
			
			if (args[0] != null) {
				if (getServer().getPlayer(args[0]) != null) {
					if (!protectedPlayers.contains(args[0])) {
						protectedPlayers.add(args[0]);
						player.sendMessage(pluginTitle + args[0] + " added.");
					} else {
						player.sendMessage(pluginTitle + args[0] + " already exists on list.");
					}
					
				} else {
					player.sendMessage(pluginTitle + "Cannot find that player!");
				}
			} else {
				player.sendMessage(pluginTitle + "You must specify a player!");
			}
			
			return true;
		}
		return false;
	}
	
	
	@Override
	public void onDisable() {
		getLogger().info("PlayerProtect Disabled");
	}
}
