package me.wuxie.mysteriousequipment.thesaurus;

import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Map;

public class Thesaurus {
    @Getter
    private Map<String,Phrase> phraseMap;
    public Thesaurus(Map<String,Phrase> phraseMap){
        this.phraseMap = phraseMap;
    }
    public Phrase getPhrase(String string){
        Phrase p = phraseMap.get(string);
        if(p == null) Bukkit.getConsoleSender().sendMessage("§c词条组 "+string+" 不存在！！");
        return p;
    }
}
