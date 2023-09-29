package com.jackrabbit.wackrab.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jackrabbit.wackrab.Main;
import com.jackrabbit.wackrab.WorldRelation;

public class CommandClaimWorld implements CommandExecutor {

	Main main;
	public CommandClaimWorld(Main main) 
	{
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		/*
		if (cmd.getName().equalsIgnoreCase("claim ")) {
			if (sender instanceof Player) {	
	            Player joueur = (Player) sender;
				if(args.length >= 0) {
					
					World world = joueur.getWorld(); //args[0];
					
				    WorldRelation worldrelation = main.hierachieWorlds.getWorldRelationByName(world.getName());
				    
				    
				    return worldrelation.claim(main, joueur);
				   
					
				}
			}
		
		}
		
		return true;
		
		*/
		return true;
	}

}
