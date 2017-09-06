package yy.zacharyguard.yeyadisp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class BlockDispenseEventHandler implements Listener {
	
	Main plugin;
	Server server = Bukkit.getServer();
	
	public BlockDispenseEventHandler(Main instance) {
		plugin = instance;
	}
	
	@EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
    	Block block = event.getBlock();
    	if (block.getType() == Material.DISPENSER) {
    		Container dispenser = (Container) block.getState();
    		Location dispenserLocation = dispenser.getLocation();
    		if (plugin.yDispVectors.contains(dispenserLocation.toVector())) {
    			plugin.replenishDispenser(dispenserLocation);
    		}
    	}
    }
	
}
