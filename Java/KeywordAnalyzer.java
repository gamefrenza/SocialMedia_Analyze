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
    private static final String CATEGORY_FILE = "category.txt";
    private static final String EXPRESS_FILE = "express.txt";
    private static final String WORD_FILE = "word.txt";
    private static final String NAME_FILE = "name.txt";
    private static final String ENCODING = "UTF-8";
    private static final int MIN_WORD_LENGTH = 2;
    private static final Pattern SAFE_FILE_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    public static void main(String[] args) throws IOException {
        String userId = "YOUR_USER_ID"; // Replace with your user ID
        analyzeKeywords(userId);
    }

    public static void analyzeKeywords(String userId) throws IOException {
        // Validate userId to prevent path traversal
        if (!SAFE_FILE_PATTERN.matcher(userId).matches()) {
            throw new IllegalArgumentException("Invalid user ID format");
        }
        
        // Use Path for safer file operations
        Path userFile = Paths.get(System.getProperty("user.dir"), "data", userId)
            .normalize();
        
        // Verify the file is within allowed directory
        if (!userFile.startsWith(Paths.get(System.getProperty("user.dir"), "data"))) {
            throw new SecurityException("Access denied: Invalid file path");
        }
        
        File f = new File(userId);
        File f1 = new File(CATEGORY_FILE);
        File f2 = new File(EXPRESS_FILE);
        File f3 = new File(WORD_FILE);
        File f4 = new File(NAME_FILE);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), ENCODING));
             BufferedWriter f1Writer = Files.newBufferedWriter(Paths.get(CATEGORY_FILE), StandardOpenOption.CREATE);
             BufferedWriter f2Writer = Files.newBufferedWriter(Paths.get(EXPRESS_FILE), StandardOpenOption.CREATE);
             BufferedWriter f3Writer = Files.newBufferedWriter(Paths.get(WORD_FILE), StandardOpenOption.CREATE);
             BufferedWriter f4Writer = Files.newBufferedWriter(Paths.get(NAME_FILE), StandardOpenOption.CREATE)) {

            // ... (initialize the necessary data structures)
            List<String> words = new ArrayList<>();
            Map<String, Integer> record = new HashMap<>();
            Map<String, Integer> express = new HashMap<>();
            Map<String, Integer> nameSet = new HashMap<>();

            // ... (continue with the rest of the code)
            String line;
            while ((line = reader.readLine()) != null) {
                String item = line.split(" ", 2)[1];
                processExpressions(item, express);

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
            Map<String, Long> wordCount = words.stream().filter(w -> w.length() >= MIN_WORD_LENGTH)
                    .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

            writeResults(wordCount, f3Writer);

            // ... (write category, express, and name data to respective files)
        }
    }

    private static void processExpressions(String item, Map<String, Integer> express) {
        Matcher exAllMatcher = Pattern.compile("\\[.*?]").matcher(item);
        while (exAllMatcher.find()) {
            String exItem = exAllMatcher.group();
            express.merge(exItem, 1, Integer::sum);
        }
    }

    private static void writeResults(Map<String, Long> wordCount, BufferedWriter writer) {
        try {
            wordCount.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> {
                        try {
                            writer.write(entry.getKey() + " " + entry.getValue() + "\n");
                        } catch (IOException e) {
                            logger.error("Error writing entry: " + entry.getKey(), e);
                        }
                    });
        } catch (Exception e) {
            logger.error("Error writing results", e);
        }
    }
}
