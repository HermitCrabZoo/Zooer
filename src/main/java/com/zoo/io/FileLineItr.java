package com.zoo.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * 文件行迭代器，默认第一行开始，调用{@link #startsWith(String)}指定匹配第一行的起始字符串。
 *
 * @author Devil
 */
public class FileLineItr extends FileItr<String> {

    public FileLineItr(Path filePath) throws FileNotFoundException {
        super(filePath);
    }

    public FileLineItr(String filePath) throws FileNotFoundException {
        super(filePath);
    }


    public FileLineItr(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    protected String cast(String textLine) {
        return textLine;
    }

}
