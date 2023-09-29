package com.jackrabbit.wackrab.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.jackrabbit.wackrab.Main;

public class CommandWackPlayer implements CommandExecutor{ 
	
	Main main;
	
	public CommandWackPlayer(Main main){
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		// TODO Auto-generated method stub
			
		sender.sendMessage("ok");
		
		return false;
	}
}
