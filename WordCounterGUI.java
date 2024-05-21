import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;

public class WordCounterGUI extends JFrame {
    private JTextArea textArea;
    private JLabel wordCountLabel;
    private JLabel filePathLabel;
    private JLabel mostRepeatedWordLabel;
    private JButton countButton;
    private JButton fileButton;
    private JFileChooser fileChooser;
    private JTable wordTable;
    private DefaultTableModel tableModel;

    public WordCounterGUI() {
        // Set up the frame
        setTitle("Word Counter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        textArea = new JTextArea();
        wordCountLabel = new JLabel("Total Word Count: 0");
        mostRepeatedWordLabel = new JLabel("Most Repeated Word: N/A");
        filePathLabel = new JLabel("File Path:");
        countButton = new JButton("Count Words");
        fileButton = new JButton("Load File");
        fileChooser = new JFileChooser();

        // Set up the table for word frequency
        tableModel = new DefaultTableModel(new String[]{"Word", "Frequency"}, 0);
        wordTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(wordTable);

        // Set up layout
        setLayout(new BorderLayout());
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(wordCountLabel);
        southPanel.add(mostRepeatedWordLabel);
        southPanel.add(countButton);
        southPanel.add(fileButton);
        southPanel.add(filePathLabel);
        add(southPanel, BorderLayout.SOUTH);
        add(tableScrollPane, BorderLayout.EAST);

        // Add action listeners
        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                Map<String, Integer> wordCountMap = countWords(text);
                displayWordFrequency(wordCountMap);
                findMostRepeatedWord(wordCountMap);
            }
        });

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getPath();
                    filePathLabel.setText("File Path: " + filePath);
                    try {
                        String text = readFile(filePath);
                        textArea.setText(text);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage());
                    }
                }
            }
        });

        // Make frame visible
        setVisible(true);
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    private Map<String, Integer> countWords(String text) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        if (text.trim().isEmpty()) {
            return wordCountMap;
        }
        String[] words = text.split("\\s+");
        for (String word : words) {
            word = word.toLowerCase();
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }
        return wordCountMap;
    }

    private void displayWordFrequency(Map<String, Integer> wordCountMap) {
        tableModel.setRowCount(0); // Clear the table
        int totalWordCount = 0;
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            totalWordCount += entry.getValue();
        }
        // Add total word count as the last row and highlight it
        tableModel.addRow(new Object[]{"Total Word Count", totalWordCount});
        wordTable.setRowSelectionInterval(tableModel.getRowCount() - 1, tableModel.getRowCount() - 1);
        wordTable.setSelectionBackground(Color.YELLOW);

        // Update the label to reflect the correct total word count
        wordCountLabel.setText("Total Word Count: " + totalWordCount);
    }

    private void findMostRepeatedWord(Map<String, Integer> wordCountMap) {
        if (wordCountMap.isEmpty()) {
            mostRepeatedWordLabel.setText("Most Repeated Word: N/A");
            return;
        }
        Map.Entry<String, Integer> mostRepeated = wordCountMap.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .orElse(null);
        if (mostRepeated != null) {
            mostRepeatedWordLabel.setText("Most Repeated Word: " + mostRepeated.getKey() + " (" + mostRepeated.getValue() + ")");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordCounterGUI());
    }
}
