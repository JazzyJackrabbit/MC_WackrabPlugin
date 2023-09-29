package com.jackrabbit.wackrab;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.w3c.dom.Document;

import com.jackrabbit.wackrab.sqlite.DbDataWorld;
import com.jackrabbit.wackrab.utils.Log;
import com.jackrabbit.worlds.MondeUtils;

public class WorldRelation implements Comparable<WorldRelation> {
    
	
	public static class ComparatorDescendant implements Comparator<WorldRelation> {
	    @Override
	    public int compare(WorldRelation objet1, WorldRelation objet2) {
	        return Integer.compare(objet2.worldSubPosition, objet1.worldSubPosition);
	    }
	}
	
	
	@Override
    public int compareTo(WorldRelation autreObjet) {
        return Integer.compare(this.worldSubPosition , autreObjet.worldSubPosition);
    }
    
    public World world = null;

    public String worldLabel;
    public String worldName;
    public String fromLabel;
    public String subRLabel;
    public String subLLabel;
    public String prefixWorld;
    
    public int worldSubPosition = 0;
    public int worldBrotherPosition = 0;
    public String worldIdentifier = "NONE"; // R / L

	public String subWorldLName = null;
	public String subWorldRName = null;

	public String fromWorldName;

    public int dynamicTmp_GoToSubDirection = 0;
    
    public World getWorld() {
    	if(world == null) {
    		 world = MondeUtils.creerMonde(worldName);
    	}
    	
    	return world;
    }

    public String getWorldIdentifier() {
    	return HierarchieWorlds.getWorldIdentifier(worldBrotherPosition);
    }
    
    public boolean isDefaultWorldRelation() {
    	World defaultWorld = Bukkit.getServer().getWorlds().get(0);	
    	if(worldName.toUpperCase().equals(defaultWorld.getName().toUpperCase())) {
    		return true;
    	}
    	return false;
    }
    public boolean canEnterOn(Main main, String playername) {
    	
    	boolean autorisationEntrer = false;
    	
    	if(worldSubPosition <= 0) {
    		autorisationEntrer = true;
    	}
    	
    	if(isClaim(main, false) && playername.equals(playerOwner)) {
    		autorisationEntrer = true;
    	}
    	
    	if(isClaim(main, false) && this.isAllowedAccessDBRequest(main, false) == 1) {
    		autorisationEntrer = true;
    	}
    	
    	if(isClaim(main, false) && this.get_allowedPlayerEnterOn(main, playername, false))
    	{
    		autorisationEntrer = true;
    	}
	
    	if(!isClaim(main, false)) {
    		autorisationEntrer = true;
    	}
    	

		return autorisationEntrer;
    }
    
    // CLAIMS
    
    
    // attr
    

    boolean alreadyloaded = false;
    boolean isclaimableAlreadyLoaded = false;
    public boolean isclaimable = false;
    
    int isclaim = 0;
    String playerOwner = "<UNKNOWN>";
    int canEnterOn = 0;
    int ispvpenable = 0;
    String allowedplayersinclaim = "";
    
    int isAllowedAccess = 1;

    public String getPlayerOwner(Main main, boolean writeondb) {
    	
    	DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); 
    	
