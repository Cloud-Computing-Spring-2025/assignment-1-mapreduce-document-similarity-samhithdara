package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.*;

public class DocumentSimilarityReducer extends Reducer<Text, Text, Text, Text> {
    private HashMap<String, HashSet<String>> documentWordMap = new HashMap<>();

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Key = DocumentID, Values = Words in Document
        String docId = key.toString();
        HashSet<String> wordSet = new HashSet<>();

        for (Text val : values) {
            wordSet.add(val.toString());
        }

        documentWordMap.put(docId, wordSet);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        List<String> docList = new ArrayList<>(documentWordMap.keySet());

        // Compare each document pair
        for (int i = 0; i < docList.size(); i++) {
            for (int j = i + 1; j < docList.size(); j++) {
                String doc1 = docList.get(i);
                String doc2 = docList.get(j);

                HashSet<String> wordsDoc1 = documentWordMap.get(doc1);
                HashSet<String> wordsDoc2 = documentWordMap.get(doc2);

                // Compute Jaccard Similarity
                HashSet<String> intersection = new HashSet<>(wordsDoc1);
                intersection.retainAll(wordsDoc2);

                HashSet<String> union = new HashSet<>(wordsDoc1);
                union.addAll(wordsDoc2);

                double jaccardSimilarity = (double) intersection.size() / union.size();

                if (jaccardSimilarity > 0.0) { // Avoid zero results
                    context.write(new Text("(" + doc1 + ", " + doc2 + ")"), new Text(String.format("%.2f", jaccardSimilarity)));
                }
            }
        }
    }
}
