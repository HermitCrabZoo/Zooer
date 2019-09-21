package com.zoo.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;

import lombok.Getter;

/**
 * 文件行迭代器抽象类，默认第一行开始。
 * 
 * @author Devil
 *
 * @param <T>
 */
public abstract class FileItr<T> implements Iterator<T>, Iterable<T>, Closeable {
	// Reader对象用于close方法的自动关闭
	private InputStream is;
	private Reader reader;
	private BufferedReader br;
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
	 * 表示是否已经初始化
	 */
	protected boolean inited = false;

	/**
	 * 表示是否已经关闭
	 */
	protected volatile boolean closed = false;
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

	public FileItr(String filePath) throws FileNotFoundException {
		is = new FileInputStream(filePath);
		reader = new UnicodeReader(is, StandardCharsets.UTF_8.name());
		br = new BufferedReader(reader);
	}

	public FileItr(InputStream inputStream) throws FileNotFoundException {
		reader = new UnicodeReader(inputStream, StandardCharsets.UTF_8.name());
		br = new BufferedReader(reader);
	}

	public boolean hasNext() {
		return next != null || (next = goNext()) != null;
	}

	public T next() {
		if (next == null) {
			return goNext();
		}
		T rt = next;
		next = null;
		return rt;
	}
	
	
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
		try (Reader fileReader = this.reader; BufferedReader bufferedReader = this.br) {
		}
		if (is != null) {
			try (InputStream inputStream = this.is) {
			}
		}
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	/**
	 * begin匹配到的行，若{@link #headType}属性为{@link HeadType#BEGIN}，则该属性值与{@link #header}相同。
	 * 
	 * @return
	 */
	public String getBeginLine() {
		// 若匹配行是null,且流未关闭，则尝试初始化。
		if (beginLine == null && !inited && !closed) {
			hasNext();
		}
		return beginLine;
	}

	/**
	 * 获取文件头部行内容
	 * 
	 * @return
	 */
	public String getHeader() {
		// 若头为null,且流未关闭，则尝试初始化。
		if (header == null && !inited && !closed) {
			hasNext();
		}
		return header;
	}

	/**
	 * 设置是否跳过匹配头的那一行(由{@link #isBegin(String)}匹配到返回true的行)，若设置为true，则第一次调用{@link #next()}方法将返回由{@link #isBegin(String)}方法匹配的行。
	 * 
	 * @param skip 是否跳过，默认false。
	 * @return
	 */
	public FileItr<T> skipHead(boolean skip) {
		this.skipHead = skip;
		return this;
	}

	/**
	 * 设置头部识别方式。
	 * 
	 * @param type see also{@link HeadType}，默认：{@link HeadType#BEGIN}。
	 * @return
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
	 * @return
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
	 * @return
	 */
	public FileItr<T> until(boolean direction) {
		this.until = direction;
		return this;
	}

	/**
	 * 获取当前行
	 * 
	 * @return
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
	 * @return
	 */
	protected boolean isBegin(String textLine) {
		if (until) {
			return textLine.toLowerCase().startsWith(startsWith);
		}
		return !textLine.toLowerCase().startsWith(startsWith);
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
		BEGIN;
	}

}
