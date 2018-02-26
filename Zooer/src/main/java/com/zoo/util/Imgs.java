package com.zoo.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import com.zoo.cons.Images;

import sun.font.FontDesignMetrics;

public final class Imgs {
	
	/**
	 * 当前对象绑定的图像
	 */
	private BufferedImage image;
	
	private BufferedImage oldImage;
	
	private boolean written=false;
	
	/**
	 * 灰度图像转换工具
	 */
	private static ColorConvertOp colorConvertOp=new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
	
	/**
	 * 推荐的图片字节密度与最高压缩质量之间对应的值<br/>
	 * 其中每个数组元素下标为0的值代表图片按png格式转换为byte[]数组时，数组长度与图片宽高像素数乘积之间的比值。该比值代表图片字节密度，大于等于该值时，使用当前数组元素下标为1的值作为参考的最高压缩质量，大于该压缩质量的压缩将是不被推荐的。<br/>
	 * 如：图片字节密度大于1.343，压缩质量应该小于等于0.8，否则压缩效果可能不理想。
	 */
	private final static double[][] RESTRICTS= {{1.343,0.8},{1.224,0.7},{1.089,0.5},{0.946,0.4},{0.825,0.3},{0.702,0.3},{0.584,0.2},{0.465,0.2},{0.308,0.0}};
	
	/**
	 * 该数组中每个数组元素中下标为0的值代表图片字节密度大于等于该值时，系统推荐的压缩质量为数组元素中下标为1的值。<br/>
	 * 如：图片字节密度大于等于0.946，压缩质量为0.5较为理想。
	 */
	private final static double[][] SUGGESTS= {{0.946,0.5},{0.825,0.6}};
	
	private Imgs() {}
	
	private Imgs(BufferedImage image) {
		this.image=image;
	}
	
	/**
	 * 根据给定的BufferedImage对象的拷贝构造一个Imgs对象,即后续操作将不改变传入的原始Image对象
	 * @param image
	 * @throws NullPointerException 如果image参数为null，则抛出此异常
	 * @return
	 */
	public static Imgs of(BufferedImage image) {
		assertNull(image);
		return new Imgs(copy(image));
	}
	
	/**
	 * 构造一个未关联image属性的Imgs对象，后续对image的操作前必须先关联当前对象的image属性到一个已存在的BufferedImage实例。
	 * @return
	 */
	public static Imgs ofNull() {
		return new Imgs();
	}
	
	/**
	 * 根据给定大小构造一个Imgs对象,该对象绑定的BufferedImage对象的颜色为黑色透明。
	 * @param width
	 * @param height
	 * @param color
	 * @return
	 */
	public static Imgs ofNew(int width,int height){
		return ofNew(width, height, Colors.bTransparent);
	}
	
	/**
	 * 根据给定大小、色系构造一个Imgs对象
	 * @param width
	 * @param height
	 * @param chroma
	 * @return
	 */
	public static Imgs ofNew(int width,int height,Chroma chroma){
		return ofNew(width, height, Colors.randColor(chroma));
	}
	
	/**
	 * 根据给定大小、颜色构造一个Imgs对象
	 * @param width
	 * @param height
	 * @param color
	 * @return
	 */
	public static Imgs ofNew(int width,int height,Color color){
		return Imgs.of(newImg(0, 0, width, height, color));
	}
	
	/**
	 * 从path中读取图片构造一个Imgs对象
	 * @param path
	 * @return
	 */
	public static Imgs of(Path path) {
		BufferedImage image=null;
		try {
			image=ImageIO.read(path.toFile());
		} catch (Exception e) {}
		return ofNull().set(image);
	}
	
	/**
	 * 从url中读取图片构造一个Imgs对象
	 * @param url
	 * @return
	 */
	public static Imgs of(URL url) {
		BufferedImage image=null;
		try {
			image=ImageIO.read(url);
		} catch (Exception e) {}
		return ofNull().set(image);
	}
	
	/**
	 * 从inputStream中读取图片构造一个Imgs对象
	 * @param inputStream
	 * @return
	 */
	public static Imgs of(InputStream inputStream) {
		BufferedImage image=null;
		try {
			image=ImageIO.read(inputStream);
		} catch (Exception e) {}
		return ofNull().set(image);
	}
	
	/**
	 * 从imageInputStream中读取图片构造一个Imgs对象
	 * @param imageInputStream
	 * @return
	 */
	public static Imgs of(ImageInputStream imageInputStream) {
		BufferedImage image=null;
		try {
			image=ImageIO.read(imageInputStream);
		} catch (Exception e) {}
		return ofNull().set(image);
	}
	
	/**
	 * 用传入的BufferedImage的拷贝来绑定当前的对象，后续操作不会改变传入的BufferedImage对象
	 * @param image
	 * @throws NullPointerException 如果image参数为null，则抛出此异常
	 * @return
	 */
	public Imgs set(BufferedImage image) {
		assertNull(image);
		this.image=copy(image);
		return this;
	}
	
	/**
	 * 将当前关联的图片输出到path中
	 * @param formatName
	 * @param path
	 * @return
	 */
	public Imgs write(String formatName, Path path) {
		try {
			written=ImageIO.write(image, formatName, path.toFile());
		} catch (Exception e) {
			written=false;
		}
		return this;
	}
	
