import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KeywordAnalysis {
    public static void main(String[] args) throws IOException {
        String userId = "YOUR_USER_ID"; // Replace with your user ID
        analyzeKeywords(userId);
    }

    public static void analyzeKeywords(String userId) throws IOException {
        File f = new File(userId);
        File f1 = new File("category.txt");
        File f2 = new File("express.txt");
        File f3 = new File("word.txt");
        File f4 = new File("name.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
             BufferedWriter f1Writer = Files.newBufferedWriter(Paths.get("category.txt"), StandardOpenOption.CREATE);
             BufferedWriter f2Writer = Files.newBufferedWriter(Paths.get("express.txt"), StandardOpenOption.CREATE);
             BufferedWriter f3Writer = Files.newBufferedWriter(Paths.get("word.txt"), StandardOpenOption.CREATE);
             BufferedWriter f4Writer = Files.newBufferedWriter(Paths.get("name.txt"), StandardOpenOption.CREATE)) {

            // ... (initialize the necessary data structures)
            List<String> words = new ArrayList<>();
            Map<String, Integer> record = new HashMap<>();
            Map<String, Integer> express = new HashMap<>();
            Map<String, Integer> nameSet = new HashMap<>();

            // ... (continue with the rest of the code)
            String line;
            while ((line = reader.readLine()) != null) {
                String item = line.split(" ", 2)[1];
                Matcher exAllMatcher = Pattern.compile("\\[.*?]").matcher(item);

                while (exAllMatcher.find()) {
                    String exItem = exAllMatcher.group();
                    express.put(exItem, express.getOrDefault(exItem, 0) + 1);
                }

                // ... (analyze keywords)

                item = item.replaceAll("\\[.*?]", "");
                List<Term> termList = StandardTokenizer.segment(item);

                for (Term term : termList) {
                    words.add(term.word);
                    if (term.nature.startsWith("nr")) {
                        nameSet.put(term.word, nameSet.getOrDefault(term.word, 0) + 1);
                    }
                }
            }

            // ... (write the results to the output files)
            Map<String, Long> wordCount = words.stream().filter(w -> w.length() >= 2)
                    .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

            wordCount.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> {
                        try {
                            f3Writer.write(entry.getKey() + " " + entry.getValue() + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            // ... (write category, express, and name data to respective files)
        }
    }
}
