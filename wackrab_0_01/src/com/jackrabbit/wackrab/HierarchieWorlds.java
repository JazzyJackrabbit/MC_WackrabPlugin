package com.jackrabbit.wackrab;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import com.jackrabbit.worlds.MondeUtils;

public class HierarchieWorlds {

	static String labelDiamond = "L";
	static String labelEmeraud = "R";
	
	public WorldRelation getWorldRelationByName(String worldName) {
		 for(WorldRelation worldRel : worlds) 
        	if(worldRel.worldName.toUpperCase().equals(worldName.toUpperCase())) {
        		return worldRel;
        	}
		 return null;
	}
	
	static public String getWorldIdentifier(int worldBrotherPosition) {
    	if(worldBrotherPosition % 2 == 0)
    		return labelDiamond;
    	else
    		return labelEmeraud;
	}
	
	public ArrayList<WorldRelation> worlds = new ArrayList<WorldRelation>();
	
	public ArrayList<WorldRelation> CreateWorlds(
			Main main,
			int maxSubWorlds, 
			String prefixWorld, 
			String parentNameDefaultWorld, 
			boolean createWorldsOnServer,
			boolean forceRecreate
		) {

		worlds = new ArrayList<WorldRelation>();
		
        int t = 2;
        int wx = 0;
        int wy = 1;
        
        while(wy <= maxSubWorlds){
            
        	// CURRENT
        	
            int wxB = (int)Math.floor((double)wx / 2);
            String identifier = null;
            
            if (wx % 2 == 0)
                identifier = labelDiamond;
            else
                identifier = labelEmeraud;
            
            String labelWorld = wy + "_" + identifier + "_" + wxB;

            // FROM
            
            int wxFrom = (int)Math.floor((double)wx / 2);
            int wxBFrom = (int)Math.floor((double)wxFrom / 2);
            String identifierFrom = null;
            
            if (wxFrom % 2 == 0)
                identifierFrom = labelDiamond;
            else
                identifierFrom = labelEmeraud;
            
            String labelWorldFrom = (wy-1) + "_" + identifierFrom + "_" + wxBFrom;
            
            // SUB R
            
            int wxSub1 = (int)Math.floor((double)wx);
            String identifierSubR = labelEmeraud;
            String labelWorldSubR = (wy+1) + "_" + identifierSubR + "_" + wxSub1;
               
            // SUB L
            
            int wxSub2 = (int)Math.floor((double)wx);
            String identifierSubL = labelDiamond;
            String labelWorldSubL = (wy+1) + "_" + identifierSubL + "_" + wxSub2;
            
            // WORLD
            
            if(Bukkit.getWorld(prefixWorld + labelWorld) == null || forceRecreate) {

	            //if(createWorldsOnServer)
	            	//Bukkit.createWorld(WorldCreator.name(prefixWorld + labelWorld));
            	
	            if(createWorldsOnServer)
	            	MondeUtils.creerMonde(prefixWorld + labelWorld);
	            
	            
	            WorldRelation worldRelation = new WorldRelation();
	            worldRelation.worldLabel = labelWorld;
	            worldRelation.fromLabel = labelWorldFrom;
	            worldRelation.subRLabel = labelWorldSubR;
	            worldRelation.subLLabel = labelWorldSubL;
	            worldRelation.prefixWorld = prefixWorld;
	            
	            worldRelation.worldName = prefixWorld + labelWorld;
	            
	            worldRelation.fromWorldName = prefixWorld + labelWorldFrom;

	            worldRelation.subWorldLName = prefixWorld + labelWorldSubL;
	            worldRelation.subWorldRName = prefixWorld + labelWorldSubR;
	            
	            worldRelation.worldSubPosition = wy;
	            worldRelation.worldBrotherPosition = wxB;
	            worldRelation.worldIdentifier = identifier;
	            
	            worlds.add(worldRelation);
	            
	            System.out.println("> Generating World, with Label: " + labelWorld + ". From World: "+ labelWorldFrom);
	            
	            if(wx >= t-1)
	            {
	                t *= 2;
	                wx = 0;
	                wy += 1;
	            }
	            else
	                wx++;
	            
            }
            
        }

        // default 
		World defaultWorld = Bukkit.getServer().getWorlds().get(0);	
		
		WorldRelation worldRelationDefault = new WorldRelation();
        worldRelationDefault.worldLabel = defaultWorld.getName();
        worldRelationDefault.fromLabel = parentNameDefaultWorld;
        worldRelationDefault.subRLabel = "1_R_0";
        worldRelationDefault.subLLabel = "1_L_0";
        worldRelationDefault.prefixWorld = prefixWorld;
        
        worldRelationDefault.worldName = defaultWorld.getName();

        worldRelationDefault.fromWorldName = prefixWorld + parentNameDefaultWorld;
		
        worldRelationDefault.subWorldLName = prefixWorld + "1_L_0";
        worldRelationDefault.subWorldRName = prefixWorld + "1_R_0";
        		
        worldRelationDefault.worldSubPosition = 0;
        worldRelationDefault.worldBrotherPosition = 0;
        worldRelationDefault.worldIdentifier = labelDiamond;
        
		worlds.add(worldRelationDefault);
		
		// returns to default..
		
		for(WorldRelation wr : worlds) {
			
			// returns to default world By Gold
			if(wr.worldLabel.equals("1_R_0")) {
				wr.fromLabel = worldRelationDefault.worldLabel;
				wr.worldName = prefixWorld + "1_R_0";
				wr.fromWorldName = defaultWorld.getName();
			}
			if(wr.worldLabel.equals("1_L_0")) {
				wr.fromLabel = worldRelationDefault.worldLabel;
				wr.worldName = prefixWorld + "1_L_0";
				wr.fromWorldName = defaultWorld.getName();
			}

			// returns to default By Diamond (L)
			if(wr.worldLabel.equals(parentNameDefaultWorld)) {
				wr.subLLabel = worldRelationDefault.worldLabel;
				wr.subWorldLName = defaultWorld.getName();
			}
		}
		
		// 
		
        System.out.println("OK");
        
		return worlds;
	}
	
	public World getWorldByName(String nm) {
		
		for(WorldRelation worldRelation : worlds) {
			
			if(nm.toUpperCase().trim().equals(worldRelation.worldName.toUpperCase().trim())) {

				System.out.println("> HierachieWorlds : getWorldByName ( String nm ) :   return " + worldRelation.worldName);
				
				return worldRelation.getWorld();
				
			}
		}
		
		System.out.println("> HierachieWorlds : getWorldByName ( String nm ) :   return null...");
        
		return null;
	}
	
}