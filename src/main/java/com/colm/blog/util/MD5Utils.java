package com.colm.blog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Colm on 2020/10/20
 */
public class MD5Utils {
    private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);

    /**
     * MD5 加密类
     * @param str 要加密的字符串
     * @return 加密后的字符串
     */
    public static String code(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buffer = new StringBuffer("");
            for(int offset = 0; offset < byteDigest.length; offset++){
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buffer.append("0");
                buffer.append(Integer.toHexString(i));
            }
            // 32 位加密
            return buffer.toString();
            // 16 位加密
//            return buffer.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            logger.error(str + "转换发生异常: {}", e);
            return null;
        }
    }

    public static void main(String[] args) {
        String code = code("123456");
        System.out.println(code);
    }
}
