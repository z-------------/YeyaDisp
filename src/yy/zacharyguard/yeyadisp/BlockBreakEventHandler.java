package yy.zacharyguard.yeyadisp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEventHandler implements Listener {
	
	Main plugin;
	Server server = Bukkit.getServer();
	
	public BlockBreakEventHandler(Main instance) {
		plugin = instance;
	}
	
	@EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	Block block = event.getBlock();
    	Player player = event.getPlayer();
    	if (block.getType() == Material.DISPENSER) {
    		Container dispenser = (Container) block.getState();
    		Location dispenserLocation = dispenser.getLocation();
    		if (plugin.yDispLocations.contains(dispenserLocation)) {
    			int yDispIndex = plugin.yDispLocations.indexOf(dispenserLocation);
    			plugin.yDispLocations.remove(yDispIndex);
    			player.sendMessage("Successfully removed YeyaDisp abilities from that dispenser.");
    		}
    	}
    }
	
}
