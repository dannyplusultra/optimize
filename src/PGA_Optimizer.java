import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PGA_Optimizer {
    private JTabbedPane tabbedPane1;
    private JPanel PGA_Optimizer_Panel;
    private JPanel Start;
    private JButton Exit_Button;
    private JButton fielButton;

    public PGA_Optimizer() {
    JFrame frame = new JFrame("PGA Optimizer");
    frame.setContentPane(PGA_Optimizer_Panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    Exit_Button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==Exit_Button) {
                System.exit(0);
            }
        }
    });
        fielButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==fielButton) {
                   JFileChooser fileChooser = new JFileChooser();
                   fileChooser.showOpenDialog(null);
                }
            }
        });
    }

    public JPanel getPGA_Optimizer_Panel() {
        return PGA_Optimizer_Panel;
    }
}
