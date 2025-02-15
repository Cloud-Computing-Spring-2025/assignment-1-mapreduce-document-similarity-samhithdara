[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18028188&assignment_repo_type=AssignmentRepo)
### **📌 Document Similarity Using Hadoop MapReduce**  

#### **Objective**  
The goal of this assignment is to compute the **Jaccard Similarity** between pairs of documents using **MapReduce in Hadoop**. You will implement a MapReduce job that:  
1. Extracts words from multiple text documents.  
2. Identifies which words appear in multiple documents.  
3. Computes the **Jaccard Similarity** between document pairs.  
4. Outputs document pairs with similarity **above 50%**.  

---

### **📥 Example Input**  

You will be given multiple text documents. Each document will contain several words. Your task is to compute the **Jaccard Similarity** between all pairs of documents based on the set of words they contain.  

#### **Example Documents**  

##### **doc1.txt**  
```
hadoop is a distributed system
```

##### **doc2.txt**  
```
hadoop is used for big data processing
```

##### **doc3.txt**  
```
big data is important for analysis
```

---

# 📏 Jaccard Similarity Calculator

## Overview

The Jaccard Similarity is a statistic used to gauge the similarity and diversity of sample sets. It is defined as the size of the intersection divided by the size of the union of two sets.

## Formula

The Jaccard Similarity between two sets A and B is calculated as:

```
Jaccard Similarity = |A ∩ B| / |A ∪ B|
```

Where:
- `|A ∩ B|` is the number of words common to both documents
- `|A ∪ B|` is the total number of unique words in both documents

## Example Calculation

Consider two documents:
 
**doc1.txt words**: `{hadoop, is, a, distributed, system}`
**doc2.txt words**: `{hadoop, is, used, for, big, data, processing}`

- Common words: `{hadoop, is}`
- Total unique words: `{hadoop, is, a, distributed, system, used, for, big, data, processing}`

Jaccard Similarity calculation:
```
|A ∩ B| = 2 (common words)
|A ∪ B| = 10 (total unique words)

Jaccard Similarity = 2/10 = 0.2 or 20%
```

## Use Cases

Jaccard Similarity is commonly used in:
- Document similarity detection
- Plagiarism checking
- Recommendation systems
- Clustering algorithms

## Implementation Notes

When computing similarity for multiple documents:
- Compare each document pair
- Output pairs with similarity > 50%

### **📤 Expected Output**  

The output should show the Jaccard Similarity between document pairs in the following format:  
```
(doc1, doc2) -> 60%  
(doc2, doc3) -> 50%  
```

---

### **🛠 Environment Setup: Running Hadoop in Docker**  

Since we are using **Docker Compose** to run a Hadoop cluster, follow these steps to set up your environment.  

