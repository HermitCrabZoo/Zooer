package com.zoo.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.opencv.core.Mat;

import com.google.zxing.common.StringUtils;
import com.zoo.cons.Charsets;
import com.zoo.cons.Images;
import com.zoo.util.Arrs;
import com.zoo.util.Beaner;
import com.zoo.util.CvBridge;
import com.zoo.util.Chars;
import com.zoo.util.Charsetor;
import com.zoo.util.Chroma;
import com.zoo.util.Colors;
import com.zoo.util.CopyResult;
import com.zoo.util.Dater;
import com.zoo.util.Facer;
import com.zoo.util.Filer;
import com.zoo.util.Funcs;
import com.zoo.util.Imager;
import com.zoo.util.Imgs;
import com.zoo.util.Pager;
import com.zoo.util.Pather;
import com.zoo.util.QRCode;
import com.zoo.util.Resource;
import com.zoo.util.Strs;
import com.zoo.util.Syss;
import com.zoo.util.Videor;
import com.zoo.util.Worder;
import com.zoo.util.Yuv;

import net.sf.cglib.beans.BeanCopier;

import javax.imageio.ImageIO;
import javax.print.attribute.HashAttributeSet;
import javax.swing.ImageIcon;

public class TestFrame{
	 static Clock clock=Clock.systemUTC();
	 static {
			System.load("E:\\GitRepositorys\\Zooer\\Zooer\\lib\\opencv_java331.dll");
	 }
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Path path=Paths.get("E:\\images\\video.mp4");
		Path fPath=Paths.get("E:\\images\\video.png");
		String abc="word分词是一个Java实现的中文分词组件，提供了多种基于词典的分词算法，并利用ngram模型来消除歧义。 能准确识别英文、数字，以及日期、时间等数量词，能识别人名、地名、组织机构名等未登录词。 同时提供了Lucene、Solr、ElasticSearch插件。";
//		System.out.println(Worder.pureWords(abc));
//		System.out.println(Worder.intactWords(abc));
//		testBeanCopy();
//		testAvg(1);
		testImg();
	}
	public static void testImg() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TFrame frame = new TFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static class TFrame extends JFrame{
		private JPanel contentPane;
		public TFrame() {
			int fw=1500,fh=980;
			int w=fw/10,h=fh/7,r=40;
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(0, 0, fw+16, fh+39);
			setLocationRelativeTo(null);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(null);
			setContentPane(contentPane);
			Color fg=new Color(0,129,211,255),bg=new Color(174,244,235);
			BufferedImage qrcode=QRCode.qrCode("https://www.baidu.com", 800, 800,fg,bg);
			Optional.ofNullable(qrcode).orElseGet(null);
			try {
				BufferedImage imageA=ImageIO.read(new File("E:\\130922.jpg"));
//				BufferedImage imageC=ImageIO.read(new File("E:\\星北.jpg"));
				BufferedImage imageC=ImageIO.read(new File("E:\\images\\video.png"));
				BufferedImage circle=Imgs.of(imageC).cutBehind().scaleWidth(100).borderDropRadius(1,Color.white,40).get();
				BufferedImage bufferedImage=Imgs.ofNew(500, 500, new Color(255, 255, 255,20 )).get();
				int pixel =bufferedImage.getRGB(1, 1);
				int red = (pixel & 0xff0000) >> 16;  
                int g = (pixel & 0xff00) >> 8;  
                int b = (pixel & 0xff);
				System.out.println(red);
				System.out.println(g);
				System.out.println(b);
				Mat mat=CvBridge.mat(circle);
				BufferedImage bImage=CvBridge.image(mat);
				Imgs.of(qrcode).pileCenter(bImage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			JLabel jlLabel=new JLabel(new ImageIcon(qrcode));
			//JLabel jlLabel=new JLabel(new ImageIcon(image1));
			jlLabel.setBounds(0, 0, fw, fh);
			contentPane.add(jlLabel,new Integer(Integer.MAX_VALUE));
			contentPane.setBackground(Colors.randColor(Chroma.lightest));
			setVisible(true);
		}
	}
	public static void testBeanCopy(){
		
		List<KV> kvf=new ArrayList<KV>();
		List<KV> kvt1=new ArrayList<KV>();
		List<KV> kvt2=new ArrayList<KV>();
		int size=100000;
		for(int i=0;i<size;i++) {
			kvf.add(new KV("key"+i,"value"+i));
			kvt1.add(new KV());
			kvt2.add(new KV());
		}
		
		System.out.println("starting");
		BeanCopier bCopier=BeanCopier.create(KV.class,KV.class, false);
		long a=clock.millis();
		for(int i=0;i<size;i++) {
//			bCopier.copy(kvf.get(i), kvt1.get(i), null);
			Beaner.copy(kvf.get(i), kvt1.get(i));
		}
		long b=clock.millis();
		for(int i=0;i<size;i++) {
			KV kv=kvf.get(i);
			KV kk=kvt2.get(i);
			kk.setKey(kv.getKey());
			kk.setValue(kk.getValue());
		}
		long c=clock.millis();
		System.out.println(b-a);
		System.out.println(c-b);
		System.out.println("end");
	}
	public static void testAvg(int times){
		Random random =new Random();
		long[] ss=new long[times];
		long s=0L,e=0L;
		long[] cc=random.longs(100000).toArray();
		String[] strings=new String[]{Chars.randChar(),null,Chars.randChar()};
		double[] doubles=new double[] {};
		System.out.println(Arrs.join(",", Arrs.distinct(strings)));
		System.out.println(Arrs.join(",", Arrs.distinct(doubles)));
		System.out.println(Arrs.avg(doubles));
		System.out.println(Dater.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss.SHSS"));
		for(int j=0;j<times;j++){
			s=System.currentTimeMillis();
			for(int i=0;i<10000;i++){
				Arrs.max(cc);
			}
			e=System.currentTimeMillis();
			ss[j]=e-s;
		}
		System.out.println("duration avg:"+Arrs.avg(ss));
	}
	public static class KV{
		private String key;
		private Object value;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public KV() {
			super();
			// TODO Auto-generated constructor stub
		}
		public KV(String key, Object value) {
			super();
			this.key = key;
			this.value = value;
		}
		
	}
	
}
