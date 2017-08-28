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
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import sun.font.FontDesignMetrics;

@SuppressWarnings("restriction")
public final class Images {
	private Images(){}
	/**
	 * 根据给定大小生成随机颜色的图片
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage randImage(int width,int height){
		return image(0,0,width,height,Colors.randColor());
	}
	/**
	 * 根据给定大小和色系该色系内的颜色的图片
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage randImage(int width,int height,Chroma chroma){
		return image(0,0,width,height,Colors.randColor(chroma));
	}
	/**
	 * 根据给定大小、颜色生成图片
	 * @param rect
	 * @param color
	 * @return
	 */
	public static BufferedImage image(int width,int height,Color color){
		return image(0,0,width,height,color);
	}
	/**
	 * 根据给定大小、颜色生成图片
	 * @param dimen
	 * @param color
	 * @return
	 */
	public static BufferedImage image(Dimension dimen,Color color){
		return image(0,0,dimen.width,dimen.height,color);
	}
	/**
	 * 根据给定坐标、大小、颜色生成图片
	 * @param dimen
	 * @param color
	 * @return
	 */
	public static BufferedImage image(Point point,Dimension dimen,Color color){
		return image(point.x,point.y,dimen.width,dimen.height,color);
	}
	/**
	 * 根据给定大小、坐标、颜色生成图片
	 * @param rect
	 * @param color
	 * @return
	 */
	public static BufferedImage image(Rectangle rect,Color color){
		return image(rect.x, rect.y, rect.width, rect.height, color);
	}
	/**
	 * 根据给定大小、坐标、颜色生成图片，若color为null那么将随机产生一种颜色
	 * @param rect
	 * @param color
	 * @return
	 */
	public static BufferedImage image(int x,int y,int width,int height,Color color){
		BufferedImage image =new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setColor(Optional.ofNullable(color).orElse(Colors.randColor()));
		g.fillRect(x, y, width, height);
		g.dispose();
		return image;
	}
	
