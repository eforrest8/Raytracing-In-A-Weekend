package io.github.eforrest8.rt;

import java.awt.*;

public class TestPatternRenderer implements Renderer{

    int width = 256;
    int height = 256;
    private final int[] pixels = new int[width*height];

    long frames = 0;

    @Override
    public int[] renderSynchronous() {

        int index = 0;
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                float r = x / (float)(width - 1);
                float g = y / (float)(height - 1);
                float b = frames == 1 ? 0.75f : 0.25f;

                Color color = new Color(r, g, b);
                pixels[flattenCoords(x, y)] = color.getRGB();
                index++;
            }
        }
        frames = (frames == 1 ? 0 : 1);
        return pixels;
    }

    @Override
    public void renderAsync() {
        renderSynchronous();
    }

    @Override
    public void cancelAsyncRender() {

    }

    @Override
    public int[] getAsyncImage() {
        return pixels;
    }

    private int flattenCoords(int x, int y) {
        return x + (y * width);
    }
}
