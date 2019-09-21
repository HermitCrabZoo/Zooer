package com.zoo.code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Version;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.zoo.se.Chroma;
import com.zoo.se.Colors;

/**
 * This object renders a QR Code as a BitMatrix 2D array of greyscale values.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class EyeQRCodeWriter implements Writer {

  private static final int QUIET_ZONE_SIZE = 4;

  @Override
  public BitMatrix encode(String contents, BarcodeFormat format, int width, int height)
      throws WriterException {

    return encode(contents, format, width, height, null);
  }

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height,
                          Map<EncodeHintType,?> hints) throws WriterException {

    if (contents.isEmpty()) {
      throw new IllegalArgumentException("Found empty contents");
    }

    if (format != BarcodeFormat.QR_CODE) {
      throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
    }

    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' +
          height);
    }

    ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
    int quietZone = QUIET_ZONE_SIZE;
    if (hints != null) {
      if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
        errorCorrectionLevel = ErrorCorrectionLevel.valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
      }
      if (hints.containsKey(EncodeHintType.MARGIN)) {
        quietZone = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
      }
    }

    QRCode code = Encoder.encode(contents, errorCorrectionLevel, hints);
    return renderResult(code, width, height, quietZone);
  }

  // Note that the input matrix uses 0 == white, 1 == black, while the output matrix uses
  // 0 == black, 255 == white (i.e. an 8 bit greyscale bitmap).
  private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
    ByteMatrix input = code.getMatrix();
    if (input == null) {
      throw new IllegalStateException();
    }
    int inputWidth = input.getWidth();
    int inputHeight = input.getHeight();
    int qrWidth = inputWidth + (quietZone * 2);
    int qrHeight = inputHeight + (quietZone * 2);
    int outputWidth = Math.max(width, qrWidth);
    int outputHeight = Math.max(height, qrHeight);

    int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
    // Padding includes both the quiet zone and the extra white pixels to accommodate the requested
    // dimensions. For example, if input is 25x25 the QR will be 33x33 including the quiet zone.
    // If the requested size is 200x160, the multiple will be 4, for a QR of 132x132. These will
    // handle all the padding from 100x100 (the actual QR) up to 200x160.
    int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
    int topPadding = (outputHeight - (inputHeight * multiple)) / 2;

    BitMatrix output = new BitMatrix(outputWidth, outputHeight);

    for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
      // Write the contents of this row of the barcode
      for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
        if (input.get(inputX, inputY) == 1) {
          output.setRegion(outputX, outputY, multiple, multiple);
        }
      }
    }

    return output;
  }
  
  
  public BitMatrix encode(String contents,
          BarcodeFormat format,
          int width,
          int height,
          Map<EncodeHintType,?> hints,Integer topLeftColor, Integer topRightColor, Integer bottomLeftColor, boolean all,BufferedImage image) throws WriterException {
	  if (contents.isEmpty()) {
	      throw new IllegalArgumentException("Found empty contents");
	    }

	    if (format != BarcodeFormat.QR_CODE) {
	      throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
	    }

	    if (width < 0 || height < 0) {
	      throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' +
	          height);
	    }

	    ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
	    int quietZone = QUIET_ZONE_SIZE;
	    if (hints != null) {
	      if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
	        errorCorrectionLevel = ErrorCorrectionLevel.valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
	      }
	      if (hints.containsKey(EncodeHintType.MARGIN)) {
	        quietZone = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
	      }
	    }

	    QRCode code = Encoder.encode(contents, errorCorrectionLevel, hints);
	    BitMatrix matrix = renderResult(code, width, height, quietZone);
        Version version = code.getVersion();
        
        
		/// big things

        
        int black=Color.BLACK.getRGB();
        int white=Color.WHITE.getRGB();
        int leftTopColor=Color.ORANGE.getRGB();
        int leftBottomColor=leftTopColor;
        int rightTopColor=leftTopColor;
        
        black=Colors.randColor(Chroma.HEAVIEST).getRGB();
        black=Colors.randColor(Chroma.HEAVY).getRGB();
        
        
		int startModel = 2;
		int endModel = 5;
		if (all) {
			startModel = 0;
			endModel = 7;
		}
		int[] tl = matrix.getTopLeftOnBit();
		int totalModelNum = (version.getVersionNumber() - 1) * 4 + 5 + 16; // 获取单边模块数
		int resultWidth = width - 2 * (tl[0]);
		int modelWidth = resultWidth / totalModelNum; // 得到每个模块长度
		// 得到三个基准点的起始与终点
		int topEndX = tl[0] + modelWidth * endModel;
		int topStartX = tl[0] + modelWidth * startModel;
		int topStartY = tl[0] + modelWidth * startModel;
		int topEndY = tl[0] + modelWidth * endModel;
		int rightStartX = (totalModelNum - endModel) * modelWidth + tl[0];
		int rightEndX = width - modelWidth * startModel - tl[0];
		int leftStartY = height - modelWidth * endModel - tl[1];
		int leftEndY = height - modelWidth * startModel - tl[1];
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				if (x >= topStartX && x < topEndX && y >= topStartY && y < topEndY) {
					// 左上角颜色
					pixels[y * width + x] = matrix.get(x, y) ? leftTopColor : white;
				} else if (x < rightEndX && x >= rightStartX && y >= topStartY && y < topEndY) {
					// 右上角颜色
					pixels[y * width + x] = matrix.get(x, y) ? rightTopColor : white;
				} else if (x >= topStartX && x < topEndX && y >= leftStartY && y < leftEndY) {
					// 左下角颜色
					pixels[y * width + x] = matrix.get(x, y) ? leftBottomColor : white;
				} else {
					pixels[y * width + x] = matrix.get(x, y) ? black : white;
				}
				image.setRGB(x, y, pixels[y * width + x]);
			}
		}
        return matrix;
  }
  
}
