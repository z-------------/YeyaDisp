package yy.zacharyguard.yeyadisp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	Logger logger = getLogger();
	
	List<Container> yeyaDisps = new ArrayList<Container>();
	
	@Override
    public void onEnable() {
		logger.info("YeyaDisp ready.");
    }
   
    @Override
    public void onDisable() {
       
    }
    
    @Override
    public boolean onCommand(CommandSender sender,
            Command command,
            String label,
            String[] args) {
        if (command.getName().equalsIgnoreCase("yeyadisp") && sender instanceof Player) {
        	LivingEntity player = (LivingEntity) sender;
        	Block targetBlock = player.getTargetBlock(null, 5);
        	if (targetBlock.getType() == Material.DISPENSER) {
        		player.sendMessage("Targeted block is dispenser. Proceeding.");
        		Container dispenser = (Container) targetBlock;
        		yeyaDisps.add(dispenser);
        	} else {
        		player.sendMessage("Targeted block is not dispenser. Aborting.");
        	}
        	
        	return true;
        }
        
        return false;
    }
    
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
    	Block block = event.getBlock();
    	ItemStack itemDispensed = event.getItem();
    	
    	if (block.getType() == Material.DISPENSER) {
    		Container dispenser = (Container) block;
    		MetadataValue containedItems = dispenser.getMetadata("Items").get(0);
    		// TODO: loop through contained items and set each count to 64, then re-add the itemDispensed if there isn't already an ItemStack with its type and data.
    	}
    }
}
