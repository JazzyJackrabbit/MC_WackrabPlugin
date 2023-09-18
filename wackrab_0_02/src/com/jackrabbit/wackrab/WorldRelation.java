package com.jackrabbit.wackrab;

import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

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
    public String worldIdentifier = "NONE";

	public String subWorldLName = null;
	public String subWorldRName = null;

	public String fromWorldName;

    public int dynamicTmp_GoToSubDirection = 0;
    
    public World getWorld() {
    	if(world == null) {
    		 world = MondeUtils.creerMonde(worldName);
    		 //Bukkit.getServer().createWorld(new WorldCreator(worldName));
    	}
    	System.out.println("****************************************");
    	System.out.println(world);
    	System.out.println("*-***********************");
    	return world;
    }

    public String getWorldIdentifier() {
    	return HierarchieWorlds.getWorldIdentifier(worldBrotherPosition);
    }
    
}
