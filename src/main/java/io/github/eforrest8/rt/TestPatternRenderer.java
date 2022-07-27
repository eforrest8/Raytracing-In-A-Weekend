package io.github.eforrest8.rt;

import java.util.concurrent.CompletableFuture;

import io.github.eforrest8.rt.geometry.Vector;

public class TestPatternRenderer implements Renderer{

    int width = 256;
    int height = 256;
    
    public TestPatternRenderer(int width, int height) {
    	this.width = width;
    	this.height = height;
    }

    public Image renderSynchronous() {
    	Image image = new Image(width, height);

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                float r = x / (float)(width - 1);
                float g = y / (float)(height - 1);
                float b = 0.25f;

                image.setPixel(new Pixel(x, y, new Vector(r, g, b)));
            }
        }
        return image;
    }

    @Override
    public Image render() {
        return renderSynchronous();
    }
}
