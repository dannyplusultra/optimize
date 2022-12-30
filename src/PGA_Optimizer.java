import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Array;
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

    public int numPlayers = 6;

    private ArrayList <PGA_Players> Players;

    public PGA_Optimizer() {
        Players = new ArrayList<PGA_Players>();
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
                Players = new ArrayList<>();
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
                       records.remove(0);
                       for (List<String> player : records){
                           Players.add(new PGA_Players(player.get(1), Double.parseDouble(player.get(5))/100, (Double.parseDouble(player.get(9).replaceAll("%","")))/100, Double.parseDouble(player.get(10))));
                       }
                       int o = 0;
                   }
                }
            }
        });

        RunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //PGA_Players[] items = (PGA_Players[]) Players.toArray();
                System.out.print("We have the following " + Players.size() + " items (value, size, volume): ");
                for(int i = 0; i < Players.size(); i++)
                    System.out.print("(" + Players.get(i).Projection + ", " + Players.get(i).Salary + ", " + Players.get(i).Ownership + ") ");
                System.out.println();
                System.out.println("And a knapsack of size " + salary + " and volume " + maxprod);


                //maxProfit(Players, salary, numPlayers);
                noRep(Players);
                System.out.println();
                // Print the solution
                double j = salary, h = 6, finalSalary = 0, finalProjection = 0, finalOwnership = 1;
                System.out.print("Items picked (value, size, volume) for 0/1 problems without repetitions: ");
                for(int i = Players.size(); i > 0; i--) {
                    if(dynNoRep[i][(int)j][(int)h] != dynNoRep[i - 1][(int)j][(int)h]) {
                        System.out.print("("+ Players.get(i-1).NameID + ", " + Players.get(i-1).Projection + ", " + Players.get(i-1).Salary + ", " + Players.get(i-1).Ownership + ") ");
                        finalSalary += Players.get(i-1).Salary;
                        finalProjection += Players.get(i-1).Projection;
                        finalOwnership *= Players.get(i-1).Ownership;
                        j -= Players.get(i-1).Salary;
                        h -= Players.get(i-1).Ownership;
                    }
                }
                System.out.println();
                System.out.println(" Final size: " + finalSalary);
                System.out.println(" Final Prod Ownership: " + finalOwnership);
                System.out.println(" Final value: " + finalProjection);

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
    private ItemProjection[][][] dynNoRep;

    private void noRep(ArrayList<PGA_Players> items) {
        maxprod = maxprod*10;
        salary = salary / 100;

        dynNoRep = new ItemProjection[items.size() + 1][(int) (salary + 1)][(int) (numPlayers+1)];
        for(int k = 0; k <= items.size(); k++) {
            for (int j = 0; j <= salary; j++) {
                for (int i = 0; i <= numPlayers; i++) {
                    dynNoRep[k][j][i] = new ItemProjection(1, 0);
                }
            }
        }

        //start with 1 player- because 0 doesn't make sense
        for(int i = 1; i <= items.size(); i++) {
            //for every salary
            for (int j = 1; j <= salary; j++) {
                //for every combination of player
                for (int h = 1; h <= numPlayers; h++) {
                    //all times we get a player's attributes we do i-1 because we're indexing at 1 but we're index 0 really, ie i-1 is current
                    if (items.get(i-1).Salary > j)
                        // If the item i is too big, I  can't put it and the solution is the same of the problem with i - 1 items
                        dynNoRep[i][j][h] = dynNoRep[i - 1][j][h];
                    else {
                        //if the prodOnwership is > max prod, the solution is the same as the problem with i-1 items (ie don't use curr player)
                        if (items.get(i-1).Ownership * dynNoRep[i-1][j][h].prodOwnership > maxprod)
                            // If the item i is too voluminous, I can't put it and the solution is the same of the problem with i - 1 items
                            dynNoRep[i][j][h] = dynNoRep[i - 1][j][h];
                        else {
                            // The item i could be useless and the solution is the same of the problem with i - 1 items, or it could be
                            // useful and the solution is "(solution of knapsack of size j - item[i].size and volume h - item[i].volume) + item[i].value"

                            //iff previous projection is greater than getting rid of previous and putting in new, take previous
                            if(dynNoRep[i - 1][j][h].Projection > dynNoRep[i - 1][(int) (j - items.get(i-1).Salary)][h-1].Projection + items.get(i-1).Projection){
                                dynNoRep[i][j][h] = dynNoRep[i - 1][j][h];
                            }
                            else{
                                dynNoRep[i][j][h] = new ItemProjection(dynNoRep[i - 1][(int) (j - items.get(i-1).Salary)][h-1].prodOwnership * items.get(i-1).Ownership, dynNoRep[i - 1][(int) (j - items.get(i-1).Salary)][h-1].Projection + items.get(i-1).Projection);
                            }
                        }
                    }
                }
            }
        }
    }

    // To store the dp values
    public double[][][] dp;

    public void maxProfit(ArrayList<PGA_Players> items, double maxSalary, int numPlayers)
    {
        dp = new double[items.size()+1][(int)maxSalary+1][numPlayers+1];
        // for each element given
        for(int i = 1; i <= items.size(); i++)
        {

            // For each possible
            // weight value
            for(int j = 1; j <= maxSalary; j++)
            {

                // For each case where
                // the total elements are
                // less than the constraint
                for(int k = 1; k <= numPlayers; k++)
                {

                    // To ensure that we dont
                    // go out of the array
                    if (j >= items.get(i - 1).Salary)
                    {
                        dp[i][j][k] = Math.max(dp[i - 1][j][k],
                                dp[i - 1][(int) (j - items.get(i - 1).Salary)][k - 1] + items.get(i - 1).Projection);
                    }
                    else
                    {
                        dp[i][j][k] = dp[i - 1][j][k];
                    }
                }
            }
        }
        //return dp[n][max_W][max_E];
    }
}

