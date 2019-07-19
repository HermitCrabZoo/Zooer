package com.zoo.mix;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

import lombok.Getter;

public abstract class FileItr<T> implements Iterator<T>, Iterable<T>, Closeable {
    private FileReader fr;
    private BufferedReader br;
    private T next = null;
    private boolean found = false;
    private boolean skipHead = false;
    private HeadType headType = HeadType.BEGIN;

    @Getter
    private String header;

    @Getter
    private long num = 0;

    public FileItr(Path path) throws FileNotFoundException {
        this(path.toString());
    }

    public FileItr(File textFile) throws FileNotFoundException {
        this(textFile.getPath());
    }

    public FileItr(String filePath) throws FileNotFoundException {
        fr = new FileReader(filePath);
        br = new BufferedReader(fr);
    }


    public boolean hasNext() {
        return next != null || next() != null;
    }

    public T next() {
        if (next != null) {
            T rt = next;
            next = null;
            return rt;
        }
        try {
            if (!found) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(header == null && headType == HeadType.FIRST){
                        header = line;
                    }
                    if (isBegin(line)) {
                        if(header == null && headType == HeadType.BEGIN){
                            header = line;
                        }
                        found = true;
                        if(!skipHead){
                            num++;
                            return this.next = cast(line);
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
                this.next = cast(line);
            }
        } catch (IOException e) {
            throw new NoSuchElementException(e.getMessage());
        }
        return this.next;
    }

    @Override
    public void close() throws IOException {
        // 关闭资源
        try (FileReader fileReader = this.fr;
             BufferedReader bufferedReader = this.br) {
        }
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    /**
     * 设置是否跳过匹配头的那一行(由{@link #isBegin(String)}匹配到返回true的行)，若设置为true，则第一次调用{@link #next()}方法将返回由{@link #isBegin(String)}方法匹配的行。
     * @param skip 是否跳过
     * @return
     */
    public FileItr<T> skipHead(boolean skip){
        this.skipHead = skip;
        return this;
    }


    /**
     * 设置头部识别方式。
     * @param type see also{@link HeadType}，默认：{@link HeadType#BEGIN}。
     * @return
     */
    public FileItr<T> withHead(HeadType type){
        if(type==null){
            throw new NullPointerException("参数'type'不能为null！");
        }
        this.headType = type;
        return this;
    }
    

    /**
     * 判断该行是否是起始行
     * @param textLine 文本的行内容
     * @return
     */
    protected abstract boolean isBegin(String textLine);

    /**
     * 将文本行转换为特定类型的对象输出。
     * @param textLine 文本的每一行内容
     * @return 特定类型的对象
     */
    protected abstract T cast(String textLine);

    /**
     * 文本头的人定方式
     */
    public enum HeadType{
        /**
         * 读取到的第一行作为头。
         */
        FIRST,
        /**
         * 使用{@link #isBegin(String)}方法匹配到的行作为头。
         */
        BEGIN;
    }
}
