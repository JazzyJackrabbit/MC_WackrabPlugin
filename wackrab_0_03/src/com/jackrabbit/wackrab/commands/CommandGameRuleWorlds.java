package com.jackrabbit.wackrab.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.jackrabbit.wackrab.Main;
import com.jackrabbit.wackrab.WorldRelation;

public class CommandGameRuleWorlds implements CommandExecutor  {

	Main main;
	public CommandGameRuleWorlds(Main main) 
	{
		this.main = main;
	}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Vérifier si la commande est /gameruleworld
        if (command.getName().equalsIgnoreCase("gameruleworld")) {
            // Vérifier s'il y a suffisamment d'arguments
            if (args.length >= 3) {
                String world = args[0];
                String gamerule = args[1];
                String[] values = new String[args.length - 2];
                System.arraycopy(args, 2, values, 0, args.length - 2);

                // Appeler la fonction setGameRule avec les arguments récupérés
                World w = Bukkit.getServer().getWorld(world);
                setGameRuleWorlds(main, w, gamerule, values);

                return true;
            } 
            else
            {
            	sender.sendMessage("Usage: /gameruleworld <world> <gamerule> <value1> <value2> ...");
                return false;
            }
        }
        return false;
    }
    
    public static void setGameRuleWorlds(Main main, World targetWorld, String gamerule, String[] values) {
    	//for(WorldRelation wr : main.hierachieWorlds.worlds) 
		//{
    	//	World targetWorld = wr.getWorld();
    	
        if (targetWorld != null) {
            // Si vous avez besoin de traiter les valeurs d'une certaine manière, faites-le ici
            StringBuilder concatenatedValues = new StringBuilder();
            for (String value : values) {
                concatenatedValues.append(value).append(" ");
            }

            // Ensuite, vous pouvez appliquer les gamerules selon vos besoins
            targetWorld.setGameRuleValue(gamerule, concatenatedValues.toString().trim());
        } else {
            System.out.println("CommandGameRuleWorlds: Le monde n'a pas pu être trouvé.");
        }
		//}
    }
    
    /*
    public void setGameRuleWorlds(String gamerule, String value) {
    	for(WorldRelation wr : main.hierachieWorlds.worlds) 
		{
			if(wr.world != null) {
				
				String value = null;
				if(arg2[1])
				
				wr.world.setGameRuleValue(gamerule, value);
				
				wr.world.setGameRule(, null)
			}
		}
    }
    /*
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		for(WorldRelation wr : main.hierachieWorlds.worlds) 
		{
			if(wr.world != null) {
				
				String value = null;
				if(arg2[1])
				
				wr.world.setGameRuleValue("gamerule", "valeur");
				
				wr.world.setGameRule(, null)
			}
		}
		
		return false;
	}*/

}
