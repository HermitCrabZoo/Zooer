package com.zoo.code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;

import com.google.zxing.common.BitMatrix;
import com.zoo.se.Imgs;

public final class MatrixBridge {
    private MatrixBridge() {
    }

    /**
     * 矩阵转换为BufferedImage
     *
     * @param matrix 位矩阵
     * @return 位矩阵对应的BufferedImage对象
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        return toBufferedImage(matrix, Color.BLACK, Color.WHITE);
    }

    /**
     * 矩阵转换为BufferedImage,并指定前景和背景色
     *
     * @param matrix    位矩阵
     * @param foreColor 前景色
     * @param backColor 背景色
     * @return 位矩阵对应的BufferedImage对象
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix, Color foreColor, Color backColor) {
        Objects.requireNonNull(matrix, "matrix");
        Objects.requireNonNull(foreColor, "foreColor");
        Objects.requireNonNull(backColor, "backColor");

        int fc = foreColor.getRGB(), bc = backColor.getRGB();
        int width = matrix.getWidth(), height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? fc : bc);
            }
        }
        return image;
    }


    /**
     * 矩阵转换为BufferedImage,并指定前景图和背景图
     *
     * @param matrix    位矩阵
     * @param foreImage 前景图
     * @param backImage 背景图
     * @return 位矩阵对应的BufferedImage对象
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix, BufferedImage foreImage, BufferedImage backImage) {
        Objects.requireNonNull(matrix, "matrix");
        Objects.requireNonNull(foreImage, "foreImage");
        Objects.requireNonNull(backImage, "backImage");

        int width = matrix.getWidth(), height = matrix.getHeight();
        //宽高与矩阵不同则调整为相同的
        if (foreImage.getWidth() != width || foreImage.getHeight() != height) {
            foreImage = Imgs.of(foreImage).cutLeftTop(width, height).abgr().get();
        }
        if (backImage.getWidth() != width || backImage.getHeight() != height) {
            backImage = Imgs.of(backImage).cutLeftTop(width, height).abgr().get();
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? foreImage.getRGB(x, y) : backImage.getRGB(x, y));
            }
        }
        return image;
    }


    static class FixedBufferedImage extends BufferedImage {

        static final FixedBufferedImage BACK = new FixedBufferedImage(0xFF000000);

        static final FixedBufferedImage WHITE = new FixedBufferedImage(0xFFFFFFFF);

        private final int color;

        public FixedBufferedImage(int color) {
            super(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
            this.color = color;
        }


        @Override
        public int getRGB(int x, int y) {
            return color;
        }
    }
}