	/**
	 * 在图片上写字,默认字体、颜色,位置居中
	 * @param base
	 * @param surface
	 * @return
	 */
	public static BufferedImage pileCenter(BufferedImage base,String surface){
		return pile(base, surface, null,null);
	}
	/**
	 * 在图片上写字,默认字体,位置居中
	 * @param base
	 * @param surface
	 * @return
	 */
	public static BufferedImage pile(BufferedImage base,String surface,Font font){
		return pile(base, surface,null,font);
	}
	/**
	 * 在图片上写字,默认字体,位置居中
	 * @param base
	 * @param surface
	 * @return
	 */
	public static BufferedImage pile(BufferedImage base,String surface,Color color){
		return pile(base, surface,color,null);
	}
	/**
	 * 在图片上写字,默认字体,位置居中
	 * @param base 图片对象
	 * @param surface 文本
	 * @param color 字体颜色
	 * @param font 字体类型
	 * @return
	 */
	public static BufferedImage pile(BufferedImage base,String surface,Color color,Font font){
		if (surface==null || surface.isEmpty()) {
			return base;
		}
		FontMetrics fm=FontDesignMetrics.getMetrics(Optional.ofNullable(font).orElse(base.getGraphics().getFont()));
		return pile(base, surface, (base.getWidth()-fm.stringWidth(surface))/2,(base.getHeight()+fm.getAscent()-fm.getDescent())/2,color, font);
	}
	/**
	 * 在图片上写字
	 * @param base 图片对象
	 * @param surface 文本
	 * @param point 字体位置
	 * @param color 字体颜色
	 * @param font 字体类型
	 * @return
	 */
	public static BufferedImage pile(BufferedImage base,String surface,Point point,Color color,Font font) {
		point=Optional.ofNullable(point).orElse(new Point(0, 0));
		return pile(base, surface, point.x, point.y, color, font);
	}
	/**
	 * 在图片上写字
	 * @param base 图片对象
	 * @param surface 文本
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @param color 字体颜色
	 * @param font 字体类型
	 * @return
	 */
	public static BufferedImage pile(BufferedImage base,String surface,int x,int y,Color color,Font font){
		if(surface!=null&&!surface.isEmpty()) {
			Graphics2D g=base.createGraphics();
			g.setColor(Optional.ofNullable(color).orElse(g.getColor()));
			g.setFont(Optional.ofNullable(font).orElse(g.getFont()));
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
			g.drawString(surface, x, y);
			g.dispose();
		}
		return base;
	}
	/**
	 * 将cover画在base上层左上角,返回base.
	 * @param base
	 * @param cover
	 * @return
	 */
	public static BufferedImage pileLeftTop(BufferedImage base,Image cover){
		return pile(base, cover,0, 0);
	}
	/**
	 * 将cover画在base上层左侧中部,返回base.
	 * @param base
	 * @param cover
	 * @return
	 */
	public static BufferedImage pileLeft(BufferedImage base,Image cover){
		if (cover!=null) {
			base=pile(base, cover,0, (base.getHeight()-cover.getHeight(null))/2);
		}
		return base;
	}
	/**
	 * 将cover画在base上层左下角,返回base.
	 * @param base
	 * @param cover
	 * @return
	 */
	public static BufferedImage pileLeftBottom(BufferedImage base,Image cover){
		if (cover!=null) {
			base=pile(base, cover,0,base.getHeight()-cover.getHeight(null));
		}
		return base;
	}
	/**
	 * 将cover画在base上层右上角,返回base.
	 * @param base
	 * @param cover
	 * @return
	 */
	public static BufferedImage pileRightTop(BufferedImage base,Image cover){
		if (cover!=null) {
			base=pile(base, cover,base.getWidth()-cover.getWidth(null), 0);
		}
		return base;
	}
	/**
	 * 将cover画在base上层右侧中部,返回base.
	 * @param base
	 * @param cover
	 * @return
	 */
	public static BufferedImage pileRight(BufferedImage base,Image cover){
		if (cover!=null) {
			base=pile(base, cover,base.getWidth()-cover.getWidth(null), (base.getHeight()-cover.getHeight(null))/2);
		}
		return base;
	}
	/**
	 * 将cover画在base上层右下角,返回base.
	 * @param base
	 * @param cover
	 * @return
	 */
	public static BufferedImage pileRightBottom(BufferedImage base,Image cover){
		if (cover!=null) {
			base=pile(base, cover,base.getWidth()-cover.getWidth(null),base.getHeight()-cover.getHeight(null));
		}
		return base;
	}
	/**
	 * 将cover画在base上层水平居中上方,返回base.
	 * @param base
	 * @param cover
	 * @return
	 */
	public static BufferedImage pileTop(BufferedImage base,Image cover){
		if (cover!=null) {
			base=pile(base, cover,(base.getWidth()-cover.getWidth(null))/2, 0);
		}
		return base;
	}
	/**
	 * 将cover画在base上层水平垂直居中,返回base.
	 * @param base
	 * @param cover
	 * @return
	 */
	public static BufferedImage pileCenter(BufferedImage base,Image cover){
		if (cover!=null) {
			base=pile(base, cover,(base.getWidth()-cover.getWidth(null))/2, (base.getHeight()-cover.getHeight(null))/2);
		}
		return base;
	}
	/**
	 * 将cover画在base上层水平居中下方,返回base.
	 * @param base
	 * @param cover
	 * @return
	 */
	public static BufferedImage pileBottom(BufferedImage base,Image cover){
		if (cover!=null) {
			base=pile(base, cover,(base.getWidth()-cover.getWidth(null))/2,base.getHeight()-cover.getHeight(null));
		}
		return base;
	}
	/**
	 * 将cover画在base上层,返回base
	 * @param base
	 * @param cover
	 * @param x
	 * @param y
	 * @return
	 */
	public static BufferedImage pile(BufferedImage base,Image cover,int x,int y){
		if (cover!=null) {
			base=pile(base, cover, x, y,cover.getWidth(null),cover.getHeight(null));
		}
		return base;
	}
	/**
	 * 将cover画在base上层,返回base
	 * @param base
	 * @param cover
	 * @param point
	 * @return
	 */
	public static BufferedImage pile(BufferedImage base,Image cover,Point point){
		if (cover!=null) {
			point=Optional.ofNullable(point).orElse(new Point(0, 0));
			base=pile(base, cover, point.x, point.y, cover.getWidth(null), cover.getHeight(null));
		}
		return base;
	}
	/**
	 * 将cover以给定的rect信息确定与base的相对坐标和大小画在base上面
	 * @param base 背景层图片
	 * @param cover 表面层图片
	 * @param rect 表面层图片的坐标、大小
	 * @return 修改后的base对象
	 */
	public static BufferedImage pile(BufferedImage base,Image cover,Rectangle rect){
		if (cover!=null) {
			rect=Optional.ofNullable(rect).orElse(new Rectangle(0, 0, cover.getWidth(null), cover.getHeight(null)));
			base=pile(base, cover, rect.x, rect.y, rect.width, rect.height);
		}
		return base;
	}
	/**
	 * 将cover以给定的rect信息确定与base的相对坐标和大小画在base上面
	 * @param base 背景层图片
	 * @param cover 表面层图片
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @param w 宽
	 * @param h 高
	 * @return 修改后的base对象
	 */
	public static BufferedImage pile(BufferedImage base,Image cover,int x,int y,int w,int h){
		if(cover!=null) {
			Graphics2D g=base.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(cover, x, y, w, h, null);
			g.dispose();
		}
		return base;
	}
	/**
	 * 将原始图片按比例缩放输出(输出图片不变形)
	 * @param srcImage
	 * @param ratio 缩放率
	 * @return 若基于缩放率为ratio下计算到的宽或高小于1,那么此方法将返回null
	 */
	public static BufferedImage scale(BufferedImage srcImage,double ratio){
		int width=(int) Math.round(srcImage.getWidth()*ratio),height=(int) Math.round(srcImage.getHeight()*ratio);
		if (width<1||height<1) {
			return null;
		}
		return pileLeftTop(image(width, height, Colors.wTransparent), scale(srcImage, width, height));
	}
	/**
	 * 基于宽的按比例缩放,输出图片宽高比不变(输出图片不变形)
	 * @param srcImage
	 * @param w 输出图片的宽度
	 * @return 若高w小于1或在宽为w下计算出来的高小于1,那么此方法将返回null
	 */
	public static BufferedImage scaleWidth(BufferedImage srcImage,int w){
		int h=(int) Math.round(w*1.0/srcImage.getWidth()*srcImage.getHeight());
		if (w<1||h<1) {
			return null;
		}
		return pileLeftTop(image(w, h, Colors.wTransparent), scale(srcImage, w, h));
    }
	/**
	 * 基于高的按比例缩放,输出图片宽高比不变(输出图片不变形)
	 * @param srcImage
	 * @param h 输出图片的高度
	 * @return 若高h小于1或在高为h下计算出来的宽小于1,那么此方法将返回null
	 */
	public static BufferedImage scaleHeight(BufferedImage srcImage,int h){
		int w=(int) Math.round(h*1.0/srcImage.getHeight()*srcImage.getWidth());
		if (w<1||h<1) {
			return null;
		}
		return pileLeftTop(image(w, h, Colors.wTransparent), scale(srcImage, w, h));
	}
	/**
	 * 图片缩放,输出图片将按输入的宽高比进行缩放输出(输出宽高比与原始图片宽高比不一致可能导致输出图片效果变形)
	 * @param srcImage
	 * @param h 输出图片的高
	 * @param w 输出图片的宽
	 * @return 若w或h小于1,那么此方法将返回null
	 */
	public static BufferedImage scaleZoom(BufferedImage srcImage,int w, int h){
		if (w<1||h<1) {
			return null;
		}
        return pileLeftTop(image(w, h, Colors.wTransparent), scale(srcImage, w, h));
    }
	/**
	 * 图片缩放,输出图片将按传入的宽高参数输出,若输入宽高比与原图片宽高比不一致,那么原始图片将按原始宽高比被缩放至刚好可以放入输出宽高大小的图片大小中,其余透明色填充.
	 * @param srcImage
	 * @param h 输出图片的高
	 * @param w 输出图片的宽
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage scaleRatioBox(BufferedImage srcImage,int w, int h){
		return scaleRatioBox(srcImage, w, h, Colors.wTransparent);
	}
	/**
	 * 图片缩放,输出图片将按输入的宽高参数输出,若输入宽高比与原图片宽高比不一致,那么原始图片将按原始宽高比被缩放至刚好可以放入输出宽高大小的图片大小中,其余用bgColor填充.
	 * @param srcImage
	 * @param h 输出图片的高
	 * @param w 输出图片的宽
	 * @param bgColor 背景ARGB
	 * @return
	 */
	public static BufferedImage scaleRatioBox(BufferedImage srcImage,int w, int h,Color bgColor){
		return pileCenter(image(w, h, Optional.ofNullable(bgColor).orElse(Colors.wTransparent)), scaleRatio(srcImage, w, h));
	}
	/**
	 * 图片缩放,图片将按原始比例缩放到刚好可以放入宽为w高为h的容器中的大小(输出图片不变形).
	 * @param srcImage
	 * @param w
	 * @param h
	 * @return 若图片无法缩放到能放到宽为w高为h的容器中,那么此方法将返回null
	 */
	public static BufferedImage scaleRatio(BufferedImage srcImage,int w, int h) {
		double ratioX=w*1.0/srcImage.getWidth();
		double ratioY=h*1.0/srcImage.getHeight();
		if (ratioX<ratioY) {
			h=(int) Math.round(w*1.0/srcImage.getWidth()*srcImage.getHeight());
		}else {
			w=(int) Math.round(h*1.0/srcImage.getHeight()*srcImage.getWidth());
		}
		if (w>0&&h>0) {
			return pileLeftTop(image(w, h, Colors.wTransparent), scale(srcImage, w, h));
		}
		return null;
	}
	/**
	 * 通过宽高获取缩放实例
	 * @param srcImage
	 * @param w
	 * @param h
	 * @return
	 */
	private static Image scale(BufferedImage srcImage,int w,int h){
        return srcImage.getScaledInstance(w, h,BufferedImage.SCALE_SMOOTH);
	}
	/**
	 * 裁剪图片的前面部分，裁剪的图片宽高值为原图片宽高值中较小的一个值。
	 * @param image
	 * @return 返回宽高值相同的图片
	 */
	public static BufferedImage cutFront(BufferedImage image){
		return cutLeftTop(image,Math.min(image.getWidth(), image.getHeight()));
	}
	/**
	 * 裁剪图片的中间部分，裁剪的图片宽高值为原图片宽高值中较小的一个值。
	 * @param image
	 * @return 返回宽高值相同的图片
	 */
	public static BufferedImage cutCenter(BufferedImage image){
		return cutCenter(image, Math.min(image.getWidth(), image.getHeight()));
	}
	/**
	 * 裁剪图片的后面部分，裁剪的图片宽高值为原图片宽高值中较小的一个值。
	 * @param image
	 * @return 返回宽高值相同的图片
	 */
	public static BufferedImage cutBehind(BufferedImage image){
		return cutRightBottom(image, Math.min(image.getWidth(), image.getHeight()));
	}
	/**
	 * 从左上角处裁剪出宽高为w的图片
	 * @param image
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public static BufferedImage cutLeftTop(BufferedImage image,int w){
		return cutLeftTop(image, w, w);
	}
	/**
	 * 从左边中间处裁剪出宽高为w的图片
	 * @param image
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public static BufferedImage cutLeft(BufferedImage image,int w){
		return cutLeft(image, w, w);
	}
	/**
	 * 从左下角处裁剪出宽高为w的图片
	 * @param image
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public static BufferedImage cutLeftBottom(BufferedImage image,int w){
		return cutLeftBottom(image, w, w);
	}
	/**
	 * 从右上角处裁剪出宽高为w的图片
	 * @param image
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public static BufferedImage cutRightTop(BufferedImage image,int w){
		return cutRightTop(image, w, w);
	}
	/**
	 * 从右边中间处裁剪出宽高为w的图片
	 * @param image
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public static BufferedImage cutRight(BufferedImage image,int w){
		return cutRight(image, w, w);
	}
	/**
	 * 从右下角处裁剪出宽高为w的图片
	 * @param image
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public static BufferedImage cutRightBottom(BufferedImage image,int w){
		return cutRightBottom(image, w, w);
	}
	/**
	 * 从上边中间处裁剪出宽高为w的图片
	 * @param image
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public static BufferedImage cutTop(BufferedImage image,int w){
		return cutTop(image, w, w);
	}
	/**
	 * 从中间处裁剪出宽高为w的图片
	 * @param image
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public static BufferedImage cutCenter(BufferedImage image,int w){
		return cutCenter(image, w, w);
	}
	/**
	 * 从下边中间处裁剪出宽高为w的图片
	 * @param image
	 * @param w
	 * @return 返回宽高相同的图片
	 */
	public static BufferedImage cutBottom(BufferedImage image,int w){
		return cutBottom(image, w, w);
	}
	/**
	 * 从左上角处裁剪出宽为w高为h的图片
	 * @param image
	 * @param w
	 * @param h
	 * @return 
	 */
	public static BufferedImage cutLeftTop(BufferedImage image,int w,int h){
		return cut(image, 0,0, w, h);
	}
	/**
	 * 从左边中间处裁剪出宽为w高为h的图片
	 * @param image
	 * @param w
	 * @param h
	 * @return 
	 */
	public static BufferedImage cutLeft(BufferedImage image,int w,int h){
		return cut(image, 0,(image.getHeight()-h)/2, w, h);
	}
	/**
	 * 从左下角处裁剪出宽为w高为h的图片
	 * @param image
	 * @param w
	 * @param h
	 * @return 
	 */
	public static BufferedImage cutLeftBottom(BufferedImage image,int w,int h){
		return cut(image, 0,image.getHeight()-h, w, h);
	}
	/**
	 * 从右上角处裁剪出宽为w高为h的图片
	 * @param image
	 * @param w
	 * @param h
	 * @return 
	 */
	public static BufferedImage cutRightTop(BufferedImage image,int w,int h){
		return cut(image, image.getWidth()-w,0, w, h);
	}
	/**
	 * 从右边中间处裁剪出宽为w高为h的图片
	 * @param image
	 * @param w
	 * @param h
	 * @return 
	 */
	public static BufferedImage cutRight(BufferedImage image,int w,int h){
		return cut(image, image.getWidth()-w,(image.getHeight()-h)/2, w, h);
	}
	/**
	 * 从右下角处裁剪出宽为w高为h的图片
	 * @param image
	 * @param w
	 * @param h
	 * @return 
	 */
	public static BufferedImage cutRightBottom(BufferedImage image,int w,int h){
		return cut(image, image.getWidth()-w,image.getHeight()-h, w, h);
	}
	/**
	 * 从上边中间处裁剪出宽为w高为h的图片
	 * @param image
	 * @param w
	 * @param h
	 * @return 
	 */
	public static BufferedImage cutTop(BufferedImage image,int w,int h){
		return cut(image, (image.getWidth()-w)/2,0, w, h);
	}
	/**
	 * 从中间处裁剪出宽为w高为h的图片
	 * @param image
	 * @param w
	 * @param h
	 * @return 
	 */
	public static BufferedImage cutCenter(BufferedImage image,int w,int h){
		return cut(image, (image.getWidth()-w)/2,(image.getHeight()-h)/2, w, h);
	}
	/**
	 * 从下边中间处裁剪出宽为w高为h的图片
	 * @param image
	 * @param w 输出图片的宽
	 * @param h 输出图片的高
	 * @return 
	 */
	public static BufferedImage cutBottom(BufferedImage image,int w,int h){
		return cut(image, (image.getWidth()-w)/2,image.getHeight()-h, w, h);
	}
	
