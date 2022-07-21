package io.github.eforrest8.rt;

import java.util.concurrent.CompletableFuture;

import io.github.eforrest8.rt.geometry.Vector;

public class TestPatternRenderer implements Renderer{

    int width = 256;
    int height = 256;

    long frames = 0;

    public Image renderSynchronous() {
    	Image image = new Image(width, height);

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                float r = x / (float)(width - 1);
                float g = y / (float)(height - 1);
                float b = frames == 1 ? 0.75f : 0.25f;

                image.setPixel(new Pixel(x, y, new Vector(r, g, b)));
            }
        }
        frames = (frames == 1 ? 0 : 1);
        return image;
    }

    @Override
    public CompletableFuture<Image> render() {
        return CompletableFuture.supplyAsync(this::renderSynchronous);
    }
}
