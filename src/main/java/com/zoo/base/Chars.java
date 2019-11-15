package com.zoo.base;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 字符工具
 */
public final class Chars {
    private Chars() {
    }

    /**
     * 生成1个随机字符，可能是大写字母、小写字母、数字
     *
     * @return 随机字符
     */
    public static char randChar() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        switch (random.nextInt(2)) {
            case 1:
                return (char) random.nextInt(65, 91);
            case 2:
                return (char) random.nextInt(97, 123);
            default:
                return (char) random.nextInt(48, 58);
        }
    }

    /**
     * 判断是否是中文字符
     *
     * @param c 字符
     * @return 是中文字符返回true
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }
}
