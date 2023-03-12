package com.github.strawberrycat_ni.nekomurahammerplugin;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class NekomuraHammerPlugin extends JavaPlugin implements Listener{

	FileConfiguration nhpconfig;

    @Override
    public void onEnable() {
        // TODO ここに、プラグインが有効化された時の処理を実装してください。
    	saveDefaultConfig();
    	nhpconfig = getConfig();
    	getLogger().info("NekomuraHammerPlugin is enabled.");
    }

    @Override
    public void onDisable() {
        // TODO ここに、プラグインが無効化された時の処理を実装してください。
    	getLogger().info("NekomuraHammerPlugin is disabled.");
    }

/*    @EventHandler
//    public void onBreakBlock(BlockBreakEvent e) {
//    	getLogger().info("BreakBlock.");
//    	if(checkHammer(e) ) {
//    		breakBlockSystem(e);
//   	}
//
    }
*/

    @EventHandler
    public void checkHammer(BlockBreakEvent e) {
    	e.getPlayer().sendMessage("プレイヤーがログインしました: " + e.getPlayer().getName());
    	if(e.getBlock().isPreferredTool(e.getPlayer().getItemInUse())) {
    		getLogger().info("Toolcheck.");
        	for(String name: nhpconfig.getStringList("Tool-Setting.Name")) {
        		for(String lore: nhpconfig.getStringList("Tool-Settings.Lore")) {
        			if(name == e.getPlayer().getItemInUse().getItemMeta().getDisplayName() && lore == e.getPlayer().getItemInUse().getItemMeta().getLore().get(1)) {

        				getLogger().info("ToolTrue.");
        				int range = nhpconfig.getInt("Tool-Setting.Range");
        		    	Block center = e.getBlock();
        		    	Block current = center;
        		    	double centerx = center.getLocation().getBlockX();
        		    	double centery = center.getLocation().getBlockY();
        		    	double centerz = center.getLocation().getBlockZ();
        		    	ItemStack tool = e.getPlayer().getItemInUse();

        		    	for(int x=-range ; x<=range ; x++) {
        		    		for(int y=-range ; y<=range ; y++) {
        		    			for(int z=-range ; z<=range ; z++) {
        		    				current.getLocation().setX(centerx + x);
        		    				current.getLocation().setY(centery + y);
        		    				current.getLocation().setZ(centerz + z);
        		    				if(current.isPreferredTool(tool)) {
        		    					getLogger().info("PreferredTool.");
        		    					e.getPlayer().breakBlock(current);
        		    				}
        		    				String blockname = current.toString();
        		    				getLogger().info(blockname);
        		    	    	}
        		        	}
        		    	}

        			}
        		}
        	}
    	}
    	getLogger().info("ToolFalse.");

    }


/*    public boolean checkWorld(BlockBreakEvent e) {
    	for(String world: nhpconfig.getStringList("Setting.Enable-World")) {
    		if(e.getPlayer().getWorld().toString() == world) {
    			getLogger().info("WorldTrue.");
    			return true;
    		}
    	}
    	return false;
    }
*/

/*    public void breakBlockSystem(BlockBreakEvent e) {
    	int range = nhpconfig.getInt("Tool-Setting.Range");
    	Block center = e.getBlock();
    	Block current = center;
    	double centerx = center.getLocation().getBlockX();
    	double centery = center.getLocation().getBlockY();
    	double centerz = center.getLocation().getBlockZ();
    	ItemStack tool = e.getPlayer().getItemInUse();

    	for(int x=-range ; x<=range ; x++) {
    		for(int y=-range ; y<=range ; y++) {
    			for(int z=-range ; z<=range ; z++) {
    				current.getLocation().setX(centerx + x);
    				current.getLocation().setY(centery + y);
    				current.getLocation().setZ(centerz + z);
    				if(current.isPreferredTool(tool)) {
    					getLogger().info("PreferredTool.");
    					e.getPlayer().breakBlock(current);
    				}
    				String blockname = current.toString();
    				getLogger().info(blockname);
    	    	}
        	}
    	}
    }
*/
}
