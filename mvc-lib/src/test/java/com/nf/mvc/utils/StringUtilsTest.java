package com.nf.mvc.utils;

import com.nf.mvc.util.Delimiters;
import com.nf.mvc.util.StringUtils;
import org.junit.Test;

import java.util.EnumSet;
import java.util.List;

public class StringUtilsTest {
    @Test
    public void testDelimiters(){
        String data = ",,,abc,def:xxx yyy;tyu,,,";

       // String combinedPattern = Delimiters.getCombinedPattern(EnumSet.of(Delimiters.Colon, Delimiters.Comma));
      //  String combinedPattern = Delimiters.getCombinedPattern(EnumSet.of( Delimiters.Comma));
        String combinedPattern = Delimiters.getCombinedPattern(EnumSet.of( Delimiters.Colon,Delimiters.Common));
        System.out.println("combinedPattern = " + combinedPattern);

       // String[] split = data.trim().split(combinedPattern);
        List<String> stringList = StringUtils.split(data, combinedPattern);
        for (String s : stringList) {
            System.out.println("s = " + s + ":" + s.length());
        }
        System.out.println("============");

    }
}
