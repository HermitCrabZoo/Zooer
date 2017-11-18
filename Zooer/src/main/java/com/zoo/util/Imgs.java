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
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.Optional;
import java.util.function.Supplier;

import sun.font.FontDesignMetrics;

@SuppressWarnings("restriction")
public final class Imgs {
	
	private BufferedImage image;
	private BufferedImage oldImage;
	private static ColorConvertOp colorConvertOp=new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
	
	private Imgs() {}
	private Imgs(BufferedImage image) {
		this.image=image;
	}
	/**
	 * 根据给定的BufferedImage对象构造一个Imgs对象
	 * @param image
	 * @throws NullPointerException 如果image参数为null，则抛出此异常
	 * @return
	 */
	public static Imgs of(BufferedImage image) {
		if (image==null) {
			throw new NullPointerException("Argument image cant not be null!");
		}
		return new Imgs(image);
	}
	/**
	 * 构造一个未关联image属性的Imgs对象，后续对image的操作前必须先关联当前对象的image属性到一个已存在的BufferedImage实例。
	 * @return
	 */
	public static Imgs ofNull() {
		return new Imgs();
	}
	/**
	 * 根据给定大小构造一个Imgs对象,该对象绑定的BufferedImage对象的颜色随机。
	 * @param width
	 * @param height
	 * @param color
	 * @return
	 */
	public static Imgs ofNew(int width,int height){
		return ofNew(width, height, Colors.randColor());
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
	 * 设置新的图像(替换当前的)
	 * @param image
	 * @throws NullPointerException 如果image参数为null，则抛出此异常
	 * @return
	 */
	public Imgs setNew(BufferedImage image) {
		if (image==null) {
			throw new NullPointerException("Argument image cant not be null!");
		}
		this.image=image;
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
	 * 根据给定大小生成随机颜色的图片
	 * @param width
	 * @param height
	 * @return
	 */
	public Imgs image(int width,int height){
		return image(0,0,width,height,Colors.randColor());
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
	 * 根据给定大小、坐标、颜色生成图片，若color为null那么将随机产生一种颜色
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @return
	 */
	public Imgs image(int x,int y,int width,int height,Color color){
		this.oldImage=image;
		image =newImg(x, y, width, height, color);
		return this;
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
		g.setColor(Optional.ofNullable(color).orElse(Colors.randColor()));
		g.fillRect(x, y, width, height);
		g.dispose();
		return image;
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
			image(w, h, Colors.wTransparent).pileLeftTop(cover);
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
		return scaleRatioBox(w, h, Colors.wTransparent);
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
		return image(w, h, Optional.ofNullable(bgColor).orElse(Colors.wTransparent)).pileCenter(cover);
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
			image(w, h, Colors.wTransparent).pileLeftTop(cover);
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
		image(image.getWidth(), image.getHeight(), Optional.ofNullable(bgColor).orElse(Colors.wTransparent));
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
		return image(image.getWidth()+w*2, image.getHeight()+w*2, Optional.ofNullable(borderColor).orElse(Colors.wTransparent)).pile(oldImage, w, w);
	}
	/**
	 * 在图片外围上加边框(图片尺寸不变)
	 * @param w  边框宽度
	 * @param borderColor 边框颜色
	 * @return
	 */
	public Imgs borderDrop(int w,Color borderColor){
		return scaleZoom(image.getWidth()-w*2, image.getHeight()-w*2).image(oldImage.getWidth(), oldImage.getHeight(),Optional.ofNullable(borderColor).orElse(Colors.wTransparent)).pile(oldImage,w, w);
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
}
