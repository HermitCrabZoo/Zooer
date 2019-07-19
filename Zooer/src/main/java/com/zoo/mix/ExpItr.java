package com.zoo.mix;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

/**
 * expression文件行迭代器
 */
public class ExpItr extends FileItr<String> {

    private String startsWith = "index";

    public ExpItr(Path path) throws FileNotFoundException {
        super(path);
    }

    public ExpItr(File textFile) throws FileNotFoundException {
        super(textFile);
    }

    public ExpItr(String filePath) throws FileNotFoundException {
        super(filePath);
    }

    @Override
    protected boolean isBegin(String textLine) {
        return textLine.toLowerCase().startsWith(startsWith);
    }

    @Override
    protected String cast(String textLine) {
        return textLine;
    }


    /**
     * 设置第一行匹配的开始字符串。
     * @param start 开始字符串
     * @return
     */
    public ExpItr startsWith(String start){
        if(start==null){
            throw new NullPointerException("A argument 'startsWith' must be not null!");
        }
        this.startsWith = start.toLowerCase();
        return this;
    }
}