	/**
	 * 图片缩放,输出图片将按输入的宽高输出,原图片不变,返回新图片.
	 * @param srcImage
	 * @param w 输出图片的宽
	 * @param h 输出图片的高
	 * @return 若w或h小于等于0，那么将返回null
	 */
	public static BufferedImage cut(BufferedImage srcImage,int x,int y,int w, int h){
		if (w>0 && h>0) {
			int width=srcImage.getWidth();
			int height=srcImage.getHeight();
			BufferedImage image = image(0, 0, w, h, Colors.wTransparent);
			if(x<width && y<height && x+w>0 && y+h>0){
				x=Math.max(0, x);
				y=Math.max(0, y);
				w=w+x>width?width-x:w;
				h=h+y>height?height-y:h;
				pileCenter(image, srcImage.getSubimage(x, y, w, h));
			}
			return image;
		}
		return null;
	}
	/**
	 * 将图片变成圆形,原图片不变,返回新图片.若原图片宽高不相同,那么将按原图片宽高值中较小的一个值为宽高,从原图片中间部位裁剪出宽高相等的图片,再将裁剪出来的图片变成圆形.
	 * @param srcImage
	 * @return
	 */
	public static BufferedImage circle(BufferedImage srcImage) {
		if (srcImage.getWidth()==srcImage.getHeight()) {
			return radius(srcImage, srcImage.getWidth(), null);
		}else {
			BufferedImage image=cutCenter(srcImage);
			return radius(image, image.getWidth(), null);
		}
	}
	/**
	 * 生成圆角图片，圆角外透明
	 * @param srcImage
	 * @param radius 圆角直径
	 * @return
	 */
	public static BufferedImage radius(BufferedImage srcImage,int radius) {
		return radius(srcImage, radius, null);
	}
	/**
	 * 生成圆角图片，圆角外borderColor做底色
	 * @param srcImage
	 * @param radius 圆角直径
	 * @param bgColor 圆角外的背景色
	 * @return
	 */
	public static BufferedImage radius(BufferedImage srcImage,int radius,Color bgColor) {
		BufferedImage image = image(srcImage.getWidth(), srcImage.getHeight(), Optional.ofNullable(bgColor).orElse(Colors.wTransparent));
		Graphics2D g=image.createGraphics();
        g.drawImage(srcImage, 0, 0, null);
        if(radius>0) {
        	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        	RoundRectangle2D round = new RoundRectangle2D.Double(0, 0, srcImage.getWidth(),srcImage.getHeight(), radius, radius);
        	Area clear = new Area(new Rectangle(0, 0, srcImage.getWidth(),srcImage.getHeight()));  
        	clear.subtract(new Area(round));  
        	g.setComposite(AlphaComposite.Clear);  
        	g.fill(clear);
        }
        g.dispose();
        return image;
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸将变大)，并将图片边框外角变成圆角
	 * @param srcImage
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param outRadius 圆角直径
	 * @return
	 */
	public static BufferedImage borderBraceOutRadius(BufferedImage srcImage,int w,Color borderColor,int outRadius){
		return radius(borderBrace(srcImage, w, borderColor), outRadius);
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸将变大)，并将图片边框的内角变成圆角
	 * @param srcImage
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param inRadius 圆角直径
	 * @return
	 */
	public static BufferedImage borderBraceInRadius(BufferedImage srcImage,int w,Color borderColor,int inRadius){
		return borderBrace(radius(srcImage,inRadius), w, borderColor);
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸将变大)，并将图片边框的内角和外角变成圆角
	 * @param srcImage
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param inRadius 内圆角直径
	 * @param outRadius 外圆角直径
	 * @return
	 */
	public static BufferedImage borderBraceRadius(BufferedImage srcImage,int w,Color borderColor,int inRadius,int outRadius){
		return radius(borderBrace(radius(srcImage,inRadius), w, borderColor),outRadius);
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸将变大)，并将图片边框的内角和外角变成圆角
	 * @param srcImage
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param radius 内外圆角直径
	 * @return
	 */
	public static BufferedImage borderBraceRadius(BufferedImage srcImage,int w,Color borderColor,int radius){
		return borderBraceRadius(srcImage, w, borderColor, radius, radius);
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸将变大)
	 * @param srcImage
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @return
	 */
	public static BufferedImage borderBrace(BufferedImage srcImage,int w,Color borderColor){
		BufferedImage image = image(srcImage.getWidth()+w*2, srcImage.getHeight()+w*2, Optional.ofNullable(borderColor).orElse(Colors.wTransparent));
		return pile(image, srcImage, w, w);
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸不变)，并将图片边框外角变成圆角
	 * @param srcImage
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param outRadius 圆角直径
	 * @return
	 */
	public static BufferedImage borderCrimpOutRadius(BufferedImage srcImage,int w,Color borderColor,int outRadius){
		return radius(borderCrimp(srcImage, w, borderColor), outRadius);
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸不变)，并将图片边框内角变成圆角
	 * @param srcImage
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param inRadius 圆角直径
	 * @return
	 */
	public static BufferedImage borderCrimpInRadius(BufferedImage srcImage,int w,Color borderColor,int inRadius){
		return borderCrimp(radius(srcImage,inRadius), w, borderColor);
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸不变)，并将图片边框的内角和外角变成圆角
	 * @param srcImage
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param inRadius 内圆角直径
	 * @param outRadius 外圆角直径
	 * @return
	 */
	public static BufferedImage borderCrimpRadius(BufferedImage srcImage,int w,Color borderColor,int inRadius,int outRadius){
		return radius(borderCrimp(radius(srcImage,inRadius), w, borderColor),outRadius);
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸不变)，并将图片边框的内角和外角变成圆角
	 * @param srcImage
	 * @param w 边框宽度
	 * @param borderColor 边框颜色
	 * @param radius 内外圆角直径
	 * @return
	 */
	public static BufferedImage borderCrimpRadius(BufferedImage srcImage,int w,Color borderColor,int radius){
		return borderCrimpRadius(srcImage, w, borderColor, radius, radius);
	}
	/**
	 * 在图片外围上加边框(输出图片尺寸不变)
	 * @param srcImage
	 * @param w  边框宽度
	 * @param borderColor 边框颜色
	 * @return
	 */
	public static BufferedImage borderCrimp(BufferedImage srcImage,int w,Color borderColor){
		BufferedImage image = image(srcImage.getWidth(), srcImage.getHeight(),Optional.ofNullable(borderColor).orElse(Colors.wTransparent));
		return pile(image, scaleZoom(srcImage, image.getWidth()-w*2, image.getHeight()-w*2), w, w);
	}
	
	/**
	 * 获取图片指定坐标的像素的Red、Green、Blue、Alpha值
	 * @param image
	 * @param x
	 * @param y
	 * @return 返回长度为4的int数组，分别对应RGBA的值。
	 */
	public static int[] rgba(BufferedImage image,int x,int y) {
		int pixel=image.getRGB(x, y);
		return new int[]{(pixel>>16)&0xff,(pixel>>8)&0xff,pixel&0xff,(pixel>>24)&0xff};
	}
	
	/**
	 * 获取图片文件的分辨率
	 * @param file
	 * @return
	 */
	public static Dimension imageDimension(File file) {
		Dimension dimension=new Dimension();
		if(Filer.isReadableFile(file)) {
			Iterator<ImageReader> it=ImageIO.getImageReadersBySuffix(Pather.splitText(file.getAbsolutePath())[1]);
			if (it.hasNext()) {
				ImageReader imageReader=it.next();
				try (ImageInputStream iis = ImageIO.createImageInputStream(file)){
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
}
