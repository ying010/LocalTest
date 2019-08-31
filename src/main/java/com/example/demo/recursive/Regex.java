package com.example.demo.recursive;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    public static  String getRegex(String s) {
        //<a href='//jiangsu.baixing.com/'>江苏</a>
        //<a href="/vodhtml/55649.html" title="Obasan専属第3弾！！夏真っ盛り、山登りで遭難した" target="_blank">
        //String regex = "<a href='//[a-zA-z0-9]+.[a-zA-z0-9]*/'>[\u4E00-\u9FA5]</a>";
        String regex2 = "^<a href=\"/vodhtml/([a-zA-z0-9]*).html\"$";
        String   regex = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
        Pattern r = Pattern.compile(regex2);
        Matcher m = r.matcher(s);
        System.out.println(m.matches());
        ArrayList list = new ArrayList();
//        while(m.find()) {
//            list.add(m.group());
//            String   regex1 = "^<a href='//(.*?)/'.*?([\\u4e00-\\u9fa5]*)</a>$";
//            Pattern r1 = Pattern.compile(regex1);
//            Matcher m1 = r1.matcher(m.group());
//            if(m1.find())
//            {
//                System.out.println(m1.group(2)+" = "+m1.group(1));
//
//            }
//        }
        return list.toString();
    }

    public static void main(String[] args) {
        getRegex("");
    }
}