		if(returnedInformations == null)
			returnedInformations = 
					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, playerOwner, ispvpenable, playerOwner, isAllowedAccess );

		if(playerOwner != returnedInformations.playerOwner) {
			playerOwner = returnedInformations.playerOwner;
			
			if(writeondb)
				main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
		}
    	
    	return playerOwner;
    }
    
    
    
    public String setPlayerOwner(Main main, String v) {
    	
    	 
    	playerOwner = v;
    	
    	// 1-select
    	// 2-if null create
    	// 3-change value 
    	// 4-insert
    	
    	
		DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); 
    	
		if(returnedInformations == null)
			returnedInformations = 
					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, getPlayerOwner(main, false), ispvpenable, allowedplayersinclaim, isAllowedAccess );

			returnedInformations.playerOwner = playerOwner;
		
    	main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
		
    	return playerOwner;
    }
    
    

    public String get_allowedAllPlayersEnterOn(Main main, boolean writeondb) {//, boolean save) {
    	
    	DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); 
    	
		if(returnedInformations == null)
			returnedInformations = 
					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, playerOwner, ispvpenable, allowedplayersinclaim, isAllowedAccess );

		if(allowedplayersinclaim != returnedInformations.allowedPlayersEnterOn) {
			allowedplayersinclaim = returnedInformations.allowedPlayersEnterOn;
			
			if(writeondb)
				main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
		}

    	return allowedplayersinclaim;
    }

    public boolean get_allowedPlayerEnterOn(Main main, String playername, boolean writeondb) {//, boolean save) {
    	
    	DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); 
    	
		if(returnedInformations == null)
			returnedInformations = 
					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, playerOwner, ispvpenable, allowedplayersinclaim, isAllowedAccess );

		//resinsertion si besoin
		if(allowedplayersinclaim != returnedInformations.allowedPlayersEnterOn) {
			returnedInformations.allowedPlayersEnterOn = allowedplayersinclaim;

			if(writeondb) {
				main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
				//allowedplayersinclaim = returnedInformations.allowedPlayersEnterOn;
			}
		}

		String listuserallowed = allowedplayersinclaim;
		String[] list = listuserallowed.split(";");

		if(playername != null && !playername.isEmpty()) {
			for(String userallwd : list) {
				if(userallwd != null && !userallwd.isEmpty() && !userallwd.isBlank()) {

					if(userallwd.trim().toUpperCase().equals(playername.toUpperCase().trim()))
						return true;
				}
			}

		}
		
    	return false;
    }
    
    public String set_allowedPlayersEnterOn(Main main, String v) {
    	 
    	allowedplayersinclaim = v;
    	
    	// 1-select
    	// 2-if null create
    	// 3-change value 
    	// 4-insert
    	
    	
		DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); 
    	
		if(returnedInformations == null)
			returnedInformations = 
					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, playerOwner, ispvpenable, get_allowedAllPlayersEnterOn(main, false), isAllowedAccess );

		returnedInformations.allowedPlayersEnterOn = allowedplayersinclaim;
		
    	main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
		
    	return allowedplayersinclaim;
    }
    
    public int setAllowedAccess(Main main, int v) {
 
    	isAllowedAccess = v;
    	
    	// 1-select
    	// 2-if null create
    	// 3-change value 
    	// 4-insert
    	
    	
		DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); 
    	
		if(returnedInformations == null)
			returnedInformations = 
					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, playerOwner, ispvpenable, get_allowedAllPlayersEnterOn(main, false), isAllowedAccess );

			returnedInformations.isAllowedAccess = isAllowedAccess;
		
    	main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
		
    	return isAllowedAccess;
    }

    public int isAllowedAccessDBRequest(Main main, boolean writeOnDb) {
    	DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); 
    	
		if(returnedInformations == null)
			returnedInformations = 
					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, playerOwner, ispvpenable, get_allowedAllPlayersEnterOn(main, false), isAllowedAccess );

		if(isAllowedAccess != returnedInformations.isAllowedAccess) {
			returnedInformations.isAllowedAccess = isAllowedAccess;
			main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
		}

    	return isAllowedAccess;
    	
    }

    /*
    public int isAllowedAccess(Main main) {
    	return isAllowedAccessDBRequest(main);

    	//return isAllowedAccess;
    	
    }*/
    
    public int isPVPEnableDBRequest(Main main, boolean writeOnDb) {
    	DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); 
    	
		if(returnedInformations == null)
			returnedInformations = 
					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, playerOwner, ispvpenable, allowedplayersinclaim, isAllowedAccess);

		if(ispvpenable != returnedInformations.ispvpenable) {
			returnedInformations.ispvpenable = ispvpenable;
			if(writeOnDb)
				main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
		}

    	return ispvpenable;
    	
    }
    
    public int setPVPEnable(Main main, int v) {
    	 
    	ispvpenable = v;
    	
    	// 1-select
    	// 2-if null create
    	// 3-change value 
    	// 4-insert
    	
    	
		DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); 
    	
		if(returnedInformations == null)
			returnedInformations = 
					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, playerOwner, isPVPEnableDBRequest(main, false), allowedplayersinclaim, isAllowedAccess );

		returnedInformations.ispvpenable = ispvpenable;
		
    	main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
		
    	return ispvpenable;
    }
    
    
    public boolean isClaimableWorld(Main main) {

    	if(isclaimableAlreadyLoaded == false) {
    		
        	DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); //Where name = this
        	
        	if(returnedInformations != null) {
        	
	        	isclaimable = returnedInformations.isclaimable == 1;
	
	    		isclaimableAlreadyLoaded = true;
	        	
	        	return isclaimable;
        	
        	}
        	
    	}
    	
    	return isclaimable;
    	
    }
    
    
    public void writeAllDataWorld() {

    }
    
    
    public boolean isClaim(Main main, boolean writeondb) {
    	//charge de la db
    	
    	DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); //Where name = this
    	
    	if(returnedInformations != null) {
	    	playerOwner = returnedInformations.playerOwner;
	    	isclaim = returnedInformations.isClaim;
			    
		    if(isclaim == 1) return true;
		    if(isclaim == 0) return false;
    	}
    	
	    return false;
    }
    
    public void claimWorldDB(Main main, DbDataWorld returnedInformations, Player joueur, boolean writeondb, String messageAfterInsert) {

    	boolean authorisedToClaim = false;
    	
    	if(authorisedToClaim) {
	    	returnedInformations.isClaim = 1;
	    	returnedInformations.playerOwner = joueur.getName();
	    	if(writeondb)
	    		main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
	
		    	joueur.sendMessage(messageAfterInsert);

    	}else {

	    	joueur.sendMessage("You are not authorized to claim this world yet. ");
	    	joueur.sendMessage("the 'Claim World' system coming soon.");
	    	joueur.sendMessage("For more informations, talk to Leetnia.  DISCORD:  leetnia ");
    	}
    }
    
    public boolean claim(Main main, Player joueur, boolean writeondb) {

    	if(isClaimableWorld(main)) {
    		
		    // claim déja "enregistré" par un joueur ou pas
			
    		DbDataWorld returnedInformations = main.DTB.worlds.select(main.DTB, main, this.worldName); //Where name = this
        	
    		if(returnedInformations == null)
    			returnedInformations = 
    					main.DTB.worlds.newDbDataWorld(this.worldName, isclaimable?1:0, isclaim, playerOwner, ispvpenable, get_allowedAllPlayersEnterOn(main, false), isAllowedAccess );
    			
    		
    		playerOwner = returnedInformations.playerOwner;
    		isclaim = returnedInformations.isClaim;
		    
    		
    		
    		
		    if(playerOwner.equals(joueur.getName())) {
		    	
		    	isclaim = 1;

			    claimWorldDB(main, returnedInformations, joueur, writeondb,
			    		"This is your own claim.");
			    
		    	return true;
		    }
		    
		    if(isclaim == 0) {
		    	 
			    isclaim = 1;
			    playerOwner = joueur.getName();
			   
			    claimWorldDB(main, returnedInformations, joueur, writeondb,
			    		"Congratulation ! You just have claim this world !");
			    
			    /*
		    	returnedInformations.isClaim = 1;
		    	returnedInformations.playerOwner = joueur.getName();
		    	if(writeondb)
		    		main.DTB.worlds.insert(main.DTB, main, returnedInformations, true);
		    	*/
			    
		    	
			    return true;
		    }
	
		    joueur.sendMessage("You can't claim this world.");
		    return false;

	    
    	}
    	else {

		    joueur.sendMessage("This world can't be claimed by no one.");
		    return false;
    	}
    }

    public static boolean containsWorldName(Collection<WorldRelation> c, String worldname) {
        for(WorldRelation o : c) {
            if(o != null && o.worldName.equals(worldname)) {
                return true;
            }
        }
        return false;
    }
    
    class Returned_WR_cClmd{
    	public Returned_WR_cClmd(ArrayList<WorldRelation> arr, int nclaimed) {
    		this.arr = arr;
    		this.nclaimed = nclaimed;
    	}
    	public ArrayList<WorldRelation> arr;
    	public int nclaimed;
    }
    
    
    public class ConvertirStringBuilderEnDocument {

    public static void main(String[] args) {
    	/*
        try {
            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element>Contenu</element></root>");

            // Convertir le StringBuilder en un flux d'entrée
            ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));

            // Créer un nouveau Document à partir du flux d'entrée
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document = factory.newDocumentBuilder().parse(inputStream);

            // Vous pouvez maintenant utiliser 'document' comme vous le feriez avec un document créé à partir d'un fichier XML.
            Log.log("Document créé avec succès.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}


    
    
    
    
    
    
    
    
    
    
}
