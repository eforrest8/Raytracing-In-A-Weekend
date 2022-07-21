package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Image;

public interface Filter {
	Image apply(Image image);
}
