package me.wuxie.mysteriousequipment.itemstack;

import lombok.Getter;
import me.wuxie.mysteriousequipment.MysteriousEquipment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemStackManager {
    @Getter
    private static Map<String,RandomItemStack> itemStackMap;
    public static void load(){
        itemStackMap = new HashMap<>();
        File file = new File(MysteriousEquipment.getPlugin().getDataFolder(), "items");
        if(!file.exists()) MysteriousEquipment.getPlugin().saveResource("items/items.yml",true);
        File[] files = file.listFiles();
        if(files!=null){
            for(File f:files){
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
                Set<String> strings = yml.getKeys(false);
                for (String s : strings) {
                    ConfigurationSection cs = yml.getConfigurationSection(s);
                    Object id = cs.get("id");
                    int data = cs.getInt("data");
                    String name = cs.getString("name");
                    String typeid = cs.getString("typeid");
                    List<String> lore = cs.getStringList("lore");
                    List<String> enchantment = cs.getStringList("enchantments");
                    int insert = cs.getInt("insert");
                    String filename = cs.getString("thesaurus.file");
                    List<String> phrases = cs.getStringList("thesaurus.list");
                    String color = cs.getString("color");
                    boolean[] b = new boolean[]{};
                    if(cs.contains("unBreak")){
                        b = new boolean[]{cs.getBoolean("unBreak")};
                    }
                    RandomItemStack randomItemStack = new RandomItemStack(typeid,id,data,name,lore,enchantment,insert,filename,phrases,color,b);
                    itemStackMap.put(s,randomItemStack);
                }
            }
        }
    }

    public static ItemStack getRandomIrem(String s,int qualityId){
        RandomItemStack itemStack = itemStackMap.get(s);
        if(itemStack!=null)return itemStack.create(qualityId);
        return null;
    }
}
