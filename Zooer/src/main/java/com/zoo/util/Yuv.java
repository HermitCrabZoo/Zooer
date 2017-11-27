package com.zoo.util;

import lombok.Data;

@Data
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
	@Override
	public String toString() {
		return "Yuv [y=" + y + ", u=" + u + ", v=" + v + "]";
	}
	
}
