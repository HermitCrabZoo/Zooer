package com.zoo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class Cver {

	private Cver() {}
	
	private Cver(Mat mat) {
		update(mat);
	}
	
	private Mat mat;
	
	private boolean written=false;
	
	private static final Size defKernelKsize=new Size(3,3);
	
	private static final Size defBlurKsize=new Size(9,9);
	
	/**
	 * 边缘检测结果
	 */
	private Mat edge;
	
	/**
	 * 圆检测结果
	 */
	private Mat circle;
	
	
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
		update(mat.clone());
		return this;
	}
	
	/**
	 * 返回当前对象关联的Mat实例
	 * @return
	 */
	public Mat get() {
		return mat;
	}
	
	/**
     * Return the mat if present, otherwise return {@code other}.
     *
     * @param other the mat to be returned if there is no value present, may
     * be null
     * @return the mat, if present, otherwise {@code other}
     */
    public Mat orElse(Mat other) {
        return mat != null ? mat : other;
    }
    
	/**
     * Return the mat if present, otherwise invoke {@code other} and return
     * the result of that invocation.
     *
     * @param other a {@code Supplier} whose result is returned if no mat
     * is present
     * @return the mat if present otherwise the result of {@code other.get()}
     * @throws NullPointerException if mat is not present and {@code other} is
     * null
     */
	public Mat orElseGet(Supplier<? extends Mat> other) {
		return mat!=null?mat:other.get();
	}
	
	/**
	 * 返回直线检测结果的Mat对象
	 * @return
	 */
	public Mat edge() {
		return edge;
	}
	
	/**
	 * 返回圆检测结果的Mat对象
	 * @return
	 */
	public Mat circle() {
		return circle;
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
	 * 释放当前关联的Mat对象和一系列操作过程中产生的各种Mat结果对象
	 * @return
	 */
	public Cver release() {
		mat=null;
		written=false;
		edge=null;
		circle=null;
		return this;
	}
	
	/**
	 * 使用原关联的Mat对象构造新的具有相同尺寸和类型的Mat对象
	 * @return
	 */
	private Mat newMat() {
		return new Mat(mat.size(), mat.type());
	}
	
	/**
	 * 使用传入参数更新当前关联的mat对象
	 * @param mat
	 * @return
	 */
	private Cver update(Mat mat) {
		this.mat=mat;
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
		Mat dst=newMat();
		Core.flip(mat, dst, flipCode);
		update(dst);
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
	 * @param borderType 用于判断图像边界的模式,默认{@link Core#BORDER_DEFAULT}<br/>
	 * 可选<br/>
	 * {@link Core#BORDER_REPLICATE}：复制法，既是复制最边缘像素，例如aaa|abc|ccc <br/>
	 * {@link Core#BORDER_REFLECT}：对称法，例如cba|abc|cba <br/>
	 * {@link Core#BORDER_REFLECT_101}：对称法，最边缘像素不会被复制，例如cb|abc|ba <br/>
	 * {@link Core#BORDER_CONSTANT}：常量法，默认为0 <br/>
	 * {@link Core#BORDER_WRAP}：镜像对称复制<br/>
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
	
	
	/**
	 * 直方图均衡化:通过拉伸像素强度分布范围来增强图像对比度
	 * @return
	 */
	public Cver equalizeHist() {
		List<Mat> mvs = new ArrayList<Mat>();
        Core.split(mat, mvs);
        for (int i = 0; i < mat.channels(); i++){
            Imgproc.equalizeHist(mvs.get(i), mvs.get(i));
        }
        Core.merge(mvs, mat);
        return this;
	}
	
	
	/**
	 * Sobel算子进行边缘检测，并且将检测结果替换当前关联的mat对象
	 * @return
	 * @see #sobel()
	 */
	public Cver sobelNow() {
		return sobel().update(edge);
	}
	
	/**
	 * Sobel算子进行边缘检测，并且将检测结果替换当前关联的mat对象
	 * @param ddpeth
	 * @param ksize
	 * @param scale
	 * @param delta
	 * @param borderType
	 * @return
	 * @see #sobel(int, int, double, int, int)
	 */
	public Cver sobelNow(int ddpeth,int ksize,double scale,int delta,int borderType) {
		return sobel(ddpeth, ksize, scale, delta, borderType).update(edge);
	}
	
	/**
	 * Sobel算子:主要是应用于边缘检测的一个离散的一阶差分算子，用来计算图像亮度函数的一阶梯度的近似值<br>
	 * 默认ddpeth:{@link CvType#CV_16S},ksize:3,scale:1,delta:0,borderType:{@link Core#BORDER_DEFAULT}
	 * @return
	 * @see #sobel(int, int, double, int, int)
	 */
	public Cver sobel() {
		return sobel(CvType.CV_16S,3,1,0,Core.BORDER_DEFAULT);
	}
	
	/**
	 * Sobel算子
	 * @param ddpeth 输出图像深度
	 * @param ksize 核的大小，默认为3
	 * @param scale 缩放因子
	 * @param delta 结果存入输出图像前可选的delta值，默认为0 
	 * @param borderType 边界模式，默认{@link Core#BORDER_DEFAULT}<br/>
	 * 可选<br/>
	 * {@link Core#BORDER_REPLICATE}：复制法，既是复制最边缘像素，例如aaa|abc|ccc <br/>
	 * {@link Core#BORDER_REFLECT}：对称法，例如cba|abc|cba <br/>
	 * {@link Core#BORDER_REFLECT_101}：对称法，最边缘像素不会被复制，例如cb|abc|ba <br/>
	 * {@link Core#BORDER_CONSTANT}：常量法，默认为0 <br/>
	 * {@link Core#BORDER_WRAP}：镜像对称复制<br/>
	 * @return 
	 */
	public Cver sobel(int ddpeth,int ksize,double scale,int delta,int borderType) {
        Mat dst_x = new Mat();
        Mat dst_y = new Mat();
        Mat abs_dst_x = new Mat();
        Mat abs_dst_y = new Mat();
        //计算x、y方向的梯度
        Imgproc.Sobel(mat, dst_x, ddpeth, 1, 0, ksize, scale, delta, borderType);
        Imgproc.Sobel(mat, dst_y, ddpeth, 0, 1, ksize,scale, delta, borderType);
        //计算x、y方向的梯度绝对值
        Core.convertScaleAbs(dst_x, abs_dst_x);
        Core.convertScaleAbs(dst_y, abs_dst_y);
        //计算结果梯度
        edge=newMat();
        Core.addWeighted(abs_dst_x, 0.5, abs_dst_y, 0.5, 0, edge);
        return this;
	}
	
	/**
	 * Canny进行多级边缘检测,并且将检测结果替换当前关联的mat对象
	 * @return
	 * @see #canny()
	 */
	public Cver cannyNow() {
		return canny().update(edge);
	}
	
	/**
	 * Canny进行多级边缘检测,并且将检测结果替换当前关联的mat对象
	 * @param threshold1
	 * @param threshold2
	 * @param apertureSize
	 * @param l2gradient
	 * @return
	 * @see #canny(double, double, int, boolean)
	 */
	public Cver cannyNow(double threshold1,double threshold2,int apertureSize,boolean l2gradient) {
		return canny(threshold1, threshold2, apertureSize, l2gradient).update(edge);
	}
	
	
	/**
	 * Canny多级边缘检测算法<br/>
	 * 默认值threshold1:40,threshold2:100,apertureSize:3,l2gradient:false
	 * @return
	 * @see #canny(double, double, int, boolean)
	 */
	public Cver canny() {
		return canny(40, 100, 3, false);
	}
	
	/**
	 * Canny多级边缘检测算法<br/>
	 * 滞后阈值（高阈值和低阈值），若某一像素位置的幅值超过高阈值，该像素被保留为边缘像素；若小于低阈值，则被排除；若在两者之间，该像素仅在连接到高阈值像素时被保留。推荐高低阈值比在2:1和3:1之间
	 * @param threshold1 低阀值
	 * @param threshold2 高阀值
	 * @param apertureSize 算子模板大小
	 * @param L2gradient 计算图像梯度幅值的标识,梯度幅值指沿某方向的方向导数最大的值，即梯度的模
	 * @return
	 */
	public Cver canny(double threshold1,double threshold2,int apertureSize,boolean l2gradient) {
		edge=newMat();
		Imgproc.Canny(mat, edge, threshold1, threshold2, apertureSize, l2gradient);
		return this;
	}
	
	/**
	 * 拉普拉斯算子边缘检测,并且将检测结果替换当前关联的mat对象
	 * @return
	 * @see #laplacian()
	 */
	public Cver laplacianNow() {
		return laplacian().update(edge);
	}
	
	/**
	 * 拉普拉斯算子边缘检测,并且将检测结果替换当前关联的mat对象
	 * @param ddepth
	 * @param ksize
	 * @param scale
	 * @param delta
	 * @param borderType
	 * @return
	 * @see #laplacian(int, int, double, double, int)
	 */
	public Cver laplacianNow(int ddepth, int ksize, double scale, double delta, int borderType) {
		return laplacian(ddepth, ksize, scale, delta, borderType).update(edge);
	}
	
	/**
	 * 拉普拉斯算子边缘检测<br/>
	 * 默认值ddepth:-1,ksize:3,scale:1,delta:0,borderType:{@link Core#BORDER_DEFAULT}
	 * @return
	 * @see #laplacian(int, int, double, double, int)
	 */
	public Cver laplacian() {
		return laplacian(-1, 3, 1, 0, Core.BORDER_DEFAULT);
	}
	
	/**
	 * 拉普拉斯算子边缘检测:是n维欧几里德空间中的一个二阶微分算子，定义为梯度（▽f）的散度（▽·f）,因此如果f是二阶可微的实函数.
	 * @param ddepth 目标图像的深度 
	 * @param ksize 计算二阶导数的滤波器的孔径大小，必须为正奇数，默认为1 
	 * @param scale 计算Laplacian的时候可选的比例因子，默认为1 
	 * @param delta 结果存入目标图之前可选的detla值，默认为0 
	 * @param borderType 边界模式，默认{@link Core#BORDER_DEFAULT}<br/>
	 * 可选<br/>
	 * {@link Core#BORDER_REPLICATE}：复制法，既是复制最边缘像素，例如aaa|abc|ccc <br/>
	 * {@link Core#BORDER_REFLECT}：对称法，例如cba|abc|cba <br/>
	 * {@link Core#BORDER_REFLECT_101}：对称法，最边缘像素不会被复制，例如cb|abc|ba <br/>
	 * {@link Core#BORDER_CONSTANT}：常量法，默认为0 <br/>
	 * {@link Core#BORDER_WRAP}：镜像对称复制<br/>
	 * @return
	 */
	public Cver laplacian(int ddepth, int ksize, double scale, double delta, int borderType) {
		Imgproc.Laplacian(mat, edge, ddepth, ksize, scale, delta, borderType);
		return this;
	}
	
	/**
	 * Scharr滤波器进行边缘检测,并且将检测结果替换当前关联的mat对象
	 * @return
	 * @see #scharr()
	 */
	public Cver scharrNow() {
		return scharr().update(edge);
	}
	
	/**
	 * Scharr滤波器进行边缘检测,并且将检测结果替换当前关联的mat对象
	 * @param ddepth
	 * @param scale
	 * @param delta
	 * @param borderType
	 * @return
	 * @see #scharr(int, double, double, int)
	 */
	public Cver scharrNow(int ddepth, double scale, double delta, int borderType) {
		return scharr(ddepth, scale, delta, borderType).update(edge);
	}
	
	
	/**
	 * Scharr滤波器进行边缘检测<br/>
	 * 默认值 ddepth:-1,scale:1,delta:0,borderType:{@link Core#BORDER_DEFAULT}
	 * @return
	 * @see #scharr(int, double, double, int)
	 */
	public Cver scharr() {
		return scharr(-1, 1, 0, Core.BORDER_DEFAULT);
	}
	
	/**
	 * Scharr滤波器进行边缘检测,是配合Sobel算子的运算而存在的。当Sobel内核为3时，结果可能会产生比较明显的误差，针对这一问题，提供了scharr函数。该函数只针对大小为3的核，并且运算速率和Sobel函数一样快，结果更加精确，但抗噪性不如Sobel函数。 
	 * @param ddepth 输出图像的深度 
	 * @param scale 缩放因子 
	 * @param delta 结果存入输出图像前可选的delta值
	 * @param borderType 边界模式，默认{@link Core#BORDER_DEFAULT}<br/>
	 * 可选<br/>
	 * {@link Core#BORDER_REPLICATE}：复制法，既是复制最边缘像素，例如aaa|abc|ccc <br/>
	 * {@link Core#BORDER_REFLECT}：对称法，例如cba|abc|cba <br/>
	 * {@link Core#BORDER_REFLECT_101}：对称法，最边缘像素不会被复制，例如cb|abc|ba <br/>
	 * {@link Core#BORDER_CONSTANT}：常量法，默认为0 <br/>
	 * {@link Core#BORDER_WRAP}：镜像对称复制<br/>
	 * @return
	 */
	public Cver scharr(int ddepth, double scale, double delta, int borderType) {
		Mat dst = newMat();
        Mat dstx = newMat();
        Mat dsty = newMat();
        //计算x、y方向的梯度
        Imgproc.Scharr(dst, dstx, ddepth, 1, 0, scale, delta, borderType);
        Imgproc.Scharr(dst, dsty, ddepth, 0, 1, scale, delta, borderType);
        edge=newMat();
        Core.addWeighted(dstx, 0.5, dsty, 0.5, 0, edge);
		return this;
	}
	
	
	
	
}
