package com.jackrabbit.wackrab.commands;

import java.util.ArrayList;

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
		
		String messageJ = "";
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
			
		
			messageJ += ("=+= =^= =+= =^= =+=");
			messageJ+="\n";
			messageJ += ("world name:   " + actualWorld);
			messageJ+="\n";
		
			String message = "";
		
			String Newligne=System.getProperty("line.separator");
		
			int identifiersFromSize = identifiersFrom.size();
			int identifiersFromCounter = identifiersFromSize;
			for(String ident : identifiersFrom) {
				String identBlocks = "unknown";
				if(ident == "L") identBlocks = "diamond";
				if(ident == "R") identBlocks = "emeraud";
				if(identifiersFromCounter > 1)
					message += "from [" + identBlocks + "] (sub="+identifiersFromCounter+"), ";
				else
					message += "from default world [" + identBlocks + "] (sub=1).";
				message+="\n";
				identifiersFromCounter--;
			}
			if(!message.isEmpty()) {
				messageJ += (message);
				messageJ+="\n";
			}
			messageJ += ("=+= =^= =+= =^= =+=");
			messageJ+="\n";
		}
		catch(Exception ex) {
			return "Unknown error...";
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