package com.jackrabbit.wackrab.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHowTo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		// TODO Auto-generated method stub

    	String helpMessage = "";
    	
        
    	if(args.length == 0 || args[0].toString().equals("0") || args[0].toString().equals("1")) {

	        helpMessage += ChatColor.GREEN;
	        helpMessage += " = ~ = ~ =   P A G E   1/2   = ~ = ~ =                     "; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
	        helpMessage += "Wackrab Plugin:                                           "; helpMessage += ChatColor.GOLD; helpMessage += "\n";
	        helpMessage += "  You can go in other world with this teleporter model:   "; helpMessage += "\n";
	        helpMessage += "                                                          "; helpMessage += "\n";
	        helpMessage += "         [O]            O = Obsidian Block                "; helpMessage += "\n";
	        helpMessage += "     [O] [q] [O]       q = Quartz Block / Cut Sandstone  "; helpMessage += "\n";
	        helpMessage += " [O] [q] [╳] [q] [O]      Diamond Block  (Go World  N+1)"; helpMessage += "\n";
	        helpMessage += "     [O] [q] [O]      ╳ = Emeraud Block  (Different N+1)  ";   helpMessage += "\n";
	        helpMessage += "         [O]                Gold Block     (Go World  N-1)"; helpMessage += "\n";
	        helpMessage += "                                                          "; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
	        helpMessage += "notes:  You can't return to the 'home world' from the Home."; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
	        helpMessage += "          All x & z positions are relied between worlds."; helpMessage += ChatColor.WHITE; helpMessage += "\n";
	        helpMessage += "              "; 
	        helpMessage += "/howto 2  (next page)                    "; helpMessage += ChatColor.GREEN; helpMessage += "\n";
	        helpMessage += " = ~ = ~ =   # # # #   #/#   = ~ = ~ =                    "; 
	        
	        helpMessage = helpMessage.replaceAll("\\[q\\]", ChatColor.WHITE + "[q]" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("\\[O\\]", ChatColor.DARK_GRAY + "[O]" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("\\[╳\\]", ChatColor.GRAY + "[╳]" + ChatColor.GOLD);
	         
	        helpMessage = helpMessage.replaceAll("q = Quartz Block / Cut Sandstone", ChatColor.WHITE + "q = Quartz Block / Cut Sandstone" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("O = Obsidian Block", ChatColor.DARK_GRAY + "O = Obsidian Block" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("Diamond Block  (Go World  N+1)", ChatColor.GRAY + "Diamond Block  (Go World  N+1)" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("╳ = Emeraud Block  (Different N+1)", ChatColor.GRAY + "╳ = Emeraud Block  (Different N+1)" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("Gold Block     (Go World  N-1)", ChatColor.GRAY + "Gold Block     (Go World  N-1)" + ChatColor.GOLD);
	        
	        
			sender.sendMessage(helpMessage);
			return true;
    	}
    	else if(args[0].toString().equals("2"))
    	{
    		helpMessage += ChatColor.GREEN;
	        helpMessage += " = ~ = ~ =   P A G E   2/2   = ~ = ~ =              "; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
	        helpMessage += "Wackrab Plugin:                                     "; helpMessage += ChatColor.GOLD; helpMessage += "\n";
	        helpMessage += "  You can teleport far & faster with this teleporter model:"; helpMessage += "\n";
	        helpMessage += "                                                         "; helpMessage += "\n";
	        helpMessage += "            [O]              O = Obsidian Block          "; helpMessage += "\n";
	        helpMessage += "        [O] [q] [O]          q = Quartz / Cut Sandstone  "; helpMessage += "\n";
	        helpMessage += "    [O] [q] [D] [q] [O]      a = Diamond     "; helpMessage += "\n";
	        helpMessage += "[O] [q] [E] [╳] [E] [q] [O]  b = Emeraud     "; helpMessage += "\n";
	        helpMessage += "    [O] [q] [D] [q] [O]      ╳ = Gold Block  "; helpMessage += "\n";
	        helpMessage += "        [O] [q] [O]           ";   helpMessage += "\n";
	        helpMessage += "            [O]               "; helpMessage += "\n";
	        helpMessage += "                              "; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
	        helpMessage += "notes:  Open an UI to choose your world."; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
	        helpMessage += "        You need EXP to use this teleporter."; helpMessage += ChatColor.LIGHT_PURPLE; helpMessage += "\n";
	        
	        helpMessage += ChatColor.GREEN; helpMessage += "\n";
	        helpMessage += " = ~ = ~ =   # # # #   #/#   = ~ = ~ =               "; 
	        
	        helpMessage = helpMessage.replaceAll("\\[q\\]", ChatColor.WHITE + "[q]" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("\\[O\\]", ChatColor.DARK_GRAY + "[O]" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("\\[╳\\]", ChatColor.YELLOW + "[╳]" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("\\[D\\]", ChatColor.BLUE + "[D]" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("\\[E\\]", ChatColor.GREEN + "[E]" + ChatColor.GOLD);
	         
	        helpMessage = helpMessage.replaceAll("q = Quartz / Cut Sandstone", ChatColor.WHITE + "q = Quartz / Cut Sandstone" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("O = Obsidian Block", ChatColor.DARK_GRAY + "O = Obsidian Block" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("a = Diamond", ChatColor.BLUE + "a = Diamond" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("b = Emeraud", ChatColor.GREEN + "b = Emeraud" + ChatColor.GOLD);
	        helpMessage = helpMessage.replaceAll("╳ = Gold Block", ChatColor.YELLOW + "╳ = Gold Block" + ChatColor.GOLD);

	        
			sender.sendMessage(helpMessage);
			return true;
    	}
		return false;
	}

}