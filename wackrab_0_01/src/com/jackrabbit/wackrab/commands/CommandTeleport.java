package com.jackrabbit.wackrab.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleport implements CommandExecutor {


	@Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
       if (label.equalsIgnoreCase("wacktp")) {
           if (args.length == 2) {
               Player player = Bukkit.getPlayer(args[0]);
               if (player != null) {
                   String monde = args[1];
                   
                   World world;
                   if (monde.equalsIgnoreCase("default")) {
                       world = Bukkit.getWorlds().get(0); // Obtient le monde par défaut
                   } else {
                       world = Bukkit.getWorld(monde);
                       
                       if(world == null) {
                    	   world = Bukkit.createWorld(WorldCreator.name(monde));
                       }
                   }
                   
                   if (world != null) {
                       player.teleport(world.getSpawnLocation());
                       sender.sendMessage("Téléportation réussie !");
                   } else {
                       sender.sendMessage("Le monde spécifié n'existe pas !");
                   }
               } else {
                   sender.sendMessage("Le joueur spécifié n'est pas en ligne !");
               }
           } else {
               sender.sendMessage("Utilisation incorrecte. Utilisation : /wacktp <pseudo> <monde>");
           }
           return true;
       }
       return false;
   }
	 
	 
	 

}
