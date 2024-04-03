package com.mystudy.lucenetest.service;

import com.mystudy.lucenetest.utils.QueryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.select.QueryParser;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class SearchService {

    private final IndexService indexService;

    public void searchIndex() {

        try {
            File indexDirectory = new File(indexService.getIndexDir());
            Directory directory = FSDirectory.open(indexDirectory.toPath());
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            Query query1 = new WildcardQuery(new Term("url", "https*"));

            Query rangeQuery = LongPoint.newRangeQuery("date", QueryUtil.makeTimeStamp(LocalDateTime.now().minusDays(1)), QueryUtil.makeTimeStamp(LocalDateTime.now()));


            BooleanQuery booleanQuery = new BooleanQuery.Builder()
                    .add(rangeQuery, BooleanClause.Occur.MUST)
                    .build();

            TopDocs hits = indexSearcher.search(booleanQuery, 1000);

            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = indexSearcher.doc(scoreDoc.doc);

                List<IndexableField> list = doc.getFields();
                for (IndexableField field : list) {
                    System.out.println(field.name()  + "   ::   " + field.stringValue());
                }

            }
        } catch (Exception e) {

        }


    }
}
