import javax.swing.JFrame;
import javax.swing.JButton;

public class Main {
    public static void main(String[] args) {
        // Création de la fenêtre
        JFrame frame = new JFrame("Fenêtre Swing");
        JButton button = new JButton("Clique moi");

        frame.add(button);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // Centre la fenêtre
        frame.setVisible(true);
    }
}
