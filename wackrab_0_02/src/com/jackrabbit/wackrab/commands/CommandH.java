package com.jackrabbit.wackrab.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandH implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	
    	String helpMessage = "";
    	helpMessage += ChatColor.GREEN;
        helpMessage += " = ~ = ~ =   H E L P   = ~ = ~ =                          "; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
        helpMessage += "Commands:                                                "; helpMessage += ChatColor.GOLD; helpMessage += "\n";
        helpMessage += "  /world : show the current world informations.           "; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
        helpMessage += "Wackrab Plugin:                                          "; helpMessage += ChatColor.GOLD; helpMessage += "\n";
        helpMessage += "  You can go in other world with this teleporter model:   "; helpMessage += "\n";
        helpMessage += "                                                          "; helpMessage += "\n";
        helpMessage += "         [O]                                              "; helpMessage += "\n";
        helpMessage += "                        O = Obsidian Block                "; helpMessage += "\n";
        helpMessage += "     [O] [q] [O]                                          "; helpMessage += "\n";
        helpMessage += "                        q = Quartz Block / Cut Sandstone  "; helpMessage += "\n";
        helpMessage += " [O] [q] [╳] [q] [O]                                      "; helpMessage += "\n";
        helpMessage += "                            Diamond Block  (Go World  N+1)"; helpMessage += "\n";
        helpMessage += "     [O] [q] [O]      ╳ = Emeraud Block  (Different N+1)";   helpMessage += "\n";
        helpMessage += "                            Gold Block     (Go World  N-1)"; helpMessage += "\n";
        helpMessage += "         [O]                                              "; helpMessage += "\n";
        helpMessage += "                                                          "; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
        helpMessage += "notes:  You can't return to the 'home world' from the Home."; helpMessage += ChatColor.GREEN; helpMessage += "\n";
        helpMessage += " = ~ = ~ =   # # # #   = ~ = ~ =                          "; 
        
        helpMessage = helpMessage.replaceAll("\\[q\\]", ChatColor.WHITE + "[q]" + ChatColor.GOLD);
        helpMessage = helpMessage.replaceAll("\\[O\\]", ChatColor.DARK_GRAY + "[O]" + ChatColor.GOLD);
        helpMessage = helpMessage.replaceAll("\\[╳\\]", ChatColor.GRAY + "[╳]" + ChatColor.GOLD);
         
        helpMessage = helpMessage.replaceAll("q = Quartz Block / Cut Sandstone", ChatColor.WHITE + "q = Quartz Block / Cut Sandstone" + ChatColor.GOLD);
        helpMessage = helpMessage.replaceAll("O = Obsidian Block", ChatColor.DARK_GRAY + "O = Obsidian Block" + ChatColor.GOLD);
        helpMessage = helpMessage.replaceAll("Diamond Block  (Go World  N+1)", ChatColor.GRAY + "Diamond Block  (Go World  N+1)" + ChatColor.GOLD);
        helpMessage = helpMessage.replaceAll("╳ = Emeraud Block  (Different N+1)", ChatColor.GRAY + "╳ = Emeraud Block  (Different N+1)" + ChatColor.GOLD);
        helpMessage = helpMessage.replaceAll("Gold Block     (Go World  N-1)", ChatColor.GRAY + "Gold Block     (Go World  N-1)" + ChatColor.GOLD);
        
         /*  with tags: .... bad...
    	helpMessage += " = ~ = ~ =   H E L P   = ~ = ~ = 								"; helpMessage += ChatColor.LIGHT_PURPLE;
    	helpMessage += " Commands:						 								"; helpMessage += ChatColor.GOLD;
    	helpMessage += "  /world : show the current world informations.					";
    	helpMessage += " 								 								"; helpMessage += ChatColor.LIGHT_PURPLE;
    	helpMessage += " Wackrab Plugin:												"; helpMessage += ChatColor.GOLD;
    	helpMessage += "  You can go in other world with this teleporter model:			";
    	helpMessage += " 								 								";
    	helpMessage += "         [q]						 							";
    	helpMessage += "                        q = Quartz Block		 				";
    	helpMessage += "     [q] [O] [q]						 						";
    	helpMessage += "                        O = Obsidian Block 						";
    	helpMessage += " [q] [O] [╳] [O] [q]				 							";
    	helpMessage += "                            Diamond Block  ( Go World N+1 )     ";
    	helpMessage += "     [q] [O] [q]        ╳ = Emeraud Block  ( Go World N+1 different )";
    	helpMessage += "                            Gold Block     ( Go World N-1 )     ";
    	helpMessage += "         [q]						 							";
    	helpMessage += "								 								"; helpMessage += ChatColor.LIGHT_PURPLE;
    	helpMessage += " notes:  You can't return to the 'home world' from the Home.    "; helpMessage += ChatColor.GREEN;
    	helpMessage += " = ~ = ~ =   # # # #   = ~ = ~ = 								";
    	
    	helpMessage.replaceAll("[q]", ChatColor.WHITE + "[q]" + ChatColor.GOLD);
    	helpMessage.replaceAll("[O]", ChatColor.DARK_GRAY + "[O]" + ChatColor.GOLD);
     	helpMessage.replaceAll("[╳]", ChatColor.GRAY + "[╳]" + ChatColor.GOLD);
     	
     	helpMessage.replaceAll("q = Quartz Block", ChatColor.WHITE + "q = Quartz Block" + ChatColor.GOLD);
     	helpMessage.replaceAll("O = Obsidian Block", ChatColor.DARK_GRAY + "O = Obsidian Block" + ChatColor.GOLD);
     	helpMessage.replaceAll("Diamond Block  ( Go World N+1 )", ChatColor.GRAY + "Diamond Block  ( Go World N+1 )" + ChatColor.GOLD);
     	helpMessage.replaceAll("╳ = Emeraud Block  ( Go World N+1 different )", ChatColor.GRAY + "╳ = Emeraud Block  ( Go World N+1 different )" + ChatColor.GOLD);
     	helpMessage.replaceAll("Gold Block     ( Go World N-1 )", ChatColor.GRAY + "Gold Block     ( Go World N-1 )" + ChatColor.GOLD);
    	*/
         
         
        if (command.getName().equalsIgnoreCase("h")) {
            sender.sendMessage(helpMessage);
            return true;
        }
        return false;
    }
}