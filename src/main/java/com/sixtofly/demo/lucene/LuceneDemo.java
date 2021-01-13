package com.sixtofly.demo.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Lucene使用示例
 * @author xie yuan bing
 * @date 2020-04-02 10:51
 * @description
 */
public class LuceneDemo {

    public static void main(String[] args) {
        LuceneDemo demo = new LuceneDemo();
//        demo.createIndex();
        demo.query();

    }

    private void createIndex() {
        // 创建3个News对象
        News news1 = new News();
        news1.setId(1);
        news1.setTitle("安倍晋三本周会晤特朗普 将强调日本对美国益处");
        news1.setContent("日本首相安倍晋三计划2月10日在华盛顿与美国总统特朗普举行会晤时提出加大日本在美国投资的设想");
        news1.setReply(672);

        News news2 = new News();
        news2.setId(2);
        news2.setTitle("北大迎4380名新生 农村学生700多人近年最多");
        news2.setContent("昨天，北京大学迎来4380名来自全国各地及数十个国家的本科新生。其中，农村学生共700余名，为近年最多...");
        news2.setReply(995);

        News news3 = new News();
        news3.setId(3);
        news3.setTitle("特朗普宣誓(Donald Trump)就任美国第45任总统");
        news3.setContent("当地时间1月20日，唐纳德·特朗普在美国国会宣誓就职，正式成为美国第45任总统。");
        news3.setReply(1872);

        // 开始时间
        Date start = new Date();
        System.out.println("**********开始创建索引**********");

        Analyzer analyzer = new IKAnalyzer(true);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        Directory dir = null;
        IndexWriter indexWriter = null;
//        Path path = Paths.get();
        File file = new File("F:\\temp\\lucene");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            dir = FSDirectory.open(file);
            indexWriter = new IndexWriter(dir, config);

            // 设置新闻ID索引并存储
            FieldType idType = new FieldType();
            idType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            idType.setStored(true);

            // 设置新闻标题索引文档、词项频率、位移信息和偏移量，存储并词条化
            FieldType titleType = new FieldType();
            titleType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            titleType.setStored(true);
            titleType.setTokenized(true);

            FieldType contentType = new FieldType();
            contentType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            contentType.setStored(true);
            contentType.setTokenized(true);
//            contentType.setStoreTermVectors(false);
//            contentType.setStoreTermVectorPositions(true);
//            contentType.setStoreTermVectorOffsets(true);

            FieldType intType = new FieldType();
            intType.setIndexOptions(FieldInfo.IndexOptions.DOCS_ONLY);
            intType.setStored(true);
            contentType.setStoreTermVectors(true);

            Document doc1 = new Document();
            doc1.add(new IntField("id", news1.getId(), Field.Store.YES));
            doc1.add(new TextField("title", news1.getTitle(), Field.Store.YES));
            doc1.add(new TextField("content", news1.getContent(), Field.Store.YES));
            doc1.add(new IntField("reply", news1.getReply(), Field.Store.YES));
            doc1.add(new StoredField("reply_display", news1.getReply()));

            Document doc2 = new Document();
            doc2.add(new IntField("id", news2.getId(), Field.Store.YES));
            doc2.add(new TextField("title", news2.getTitle(), Field.Store.YES));
            doc2.add(new TextField("content", news2.getContent(), Field.Store.YES));
            doc2.add(new IntField("reply", news2.getReply(), Field.Store.YES));
            doc2.add(new StoredField("reply_display", news2.getReply()));

//            Document doc3 = new Document();
//            doc3.add(new Field("id", String.valueOf(news3.getId()), idType));
//            doc3.add(new Field("title", news3.getTitle(), titleType));
//            doc3.add(new Field("content", news3.getContent(), contentType));
//            doc3.add(new IntField("reply", news3.getReply(), intType));
//            doc3.add(new StoredField("reply_display", news3.getReply()));

            indexWriter.addDocument(doc1);
            indexWriter.addDocument(doc2);
//            indexWriter.addDocument(doc3);

            indexWriter.commit();
            indexWriter.close();
            dir.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Date end = new Date();
        System.out.println("索引文档用时:" + (end.getTime() - start.getTime()) + " milliseconds");
        System.out.println("**********索引创建完成**********");
    }


    public void query() {
        File file = new File("F:\\temp\\lucene");
        Analyzer analyzer = new IKAnalyzer(true);
        try {
            Directory directory = FSDirectory.open(file);
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            String key = "农村";
            TermQuery query = new TermQuery(new Term("content", key));
//            QueryParser queryParser = new QueryParser(Version.LUCENE_47, key, analyzer);
//            Query query = queryParser.parse(key);
            TopDocs hits = searcher.search(query, 10);
            System.out.println("命中：" + hits.totalHits);
            System.out.println("最高得分：" + hits.getMaxScore());
            // 输出结果
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println(doc);
                System.out.println("得分：" + scoreDoc.score + "\t标题："
                        + doc.get("title") + "\t内容：" + doc.get("content"));
            }
            reader.close();
            directory.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
