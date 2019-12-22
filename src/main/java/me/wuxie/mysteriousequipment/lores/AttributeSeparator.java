package me.wuxie.mysteriousequipment.lores;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.wuxie.mysteriousequipment.MysteriousEquipment;
import me.wuxie.mysteriousequipment.itemstack.RandomItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;

public class AttributeSeparator {
    public AttributeSeparator(){}
    @Getter
    public static double probability;
    @Getter
    public static ItemStack defaultItem;
    @Getter
    public static ItemStack adsorbateItem;
    public static void load(){
        File file = new File(MysteriousEquipment.getPlugin().getDataFolder(),"adsorbate.yml");
        if(!file.exists()) MysteriousEquipment.getPlugin().saveResource("adsorbate.yml",true);
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        Object defaultid = yml.get("DefaultItem.id");
        int defaultdata = yml.getInt("DefaultItem.data");
        String defaultnaame = ChatColor.translateAlternateColorCodes('&',yml.getString("DefaultItem.name"));
        List<String> defaultlore = RandomItemStack.replacecolor(yml.getStringList("DefaultItem.lore"));
        probability = yml.getDouble("DefaultItem.probability");
        Material m;
        if(defaultid instanceof Integer){
            m = Material.getMaterial((int) defaultid);
        }else m = Material.getMaterial((String) defaultid);
        defaultItem = new ItemStack(m,1, (short) defaultdata);
        ItemMeta dmeta = defaultItem.getItemMeta();
        dmeta.setDisplayName(defaultnaame);
        dmeta.setLore(defaultlore);
        dmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        dmeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        dmeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        dmeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        dmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        dmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        defaultItem.setItemMeta(dmeta);
        Material am;
        Object id = yml.get("Adsorbate.id");
        int data = yml.getInt("Adsorbate.data");
        String name = ChatColor.translateAlternateColorCodes('&',yml.getString("Adsorbate.name"));
        List<String> lore = RandomItemStack.replacecolor(yml.getStringList("Adsorbate.lore"));
        if(id instanceof Integer){
            am = Material.getMaterial((int) id);
        }else am = Material.getMaterial((String) id);
        adsorbateItem = new ItemStack(am,1, (short) data);
        ItemMeta meta = adsorbateItem.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        adsorbateItem.setItemMeta(meta);
    }
    public static ItemStack getDefaultitemClone(int amount){
        ItemStack item = defaultItem.clone();
        item.setAmount(amount);
        return item;
    }
    public static boolean isAdsorbateItem(ItemStack itemStack){
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey("REType")&&nbtItem.hasKey("REAttribute")&&nbtItem.hasKey("REPhrase");
    }
    public static ItemStack adsorbateItemInstantiation(String type,String attribute,String phrase){
        ItemStack itemStack = adsorbateItem.clone();
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        for(String l:lore){
            int loc = lore.indexOf(l);
            if(l.contains("%type%"))l=l.replace("%type%",type);
            if(l.contains("%phrase%"))l=l.replace("%phrase%",phrase);
            if(l.contains("%attribute%"))l=l.replace("%attribute%","§X"+attribute);
            lore.set(loc,l);
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("REType",type);
        nbtItem.setString("REAttribute",attribute);
        nbtItem.setString("REPhrase",phrase);
        return nbtItem.getItem();
    }
}
