package com.zoo.util;

import lombok.Data;

@Data
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
	@Override
	public String toString() {
		return "YCbCr [y=" + y + ", cb=" + cb + ", cr=" + cr + "]";
	}
	
}
