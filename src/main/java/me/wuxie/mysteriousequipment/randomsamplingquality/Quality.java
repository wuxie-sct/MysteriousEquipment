package me.wuxie.mysteriousequipment.randomsamplingquality;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class Quality {
    @Getter
    private String name;    //品质名称
    @Getter
    private double prob;    //抽中概率
    @Getter
    private int id;
    @Getter
    private int minAttribute;
    @Getter
    private int maxAttribute;
    @Getter
    private ItemStack is;
    @Getter
    private int isamount;
    @Getter
    private String color;
    public Quality(int id, String name, double prob, int minAttribute, int maxAttribute, ItemStack is ,int isamount,String color){
        this.prob = prob;
        this.name = name.replace("&","§");
        this.id = id;
        this.maxAttribute = maxAttribute;
        this.minAttribute = minAttribute;
        this.is = is;
        this.isamount = isamount;
        this.color = color;
    }
}
