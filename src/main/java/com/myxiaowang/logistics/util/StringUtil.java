package com.myxiaowang.logistics.util;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月21日 18:00:00
 */
public class StringUtil {


    /**
     * 判单字符串是否存在指定字符串
     * @param str1 目标
     * @param str2 需要查找的字符串
     * @return 结果
     */
    public static int hasStr(String str1, String str2){
        int[] next=getNext(str2);
        for (int i = 0, j = 0; i < str1.length(); i++) {
            while (j > 0 && str1.charAt(i) != str2.charAt(j)) {
                j = next[j - 1];
            }

            if (str1.charAt(i) == str2.charAt(j)) {
                j++;
            }
            if (j == str2.length()) {
                return i - j + 1;
            }
        }
        return -1;
    }


    private static int[] getNext(String dest) {
        int[] next = new int[dest.length()];
        next[0] = 0;
        for (int i = 1, j = 0; i < dest.length(); i++) {
            while (j > 0 && dest.charAt(i) != dest.charAt(j)) {
                j = next[j - 1];
            }
            if(dest.charAt(i) == dest.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }

}
