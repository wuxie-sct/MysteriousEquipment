package me.wuxie.mysteriousequipment.thesaurus;

import me.wuxie.mysteriousequipment.MysteriousEquipment;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ThesaurusManager {
    public static Map<String, Thesaurus> tnesaurusMap;
    public static void load(){
        tnesaurusMap = new HashMap<>();
        File file = new File(MysteriousEquipment.getPlugin().getDataFolder(),"thesaurus");
        if(!file.exists()){
            MysteriousEquipment.getPlugin().saveResource("thesaurus/level_0.yml",true);
        }
        File[] files = file.listFiles();
        if(files==null)return;
        for (File f : files) {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
            Map<String,Phrase> phrases = new HashMap<>();
            yml.getKeys(false).forEach((key)->phrases.put(key,new Phrase(yml.getStringList(key),key)));
            tnesaurusMap.put(f.getName().replace(".yml",""),new Thesaurus(phrases));
        }
    }
    public static Thesaurus getThesaurus(String s){
        return tnesaurusMap.get(s);
    }
}
