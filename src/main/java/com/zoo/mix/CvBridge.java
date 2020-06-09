package com.zoo.mix;

import com.zoo.base.Arrs;
import com.zoo.base.Typer;
import com.zoo.se.Colors;
import com.zoo.system.Platform;
import org.bytedeco.javacv.Frame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;


/**
 * BufferedImage、OpenCV Mat、JavaCV Frame之间的转换工具类
 *
 * @author ZOO
 */
public final class CvBridge {

    private CvBridge() {
    }


    public static final Scalar bTransparent = new Scalar(0, 0, 0, 0);

    public static final Scalar wTransparent = new Scalar(255, 255, 255, 0);

    /**
     * BufferedImage的类型
     */
    private static final int[] JAVA_IMG_TYPES = {BufferedImage.TYPE_CUSTOM,
            BufferedImage.TYPE_INT_RGB,
            BufferedImage.TYPE_INT_ARGB,
            BufferedImage.TYPE_INT_ARGB_PRE,
            BufferedImage.TYPE_INT_BGR,
            BufferedImage.TYPE_3BYTE_BGR,
            BufferedImage.TYPE_4BYTE_ABGR,
            BufferedImage.TYPE_4BYTE_ABGR_PRE,
            BufferedImage.TYPE_BYTE_GRAY,
            BufferedImage.TYPE_BYTE_BINARY,
            BufferedImage.TYPE_BYTE_INDEXED,
            BufferedImage.TYPE_USHORT_GRAY,
            BufferedImage.TYPE_USHORT_565_RGB,
            BufferedImage.TYPE_USHORT_555_RGB};

    /**
     * OpenCV中Mat的类型,与{@link #JAVA_IMG_TYPES}中的值一一对应
     */
    private static final int[] OPENCV_IMG_TYPES = {0,
            CvType.CV_32SC3,
            CvType.CV_32SC4,
            CvType.CV_32SC4,
            CvType.CV_32SC3,
            CvType.CV_8UC3,
            CvType.CV_8UC4,
            CvType.CV_8UC4,
            CvType.CV_8UC1,
            CvType.CV_8UC3,
            CvType.CV_8UC3,
            CvType.CV_16UC1,
            CvType.CV_16UC3,
            CvType.CV_16UC3};

    /**
     * 是否未加载OpenCV库文件
     */
    private static boolean unload = true;


    static {
        loadOpenCv();
    }

