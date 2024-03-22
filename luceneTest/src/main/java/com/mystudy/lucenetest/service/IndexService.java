package com.mystudy.lucenetest.service;

import com.mystudy.lucenetest.dto.WebContentDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class IndexService {

    private String dirPath = "d:\\index\\ruliweb";
    private IndexWriter indexWriter;

    @PostConstruct
    public void initIndexWriter() {

        try {
            File indexDirectory = new File(dirPath);
            Directory directory = FSDirectory.open(indexDirectory.toPath());
            indexWriter = new IndexWriter(directory, new IndexWriterConfig((new StandardAnalyzer())));
        } catch (IOException e) {
            log.info("indexWriter error  :: ", e);
        }
    }

    public void doIndex(List<WebContentDto> list) {

        for (WebContentDto dto : list) {

            log.info(dto.toString());

            Term term = new Term("title", dto.getTitle());
            Document document = new Document();
            document.add(new StringField("title"   , dto.getTitle(), Field.Store.YES));
            document.add(new StringField("url"  , dto.getUrl(), Field.Store.YES));
            try {
                indexWriter.updateDocument(term, document);
                indexWriter.commit();
                indexWriter.flush();
            } catch (IOException e) {
                log.info("index error", e);
            }

        }
    }
}
