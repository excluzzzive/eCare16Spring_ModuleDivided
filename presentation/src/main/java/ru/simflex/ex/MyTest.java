package ru.simflex.ex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 03.04.2016.
 */
public class MyTest {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("^[A-z0-9\\s]{2,30}$");
        Matcher m = p.matcher("hello world sdsdfsdf sdf sdfsdf sf ");
        System.out.println(m.matches());
    }
}