	/**
	 * 将当前关联的图片输出到outputStream中
	 * @param outputStream
	 * @param path
	 * @return
	 */
	public Imgs write(String formatName, OutputStream outputStream) {
		try {
			written=ImageIO.write(image, formatName, outputStream);
		} catch (Exception e) {
			written=false;
		}
		return this;
	}
	
	/**
	 * 将当前关联的图片输出到imageOutputStream中
	 * @param imageOutputStream
	 * @param path
	 * @return
	 */
	public Imgs write(String formatName, ImageOutputStream imageOutputStream) {
		try {
			written=ImageIO.write(image, formatName, imageOutputStream);
		} catch (Exception e) {
			written=false;
		}
		return this;
	}
	
	
	/**
	 * 返回当前对象关联的BufferedImage实例
	 * @return
	 */
	public BufferedImage get() {
		return image;
	}
	
	/**
     * Return the image if present, otherwise return {@code other}.
     *
     * @param other the image to be returned if there is no value present, may
     * be null
     * @return the image, if present, otherwise {@code other}
     */
    public BufferedImage orElse(BufferedImage other) {
        return image != null ? image : other;
    }
    
	/**
     * Return the image if present, otherwise invoke {@code other} and return
     * the result of that invocation.
     *
     * @param other a {@code Supplier} whose result is returned if no image
     * is present
     * @return the image if present otherwise the result of {@code other.get()}
     * @throws NullPointerException if image is not present and {@code other} is
     * null
     */
	public BufferedImage orElseGet(Supplier<? extends BufferedImage> other) {
		return image!=null?image:other.get();
	}
	
	/**
	 * 判断当前关联的图片对象是否为null
	 * @return
	 */
	public boolean empty() {
		return image==null;
	}
	
	/**
	 * 获取最近一次将当前对象关联的图片写到某些介质中是否成功的标志
	 * @return
	 */
	public boolean written() {
		return written;
	}
	
	/**
	 * 释放当前关联的图片对象
	 * @return
	 */
	public Imgs release() {
		image=null;
		oldImage=null;
		written=false;
		return this;
	}
	
	/**
	 * 做某些额外的操作,参数freedom的doIt方法的第一个参数为当前绑定的BufferedImage对象,第二个参数为上一个(旧的)与当前绑定的BufferedImage对象,第三个参数为当前的Imgs对象
	 * @param freedom
	 * @return
	 */
	public Imgs free(Eacher<BufferedImage, BufferedImage, Imgs> freedom) {
		freedom.doIt(image, oldImage, this);
		return this;
	}
	
	/**
	 * 根据给定大小生成黑色透明的图片
	 * @param width
	 * @param height
	 * @return
	 */
	public Imgs image(int width,int height){
		return image(0,0,width,height,null);
	}
	
	/**
	 * 根据给定大小和色系该色系内的颜色的图片
	 * @param width
	 * @param height
	 * @param chroma 色系
	 * @return
	 */
	public Imgs image(int width,int height,Chroma chroma){
		return image(0,0,width,height,Colors.randColor(chroma));
	}
	
	/**
	 * 根据给定大小、颜色生成图片
	 * @param width
	 * @param height
	 * @param color
	 * @return
	 */
	public Imgs image(int width,int height,Color color){
		return image(0,0,width,height,color);
	}
	
	/**
	 * 根据给定大小、颜色生成图片
	 * @param dimen
	 * @param color
	 * @return
	 */
	public Imgs image(Dimension dimen,Color color){
		return image(0,0,dimen.width,dimen.height,color);
	}
	
	/**
	 * 根据给定坐标、大小、颜色生成图片
	 * @param dimen
	 * @param color
	 * @return
	 */
	public Imgs image(Point point,Dimension dimen,Color color){
		return image(point.x,point.y,dimen.width,dimen.height,color);
	}
	
	/**
	 * 根据给定大小、坐标、颜色生成图片
	 * @param rect
	 * @param color
	 * @return
	 */
	public Imgs image(Rectangle rect,Color color){
		return image(rect.x, rect.y, rect.width, rect.height, color);
	}
	
	/**
	 * 根据给定大小、坐标、颜色生成图片，若color为null那么将使用透明黑色
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @return
	 */
	public Imgs image(int x,int y,int width,int height,Color color){
		return update(newImg(x, y, width, height, color));
	}
	
