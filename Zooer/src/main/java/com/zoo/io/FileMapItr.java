package com.zoo.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * 文件行转Map<String,String>迭代器，默认以\t分割。
 *
 */
public class FileMapItr extends FileItr<Map<String, String>> {

	private String delimiter = "\t";
	private String[] heads = {};
	/**
	 * 保存了头对应的索引，与heads一一对应。
	 */
	private int[] headIndexs;
	private DuplicateStrategy strategy = DuplicateStrategy.EXCEPTION;
	private String duplicate = null;

	public FileMapItr(String filePath) throws FileNotFoundException {
		super(filePath);
	}
	

	public FileMapItr(InputStream inputStream) throws FileNotFoundException {
		super(inputStream);
	}

	@Override
    protected void onMatched(String matched, String header) {
        heads = header.split(delimiter);
        if(strategy == DuplicateStrategy.EXCEPTION) {
        	//EXCEPTION策略时判断列是否重复
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
        	headIndexs = IntStream.range(0, heads.length).toArray();
        }else{
        	//FIRST与COVER策略时，分别对重复的列取第一、最后一个的索引
            List<String> headList = Arrays.asList(heads);
        	List<String>  list = new ArrayList<>(new HashSet<>(headList));
        	Comparator<? super String> c;
        	Function<? super String, ? extends Integer> mapper;
        	if(strategy == DuplicateStrategy.FIRST) {
        		c = (one,two)->headList.indexOf(one)-headList.indexOf(two);
        		mapper = headList::indexOf;
        	}else {
        		c = (one,two)->headList.lastIndexOf(one)-headList.lastIndexOf(two);
        		mapper = headList::lastIndexOf;
        	}
        	//字段名按索引升序
        	list.sort(c);
        	heads = list.toArray(new String[list.size()]);
        	//字段索引升序，最终字段名与字段索引一一对应。
        	headIndexs = list.stream().map(mapper).mapToInt(Integer::intValue).sorted().toArray();
        }
        
    }

	@Override
	protected Map<String, String> cast(String textLine) {
		if(duplicate !=null && strategy==DuplicateStrategy.EXCEPTION) {
			throw new IllegalFileFormatException(String.format("It's not allows duplicate key '%s' under '%s' strategy!", duplicate, strategy));
		}
		//FIRST和COVER策略按内容的数量来添加
		String[] ls = textLine.split(delimiter);
		String[] heads = this.heads;
		int[] headIndexs = this.headIndexs;
		int hLen = headIndexs.length;
		int minLen = headIndexs[hLen-1]+1;
		int cLen = ls.length;
		Map<String, String> map = new HashMap<>(hLen);
		if(cLen>=minLen) {
			//内容数达到最大索引对应的列数
			for (int i = 0; i < hLen; i++) {
				map.put(heads[i], ls[headIndexs[i]]);
			}
		}else {
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
		if(strategy == null) {
			throw new NullPointerException("strategy can't be null.");
		}
		this.strategy = strategy;
		return this;
	}
	
	/**
	 * 获取头部
	 * @return
	 */
	public String[] getHeads() {
		//若匹配行是null,且流未关闭，则尝试初始化。
    	if(heads.length == 0 && !inited && !closed) {
    		hasNext();
    	}
		return Arrays.copyOf(heads, heads.length);
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
