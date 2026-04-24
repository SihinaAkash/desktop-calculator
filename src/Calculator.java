import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;


public static void main(String[] args) {
    Calculator calculator = new Calculator();
}

public static class Calculator {
    int boardWidth = 360;
    int boardHeight = 540;

    Color customLightGray = new Color(212, 212, 210);
    Color customDarkGray = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);

    String[] buttonValues = {
            "CE", "C", "\u232B", "%",
            "1/x", "x\u00B2", "\u221Ax", "/",
            "7", "8", "9", "\u00D7",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "\u00B1", "0", ".", "="
    };

    String[] rightSymbols = {"%", "/", "×", "-", "+", "="};
    String[] topSymbols = {"CE", "C", "\u232B"};

    String A = "0";
    String operator = null;
    String B = null;

    JFrame frame = new JFrame("My Calculator");
    JPanel topPanel = new JPanel();
    JLabel title = new JLabel();
    JPanel titlePanel = new JPanel();
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    Calculator() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayPanel.setPreferredSize(new Dimension(boardWidth, 80));
        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        title.setVisible(true);
        title.setBackground(customBlack);
        title.setForeground(Color.white);
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setHorizontalAlignment(JLabel.LEFT);
        title.setText("Standard Calculator");
        title.setOpaque(true);

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(title);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);

        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(displayPanel, BorderLayout.NORTH);

        frame.add(topPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(6, 4));
        buttonsPanel.setBackground(customBlack);
        frame.add(buttonsPanel, BorderLayout.CENTER);

        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];
            button.setFont(new Font("DejaVu Sans", Font.PLAIN, 30));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack));

            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customLightGray);
                button.setForeground(customBlack);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customOrange);
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(customDarkGray);
                button.setForeground(Color.WHITE);
            }

            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String buttonValue = button.getText();

                    if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                        if (buttonValue.equals("%")) {
                            Double numDisplay = Double.parseDouble(displayLabel.getText());
                            numDisplay /= 100;
                            displayLabel.setText(removeZeroDecimal(numDisplay));
                        } else if (buttonValue.equals("=")) {
                            if (displayLabel.getText().matches("\\d+")) {
                                if (A != null) {
                                    B = displayLabel.getText();
                                    double numA = Double.parseDouble(A);
                                    double numB = Double.parseDouble(B);

                                    if (operator.equals("+")) {
                                        displayLabel.setText(removeZeroDecimal(numA + numB));
                                    } else if (operator.equals("-")) {
                                        displayLabel.setText(removeZeroDecimal(numA - numB));
                                    } else if (operator.equals("×")) {
                                        displayLabel.setText(removeZeroDecimal(numA * numB));
                                    } else if (operator.equals("/")) {
                                        if (numB != 0) {
                                            displayLabel.setText(removeZeroDecimal(numA / numB));
                                        } else {
                                            displayLabel.setText("Error");
                                        }
                                    }
                                    clearAll();
                                }
                            } else {
                                displayLabel.setText("0");
                            }
                        } else if ("+-×/".contains(buttonValue)) {
                            if (operator == null) {
                                A = displayLabel.getText();
                                displayLabel.setText("0");
                                B = "0";
                            }
                            operator = buttonValue;
                        }
                    } else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                        if (buttonValue.equals("C")) {
                            clearAll();
                            displayLabel.setText("0");
                        } else if (buttonValue.equals("CE")) {
                            if (A != null) {
                                B = null;
                                displayLabel.setText("0");
                            }
                        } else if (buttonValue.equals("\u232B")) {
                            String displayText = displayLabel.getText();
                            if (displayLabel.getText().length() > 1 && displayLabel.getText().matches("\\d+")) {
                                displayLabel.setText(displayText.substring(0, displayText.length() - 1));
                            } else {
                                displayLabel.setText("0");
                            }
                        }
                    } else {
                        if (buttonValue.equals(".")) {
                            if (!displayLabel.getText().contains(buttonValue)) {
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                            }
                        }
                        else if ("0123456789".contains(buttonValue)) {
                            if (displayLabel.getText().equals("0")) {
                                displayLabel.setText(buttonValue);
                            } else {
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                            }
                        } else if (buttonValue.equals("1/x")) {
                            if (!displayLabel.getText().equals("0")) {
                                Double numDisplay = Double.parseDouble(displayLabel.getText());
                                numDisplay = 1.0 / numDisplay;
                                displayLabel.setText(removeZeroDecimal(numDisplay));
                            }
                        } else if (buttonValue.equals("x\u00B2")) {
                            Double numDisplay = Double.parseDouble(displayLabel.getText());
                            numDisplay = Math.pow(numDisplay, 2);
                            displayLabel.setText(removeZeroDecimal(numDisplay));
                        } else if (buttonValue.equals("\u221Ax")) {
                            Double numDisplay = Double.parseDouble(displayLabel.getText());
                            if (numDisplay < 0) {
                                displayLabel.setText("Error");
                            } else {
                                numDisplay = Math.sqrt(numDisplay);
                                displayLabel.setText(removeZeroDecimal(numDisplay));
                            }
                        } else if (buttonValue.equals("\u00B1")) {
                            Double numDisplay = Double.parseDouble(displayLabel.getText());
                            numDisplay *= -1;
                            displayLabel.setText(removeZeroDecimal(numDisplay));
                        }
                    }
                }
            });
        }

        frame.setVisible(true);

    }

    void clearAll() {
        A = "0";
        operator = null;
        B = null;
    }

    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        }
        return  Double.toString(numDisplay);
    }
}
