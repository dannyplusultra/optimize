import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PGA_Optimizer {
    private JTabbedPane tabbedPane1;
    private JPanel PGA_Optimizer_Panel;
    private JPanel Start;
    private JButton Exit_Button;
    private JButton fileButton;
    private JButton RunButton;
    private JPanel Rules;
    private JTextField SalaryField;
    private JButton SetButton;
    private JTextField ProdOwnField;
    private JTextField MaxSumOwn;
    private JLabel UniquesPerLineup;
    private JTextField MinUniques;
    private JLabel NumLineups;
    private JTextField NumLineupsField;
    private File file;

    private double numlineups;

    private double uniques;

    private double maxsum;

    private double maxprod;

    private double salary;

    private ArrayList <PGA_Players> Players;

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
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()== fileButton) {
                   JFileChooser fileChooser = new JFileChooser();
                   int response = fileChooser.showOpenDialog(null);

                   if(response == JFileChooser.APPROVE_OPTION){
                       file = new File(String.valueOf(fileChooser.getSelectedFile().getAbsoluteFile()));


                       List<List<String>> records = new ArrayList<>();
                       try (Scanner scanner = new Scanner(file);) {
                           while (scanner.hasNextLine()) { records.add(getRecordFromLine(scanner.nextLine())); }
                       }
                       catch(Exception x){}
                       for (List<String> player : records){
                           Players.add(new PGA_Players(player.get(1), Double.parseDouble(player.get(5)), Double.parseDouble(player.get(9).replaceAll("%","")), Double.parseDouble(player.get(10))));
                       }
                       int o = 0;
                   }
                }
            }
        });
        RunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int x = 0; x < numlineups; x++) {
                    for (int i = 0; i < 6; i++) {

                    }
                }
            }
        });
        SetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uniques = Double.parseDouble(MinUniques.getText());
                maxprod = Double.parseDouble(ProdOwnField.getText());
                maxsum = Double.parseDouble(MaxSumOwn.getText());
                salary = Double.parseDouble(SalaryField.getText());
                numlineups = Double.parseDouble(NumLineupsField.getText());
            }
        });
    }

    public JPanel getPGA_Optimizer_Panel() {
        return PGA_Optimizer_Panel;
    }
    private List<String> getRecordFromLine(String line) { List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) { rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) { values.add(rowScanner.next()); }
        } return values;
    }
}

