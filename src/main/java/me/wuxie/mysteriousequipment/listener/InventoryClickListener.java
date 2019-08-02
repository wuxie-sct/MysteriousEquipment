package me.wuxie.mysteriousequipment.listener;

import de.tr7zw.itemnbtapi.NBTItem;
import lombok.Getter;
import me.wuxie.mysteriousequipment.Message;
import me.wuxie.mysteriousequipment.MysteriousEquipment;
import me.wuxie.mysteriousequipment.itemstack.RandomNBTItem;
import me.wuxie.mysteriousequipment.lores.AttributeSeparator;
import me.wuxie.mysteriousequipment.gui.SuckingTableGui;
import me.wuxie.mysteriousequipment.randomsamplingquality.Quality;
import me.wuxie.mysteriousequipment.randomsamplingquality.QualityManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class InventoryClickListener implements Listener {
    @Getter
    private static List<UUID> zaiChouShuXing = new ArrayList<>();
    @EventHandler
    public void onClickChou(InventoryClickEvent e){
        Inventory inv = e.getClickedInventory();
        Player player = (Player) e.getWhoClicked();
        if(zaiChouShuXing.contains(player.getUniqueId())){
            e.setCancelled(true);
        }
        if(e.getSlotType().equals(InventoryType.SlotType.CONTAINER)){
            if(e.getClickedInventory().getTitle()!=null&&e.getClickedInventory().getTitle().equalsIgnoreCase(Message.getMsg(Message.s_guiInfo))){
                e.setCancelled(true);
                if(!(e.getClickedInventory() instanceof PlayerInventory)&&!zaiChouShuXing.contains(player.getUniqueId())){
                    int slot = e.getSlot();
                    if(slot==10||slot==16)e.setCancelled(false);
                    if(slot==31){
                        if(inv.getItem(slot)!=null){
                            if(new NBTItem(inv.getItem(slot)).hasKey("REType")){
                                e.setCancelled(false);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        ItemStack is3 = new ItemStack(Material.END_CRYSTAL);//末影水晶
                                        SuckingTableGui.setName(is3);
                                        e.getClickedInventory().setItem(slot,is3);
                                    }
                                }.runTaskLater(MysteriousEquipment.getPlugin(),1);
                            }
                        }
                    }
                    if(slot==49){
                        ItemStack item = inv.getItem(10);
                        ItemStack item1 = inv.getItem(16);
                        if(item!=null&&item1!=null){
                            NBTItem nbtItem = new NBTItem(item);
                            boolean t = true;
                            if(!RandomNBTItem.isRandomNBTItem(item)){
                                t = false;
                                player.sendMessage(Message.getMsg(Message.m_c_noMEItem));
                            }else if(nbtItem.getInteger("attributeLength")<=0){
                                t = false;
                                player.sendMessage(Message.getMsg(Message.m_c_noAttribute));
                            }
                            if(!item1.isSimilar(AttributeSeparator.getDefaultItem())){
                                t = false;
                                player.sendMessage(Message.getMsg(Message.m_c_noXiFu));
                            }
                            if(t){
                                zaiChouShuXing.add(player.getUniqueId());
                                //item1.setAmount(item1.getAmount()-1);
                                takeItem(inv,item1,1);
                                Effect_1(inv,player);
                                Effect_2(inv);
                                doAbsent(inv, player, item, nbtItem);
                            }
                        }else {
                            player.sendMessage(Message.getMsg(Message.m_c_noPlace));
                        }
                    }
                }
            }
            if(e.getInventory().getTitle()!=null&&e.getInventory().getTitle().equalsIgnoreCase(Message.getMsg(Message.s_guiInfo))&&e.getClickedInventory() instanceof PlayerInventory&&e.isShiftClick())e.setCancelled(true);
        }
    }

    /**
     *  执行属性抽离
     * @param inv gui
     * @param player olayer
     * @param item item
     * @param nbtItem nbtitem
     */
    private void doAbsent(Inventory inv, Player player, ItemStack item, NBTItem nbtItem) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(inv);
                if(MysteriousEquipment.getR().nextDouble()<= AttributeSeparator.getProbability()){
                    int minloc = nbtItem.getInteger("minLoc");
                    int maxloc = nbtItem.getInteger("maxLoc");
                    int attributeLength = nbtItem.getInteger("attributeLength");
                    String[] locmsg = nbtItem.getString("locMsg").split(",");
                    String typeid = nbtItem.getString("typeId");
                    Map<String,List<Integer>> locs = new HashMap<>();
                    extractingAttributeData(locmsg, locs);
                    int loc = MysteriousEquipment.getR().nextInt(maxloc-minloc+1)+minloc;
                    List<String> lore = item.getItemMeta().getLore();
                    String attribute = lore.get(loc);
                    lore.remove(loc);
                    maxloc -= 1;
                    attributeLength -= 1;
                    StringBuilder newlocmsg = new StringBuilder();
                    String pharse = null;
                    for(Map.Entry<String,List<Integer>> m:locs.entrySet()){
                        List<Integer> integers = m.getValue();
                        if(integers.contains(loc)){
                            integers.remove(integers.indexOf(loc));
                            pharse = m.getKey();
                        }
                        for(int a:integers){
                            if(a>loc)integers.set(integers.indexOf(a),a-1);
                        }
                        if(integers.size()>0){
                            if(integers.size()>1) newlocmsg.append(m.getKey()).append(":").append(integers.get(0)).append("#").append(integers.get(1)).append(",");
                            else newlocmsg.append(m.getKey()).append(":").append(integers.get(0)).append(",");
                        }
                    }
                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    NBTItem n = new NBTItem(item);
                    n.setInteger("maxLoc",maxloc);
                    n.setInteger("attributeLength",attributeLength);
                    n.setString("locMsg",newlocmsg.toString());
                    ItemStack newitem = n.getItem();
                    inv.setItem(10,newitem);
                    inv.setItem(31,AttributeSeparator.adsorbateItemInstantiation(typeid,attribute,pharse));
                    player.sendMessage(Message.getMsg(Message.m_c_success,attribute));
                }else player.sendMessage(Message.getMsg(Message.m_c_fail));
            }
        }.runTaskLater(MysteriousEquipment.getPlugin(),12*20);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        if(!zaiChouShuXing.contains(player.getUniqueId())){
            if(inv.getTitle()!=null&&inv.getTitle().equalsIgnoreCase(Message.getMsg(Message.s_guiInfo))){
                ItemStack item = inv.getItem(10);
                ItemStack item1 = inv.getItem(16);
                ItemStack item2 = inv.getItem(31);
                if(item!=null){
                    if(player.getInventory().firstEmpty()>0)
                        player.getInventory().addItem(item);
                    else player.getWorld().dropItem(player.getLocation(),item);
                }
                if(item1!=null){
                    if(player.getInventory().firstEmpty()>0)
                        player.getInventory().addItem(item1);
                    else player.getWorld().dropItem(player.getLocation(),item1);
                }
                if(item2!=null&&new NBTItem(item2).hasKey("REType")){
                    if(player.getInventory().firstEmpty()>0)
                        player.getInventory().addItem(item2);
                    else player.getWorld().dropItem(player.getLocation(),item2);
                }
            }
        }
        if(inv.getTitle()!=null&&inv.getTitle().equalsIgnoreCase(Message.getMsg(Message.c_guiInfo))){
            List<Integer> noCancelSlots = Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,39,41);
            for(int a:noCancelSlots){
                if(inv.getItem(a)!=null){
                    if(player.getInventory().firstEmpty()>0)
                        player.getInventory().addItem(inv.getItem(a));
                    else player.getWorld().dropItem(player.getLocation(),inv.getItem(a));
                }
            }
        }
    }
    private void Effect_2(Inventory inv){
        ItemStack is8 = new ItemStack(Material.FERMENTED_SPIDER_EYE);//发酵蜘蛛眼
        ItemStack is9 = new ItemStack(Material.SPIDER_EYE);//蜘蛛眼
        ItemStack is10 = new ItemStack(Material.getMaterial(372));//地狱疣
        SuckingTableGui.setName(is8);
        SuckingTableGui.setName(is9);
        SuckingTableGui.setName(is10);
        ItemStack[] is = {is8,is9,is10};
        int[] slot = {19,25,28,34,37,43};
        new BukkitRunnable() {
            @Override
            public void run() {
                for(double tick = 34D;tick>0D;tick--){
                    for(int s:slot){
                        if(inv.getItem(s).isSimilar(is[0]))inv.setItem(s,is[1]);
                        else if(inv.getItem(s).isSimilar(is[1]))inv.setItem(s,is[2]);
                        else if(inv.getItem(s).isSimilar(is[2]))inv.setItem(s,is[0]);
                    }
                    try {
                        Thread.sleep((long) (400));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(MysteriousEquipment.getPlugin());
    }
    private void Effect_1(Inventory inv,Player player) {
        ItemStack is3 = new ItemStack(Material.END_CRYSTAL);//末影水晶
        ItemStack is4 = new ItemStack(Material.SNOW_BALL);//雪球
        SuckingTableGui.setName(is3);
        SuckingTableGui.setName(is4);
        //
        int[] slot = {21,22,23,30,32,39,40,41};
        new BukkitRunnable() {
            @Override
            public void run() {
                for(double tick = 40D;tick>0.2D;tick-=tick*0.15D){
                    for(int s:slot){
                        if(inv.getItem(s).isSimilar(is3))inv.setItem(s,is4);
                        else inv.setItem(s,is3);
                    }
                    try {
                        Thread.sleep((long) (tick*50));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                zaiChouShuXing.remove(player.getUniqueId());
            }
        }.runTaskAsynchronously(MysteriousEquipment.getPlugin());
    }

    /**
     * 合成
     * @param e
     */
    @EventHandler
    public void onClickHe(InventoryClickEvent e){
        Inventory inv = e.getClickedInventory();
        Player player = (Player) e.getWhoClicked();
        if(e.getSlotType().equals(InventoryType.SlotType.CONTAINER)) {
            if (e.getClickedInventory().getTitle() != null && e.getClickedInventory().getTitle().equalsIgnoreCase(Message.getMsg(Message.c_guiInfo))) {
                e.setCancelled(true);
                if (!(e.getClickedInventory() instanceof PlayerInventory)) {
                    List<Integer> noCancelSlots = Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,39,41);
                    int slot = e.getSlot();
                    if(noCancelSlots.contains(slot))e.setCancelled(false);
                    if(slot==40||slot==31){
                        ItemStack zbitem = inv.getItem(39);
                        ItemStack sjitem = inv.getItem(41);
                        boolean isnull = false;
                        if(zbitem==null){
                            player.sendMessage(Message.getMsg(Message.m_f_noPlace));
                            isnull = true;
                        }else {
                            if(!RandomNBTItem.isRandomNBTItem(zbitem)){
                                player.sendMessage(Message.getMsg(Message.m_f_noMEItem));
                                isnull = true;
                            }else if(!RandomNBTItem.isCulturable(zbitem)){
                                player.sendMessage(Message.getMsg(Message.m_f_noGrowUp));
                                isnull = true;
                            }
                        }
                        if(isnull)return;
                        if(slot==40){
                            if(sjitem==null){
                                player.sendMessage(Message.getMsg(Message.m_f_noSjItem));
                                return;
                            }
                            RandomNBTItem nbtItem = new RandomNBTItem(zbitem);
                            int qualityId = nbtItem.getQualityId();
                            Quality quality = QualityManager.getQualityMap().get(qualityId);
                            String[] locMsg =  nbtItem.getLocMsg().split(",");
                            Map<String,List<Integer>> locs = new HashMap<>();
                            extractingAttributeData(locMsg, locs);
                            if(quality.getIs()!=null){
                                int sjAmount = quality.getIsamount();
                                if(!quality.getIs().isSimilar(sjitem)){
                                    player.sendMessage(Message.getMsg(Message.m_f_sjItemError,sjAmount,quality.getIs().getItemMeta().getDisplayName()));
                                    return;
                                }
                                if(sjitem.getAmount()<sjAmount){
                                    player.sendMessage(Message.getMsg(Message.m_f_sjItemNum,sjAmount-sjitem.getAmount(),sjitem.getItemMeta().getDisplayName()));
                                    return;
                                }
                                Quality nextQuality = QualityManager.getQualityMap().get(qualityId+1);
                                if(nextQuality!=null){
                                    int[] containloc = {10,11,12,13,14,15,16,19,20,21,22,23,24,25};
                                    Map<Integer,ItemStack> contains = new HashMap<>();
                                    Map<String,List<String>> attributes = new HashMap<>();
                                    for(int s:containloc){
                                        contains.put(s,inv.getItem(s));
                                    }
                                    //sjitem.setAmount(sjitem.getAmount()-sjAmount);
                                    takeItem(inv,sjitem,sjAmount);
                                    int minAttribute = nextQuality.getMinAttribute();
                                    int maxAttribute = nextQuality.getMaxAttribute();

                                    int attributelength = nbtItem.getAttributeLength();
                                    int minloc = nbtItem.getMinLoc();
                                    int maxloc = nbtItem.getMaxLoc();
                                    String typeid =  nbtItem.getTypeId();
                                    ItemMeta meta = zbitem.getItemMeta();
                                    List<String> lore = meta.getLore();
                                    for(Map.Entry<Integer, ItemStack> ct:contains.entrySet()){
                                        if(ct.getValue()!=null){
                                            ItemStack clItem = ct.getValue();
                                            if(AttributeSeparator.isAdsorbateItem(clItem)){
                                                NBTItem nbtItem1 = new NBTItem(clItem);
                                                String retypeid = nbtItem1.getString("REType");
                                                String attribute = nbtItem1.getString("REAttribute");
                                                String phrase = nbtItem1.getString("REPhrase");
                                                if(attributelength<maxAttribute){
                                                    if(retypeid.equals(typeid)){
                                                        boolean isCan = false;
                                                        int attl = attributes.get(phrase)!=null?attributes.get(phrase).size():0;
                                                        if(locs.get(phrase)==null){
                                                            isCan = true;
                                                        }else {
                                                            if (locs.get(phrase).size() + attl < 2) {
                                                                isCan = true;
                                                            } else {
                                                                player.sendMessage(Message.getMsg(Message.m_f_moreAttr,invloc(ct.getKey())));
                                                            }
                                                        }
                                                        if(isCan){
                                                            if(clItem.getAmount()-1==0){
                                                                inv.setItem(ct.getKey(),null);
                                                            }else {
                                                                clItem.setAmount(clItem.getAmount()-1);
                                                                inv.setItem(ct.getKey(),clItem);
                                                            }
                                                            attributelength+=1;
                                                            if(attributes.get(phrase)==null){
                                                                List<String> att = new ArrayList<>();
                                                                att.add(attribute);
                                                                attributes.put(phrase,att);
                                                            }else {
                                                                List<String> att = attributes.get(phrase);
                                                                att.add(attribute);
                                                                attributes.put(phrase,att);
                                                            }
                                                        }
                                                    }else {
                                                        player.sendMessage(Message.getMsg(Message.m_f_typeError,invloc(ct.getKey())));
                                                    }
                                                }else {
                                                    player.sendMessage(Message.getMsg(Message.m_f_maxAttr,invloc(ct.getKey())));
                                                }
                                            }else {
                                                player.sendMessage(Message.getMsg(Message.m_f_noXiFu,invloc(ct.getKey())));
                                            }
                                        }
                                    }
                                    for(Map.Entry<String,List<String>> pa:attributes.entrySet()){
                                        for(String s:pa.getValue()){
                                            maxloc+=1;
                                            lore.add(maxloc,s);
                                            if(locs.get(pa.getKey())!=null){
                                                List<Integer> l = locs.get(pa.getKey());
                                                l.add(maxloc);
                                                locs.put(pa.getKey(),l);
                                            }else {
                                                List<Integer> l = new ArrayList<>();
                                                l.add(maxloc);
                                                locs.put(pa.getKey(),l);
                                            }
                                        }
                                    }
                                    List<String> newlore = new ArrayList<>();
                                    for(String l:lore){
                                        newlore.add(l.replace(quality.getName(),nextQuality.getName()));
                                    }
                                    meta.setLore(newlore);
                                    StringBuilder newlocmsg = new StringBuilder();
                                    for(Map.Entry<String,List<Integer>> m:locs.entrySet()){
                                        List<Integer> integers = m.getValue();
                                        if(integers.size()>0){
                                            if(integers.size()>1) newlocmsg.append(m.getKey()).append(":").append(integers.get(0)).append("#").append(integers.get(1)).append(",");
                                            else newlocmsg.append(m.getKey()).append(":").append(integers.get(0)).append(",");
                                        }
                                    }
                                    zbitem.setItemMeta(meta);
                                    RandomNBTItem nnbtitem = new RandomNBTItem(zbitem);
                                    nnbtitem.setAttributeLength(attributelength);
                                    nnbtitem.setLocMsg(newlocmsg.toString());
                                    nnbtitem.setQuality(qualityId+1);
                                    nnbtitem.setMaxLoc(maxloc);
                                    ItemStack item = nnbtitem.getItem();
                                    inv.setItem(39,item);
                                    player.sendMessage(Message.getMsg(Message.m_f_up,nextQuality.getName()));
                                    for(Map.Entry<String,List<String>> pa:attributes.entrySet()){
                                        for(String s:pa.getValue()){
                                            player.sendMessage(Message.getMsg(Message.m_f_add,s));
                                        }
                                    }
                                }else player.sendMessage(Message.getMsg(Message.m_f_qualityError,(qualityId+1)));
                            }else {
                                player.sendMessage(Message.getMsg(Message.m_f_maxQuality));
                            }
                        }
                        if(slot==31){

                            RandomNBTItem nbtItem = new RandomNBTItem(zbitem);
                            Quality quality = QualityManager.getQualityMap().get(nbtItem.getQualityId());
                            Quality nextQuality = QualityManager.getQualityMap().get(nbtItem.getQualityId()+1);
                            List<String> lore = Message.getMsg(Message.c_clickInfo_lore,
                                    nbtItem.getTypeId(),
                                    quality.getName(),
                                    nbtItem.getAttributeLength(),
                                    quality.getMaxAttribute(),
                                    (nextQuality==null?Message.getMsg(Message.maxQuality):nextQuality.getName()),
                                    (nextQuality==null?Message.getMsg(Message.maxQuality):quality.getIs().getItemMeta().getDisplayName()),
                                    (nextQuality==null?Message.getMsg(Message.maxQuality):nextQuality.getMaxAttribute()));
                            ItemMeta meta = inv.getItem(31).getItemMeta();
                            meta.setLore(lore);
                            inv.getItem(31).setItemMeta(meta);
                        }
                    }
                }
            }
            if(e.getInventory().getTitle()!=null&&e.getInventory().getTitle().equalsIgnoreCase(Message.getMsg(Message.c_guiInfo))&&e.getClickedInventory() instanceof PlayerInventory&&e.isShiftClick()){
                e.setCancelled(true);
            }
        }
    }

    private String invloc(int slot){
        int y = 0;
        int x = 0;
        for(int a=0;a<=slot;){
            a+=9;
            y+=1;
            if(a>=slot){
                x=9-(a-slot)+1;
            }
        }
        return "X:"+x+" Y:"+y;
    }

    /**
     * 从数组中提取属性数据附给map
     * @param locMsg 数组
     * @param locs map
     */
    private void extractingAttributeData(String[] locMsg, Map<String, List<Integer>> locs) {
        for(String s:locMsg){
            if(s!=null&&!s.equals("")){
                List<Integer> integers = new ArrayList<>();
                String[] s1 = s.split(":");
                for(String s2:s1[1].split("#")){
                    integers.add(Integer.parseInt(s2));
                }
                if(locs.containsKey(s1[0])){
                    integers.addAll(0,locs.get(s1[0]));
                }
                locs.put(s1[0],integers);
            }
        }
    }
    private boolean takeItem(Inventory inv,ItemStack is,int amount){
        if (is.getAmount()-amount>0){
            is.setAmount(is.getAmount()-amount);
            return true;
        }else if(is.getAmount()-amount == 0){
            for(int a=0;a<inv.getSize()-1;){
                ItemStack i = inv.getItem(a);
                if(i!=null&&i.isSimilar(is)&&i.getAmount()==amount){
                    inv.setItem(a,null);
                    break;
                }
                a+=1;
            }
            return true;
        }
        return false;
    }
}
