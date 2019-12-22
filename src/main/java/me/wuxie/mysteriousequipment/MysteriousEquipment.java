package me.wuxie.mysteriousequipment;

import lombok.Getter;
import lombok.Setter;
import me.wuxie.mysteriousequipment.gui.CompositeGui;
import me.wuxie.mysteriousequipment.gui.SuckingTableGui;
import me.wuxie.mysteriousequipment.itemstack.ItemStackManager;
import me.wuxie.mysteriousequipment.listener.InventoryClickListener;
import me.wuxie.mysteriousequipment.listener.MythicMobsDeathListener;
import me.wuxie.mysteriousequipment.lores.AttributeSeparator;
import me.wuxie.mysteriousequipment.randomsamplingquality.QualityManager;
import me.wuxie.mysteriousequipment.thesaurus.ThesaurusManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class MysteriousEquipment extends JavaPlugin {
    @Getter
    private static Random r;
    @Getter
    private static JavaPlugin plugin;
    @Getter
    private static String[] colors;
    @Getter
    @Setter
    private static DecimalFormat df;
    @Getter
    private static Message message;

    @Override
    public void onEnable() {
        plugin = this;
        new Metrics(this);
        colors = new String[]{"§1","§2","§3","§4","§5","§6","§7","§9","§a","§b","§c","§d","§e","§f"};
        r=new Random();
        saveDefaultConfig();
        df = new DecimalFormat(getConfig().getString("decimalFormat"));
        message = new Message(getConfig(),this);
        SuckingTableGui.createGui();
        CompositeGui.createGui();
        ThesaurusManager.load();
        ItemStackManager.load();
        QualityManager.load();
        AttributeSeparator.load();

        Bukkit.getPluginCommand("mysteriousequipment").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(),this);
        if(Bukkit.getPluginManager().getPlugin("MythicMobs")!=null){
            Bukkit.getPluginManager().registerEvents(new MythicMobsDeathListener(),this);
        }
    }

    @Override
    public void onDisable() {
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length>0){
            String permission = this.getName();
            if(sender instanceof Player){
                if(args[0].equalsIgnoreCase("get")) {
                    if(sender.hasPermission(permission+".get")){
                        if(args.length>1){
                            if (args[1].equalsIgnoreCase("xifu")) {
                                int amount = 1;
                                if(args.length>2) amount = Integer.parseInt(args[2]);

                                ((Player) sender).getInventory().addItem(AttributeSeparator.getDefaultitemClone(amount));
                            }else if (args[1].equalsIgnoreCase("sjitem")&&args.length>2) {
                                int amount = 1;
                                if(args.length>3)amount = Integer.parseInt(args[3]);
                                ItemStack sjitem = QualityManager.getQualityMap().get(Integer.parseInt(args[2])).getIs().clone();
                                sjitem.setAmount(amount);
                                ((Player) sender).getInventory().addItem(sjitem);
                            } else{
                                int qualityid=-1;
                                int amount= 1;
                                if(args.length>=3){
                                    String s = args[2];
                                    if(s!=null){
                                        qualityid = Integer.parseInt(s);
                                    }
                                    if(args.length>3){
                                        String s2 = args[3];
                                        amount = Integer.parseInt(s2);
                                    }
                                }
                                List<ItemStack> iss = new ArrayList<>();
                                for(int a = 0;a<amount;){
                                    iss.add(ItemStackManager.getRandomIrem(args[1],qualityid));
                                    a+=1;
                                }
                                for(ItemStack i:iss){
                                    if(i!=null)((Player) sender).getInventory().addItem(i);
                                }
                            }
                        }
                    }else sender.sendMessage("§c[§eMEquipment§c] §7权限不足，你需要 "+permission+".get 权限！");

                    return true;
                }else if(args[0].equalsIgnoreCase("open")){
                    if(sender.hasPermission(permission+".open")){
                        if(args.length>1){
                            if(args[1].equalsIgnoreCase("chou")){
                                Inventory inv = SuckingTableGui.cloneInventory((Player) sender);
                                ((Player) sender).openInventory(inv);
                            }else if(args[1].equalsIgnoreCase("fu")){
                                Inventory inv = CompositeGui.cloneInventory((Player) sender);
                                ((Player) sender).openInventory(inv);
                            }else {
                                help(sender,label);
                            }
                        }
                    }else  sender.sendMessage("§c[§eMEquipment§c] §7权限不足，你需要 "+permission+".open 权限！");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("reload")&&sender.hasPermission(getName()+".reload")){
                reloadConfig();
                df = new DecimalFormat(getConfig().getString("decimalFormat"));
                message.load();
                ThesaurusManager.load();
                ItemStackManager.load();
                QualityManager.load();
                AttributeSeparator.load();
                sender.sendMessage("§c[§eMEquipment§c] §7§a重载成功！");
                return true;
            }else {
                help(sender,label);
            }
            return true;
        }
        help(sender,label);
        return true;
    }
    private void help(CommandSender sender,String label){
        sender.sendMessage("§7§m=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        if(sender.hasPermission(getName()+".get")){
            sender.sendMessage("§c/"+label+" get xifu [amount] §7发送吸附水晶给输入命令的玩家!");
            sender.sendMessage("§c/"+label+" get sjitem [品质id] [amount] §7发送品质水晶给输入命令的玩家!");
            sender.sendMessage("§c/"+label+" get [物品id] [品质id|§f-1§c] [amount] §7发送[对应|§f随机§7]品质随机装备给输入命令的玩家!");
        }
        if(sender.hasPermission(getName()+".open")){
            sender.sendMessage("§c/"+label+" open chou §7为输入命令的玩家打开抽属性界面!");
            sender.sendMessage("§c/"+label+" open fu §7为输入命令的玩家打开添加属性界面!");
        }
        if(sender.hasPermission(getName()+".reload")){
            sender.sendMessage("§c/"+label+" reload §7重载插件!");
        }
        sender.sendMessage("§7§m=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }
}
