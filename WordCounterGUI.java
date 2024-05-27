import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
        setTitle("Word Counter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setBackground(Color.lightGray);

        wordCountLabel = new JLabel("Total Word Count: 0");
        mostRepeatedWordLabel = new JLabel("Most Repeated Word: N/A");
        filePathLabel = new JLabel("File Path:");
        countButton = new JButton("Count Words");
        fileButton = new JButton("Load File");
        fileChooser = new JFileChooser();

        tableModel = new DefaultTableModel(new String[]{"Word", "Frequency"}, 0);
        wordTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(wordTable);

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

        countButton.addActionListener(e -> {
            String text = textArea.getText();
            Map<String, Integer> wordCountMap = countWords(text);
            displayWordFrequency(wordCountMap);
            findMostRepeatedWord(wordCountMap);
        });

        fileButton.addActionListener(e -> {
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getPath();
                filePathLabel.setText("File Path: " + filePath);
                try {
                    String text = readFile(filePath);
                    textArea.setText(text);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine())!= null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    private Map<String, Integer> countWords(String text) {
        return Arrays.stream(text.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+"))
               .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    }

    private void displayWordFrequency(Map<String, Integer> wordCountMap) {
        tableModel.setRowCount(0);
        int totalWordCount = 0;
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            totalWordCount += entry.getValue();
        }
        tableModel.addRow(new Object[]{"Total Word Count", totalWordCount});
        wordTable.setRowSelectionInterval(tableModel.getRowCount() - 1, tableModel.getRowCount() - 1);
        wordTable.setSelectionBackground(Color.YELLOW);
        wordCountLabel.setText("Total Word Count: " + totalWordCount);
    }

    private void findMostRepeatedWord(Map<String, Integer> wordCountMap) {
        wordCountMap.entrySet().stream()
               .max(Map.Entry.comparingByValue())
               .ifPresent(mostRepeated -> {
                    mostRepeatedWordLabel.setText("Most Repeated Word: " + mostRepeated.getKey() + " (" + mostRepeated.getValue() + ")");
                });
    }

    private void restrictCopyPaste(JTextArea textArea) {
        textArea.setTransferHandler(null); // Disable paste action
        textArea.setDragEnabled(false); // Disable drag action
        textArea.setLineWrap(true); // Enable line wrapping
        textArea.setWrapStyleWord(true); // Wrap at word boundaries
        ((AbstractDocument) textArea.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text!= null) {
                    super.replace(fb, offset, length, text.replaceAll("\\p{Cntrl}", ""), attrs); // Remove control characters
                } else {
                    super.replace(fb, offset, length, "", attrs);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WordCounterGUI::new);
    }
}