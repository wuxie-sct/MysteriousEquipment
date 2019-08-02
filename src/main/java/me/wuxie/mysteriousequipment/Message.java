package me.wuxie.mysteriousequipment;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private FileConfiguration c;
    public Message(FileConfiguration c,MysteriousEquipment p){
        this.c = c;
        load();
    }
    public static String c_guiInfo;
    public static String c_clickInfo_name;
    public static List<String> c_clickInfo_lore;

    public static String c_clickStartInfo_name;
    public static List<String> c_clickStartInfo_lore;

    public static String s_guiInfo;
    public static String s_clickStartInfo_name;
    public static List<String> s_clickStartInfo_lore;

    public static String growUp;
    public static String noGrowUp;
    public static String maxQuality;

    public static String m_c_noMEItem;
    public static String m_c_noAttribute;
    public static String m_c_noXiFu;
    public static String m_c_noPlace;
    public static String m_c_success;
    public static String m_c_fail;

    public static String m_f_noPlace;
    public static String m_f_noMEItem;
    public static String m_f_noGrowUp;
    public static String m_f_noSjItem;
    public static String m_f_sjItemError;
    public static String m_f_sjItemNum;
    public static String m_f_moreAttr;
    public static String m_f_typeError;
    public static String m_f_maxAttr;
    public static String m_f_noXiFu;
    public static String m_f_up;
    public static String m_f_add;
    public static String m_f_qualityError;
    public static String m_f_maxQuality;

    public void load(){
        ConfigurationSection compositeGui = c.getConfigurationSection("compositeGui");
        c_guiInfo = compositeGui.getString("guiInfo");
        c_clickInfo_name = compositeGui.getString("clickInfo.name");
        c_clickInfo_lore = compositeGui.getStringList("clickInfo.lore");
        c_clickStartInfo_name = compositeGui.getString("clickStartInfo.name");
        c_clickStartInfo_lore = compositeGui.getStringList("clickStartInfo.lore");

        ConfigurationSection suckingTableGui = c.getConfigurationSection("suckingTableGui");
        s_guiInfo = suckingTableGui.getString("guiInfo");
        s_clickStartInfo_name = suckingTableGui.getString("clickStartInfo.name");
        s_clickStartInfo_lore = suckingTableGui.getStringList("clickStartInfo.lore");

        growUp = c.getString("itemstack.growUp");
        noGrowUp = c.getString("itemstack.noGrowUp");
        maxQuality = c.getString("itemstack.maxQuality");

        ConfigurationSection chou = c.getConfigurationSection("message.chou");
        m_c_noAttribute = chou.getString("noAttribute");
        m_c_fail = chou.getString("fail");
        m_c_noMEItem = chou.getString("noMEItem");
        m_c_noPlace = chou.getString("noPlace");
        m_c_noXiFu = chou.getString("noXiFu");
        m_c_success =  chou.getString("success");

        ConfigurationSection fu = c.getConfigurationSection("message.fu");
        m_f_add = fu.getString("add");
        m_f_maxAttr =  fu.getString("maxAttr");
        m_f_maxQuality =  fu.getString("maxQuality");
        m_f_moreAttr =  fu.getString("moreAttr");
        m_f_noGrowUp =  fu.getString("noGrowUp");
        m_f_noMEItem =  fu.getString("noMEItem");
        m_f_noPlace =  fu.getString("noPlace");
        m_f_qualityError =  fu.getString("qualityError");
        m_f_up =  fu.getString("up");
        m_f_typeError =  fu.getString("typeError");
        m_f_sjItemError =  fu.getString("sjItemError");
        m_f_noSjItem =  fu.getString("noSjItem");
        m_f_sjItemNum =  fu.getString("sjItemNum");
        m_f_noXiFu =  fu.getString("noXiFu");
    }

    public static void send(Player player, String message, Object...args){
        player.sendMessage(getMsg(message,args));
    }
    public static String getMsg(String message, Object...args){
        message = ChatColor.translateAlternateColorCodes('&',message);
        return MessageFormat.format(message,args);
    }
    public static List<String> getMsg(List<String> messages, Object...args){
        List<String> msg = new ArrayList<>();
        for(String s:messages){
            msg.add(MessageFormat.format(ChatColor.translateAlternateColorCodes('&',s),args));
        }
        return msg;
    }
}
