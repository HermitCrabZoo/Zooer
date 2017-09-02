package com.zoo.util;

import java.nio.file.Path;
@FunctionalInterface
public interface Eacher {
	void doIt(Path in,Path to,Exception e);
}
