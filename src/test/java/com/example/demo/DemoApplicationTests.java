package com.example.demo;

import com.example.demo.utils.WordParseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        String str = "一、常识判断（每题1.1分）";
        boolean match = WordParseUtil.matchQuestionType(str);
        if (match){
            Double score = WordParseUtil.getScore(str);
            Integer typeId = WordParseUtil.getQuestionTypeId(str);
            System.out.println(typeId);
            System.out.println(score);
        }
    }

}
