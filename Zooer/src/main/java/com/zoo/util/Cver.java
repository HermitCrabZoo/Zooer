package com.zoo.util;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class Cver {

	private Cver() {}
	
	private Cver(Mat mat) {
		this.mat=mat;
	}
	
	private Mat mat;
	
	private boolean written=false;
	
	private static final Size defKernelKsize=new Size(3,3);
	
	private static final Size defBlurKsize=new Size(9,9);
	
	/**
	 * 构造一个未关联mat属性的Cver对象，后续对mat的操作前必须先关联当前对象的mat属性到一个已存在的Mat实例。
	 * @return
	 */
	public static Cver ofNull() {
		return new Cver();
	}
	
	/**
	 * 根据给定的Mat对象的拷贝构造一个Cver对象,即后续操作将不改变传入的原始Mat对象
	 * @param mat
	 * @throws NullPointerException 如果mat参数为null，则抛出此异常
	 * @return
	 */
	public static Cver of(Mat mat) {
		assertNull(mat);
		return new Cver(mat.clone());
	}
	
	/**
	 * 根据给定的Mat对象的拷贝构造一个Cver对象,即后续操作将不改变传入的原始Mat对象
	 * @param mat
	 * @throws NullPointerException 如果mat参数为null，则抛出此异常
	 * @return
	 */
	public static Cver of(String fileName) {
		return new Cver(Imgcodecs.imread(fileName));
	}
	
	/**
	 * 用传入的Mat的拷贝来绑定当前的对象，后续操作不会改变传入的Mat对象
	 * @param mat
	 * @throws NullPointerException 如果mat参数为null，则抛出此异常
	 * @return
	 */
	public Cver set(Mat mat) {
		assertNull(mat);
		this.mat=mat.clone();
		return this;
	}
	
	/**
	 * 将当前关联的对象输出到入参指向的文件
	 * @param fileName
	 * @return
	 */
	public Cver write(String fileName) {
		written=Imgcodecs.imwrite(fileName, mat);
		return this;
	}
	
	/**
	 * 获取上一次将当前关联的Mat输出到某些介质中是否成功
	 * @return
	 */
	public boolean written() {
		return written;
	}
	
	/**
	 * 判断当前关联的Mat对象是否是null,是就返回true,反之返回true
	 * @return
	 */
	public boolean empty() {
		return mat==null;
	}
	
	/**
	 * 释放当前关联的Mat对象
	 * @return
	 */
	public Cver release() {
		mat=null;
		written=false;
		return this;
	}

	/**
	 * 如果mat为null将抛异常
	 * @param mat
	 */
	private static void assertNull(Mat mat) {
		if (mat==null) {
			throw new NullPointerException("Argument mat cant not be null!");
		}
	}
	
	/**
	 * 按x轴垂直翻转图片
	 * @return
	 */
	public Cver flipX() {
		return flip(0);
	}
	
	/**
	 * 按y轴水平翻转图片
	 * @return
	 */
	public Cver flipY() {
		return flip(1);
	}
	
	/**
	 * x、y轴分别翻转图片(180度旋转)
	 * @return
	 */
	public Cver flipXY() {
		return flip(-1);
	}
	
	/**
	 * 图片翻转
	 * @param flipCode 为0、1、-1分别代表垂直翻转(x轴)、水平翻转(y轴)、垂直水平都翻转
	 * @return
	 */
	private Cver flip(int flipCode) {
		Mat dst=new Mat(mat.size(), mat.type());
		Core.flip(mat, dst, flipCode);
		mat=dst;
		return this;
	}
	
	/**
	 * 转成灰度图
	 * @return
	 */
	public Cver gray() {
		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
		return this;
	}
	
	/**
	 * 二值化,默认阈值:127,类型:{@link Imgproc#THRESH_BINARY}
	 * @return
	 */
	public Cver threshold() {
		return threshold(127, 255, Imgproc.THRESH_BINARY);
	}
	
	/**
	 * 二值化
	 * @param thresh 阈值
	 * @param maxval 最大值
	 * @param type 取值(0,1),当0时大于thresh的值设置为maxval,当1时小于thresh的值设置为maxval
	 * @return
	 */
	public Cver threshold(double thresh, double maxval, int type) {
		Imgproc.threshold(mat, mat, thresh, maxval, type);
		return this;
	}
	
	/**
	 * 自动白平衡
	 * @return
	 */
	public Cver whiteBalance() {
		List<Mat> channels=new ArrayList<>();
		//分离颜色通道
		Core.split(mat, channels);
		Mat cb=channels.get(0);
		Mat cg=channels.get(1);
		Mat cr=channels.get(2);
		
		//对通道求平均值
		double ab=Core.mean(cb).val[0];
		double ag=Core.mean(cg).val[0];
		double ar=Core.mean(cr).val[0];
		
		//计算通道的分量增益
		double sum=ab+ag+ar;
		double b=sum/(3*ab);
		double g=sum/(3*ag);
		double r=sum/(3*ar);
		
		//调整各通道值
		Core.addWeighted(cb, b, cb, 0, 0, cb);
		Core.addWeighted(cg, g, cg, 0, 0, cg);
		Core.addWeighted(cr, r, cr, 0, 0, cr);
		
		//合成
		Core.merge(channels, mat);
		return this;
	}
	
	/**
	 * 膨胀,默认kernel:{@link Cver#defKernelKsize} 3x3,anchor:(-1,-1),iterations:1
	 * @return
	 */
	public Cver dilate() {
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, defKernelKsize);
		Imgproc.dilate(mat, mat, kernel);
		return this;
	}
	
	/**
	 * 膨胀
	 * @param kernel 卷织的核
	 * @param anchor 锚点
	 * @param iterations 腐蚀次数
	 * @return
	 */
	public Cver dilate(Mat kernel,Point anchor,int iterations) {
		Imgproc.dilate(mat, mat, kernel, anchor, iterations);
		return this;
	}
	
	/**
	 * 腐蚀,默认kernel:{@link Cver#defKernelKsize} 3x3,anchor:(-1,-1),iterations:1
	 * @return
	 */
	public Cver erode() {
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, defKernelKsize);
		Imgproc.erode(mat, mat, kernel);
		return this;
	}
	
	/**
	 * 腐蚀
	 * @param kernel 卷织的核
	 * @param anchor 锚点
	 * @param iterations 腐蚀次数
	 * @return
	 */
	public Cver erode(Mat kernel,Point anchor,int iterations) {
		Imgproc.erode(mat, mat, kernel, anchor, iterations);
		return this;
	}
	
	/**
	 * 均值滤波:利用某像素点周边的像素的平均值来达到平滑噪声的目的,此方法不能很好的保护图像的细节，在去噪的同时会破坏图像的细节部分，不能很好的去除噪点
	 * @return
	 */
	public Cver blur() {
		Imgproc.blur(mat, mat, defBlurKsize);
		return this;
	}
	
	/**
	 * 均值滤波
	 * @param ksize 内核的大小
	 * @param anchor 锚点，默认值(-1,-1)，代表核的中心 
	 * @param borderType 推断图像外部像素的边界模式<br/>
	 * 默认值{@link Core#BORDER_DEFAULT}<br/>
	 * 可选<br/>
	 * {@link Core#BORDER_REPLICATE}：复制法，既是复制最边缘像素，例如aaa|abc|ccc <br/>
	 * {@link Core#BORDER_REFLECT}：对称法，例如cba|abc|cba <br/>
	 * {@link Core#BORDER_REFLECT_101}：对称法，最边缘像素不会被复制，例如cb|abc|ba <br/>
	 * {@link Core#BORDER_CONSTANT}：常量法，默认为0 <br/>
	 * {@link Core#BORDER_WRAP}：镜像对称复制<br/>
	 * @return
	 */
	public Cver blur(Size ksize, Point anchor, int borderType) {
		Imgproc.blur(mat, mat, ksize, anchor, borderType);
		return this;
	}
	
	/**
	 * 高斯滤波,默认ksize:{@link Cver#defBlurKsize},sigmaX:0,borderType:{@link Core#BORDER_DEFAULT}
	 * @return
	 */
	public Cver gaussianBlur() {
		Imgproc.GaussianBlur(mat, mat, defBlurKsize, 0);
		return this;
	}
	
	/**
	 * 高斯滤波
	 * @param ksize 内核模板大小,用模板扫描图像中的每一个像素,用模板确定的邻域内像素的加权平均值去替代模板中心像素点的值
	 * @param sigmaX 高斯内核在X 方向的标准偏差
	 * @param sigmaY 高斯内核在Y方向的标准偏差。 如果sigmaY为0，他将和sigmaX的值相同，如果他们都为0，那么他们由ksize.width和ksize.height计算得出
	 * @param borderType 用于判断图像边界的模式({@link Core#BORDER_DEFAULT}...)
	 * @return
	 */
	public Cver gaussianBlur(Size ksize, double sigmaX, double sigmaY, int borderType) {
		Imgproc.GaussianBlur(mat, mat, ksize, sigmaX, sigmaY, borderType);
		return this;
	}
	
	/**
	 * 中值滤波:非线性的滤波技术，将每一像素点的值设置为该点邻域窗口内所有像素点灰度值得中值。能有效的消除椒盐噪声（椒盐噪声是由图像传感器，传输信道，解码处理等产生的黑白相间的亮暗点噪声<br/>
	 * 默认ksize:7
	 * @return
	 * @see Cver#medianBlur(int)
	 */
	public Cver medianBlur() {
		return medianBlur(7);
	}
	
	/**
	 * 中值滤波
	 * @param ksize 内核大小
	 * @return
	 */
	public Cver medianBlur(int ksize) {
		Imgproc.medianBlur(mat, mat, ksize);
		return this;
	}
}
