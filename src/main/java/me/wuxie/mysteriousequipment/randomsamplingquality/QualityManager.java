package me.wuxie.mysteriousequipment.randomsamplingquality;

import lombok.Getter;
import me.wuxie.mysteriousequipment.MysteriousEquipment;
import me.wuxie.mysteriousequipment.itemstack.RandomItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class QualityManager {
    @Getter
    private static Map<Integer,Quality> qualityMap;
    @Getter
    private static List<Quality> qualities;
    @Getter
    private static YamlConfiguration qualityYml;
    public static void load(){
        qualityMap = new HashMap<>();
        qualities = new ArrayList<>();
        File file = new File(MysteriousEquipment.getPlugin().getDataFolder(),"quality.yml");
        if(!file.exists()) MysteriousEquipment.getPlugin().saveResource("quality.yml",true);
        qualityYml = YamlConfiguration.loadConfiguration(file);
        Set<String> keys = qualityYml.getKeys(false);
        for (String key : keys) {
            ConfigurationSection cs = qualityYml.getConfigurationSection(key);
            int id = cs.getInt("id");
            String name = cs.getString("name");
            double probability = cs.getDouble("probability");
            int maxLength = cs.getInt("maxLength");
            int minLength = cs.getInt("minLength");
            ItemStack is = null;
            int isamount =0;
            if(cs.get("material")!=null){
                isamount = cs.getInt("material.amount");
                Object iid = cs.get("material.id");
                int data = cs.getInt("material.data");
                String iname = cs.getString("material.name");
                List<String> lore = cs.getStringList("material.lore");
                if(iid instanceof Integer)is = new ItemStack(Material.getMaterial((int) iid),1,(short)data);
                else is = new ItemStack(Material.getMaterial((String) iid),1, (short) data);
                ItemMeta meta = is.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',iname));
                meta.setLore(RandomItemStack.replacecolor(lore));
                is.setItemMeta(meta);
            }
            String color = cs.getString("color");
            color = color!=null?color:"";
            Quality q = new Quality(id,name,probability,minLength,maxLength,is,isamount,color);
            qualities.add(q);
            qualityMap.put(id,q);
        }
    }
}
