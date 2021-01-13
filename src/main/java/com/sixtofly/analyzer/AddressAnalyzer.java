package com.sixtofly.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址解析版本1
 * @author xie yuan bing
 * @date 2020-03-27 10:54
 * @description
 */
@Slf4j
public class AddressAnalyzer {

    private static final Analyzer analyzer = new IKAnalyzer(true);

    /**
     * 截取地址长度
     */
    private static final int SPLIT_ADDRESS_LEN = 20;


    public static List<String> analyze(String address){
        TokenStream tokenStream = null;
        List<String> splits = new ArrayList<>(SPLIT_ADDRESS_LEN);
        try {
            tokenStream = analyzer.tokenStream("content", address);
            tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            for (int i = 0; i < SPLIT_ADDRESS_LEN; i++) {
                if (tokenStream.incrementToken()) {
                    CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
                    splits.add(charTermAttribute.toString());
                }
            }
        } catch (IOException e) {
            log.error("地址解析失败", e);
        } finally {
            try {
                if (tokenStream != null) {
                    tokenStream.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return splits;
    }
}
