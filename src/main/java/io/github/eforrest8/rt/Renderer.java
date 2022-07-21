package io.github.eforrest8.rt;

import java.util.concurrent.CompletableFuture;

public interface Renderer {
    CompletableFuture<Image> render();
}
