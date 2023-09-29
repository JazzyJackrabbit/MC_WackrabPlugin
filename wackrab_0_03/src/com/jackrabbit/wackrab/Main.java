package com.jackrabbit.wackrab;

import org.bukkit.plugin.java.JavaPlugin;

import com.jackrabbit.wackrab.TimerPlayers.TimePlayerRelation;
import com.jackrabbit.wackrab.commands.CommandAllowedPlayer;
import com.jackrabbit.wackrab.commands.CommandClaimWorld;
import com.jackrabbit.wackrab.commands.CommandGameRuleWorlds;
import com.jackrabbit.wackrab.commands.CommandH;
import com.jackrabbit.wackrab.commands.CommandHowTo;
import com.jackrabbit.wackrab.commands.CommandPath;
import com.jackrabbit.wackrab.commands.CommandTeleport;
import com.jackrabbit.wackrab.commands.CommandTest;
import com.jackrabbit.wackrab.commands.CommandWackPlayer;
import com.jackrabbit.wackrab.commands.CommandWhereIsPlayer;
import com.jackrabbit.wackrab.commands.CommandeMonde;
import com.jackrabbit.wackrab.commands.MaCommandeExecutor;
import com.jackrabbit.wackrab.sqlite.Db;
import com.jackrabbit.wackrab.sqlite.DbDataPlayer;
import com.jackrabbit.wackrab.ui.UIHierarchieWorlds;
import com.jackrabbit.wackrab.ui.UIHierarchiesWorlds;
import com.jackrabbit.wackrab.utils.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.World;


public class Main extends JavaPlugin implements Listener{

	public String pathDB_SQLite = null;
	
	public int defaultAllowedSubLevelForPlayers = 5;
	
	public HierarchieWorlds hierachieWorlds;
	public TimerPlayers timerPlayers;
	public UIHierarchiesWorlds uiHierarchiesWorlds;

	public int maxSubWorlds = 4;
	public int enablePvpSubWorlds = 4;

	public int timeCounterSavePositionPlayer = 5;
	public int timeCounterSavePositionPlayerMax = 5;

	String wackrabconfig = "/wackrab.conf";
	
	Main main = this;


	String prefixWorld = null; //define later
	
	public Db DTB;
	
	public class AllowedPlayers{
		public String playerName;
		public int SubLevel;
	}
	
	public void verrouilleInventory(boolean verrouille, Player player) {
		UIHierarchieWorlds ui = uiHierarchiesWorlds.getHierarchieWorlds(player.getName());
		if(ui != null) {
			if( !ui.isVerrouille) {
				//uiHierarchiesWorlds.creerInventaire(this, player);
			}
			ui.isVerrouille = verrouille;
		}
	}
	
	boolean isOpeningInventary = false;
	Lock lockOpeningInventary = new ReentrantLock();;
	
