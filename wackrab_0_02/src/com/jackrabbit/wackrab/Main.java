package com.jackrabbit.wackrab;

import org.bukkit.plugin.java.JavaPlugin;

import com.jackrabbit.wackrab.TimerPlayers.TimePlayerRelation;
import com.jackrabbit.wackrab.commands.CommandH;
import com.jackrabbit.wackrab.commands.CommandPath;
import com.jackrabbit.wackrab.commands.CommandTeleport;
import com.jackrabbit.wackrab.commands.CommandTest;
import com.jackrabbit.wackrab.commands.CommandWhereIsPlayer;
import com.jackrabbit.wackrab.commands.CommandeMonde;
import com.jackrabbit.wackrab.commands.MaCommandeExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;


public class Main extends JavaPlugin implements Listener{

	public HierarchieWorlds hierachieWorlds;
	public TimerPlayers timerPlayers;
	
	public int maxSubWorlds = 4;

	public int timeCounterSavePositionPlayer = 5;
	public int timeCounterSavePositionPlayerMax = 5;

	String absFilepathPlayers;
	String absFilepathAllowedPlayers;
	
	public class AllowedPlayers{
		public String playerName;
		public int SubLevel;
	}

	

	

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player joueur = event.getPlayer();

        if (joueur.isOnline()) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage("Vous êtes déjà connecté !");
        }

        
    }

    public boolean isExoticWorld(World world) {
    	return false;
    	//todo  en gros il retourne true si le monde appartient pas a notre hiérarchie.
    }
    
  
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        //if(event.getPlayer().hasPermission("al.joinvip"))
        //{
        //    Player p = event.getPlayer();
        //event.setJoinMessage(p.getDisplayName() + ChatColor.YELLOW + " has joined !");
        //}
        Player joueur = event.getPlayer();

        String MessageBienvenue = ChatColor.GOLD + "Hey ! \n";
        MessageBienvenue += "Survival server, with parallel dimentions management ! \n";
        MessageBienvenue += "Please use /h for help. ";
        joueur.sendMessage(MessageBienvenue);
        
    }
    
  
    
    

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        // Récupérer le joueur qui a envoyé le message
        String joueur = event.getPlayer().getName();

        // Récupérer le contenu du message
        String message = event.getMessage();

        // Faire quelque chose avec le message
        // Par exemple, l'afficher dans la console
        if(message.toUpperCase().equals("/WORLD")) {
        	Player p = event.getPlayer();
        	String msg = CommandeMonde.WorldInfo(this, p);
        	p.sendMessage(msg);
        }
        
    }
    
    
    public void teleportPlayer(Player p, World desiredWorld) {
    	p.sendMessage("Teleporting...");
		//if(!canGoToTheWorld)
		//	p.sendMessage("Uh.. You are not allowed to be there...");
			
		Location loc2 = p.getLocation();
		int ypos = getHighestSolidBlockYPosition(loc2.getX(),loc2.getZ(), desiredWorld);
		Block block = getHighestSolidBlock(loc2.getX(),loc2.getZ(), desiredWorld);

		if(block == null||block.getType() == Material.WATER || block.getType() == Material.AIR || block.getType() == Material.LAVA || block.getType() == Material.FIRE) {
			Location loc3 = new Location(desiredWorld,loc2.getX(), ypos +1, loc2.getZ());
			loc3.getBlock().setType(Material.STONE);
			p.teleport(new Location(desiredWorld, loc3.getX(), ypos +3, loc3.getZ()));
		}
		else
			p.teleport(new Location(desiredWorld, loc2.getX(), ypos, loc2.getZ()));
		
    }
    
    //boolean teleporting = false;
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {

    	try {
	    	//if(teleporting == true) return;
	    	Player player = event.getPlayer();    
	    	String mondeDepart = event.getFrom().getWorld().getName();
	    	World mondeArrive = event.getTo().getWorld();
	    	
	    	String defaultWorldName = hierachieWorlds.getDefaultWorldRelation().worldName;
	    	String wantedWorldName = mondeArrive.getName();
	    	
	    	// le monde est celui par default, ...  (donc on veux se tp)
	    	if(wantedWorldName.toUpperCase().equals(defaultWorldName.toUpperCase())) {

		    	JoueurInfo ji = lirePositionJoueur(player);
		    	String lastWorldFilePlayer = ji.getNomMonde();
		    	
	    		// soit on veux ce monde (est ecrit dans le fichier, il se passe rien)
		    	if(lastWorldFilePlayer.toUpperCase().equals(wantedWorldName.toUpperCase()))
	    			return;
	    			
	    		// soit on va chercher un autre monde  car  on le veux pas.
		    	// et l'autre monde n'est pas celui par default (on le vois dans le fichier)
	    	
		    	//if(!mondeArrive.getName().toUpperCase().equals(defaultWorldName.toUpperCase())) {
	    		if(mondeDepart.toUpperCase().endsWith("NETHER") ||
	    		           mondeDepart.toUpperCase().endsWith("THE_END")) {
	    	
	    			// et on viens d'un monde exotique (nether et end)
	        		event.setCancelled(true);

	        		// on se TP !!!!!
	        		TeleportPlayerOnLastPositionFile(player);

	    		}
		    	//}
	    	}
	    	
	    	/*
	        if(mondeDepart.toUpperCase().endsWith("NETHER") ||
	           mondeDepart.toUpperCase().endsWith("THE_END")) {
	        	if(mondeArrive.getName().equals(hierachieWorlds.getDefaultWorldRelation())) {
	        		

	        		event.setCancelled(true);
	        		//event.setCancelled(true);
	        		
	        		TeleportPlayerOnLastPositionFile(player);

	        	}
	        }*/
    	}catch(Exception ex) {

    		event.setCancelled(true);
    	}finally {
    		//teleporting = false;
		}
        
        
    }
    
    
	@Override
	public void onEnable() {
		
		System.out.println("== == == == == == == == == == == == == == == == == == == ==");
		System.out.println(" OK Wackrab.");
		System.out.println("== == == == == == == == == == == == == == == == == == == ==");

        
		
        getServer().getPluginManager().registerEvents(this, this);

        System.out.println("Reading Config.");

        // def
        
   	 	String prefixWorld = "Wackrab_";
		String parentNameDefaultWorld = null; //"5_L_10";
		boolean createWorlds = false;
		String commandInfoWorld = "world";
		
		
		// PATH PLUGIN
		
		String absPathPluginData = getDataFolder().getPath();
		new File(absPathPluginData).mkdirs();  

		absFilepathPlayers = absPathPluginData + "_players.dat";
		absFilepathAllowedPlayers = absPathPluginData + "_allowedPlayers.dat"; //Wackrab_allowedPlayers.dat

		// path plugin file
		
		File file = new File(absFilepathPlayers);
		if (!file.exists()) {
		    file.getParentFile().mkdirs();

		    try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
		  System.out.println("File already exists");
		}
		
		
	    
        //FILE CONFIG
        
        String absPath= getDataFolder().getParent();

    	System.out.println(absPath);
        
        Path filePath = Path.of(absPath + "/wackrab.conf");
        String fileContent = "";

        try {
            byte[] bytes = Files.readAllBytes(filePath);
            fileContent = new String (bytes);
            
            String[] argsKV = fileContent.split(";");
            for(String argKV : argsKV)
            {
            	if(argKV.contains("=")) {
	            	String argKey = argKV.split("=")[0].toUpperCase();
	            	String argValue = argKV.split("=")[1];

	            	if(argKey.equals("PREFIXWORLD")) {
	            		prefixWorld = argValue;
	            	}
	            	if(argKey.equals("PARENTNAMEDEFAULTWORLD")) {
	            		if(argValue.trim().length() > 0 && argValue.toUpperCase().equals("NULL"))
	            			parentNameDefaultWorld = argValue;
	            	}
	            	if(argKey.equals("MAXSUBWORLDS")) {
	            		maxSubWorlds = Integer.valueOf(argValue);
	            	}
	            	if(argKey.equals("CREATEWORLDS")) {
	            		if(argValue.toUpperCase() == "TRUE") {
		            		createWorlds = true;
	            		}
	            	}
	            	if(argKey.equals("COMMANDINFOWORLD")) {
	            		if(argValue.trim().length() != 0)
	            			commandInfoWorld = argValue;
	            		
	            	}
	            }
            }
            
            System.out.println(">> fileContent: "+fileContent);
            
        } catch (IOException e) {
            //handle exception

        	System.out.println("Json Config './wackrab.conf' Reading File Error");
        	return;
        }

    	System.out.println("== CONFIG : ==");

    	System.out.println("prefixWorld "+prefixWorld);
    	System.out.println("parentNameDefaultWorld "+parentNameDefaultWorld);
    	System.out.println("maxSubWorlds "+maxSubWorlds);
    	System.out.println("createWorlds "+createWorlds);	
    	System.out.println("commandInfoWorld "+commandInfoWorld);		
        
    	System.out.println("=============================================");
    	System.out.println("=============================================");
    	System.out.println("=============================================");
        
    	// instances
        
    	timerPlayers = new TimerPlayers();
    	System.out.println("timerPlayers ok");
    	
		// créer la hiérarchie de monde (dans le serveur)
		
		hierachieWorlds = new HierarchieWorlds();
		ArrayList<WorldRelation> worlds = hierachieWorlds.CreateWorlds(
				this, maxSubWorlds, prefixWorld, parentNameDefaultWorld, createWorlds, false);
		
        //int lockedTeleportationTime = 0;
        //int lockedTeleportationTimeMaxV = 25;
        
		// commands

		 // commands
         getCommand("test").setExecutor(new CommandTest());
         getCommand("wacktp").setExecutor(new CommandTeleport());
		 // cmd monde info
		 this.getCommand(commandInfoWorld).setExecutor(new CommandeMonde(this));
		 // help
	     getCommand("h").setExecutor(new CommandH());
		 // commande all world
	     getCommand("cmd").setExecutor(new MaCommandeExecutor(this));
	     // whereis Player
	     // logical path
	     getCommand("path").setExecutor(new CommandPath(this, false));
	     getCommand("pathfrom").setExecutor(new CommandPath(this, true));
	     getCommand("whereis").setExecutor(new CommandWhereIsPlayer(this));
	     
	     
		 // listenners: (deco reco players)
		 
		 getServer().getPluginManager().registerEvents(new MonPluginListener(), this);

	    	
    	 Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

             @Override
             public void run() {

           		timeCounterSavePositionPlayer--;
             	// check positions player
             	for(Player p: Bukkit.getOnlinePlayers()){
             		
             		Location loc = p.getLocation();
             		
             		World worldplayer = p.getWorld();
             		
             		double x = loc.getX();
             		double z = loc.getZ();
             		double y = loc.getY();

             		int xi = (int)Math.floor(x);
             		int zi = (int)Math.floor(z);
             		int yi = (int)Math.floor(y)-1;
             		

             		// SAVE PLAYER POSITION IN SERVER IF DIFFERENT
             		
             		if(timeCounterSavePositionPlayer < 0) {
	             		JoueurInfo joueurInfoRead = lirePositionJoueur(p);
	             		if(joueurInfoRead != null) {
	             			if(		(int)(joueurInfoRead.getX()) != (int)(x) ||
	             					(int)(joueurInfoRead.getY()) != (int)(y) ||
	             					(int)(joueurInfoRead.getZ()) != (int)(z) ||
	             					!joueurInfoRead.getNomMonde().toUpperCase().equals(worldplayer.getName().toUpperCase())
	             					) {
	             					JoueurInfo joueurInfoWrite = new JoueurInfo(p.getName(), x, y, z, worldplayer.getName());
	
	             					Location loc2 = new Location(worldplayer, x, y, z);
	             					enregistrerPositionJoueur(joueurInfoWrite, p.getName(), loc2);
	             				
	
	             					System.out.println("Save position on server file:  "+p.getName()+"  x"+ x+"  y"+ y+"  "+"  z"+z+ "  -  "+worldplayer.getName());
	             			}
	             		}
	             		else {
	             			JoueurInfo joueurInfoWrite = new JoueurInfo(p.getName(), x, y, z, worldplayer.getName());
	             			
         					Location loc2 = new Location(worldplayer, x, y, z);
         					enregistrerPositionJoueur(joueurInfoWrite, p.getName(), loc2);
         				
	             			System.out.println("Save First position on server file:  "+p.getName()+"  x"+ x+"  y"+ y+"  "+"  z"+z+ "  -  "+worldplayer.getName());
	             		}
             		}
             		
             		
             		// CHECK TELEPORTER
             		
             		Material blockType = getBlockTypeAtLocation(worldplayer, xi, yi, zi);
             		
             		
             		// CHECK ALLOWED PSEUDOS
             		ArrayList<AllowedPlayers> allowedPseudos = new ArrayList<AllowedPlayers>();

        	        int defaultAllowedSubLevelForPlayers = 1;
        	        
             		try {
             			
             			// lire fichier pseudos alloués
             			BufferedReader reader = new BufferedReader(new FileReader(absFilepathAllowedPlayers));
            	        StringBuilder stringBuilder = new StringBuilder();
            	        String line = null;
            	        String ls = System.getProperty("line.separator");
            	        while ((line = reader.readLine()) != null) {
            	        	stringBuilder.append(line);
            	        	//stringBuilder.append(ls);
            	        }
            	        // delete the last new line separator
            	        //stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            	        String content = stringBuilder.toString().replaceAll(ls, "");
            	        
            	        String[] lines = content.split(";");
            	        
            	        reader.close();
            	        
            	        
            	        for(String lineSEP : lines)
            	        	if(lineSEP.length() > 0 && lineSEP.contains("=")) {
            	        		String nomJoueur = lineSEP.split("=")[0];
            		        	String subLevelJoueur = lineSEP.split("=")[1];
            		        	
            		        	if(nomJoueur.equals("<DEFAULT>")) {
            		        		defaultAllowedSubLevelForPlayers = Integer.parseInt(subLevelJoueur);
            		        	}
            	        	}
            	        
            	        
            	        for(String lineSEP : lines)
            	        {
            	        	if(lineSEP.length() > 0 && lineSEP.contains("=")) {
            		        	String nomJoueur = lineSEP.split("=")[0];
            		        	String subLevelJoueur = lineSEP.split("=")[1];
            		        	
            		        	AllowedPlayers allowedplr = new AllowedPlayers();
            		        	allowedplr.playerName = nomJoueur;
            		        	allowedplr.SubLevel = Integer.parseInt(subLevelJoueur);
            		        	
            		        	allowedPseudos.add(allowedplr);
            		        }
            	        }
            	        
             			// remplir la liste
             			
             		}catch (Exception ex){
             			
             		}
             		
             		// sublevel player (original or listed)
             		int sublevelplayer = defaultAllowedSubLevelForPlayers;
             		for(AllowedPlayers allowedPseudo : allowedPseudos)
                 		if(p.getName().toUpperCase().equals(allowedPseudo.playerName.toUpperCase()))
                 			sublevelplayer = allowedPseudo.SubLevel;
             		
             		
             		//for(AllowedPlayers allowedPseudo : allowedPseudos)
             		//if(p.getName().toUpperCase().equals(allowedPseudo.playerName.toUpperCase()))
             		if(
             				blockType == Material.LEGACY_DIAMOND_BLOCK ||
             				blockType == Material.LEGACY_EMERALD_BLOCK ||
             				blockType == Material.LEGACY_GOLD_BLOCK ||
             				blockType == Material.DIAMOND_BLOCK ||
             				blockType == Material.EMERALD_BLOCK ||
             				blockType == Material.GOLD_BLOCK ) {
             			
             			Material btX8 = getBlockTypeAtLocation(worldplayer, xi-2, yi, zi);
             			Material btX9 = getBlockTypeAtLocation(worldplayer, xi-1, yi, zi);
             			Material btX11 = getBlockTypeAtLocation(worldplayer, xi+1, yi, zi);
             			Material btX12 = getBlockTypeAtLocation(worldplayer, xi+2, yi, zi);
             			
             			Material btZ8 = getBlockTypeAtLocation(worldplayer, xi, yi, zi-2);
             			Material btZ9 = getBlockTypeAtLocation(worldplayer, xi, yi, zi-1);
             			Material btZ11 = getBlockTypeAtLocation(worldplayer, xi, yi, zi+1);
             			Material btZ12 = getBlockTypeAtLocation(worldplayer, xi, yi, zi+2);
             			
             			Material btX9Z9 = getBlockTypeAtLocation(worldplayer, xi-1, yi, zi-1);
             			Material btX11Z9 = getBlockTypeAtLocation(worldplayer, xi+1, yi, zi-1);
             			Material btX9Z11 = getBlockTypeAtLocation(worldplayer, xi-1, yi, zi+1);
             			Material btX11Z11 = getBlockTypeAtLocation(worldplayer, xi+1, yi, zi+1);
             			
             			String cutsandstone_blockstr = "CUT_SANDSTONE";
             			String quartzblockstr = "QUARTZ_BLOCK";
             			String obsidianblockstr = "OBSIDIAN";
             			
             			if(
             					btX9Z9.name().contains(obsidianblockstr) 
             				&& btX11Z9.name().contains(obsidianblockstr)
             				&& btX9Z11.name().contains(obsidianblockstr)
             				&& btX11Z11.name().contains(obsidianblockstr))
             			{
             				if(
         						(btX8.name().contains(obsidianblockstr) &&
         						(btX9.name().contains(cutsandstone_blockstr) || btX9.name().contains(quartzblockstr)) && 
         						(btX11.name().contains(cutsandstone_blockstr) || btX11.name().contains(quartzblockstr))  && 
         						btX12.name().contains(obsidianblockstr) && 
                 	        	btZ8.name().contains(obsidianblockstr) &&
                 	            (btZ9.name().contains(cutsandstone_blockstr) || btZ9.name().contains(quartzblockstr))  && 
                 	            (btZ11.name().contains(cutsandstone_blockstr) || btZ11.name().contains(quartzblockstr))  &&
                 	            btZ12.name().contains(obsidianblockstr)) ||
         						
         						(btX8.name().contains(obsidianblockstr) &&
         						(btX9.name().contains(cutsandstone_blockstr)  || btX9.name().contains(quartzblockstr)) &&
         						(btX11.name().contains(cutsandstone_blockstr)  || btX11.name().contains(quartzblockstr)) &&
         						btX12.name().contains(obsidianblockstr) &&
                 	        	btZ8.name().contains(obsidianblockstr) &&
                 	            (btZ9.name().contains(cutsandstone_blockstr)  || btZ9.name().contains(quartzblockstr)) &&
                 	            (btZ11.name().contains(cutsandstone_blockstr) || btZ11.name().contains(quartzblockstr))  &&
                 	            btZ12.name().contains(obsidianblockstr) ) ) {

             					
             					//search the "from" world
         						for(WorldRelation w : worlds) {
         							
         							//boolean canGoToTheWorld = false;
         							if( !w.worldName.isEmpty() && w.worldName.toUpperCase().trim().equals(worldplayer.getName().toUpperCase().trim())) {
	         							//	canGoToTheWorld = true;
	         							//}

                     					boolean allowedToTeleport = false;
                     					
	     								// si le joueur mérite de traverser le monde:
	
	         							//System.out.println("test: ");
	         							//System.out.println(sublevelplayer);
	         							//System.out.println(w.worldSubPosition);
	         							
	 									// si monde existe pas: le créer
	 									
	     								TimePlayerRelation prel = timerPlayers.getPlayerRelation(p);
	     								
	                 					if(blockType == Material.LEGACY_GOLD_BLOCK || 
	                 							blockType == Material.GOLD_BLOCK) {
	
	                 						if(sublevelplayer > w.worldSubPosition-1) 
	         									allowedToTeleport = true;
	         								
	                 						
	                 						if(allowedToTeleport) {
		                 						if(prel.desiredWorld == null) {
		                 							if(prel.enableteleportmessage)
		                 								p.sendMessage("Loading...");
		     										prel.desiredWorld = hierachieWorlds.getWorldByName(w.fromWorldName);
		                 						}
								
		         								if(prel.desiredWorld != null) {
		         									if(prel.count()) {
		         										
		         										teleportPlayer(p, prel.desiredWorld);
		         									}
		         								}
		         								else {
		         									if(prel.enableteleportmessage)
		         										p.sendMessage("Can't teleport, There are no world on the other side...");
		         									prel.enableteleportmessage = false;
		         								}
	                 						}else {
	                 							if(prel.enableteleportmessage)
	         										p.sendMessage("You are not allowed to go here...");
	                 							prel.enableteleportmessage = false;
	                 						}
	         								
	                 					}
	
	                 					else if(blockType == Material.LEGACY_DIAMOND_BLOCK 
	                 							|| blockType == Material.DIAMOND_BLOCK) {
	
	                 						if(sublevelplayer > w.worldSubPosition+1) 
	         									allowedToTeleport = true;
	                 						
	     									String subWorldName = w.subWorldLName;
	     									if(allowedToTeleport) {
		     									if(prel.desiredWorld == null) {
		                 							if(prel.enableteleportmessage)
		                 								p.sendMessage("Loading...");
		     										prel.desiredWorld = hierachieWorlds.getWorldByName(subWorldName);
		     									}
		     									
	             								if(prel.desiredWorld != null) {
	             									if(prel.count()) {

		         										teleportPlayer(p, prel.desiredWorld);
		             								}
	             								}
	             								else {
	             									if(prel.enableteleportmessage)
	             										p.sendMessage("Can't teleport, There are no world on the other side...");
	             									prel.enableteleportmessage = false;
	             								}
	                     						
		             						}else {
		             							if(prel.enableteleportmessage)
		     										p.sendMessage("You are not allowed to go here...");
		     									prel.enableteleportmessage = false;
		             						}
	                 					}
	                 					
	                 					else if(blockType == Material.LEGACY_EMERALD_BLOCK 
	                 							|| blockType == Material.EMERALD_BLOCK) {
	
	                 						if(sublevelplayer > w.worldSubPosition+1) 
	         									allowedToTeleport = true;
	                 						
	                 						String subWorldName = w.subWorldRName;
	                 						
	                 						if(allowedToTeleport) {
		     									if(prel.desiredWorld == null) {
		                 							if(prel.enableteleportmessage)
		                 								p.sendMessage("Loading...");
		     										prel.desiredWorld = hierachieWorlds.getWorldByName(subWorldName);
		     									}
		     										
		         								if(prel.desiredWorld != null) {
		         									if(prel.count()) {

		         										teleportPlayer(p, prel.desiredWorld);
		             								}
		         								}
		         								else {
		         									if(prel.enableteleportmessage)
		         										p.sendMessage("Can't teleport, There are no world on the other side...");
		         									prel.enableteleportmessage = false;
		         								}
	                 						}else {
		             							if(prel.enableteleportmessage)
		     										p.sendMessage("You are not allowed to go here...");
		     									prel.enableteleportmessage = false;
		             						}
	                 					}
	                 					else {
	     									prel.reset();
	     									prel.enableteleportmessage = true;
	                 					}
         							}
     							}
             				}
             			}
             			
             		}
             		else {

						TimePlayerRelation prel = timerPlayers.getPlayerRelation(p);
						prel.reset();
						prel.enableteleportmessage = true;
						
             		}
             		
             	}
             	

           		if(timeCounterSavePositionPlayer < 0) {
           			timeCounterSavePositionPlayer = timeCounterSavePositionPlayerMax;
           			
           		}
             }
             
            
         }, 30, 10);
                
	}
	
	
	public Block getHighestSolidBlock(int x, int z, World world) {
	    int maxY = world.getMaxHeight();

	    for (int y = maxY; y >= 0; y--) {
	        Block block = world.getBlockAt(x, y, z);

	        if (block.getType() != Material.AIR) {
	            return block;
	        }
	    }

	    return null; // Aucun bloc solide trouvé
	}

	public int getHighestSolidBlockYPosition(double x, double z, World world) {
		int xi = (int)Math.floor(x);
		int zi = (int)Math.floor(z);
		Block block = this.getHighestSolidBlock(xi, zi, world);
		if(block == null) return 64;
		return block.getY() + 2;
	}

	public Block getHighestSolidBlock(double x, double z, World world) {
		int xi = (int)Math.floor(x);
		int zi = (int)Math.floor(z);
		Block block = this.getHighestSolidBlock(xi, zi, world);
		return block;
	}
		
	public Material getBlockTypeAtLocation(World world, int x, int y, int z) {
        Location location = new Location(world, x, y, z);
        Block block = location.getBlock();
        Material blockType = block.getType();
        return blockType;
    }
	
	@Override
	public void onDisable() {
		System.out.println("Bye Plugin Wackrab.");
	}
	
	
	

	private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();
	
    
    public JoueurInfo lirePositionJoueur(Player joueur) {
    	 try {
             lock.lock(); // Acquérir le verrou

             JoueurInfo joueurinfo = null;
             
	    	// Lire toutes les données du fichier
	        
	        BufferedReader reader = new BufferedReader(new FileReader(absFilepathPlayers));
	        StringBuilder stringBuilder = new StringBuilder();
	        String line = null;
	        String ls = System.getProperty("line.separator");
	        while ((line = reader.readLine()) != null) {
	        	stringBuilder.append(line);
	        	//stringBuilder.append(ls);
	        }
	        // delete the last new line separator
	        //stringBuilder.deleteCharAt(stringBuilder.length() - 1);

	        String content = stringBuilder.toString().replaceAll(ls, "");
	        
	        String[] lines = content.split(">>");
	        
	        reader.close();
	        
	        for(String lineSEP : lines)
	        {
	        	if(lineSEP.length() > 0) {
		        	String nomJoueur = lineSEP.split(";")[0];
		        	if(nomJoueur.toString().equals(joueur.getName())) {
		        		joueurinfo = new JoueurInfo();
		        		joueurinfo.FromStringFile(lineSEP);
		        	}   
		        }
	        }
	        
	        
	        return joueurinfo;
	        
	        
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             lock.unlock(); // Toujours libérer le verrou dans un bloc finally
         }
	     
    	 return null;
        
    }
    
    public void enregistrerPositionJoueur(JoueurInfo joueurinfo, String joueurName, Location nouvellePosition) {
    	  try {
              lock.lock(); 

              if(nouvellePosition.getWorld() == null) return;
              
            String worldName = nouvellePosition.getWorld().getName();
            
             if(worldName.isEmpty()) return;

            if(worldName.contains("nether")) {
            	//lock.unlock();
            	return;
            }
            if(worldName.contains("the_end")) {
            	//lock.unlock();
            	return;
            }
            

            
          	joueurinfo.setNomJoueur(joueurName);
          	joueurinfo.setX(nouvellePosition.getX());
          	joueurinfo.setY(nouvellePosition.getY());
          	joueurinfo.setZ(nouvellePosition.getZ());
          	joueurinfo.setNomMonde(worldName);
          	
              
          	String dataJoueurInfo = joueurinfo.ToStringFile();            
              
          	// Lire toutes les données du fichier

	        BufferedReader reader = new BufferedReader(new FileReader(absFilepathPlayers));
	        StringBuilder stringBuilder = new StringBuilder();
	        String line = null;
	        String ls = System.getProperty("line.separator");
	        while ((line = reader.readLine()) != null) {
	        	stringBuilder.append(line);
	        	//stringBuilder.append(ls);
	        }
	        // delete the last new line separator
	        //stringBuilder.deleteCharAt(stringBuilder.length() - 1);

	        String content = stringBuilder.toString().replaceAll(ls, "");
	        
	        reader.close();

	        String[] lines = content.split(">>");
  	        
  	        if(lines.length > 0) {
  		        String newFileContent = "";
  		        for(String line2 : lines)
  		        {
  		        	if(line2.length() > 0) {
  			        	String nomJoueur = line2.split(";")[0];
  			        	if(!nomJoueur.toString().equals(joueurName) && !nomJoueur.toString().isEmpty()) {
  			        		newFileContent += ">>" + line2.toString() + ls;
  			        	}
  		        	}
  		        }
  	
  	    		newFileContent += ">>" + dataJoueurInfo.toString() + ls;
  	    		
  	    		PrintWriter writer = new PrintWriter(absFilepathPlayers);
  	    		writer.print(newFileContent.toString());
  	    		writer.close();
  	    		
  	    		//BufferedWriter writer2 = new BufferedWriter(new FileWriter(absFilepathPlayers));
  	    		
  	    	    //writer2.write(newFileContent.toString());
  	    	    
  	    	    //writer2.close();
  	        }
      	    
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
              lock.unlock(); // Toujours libérer le verrou dans un bloc finally
          }
    }
    
    public void enregistrerPositionJoueur(Player joueur) {
        try {
            lock.lock(); // Acquérir le verrou

            Location nouvellePosition = joueur.getLocation();
        	JoueurInfo joueurinfo = new JoueurInfo();
        	joueurinfo.setNomJoueur(joueur.getName());
        	joueurinfo.setX(nouvellePosition.getX());
        	joueurinfo.setY(nouvellePosition.getY());
        	joueurinfo.setZ(nouvellePosition.getZ());
        	joueurinfo.setNomMonde(nouvellePosition.getWorld().getName());
            
        	String dataJoueurInfo = joueurinfo.ToStringFile();            
            
        	enregistrerPositionJoueur(joueurinfo, joueur.getName(), nouvellePosition);
    	    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // Toujours libérer le verrou dans un bloc finally
        }
    }
    
    
	    
	    
	
	public class JoueurInfo implements Serializable {
		
		public String ToStringFile() {
			return ">>" 
					+ nomJoueur + ";" 
					+ Double.toString(x) + ";" 
					+ Double.toString(y) + ";" 
					+ Double.toString(z) + ";" 
					+ nomMonde ;
		}
		
		public void FromStringFile(String dataFile) {
			String[] data = dataFile.split(";");
			nomJoueur = data[0];
			x =  Double.parseDouble(data[1]);
			y =  Double.parseDouble(data[2]);
			z =  Double.parseDouble(data[3]);
			nomMonde = data[4];
		}
	
	    private String nomJoueur;
	    private double x;
	    private double y;
	    private double z;
	    private String nomMonde;

	    public JoueurInfo(String nomJoueur, double x, double y, double z, String nomMonde) {
	        this.nomJoueur = nomJoueur;
	        this.x = x;
	        this.y = y;
	        this.z = z;
	        this.nomMonde = nomMonde;
	    }
	    
	    public JoueurInfo() {
	    }
	
	    // Getters
	    public String getNomJoueur() {
	        return nomJoueur;
	    }
	
	    public double getX() {
	        return x;
	    }
	
	    public double getY() {
	        return y;
	    }
	
	    public double getZ() {
	        return z;
	    }
	
	    public String getNomMonde() {
	        return nomMonde;
	    }
	
	    // Setters
	    public void setNomJoueur(String nomJoueur) {
	        this.nomJoueur = nomJoueur;
	    }
	
	    public void setX(double x) {
	        this.x = x;
	    }
	
	    public void setY(double y) {
	        this.y = y;
	    }
	
	    public void setZ(double z) {
	        this.z = z;
	    }
	
	    public void setNomMonde(String nomMonde) {
	        this.nomMonde = nomMonde;
	    }
	}
	
	  
    public void TeleportPlayerOnLastPositionFile(Player joueur) {

        JoueurInfo joueurInfo = lirePositionJoueur(joueur);

        if(joueurInfo != null) {
        	
	        // Récupérer les coordonnées du joueur
	        //double x = joueur.getLocation().getX();
	        //double y = joueur.getLocation().getY();
	        //double z = joueur.getLocation().getZ();
	        // Récupérer le nom du monde
	        //String nomDuMonde = joueur.getWorld().getName();

	        double x = joueurInfo.getX();
	        double y = joueurInfo.getY();
	        double z = joueurInfo.getZ();
	        String nomDuMonde =  joueurInfo.getNomMonde();
	        
	        //double y = joueur.getLocation().getY();
	        //double z = joueur.getLocation().getZ();
	       
	        
	    	 System.out.println(joueur.getName() + " s'est reco à la position : X=" + x + ", Y=" + y + ", Z=" + z + " dans le monde : " + nomDuMonde);
	    	 
	    	 // si le monde n'existe pas, monde normal
	    	 
	    	 World world = hierachieWorlds.getWorldByName(nomDuMonde);

    		 System.out.println("test monde "+nomDuMonde);
    		 
	    	 if(world != null) {
		        // Définir la nouvelle position du joueur
	    		 

		        Location nouvellePosition = new Location(world, x, y, z);
		        joueur.teleport(nouvellePosition);
	    	 }
	    	 else {
	    		 System.out.println("monde introuvable pour le joueur "+ joueurInfo.getNomJoueur());
	    	 }
	    		 
        
        }
    }
	
	
	

	//absFilepathPlayers
	public class MonPluginListener implements Listener {
	
	    @EventHandler
	    public void onPlayerQuit(PlayerQuitEvent event) {
	        // Récupérer le joueur qui se déconnecte
	        Player joueur = event.getPlayer();
	        
	        enregistrerPositionJoueur(joueur);
	    }
	  
	    
	    @EventHandler
	    public void onPlayerJoin(PlayerJoinEvent event) {
	        Player joueur = event.getPlayer();
	        
	        TeleportPlayerOnLastPositionFile(joueur);
	        
	    }
	    
	}
	
	
	

	
	
}
