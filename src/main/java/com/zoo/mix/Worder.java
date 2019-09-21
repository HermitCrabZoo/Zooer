package com.zoo.mix;

import java.util.List;
import java.util.stream.Collectors;

import org.apdplat.word.WordSegmenter;

public final class Worder {
	private Worder() {}
	/**
	 * 对字符串分词，不保留中断词(的、是等。)
	 * @param sentence
	 * @return
	 */
	public static List<String> pureWords(String sentence) {
		return WordSegmenter.seg(sentence).parallelStream().map(w->w.getText()).collect(Collectors.toList());
	}
	/**
	 * 对字符串进行分词，保留重点词(的、是等)
	 * @param sentence
	 * @return
	 */
	public static List<String> intactWords(String sentence) {
		return WordSegmenter.segWithStopWords(sentence).parallelStream().map(w->w.getText()).collect(Collectors.toList());
	}
}
