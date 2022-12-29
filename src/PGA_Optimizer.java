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
                PGA_Players[] items = (PGA_Players[]) Players.toArray();
                System.out.print("We have the following " + items.length + " items (value, size, volume): ");
                for(int i = 0; i < items.length; i++)
                    System.out.print("(" + items[i].Projection + ", " + items[i].Salary + ", " + items[i].Ownership + ") ");
                System.out.println();
                System.out.println("And a knapsack of size " + salary + " and volume " + maxprod);

                noRep(items);
                System.out.println();
                // Print the solution
                double j = salary, h = maxprod, finalSize = 0, finalValue = 0, finalVolume = 0;
                System.out.print("Items picked (value, size, volume) for 0/1 problems without repetitions: ");
                for(int i = items.length; i > 0; i--) {
                    if(dynNoRep[i][(int)j][(int)h] != dynNoRep[i - 1][(int)j][(int)h]) {
                        System.out.print("(" + items[i - 1].Projection + ", " + items[i - 1].Salary + ", " + items[i - 1].Ownership + ") ");
                        finalSize += items[i - 1].Salary;
                        finalValue += items[i - 1].Projection;
                        finalVolume += items[i - 1].Ownership;
                        j -= items[i - 1].Salary;
                        h -= items[i - 1].Ownership;
                    }
                }
                System.out.println();
                System.out.println(" Final size: " + finalSize);
                System.out.println(" Final volume: " + finalVolume);
                System.out.println(" Final value: " + finalValue);

                /*for (int x = 0; x < numlineups; x++) {
                    for (int i = 0; i < 6; i++) {

                    }
                }*/
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

    // Knapsack 0/1 without repetition
    // Row: problem having only the first i items
    // Col: problem having a knapsack of size j
    // Third dimension: problem having a knapsack of volume h
    private double[][][] dynNoRep;

    private void noRep(PGA_Players[] items) {
        dynNoRep = new double[items.length + 1][(int) (salary + 1)][(int) (maxprod + 1)];
        for(int j = 0; j <= salary; j++) {
            dynNoRep[0][j][0] = 0;
        }
        for(int i = 0; i <= maxprod; i++) {
            dynNoRep[0][0][i] = 0;
        }
        for(int i = 0; i <= items.length; i++) {
            dynNoRep[i][0][0] = 0;
        }
        for(int i = 1; i <= items.length; i++) {
            for (int j = 0; j <= salary; j++) {
                for (int h = 0; h <= maxprod; h++) {
                    if (items[i - 1].Salary > j)
                        // If the item i is too big, I  can't put it and the solution is the same of the problem with i - 1 items
                        dynNoRep[i][j][h] = dynNoRep[i - 1][j][h];
                    else {
                        if (items[i - 1].Ownership > h)
                            // If the item i is too voluminous, I can't put it and the solution is the same of the problem with i - 1 items
                            dynNoRep[i][j][h] = dynNoRep[i - 1][j][h];
                        else {
                            // The item i could be useless and the solution is the same of the problem with i - 1 items, or it could be
                            // useful and the solution is "(solution of knapsack of size j - item[i].size and volume h - item[i].volume) + item[i].value"
                            dynNoRep[i][j][h] = Math.max(dynNoRep[i - 1][j][h], dynNoRep[i - 1][(int) (j - items[i - 1].Salary)][(int) (h - items[i - 1].Ownership)] + items[i - 1].Projection);
                        }
                    }
                }
            }
        }
    }
}

