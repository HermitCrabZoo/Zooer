package com.zoo.mix;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.List;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_face.FisherFaceRecognizer;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import com.zoo.base.Arrs;

public final class Facer {
	private Facer() {}
	
	//脸部级联分拣器
	private static CascadeClassifier faceDetector=null;
	//左眼
	private static CascadeClassifier eyeLeftDetector=null;
	//右眼
	@SuppressWarnings("unused")
	private static CascadeClassifier eyeRightDetector=null;
	
	@SuppressWarnings("unused")
	private static CascadeClassifier eyeDetector=null;
	
	private static final Size MIN_EYE=new Size(20, 20);
	private static final Size MAX_EYE=new Size(900, 900);
	private static final Size MIN_FACE=new Size(80, 100);
	private static final Size MAX_FACE=new Size(3000, 3600);
	
	private static final Color GREEN_1=new Color(0, 255, 0,255);
	private static final Scalar GREEN_2=new Scalar(0, 255, 0,255);
	
	static {CvBridge.loadOpenCv();}
	
	static {
		faceDetector = new CascadeClassifier("E:\\images\\lbpcascade_frontalface.xml");
		eyeLeftDetector = new CascadeClassifier("E:\\images\\haarcascade_lefteye_2splits.xml");
		eyeRightDetector = new CascadeClassifier("E:\\images\\haarcascade_righteye_2splits.xml");
		eyeDetector = new CascadeClassifier("E:\\images\\haarcascade_eye.xml");
	}
	
	/**
	 * 检测并获取Mat对象中识别出来的'在脸上的眼睛',并返回可能包含多个眼睛检测结果的Mat[]数组
	 * @param mat
	 * @return
	 */
	public static Mat[] eyesOnFace(Mat mat) {
		Rect[] rects=eyeAreasOnFace(mat);
		return subMats(mat, rects);
	}
	
	/**
	 * 检测并获取fileName指向的图片文件中识别出来的眼睛,并返回可能包含多个眼睛检测结果的Mat[]数组
	 * @param fileName
	 * @return
	 * @see {@link #eyes(Mat)}
	 */
	public static Mat[] eyes(String fileName) {
		return eyes(Imgcodecs.imread(fileName));
	}
	
	/**
	 * 检测并获取BufferedImage对象中识别出来的眼睛,并返回可能包含多个眼睛检测结果的BufferedImage[]数组
	 * @param image
	 * @return
	 */
	public static BufferedImage[] eyes(BufferedImage image) {
		Rect[] rects=eyeAreas(image);
		int len=rects.length;
		BufferedImage[] imgs=new BufferedImage[len];
		for (int i=0;i<len;i++) {
			Rect rect=rects[i];
			imgs[i]=image.getSubimage(rect.x,rect.y,rect.width,rect.height);
		}
		return imgs;
	}
	
	/**
	 * 检测并获取Mat对象中识别出来的眼睛,并返回可能包含多个眼睛检测结果的Mat[]数组
	 * @param mat
	 * @return
	 */
	public static Mat[] eyes(Mat mat) {
		Rect[] rects=eyeAreas(mat);
		return subMats(mat, rects);
	}
	
	/**
	 * 检测并画出Mat中出现的'人脸上的眼睛',不改变原始的Mat对象,返回新的Mat对象.
	 * @param mat
	 * @return
	 */
	public static Mat drawEyeOnFace(Mat mat) {
		Mat dst=mat.clone();
		Rect[] rois=faceAreas(mat);
		for(Rect roi:rois) {
			Rect eyearea = new Rect(roi.x +roi.width/8,(int)(roi.y + (roi.height/4.5)),roi.width - 2*roi.width/8,(int)( roi.height/3.0));
			Rect[] rects=eyeAreas(mat.submat(eyearea),MIN_EYE,eyearea.size());
			for (Rect rect : rects) {
				rect.x+=eyearea.x;
				rect.y+=eyearea.y;
				Imgproc.rectangle(dst, rect.tl(), rect.br(), GREEN_2);
			}
		}
		return dst;
	}
	
	/**
	 * 检测并画出fileName指向的图片文件中出现的眼睛,不改变原始的图片文件,返回新的Mat对象.
	 * @param fileName
	 * @return
	 * @see {@link #drawEye(Mat)}
	 */
	public static Mat drawEye(String fileName) {
		return drawEye(Imgcodecs.imread(fileName));
	}
	
