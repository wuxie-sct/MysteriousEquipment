package me.wuxie.mysteriousequipment.listener;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.wuxie.mysteriousequipment.MysteriousEquipment;
import me.wuxie.mysteriousequipment.itemstack.ItemStackManager;
import me.wuxie.mysteriousequipment.lores.AttributeSeparator;
import me.wuxie.mysteriousequipment.randomsamplingquality.QualityManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MythicMobsDeathListener implements Listener {
    @EventHandler
    public void onKillMob(MythicMobDeathEvent e) {
        Player p=null;
        if(e.getKiller()==null) {return;}
        if(e.getKiller().getType().equals(EntityType.PLAYER)) {
            p =(Player) e.getKiller();
        }
        if(p==null) {return;}
        MythicMob mm = e.getMobType();
        List<String> dropList = mm.getDrops();
        List<ItemStack> randdrops =  getRandomDrops(mm, dropList);
        List<ItemStack> drops = e.getDrops();
        drops.addAll(randdrops);
    }
    private List<ItemStack> getRandomDrops(MythicMob mm, List<String> dropList) {
        List<ItemStack> sxdrops = new ArrayList<>();
        for (String str : dropList) {
            if (str.contains(" ")) {
                String[] args = str.split(" ");
                if (args.length > 1 && args[0].equalsIgnoreCase("meitem")||args[0].equalsIgnoreCase("quality")||args[0].equalsIgnoreCase("sjitem")) {
                    int amount = 1;
                        if (args.length > 3 && args[3].length() > 0 && MysteriousEquipment.getR().nextDouble() > Double.valueOf(args[3].replaceAll("[^0-9.]", ""))) {// 几率判断
                        continue;
                    }
                    amount = getAmount(str, args, amount);
                    for(int a=0;a<amount;){
                        a+=1;
                        ItemStack item = null;
                        if(args[0].equalsIgnoreCase("meitem")) {
                            item = ItemStackManager.getRandomIrem(args[1], -1);
                        }else if(args[0].equalsIgnoreCase("quality")) {
                            item = QualityManager.getQualityMap().get(Integer.parseInt(args[1])).getIs();
                        }
                        else if(args[0].equalsIgnoreCase("sjitem")) {
                            item = AttributeSeparator.getDefaultitemClone(1);
                        }
                        if (item != null) {
                            sxdrops.add(item.clone());
                        } else {
                            Bukkit.getConsoleSender().sendMessage("§cMythicmobs怪物: §4" + mm.getDisplayName() + "§c 不存在这个掉落物品: §4 "+args[0]+" "+ args[1]);
                            break;
                        }
                    }
                }
            }
        }
        return sxdrops;
    }
    private int getAmount(String str, String[] args, int amount) {
        if (args.length > 2 && args[2].length() > 0) {// 数量判断
            if (args[2].contains("-") && args[2].split("-").length > 1) {
                int i1 = Integer.valueOf(args[2].split("-")[0].replaceAll("[^0-9]", ""));
                int i2 = Integer.valueOf(args[2].split("-")[1].replaceAll("[^0-9]", ""));
                if (i1 > i2) {
                    Bukkit.getConsoleSender().sendMessage("§c随机数大小不正确!: §4" + str);
                } else {
                    amount = MysteriousEquipment.getR().nextInt(i2 - i1 + 1) + i1;
                }
            } else {
                amount = Integer.valueOf(args[2].replaceAll("[^0-9]", ""));
            }
        }
        return amount;
    }
}
