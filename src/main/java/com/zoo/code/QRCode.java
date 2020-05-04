package com.zoo.code;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zoo.base.Strs;
import com.zoo.cons.Charsets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * quick response code process tool
 *
 * @author ZOO
 */
public final class QRCode {

    private QRCode() {
    }


    private static MultiFormatWriter writer = new MultiFormatWriter();

    private static MultiFormatReader reader = new MultiFormatReader();

    private static Map<EncodeHintType, Object> writerHints = new HashMap<>() {
        private static final long serialVersionUID = 1L;

        {
            put(EncodeHintType.CHARACTER_SET, Charsets.UTF8);
            put(EncodeHintType.MARGIN, 0);
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        }
    };

    private static Map<DecodeHintType, Object> readerHints = new HashMap<>() {
        private static final long serialVersionUID = 1L;

        {
            put(DecodeHintType.CHARACTER_SET, Charsets.UTF8);
        }
    };


    /**
     * 通过任何非空字符串生成Quick Response Code 图片
     *
     * @param content 非空字符串
     * @param width   图片宽
     * @param height  图片高
     * @return 表示Quick Response Code图片的BufferedImage对象
     */
    public static BufferedImage qrCode(String content, int width, int height) {
        return qrCode(content, width, height, Color.BLACK, Color.WHITE);
    }


    /**
     * 通过任何非空字符串生成Quick Response Code 图片
     *
     * @param content 非空字符串
     * @param width   图片宽
     * @param height  图片高
     * @param color   前景颜色
     * @param bgColor 背景颜色
     * @return 表示Quick Response Code图片的BufferedImage对象
     */
    public static BufferedImage qrCode(String content, int width, int height, Color color, Color bgColor) {
		return qrCode(content, width, height, m -> MatrixBridge.toBufferedImage(m, Objects.requireNonNullElse(color, Color.BLACK), Objects.requireNonNullElse(bgColor, Color.WHITE)));
    }


    /**
     * 通过任何非空字符串生成Quick Response Code 图片
     *
     * @param content   非空字符串
     * @param width     图片宽
     * @param height    图片高
     * @param foreImage 前景图
     * @param backImage 背景图
     * @return 表示Quick Response Code图片的BufferedImage对象
     */
    public static BufferedImage qrCode(String content, int width, int height, BufferedImage foreImage, BufferedImage backImage) {
		return qrCode(content, width, height, m -> MatrixBridge.toBufferedImage(m, Objects.requireNonNullElse(foreImage, MatrixBridge.FixedBufferedImage.BACK), Objects.requireNonNullElse(backImage, MatrixBridge.FixedBufferedImage.WHITE)));
    }



    private static BufferedImage qrCode(String content, int width, int height, Function<BitMatrix, BufferedImage> function) {
		try {
			BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, writerHints);
			return function.apply(matrix);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * 通过任何非空字符串生成Quick Response Code 图片
     *
     * @param content   非空字符串
     * @param width     图片宽
     * @param height    图片高
     * @param foreColor 前景颜色
     * @param backImage 背景图
     * @return 表示Quick Response Code图片的BufferedImage对象
     */
    public static BufferedImage qrCode(String content, int width, int height, Color foreColor, BufferedImage backImage) {
        BufferedImage foreImage = new MatrixBridge.FixedBufferedImage(Objects.requireNonNullElse(foreColor, Color.BLACK).getRGB());
        return qrCode(content, width, height, m -> MatrixBridge.toBufferedImage(m, foreImage, Objects.requireNonNullElse(backImage, MatrixBridge.FixedBufferedImage.WHITE)));
    }


    /**
     * 通过任何非空字符串生成Quick Response Code 图片
     *
     * @param content   非空字符串
     * @param width     图片宽
     * @param height    图片高
     * @param foreImage 前景图
     * @param backColor 背景颜色
     * @return 表示Quick Response Code图片的BufferedImage对象
     */
    public static BufferedImage qrCode(String content, int width, int height, BufferedImage foreImage, Color backColor) {
        BufferedImage backImage = new MatrixBridge.FixedBufferedImage(Objects.requireNonNullElse(backColor, Color.WHITE).getRGB());
        return qrCode(content, width, height, m -> MatrixBridge.toBufferedImage(m, Objects.requireNonNullElse(foreImage, MatrixBridge.FixedBufferedImage.BACK), backImage));
    }


    public static BufferedImage qrCode(String content, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        if (content != null && !content.isEmpty()) {
            try {
                new EyeQRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, writerHints, null, null, null, true, image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return image;
    }


    /**
     * 从BufferedImage处读取Quick Response Code 并解析成字符串返回,若解析失败将返回空字符串
     *
     * @param image 表示Quick Response Code图片的BufferedImage对象
     * @return 解码后的字符串
     */
    public static String decode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(binarizer);
            Result result = reader.decode(bitmap, readerHints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Strs.empty();
    }
}
