package com.zoo.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public final class Facer {
	private Facer() {}
	
	//脸部级联分拣器
	private static CascadeClassifier faceDetector=null;
	//左眼
	private static CascadeClassifier eyeLeftDetector=null;
	//右眼
	private static CascadeClassifier eyeRightDetector=null;
	
	private static CascadeClassifier eyeDetector=null;
	
	private static final Size MIN_EYE=new Size(20, 20);
	private static final Size MAX_EYE=new Size(900, 900);
	private static final Size MIN_FACE=new Size(80, 100);
	private static final Size MAX_FACE=new Size(3000, 3600);
	
	private static final Color GREEN_1=new Color(0, 255, 0,255);
	private static final Scalar GREEN_2=new Scalar(0, 255, 0,255);
	
	private static final IllegalAccessError FACE_EMPTY_ERR=new IllegalAccessError("OpenCV Error: Assertion failed faceDetector is empty!");
	private static final IllegalAccessError EYE_EMPTY_ERR=new IllegalAccessError("OpenCV Error: Assertion failed eyeDetector is empty!");
	
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
			throw EYE_EMPTY_ERR;
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
			throw FACE_EMPTY_ERR;
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
}
