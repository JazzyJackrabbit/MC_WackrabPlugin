package com.jackrabbit.wackrab;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import com.jackrabbit.worlds.MondeUtils;

public class WorldRelation {

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
