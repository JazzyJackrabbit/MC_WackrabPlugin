package com.jackrabbit.wackrab.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jackrabbit.wackrab.Main;
import com.jackrabbit.wackrab.WorldRelation;
import com.jackrabbit.wackrab.sqlite.DbDataWorld;
import com.jackrabbit.wackrab.ui.UIHierarchieWorlds;

public class CommandeMonde implements CommandExecutor {

	Main main;
	public CommandeMonde(Main main) {
		this.main = main;
	}
	
	public static boolean canUseWorldCommand(Main main, Player p, WorldRelation wr) {
		return (p.getName().equals(wr.getPlayerOwner(main, false)));
	}

	public static void WorldAccess(
			Main main, CommandSender sender, Command command, 
			String label, String[] args, String idt, String arg) {
	
			// GET   ON   OFF   USERS,filter    ADD,player    RMV,player
	
		// 
		if (sender instanceof Player) {	
            Player joueur = (Player) sender;
				
			World world = joueur.getWorld();
			
		    WorldRelation worldrelation = main.hierachieWorlds.getWorldRelationByName(world.getName());
		    				
			if(idt.toUpperCase().equals("GET")) {
				sender.sendMessage(worldrelation.worldName +": " + worldrelation.isAllowedAccessDBRequest(main, true));
			}
			
			if(idt.toUpperCase().equals("ON")) {
				if(canUseWorldCommand(main, joueur, worldrelation)){
					worldrelation.setAllowedAccess(main, 1);
					sender.sendMessage(worldrelation.worldName +": " + worldrelation.isAllowedAccessDBRequest(main, true));
				}
			}
			
			if(idt.toUpperCase().equals("OFF")) {
				if(canUseWorldCommand(main, joueur, worldrelation)){

				worldrelation.setAllowedAccess(main, 0);
				sender.sendMessage(worldrelation.worldName +": " + worldrelation.isAllowedAccessDBRequest(main, true));
			}}
			
			if(idt.toUpperCase().equals("USERS")) {
				
				String filter = null;
				if(arg != null)
					filter = arg.toUpperCase();
				String listuserallowed = worldrelation.get_allowedAllPlayersEnterOn(main, false);
				String msg = "Users allowed on this world: \n";
				String[] list = listuserallowed.split(";");
				for(String userallwd : list) {
					if(userallwd != null && !userallwd.isEmpty()) {
						if(filter == null || filter.isEmpty() || userallwd.toUpperCase().contains(filter))
							msg += userallwd + " ; ";
					}
				}
				sender.sendMessage(msg);
			}
			
			if(idt.toUpperCase().equals("ADD")) {
				if(canUseWorldCommand(main, joueur, worldrelation)){

				String player = arg.toUpperCase();
				String listuserallowed = worldrelation.get_allowedAllPlayersEnterOn(main, false);
				String[] list = listuserallowed.split(";");
				String msg = "";
				for(String userallwd : list) {
					if(userallwd != null && !userallwd.isEmpty()) {
						msg += userallwd + ";";
					}
				}
				String regexpseudo = "^[a-zA-Z0-9_]{2,16}$";
				Pattern pattern = Pattern.compile(regexpseudo);
				Matcher matcher1 = pattern.matcher(player);
				
				if(matcher1.matches()) {;
					msg += player + ";";
					
					worldrelation.set_allowedPlayersEnterOn(main, msg);
					sender.sendMessage("done.");
				}
				else {
					sender.sendMessage("ko..");
				}}
			}
			
			if(idt.equals("RMV")) {
				if(canUseWorldCommand(main, joueur, worldrelation)){

				String player = arg.toUpperCase();
				String listuserallowed = worldrelation.get_allowedAllPlayersEnterOn(main, false);
				String[] list = listuserallowed.split(";");
				String msg = "";
					
				if(player != null && !player.isEmpty()) {
					for(String userallwd : list) {
						if(userallwd != null && !userallwd.isEmpty()) {

							if(!userallwd.toUpperCase().equals(player.toUpperCase()))
								msg += userallwd + ";";
						}
					}

					worldrelation.set_allowedPlayersEnterOn(main, msg);
					sender.sendMessage("done.");
				}}
			}
		}

	}
	
