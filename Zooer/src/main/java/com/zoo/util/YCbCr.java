package com.zoo.util;

public class YCbCr {
	private double y;
	private double cb;
	private double cr;
	public double getY() {
		return y;
	}
	public void setY(double y) {
		if (y<0.0 || y>255.0) {
			throw new IllegalArgumentException("argument y must be great than -1 and less than 256");
		}
		this.y = y;
	}
	public double getCb() {
		return cb;
	}
	public void setCb(double cb) {
		this.cb = cb;
	}
	public double getCr() {
		return cr;
	}
	public void setCr(double cr) {
		this.cr = cr;
	}
	@Override
	public String toString() {
		return "YCbCr [y=" + y + ", cb=" + cb + ", cr=" + cr + "]";
	}
	
}
