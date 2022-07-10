package io.github.eforrest8.rt;

import javax.swing.*;

public class RTDisplay {
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new SimpleDisplay();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RTDisplay::createAndShowGUI);
    }
}
