package com.zoo.test;

import org.junit.*;

public class TestUtil {

	@Before
	public void before() {
		System.out.println("before");
	}
	public static void main(String[] args) {
		new TestUtil().testing();
	}
	@Test
	public void testing() {
		/*long startTime=System.currentTimeMillis();//开始毫秒数
		String[] names=ExtensionUtil.getExtensionNames(new File("F:\\参考文档\\python\\"), null);
		long endTime=System.currentTimeMillis();//结束毫秒数
		System.out.println("用时:"+(endTime-startTime));//用时
		String[] names2=ExtensionUtil.getExtensionNames(new File("F:\\参考文档\\python\\"), null);
		System.out.println("names length:"+names.length);
		System.out.println("names extensions:"+ArrayUtil.join("|", names));
		System.out.println("names2 length:"+names2.length);
		System.out.println("names2 extensions:"+ArrayUtil.join("|", names2));*/
		
		/*File[] files=new File("e:\\uploads\\2016\\20161006\\ccc").listFiles();
		System.out.println(files.length);
		System.out.println();*/
		
		/*String[] strs = new String[] { "A", "B", "C", "D", "E" };
		int[] ints = new int[] { 1, 2, 3, 4, 5 };
		long[] longs = new long[] { 6L, 7L, 8L, 9L, 10L };
		short[] shorts = new short[] { 11, 12, 13, 14, 15 };
		byte[] bytes = new byte[] { 16, 17, 18, 19, 20 };
		char[] chars = new char[] { '我', '在', '测', '试', 'j', 'o', 'i', 'n' };
		boolean[] booleans = new boolean[] { true, false, true, true, false };
		String delimiter = ",";
		long start = System.currentTimeMillis();
		ArrayUtil.join(delimiter, strs);
		String.join(delimiter, strs);
		ArrayUtil.join(delimiter, ints);
		System.out.println(ArrayUtil.join(delimiter, longs));
		System.out.println(ArrayUtil.join(delimiter, shorts));
		System.out.println(ArrayUtil.join(delimiter, bytes));
		System.out.println(ArrayUtil.join(delimiter, chars));
		System.out.println(ArrayUtil.join(delimiter, booleans));
		long end = System.currentTimeMillis();
		System.out.println(end - start);*/
		
		
		
		
		
		
	}

	@After
	public void after() {
		System.out.println("after");
	}

}
