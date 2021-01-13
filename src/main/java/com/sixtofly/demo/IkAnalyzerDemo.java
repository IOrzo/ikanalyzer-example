package com.sixtofly.demo;

import com.sixtofly.config.ChineseDefaultConfig;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xie yuan bing
 * @date 2020-04-01 09:47
 * @description
 */
public class IkAnalyzerDemo {


    /**
     * 示例用法一
     *
     * @param content
     */
    public void one(String content) {
        Analyzer analyzer = new IKAnalyzer(true);
        TokenStream tokenStream = null;
        List<String> splits = new LinkedList<>();
        try {
            tokenStream = analyzer.tokenStream("content", content);
            tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
                splits.add(charTermAttribute.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (tokenStream != null) {
                    tokenStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(splits);
    }

    /**
     * 示例用法二
     * 该方法可以自定义配置项
     * @param content
     */
    public void two(String content){
        List<String> splits = new LinkedList<>();
        StringReader input = new StringReader(content);
        /**
         * 可以自定义配置类，修改里面的配置项
         * 如： 替换掉默认的主词库main2012.dic
         * @see DefaultConfig
         */
        Configuration configuration = DefaultConfig.getInstance();
        configuration.setUseSmart(true);
        IKSegmenter ikSegmenter = new IKSegmenter(input, configuration);
        try {
            Lexeme lexeme = null;
            while ((lexeme = ikSegmenter.next()) != null) {
                splits.add(lexeme.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(splits);
    }

    /**
     * 示例用法三
     * 自定义配置项
     * @param content
     */
    public void three(String content){
        List<String> splits = new LinkedList<>();
        StringReader input = new StringReader(content);
        /**
         * 可以自定义配置类，修改里面的配置项
         * 如： 替换掉默认的主词库main2012.dic
         * @see DefaultConfig
         */
        Configuration configuration = ChineseDefaultConfig.getInstance();
        configuration.setUseSmart(true);
        IKSegmenter ikSegmenter = new IKSegmenter(input, configuration);
        try {
            Lexeme lexeme = null;
            while ((lexeme = ikSegmenter.next()) != null) {
                splits.add(lexeme.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(splits);
    }

    public static void main(String[] args) {
        IkAnalyzerDemo demo = new IkAnalyzerDemo();
//        demo.one("成都市双流区公兴镇通瑞月光湖");
//        demo.two("成都市双流区公兴镇通瑞月光湖");

        // 注: 词库只会加载一次, 不能和上面方法一起测试。 因为上面方法先执行, 则先加载
        // 默认词库, 自定义词库便不会加载
        demo.three("成都市双流区公兴镇通瑞月光湖");
    }

}
