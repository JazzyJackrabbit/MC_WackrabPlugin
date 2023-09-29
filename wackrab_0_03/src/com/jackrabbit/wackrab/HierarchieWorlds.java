package com.jackrabbit.wackrab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.jackrabbit.wackrab.sqlite.DbDataWorld;
import com.jackrabbit.wackrab.utils.Log;
import com.jackrabbit.worlds.MondeUtils;

public class HierarchieWorlds {

	static String labelDiamond = "L";
	static String labelEmeraud = "R";
	
	public WorldRelation getWorldBySubIndBro(String prefix, int sub, String ind, int bro) {
		return getWorldRelationByName(prefix+sub+ "_"+ind+"_"+bro);
	}
	
	public WorldRelation getWorldRelationByName(String worldName) {
		 for(WorldRelation worldRel : worlds) 
        	if(worldRel.worldName.toUpperCase().equals(worldName.toUpperCase())) {
        		return worldRel;
        	}
		 return null;
	}
	public WorldRelation getDefaultWorldRelation() {
		World defaultWorld = Bukkit.getServer().getWorlds().get(0);	
		for(WorldRelation worldRel : worlds) 
        	if(worldRel.worldName.toUpperCase().equals(defaultWorld.getName().toUpperCase())) {
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

            	// c'est ici que je créer le monde si il n'existe pas.
            	
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
	            
	            Log.log("> Generating World: " + prefixWorld + labelWorld + ". From World: "+ prefixWorld +labelWorldFrom);
	            
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
				
				Log.log("> Link World: " + wr.worldName + ". From World: "+ wr.fromWorldName);
			}
			if(wr.worldLabel.equals("1_L_0")) {
				wr.fromLabel = worldRelationDefault.worldLabel;
				wr.worldName = prefixWorld + "1_L_0";
				wr.fromWorldName = defaultWorld.getName();

				Log.log("> Link World: " + wr.worldName + ". From World: "+ wr.fromWorldName);
			}

			// returns to default By Diamond (L)
			if(wr.worldLabel.equals(parentNameDefaultWorld)) {
				wr.subLLabel = worldRelationDefault.worldLabel;
				wr.subWorldLName = defaultWorld.getName();

				Log.log("> Link World: " + wr.worldName + ". From World: "+ wr.fromWorldName);
			}
		}
		
		// 
		Log.log("OK");
        
		return worlds;
	}

    public List<DbDataWorld> getRandomClaimableWorldRelation(Main main, int numOfClaimableWorlds){
    	 
    	ArrayList<DbDataWorld> selectClaimableWorlds = main.DTB.worlds.selectClaimableWorlds(main);
    	ArrayList<DbDataWorld> shuffle = new ArrayList<DbDataWorld>();

	    Collections.shuffle(selectClaimableWorlds);
	    
    	int counter = 1;
    	for(DbDataWorld dw : selectClaimableWorlds) {
    		String nameworld = dw.worldName;
    		
    		//WorldRelation wr = getWorldRelationByName(nameworld);
    		Log.log(nameworld);
    		if (dw != null && dw.isclaimable == 1 && dw.isClaim == 0) {
    			if(counter <= numOfClaimableWorlds) {
	    			shuffle.add(dw);
	    			counter++;
    			}
    		}
    	}
    	
    	Collections.shuffle(shuffle);
	    
    	
    	
	    //Collections.shuffle(shuffle);

    	//List<WorldRelation> subItems = shuffle.subList(0, Math.min(shuffle.size(),numOfClaimableWorlds+1));
    	
    	return shuffle;
    }
    
	public boolean AddClaimableWorldsOnDB(String prefixWorld, int maxSubWorlds) {
		boolean isok = false;
		
		try {
			
			int startSb = 6;
			
			int totalcounter = 1;
			
			
			ArrayList<WorldRelation> selectedWorldsNm = new ArrayList<WorldRelation>();
			
			Log.log("==== GENERATING CLAIMABLE WORLDS ====");
			
			for(int is = startSb; is <  maxSubWorlds -((0)); is++) {
				
				int broMax = (int)Math.floor(Math.pow(2, is)) / 2;
				
				int from0 = (is - startSb);
				
				int emptyPart = (int)broMax / 2;
				
				emptyPart /= 8;
				
				//emptyPart = emptyPart + from0 * 4;
				
				int usedPart = emptyPart;
				
				int broCounter = usedPart;
				while(broCounter > 0) {
					
					int broRdm = (int)Math.floor(Math.random() * broMax);
					String indRdm = Math.round(Math.random()) == 0 ? "L" : "R";
					
					WorldRelation wr = getWorldBySubIndBro(prefixWorld, is, indRdm, broRdm);

					
					// add world, but check his parent & grand parents
					
					boolean hasGrandParent = false;
					for(WorldRelation wfold : selectedWorldsNm) {
						if(wfold.worldSubPosition < wr.worldSubPosition && hasGrandParent == false) {
							WorldRelation wfcrosser = getWorldRelationByName(wr.fromWorldName); // wfold;
							while(wfcrosser != null && hasGrandParent == false && wfcrosser.worldSubPosition >= startSb ) {
								 //Log.log(">>>>  "+wfcrosser.worldName+"   "+wr.worldName);
								if(selectedWorldsNm.contains(wfcrosser)) { //.worldName.equals(wr.worldName)) {
									hasGrandParent = true;
								}
								wfcrosser = getWorldRelationByName(wfcrosser.fromWorldName);
							}
						}
					}
					// hasGrandParent  , wr.toString()
					Log.log("> GENERATED CLAIMABLE WORLD: "+totalcounter+ ", bro.max: "+emptyPart+", bro.rest: "+broCounter +", name: " +prefixWorld+ is +"_"+ indRdm +"_"+ broRdm);
					
					// add world 
					
					if(hasGrandParent == false && !selectedWorldsNm.contains(wr) && wr != null) {
						selectedWorldsNm.add(wr);
						broCounter--;
						
						totalcounter++;
					}
				}
				
			}

			Log.log("==== ########## ######### ###### ====");

			Log.log("==== WORLDS TO GENERATE: ====");
			
			String pillworlds = "\n\n";
			for(WorldRelation wr : selectedWorldsNm) {
				pillworlds += wr.worldName + ";";
			}
			Log.log(pillworlds);
			
			Log.log("\n\n==== ###### ## ########  ====");
			
			
		
		}
		catch(Exception ex) {
			isok = false;
		}
		
		return isok;
	}
	
	public World getWorldByName(String nm) {

		if(nm == null) return null;
		
		
		for(WorldRelation worldRelation : worlds) {
			
			if(nm.toUpperCase().trim().equals(worldRelation.worldName.toUpperCase().trim())) {

				return worldRelation.getWorld();
				
			}
		}
		
		return null;
	}
	
}