#### **Step 1: Install Docker & Docker Compose**  
- **Windows**: Install **Docker Desktop** and enable WSL 2 backend.  
- **macOS/Linux**: Install Docker using the official guide: [Docker Installation](https://docs.docker.com/get-docker/)  

#### **Step 2: Start the Hadoop Cluster**  
Navigate to the project directory where `docker-compose.yml` is located and run:  
```sh
docker-compose up -d
```  
This will start the Hadoop NameNode, DataNode, and ResourceManager services.  

#### **Step 3: Access the Hadoop Container**  
Once the cluster is running, enter the **Hadoop master node** container:  
```sh
docker exec -it hadoop-master /bin/bash
```

---

### **📦 Building and Running the MapReduce Job with Maven**  

#### **Step 1: Build the JAR File**  
Ensure Maven is installed, then navigate to your project folder and run:  
```sh
mvn clean package
```  
This will generate a JAR file inside the `target` directory.  

#### **Step 2: Copy the JAR File to the Hadoop Container**  
Move the compiled JAR into the running Hadoop container:  
```sh
docker cp target/similarity.jar hadoop-master:/opt/hadoop-3.2.1/share/hadoop/mapreduce/similarity.jar
```

---

### **📂 Uploading Data to HDFS**  

#### **Step 1: Create an Input Directory in HDFS**  
Inside the Hadoop container, create the directory where input files will be stored:  
```sh
hdfs dfs -mkdir -p /input
```

#### **Step 2: Upload Dataset to HDFS**  
Copy your local dataset into the Hadoop cluster’s HDFS:  
```sh
hdfs dfs -put /path/to/local/input/* /input/
```

---

### **🚀 Running the MapReduce Job**  

Run the Hadoop job using the JAR file inside the container:  
```sh
hadoop jar similarity.jar DocumentSimilarityDriver /input /output_similarity /output_final
```

---

### **📊 Retrieving the Output**  

To view the results stored in HDFS:  
```sh
hdfs dfs -cat /output_final/part-r-00000
```

If you want to download the output to your local machine:  
```sh
hdfs dfs -get /output_final /path/to/local/output
```
---
Here's how you can structure the sections for your project:

### 1. **Project Overview**

The goal of this project is to compute the document similarity using a MapReduce approach, based on the **Jaccard Similarity** metric. The project takes multiple documents as input, processes them to extract word sets, and calculates pairwise similarity between the documents. The similarity score is output in percentage format, where higher values indicate more similarity in terms of shared words between the document pairs.

---

### 2. **Approach and Implementation**

#### **Mapper Logic:**

- **Input**: The Mapper takes each document as input (from the input text file), processes the text, and creates key-value pairs where the key is a unique identifier for the document (e.g., `doc1`, `doc2`, etc.), and the value is a list of words from the document.
  
- **Process**: The Mapper tokenizes the document into individual words, converts them to lowercase for uniformity, and outputs each word along with its corresponding document identifier. This helps later group the words from the same document for comparison.

- **Output**: The Mapper emits key-value pairs like `(document_id, word)` for each word in a document. These key-value pairs are then sent to the Reducer.

#### **Reducer Logic:**

- **Input**: The Reducer receives the key-value pairs output from the Mapper, where the key is the document identifier, and the value is a list of words (from multiple documents).

- **Process**: The Reducer takes the word lists from the Mapper, compares them document-by-document, and computes the Jaccard similarity for each pair. The Jaccard similarity is computed using the formula:
  
  \[
  \text{Jaccard Similarity} = \frac{\text{Intersection of Words}}{\text{Union of Words}}
  \]

- **Output**: The Reducer then outputs the similarity score for each document pair in the format `doc1, doc2 -> 60%`.

---

### 3. **Execution Steps**

#### **Step 1**: **Build the project**
- Use Maven to compile the project.

```bash
mvn clean install
```

#### **Step 2**: **Run the Hadoop job**

- To submit the job to Hadoop, run the following command:

```bash
hadoop jar DocumentSimilarity.jar com.example.DocumentSimilarityDriver /input /output_similarity
```

Here, `/input` is the HDFS directory containing the input files, and `/output_similary` is the directory where the result will be stored.

#### **Step 3**: **Check the output**

- After the job completes, you can check the output using:

```bash
hdfs dfs -cat /output_similarity/part-r-00000
```

---

### 4. **Challenges Faced & Solutions**
Faced challenge in writting code but later able to write.
---

### 5. **Sample Input and Output**

#### **Sample Input**:

```text
doc1	Hadoop is an open-source framework for processing big data.
doc2	Big data processing is efficiently handled by Hadoop.
doc3	Machine learning algorithms can be applied on Hadoop datasets.
```

#### **Expected Output**:

```
doc1,doc2    28.57%
doc1,doc3    5.88%
doc2,doc3    6.67%
```

### Explanation of the Output:
- The Jaccard similarity between `doc1` and `doc2` is 28.57%, meaning they share around 28.57% of their words in common.
- `doc1` and `doc3` share only "Hadoop", so the similarity is low at 5.88%.
- `doc2` and `doc3` also only share "Hadoop", yielding a similarity of 6.67%.

This output is based on the assumption that the words are treated case-insensitively and punctuation is ignored.