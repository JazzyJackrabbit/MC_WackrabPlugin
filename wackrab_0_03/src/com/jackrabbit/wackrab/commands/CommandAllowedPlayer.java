package com.jackrabbit.wackrab.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.jackrabbit.wackrab.Main;
import com.jackrabbit.wackrab.Main.AllowedPlayers;
import com.jackrabbit.wackrab.sqlite.DbDataPlayer;
import com.jackrabbit.wackrab.sqlite.DbPlayers;

public class CommandAllowedPlayer implements CommandExecutor {

	Main main;
	public CommandAllowedPlayer(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("allowplayer")) {
            if (args.length >= 2) {

         		try 
         		{
                    String pseudo = args[0];
                    int level = Integer.parseInt(args[1]);
                    String reason = "";
                    
                    if(args.length >= 3)
                    	reason = args[2];
                    
                    if(pseudo.length()>0) {
                    
                    	
                    	// replace, so select & insert
                    	DbDataPlayer d = main.DTB.players.select(main.DTB, main.pathDB_SQLite, pseudo);
                    	if(d == null)
                    		d = main.DTB.players.newDbDataPlayer(pseudo, level, 0, 90, 0, "unknown");
                    	
                    	d.allowedSubWorld = level;
                    	main.DTB.players.insert(main.DTB, main.pathDB_SQLite, d, true);
                    	 
                    	
                    	/* old algo :
	         			// lire fichier pseudos alloués
	         			BufferedReader reader = new BufferedReader(new FileReader(main.absFilepathAllowedPlayers));
	        	        StringBuilder stringBuilder = new StringBuilder();
	        	        
	        	        String ls = System.getProperty("line.separator");
	        	        
	        	        String line = null;
	        	       
	        	        while ((line = reader.readLine()) != null) {
	        	        	stringBuilder.append(line);
	        	        	stringBuilder.append(ls);
	        	        }
	        	        
	        	        // delete the last new line separator
	        	        //stringBuilder.deleteCharAt(stringBuilder.length() - 1);
	
	        	        String content = stringBuilder.toString(); //stringBuilder.toString().replaceAll(ls, "");
	        	        
	        	        reader.close();
	        	        
	        	        //content += ls;
	        	        
	        	        content += ";" + pseudo + "=" + level + ";";
	        	        
	        	        if(reason.length() > 0) {
	        	        	
		        	        content += ";" + "<REASON>" + reason +  "=" + level + ";";
	        	        }
	        	        
	        	        content += ls;
	        	        
	        	    	PrintWriter writer = new PrintWriter(main.absFilepathAllowedPlayers);
	      	    		writer.print(content.toString());
	      	    		writer.close();
	      	    		
                    	 */

	      	    		//System.out.println("++ " + main.absFilepathAllowedPlayers);
	      	    		System.out.println("++ " + pseudo + "=" + level);
	      	    		System.out.println("++ " + reason);
                    }
        	        
         			// remplir la liste
         			
         		}catch (Exception ex){
         			
         		}
                
                
                
            } else {
                sender.sendMessage("Utilisation incorrecte. Utilisez /cloneworld <nomDuMonde> <nouveauNomDuMonde>");
            }
        }
		return false;
		
	}

}
