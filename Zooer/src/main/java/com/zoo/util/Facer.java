package com.zoo.util;

import java.nio.file.Paths;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public final class Facer {
	private Facer() {}
	
	/**
	 * 级联分拣器
	 */
	private static CascadeClassifier faceDetector=null;
	static {
		System.load("E:\\opencv_java331.dll");
		faceDetector = new CascadeClassifier(Paths.get("E:\\IDE\\opencv-3.3.1-vc14\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml").toString());
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
	    faceDetector.detectMultiScale(image, faceDetections);
	    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
	    // Draw a bounding box around each face.
	    for (Rect rect : faceDetections.toArray()) {
	        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
	    }
	    // Save the visualized detection.
	    System.out.println(String.format("Writing %s", filenameDest));
	    Imgcodecs.imwrite(filenameDest, image);
	}
}
