package me.wuxie.mysteriousequipment.thesaurus;

import lombok.Getter;
import me.wuxie.mysteriousequipment.MysteriousEquipment;

import java.util.ArrayList;
import java.util.List;

public class Phrase {
    @Getter
    private List<String> list;
    @Getter
    private String name;
    Phrase(List<String> list, String name){
        this.list = list;
        this.name = name;
    }

    /**
     *
     * @param number 剩余可产生条数
     * @return list
     */
    public List<String> getRandomPhrase(int number){
        List<String> randomList = new ArrayList<>();
        String[] colors = MysteriousEquipment.getColors();
        int frequency = MysteriousEquipment.getR().nextInt(3);
        if(number<frequency)return randomList;
        getListPhrases(frequency, randomList, colors);
        return randomList;
    }
    public List<String> getListPhrases(int number){
        List<String> randomList = new ArrayList<>();
        String[] colors = MysteriousEquipment.getColors();
        getListPhrases(number, randomList, colors);
        return randomList;
    }

    private void getListPhrases(int number, List<String> randomList, String[] colors) {
        for(int a=0;a<number;a++){
            int i = MysteriousEquipment.getR().nextInt(list.size());
            String s = list.get(i);
            while (true){
                if(s.contains("<color>")){
                    s = s.replaceFirst("<color>",colors[MysteriousEquipment.getR().nextInt(colors.length)]);
                }else break;
            }
            List<String> stringList = getStringList(s);
            s = getColorString(s, stringList);
            randomList.add(s);
        }
    }
    public static String getString(String s, List<String> stringList) {
        if(stringList.size()>0){
            for(String s4:stringList){
                if(s4.contains("_")&&s4.split("_").length==2){
                    String s5[] = s4.split("_");
                    try{
                        double num1 = Double.valueOf(s5[0]);
                        double num2 = Double.valueOf(s5[1]);
                        double num = Double.valueOf(MysteriousEquipment.getDf().format(nextDouble(num1,num2)));
                        s = s.replaceFirst("<"+s4+">",formatNum(num));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return s;
    }
    private static String getColorString(String s, List<String> stringList) {
        int colorid = 0;
        String[] colors = {"§2","§a","§b","§3","§9","§f","§e","§6","§c","§4"};
        if(stringList.size()>0){
            double value = 0D;
            double maxvalue = 0D;
            double minvalue = 0D;
            for(String s4:stringList){
                if(s4.contains("_")&&s4.split("_").length==2){
                    String s5[] = s4.split("_");
                    try{
                        double num1 = Double.valueOf(s5[0]);
                        double num2 = Double.valueOf(s5[1]);
                        maxvalue+=num2;
                        minvalue+=num1;
                        double num = Double.valueOf(MysteriousEquipment.getDf().format(nextDouble(num1,num2)));
                        value+=num;
                        s = s.replaceFirst("<"+s4+">",formatNum(num));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            double c = ((value-minvalue)/(maxvalue-minvalue))*10;
            if(c<0.5D&&c>0D)c = 1;
            colorid = (int) Math.round(c);
        }
        // 属性强度判断颜色
        return colorid>0?colors[colorid-1]+s:colors[0]+s;
    }
    private static String formatNum(double num){
        String str = Double.toString(num);
        String[] spl =str .split("\\.");
        // 判断整数
        if(spl[1].equalsIgnoreCase("0")){
            return spl[0];
        }
        return str;
    }

    public static List<String> getStringList(String string) {
        List<String> stringList = new ArrayList<>();
        if (string.contains("<")) {
            String[] args = string.split("<");
            if (args.length > 1 && args[1].contains(">")) {
                for (int i = 1; i < args.length && args[i].contains(">"); i++) {
                    stringList.add(args[i].split(">")[0]);
                }
            }
        }
        return stringList;
    }
    private static double nextDouble(final double min, final double max) throws Exception {
        if (max < min) {
            throw new Exception("max < min");
        }
        if (min == max) {
            return min;
        }
        return min + ((max - min) * MysteriousEquipment.getR().nextDouble());
    }
}
