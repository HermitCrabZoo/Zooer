package com.zoo.mix;
@FunctionalInterface
public interface Eacher<I,T,E> {
	void doIt(I in,T to,E e);
}
