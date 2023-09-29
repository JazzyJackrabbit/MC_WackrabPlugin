package com.jackrabbit.wackrab.ui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.jackrabbit.wackrab.Main;

public class UIHierarchiesWorlds {
	ArrayList<UIHierarchieWorlds> hierarchiesPlayers;

	public UIHierarchieWorlds creerInventaire(Main _main, Player player, String title) {

		// On récupere, ou on créé la classe d'inventaire si il n'existe pas
		UIHierarchieWorlds hw = null;
		hw = getHierarchieWorlds(player.getName());
		if(hw != null) {
			hierarchiesPlayers.remove(hw);
			System.out.println("TESTA1");
		}
		hw = new UIHierarchieWorlds(_main, player);
		hierarchiesPlayers.add(hw);
		hw.inventaire = Bukkit.createInventory(null,9*5, title);
		hw.inventaire.setMaxStackSize(9*5);
		System.out.println("TESTA2");
		return hw;
	}
	
	public UIHierarchieWorlds getHierarchieWorlds(String playernm) {
		for(UIHierarchieWorlds uihw : hierarchiesPlayers)
			if(uihw.player.getName().equals(playernm))
			{
				return uihw;
			}
		return null;
	}
	
	/*public UIHierarchieWorlds removeHierarchieWorlds(String playernm) {
		UIHierarchieWorlds toRemove = getHierarchieWorlds(playernm);
		hierarchiesPlayers.remove(toRemove);
		return toRemove;
	}*/
	/*
	public UIHierarchieWorlds getHierarchieWorlds(Inventory invent) {
		for(UIHierarchieWorlds uihw : hierarchiesPlayers)
			if(uihw.inventaire.equals(invent))
			{
				return uihw;
			}
		return null;
	}
	*/
	
	public UIHierarchiesWorlds() {
		hierarchiesPlayers = new ArrayList<UIHierarchieWorlds>();
	}
}
