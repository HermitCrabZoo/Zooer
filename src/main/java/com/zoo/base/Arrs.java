package com.zoo.base;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zoo.mix.Beaner;

public final class Arrs {

    private Arrs() {
    }

    /**
     * 构造long型数组,无传入参数则返回一个空数组(0个元素)
     *
     * @param ls
     * @return
     */
    public static long[] of(long... ls) {
        return ls == null ? Typer.longs() : ls;
    }

    /**
     * 构造int型数组,无传入参数则返回一个空数组(0个元素)
     *
     * @param is
     * @return
     */
    public static int[] of(int... is) {
        return is == null ? Typer.ints() : is;
    }


    /**
     * 构造short型数组,无传入参数则返回一个空数组(0个元素)
     *
     * @param ss
     * @return
     */
    public static short[] of(short... ss) {
        return ss == null ? Typer.shorts() : ss;
    }


    /**
     * 构造byte型数组,无传入参数则返回一个空数组(0个元素)
     *
     * @param bs
     * @return
     */
    public static byte[] of(byte... bs) {
        return bs == null ? Typer.bytes() : bs;
    }


    /**
     * 构造double型数组,无传入参数则返回一个空数组(0个元素)
     *
     * @param ds
     * @return
     */
    public static double[] of(double... ds) {
        return ds == null ? Typer.doubles() : ds;
    }


    /**
     * 构造float型数组,无传入参数则返回一个空数组(0个元素)
     *
     * @param fs
     * @return
     */
    public static float[] of(float... fs) {
        return fs == null ? Typer.floats() : fs;
    }


    /**
     * 构造char型数组,无传入参数则返回一个空数组(0个元素)
     *
     * @param cs
     * @return
     */
    public static char[] of(char... cs) {
        return cs == null ? Typer.chars() : cs;
    }


    /**
     * 构造boolean型数组,无传入参数则返回一个空数组(0个元素)
     *
     * @param bs
     * @return
     */
    public static boolean[] of(boolean... bs) {
        return bs == null ? Typer.booleans() : bs;
    }


    /**
     * 生成一个可增删改的list,传入参数中为null的将被过滤掉
     *
     * @param ts
     * @return
     */
    @SafeVarargs
    public static <T> List<T> of(T... ts) {
        List<T> list;
        if (ts != null) {
            list = new ArrayList<>(ts.length);
            Collections.addAll(list, ts);
        } else {
            list = new ArrayList<>();
        }
        return list;
    }


    /**
     * 将total平均分成len份的数组返回,若total不是len的整数倍,
     * 那么数组值将尽量平均(数组中元素和仍然等于total).
     *
     * @param total
     * @param len   必须大于0
     * @return
     */
    public static long[] avgs(long total, int len) {
        if (len > 0) {
            long[] avg = new long[len];
            long unit = total / len;
            long remain = total % len;
            long one = remain < 0 ? -1 : 1;
            remain = Math.abs(remain);
            if (total != 0) {
                for (int i = 0; i < len; i++) {
                    avg[i] = unit;
                }
                for (int i = 0; i < remain; i++) {
                    avg[i] += one;
                }
            }
            return avg;
        }
        return Typer.longs();
    }


    /**
     * 将total平均分成len份的数组返回,若total不是len的整数倍,
     * 那么数组值将尽量平均(数组中元素和仍然等于total).
     *
     * @param total
     * @param len   必须大于0
     * @return
     */
    public static int[] avgs(int total, int len) {
        if (len > 0) {
            int[] avg = new int[len];
            int unit = total / len;
            int remain = total % len;
            int one = remain < 0 ? -1 : 1;
            remain = Math.abs(remain);
            if (total != 0) {
                for (int i = 0; i < len; i++) {
                    avg[i] = unit;
                }
                for (int i = 0; i < remain; i++) {
                    avg[i] += one;
                }
            }
            return avg;
        }
        return Typer.ints();
    }


    /**
     * 将总数total按每份为each来平均分,返回数组中各元素和等于total.<br/>若total大于each,并且total不是each的整数倍,那么返回数组中最后一个元素的值等于total%each,而其他元素的值在total大于0时为each,在total小于0时值为-each.
     *
     * @param total
     * @param each  必须大于0
     * @return
     */
    public static int[] everys(int total, int each) {
        if (each > 0) {
            int baseLen = Math.abs(total / each);
            int remainder = total % each;
            boolean hasRemainder = remainder != 0;
            int[] every = new int[baseLen + (hasRemainder ? 1 : 0)];
            if (total < 0) {
                for (int i = 0; i < baseLen; i++) {
                    every[i] = -each;
                }
            } else {
                for (int i = 0; i < baseLen; i++) {
                    every[i] = each;
                }
            }
            if (hasRemainder) {
                every[baseLen] = remainder;
            }
            return every;
        }
        return Typer.ints();
    }


    /**
     * 为数组中的每个元素增step
     *
     * @param longs
     * @param step
     * @return
     */
    public static long[] raise(long[] longs, long step) {
        if (longs != null) {
            for (int i = 0; i < longs.length; i++) {
                longs[i] += step;
            }
            return longs;
        }
        return Typer.longs();
    }


    /**
     * 为数组中的每个元素增step
     *
     * @param ints
     * @param step
     * @return
     */
    public static int[] raise(int[] ints, int step) {
        if (ints != null) {
            for (int i = 0; i < ints.length; i++) {
                ints[i] += step;
            }
            return ints;
        }
        return Typer.ints();
    }


    /**
     * 为数组中的每个元素增step
     *
     * @param shorts
     * @param step
     * @return
     */
    public static short[] raise(short[] shorts, short step) {
        if (shorts != null) {
            for (int i = 0; i < shorts.length; i++) {
                shorts[i] += step;
            }
            return shorts;
        }
        return Typer.shorts();
    }