	/**
	 * 检测并画出BufferedImage中出现的眼睛,不改变原始的BufferedImage对象,返回新的BufferedImage对象.
	 * @param image
	 * @return
	 */
	public static BufferedImage drawEye(BufferedImage image) {
		BufferedImage dst=copy(image);
		Graphics2D g=dst.createGraphics();
		g.setColor(GREEN_1);
		Rect[] rects=eyeAreas(image);
		for (Rect rect : rects) {
			g.drawRect(rect.x,rect.y,rect.width,rect.height);
		}
		g.dispose();
		return dst;
	}
	
	/**
	 * 检测并画出Mat中出现的眼睛,不改变原始的Mat对象,返回新的Mat对象.
	 * @param mat
	 * @return
	 */
	public static Mat drawEye(Mat mat) {
		Mat dst=mat.clone();
		Rect[] rects=eyeAreas(mat);
		for (Rect rect : rects) {
			Imgproc.rectangle(dst, rect.tl(), rect.br(), GREEN_2);
		}
		return dst;
	}
	
	/**
	 * 从Mat中获取在脸上的眼睛区域在Mat中所处的矩形区域位置的数组
	 * @param mat
	 * @return
	 */
	public static Rect[] eyeAreasOnFace(Mat mat) {
		Rect[] eyes=new Rect[0];
		Rect[] rois=faceAreas(mat);
		for(Rect roi:rois) {
			Rect eyearea = new Rect(roi.x +roi.width/8,(int)(roi.y + (roi.height/4.5)),roi.width - 2*roi.width/8,(int)( roi.height/3.0));
			Rect[] rects=eyeAreas(mat.submat(eyearea),MIN_EYE,eyearea.size());
			for (Rect rect : rects) {
				rect.x+=eyearea.x;
				rect.y+=eyearea.y;
			}
			eyes=Arrs.concat(eyes,rects);
		}
		return eyes;
	}
	
	/**
	 * 从fileName指向的图片文件中中获取眼睛区域在图片中所处矩形区域位置的数组
	 * @param fileName
	 * @return
	 * @see {@link #eyeAreas(Mat)}
	 */
	public static Rect[] eyeAreas(String fileName) {
		return eyeAreas(Imgcodecs.imread(fileName));
	}
	
	/**
	 * 从BufferedImage中获取眼睛区域在BufferedImage中所处矩形区域位置的数组
	 * @param image
	 * @return
	 * @see {@link #eyeAreas(Mat)}
	 */
	public static Rect[] eyeAreas(BufferedImage image) {
		return eyeAreas(CvBridge.mat(image));
	}
	
	/**
	 * 从Mat中获取眼睛区域在Mat中所处矩形区域位置的数组
	 * @param mat
	 * @return
	 * @see {@link #eyeAreas(Mat, Size, Size)}
	 */
	public static Rect[] eyeAreas(Mat mat) {
		return eyeAreas(mat, MIN_EYE, MAX_EYE);
	}
	
	/**
	 * 从Mat对象中检测眼睛,并返回检测结果在Mat里对应的矩形区域位置数组，适当设置min、max的大小有助于提高眼睛检测的效率和准确性
	 * @param mat
	 * @param min 最小的检测尺寸
	 * @param max 最大的检测尺寸
	 * @return
	 */
	public static Rect[] eyeAreas(Mat mat,Size min,Size max) {
		if(eyeLeftDetector.empty()) {
			return new Rect[0];
		}
		MatOfRect eyeDetections = new MatOfRect();
		eyeLeftDetector.detectMultiScale(mat, eyeDetections, 1.1, 3, Objdetect.CASCADE_SCALE_IMAGE, min, max);
		Rect[] rects=eyeDetections.toArray();
		return rects;
	}
	
	/**
	 * 检测并获取fileName所指向图片文件中识别出来的人脸,并返回可能包含多个人脸检测结果的Mat[]数组
	 * @param fileName
	 * @return
	 * @see {@link #faces(Mat)}
	 */
	public static Mat[] faces(String fileName) {
		return faces(Imgcodecs.imread(fileName));
	}
	
	/**
	 * 检测并获取BufferedImage对象中识别出来的人脸,并返回可能包含多个人脸检测结果的BufferedImage[]数组
	 * @param image
	 * @return
	 */
	public static BufferedImage[] faces(BufferedImage image) {
		Rect[] rects=faceAreas(image);
		int len=rects.length;
		BufferedImage[] imgs=new BufferedImage[len];
		for (int i=0;i<len;i++) {
			Rect rect=rects[i];
			imgs[i]=image.getSubimage(rect.x,rect.y,rect.width,rect.height);
		}
		return imgs;
	}
	
