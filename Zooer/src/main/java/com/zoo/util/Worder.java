package com.zoo.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apdplat.word.WordSegmenter;

public final class Worder {
	private Worder() {}
	
	public static List<String> pureWords(String sentence) {
		return WordSegmenter.seg(sentence).parallelStream().map(w->w.getText()).collect(Collectors.toList());
	}
	public static List<String> intactWords(String sentence) {
		return WordSegmenter.segWithStopWords(sentence).parallelStream().map(w->w.getText()).collect(Collectors.toList());
	}
}