    /**
     * 加载OpenCV库
     *
     * @return 加载成功或已加载为true, 否则为false
     */
    public static synchronized boolean loadOpenCv() {
        if (unload) {
            String filename = Core.NATIVE_LIBRARY_NAME + Platform.jniSuffix();
            try {
                //先将文件抽取到临时目录再加载
                String nativeTempDir = System.getProperty("java.io.tmpdir");
                String output = Paths.get(nativeTempDir, filename).toString();
                extractLib(filename, output);
                System.load(output);
                unload = false;
                System.out.println("Usage OpenCV " + Core.VERSION);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return !unload;
    }


    /**
     * 抽取库文件到指定目录。
     *
	 * @param filename lib库文件名
	 * @param output 输出文件路径
     * @return 成功则true，否则false。
     * @throws IOException 写入到目标文件时
     */
    public static synchronized boolean extractLib(String filename, String output) throws IOException {
        try (InputStream in = Core.class.getClassLoader().getResourceAsStream("lib/" + filename)) {
            if (in != null) {
                // 将文件抽取到指定目录
                Files.copy(in, Paths.get(output), StandardCopyOption.REPLACE_EXISTING);
                return true;
            }
        }
        return false;
    }


    /**
     * 将BufferedImage对象转换为JavaCV的Frame对象
     *
     * @param image BufferedImage对象
     * @return JavaCV的Frame对象
     */
    public static Frame frame(BufferedImage image) {
        return Videor.getFrameconverter().convert(image);
    }

    /**
     * 将JavaCV的Frame对象转换为BufferedImage对象
     *
     * @param frame JavaCV的Frame对象
     * @return BufferedImage对象
     */
    public static BufferedImage image(Frame frame) {
        return Videor.getFrameconverter().convert(frame);
    }

    /**
     * 将BufferedImage对象转换为OpenCV的Mat对象
     *
     * @param image BufferedImage对象
     * @return OpenCV的Mat对象
     */
    public static Mat mat(BufferedImage image) {
        int type = image.getType();
        Mat mat = new Mat(image.getHeight(), image.getWidth(), OPENCV_IMG_TYPES[Arrs.index(JAVA_IMG_TYPES, type)]);

        if (Arrs.contains(type, BufferedImage.TYPE_BYTE_GRAY, BufferedImage.TYPE_3BYTE_BGR)) {

            byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            mat.put(0, 0, pixels);

        } else if (Arrs.contains(type, BufferedImage.TYPE_4BYTE_ABGR, BufferedImage.TYPE_4BYTE_ABGR_PRE)) {

            byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            int len = pixels.length;
            byte[] npixels = new byte[len];
            //拷贝像素值,并将每个像素分量排布从ABGR转为BGRA
            for (int i = 0; i < len; i += 4) {
                npixels[i + 3] = pixels[i];
                System.arraycopy(pixels, i + 1, npixels, i, 3);
            }
            mat.put(0, 0, npixels);

        } else if (Arrs.contains(type, BufferedImage.TYPE_BYTE_BINARY, BufferedImage.TYPE_BYTE_INDEXED)) {

            byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            int len = image.getWidth() * image.getHeight() * 3;
            byte[] npixels = new byte[len];
            //反转拷贝*3
            for (int i = 0, j = pixels.length - 1; j > -1; i += 3, j--) {
                Arrays.fill(npixels, i, i + 3, pixels[j]);
            }
            mat.put(0, 0, npixels);

        } else if (type == BufferedImage.TYPE_USHORT_GRAY) {

            short[] pixels = ((DataBufferUShort) image.getRaster().getDataBuffer()).getData();
            mat.put(0, 0, pixels);

        } else if (Arrs.contains(type, BufferedImage.TYPE_USHORT_565_RGB, BufferedImage.TYPE_USHORT_555_RGB)) {

            short[] pixels = ((DataBufferUShort) image.getRaster().getDataBuffer()).getData();
            int len = pixels.length * 3;
            short[] npixels = new short[len];
            //反转拷贝
            for (int i = 0, j = pixels.length - 1; i < len; i += 3, j--) {
                System.arraycopy(Typer.shorts(Colors.bgra(pixels[j])), 0, npixels, i, 3);
            }
            mat.put(0, 0, npixels);

        } else if (Arrs.contains(type, BufferedImage.TYPE_INT_RGB, BufferedImage.TYPE_INT_ARGB, BufferedImage.TYPE_INT_ARGB_PRE)) {

            int channel = type == BufferedImage.TYPE_INT_RGB ? 3 : 4;
            int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            int len = pixels.length * channel;
            int[] npixels = new int[len];
            //反转拷贝
            for (int i = 0, j = pixels.length - 1; i < len; i += channel, j--) {
                System.arraycopy(Colors.bgra(pixels[j]), 0, npixels, i, channel);
            }
            mat.put(0, 0, npixels);

        } else if (type == BufferedImage.TYPE_INT_BGR) {

            int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            int len2 = pixels.length * 3;
            int[] npixels = new int[len2];
            for (int i = 0, j = 0; i < len2; j++) {
                npixels[i++] = (pixels[j] & 0xff0000) >> 16;
                npixels[i++] = (pixels[j] & 0xff00) >> 8;
                npixels[i++] = (pixels[j] & 0xff);
            }
            mat.put(0, 0, npixels);
        }

        return mat;
    }


    /**
     * 将OpenCV的Mat对象转换为BufferedImage对象
     *
     * @param mat OpenCV的Mat对象
     * @return BufferedImage对象
     */
    public static BufferedImage image(Mat mat) {

        int channel = mat.channels(), row = mat.rows(), col = mat.cols();
        int bufferSize = channel * row * col;

        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (channel > 3) {
            type = BufferedImage.TYPE_4BYTE_ABGR;
        } else if (channel > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        mat = bytelize(mat, row, col, CvType.CV_8UC(channel));
        byte[] b = new byte[bufferSize];
        mat.get(0, 0, b); // get all the pixels

        BufferedImage image = new BufferedImage(col, row, type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        if (type == BufferedImage.TYPE_4BYTE_ABGR) {
            //拷贝像素值,并将每个像素分量排布从BGRA转为ABGR
            for (int i = 0; i < bufferSize; i += 4) {
                targetPixels[i] = b[i + 3];
                System.arraycopy(b, i, targetPixels, i + 1, 3);
            }
        } else {
            //直接拷贝
            System.arraycopy(b, 0, targetPixels, 0, bufferSize);
        }
        return image;
    }


    /**
     * 判断传入的mat数据类型是否是byte类型,若是则返回原mat对象,若不是则返回:原mat对象转为数据类型为byte的新mat对象
     *
     * @param mat  OpenCV的Mat对象
     * @param rows 行数
     * @param cols 列数
     * @return 返回转换为byte类型的Mat后的对象，或原始byte类型的Mat对象。
     */
    private static Mat bytelize(Mat mat, int rows, int cols, int channel) {
        int t = mat.type();
        if (!(CvType.depth(t) == CvType.CV_8U || CvType.depth(t) == CvType.CV_8S)) {
            int type = CvType.CV_8UC(channel);//将通道转类型值
            Mat byteMat = new Mat(rows, cols, type);
            mat.convertTo(byteMat, type);
            return byteMat;
        }
        return mat;
    }
}
