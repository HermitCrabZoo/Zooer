package com.zoo.util;

import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Optional;

@FunctionalInterface
public interface Processor<T> {
	T process(T property);

	Processor<?> dateProcessor = obj -> Optional.ofNullable(obj).map(o -> {
		if (TemporalAccessor.class.isAssignableFrom(o.getClass())) {
			return Dates.format((TemporalAccessor) o, Dates.allFormat);
		} else if (Date.class.isAssignableFrom(o.getClass())) {
			return Dates.format((Date) o, Dates.allFormat);
		}
		return o;
	}).orElse(Strs.empty());
}
