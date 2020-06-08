package com.zoo.io;

import lombok.Getter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 文件行迭代器抽象类，默认第一行开始。
 *
 * @param <T>
 * @author Devil
 */
public abstract class FileItr<T> implements Iterator<T>, Iterable<T>, Closeable {
    // BufferedReader对象用于close方法的自动关闭
    private final BufferedReader br;
    /**
     * 下一行要返回的内容
     */
    private T next = null;
    private String current = null;
    private boolean found = false;
    private boolean skipHead = false;
    private HeadType headType = HeadType.BEGIN;
    private String startsWith = "";

    /**
     * 表示头部匹配规则的正反向，若为true，表示直到通过{@link #startsWith}匹配到；若为false，表示直到未通过{@link #startsWith}匹配到。
     */
    private boolean until = true;


    /**
     * 表示起始匹配时是否对字符串大小写敏感，用于{@link #isBegin(String)}方法的判断，若该方法被子类重写，则此属性可能失去意义。
     */
    private boolean sensitive = true;

    /**
     * 表示是否已经初始化
     */
    protected boolean inited = false;

    /**
     * 表示是否已经关闭
     */
    protected volatile boolean closed = false;

    /**
     * 并发关闭锁
     */
    private final Object closeLock = new Object();

    /**
     * 匹配到的头，参考{@link HeadType}
     */
    private String header;

    /**
     * begin匹配到的行，若{@link #headType}属性为{@link HeadType#BEGIN}，则该属性值与{@link #header}相同。
     */
    private String beginLine;

    /**
     * 从匹配到开始的当前行的行号。
     */
    @Getter
    private long num = 0;

    /**
     * 通过Path路径构造
     *
     * @param filePath 必须是指向一个文件的Path
     * @throws FileNotFoundException 文件不存在时抛出
     */
    public FileItr(Path filePath) throws FileNotFoundException {
        this(filePath.toString());
    }

    /**
     * 通过字符串路径构造
     *
     * @param filePath 必须是指向一个文件的路径
     * @throws FileNotFoundException 文件不存在时抛出
     */
    public FileItr(String filePath) throws FileNotFoundException {
        this(new FileInputStream(filePath));
    }

    /**
     * 通过输入流来构造
     *
     * @param inputStream 不能为null
     */
    public FileItr(InputStream inputStream) {
        br = new BufferedReader(new UnicodeReader(inputStream, StandardCharsets.UTF_8.name()));
    }

    public boolean hasNext() {
        //未关闭才可能有下一行
        return !closed && (next != null || (next = goNext()) != null);
    }

    public T next() {
        if (closed) {
            return null;
        }
        if (next == null) {
            return goNext();
        }
        T rt = next;
        next = null;
        return rt;
    }


    /**
     * 获取下一行内容。当未找到起始行时
     * @return 下一行的内容，当没有时，则返回null
     * @throws NoSuchElementException 当IO异常时
     */
    private T goNext() {
        try {
            if (!found && !inited) {
                inited = true;
                String line;
                while ((line = br.readLine()) != null) {
                    if (header == null && headType == HeadType.FIRST) {
                        header = line;
                    }
                    if (isBegin(line)) {
                        if (header == null && headType == HeadType.BEGIN) {
                            header = line;
                        }
                        beginLine = line;
                        onMatched(line, header);
                        found = true;
                        if (!skipHead) {
                            num++;
                            this.current = line;
                            return cast(line);
                        }
                        break;
                    }
                }
            }
            if (found) {
                String line;
                if ((line = br.readLine()) == null)
                    return null;
                num++;
                this.current = line;
                return cast(line);
            }
        } catch (IOException e) {
            throw new NoSuchElementException(e.getMessage());
        }
        return null;
    }


