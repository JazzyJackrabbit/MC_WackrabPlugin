package com.jackrabbit.wackrab.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jackrabbit.wackrab.Main;
import com.jackrabbit.wackrab.WorldRelation;

public class CommandeMonde implements CommandExecutor {

	Main main;
	public CommandeMonde(Main main) {
		this.main = main;
	}
	
	public static String WorldInfo(Main main, Player joueur) {
		
		String messageJ =  ChatColor.GOLD + "";
		try {
		    String actualWorld = joueur.getWorld().getName();
		    
		    ArrayList<WorldRelation> worlds = main.hierachieWorlds.worlds;
		
		    ArrayList<String> identifiersFrom = new ArrayList<String>();
		    ArrayList<String> identifiersSub = new ArrayList<String>();
		    
		    WorldRelation worldRel = main.hierachieWorlds.getWorldRelationByName(actualWorld);
		
		    boolean canContinueFrom = false;
		    boolean canContinueSub = false;
		    
			while(worldRel != null && worldRel.worldSubPosition > 0) {
		
				String worldIdentifier = worldRel.worldIdentifier;
				
				identifiersFrom.add(worldIdentifier);
				worldRel = main.hierachieWorlds.getWorldRelationByName(worldRel.fromWorldName);
		
				if (worldRel != null)
					canContinueFrom = true;
		    }
			
		
			String message = ChatColor.WHITE + "";
		
			String Newligne=System.getProperty("line.separator");
		
			int identifiersFromSize = identifiersFrom.size();
			int identifiersFromCounter = identifiersFromSize;
			
			String firstident = "";
			for(String ident : identifiersFrom) {
				if(firstident.isEmpty()) firstident = ident;
				String identBlocks = "unknown";
				if(ident == "L") identBlocks = ChatColor.BLUE + "diamond" + ChatColor.WHITE;
				if(ident == "R") identBlocks = ChatColor.GREEN + "emeraud" + ChatColor.WHITE;
				if(identifiersFromCounter > 1)
					message += "from [" + identBlocks + "] (sub="+identifiersFromCounter+"), ";
				else
					message += "from default world [" + identBlocks + "] (sub=1).";
				message+="\n";
				identifiersFromCounter--;
			}

			messageJ += ("=+= =^= =+= =^= =+=") +  ChatColor.WHITE;
			messageJ+="\n";
			
			if(firstident.isEmpty())
				messageJ += ("world name:   " + ChatColor.GOLD + actualWorld +  ChatColor.WHITE);
			else if(firstident.equals("L"))
				messageJ += ("world name:   " + ChatColor.BLUE + actualWorld +  ChatColor.WHITE);
			else if(firstident.equals("R"))
				messageJ += ("world name:   " + ChatColor.GREEN + actualWorld +  ChatColor.WHITE);
			
			messageJ+="\n";
		
			if(!message.isEmpty() && !message.equals(ChatColor.WHITE.toString())) {
				messageJ += (message);
				messageJ+="\n";
			}
			messageJ += ChatColor.GOLD + ("=+= =^= =+= =^= =+=");
			messageJ+="\n";
		}
		catch(Exception ex) {
			return  ChatColor.RED + "Unknown error...";
		}
		
		return messageJ;
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player joueur = (Player) sender;
        	joueur.sendMessage(WorldInfo(main, joueur));
        } else {
            sender.sendMessage("This command can be use only by players entities.");
        }
        return true;
    }
}