package me.wuxie.mysteriousequipment.itemstack;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class RandomNBTItem extends NBTItem {
    public RandomNBTItem(ItemStack item) {
        super(item);
    }
    public int getQualityId(){
        return getInteger("qualityId");
    }
    public int getAttributeLength(){
        return getInteger("attributeLength");
    }
    public int getMinLoc(){
        return getInteger("minLoc");
    }
    public int getMaxLoc(){
        return getInteger("maxLoc");
    }
    public String getTypeId(){
        return getString("typeId");
    }
    public String getLocMsg(){
        return getString("locMsg");
    }
    public void setQuality(int num){ this.setInteger("qualityId",num);
        Bukkit.getConsoleSender().sendMessage(num+"");
    }
    public void setAttributeLength(int num){ setInteger("attributeLength",num); }
    public void setMinLoc(int num){setInteger("minLoc",num); }
    public void setMaxLoc(int num){ setInteger("maxLoc",num);
    }
    public void setTypeId(String str){
        setString("typeId",str);
    }
    public void setLocMsg(String str){
        setString("locMsg",str);
    }

    public static boolean isRandomNBTItem(ItemStack itemStack){
        RandomNBTItem nbtItem = new RandomNBTItem(itemStack);
        return nbtItem.hasKey("qualityId")&&nbtItem.hasKey("attributeLength")&&nbtItem.hasKey("minLoc")&&nbtItem.hasKey("maxLoc")&&nbtItem.hasKey("typeId")&&nbtItem.hasKey("locMsg")&&nbtItem.hasKey("isCulturable");
    }
    /**
     * 判断是否可培养
     * @param item
     * @return
     */
    public static boolean isCulturable(ItemStack item){
        RandomNBTItem nbtItem = new RandomNBTItem(item);
        return isRandomNBTItem(item)&&nbtItem.getBoolean("isCulturable");
    }
}
