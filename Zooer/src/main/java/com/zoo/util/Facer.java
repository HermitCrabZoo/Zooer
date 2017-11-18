package com.zoo.util;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.utils.Converters;

public final class Facer {
	private Facer() {}
	
	/**
	 * 级联分拣器
	 */
	private static CascadeClassifier faceDetector=null;
	private static Size min=new Size(100, 120);
	private static Size max=new Size(3000, 3600);
	static {
		faceDetector = new CascadeClassifier(Paths.get("E:\\images\\lbpcascade_frontalface.xml").toString());
		//System.load(Pather.join(System.getProperty("java.library.path"),"opencv_java331.dll"));
//		System.loadLibrary("opencv_java331.dll");
	}
	public static void detect() {
		String n="5";
		String filename="E:\\images\\"+n+".jpeg";
		String filenameDest="E:\\images\\"+n+"Detection.png";
		System.out.println();
		Mat image = Imgcodecs.imread(filename);
	    // Detect faces in the image.
	    // MatOfRect is a special container class for Rect.
	    MatOfRect faceDetections = new MatOfRect();
	    faceDetector.detectMultiScale(image, faceDetections, 1.1, 3, Objdetect.CASCADE_SCALE_IMAGE, min, max);
	    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
	    // Draw a bounding box around each face.
	    for (Rect rect : faceDetections.toArray()) {
	        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
	    }
	    // Save the visualized detection.
	    System.out.println(String.format("Writing %s", filenameDest));
	    Imgcodecs.imwrite(filenameDest, image);
	}
	public static List<BufferedImage> faces(BufferedImage img) {
		String n="5";
		String filename="E:\\images\\"+n+".jpeg";
		String filenameDest="E:\\images\\"+n+"Detection.png";
		System.out.println();
		Mat image = Imgcodecs.imread(filename);
		// Detect faces in the image.
		// MatOfRect is a special container class for Rect.
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);
		faceDetector.detectMultiScale(image, faceDetections, 1.1, 3, Objdetect.CASCADE_SCALE_IMAGE, min, max);
		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
		// Draw a bounding box around each face.
		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}
		// Save the visualized detection.
		System.out.println(String.format("Writing %s", filenameDest));
		Imgcodecs.imwrite(filenameDest, image);
		return null;
	}
}
