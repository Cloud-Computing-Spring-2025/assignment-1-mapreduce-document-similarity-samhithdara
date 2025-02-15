package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DocumentSimilarityReducer extends Reducer<Text, Text, Text, Text> {
    private Text resultValue = new Text();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Collect all the words for the document identified by the key
        String doc1 = key.toString();
        Set<String> wordSet1 = new HashSet<>();
        
        for (Text val : values) {
            // Assuming words are comma-separated in the value
            String[] words = val.toString().split(",");
            for (String word : words) {
                wordSet1.add(word);
            }
        }

        // Now compare this document (doc1) with all other documents
        // We do this by storing document pairs and calculating Jaccard similarity
        // We'll need to handle this comparison logic manually in the reducer

        // You can't access other keys in the same reducer, so you'll need to compare doc1 against others manually
        // Store word sets for other docs, this might need redesigning logic if you want to compare across reducers

        // Sample logic for comparing with another hypothetical document "doc2":
        Set<String> wordSet2 = new HashSet<>();
        // Assume we get the wordSet2 from somewhere (from another document or by processing other data)

        // Calculate Jaccard similarity between doc1 and doc2
        Set<String> intersection = new HashSet<>(wordSet1);
        intersection.retainAll(wordSet2);  // Intersection of doc1 and doc2
        Set<String> union = new HashSet<>(wordSet1);
        union.addAll(wordSet2);  // Union of doc1 and doc2

        double jaccardSimilarity = (double) intersection.size() / (double) union.size();
        resultValue.set(String.format("%.2f%%", jaccardSimilarity * 100));

        // Write the similarity of the document pair to context
        context.write(new Text(doc1 + "," + "doc2"), resultValue);  // "doc2" is an example, replace with actual pair
    }
}
