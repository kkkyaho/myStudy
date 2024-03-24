package com.mystudy.lucenetest.test;

import com.mystudy.lucenetest.dto.Person;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class luceneTest {

    public static String dirPath = "d:\\index\\ruliweb";
    public static String url = "https://bbs.ruliweb.com/best/selection?orderby=best_id&range=24h";

    @Test
    public void test() throws Exception{
        File indexDirectory = new File(dirPath);
        Directory directory = FSDirectory.open(indexDirectory.toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig((new StandardAnalyzer())));

        List<Person> personList = Arrays.asList(
                new Person("id0", "이름0", 30),
                new Person("id1", "이름1", 31),
                new Person("id2", "이름2", 32),
                new Person("id3", "이름3", 33),
                new Person("id4", "이름4", 34)
        );

        for (Person person : personList) {
            Term term = new Term("ID", person.getId());
            Document document = new Document();
            document.add(new StringField("id"   , person.getId(), Field.Store.YES));
            document.add(new StringField("name" , person.getName(), Field.Store.NO));
            document.add(new LongPoint("age"  , person.getAge()));
            indexWriter.updateDocument(term, document);
        }
        indexWriter.commit();
    }

    @Test
    public void testee() throws Exception {
        File indexDirectory = new File(dirPath);
        Directory directory = FSDirectory.open(indexDirectory.toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);


        Query query1 = new WildcardQuery(new Term("url", "https*"));

        BooleanQuery booleanQuery = new BooleanQuery.Builder()
                .add(query1, BooleanClause.Occur.MUST)
                .build();

        TopDocs hits = indexSearcher.search(booleanQuery, 1000);
        System.out.println("count : " + hits.totalHits.value);

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("title"));
            System.out.println(doc.get("url"));
        }

    }

    @Test
    public void test2() throws Exception{
       File indexDirectory = new File(dirPath);
       Directory directory = FSDirectory.open(indexDirectory.toPath());
        System.out.println(indexDirectory.toPath());
       IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
       TermQuery nameQuery = new TermQuery(new Term("name", "이름1"));
       Query ageQuery = LongPoint.newSetQuery("age", 31);
       BooleanQuery.Builder booleanQUeryBuilder = new BooleanQuery.Builder();
       booleanQUeryBuilder.add(nameQuery, BooleanClause.Occur.MUST);
       booleanQUeryBuilder.add(ageQuery, BooleanClause.Occur.MUST);
       TopDocs topDocs = indexSearcher.search(booleanQUeryBuilder.build(), 10);
       System.out.println("count : " + topDocs.totalHits.value);
       long searchCount = topDocs.totalHits.value;

       for (int index = 0; index < searchCount; index++) {
           Document document = indexSearcher.doc(topDocs.scoreDocs[index].doc);
           System.out.println(" - title : " + document.get("title"));
       }

    }

    @Test
    public void test3() throws Exception{

        for (int i=1; i<11; i++) {
            String params = "?page=" + i + "&mod=&idx=";

//            org.jsoup.nodes.Document doc = Jsoup.connect(url+params).get();
            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select(".article_wrapper .title_wrapper");
            for (Element element : elements) {
                String contentUrl = element.attr("href");
                String title = element.html().replaceAll("\\<span.*\\)</span>","");
                org.jsoup.nodes.Document subDoc = Jsoup.connect(contentUrl).get();

                Elements subElements = subDoc.select(".view_content p");
                for (Element subElemenet : subElements) {
                    System.out.println(subElemenet);
                    System.out.println(subElemenet.text());
                }
           }
        }
    }

    @Test
    public void test4() throws Exception{
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
//        System.out.println(doc);
        Elements elements = doc.select(".article_wrapper .title_wrapper");

//        System.out.println(elements);

        System.out.println(elements.not("href"));
    }

}
