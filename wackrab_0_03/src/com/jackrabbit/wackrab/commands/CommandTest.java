package com.jackrabbit.wackrab.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.jackrabbit.wackrab.Main;
import com.jackrabbit.wackrab.WorldRelation;

public class CommandTest implements CommandExecutor {

	Main main;
	public CommandTest(Main main) {
		this.main = main;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		// TODO Auto-generated method stub
		
		//test:

		return true;
		/*
		 * 
		 * 5000 = 250
		 * 1/20
		 * 
		 * 
		int n1 = 0;
		int n2 = 0;
		for(WorldRelation wr : main.hierachieWorlds.worlds) {
			n1++;
			if(wr.isClaimableWorld(main)) {
				n2++;
			}
			sender.sendMessage(wr.worldLabel +"  " +n1+ "  "+n2);
		}
		
		
		sender.sendMessage("ok");
		
		return false;
		*/
	}

}
