package com.zoo.test;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.zoo.cons.Imagec;
import com.zoo.util.ArrayUtil;
import com.zoo.util.Bean;
import com.zoo.util.Chars;
import com.zoo.util.Chroma;
import com.zoo.util.Colors;
import com.zoo.util.Dates;
import com.zoo.util.Images;
import com.zoo.util.QRCode;
import com.zoo.util.Systems;

import net.sf.cglib.beans.BeanCopier;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class TestFrame{
	 static Clock clock=Clock.systemUTC();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		testBeanCopy();
//		testAvg(1);
		testImg();
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
			Bean.copy(kvf.get(i), kvt1.get(i));
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
		System.out.println(ArrayUtil.join(",", ArrayUtil.distinct(strings)));
		System.out.println(ArrayUtil.join(",", ArrayUtil.distinct(doubles)));
		System.out.println(ArrayUtil.avg(doubles));
		System.out.println(Dates.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss.SHSS"));
		for(int j=0;j<times;j++){
			s=System.currentTimeMillis();
			for(int i=0;i<10000;i++){
				ArrayUtil.max(cc);
			}
			e=System.currentTimeMillis();
			ss[j]=e-s;
		}
		System.out.println("duration avg:"+ArrayUtil.avg(ss));
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
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(0, 0, 1200, 1000);
			setLocationRelativeTo(null);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(null);
			setContentPane(contentPane);
			BufferedImage qrcode=QRCode.qrCode("https://www.baidu.com", 200, 200,new Color(0,129,211,255),new Color(174,244,235));
			try {
				BufferedImage imageA=ImageIO.read(new File("E:\\130922.jpg"));
				BufferedImage imageB=Images.scaleRatio(imageA, 40, 40);
				BufferedImage imageC=ImageIO.read(new File("E:\\星北.jpg"));
				System.out.println(System.currentTimeMillis());
				//Images.pileCenter(qrcode, Images.borderCrimp(Images.circle(imageB),1,null));
				Images.pileCenter(qrcode, Images.borderCrimpRadius(Images.scaleWidth(Images.cutBehind(imageC),40),1,Color.white,40));
				System.out.println(System.currentTimeMillis());
				ImageIO.write(Systems.screenCatpure(), Imagec.png, new File("E:\\capture.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			JLabel jlLabel=new JLabel(new ImageIcon(qrcode));
			//JLabel jlLabel=new JLabel(new ImageIcon(image1));
			jlLabel.setBounds(0, 0, 1200, 1000);
			contentPane.add(jlLabel,new Integer(Integer.MAX_VALUE));
			contentPane.setBackground(Colors.randColor(Chroma.lightest));
			setVisible(true);
		}
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