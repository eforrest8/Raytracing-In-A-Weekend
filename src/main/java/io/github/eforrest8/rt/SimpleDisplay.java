package io.github.eforrest8.rt;

import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SimpleDisplay extends JFrame {

    public SimpleDisplay() {
        int[] pixels = new int[640 * 360];
        Arrays.fill(pixels, 0xffaa00aa);
        MemoryImageSource imageSource = new MemoryImageSource(640, 360, ColorModel.getRGBdefault(), pixels, 0, 640);
        imageSource.setAnimated(true);
        java.awt.Image image = createImage(imageSource);
        JLabel label = new JLabel(new ImageIcon(image));
        add(label);
        Renderer renderer = new MultiStageRenderer();
        try {
        	io.github.eforrest8.rt.Image render = renderer.render().get();
        	imageSource.newPixels(
                    render.getAsIntArray(),
                    ColorModel.getRGBdefault(),
                    0,
                    640);
        } catch (InterruptedException | ExecutionException ignored) {}
    }

}
