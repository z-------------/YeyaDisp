package yy.zacharyguard.yeyadisp;

import java.util.ArrayList;
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

public class Main extends JavaPlugin {
	
	Logger logger = getLogger();
	Server server = getServer();
	
	List<Location> yDispLocations = new ArrayList<Location>();
	
	@Override
    public void onEnable() {
		logger.info("YeyaDisp ready.");
		Bukkit.getPluginManager().registerEvents(new BlockDispenseEventHandler(this), this); // register EventHandlers
    }
   
    @Override
    public void onDisable() {
       
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
        	Block targetBlock = player.getTargetBlock(null, 5);
        	if (targetBlock.getType() == Material.DISPENSER) {
        		player.sendMessage("Targeted block is dispenser. Proceeding.");
        		Container dispenser = (Container) targetBlock.getState();
        		Location dispenserLocation = dispenser.getLocation();
        		yDispLocations.add(dispenserLocation);
        		replenishDispenser(dispenserLocation);
        	} else {
        		player.sendMessage("Targeted block is not dispenser. Aborting.");
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
