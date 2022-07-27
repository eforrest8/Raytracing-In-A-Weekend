package io.github.eforrest8.rt;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class RenderPipeline {
	
	private Supplier<Image> fallbackSupplier = () -> new Image(256, 256);
	private CompletableFuture<Image> completableFuture;
	
	public RenderPipeline(CompletableFuture<Image> completableFuture) {
		this.completableFuture = completableFuture;
	}
	
	public void setFallbackSupplier(Supplier<Image> supplier) {
		fallbackSupplier = supplier;
	}
	
	public Image get() {
		try {
			return completableFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			return fallbackSupplier.get();
		}
	}
	
	public Image get(long timeout, TimeUnit unit) {
		try {
			return completableFuture.get(timeout, unit);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			return fallbackSupplier.get();
		}
	}
}
