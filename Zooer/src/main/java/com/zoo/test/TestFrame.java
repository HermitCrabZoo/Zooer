package com.zoo.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.zxing.common.StringUtils;
import com.zoo.cons.Charsets;
import com.zoo.cons.Images;
import com.zoo.util.Arrs;
import com.zoo.util.Beaner;
import com.zoo.util.Chars;
import com.zoo.util.Charsetor;
import com.zoo.util.Chroma;
import com.zoo.util.Colors;
import com.zoo.util.CopyResult;
import com.zoo.util.Dater;
import com.zoo.util.Filer;
import com.zoo.util.Funcs;
import com.zoo.util.Imager;
import com.zoo.util.Imgs;
import com.zoo.util.Pather;
import com.zoo.util.QRCode;
import com.zoo.util.Resource;
import com.zoo.util.Strs;
import com.zoo.util.Syss;
import com.zoo.util.Yuv;

import net.sf.cglib.beans.BeanCopier;

import javax.imageio.ImageIO;
import javax.print.attribute.HashAttributeSet;
import javax.swing.ImageIcon;

public class TestFrame{
	 static Clock clock=Clock.systemUTC();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Path path=Paths.get("E:\\ffmpeg-3.3");
		Path fPath=Paths.get("E:\\ffmpeg-3.3\\新建文本文档.txt");
		Path npath=Paths.get("E:\\ffmpeg-3.300");
		Path in=Paths.get("E:\\Arrs.java"),to=Paths.get("E:\\CBD\\Arrs.java");
		LocalDateTime localDateTime=LocalDateTime.now();
		long a=clock.millis();
		/*CopyResult cr=Filer.copy(in,to, null, Charsetor.GBK);
		cr.forEach((i,t,e)->{
			System.out.println(i);
			System.out.println(t);
			System.out.println(e);
			});*/
		System.out.println(Dater.format(localDateTime, Dater.allFormat));
		long b=clock.millis();
		for(int i=0;i<5000;i++) {
			Dater.format(localDateTime, Dater.allFormat);
		}
		long c=clock.millis();
		System.out.println("时长:"+(b-a));
		System.out.println("时长:"+(c-b));
//		testBeanCopy();
//		testAvg(1);
//		testImg();
//		testFileCopy();
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
				Chroma[] chromas= {Chroma.lightest,Chroma.lighter,Chroma.light,Chroma.middle,Chroma.heavy,Chroma.heavier,Chroma.heaviest};
				/*Imgs imgs=Imgs.ofNull();
				Imgs imgs2=Imgs.of(bgImage);
				for(int j=0;j<7;j++) {
					for(int i=0;i<12;i++) {
						Color color=Colors.randColor(chromas[j]);
						String surface=color.getRed()+","+color.getGreen()+","+color.getBlue();
						BufferedImage ii=imgs.image(w, h,color).borderDropRadius(3,Color.WHITE,r).pile(surface).get();
						imgs2.pile(ii, i*w, j*h);
					}
				}*/
				BufferedImage imageA=ImageIO.read(new File("E:\\130922.jpg"));
				BufferedImage imageC=ImageIO.read(new File("E:\\星北.jpg"));
				System.out.println(System.currentTimeMillis());
				Imgs imgs=Imgs.ofNull();
				BufferedImage circle=imgs.setNew(imageC).cutBehind().scaleWidth(40).borderDropRadius(1,Color.white,40).get();
				BufferedImage scale=imgs.setNew(imageC).scale(0.5).get();
				BufferedImage scaleWidth=imgs.setNew(imageC).scaleWidth(200).get();
				BufferedImage scaleHeight=imgs.setNew(imageC).scaleHeight(100).get();
				BufferedImage scaleRatio=imgs.setNew(imageC).scaleRatio(400, 130).get();
				BufferedImage scaleRatioBox=imgs.setNew(imageC).scaleRatioBox(200, 300).get();
				BufferedImage scaleRatioBoxColor=imgs.setNew(imageC).scaleRatioBox(200, 300,Colors.randColor()).get();
				BufferedImage scaleZoom=imgs.setNew(imageC).scaleZoom(200,200).get();
				imgs.setNew(qrcode).pileLeftTop(scaleZoom).pileCenter(circle).pileRightTop(scale).pileRight(scaleWidth).pileRightBottom(scaleHeight).pileBottom(scaleRatio).pileLeftBottom(scaleRatioBox).pileLeft(scaleRatioBoxColor);
				double rat=imageC.getWidth()/(double)imageC.getHeight();
				System.out.println(rat);
				System.out.println(circle.getWidth()+"-"+circle.getHeight()+" r:"+circle.getWidth()/(double)circle.getHeight());
				System.out.println(scale.getWidth()+"-"+scale.getHeight()+" r:"+scale.getWidth()/(double)scale.getHeight());
				System.out.println(scaleWidth.getWidth()+"-"+scaleWidth.getHeight()+" r:"+scaleWidth.getWidth()/(double)scaleWidth.getHeight());
				System.out.println(scaleHeight.getWidth()+"-"+scaleHeight.getHeight()+" r:"+scaleHeight.getWidth()/(double)scaleHeight.getHeight());
				System.out.println(scaleRatio.getWidth()+"-"+scaleRatio.getHeight()+" r:"+scaleRatio.getWidth()/(double)scaleRatio.getHeight());
				System.out.println(scaleRatioBox.getWidth()+"-"+scaleRatioBox.getHeight()+" r:"+scaleRatioBox.getWidth()/(double)scaleRatioBox.getHeight());
				System.out.println(scaleRatioBoxColor.getWidth()+"-"+scaleRatioBoxColor.getHeight()+" r:"+scaleRatioBoxColor.getWidth()/(double)scaleRatioBoxColor.getHeight());
				System.out.println(scaleZoom.getWidth()+"-"+scaleZoom.getHeight()+" r:"+scaleZoom.getWidth()/(double)scaleZoom.getHeight());
				System.out.println(System.currentTimeMillis());
				
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
	public static void testFileCopy() {
		File file=new File("E:\\130922.jpg");
		Path dir=Paths.get("E:\\ffmpeg-win64-static");
		Path cFile=Paths.get("E:\\testJavacv\\javacvContent");
		Path gFile=Paths.get("E:\\TestResource\\ccd");
//		System.out.println(pp.toString());
//		System.out.println(pp.getFileName().toString());
//		System.out.println(pp.getParent().toString());
//		System.out.println(pp.getRoot().toString());
		try(Stream<Path> pathStream = Files.walk(cFile, FileVisitOption.FOLLOW_LINKS).parallel()) {
//			pathStream.filter(Funcs.pathTrue).forEach(p->System.out.println(p.toAbsolutePath()));
			Path path=Paths.get("E:\\TestResource\\nobb\\ccd\\cmos.pp");
//			Files.createDirectories(path.getParent());
//			Files.createFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long a=clock.millis();
//		Filer.copy(cFile, gFile);
		long b=clock.millis();
		long c=clock.millis();
		System.out.println(b-a);
		System.out.println(c-b);
		
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
