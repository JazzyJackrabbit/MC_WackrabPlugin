package com.jackrabbit.wackrab.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jackrabbit.wackrab.Main;
import com.jackrabbit.wackrab.WorldRelation;
import com.jackrabbit.wackrab.utils.Utils;

public class CommandPath implements CommandExecutor{

	Main main;
	boolean isPathFrom;
	public CommandPath(Main main, boolean isPathFrom) {
		this.main = main;
		this.isPathFrom = isPathFrom;
	}
	 public static ArrayList<WorldRelation> trouverValeursNonCorrespondantes
	 								(ArrayList<WorldRelation> tableau1, ArrayList<WorldRelation> tableau2) {
		 
		 
	 	ArrayList<WorldRelation> tableau3 = new ArrayList<>();

        for (WorldRelation valeur : tableau1) {
            boolean correspondanceTrouvee = false;
            for (WorldRelation valeur2 : tableau2) {
                if (valeur.worldName.equals(valeur2.worldName)) {
                    correspondanceTrouvee = true;
                    break;
                }
            }
            if (!correspondanceTrouvee) {
                tableau3.add(valeur);
            }
        }

        for (WorldRelation valeur : tableau2) {
            boolean correspondanceTrouvee = false;
            for (WorldRelation valeur1 : tableau1) {
                if (valeur.worldName.equals(valeur1.worldName)) {
                    correspondanceTrouvee = true;
                    break;
                }
            }
            if (!correspondanceTrouvee) {
                tableau3.add(valeur);
            }
        }

        return tableau3;
    }
 
