package io.github.eforrest8.rt;

public interface Renderer {
    int[] renderSynchronous();

    void renderAsync();

    void cancelAsyncRender();

    int[] getAsyncImage();
}
