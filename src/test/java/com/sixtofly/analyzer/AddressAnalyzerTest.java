package com.sixtofly.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xie yuan bing
 * @date 2020-05-13 16:29
 * @description
 */
@Slf4j
public class AddressAnalyzerTest {



    @Test
    public void splits(){
        System.out.println(AddressAnalyzer.analyze("李奇15378680860四川雅安雨城区汉源县富林镇江汉大道2段"));
        System.out.println(AddressAnalyzer.analyze("15378680860四川雅安雨城区汉源县富林镇江汉大道2段李奇"));
        System.out.println(AddressAnalyzer.analyze("四川雅安雨城区汉源县富林镇江汉大道2段李奇15378680860"));
        System.out.println(AddressAnalyzer.analyze("李奇 15378680860 四川雅安雨城区汉源县富林镇江汉大道2段"));
        System.out.println(AddressAnalyzer.analyze("李奇，15378680860，四川雅安雨城区汉源县富林镇江汉大道2段"));
        System.out.println(AddressAnalyzer.analyze("李奇、15378680860、四川雅安雨城区汉源县富林镇江汉大道2段"));
        System.out.println(AddressAnalyzer.analyze("李奇15378680860，四川雅安雨城区汉源县富林镇江汉大道2段"));
        System.out.println(AddressAnalyzer.analyze("李奇，15378680860四川雅安雨城区汉源县富林镇江汉大道2段"));
        System.out.println(AddressAnalyzer.analyze("15378680860 四川雅安雨城区汉源县富林镇江汉大道2段，李奇"));
        System.out.println(AddressAnalyzer.analyze("李奇028-66999922四川雅安雨城区汉源县富林镇江汉大道2段"));
        System.out.println(AddressAnalyzer.analyze("66999922四川雅安雨城区汉源县富林镇江汉大道2段李奇"));
        System.out.println(AddressAnalyzer.analyze("四川雅安雨城区汉源县富林镇江汉大道2段李奇66999922"));

    }

    @Test
    public void test(){
        List<String> list = Stream.of("1", "2", "3").collect(Collectors.toList());
        System.out.println(list);
        System.out.println(list.subList(0, 1));
    }

}
