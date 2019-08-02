package me.wuxie.mysteriousequipment.itemstack;

import de.tr7zw.itemnbtapi.NBTItem;
import lombok.Getter;
import me.wuxie.mysteriousequipment.Message;
import me.wuxie.mysteriousequipment.MysteriousEquipment;
import me.wuxie.mysteriousequipment.randomsamplingquality.DrawLotteryUtil;
import me.wuxie.mysteriousequipment.randomsamplingquality.Quality;
import me.wuxie.mysteriousequipment.randomsamplingquality.QualityManager;
import me.wuxie.mysteriousequipment.thesaurus.Phrase;
import me.wuxie.mysteriousequipment.thesaurus.Thesaurus;
import me.wuxie.mysteriousequipment.thesaurus.ThesaurusManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public class RandomItemStack {
    @Getter private Object id;
    @Getter private int data;
    @Getter private String name;
    @Getter private List<String> lore;
    @Getter private List<String> enchants;
    @Getter private int insertLine;
    @Getter private String filename;
    @Getter private String typeid;
    @Getter private String color;
    @Getter private List<String> phrases;
    @Getter private Thesaurus thesaurus;

    public RandomItemStack(String typeid,Object id, int data, String name, List<String> lore, List<String> enchants, int insertLine, String filename, List<String> phrases,String color){
        this.id = id;
        this.data=data;
        this.name=name;
        this.lore=lore;
        this.enchants=enchants;
        this.insertLine=insertLine;
        this.filename=filename;
        this.phrases=phrases;
        this.typeid=typeid;
        this.color=color;
        thesaurus = ThesaurusManager.getThesaurus(filename);
    }
    public ItemStack create(int qualityId){
        ItemStack item;
        Material material;
        if(id instanceof Integer)material = Material.getMaterial((int) id);
        else material = Material.getMaterial((String) id);
        item = new ItemStack(material,1, (short) data);
        List<String> lore = new ArrayList<>(this.lore);
        List<String> enchants = new ArrayList<>(this.enchants);
        Map<Enchantment,Integer> map = new HashMap<>();
        for (String enchant : enchants) {
            if(enchant.contains(":")&&enchant.split(":").length>1){
                String enchname = enchant.split(":")[0];
                int level = 0;
                try{
                    level = Integer.parseInt(enchant.split(":")[1]);
                }catch (Exception e){
                    Bukkit.getConsoleSender().sendMessage("§c附魔数字解析错误");
                    e.printStackTrace();
                }
                Enchantment e = Enchantment.getByName(enchname);
                map.put(e,level);
            }
        }
        item.addUnsafeEnchantments(map);
        ItemMeta meta = item.getItemMeta();
        if(material.name().contains("LEATHER")){
            if(color!=null){
                LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                int blue;int green; int red;
                if (color.equals("random")) {
                    blue = MysteriousEquipment.getR().nextInt(256);
                    green = MysteriousEquipment.getR().nextInt(256);
                    red = MysteriousEquipment.getR().nextInt(256);
                }else {
                    blue = Integer.parseInt(color.split(",")[0]);
                    green = Integer.parseInt(color.split(",")[1]);
                    red = Integer.parseInt(color.split(",")[2]);
                }
                armorMeta.setColor(Color.fromBGR(blue, green, red));
                meta = armorMeta;
            }
        }
        meta.setUnbreakable(true);
        int maxloc = insertLine;
        Quality quality;
        if(qualityId==-1){
            //品质决定条数
            int chou = DrawLotteryUtil.drawGift(QualityManager.getQualities());
            quality = QualityManager.getQualities().get(chou);
        }else quality = QualityManager.getQualities().get(qualityId);
        int maxnum = quality.getMaxAttribute();
        int minnum = quality.getMinAttribute();
        int qualityid = quality.getId();
        //随机lore内部
        doString(lore,quality);
        doString(enchants,null);
        List<String> s = new ArrayList<>();
        if(thesaurus!=null){
            List<String> phlist = new ArrayList<>();
            List<String> p = listRandomClone();
            Map<Phrase,Integer> phrases = new HashMap<>();
            for (String phrase : p) {
                Phrase phaser = thesaurus.getPhrase(phrase);
                if(phaser!=null){
                    List<String> l = phaser.getRandomPhrase(maxnum);
                    maxnum-=l.size();
                    minnum-=l.size();
                    if(l.size()==1){
                        phrases.put(phaser,1);
                    }if(l.size()==0){
                        phrases.put(phaser,2);
                    }
                    if(l.size()>0){
                        phlist.addAll(l);
                        StringBuilder sb = new StringBuilder();
                        Iterator<String> li =l.iterator();
                        while (li.hasNext()){
                            sb.append(insertLine+phlist.indexOf(li.next()));
                            if(li.hasNext())sb.append("#");
                        }
                        s.add(phrase+":"+sb);
                    }
                }
            }
            if(minnum>0){
                for(Map.Entry<Phrase,Integer> phs :phrases.entrySet()){
                    if(minnum-phs.getValue()>=0){
                        minnum-=phs.getValue();
                        List<String> l = phs.getKey().getListPhrases(phs.getValue());
                        phlist.addAll(l);
                        StringBuilder sb = new StringBuilder();
                        Iterator<String> li =l.iterator();
                        while (li.hasNext()){
                            sb.append(insertLine+phlist.indexOf(li.next()));
                            if(li.hasNext())sb.append("#");
                        }
                        s.add(phs.getKey().getName()+":"+sb);
                        if(minnum<=0)break;
                    }else {
                        int a = phs.getValue()-minnum;
                        List<String> l = phs.getKey().getListPhrases(a);
                        phlist.addAll(l);
                        StringBuilder sb = new StringBuilder();
                        Iterator<String> li =l.iterator();
                        while (li.hasNext()){
                            sb.append(insertLine+phlist.indexOf(li.next()));
                            if(li.hasNext())sb.append("#");
                        }
                        s.add(phs.getKey().getName()+":"+sb);
                        break;
                    }
                }
            }
            maxloc+=phlist.size()-1;
            if(phlist.size()<=0)maxloc=insertLine-1;else lore.addAll(insertLine,replacecolor(phlist));
        }
        meta.setLore(lore);
        String name = ChatColor.translateAlternateColorCodes('&',Phrase.getString(this.name, Phrase.getStringList(this.name)));
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        NBTItem nitem = new NBTItem(item);
        if(quality.getId()==0){
            nitem.setBoolean("isCulturable",true);
        }else nitem.setBoolean("isCulturable",false);
        nitem.setInteger("attributeLength",maxloc-insertLine+1);
        nitem.setInteger("qualityId",qualityid);
        nitem.setInteger("minLoc",insertLine);
        nitem.setInteger("maxLoc",maxloc);
        nitem.setString("typeId",typeid);
        StringBuilder sb1 = new StringBuilder();
        Iterator<String> si =s.iterator();
        while (si.hasNext()){
            sb1.append(si.next());
            if(si.hasNext())sb1.append(",");
        }
        nitem.setString("locMsg",sb1.toString());
        return nitem.getItem();
    }

    private void doString(List<String> lore,Quality q ) {
        for(String s:lore){
            int loc = lore.indexOf(s);
            List<String> sl = Phrase.getStringList(s);
            s = Phrase.getString(s, sl);
            if(q!=null)s = s.replace("%p%",ChatColor.translateAlternateColorCodes('&',q.getName()));
            s = s.replace("%t%",ChatColor.translateAlternateColorCodes('&',typeid));
            if(q!=null)s = s.replace("%py%",ChatColor.translateAlternateColorCodes('&',q.getId()==0? Message.getMsg(Message.growUp):Message.getMsg(Message.noGrowUp)));
            lore.set(loc, ChatColor.translateAlternateColorCodes('&',s));
        }
    }

    /**
     * 打乱顺序
     * @return
     */
    private List<String> listRandomClone(){
        List<String> phlists = new ArrayList<>(phrases);
        List<String> p = new ArrayList<>();
        for (int a=0;a<phlists.size();++a){
            int loc = MysteriousEquipment.getR().nextInt(phlists.size());
            p.add(phlists.get(loc));
            phlists.remove(loc);
        }
        return p;
    }

    public static List<String> replacecolor(List<String> list){
        List<String> p = new ArrayList<>();
        for (String s : list) {
            p.add(ChatColor.translateAlternateColorCodes('&',s));
        }
        return p;
    }

    /**
     * 判断是否是随机装备
     * @param item
     * @return
     */
    public static boolean isRandomItem(ItemStack item){
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey("qualityId");
    }

    /**
     * 判断是否可培养
     * @param item
     * @return
     */
    public static boolean isCulturable(ItemStack item){
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey("qualityId")&&nbtItem.getBoolean("isCulturable");
    }
}