	 @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		 if (label.equalsIgnoreCase("path") || label.equalsIgnoreCase("pathfrom")) {

			 // get worlds
			 WorldRelation worldFrom = null;
			 WorldRelation worldTo = null;
			 if(label.equalsIgnoreCase("pathfrom") && isPathFrom == true) {
				 worldFrom = main.hierachieWorlds.getWorldRelationByName(args[0]);
				 worldTo = main.hierachieWorlds.getWorldRelationByName(args[1]);
			 }
			 else if (label.equalsIgnoreCase("path") && isPathFrom == false) {
				 if (sender instanceof Player) {
					 Player playerfrom = ((Player) sender).getPlayer();
					 worldFrom = main.hierachieWorlds.getWorldRelationByName((playerfrom.getWorld()).getName());
					 worldTo = main.hierachieWorlds.getWorldRelationByName(args[0]); 
				 }
				 else {
					 return true;
				 }
			 }

			 if(worldFrom == null)  {
				 sender.sendMessage("'From' world not found.");
				 return true;
			 }

			 if(worldTo == null) {
				 sender.sendMessage("'To' world not found");
				 return true;
			 }
			 
			 // paths
			 
			 // rework on
			 ArrayList<WorldRelation> identifiersFrom = new ArrayList<WorldRelation>();

			 //paths from	 
			 //WorldRelation worldRel = worldFrom;
			 //WorldRelation worldRel = main.hierachieWorlds.getWorldRelationByName(worldFrom.fromWorldName);
			 WorldRelation worldRel = main.hierachieWorlds.getWorldRelationByName(worldFrom.worldName);
			 
			 //ArrayList<WorldRelation> identifiersFrom = new ArrayList<WorldRelation>();
			 while(worldRel.worldSubPosition > 0) {
			 			
				worldRel.dynamicTmp_GoToSubDirection = -1;
				identifiersFrom.add(worldRel);
				worldRel = main.hierachieWorlds.getWorldRelationByName(worldRel.fromWorldName);
		
		     }
			 
			 boolean passByDefaultWorld1 = false;
			 boolean passByDefaultWorld2 = false;
			 
			 if(worldRel != null) {
				 identifiersFrom.add(worldRel); //or parent ?
			 }
				//WorldRelation wrdflt = main.hierachieWorlds.getDefaultWorldRelation();
				 //System.out.println("default world:");
				 //System.out.println( wrdflt.worldName );
				 
				 //identifiersFrom.add(wrdflt);
			 
			 //boolean passByHomeWorld = false;
			 //if(worldRel.worldSubPosition == 0)
			 //	 passByHomeWorld = true;
			 
			 
			 //paths to
			 worldRel = worldTo;
			 ArrayList<WorldRelation> identifiersTo = new ArrayList<WorldRelation>();
			 while(worldRel.worldSubPosition > 0) {

				 worldRel.dynamicTmp_GoToSubDirection = +1;
				identifiersTo.add(worldRel);
				worldRel = main.hierachieWorlds.getWorldRelationByName(worldRel.fromWorldName);
		     }
			 			 
			 //if(worldRel == null || worldRel.worldSubPosition == 0) {
			//	 passByDefaultWorld2 = true;
			// }
			 
			 // 3 . remove if possibilities are equals, and remove the from possibilies 

			 ArrayList<WorldRelation> identifiersNew = trouverValeursNonCorrespondantes(identifiersFrom, identifiersTo);
			
			 //sort:
			 ArrayList<WorldRelation> identifiersNewNegativeSubRoad = new ArrayList<WorldRelation>();
			 ArrayList<WorldRelation> identifiersNewPositiveSubRoad = new ArrayList<WorldRelation>();
			 
			 
			 //Collections.sort(identifiersNew, Collections.reverseOrder());
			 //Collections.sort(identifiersNew, identifiersNew );

			 Collections.sort(identifiersNew, new WorldRelation.ComparatorDescendant());
			 
			 for(WorldRelation wr : identifiersNew)
				 if (wr.dynamicTmp_GoToSubDirection < 0)
					 identifiersNewNegativeSubRoad.add(wr);

			 Collections.sort(identifiersNew);
			 
			 for(WorldRelation wr : identifiersNew)
				 if (wr.dynamicTmp_GoToSubDirection > 0)
					 identifiersNewPositiveSubRoad.add(wr);
			 
				
			 // 4 . 
			 
			 String message = ChatColor.GRAY + "";
			 message += "" + worldFrom.worldName + " --> "+ worldTo.worldName;
			 
			 message += ChatColor.WHITE+ "\n";
			 
			 for(WorldRelation wr : identifiersNewNegativeSubRoad)
					 message += "[GOLD]" + "   "
					 +"sub: "+(wr.worldSubPosition+1)+"   world: "+wr.worldName +"\n";
					 //+"    "+wr.dynamicTmp_GoToSubDirection +" \n";
			 
			 //if(passByDefaultWorld1 && passByDefaultWorld2)
			//	 message += "GGG to HOME"+" \n";
			 //if(passByDefaultWorld)
			//	 message += "GGG to HOME"+" \n";
				
			 
			 for(WorldRelation wr : identifiersNewPositiveSubRoad)
			 {
				 String identmsg = "[?]";
				 if(wr.worldIdentifier == "L") identmsg = "[DIAM]";
				 if(wr.worldIdentifier == "R") identmsg = "[EMER]";

				 message += identmsg + "   "
				 +"sub: "+(wr.worldSubPosition+1) +"   world: "+wr.worldName +"\n";
				 //+"    "+wr.dynamicTmp_GoToSubDirection +" \n";
			 }
			 
			 
			 message = message.replaceAll("\\[DIAM\\]", ChatColor.BLUE + "[DIAM]" + ChatColor.WHITE);
			 message = message.replaceAll("\\[EMER\\]", ChatColor.GREEN + "[EMER]" + ChatColor.WHITE);
			 message = message.replaceAll("\\[GOLD\\]", ChatColor.GOLD + "[GOLD]" + ChatColor.WHITE);
									 
			 sender.sendMessage(message);
			 
			 	
			 
		      /*
			 
			 
			 ArrayList<WorldRelation> identifiersRemover = new ArrayList<WorldRelation>();
			 for(WorldRelation wrfrom : identifiersFrom) {
				 WorldRelation wrto2 = null;
				 for(WorldRelation wrto : identifiersTo)
					 if(wrfrom.equals(wrto)) {
						 identifiersRemover.add(wrfrom);
					 }
			 }

			 ArrayList<WorldRelation> identifiersNewFrom = new ArrayList<WorldRelation>();
			 
			 for(WorldRelation wrfrom : identifiersFrom)
				 for(WorldRelation wrRmvr : identifiersRemover)
					 if(!wrfrom.equals(wrRmvr))
						 identifiersNewFrom.add(wrfrom);
						 
			 ArrayList<WorldRelation> identifiersNewTo = new ArrayList<WorldRelation>();

			 for(WorldRelation wrto : identifiersNewTo)
				 for(WorldRelation wrRmvr : identifiersRemover)
					 if(!wrto.equals(wrRmvr))
						 identifiersNewFrom.add(wrto);
						 
			 
			 
			 
			 // 4 . add froms[] + 1 
			 
			 // 5 . add to[]
			 
			 
			 
			 //int subIdLinker = worldFrom.worldSubPosition;

			// if(subIdLinker+1 == wrfrom.worldSubPosition)
			//	 subIdLinker++;
			 if(true == false) {
				 for(WorldRelation wrfrom : identifiersFrom) {
					 boolean bestRoad = true;
					 boolean bestRoadNext = true;
					 for(WorldRelation wrto : identifiersTo) {
						 if(wrfrom.worldName.equals(wrto.worldName)) {
							 if(bestRoad == false) bestRoadNext = false;
							 bestRoad = false;
						 }
					 }
					 if(bestRoad || bestRoadNext) {
						 wrfrom.dynamicTmp_GoTo = ChatColor.GOLD + "Gold" +  ChatColor.WHITE;
						 road.add(wrfrom);
	
					 }
				 }
				 
				 // msg
				 Collections.reverse(identifiersTo);
				 
				 for(WorldRelation wrfrom : identifiersTo) {
					 boolean bestRoad = true;
					 for(WorldRelation wrto : identifiersFrom) {
						 if(wrfrom.worldName.equals(wrto.worldName)) {
							 bestRoad = false;
						 }
					 }
					 if(bestRoad) {
						 if(wrfrom.worldIdentifier.equals("L")) {
							 wrfrom.dynamicTmp_GoTo =  ChatColor.BLUE + "Diam" +  ChatColor.WHITE;
						 }
						 if(wrfrom.worldIdentifier.equals("R")) {
							 wrfrom.dynamicTmp_GoTo =  ChatColor.GREEN + "Emer" +  ChatColor.WHITE;
						 }
						 
						 road.add(wrfrom);
					 }
				 }
				 
				 String messageJoueur = "";
	
				 messageJoueur += (ChatColor.GOLD  + "=+= =^= =+= =^= =+=") + ChatColor.WHITE +"\n";
				 messageJoueur += "Path = ";
	
				 
				 
				 if(road.size() == 0) {
					 sender.sendMessage("can't join this world.");
				 }
				 else
					 for(WorldRelation wr : road) {
						 messageJoueur += wr.dynamicTmp_GoTo +" -> ";
					 }
	
				 messageJoueur = messageJoueur.substring(0, messageJoueur.length() - 4) + "\n";
				 
				 messageJoueur += (ChatColor.GOLD  + "=+= =^= =+= =^= =+=");
				 sender.sendMessage(messageJoueur);
			 }
			 // old script Bad:..
			 /*
			 //paths from
			 WorldRelation worldRel = worldFrom;
			 ArrayList<String> identifiersFrom = new ArrayList<String>();
			 while(worldRel != null && worldRel.worldSubPosition > 0) {
			 	
				String worldIdentifier = worldRel.worldIdentifier;
				
				identifiersFrom.add(worldIdentifier);
				worldRel = main.hierachieWorlds.getWorldRelationByName(worldRel.fromWorldName);
		
		     }

			 //paths to
			 worldRel = worldTo;
			 ArrayList<String> identifiersTo = new ArrayList<String>();
			 while(worldRel != null && worldRel.worldSubPosition > 0) {
			 	
				String worldIdentifier = worldRel.worldIdentifier;
				
				identifiersTo.add(worldIdentifier);
				worldRel = main.hierachieWorlds.getWorldRelationByName(worldRel.fromWorldName);
		     }

			 // collision point
			 String collisionWorld = "";
			 for(String frompoint : identifiersFrom)
				 for(String topoint : identifiersTo)
					 if(frompoint.equals(topoint))
						 collisionWorld = frompoint;
			
			 if(collisionWorld.isEmpty() || collisionWorld.equals("")) {
				 sender.sendMessage("can't join this world.");
			 }
			 else {
				 Collections.reverse(identifiersTo);

				 // conditions & message 
				 
				 // from world
				 String messageJoueur = "";

				 messageJoueur += (ChatColor.GOLD  + "=+= =^= =+= =^= =+=") + ChatColor.WHITE +"\n";
				 messageJoueur += "Path = ";
				 boolean isCollisionWorldOver = false;
				 for(String identpoint : identifiersTo) {
					 if(collisionWorld.equals(identpoint) || !isCollisionWorldOver) {
						
						 messageJoueur += ChatColor.GOLD + "Gold "+ ChatColor.WHITE+" -> ";
						 
						 isCollisionWorldOver = true;
						 
					 }
				 }
				 
				 // from collision
				 isCollisionWorldOver = false;
				 for(String identpath2 : identifiersTo) {
					 if(collisionWorld.equals(identpath2) || isCollisionWorldOver) {
						 isCollisionWorldOver = true;

						 if(identpath2.equals("L"))
							 messageJoueur += ChatColor.BLUE + "Diam "+ ChatColor.WHITE+" -> ";
						 if(identpath2.equals("R"))
							 messageJoueur += ChatColor.GREEN + "Emer "+ ChatColor.WHITE+" -> ";
						 
					 }
				 }
				 
				 messageJoueur = messageJoueur.substring(0, messageJoueur.length() - 4) + "\n";
				 
				 messageJoueur += (ChatColor.GOLD  + "=+= =^= =+= =^= =+=");
				 sender.sendMessage(messageJoueur);
			 }
			 
			 /*
			 Collections.reverse(identifiersTo);
			 
			 ArrayList<String> 
			 
			 /*if (sender instanceof Player) {			 
	            if (args.length == 1) {
	                Player target = Bukkit.getPlayer(args[0]);
	                if (target != null) {
	                    double x = target.getLocation().getX();
	                    double y = target.getLocation().getY();
	                    double z = target.getLocation().getZ();
	                    World w = target.getWorld();
	                    sender.sendMessage(args[0] + "  ~  Monde: "+w.getName()+ ", X:" + x + ", Y:" + y + ", Z:" + z);
	                } else {
	                    sender.sendMessage("Le joueur spécifié n'est pas en ligne.");
	                }
	            } else {
	                sender.sendMessage("Utilisation : /whereis <joueur>");
	            }
	        } else {
	            sender.sendMessage("Seuls les joueurs peuvent exécuter cette commande.");
	        }
		     return true;*/
			 
			 //TODO
			 //sender.sendMessage("todo");
			 
		 }
	     return true;
	 }

}