    /**
     * 为数组中的每个元素增step
     *
     * @param bytes
     * @param step
     * @return
     */
    public static byte[] raise(byte[] bytes, byte step) {
        if (bytes != null) {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] += step;
            }
            return bytes;
        }
        return Typer.bytes();
    }


    /**
     * 为数组中的每个元素增step
     *
     * @param doubles
     * @param step
     * @return
     */
    public static double[] raise(double[] doubles, double step) {
        if (doubles != null) {
            for (int i = 0; i < doubles.length; i++) {
                doubles[i] += step;
            }
            return doubles;
        }
        return Typer.doubles();
    }


    /**
     * 为数组中的每个元素增step
     *
     * @param floats
     * @param step
     * @return
     */
    public static float[] raise(float[] floats, float step) {
        if (floats != null) {
            for (int i = 0; i < floats.length; i++) {
                floats[i] += step;
            }
            return floats;
        }
        return Typer.floats();
    }


    /**
     * 为数组中的每个元素减step
     *
     * @param longs
     * @param step
     * @return
     */
    public static long[] reduce(long[] longs, long step) {
        if (longs != null) {
            for (int i = 0; i < longs.length; i++) {
                longs[i] -= step;
            }
            return longs;
        }
        return Typer.longs();
    }


    /**
     * 为数组中的每个元素减step
     *
     * @param ints
     * @param step
     * @return
     */
    public static int[] reduce(int[] ints, int step) {
        if (ints != null) {
            for (int i = 0; i < ints.length; i++) {
                ints[i] -= step;
            }
            return ints;
        }
        return Typer.ints();
    }


    /**
     * 为数组中的每个元素减step
     *
     * @param shorts
     * @param step
     * @return
     */
    public static short[] reduce(short[] shorts, short step) {
        if (shorts != null) {
            for (int i = 0; i < shorts.length; i++) {
                shorts[i] -= step;
            }
            return shorts;
        }
        return Typer.shorts();
    }


    /**
     * 为数组中的每个元素减step
     *
     * @param bytes
     * @param step
     * @return
     */
    public static byte[] reduce(byte[] bytes, byte step) {
        if (bytes != null) {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] -= step;
            }
            return bytes;
        }
        return Typer.bytes();
    }


    /**
     * 为数组中的每个元素减step
     *
     * @param doubles
     * @param step
     * @return
     */
    public static double[] reduce(double[] doubles, double step) {
        if (doubles != null) {
            for (int i = 0; i < doubles.length; i++) {
                doubles[i] -= step;
            }
            return doubles;
        }
        return doubles;
    }


    /**
     * 为数组中的每个元素减step
     *
     * @param floats
     * @param step
     * @return
     */
    public static float[] reduce(float[] floats, float step) {
        if (floats != null) {
            for (int i = 0; i < floats.length; i++) {
                floats[i] -= step;
            }
            return floats;
        }
        return Typer.floats();
    }


    /**
     * 为数组中的每个元素增step,返回新数组
     *
     * @param longs
     * @param step
     * @return
     */
    public static long[] raiseNew(long[] longs, long step) {
        return Optional.ofNullable(longs).map(ls -> Arrays.stream(ls).parallel().map(l -> l + step).toArray()).orElse(Typer.longs());
    }


    /**
     * 为数组中的每个元素增step,返回新数组
     *
     * @param ints
     * @param step
     * @return
     */
    public static int[] raiseNew(int[] ints, int step) {
        return Optional.ofNullable(ints).map(is -> Arrays.stream(is).parallel().map(i -> i + step).toArray()).orElse(Typer.ints());
    }


    /**
     * 为数组中的每个元素减step,返回新数组
     *
     * @param shorts
     * @param step
     * @return
     */
    public static short[] raiseNew(short[] shorts, short step) {
        return Optional.ofNullable(shorts).map(ss -> Typer.shorts(raiseNew(Typer.ints(ss), step))).orElse(Typer.shorts());
    }


    /**
     * 为数组中的每个元素减step,返回新数组
     *
     * @param bytes
     * @param step
     * @return
     */
    public static byte[] raiseNew(byte[] bytes, byte step) {
        return Optional.ofNullable(bytes).map(bs -> Typer.bytes(raiseNew(Typer.ints(bs), step))).orElse(Typer.bytes());
    }


    /**
     * 为数组中的每个元素增step,返回新数组
     *
     * @param doubles
     * @param step
     * @return
     */
    public static double[] raiseNew(double[] doubles, double step) {
        return Optional.ofNullable(doubles).map(ds -> Arrays.stream(ds).parallel().map(d -> d + step).toArray()).orElse(Typer.doubles());
    }


    /**
     * 为数组中的每个元素增step,返回新数组
     *
     * @param floats
     * @param step
     * @return
     */
    public static float[] raiseNew(float[] floats, float step) {
        return Optional.ofNullable(floats).map(fs -> Typer.floats(raiseNew(Typer.doubles(fs), step))).orElse(Typer.floats());
    }


    /**
     * 为数组中的每个元素减step,返回新数组
     *
     * @param longs
     * @param step
     * @return
     */
    public static long[] reduceNew(long[] longs, long step) {
        return Optional.ofNullable(longs).map(ls -> Arrays.stream(ls).parallel().map(l -> l - step).toArray()).orElse(Typer.longs());
    }


    /**
     * 为数组中的每个元素减step,返回新数组
     *
     * @param ints
     * @param step
     * @return
     */
    public static int[] reduceNew(int[] ints, int step) {
        return Optional.ofNullable(ints).map(is -> Arrays.stream(is).parallel().map(i -> i - step).toArray()).orElse(Typer.ints());
    }


    /**
     * 为数组中的每个元素减step,返回新数组
     *
     * @param shorts
     * @param step
     * @return
     */
    public static short[] reduceNew(short[] shorts, short step) {
        return Optional.ofNullable(shorts).map(ss -> Typer.shorts(reduceNew(Typer.ints(ss), step))).orElse(Typer.shorts());
    }


    /**
     * 为数组中的每个元素减step,返回新数组
     *
     * @param bytes
     * @param step
     * @return
     */
    public static byte[] reduceNew(byte[] bytes, byte step) {
        return Optional.ofNullable(bytes).map(bs -> Typer.bytes(reduceNew(Typer.ints(bs), step))).orElse(Typer.bytes());
    }


    /**
     * 为数组中的每个元素减step,返回新数组
     *
     * @param doubles
     * @param step
     * @return
     */
    public static double[] reduceNew(double[] doubles, double step) {
        return Optional.ofNullable(doubles).map(ds -> Arrays.stream(ds).parallel().map(d -> d - step).toArray()).orElse(Typer.doubles());
    }


    /**
     * 为数组中的每个元素减step,返回新数组
     *
     * @param floats
     * @param step
     * @return
     */
    public static float[] reduceNew(float[] floats, float step) {
        return Optional.ofNullable(floats).map(fs -> Typer.floats(reduceNew(Typer.doubles(fs), step))).orElse(Typer.floats());
    }


    /**
     * 去重复
     *
     * @param longs
     * @return
     */
    public static long[] distinct(long... longs) {
        return Optional.ofNullable(longs).map(ls -> Arrays.stream(ls).parallel().distinct().toArray()).orElse(Typer.longs());
    }


    /**
     * 去重复
     *
     * @param ints
     * @return
     */
    public static int[] distinct(int... ints) {
        return Optional.ofNullable(ints).map(is -> Arrays.stream(is).parallel().distinct().toArray()).orElse(Typer.ints());
    }


    /**
     * 去重复
     *
     * @param shorts
     * @return
     */
    public static short[] distinct(short... shorts) {
        return Optional.ofNullable(shorts).map(ss -> Typer.shorts(distinct(Typer.ints(ss)))).orElse(Typer.shorts());
    }


    /**
     * 去重复
     *
     * @param bytes
     * @return
     */
    public static byte[] distinct(byte... bytes) {
        return Optional.ofNullable(bytes).map(bs -> Typer.bytes(distinct(Typer.ints(bs)))).orElse(Typer.bytes());
    }


    /**
     * 去重复
     *
     * @param doubles
     * @return
     */
    public static double[] distinct(double... doubles) {
        return Optional.ofNullable(doubles).map(ds -> Arrays.stream(ds).parallel().distinct().toArray()).orElse(Typer.doubles());
    }


    /**
     * 去重复
     *
     * @param floats
     * @return
     */
    public static float[] distinct(float... floats) {
        return Optional.ofNullable(floats).map(fs -> Typer.floats(distinct(Typer.doubles(fs)))).orElse(Typer.floats());
    }


    /**
     * 去重复
     *
     * @param strings
     * @return
     */
    public static String[] distinct(String... strings) {
        return Optional.ofNullable(strings).map(ds -> Arrays.stream(ds).parallel().distinct().toArray(String[]::new)).orElse(Typer.strings());
    }


    /**
     * 求平均值
     *
     * @param longs
     * @return
     */
    public static double avg(long... longs) {
        return Optional.ofNullable(longs).map(ls -> Arrays.stream(ls).parallel().average().orElse(0.0)).orElse(0.0);
    }


    /**
     * 求平均值
     *
     * @param ints
     * @return
     */
    public static double avg(int... ints) {
        return Optional.ofNullable(ints).map(is -> Arrays.stream(is).parallel().average().orElse(0.0)).orElse(0.0);
    }


    /**
     * 求平均值
     *
     * @param shorts
     * @return
     */
    public static double avg(short... shorts) {
        return Optional.ofNullable(shorts).map(ss -> avg(Typer.ints(ss))).orElse(0.0);
    }


    /**
     * 求平均值
     *
     * @param bytes
     * @return
     */
    public static double avg(byte... bytes) {
        return Optional.ofNullable(bytes).map(bs -> avg(Typer.ints(bs))).orElse(0.0);
    }


    /**
     * 求平均值
     *
     * @param doubles
     * @return
     */
    public static double avg(double... doubles) {
        return Optional.ofNullable(doubles).map(ds -> Arrays.stream(ds).parallel().average().orElse(0.0)).orElse(0.0);
    }


    /**
     * 求平均值
     *
     * @param floats
     * @return
     */
    public static double avg(float... floats) {
        return Optional.ofNullable(floats).map(fs -> avg(Typer.doubles(fs))).orElse(0.0);
    }


    /**
     * 求和
     *
     * @param longs
     * @return
     */
    public static long sum(long... longs) {
        return Optional.ofNullable(longs).map(ls -> Arrays.stream(ls).parallel().sum()).orElse(0L);
    }


    /**
     * 求和
     *
     * @param ints
     * @return
     */
    public static long sum(int... ints) {
        return Optional.ofNullable(ints).map(is -> Arrays.stream(is).parallel().sum()).orElse(0);
    }


    /**
     * 求和
     *
     * @param shorts
     * @return
     */
    public static long sum(short... shorts) {
        return Optional.ofNullable(shorts).map(ss -> sum(Typer.ints(ss))).orElse(0L);
    }


    /**
     * 求和
     *
     * @param bytes
     * @return
     */
    public static long sum(byte... bytes) {
        return Optional.ofNullable(bytes).map(bs -> sum(Typer.ints(bs))).orElse(0L);
    }


    /**
     * 求和
     *
     * @param doubles
     * @return
     */
    public static double sum(double... doubles) {
        return Optional.ofNullable(doubles).map(ds -> Arrays.stream(ds).parallel().sum()).orElse(0.0);
    }


    /**
     * 求和
     *
     * @param floats
     * @return
     */
    public static double sum(float... floats) {
        return Optional.ofNullable(floats).map(fs -> sum(Typer.doubles(fs))).orElse(0.0);
    }


    /**
     * 求最大值
     *
     * @param longs
     * @return
     */
    public static long max(long... longs) {
        return Optional.ofNullable(longs).map(ls -> Arrays.stream(ls).parallel().max().orElse(0L)).orElse(0L);
    }


    /**
     * 求最大值
     *
     * @param ints
     * @return
     */
    public static int max(int... ints) {
        return Optional.ofNullable(ints).map(is -> Arrays.stream(is).parallel().max().orElse(0)).orElse(0);
    }


    /**
     * 求最大值
     *
     * @param shorts
     * @return
     */
    public static short max(short... shorts) {
        return Optional.ofNullable(shorts).map(ss -> (short) max(Typer.ints(ss))).orElse((short) 0);
    }


    /**
     * 求最大值
     *
     * @param bytes
     * @return
     */
    public static byte max(byte... bytes) {
        return Optional.ofNullable(bytes).map(bs -> (byte) max(Typer.ints(bs))).orElse((byte) 0);
    }


    /**
     * 求最大值
     *
     * @param doubles
     * @return
     */
    public static double max(double... doubles) {
        return Optional.ofNullable(doubles).map(ds -> Arrays.stream(ds).parallel().max().orElse(0.0)).orElse(0.0);
    }


    /**
     * 求最大值
     *
     * @param floats
     * @return
     */
    public static float max(float... floats) {
        return Optional.ofNullable(floats).map(fs -> (float) max(Typer.doubles(fs))).orElse(0.0f);
    }


    /**
     * 求最小值
     *
     * @param longs
     * @return
     */
    public static long min(long... longs) {
        return Optional.ofNullable(longs).map(ls -> Arrays.stream(ls).parallel().min().orElse(0L)).orElse(0L);
    }


    /**
     * 求最小值
     *
     * @param ints
     * @return
     */
    public static int min(int... ints) {
        return Optional.ofNullable(ints).map(is -> Arrays.stream(is).parallel().min().orElse(0)).orElse(0);
    }


    /**
     * 求最小值
     *
     * @param shorts
     * @return
     */
    public static short min(short... shorts) {
        return Optional.ofNullable(shorts).map(ss -> (short) min(Typer.ints(ss))).orElse((short) 0);
    }


    /**
     * 求最小值
     *
     * @param bytes
     * @return
     */
    public static byte min(byte... bytes) {
        return Optional.ofNullable(bytes).map(bs -> (byte) min(Typer.ints(bs))).orElse((byte) 0);
    }


    /**
     * 求最小值
     *
     * @param doubles
     * @return
     */
    public static double min(double... doubles) {
        return Optional.ofNullable(doubles).map(ds -> Arrays.stream(ds).parallel().min().orElse(0.0)).orElse(0.0);
    }


    /**
     * 求最小值
     *
     * @param floats
     * @return
     */
    public static float min(float... floats) {
        return Optional.ofNullable(floats).map(fs -> (float) min(Typer.doubles(fs))).orElse(0.0f);
    }


    /**
     * 传入int类型的数组，返回将int数组每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param ints
     * @return
     */
    public static String join(String separator, int... ints) {
        return join(separator, toStrings(ints));
    }


    /**
     * 传入long类型的数组，返回将long数组每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param longs
     * @return
     */
    public static String join(String separator, long... longs) {
        return join(separator, toStrings(longs));
    }


    /**
     * 传入short类型数组，返回将short数组每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param shorts
     * @return
     */
    public static String join(String separator, short... shorts) {
        return join(separator, toStrings(shorts));
    }


    /**
     * 传入byte类型数组，返回将byte数组每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param bytes
     * @return
     */
    public static String join(String separator, byte... bytes) {
        return join(separator, toStrings(bytes));
    }


    /**
     * 传入char类型数组，返回将char数组每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param chars
     * @return
     */
    public static String join(String separator, char... chars) {
        return join(separator, toStrings(chars));
    }


    /**
     * 传入boolean数组，返回将boolean数组每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param booleans
     * @return
     */
    public static String join(String separator, boolean... booleans) {
        return join(separator, toStrings(booleans));
    }


    /**
     * 传入double数组，返回将double数组每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param doubles
     * @return
     */
    public static String join(String separator, double... doubles) {
        return join(separator, toStrings(doubles));
    }


    /**
     * 传入float数组，返回将float数组每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param floats
     * @return
     */
    public static String join(String separator, float... floats) {
        return join(separator, toStrings(floats));
    }


    /**
     * 传入连接符，将字符串数组的每个元素之间用连接符连接起来
     *
     * @param separator
     * @param strs
     * @return
     */
    public static String join(String separator, String... strs) {
        if (separator == null || strs == null || strs.length < 1) {
            return Strs.empty();
        }
        strs = nullToEmpty(strs);
        int len = strs.length;
        StringBuffer stringBuffer = new StringBuffer(len).append(strs[0]);
        for (int i = 1; i < len; i++) {
            stringBuffer.append(separator).append(strs[i]);
        }
        return stringBuffer.toString();
    }


    /**
     * 传入T数组，返回将T数组每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param array
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> String join(String separator, T... array) {
        return join(separator, toStrings(array));
    }


    /**
     * 传入T型的List，返回将List每个元素用连接符连接起来的字符串
     *
     * @param separator
     * @param list
     * @return
     */
    public static <T> String join(String separator, List<T> list) {
        if (list == null) {
            return Strs.empty();
        }
        int len = list.size();
        String[] strings = new String[len];
        for (int i = 0; i < len; i++) {
            strings[i] = String.valueOf(list.get(i));
        }
        return join(separator, strings);
    }


    /**
     * 将obj对象转换成String数组返回,若obj对象为null或者obj对象不是数组类型那么将返回一个空数组(长度为0)
     * 若传入的为对象类型的数组，此类型未对'toString'方法做任何特定的实现，那么结果可能料想不到。
     *
     * @param arrayObj 任何类型的数组对象
     * @return
     */
    public static String[] toStrings(Object arrayObj) {
        if (arrayObj == null || !arrayObj.getClass().isArray()) {
            return Strs.empties();
        }
        int len = Array.getLength(arrayObj);
        String[] strs = new String[len];
        for (int i = 0; i < len; i++) {
            strs[i] = String.valueOf(Array.get(arrayObj, i));
        }
        return strs;
    }


    /**
     * 获取list中每个元素的field属性的值，并返回，若list中的元素是Map的子类的实例，那么将获取该map的key为field的值。
     *
     * @param list
     * @param field
     * @return
     */
    public static <T> List<Object> fields(List<T> list, String field) {
        return Beaner.values(list, field);
    }


    /**
     * 判断elements中的元素是否都在onlys中，如果是返回true(即elements是onlys的可重复元素的子集)，否则elements存在非onlys集合里的元素返回false
     *
     * @param elements
     * @param onlys
     * @return 若elements为null或empty，或onlys为null或empty，则返回false。
     */
    public static <T> boolean containOnly(List<T> elements, List<T> onlys) {
        if (elements != null && !elements.isEmpty() && onlys != null && !onlys.isEmpty()) {
            for (T t : elements) {
                if (!onlys.contains(t)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 判断elements是否包含anys中的任一元素，如果包含返回true(即elements与anys有交集)，否则不包含anys中任一元素返回false
     *
     * @param elements
     * @param anys
     * @return 若elements为null或empty，或anys为null或empty，则返回false。
     */
    public static <T> boolean containAny(List<T> elements, List<T> anys) {
        if (elements != null && !elements.isEmpty() && anys != null && !anys.isEmpty()) {
            for (T t : elements) {
                if (anys.contains(t)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 将String型数组中的null值转换成空字符串，返回新生成的String数组
     *
     * @param strings
     * @return
     */
    public static String[] nullToEmpty(String[] strings) {
        return Optional.ofNullable(strings).map(ss -> Arrays.stream(ss).parallel().map(Strs::nullToEmpty).toArray(String[]::new)).orElse(Strs.empties());
    }


    /**
     * 将String型字符串数组中的null去除，返回新生成的String数组
     *
     * @param strings
     * @return
     */
    public static String[] removeNull(String[] strings) {
        return Optional.ofNullable(strings).map(ss -> Arrays.stream(ss).parallel().filter(Objects::nonNull).toArray(String[]::new)).orElse(Strs.empties());
    }


    /**
     * 将List对象里的null元素去除，返回新生成的List对象
     *
     * @param list
     * @return
     */
    public static <T> List<T> removeNull(List<T> list) {
        return Optional.ofNullable(list).map(ss -> ss.parallelStream().filter(Objects::nonNull).collect(Collectors.toList())).orElse(Collections.emptyList());
    }


    /**
     * 将T型数组转换成List<T>返回
     *
     * @param array
     * @return
     */
    public static <T> List<T> toList(T[] array) {
        return Optional.ofNullable(array).map(arr -> Arrays.stream(arr).parallel().collect(Collectors.toList())).orElse(Collections.emptyList());
    }


    /**
     * 判断list是否不为null并且不为empty(有元素)，为null或没有元素都会返回false。
     *
     * @param list
     * @return
     */
    public static <T> boolean notEmpty(List<T> list) {
        return !(list == null || list.isEmpty());
    }


    /**
     * 判断arr是否不为null并且长度大于0，为null或长度小于0都会返回false。
     *
     * @param arr
     * @return
     */
    public static <T> boolean notEmpty(T[] arr) {
        return !(arr == null || arr.length == 0);
    }


    /**
     * 判断longs是否不为null并且长度大于0，为null或长度小于0都会返回false。
     *
     * @param longs
     * @return
     */
    public static boolean notEmpty(long[] longs) {
        return !(longs == null || longs.length == 0);
    }


    /**
     * 判断ints是否不为null并且长度大于0，为null或长度小于0都会返回false。
     *
     * @param ints
     * @return
     */
    public static boolean notEmpty(int[] ints) {
        return !(ints == null || ints.length == 0);
    }


    /**
     * 判断shorts是否不为null并且长度大于0，为null或长度小于0都会返回false。
     *
     * @param shorts
     * @return
     */
    public static boolean notEmpty(short[] shorts) {
        return !(shorts == null || shorts.length == 0);
    }


    /**
     * 判断bytes是否不为null并且长度大于0，为null或长度小于0都会返回false。
     *
     * @param bytes
     * @return
     */
    public static boolean notEmpty(byte[] bytes) {
        return !(bytes == null || bytes.length == 0);
    }


    /**
     * 判断doubles是否不为null并且长度大于0，为null或长度小于0都会返回false。
     *
     * @param doubles
     * @return
     */
    public static boolean notEmpty(double[] doubles) {
        return !(doubles == null || doubles.length == 0);
    }


    /**
     * 判断floats是否不为null并且长度大于0，为null或长度小于0都会返回false。
     *
     * @param floats
     * @return
     */
    public static boolean notEmpty(float[] floats) {
        return !(floats == null || floats.length == 0);
    }


    /**
     * 判断chars是否不为null并且长度大于0，为null或长度小于0都会返回false。
     *
     * @param chars
     * @return
     */
    public static boolean notEmpty(char[] chars) {
        return !(chars == null || chars.length == 0);
    }


    /**
     * list深度拷贝的方法，返回新list，若src为null或拷贝失败则返回空list(empty)
     *
     * @param src
     * @return 不会返回null
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> List<T> deepCopy(List<T> src) {
        byte[] serializeds = Serializer.serialize(src);
        Optional<List> newObj = Serializer.deserialize(serializeds, List.class);
        return newObj.orElseGet(ArrayList::new);
    }


    /**
     * 查到list中第一个字段名为equalKey，对应字段值为equalValue的元素并返回。
     * 若list中的元素是Map或Map的子类的实例，那么上述的equalKey对应了Map实例的Key。
     *
     * @param list
     * @param equalKey
     * @param equalValue
     * @return 若list不为空，并匹配到至少一个对象，那么将返回该对象，否则返回空
     */
    public static <T> T firstEqualIf(List<T> list, Object equalKey, Object equalValue) {
        if (list != null) {
            for (T t : list) {
                if (Objects.equals(Beaner.value(t, equalKey), equalValue)) {
                    return t;
                }
            }
        }
        return null;
    }


    /**
     * 查找list中所有字段名为equalKey，，对应字段值为equalValue的元素，并返回这些元素的集合，
     * 若list中的元素是Map或Map的子类的实例，那么上述的equalKey对应了Map实例的Key。
     *
     * @param list
     * @param equalKey
     * @param equalValue
     * @return 不会返回null
     */
    public static <T> List<T> equalsIf(List<T> list, Object equalKey, Object equalValue) {
        List<T> items = new ArrayList<>();
        if (list != null) {
            for (T t : list) {
                if (Objects.equals(Beaner.value(t, equalKey), equalValue)) {
                    items.add(t);
                }
            }
        }
        return items;
    }

    /**
     * 查找list中第一个字段名为equalKey，对应字段值为equalValue的元素，并返回该元素字段名为valueKey的字段值，
     * 若list中的元素是Map或Map的子类的实例，那么上述的equalKey、valueKey对应了Map实例的Key。
     *
     * @param list
     * @param equalKey
     * @param equalValue
     * @param valueKey
     * @return 若未匹配到则返回null
     */
    public static <T> Object firstValueIf(List<T> list, Object equalKey, Object equalValue, Object valueKey) {
        return Beaner.value(firstEqualIf(list, equalKey, equalValue), valueKey);
    }


    /**
     * 查找list中所有字段名为equalKey，对应字段值为equalValue的元素，并返回这些元素的字段名为valueKey对应的字段值的集合，
     * 若list中的元素是Map或Map的子类的实例，那么上述的equalKey、valueKey对应了Map实例的Key。
     *
     * @param list
     * @param equalKey
     * @param equalValue
     * @param valueKey
     * @return 不会返回null
     */
    public static <T> List<Object> valuesIf(List<T> list, Object equalKey, Object equalValue, Object valueKey) {
        return Beaner.values(equalsIf(list, equalKey, equalValue), valueKey);
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @param ts
     * @param t
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean contains(T t, T... ts) {
        if (ts != null) {
            for (T t1 : ts) {
                if (Objects.equals(t1, t)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @param ls
     * @param l
     * @return
     */
    public static boolean contains(long l, long... ls) {
        if (ls != null) {
            for (long l1 : ls) {
                if (l1 == l) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @param ls
     * @param l
     * @return
     */
    public static boolean contains(int l, int... ls) {
        if (ls != null) {
            for (int l1 : ls) {
                if (l1 == l) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @param ls
     * @param l
     * @return
     */
    public static boolean contains(short l, short... ls) {
        if (ls != null) {
            for (short l1 : ls) {
                if (l1 == l) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @param ls
     * @param l
     * @return
     */
    public static boolean contains(byte l, byte... ls) {
        if (ls != null) {
            for (byte l1 : ls) {
                if (l1 == l) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @param ls
     * @param l
     * @return
     */
    public static boolean contains(double l, double... ls) {
        if (ls != null) {
            for (double l1 : ls) {
                if (l1 == l) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @param ls
     * @param l
     * @return
     */
    public static boolean contains(float l, float... ls) {
        if (ls != null) {
            for (float l1 : ls) {
                if (l1 == l) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @param ls
     * @param l
     * @return
     */
    public static boolean contains(char l, char... ls) {
        if (ls != null) {
            for (char l1 : ls) {
                if (l1 == l) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @param ls
     * @param l
     * @return
     */
    public static boolean contains(boolean l, boolean... ls) {
        if (ls != null) {
            for (boolean l1 : ls) {
                if (l1 == l) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 将数组按参数中出现的先后顺序合并为一个数组返回。
     *
     * @param mores
     * @return 若传入至少有一个非null数组，那么就不会返回null，否则返回null，
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] concat(T[]... mores) {
        if (mores == null) {
            return null;
        }
        int len = Arrays.stream(mores).filter(Objects::nonNull).map(a -> a.length).reduce(Integer::sum).orElse(0);
        T[] array = Arrays.stream(mores).filter(Objects::nonNull).findFirst().orElse(null);
        if (len <= 0 || array == null) {
            return null;
        }
        T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), len);
        int start = 0;
        for (T[] arr : mores) {
            if (arr != null && arr.length > 0) {
                int cpLen = arr.length;
                System.arraycopy(arr, 0, result, start, cpLen);
                start += cpLen;
            }
        }
        return result;
    }

    /**
     * 将多个数组合并为一个新的数组输出,若全部数组为null则返回一个包含0个元素的空数组对象
     *
     * @param mores
     * @return
     */
    public static long[] concat(long[]... mores) {
        if (mores != null) {
            int len = 0;
            for (long[] arr : mores) {
                if (arr != null) {
                    len += arr.length;
                }
            }
            if (len > 0) {
                int start = 0, cpLen = 0;
                long[] result = new long[len];
                for (long[] arr : mores) {
                    if (arr != null) {
                        cpLen = arr.length;
                        System.arraycopy(arr, 0, result, start, cpLen);
                        start += cpLen;
                    }
                }
                return result;
            }
        }
        return Typer.longs();
    }


    /**
     * 将多个数组合并为一个新的数组输出,若全部数组为null则返回一个包含0个元素的空数组对象
     *
     * @param mores
     * @return
     */
    public static int[] concat(int[]... mores) {
        if (mores != null) {
            int len = 0;
            for (int[] arr : mores) {
                if (arr != null) {
                    len += arr.length;
                }
            }
            if (len > 0) {
                int start = 0, cpLen = 0;
                int[] result = new int[len];
                for (int[] arr : mores) {
                    if (arr != null) {
                        cpLen = arr.length;
                        System.arraycopy(arr, 0, result, start, cpLen);
                        start += cpLen;
                    }
                }
                return result;
            }
        }
        return Typer.ints();
    }


    /**
     * 将多个数组合并为一个新的数组输出,若全部数组为null则返回一个包含0个元素的空数组对象
     *
     * @param mores
     * @return
     */
    public static short[] concat(short[]... mores) {
        if (mores != null) {
            int len = 0;
            for (short[] arr : mores) {
                if (arr != null) {
                    len += arr.length;
                }
            }
            if (len > 0) {
                int start = 0, cpLen = 0;
                short[] result = new short[len];
                for (short[] arr : mores) {
                    if (arr != null) {
                        cpLen = arr.length;
                        System.arraycopy(arr, 0, result, start, cpLen);
                        start += cpLen;
                    }
                }
                return result;
            }
        }
        return Typer.shorts();
    }


    /**
     * 将多个数组合并为一个新的数组输出,若全部数组为null则返回一个包含0个元素的空数组对象
     *
     * @param mores
     * @return
     */
    public static byte[] concat(byte[]... mores) {
        if (mores != null) {
            int len = 0;
            for (byte[] arr : mores) {
                if (arr != null) {
                    len += arr.length;
                }
            }
            if (len > 0) {
                int start = 0, cpLen = 0;
                byte[] result = new byte[len];
                for (byte[] arr : mores) {
                    if (arr != null) {
                        cpLen = arr.length;
                        System.arraycopy(arr, 0, result, start, cpLen);
                        start += cpLen;
                    }
                }
                return result;
            }
        }
        return Typer.bytes();
    }


    /**
     * 将多个数组合并为一个新的数组输出,若全部数组为null则返回一个包含0个元素的空数组对象
     *
     * @param mores
     * @return
     */
    public static double[] concat(double[]... mores) {
        if (mores != null) {
            int len = 0;
            for (double[] arr : mores) {
                if (arr != null) {
                    len += arr.length;
                }
            }
            if (len > 0) {
                int start = 0, cpLen = 0;
                double[] result = new double[len];
                for (double[] arr : mores) {
                    if (arr != null) {
                        cpLen = arr.length;
                        System.arraycopy(arr, 0, result, start, cpLen);
                        start += cpLen;
                    }
                }
                return result;
            }
        }
        return Typer.doubles();
    }


    /**
     * 将多个数组合并为一个新的数组输出,若全部数组为null则返回一个包含0个元素的空数组对象
     *
     * @param mores
     * @return
     */
    public static float[] concat(float[]... mores) {
        if (mores != null) {
            int len = 0;
            for (float[] arr : mores) {
                if (arr != null) {
                    len += arr.length;
                }
            }
            if (len > 0) {
                int start = 0, cpLen = 0;
                float[] result = new float[len];
                for (float[] arr : mores) {
                    if (arr != null) {
                        cpLen = arr.length;
                        System.arraycopy(arr, 0, result, start, cpLen);
                        start += cpLen;
                    }
                }
                return result;
            }
        }
        return Typer.floats();
    }


    /**
     * 将多个数组合并为一个新的数组输出,若全部数组为null则返回一个包含0个元素的空数组对象
     *
     * @param mores
     * @return
     */
    public static char[] concat(char[]... mores) {
        if (mores != null) {
            int len = 0;
            for (char[] arr : mores) {
                if (arr != null) {
                    len += arr.length;
                }
            }
            if (len > 0) {
                int start = 0, cpLen = 0;
                char[] result = new char[len];
                for (char[] arr : mores) {
                    if (arr != null) {
                        cpLen = arr.length;
                        System.arraycopy(arr, 0, result, start, cpLen);
                        start += cpLen;
                    }
                }
                return result;
            }
        }
        return Typer.chars();
    }


    /**
     * 将多个数组合并为一个新的数组输出,若全部数组为null则返回一个包含0个元素的空数组对象
     *
     * @param mores
     * @return
     */
    public static boolean[] concat(boolean[]... mores) {
        if (mores != null) {
            int len = 0;
            for (boolean[] arr : mores) {
                if (arr != null) {
                    len += arr.length;
                }
            }
            if (len > 0) {
                int start = 0, cpLen = 0;
                boolean[] result = new boolean[len];
                for (boolean[] arr : mores) {
                    if (arr != null) {
                        cpLen = arr.length;
                        System.arraycopy(arr, 0, result, start, cpLen);
                        start += cpLen;
                    }
                }
                return result;
            }
        }
        return Typer.booleans();
    }

    /**
     * 判断对象是否是一个数组对象。
     *
     * @param obj
     * @return 是:true,否:false
     */
    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }


    /**
     * 将array里的元素'反转'或'拷贝到新的长度与array相同的数组中,并将新数组里的元素反转'
     *
     * @param array 需要被拷贝或反转的数组,取决于isNew的值
     * @param isNew 是否反转拷贝到新数组,true:拷贝到新数组并将新数组元素反转;false:反转原来的数组元素
     * @return isNew为true时返回新数组, 为false时返回原来的数组, 若array为null, isNew为true时, 那么将返回一个空数组(0个元素).
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] reverse(T[] array, boolean isNew) {
        if (array != null) {
            int len = array.length;
            T[] target = null;
            if (isNew) {
                target = ((Object) array.getClass() == (Object) Object[].class) ? (T[]) new Object[len] : (T[]) Array.newInstance(array.getClass().getComponentType(), len);
            } else {
                target = array;
            }
            if (len % 2 != 0) target[len / 2] = array[len / 2];
            for (int start = 0, end = array.length - 1; start < end; start++, end--) {
                T temp = array[end];
                target[end] = array[start];
                target[start] = temp;
            }
            return target;
        }
        return null;
    }


    /**
     * 将array里的元素'反转'或'拷贝到新的长度与array相同的数组中,并将新数组里的元素反转'
     *
     * @param array 需要被拷贝或反转的数组,取决于isNew的值
     * @param isNew 是否反转拷贝到新数组,true:拷贝到新数组并将新数组元素反转;false:反转原来的数组元素
     * @return isNew为true时返回新数组, 为false时返回原来的数组, 若array为null, isNew为true时, 那么将返回一个空数组(0个元素).
     */
    public static long[] reverse(long[] array, boolean isNew) {
        if (array != null) {
            int len = array.length;
            long[] target = isNew ? new long[len] : array;
            if (len % 2 != 0) target[len / 2] = array[len / 2];
            for (int start = 0, end = array.length - 1; start < end; start++, end--) {
                long temp = array[end];
                target[end] = array[start];
                target[start] = temp;
            }
            return target;
        }
        return isNew ? Typer.longs() : array;
    }


    /**
     * 将array里的元素'反转'或'拷贝到新的长度与array相同的数组中,并将新数组里的元素反转'
     *
     * @param array 需要被拷贝或反转的数组,取决于isNew的值
     * @param isNew 是否反转拷贝到新数组,true:拷贝到新数组并将新数组元素反转;false:反转原来的数组元素
     * @return isNew为true时返回新数组, 为false时返回原来的数组, 若array为null, isNew为true时, 那么将返回一个空数组(0个元素).
     */
    public static int[] reverse(int[] array, boolean isNew) {
        if (array != null) {
            int len = array.length;
            int[] target = isNew ? new int[len] : array;
            if (len % 2 != 0) target[len / 2] = array[len / 2];
            for (int start = 0, end = array.length - 1; start < end; start++, end--) {
                int temp = array[end];
                target[end] = array[start];
                target[start] = temp;
            }
            return target;
        }
        return isNew ? Typer.ints() : array;
    }


    /**
     * 将array里的元素'反转'或'拷贝到新的长度与array相同的数组中,并将新数组里的元素反转'
     *
     * @param array 需要被拷贝或反转的数组,取决于isNew的值
     * @param isNew 是否反转拷贝到新数组,true:拷贝到新数组并将新数组元素反转;false:反转原来的数组元素
     * @return isNew为true时返回新数组, 为false时返回原来的数组, 若array为null, isNew为true时, 那么将返回一个空数组(0个元素).
     */
    public static short[] reverse(short[] array, boolean isNew) {
        if (array != null) {
            int len = array.length;
            short[] target = isNew ? new short[len] : array;
            if (len % 2 != 0) target[len / 2] = array[len / 2];
            for (int start = 0, end = array.length - 1; start < end; start++, end--) {
                short temp = array[end];
                target[end] = array[start];
                target[start] = temp;
            }
            return target;
        }
        return isNew ? Typer.shorts() : array;
    }


    /**
     * 将array里的元素'反转'或'拷贝到新的长度与array相同的数组中,并将新数组里的元素反转'
     *
     * @param array 需要被拷贝或反转的数组,取决于isNew的值
     * @param isNew 是否反转拷贝到新数组,true:拷贝到新数组并将新数组元素反转;false:反转原来的数组元素
     * @return isNew为true时返回新数组, 为false时返回原来的数组, 若array为null, isNew为true时, 那么将返回一个空数组(0个元素).
     */
    public static byte[] reverse(byte[] array, boolean isNew) {
        if (array != null) {
            int len = array.length;
            byte[] target = isNew ? new byte[len] : array;
            if (len % 2 != 0) target[len / 2] = array[len / 2];
            for (int start = 0, end = array.length - 1; start < end; start++, end--) {
                byte temp = array[end];
                target[end] = array[start];
                target[start] = temp;
            }
            return target;
        }
        return isNew ? Typer.bytes() : array;
    }


    /**
     * 将array里的元素'反转'或'拷贝到新的长度与array相同的数组中,并将新数组里的元素反转'
     *
     * @param array 需要被拷贝或反转的数组,取决于isNew的值
     * @param isNew 是否反转拷贝到新数组,true:拷贝到新数组并将新数组元素反转;false:反转原来的数组元素
     * @return isNew为true时返回新数组, 为false时返回原来的数组, 若array为null, isNew为true时, 那么将返回一个空数组(0个元素).
     */
    public static double[] reverse(double[] array, boolean isNew) {
        if (array != null) {
            int len = array.length;
            double[] target = isNew ? new double[len] : array;
            if (len % 2 != 0) target[len / 2] = array[len / 2];
            for (int start = 0, end = array.length - 1; start < end; start++, end--) {
                double temp = array[end];
                target[end] = array[start];
                target[start] = temp;
            }
            return target;
        }
        return isNew ? Typer.doubles() : array;
    }


    /**
     * 将array里的元素'反转'或'拷贝到新的长度与array相同的数组中,并将新数组里的元素反转'
     *
     * @param array 需要被拷贝或反转的数组,取决于isNew的值
     * @param isNew 是否反转拷贝到新数组,true:拷贝到新数组并将新数组元素反转;false:反转原来的数组元素
     * @return isNew为true时返回新数组, 为false时返回原来的数组, 若array为null, isNew为true时, 那么将返回一个空数组(0个元素).
     */
    public static float[] reverse(float[] array, boolean isNew) {
        if (array != null) {
            int len = array.length;
            float[] target = isNew ? new float[len] : array;
            if (len % 2 != 0) target[len / 2] = array[len / 2];
            for (int start = 0, end = array.length - 1; start < end; start++, end--) {
                float temp = array[end];
                target[end] = array[start];
                target[start] = temp;
            }
            return target;
        }
        return isNew ? Typer.floats() : array;
    }


    /**
     * 将array里的元素'反转'或'拷贝到新的长度与array相同的数组中,并将新数组里的元素反转'
     *
     * @param array 需要被拷贝或反转的数组,取决于isNew的值
     * @param isNew 是否反转拷贝到新数组,true:拷贝到新数组并将新数组元素反转;false:反转原来的数组元素
     * @return isNew为true时返回新数组, 为false时返回原来的数组, 若array为null, isNew为true时, 那么将返回一个空数组(0个元素).
     */
    public static char[] reverse(char[] array, boolean isNew) {
        if (array != null) {
            int len = array.length;
            char[] target = isNew ? new char[len] : array;
            if (len % 2 != 0) target[len / 2] = array[len / 2];
            for (int start = 0, end = array.length - 1; start < end; start++, end--) {
                char temp = array[end];
                target[end] = array[start];
                target[start] = temp;
            }
            return target;
        }
        return isNew ? Typer.chars() : array;
    }


    /**
     * 将array里的元素'反转'或'拷贝到新的长度与array相同的数组中,并将新数组里的元素反转'
     *
     * @param array 需要被拷贝或反转的数组,取决于isNew的值
     * @param isNew 是否反转拷贝到新数组,true:拷贝到新数组并将新数组元素反转;false:反转原来的数组元素
     * @return isNew为true时返回新数组, 为false时返回原来的数组, 若array为null, isNew为true时, 那么将返回一个空数组(0个元素).
     */
    public static boolean[] reverse(boolean[] array, boolean isNew) {
        if (array != null) {
            int len = array.length;
            boolean[] target = isNew ? new boolean[len] : array;
            if (len % 2 != 0) target[len / 2] = array[len / 2];
            for (int start = 0, end = array.length - 1; start < end; start++, end--) {
                boolean temp = array[end];
                target[end] = array[start];
                target[start] = temp;
            }
            return target;
        }
        return isNew ? Typer.booleans() : array;
    }


    /**
     * ele在数组array中首次出现的索引,未出现则返回-1
     *
     * @param array
     * @param ele
     * @return
     */
    public static <T> int index(T[] array, T ele) {
        if (array != null) {
            for (int i = 0, len = array.length; i < len; i++) {
                if (Objects.equals(array[i], ele)) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * ele在数组array中首次出现的索引,未出现则返回-1
     *
     * @param array
     * @param ele
     * @return
     */
    public static int index(long[] array, long ele) {
        if (array != null) {
            for (int i = 0, len = array.length; i < len; i++) {
                if (array[i] == ele) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * ele在数组array中首次出现的索引,未出现则返回-1
     *
     * @param array
     * @param ele
     * @return
     */
    public static int index(int[] array, int ele) {
        if (array != null) {
            for (int i = 0, len = array.length; i < len; i++) {
                if (array[i] == ele) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * ele在数组array中首次出现的索引,未出现则返回-1
     *
     * @param array
     * @param ele
     * @return
     */
    public static int index(short[] array, short ele) {
        if (array != null) {
            for (int i = 0, len = array.length; i < len; i++) {
                if (array[i] == ele) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * ele在数组array中首次出现的索引,未出现则返回-1
     *
     * @param array
     * @param ele
     * @return
     */
    public static int index(byte[] array, byte ele) {
        if (array != null) {
            for (int i = 0, len = array.length; i < len; i++) {
                if (array[i] == ele) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * ele在数组array中首次出现的索引,未出现则返回-1
     *
     * @param array
     * @param ele
     * @return
     */
    public static int index(double[] array, double ele) {
        if (array != null) {
            for (int i = 0, len = array.length; i < len; i++) {
                if (array[i] == ele) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * ele在数组array中首次出现的索引,未出现则返回-1
     *
     * @param array
     * @param ele
     * @return
     */
    public static int index(float[] array, float ele) {
        if (array != null) {
            for (int i = 0, len = array.length; i < len; i++) {
                if (array[i] == ele) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * ele在数组array中首次出现的索引,未出现则返回-1
     *
     * @param array
     * @param ele
     * @return
     */
    public static int index(char[] array, char ele) {
        if (array != null) {
            for (int i = 0, len = array.length; i < len; i++) {
                if (array[i] == ele) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * ele在数组array中首次出现的索引,未出现则返回-1
     *
     * @param array
     * @param ele
     * @return
     */
    public static int index(boolean[] array, boolean ele) {
        if (array != null) {
            for (int i = 0, len = array.length; i < len; i++) {
                if (array[i] == ele) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * 获取数组的长度,若数组为空返回0
     *
     * @param array
     * @return
     */
    public static <T> int len(T[] array) {
        return array == null ? 0 : array.length;
    }


    /**
     * 获取数组的长度,若数组为空返回0
     *
     * @param array
     * @return
     */
    public static int len(long[] array) {
        return array == null ? 0 : array.length;
    }


    /**
     * 获取数组的长度,若数组为空返回0
     *
     * @param array
     * @return
     */
    public static int len(int[] array) {
        return array == null ? 0 : array.length;
    }


    /**
     * 获取数组的长度,若数组为空返回0
     *
     * @param array
     * @return
     */
    public static int len(short[] array) {
        return array == null ? 0 : array.length;
    }


    /**
     * 获取数组的长度,若数组为空返回0
     *
     * @param array
     * @return
     */
    public static int len(byte[] array) {
        return array == null ? 0 : array.length;
    }


    /**
     * 获取数组的长度,若数组为空返回0
     *
     * @param array
     * @return
     */
    public static int len(double[] array) {
        return array == null ? 0 : array.length;
    }


    /**
     * 获取数组的长度,若数组为空返回0
     *
     * @param array
     * @return
     */
    public static int len(float[] array) {
        return array == null ? 0 : array.length;
    }


    /**
     * 获取数组的长度,若数组为空返回0
     *
     * @param array
     * @return
     */
    public static int len(char[] array) {
        return array == null ? 0 : array.length;
    }


    /**
     * 获取数组的长度,若数组为空返回0
     *
     * @param array
     * @return
     */
    public static int len(boolean[] array) {
        return array == null ? 0 : array.length;
    }


    /**
     * 将数组array中的元素'尽量'平均分成amount份,若array长度刚好能被amount整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的每个一维数组长度将被'尽量'设为相等.<br/>
     * 如输入:array={"1","2","3","4","5"},amount=3,返回:{{"1","2"},{"3","4"},{"5"}}
     *
     * @param array  若此参数为null则返回null
     * @param amount 此参数若小于0则返回0个元素的二维数组(当array不为null时)
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[][] splitAvg(T[] array, int amount) {
        if (array != null && amount >= 0) {
            int[] quotas = avgs(array.length, amount);//计算每个数组的长度
            return split(array, quotas);
        }
        return array == null ? null : (T[][]) Array.newInstance(array.getClass(), 0);
    }


    /**
     * 将数组array中的元素'尽量'平均分成amount份,若array长度刚好能被amount整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的每个一维数组长度将被'尽量'设为相等.<br/>
     * 如输入:array={1,2,3,4,5},amount=3,返回:{{1,2},{3,4},{5}}
     *
     * @param array  被分割的数组
     * @param amount 均分的数量
     * @return 返回长度为amount的二维数组
     */
    public static long[][] splitAvg(long[] array, int amount) {
        if (array != null && amount >= 0) {
            int[] quotas = avgs(array.length, amount);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.longss(amount);
    }


    /**
     * 将数组array中的元素'尽量'平均分成amount份,若array长度刚好能被amount整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的每个一维数组长度将被'尽量'设为相等.<br/>
     * 如输入:array={1,2,3,4,5},amount=3,返回:{{1,2},{3,4},{5}}
     *
     * @param array  被分割的数组
     * @param amount 均分的数量
     * @return 返回长度为amount的二维数组
     */
    public static int[][] splitAvg(int[] array, int amount) {
        if (array != null && amount >= 0) {
            int[] quotas = avgs(array.length, amount);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.intss(amount);
    }


    /**
     * 将数组array中的元素'尽量'平均分成amount份,若array长度刚好能被amount整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的每个一维数组长度将被'尽量'设为相等.<br/>
     * 如输入:array={1,2,3,4,5},amount=3,返回:{{1,2},{3,4},{5}}
     *
     * @param array  被分割的数组
     * @param amount 均分的数量
     * @return 返回长度为amount的二维数组
     */
    public static short[][] splitAvg(short[] array, int amount) {
        if (array != null && amount >= 0) {
            int[] quotas = avgs(array.length, amount);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.shortss(amount);
    }


    /**
     * 将数组array中的元素'尽量'平均分成amount份,若array长度刚好能被amount整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的每个一维数组长度将被'尽量'设为相等.<br/>
     * 如输入:array={1,2,3,4,5},amount=3,返回:{{1,2},{3,4},{5}}
     *
     * @param array  被分割的数组
     * @param amount 均分的数量
     * @return 返回长度为amount的二维数组
     */
    public static byte[][] splitAvg(byte[] array, int amount) {
        if (array != null && amount >= 0) {
            int[] quotas = avgs(array.length, amount);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.bytess(amount);
    }


    /**
     * 将数组array中的元素'尽量'平均分成amount份,若array长度刚好能被amount整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的每个一维数组长度将被'尽量'设为相等.<br/>
     * 如输入:array={1.0,2.0,3.0,4.0,5.0},amount=3,返回:{{1.0,2.0},{3.0,4.0},{5.0}}
     *
     * @param array  被分割的数组
     * @param amount 均分的数量
     * @return 返回长度为amount的二维数组
     */
    public static double[][] splitAvg(double[] array, int amount) {
        if (array != null && amount >= 0) {
            int[] quotas = avgs(array.length, amount);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.doubless(amount);
    }


    /**
     * 将数组array中的元素'尽量'平均分成amount份,若array长度刚好能被amount整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的每个一维数组长度将被'尽量'设为相等.<br/>
     * 如输入:array={1.0f,2.0f,3.0f,4.0f,5.0f},amount=3,返回:{{1.0f,2.0f},{3.0f,4.0f},{5.0f}}
     *
     * @param array  被分割的数组
     * @param amount 均分的数量
     * @return 返回长度为amount的二维数组
     */
    public static float[][] splitAvg(float[] array, int amount) {
        if (array != null && amount >= 0) {
            int[] quotas = avgs(array.length, amount);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.floatss(amount);
    }


    /**
     * 将数组array中的元素'尽量'平均分成amount份,若array长度刚好能被amount整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的每个一维数组长度将被'尽量'设为相等.<br/>
     * 如输入:array={'1','2','3','4','5'},amount=3,返回:{{'1','2'},{'3','4'},{'5'}}
     *
     * @param array  被分割的数组
     * @param amount 均分的数量
     * @return 返回长度为amount的二维数组
     */
    public static char[][] splitAvg(char[] array, int amount) {
        if (array != null && amount >= 0) {
            int[] quotas = avgs(array.length, amount);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.charss(amount);
    }


    /**
     * 将数组array中的元素'尽量'平均分成amount份,若array长度刚好能被amount整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的每个一维数组长度将被'尽量'设为相等.<br/>
     * 如输入:array={true,false,true,true,false},amount=3,返回:{{true,false},{true,true},{false}}
     *
     * @param array  被分割的数组
     * @param amount 均分的数量
     * @return 返回长度为amount的二维数组
     */
    public static boolean[][] splitAvg(boolean[] array, int amount) {
        if (array != null && amount >= 0) {
            int[] quotas = avgs(array.length, amount);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.booleanss(amount);
    }


    /**
     * 将数组array中的元素按每份every个元素分成一个二维数组,二维数组中每个元素的长度之和等于array.length.若array长度刚好能被every整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中最后一个元素的长度将等于array.length%every.<br/>
     * 如输入:array={"1","2","3","4","5"},every=3,返回:{{"1","2","3"},{"4","5"}}
     *
     * @param array 若此参数为null则返回null
     * @param every 此参数若小于0则返回0个元素的二维数组(当array不为null时)
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[][] splitEverys(T[] array, int every) {
        if (array != null && every >= 0) {
            int[] quotas = everys(array.length, every);//计算每个数组的长度
            return split(array, quotas);
        }
        return array == null ? null : (T[][]) Array.newInstance(array.getClass(), 0);
    }


    /**
     * 将数组array中的元素按每份every个元素分成一个二维数组,二维数组中每个元素的长度之和等于array.length.若array长度刚好能被every整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中最后一个元素的长度将等于array.length%every.<br/>
     * 如输入:array={1,2,3,4,5},every=3,返回:{{1,2,3},{4,5}}
     *
     * @param array 被分割的数组
     * @param every 均分的数量
     * @return 返回长度为every的二维数组
     */
    public static long[][] splitEverys(long[] array, int every) {
        if (array != null && every >= 0) {
            int[] quotas = everys(array.length, every);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.longss(every);
    }


    /**
     * 将数组array中的元素按每份every个元素分成一个二维数组,二维数组中每个元素的长度之和等于array.length.若array长度刚好能被every整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中最后一个元素的长度将等于array.length%every.<br/>
     * 如输入:array={1,2,3,4,5},every=3,返回:{{1,2,3},{4,5}}
     *
     * @param array 被分割的数组
     * @param every 均分的数量
     * @return 返回长度为every的二维数组
     */
    public static int[][] splitEverys(int[] array, int every) {
        if (array != null && every >= 0) {
            int[] quotas = everys(array.length, every);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.intss(every);
    }


    /**
     * 将数组array中的元素按每份every个元素分成一个二维数组,二维数组中每个元素的长度之和等于array.length.若array长度刚好能被every整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中最后一个元素的长度将等于array.length%every.<br/>
     * 如输入:array={1,2,3,4,5},every=3,返回:{{1,2,3},{4,5}}
     *
     * @param array 被分割的数组
     * @param every 均分的数量
     * @return 返回长度为every的二维数组
     */
    public static short[][] splitEverys(short[] array, int every) {
        if (array != null && every >= 0) {
            int[] quotas = everys(array.length, every);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.shortss(every);
    }


    /**
     * 将数组array中的元素按每份every个元素分成一个二维数组,二维数组中每个元素的长度之和等于array.length.若array长度刚好能被every整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中最后一个元素的长度将等于array.length%every.<br/>
     * 如输入:array={1,2,3,4,5},every=3,返回:{{1,2,3},{4,5}}
     *
     * @param array 被分割的数组
     * @param every 均分的数量
     * @return 返回长度为every的二维数组
     */
    public static byte[][] splitEverys(byte[] array, int every) {
        if (array != null && every >= 0) {
            int[] quotas = everys(array.length, every);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.bytess(every);
    }


    /**
     * 将数组array中的元素按每份every个元素分成一个二维数组,二维数组中每个元素的长度之和等于array.length.若array长度刚好能被every整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中最后一个元素的长度将等于array.length%every.<br/>
     * 如输入:array={1.0,2.0,3.0,4.0,5.0},every=3,返回:{{1.0,2.0,3.0},{4.0,5.0}}
     *
     * @param array 被分割的数组
     * @param every 均分的数量
     * @return 返回长度为every的二维数组
     */
    public static double[][] splitEverys(double[] array, int every) {
        if (array != null && every >= 0) {
            int[] quotas = everys(array.length, every);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.doubless(every);
    }


    /**
     * 将数组array中的元素按每份every个元素分成一个二维数组,二维数组中每个元素的长度之和等于array.length.若array长度刚好能被every整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中最后一个元素的长度将等于array.length%every.<br/>
     * 如输入:array={1.0f,2.0f,3.0f,4.0f,5.0f},every=3,返回:{{1.0f,2.0f,3.0f},{4.0f,5.0f}}
     *
     * @param array 被分割的数组
     * @param every 均分的数量
     * @return 返回长度为every的二维数组
     */
    public static float[][] splitEverys(float[] array, int every) {
        if (array != null && every >= 0) {
            int[] quotas = everys(array.length, every);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.floatss(every);
    }


    /**
     * 将数组array中的元素按每份every个元素分成一个二维数组,二维数组中每个元素的长度之和等于array.length.若array长度刚好能被every整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中最后一个元素的长度将等于array.length%every.<br/>
     * 如输入:array={'1','2','3','4','5'},every=3,返回:{{'1','2','3'},{'4','5'}}
     *
     * @param array 被分割的数组
     * @param every 均分的数量
     * @return 返回长度为every的二维数组
     */
    public static char[][] splitEverys(char[] array, int every) {
        if (array != null && every >= 0) {
            int[] quotas = everys(array.length, every);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.charss(every);
    }


    /**
     * 将数组array中的元素按每份every个元素分成一个二维数组,二维数组中每个元素的长度之和等于array.length.若array长度刚好能被every整除,则返回的二维数组中每个一维数组的长度都是相等的,否则二维数组中的最后一个元素的长度将等于array.length%every.<br/>
     * 如输入:array={true,false,true,true,false},every=3,返回:{{true,false,true},{true,false}}
     *
     * @param array 被分割的数组
     * @param every 均分的数量
     * @return 返回长度为every的二维数组
     */
    public static boolean[][] splitEverys(boolean[] array, int every) {
        if (array != null && every >= 0) {
            int[] quotas = everys(array.length, every);//计算每个数组的长度
            return split(array, quotas);
        }
        return Typer.booleanss(every);
    }


    /**
     * 将数组array中的元素按指标quotas来分割，返回包含每个分割后的一维数组的二维数组,分割将尽量按quotas中每个元素指定的长度分到每个一维数组中,若quotas中的元素和大于array的长度,那么分不到元素的数组将不包含元素.<br/>
     * 如输入:array={"1","2","3","4","5"},quotas={1,2,3,4},返回:{{"1"},{"2","3"},{"4","5"},{}}
     *
     * @param array  被分割的数组
     * @param quotas 分割指标,其中每个元素对应array分割后的每个一维数组的长度,此指标并不保证返回值中每个一维数组的长度.
     * @return 返回长度等于quotas的长度的二维数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[][] split(T[] array, int... quotas) {
        if (Typer.notNull(array, quotas)) {
            return split(array, array.length, quotas);
        }
        return array == null ? null : (T[][]) Array.newInstance(array.getClass(), 0);
    }


    /**
     * 将数组array中的元素按指标quotas来分割，返回包含每个分割后的一维数组的二维数组,分割将尽量按quotas中每个元素指定的长度分到每个一维数组中,若quotas中的元素和大于array的长度,那么分不到元素的数组将不包含元素.<br/>
     * 如输入:array={1,2,3,4,5},quotas={1,2,3,4},返回:{{1},{2,3},{4,5},{}}
     *
     * @param array  被分割的数组
     * @param quotas 分割指标,其中每个元素对应array分割后的每个一维数组的长度,此指标并不保证返回值中每个一维数组的长度.
     * @return 返回长度等于quotas的长度的二维数组
     */
    public static long[][] split(long[] array, int... quotas) {
        if (Typer.notNull(array, quotas)) {
            return split(array, array.length, quotas);
        }
        return Typer.longss(len(quotas));
    }


    /**
     * 将数组array中的元素按指标quotas来分割，返回包含每个分割后的一维数组的二维数组,分割将尽量按quotas中每个元素指定的长度分到每个一维数组中,若quotas中的元素和大于array的长度,那么分不到元素的数组将不包含元素.<br/>
     * 如输入:array={1,2,3,4,5},quotas={1,2,3,4},返回:{{1},{2,3},{4,5},{}}
     *
     * @param array  被分割的数组
     * @param quotas 分割指标,其中每个元素对应array分割后的每个一维数组的长度,此指标并不保证返回值中每个一维数组的长度.
     * @return 返回长度等于quotas的长度的二维数组
     */
    public static int[][] split(int[] array, int... quotas) {
        if (Typer.notNull(array, quotas)) {
            return split(array, array.length, quotas);
        }
        return Typer.intss(len(quotas));
    }


    /**
     * 将数组array中的元素按指标quotas来分割，返回包含每个分割后的一维数组的二维数组,分割将尽量按quotas中每个元素指定的长度分到每个一维数组中,若quotas中的元素和大于array的长度,那么分不到元素的数组将不包含元素.<br/>
     * 如输入:array={1,2,3,4,5},quotas={1,2,3,4},返回:{{1},{2,3},{4,5},{}}
     *
     * @param array  被分割的数组
     * @param quotas 分割指标,其中每个元素对应array分割后的每个一维数组的长度,此指标并不保证返回值中每个一维数组的长度.
     * @return 返回长度等于quotas的长度的二维数组
     */
    public static short[][] split(short[] array, int... quotas) {
        if (Typer.notNull(array, quotas)) {
            return split(array, array.length, quotas);
        }
        return Typer.shortss(len(quotas));
    }


    /**
     * 将数组array中的元素按指标quotas来分割，返回包含每个分割后的一维数组的二维数组,分割将尽量按quotas中每个元素指定的长度分到每个一维数组中,若quotas中的元素和大于array的长度,那么分不到元素的数组将不包含元素.<br/>
     * 如输入:array={1,2,3,4,5},quotas={1,2,3,4},返回:{{1},{2,3},{4,5},{}}
     *
     * @param array  被分割的数组
     * @param quotas 分割指标,其中每个元素对应array分割后的每个一维数组的长度,此指标并不保证返回值中每个一维数组的长度.
     * @return 返回长度等于quotas的长度的二维数组
     */
    public static byte[][] split(byte[] array, int... quotas) {
        if (Typer.notNull(array, quotas)) {
            return split(array, array.length, quotas);
        }
        return Typer.bytess(len(quotas));
    }


    /**
     * 将数组array中的元素按指标quotas来分割，返回包含每个分割后的一维数组的二维数组,分割将尽量按quotas中每个元素指定的长度分到每个一维数组中,若quotas中的元素和大于array的长度,那么分不到元素的数组将不包含元素.<br/>
     * 如输入:array={1.0,2.0,3.0,4.0,5.0},quotas={1,2,3,4},返回:{{1.0},{2.0,3.0},{4.0,5.0},{}}
     *
     * @param array  被分割的数组
     * @param quotas 分割指标,其中每个元素对应array分割后的每个一维数组的长度,此指标并不保证返回值中每个一维数组的长度.
     * @return 返回长度等于quotas的长度的二维数组
     */
    public static double[][] split(double[] array, int... quotas) {
        if (Typer.notNull(array, quotas)) {
            return split(array, array.length, quotas);
        }
        return Typer.doubless(len(quotas));
    }


    /**
     * 将数组array中的元素按指标quotas来分割，返回包含每个分割后的一维数组的二维数组,分割将尽量按quotas中每个元素指定的长度分到每个一维数组中,若quotas中的元素和大于array的长度,那么分不到元素的数组将不包含元素.<br/>
     * 如输入:array={1.0f,2.0f,3.0f,4.0f,5.0f},quotas={1,2,3,4},返回:{{1.0f},{2.0f,3.0f},{4.0f,5.0f},{}}
     *
     * @param array  被分割的数组
     * @param quotas 分割指标,其中每个元素对应array分割后的每个一维数组的长度,此指标并不保证返回值中每个一维数组的长度.
     * @return 返回长度等于quotas的长度的二维数组
     */
    public static float[][] split(float[] array, int... quotas) {
        if (Typer.notNull(array, quotas)) {
            return split(array, array.length, quotas);
        }
        return Typer.floatss(len(quotas));
    }


    /**
     * 将数组array中的元素按指标quotas来分割，返回包含每个分割后的一维数组的二维数组,分割将尽量按quotas中每个元素指定的长度分到每个一维数组中,若quotas中的元素和大于array的长度,那么分不到元素的数组将不包含元素.<br/>
     * 如输入:array={'1','2','3','4','5'},quotas={1,2,3,4},返回:{{'1'},{'2','3'},{'4','5'},{}}
     *
     * @param array  被分割的数组
     * @param quotas 分割指标,其中每个元素对应array分割后的每个一维数组的长度,此指标并不保证返回值中每个一维数组的长度.
     * @return 返回长度等于quotas的长度的二维数组
     */
    public static char[][] split(char[] array, int... quotas) {
        if (Typer.notNull(array, quotas)) {
            return split(array, array.length, quotas);
        }
        return Typer.charss(len(quotas));
    }


    /**
     * 将数组array中的元素按指标quotas来分割，返回包含每个分割后的一维数组的二维数组,分割将尽量按quotas中每个元素指定的长度分到每个一维数组中,若quotas中的元素和大于array的长度,那么分不到元素的数组将不包含元素.<br/>
     * 如输入:array={true,false,true,true,false},quotas={1,2,3,4},返回:{{true},{false,true},{true,false},{}}
     *
     * @param array  被分割的数组
     * @param quotas 分割指标,其中每个元素对应array分割后的每个一维数组的长度,此指标并不保证返回值中每个一维数组的长度.
     * @return 返回长度等于quotas的长度的二维数组
     */
    public static boolean[][] split(boolean[] array, int... quotas) {
        if (Typer.notNull(array, quotas)) {
            return split(array, array.length, quotas);
        }
        return Typer.booleanss(len(quotas));
    }


    @SuppressWarnings("unchecked")
    private static <T> T[] split(T array, int total, int... quotas) {

        int amount = quotas.length, len = 0;

        T[] result = (T[]) Array.newInstance(array.getClass(), amount);//构造二维数组
        Class<?> ct = array.getClass().getComponentType();

        for (int i = 0, start = 0; i < amount; start += quotas[i], i++) {
            len = Math.max(Math.min(total - start, quotas[i]), 0);//计算数组实际可行的长度(array中还有那么多未被分掉的元素)
            T ones = (T) Array.newInstance(ct, len);
            result[i] = ones;
            if (len > 0) {
                System.arraycopy(array, start, ones, 0, len);
            }
        }
        return result;
    }


    /**
     * 为数组中每个元素添加字符串前缀prefix,不会改变原始数组.
     *
     * @param arr    不能为null
     * @param prefix 不能为null
     * @return 返回新数组
     */
    public static String[] prefix(String[] arr, String prefix) {
        if (Typer.notNull(arr, prefix)) {
            return Stream.of(arr).parallel().map(s -> prefix + (s == null ? "" : s)).toArray(String[]::new);
        }
        return Typer.strings();
    }


    /**
     * 为数组中每个元素添加字符串后缀suffix,不会改变原始数组.
     *
     * @param arr    不能为null
     * @param suffix 不能为null
     * @return 返回新数组
     */
    public static String[] suffix(String[] arr, String suffix) {
        if (Typer.notNull(arr, suffix)) {
            return Stream.of(arr).parallel().map(s -> (s == null ? "" : s) + suffix).toArray(String[]::new);
        }
        return Typer.strings();
    }


    /**
     * 从src的start位置开始,获取sub在src中第一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub   子数组
     * @param src   父数组
     * @param start 从src中查找的起始位置
     * @return
     */
    public static <T> int index(T[] sub, T[] src, int start) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen + 1;
            int flag = subLen - 1;
            for (int i = Math.max(start, 0); i < len; i++) {
                for (int j = 0; j < subLen; j++) {
                    if (!Objects.equals(sub[j], src[i + j])) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src的start位置开始,获取sub在src中第一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub   子数组
     * @param src   父数组
     * @param start 从src中查找的起始位置
     * @return
     */
    public static int index(long[] sub, long[] src, int start) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen + 1;
            int flag = subLen - 1;
            for (int i = Math.max(start, 0); i < len; i++) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src的start位置开始,获取sub在src中第一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub   子数组
     * @param src   父数组
     * @param start 从src中查找的起始位置
     * @return
     */
    public static int index(int[] sub, int[] src, int start) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen + 1;
            int flag = subLen - 1;
            for (int i = Math.max(start, 0); i < len; i++) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src的start位置开始,获取sub在src中第一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub   子数组
     * @param src   父数组
     * @param start 从src中查找的起始位置
     * @return
     */
    public static int index(short[] sub, short[] src, int start) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen + 1;
            int flag = subLen - 1;
            for (int i = Math.max(start, 0); i < len; i++) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src的start位置开始,获取sub在src中第一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub   子数组
     * @param src   父数组
     * @param start 从src中查找的起始位置
     * @return
     */
    public static int index(byte[] sub, byte[] src, int start) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen + 1;
            int flag = subLen - 1;
            for (int i = Math.max(start, 0); i < len; i++) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src的start位置开始,获取sub在src中第一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub   子数组
     * @param src   父数组
     * @param start 从src中查找的起始位置
     * @return
     */
    public static int index(double[] sub, double[] src, int start) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen + 1;
            int flag = subLen - 1;
            for (int i = Math.max(start, 0); i < len; i++) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src的start位置开始,获取sub在src中第一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub   子数组
     * @param src   父数组
     * @param start 从src中查找的起始位置
     * @return
     */
    public static int index(float[] sub, float[] src, int start) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen + 1;
            int flag = subLen - 1;
            for (int i = Math.max(start, 0); i < len; i++) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src的start位置开始,获取sub在src中第一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub   子数组
     * @param src   父数组
     * @param start 从src中查找的起始位置
     * @return
     */
    public static int index(char[] sub, char[] src, int start) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen + 1;
            int flag = subLen - 1;
            for (int i = Math.max(start, 0); i < len; i++) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src的start位置开始,获取sub在src中第一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub   子数组
     * @param src   父数组
     * @param start 从src中查找的起始位置
     * @return
     */
    public static int index(boolean[] sub, boolean[] src, int start) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen + 1;
            int flag = subLen - 1;
            for (int i = Math.max(start, 0); i < len; i++) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src中查找(不包含末尾长度endOffset)sub在src中最后一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub       子数组
     * @param src       父数组
     * @param endOffset src数组末尾部分不参与查找的长度,如:sub={1,2},src={0,1,2,1,2},endOffset=2将返回1
     * @param end       从后往前找时,结束的位置,即:找到此位置时还未找到出现的索引,则不再继续往前找
     * @return
     */
    public static <T> int lastIndex(T[] sub, T[] src, int endOffset, int end) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen - endOffset;
            int flag = subLen - 1;
            for (int i = len; i >= 0 && i >= end; i--) {
                for (int j = 0; j < subLen; j++) {
                    if (!Objects.equals(sub[j], src[i + j])) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src中查找(不包含末尾长度endOffset)sub在src中最后一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub       子数组
     * @param src       父数组
     * @param endOffset src数组末尾部分不参与查找的长度,如:sub={1,2},src={0,1,2,1,2},endOffset=2将返回1
     * @param end       从后往前找时,结束的位置,即:找到此位置时还未找到出现的索引,则不再继续往前找
     * @return
     */
    public static int lastIndex(long[] sub, long[] src, int endOffset, int end) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen - endOffset;
            int flag = subLen - 1;
            for (int i = len; i >= 0 && i >= end; i--) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src中查找(不包含末尾长度endOffset)sub在src中最后一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub       子数组
     * @param src       父数组
     * @param endOffset src数组末尾部分不参与查找的长度,如:sub={1,2},src={0,1,2,1,2},endOffset=2将返回1
     * @param end       从后往前找时,结束的位置,即:找到此位置时还未找到出现的索引,则不再继续往前找
     * @return
     */
    public static int lastIndex(int[] sub, int[] src, int endOffset, int end) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen - endOffset;
            int flag = subLen - 1;
            for (int i = len; i >= 0 && i >= end; i--) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src中查找(不包含末尾长度endOffset)sub在src中最后一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub       子数组
     * @param src       父数组
     * @param endOffset src数组末尾部分不参与查找的长度,如:sub={1,2},src={0,1,2,1,2},endOffset=2将返回1
     * @param end       从后往前找时,结束的位置,即:找到此位置时还未找到出现的索引,则不再继续往前找
     * @return
     */
    public static int lastIndex(short[] sub, short[] src, int endOffset, int end) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen - endOffset;
            int flag = subLen - 1;
            for (int i = len; i >= 0 && i >= end; i--) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src中查找(不包含末尾长度endOffset)sub在src中最后一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub       子数组
     * @param src       父数组
     * @param endOffset src数组末尾部分不参与查找的长度,如:sub={1,2},src={0,1,2,1,2},endOffset=2将返回1
     * @param end       从后往前找时,结束的位置,即:找到此位置时还未找到出现的索引,则不再继续往前找
     * @return
     */
    public static int lastIndex(byte[] sub, byte[] src, int endOffset, int end) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen - endOffset;
            int flag = subLen - 1;
            for (int i = len; i >= 0 && i >= end; i--) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src中查找(不包含末尾长度endOffset)sub在src中最后一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub       子数组
     * @param src       父数组
     * @param endOffset src数组末尾部分不参与查找的长度,如:sub={1,2},src={0,1,2,1,2},endOffset=2将返回1
     * @param end       从后往前找时,结束的位置,即:找到此位置时还未找到出现的索引,则不再继续往前找
     * @return
     */
    public static int lastIndex(double[] sub, double[] src, int endOffset, int end) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen - endOffset;
            int flag = subLen - 1;
            for (int i = len; i >= 0 && i >= end; i--) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src中查找(不包含末尾长度endOffset)sub在src中最后一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub       子数组
     * @param src       父数组
     * @param endOffset src数组末尾部分不参与查找的长度,如:sub={1,2},src={0,1,2,1,2},endOffset=2将返回1
     * @param end       从后往前找时,结束的位置,即:找到此位置时还未找到出现的索引,则不再继续往前找
     * @return
     */
    public static int lastIndex(float[] sub, float[] src, int endOffset, int end) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen - endOffset;
            int flag = subLen - 1;
            for (int i = len; i >= 0 && i >= end; i--) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src中查找(不包含末尾长度endOffset)sub在src中最后一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub       子数组
     * @param src       父数组
     * @param endOffset src数组末尾部分不参与查找的长度,如:sub={1,2},src={0,1,2,1,2},endOffset=2将返回1
     * @param end       从后往前找时,结束的位置,即:找到此位置时还未找到出现的索引,则不再继续往前找
     * @return
     */
    public static int lastIndex(char[] sub, char[] src, int endOffset, int end) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen - endOffset;
            int flag = subLen - 1;
            for (int i = len; i >= 0 && i >= end; i--) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 从src中查找(不包含末尾长度endOffset)sub在src中最后一次出现的索引,若sub不在src内,则返回-1
     *
     * @param sub       子数组
     * @param src       父数组
     * @param endOffset src数组末尾部分不参与查找的长度,如:sub={1,2},src={0,1,2,1,2},endOffset=2将返回1
     * @param end       从后往前找时,结束的位置,即:找到此位置时还未找到出现的索引,则不再继续往前找
     * @return
     */
    public static int lastIndex(boolean[] sub, boolean[] src, int endOffset, int end) {
        if (Typer.notNull(sub, src) && sub.length > 0) {
            int subLen = sub.length;
            int len = src.length - subLen - endOffset;
            int flag = subLen - 1;
            for (int i = len; i >= 0 && i >= end; i--) {
                for (int j = 0; j < subLen; j++) {
                    if (sub[j] != src[i + j]) {
                        break;
                    }
                    if (j == flag) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

}
