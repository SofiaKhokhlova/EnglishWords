import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SwingWordsFrame extends JFrame {
    private JPanel panel;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private JButton saveButton;
    private JButton newWordButton;
    private JButton backButton;

    public SwingWordsFrame() {
        try {
            Vocabulary vocabulary = new Vocabulary(new File("eng.docx"));

            panel = new JPanel(new GridBagLayout());
            panel.setBackground(new Color(247, 237, 226));

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            topPanel.setBackground(new Color(247, 237, 226));
            JLabel mainLabel = new JLabel("Words("+vocabulary.getVocSize()+")");
            mainLabel.setFont(new Font(mainLabel.getFont().getName(), Font.BOLD, mainLabel.getFont().getSize()));
            topPanel.add(mainLabel);
            getContentPane().add(topPanel, BorderLayout.NORTH);

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;

            for(Map.Entry<Integer, Map<String, String>> itr: vocabulary.showVoc().entrySet()){
                for (Map.Entry<String, String> entry : itr.getValue().entrySet()) {
                    int wordNumber = itr.getKey() + 1;
                    String word = entry.getKey();
                    String translation = entry.getValue();
                    JLabel wordLabel = new JLabel(wordNumber + ") " + word + " - " + translation);
                    panel.add(wordLabel, gbc);
                    gbc.gridy++;
                }
            }

            scrollPane = new JScrollPane(panel);
            getContentPane().add(scrollPane, BorderLayout.CENTER);

            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            saveButton = new JButton("Word range");
            newWordButton = new JButton("New word");
            backButton = new JButton("Back");

            newWordButton.addActionListener(e -> {
                JPanel newWordPopup = new JPanel();
                JTextField word = new JTextField(20);
                JTextField translation = new JTextField(20);

                newWordPopup.add(new JLabel("Word:"));
                newWordPopup.add(word);
                newWordPopup.add(Box.createHorizontalStrut(15));
                newWordPopup.add(new JLabel("Translation:"));
                newWordPopup.add(translation);

                int result = JOptionPane.showConfirmDialog(this, newWordPopup, "Enter the new word and its translation",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION){
                    String w = word.getText();
                    String t = translation.getText();
                    System.out.println(w + " - " + t);
                }
            });

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingFrameManager.switchToEnglishWords();
                }
            });

            buttonPanel.setBackground(new Color(247, 237, 226));

            buttonPanel.add(saveButton);
            buttonPanel.add(newWordButton);
            buttonPanel.add(backButton);

            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            setTitle("English Words");
            setSize(800, 400);
            setPreferredSize(new Dimension(800, 400));
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
