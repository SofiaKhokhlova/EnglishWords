import javax.swing.*;

public class SwingFrameManager {
    private static SwingEnglishWords englishWordsFrame;
    private static SwingWordsFrame wordsFrame;

    public static void setEnglishWords(SwingEnglishWords sew) {
        englishWordsFrame = sew;
    }

    public static void setWordsFrame(SwingWordsFrame swf) {
        wordsFrame = swf;
    }

    public static SwingEnglishWords getEnglishWordsFrame() {
        return englishWordsFrame;
    }

    public static SwingWordsFrame getWordsFrame() {
        return wordsFrame;
    }

    public static void switchToWords() {
        englishWordsFrame.setVisible(false);
        wordsFrame.setVisible(true);
    }

    public static void switchToEnglishWords() {
        wordsFrame.setVisible(false);
        englishWordsFrame.setVisible(true);
    }
}
