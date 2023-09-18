package com.jackrabbit.wackrab.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jackrabbit.wackrab.Main;
import com.jackrabbit.wackrab.WorldRelation;

// La classe interne qui gère l'exécution de la commande
public class MaCommandeExecutor implements org.bukkit.command.CommandExecutor {

	Main main;
	public MaCommandeExecutor(Main main) {
		this.main = main;
	}  
	
	
    // méthode commande multiworld:
    public void ExecuteMultiWorldCommand(String worldname, String commande) {
    	try {
    	if(worldname.equals("*")) {
    		for(WorldRelation wr : main.hierachieWorlds.worlds) {
    			try {
    				World w = wr.getWorld();
    				if ( w != null) {
    		            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute in " +  w.getName() + " run " + commande);
    		        } else {
    		        	 main.getLogger().warning("Le monde " + worldname + " n'existe pas !");
    		        }
    			}catch(Exception ex2){
    				 main.getLogger().warning("Erreur execution commande dans " + worldname + " !");
    	    	}
    		}
    	}
    	else {
    		World w =  main.hierachieWorlds.getWorldByName(worldname);
    		if ( w != null) {
	            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute in " +  w.getName() + " run " + commande);
	        } else {
	        	 main.getLogger().warning("Le monde " + worldname + " n'existe pas !");
	        }
    	}
    	}catch(Exception ex){
    		 main.getLogger().warning("Erreur execution commande dans " + worldname + " !");
    	}
    	
    }
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Vérifiez si la commande a été exécutée par un joueur
        if (sender instanceof Player) {
            Player joueur = (Player) sender;

            // Votre code ici pour gérer la commande pour le joueur
            joueur.sendMessage("OK");
            // Ajoutez ici votre logique spécifique pour la commande du joueur

            return true;
        } else {
            // Si la commande est exécutée par autre chose qu'un joueur (console, autre plugin, etc.)
            sender.sendMessage("Cette commande ne peut être exécutée que par un joueur.");
            return false;
        }
    }
}