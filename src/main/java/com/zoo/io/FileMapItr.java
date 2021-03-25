package com.zoo.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

/**
 * 文件行转Map<String,String>迭代器,此Map的key是具有插入顺序的LinkedHashMap实现，默认以\t分割。
 */
public class FileMapItr extends FileItr<Map<String, String>> {

    /**
     * 字段分隔符
     */
    private String delimiter = "\t";

    /**
     * 头部数组内容
     */
    private String[] heads = {};


    /**
     * 保存了头对应的索引，与heads一一对应。
     */
    private int[] headIndexs;

    /**
     * 头部重复的策略，默认：当头部重复时抛出异常。
     */
    private DuplicateStrategy strategy = DuplicateStrategy.EXCEPTION;

    /**
     * 重复的第一个头字段(若存在重复)
     */
    private String duplicate = null;

    /**
     * 是否去掉头部前后的空白字符
     */
    private boolean stripHead = false;


    public FileMapItr(Path filePath) throws FileNotFoundException {
        super(filePath);
    }


    public FileMapItr(String filePath) throws FileNotFoundException {
        super(filePath);
    }


    public FileMapItr(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    protected void onMatched(String matched, String header) {
        heads = header.split(delimiter);
        //修剪头
        if (stripHead) {
            for (int i = 0, len = heads.length; i < len; i++) {
                heads[i] = heads[i].trim();
            }
        }
        if (strategy == DuplicateStrategy.EXCEPTION) {
            //EXCEPTION策略时判断列是否重复
            int len = heads.length;
            Set<String> set = new HashSet<>(len);
            for (String h : heads) {
                if (set.contains(h)) {
                    this.duplicate = h;
                    throw new IllegalFileFormatException(String.format("It's not allows duplicate key '%s' under '%s' strategy!", h, strategy));
                }
                set.add(h);
            }
            headIndexs = IntStream.range(0, heads.length).toArray();
        } else {
            //FIRST与COVER策略时，分别对重复的列取第一、最后一个的索引
            List<String> headList = Arrays.asList(heads);
            List<String> list = new ArrayList<>(new HashSet<>(headList));
            Comparator<? super String> c;
            ToIntFunction<? super String> mapper;
            if (strategy == DuplicateStrategy.FIRST) {
                mapper = headList::indexOf;
                c = Comparator.comparingInt(mapper);
            } else {
                mapper = headList::lastIndexOf;
                c = Comparator.comparingInt(headList::lastIndexOf);
            }
            //字段名按索引升序
            list.sort(c);
            heads = list.toArray(String[]::new);
            //字段索引升序，最终字段名与字段索引一一对应。
            headIndexs = list.stream().mapToInt(mapper).sorted().toArray();
        }

    }

    @Override
    protected Map<String, String> cast(String textLine) {
        if (duplicate != null && strategy == DuplicateStrategy.EXCEPTION) {
            throw new IllegalFileFormatException(String.format("It's not allows duplicate key '%s' under '%s' strategy!", duplicate, strategy));
        }
        //FIRST和COVER策略按内容的数量来添加
        String[] ls = textLine.split(delimiter);
        String[] heads = this.heads;
        int[] headIndexs = this.headIndexs;
        int hLen = headIndexs.length;
        int minLen = headIndexs[hLen - 1] + 1;
        int cLen = ls.length;
        Map<String, String> map = new LinkedHashMap<>((int) (hLen / 0.75) + 1);//to avoid resize
        if (cLen >= minLen) {
            //内容数达到最大索引对应的列数
            for (int i = 0; i < hLen; i++) {
                map.put(heads[i], ls[headIndexs[i]]);
            }
        } else {
            //内容数量小于要求的最小列数
            //按内容数量来添加
            for (int i = 0; i < cLen; i++) {
                map.put(heads[i], ls[i]);
            }
            //剩余没有内容的为空
            for (int i = cLen; i < hLen; i++) {
                map.put(heads[i], "");
            }
        }
        return map;
    }

    /**
     * 设置单字段(列)分割符
     *
     * @param delimiter 默认：\t
     * @return this
     */
    public FileMapItr withDelimiter(String delimiter) {
        if (delimiter == null) {
            throw new NullPointerException("delimiter can't be null.");
        }
        this.delimiter = delimiter;
        return this;
    }

    /**
     * 设置重复列的时候的处理策略,参考{@link DuplicateStrategy}
     *
     * @param strategy 处理策略，默认{@link DuplicateStrategy#EXCEPTION}
     * @return this
     */
    public FileMapItr withDuplicate(DuplicateStrategy strategy) {
        if (strategy == null) {
            throw new NullPointerException("strategy can't be null.");
        }
        this.strategy = strategy;
        return this;
    }


    /**
     * 是否去除头部字段前后的空格
     *
     * @param striped 是否去除空格，默认false。
     * @return this
     */
    public FileMapItr stripHead(boolean striped) {
        this.stripHead = striped;
        return this;
    }


    /**
     * 获取头部
     *
     * @return 头字段数组
     */
    public String[] getHeads() {
        //若匹配行是null,且流未关闭，则尝试初始化。
        if (heads.length == 0 && !inited && !closed) {
            hasNext();
        }
        return Arrays.copyOf(heads, heads.length);
    }


    /**
     * 列重复的处理策略
     */
    public enum DuplicateStrategy {
        /**
         * 抛异常
         */
        EXCEPTION,
        /**
         * 选第一个
         */
        FIRST,
        /**
         * 后面覆盖前面的(选最后一个)
         */
        COVER
    }

    public static class IllegalFileFormatException extends RuntimeException {
        public IllegalFileFormatException() {
            super();
        }

        public IllegalFileFormatException(String s) {
            super(s);
        }

        public IllegalFileFormatException(String message, Throwable cause) {
            super(message, cause);
        }

        public IllegalFileFormatException(Throwable cause) {
            super(cause);
        }

        private static final long serialVersionUID = -1667741761651273328L;
    }
}
