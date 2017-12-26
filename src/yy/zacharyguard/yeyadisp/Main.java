package yy.zacharyguard.yeyadisp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	Logger logger = getLogger();
	Server server = getServer();
	
	List<Location> yDispLocations = new ArrayList<Location>();
	
	@Override
    public void onEnable() {
		/* register EventHandlers */
		Bukkit.getPluginManager().registerEvents(new BlockDispenseEventHandler(this), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakEventHandler(this), this);
		
		@SuppressWarnings("rawtypes")
		List configYDispCoords = this.getConfig().getList("yeyadisp_coords");
		logger.info(Integer.toString(configYDispCoords.size()));
		for (int i = 0; i < configYDispCoords.size(); i++) {
			@SuppressWarnings("rawtypes")
			Map info = (Map) configYDispCoords.get(i);
			yDispLocations.add(new Location(
					(World) server.getWorld((String) info.get("world_name")),
					(double) info.get("x"),
					(double) info.get("y"),
					(double) info.get("z")
					));
		}
		
		logger.info("YeyaDisp ready.");
    }
   
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void onDisable() {
    	logger.info("Saving YeyaDisps to config...");
    	
    	List<Map> yDispCoords = new ArrayList<Map>();
    	for (int i = 0; i < yDispLocations.size(); i++) {
    		Location location = yDispLocations.get(i);
    		HashMap info = new HashMap();
    		info.put("world_name", location.getWorld().getName());
    		info.put("x", location.getX());
    		info.put("y", location.getY());
    		info.put("z", location.getZ());
    		yDispCoords.add(info);
    	}
    	
    	this.getConfig().set("yeyadisp_coords", yDispCoords);
    	this.saveConfig();
    	logger.info("YeyaDisps saved.");
    }
    
    @Override
    public boolean onCommand(CommandSender sender,
            Command command,
            String label,
            String[] args) {
        if (
        		(command.getName().equalsIgnoreCase("yeyadisp") || command.getName().equalsIgnoreCase("ydisp"))
        		&& sender instanceof Player
        		) {
        	LivingEntity player = (LivingEntity) sender;
        	Block targetBlock = player.getTargetBlock(null, this.getConfig().getInt("target_block_max_distance"));
        	if (targetBlock.getType() == Material.DISPENSER) {
        		Container dispenser = (Container) targetBlock.getState();
        		Location dispenserLocation = dispenser.getLocation();
        		if (args.length > 0 && args[0].equalsIgnoreCase("disable")) {
        			int yDispIndex = yDispLocations.indexOf(dispenserLocation);
        			if (yDispIndex != -1) {
            			yDispLocations.remove(yDispIndex);
                		player.sendMessage("Successfully removed YeyaDisp abilities from that dispenser.");
            		} else {
            			player.sendMessage("No YeyaDisp exists here.");
            		}
        		} else {
        			if (!yDispLocations.contains(dispenserLocation)) {
            			yDispLocations.add(dispenserLocation);
                		player.sendMessage("Successfully created a YeyaDisp.");
            		} else {
            			player.sendMessage("That YeyaDisp already exists.");
            		}
        		}
        	} else {
        		player.sendMessage("That block is not a dispenser.");
        	}
        }
        
        return true;
    }
    
    public boolean replenishDispenser(Inventory inventory, ItemStack itemDispensed) {
    	server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                inventory.addItem(itemDispensed);
            }
        }, 1L);
		return true;
    }
}
