package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class Main extends JFrame {
    private JPanel mainPanel;
    private JPanel adminPanel;
    private JPanel userPanel;

    private double costPerKilometer = 0.3;
    private double cateringCost = 15;
    private double kilometerLimit = 100;
    private double cateringLimit = 50;
    private double totalLimit = 200;

    private JList<String> globalDataList;
    private JList<ExpenseItem> expensesJList;
    private JList<ExpenseItem> expensesJList2;
    private DefaultListModel<ExpenseItem> expensesListModel;
    private DefaultListModel<ExpenseItem> userExpensesListModel;

    private JTextField cateringDaysField;
    private JTextField distanceField;
    private JComboBox<String> startDateComboBox;
    private JComboBox<String> endDateComboBox;

    public Main() {
        setTitle("Aplikacja z menu");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        adminPanel = new JPanel();
        userPanel = new JPanel();

        initializeMainPanel();
        initializeAdminPanel();
        initializeUserPanel();

        setContentPane(mainPanel);
    }

    private void initializeMainPanel() {
        mainPanel.setLayout(new BorderLayout());

        String[] globalData = {
                "Koszt za kilometr: " + costPerKilometer,
                "Koszt kateringu: " + cateringCost,
                "Limit zwrotu na kilometr: " + kilometerLimit,
                "Limit zwrotu na katering: " + cateringLimit,
                "Limit zwrotu całkowity: " + totalLimit
        };
        globalDataList = new JList<>(globalData);

        JButton adminButton = new JButton("Admin");
        JButton userButton = new JButton("Użytkownik");

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(adminPanel);
                revalidate();
            }
        });

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(userPanel);
                revalidate();
            }
        });

        mainPanel.add(new JScrollPane(globalDataList), BorderLayout.CENTER);
        mainPanel.add(adminButton, BorderLayout.WEST);
        mainPanel.add(userButton, BorderLayout.EAST);
    }

    private void initializeAdminPanel() {
        adminPanel.setLayout(new GridLayout(9, 2));

        JLabel adminLabel = new JLabel("Panel Administratora");

        JLabel costPerKilometerLabel = new JLabel("Koszt za kilometr:");
        JTextField costPerKilometerField = new JTextField(Double.toString(costPerKilometer));

        JLabel cateringCostLabel = new JLabel("Koszt kateringu:");
        JTextField cateringCostField = new JTextField(Double.toString(cateringCost));

        JLabel kilometerLimitLabel = new JLabel("Limit zwrotu na kilometr:");
        JTextField kilometerLimitField = new JTextField(Double.toString(kilometerLimit));

        JLabel cateringLimitLabel = new JLabel("Limit zwrotu na katering:");
        JTextField cateringLimitField = new JTextField(Double.toString(cateringLimit));

        JLabel totalLimitLabel = new JLabel("Limit zwrotu całkowity:");
        JTextField totalLimitField = new JTextField(Double.toString(totalLimit));

        JButton editExpenseButton = new JButton("Edytuj Limit rachunku");
        editExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedExpense();

            }
        });
        JButton addExpenseButton = new JButton("Dodaj rachunek");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpenseLimit();
            }
        });
        expensesListModel = new DefaultListModel<>();
        expensesJList = new JList<>(expensesListModel);
        expensesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expensesJList.setVisibleRowCount(5);



        expensesJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = expensesJList.getSelectedIndex();
                if (selectedIndex != -1) {
                    ExpenseItem selectedExpense = expensesListModel.getElementAt(selectedIndex);

                }
            }
        });

        JButton backButtonAdmin = new JButton("Powrót do menu");
        backButtonAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                costPerKilometer = Double.parseDouble(costPerKilometerField.getText());
                cateringCost = Double.parseDouble(cateringCostField.getText());
                kilometerLimit = Double.parseDouble(kilometerLimitField.getText());
                cateringLimit = Double.parseDouble(cateringLimitField.getText());
                totalLimit = Double.parseDouble(totalLimitField.getText());

                updateGlobalDataList();


                setContentPane(mainPanel);
                revalidate();
            }
        });

        adminPanel.add(adminLabel);
        adminPanel.add(new JLabel());
        adminPanel.add(costPerKilometerLabel);
        adminPanel.add(costPerKilometerField);
        adminPanel.add(cateringCostLabel);
        adminPanel.add(cateringCostField);
        adminPanel.add(kilometerLimitLabel);
        adminPanel.add(kilometerLimitField);
        adminPanel.add(cateringLimitLabel);
        adminPanel.add(cateringLimitField);
        adminPanel.add(totalLimitLabel);
        adminPanel.add(totalLimitField);
        adminPanel.add(addExpenseButton);
        adminPanel.add(editExpenseButton);
        adminPanel.add(new JLabel("Wybierz rachunek:"));
        adminPanel.add(new JScrollPane(expensesJList));
        adminPanel.add(backButtonAdmin);
    }

    private void editSelectedExpense() {
        int selectedIndex = expensesJList.getSelectedIndex();
        if (selectedIndex != -1) {
            ExpenseItem selectedExpense = expensesListModel.getElementAt(selectedIndex);
            String newValue = JOptionPane.showInputDialog(this, "Edytuj wartość rachunku:", selectedExpense.getValue());
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    double newExpenseValue = Double.parseDouble(newValue);
                    selectedExpense.setValue(newExpenseValue);
                    expensesListModel.setElementAt(selectedExpense, selectedIndex);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Wprowadź poprawną wartość liczbową.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Wybierz rachunek do edycji.");
        }
    }

    private void initializeUserPanel() {
        userPanel.setLayout(new GridLayout(16, 2));

        JLabel userLabel = new JLabel("Panel Użytkownika");

        JLabel startDateLabel = new JLabel("Data początkowa:");
        startDateComboBox = createComboBoxForDate();

        JLabel endDateLabel = new JLabel("Data końcowa:");
        endDateComboBox = createComboBoxForDate();

        JLabel cateringDaysLabel = new JLabel("Ilość dni kateringu:");
        cateringDaysField = new JTextField();

        JLabel distanceLabel = new JLabel("Ilość przejechanych km:");
        distanceField = new JTextField();

        JButton calculateButton = new JButton("Oblicz");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateValues();
            }
        });

        JButton addExpenseButton = new JButton("Dodaj rachunek");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });

        JButton backButtonUser = new JButton("Powrót do menu");
        backButtonUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(mainPanel);
                revalidate();
            }
        });

        userExpensesListModel = new DefaultListModel<>();
        expensesJList2 = new JList<>(userExpensesListModel);

        userPanel.add(userLabel);
        userPanel.add(new JLabel());
        userPanel.add(startDateLabel);
        userPanel.add(startDateComboBox);
        userPanel.add(endDateLabel);
        userPanel.add(endDateComboBox);
        userPanel.add(cateringDaysLabel);
        userPanel.add(cateringDaysField);
        userPanel.add(distanceLabel);
        userPanel.add(distanceField);
        userPanel.add(new JLabel());
        userPanel.add(addExpenseButton);
        userPanel.add(new JLabel());
        userPanel.add(new JScrollPane(expensesJList2));
        userPanel.add(new JLabel());
        userPanel.add(calculateButton);
        userPanel.add(backButtonUser);
    }

    private void addExpenseLimit() {
        String expenseName = JOptionPane.showInputDialog(this, "Podaj nazwę rachunku:");
        if (expenseName != null && !expenseName.isEmpty()) {
            try {
                double expenseValue = Double.parseDouble(JOptionPane.showInputDialog(this, "Podaj limit rachunku:"));
                ExpenseItem newExpense = new ExpenseItem(expenseName, expenseValue);
                expensesListModel.addElement(newExpense);


            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Wprowadź poprawną wartość liczbową.");
            }
        }
    }
    private void addExpense() {
        int expenseCount = expensesListModel.size();

        if (expenseCount == 0) {
            JOptionPane.showMessageDialog(this, "Brak dostępnych rachunków.");
            return;
        }

        String[] expenseOptions = new String[expenseCount];
        for (int i = 0; i < expenseCount; i++) {
            ExpenseItem expenseItem = expensesListModel.getElementAt(i);
            expenseOptions[i] = expenseItem.getName();
        }

        String selectedExpense = (String) JOptionPane.showInputDialog(
                this,
                "Wybierz rachunek:",
                "Wybór rachunku",
                JOptionPane.PLAIN_MESSAGE,
                null,
                expenseOptions,
                expenseOptions[0]);

        if (selectedExpense != null) {
            try {
                double expenseValue = Double.parseDouble(JOptionPane.showInputDialog(this, "Podaj wartość rachunku:"));


                for (int i = 0; i < expenseCount; i++) {
                    ExpenseItem expenseItem = expensesListModel.getElementAt(i);
                    if (selectedExpense.equals(expenseItem.getName())) {
                        if (expenseValue > expenseItem.getValue()) {

                            expenseValue = Math.min(expenseValue, expenseItem.getValue());
                        }
                        break;
                    }
                }

                ExpenseItem newExpense = new ExpenseItem(selectedExpense, expenseValue);
                userExpensesListModel.addElement(newExpense);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Wprowadź poprawną wartość liczbową.");
            }
        }
    }





    private JComboBox<String> createComboBoxForDate() {
        JComboBox<String> comboBox = new JComboBox<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        for (int i = 0; i < 365; i++) {
            Date date = calendar.getTime();
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
            comboBox.addItem(dateString);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return comboBox;
    }

    private void calculateValues() {
        try {
            double cateringDays = Double.parseDouble(cateringDaysField.getText());
            double distance = Double.parseDouble(distanceField.getText());

            String startDateString = (String) startDateComboBox.getSelectedItem();
            String endDateString = (String) endDateComboBox.getSelectedItem();

            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);

            // Calculate the number of days between start and end dates
            long daysBetween = ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant());

            // Ensure catering days are within the range of the selected dates
            if (cateringDays > daysBetween+1) {
                JOptionPane.showMessageDialog(this, "Ilość dni kateringu nie może być większa niż ilość dni w wybranym przedziale dat.");
                return; // Exit the method
            }

            double foodValue = cateringDays * cateringCost;
            double kmValue = distance * costPerKilometer;

            double totalValue = foodValue + kmValue;

            // Calculate expenses from the added items
            double addedExpensesTotal = 0;
            for (int i = 0; i < userExpensesListModel.size(); i++) {
                addedExpensesTotal += userExpensesListModel.getElementAt(i).getValue();
            }

            totalValue += addedExpensesTotal; // Include added expenses in total

            // Display results
            JOptionPane.showMessageDialog(this,
                    "Wartość jedzenia: " + foodValue +
                            "\nWartość za km: " + kmValue +
                            "\nDodatkowe rachunki: " + addedExpensesTotal +
                            "\nRazem: " + totalValue +
                            "\nData początkowa: " + startDateString +
                            "\nData końcowa: " + endDateString);
            userExpensesListModel.clear();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Wystąpił błąd. Sprawdź wprowadzone wartości.");
        }
    }


    private void updateGlobalDataList() {
        String[] globalData = {
                "Koszt za kilometr: " + costPerKilometer,
                "Koszt kateringu: " + cateringCost,
                "Limit zwrotu na kilometr: " + kilometerLimit,
                "Limit zwrotu na katering: " + cateringLimit,
                "Limit zwrotu całkowity: " + totalLimit
        };
        globalDataList.setListData(globalData);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main app = new Main();
                app.setVisible(true);
            }
        });
    }
}

class ExpenseItem {
    private String name;
    private double value;

    public ExpenseItem(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}