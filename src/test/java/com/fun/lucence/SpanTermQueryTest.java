/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.fun.lucence;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;


/**
 * 
 * @author lujun.xlj
 * @version $Id: SpanTermQueryTest.java, v 0.1 Jun 27, 2016 5:11:39 PM lujun.xlj Exp $
 */
public class SpanTermQueryTest {

    public static void main(String[] args) throws IOException {
        Directory dir = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(dir, iwc);

        Document doc = new Document();
        doc.add(new TextField("text", "the quick brown fox jumps over the lazy dog", Field.Store.YES));
        writer.addDocument(doc);

        doc = new Document();
        doc.add(new TextField("text", "the quick red fox jumps over the sleepy cat", Field.Store.YES));
        writer.addDocument(doc);

        doc = new Document();
        doc.add(new TextField("text", "the quick brown fox jumps over the lazy dog", Field.Store.YES));
        writer.addDocument(doc);
        writer.close();

        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        String queryString = "red";
        SpanQuery query = new SpanTermQuery(new Term("text", queryString));

        TopDocs results = searcher.search(query, 100);
        ScoreDoc[] scoreDocs = results.scoreDocs;

        for (int i = 0; i < scoreDocs.length; ++i) {
            // System.out.println(searcher.explain(query, scoreDocs[i].doc));
            int docID = scoreDocs[i].doc;
            Document document = searcher.doc(docID);
            String path = document.get("text");
            System.out.println("text:" + path);
        }
    }
}
