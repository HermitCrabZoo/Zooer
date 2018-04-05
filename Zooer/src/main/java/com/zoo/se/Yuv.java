package com.zoo.se;

public class Yuv {
	private double y;
	private double u;
	private double v;
	public double getY() {
		return y;
	}
	public void setY(double y) {
		if (y<0.0 || y>255.0) {
			throw new IllegalArgumentException("argument y must be great than -1 and less than 256");
		}
		this.y = y;
	}
	public double getU() {
		return u;
	}
	public void setU(double u) {
		this.u = u;
	}
	public double getV() {
		return v;
	}
	public void setV(double v) {
		this.v = v;
	}
	@Override
	public String toString() {
		return "Yuv [y=" + y + ", u=" + u + ", v=" + v + "]";
	}
	
}
