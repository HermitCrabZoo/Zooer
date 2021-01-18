package com.zoo.base;

import sun.misc.Unsafe;

import java.util.BitSet;

/**
 * <p>参考序列位映射类，将A、T、C、G映射为每2位bits构成的数据结构，用以增加在存储参考序列时的内存使用效率。<p>
 * 相比String此类损失部分性能，大概是String的1/6性能，而因此换来更高的空间使用效率，内存空间占用大概是String的1/16 ~
 * 1/2,具体取决于映射的字符串中字符种类的数量。<br>
 * <p>
 * <pre>如：
 * 字符种类 <= 2     | 空间 1/32
 * 字符种类 <= 4     | 空间 1/16
 * 字符种类 <= 16    | 空间 1/8
 * 字符种类 <= 256   | 空间 1/4
 * 字符种类 <= 65536 | 空间 1/2
 * </pre>
 * </p>
 * 如果需要映射的字符串中包含的'字符种类数量'=='字符总数'，则此时 {@link AbstractCompressionString} 的内存空间效率和性能均不如{@link String}。
 */
public abstract class AbstractCompressionString implements CharSequence {

    protected static final Unsafe UNSAFE = Unsafe.getUnsafe();


    /**
     * 每个字符占用的bit位数，默认1bits
     */
    protected int perBits = 1;

    /**
     * 每个字符在数组中的索引即是其在位域中的映射值
     */
    protected char[] bitChars;

    /**
     * 每个字符占用的bit位，数组索引对应字符值，数组元素值对应其占用的bit位
     */
    protected int[] charBits;

    /**
     * 碱基序列长度(碱基个数)
     */
    protected final int length;

    /**
     * 分配的内存初始地址
     */
    protected final long address;

    /**
     * 占用的总字节数(包括最后一字节64位对齐的部分)
     */
    protected final long size;

    /**
     * Used to shift left or right for a partial word mask
     */
    protected final int valueMask;


    /**
     * 将参考碱基序列包装为压缩的对象
     *
     * @param str 要包装的参考碱基序列，只支持A、T、C、G
     */
    public AbstractCompressionString(String str){
        this(str.getBytes());
    }


    /**
     * 将参考碱基序列包装为压缩的对象
     *
     * @param bytes 要包装的参考碱基序列，只支持A、T、C、G
     */
    public AbstractCompressionString(byte[] bytes) {
//        long s1 = System.currentTimeMillis();
        init(bytes);
//        long e1 = System.currentTimeMillis();
        valueMask = -1 >>> (Integer.SIZE - perBits);
        length = bytes.length;// chars count
        size = length > 0 ? bytesFor((long) length * perBits) : 0;// bytes
        address = size > 0 ? UNSAFE.allocateMemory(size) : -1;
//        long s2 = System.currentTimeMillis();
        mapBits(bytes);
//        long e2 = System.currentTimeMillis();
//        System.out.printf("init time:%d, put time:%d%n", e1 - s1, e2 - s2);
    }

    protected void init(byte[] bytes) {
        if (bytes.length <= 0) {
            return;
        }
        int max = Integer.MIN_VALUE;
        BitSet bitSet = new BitSet(Byte.MAX_VALUE);// BitSet没有装箱操作比HashSet在去重方面更有优势
        for (byte aByte : bytes) {
            int c = aByte & 0xff;
            bitSet.set(c);
            if (max < c) {
                max = c;
            }
        }
        int[] kinds = bitSet.stream().toArray();
        perBits = unitBitsFor(kinds.length);// 获取表示一个字符所需的位长度
        bitChars = new char[kinds.length];
        charBits = new int[max + 1];
        for (int i = 0; i < kinds.length; i++) {
            bitChars[i] = (char) kinds[i];
            charBits[kinds[i]] = i;
        }
    }

    protected abstract void mapBits(byte[] bytes);

    /**
     * 根据容量计算每个字符所占用的bit数
     *
     * @param cap 容量/最大字符数
     * @return 每字符占用的bit数
     */
    static int unitBitsFor(int cap) {
        int n = cap - 1;
        n = Integer.toBinaryString(n).length();
        return (n <= 0) ? 1 : n;
    }

    /**
     * last byte 64 bits alignment.
     *
     * @param bits 至少所需的bits
     * @return 对齐后的字节数
     */
    protected abstract long bytesFor(long bits);

    @Override
    public int length() {
        return length;
    }

    /**
     * 占用的字节数
     *
     * @return long型字节总数
     */
    public long size() {
        return size;
    }

    /**
     * Returns {@code true} if, and only if, {@link #length()} is {@code 0}.
     *
     * @return {@code true} if {@link #length()} is {@code 0}, otherwise
     *         {@code false}
     */
    public boolean isEmpty() {
        return length <= 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.substring(start, end);
    }


    /**
     * Returns a string that is a substring of this string. The substring begins at
     * the specified {@code beginIndex} and extends to the character at index
     * {@code endIndex - 1}. Thus the length of the substring is
     * {@code endIndex-beginIndex}.
     * <p>
     * Examples: <blockquote>
     *
     * <pre>
     * "hamburger".substring(4, 8) returns "urge"
     * "smiles".substring(1, 5) returns "mile"
     * </pre>
     *
     * </blockquote>
     *
     * @param beginIndex the beginning index, inclusive.
     * @param endIndex   the ending index, exclusive.
     * @return the specified substring.
     * @throws IndexOutOfBoundsException if the {@code beginIndex} is negative, or
     *                                   {@code endIndex} is larger than the length
     *                                   of this {@code String} object, or
     *                                   {@code beginIndex} is larger than
     *                                   {@code endIndex}.
     */
    public String substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        int subLen = endIndex - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }

        if ((beginIndex == 0) && (endIndex == length)) {
            return this.toString();
        } else {
            return toString(beginIndex, endIndex);
        }
    }

    protected abstract String toString(int start, int end);

    @Override
    public String toString() {
        return toString(0, length);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(length > 0){
            UNSAFE.freeMemory(address);
        }
    }
}
