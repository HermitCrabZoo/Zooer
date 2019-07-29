package com.zoo.mix;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 文件行转Map<String,String>迭代器，默认以\t分割。
 *
 */
public class FileMapItr extends FileItr<Map<String, String>> {

	private String delimiter = "\t";
	private String[] heads;
	private DuplicateStrategy strategy = DuplicateStrategy.EXCEPTION;
	private String duplicate = null;

	public FileMapItr(String filePath) throws FileNotFoundException {
		super(filePath);
	}

	@Override
    protected void onMatched(String matched, String header) {
        heads = header.split(delimiter);
        if(strategy == DuplicateStrategy.EXCEPTION) {
        	int len = heads.length;
        	Set<String> set = new HashSet<>(len);
        	for(int i=0;i<len;i++) {
        		String h = heads[i];
        		if(set.contains(h)) {
        			this.duplicate = h;
        			throw new IllegalFileFormatException(String.format("It's not allows duplicate key '%s' under '%s' strategy!", h, strategy));
        		}
        		set.add(h);
        	}
        }
        
    }

	@Override
	protected Map<String, String> cast(String textLine) {
		if(duplicate !=null && strategy==DuplicateStrategy.EXCEPTION) {
			throw new IllegalFileFormatException(String.format("It's not allows duplicate key '%s' under '%s' strategy!", duplicate, strategy));
		}
		String[] ls = textLine.split(delimiter);
		String[] heads = this.heads;
		int hLen = heads.length;
		int len = Math.min(hLen, ls.length);
		Map<String, String> map = new HashMap<>(hLen);
		if(strategy==DuplicateStrategy.FIRST) {
			//FIRST的情况下不覆盖相同key的值
			for (int i = 0; i < len; i++) {
				String h = heads[i];
				if(!map.containsKey(h)) {
					map.put(heads[i], ls[i]);
				}
			}
		}else {
			//COVER的情况若存在相同key则覆盖旧的值
			for (int i = 0; i < len; i++) {
				map.put(heads[i], ls[i]);
			}
		}
		//不管是FIRST或COVER，都不覆盖没有值的那些头
		for (int i = len; i < hLen; i++) {
			String h = heads[i];
			if(!map.containsKey(h)) {
				map.put(heads[i], "");
			}
		}
		return map;
	}

	/**
	 * 设置单字段(列)分割符
	 * 
	 * @param delimiter 默认：\t
	 * @return
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
	 * @return
	 */
	public FileMapItr withDuplicate(DuplicateStrategy strategy) {
		this.strategy = strategy;
		return this;
	}

	/**
	 * 列重复的处理策略
	 *
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

	public class IllegalFileFormatException extends RuntimeException {
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