	/**
	 * 检测并获取Mat对象中识别出来的人脸,并返回可能包含多个人脸检测结果的Mat[]数组
	 * @param mat
	 * @return
	 */
	public static Mat[] faces(Mat mat) {
		Rect[] rects=faceAreas(mat);
		return subMats(mat, rects);
	}
	
	/**
	 * 检测并画出fileName所指向的图片文件中出现的人脸,不改变原始的文件,返回新的Mat对象.
	 * @param fileName
	 * @return
	 * @see {@link #drawFace(Mat)}
	 */
	public static Mat drawFace(String fileName) {
		return drawFace(Imgcodecs.imread(fileName));
	}
	
	/**
	 * 检测并画出BufferedImage中出现的人脸,不改变原始的BufferedImage对象,返回新的BufferedImage对象.
	 * @param image
	 * @return
	 */
	public static BufferedImage drawFace(BufferedImage image) {
		BufferedImage dst=copy(image);
		Rect[] rects=faceAreas(image);
		Graphics2D g=dst.createGraphics();
		g.setColor(GREEN_1);
		for (Rect rect : rects) {
			g.drawRect(rect.x,rect.y,rect.width,rect.height);
		}
		g.dispose();
		return dst;
	}
	
	/**
	 * 检测并画出Mat中出现的人脸,不改变原始的Mat对象,返回新的Mat对象.
	 * @param mat
	 * @return
	 */
	public static Mat drawFace(Mat mat) {
		Rect[] rects=faceAreas(mat);
		Mat dst=mat.clone();
		for (Rect rect : rects) {
			Imgproc.rectangle(dst, rect.tl(), rect.br(), GREEN_2);
		}
		return dst;
	}
	
	/**
	 * 检测并获取脸部在fileName所指向的图片文件里对相应的矩形区域
	 * @param fileName
	 * @return
	 * @see {@link #faceAreas(Mat)}
	 */
	public static Rect[] faceAreas(String fileName) {
		return faceAreas(Imgcodecs.imread(fileName));
	}
	
	/**
	 * 检测并获取脸部在BufferedImage对象里对相应的矩形区域
	 * @param image
	 * @return
	 * @see {@link #faceAreas(Mat)}
	 */
	public static Rect[] faceAreas(BufferedImage image) {
		return faceAreas(CvBridge.mat(image));
	}
	
	/**
	 * 检测并获取脸部在Mat对象里对相应的矩形区域
	 * @param mat
	 * @return
	 */
	public static Rect[] faceAreas(Mat mat) {
		if(faceDetector.empty()) {
			return new Rect[0];
		}
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(mat, faceDetections, 1.1, 3, Objdetect.CASCADE_SCALE_IMAGE, MIN_FACE, MAX_FACE);
		Rect[] rects=faceDetections.toArray();
		return rects;
	}
	
	/**
	 * 从mat中获取rects里每一个Rect对象在mat中对应区域的子Mat对象.并返回这些对象的数组
	 * @param mat
	 * @param rects
	 * @return
	 */
	private static Mat[] subMats(Mat mat,Rect[] rects) {
		int len=rects.length;
		Mat[] mats=new Mat[len];
		for (int i=0;i<len;i++) {
			mats[i]=mat.submat(rects[i]);
		}
		return mats;
	}
	
	/**
	 * 拷贝出一个新的BufferedImage对象
	 * @param image
	 * @return
	 */
	private static BufferedImage copy(BufferedImage image) {
		BufferedImage ni=new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		ni.setData(image.getData());
		return ni;
	}
	
	/**
	 * 比较两个图片的相似度,比较失败则返回-1
	 * @param feature1
	 * @param feature2
	 * @return 相似度范围[0,100]
	 */
	public static double compare(BufferedImage feature1,BufferedImage feature2) {
		return compare(Java2DFrameUtils.toIplImage(feature1), Java2DFrameUtils.toIplImage(feature2));
	}
	
