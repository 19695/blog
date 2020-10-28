package com.colm.blog.util;

import java.util.Random;

/**
 * Created by Colm on 2020/10/28
 */
public class RandomAvatarUtils {
    public static String avatar() {
        Random random = new Random();
        int i = random.nextInt(8) + 1;
        String suffix = ".png";
        if (i == 1 || i == 3) {
            suffix = ".jpg";
        }
        return "avatar" + i + suffix;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(avatar());
        }
    }
}