	public static void WorldPVP(Main main, CommandSender sender, Command command, String label, String[] args, String idt, String arg) {
		
		if (sender instanceof Player) 
		{	
            Player joueur = (Player) sender;
			World world = joueur.getWorld();
			WorldRelation worldrelation = main.hierachieWorlds.getWorldRelationByName(world.getName());
	  		
			if(idt.toUpperCase().equals("GET")) {
				
				sender.sendMessage(worldrelation.worldName +": " + worldrelation.isPVPEnableDBRequest(main, true));
			}
			if(idt.toUpperCase().equals("ON")) {
				
				if(canUseWorldCommand(main, joueur, worldrelation)){

				
				worldrelation.setPVPEnable(main, 1);
				sender.sendMessage(worldrelation.worldName +": " + worldrelation.isPVPEnableDBRequest(main, true));
			}
			}
			if(idt.toUpperCase().equals("OFF")) {
				if(canUseWorldCommand(main, joueur, worldrelation)){

				worldrelation.setPVPEnable(main, 0);
				sender.sendMessage(worldrelation.worldName +": " + worldrelation.isPVPEnableDBRequest(main, true));
			}}
		}
		
	}
		
	public static boolean WorldWhoClaim(Main main, CommandSender sender, Command command, String label, String[] args, String cmd, String arg) {
		
		if(cmd.equals("WHO")) {
			if (sender instanceof Player) {	
	            Player joueur = (Player) sender;
				if(args.length >= 1) {
					
					World world = joueur.getWorld(); //args[0];
					
				    WorldRelation worldrelation = main.hierachieWorlds.getWorldRelationByName(world.getName());
				    
				    String owner = worldrelation.getPlayerOwner(main, false);
				    
				    if(owner == null || owner.isEmpty()) 
				    {
					    sender.sendMessage("No owner.");
				    }
	
				    sender.sendMessage("The owner is: " + owner);
				    
				    return true;
				   
				}
			}
				
			return true;
		}
		else if(cmd.equals("GIVE")) {
			if (sender instanceof Player) {	
				
	            Player joueur = (Player) sender;
	            
	            World world = joueur.getWorld(); 
	            WorldRelation worldrelation = main.hierachieWorlds.getWorldRelationByName(world.getName());
			    
	            if(canUseWorldCommand(main, joueur, worldrelation)){

		    			
					if(args.length >= 1) {
						
						String pseudo = "";
						if(arg != null && !arg.isBlank() && !arg.isEmpty()) {
							pseudo = arg;
							
							//Player to = Bukkit.getPlayer(pseudo);
						
						    String owner = pseudo; //worldrelation.getPlayerOwner();
						    
						    worldrelation.setPlayerOwner(main, owner);
						    
						    sender.sendMessage("The owner is now: " + pseudo);
						    
						    return true;
						}
					   
					}
				}
			}
		}
		return true;
	}
	public static boolean WorldClaim(Main main, CommandSender sender, Command command, String label, String[] args) {
		
		//if (command.getName().equalsIgnoreCase("world")) {
			if (sender instanceof Player) {	
	            Player joueur = (Player) sender;
				//if(args.length >= 1) {
				
				World world = joueur.getWorld(); //args[0];
				
			    WorldRelation worldrelation = main.hierachieWorlds.getWorldRelationByName(world.getName());
			    
			    return worldrelation.claim(main, joueur, true);
				   
				//}
			}
		
		//}
		
		return true;
	}

