package com.ir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class App {
    public static void main(String[] args) {
        String inputPath = args[0];
        String indexPath = args[1];

        if (inputPath == null || indexPath == null) {
            System.err.println("Usage: indexer.jar InputDirectory IndexTargetDirectory");
            System.exit(1);
        }

        final Path path = Paths.get(inputPath);
        if (!Files.isReadable(path)) {
            System.out.println("The Directory '" + path.toAbsolutePath() + "' does not exist or is not readable.");
            System.exit(1);
        }

        try {
            System.out.println("Indexing to directory '" + indexPath + "'...");
            Directory directory = FSDirectory.open(Paths.get(indexPath));
            Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();
            analyzerPerField.put("Hashtags", new KeywordAnalyzer());
            analyzerPerField.put("Username", new KeywordAnalyzer());

            PerFieldAnalyzerWrapper analyzer =
                    new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            IndexWriter writer = new IndexWriter(directory, iwc);
            long startTime = System.currentTimeMillis();
            indexTweets(writer, path);
            writer.close();
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Time Taken is: " + totalTime);
        } catch (IOException e) {
            System.out.println("Exception: " + e.getClass() +
                    "\n Message: " + e.getMessage());
        }
    }


    static void indexTweets(final IndexWriter writer, final Path path) throws IOException {
        File dir = path.toFile();
        List<String> storedFields = new ArrayList<String>();
        storedFields.add("Username");
        storedFields.add("Name");
        storedFields.add("User Location");
        storedFields.add("Timestamp");
        storedFields.add("Geo Location Latitude");
        storedFields.add("Geo Location Longitude");
        if (Files.isDirectory(path)) {
            for (File file : dir.listFiles()) {
                System.out.println(file.getName());
                try {
                    Scanner sc = new Scanner(file);
                    sc.useDelimiter("\r");
                    int count = 1;
                    while (sc.hasNext()) {
                        Document doc = new Document();
                        String tweet = sc.next();
                        String[] tweetLines = tweet.split("\n");
                        StringBuilder sb = new StringBuilder();
                        for (String line : tweetLines) {
                            if(line.contains("|") || line.contains("\\|")){
                                String[] lineSplit = line.split("\\|");
                                String field = lineSplit[0].trim();
                                String value = "";
                                if(lineSplit.length > 1)
                                    value = lineSplit[1].trim();
                                if(storedFields.contains(field)){
                                    doc.add(new StoredField(field, value));
                                }
                                if(field.equals("Username")){
                                    doc.add(new StringField("Username", value, Store.YES));
                                }
                                if(field.equals("Status")){
                                    sb.append(value).append("\n");
                                }
                                if(field.equals("Hashtags")){
                                    doc.add(new StringField("Hashtags", value, Store.YES));
                                    doc.add(new TextField("Status", sb.toString(), Store.YES));
                                }
                                if(field.equals("#Followers")){
                                    doc.add(new StoredField("Followers", value));
                                }
                            }
                            else{
                                sb.append(line).append("\n");
                            }
                        }
                        writer.addDocument(doc);
                    }
                } catch (IOException ignore) {
                    System.out.println("There was error reading file: " + path);
                }
            }
        }
    }
}
