package com.zoo.util;

import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Optional;

@FunctionalInterface
public interface Processor<T> {
	T process(T property);

	Processor<?> DATE_PROCESSOR = obj -> Optional.ofNullable(obj).map(o -> {
		if (TemporalAccessor.class.isAssignableFrom(o.getClass())) {
			return Dater.format((TemporalAccessor) o, Dater.ALL_FORMAT);
		} else if (Date.class.isAssignableFrom(o.getClass())) {
			return Dater.format((Date) o, Dater.ALL_FORMAT);
		}
		return o;
	}).orElse(Strs.empty());
}
