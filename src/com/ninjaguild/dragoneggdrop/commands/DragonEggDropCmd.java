/*
    DragonEggDrop
    Copyright (C) 2016  NinjaStix
    ninjastix84@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.ninjaguild.dragoneggdrop.commands;

import java.util.ArrayList;
import java.util.List;

import com.ninjaguild.dragoneggdrop.DragonEggDrop;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class DragonEggDropCmd implements CommandExecutor, TabCompleter {
	
	private final DragonEggDrop plugin;
	
	public DragonEggDropCmd(final DragonEggDrop plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "-----------------------");
			sender.sendMessage(ChatColor.GOLD + "-- DRAGONEGGDROP INFO --");
			sender.sendMessage(ChatColor.GOLD + "-----------------------");
			sender.sendMessage(ChatColor.GOLD + "Original Author: PixelStix");
			sender.sendMessage(ChatColor.GOLD + "Maintainer: 2008Choco");
			sender.sendMessage(ChatColor.GOLD + "Version: " + plugin.getDescription().getVersion());
			if (sender.isOp() && plugin.isNewVersionAvailable())
				sender.sendMessage(ChatColor.AQUA + "NEW VERSION AVAILABLE!: " + plugin.getNewVersion());
			sender.sendMessage(ChatColor.YELLOW + "/dragoneggdrop help");
			sender.sendMessage(ChatColor.GOLD + "-----------------------");
			
			return true;
		}
		
		// "help" and "reload" params
		if (args[0].equalsIgnoreCase("help")) {
			if (!sender.hasPermission("dragoneggdrop.help")) {
				this.plugin.sendMessage(sender, ChatColor.RED + "Permission denied!");
				return true;
			}
			
			sender.sendMessage(ChatColor.GOLD + "-----------------------");
			sender.sendMessage(ChatColor.GOLD + "-- DRAGONEGGDROP HELP --");
			sender.sendMessage(ChatColor.GOLD + "-----------------------");
			sender.sendMessage(ChatColor.GOLD + "/dragoneggdrop reload");
			sender.sendMessage(ChatColor.GOLD + "/dragontemplate list");
			sender.sendMessage(ChatColor.GOLD + "/dragontemplate <template> (view/info)");
			sender.sendMessage(ChatColor.GOLD + "/dragontemplate <template> edit addloot [weight]");
			sender.sendMessage(ChatColor.GOLD + "-----------------------");
		}
		
		else if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("dragoneggdrop.reload")) {
				this.plugin.sendMessage(sender, ChatColor.RED + "Permission denied!");
				return true;
			}
			
			this.plugin.reloadConfig();
			this.plugin.getDEDManager().reloadDragonTemplates();
			
			this.plugin.sendMessage(sender, ChatColor.GREEN + "Reload complete!");
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> options = new ArrayList<>();
		
		if (args.length == 1) {
			options.add("help");
			options.add("reload");
		}
		
		return options;
	}
}