	/**
     * 特征相似度的对比,比较失败则返回-1
     * @param feature1 特征1
     * @param feature2 特征2
     * @return 相似度范围[0,100]
     */
    public static double compare(String feature1, String feature2) {
    	//灰度图
    	IplImage image1 = opencv_imgcodecs.cvLoadImage(feature1, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    	IplImage image2 = opencv_imgcodecs.cvLoadImage(feature2, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        return compare(image1, image2);
    }
	
    /**
     * 比较两个图片的相似度,比较失败则返回-1
     * @param image1
     * @param image2
     * @return 相似度范围[0,100]
     */
    public static double compare(IplImage image1,IplImage image2) {
    	if (image1!=null || image2!=null) {
    		try {
    			int l_bins = 20;
    			int hist_size[] = {l_bins};
    			float v_ranges[] = {0, 255};
    			float ranges[][] = {v_ranges};
    			
    			IplImage imageArr1[] = {image1};
    			IplImage imageArr2[] = {image2};
    			
    			opencv_core.CvHistogram Histogram1 = opencv_core.CvHistogram.create(1, hist_size, opencv_core.CV_HIST_ARRAY, ranges, 1);
    			opencv_core.CvHistogram Histogram2 = opencv_core.CvHistogram.create(1, hist_size, opencv_core.CV_HIST_ARRAY, ranges, 1);
    			
    			opencv_imgproc.cvCalcHist(imageArr1, Histogram1, 0, null);
    			opencv_imgproc.cvCalcHist(imageArr2, Histogram2, 0, null);
    			
    			opencv_imgproc.cvNormalizeHist(Histogram1, 100.0);
    			opencv_imgproc.cvNormalizeHist(Histogram2, 100.0);
    			
//    			double c1 = opencv_imgproc.cvCompareHist(Histogram1, Histogram2, opencv_imgproc.CV_COMP_CORREL) * 100;
    			double c2 = opencv_imgproc.cvCompareHist(Histogram1, Histogram2, opencv_imgproc.CV_COMP_INTERSECT);
    			
//    			return c1 * 0.3 + c2 * 0.7;
    			return c2;
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
		return -1;
    }
    
    /**
     * 比较两个图片的相似度[0.0,100]
     * @param finename1
     * @param finename2
     * @return
     */
    public static double compareHist(String finename1,String finename2) {
    	Mat mat1=Imgcodecs.imread(finename1, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    	Mat mat2=Imgcodecs.imread(finename2, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    	if (mat1.empty() || mat2.empty()) {
			return 0.0;
		}
    	return compareHist(mat1, mat2);
    }
    
    
    /**
     * 比较两个mat的相似度[0.0,100]
     * @param mat1
     * @param mat2
     * @return
     */
    public static double compareHist(Mat mat1, Mat mat2) {
        Mat srcMat = gray(mat1, true);
        Mat desMat = gray(mat2, true);
        srcMat.convertTo(srcMat, CvType.CV_32F);
        desMat.convertTo(desMat, CvType.CV_32F);
        double target = Imgproc.compareHist(srcMat, desMat,Imgproc.CV_COMP_CORREL)*100;
        return target;
    }
    
    
    
    
    /**
     * 使用mats和labels来训练，再对face进行预测，返回预测出来的label值，mats与labels长度应相等里面的元素应该是一一对应。
     * @param mats
     * @param labels
     * @param face
     * @return
     */
    public static int trainPredict(List<org.bytedeco.javacpp.opencv_core.Mat> mats,int[] labels,org.bytedeco.javacpp.opencv_core.Mat face) {
    	
    	int size=labels.length;
    	
    	MatVector mv=new MatVector(size);
    	org.bytedeco.javacpp.opencv_core.Mat mat=new org.bytedeco.javacpp.opencv_core.Mat(size, 1, opencv_core.CV_32SC1);
        IntBuffer intBuffer=mat.createBuffer();
        
        for (int i = 0; i < size; i++) {
        	
			mv.put(i, gray(mats.get(i),true));
			intBuffer.put(i, labels[i]);
		}
        
        face=gray(face, true);
        
        IntPointer result = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
    	
        FaceRecognizer fr = FisherFaceRecognizer.create();
        fr.train(mv, mat);
    	fr.predict(face, result, confidence);
    	
    	return result.get();
    }
    
    private static org.bytedeco.javacpp.opencv_core.Mat gray(org.bytedeco.javacpp.opencv_core.Mat mat,boolean clone){
    	if (mat!=null && !mat.empty() && mat.channels()>1) {
    		if (clone) {
    			mat=mat.clone();
			}
    		opencv_imgproc.cvtColor(mat, mat, mat.channels()>3?opencv_imgproc.COLOR_BGRA2GRAY:opencv_imgproc.COLOR_BGR2GRAY);
		}
    	return mat;
    }
    
    private static Mat gray(Mat mat,boolean isNew){
    	if (mat!=null && !mat.empty() && mat.channels()>1) {
    		Mat dst=isNew?new Mat():mat;
    		Imgproc.cvtColor(dst, dst, mat.channels()>3?opencv_imgproc.COLOR_BGRA2GRAY:opencv_imgproc.COLOR_BGR2GRAY);
    		return dst;
		}
    	return isNew&&mat!=null?mat.clone():mat;
    }
    
}
