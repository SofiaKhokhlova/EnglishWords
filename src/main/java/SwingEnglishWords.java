import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class SwingEnglishWords extends JFrame{
    public JPanel panel;
    public JLabel mainLabel;
    public JLabel label;
    public JTextField textField;
    public JButton button;
    public JButton wordsButton;
    public JButton restartButton;

    public SwingEnglishWords() {
        try {
            Vocabulary vocabulary = new Vocabulary(new File("eng.docx"));

            panel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();

            mainLabel = new JLabel("Translate the word(s) into English");
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(mainLabel, gbc);

            gbc.gridy++;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            label = new JLabel(vocabulary.getRandomWord());
            label.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(label, gbc);

            gbc.gridy++;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            textField = new JTextField(20);

            panel.add(textField, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.LINE_END;

            button = new JButton("Check");

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = textField.getText();
                    String answer = checkWords(vocabulary, label, text);

                    label.setText(answer);
                    if (answer.equals("Correct"))
                        panel.setBackground(new Color(132, 165, 157));
                    else
                        panel.setBackground(new Color(242, 132, 130));

                    Timer timer = new Timer(1500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (vocabulary.getRandomWord().equals("No words")) {
                                panel.setBackground(new Color(246, 189, 96));
                                mainLabel.setText("You know ALL words. Click to 'Refresh' to start again.");
                            } else {
                                label.setText(vocabulary.getRandomWord());
                                panel.setBackground(new Color(247, 237, 226));
                                textField.setText("");
                            }
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            });

            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                        button.doClick();
                }
            });

            panel.add(button, gbc);

            panel.setBackground(new Color(247, 237, 226));

            wordsButton = new JButton("Words");
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.LAST_LINE_START;

            wordsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingFrameManager.setEnglishWords(SwingEnglishWords.this);
                    SwingFrameManager.setWordsFrame(new SwingWordsFrame());
                    SwingFrameManager.switchToWords();
                }
            });

            panel.add(wordsButton, gbc);

            restartButton = new JButton("Restart");
            gbc.gridx = 1;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.LAST_LINE_END;

            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    vocabulary.restartCheck();
                    panel.setBackground(new Color(247, 237, 226));
                    mainLabel.setText("Translate the word(s) into English");
                    label.setText(vocabulary.getRandomWord());
                    textField.setText("");
                }
            });

            panel.add(restartButton, gbc);

            getContentPane().add(panel);

            setTitle("English Words");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setSize(800, 400);
            setLocationRelativeTo(null);
            setVisible(true);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public String checkWords(Vocabulary vocabulary, JLabel label, String word) {
        while (!vocabulary.getToCheck().isEmpty()){
             return vocabulary.check(label.getText(), word);
        }
        return null;
    }
}
