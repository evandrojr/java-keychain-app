package com.example.keychainapp.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.example.keychainapp.logic.SystemKeychain;
import java.util.List;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField keyField;
    private JTextField valueField;
    private JButton saveButton;
    private JButton loadButton;
    private JTextArea outputArea;


    public MainFrame() {
        setTitle("Keychain App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Key:"));
        keyField = new JTextField();
        inputPanel.add(keyField);

        inputPanel.add(new JLabel("Value:"));
        valueField = new JTextField();
        inputPanel.add(valueField);


        saveButton = new JButton("Save");
        inputPanel.add(saveButton);

        loadButton = new JButton("Load");
        inputPanel.add(loadButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);


        // Mensagem inicial
        outputArea.append("Enter the key and click Load to fetch the value.\n");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                if (key != null && !key.isEmpty()) {
                    String loaded = SystemKeychain.loadPassword("KeychainApp", key);
                    if (loaded != null) {
                        valueField.setText(loaded);
                        outputArea.append("Value loaded for key '" + key + "': " + loaded + "\n");
                    } else {
                        valueField.setText("");
                        outputArea.append("No value found for key '" + key + "'.\n");
                    }
                } else {
                    outputArea.append("Please enter the key to load the value.\n");
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                String value = valueField.getText();
                if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
                    boolean ok = SystemKeychain.savePassword("KeychainApp", key, value);
                    if (ok) {
                        outputArea.append("Value saved in the system keychain for key: " + key + ". Value: " + value + "\n");
                    } else {
                        outputArea.append("Error saving to the system keychain.\n");
                    }
                    keyField.setText("");
                    valueField.setText("");
                } else {
                    outputArea.append("Please fill in both fields!\n");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}