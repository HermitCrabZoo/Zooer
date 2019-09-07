package com.zoo.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zoo.mix.Eacher;

public final class CopyResult {

	private CopyResult() {}
	protected static CopyResult instance() {
		return new CopyResult();
	}
	private List<Path> ins=new ArrayList<Path>();
	private List<Path> tos=new ArrayList<Path>();
	private Path nowIn=null;
	private int size=0;
	private int failed=0;
	private Map<Path, Exception> pex=new HashMap<Path, Exception>();
	private int status=UN_COPIED;
	
	/**
	 * 添加输入和输出的文件或目录，两者都不能为空，不然无法添加成功。
	 * @param in
	 * @param to
	 * @return
	 */
	protected CopyResult add(Path in,Path to) {
		if (in!=null || to!=null) {
			ins.add(in);
			tos.add(to);
			this.nowIn=in;
			size++;
		}
		return this;
	}
	
	/**
	 * 添加异常信息，该异常对应着前一次调用 {@link #add(Path, Path)}方法时所添加的文件拷贝信息
	 * @param e
	 * @return
	 */
	protected CopyResult addException(Exception e) {
		if (nowIn!=null) {
			pex.put(nowIn, e);
			failed++;
		}
		return this;
	}
	
	/**
	 * 输入与输出文件或目录对的总数
	 * @return
	 */
	public int size() {
		return size;
	}
	
	public int successful() {
		return size-failed;
	}
	
	public int failed() {
		return failed;
	}
	
	public Path toByIn(Path in) {
		return getPath(in,ins,tos);
	}
	
	public Path inByTo(Path to) {
		return getPath(to,tos,ins);
	}
	
	public int status() {
		return status;
	}
	
	protected void setStatus(int status) {
		this.status=status;
	}
	
	private Path getPath(Path x,List<Path> froms,List<Path> targets) {
		if (x!=null) {
			int i=froms.indexOf(x);
			if (i>-1) {
				return Paths.get(targets.get(i).toString());
			}
		}
		return null;
	}
	
	public Exception exceptByIn(Path in) {
		if (in!=null) {
			return pex.get(in);
		}
		return null;
	}
	
	public Exception exceptByTo(Path to) {
		Path in=inByTo(to);
		if (in!=null) {
			return pex.get(in);
		}
		return null;
	}
	
	/**
	 * 调用Eacher对象的doIt方法时传入的第三个参数e可能为null
	 * @param eacher
	 * @return
	 */
	public CopyResult forEach(Eacher<Path,Path,Exception> eacher) {
		if (eacher!=null) {
			for(int i=0;i<size;i++) {
				Path in=ins.get(i);
				eacher.doIt(in, tos.get(i), pex.get(in));
			}
		}
		return this;
	}
	
	/**
	 * 未完成或未开始的拷贝
	 */
	public static final int UN_COPIED=-1;
	
	/**
	 * 源文件/目录无读取权限或不是可读的文件或目录
	 */
	public static final int UN_READABLE=0;
	
	/**
	 * 目标文件/目录为null,或从目录拷贝到文件
	 */
	public static final int UN_WRITEABLE=1;
	
	/**
	 * 源文件/目录与目标文件/目录相同
	 */
	public static final int SAME=2;
	
	/**
	 * 拷贝完成
	 */
	public static final int COMPLETED=3;
}