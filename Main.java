import java.io.*;
import java.util.*;

public class Main {

    // Метод для удаления дублирующихся строк и записи информации в файл назначения
    public static void removeDuplicates(String inputFile, String outputFile) {
        Map<String, Integer> lineCount = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                lineCount.put(line, lineCount.getOrDefault(line, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : lineCount.entrySet()) {
                writer.write(entry.getKey());
                writer.newLine();
                if (entry.getValue() > 1) {
                    writer.write("[Removed Duplicates: " + (entry.getValue() - 1) + "]");
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка чтения/записи файла: " + e.getMessage());
        }
    }

    // Метод для восстановления файла из сжатой версии
    public static void restoreFile(String compressedFile, String restoredFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(compressedFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(restoredFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[Removed Duplicates: ")) {
                    int duplicates = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                    String previousLine = ((LinkedList<String>)((ArrayList<String>)Arrays.asList(line.split("\n")))).get(0);
                    for (int i = 0; i < duplicates; i++) {
                        writer.write(previousLine);
                        writer.newLine();
                    }
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка чтения/записи файла: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Пример использования
        String inputFile = "input.txt";
        String compressedFile = "compressed.txt";
        String restoredFile = "restored.txt";

        // Удаление дубликатов
        removeDuplicates(inputFile, compressedFile);
        System.out.println("Файл с удалёнными дубликатами создан: " + compressedFile);

        // Восстановление исходного файла
        restoreFile(compressedFile, restoredFile);
        System.out.println("Файл восстановлен: " + restoredFile);
    }
}
