package com.zoo.util;
@FunctionalInterface
public interface Eacher<I,T,E> {
	void doIt(I in,T to,E e);
}
