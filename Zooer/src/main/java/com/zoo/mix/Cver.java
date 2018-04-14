package com.zoo.mix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;



public class Cver {

	private Cver() {}
	
	static {CvBridge.loadOpenCv();}
	
	private Cver(Mat mat) {
		update(mat);
	}
	
	private Mat mat;
	
	private boolean written=false;
	
	private static final Size defKernelKsize=new Size(3,3);
	
	private static final Size defBlurKsize=new Size(9,9);
	
	private static final Scalar scalar=new Scalar(0, 0, 255);
	
	private Scalar color=scalar;
	
	/**
	 * 边缘检测结果
	 */
	private Mat edge;
	
	/**
	 * 圆检测结果
	 */
	private Mat circle;
	
	/**
	 * 标准Hough直线检测结果
	 */
	private Mat line;
	
	/**
	 * 累计概率Hough直线检测结果
	 */
	private Mat lineP;
	
	/**
	 * 模板匹配结果
	 */
	private Mat matched;
	
	/**
	 * 通过模板匹配结果计算出来坐标信息,该对象代表矩形的定位,maxLoc属性为左上角,minLoc属性为右下角
	 */
	private MinMaxLocResult minMaxLocResult;
	
	
	/**
	 * 直方图计算结果
	 */
	private List<Mat> histograms=new ArrayList<>();
	
	
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
		return new Cver(Imgcodecs.imread(fileName,Imgcodecs.CV_LOAD_IMAGE_UNCHANGED));
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
	 * 获取当前对象的画笔颜色
	 * @return
	 */
	public Scalar color() {
		return this.color;
	}
	
