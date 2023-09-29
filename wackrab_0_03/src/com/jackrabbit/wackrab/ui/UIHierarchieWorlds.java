package com.jackrabbit.wackrab.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jackrabbit.wackrab.Main;
import com.jackrabbit.wackrab.WorldRelation;
import com.jackrabbit.wackrab.commands.CommandPath;

public class UIHierarchieWorlds implements Listener {
	
	// ouvrir avec:    joueur.openInventory(creerInventaire());
	
	public Player player;
	public Inventory inventaire;

	WorldRelation currentworldRelation = null;
	WorldRelation playerWorldRelation = null;

	WorldRelation currentworldRelationUp = null;
	WorldRelation currentworldRelationUpUp = null;
	WorldRelation currentworldRelationDL = null;
	WorldRelation currentworldRelationDR = null;
	
	public boolean isVerrouille = true;
	
	boolean isEnableTeleportation = false;
	
	Main main;
	
	public UIHierarchieWorlds(Main main, Player player) {
		this.main = main;
		this.player = player;
	}
	

	boolean isOpeningInventary = false;
	Lock lockOpeningInventary = new ReentrantLock();;
	
	public void openInventory(boolean isEnableTeleportation) {

		try {
			this.isEnableTeleportation = isEnableTeleportation;
			
			lockOpeningInventary.lock();

			if(isOpeningInventary != true) {
				isOpeningInventary = true;

				if(inventaire != null) {
					if(player != null) {
						try {
							World w = player.getWorld();
		
							if(w != null) {
								String wn = w.getName();
								playerWorldRelation = main.hierachieWorlds.getWorldRelationByName(wn);
								currentworldRelation = main.hierachieWorlds.getWorldRelationByName(wn);
								System.out.println(currentworldRelation);
							}
	
							
							player.openInventory(inventaire);
							
							
		
					    	refreshItems();
						}catch(Exception ex) {
							System.out.println("> Exception: "+ ex.toString());
						}
					}
				}
			}	 
		}catch(Exception ex) {

		}finally {
			isOpeningInventary = false;
			lockOpeningInventary.unlock();
		}
	}

    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() == null) {
            ItemStack itemClique = event.getCurrentItem();
            
            if (itemClique != null) {
                ItemMeta meta = itemClique.getItemMeta();
                if (meta != null) {
                    List<String> lore = meta.getLore();
                    if (lore != null) {
                    	//event.setCancelled(true);
                        ClickItem(event, lore.get(0));
                        // Annuler le déplacement de l'item (pour éviter de laisser l'item être déplacé)
                        
                    }
                }
            }
        }
    	event.setCancelled(true);
    }
    

    public void subUp() {
    	if(currentworldRelation != null)
    		if(currentworldRelation.fromWorldName != null) {
    			currentworldRelation = 
    					main.hierachieWorlds.getWorldRelationByName(currentworldRelation.fromWorldName);
    		}
    } 
    public void subDownLeft() {
    	if(currentworldRelation != null)
    		if(currentworldRelation.subWorldLName != null) {
    			currentworldRelation = 
    					main.hierachieWorlds.getWorldRelationByName(currentworldRelation.subWorldLName);
    		}
    }
    public void subDownRight() {
    	if(currentworldRelation != null)
    		if(currentworldRelation.subWorldRName != null) {
    			currentworldRelation = 
    					main.hierachieWorlds.getWorldRelationByName(currentworldRelation.subWorldRName);
    		}
    }
    
    public void ClickItem(InventoryClickEvent event, String lore) {

    	System.out.println("Click");
    	System.out.println(lore);
    	System.out.println("Click");
    	
    	if(lore.equals("Wackrab_HierarchieWorld_REFRESH")) {
    		try {
	    		currentworldRelation =
	    				main.hierachieWorlds.getWorldRelationByName(playerWorldRelation.worldName);
    		}catch(Exception ex){
    			
    		}
    	}
    	if(lore.equals("Wackrab_HierarchieWorld_ITEM_H_UP_L")) 
    	{
    		subUp();
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_ITEM_H_UP_R")) 
    	{
    		subUp();
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_ITEM_H_MIDDLE")) 
    	{
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_ITEM_H_DOWN_L")) 
    	{
    		subDownLeft();
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_ITEM_H_DOWN_R")) 
    	{
    		subDownRight();
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_POSITION_U")) 
    	{
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_POSITION_M")) 
    	{
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_POSITION_D")) 
    	{
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_INFO1")) 
    	{
    		if(currentworldRelationUp != null) {
        		CommandPath.ShowPath(main, player, playerWorldRelation, currentworldRelationUp );
    		}
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_INFO2")) 
    	{
    		if(currentworldRelation != null) {
        		CommandPath.ShowPath(main, player, playerWorldRelation, currentworldRelation );
    		}
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_INFO3_L")) 
    	{
    		if(currentworldRelationDL != null) {
        		CommandPath.ShowPath(main, player, playerWorldRelation, currentworldRelationDL );
    		}
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_INFO3_R")) 
    	{
    		if(currentworldRelationDR != null) {
        		CommandPath.ShowPath(main, player, playerWorldRelation, currentworldRelationDR );
    		}
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_INFO4")) 
    	{
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_INFO5")) 
    	{
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_INFO6")) 
    	{
    		
    	};
    	if(lore.equals("Wackrab_HierarchieWorld_TELEPORT")) {
    		
    		try {
    			
    			
	    		// get current world
	    		WorldRelation currentWR = currentworldRelation;
	    		WorldRelation playerWR = playerWorldRelation;
	    		
	    		int xpneeded = getPathXp(currentWR, playerWR);
	    		
	    		int xpplayer = player.getTotalExperience();
	    		
				
				int finalexp = xpplayer - xpneeded;
				
				if(xpneeded <= 0) 
				{

				}
				else {
					if(finalexp >= 0 ) {
						
						// teleportation
						World w = currentWR.getWorld();
						if(w != null) {
							//if(currentWR.isAllowedAccessDBRequest(main, false) == 1)
								//if(currentWR.get_allowedPlayerEnterOn(main, player.getName()))

		             		//int sublevelplayer = main.getAllowedPlayerSubWorldLevel(player);
							
		             		
						
							if(currentWR.canEnterOn(main, player.getName())) {
								if(main.teleportPlayer(player, w)) {
									
									player.sendMessage("Done ! You just used " + xpneeded +" XPs.  ("+finalexp+")");

									// xp
									player.giveExp(-xpneeded);
									player.updateInventory();
								};
						
							}
							else
								player.sendMessage("Not allowed..");

								
							//}
							//else
							//	player.sendMessage("Not allowed.. (sub position too far)");
							
						}
					}
					else
						player.sendMessage("You need " + xpneeded +" to teleport you (actually = "+xpplayer+")");
				}
    		}
    		catch(Exception ex) {
    			
    		}
    	}
    	
    	refreshItems();
    	
    }

    public int getPathXp(WorldRelation wrfrom, WorldRelation wrto) {
    	

		if(wrfrom.worldName.toUpperCase().equals(wrto.worldName.toUpperCase()))
			return 0;
		
		
		int xpneeded = 0;
		
		float xpMultiplierSub = 1.2f;
		float xpMultiplierChild = 1.05f;
		
		float xpCounterSub = 15;
		float xpCounterChild = 15;
		
		// get xp

		int subDelta = wrto.worldSubPosition - wrfrom.worldSubPosition; // isnegative = ^   ispositif = v   
		int subDeltaAbs = Math.abs(subDelta);

		int brotherDefault = wrfrom.worldBrotherPosition;
		int brotherFinal = wrto.worldBrotherPosition;
		
		// sub
		if(subDelta > 0) {

			for(int i = 0; i < subDeltaAbs; i++) {
				brotherDefault *= 2;
    			xpneeded += xpCounterSub;
    			xpneeded *= xpMultiplierSub;
			}
			
			//si brother final a droite,
			//brotherDefault *= 2;
		}
		
		if(subDelta < 0) {
			
			for(int i = 0; i < subDeltaAbs; i++) {
				brotherDefault = (int)Math.floor(brotherDefault/2);
    			xpneeded += xpCounterSub;
			}
		}
		
		int brotherDelta = 0;

		// no pair,  it's 1 by 1
		brotherDefault = brotherDefault / 2;
		if(wrfrom.worldIdentifier.equals("R")) brotherDefault += 1;

		// no pair,  it's 1 by 1
		brotherFinal = brotherFinal / 2;
		if(wrto.worldIdentifier.equals("R")) brotherFinal += 1;
		
		
		// broths
		brotherDelta = brotherFinal - brotherDefault;
		brotherDelta = (int)Math.floor(brotherDelta);
		
		// min 100
		xpneeded = Math.max(xpneeded, (int)xpCounterChild*2);
		
		return xpneeded;
				
    }
    
    public static int convertTotalExperienceToLevels(int totalExperience) {
        int level = 0;
        int exp = 0;
        int requiredExperience;

        while (totalExperience >= (requiredExperience = level >= 16 ? 17 * level - 6 : 3 * level + 1)) {
            level++;
            totalExperience -= requiredExperience;
        }

        return level;
    }

    
    public void refreshItems() {
    	try {
    		System.out.println(playerWorldRelation);
    		System.out.println(currentworldRelation);
	    	if(playerWorldRelation == null) return;
	    	if(currentworldRelation == null) return;

    		System.out.println("-");
    		System.out.println("-");
	
	    	currentworldRelationUp = null;
	    	currentworldRelationUpUp = null;
	    	currentworldRelationDL = null;
	    	currentworldRelationDR = null;
	
	    	currentworldRelationUp = main.hierachieWorlds.getWorldRelationByName(
	    			currentworldRelation.fromWorldName);
	    	
	    	if(currentworldRelationUp != null)
		    	currentworldRelationUpUp = main.hierachieWorlds.getWorldRelationByName(
		    			currentworldRelationUp.fromWorldName);
		
	    	currentworldRelationDL = main.hierachieWorlds.getWorldRelationByName(
	    			currentworldRelation.subWorldLName);
	
	    	currentworldRelationDR = main.hierachieWorlds.getWorldRelationByName(
	    			currentworldRelation.subWorldRName);
	
	    	// items

	    	int s = 9;
	    	int s2 = 9;

	    	for(int i = 0; i <= (9*5)-1; i++)
	    		inventaire.setItem(i, null);

	    	// lines
	    	
	    	{
				ItemStack is = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		        ItemMeta meta = is.getItemMeta();
		        List<String> lore = new ArrayList<>();
		        lore.add("Wackrab_HierarchieWorld_BORDER");
		        meta.setLore(lore);
		        meta.setDisplayName("PVP ZONE");
		        is.setItemMeta(meta);
		        
		    	for(int i = 0; i <= (9)-1; i++)
		    		inventaire.setItem(i, is);
		    	
		    	for(int i = s+s+s+s2; i <= (9*5)-1; i++)
		    		inventaire.setItem(i, is);
		    	
		    	
	    	}
	    	
	    	
			
			{
				ItemStack is = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		        ItemMeta meta = is.getItemMeta();
		        List<String> lore = new ArrayList<>();
		        lore.add("Wackrab_HierarchieWorld_PVP_ZONE");
		        meta.setLore(lore);
		        meta.setDisplayName("PVP ZONE");
		        is.setItemMeta(meta);
			
		        
				for(int i = 0; i <= 8; i++) {
					if(currentworldRelationUp != null) {
						if(currentworldRelationUp.worldSubPosition >= main.enablePvpSubWorlds) 
						{
							if(currentworldRelationUp.worldSubPosition < main.maxSubWorlds+1) {
								//if(inventaire.getItem(i) == null || inventaire.getItem(i).getType() == Material.AIR );
									inventaire.setItem(i+s2, is);
							}
						}
					}
				}
				
				for(int i = 9; i <= 17; i++) {
					if(currentworldRelation != null) {
						if(currentworldRelation.worldSubPosition >= main.enablePvpSubWorlds) 
						{
							if(currentworldRelation.worldSubPosition < main.maxSubWorlds+1) {
								//if(inventaire.getItem(i) == null || inventaire.getItem(i).getType() == Material.AIR );
									inventaire.setItem(i+s2, is);
							}
						}
					}
				}
				
				for(int i = 18; i <= 26; i++) {
					if(currentworldRelationDR != null) {
						if(currentworldRelationDR.worldSubPosition >= main.enablePvpSubWorlds) 
						{
							if(currentworldRelationDR.worldSubPosition < main.maxSubWorlds+1) {
								//if(inventaire.getItem(i) == null || inventaire.getItem(i).getType() == Material.AIR );
									inventaire.setItem(i+s2, is);
							}
						}
					}
					else if(currentworldRelationDL != null) {
						if(currentworldRelationDL.worldSubPosition >= main.enablePvpSubWorlds) 
						{
							if(currentworldRelationDL.worldSubPosition < main.maxSubWorlds+1) {
								//if(inventaire.getItem(i) == null || inventaire.getItem(i).getType() == Material.AIR );
									inventaire.setItem(i+s2, is);
							}
						}
					}
				}	
								
			}
			
    		{
	    		ItemStack is = new ItemStack(Material.COBWEB);
		        ItemMeta meta = is.getItemMeta();
		        List<String> lore = new ArrayList<>();
		        lore.add("Wackrab_HierarchieWorld_REFRESH");
		        meta.setLore(lore);
		        meta.setDisplayName("REFRESH");
    	        is.setItemMeta(meta);
				inventaire.setItem(0+s2, is);
    		}
			
    		
			
			
			
	    	
	    	
	    	
	    	
	    	
	    	
	    	//top line
	    	if(currentworldRelationUp != null) {
	
	    	
	    		int wIdentifier = -1;
	    		if(currentworldRelationUpUp != null) // ta un parrent
	    		{
		    		if(currentworldRelationUp.worldIdentifier.equals("L"))
		    			wIdentifier = 0;
		    		else if(currentworldRelationUp.worldIdentifier.equals("R"))
		    			wIdentifier = 1;
	    		}
	    		
	    		//if(currentworldRelationUp.worldIdentifier != null)
	    		//	wIdentifier = currentworldRelationUp.worldBrotherPosition % 2;
	    		  
	    		if(wIdentifier == 0) 
	    		{
	    			ItemStack is = new ItemStack(Material.DIAMOND_BLOCK);
	    			//if(wIdentifierUpUp.equals("G"))
	    			//	is = new ItemStack(Material.QUARTZ_BLOCK);
	    			
	    	        ItemMeta meta = is.getItemMeta();
	    	        List<String> lore = new ArrayList<>();
	    	        lore.add("Wackrab_HierarchieWorld_ITEM_H_UP_L");
	    	        meta.setLore(lore);
	    	        meta.setDisplayName(currentworldRelationUp.worldName);
	    	        is.setItemMeta(meta);
	    			inventaire.setItem(4+s2, is);
	    		}
	    		else if(wIdentifier == 1)
	    		{
	    			ItemStack is = new ItemStack(Material.EMERALD_BLOCK);
	    			//if(wIdentifierUpUp.equals("G"))
	    			//	is = new ItemStack(Material.QUARTZ_BLOCK);
	    			
	    	        ItemMeta meta = is.getItemMeta();
	    	        List<String> lore = new ArrayList<>();
	    	        lore.add("Wackrab_HierarchieWorld_ITEM_H_UP_R");
	    	        meta.setLore(lore);
	    	        meta.setDisplayName(currentworldRelationUp.worldName);
	    	        is.setItemMeta(meta);
	    			inventaire.setItem(4+s2, is);
	    		}
	    		else {
	    			ItemStack is = new ItemStack(Material.GOLD_BLOCK);
	    			//if(wIdentifierUpUp.equals("G"))
	    			//	is = new ItemStack(Material.QUARTZ_BLOCK);
	    			
	    	        ItemMeta meta = is.getItemMeta();
	    	        List<String> lore = new ArrayList<>();
	    	        lore.add("Wackrab_HierarchieWorld_ITEM_H_UP_R");
	    	        meta.setLore(lore);
	    	        meta.setDisplayName(currentworldRelationUp.worldName);
	    	        is.setItemMeta(meta);
	    			inventaire.setItem(4+s2, is);
	    		}
			}
	    	
	    	
	
			if(currentworldRelationUp != null)
			{
				{
		    		ItemStack is = new ItemStack(Material.STONE_BUTTON);
		    		if(currentworldRelationUp.worldName.equals(playerWorldRelation.worldName))
			    		is = new ItemStack(Material.CHERRY_BUTTON);
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_POSITION_U");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelationUp.worldName);
			        is.setItemMeta(meta);
					inventaire.setItem(6+s2, is);
					inventaire.setItem(2+s2, is);
				}
				{
					if(currentworldRelationUp.isClaimableWorld(main)) {
			    		ItemStack is = new ItemStack(Material.BAMBOO_TRAPDOOR);
			    		
				        ItemMeta meta = is.getItemMeta();
				        List<String> lore = new ArrayList<>();
				        lore.add("Wackrab_HierarchieWorld_ITEM_CLAIMABLE");
				        meta.setLore(lore);
				        meta.setDisplayName(currentworldRelationUp.worldName);
				        is.setItemMeta(meta);
						inventaire.setItem(6+s2, is);
						inventaire.setItem(2+s2, is);
			    	}
			    	if(currentworldRelationUp.isClaim(main, false)) {
			    		ItemStack is = new ItemStack(Material.IRON_TRAPDOOR);
			    		
				        ItemMeta meta = is.getItemMeta();
				        List<String> lore = new ArrayList<>();
				        lore.add("Wackrab_HierarchieWorld_ITEM_CLAIMED");
				        meta.setLore(lore);
				        meta.setDisplayName(currentworldRelationUp.worldName);
				        is.setItemMeta(meta);
						inventaire.setItem(6+s2, is);
						inventaire.setItem(2+s2, is);
			    	}
				}
			}
	 
	    	// middle line
	    	if(currentworldRelation != null) {
	
	    		int wIdentifier = -1;
	    		if(currentworldRelationUp != null) // ta un parrent
	    		{
		    		if(currentworldRelation.worldIdentifier.equals("L"))
		    			wIdentifier = 0;
		    		else if(currentworldRelation.worldIdentifier.equals("R"))
		    			wIdentifier = 1;
	    		}
	    		if(wIdentifier == 0){
	    			
		    		ItemStack is = new ItemStack(Material.DIAMOND_BLOCK);
		    			
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_ITEM_H_MIDDLE");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelation.worldName);
	    	        is.setItemMeta(meta);
					inventaire.setItem(4+s+s2, is);
		    		
	    		}
	    		else if (wIdentifier == 1) {
	    			
	    			ItemStack is = new ItemStack(Material.EMERALD_BLOCK);
		    			
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_ITEM_H_MIDDLE");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelation.worldName);
	    	        is.setItemMeta(meta);
					inventaire.setItem(4+s+s2, is);
		    		
	    		}
	    		else
	    		{
		    		ItemStack is = new ItemStack(Material.GOLD_BLOCK);
		    		//if(wIdentifier == 0) 
		    		//	is = new ItemStack(Material.DIAMOND_BLOCK);
		    		//else if(wIdentifier == 1) 
		    		//	is = new ItemStack(Material.EMERALD_BLOCK);
		    		//else
		    		//	is = new ItemStack(Material.GOLD_BLOCK);
		    			
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_ITEM_H_MIDDLE");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelation.worldName);
	    	        is.setItemMeta(meta);
					inventaire.setItem(4+s+s2, is);
	    		}
	    		
	    		
				{
		    		ItemStack is = new ItemStack(Material.STONE_BUTTON);
		    		if(currentworldRelation.worldName.equals(playerWorldRelation.worldName))
			    		is = new ItemStack(Material.CHERRY_BUTTON);
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_POSITION_M");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelation.worldName);
	    	        is.setItemMeta(meta);
					inventaire.setItem(6+s+s2, is);
					inventaire.setItem(2+s+s2, is);
	    		}
				
				if(currentworldRelation.isClaimableWorld(main)) {
		    		ItemStack is = new ItemStack(Material.BAMBOO_TRAPDOOR);
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_ITEM_CLAIMABLE");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelation.worldName);
			        is.setItemMeta(meta);
					inventaire.setItem(6+s+s2, is);
					inventaire.setItem(2+s+s2, is);
		    	}
		    	if(currentworldRelation.isClaim(main, false)) {
		    		ItemStack is = new ItemStack(Material.IRON_TRAPDOOR);
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_ITEM_CLAIMED");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelation.worldName);
			        is.setItemMeta(meta);
					inventaire.setItem(6+s+s2, is);
					inventaire.setItem(2+s+s2, is);
		    	}

		    	
	    	}
	    	
	    	
	    
			// bottom line
	    	if(currentworldRelationDL != null) {
	
	    		ItemStack is = new ItemStack(Material.DIAMOND_BLOCK);
	
		        ItemMeta meta = is.getItemMeta();
		        List<String> lore = new ArrayList<>();
		        lore.add("Wackrab_HierarchieWorld_ITEM_H_DOWN_L");
		        meta.setLore(lore);
		        meta.setDisplayName(currentworldRelationDL.worldName);
		        is.setItemMeta(meta);
				inventaire.setItem(3+s+s+s2, is);
		
	    	}
	    	if(currentworldRelationDR != null) {
	
	    		ItemStack is = new ItemStack(Material.EMERALD_BLOCK);
	    		
		        ItemMeta meta = is.getItemMeta();
		        List<String> lore = new ArrayList<>();
		        lore.add("Wackrab_HierarchieWorld_ITEM_H_DOWN_R");
		        meta.setLore(lore);
		        meta.setDisplayName(currentworldRelationDR.worldName);
		        is.setItemMeta(meta);
				inventaire.setItem(5+s+s+s2, is);
	    	}

	    	
	    	if(currentworldRelationDL != null || currentworldRelationDR != null) {

	    		{
		    		ItemStack is = new ItemStack(Material.STONE_BUTTON);
	
		    		if(currentworldRelationDL.worldName.equals(playerWorldRelation.worldName))
			    		is = new ItemStack(Material.CHERRY_BUTTON);
		    			    		
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_POSITION_D");
			        meta.setLore(lore);
			        meta.setDisplayName("[...]");
			        is.setItemMeta(meta);
					inventaire.setItem(2+s+s+s2, is);
	    		}

	    		{
		    		ItemStack is = new ItemStack(Material.STONE_BUTTON);
		    		
		    		if(currentworldRelationDR.worldName.equals(playerWorldRelation.worldName))
			    		is = new ItemStack(Material.CHERRY_BUTTON);
		    			    		
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_POSITION_D");
			        meta.setLore(lore);
			        meta.setDisplayName("[...]");
			        is.setItemMeta(meta);
					inventaire.setItem(6+s+s+s2, is);
	    		}
	    		

	    		if(currentworldRelationDL.isClaimableWorld(main)) {
		    		ItemStack is = new ItemStack(Material.BAMBOO_TRAPDOOR);
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_ITEM_CLAIMABLE");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelationDL.worldName);
			        is.setItemMeta(meta);
					inventaire.setItem(2+s+s+s2, is);
		    	}
		    	if(currentworldRelationDL.isClaim(main, false)) {
		    		ItemStack is = new ItemStack(Material.IRON_TRAPDOOR);
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_ITEM_CLAIMED");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelationDL.worldName);
			        is.setItemMeta(meta);
					inventaire.setItem(2+s+s+s2, is);
		    	}

	    		if(currentworldRelationDR.isClaimableWorld(main)) {
		    		ItemStack is = new ItemStack(Material.BAMBOO_TRAPDOOR);
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_ITEM_CLAIMABLE");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelationDR.worldName);
			        is.setItemMeta(meta);
					inventaire.setItem(6+s+s+s2, is);
		    	}
		    	if(currentworldRelationDR.isClaim(main, false)) {
		    		ItemStack is = new ItemStack(Material.IRON_TRAPDOOR);
		    		
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_ITEM_CLAIMED");
			        meta.setLore(lore);
			        meta.setDisplayName(currentworldRelationDR.worldName);
			        is.setItemMeta(meta);
					inventaire.setItem(6+s+s+s2, is);
		    	}
		    	
	    		
	    	}
	    	
	    	
	
	    	// infos
	
	    	if(currentworldRelationUp != null) {
	    		{
		    		ItemStack is = new ItemStack(Material.ACACIA_DOOR);
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_INFO1");
			        meta.setLore(lore);
			        meta.setDisplayName("GOTO " +currentworldRelationUp.worldName);
	    	        is.setItemMeta(meta);
					inventaire.setItem(7+s2, is);
					inventaire.setItem(1+s2, is);
	    		}
	    		{
		    		ItemStack is = new ItemStack(Material.NETHER_STAR);
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_INFO4");
			        meta.setLore(lore);
			        meta.setDisplayName("TELEPORT TO " +currentworldRelationUp.worldName);
	    	        is.setItemMeta(meta);
					//inventaire.setItem(8, is);
	    		}
	    	}
	
	    	if(currentworldRelation != null) {
	    		{
		    		ItemStack is = new ItemStack(Material.ACACIA_DOOR);
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_INFO2");
			        meta.setLore(lore);
			        meta.setDisplayName("GOTO " +currentworldRelation.worldName);
	    	        is.setItemMeta(meta);
					inventaire.setItem(7+s+s2, is);
					inventaire.setItem(1+s+s2, is);
	    		}
	    		
	    		if(this.isEnableTeleportation == true)
	    		{
	    			//if(isEnableTeleportation) {
			    		ItemStack is = new ItemStack(Material.NETHER_STAR);
				        ItemMeta meta = is.getItemMeta();
				        List<String> lore = new ArrayList<>();
				        lore.add("Wackrab_HierarchieWorld_TELEPORT");
				        meta.setLore(lore);
				        
				        String textToWorldnXp = "";
				        int xpneeded = getPathXp(playerWorldRelation, currentworldRelation);
			    		int xpplayer = player.getTotalExperience();
			    		
			    		//int levelxp = convertTotalExperienceToLevels(xpneeded);
			    		
				        textToWorldnXp = "Cost " + xpneeded + "xp  "; //levelxp +"LVL, "+
				        
				        if(xpplayer >= xpneeded) {
				        	// ok
				        	textToWorldnXp = ChatColor.GREEN + textToWorldnXp;
				        }
				        else {
				        	// need
				        	textToWorldnXp = ChatColor.RED + textToWorldnXp;
				        }
				        
				        textToWorldnXp += ChatColor.GRAY + "(" +xpplayer + "xp)  ~  GO TO: " +  ChatColor.GOLD +currentworldRelation.worldName ;
				        
				        meta.setDisplayName(textToWorldnXp);
		    	        is.setItemMeta(meta);
						inventaire.setItem(8+s+s2, is);
	    			//}
	    		}
	    	}
	    	
	    	if(currentworldRelationDL != null || currentworldRelationDR != null) {
	    		{
		    		ItemStack is = new ItemStack(Material.ACACIA_DOOR);
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_INFO3_R");
			        meta.setLore(lore);
			        meta.setDisplayName("GOTO");
	    	        is.setItemMeta(meta);
					inventaire.setItem(7+s+s+s2, is);
	    		}{
		    		ItemStack is = new ItemStack(Material.ACACIA_DOOR);
			        ItemMeta meta = is.getItemMeta();
			        List<String> lore = new ArrayList<>();
			        lore.add("Wackrab_HierarchieWorld_INFO3_L");
			        meta.setLore(lore);
			        meta.setDisplayName("GOTO");
	    	        is.setItemMeta(meta);
					inventaire.setItem(1+s+s+s2, is);
	    		}
	    		{
	        		ItemStack is = new ItemStack(Material.NETHER_STAR);
	    	        ItemMeta meta = is.getItemMeta();
	    	        List<String> lore = new ArrayList<>();
	    	        lore.add("Wackrab_HierarchieWorld_INFO6");
	    	        meta.setLore(lore);
	    	        meta.setDisplayName("[...]");
	    	        is.setItemMeta(meta);
	    			//inventaire.setItem(8+s+s, is);
	    		}
	    	}
	    	
		}
		catch(Exception ex) {
			
		}
	
    }
    
}
