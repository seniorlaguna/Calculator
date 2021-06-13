package org.seniorlaguna.calculator.apc;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PrettyString {
    
    /*static String prettifier(String input) {
        Pattern numberPattern = Pattern.compile("[0-9]*\\.[0-9]*");
        Matcher matcher = numberPattern.matcher(input);
        ArrayList<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group());
        }

        DecimalFormat format = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.ITALIAN));
        format.setMaximumIntegerDigits(309);
        format.setMaximumFractionDigits(340);
        for (int i = 0; i < list.size(); i++) {
            //System.out.println(list.get(i));
            System.out.println(format.format(Double.valueOf(list.get(i))));
        }

        return "";
    }
    
    public static void main(String args[]) {
        String exp = "100000000000000.123+100.1234567890";
        prettifier(exp);
    }*/
}
