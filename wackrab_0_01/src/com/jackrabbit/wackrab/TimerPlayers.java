package com.jackrabbit.wackrab;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TimerPlayers {

	ArrayList<TimePlayerRelation> timePlayers;
	
	public TimerPlayers() {
		timePlayers = new ArrayList<TimePlayerRelation>();
	}
	
	public TimePlayerRelation getPlayerRelation(Player player) {
		
		for(TimePlayerRelation tpr : timePlayers)
			if(player.getName().equals(tpr.player.getName()))
				return tpr;
		
		TimePlayerRelation timePlayerRelation = new TimePlayerRelation();
		timePlayerRelation.timeCounter = 0;
		timePlayerRelation.player = player;
		timePlayers.add(timePlayerRelation);
		
		return timePlayerRelation;
	}
	
	public class TimePlayerRelation {

		public Player player;
		public World desiredWorld = null;
		public int timeCounter = 8;
		public boolean enableteleportmessage;
		
		public void reset() {
			timeCounter = 8;
			desiredWorld = null;
			retirerNausee(player);
		}
		
		public boolean count() {
			if(timeCounter != 8) {
				donnerNausee(player, 8-timeCounter);
			}
			
			if(timeCounter > 0) {
				timeCounter--;
				player.sendMessage(timeCounter + "..");
				return false;
			}
			else {
				//timeCounter = 8;
				//retirerNausee(player);
				return true;
			}
		}
		
		void donnerNausee(Player joueur, int intensite) {
		    PotionEffectType type = PotionEffectType.CONFUSION; // Type d'effet de nausée
		    int duree = 30 * 20; // 60 secondes * 20 ticks (1 seconde = 20 ticks)

		    PotionEffect effetNausee = new PotionEffect(type, duree, intensite);
		    joueur.addPotionEffect(effetNausee);
		}
		
		void retirerNausee(Player joueur) {
		    joueur.removePotionEffect(PotionEffectType.CONFUSION);
		}
	}

}