	/**
	 * 生成新的BufferedImage对象
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @return
	 */
	private static BufferedImage newImg(int x,int y,int width,int height,Color color) {
		BufferedImage image =new BufferedImage(width, height,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = image.createGraphics();
		g.setColor(Optional.ofNullable(color).orElse(Colors.bTransparent));
		g.fillRect(x, y, width, height);
		g.dispose();
		return image;
	}
	
	/**
	 * 拷贝出一个与传入对象一样的新对象
	 * @param image
	 * @return
	 */
	private static BufferedImage copy(BufferedImage image) {
		BufferedImage ni=new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		ni.setData(image.getData());
		return ni;
	}
	
	/**
	 * 将当前的图像关联到传入参数image,{@link #oldImage}则关联到原image对象
	 * @param image
	 * @return
	 */
	private Imgs update(BufferedImage image) {
		this.oldImage=this.image;
		this.image=image;
		return this;
	}
	
	/**
	 * 如果image为null将抛异常
	 * @param image
	 */
	private static void assertNull(BufferedImage image) {
		if (image==null) {
			throw new NullPointerException("Argument image cant not be null!");
		}
	}
	
	/**
	 * 在图片上写字,默认字体、颜色,位置居中
	 * @param surface
	 * @return
	 */
	public Imgs pile(String surface){
		return pile(surface, null,null);
	}
	
	/**
	 * 在图片上写字,默认字体,位置居中
	 * @param surface
	 * @return
	 */
	public Imgs pile(String surface,Font font){
		return pile(surface,null,font);
	}
	
	/**
	 * 在图片上写字,默认字体,位置居中
	 * @param surface
	 * @return
	 */
	public Imgs pile(String surface,Color color){
		return pile(surface,color,null);
	}
	
	/**
	 * 在图片上写字,默认字体,位置居中
	 * @param surface 文本
	 * @param color 字体颜色
	 * @param font 字体类型
	 * @return
	 */
	public Imgs pile(String surface,Color color,Font font){
		if (Strs.notEmpty(surface)) {
			FontMetrics fm=FontDesignMetrics.getMetrics(Optional.ofNullable(font).orElse(image.getGraphics().getFont()));
			pile(surface, (image.getWidth()-fm.stringWidth(surface))/2,(image.getHeight()+fm.getAscent()-fm.getDescent())/2,color, font);
		}
		return this;
	}
	
	/**
	 * 在图片上写字
	 * @param surface 文本
	 * @param point 字体位置
	 * @param color 字体颜色
	 * @param font 字体类型
	 * @return
	 */
	public Imgs pile(String surface,Point point,Color color,Font font) {
		point=Optional.ofNullable(point).orElse(new Point(0, 0));
		return pile(surface, point.x, point.y, color, font);
	}
	
	/**
	 * 在图片上写字
	 * @param surface 文本
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @param color 字体颜色
	 * @param font 字体类型
	 * @return
	 */
	public Imgs pile(String surface,int x,int y,Color color,Font font){
		if(Strs.notEmpty(surface)) {
			Graphics2D g=image.createGraphics();
			g.setColor(Optional.ofNullable(color).orElse(g.getColor()));
			g.setFont(Optional.ofNullable(font).orElse(g.getFont()));
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
			g.drawString(surface, x, y);
			g.dispose();
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层左上角,返回base.
	 * @param cover
	 * @return
	 */
	public Imgs pileLeftTop(Image cover){
		return pile(cover,0, 0);
	}
	
	/**
	 * 将cover画在base上层左侧中部,返回base.
	 * @param cover
	 * @return
	 */
	public Imgs pileLeft(Image cover){
		if (cover!=null) {
			pile(cover,0, (image.getHeight()-cover.getHeight(null))/2);
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层左下角,返回base.
	 * @param cover
	 * @return
	 */
	public Imgs pileLeftBottom(Image cover){
		if (cover!=null) {
			pile(cover,0,image.getHeight()-cover.getHeight(null));
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层右上角,返回base.
	 * @param cover
	 * @return
	 */
	public Imgs pileRightTop(Image cover){
		if (cover!=null) {
			pile(cover,image.getWidth()-cover.getWidth(null), 0);
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层右侧中部,返回base.
	 * @param cover
	 * @return
	 */
	public Imgs pileRight(Image cover){
		if (cover!=null) {
			pile(cover,image.getWidth()-cover.getWidth(null), (image.getHeight()-cover.getHeight(null))/2);
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层右下角,返回base.
	 * @param cover
	 * @return
	 */
	public Imgs pileRightBottom(Image cover){
		if (cover!=null) {
			pile(cover,image.getWidth()-cover.getWidth(null),image.getHeight()-cover.getHeight(null));
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层水平居中上方,返回base.
	 * @param cover
	 * @return
	 */
	public Imgs pileTop(Image cover){
		if (cover!=null) {
			pile(cover,(image.getWidth()-cover.getWidth(null))/2, 0);
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层水平垂直居中,返回base.
	 * @param cover
	 * @return
	 */
	public Imgs pileCenter(Image cover){
		if (cover!=null) {
			pile(cover,(image.getWidth()-cover.getWidth(null))/2, (image.getHeight()-cover.getHeight(null))/2);
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层水平居中下方,返回base.
	 * @param cover
	 * @return
	 */
	public Imgs pileBottom(Image cover){
		if (cover!=null) {
			pile(cover,(image.getWidth()-cover.getWidth(null))/2,image.getHeight()-cover.getHeight(null));
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层,返回base
	 * @param cover
	 * @param x
	 * @param y
	 * @return
	 */
	public Imgs pile(Image cover,int x,int y){
		if (cover!=null) {
			pile(cover, x, y,cover.getWidth(null),cover.getHeight(null));
		}
		return this;
	}
	
	/**
	 * 将cover画在base上层,返回base
	 * @param cover
	 * @param point
	 * @return
	 */
	public Imgs pile(Image cover,Point point){
		if (cover!=null) {
			point=Optional.ofNullable(point).orElse(new Point(0, 0));
			pile(cover, point.x, point.y, cover.getWidth(null), cover.getHeight(null));
		}
		return this;
	}
	
	/**
	 * 将cover以给定的rect信息确定与base的相对坐标和大小画在base上面
	 * @param cover 表面层图片
	 * @param rect 表面层图片的坐标、大小
	 * @return 修改后的base对象
	 */
	public Imgs pile(Image cover,Rectangle rect){
		if (cover!=null) {
			rect=Optional.ofNullable(rect).orElse(new Rectangle(0, 0, cover.getWidth(null), cover.getHeight(null)));
			pile(cover, rect.x, rect.y, rect.width, rect.height);
		}
		return this;
	}
	
	/**
	 * 将cover以给定的rect信息确定与base的相对坐标和大小画在base上面
	 * @param cover 表面层图片
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @param w 宽
	 * @param h 高
	 * @return 修改后的base对象
	 */
	public Imgs pile(Image cover,int x,int y,int w,int h){
		if(cover!=null) {
			Graphics2D g=image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(cover, x, y, w, h, null);
			g.dispose();
		}
		return this;
	}
	
	/**
	 * 将原始图片按比例缩放输出(输出图片不变形)
	 * @param ratio 缩放率
	 * @return 若基于缩放率为ratio下计算到的宽或高小于1,那么将不缩放
	 */
	public Imgs scale(double ratio){
		int width=(int) Math.round(image.getWidth()*ratio),height=(int) Math.round(image.getHeight()*ratio);
		return scaleZoom(width, height);
	}
	
	/**
	 * 基于宽的按比例缩放,输出图片宽高比不变(输出图片不变形)
	 * @param w 输出图片的宽度
	 * @return 若高w小于1或在宽为w下计算出来的高小于1,那么将不缩放
	 */
	public Imgs scaleWidth(int w){
		int h=(int) Math.round(w*1.0/image.getWidth()*image.getHeight());
		return scaleZoom(w, h);
    }
	
	/**
	 * 基于高的按比例缩放,输出图片宽高比不变(输出图片不变形)
	 * @param h 输出图片的高度
	 * @return 若高h小于1或在高为h下计算出来的宽小于1,那么将不缩放
	 */
	public Imgs scaleHeight(int h){
		int w=(int) Math.round(h*1.0/image.getHeight()*image.getWidth());
		return scaleZoom(w, h);
	}
	
	/**
	 * 图片缩放,输出图片将按输入的宽高比进行缩放输出(输出宽高比与原始图片宽高比不一致可能导致输出图片效果变形)
	 * @param h 输出图片的高
	 * @param w 输出图片的宽
	 * @return 若w或h小于1,那么将不缩放
	 */
	public Imgs scaleZoom(int w, int h){
		if (w>0&&h>0) {
			Image cover=scale(w, h);
			image(w, h).pileLeftTop(cover);
		}
        return this;
    }
	
	/**
	 * 图片缩放,输出图片将按传入的宽高参数输出,若输入宽高比与原图片宽高比不一致,那么原始图片将按原始宽高比被缩放至刚好可以放入输出宽高大小的图片大小中,其余透明色填充.
	 * @param h 输出图片的高
	 * @param w 输出图片的宽
	 * @return
	 */
	public Imgs scaleRatioBox(int w, int h){
		return scaleRatioBox(w, h, Colors.bTransparent);
	}
	
	/**
	 * 图片缩放,输出图片将按输入的宽高参数输出,若输入宽高比与原图片宽高比不一致,那么原始图片将按原始宽高比被缩放至刚好可以放入输出宽高大小的图片大小中,其余用bgColor填充.
	 * @param h 输出图片的高
	 * @param w 输出图片的宽
	 * @param bgColor 背景ARGB
	 * @return
	 */
	public Imgs scaleRatioBox(int w, int h,Color bgColor){
		Image cover=scaleRatio(w, h).image;
		return image(w, h, bgColor).pileCenter(cover);
	}
	
	/**
	 * 图片缩放,图片将按原始比例缩放到刚好可以放入宽为w高为h的容器中的大小(输出图片不变形).
	 * @param w
	 * @param h
	 * @return 若图片无法缩放到能放到宽为w高为h的容器中,那么将不缩放
	 */
	public Imgs scaleRatio(int w, int h) {
		double ratioX=w*1.0/image.getWidth();
		double ratioY=h*1.0/image.getHeight();
		if (ratioX<ratioY) {
			h=(int) Math.round(w*1.0/image.getWidth()*image.getHeight());
		}else {
			w=(int) Math.round(h*1.0/image.getHeight()*image.getWidth());
		}
		if (w>0&&h>0) {
			Image cover=scale(w, h);
			image(w, h).pileLeftTop(cover);
		}
		return this;
	}
	
	/**
	 * 通过宽高获取缩放实例
	 * @param w
	 * @param h
	 * @return
	 */
	private Image scale(int w,int h){
        return image.getScaledInstance(w, h,BufferedImage.SCALE_SMOOTH);
	}
	
	/**
	 * 裁剪图片的前面部分，裁剪的图片宽高值为原图片宽高值中较小的一个值。
	 * @return 返回宽高值相同的图片
	 */
	public Imgs cutFront(){
		return cutLeftTop(Math.min(image.getWidth(), image.getHeight()));
	}
	
	/**
	 * 裁剪图片的中间部分，裁剪的图片宽高值为原图片宽高值中较小的一个值。
	 * @return 返回宽高值相同的图片
	 */
	public Imgs cutCenter(){
		return cutCenter(Math.min(image.getWidth(), image.getHeight()));
	}
	
	/**
	 * 裁剪图片的后面部分，裁剪的图片宽高值为原图片宽高值中较小的一个值。
	 * @return 返回宽高值相同的图片
	 */
	public Imgs cutBehind(){
		return cutRightBottom(Math.min(image.getWidth(), image.getHeight()));
	}
	
	/**
	 * 从左上角处裁剪出宽高为w的图片
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public Imgs cutLeftTop(int w){
		return cutLeftTop(w, w);
	}
	
	/**
	 * 从左边中间处裁剪出宽高为w的图片
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public Imgs cutLeft(int w){
		return cutLeft(w, w);
	}
	
	/**
	 * 从左下角处裁剪出宽高为w的图片
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public Imgs cutLeftBottom(int w){
		return cutLeftBottom(w, w);
	}
	
	/**
	 * 从右上角处裁剪出宽高为w的图片
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public Imgs cutRightTop(int w){
		return cutRightTop(w, w);
	}
	
	/**
	 * 从右边中间处裁剪出宽高为w的图片
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public Imgs cutRight(int w){
		return cutRight(w, w);
	}
	
	/**
	 * 从右下角处裁剪出宽高为w的图片
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public Imgs cutRightBottom(int w){
		return cutRightBottom(w, w);
	}
	
	/**
	 * 从上边中间处裁剪出宽高为w的图片
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public Imgs cutTop(int w){
		return cutTop(w, w);
	}
	
	/**
	 * 从中间处裁剪出宽高为w的图片
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public Imgs cutCenter(int w){
		return cutCenter( w, w);
	}
	
	/**
	 * 从下边中间处裁剪出宽高为w的图片
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public Imgs cutBottom(int w){
		return cutBottom(w, w);
	}
	
	/**
	 * 从左上角处裁剪出宽为w高为h的图片
	 * @param w
	 * @param h
	 * @return 
	 */
	public Imgs cutLeftTop(int w,int h){
		return cut(0,0, w, h);
	}
	
	/**
	 * 从左边中间处裁剪出宽为w高为h的图片
	 * @param w
	 * @param h
	 * @return 
	 */
	public Imgs cutLeft(int w,int h){
		return cut(0,(image.getHeight()-h)/2, w, h);
	}
	
	/**
	 * 从左下角处裁剪出宽为w高为h的图片
	 * @param w
	 * @param h
	 * @return 
	 */
	public Imgs cutLeftBottom(int w,int h){
		return cut(0,image.getHeight()-h, w, h);
	}
	
	/**
	 * 从右上角处裁剪出宽为w高为h的图片
	 * @param w
	 * @param h
	 * @return 
	 */
	public Imgs cutRightTop(int w,int h){
		return cut(image.getWidth()-w,0, w, h);
	}
	
	/**
	 * 从右边中间处裁剪出宽为w高为h的图片
	 * @param w
	 * @param h
	 * @return 
	 */
	public Imgs cutRight(int w,int h){
		return cut(image.getWidth()-w,(image.getHeight()-h)/2, w, h);
	}
	
	/**
	 * 从右下角处裁剪出宽为w高为h的图片
	 * @param w
	 * @param h
	 * @return 
	 */
	public Imgs cutRightBottom(int w,int h){
		return cut(image.getWidth()-w,image.getHeight()-h, w, h);
	}
	
	/**
	 * 从上边中间处裁剪出宽为w高为h的图片
	 * @param w
	 * @param h
	 * @return 
	 */
	public Imgs cutTop(int w,int h){
		return cut((image.getWidth()-w)/2,0, w, h);
	}
	
	/**
	 * 从中间处裁剪出宽为w高为h的图片
	 * @param w
	 * @param h
	 * @return 
	 */
	public Imgs cutCenter(int w,int h){
		return cut((image.getWidth()-w)/2,(image.getHeight()-h)/2, w, h);
	}
	
	/**
	 * 从下边中间处裁剪出宽为w高为h的图片
	 * @param w 输出图片的宽
	 * @param h 输出图片的高
	 * @return 
	 */
	public Imgs cutBottom(int w,int h){
		return cut((image.getWidth()-w)/2,image.getHeight()-h, w, h);
	}
	
	/**
	 * 图片裁剪，从x,y处将图片裁剪成宽w高h
	 * @param x
	 * @param y
	 * @param w 图片宽
	 * @param h 图片高
	 * @return 
	 */
	public Imgs cut(int x,int y,int w, int h){
		if (w>0 && h>0) {
			int width=image.getWidth();
			int height=image.getHeight();
			image(0, 0, w, h, Colors.wTransparent);
			if(x<width && y<height && x+w>0 && y+h>0){
				x=Math.max(0, x);
				y=Math.max(0, y);
				w=w+x>width?width-x:w;
				h=h+y>height?height-y:h;
				pileCenter(this.oldImage.getSubimage(x, y, w, h));
			}
		}
		return this;
	}
	
	/**
	 * 在图片外围上加边框(图片尺寸将变大)，并将图片边框的内角和外角变成圆角
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param radius 内外圆角直径
	 * @return
	 */
	public Imgs borderRiseRadius(int w,Color borderColor,int radius){
		return borderRiseRadius(w, borderColor, radius, radius);
	}
	
	/**
	 * 在图片外围上加边框(图片尺寸不变)，并将图片边框的内角和外角变成圆角
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param radius 内外圆角直径
	 * @return
	 */
	public Imgs borderDropRadius(int w,Color borderColor,int radius){
		return borderDropRadius(w, borderColor, radius, radius);
	}
	
	/**
	 * 在图片外围上加边框(图片尺寸将变大)，并将图片边框的内角和外角变成圆角
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param inRadius 内圆角直径
	 * @param outRadius 外圆角直径
	 * @return
	 */
	public Imgs borderRiseRadius(int w,Color borderColor,int inRadius,int outRadius){
		return radius(inRadius).borderRise(w, borderColor).radius(outRadius);
	}
	
	/**
	 * 在图片外围上加边框(图片尺寸不变)，并将图片边框的内角和外角变成圆角
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param inRadius 内圆角直径
	 * @param outRadius 外圆角直径
	 * @return
	 */
	public Imgs borderDropRadius(int w,Color borderColor,int inRadius,int outRadius){
		return radius(inRadius).borderDrop(w, borderColor).radius(outRadius);
	}
	
	/**
	 * 在图片外围上加边框(图片尺寸将变大)，并将图片边框外角变成圆角
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param outRadius 圆角直径
	 * @return
	 */
	public Imgs borderRiseRadiusOut(int w,Color borderColor,int outRadius){
		return borderRise(w, borderColor).radius(outRadius);
	}
	
	/**
	 * 在图片外围上加边框(图片尺寸将变大)，并将图片边框的内角变成圆角
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param inRadius 圆角直径
	 * @return
	 */
	public Imgs borderRiseRadiusIn(int w,Color borderColor,int inRadius){
		return radius(inRadius).borderRise(w, borderColor);
	}
	
	/**
	 * 在图片外围上加边框(图片尺寸不变)，并将图片边框外角变成圆角
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param outRadius 圆角直径
	 * @return
	 */
	public Imgs borderDropRadiusOut(int w,Color borderColor,int outRadius){
		return borderDrop(w, borderColor).radius(outRadius);
	}
	
	/**
	 * 在图片外围上加边框(图片尺寸不变)，并将图片边框内角变成圆角
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param inRadius 圆角直径
	 * @return
	 */
	public Imgs borderDropRadiusIn(int w,Color borderColor,int inRadius){
		return radius(inRadius).borderDrop(w, borderColor);
	}
	
	/**
	 * 将图片变成圆形,原图片不变,返回新图片.若原图片宽高不相同,那么将按原图片宽高值中较小的一个值为宽高,从原图片中间部位裁剪出宽高相等的图片,再将裁剪出来的图片变成圆形.
	 * @return
	 */
	public Imgs circle() {
		if (image.getWidth()==image.getHeight()) {
			return radius(image.getWidth(), null);
		}else {
			return cutCenter().radius(image.getWidth(), null);
		}
	}
	
	/**
	 * 生成圆角图片，圆角外透明
	 * @param radius 圆角直径
	 * @return
	 */
	public Imgs radius(int radius) {
		return radius(radius, null);
	}
	
	/**
	 * 生成圆角图片，圆角外borderColor做底色
	 * @param radius 圆角直径
	 * @param bgColor 圆角外的背景色
	 * @return
	 */
	public Imgs radius(int radius,Color bgColor) {
		image(image.getWidth(), image.getHeight(), bgColor);
		Graphics2D g=image.createGraphics();
		g.drawImage(oldImage, 0, 0, null);
		if(radius>0) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			RoundRectangle2D round = new RoundRectangle2D.Double(0, 0, image.getWidth(),image.getHeight(), radius, radius);
			Area clear = new Area(new Rectangle(0, 0, image.getWidth(),image.getHeight()));  
			clear.subtract(new Area(round));  
			g.setComposite(AlphaComposite.Clear);  
			g.fill(clear);
		}
		g.dispose();
		return this;
	}
	
	/**
	 * 在图片外围上加边框(图片尺寸将变大)
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @return
	 */
	public Imgs borderRise(int w,Color borderColor){
		return image(image.getWidth()+w*2, image.getHeight()+w*2, borderColor).pile(oldImage, w, w);
	}
	/**
	 * 在图片外围上加边框(图片尺寸不变)
	 * @param w  边框宽度
	 * @param borderColor 边框颜色
	 * @return
	 */
	public Imgs borderDrop(int w,Color borderColor){
		return scaleZoom(image.getWidth()-w*2, image.getHeight()-w*2).image(oldImage.getWidth(), oldImage.getHeight(),borderColor).pile(oldImage,w, w);
	}
	
	/**
	 * 在图片外圈加边框(图片尺寸不变),原图部分内容会被边框覆盖
	 * @param w
	 * @param borderColor
	 * @return
	 */
	public Imgs border(int w,Color borderColor){
		return cutCenter(image.getWidth()-w*2, image.getHeight()-w*2).image(oldImage.getWidth(), oldImage.getHeight(),borderColor).pile(oldImage,w, w);
	}
	
	/**
	 * 图片加阴影(未完成)
	 * @param color
	 * @param w
	 * @param alpha
	 * @return
	 */
	public Imgs shadow(Color color, int w, double alpha) {
		BufferedImage bufImg = new BufferedImage(this.image.getWidth() + w, this.image.getHeight() + w,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = bufImg.createGraphics();
		// 画阴影部分
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.XOR, (float) alpha));
		g.setColor(Color.black);
		g.fillRect(w, w, this.image.getWidth(), this.image.getHeight());
		g.dispose();
		// 画源图像
		g = bufImg.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		g.drawImage(this.image, 0, 0, null);
		g.dispose();
		update(bufImg);
		return this;
	}
	
	/**
	 * 为图片添加背景颜色
	 * @param bgColor
	 * @return
	 */
	public Imgs bg(Color bgColor) {
		if (bgColor!=null) {
			image(image.getWidth(), image.getHeight(), bgColor).pile(oldImage, 0, 0);
		}
		return this;
	}
	
	/**
	 * 为图片添加给定色系的背景颜色
	 * @param bgColor
	 * @return
	 * @see {@link #bg(Color)}
	 */
	public Imgs bg(Chroma bgColorChroma) {
		return bg(Colors.randColor(bgColorChroma));
	}
	
	/**
	 * 将图片转为黑白色
	 * @return
	 */
	public Imgs gray() {
		this.image=colorConvertOp.filter(image, null);
		return this;
	}
	
	/**
	 * 将图片按y轴水平方向的翻转
	 * @return
	 */
	public Imgs flipY() {
		return flip(1);
	}
	
	/**
	 * 将图片按x轴垂直方向的翻转
	 * @return
	 */
	public Imgs flipX() {
		return flip(0);
	}
	
	/**
	 * 将图片按x轴翻转，再按y轴翻转(180度翻转)
	 * @return
	 */
	public Imgs flipXY() {
		return flip(-1);
	}
	
	/**
	 * 将图片翻转，flipCode在等于0、1、-1时分别代表：垂直翻转、水平翻转、两者都翻转
	 * @param flipCode
	 * @return
	 */
	private Imgs flip(int flipCode) {
		int w = image.getWidth();
		int h = image.getHeight();
		image(0, 0, w, h, Colors.wTransparent);
		Graphics2D graphics2d=image.createGraphics();
		if (flipCode==0) {//垂直翻转(x轴)
			graphics2d.drawImage(oldImage, 0, 0, w, h, 0, h, w, 0, null);
		}else if(flipCode==1) {//水平翻转(y轴)
			graphics2d.drawImage(oldImage, 0, 0, w, h, w, 0, 0, h, null);
		}else if (flipCode==-1) {//垂直水平都翻转(x、y轴)
			graphics2d.drawImage(oldImage, 0, 0, w, h, w, h, 0, 0, null);
		}
		graphics2d.dispose();
		return this;
	}
	
	/**
	 * 获取当前图片的字节密度，密度过低则代表图片不适合压缩。
	 */
	private double density() {
		try(ByteArrayOutputStream baos=new ByteArrayOutputStream()) {
			ImageIO.write(this.image, this.image.getType()==BufferedImage.TYPE_4BYTE_ABGR?Images.png:Images.jpeg, baos);
			return baos.toByteArray().length*1.0/(this.image.getWidth()*this.image.getHeight());
		} catch (IOException e) {
			return 0.0;
		}
	}
	
	/**
	 * 对图片进行适当的压缩，系统根据当前图片的质量来决定是否对当前图片进行压缩和压缩的程度，注意：压缩图片会导致图片清晰度降低。<br/>
	 * 图片'质量-压缩图片质量'的界定参考  @see {@link #SUGGESTS}
	 * @see #quality(double)
	 */
	public Imgs properCompress() {
		double d=density();
		for(double[] ss:SUGGESTS) {
			if (d>=ss[0]) {
				return quality(ss[1]);
			}
		}
		return this;
	}
	
	/**
	 * 对图片按原图片质量的f倍进行压缩，若当前图片质量和传入的f值在推荐范围之内，那么将会对该图片进行压缩，若系统判定该图片不在合适的可压缩范围，则不对图片进行压缩。<br/>
	 * 图片'质量-压缩质量上限'界定的范围参考 @see {@link #RESTRICTS}
	 * @see #quality(double)
	 */
	public Imgs mayCompress(double f) {
		double d=density();
		for(double[] ss:RESTRICTS) {
			if (d>=ss[0] && f<=ss[1]) {
				return quality(f);
			}
		}
		return this;
	}
	
	/**
	 * 改变当前图片的质量，传入值在[0.0,1.0]之间，传入该区间外的值将不做任何处理
	 * @param f
	 * @return
	 */
	public Imgs quality(double f) {
		if (f >= 0.0 && f <= 1.0) {
			// 得到指定Format图片的writer
			ImageWriter writer = ImageIO.getImageWritersByFormatName(Images.jpeg).next();
			// 得到指定writer的输出参数设置(ImageWriteParam )
			ImageWriteParam iwp = writer.getDefaultWriteParam();
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
			iwp.setCompressionQuality((float)f); // 设置压缩质量参数
			iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
			ColorModel colorModel = this.image.getColorModel();
			// 指定压缩时使用的色彩模式
			iwp.setDestinationType(new ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
			// 通过ImageIo中的静态方法，得到ByteArrayOutputStream的ImageOutput
			IIOImage iIamge = new IIOImage(this.image, null, null);
			try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageOutputStream ios=ImageIO.createImageOutputStream(baos);) {
				// 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput
				writer.setOutput(ios);
				//将图片压缩到输出流里
				writer.write(null, iIamge, iwp);
				ByteArrayInputStream bais=new ByteArrayInputStream(baos.toByteArray());
				update(ImageIO.read(bais));
				bais.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	/**
	 * 图片按degree度旋转,旋转后图片尺寸不变。
	 * @param degree 旋转的角度，大于0图片按顺时针旋转,小于0图片按逆时针旋转
	 * @return
	 */
	public Imgs rotateDrop(int degree) {
		int width=image.getWidth(),height=image.getHeight();
		return rotateRise(degree).scaleRatioBox(width, height);
	}
	
	/**
	 * 图片按degree度旋转,旋转后图片尺寸刚好容下原来图片在旋转后所需占用的矩形空间，既旋转后的图片尺寸可能发生改变。
	 * @param degree 旋转的角度，大于0图片按顺时针旋转,小于0图片按逆时针旋转
	 * @return
	 */
	public Imgs rotateRise(int degree) {
		int width=image.getWidth(),height=image.getHeight();
		int new_w=0,new_h=0;
		double radians=Math.toRadians(degree);
		double cos=Math.cos(radians);
		double sin=Math.sin(radians);
		new_w = (int) (Math.abs(width*cos) + Math.abs(height*sin));
		new_h = (int) (Math.abs(height*cos) + Math.abs(width*sin));
		image(new_w, new_h);
        Graphics2D graphics2d = image.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(radians, new_w / 2, new_h / 2);
		AffineTransform rAffineTransform=this.findTranslation(affineTransform, this.oldImage, degree);
		affineTransform.preConcatenate(rAffineTransform);
		graphics2d.drawRenderedImage(this.oldImage, affineTransform);
        graphics2d.dispose();
        return this;
	}
	
	/**
	 * 图片按degree度旋转,旋转后图片内容超出的部分可能会被裁减，旋转后图片尺寸不变
	 * @param degree 旋转的角度，大于0图片按顺时针旋转,小于0图片按逆时针旋转
	 * @return
	 */
	public Imgs rotate(int degree) {
		int width=image.getWidth(),height=image.getHeight();
		image(width, height);
        Graphics2D graphics2d = image.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), width / 2, height / 2);
        graphics2d.drawImage(this.oldImage, 0, 0, null);  
        graphics2d.dispose();
        return this;
	}
	
	/**
	 * 将图片透明化
	 * @param alpha the constant alpha to be multiplied with the alpha of the source. alpha must be a floating point number in the inclusive range [0.0, 1.0].
	 * @return
	 */
	public Imgs transparency(double alpha) {
		image(this.image.getWidth(), this.image.getHeight());
		Graphics2D g=this.image.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
		g.drawImage(this.oldImage, 0, 0, null);
		g.dispose();
		return this;
	}
	
	/**
	 * 图片宽
	 * @return
	 */
	public int width() {
		return image.getWidth();
	}
	
	/**
	 * 图片高
	 * @return
	 */
	public int height() {
		return image.getHeight();
	}
	
	/**
	 * 图片分辨率
	 * @return
	 */
	public Dimension dimen() {
		return new Dimension(image.getWidth(),image.getHeight());
	}
	
	/**
	 * 获取图片文件的分辨率
	 * @param path
	 * @return
	 */
	public static Dimension dimen(Path path) {
		Dimension dimension=new Dimension();
		if(Filer.isReadableFile(path)) {
			Iterator<ImageReader> it=ImageIO.getImageReadersBySuffix(Pather.suffix(path.normalize().toString()));
			if (it.hasNext()) {
				ImageReader imageReader=it.next();
				try (ImageInputStream iis = ImageIO.createImageInputStream(path)){
					imageReader.setInput(iis, true);
					dimension.width=imageReader.getWidth(0);
					dimension.height=imageReader.getHeight(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dimension;
	}
	
	
	private AffineTransform findTranslation(AffineTransform at, BufferedImage bi, int angle) {
		Point2D p2din, p2dout;
		double ytrans = 0.0, xtrans = 0.0;
		if (angle <= 90) {
			p2din = new Point2D.Double(0.0, 0.0);
			p2dout = at.transform(p2din, null);
			ytrans = p2dout.getY();
			p2din = new Point2D.Double(0, bi.getHeight());
			p2dout = at.transform(p2din, null);
			xtrans = p2dout.getX();
		}
		else if (angle <= 180) {
			p2din = new Point2D.Double(0.0, bi.getHeight());
			p2dout = at.transform(p2din, null);
			ytrans = p2dout.getY();

			p2din = new Point2D.Double(bi.getWidth(), bi.getHeight());
			p2dout = at.transform(p2din, null);
			xtrans = p2dout.getX();

		}
		else if (angle <= 270) {
			p2din = new Point2D.Double(bi.getWidth(), bi.getHeight());
			p2dout = at.transform(p2din, null);
			ytrans = p2dout.getY();

			p2din = new Point2D.Double(bi.getWidth(), 0.0);
			p2dout = at.transform(p2din, null);
			xtrans = p2dout.getX();

		} else {
			p2din = new Point2D.Double(bi.getWidth(), 0.0);
			p2dout = at.transform(p2din, null);
			ytrans = p2dout.getY();

			p2din = new Point2D.Double(0.0, 0.0);
			p2dout = at.transform(p2din, null);
			xtrans = p2dout.getX();

		}
		AffineTransform tat = new AffineTransform();
		tat.translate(-xtrans, -ytrans);
		return tat;
	}
	
}
