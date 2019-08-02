package me.wuxie.mysteriousequipment.gui;

import lombok.Getter;
import me.wuxie.mysteriousequipment.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CompositeGui {
    @Getter
    private static Inventory inventory;
    public static void createGui(){
        ItemStack is2 = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 15);//黑
        ItemStack is6 = new ItemStack(Material.ENDER_PORTAL_FRAME);//末地传送门
        ItemStack is3 = new ItemStack(Material.END_CRYSTAL);//末影水晶
        ItemStack is1 = new ItemStack(Material.HOPPER);//漏斗
        ItemStack is4 = new ItemStack(Material.ANVIL);//铁砧
        ItemMeta is4meta = is4.getItemMeta();
        is4meta.setDisplayName(Message.getMsg(Message.c_clickInfo_name));
        is4.setItemMeta(is4meta);
        ItemMeta is1meta = is1.getItemMeta();
        is1meta.setDisplayName(Message.getMsg(Message.c_clickStartInfo_name));
        List<String> lore = Message.getMsg(Message.c_clickStartInfo_lore);
        is1meta.setLore(lore);
        is1.setItemMeta(is1meta);
        SuckingTableGui.setName(is2);
        SuckingTableGui.setName(is6);
        SuckingTableGui.setName(is3);
        inventory = Bukkit.createInventory(null,54,Message.getMsg(Message.c_guiInfo));
        for(int a=0;a<9;++a){
            inventory.setItem(a,is2);
        }
        for(int a=27;a<53;++a){
            inventory.setItem(a,is2);
        }
        inventory.setItem(9,is2);
        inventory.setItem(17,is2);
        inventory.setItem(18,is2);
        inventory.setItem(26,is2);
        inventory.setItem(31,is4);
        inventory.setItem(39,null);
        inventory.setItem(40,is1);
        inventory.setItem(41,null);
        inventory.setItem(45,is6);
        inventory.setItem(49,is3);
        inventory.setItem(53,is6);
    }
    public static Inventory cloneInventory(Player player){
        Inventory inv = Bukkit.createInventory(player,54,Message.getMsg(Message.c_guiInfo));
        inv.setContents(inventory.getContents());
        return inv;
    }
}
