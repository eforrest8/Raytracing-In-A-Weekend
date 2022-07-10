package io.github.eforrest8.rt;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleDisplay extends JFrame {



    public SimpleDisplay() {
        int[] pixels = new int[640 * 360];
        Arrays.fill(pixels, 0xffaa00aa);
        MemoryImageSource imageSource = new MemoryImageSource(640, 360, ColorModel.getRGBdefault(), pixels, 0, 640);
        imageSource.setAnimated(true);
        Image image = createImage(imageSource);
        JLabel label = new JLabel(new ImageIcon(image));
        add(label);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Renderer renderer = new RTRenderer();
        renderer.renderAsync();
        executor.scheduleAtFixedRate(() ->
                imageSource.newPixels(
                        renderer.getAsyncImage(),
                        ColorModel.getRGBdefault(),
                        0,
                        640),
                0,
                100,
                TimeUnit.MILLISECONDS);
    }

}
