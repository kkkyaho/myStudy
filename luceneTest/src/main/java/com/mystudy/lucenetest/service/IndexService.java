package com.mystudy.lucenetest.service;

import com.mystudy.lucenetest.dto.WebContentDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

            Term term = new Term("title", dto.getTitle());
            Document document = new Document();

            LocalDateTime localDateTime = LocalDateTime.now();

            document.add(new LongPoint("date", ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli()));
            document.add(new StoredField("date", ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli()));
            document.add(new StringField("dateField", localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), Field.Store.YES));
            document.add(new StringField("title", dto.getTitle(), Field.Store.YES));
            document.add(new StringField("url", dto.getUrl(), Field.Store.YES));
            document.add(new StringField("html", dto.getHtmlCode(), Field.Store.YES));
            document.add(new StringField("content", dto.getContent(), Field.Store.YES));
            try {
                indexWriter.updateDocument(term, document);
                indexWriter.commit();
                indexWriter.flush();
            } catch (IOException e) {
                log.info("index error", e);
            }

        }
    }

    public String getIndexDir() {
        return this.dirPath;
    }
}
