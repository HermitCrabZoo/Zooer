package com.zoo.test;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.GpuMat;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.zoo.base.Arrs;
import com.zoo.base.Chars;
import com.zoo.base.Strs;
import com.zoo.bean.Bun;
import com.zoo.code.QRCode;
import com.zoo.code.RSA;
import com.zoo.code.Secret;
import com.zoo.cons.Funcs;
import com.zoo.cons.Videos;
import com.zoo.mix.Beaner;
import com.zoo.mix.CvBridge;
import com.zoo.mix.Cver;
import com.zoo.mix.Dater;
import com.zoo.mix.Facer;
import com.zoo.mix.Filer;
import com.zoo.mix.Http2;
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
//			System.setProperty("java.specification.version", "1.9");//由于jai-imageio-core-1.3.1.jar还未支持jdk1.9所以需要设置此属性，但是jai-imageio-core-1.4.0.jar已经支持jdk1.9了
	 }
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		String result=Http2.get("https://www.baidu.com");
//		System.out.println(result);
		
		String abc="word分词是一个Java实现的中文分词组件，提供了多种基于词典的分词算法，并利用ngram模型来消除歧义。 能准确识别英文、数字，以及日期、时间等数量词，能识别人名、地名、组织机构名等未登录词。 同时提供了Lucene、Solr、ElasticSearch插件。";
		long a=clock.millis();
		
		long size=0L;
		try {
			testFaceRecognition();
//			testFaceRecognitionUseGpuWithJavacv();
//			testCver();
//			testOCR();
//			testBeanCopy();
//			testAvg(1);
//			testImg();
//			testEncode();
//			testCvBridge();
//			cleanRepeatedMusic();
//			testFace();
//			testArrs();
//			testZipCompress();
//			testSplitWord();
//			testNewAPI();
//			watchDrivers();
//			System.out.println(Http2.get("https://www.baidu.com"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		long b=clock.millis();
		System.out.println("used time："+(b-a)+" milliseconds");
	}
	
	
	public static void watchDrivers() {
		Stream.of("C:\\Windows","D:\\CNTV","E:\\media","F:\\FFOutput","G:\\火线时刻").map(Paths::get)
		.forEach(p->System.out.println("Driver:"+p+"->total:"+Filer.total(p)+",unallocated:"+Filer.total(p)+",usable:"+Filer.usable(p)+",used:"+Filer.used(p)));
		
	}
	
	
	public static void testCver() throws TesseractException {
		
		Cver cver=Cver.of("E:\\images\\Tracer.png").scaleW(640);
		Mat mat=cver.get();
		Mat style=Cver.of(cver.get()).contrast(1).get();
		Mat style1=Cver.of(cver.get()).contrast(3).get();
		Mat style2=Cver.of(cver.get()).contrast(0.2).get();
		ImageGUI sourcer=ImageGUI.of().createWin("source");
		ImageGUI styler=ImageGUI.of().createWin("brightness1");
		ImageGUI styler1=ImageGUI.of().createWin("brightness1.2");
		ImageGUI styler2=ImageGUI.of().createWin("brightness0.5");
		
		sourcer.imshow(CvBridge.image(mat));
		styler.imshow(CvBridge.image(style));
		styler1.imshow(CvBridge.image(style1));
		styler2.imshow(CvBridge.image(style2));
		
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
	
	public static void testStack() {
		StackTraceElement[] stackTraceElements=Thread.currentThread().getStackTrace();
		for (StackTraceElement stack : stackTraceElements) {
			System.out.println(stack.getClassName());
			System.out.println(stack.getFileName());
			System.out.println(stack.getMethodName());
		}
	}
	
	
	public static void testFaceRecognition() throws InterruptedException {
		VideoCapture vc=new VideoCapture(0);
//		VideoCapture vc=new VideoCapture("E:\\Crysis3.mp4");
		vc.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
		vc.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
		Mat m=new Mat(480,640,CvType.CV_32FC3);
		Bun<Boolean> booleaner=Bun.of(true);
		
		ImageGUI gui=ImageGUI.of().createWin("摄像头");
		gui.getDialog().addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						booleaner.set(false);
						vc.release();
					}
				});
		
		while (booleaner.get()&&vc.read(m)) {
			m = Cver.of(m).flipY()/* .whiteBalance() *//*.stylizztion()*//*.pencilGray()*//*.pencilColor()*//*.detailEnhance()*//*.edgePreserving()*//*.add(cover)*/.get();
			m=Facer.drawFace(m);
			gui.imshow(CvBridge.image(m));
		}
		if(vc.isOpened()) {
			vc.release();
		}
	}
	
	
	public static void testFaceRecognitionUseGpuWithJavacv() {
		opencv_videoio.VideoCapture vc=new opencv_videoio.VideoCapture(0);
		vc.set(opencv_videoio.CAP_PROP_FRAME_WIDTH, 640);
		vc.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
		GpuMat m=new GpuMat(480,640,opencv_core.CV_32FC3);
		Bun<Boolean> booleaner=Bun.of(true);
		boolean unregistered=true;
		while (booleaner.get()&&vc.retrieve(m)) {
//			m=Cver.of(m).flipY()/*.whiteBalance()*//*.stylizztion()*//*.pencilGray()*//*.pencilColor()*//*.detailEnhance()*/.edgePreserving().get();
//			m=Facer.drawFace(m);
			opencv_core.flip(m, m, 1);//左右翻转
			
			
			opencv_highgui.imshow("FaceRecognition", m);
			int i=opencv_highgui.waitKey(1);
			if (i == 27) {//Press esc to destroy these windows
				opencv_highgui.destroyWindow("FaceRecognition");
				break;
			}
			/*HighGui.imshow("FaceRecognition", m);//构造ImageWindow对象
			int i=HighGui.waitKey(1);//构造JFrame对象
			if (unregistered) {
				HighGui.windows.get("FaceRecognition").frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						booleaner.set(false);
						vc.release();
						e.getWindow().dispose();
					}
				});
				unregistered=false;
			}
			if (i == 27) {//Press esc to destroy these windows
				HighGui.windows.forEach((k,v)->v.frame.dispose());//使用此方法来销毁窗体
				break;
			}*/
		}
		vc.release();
	}
	
	
	
	public static void testOCR() {
		Path path=Paths.get("E:\\codonst.png");
//		Cver.of(path.toString()).threshold(190,255,0).write("E:\\codonst.png");
		Mat mat=Cver.of(path.toString()).get();
		BufferedImage image=CvBridge.image(mat);
		String result=OCR.read(image);
		System.out.println(result);
		
	}
	
	
	public static void testCvBridge() throws IOException {
		Path rotate=Paths.get("E:\\images\\videoRotate.png");
		Path rotateMatToImg=Paths.get("E:\\images\\videoRotateMatToImg.png");
		Path rotateImgToMat=Paths.get("E:\\images\\videoRotateImgToMat.png");
		BufferedImage image=null;
		Mat mat=null;
		
		//mat to image
//		mat=Imgcodecs.imread(rotate.toString(), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
//		image=CvBridge.image(mat);
//		ImageIO.write(image, "png", rotateMatToImg.toFile());
		
		
//		image to mat
//		image=ImageIO.read(rotate.toFile());
//		mat=CvBridge.mat(image);
//		Imgcodecs.imwrite(rotateImgToMat.toString(), mat);
		
		
//		TYPE_INT_RGB
//		TYPE_INT_ARGB
//		TYPE_INT_ARGB_PRE
//		TYPE_INT_BGR
//		TYPE_3BYTE_BGR
//		TYPE_4BYTE_ABGR
//		TYPE_4BYTE_ABGR_PRE
//		TYPE_BYTE_GRAY
//		TYPE_BYTE_BINARY
//		TYPE_BYTE_INDEXED
//		TYPE_USHORT_GRAY
//		TYPE_USHORT_565_RGB
//		TYPE_USHORT_555_RGB
		BufferedImage image2=new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		BufferedImage image3=new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		BufferedImage image4=new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
		BufferedImage image5=new BufferedImage(100, 100, BufferedImage.TYPE_INT_BGR);
		BufferedImage image6=new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage image7=new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		BufferedImage image8=new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR_PRE);
		BufferedImage image9=new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
		BufferedImage image10=new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_BINARY);
		BufferedImage image11=new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_INDEXED);
		BufferedImage image12=new BufferedImage(100, 100, BufferedImage.TYPE_USHORT_GRAY);
		BufferedImage image13=new BufferedImage(100, 100, BufferedImage.TYPE_USHORT_565_RGB);
		BufferedImage image14=new BufferedImage(100, 100, BufferedImage.TYPE_USHORT_555_RGB);
		BufferedImage[] images= {image2,image3,image4,image5,image6,image7,image8,image9,image10,image11,image12,image13,image14};
		Color color=new Color(120, 255, 10, 150);
		int i=1;
		for (BufferedImage img : images) {
			i++;
			Graphics2D g2d=(Graphics2D) img.getGraphics();
			g2d.setBackground(color);
			g2d.clearRect(0, 0, 100, 100);
			g2d.dispose();
//			ImageIO.write(img, "png", new File("E:\\images\\newImg"+i+".png"));
			int rgba=img.getRGB(0, 0);
			int a = (rgba & 0xff000000) >>> 24;
			int r = (rgba & 0xff0000) >> 16;
            int g = (rgba & 0xff00) >> 8;
            int b = (rgba & 0xff);
            System.out.println(i+" rgba:"+rgba+" r:"+r+" g:"+g+" b:"+b+" a:"+a);
            
            Mat matt=CvBridge.mat(img);
            double[] vs=matt.get(0, 0);
            System.out.println(i+"-"+matt.type()+"-abgr or bgr:"+Arrs.join(",", vs));
//    		Imgcodecs.imwrite("E:\\images\\newImg"+i+"_Mat.png", matt);
            
		}
		
		
