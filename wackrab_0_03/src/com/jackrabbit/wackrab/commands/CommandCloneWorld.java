package com.jackrabbit.wackrab.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCloneWorld implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("cloneworld")) {
            if (args.length == 2) {
                String worldName = args[0];
                String cloneName = args[1];

                World worldToClone = Bukkit.getWorld(worldName);

                if (worldToClone != null) {
                    WorldCreator creator = new WorldCreator(cloneName);
                    creator.copy(worldToClone);
                    World clonedWorld = creator.createWorld();
                    
                    sender.sendMessage("Le monde " + worldName + " a été cloné sous le nom " + cloneName);
            		return true;
                } else {
                    sender.sendMessage("Le monde " + worldName + " n'existe pas.");
                }
            } else {
                sender.sendMessage("Utilisation incorrecte. Utilisez /cloneworld <nomDuMonde> <nouveauNomDuMonde>");
            }
        }
		return false;
		
		
	}

	
	/*
	 * public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("cloneworld")) {
            if (args.length == 2) {
                String worldName = args[0];
                String cloneName = args[1];

                World worldToClone = Bukkit.getWorld(worldName);

                if (worldToClone != null) {
                    WorldCreator creator = new WorldCreator(cloneName);
                    creator.copy(worldToClone);
                    World clonedWorld = creator.createWorld();
                    sender.sendMessage("Le monde " + worldName + " a été cloné sous le nom " + cloneName);
                } else {
                    sender.sendMessage("Le monde " + worldName + " n'existe pas.");
                }
            } else {
                sender.sendMessage("Utilisation incorrecte. Utilisez /cloneworld <nomDuMonde> <nouveauNomDuMonde>");
            }
        }
    }
	 */
}
