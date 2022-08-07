package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Filter;
import io.github.eforrest8.rt.Image;
import org.ejml.data.DMatrix;

public class GSRInpaintingFilter implements Filter {
    @Override
    public Image apply(Image image) {
        //math initializations
        /*int t = 0;
        double[] b = new double[];
        double[] alphaG = new double[];
        double[] u = new double[];
        double Bs = 0;
        double c;
        double lambda;
        double mu;

        int iterations = 0;
        int maxIterations = 5;
        do {
            // update u(t+1) by Eq.(28)

            // r(t+1) = u(t+1)-b(t); eta = (lambda * k)/(mu * N)
            // for each group x.G.k
            // construct dictionary D.G.k by Eq (16)
            // Reconstruct a.G.k by Eq (40)
            // end for
            // update D(t+1).G by concatenating all D.G.k
            // update a(t+1).G by concatenating all a.G.k
            // b(t+1) by Eq (26)
            // t <- t+1
        } while (iterations < maxIterations);
        // final image x = D.g o a.G*/
        return null;
    }

    private DMatrix calculateMask(Image image) {/*
        DMatrix H = new DMatrix2();
        for (int y = 0; y < image.height; y++) {
            for (int x = 0; x < image.width; x++) {
                H[y][x] = image.getPixel(x, y).color().equals(new Vector(0,0,0)) ? 1 : 0;
            }
        }*/
        return null;
    }

}
