package com.zoo.test;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bytedeco.javacpp.opencv_imgcodecs;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.zoo.base.Arrs;
import com.zoo.base.Chars;
import com.zoo.mix.Beaner;
import com.zoo.mix.CvBridge;
import com.zoo.mix.Cver;
import com.zoo.mix.Dater;
import com.zoo.mix.Facer;
import com.zoo.mix.OCR;
import com.zoo.mix.Pather;
import com.zoo.se.Chroma;
import com.zoo.se.Colors;
import com.zoo.se.Imgs;

import net.sf.cglib.beans.BeanCopier;
import net.sourceforge.tess4j.TesseractException;

public class TestFrame{
	
	 static Clock clock=Clock.systemUTC();
	 static {
		 CvBridge.loadOpenCv();
		 System.setProperty("java.specification.version","1.9");
	 }
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		String result=Http2.get("https://www.baidu.com");
//		System.out.println(result);
		double v1=Facer.compare("E:\\images\\faces\\2face_2.jpeg", "E:\\images\\faces\\1face_1.jpeg");
		double v2=Facer.compareHist("E:\\images\\faces\\2face_2.jpeg", "E:\\images\\faces\\1face_1.jpeg");
		
		
		System.out.println(v1);
		System.out.println(v2);
		
		try {
//			testOCR();
//			testFace();
//			testCver();
//			testArrs();
//			testCvBridge();
//			testZipCompress();
//			testSplitWord();
//			testBeanCopy();
//			testAvg(1);
//			testImg();
//			testNewAPI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void testOCR() {
		
		Mat mat=Cver.of("E:\\images\\six.jpg").gray().threshold().erode()/*.write("E:\\images\\sixerode.jpg")*/.get();
		BufferedImage image=CvBridge.image(mat);
		String result=OCR.read(image);
		System.out.println(result);
	}
	
	
	
	
	public static void testCver() throws TesseractException {
		Cver.of("E:\\images\\one.jpg").transparency(0.9).write("E:\\images\\onet.png").get();
		
//		Cver.of("E:\\images\\videoRotateMatToImgAlpha.png").pyrUp().write("E:\\images\\videoRotateMatToImgAlphaPyrUp.png");
//		Cver.of("E:\\images\\videoRotateMatToImgAlpha.png").pyrDown().write("E:\\images\\videoRotateMatToImgAlphaPyrDown.png");
//		Cver.of("E:\\images\\videoRotateMatToImgAlpha.png").pyrUp().pyrDown().write("E:\\images\\videoRotateMatToImgAlphaPyrUpDown.png");
//		Cver.of("E:\\images\\five.jpg").morphologyEx(Imgproc.MORPH_OPEN).write("E:\\images\\five_open.jpg");
//		Cver.of("E:\\images\\five.jpg").morphologyEx(Imgproc.MORPH_CLOSE).write("E:\\images\\five_close.jpg");
//		Cver.of("E:\\images\\five.jpg").morphologyEx(Imgproc.MORPH_GRADIENT ).write("E:\\images\\five_gradient.jpg");
//		Cver.of("E:\\images\\five.jpg").morphologyEx(Imgproc.MORPH_TOPHAT).write("E:\\images\\five_tophat.jpg");
//		Cver.of("E:\\images\\five.jpg").morphologyEx(Imgproc.MORPH_BLACKHAT).write("E:\\images\\five_blackhat.jpg");
		
		
		//边缘、直线、圆检测
//		Cver.of("E:\\images\\one.jpg").equalizeHist().write("E:\\images\\oneEqualize.jpg");
//		Cver.of("E:\\images\\one.jpg").gaussianBlur(new Size(3, 3),0,0,Core.BORDER_DEFAULT).cannyNow().write("E:\\images\\oneCanny.jpg");
//		Cver.of("E:\\images\\one.jpg").gaussianBlur(new Size(3, 3),0,0,Core.BORDER_DEFAULT).sobelNow().write("E:\\images\\oneSobel.jpg");
//		Cver.of("E:\\images\\one.jpg").gaussianBlur(new Size(3, 3),0,0,Core.BORDER_DEFAULT).scharrNow().write("E:\\images\\oneScharr.jpg");
//		Cver.of("E:\\images\\one.jpg").gaussianBlur(new Size(3, 3),0,0,Core.BORDER_DEFAULT).laplacianNow().write("E:\\images\\oneLaplacian.jpg");
//		Cver.of("E:\\images\\one.jpg").hls().write("E:\\images\\oneHls.jpg");
//		Cver.of("E:\\images\\one.jpg").gray().write("E:\\images\\oneGray.jpg");
//		Cver.of("E:\\images\\circleLine.jpg").houghCircleNow(Imgproc.CV_HOUGH_GRADIENT, 1, 100, 440, 50, 0, 345).write("E:\\images\\circleLineHoughCircle.jpg");
//		Cver.of("E:\\images\\circleLine.jpg").houghLineNow().write("E:\\images\\circleLineHoughLine.jpg");
//		Cver.of("E:\\images\\circleLine.jpg").houghLinePNow().write("E:\\images\\circleLineHoughLineP.jpg");
		
		//模板匹配
//		Cver.of("E:\\images\\template.jpg").gaussianBlur().write("E:\\images\\template.jpg");
		
//		Mat templ=Imgcodecs.imread("E:\\images\\template.jpg");
//		Cver.of("E:\\images\\five.jpg").matchTemplateNow(templ,Imgproc.TM_CCORR).write("E:\\images\\fiveMatched_CCORR.jpg");
//		Cver.of("E:\\images\\five.jpg").matchTemplateNow(templ).write("E:\\images\\fiveMatched_CCORR_NORMED.jpg");
//		Cver.of("E:\\images\\five.jpg").matchTemplateNow(templ,Imgproc.TM_CCOEFF).write("E:\\images\\fiveMatched_CCOEFF.jpg");
//		Cver.of("E:\\images\\five.jpg").matchTemplateNow(templ,Imgproc.TM_CCOEFF_NORMED).write("E:\\images\\fiveMatched_CCOEFF_NORMED.jpg");
//		Cver.of("E:\\images\\five.jpg").matchTemplateNow(templ,Imgproc.TM_SQDIFF).write("E:\\images\\fiveMatched_SQDIFF.jpg");
//		Cver.of("E:\\images\\five.jpg").matchTemplateNow(templ,Imgproc.TM_SQDIFF_NORMED).write("E:\\images\\fiveMatched_SQDIFF_NORMED.jpg");
		
	}
	
	
	
	
	
	
	public static void testArrs() {
		
		long[] a0= {};
		long[] a1= {1};
		long[] a2= {1,2};
		long[] a3= {1,2,3};
		long[] a4= {1,2,3,4};
		long[] a5= {1,2,3,4,5};
		long[][] as= {a0,a1,a2,a3,a4,a5};
		
		long[][] bs= {null,null,null,null,null,null};
		
		for (int i=0,len=as.length;i<len;i++) {
			bs[i]=Arrs.reverse(as[i], true);
			System.out.println("a"+i+":"+Arrs.join(",", as[i])+" b"+i+":"+Arrs.join(",", bs[i])+" equals:"+(as[i]==bs[i]));
		}
		
		
		KV[] k0= {};
		KV[] k1= {KV.of("k1", "v1")};
		KV[] k2= {KV.of("k1", "v1"),KV.of("k2", "v2")};
		KV[] k3= {KV.of("k1", "v1"),KV.of("k2", "v2"),KV.of("k3", "v3")};
		KV[] k4= {KV.of("k1", "v1"),KV.of("k2", "v2"),KV.of("k3", "v3"),KV.of("k4", "v4")};
		KV[] k5= {KV.of("k1", "v1"),KV.of("k2", "v2"),KV.of("k3", "v3"),KV.of("k4", "v4"),KV.of("k5", "v5")};
		
		KV[][] kv1s= {k0,k1,k2,k3,k4,k5};
		KV[][] kv2s= new KV[6][];
		for (int i=0,len=kv1s.length;i<len;i++) {
			kv2s[i]=Arrs.reverse(kv1s[i], true);
			System.out.println("kv1s"+i+":"+Arrs.join(",", kv1s[i])+"==kv2s"+i+":"+Arrs.join(",", kv2s[i])+" equals:"+(kv1s[i]==kv2s[i]));
		}
	}
	
	
	public static void testNewAPI() {
		ProcessHandle self = ProcessHandle.current();  
		long PID = self.pid();
		ProcessHandle.Info procInfo = self.info();  
		    
		Optional<String[]> args = procInfo.arguments();  
		Optional<String> cmd =  procInfo.commandLine();  
		Optional<Instant> startTime = procInfo.startInstant();  
		Optional<Duration> cpuUsage = procInfo.totalCpuDuration();
		System.out.println(PID);
		System.out.println(cmd.orElse(""));
		System.out.println(startTime.orElse(null));
		System.out.println(cpuUsage.orElse(null));
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
//			BufferedImage qrcode=QRCode.qrCode("https://www.baidu.com", 1000, 900,fg,bg);
			BufferedImage bg1=Imgs.ofNew(fw, fh, Color.WHITE).get();
			try {
				BufferedImage imageA=ImageIO.read(new File("E:\\images\\five.jpg"));
//				BufferedImage imageC=ImageIO.read(new File("E:\\星北.jpg"));
				BufferedImage result=Imgs.of(imageA).scaleHeight(200).get();
				BufferedImage result2=Imgs.of(result).rotate(30).get();
				BufferedImage result3=Imgs.of(result).rotateRise(30).get();
				BufferedImage result4=Imgs.of(result).rotateDrop(30).transparency(0.2).get();
				BufferedImage result5=Imgs.of(result).shadow(null, 10, 0.3).get();
				BufferedImage result6=Imgs.of(imageA).cut(500, 450, 200, 250).write("jpg", Paths.get("E:\\images\\template.jpg")).get();
				bg1=Imgs.of(bg1).pileLeftTop(result6).get();
				/*BufferedImage bufferedImage=Imgs.ofNew(500, 500, new Color(255, 255, 255,20 )).get();
				int pixel =bufferedImage.getRGB(1, 1);
				int red = (pixel & 0xff0000) >> 16;  
                int g = (pixel & 0xff00) >> 8;  
                int b = (pixel & 0xff);
				System.out.println(red);
				System.out.println(g);
				System.out.println(b);
				Mat mat=CvBridge.mat(circle);
				BufferedImage bImage=CvBridge.image(mat);*/
			} catch (Exception e) {
				e.printStackTrace();
			}
			JLabel label1=new JLabel(new ImageIcon(bg1));
			label1.setBounds(0, 0, fw, fh);
			contentPane.add(label1,Integer.valueOf(Integer.MAX_VALUE));
			contentPane.setBackground(Colors.randColor(Chroma.lightest));
			setVisible(true);
		}
	}
	
	
	public static void testCvBridge() throws IOException {
		
//		BufferedImage image=ImageIO.read(Paths.get("E:\\images\\videoRotate.png").toFile());
//		System.out.println(image.getType());
		Mat mat=Imgcodecs.imread("E:\\images\\videoRotate.png",Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
//		System.out.println(mat.depth());
//		System.out.println(mat.type());
		long s=clock.millis();
//		Mat mat=CvBridge.mat(image);
		BufferedImage image=CvBridge.image(mat);
		long e=clock.millis();
//		Imgcodecs.imwrite("E:\\images\\videoRotateImgToMat.png", mat);
		ImageIO.write(image, "png", Paths.get("E:\\images\\videoRotateMatToImg.png").toFile());
		System.out.println(e-s);
		
	}
	
	
	public static void testFace() {
		try {
//			Mat mat=Imgcodecs.imread("E:\\images\\2.jpeg");
//			BufferedImage image=ImageIO.read(new File("E:\\images\\1.jpeg"));
			long a=clock.millis();
//			Mat dst=Facer.drawFace(mat);
			/*BufferedImage image=ImageIO.read(Paths.get("E:\\images\\1.jpeg").toFile());
			BufferedImage[] dfs=Facer.faces(image);
			ImageIO.write(dfs[0], "jpg", Paths.get("E:\\images\\1face.jpeg").toFile());*/
//			BufferedImage bdst=Facer.drawEye(image);
			String[] faces= {"1face_1.jpeg","2face_2.jpeg","4face_4.jpeg","5face_5.jpeg"};
			
			int size=faces.length;
			org.bytedeco.javacpp.opencv_core.Mat face=opencv_imgcodecs.imread("E:\\images\\faces\\5face_5.jpeg", opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
			
			List<org.bytedeco.javacpp.opencv_core.Mat> mats=new ArrayList();
			int[] types=new int[size];
			
			for (int i=0;i<size;i++) {
				Path path=Paths.get("E:\\images\\faces\\"+faces[i]);
				org.bytedeco.javacpp.opencv_core.Mat m=opencv_imgcodecs.imread(path.toString(), opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
				mats.add(m);
				types[i]=i;
			}
			
			int fl=Facer.trainPredict(mats,types, face);
			System.out.println("type:"+fl);
			
			
			long b=clock.millis();
			System.out.println((b-a)+"毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testZipCompress() {
		Path gzipDock=Paths.get("E:\\ziptest","javassist.pdf");
		Path gzipPath=Paths.get("E:\\ziptest","javassist.pdf.gz");
		
		Path zipPath=Paths.get("E:\\ziptest\\bbc","echarts3.zip");
		Path dock=Paths.get("E:\\ziptest","echarts3-docs-master");
		Path unzipDock=Paths.get("E:\\ziptest",Pather.shortName(zipPath.toString()));
		try {
			long a=clock.millis();
//			Packer.zip(dock, zipPath);
			long b=clock.millis();
//			Packer.unzip(zipPath, unzipDock);
//			Packer.ungzip(gzipPath, Paths.get("E:\\ziptest\\abc\\javassist.pdf"));
			long c=clock.millis();
			System.out.println((b-a)+"毫秒");
			System.out.println((c-b)+"毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testImgCompress() {
		try {
			BufferedImage imageA=ImageIO.read(new File("E:\\images\\video.png"));
			BufferedImage imageC=ImageIO.read(new File("E:\\images\\video.png"));
			double[] qualitys= {0.9,0.8,0.7,0.6,0.5,0.4,0.3,0.2,0.1};
			int[][] lenss=new int[qualitys.length][qualitys.length];
			for(int i=0;i<qualitys.length;i++) {
				BufferedImage img=ImageIO.read(new File("E:\\images\\video"+(i+1)+".png"));
				int[] lens=new int[qualitys.length];
				for(int j=0;j<qualitys.length;j++) {
					BufferedImage bi=Imgs.of(img).quality(qualitys[j]).get();
					ImageIO.write(bi, "png", new File("E:\\images\\video"+(i+1)+"-"+(j+1)+".png"));
					ByteArrayOutputStream baos=new ByteArrayOutputStream();
					ImageIO.write(bi, "png", baos);
					lens[j]=baos.toByteArray().length;
					baos.close();
				}
				lenss[i]=lens;
			}
			System.out.println(Arrs.join(",", qualitys));
			for(int i=0;i<lenss.length;i++) {
				System.out.println(Arrs.join(",", lenss[i]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void testSplitWord() {
		String abc="word分词是一个Java实现的中文分词组件，提供了多种基于词典的分词算法，并利用ngram模型来消除歧义。 能准确识别英文、数字，以及日期、时间等数量词，能识别人名、地名、组织机构名等未登录词。 同时提供了Lucene、Solr、ElasticSearch插件。";
//		System.out.println(Worder.pureWords(abc));
//		System.out.println(Worder.intactWords(abc));
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
		
		public static KV of(String key,Object value) {
			return new KV(key, value);
		}
		
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
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "key: "+key+" value: "+value;
		}
	}
	
}