//		System.out.println(((DataBufferInt)image2.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferInt)image3.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferInt)image4.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferInt)image5.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferByte)image6.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferByte)image7.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferByte)image8.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferByte)image9.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferByte)image10.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferByte)image11.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferUShort)image12.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferUShort)image13.getRaster().getDataBuffer()).getData().length/10000);
//		System.out.println(((DataBufferUShort)image14.getRaster().getDataBuffer()).getData().length/10000);
		
		
		
		
		

//		BufferedImage image=ImageIO.read(Paths.get("E:\\images\\videoRotate.png").toFile());
//		System.out.println(image.getType());
		Mat mat1=Imgcodecs.imread("E:\\images\\videoRotate.png",Imgcodecs.IMREAD_UNCHANGED);
//		System.out.println(mat.depth());
//		System.out.println(mat.type());
		long s=clock.millis();
//		Mat mat=CvBridge.mat(image);
		BufferedImage image1=CvBridge.image(mat1);
		long e=clock.millis();
//		Imgcodecs.imwrite("E:\\images\\videoRotateImgToMat.png", mat);
		ImageIO.write(image1, "png", Paths.get("E:\\images\\videoRotateMatToImg.png").toFile());
		System.out.println(e-s);
	}

	
	
	public static void testEncode() {
		
		String meta="Who are you ?";
		
		RSA rsa=RSA.of();
		byte[] secrets=rsa.encode(meta.getBytes());
		String base64Str=new String(Secret.base64(secrets));
		System.out.println("客户端发送经过BASE64编码的密文:"+base64Str);
		
		byte[] decodeds=rsa.decode(secrets);
		System.out.println("客户端解密:"+new String(decodeds));
		
		System.out.println("======================================");
		
		
		
		
		
		
		
//		String secret=new String(Secret.des("广东省广州市".getBytes(), "123".getBytes()));
//		System.out.println(secret);
//		System.out.println(Secret.unDes(secret, "123"));
		
		
//		Path file=Paths.get("F:\\迅雷下载\\ideaIU-2018.1.exe");
		
		
//		System.out.println(Encrypt.hmacMd5("123", "123"));
//		System.out.println(Encrypt.hmacSha1("123", "123"));
//		System.out.println(Encrypt.hmacSha224("123", "123"));
//		System.out.println(Encrypt.hmacSha256("123", "123"));
//		System.out.println(Encrypt.hmacSha384("123", "123"));
//		System.out.println(Encrypt.hmacSha512("123", "123"));
		
		
		
//		System.out.println("=======================================");
		
//		
//		try(FileChannel in=FileChannel.open(file, StandardOpenOption.READ)) {
//			MappedByteBuffer byteBuffer = in.map(FileChannel.MapMode.READ_ONLY, 0, Files.size(file));
//			MessageDigest md5 = MessageDigest.getInstance("SHA-256");
//			md5.update(byteBuffer);
//            String coded= Encrypt.hex(md5.digest());
//            System.out.println(coded.equals("1FAB7E71016420F01CFBB3B65EF52BA365DCB35724EEE79E5AC97A52ACC60249"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		
	}
	
	
	
	
	public static void cleanRepeat() {
		try {
			Path path=Paths.get("F:\\参考文档\\技巧");
			List<Path> files=Filer.paths(path, p->Filer.isFile(p));
			
			System.out.println(files.size());
			int repeatCount=0;
			Map<String, List<Path>> repeats=new HashMap<String,List<Path>>();
			for (Path f1 : files) {
				String name=f1.getFileName().toString();
				long size=Files.size(f1);
				for (Path f2 : files) {
					String fn=f2.toString();
					String n=f2.getFileName().toString();
					if (!f1.toString().equals(fn) && name.equals(n)) {
						if (size!=Files.size(f2)) {
							System.out.println("size not equals file1:"+f1.toString()+",size:"+size+" file2:"+fn+",size:"+Files.size(f2));
						}
						repeatCount++;
						List<Path> rpts=repeats.get(n);
						if (rpts==null) {
							rpts=new ArrayList<Path>();
						}
						rpts.add(f2);
						repeats.put(name, rpts);
					}
				}
			}
			System.out.println("repeatCount:"+repeatCount);
			for (String n:repeats.keySet()) {
				List<Path> rpts=repeats.get(n);
				Path target=Paths.get("F:\\参考文档\\技巧\\repeat\\"+n);
//				System.out.println(n+"======="+rpts);
				for (Path p : rpts) {
//					Files.move(p, target,StandardCopyOption.REPLACE_EXISTING);
					System.out.println("move:"+p.toString()+" to:"+target);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	
	
	public static void testVideoCombine() throws org.bytedeco.javacv.FrameGrabber.Exception, org.bytedeco.javacv.FrameRecorder.Exception {
		String p="F:\\Download\\VideoData\\1521560713930\\";
		List<Integer> paths=Filer.paths(Paths.get(p),Funcs.onlyFile).stream().filter(ps->Strs.isNumeric(ps.getFileName().toString())).map(ps->Integer.valueOf(ps.getFileName().toString())).sorted().collect(Collectors.toList());
		paths.forEach(System.out::println);
		System.out.println("length:"+paths.size());
		FFmpegFrameGrabber frameGrabber = FFmpegFrameGrabber.createDefault(p+"0");
		frameGrabber.start();
		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("F:\\Download\\record.mp4", frameGrabber.getImageWidth(), frameGrabber.getImageHeight(),frameGrabber.getAudioChannels());
		recorder.setFormat(Videos.mp4);//封装格式
		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);//视频编码格式
		recorder.setVideoBitrate(frameGrabber.getVideoBitrate());
		recorder.setFrameRate(frameGrabber.getFrameRate());//视频帧率
		recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);//音频编码格式
		recorder.setAudioBitrate(frameGrabber.getAudioBitrate());
		recorder.setSampleRate(frameGrabber.getSampleRate());//音频采样率
		recorder.start();
		frameGrabber.close();
		paths.forEach(ps->{
			try (FFmpegFrameGrabber fg=FFmpegFrameGrabber.createDefault(p+ps)){
				fg.start();
				Frame captured_frame =fg.grab();
				while (captured_frame!=null) {
					try {
						recorder.setTimestamp(fg.getTimestamp());
						recorder.record(captured_frame); 
						captured_frame = fg.grab();
					} catch (Exception e) {
					}
				}
			} catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
				e1.printStackTrace();
			}
		});
		recorder.close();
	}
	
	
	public static void testVideoCut() {
		try (FFmpegFrameGrabber frameGrabber=FFmpegFrameGrabber.createDefault("F:\\Download\\input.mp4");){
			frameGrabber.start();
			FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("F:\\Download\\output2.mp4", frameGrabber.getImageWidth(), frameGrabber.getImageHeight(),frameGrabber.getAudioChannels());
			recorder.setFormat(Videos.mp4);//封装格式
//			recorder.setVideoCodec(frameGrabber.getVideoCodec());
			recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);//视频编码格式
			recorder.setVideoBitrate(frameGrabber.getVideoBitrate());
			recorder.setFrameRate(frameGrabber.getFrameRate());//视频帧率
			recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);//音频编码格式
//			recorder.setAudioCodec(frameGrabber.getAudioCodec());
			recorder.setAudioBitrate(frameGrabber.getAudioBitrate());
			recorder.setSampleRate(frameGrabber.getSampleRate());//音频采样率
			recorder.start();
			
			int now=1800;
			long timestamp=Math.round(1000000L * 1800 / frameGrabber.getFrameRate());
//			frameGrabber.setFrameNumber(now);
			Frame captured_frame =null;
			while ((captured_frame =frameGrabber.grab())!=null) {
				try {
					recorder.setTimestamp(frameGrabber.getTimestamp());
//					recorder.setTimestamp(frameGrabber.getTimestamp()-timestamp);
					recorder.record(captured_frame); 
				} catch (Exception e) {
				}
//				now++;
//				frameGrabber.setFrameNumber(now);
			}
			recorder.close();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception|org.bytedeco.javacv.FrameRecorder.Exception e1 ) {
			e1.printStackTrace();
		}
		
	}
	
	public static void testImg() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TFrame frame = new TFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static class TFrame extends JFrame{
		private static final long serialVersionUID = 4567469956698865503L;
		private JPanel contentPane;
		public TFrame() {
			int fw=640,fh=480;
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(0, 0, fw+16, fh+39);
			setLocationRelativeTo(null);
			setAlwaysOnTop(true);
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
//			BufferedImage bimage=Imgs.of("e:\\images\\snow.jpg").cutCenter().scaleRatio(200, 200).get();
//			bimage=CvBridge.image(Cver.of(CvBridge.mat(bimage)).gray().threshold().get());
			BufferedImage qrcode=QRCode.qrCode("https://www.baidu.com?w=唔等", 200, 200,null,Colors.randColor(Chroma.light));
//			BufferedImage qrcode=QRCode.qrCode("https://www.baidu.com?w=唔等", 400);
			JLabel jlLabel=new JLabel(new ImageIcon(qrcode));
			jlLabel.setBounds(0, 0, fw, fh);
			contentPane.add(jlLabel,Integer.valueOf(Integer.MAX_VALUE));
			contentPane.setBackground(Colors.randColor(Chroma.lightest));
		}
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
	
	
	public static void cleanRepeatedMusic() {
		try {
			Path base=Paths.get("F:\\KuGou");
			Path path=Paths.get("G:\\KuGou");
			Path notExists=Paths.get("G:\\NotExists");
			Path notType=Paths.get("G:\\NotType");
			Path notSize=Paths.get("G:\\NotSize");
			
			
			List<Path> baseFiles=Filer.paths(base, p->{
				String s=p.toString().toLowerCase();
				return s.endsWith(".mp3") || s.endsWith(".ape") || s.endsWith(".flac") || s.endsWith(".wma") || s.endsWith(".aac");
			});
			
			List<String> baseNames=baseFiles.parallelStream().map(Path::getFileName).map(Path::toString).collect(Collectors.toList());
			List<String> baseShortNames=baseNames.parallelStream().map(Pather::shortName).collect(Collectors.toList());
			
			
			System.out.println(baseFiles.get(0));
			System.out.println(baseNames.get(0));
			System.out.println(baseShortNames.get(0));
			
			List<Path> files=Filer.paths(path, Funcs.onlyFile);
			System.out.println(files.size());
			
			for (Path f1 : files) {
				
				String name1=f1.getFileName().toString();
				String shortName1=Pather.shortName(name1);
				long size1=Files.size(f1);
				
				if (baseNames.contains(name1)) {//文件同名
					int i=baseNames.indexOf(name1);
					if (size1!=Files.size(baseFiles.get(i))) {//文件大小不等(代表音质不同)
						Files.move(f1, Paths.get(notSize.toString()+"\\"+name1),StandardCopyOption.REPLACE_EXISTING);
					}
				}else if(baseShortNames.contains(shortName1)) {//文件类型不同
					Files.move(f1, Paths.get(notType.toString()+"\\"+name1),StandardCopyOption.REPLACE_EXISTING);
				}else {//不存在
					Files.move(f1, Paths.get(notExists.toString()+"\\"+name1),StandardCopyOption.REPLACE_EXISTING);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
}