	public static <T> ArrayList<T> retirerDoublons(ArrayList<T> listeAvecDoublons) {
        HashSet<T> set = new HashSet<>(listeAvecDoublons);

        ArrayList<T> listeSansDoublons = new ArrayList<>(set);

        return listeSansDoublons;
    }
	public static boolean WorldLocateClaim(Main main, CommandSender sender, Command command, String label, String[] args, int nclaim) {
		
		List<DbDataWorld> wrs = main.hierachieWorlds.getRandomClaimableWorldRelation(main, nclaim);
		sender.sendMessage("Some Random Claimable Worlds: \n");
		for(int i = 0; i < wrs.size(); i++) {
			DbDataWorld w = wrs.get(i);
			if(w != null)
			sender.sendMessage("> "+ w.worldName +" \n");
		}
		
		return true;
	}
	
	
	public static String WorldInfo(Main main, Player joueur) {
		
		try {
			UIHierarchieWorlds inv = main.uiHierarchiesWorlds.creerInventaire(main,joueur, "~ World Hierarchie ~");
			inv.openInventory(false);
		}
		catch (Exception ex){
			System.out.println("exception: "+ex);
		}
		
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
    	if (command.getName().equalsIgnoreCase("world")) 
    	if (sender instanceof Player) {
        	
            Player joueur = (Player) sender;

            if(args.length == 0) {
            	WorldInfo(main, joueur);
            }

            if(args.length >= 1 && args[0].toUpperCase().equals("INFO")) {
            	WorldInfo(main, joueur);
            }

            else if(args.length >= 1 && args[0].toUpperCase().equals("CLAIM")) {
            	WorldClaim(main, sender, command, label, args);
            }

            else if(args.length >= 1 && args[0].toUpperCase().equals("WHOCLAIM")) {
            	WorldWhoClaim(main, sender, command, label, args, "WHO", "");
            }

            else if(args.length >= 1 && args[0].toUpperCase().equals("GIVECLAIM")) {
            	if(args.length >= 2) {
            		String arg2 = args[1];
            		WorldWhoClaim(main, sender, command, label, args, "GIVE", arg2);
            	}
            }

            else if(args.length >= 1 && args[0].toUpperCase().equals("LOCATE")) {
            	int nClaimablesWorlds = 4;
            	if(args.length >= 2)
            		nClaimablesWorlds = Integer.parseInt(args[1]);
            	WorldLocateClaim(main, sender, command, label, args, nClaimablesWorlds);
            	//sender.sendMessage(label);
            }
            
            else if(args.length >= 1 && args[0].toUpperCase().equals("ACCESS")) {
            	
            	if(args.length >= 2) {
            		String arg2 = args[1];
            		
            		
            		// voir l'accés au monde (publique/privé)
            		if(arg2.toUpperCase().equals("GET"))
            			WorldAccess(main, sender, command, label, args, "GET", "");
            		
            		// accés au monde publique
            		if(arg2.toUpperCase().equals("ON")) 
            		{
            			WorldAccess(main, sender, command, label, args, "ON", "");
            		}
            		// accés au monde privé
            		if(arg2.toUpperCase().equals("OFF")) 
            		{
            			WorldAccess(main, sender, command, label, args, "OFF", "");
            		}
            		
            		
            		// ajouter un joueur dans la whitelist
            		if(arg2.toUpperCase().equals("USERS")) 
            		{
            			String filter = null;
            			if(args.length >= 3) {
                    		String arg3 = args[2];
                    		filter = arg3;
            			}
            			WorldAccess(main, sender, command, label, args, "USERS", filter);
            		}
            		
            		// ajouter un joueur dans la whitelist
            		if(arg2.toUpperCase().equals("ADD")) 
            		{
            			String player = "";
            			if(args.length >= 3) {
                    		String arg3 = args[2];
                    		player = arg3;
            			}
            			WorldAccess(main, sender, command, label, args, "ADD", player);
            		}
            		
            		// retirer un joueur de la whitelist
            		if(arg2.toUpperCase().equals("RMV")) 
            		{
            			String player = "";
            			if(args.length >= 3) {
                    		String arg3 = args[2];
                    		player = arg3;
            			}
            			WorldAccess(main, sender, command, label, args, "RMV", player);
            		}
            	}
            	else 
            	{
            		String player = "";
        			if(args.length >= 3) {
                		String arg3 = args[2];
                		player = arg3;
        			}
            		WorldAccess(main, sender, command, label, args, "GET", "");
            	}
            	
            	
            }
            
            else if(args.length >= 1 && args[0].toUpperCase().equals("PVP")) {
            	

            	if(args.length >= 2) 
            	{
            		String arg2 = args[1];
	            	// voir l'accés au monde (publique/privé)
	        		if(arg2.toUpperCase().equals("GET"))
	        			WorldPVP(main, sender, command, label, args, "GET", "");
	        		
	        		// accés au monde publique
	        		if(arg2.toUpperCase().equals("ON")) 
	        		{
	        			WorldPVP(main, sender, command, label, args, "ON", "");
	        		}
	        		
	        		// accés au monde privé
	        		if(arg2.toUpperCase().equals("OFF")) 
	        		{
	        			WorldPVP(main, sender, command, label, args, "OFF", "");
	        		}
            	}
            	else 
            	{
            		String player = "";
        			if(args.length >= 3) {
                		String arg3 = args[2];
                		player = arg3;
        			}
            		WorldPVP(main, sender, command, label, args, "GET", "");
            	}
        		
            }
            
        } else {
            sender.sendMessage("This command can be use only by players entities.");
        }
        return true;
    }
}