package com.zoo.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import sun.font.FontDesignMetrics;

@SuppressWarnings("restriction")
public final class Imger {
	private BufferedImage image;
	private Imger(BufferedImage image) {
		this.image=image;
	}
	/**
	 * 构造一个Imger对象
	 * @param image
	 * @throws NullPointerException 如果image参数为空，则此方法将抛出此异常
	 * @return
	 */
	public static Imger of(BufferedImage image) {
		if (image==null) {
			throw new NullPointerException("Argument image cant not be null!");
		}
		return new Imger(image);
	}
	/**
	 * 返回当前对象关联的BufferedImage实例
	 * @return
	 */
	public BufferedImage get() {
		return this.image;
	}
	/**
	 * 根据给定大小生成随机颜色的图片
	 * @param width
	 * @param height
	 * @return
	 */
	public Imger randImage(int width,int height){
		return image(0,0,width,height,Colors.randColor());
	}
	/**
	 * 根据给定大小和色系该色系内的颜色的图片
	 * @param width
	 * @param height
	 * @return
	 */
	public Imger randImage(int width,int height,Chroma chroma){
		return image(0,0,width,height,Colors.randColor(chroma));
	}
	/**
	 * 根据给定大小、颜色生成图片
	 * @param rect
	 * @param color
	 * @return
	 */
	public Imger image(int width,int height,Color color){
		return image(0,0,width,height,color);
	}
	/**
	 * 根据给定大小、颜色生成图片
	 * @param dimen
	 * @param color
	 * @return
	 */
	public Imger image(Dimension dimen,Color color){
		return image(0,0,dimen.width,dimen.height,color);
	}
	/**
	 * 根据给定坐标、大小、颜色生成图片
	 * @param dimen
	 * @param color
	 * @return
	 */
	public Imger image(Point point,Dimension dimen,Color color){
		return image(point.x,point.y,dimen.width,dimen.height,color);
	}
	/**
	 * 根据给定大小、坐标、颜色生成图片
	 * @param rect
	 * @param color
	 * @return
	 */
	public Imger image(Rectangle rect,Color color){
		return image(rect.x, rect.y, rect.width, rect.height, color);
	}
	/**
	 * 根据给定大小、坐标、颜色生成图片，若color为null那么将随机产生一种颜色
	 * @param rect
	 * @param color
	 * @return
	 */
	public Imger image(int x,int y,int width,int height,Color color){
		BufferedImage image =new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setColor(Optional.ofNullable(color).orElse(Colors.randColor()));
		g.fillRect(x, y, width, height);
		g.dispose();
		this.image=image;
		return this;
	}
	/**
	 * 在图片上写字,默认字体、颜色,位置居中
	 * @param surface
	 * @return
	 */
	public Imger pile(String surface){
		return pile(surface, null,null);
	}
	/**
	 * 在图片上写字,默认字体,位置居中
	 * @param surface
	 * @return
	 */
	public Imger pile(String surface,Font font){
		return pile(surface,null,font);
	}
	/**
	 * 在图片上写字,默认字体,位置居中
	 * @param surface
	 * @return
	 */
	public Imger pile(String surface,Color color){
		return pile(surface,color,null);
	}
	/**
	 * 在图片上写字,默认字体,位置居中
	 * @param surface 文本
	 * @param color 字体颜色
	 * @param font 字体类型
	 * @return
	 */
	public Imger pile(String surface,Color color,Font font){
		if (Strings.notEmpty(surface)) {
			FontMetrics fm=FontDesignMetrics.getMetrics(Optional.ofNullable(font).orElse(this.image.getGraphics().getFont()));
			pile(surface, (this.image.getWidth()-fm.stringWidth(surface))/2,(this.image.getHeight()+fm.getAscent()-fm.getDescent())/2,color, font);
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
	public Imger pile(String surface,Point point,Color color,Font font) {
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
	public Imger pile(String surface,int x,int y,Color color,Font font){
		if(Strings.notEmpty(surface)) {
			Graphics2D g=this.image.createGraphics();
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
	public Imger pileLeftTop(Image cover){
		return pile(cover,0, 0);
	}
	/**
	 * 将cover画在base上层左侧中部,返回base.
	 * @param cover
	 * @return
	 */
	public Imger pileLeft(Image cover){
		if (cover!=null) {
			pile(cover,0, (this.image.getHeight()-cover.getHeight(null))/2);
		}
		return this;
	}
	/**
	 * 将cover画在base上层左下角,返回base.
	 * @param cover
	 * @return
	 */
	public Imger pileLeftBottom(Image cover){
		if (cover!=null) {
			pile(cover,0,this.image.getHeight()-cover.getHeight(null));
		}
		return this;
	}
	/**
	 * 将cover画在base上层右上角,返回base.
	 * @param cover
	 * @return
	 */
	public Imger pileRightTop(Image cover){
		if (cover!=null) {
			pile(cover,this.image.getWidth()-cover.getWidth(null), 0);
		}
		return this;
	}
	/**
	 * 将cover画在base上层右侧中部,返回base.
	 * @param cover
	 * @return
	 */
	public Imger pileRight(Image cover){
		if (cover!=null) {
			pile(cover,this.image.getWidth()-cover.getWidth(null), (this.image.getHeight()-cover.getHeight(null))/2);
		}
		return this;
	}
	/**
	 * 将cover画在base上层右下角,返回base.
	 * @param cover
	 * @return
	 */
	public Imger pileRightBottom(Image cover){
		if (cover!=null) {
			pile(cover,this.image.getWidth()-cover.getWidth(null),this.image.getHeight()-cover.getHeight(null));
		}
		return this;
	}
	/**
	 * 将cover画在base上层水平居中上方,返回base.
	 * @param cover
	 * @return
	 */
	public Imger pileTop(Image cover){
		if (cover!=null) {
			pile(cover,(this.image.getWidth()-cover.getWidth(null))/2, 0);
		}
		return this;
	}
	/**
	 * 将cover画在base上层水平垂直居中,返回base.
	 * @param cover
	 * @return
	 */
	public Imger pileCenter(Image cover){
		if (cover!=null) {
			pile(cover,(this.image.getWidth()-cover.getWidth(null))/2, (this.image.getHeight()-cover.getHeight(null))/2);
		}
		return this;
	}
	/**
	 * 将cover画在base上层水平居中下方,返回base.
	 * @param cover
	 * @return
	 */
	public Imger pileBottom(Image cover){
		if (cover!=null) {
			pile(cover,(this.image.getWidth()-cover.getWidth(null))/2,this.image.getHeight()-cover.getHeight(null));
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
	public Imger pile(Image cover,int x,int y){
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
	public Imger pile(Image cover,Point point){
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
	public Imger pile(Image cover,Rectangle rect){
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
	public Imger pile(Image cover,int x,int y,int w,int h){
		if(cover!=null) {
			Graphics2D g=this.image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(cover, x, y, w, h, null);
			g.dispose();
		}
		return this;
	}
	/**
	 * 将原始图片按比例缩放输出(输出图片不变形)
	 * @param ratio 缩放率
	 * @return 若基于缩放率为ratio下计算到的宽或高小于1,那么此方法将返回null
	 */
	public Imger scale(double ratio){
		int width=(int) Math.round(this.image.getWidth()*ratio),height=(int) Math.round(this.image.getHeight()*ratio);
		return scaleZoom(width, height);
	}
	/**
	 * 基于宽的按比例缩放,输出图片宽高比不变(输出图片不变形)
	 * @param w 输出图片的宽度
	 * @return 若高w小于1或在宽为w下计算出来的高小于1,那么此方法将返回null
	 */
	public Imger scaleWidth(int w){
		int h=(int) Math.round(w*1.0/this.image.getWidth()*this.image.getHeight());
		return scaleZoom(w, h);
    }
	/**
	 * 基于高的按比例缩放,输出图片宽高比不变(输出图片不变形)
	 * @param h 输出图片的高度
	 * @return 若高h小于1或在高为h下计算出来的宽小于1,那么此方法将返回null
	 */
	public Imger scaleHeight(int h){
		int w=(int) Math.round(h*1.0/this.image.getHeight()*this.image.getWidth());
		return scaleZoom(w, h);
	}
	/**
	 * 图片缩放,输出图片将按输入的宽高比进行缩放输出(输出宽高比与原始图片宽高比不一致可能导致输出图片效果变形)
	 * @param h 输出图片的高
	 * @param w 输出图片的宽
	 * @return 若w或h小于1,那么此方法将返回null
	 */
	public Imger scaleZoom(int w, int h){
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
	 * @throws IOException
	 */
	public Imger scaleRatioBox(int w, int h){
		return scaleRatioBox(w, h, Colors.wTransparent);
	}
	/**
	 * 图片缩放,输出图片将按输入的宽高参数输出,若输入宽高比与原图片宽高比不一致,那么原始图片将按原始宽高比被缩放至刚好可以放入输出宽高大小的图片大小中,其余用bgColor填充.
	 * @param h 输出图片的高
	 * @param w 输出图片的宽
	 * @param bgColor 背景ARGB
	 * @return
	 */
	public Imger scaleRatioBox(int w, int h,Color bgColor){
		Image cover=scaleRatio(w, h).image;
		return image(w, h, Optional.ofNullable(bgColor).orElse(Colors.wTransparent)).pileCenter(cover);
	}
	/**
	 * 图片缩放,图片将按原始比例缩放到刚好可以放入宽为w高为h的容器中的大小(输出图片不变形).
	 * @param w
	 * @param h
	 * @return 若图片无法缩放到能放到宽为w高为h的容器中,那么此方法将返回null
	 */
	public Imger scaleRatio(int w, int h) {
		double ratioX=w*1.0/this.image.getWidth();
		double ratioY=h*1.0/this.image.getHeight();
		if (ratioX<ratioY) {
			h=(int) Math.round(w*1.0/this.image.getWidth()*this.image.getHeight());
		}else {
			w=(int) Math.round(h*1.0/this.image.getHeight()*this.image.getWidth());
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
        return this.image.getScaledInstance(w, h,BufferedImage.SCALE_SMOOTH);
	}
}
