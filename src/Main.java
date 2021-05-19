import java.awt.*;
import javax.swing.*;

class MyFrame extends JFrame {
    MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        g2D.drawLine(0,0,500,500);
    }
}

public class Main {
    public static void main(String[] args) {
        var frame = new MyFrame();
    }
}
