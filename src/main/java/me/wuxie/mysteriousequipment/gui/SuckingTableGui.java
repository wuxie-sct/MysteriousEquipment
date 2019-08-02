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

public class SuckingTableGui {
    @Getter
    public static Inventory inventory;

    public static void createGui(){
        ItemStack is1 = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 14);//红
        ItemStack is2 = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 15);//黑
        ItemStack is3 = new ItemStack(Material.END_CRYSTAL);//末影水晶
        ItemStack is4 = new ItemStack(Material.SNOW_BALL);//雪球
        ItemStack is5 = new ItemStack(Material.EYE_OF_ENDER);//末影之眼
        ItemStack is6 = new ItemStack(Material.ENDER_PORTAL_FRAME);//末地传送门
        ItemStack is7 = new ItemStack(Material.REDSTONE_TORCH_ON);//红石火把
        ItemStack is8 = new ItemStack(Material.FERMENTED_SPIDER_EYE);//发酵蜘蛛眼
        ItemStack is9 = new ItemStack(Material.SPIDER_EYE);//蜘蛛眼
        ItemStack is10 = new ItemStack(Material.getMaterial(372));//地狱疣
        ItemMeta is5meta = is1.getItemMeta();
        is5meta.setDisplayName(Message.getMsg(Message.s_clickStartInfo_name));
        List<String> lore = Message.getMsg(Message.s_clickStartInfo_lore);

        is5meta.setLore(lore);
        is5.setItemMeta(is5meta);
        setName(is1);
        setName(is2);
        setName(is3);
        setName(is4);
        setName(is6);
        setName(is7);
        setName(is8);
        setName(is9);
        setName(is10);
        inventory = Bukkit.createInventory(null,54,Message.getMsg(Message.s_guiInfo));
        inventory.setItem(0,is1);inventory.setItem(1,is1);inventory.setItem(2,is1);inventory.setItem(3,is2);
        inventory.setItem(4,is2);inventory.setItem(5,is2);inventory.setItem(6,is1);inventory.setItem(7,is1);
        inventory.setItem(8,is1);inventory.setItem(9,is1);inventory.setItem(11,is1);inventory.setItem(12,is2);
        inventory.setItem(13,is7);inventory.setItem(14,is2);inventory.setItem(15,is1);inventory.setItem(17,is1);
        inventory.setItem(18,is2);inventory.setItem(19,is8);inventory.setItem(20,is2);inventory.setItem(21,is3);
        inventory.setItem(22,is4);inventory.setItem(23,is3);inventory.setItem(24,is2);inventory.setItem(25,is8);
        inventory.setItem(26,is2);inventory.setItem(27,is2);inventory.setItem(28,is9);inventory.setItem(29,is2);
        inventory.setItem(30,is4);inventory.setItem(31,is3);inventory.setItem(32,is4);inventory.setItem(33,is2);
        inventory.setItem(34,is9);inventory.setItem(35,is2);inventory.setItem(36,is2);inventory.setItem(37,is10);
        inventory.setItem(38,is2);inventory.setItem(39,is3);inventory.setItem(40,is4);inventory.setItem(41,is3);
        inventory.setItem(42,is2);inventory.setItem(43,is10);inventory.setItem(44,is2);inventory.setItem(45,is2);
        inventory.setItem(46,is6);inventory.setItem(47,is2);inventory.setItem(48,is2);inventory.setItem(49,is5);
        inventory.setItem(50,is2);inventory.setItem(51,is2);inventory.setItem(52,is6);inventory.setItem(53,is2);
    }

    public static void setName(ItemStack is1) {
        ItemMeta is1meta = is1.getItemMeta();
        is1meta.setDisplayName(" ");
        is1.setItemMeta(is1meta);
    }
    public static Inventory cloneInventory(Player player){
        Inventory inv = Bukkit.createInventory(player,54,Message.getMsg(Message.s_guiInfo));
        inv.setContents(inventory.getContents());
        return inv;
    }
}
