import animals.Fox;
import models.Animal;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

class MyFrame extends JFrame {
    MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        try {
            var animals = new Vector<Animal>();
            for (var x = 0; x < 10; x++)
                for (var y = 0; y < 10; y++)
                    animals.add(Fox.create(x * x * 3, y * y * 3));

            for (var animal : animals) {
                animal.paint(g2D);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        var frame = new MyFrame();
    }
}
