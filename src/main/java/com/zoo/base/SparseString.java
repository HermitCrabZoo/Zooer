package com.zoo.base;

public class SparseString extends AbstractCompressionString {

    /**
     * 每个long型中可用于映射的bit数 <= {@link Long#SIZE}
     */
    private int validatedBits;

    /**
     * 将参考碱基序列包装为RefSeq对象
     *
     * @param str 要包装的参考碱基序列，只支持A、T、C、G
     */
    public SparseString(String str) {
        super(str);
    }

    /**
     * 将参考碱基序列包装为RefSeq对象
     *
     * @param bytes 要包装的参考碱基序列，只支持A、T、C、G
     */
    public SparseString(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected void init(byte[] bytes) {
        super.init(bytes);
        validatedBits = Long.SIZE - Long.SIZE % perBits;
    }

    @Override
    protected void mapBits(byte[] bytes) {
        long addr = address;
        for (int i = 0, len = bytes.length; i < len;) {
            long value = 0;
            for (int j = 0; j < validatedBits && i < len; j += perBits, i++) {
                value |= (long) charBits[bytes[i] & 0xff] << j;
            }
            UNSAFE.putLong(addr, value);
            addr += Long.BYTES;// 每8个字节写一次
        }
    }

    @Override
    protected long bytesFor(long bits) {
        long size = (bits - 1) / validatedBits + 1;//需要long型个数
        return size * Long.BYTES;
    }

    @Override
    protected String toString(int start, int end) {
        int count = 0, capacity = end - start;
        //这里直接使用char[]来接收结果比StringBuilder.append(char c)性能更佳(即使最后需要用new String(char[] value)来返回结果)
        char[] chars = new char[capacity];
        //定位首字符所在的第一个字节的地址和首bit在起始字节中的偏移
        long bits = (long) start * perBits;
        long multiple = bits / validatedBits;
        long addr = address + multiple * Long.BYTES;//start所在long的起始字节
        long offset = bits - multiple * validatedBits;
        while (count < capacity) {
            long value = UNSAFE.getLong(addr) >>> offset;
            for (long i = offset; i < validatedBits && count < capacity; i += perBits) {
                chars[count++] = bitChars[(int) value & valueMask];
                value >>>= perBits;
            }
            offset = 0;
            addr += Long.BYTES;// 每8个字节获取一次
        }
        return new String(chars);
    }

    @Override
    public char charAt(int index) {
        if ((index < 0) || (index >= length)) {
            throw new StringIndexOutOfBoundsException(index);
        }
        long bits = (long) index * perBits;
        long multiple = bits / validatedBits;
        long addr = address + multiple * Long.BYTES;//index所在long的起始字节
        long r = bits - multiple * validatedBits;
        int value = (int) (UNSAFE.getLong(addr) >>> r);
        return bitChars[value & valueMask];
    }
}
