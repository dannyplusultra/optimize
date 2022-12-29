import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptimizerPanel {

    private JPanel OptimizerPanel;
    private JTabbedPane tabbedPane1;
    private JButton exitButton;

    public OptimizerPanel() {



        JFrame frame = new JFrame("OptimizerPanel");
        frame.setContentPane(OptimizerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == exitButton) {
                    System.exit(0);
                }
            }
        });
    }

    public JPanel getOptimizerPanel(){
        return  OptimizerPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
