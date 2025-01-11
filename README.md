# SocialMedia_Analyze

A Java-based tool for scraping and analyzing social media content (currently supporting Weibo) with keyword analysis capabilities.

## Features

- Weibo content scraping using user ID and authentication
- Text analysis including:
  - Keyword extraction and frequency analysis
  - Named entity recognition for person names
  - Emoji and expression analysis
  - Content categorization

## Prerequisites

- Java 8 or higher
- Maven
- Valid Weibo account credentials

## Dependencies

- HanLP (v1.8.13) - For Chinese text processing and analysis
- Apache HttpClient (v4.5.13) - For making HTTP requests
- JSoup (v1.14.3) - For HTML parsing

## Setup

1. Clone the repository
2. Configure Maven dependencies (using the provided `pom.xml`)
3. Update the following credentials in `WeiboScraper.java`:
   ```java
   private static final int USER_ID = 123456; // Your Weibo user ID
   private static final String COOKIE = "YOUR_COOKIE"; // Your Weibo cookie
   ```

## Usage

### Scraping Weibo Content
bash
java -cp target/KeywordAnalyzer-1.0-SNAPSHOT.jar WeiboScraper

### Analyzing Keywords
java -cp target/KeywordAnalyzer-1.0-SNAPSHOT.jar KeywordAnalysis

The analysis will generate four output files:
- `category.txt` - Content categorization results
- `express.txt` - Emoji and expression analysis
- `word.txt` - Keyword frequency analysis
- `name.txt` - Extracted person names

## Output Format

Each output file contains entries in the following format:

term frequency

Where:
- `term` is the extracted word, expression, or category
- `frequency` is the number of occurrences

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

