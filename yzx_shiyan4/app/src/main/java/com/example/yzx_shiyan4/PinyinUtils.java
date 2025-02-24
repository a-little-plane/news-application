package com.example.yzx_shiyan4;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.HashSet;

public class PinyinUtils {
//    implementation 'com.belerweb:pinyin4j:2.5.1'
    public static String toPinyin(String chinese){
        //将chinese转换为拼音，各汉字拼音之间用空格隔开
        if(!TextUtils.isEmpty(chinese)) {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            //format控制转换结果的输出格式
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            //输出为全部小写
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            //输出不带声调
            format.setVCharType(HanyuPinyinVCharType.WITH_V);
            //输出用v替代拼音中的ü
            try {
                String s = PinyinHelper.toHanYuPinyinString(chinese, format,
                        " ", true);
                return s;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }
    public static String toPinyinFirstLetter(String chinese){
        //将chinese转换为拼音首字母
        if(!TextUtils.isEmpty(chinese)) {
            String s = toPinyin(chinese);
            return fullPinyin2FirstLetter(s);
        }else {
            return "";
        }
    }
    private static String fullPinyin2FirstLetter(String s){
        //按空格分割字符串，取首字母
        if(!TextUtils.isEmpty(s)){
            StringBuilder sb = new StringBuilder();
            try {
                String[] split = s.split("\\s+");//多个连续空格按1个分割符处理
                //按空格将s分解成数组
                for (int i = 0; i < split.length; i++) {
                    sb.append(split[i].charAt(0));
                    //对数组中每个元素取首字母
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }else {
            return "";
        }
    }
    public static ArrayList<String> toPinyinList(String chinese){
        //转换结果为拼音列表数据
        ArrayList<String[]> splitList = toPinyinSplitList(chinese);
        return convertSplit2String(splitList);
    }
    public static ArrayList<String> toPinyinFirstLetterList(String chinese){
        //转换结果为拼音首字母列表数据
        ArrayList<String[]> splitList = toPinyinSplitList(chinese);
        ArrayList<String> fullList = convertSplit2String(splitList);
        HashSet<String> outSet=new HashSet<>();//利用集合去重复
        for (String s : fullList) {
            String out = fullPinyin2FirstLetter(s);
            if(!TextUtils.isEmpty(out)) {
                outSet.add(out);
            }
        }
        ArrayList<String> list=new ArrayList<>();
        list.addAll(outSet);//将集合转换为列表
        return list;
    }
    private static ArrayList<String> convertSplit2String(
            ArrayList<String[]> splits){
        //将toPinyinSplitList(String chines)的转换结果按组合拼成字符串
        //若有3个列表元素，各元素的数组长度是1,2,3 则组合输出为1*2*3=6种
        if(splits==null||splits.size()==0){
            return new ArrayList<String>();
        }
        ArrayList<String> out = new ArrayList<>();
        String[] array0 = splits.get(0);
        for (String array : array0) {
            out.add(array);
        }
        for (int i = 1; i <splits.size() ; i++) {
            int pre_out_size=out.size();
            String[] arrays = splits.get(i);
            if(arrays.length>1){
                ArrayList<String> temp=new ArrayList<>();
                temp.addAll(out);//将out的值复制给temp
                for (int j = 1; j < arrays.length; j++) {
                    out.addAll(temp);//利用temp对out重复复制
                }
                //将out扩展成out.size*arrays.length的列表
            }
            for (int j = 0; j < out.size(); j++) {
                int idx=j/pre_out_size;//求整，得到的idx刚好可以指向arrays的索引
                //对out第j个元素拼接，拼接间隔用空格
                out.set(j,out.get(j)+" "+arrays[idx]);
            }
        }
        return out;
    }
    private static ArrayList<String[]> toPinyinSplitList(String chines) {
        //私有方法，将汉字多音字按数组处理，加入列表中
        ArrayList<String[]> list=new ArrayList<>();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder sb=new StringBuilder();//存放ASCII码的临时字符串
        for (int i = 0; i < nameChar.length; i++) {
            char pending = nameChar[i];
            if (pending > 128) {
                if(sb.length()>0){
                    list.add(new String[]{sb.toString()});
                    //按数组处理，存入list
                    sb.setLength(0);//对StringBuilder清空
                }
                try {
                    // 取得当前汉字的所有全拼
                    String[] pys = PinyinHelper.toHanyuPinyinStringArray(
                            pending, format);
                    if (pys != null) {
                        //利用HashSet集合不重复的特点进行数据去重
                        HashSet<String> pySet = new HashSet<>();
                        for (String py : pys) {
                            pySet.add(py);
                        }
                        list.add(pySet.toArray(new String[pySet.size()]));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(pending);//将ASCII码加到StringBuilder对象
            }
        }
        if(sb.length()>0){//for结束，判断sb中是否有ASCII码
            list.add(new String[]{sb.toString()});
        }
        return list;
    }
    private static HashSet<String> generatePartLookup(String initialName,
                                                      String[] enNameArray) {
        //根据拼音首字母和全拼数组生成检索信息的HashSet集合
        //使用HashSet的目的是去重复
        HashSet<String> hashSet=new HashSet<>();
        for (int i = 1; i < enNameArray.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(initialName.substring(0, i));
            //取前i个汉字的拼音首字母，后续用全拼拼音
            for (int j = i; j < enNameArray.length; j++) {
                sb.append(enNameArray[j]);
            }
            hashSet.add(sb.toString());
        }
        return hashSet;
    }
    private static String parseHashSetLookup(HashSet<String> hashSet){
        //将HashSet的检索信息转换为String，实现了去重复
        StringBuilder sb=new StringBuilder();
        for (String s : hashSet) {
            sb.append(s+" ");
        }
        return sb.toString();
    }
    public static String generateLookup(String name){//根据姓名生成拼音搜索关键词
        String name0 = name.toLowerCase();//将输入信息转为小写
        ArrayList<String> enNameList = toPinyinList(name0);
        //enNameList为拼音列表数据，含多音字情况
        ArrayList<String> initialNameList= PinyinUtils
                .toPinyinFirstLetterList(name0);
        //initialNameList为拼音首字母列表数据，含多音字
        StringBuilder sb = new StringBuilder();
        HashSet<String> hashSet=new HashSet<>();
        hashSet.add(name0 + " ");////汉字检索
        for (String s : enNameList) {
            hashSet.add(s+" ");//全拼检索
            hashSet.add(s.replaceAll("\\s+","")+" ");
            //去空格全拼检索
            String[] enNameArray = s.split("\\s+");
            for (String ini : initialNameList) {
                HashSet<String> tempSet = generatePartLookup(ini, enNameArray);
                hashSet.addAll(tempSet);
                //部分首字母+后续全拼检索
            }
        }
        String part = parseHashSetLookup(hashSet);
        //将HashSet转为字符串
        sb.append(part);
        for (String s : initialNameList) {
            sb.append(s+" ");
            //首字母检索
        }
        return sb.toString();
    }
}
