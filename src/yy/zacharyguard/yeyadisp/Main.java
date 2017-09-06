package yy.zacharyguard.yeyadisp;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin {
	
	Logger logger = getLogger();
	Server server = getServer();
	
	List yDispVectors;
	
	@Override
    public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new BlockDispenseEventHandler(this), this); // register EventHandlers
		
		yDispVectors = this.getConfig().getList("yeyadisp_coords");
		logger.info("YeyaDisp ready.");
    }
   
    @Override
    public void onDisable() {
    	logger.info("Saving YeyaDisps to config...");
    	this.getConfig().set("yeyadisp_coords", yDispVectors);
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
        		Vector dispenserLocationVector = dispenserLocation.toVector();
        		if (!yDispVectors.contains(dispenserLocationVector)) {
        			yDispVectors.add(dispenserLocationVector);
            		replenishDispenser(dispenserLocation);
            		player.sendMessage("Successfully created a YeyaDisp.");
        		} else {
        			player.sendMessage("That YeyaDisp already exists.");
        		}
        	} else {
        		player.sendMessage("That block is not a dispenser.");
        	}
        	
        	return true;
        }
        
        return false;
    }
    
    public boolean replenishDispenser(Location location) {
    	Inventory inventory = ((Container) location.getBlock().getState()).getInventory();
		for (int i = 0; i < 9; i++) {
			ItemStack itemStack = inventory.getItem(i);
			if (itemStack != null) {
				itemStack.setAmount(itemStack.getMaxStackSize());
				inventory.setItem(i, itemStack);
			}
		}
		return true;
    }
}
