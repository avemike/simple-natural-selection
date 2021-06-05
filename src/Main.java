import animals.Fox;
import animals.Rabbit;
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

            // 1. Add foxes
            for (var x = 0; x < 10; x++)
                animals.add(Fox.create(x * 64, x + 24));
            // 2. Add rabbits
            for (var x = 0; x < 50; x++)
                animals.add(Rabbit.create(x * 32 % 500, 192 + 32 * (x * 32 / 500)));

            // 3. Display animals
            for (var animal : animals) {
                if (animal != null)
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