	public int getAllowedPlayerSubWorldLevel(Player p) {
		
		// CHECK ALLOWED PSEUDOS

 		try 
 		{
 			DbDataPlayer d = main.DTB.players.select(main.DTB, main.pathDB_SQLite, p.getName());
 			
 			if(d == null) {
 				d = main.DTB.players.newDbDataPlayer(p.getName(), defaultAllowedSubLevelForPlayers,
 						0, 90, 0, "unknown");
 				
 				main.DTB.players.insert(main.DTB, main.pathDB_SQLite, d, true);
 			}
 			return d.allowedSubWorld;
 		}
 		catch (Exception ex)
 		{
 			System.out.println("Exception getAllowedPlayerSubWorldLevel: "+ex.toString());
 		}
 		
 		return -1;
 	
	}
   
	
	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
	  	
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		    	
	}

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
    	Inventory i = event.getInventory();

    	Log.log(i.getClass());
    	Log.log(i.getClass().toString());
    	
    	boolean isWorldHierachiePanel = false;
    	
    	if(i.getItem(0) != null) {
    		
    		if(i.getItem(0).getItemMeta() != null) {
	    	
		    	isWorldHierachiePanel = i.getItem(0).getItemMeta().getAsString().toUpperCase().contains("Wackrab_HierarchieWorld".toUpperCase());
	    	
	    		if(isWorldHierachiePanel) {
			    	
					if(i.getClass().toString().toUpperCase().contains("CraftInventoryCustom".toUpperCase())) {
			
			        	event.setCancelled(true);
				    	String playernm = event.getWhoClicked().getName();
				    	UIHierarchieWorlds ui = uiHierarchiesWorlds.getHierarchieWorlds(playernm);
				    	
				    	if(ui != null)
				    		ui.onClick(event);
			        	//event.setCancelled(true);
					}
					else {
			
				    	//System.out.println("bad");
					}
		    	}
    		
    		}
    	
    	}
		
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
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        // Récupérer le joueur
        Player joueur = event.getPlayer();

        // Récupérer le nouveau monde dans lequel le joueur est entré
        World nouveauMonde = joueur.getWorld();

        // Appeler la fonction que vous souhaitez exécuter lors du changement de monde
        String[] strArrayTrue = new String[1];
        strArrayTrue[0] = "true";
        
        String[] strArrayFalse = new String[1];
        strArrayFalse[0] = "false";
        
        CommandGameRuleWorlds.setGameRuleWorlds(this, nouveauMonde, "keepInventory", strArrayTrue);
        CommandGameRuleWorlds.setGameRuleWorlds(this, nouveauMonde, "tntExplosionDropDecay", strArrayFalse);
        CommandGameRuleWorlds.setGameRuleWorlds(this, nouveauMonde, "mobExplosionDropDecay", strArrayFalse);
        CommandGameRuleWorlds.setGameRuleWorlds(this, nouveauMonde, "fallDamage", strArrayFalse);
    }

    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
    	verrouilleInventory(false, event.getPlayer());
        //if(event.getPlayer().hasPermission("al.joinvip"))
        //{
        //    Player p = event.getPlayer();
        //event.setJoinMessage(p.getDisplayName() + ChatColor.YELLOW + " has joined !");
        //}
        Player joueur = event.getPlayer();
        
        // add timer and main. (pas besoin)
        //getPlayerRelation

       
        String MessageBienvenue = ChatColor.GREEN + "";


        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*1.0)==0 ? "-":" ";
        MessageBienvenue += "\n";

        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*1.5)==0 ? "-":" ";
        MessageBienvenue += "\n";
        
        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*2.5)==0 ? "-":" ";
        MessageBienvenue += "\n";

        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*1.5)==0 ? "-":" ";
        MessageBienvenue += "\n";
        
        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*1.0)==0 ? "-":" ";
        MessageBienvenue += "\n";
        
        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*1.5)==0 ? "-":" ";
        MessageBienvenue += "\n";
        
        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*2.5)==0 ? "-":" ";
        MessageBienvenue += "\n";

        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*1.5)==0 ? "-":" ";
        MessageBienvenue += "\n";
        
        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*1.0)==0 ? "-":" ";
        MessageBienvenue += "\n";
        
        
        
        
        String Version = "V0.16";
        MessageBienvenue += ChatColor.GRAY + "Welcome to the Matrix "+ChatColor.WHITE +" (Leetnia System "+Version+")"+ChatColor.GRAY +". \n";
        MessageBienvenue += "Please use  /h  for help. \n";
        
        MessageBienvenue += ChatColor.GREEN;
        
        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*1.0)==0 ? "-":" ";
        MessageBienvenue += "\n";

       /*for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*1.5)==0 ? "-":" ";
        MessageBienvenue += "\n";
        */
        
        for(int i = 0; i < 32*1.5; i++)
        	MessageBienvenue += Math.round(Math.random()*2.5)==0 ? "-":" ";
        MessageBienvenue += "\n";
        
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
    
    public boolean teleportPlayer(Player p, World desiredWorld) {
    	
		//if(!canGoToTheWorld)
		//	p.sendMessage("Uh.. You are not allowed to be there...");
			
    	double multiplicatorXZFactor = 1;

    	
    	if(desiredWorld != null) {

    		
	    	WorldRelation desiredwr = hierachieWorlds.getWorldRelationByName(desiredWorld.getName());

	    	World actualWorld = p.getWorld();
	    	WorldRelation actualWorldR = hierachieWorlds.getWorldRelationByName(actualWorld.getName());

			Location loc2 = p.getLocation();

			// factor: 
	    	if(desiredwr != null && actualWorldR != null) {

	    		double delta = 1;
	    		
	    		double deltasub = desiredwr.worldSubPosition - actualWorldR.worldSubPosition;
	    		
	    		if(deltasub>0) {
	    			for(int i = 0; i < (int)Math.abs(deltasub); i++) {
	    				delta = delta * multiplicatorXZFactor;
	    			}
	    		}
	    		else if (deltasub<0) {
	    			for(int i = 0; i < (int)Math.abs(deltasub); i++) {
	    				delta = delta / multiplicatorXZFactor;
	    			}
	    		}
				
	    		loc2.setX(loc2.getX()*delta);
				loc2.setZ(loc2.getZ()*delta);
				
	    	}
	    	
			int ypos = getHighestSolidBlockYPosition(loc2.getX(),loc2.getZ(), desiredWorld);
			Block block = getHighestSolidBlock(loc2.getX(),loc2.getZ(), desiredWorld);
	
			if(block == null||block.getType() == Material.WATER || block.getType() == Material.AIR || block.getType() == Material.LAVA || block.getType() == Material.FIRE) {
				Location loc3 = new Location(desiredWorld,loc2.getX(), ypos +1, loc2.getZ());
				loc3.getBlock().setType(Material.STONE);
	    		p.sendMessage("Teleporting...");
				return p.teleport(new Location(desiredWorld, loc3.getX(), ypos +3, loc3.getZ()));
			}
			else {
	    		p.sendMessage("Teleporting...");
				return p.teleport(new Location(desiredWorld, loc2.getX(), ypos, loc2.getZ()));
			}
			
		}
    	return false;
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

		    	JoueurInfo ji = lirePositionJoueur(player.getName());
		    	String lastWorldFilePlayer = ji.getNomMonde();
			    //.lastWorldFilePlayer => null"""//
			    //	Log.log("123 >>" + lastWorldFilePlayer);
		    	
		    	if(lastWorldFilePlayer == null)
		    		return;
		    	
	    		// soit on veux ce monde (est ecrit dans le fichier, il se passe rien)
		    	if(lastWorldFilePlayer.toUpperCase().equals(wantedWorldName.toUpperCase()))
	    			return;
		    	
		    	// soit ce monde est null, donc c'est le monde principal:
		    	
	    			

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
	    		
	    	}
	    	
    	}catch(Exception ex) {

    		event.setCancelled(true);
    	}finally {
    		//teleporting = false;
		}
        
        
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
    	
    	// Joueur VS Joueur
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            
            World world = damaged.getWorld();

            // Vérifier si le PvP est autorisé dans ce monde
            if (isPvPEnabledInWorld(world)) {
                event.setCancelled(false); // Autoriser le PvP
            } else {
                event.setCancelled(true); // Désactiver le PvP

                return;
            }
        }
        
        // HORSE
        else if (event.getEntity() instanceof Horse) {
        	Horse horse = (Horse) event.getEntity();
            World world = horse.getWorld();
            if (isPvPEnabledInWorld(world)) {
                event.setCancelled(false); // Autoriser le PvP
            } else {
                event.setCancelled(true); // Désactiver le PvP

                return;
            }
        }
        
        // ARROW  
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
             
            if (event.getEntity() instanceof Horse || event.getEntity() instanceof Player)
	            if (arrow.getShooter() instanceof Player) {
	            	
	         	    //System.out.println("333");
	                Player shooter = (Player) arrow.getShooter();
	            	if(!isPvPEnabledInWorld(shooter.getWorld())) {
	            		//System.out.println("444ok");
	            		event.setCancelled(true); // Annuler les dégâts
	                	arrow.remove(); // Retirer la flèche
	                	
	
	                    return;
	            	}
	            }
            
        }
    }
    
    
    
    private boolean isPvPEnabledInWorld(World world) {

    	 WorldRelation wr = hierachieWorlds.getWorldRelationByName(world.getName());
    	 
    	 boolean isPHPENABLE = false;
    	 
    	 if(wr.worldSubPosition >= enablePvpSubWorlds) 
    	 {
    		 isPHPENABLE = true;
    		 // c'est du pvp
    		 
    	 };
    	 
		 if(wr.isPVPEnableDBRequest(this, false) == 1) {
    		 // c'est du pvp
			 isPHPENABLE = true;
    	  }
		 // c'est pas du pvp
		 
		 //wr.isClaim(main) && 
    	 
    	 
		 return isPHPENABLE;
    }
    
    
	@Override
	public void onEnable() {
		
		Log.log("== == == == == == == == == == == == == == == == == == == ==");
		Log.log(" OK Wackrab.");
		Log.log("== == == == == == == == == == == == == == == == == == == ==");

   	 	main.DTB = new Db();
   	 
		uiHierarchiesWorlds = new UIHierarchiesWorlds();
		
        getServer().getPluginManager().registerEvents(this, this);

        Log.log("Reading Config.");

        // def
        
   	 	prefixWorld = "WackrabWorld_";
		String parentNameDefaultWorld = null; //"5_L_10";
		boolean createWorlds = false;
		String commandInfoWorld = "world";
		
		
		// PATH PLUGIN
		
		String absPathPluginData = getDataFolder().getPath();
		new File(absPathPluginData).mkdirs();  

		pathDB_SQLite = absPathPluginData + "/../../DB/leetnia.db";
	
        //FILE CONFIG
        
        String absPath= getDataFolder().getParent();

        Log.log(absPath);
        
        Path filePath = Path.of(absPath + wackrabconfig);
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
	            	
	            	if(argKey.equals("EnablePvpFromSubWorlds".toUpperCase())) 
	            		enablePvpSubWorlds = Integer.valueOf(argValue);
	            	
	            }
            }
            
            
        } catch (IOException e) {
            //handle exception

        	Log.log("Json Config './wackrab.conf' Reading File Error");
        	return;
        }

        Log.log("== CONFIG : ==");

        Log.log("prefixWorld "+prefixWorld);
        Log.log("parentNameDefaultWorld "+parentNameDefaultWorld);
        Log.log("maxSubWorlds "+maxSubWorlds);
        Log.log("createWorlds "+createWorlds);	
        Log.log("commandInfoWorld "+commandInfoWorld);		
        
        Log.log("=============================================");
        Log.log("=============================================");
        
    	// instances
        
    	timerPlayers = new TimerPlayers();
    	Log.log("timerPlayers ok");
    	
		// créer la hiérarchie de monde (dans le serveur)
		
		hierachieWorlds = new HierarchieWorlds();
		ArrayList<WorldRelation> worlds = hierachieWorlds.CreateWorlds(
				this, maxSubWorlds, prefixWorld, parentNameDefaultWorld, createWorlds, false);

		 // test ajoute les mondes claimables en db:

		
		
		
		
		
		
		
		
		 //hierachieWorlds.AddClaimableWorldsOnDB(prefixWorld, maxSubWorlds);
		
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 // commands
         getCommand("test").setExecutor(new CommandTest(main));
         getCommand("wacktp").setExecutor(new CommandTeleport());
		 // cmd monde info
		 this.getCommand(commandInfoWorld).setExecutor(new CommandeMonde(this));
		 // help
	     getCommand("h").setExecutor(new CommandH());
	     getCommand("howto").setExecutor(new CommandHowTo());
		 // commande all world
	     getCommand("cmd").setExecutor(new MaCommandeExecutor(this));
	     // whereis Player
	     // logical path
	     getCommand("path").setExecutor(new CommandPath(this, false));
	     getCommand("pathfrom").setExecutor(new CommandPath(this, true));
	     getCommand("whereis").setExecutor(new CommandWhereIsPlayer(this));
	     getCommand("wackplayer").setExecutor(new CommandWackPlayer(this));
	     getCommand("gameruleworlds").setExecutor(new CommandGameRuleWorlds(this));
	     getCommand("allowplayer").setExecutor(new CommandAllowedPlayer(this));
	     getCommand("claim").setExecutor(new CommandClaimWorld(this));


		 getServer().getPluginManager().registerEvents(new MonPluginListener(), this);

		 Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

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
	             		JoueurInfo joueurInfoRead = lirePositionJoueur(p.getName());
	             		if(joueurInfoRead != null) {
	             			if(		(int)(joueurInfoRead.getX()) != (int)(x) ||
	             					(int)(joueurInfoRead.getY()) != (int)(y) ||
	             					(int)(joueurInfoRead.getZ()) != (int)(z) ||
	             					!joueurInfoRead.getNomMonde().toUpperCase().equals(worldplayer.getName().toUpperCase())
	             					) {
	             				
	             					JoueurInfo joueurInfoWrite = new JoueurInfo(p.getName(), x, y, z, worldplayer.getName());
	
	             					Location loc2 = new Location(worldplayer, x, y, z);
	             					enregistrerPositionJoueur(joueurInfoWrite, p.getName(), loc2);

	             			}
	             		}
	             		else {
	             			JoueurInfo joueurInfoWrite = new JoueurInfo(p.getName(), x, y, z, worldplayer.getName());
	             			
         					Location loc2 = new Location(worldplayer, x, y, z);
         					enregistrerPositionJoueur(joueurInfoWrite, p.getName(), loc2);
         				
	             		}
	             	
             		}

             		// allowed player 
             		
             		WorldRelation wr = hierachieWorlds.getWorldRelationByName(worldplayer.getName());

             		int sublevelplayer = getAllowedPlayerSubWorldLevel(p);
             		
         			if(wr != null) {
         				
         				if(wr.worldSubPosition > sublevelplayer) {
         					teleportPlayer(p, hierachieWorlds.getDefaultWorldRelation().getWorld());
         					
	         			}
         				
         				if(!wr.canEnterOn(main, p.getName())){
     						
     						// return
         					teleportPlayer(p, hierachieWorlds.getDefaultWorldRelation().getWorld());
     					}
         				
         			}
             		
             		// CHECK TELEPORTER
             		
             		Material blockType = getBlockTypeAtLocation(worldplayer, xi, yi, zi);
             		
             		// Téléporteurs
             		
					TimePlayerRelation prel = timerPlayers.getPlayerRelation(p, main);
             		
             		if(
             				blockType == Material.LEGACY_DIAMOND_BLOCK ||
             				blockType == Material.LEGACY_EMERALD_BLOCK ||
             				blockType == Material.LEGACY_GOLD_BLOCK ||
             				blockType == Material.DIAMOND_BLOCK ||
             				blockType == Material.EMERALD_BLOCK ||
             				blockType == Material.GOLD_BLOCK ) {

             			// teleporteur 2 ++
             			
             			// obsi
             			Material btX7 = getBlockTypeAtLocation(worldplayer, xi-3, yi, zi);
             			Material btX13 = getBlockTypeAtLocation(worldplayer, xi+3, yi, zi);
             			Material btZ7 = getBlockTypeAtLocation(worldplayer, xi, yi, zi-3);
             			Material btZ13 = getBlockTypeAtLocation(worldplayer, xi, yi, zi+3);
             			
             			Material btX8Z9 = getBlockTypeAtLocation(worldplayer, xi-2, yi, zi-1);
             			Material btX11Z8 = getBlockTypeAtLocation(worldplayer, xi+1, yi, zi-2);
             			Material btX11Z12 = getBlockTypeAtLocation(worldplayer, xi+1, yi, zi+2);
             			Material btX12Z11 = getBlockTypeAtLocation(worldplayer, xi+2, yi, zi+1);
             			Material btX12Z9 = getBlockTypeAtLocation(worldplayer, xi+2, yi, zi-1);
             			Material btX9Z8 = getBlockTypeAtLocation(worldplayer, xi-1, yi, zi-2);
             			Material btX8Z11 = getBlockTypeAtLocation(worldplayer, xi-2, yi, zi+1);
             			Material btX9Z12 = getBlockTypeAtLocation(worldplayer, xi-1, yi, zi+2);
             			
             			// teleporteur 1
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
             			
             			String cutsandstone_blockstr = "SANDSTONE";
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
                 	            btZ12.name().contains(obsidianblockstr))
         						) {

             					
             					//search the "from" world
         						for(WorldRelation w : worlds) {
         							
         							//boolean canGoToTheWorld = false;
         							if( !w.worldName.isEmpty() && w.worldName.toUpperCase().trim().equals(worldplayer.getName().toUpperCase().trim())) {
	         							//	canGoToTheWorld = true;
	         							//}

                     					boolean allowedToTeleport = false;

	 									// si monde existe pas: le créer
	 									
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
		         									if(prel.count(p)) {
		         										
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
	             									if(prel.count(p)) {

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
		         									if(prel.count(p)) {

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
             			

             			if(
         					(btX9Z9.name().contains(cutsandstone_blockstr) || btX9Z9.name().contains(quartzblockstr)) && 
         					(btX11Z9.name().contains(cutsandstone_blockstr) || btX11Z9.name().contains(quartzblockstr)) && 
         					(btX9Z11.name().contains(cutsandstone_blockstr) || btX9Z11.name().contains(quartzblockstr)) && 
         					(btX11Z11.name().contains(cutsandstone_blockstr) || btX11Z11.name().contains(quartzblockstr)) 
             			)
             			{
             				if(
             						((btX9.name().toString().contains("DIAMOND_BLOCK") && btX11.name().toString().contains("DIAMOND_BLOCK")) &&
                     						(btZ9.name().toString().contains("EMERALD_BLOCK") && btZ11.name().toString().contains("EMERALD_BLOCK"))) ||
             						((btZ9.name().toString().contains("DIAMOND_BLOCK") && btZ11.name().toString().contains("DIAMOND_BLOCK")) &&
                             				(btX9.name().toString().contains("EMERALD_BLOCK") && btX11.name().toString().contains("EMERALD_BLOCK")))
             				  )
             				{
             					if(	
		         						((btX8.name().contains(cutsandstone_blockstr) || btX8.name().contains(quartzblockstr)) &&
		         						(btX12.name().contains(cutsandstone_blockstr) || btX12.name().contains(quartzblockstr)) && 
		                 	        	(btZ8.name().contains(cutsandstone_blockstr) || btZ8.name().contains(quartzblockstr)) &&
		                 	            (btZ12.name().contains(cutsandstone_blockstr) || btZ12.name().contains(quartzblockstr)) ) 
	         					  )
             					{

                         			if(btX7.name().contains(obsidianblockstr) && 
                         					btX13.name().contains(obsidianblockstr) && 
                         					btZ7.name().contains(obsidianblockstr) && 
                         					btZ13.name().contains(obsidianblockstr) && 
                         					btX8Z9.name().contains(obsidianblockstr) && 
                         					btX11Z8.name().contains(obsidianblockstr) && 
                         					btX11Z12.name().contains(obsidianblockstr) && 
                         					btX12Z11.name().contains(obsidianblockstr) && 
                         					btX12Z9.name().contains(obsidianblockstr) && 
                         					btX9Z8.name().contains(obsidianblockstr) && 
                         					btX8Z11.name().contains(obsidianblockstr) && 
                         					btX9Z12.name().contains(obsidianblockstr)
                         			)
                         			{

	     								
	     								try {
	     								
		     								if(blockType == Material.LEGACY_GOLD_BLOCK || 
		                 							blockType == Material.GOLD_BLOCK) {
		
		     									
	         									if(prel.enableteleportmessage2 == true)
	         									{	

	         								    	try {
	         								    		UIHierarchieWorlds inv = main.uiHierarchiesWorlds.creerInventaire(main, p, "~ Choose World To Teleport ~");
	         											inv.openInventory(true);
	         										}
	         										catch (Exception ex){
	         											Log.log("exception: "+ex);
	         										}
		         									prel.enableteleportmessage2 = false;
	         									}
	         									
	         									
	         									
		                 					}
		     								
	     								}
	     								catch (Exception ex){
	     									Log.log("exception: "+ex);
	     								}
	     								
                         			}
             					}
             				}
             				
             			}
             			
             		}
             		else {

						prel = timerPlayers.getPlayerRelation(p, main);
						prel.reset();
						prel.enableteleportmessage = true;
						prel.enableteleportmessage2 = true;
             		}
             	}
             	
           		if(timeCounterSavePositionPlayer < 0) {
           			timeCounterSavePositionPlayer = timeCounterSavePositionPlayerMax;
           			
           		}
             }
             
         }, 40, 10);
                
	}
	
	public Block getHighestSolidBlock(int x, int z, World world) {
	    int maxY = world.getMaxHeight();

	    for (int y = maxY; y >= 0; y--) {
	        Block block = world.getBlockAt(x, y, z);

	        if (block.getType() != Material.AIR) {
	            return block;
	        }
	    }

	    return null;
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
		Log.log("Bye Plugin Wackrab.");
	}
	
	private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();
	
    public JoueurInfo lirePositionJoueur(String joueurname) {
    	 try {
            lock.lock();

            JoueurInfo joueurinfo = new JoueurInfo();
            
            joueurinfo.SelectDB(this, joueurname);

            return joueurinfo;
            
         } 
    	 catch (Exception e)
    	 {
        	 Log.log("Err 'lirePositionJoueur' : "+e.toString());
         } 
    	 finally 
    	 {
             lock.unlock();
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
          	
          	joueurinfo.InsertDB(main, joueurName);
    	  } 
    	  catch(Exception e) 
    	  {
    		  
          } 
    	  finally 
    	  {
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
            
        	//String dataJoueurInfo = joueurinfo.ToStringFile();            
            
        	enregistrerPositionJoueur(joueurinfo, joueur.getName(), nouvellePosition);
    	    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // Toujours libérer le verrou dans un bloc finally
        }
    }
    
    
    public boolean TeleportPlayerOnLastPositionFile(Player joueur) {

        JoueurInfo joueurInfo = lirePositionJoueur(joueur.getName());
        
        //String worldname = joueurInfo.getNomMonde();
        
        if(joueurInfo != null) {

	        double x = joueurInfo.getX();
	        double y = joueurInfo.getY();
	        double z = joueurInfo.getZ();
	        String nomDuMonde = joueurInfo.getNomMonde();

	        Log.log(joueur.getName() + " Reconnection : X=" + x + ", Y=" + y + ", Z=" + z + " dans le monde : " + nomDuMonde);
	    	 
	    	 // si le monde n'existe pas, monde normal
	    	 
	    	 World world = hierachieWorlds.getWorldByName(nomDuMonde);
    		 
	    	 if(world != null) {

		        Location nouvellePosition = new Location(world, x, y, z);
		        return joueur.teleport(nouvellePosition);
	    	 }
	    	 else 
	    	 {
	    		 Log.log("monde introuvable pour le joueur "+ joueurInfo.getNomJoueur());
	    		 return false;
	    	 }
	    	 
        } 
        return false;
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