	/**
	 * 设置当前对象的画笔颜色
	 * @param color
	 * @return
	 */
	public Cver color(Scalar color) {
		this.color=color==null?scalar:color;
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
	 * 返回标准Hough直线检测结果的Mat对象
	 * @return
	 */
	public Mat line() {
		return line;
	}
	
	/**
	 * 返回累计概率Hough直线检测结果的Mat对象
	 * @return
	 */
	public Mat lineP() {
		return lineP;
	}
	
	/**
	 * 返回模板匹配结果对象(建议使用{@link #minMaxLocResult()}函数直接获取匹配结果的坐标信息)
	 * @return
	 */
	public Mat matched() {
		return matched;
	}
	
	/**
	 * 获取通过模板匹配结果计算出来坐标信息,返回MinMaxLocResult的对象,该对象代表矩形的定位,maxLoc属性为左上角坐标,minLoc属性为右下角坐标
	 * @return
	 */
	public MinMaxLocResult minMaxLocResult() {
		return minMaxLocResult;
	}
	
	/**
	 * 计算直方图得到的结果
	 * @return
	 */
	public List<Mat> histograms() {
		return histograms;
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
	 * 做某些额外的操作,参数freedom的accept方法的第一个参数为当前绑定的Mat对象,第二个参数为当前的Cver对象
	 * @param freedom
	 * @return
	 */
	public Cver free(BiConsumer<Mat, Cver> freedom) {
		freedom.accept(mat, this);
		return this;
	}
	
	/**
	 * 将宽高按ratio倍比例缩放
	 * @param ratio 输出图片宽高与原图片宽高的比值
	 * @return
	 * @see #scale(int, int)
	 */
	public Cver scale(double ratio) {
		int w=(int) Math.round(mat.width()*ratio),h=(int) Math.round(mat.height()*ratio);
		return scale(w, h);
	}
	
	
	/**
	 * 按宽缩放,输出图片宽为w,高按w与原始比例计算而定,输出图片不变形.
	 * @param w 输出图片的宽
	 * @return
	 * @see #scale(int, int)
	 */
	public Cver scaleW(int w){
		int h=(int) Math.round(w*1.0/mat.width()*mat.height());
		return scale(w, h);
	}
	
	
	/**
	 * 按高缩放,输出图片高为h,宽按h与原始比例计算而定,输出图片不变形.
	 * @param h 输出图片高
	 * @return
	 * @see #scale(int, int)
	 */
	public Cver scaleH(int h){
		int w=(int) Math.round(h*1.0/mat.height()*mat.width());
		return scale(w, h);
	}
	
	
	/**
	 * 图片将按原始比例缩放到刚好可以放入宽为w高为h的容器中的大小(输出图片不变形).<br>
	 * 若图片无法缩放至能放到宽为w高为h的容器中,那么将不缩放
	 * @param w 容器宽
	 * @param h 容器高
	 * @return 输出图片宽高无法保证与参数w、h相等,但至少有一边(宽或高)与输入参数相等。
	 */
	public Cver scaleBox(int w, int h) {
		int[] size=boxSize(w, h, mat.width(), mat.height());
		return scale(size[0], size[1]);
	}
	
	
	
	/**
	 * 按输入宽(w)高(h)参数缩放,输出mat的rows==h,cols==w,若输入的宽高与原宽高比例不一致，那么输出的图片可能将会变形.<br/>
	 * 若输入参数w、h至少有一个小于等于0,那么将不做任何操作.
	 * @param w 缩放后的宽(cols)
	 * @param h 缩放后的高(rows)
	 * @return
	 */
	public Cver scale(int w, int h){
		if (w>0&&h>0) {
			Imgproc.resize(mat, mat, new Size(w,h));
		}
		return this;
	}
	
	
	/**
	 * 计算宽高为width、height的原图在保持宽高比的情况下，缩放到刚好可以放入宽高为boxW、boxH的盒子中的宽高值.
	 * @param boxW 盒子宽
	 * @param boxH 盒子高
	 * @param width 原图宽
	 * @param height 原图高
	 * @return 新的宽高值数组:第一个元素为新宽度,第二个元素为新高度
	 */
	private int[] boxSize(int boxW,int boxH,int width,int height) {
		int[] size=new int[2];
		double ratioX=boxW*1.0/width;
		double ratioY=boxH*1.0/height;
		if (ratioX<ratioY) {
			boxH=(int) Math.round(ratioX*height);
		}else {
			boxW=(int) Math.round(ratioY*width);
		}
		size[0]=boxW;
		size[1]=boxH;
		return size;
	}
	
	
	
	/**
	 * 图片按angle度旋转,旋转后图片尺寸不变。
	 * @param angle 旋转的角度，大于0图片按顺时针旋转,小于0图片按逆时针旋转
	 * @return
	 */
	public Cver rotateDrop(double angle) {
		angle=-angle;
		int width=mat.width(),height=mat.height();
		int[] size=rotateSize(angle, width, height);
		int new_w=size[0],new_h=size[1];
		
		Mat m=getM(angle,boxSize(width, height, new_w, new_h)[0]*1.0/new_w, 0.0,0.0);
		Imgproc.warpAffine(mat, mat, m, mat.size(),Imgproc.WARP_FILL_OUTLIERS,Core.BORDER_CONSTANT,CvBridge.bTransparent);
		
		return this;
	}
	
	
	/**
	 * 图片按angle度旋转,旋转后图片尺寸刚好容下原来图片在旋转后所需占用的矩形空间，既旋转后的图片尺寸可能发生改变。
	 * @param angle 旋转的角度，大于0图片按顺时针旋转,小于0图片按逆时针旋转
	 * @return
	 */
	public Cver rotateRise(double angle) {
		angle=-angle;
		int width=mat.width(),height=mat.height();
		int[] size=rotateSize(angle, width, height);
		int new_w=size[0],new_h=size[1];
		
		Mat dst=new Mat(new_h, new_w, mat.type());
		Mat m=getM(angle,1.0, (new_w-width)/2.0,(new_h-height)/2.0);
		Imgproc.warpAffine(mat, dst, m, dst.size(),Imgproc.WARP_FILL_OUTLIERS,Core.BORDER_CONSTANT,CvBridge.bTransparent);
		
		return update(dst);
	}
	
	/**
	 * 图片按angle度旋转,旋转后图片内容超出的部分可能会被裁减，旋转后图片尺寸不变
	 * @param angle 旋转的角度，大于0图片按顺时针旋转,小于0图片按逆时针旋转
	 * @return
	 */
	public Cver rotate(double angle) {
		Mat m=getM(angle,1.0, 0.0, 0.0);
		Imgproc.warpAffine(mat, mat, m, mat.size(),Imgproc.WARP_INVERSE_MAP,Core.BORDER_CONSTANT,CvBridge.bTransparent);
		return this;
	}
	
	
	/**
	 * 计算图片在旋转后至少需要占用的宽高值.
	 * @param angle 旋转角
	 * @param width 原图宽
	 * @param height 原图高
	 * @return 返回旋转后至少占用的宽高值数组:第一个元素为新宽度,第二个元素为新高度
	 */
	private int[] rotateSize(double angle,int width,int height) {
		int[] nsize=new int[2];
		double radians=Math.toRadians(angle);
		double cos=Math.cos(radians);
		double sin=Math.sin(radians);
		nsize[0] = (int) (Math.abs(width*cos) + Math.abs(height*sin));
		nsize[1] = (int) (Math.abs(height*cos) + Math.abs(width*sin));
		return nsize; 
	}
	
	
	/**
	 * 获取仿射变换的矩阵
	 * @param angle
	 * @param x
	 * @param y
	 * @return
	 */
	private Mat getM(double angle, double scale, double x,double y) {
		Point center=new Point(mat.width()/2.0, mat.height()/2.0);
		Mat m=Imgproc.getRotationMatrix2D(center, angle, scale);
		if (x!=0) {
			m.put(0, 2, m.get(0,2)[0]+=x);
		}
		if (y!=0) {
			m.put(1, 2, m.get(1,2)[0]+=y);
		}
		return m;
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
		Core.flip(mat, mat, flipCode);
		return this;
	}
	
	
	/**
	 * 图片透明化处理,若当前关联的mat对象无透通道，则将被转为BGRA通道图(类型为:{@link CvType#CV_32SC4})，并将透明度设置为alpha值
	 * @param alpha 该值范围必须在[0.0,1.0]
	 * @return
	 */
	public Cver transparency(double alpha) {
		if (mat.channels()==1) {
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2BGRA);
		}else if (mat.channels()==3) {
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2BGRA);
		}
		
		int a=(int) (alpha*255);
		
		int t=mat.type(),rows=mat.rows(),cols=mat.cols(),ch=mat.channels();
		int len=rows*cols*ch;
		int[] data=new int[len];
		if (CvType.depth(t) != CvType.CV_32S) {//转成int类型的值
			int type=CvType.CV_32SC(ch);//将通道转类型值
			Mat intMat=new Mat(rows, cols, type);
			mat.convertTo(intMat, type);
			mat=intMat;
		}
		mat.get(0, 0, data);
		for (int i = 3; i < len; i+=4) {
			data[i]=a;
		}
		mat.put(0, 0, data);
		return this;
	}
	
	
	/**
	 * 转成灰度图
	 * @return
	 */
	public Cver gray() {
		int c=mat.channels();
		if (c==3) {
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
		}else if (c==4) {
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2GRAY);
		}
		return this;
	}
	
	
	/**
	 * 转成BGR图
	 * @return
	 */
	public Cver bgr() {
		int c=mat.channels();
		if (c==1) {
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2BGR);
		}else if (c==4) {
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2BGR);
		}
		return this;
	}
	
	
	/**
	 * 转成BGRA图
	 * @return
	 */
	public Cver bgra() {
		int c=mat.channels();
		if (c==1) {
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2BGRA);
		}else if (c==3) {
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2BGRA);
		}
		return this;
	}
	
	
	/**
	 * 转换为HSV颜色空间,这个模型中颜色的参数分别是:色调(H),饱和度(S),明度(V)
	 * @return
	 */
	public Cver hsv() {
		bgr();
		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV);
		return this;
	}
	
	/**
	 * 转换为HLS颜色空间,这个模型中颜色的参数分别是:色调(H),亮度(L),饱和度(S)
	 * @return
	 */
	public Cver hls() {
		bgr();
		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HLS);
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
		if (mat.channels()>=3) {
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
		}
		return this;
	}
	
	/**
	 * 转换为反色图
	 * @return
	 */
	public Cver inverseColor() {
		Core.bitwise_not(mat, mat);
		return this;
	}
	
	
	/**
	 * 计算每个通道的直方图,结果保存在histograms属性中.
	 * @return
	 */
	public Cver histogram() {
        List<Mat> images = new ArrayList<>();
        Core.split(mat, images);
        MatOfInt channels = new MatOfInt(0); // 图像通道数，0表示只有一个通道 
        MatOfInt histSize = new MatOfInt(256); // CV_8U类型的图片范围是0~255，共有256个灰度级
        MatOfFloat histRange = new MatOfFloat(0, 255);
        histograms=new ArrayList<>();
        for(Mat oneChannel:images) {
        	Mat oneHistogram = new Mat(); // 输出直方图结果，共有256行，行数的相当于对应灰度值，每一行的值相当于该灰度值所占比例
        	Imgproc.calcHist(Arrays.asList(oneChannel), channels, new Mat(), oneHistogram, histSize, histRange, false);  // 计算直方图 
        	// 按行归一化
        	Core.normalize(oneHistogram, oneHistogram, 0, oneHistogram.rows(), Core.NORM_MINMAX, -1, new Mat());
        	histograms.add(oneHistogram);
        }
        return this;
    }
	
	
	/**
	 * 获取Blue通道的直方图对象
	 * @param width 直方图宽(最小256)
	 * @param height 直方图高(最小256)
	 * @return
	 */
	public Mat histogramBlue(int width,int height) {
		return plotHistogram(histograms.size()>0?histograms.get(0):null, width, height,new Scalar(255, 0, 0));
	}
	
	
	/**
	 * 获取Green通道的直方图对象
	 * @param width 直方图宽(最小256)
	 * @param height 直方图高(最小256)
	 * @return
	 */
	public Mat histogramGreen(int width,int height) {
		return plotHistogram(histograms.size()>1?histograms.get(1):(histograms.size()>0?histograms.get(0):null), width, height,new Scalar(0, 255, 0));
	}
	
	
	/**
	 * 获取Red通道的直方图对象
	 * @param width 直方图宽(最小256)
	 * @param height 直方图高(最小256)
	 * @return
	 */
	public Mat histogramRed(int width,int height) {
		return plotHistogram(histograms.size()>2?histograms.get(2):(histograms.size()>0?histograms.get(0):null), width, height,new Scalar(0, 0, 255));
	}
	
	
	/**
	 * 画直方图并返回直方图
	 * @param hist 某通道的直方图计算结果,rows=256,cols=1
	 * @param width
	 * @param height
	 * @param scalar
	 * @return
	 */
	private Mat plotHistogram(Mat hist, int width,int height,Scalar scalar) {
		//限制最小宽高为256*256
		width=Math.max(width, 256);
		height=Math.max(height, 256);
		int linew=width/256,colOffset = width%256==0?0:(width%256)/2;//计算线宽和偏移
		Mat result=new Mat(height,width,CvType.CV_8UC3, CvBridge.wTransparent);
		if (hist!=null) {
			for (int i = 0; i < 256; i++) {  // 画出每一个灰度级分量的比例，注意OpenCV将Mat最左上角的点作为坐标原点
				int x=linew*i+colOffset;//x轴位置
				Imgproc.line(result,new Point(x, height),new Point(x, height - Math.round(hist.get(i, 0)[0]/255*height)),scalar, linew,8,0);
			}
		}
		return result;
	}
	
	
	/**
	 * 图像风格化
	 * @return
	 */
	public Cver stylizztion() {
		Photo.stylization(mat, mat);
		return this;
	}
	
	
	/**
	 * 图像风格化
	 * @param sigma_s (sigma_size)计算临近像素的尺寸,取值[0,200]
	 * @param sigma_r (sigma_range)取值(0,1]
	 * @return
	 */
	public Cver stylizztion(float sigma_s, float sigma_r) {
		Photo.stylization(mat, mat,sigma_s,sigma_r);
		return this;
	}
	
	
	/**
	 * 转为灰色铅笔画
	 * @return
	 */
	public Cver pencilGray() {
		Photo.pencilSketch(mat,mat, new Mat());
		return this;
	}
	
	
	/**
	 * 转为灰色铅笔画风格
	 * @param sigma_s (sigma_size)计算临近像素的尺寸,取值[0,200]
	 * @param sigma_r (sigma_range)取值(0,1]
	 * @param shade_factor
	 * @return
	 */
	public Cver pencilGray(float sigma_s, float sigma_r, float shade_factor) {
		Photo.pencilSketch(mat,mat, new Mat(), sigma_s, sigma_r, shade_factor);
		return this;
	}
	
	
	/**
	 * 转为彩色铅笔画风格
	 * @return
	 */
	public Cver pencilColor() {
		Photo.pencilSketch(mat, new Mat(),mat);
		return this;
	}
	
	
	/**
	 * 转为彩色铅笔画风格
	 * @param sigma_s (sigma_size)计算临近像素的尺寸,取值[0,200]
	 * @param sigma_r (sigma_range)取值(0,1]
	 * @param shade_factor
	 * @return
	 */
	public Cver pencilColor(float sigma_s, float sigma_r, float shade_factor) {
		Photo.pencilSketch(mat, new Mat(),mat,sigma_s, sigma_r, shade_factor);
		return this;
	}
	
	
	/**
	 * 增强细节
	 * @return
	 */
	public Cver detailEnhance() {
		Photo.detailEnhance(mat,mat);
		return this;
	}
	
	
	/**
	 * 增强细节
	 * @param sigma_s (sigma_size)计算临近像素的尺寸,取值[0,200]
	 * @param sigma_r (sigma_range)取值(0,1]
	 * @return
	 */
	public Cver detailEnhance(float sigma_s, float sigma_r) {
		Photo.detailEnhance(mat, mat, sigma_s, sigma_r);
		return this;
	}
	
	
	/**
	 * 边缘处理(降噪磨皮美白)<br/>
	 * 默认值flags:{@link Photo#RECURS_FILTER},sigma_s:50,sigma_r:0.05f
	 * @return
	 */
	public Cver edgePreserving() {
		Photo.edgePreservingFilter(mat, mat,Photo.RECURS_FILTER,50,0.05f);
		return this;
	}
	
	
	/**
	 * 边缘处理(降噪磨皮美白)
	 * @param flags <br/>
	 * 可选：<br/>
	 * {@link Photo#RECURS_FILTER} 递归过滤器(推荐值)速度比归一化卷织快3.5倍<br/>
	 * {@link Photo#NORMCONV_FILTER} 卷织过滤器
	 * @param sigma_s (sigma_size)计算临近像素的尺寸,取值[0,200]
	 * @param sigma_r (sigma_range)磨皮效果,取值(0,1]
	 * @return
	 */
	public Cver edgePreserving(int flags, float sigma_s, float sigma_r) {
		Photo.edgePreservingFilter(mat, mat, flags, sigma_s, sigma_r);
		return this;
	}
	
	
	/**
	 * 添加图片
	 * @param cover 需与原图宽高一致
	 * @return
	 */
	public Cver add(Mat cover) {
		int mc=mat.channels(),cc=cover.channels();
		if (mc!=cc) {
			if (mc==1) {
				if (cc==3) {
					Imgproc.cvtColor(cover, cover, Imgproc.COLOR_BGR2GRAY);
				}else if (cc==4) {
					Imgproc.cvtColor(cover, cover, Imgproc.COLOR_BGRA2GRAY);
				}
			}else if (mc==3) {
				if (cc==1) {
					Imgproc.cvtColor(cover, cover, Imgproc.COLOR_GRAY2BGR);
				}else if (cc==4) {
					Imgproc.cvtColor(cover, cover, Imgproc.COLOR_BGRA2BGR);
				}
			}else if (mc==4) {
				if (cc==1) {
					Imgproc.cvtColor(cover, cover, Imgproc.COLOR_GRAY2BGRA);
				}else if (cc==3) {
					Imgproc.cvtColor(cover, cover, Imgproc.COLOR_BGR2BGRA);
				}
			}
		}
		Core.add(mat, cover, mat);
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
        edge=newMat();
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
        Mat dstx = newMat();
        Mat dsty = newMat();
        //计算x、y方向的梯度
        Imgproc.Scharr(mat, dstx, ddepth, 1, 0, scale, delta, borderType);
        Imgproc.Scharr(mat, dsty, ddepth, 0, 1, scale, delta, borderType);
        edge=newMat();
        Core.addWeighted(dstx, 0.5, dsty, 0.5, 0, edge);
		return this;
	}
	
	/**
	 * Hough圆检测,并且将检测结果画到当前的mat对象上
	 * @return
	 * @see #houghCircle()
	 */
	public Cver houghCircleNow() {
		return houghCircle().drawCircle();
	}
	
	/**
	 * Hough圆检测,并且将检测结果画到当前的mat对象上
	 * @param method
	 * @param dp
	 * @param minDist
	 * @param param1
	 * @param param2
	 * @param minRadius
	 * @param maxRadius
	 * @return
	 * @see #houghCircle(int, double, double, double, double, int, int)
	 */
	public Cver houghCircleNow(int method, double dp, double minDist, double param1, double param2, int minRadius, int maxRadius) {
		return houghCircle(method, dp, minDist, param1, param2, minRadius, maxRadius).drawCircle();
	}
	
	/**
	 * Hough圆检测<br/>
	 * 默认值method:{@link Imgproc#HOUGH_GRADIENT},dp:1,minDist:100,param1:440,param2:50,minRadius:0,maxRadius:0 
	 * @return
	 * @see #houghCircle(int, double, double, double, double, int, int)
	 */
	public Cver houghCircle() {
		return houghCircle(Imgproc.HOUGH_GRADIENT, 1, 100, 440, 50, 0, 0);
	}
	
	/**
	 * Hough圆检测
	 * @param method 检测方法,目前唯一可用 {@link Imgproc#HOUGH_GRADIENT}
	 * @param dp 如果dp=1,Hough空间的叠加器就和输入图片的分辨率一致。如果dp=2，Hough空间叠加器就只有一半大小.
	 * @param minDist 检测的圆心最小距离间隔.如果设置太小,一些旁边的圆可能就被错误的检测,而正确的就没检测出来.如果设置太大,可能就漏掉很多圆.
	 * @param param1 它是第三个参数method设置的检测方法的对应的参数。对当前唯一的方法霍夫梯度法HOUGH_GRADIENT，它表示传递给canny边缘检测算子的高阈值，而低阈值为高阈值的一半
	 * @param param2 它是第三个参数method设置的检测方法的对应的参数。对当前唯一的方法霍夫梯度法HOUGH_GRADIENT，它表示在检测阶段圆心的累加器阈值。它越小的话，就可以检测到更多根本不存在的圆，而它越大的话，能通过检测的圆就更加接近完美的圆形了
	 * @param minRadius 圆最小半径
	 * @param maxRadius 圆最大半径(若minRadius和maxRadius都为0，则函数会自动计算半径)
	 * @return
	 */
	public Cver houghCircle(int method, double dp, double minDist, double param1, double param2, int minRadius, int maxRadius) {
		Mat src=singleChannel();
		circle=newMat();
        Imgproc.HoughCircles(src, circle, method, dp, minDist, param1, param2, minRadius, maxRadius);
		return this;
	}
	
	/**
	 * 在关联的mat对象上把检测到的圆圈画上去
	 * @return
	 */
	private Cver drawCircle() {
		for (int i = 0; i < circle.cols(); i++){
            double[] vCircle = circle.get(0, i);
            Point center = new Point(vCircle[0], vCircle[1]);
            int radius = (int) Math.round(vCircle[2]);
            // circle center
            Imgproc.circle(mat, center, 3, color, -1, Imgproc.LINE_4, 0);
            // circle outline
            Imgproc.circle(mat, center, radius, color, 3, Imgproc.LINE_4, 0);
        }
		return this;
	}
	
	/**
	 * 标准Hough直线检测,并且将检测结果画到当前的mat对象上
	 * @return
	 * @see #houghLine()
	 */
	public Cver houghLineNow() {
		return houghLine().drawLine();
	}
	
	
	/**
	 * 标准Hough直线检测,并且将检测结果画到当前的mat对象上
	 * @param rho
	 * @param theta
	 * @param threshold
	 * @param srn
	 * @param stn
	 * @param min_theta
	 * @param max_theta
	 * @return
	 * @see #houghLine(double, double, int, double, double, double, double)
	 */
	public Cver houghLineNow(double rho, double theta, int threshold, double srn, double stn, double min_theta, double max_theta) {
		return houghLine(rho, theta, threshold, srn, stn, min_theta, max_theta).drawLine();
	}
	
	/**
	 * 标准Hough直线检测<br/>
	 * 默认值rho:1,theta:{@link Math#PI}/180,threshold:200,srn:0, stn:0,min_theta:0,max_theta:90
	 * @return
	 * @see #houghLine(double, double, int, double, double, double, double)
	 */
	public Cver houghLine() {
		return houghLine(1, Math.PI / 180, 200, 0, 0, 0, 90);
	}
	
	
	/**
	 * 标准Hough直线检测:Hough变换是图像处理中的一种特征提取技术,该过程在一个参数空间中通过计算累计结果的局部最大值得到一个符合特定形状的集合作为hough变换结果.
	 * @param rho 以像素为单位的距离精度 
	 * @param theta 以弧度为单位的角度精度 
	 * @param threshold 累加平面的阈值参数《即识别某部分为图中的一条直线时它在累加平面中必须达到的值，大于阈值threshold的线段才可以被检测通过并返回到结果中。
	 * @param srn 对于多尺度的霍夫变换,这是参数rho的除数距离,粗略的累加器进步尺寸直接是参数rho，而精确的累加器进步尺寸为rho/srn
	 * @param stn 对于多尺度霍夫变换,srn表示参数theta的除数距离,且如果srn和stn同时为0,就表示使用经典的霍夫变换.否则,这两个参数应该都为正数.
	 * @param min_theta 检测到的直线的最小角度 
	 * @param max_theta 检测到的直线的最大角度
	 * @return
	 */
	public Cver houghLine(double rho, double theta, int threshold, double srn, double stn, double min_theta, double max_theta) {
		Mat src=cannyMat();
		line=newMat();
        Imgproc.HoughLines(src, line, rho, theta, threshold, srn, stn, min_theta, max_theta);
        return this;
	}
	
	
	/**
	 * 累计概率Hough直线检测,并且将检测结果画到当前的mat对象上
	 * @return
	 * @see #houghLineP()
	 */
	public Cver houghLinePNow() {
		return houghLineP().drawLineP();
	}
	
	
	/**
	 * 累计概率Hough直线检测,并且将检测结果画到当前的mat对象上
	 * @param rho
	 * @param theta
	 * @param threshold
	 * @param minLineLength
	 * @param maxLineGap
	 * @return
	 * @see #houghLineP(double, double, int, double, double)
	 */
	public Cver houghLinePNow(double rho, double theta, int threshold, double minLineLength, double maxLineGap) {
		return houghLineP(rho, theta, threshold, minLineLength, maxLineGap).drawLineP();
	}
	
	/**
	 * 累计概率Hough直线检测<br/>
	 * 默认值rho:1,theta:{@link Math#PI}/180,threshold:50,minLineLength:1, maxLineGap:1
	 * @return
	 * @see #houghLineP(double, double, int, double, double)
	 */
	public Cver houghLineP() {
		return houghLineP(1, Math.PI / 180, 50, 1, 1);
	}
	
	
	/**
	 * 累计概率Hough直线检测:采用累计概率霍夫变换(PPHT)来找出二值图像中的直线.
	 * @param rho 以像素为单位的距离精度 
	 * @param theta 以弧度为单位的角度精度 
	 * @param threshold 累加平面的阈值参数《即识别某部分为图中的一条直线时它在累加平面中必须达到的值，大于阈值threshold的线段才可以被检测通过并返回到结果中。
	 * @param minLineLength 最低线段长度 
	 * @param maxLineGap 允许将同一行点与点之间连接起来的最大的距离
	 * @return
	 */
	public Cver houghLineP(double rho, double theta, int threshold, double minLineLength, double maxLineGap) {
		Mat src=cannyMat();
		lineP=newMat();
        Imgproc.HoughLinesP(src, lineP, rho, theta, threshold, minLineLength, maxLineGap);
        return this;
	}
	
	/**
	 * 将标准Hough检测出来的直线画到Mat上
	 * @return
	 */
	private Cver drawLine() {
		for (int x = 0; x < line.rows(); x++) {
			double[] vec = line.get(x, 0);
			double rho = vec[0],theta = vec[1];
			if (theta >= 0) {
				Point pt1 = new Point(),pt2 = new Point();
				double a = Math.cos(theta),b = Math.sin(theta);
				double x0 = a * rho,y0 = b * rho;
				pt1.x = Math.round(x0 + 1000 * (-b));
				pt1.y = Math.round(y0 + 1000 * (a));
				pt2.x = Math.round(x0 - 1000 * (-b));
				pt2.y = Math.round(y0 - 1000 * (a));
				Imgproc.line(mat, pt1, pt2, color, 1, Imgproc.LINE_4, 0);
			}
		}
		return this;
	}
	
	/**
	 * 将累计概率Hough检测出来的直线画到Mat上
	 * @return
	 */
	private Cver drawLineP() {
		for (int x = 0; x < lineP.rows(); x++){
            double[] vec = lineP.get(x, 0);
            double x1 = vec[0], y1 = vec[1], x2 = vec[2], y2 = vec[3];
            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);
            Imgproc.line(mat, start, end, color, 1, Imgproc.LINE_4, 0);
        }
		return this;
	}
	
	/**
	 * 若当前的mat是单通道时返回当前mat,否则返回当前mat转换为灰度图后的新mat对象
	 * @return
	 */
	private Mat singleChannel() {
		Mat src=null;
		if (mat.channels()==1) {
			src=mat;
		}else {
			src=newMat();
			Imgproc.cvtColor(mat, src, Imgproc.COLOR_BGR2GRAY);
		}
		return src;
	}
	
	/**
	 * 获取当前mat用Canny算子检测边缘产生的edge结果对象,并且不改变原edge的引用
	 * @return
	 */
	private Mat cannyMat() {
		Mat oldEdge=edge;
		canny();
		Mat newEdge=edge;
		edge=oldEdge;
		return newEdge;
	}
	
	/**
	 * 模板匹配,并把当前mat匹配到的区域标记(用矩形线框起来)<br/>
	 * 默认使用归一化相关匹配法,
	 * 默认值method:{@link Imgproc#TM_CCORR_NORMED}
	 * @param templ 要匹配的模板
	 * @return
	 */
	public Cver matchTemplateNow(Mat templ) {
		return matchTemplate(templ).drawMatched();
	}
	
	
	/**
	 * 模板匹配,并把当前mat匹配到的区域标记(用矩形线框起来)
	 * @param templ 要匹配的模板
	 * @param method 匹配算法<br/>
	 * 可选<br/>
	 * {@link Imgproc#TM_CCORR}：相关匹配法,该方法采用乘法操作；数值越大表明匹配程度越好。<br/>
	 * {@link Imgproc#TM_CCORR_NORMED}：归一化相关匹配法<br/>
	 * {@link Imgproc#TM_SQDIFF}：平方差匹配法,该方法采用平方差来进行匹配,最好的匹配值为0,匹配越差,匹配值越大。<br/>
	 * {@link Imgproc#TM_SQDIFF_NORMED}： 归一化平方差匹配法<br/>
	 * {@link Imgproc#TM_CCOEFF}：相关系数匹配法,1表示完美的匹配,-1表示最差的匹配. <br/>
	 * {@link Imgproc#TM_CCOEFF_NORMED}：归一化相关系数匹配法 <br/>
	 * @return
	 */
	public Cver matchTemplateNow(Mat templ, int method) {
		return matchTemplate(templ, method).drawMatched();
	}
	
	
	/**
	 * 模板匹配<br/>
	 * 默认使用归一化相关匹配法,
	 * 默认值method:{@link Imgproc#TM_CCORR_NORMED}
	 * @param templ 要匹配的模板
	 * @return
	 */
	public Cver matchTemplate(Mat templ) {
		return matchTemplate(templ,Imgproc.TM_CCOEFF_NORMED);
	}
	
	
	/**
	 * 模板匹配
	 * @param templ 要匹配的模板
	 * @param method 匹配算法<br/>
	 * 可选<br/>
	 * {@link Imgproc#TM_CCORR}：相关匹配法,该方法采用乘法操作；数值越大表明匹配程度越好。<br/>
	 * {@link Imgproc#TM_CCORR_NORMED}：归一化相关匹配法<br/>
	 * {@link Imgproc#TM_SQDIFF}：平方差匹配法,该方法采用平方差来进行匹配,最好的匹配值为0,匹配越差,匹配值越大。<br/>
	 * {@link Imgproc#TM_SQDIFF_NORMED}： 归一化平方差匹配法<br/>
	 * {@link Imgproc#TM_CCOEFF}：相关系数匹配法,1表示完美的匹配,-1表示最差的匹配. <br/>
	 * {@link Imgproc#TM_CCOEFF_NORMED}：归一化相关系数匹配法 <br/>
	 * @return
	 */
	public Cver matchTemplate(Mat templ, int method) {
		matched=new Mat(mat.rows()-templ.rows(), mat.cols()-templ.cols(), mat.type());
		Imgproc.matchTemplate(mat, templ, matched, method);
        MinMaxLocResult mmlr = Core.minMaxLoc(matched);
        if (method==Imgproc.TM_SQDIFF || method==Imgproc.TM_SQDIFF_NORMED) {
			mmlr.maxLoc=mmlr.minLoc;
		}
		mmlr.minLoc=new Point(mmlr.maxLoc.x+templ.cols(),mmlr.maxLoc.y+templ.rows());
		minMaxLocResult=mmlr;
		return this;
	}
	
	
	private Cver drawMatched() {
        Imgproc.rectangle(mat, minMaxLocResult.maxLoc,minMaxLocResult.minLoc,color);
		return this;
	}
	
	/**
	 * 高斯金字塔:向上采样<br/>
	 * 默认行列变为原来的2倍,borderType:{@link Core#BORDER_DEFAULT}
	 * @return
	 */
	public Cver pyrUp() {
		return pyrUp(Core.BORDER_DEFAULT);
	}
	
	/**
	 * 高斯金字塔:向上采样,行列变为原来的2倍
	 * @param borderType 边界模式<br/>
	 * 可选<br/>
	 * {@link Core#BORDER_REPLICATE}：复制法，既是复制最边缘像素，例如aaa|abc|ccc <br/>
	 * {@link Core#BORDER_REFLECT}：对称法，例如cba|abc|cba <br/>
	 * {@link Core#BORDER_REFLECT_101}：对称法，最边缘像素不会被复制，例如cb|abc|ba <br/>
	 * {@link Core#BORDER_CONSTANT}：常量法，默认为0 <br/>
	 * {@link Core#BORDER_WRAP}：镜像对称复制<br/>
	 * @return
	 */
	public Cver pyrUp(int borderType) {
		Imgproc.pyrUp(mat, mat, new Size(mat.cols()*2, mat.rows()*2), borderType);
		return this;
	}
	
	
	/**
	 * 高斯金字塔:向下采样<br/>
	 * 默认行列变为原来的一半,borderType:{@link Core#BORDER_DEFAULT}
	 * @return
	 */
	public Cver pyrDown() {
		return pyrDown(Core.BORDER_DEFAULT);
	}
	
	
	/**
	 * 高斯金字塔:向下采样,行列变为原来的一半
	 * @param borderType 边界模式<br/>
	 * 可选<br/>
	 * {@link Core#BORDER_REPLICATE}：复制法，既是复制最边缘像素，例如aaa|abc|ccc <br/>
	 * {@link Core#BORDER_REFLECT}：对称法，例如cba|abc|cba <br/>
	 * {@link Core#BORDER_REFLECT_101}：对称法，最边缘像素不会被复制，例如cb|abc|ba <br/>
	 * {@link Core#BORDER_CONSTANT}：常量法，默认为0 <br/>
	 * {@link Core#BORDER_WRAP}：镜像对称复制<br/>
	 * @return
	 */
	public Cver pyrDown(int borderType) {
		Imgproc.pyrDown(mat, mat, new Size(mat.cols()/2, mat.rows()/2), borderType);
		return this;
	}
	
	
	/**
	 * 形态学运算
	 * @param op 形态学运算类型<br/>
	 * {@link Imgproc#MORPH_OPEN} – 开运算(Opening operation),先腐蚀后膨胀:放大裂缝和低密度区域，消除小物体，在平滑较大物体的边界时，不改变其面积<br/>
	 * {@link Imgproc#MORPH_CLOSE} – 闭运算(Closing operation),先膨胀后腐蚀:排除小型黑洞，突触了比原图轮廓区域更暗的区域<br/>
	 * {@link Imgproc#MORPH_GRADIENT} -形态学梯度(Morphological gradient),膨胀图-腐蚀图:保留图像边缘<br/>
	 * {@link Imgproc#MORPH_TOPHAT} - 顶帽(Top hat),原图-开运算:分离邻近点亮一些的斑块，进行背景提取<br/>
	 * {@link Imgproc#MORPH_BLACKHAT} - 黑帽(Black hat),闭运算-原图:用来分离比邻近点暗一些的斑块<br/>
	 * @param kernel 运算的核
	 * @param anchor 锚的位置(-1，-1)，表示锚位于中心。
	 * @param iterations 迭代使用函数的次数。
	 * @param borderType 边界模式<br/>
	 * 可选<br/>
	 * {@link Core#BORDER_REPLICATE}：复制法，既是复制最边缘像素，例如aaa|abc|ccc <br/>
	 * {@link Core#BORDER_REFLECT}：对称法，例如cba|abc|cba <br/>
	 * {@link Core#BORDER_REFLECT_101}：对称法，最边缘像素不会被复制，例如cb|abc|ba <br/>
	 * {@link Core#BORDER_CONSTANT}：常量法，默认为0 <br/>
	 * {@link Core#BORDER_WRAP}：镜像对称复制<br/>
	 * @param borderValue 边界值
	 * @return
	 */
	public Cver morphologyEx(int op, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue) {
		Imgproc.morphologyEx(mat,mat,op, kernel, anchor, iterations, borderType, borderValue);
		return this;
	}
	
	
	/**
	 * 形态学运算<br/>
	 * 默认kernel:@{@link Imgproc#MORPH_RECT}、{@link Cver#defKernelKsize} 3x3,anchor:(-1,-1),iterations:1,borderType:{@linkplain Core#BORDER_CONSTANT}
	 * @param op 形态学运算类型<br/>
	 * {@link Imgproc#MORPH_OPEN} – 开运算(Opening operation),先腐蚀后膨胀:放大裂缝和低密度区域，消除小物体，在平滑较大物体的边界时，不改变其面积<br/>
	 * {@link Imgproc#MORPH_CLOSE} – 闭运算(Closing operation),先膨胀后腐蚀:排除小型黑洞，突触了比原图轮廓区域更暗的区域<br/>
	 * {@link Imgproc#MORPH_GRADIENT} -形态学梯度(Morphological gradient),膨胀图-腐蚀图:保留图像边缘<br/>
	 * {@link Imgproc#MORPH_TOPHAT} - 顶帽(Top hat),原图-开运算:分离邻近点亮一些的斑块，进行背景提取<br/>
	 * {@link Imgproc#MORPH_BLACKHAT} - 黑帽(Black hat),闭运算-原图:用来分离比邻近点暗一些的斑块<br/>
	 * @return
	 */
	public Cver morphologyEx(int op) {
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, defKernelKsize);
		Imgproc.morphologyEx(mat, mat, op, kernel);
		return this;
	}
	
	/**
	 * 形态学运算-开运算<br/>
	 * 先腐蚀后膨胀:放大裂缝和低密度区域，消除小物体，在平滑较大物体的边界时，不改变其面积
	 * @return
	 */
	public Cver morphologyOpen() {
		return morphologyEx(Imgproc.MORPH_OPEN);
	}
	
	/**
	 * 形态学运算-闭运算<br/>
	 * 先膨胀后腐蚀:排除小型黑洞，突触了比原图轮廓区域更暗的区域
	 * @return
	 */
	public Cver morphologyClose() {
		return morphologyEx(Imgproc.MORPH_CLOSE);
	}
	
	/**
	 * 形态学运算-形态学梯度<br/>
	 * 膨胀图-腐蚀图:保留图像边缘
	 * @return
	 */
	public Cver morphologyGradient() {
		return morphologyEx(Imgproc.MORPH_GRADIENT);
	}
	
	/**
	 * 形态学运算-顶帽<br/>
	 * 原图-开运算:分离邻近点亮一些的斑块，进行背景提取
	 * @return
	 */
	public Cver morphologyTopHat() {
		return morphologyEx(Imgproc.MORPH_TOPHAT);
	}
	
	/**
	 * 形态学运算-黑帽<br/>
	 * 闭运算-原图:用来分离比邻近点暗一些的斑块
	 * @return
	 */
	public Cver morphologyBlackHat() {
		return morphologyEx(Imgproc.MORPH_BLACKHAT);
	}
	
	
	
	
}
