package com.zoo.mix;

import java.io.FileNotFoundException;

/**
 * 文件行迭代器，默认第一行开始，调用{@link #startsWith(String)}指定匹配第一行的起始字符串。
 * @author Devil
 *
 */
public class FileLineItr extends FileItr<String>{

	public FileLineItr(String filePath) throws FileNotFoundException {
		super(filePath);
	}

	@Override
	protected String cast(String textLine) {
		return textLine;
	}

}
