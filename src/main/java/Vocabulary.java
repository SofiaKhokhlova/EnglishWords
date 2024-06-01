import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.util.*;

public class Vocabulary {
    private Map<Integer, Map<String, String>> voc;
    private int index = 0;
    private int lastIndWord = -1;

    private Map< Map<String, String>, Integer> toCheck = new HashMap<>();
    private int toCheckMapSize;

    private final int from;
    private int to;

    public Vocabulary(File file) throws IOException {
        int rows = -1;
        int count = 1;
        voc = new HashMap<>();
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument doc = new XWPFDocument(fis);

        for (XWPFTable table : doc.getTables()) {
            rows = table.getRows().size();
        }

        this.from = 0;
        this.to = rows -1;

        List<XWPFTable> tables = doc.getTables();
        for (XWPFTable table : tables) {
            for (XWPFTableRow row : table.getRows()) {
                String prev = "";
                for (XWPFTableCell cell : row.getTableCells()) {
                    if (cell.getText().equals("")) {
                        continue;
                    }

                    HashMap<String, String> subMap = new HashMap<>();

                    if (count % 2 == 1)
                        prev = cell.getText();
                    else {
                        subMap.put(cell.getText(), prev);
                        voc.put(index, subMap);
                        index++;
                    }
                    count++;
                }
            }
        }
        for (int i = from; i <= to; i++)
            toCheck.put(voc.get(i), 0);

        fis.close();
        doc.close();
    }

    public void showVocWithInd() {
        voc.entrySet().forEach(System.out::println);
    }

    public Map<Map<String, String>, Integer> getToCheck() {
        return toCheck;
    }

    public Map<Integer, Map<String, String>> showVoc() {
        /*Map<String, String> subMap = new HashMap<>();
        for (Map<String, String> i: voc.values()) {
            subMap.putAll(i);
        }*/
        return voc;
    }

    public void showMapToCheck () {
        toCheck.entrySet().forEach(System.out::println);
    }

    public String getRandomWord() {
        if(toCheck.isEmpty()){
            return "No words";
        }

        List<Map<String, String>> wordsList = new ArrayList<>(toCheck.keySet());
        Map<String, String> randomWordMap = wordsList.get(new Random().nextInt(wordsList.size()));
        Object[] arr = randomWordMap.entrySet().toArray();
        return arr[0].toString().split("=")[0];
    }

    public String check(String word, String answer) {
        String resultAnswer = "";
        for (Map.Entry<Map<String, String>, Integer> entry : toCheck.entrySet()) {
            Map<String, String> subMap = entry.getKey();
            if (!subMap.containsKey(word)) {
                continue;
            }
            String correctAnswer = subMap.get(word);
            String temp = answer.trim();
            if (temp.equalsIgnoreCase(correctAnswer)) {
                int switcher = entry.getValue();
                switch (switcher) {
                    case 0:
                        toCheck.put(subMap, 1);
                        resultAnswer = "Correct";
                        break;
                    case 1:
                        toCheck.remove(subMap);
                        resultAnswer = "Correct";
                        break;
                }
                break;
            } else {
                resultAnswer = "Incorrect! The correct answer is: " + correctAnswer;
            }
        }
        return resultAnswer;
    }

    public void restartCheck() {
        if(toCheck.isEmpty()){
            toCheck = new HashMap<>();
            for (int i = from; i <= to; i++)
                toCheck.put(voc.get(i), 0);
        } else {
            toCheck.clear();
            for (int i = from; i <= to; i++)
                toCheck.put(voc.get(i), 0);
        }
    }

    public int getVocSize() {return voc.size();}

    public void addNewWord(String newEnglishWord, String newTranslation) {
        try {
            FileInputStream fis = new FileInputStream(new File("eng.docx"));
            XWPFDocument document = new XWPFDocument(fis);

            XWPFTable table = document.getTables().get(0);

            XWPFTableRow lastRow = table.getRow(table.getRows().size() - 1);

            XWPFTableRow newRow = table.createRow();

            XWPFTableCell englishCell = newRow.getCell(0);
            if (englishCell == null) {
                englishCell = newRow.createCell();
            }
            englishCell.setText(newEnglishWord);

            XWPFTableCell translationCell = newRow.getCell(1);
            if (translationCell == null) {
                translationCell = newRow.createCell();
            }
            translationCell.setText(newTranslation);

            FileOutputStream fos = new FileOutputStream(new File("eng.docx"));
            document.write(fos);
            fos.close();
            document.close();

            System.out.println("New word added in file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void refreshVoc() {

    }
}
