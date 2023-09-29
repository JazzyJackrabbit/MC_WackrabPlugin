package com.jackrabbit.wackrab.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.command.CommandExecutor;

import com.jackrabbit.wackrab.Main;

public class CommandWhereIsPlayer implements CommandExecutor{

	Main main;
	public CommandWhereIsPlayer(Main main) {
		this.main = main;
	}  
	 @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		 if (label.equalsIgnoreCase("whereis")) {
			 if (sender instanceof Player) {			 
	            if (args.length == 1) {
	                Player target = Bukkit.getPlayer(args[0]);
	                if (target != null) {
	                    double x = target.getLocation().getX();
	                    double y = target.getLocation().getY();
	                    double z = target.getLocation().getZ();
	                    World w = target.getWorld();
	                    sender.sendMessage(args[0] + "  ~  Monde: "+w.getName()+ ", X:" + x + ", Y:" + y + ", Z:" + z);
	                } else {
	                    sender.sendMessage("The player is offline.");
	                }
	            } else {
	                sender.sendMessage("arguments : /whereis <player>");
	            }
	        } else {
	            sender.sendMessage("this command can be use by player only.");
	        }
		     return true;
		 }
         sender.sendMessage("invalid command.");
	     return true;
	 }
	
}
