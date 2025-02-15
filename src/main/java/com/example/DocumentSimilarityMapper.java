package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DocumentSimilarityMapper extends Mapper<Object, Text, Text, Text> {
    private Text docPairKey = new Text();
    private Text wordSetValue = new Text();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split("\t");
        String docId = parts[0];
        String document = parts[1];
        String[] words = document.split("\\s+");

        // Convert the words into a Set to eliminate duplicates
        Set<String> wordSet = new HashSet<>();
        for (String word : words) {
            wordSet.add(word.toLowerCase());  // Convert to lowercase for case insensitivity
        }

        // Emit the document word set for comparison between pairs
        wordSetValue.set(String.join(",", wordSet));
        context.write(new Text(docId), wordSetValue);
    }
}
