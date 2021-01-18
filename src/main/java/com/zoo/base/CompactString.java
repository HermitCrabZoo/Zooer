package com.zoo.base;

public class CompactString extends AbstractCompressionString {

    /*
     * RefSeqs are packed into arrays of "words." Currently a word is a byte, which
     * consists of 8 bits, requiring 3 address bits. The choice of word size is
     * determined purely by performance concerns.
     */
    private final static int ADDRESS_BITS_PER_WORD = 3;

    private final static int BIT_SHIFT_RIGHT_MASK = (1 << ADDRESS_BITS_PER_WORD) - 1;

    /**
     * 将参考碱基序列包装为RefSeq对象
     *
     * @param str 要包装的参考碱基序列，只支持A、T、C、G
     */
    public CompactString(String str) {
        super(str);
    }

    /**
     * 将参考碱基序列包装为RefSeq对象
     *
     * @param bytes 要包装的参考碱基序列，只支持A、T、C、G
     */
    public CompactString(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected void mapBits(byte[] bytes) {
        long addr = address;
        // lastBits为上一轮循环余下的位数(小于perBits),lastValue为上一轮循环中余下的位数从低位对齐后的值
        int lastBits = 0, lastValue = 0;
        for (int i = 0, len = bytes.length; i < len; ) {
            long value = lastValue;
            int bits = 0;
            for (int j = lastBits; j < Long.SIZE && i < len; j += perBits, i++) {
                bits = charBits[bytes[i] & 0xff];
                value |= (long) bits << j;
            }
            int size = Long.SIZE - lastBits;
            lastBits = (perBits - size % perBits) % perBits;//TODO
            lastValue = bits >> (perBits - lastBits);// 由于bits是最多占用perBits位的正整数，那么如果lastBits为0则lastValue为0
            UNSAFE.putLong(addr, value);
            addr += Long.BYTES;// 每8个字节写一次
        }
        if (lastBits > 0) {
            UNSAFE.putLong(addr, lastValue);// 写入冗余的部分字节
        }
    }

    @Override
    protected long bytesFor(long bits) {
        return ((bits - 1) >> ADDRESS_BITS_PER_WORD) + Long.BYTES;
    }

    @Override
    protected String toString(int start, int end) {
        int count = 0, capacity = end - start;
        //这里直接使用char[]来接收结果比StringBuilder.append(char c)性能更佳(即使最后需要用new String(char[] value)来返回结果)
        char[] chars = new char[capacity];
        //定位首字符所在的第一个字节的地址和首bit在起始字节中的偏移
        long bits = (long) start * perBits;
        long addr = address + (bits >> ADDRESS_BITS_PER_WORD), lastValue = 0;//lastValue为上一轮循环中余下的位数从低位对齐后的值
        int offset = (int) (bits & BIT_SHIFT_RIGHT_MASK), lastBits = 0;//lastBits为上一轮循环余下的位数(小于perBits)
        while (count < capacity) {
            long value = UNSAFE.getLong(addr);
            if (lastBits > 0) {
                long v = (value << lastBits) | lastValue;// 低lastBits位合并上一轮循环剩下的bits
                chars[count++] = bitChars[(int) v & valueMask];
            }
            value >>>= offset;
            int size = Long.SIZE - offset;
            lastBits = size % perBits;
            for (int i = offset, len = Long.SIZE - lastBits; i < len && count < capacity; i += perBits) {
                chars[count++] = bitChars[(int) value & valueMask];
                value >>>= perBits;
            }
            offset = (perBits - lastBits) % perBits;
            lastValue = value;// 由于每个value最多占用perBits位的正整数，那么如果lastBits为0则lastValue为0
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
        long addr = address + (bits >> ADDRESS_BITS_PER_WORD);
        long r = bits & BIT_SHIFT_RIGHT_MASK;
        int value = (int) (UNSAFE.getLong(addr) >>> r);
        return bitChars[value & valueMask];
    }
}
