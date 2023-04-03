package com.github.strawberrycat_ni.nekomurahammerplugin;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class NekomuraHammerPlugin extends JavaPlugin implements Listener, CommandExecutor{
	FileConfiguration nhpconfig;
	boolean loopproof = false;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
    	saveDefaultConfig();
    	nhpconfig = getConfig();
		System.out.println("プラグインが有効になりました");
	}

	@Override
	public void onDisable() {
		System.out.println("プラグインが無効になりました");
	}


    @EventHandler
    public void breakBlock(BlockBreakEvent e) {

    	boolean checkenableworld = false;
    	for(String enableworld : nhpconfig.getStringList("Worlds")) {
			if (e.getPlayer().getWorld().getName().equals(enableworld)) {
				checkenableworld = true;
				break;
			}
		}//↑configで設定されたワールドかどうかを確認する

    	if(!loopproof && checkenableworld) {

        		for(String toolsetting: nhpconfig.getConfigurationSection("Tool-Settings").getKeys(false)) {
					//↑ツールを複数設定できるようにするためconfig内のTool-Settingsの直下の要素を1つづつtoolsetting変数に入れて以下の処理を行う
					//メインハンドのアイテムが複数のツールの条件に当てはまっている場合、プレイヤーの壊したブロックが設定されているツールの特性を全て併せ持つものとして働く
        			if(toolCheck(e,toolsetting)) {//持ってるアイテムがハンマーかを確認する

        				int range = nhpconfig.getInt("Tool-Settings."+toolsetting+".Range");
        		    	Block center = e.getBlock();

        		    	boolean centerEnable = false;
	    				for(String WhitelistBlock : nhpconfig.getStringList("Tool-Settings."+toolsetting+".BlockWhitelist")) {
	    					if(center.getType().toString().equals(WhitelistBlock)) {
	    						centerEnable = true;
	    						break;
	    					}
	    				}//↑中央(プレイヤーが壊したブロック)がメインハンドのツールで設定されているブロックかどうかを判定する

	    				if(centerEnable) {
        		    		Block current = center;
        		    		double centerx = center.getLocation().getBlockX();
        		    		double centery = center.getLocation().getBlockY();
        		    		double centerz = center.getLocation().getBlockZ();
							//↑中央の
        		    		loopproof = true;//blockBreak関数によってBlockBreakEventが発生して無限ループが発生してしまうことを防ぐ

        		    		for(int x= -1 *range ; x<=range ; x++) {
        		    			for(int y= -1 *range ; y<=range ; y++) {
        		    				for(int z= -1 *range ; z<=range ; z++) {
        		    					current = e.getPlayer().getWorld().getBlockAt((int)centerx + x, (int)centery + y, (int)centerz + z);
										//↑指定された範囲内のブロックを順にターゲットしていく
        		    					for(String enableblock : nhpconfig.getStringList("Tool-Settings."+toolsetting+".BlockWhitelist")) {
        		    						if(current.getType().toString().equals(enableblock)) {
												//↑毎度ホワイトリストに登録されているブロックかを確認する
        		    							e.getPlayer().breakBlock(current);//今ターゲットしているブロックを持っているアイテムでプレイヤーが壊した判定として壊す
        		    							break;
        		    						}
        		    					}
        		    				}
        		    			}
        		    		}
        		    		loopproof = false;
	    				}
        			}
        		}
    	}
    }

    public boolean toolCheck(BlockBreakEvent e ,String toolsetting) {
    	if( e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {//メインハンドのメタデータが存在しない場合getItemMetaでエラーが発生するためここで弾く
    		if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {//メインハンドのLoreが存在しない場合getLoreでエラーが発生するためここで弾く
    			if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().contains(nhpconfig.getString("Tool-Settings."+toolsetting+".Lore"))) {
					return true;//メインハンドのLoreにconfigで設定されたものが含まれている場合にtrueを返す
    			}
    		}
    	}
    	return false;
    }


	 @Override
	    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	        if (command.getName().equalsIgnoreCase("cbreak")) { //親コマンドの判定
	            if (args.length == 0) { //サブコマンドの個数が0、つまりサブコマンド無し
	                return true; //終わり
	            } else { //サブコマンドの個数が0以外
	                if (args[0].equalsIgnoreCase("reload")) { //サブコマンドが「reload」である場合
	                	reloadConfig();
	                	nhpconfig = getConfig();
	                    sender.sendMessage("CubeBreakReload.");
	                    return true;
	                } else { //サブコマンドが「reload」以外
	                }
	                return true; //終わり
	            }
	        }
	        return false;
	    }
}
