import models.Terrain;
import services.Config;

import javax.swing.*;
import java.awt.*;

class MyFrame extends JFrame {
    MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        g2D.drawLine(0, 0, 500, 500);
    }
}

public class Main {
    public static void main(String[] args) throws Exception {

        final var terrain = new Terrain(Config.get("terrain_path"));
        var frame = new MyFrame();
    }
}
