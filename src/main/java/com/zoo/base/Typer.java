package com.zoo.base;

import java.util.Optional;

/**
 * 基本类型工具
 */
public final class Typer {

    private Typer() {
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final float[] FLOATS = new float[]{};

    private static final double[] DOUBLES = new double[]{};

    private static final int[] INTS = new int[]{};

    private static final long[] LONGS = new long[]{};

    private static final short[] SHORTS = new short[]{};

    private static final byte[] BYTES = new byte[]{};

    private static final char[] CHARS = new char[]{};

    private static final boolean[] BOOLEANS = new boolean[]{};


    /**
     * 判断obj是否是基本类型的包装类的实例，若是返回true，不是返回false
     *
     * @param obj 要被判断的对象
     * @return if obj instance one of [Integer、Double、Long、Float、Short、Byte、Character、Boolean]
     */
    public static boolean isWrap(Object obj) {
        return obj instanceof Integer
                || obj instanceof Double
                || obj instanceof Long
                || obj instanceof Float
                || obj instanceof Short
                || obj instanceof Byte
                || obj instanceof Character
                || obj instanceof Boolean;
    }

    /**
     * 判断obj是否是String的实例
     *
     * @param obj 要被判断的对象
     * @return if obj instanceof String then return true
     */
    public static boolean isStr(Object obj) {
        return obj instanceof String;
    }

    /**
     * 验证字符串的有效性(不为null并且不为空字符串)
     *
     * @param s 验证字符串
     * @return 是否有效的布尔值
     */
    public static boolean isString(String s) {
        return s != null && s.length() > 0;
    }

    /**
     * 验证浮点对象的有效性(不为null并且大于0)
     *
     * @param f 浮点对象
     * @return 是否有效的布尔值
     */
    public static boolean isFloat(Float f) {
        return f != null && f > 0;
    }

    /**
     * 验证双精度浮点对象的有效性(不为null并且大于0)
     *
     * @param d 浮点对象
     * @return 是否有效的布尔值
     */
    public static boolean isDouble(Double d) {
        return d != null && d > 0;
    }

    /**
     * 验证整数对象的有效性(不为null并且大于0)
     *
     * @param i 整数对象
     * @return 是否有效的布尔值
     */
    public static boolean isInteger(Integer i) {
        return i != null && i > 0;
    }

    /**
     * 验证长整数对象的有效性(不为null并且大于0)
     *
     * @param l 长整数对象
     * @return 是否有效的布尔值
     */
    public static boolean isLong(Long l) {
        return l != null && l > 0;
    }

    /**
     * 验证短整数对象的有效性(不为null并且大于0)
     *
     * @param s 短整数对象
     * @return 是否有效的布尔值
     */
    public static boolean isShort(Short s) {
        return s != null && s > 0;
    }

    /**
     * 验证字节对象的有效性(不为null并且大于0)
     *
     * @param b 字节对象
     * @return 是否有效的布尔值
     */
    public static boolean isByte(Byte b) {
        return b != null && b > 0;
    }

    /**
     * 0个元素的字符串数组
     *
     * @return 长度为0的字符串数组
     */
    public static String[] strings() {
        return Strs.empties();
    }

    /**
     * 0个元素的单精度浮点数组
     *
     * @return 长度为0的单精度浮点数组
     */
    public static float[] floats() {
        return FLOATS;
    }

    /**
     * 0个元素的双精度浮点数组
     *
     * @return 长度为0的双精度浮点数组
     */
    public static double[] doubles() {
        return DOUBLES;
    }

    /**
     * 0个元素的整型数组
     *
     * @return 长度为0的整型数组
     */
    public static int[] ints() {
        return INTS;
    }

    /**
     * 0个元素的长整型数组
     *
     * @return 长度为0的长整型数组
     */
    public static long[] longs() {
        return LONGS;
    }

    /**
     * 0个元素的短整型数组
     *
     * @return 长度为0的短整型数组
     */
    public static short[] shorts() {
        return SHORTS;
    }

    /**
     * 0个元素的字节数组
     *
     * @return 长度为0的字节数组
     */
    public static byte[] bytes() {
        return BYTES;
    }

    /**
     * 0个元素的字符数组
     *
     * @return 长度为0的字符数组
     */
    public static char[] chars() {
        return CHARS;
    }

    /**
     * 0个元素的布尔型数组
     *
     * @return 长度为0的布尔型数组
     */
    public static boolean[] booleans() {
        return BOOLEANS;
    }


    /**
     * 构造二维空数组,长度为len,二维数组中的每个一维数组长度都为0.相当于构造len个一维空数组
     *
     * @param len 二维数组长度
     * @return 若len小于0, 则置返回的数组长度为0
     */
    public static long[][] longss(int len) {
        return new long[Math.max(len, 0)][0];
    }


    /**
     * 构造二维空数组,长度为len,二维数组中的每个一维数组长度都为0.相当于构造len个一维空数组
     *
     * @param len 二维数组长度
     * @return 若len小于0, 则置返回的数组长度为0
     */
    public static int[][] intss(int len) {
        return new int[Math.max(len, 0)][0];
    }


    /**
     * 构造二维空数组,长度为len,二维数组中的每个一维数组长度都为0.相当于构造len个一维空数组
     *
     * @param len 二维数组长度
     * @return 若len小于0, 则置返回的数组长度为0
     */
    public static short[][] shortss(int len) {
        return new short[Math.max(len, 0)][0];
    }


    /**
     * 构造二维空数组,长度为len,二维数组中的每个一维数组长度都为0.相当于构造len个一维空数组
     *
     * @param len 二维数组长度
     * @return 若len小于0, 则置返回的数组长度为0
     */
    public static byte[][] bytess(int len) {
        return new byte[Math.max(len, 0)][0];
    }


    /**
     * 构造二维空数组,长度为len,二维数组中的每个一维数组长度都为0.相当于构造len个一维空数组
     *
     * @param len 二维数组长度
     * @return 若len小于0, 则置返回的数组长度为0
     */
    public static double[][] doubless(int len) {
        return new double[Math.max(len, 0)][0];
    }


    /**
     * 构造二维空数组,长度为len,二维数组中的每个一维数组长度都为0.相当于构造len个一维空数组
     *
     * @param len 二维数组长度
     * @return 若len小于0, 则置返回的数组长度为0
     */
    public static float[][] floatss(int len) {
        return new float[Math.max(len, 0)][0];
    }


    /**
     * 构造二维空数组,长度为len,二维数组中的每个一维数组长度都为0.相当于构造len个一维空数组
     *
     * @param len 二维数组长度
     * @return 若len小于0, 则置返回的数组长度为0
     */
    public static char[][] charss(int len) {
        return new char[Math.max(len, 0)][0];
    }


    /**
     * 构造二维空数组,长度为len,二维数组中的每个一维数组长度都为0.相当于构造len个一维空数组
     *
     * @param len 二维数组长度
     * @return 若len小于0, 则置返回的数组长度为0
     */
    public static boolean[][] booleanss(int len) {
        return new boolean[Math.max(len, 0)][0];
    }


    /**
     * long型数组转成其包装类的数组
     *
     * @param ls 要转换的long型数组
     * @return 返回long的包装类Long的数组(Long[]), 若ls为null则返回0个元素的空数组
     */
    public static Long[] longs(long... ls) {
        Long[] nls = null;
        if (ls != null) {
            int len = ls.length;
            nls = new Long[len];
            for (int i = 0; i < len; i++) {
                nls[i] = ls[i];
            }
        } else {
            nls = new Long[0];
        }
        return nls;
    }

    /**
     * int型数组转成其包装类型数组
     *
     * @param is 要转换的int型数组
     * @return 返回int的包装类Integer的数组(Integer[]), 若is为null则返回0个元素的空数组
     */
    public static Integer[] ints(int... is) {
        Integer[] nis = null;
        if (is != null) {
            int len = is.length;
            nis = new Integer[len];
            for (int i = 0; i < len; i++) {
                nis[i] = is[i];
            }
        } else {
            nis = new Integer[0];
        }
        return nis;
    }

    /**
     * short型数组转换成其包装类型数组
     *
     * @param ss 要转换的short型数组
     * @return 返回short的包装类Short的数组(Short[]), 若ss为null则返回0个元素的空数组
     */
    public static Short[] shorts(short... ss) {
        Short[] nss = null;
        if (ss != null) {
            int len = ss.length;
            nss = new Short[len];
            for (int i = 0; i < len; i++) {
                nss[i] = ss[i];
            }
        } else {
            nss = new Short[0];
        }
        return nss;
    }

    /**
     * byte型数组转换成其包装类的数组
     *
     * @param bs 要转换的byte型数组
     * @return 返回byte的包装类Byte的数组(Byte[]), 若bs为null则返回0个元素的空数组
     */
    public static Byte[] bytes(byte... bs) {
        Byte[] nbs = null;
        if (bs != null) {
            int len = bs.length;
            nbs = new Byte[len];
            for (int i = 0; i < len; i++) {
                nbs[i] = bs[i];
            }
        } else {
            nbs = new Byte[0];
        }
        return nbs;
    }

    /**
     * double型数组转换成其包装类型的数组
     *
     * @param ds 要转换的double型数组
     * @return 返回double的包装类Double的数组(Double[]), 若ds为null则返回0个元素的空数组
     */
    public static Double[] doubles(double... ds) {
        Double[] nds = null;
        if (ds != null) {
            int len = ds.length;
            nds = new Double[len];
            for (int i = 0; i < len; i++) {
                nds[i] = ds[i];
            }
        } else {
            nds = new Double[0];
        }
        return nds;
    }

    /**
     * float型数组转换成其包装类型数组
     *
     * @param fs 要转换的float型数组
     * @return 返回float的包装类Float的数组(Float[]), 若fs为null则返回0个元素的空数组
     */
    public static Float[] floats(float... fs) {
        Float[] nfs = null;
        if (fs != null) {
            int len = fs.length;
            nfs = new Float[len];
            for (int i = 0; i < len; i++) {
                nfs[i] = fs[i];
            }
        } else {
            nfs = new Float[0];
        }
        return nfs;
    }

    /**
     * char型数组转换成其包装类型的数组
     *
     * @param cs 要转换的char型数组
     * @return 返回char的包装类Character的数组(Character[]), 若cs为null则返回0个元素的空数组
     */
    public static Character[] chars(char... cs) {
        Character[] ncs = null;
        if (cs != null) {
            int len = cs.length;
            ncs = new Character[len];
            for (int i = 0; i < len; i++) {
                ncs[i] = cs[i];
            }
        } else {
            ncs = new Character[0];
        }
        return ncs;
    }

    /**
     * boolean型数组转换成其包装类型的数组
     *
     * @param bs 要转换的boolean型数组
     * @return 返回boolean的包装类Boolean的数组(Boolean[]), 若bs为null则返回0个元素的空数组
     */
    public static Boolean[] booleans(boolean... bs) {
        Boolean[] nbs = null;
        if (bs != null) {
            int len = bs.length;
            nbs = new Boolean[len];
            for (int i = 0; i < len; i++) {
                nbs[i] = bs[i];
            }
        } else {
            nbs = new Boolean[0];
        }
        return nbs;
    }


    /**
     * Long型数组转成其对应基本类型的数组
     *
     * @param ls 要转换的Long型数组
     * @return 返回Long的基本类型long的数组(long[]), 若ls为null则返回0个元素的空数组
     */
    public static long[] longs(Long... ls) {
        if (ls != null) {
            int len = ls.length;
            long[] nls = new long[len];
            for (int i = 0; i < len; i++) {
                nls[i] = ls[i] == null ? 0 : ls[i];
            }
            return nls;
        }
        return longs();
    }

    /**
     * Integer型数组转成其对应基本类型的数组
     *
     * @param is 要转换的Integer型数组
     * @return 返回Integer的基本类型int的数组(int[]), 若is为null则返回0个元素的空数组
     */
    public static int[] ints(Integer... is) {
        if (is != null) {
            int len = is.length;
            int[] nis = new int[len];
            for (int i = 0; i < len; i++) {
                nis[i] = is[i] == null ? 0 : is[i];
            }
            return nis;
        }
        return ints();
    }

    /**
     * Short型数组转换成其对应基本类型的数组
     *
     * @param ss 要转换的Short型数组
     * @return 返回Short的基本类型short的数组(short[]), 若ss为null则返回0个元素的空数组
     */
    public static short[] shorts(Short... ss) {
        if (ss != null) {
            int len = ss.length;
            short[] nss = new short[len];
            for (int i = 0; i < len; i++) {
                nss[i] = ss[i] == null ? 0 : ss[i];
            }
            return nss;
        }
        return shorts();
    }

    /**
     * Byte型数组转换成其对应基本类型的数组
     *
     * @param bs 要转换的Byte型数组
     * @return 返回Byte的基本类型byte的数组(byte[]), 若bs为null则返回0个元素的空数组
     */
    public static byte[] bytes(Byte... bs) {
        if (bs != null) {
            int len = bs.length;
            byte[] nbs = new byte[len];
            for (int i = 0; i < len; i++) {
                nbs[i] = bs[i] == null ? 0 : bs[i];
            }
            return nbs;
        }
        return bytes();
    }

    /**
     * Double型数组转换成其对应基本类型的数组
     *
     * @param ds 要转换的Double型数组
     * @return 返回Double的基本类型double的数组(double[]), 若ds为null则返回0个元素的空数组
     */
    public static double[] doubles(Double... ds) {
        if (ds != null) {
            int len = ds.length;
            double[] nds = new double[len];
            for (int i = 0; i < len; i++) {
                nds[i] = ds[i] == null ? 0.0 : ds[i];
            }
            return nds;
        }
        return doubles();
    }

    /**
     * Float型数组转换成其对应基本类型的数组
     *
     * @param fs 要转换的Float型数组
     * @return 返回Float的基本类型float的数组(float[]), 若fs为null则返回0个元素的空数组
     */
    public static float[] floats(Float... fs) {
        if (fs != null) {
            int len = fs.length;
            float[] nfs = new float[len];
            for (int i = 0; i < len; i++) {
                nfs[i] = fs[i] == null ? 0.0f : fs[i];
            }
            return nfs;
        }
        return floats();
    }

    /**
     * Character型数组转换成其对应基本类型的数组
     *
     * @param cs 要转换的Character型数组
     * @return 返回Character的基本类型char的数组(char[]), 若cs为null则返回0个元素的空数组
     */
    public static char[] chars(Character... cs) {
        if (cs != null) {
            int len = cs.length;
            char[] ncs = new char[len];
            for (int i = 0; i < len; i++) {
                ncs[i] = cs[i] == null ? '\u0000' : cs[i];
            }
            return ncs;
        }
        return chars();
    }

    /**
     * Boolean型数组转换成其对应基本类型的数组
     *
     * @param bs 要转换的Boolean型数组
     * @return 返回Boolean的基本类型boolean的数组(boolean[]), 若bs为null则返回0个元素的空数组
     */
    public static boolean[] booleans(Boolean... bs) {
        if (bs != null) {
            int len = bs.length;
            boolean[] nbs = new boolean[len];
            for (int i = 0; i < len; i++) {
                nbs[i] = bs[i] == null ? false : bs[i];
            }
            return nbs;
        }
        return booleans();
    }

    /**
     * int数组转long数组
     *
     * @param ints 要转换的int数组
     * @return 返回long数组，若ints为null则返回0个元素的long数组
     */
    public static long[] longs(int... ints) {
        if (ints != null && ints.length > 0) {
            int len = ints.length;
            long[] longs = new long[len];
            for (int i = 0; i < len; i++) {
                longs[i] = ints[i];
            }
            return longs;
        }
        return longs();
    }

    /**
     * short数组转long数组
     *
     * @param shorts 要转换的short数组
     * @return 返回long数组，若shorts为null则返回0个元素的long数组
     */
    public static long[] longs(short... shorts) {
        if (shorts != null && shorts.length > 0) {
            int len = shorts.length;
            long[] longs = new long[len];
            for (int i = 0; i < len; i++) {
                longs[i] = shorts[i];
            }
            return longs;
        }
        return longs();
    }

    /**
     * byte数组转long数组
     *
     * @param bytes 要转换的byte数组
     * @return 返回long数组，若bytes为null则返回0个元素的long数组
     */
    public static long[] longs(byte... bytes) {
        if (bytes != null && bytes.length > 0) {
            int len = bytes.length;
            long[] longs = new long[len];
            for (int i = 0; i < len; i++) {
                longs[i] = bytes[i];
            }
            return longs;
        }
        return longs();
    }

    /**
     * long数组转int数组
     *
     * @param longs 要转换的long数组
     * @return 返回int数组，若longs为null则返回0个元素的int数组
     */
    public static int[] ints(long... longs) {
        if (longs != null && longs.length > 0) {
            int len = longs.length;
            int[] ints = new int[len];
            for (int i = 0; i < len; i++) {
                ints[i] = (int) longs[i];
            }
            return ints;
        }
        return ints();
    }

    /**
     * short数组转int数组
     *
     * @param shorts 要转换的short数组
     * @return 返回int数组，若shorts为null则返回0个元素的int数组
     */
    public static int[] ints(short... shorts) {
        if (shorts != null && shorts.length > 0) {
            int len = shorts.length;
            int[] ints = new int[len];
            for (int i = 0; i < len; i++) {
                ints[i] = shorts[i];
            }
            return ints;
        }
        return ints();
    }

    /**
     * byte数组转int数组
     *
     * @param bytes 要转换的byte数组
     * @return 返回int数组，若bytes为null则返回0个元素的int数组
     */
    public static int[] ints(byte... bytes) {
        if (bytes != null && bytes.length > 0) {
            int len = bytes.length;
            int[] ints = new int[len];
            for (int i = 0; i < len; i++) {
                ints[i] = bytes[i];
            }
            return ints;
        }
        return ints();
    }

    /**
     * long数组转short数组
     *
     * @param longs 要转换的long数组
     * @return 返回short数组，若longs为null则返回0个元素的short数组
     */
    public static short[] shorts(long... longs) {
        if (longs != null && longs.length > 0) {
            int len = longs.length;
            short[] shorts = new short[len];
            for (int i = 0; i < len; i++) {
                shorts[i] = (short) longs[i];
            }
            return shorts;
        }
        return shorts();
    }

    /**
     * int数组转short数组
     *
     * @param ints 要转换的int数组
     * @return 返回short数组，若ints为null则返回0个元素的short数组
     */
    public static short[] shorts(int... ints) {
        if (ints != null && ints.length > 0) {
            int len = ints.length;
            short[] shorts = new short[len];
            for (int i = 0; i < len; i++) {
                shorts[i] = (short) ints[i];
            }
            return shorts;
        }
        return shorts();
    }

    /**
     * byte数组转short数组
     *
     * @param bytes 要转换的byte数组
     * @return 返回short数组，若bytes为null则返回0个元素的short数组
     */
    public static short[] shorts(byte... bytes) {
        if (bytes != null && bytes.length > 0) {
            int len = bytes.length;
            short[] shorts = new short[len];
            for (int i = 0; i < len; i++) {
                shorts[i] = bytes[i];
            }
            return shorts;
        }
        return shorts();
    }

    /**
     * long数组转byte数组
     *
     * @param longs 要转换的long数组
     * @return 返回byte数组，若longs为null则返回0个元素的byte数组
     */
    public static byte[] bytes(long... longs) {
        if (longs != null && longs.length > 0) {
            int len = longs.length;
            byte[] bytes = new byte[len];
            for (int i = 0; i < len; i++) {
                bytes[i] = (byte) longs[i];
            }
            return bytes;
        }
        return bytes();
    }

    /**
     * int数组转byte数组
     *
     * @param ints 要转换的int数组
     * @return 返回byte数组，若ints为null则返回0个元素的byte数组
     */
    public static byte[] bytes(int... ints) {
        if (ints != null && ints.length > 0) {
            int len = ints.length;
            byte[] bytes = new byte[len];
            for (int i = 0; i < len; i++) {
                bytes[i] = (byte) ints[i];
            }
            return bytes;
        }
        return bytes();
    }

    /**
     * short数组转byte数组
     *
     * @param shorts 要转换的short数组
     * @return 返回byte数组，若shorts为null则返回0个元素的byte数组
     */
    public static byte[] bytes(short... shorts) {
        if (shorts != null && shorts.length > 0) {
            int len = shorts.length;
            byte[] bytes = new byte[len];
            for (int i = 0; i < len; i++) {
                bytes[i] = (byte) shorts[i];
            }
            return bytes;
        }
        return bytes();
    }

    /**
     * float数组转double数组
     *
     * @param floats 要转换的float数组
     * @return 返回double数组，若floats为null则返回0个元素的double数组
     */
    public static double[] doubles(float... floats) {
        if (floats != null && floats.length > 0) {
            int len = floats.length;
            double[] doubles = new double[len];
            for (int i = 0; i < len; i++) {
                doubles[i] = floats[i];
            }
            return doubles;
        }
        return doubles();
    }

    /**
     * double数组转float数组
     *
     * @param doubles 要转换的double数组
     * @return 返回float数组，若doubles为null则返回0个元素的float数组
     */
    public static float[] floats(double... doubles) {
        if (doubles != null && doubles.length > 0) {
            int len = doubles.length;
            float[] floats = new float[len];
            for (int i = 0; i < len; i++) {
                floats[i] = (float) doubles[i];
            }
            return floats;
        }
        return floats();
    }

    /**
     * 判断传入的对象数组是否都不为null
     *
     * @param objs 要判断的对象数组
     * @return 若objs中的每个元素都不为null，则返回true,只要有一个是null或者objs数组本身是null就返回false
     */
    public static boolean notNull(Object... objs) {
        if (objs != null) {
            for (Object obj : objs) {
                if (obj == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 查找objs中第一个不为null的对象
     *
     * @param objs 要查找的对象
     * @return 返回这个对象对应的Optional, 若objs为null, 或里面的元素都为null, 则返回空的Optional对象{@link Optional#empty()}
     */
    @SafeVarargs
    public static <T> Optional<T> firstNotNull(T... objs) {
        if (objs != null) {
            for (T t : objs) {
                if (t != null) {
                    return Optional.of(t);
                }
            }
        }
        return Optional.empty();
    }


    /**
     * byte数组转成16进制表示的字符串数组
     *
     * @param array 字节数组
     * @return 返回十六进制的字符串数组，数组中每个元素对应了byte数组array中每个元素的十六进制的表示形式
     */
    public static String[] hex(byte[] array) {
        if (array != null) {
            String[] hexs = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                byte b = array[i];
                hexs[i] = HEX_DIGITS[b >>> 4 & 0xf] + "" + HEX_DIGITS[b & 0xf];
            }
            return hexs;
        }
        return strings();
    }

}