    @Override
    public void close() throws IOException {
        synchronized (closeLock) {
            if (closed) {
                return;
            }
            closed = true;
        }
        // 关闭资源
        this.br.close();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    /**
     * begin匹配到的行，若{@link #headType}属性为{@link HeadType#BEGIN}，则该属性值与{@link #header}相同。
     *
     * @return 起始行的内容
     */
    public String getBeginLine() {
        // 若匹配行是null,且流未关闭，则尝试初始化。
        if (beginLine == null && !inited && !closed) {
            hasNext();
        }
        return beginLine;
    }

    /**
     * 获取文件头部行内容，取决于{@link #headType}属性：<br>
     * {@link HeadType#FIRST}：遍历开始的第一行
     * {@link HeadType#BEGIN}：通过{@link #isBegin(String)}匹配到的那一行，此时当前方法返回的行与{@link #getBeginLine}返回的行相同
     *
     * @return 表示头行内容
     */
    public String getHeader() {
        // 若头为null,且流未关闭，则尝试初始化。
        if (header == null && !inited && !closed) {
            hasNext();
        }
        return header;
    }

    /**
     * 设置是否跳过匹配头的那一行(由{@link #isBegin(String)}匹配到返回true的行)，若设置为true，则第一次调用{@link #next()}方法将返回由{@link #isBegin(String)}方法匹配的行的下一行。
     *
     * @param skip 是否跳过，默认false。
     * @return this
     */
    public FileItr<T> skipHead(boolean skip) {
        this.skipHead = skip;
        return this;
    }

    /**
     * 设置头部识别方式。
     *
     * @param type see also{@link HeadType}，默认：{@link HeadType#BEGIN}。
     * @return this
     */
    public FileItr<T> withHead(HeadType type) {
        if (type == null) {
            throw new NullPointerException("参数'type'不能为null！");
        }
        this.headType = type;
        return this;
    }

    /**
     * 设置第一行匹配的开始字符串。
     *
     * @param start 开始字符串，默认空字符串
     * @return this
     */
    public FileItr<T> startsWith(String start) {
        if (start == null) {
            throw new NullPointerException("A argument 'startsWith' must be not null!");
        }
        this.startsWith = start.toLowerCase();
        return this;
    }

    /**
     * 设置开始行的匹配规则的正反向，若为true，表示直到通过{@link #startsWith}匹配到；若为false，表示直到未通过{@link #startsWith}匹配到。
     *
     * @param direction 默认true
     * @return this
     */
    public FileItr<T> until(boolean direction) {
        this.until = direction;
        return this;
    }

    /**
     * 设置开始行的匹配是否大小写敏感，若为true，表示直到通过{@link #startsWith}设置的匹配对字符串大小做区分；若为false，表示通过{@link #startsWith}设置的匹配对字符串大小不敏感。
     *
     * @param sensitive 默认true
     * @return this
     */
    public FileItr<T> startsWithCase(boolean sensitive) {
        this.sensitive = sensitive;
        return this;
    }

    /**
     * 获取当前行，用于迭代过程中返回当前的迭代行
     *
     * @return 当前行内容
     */
    public String getCurrent() {
        return this.current;
    }

    /**
     * 在使用{@link #isBegin(String)}方法匹配到行后，会调用该方法。
     *
     * @param matched 匹配到的行。
     * @param header  标记为header的那一行，参考{@link HeadType}。
     */
    protected void onMatched(String matched, String header) {
    }

    /**
     * 判断该行是否是起始行
     *
     * @param textLine 文本的行内容
     * @return 是返回true，否返回false
     */
    protected boolean isBegin(String textLine) {
        String sw = startsWith;
        if (!sensitive) {
            textLine = textLine.toLowerCase();
            sw = sw.toLowerCase();
        }
        if (until) {
            return textLine.startsWith(sw);
        }
        return !textLine.startsWith(sw);
    }

    /**
     * 将文本行转换为特定类型的对象输出。
     *
     * @param textLine 文本的每一行内容
     * @return 特定类型的对象
     */
    protected abstract T cast(String textLine);

    /**
     * 文本头的认定方式
     */
    public enum HeadType {
        /**
         * 读取到的第一行作为头。
         */
        FIRST,
        /**
         * 使用{@link #isBegin(String)}方法匹配到的行作为头。
         */
        BEGIN
    }

}
