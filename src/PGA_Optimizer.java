import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PGA_Optimizer {
    private JTabbedPane tabbedPane1;
    private JPanel PGA_Optimizer_Panel;
    private JPanel Start;
    private JButton Exit_Button;
public PGA_Optimizer() {
    Exit_Button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==Exit_Button) {
                System.exit(0);
            }
        }
    });
}

    public JPanel getPGA_Optimizer_Panel() {
        return PGA_Optimizer_Panel;
    }
}
