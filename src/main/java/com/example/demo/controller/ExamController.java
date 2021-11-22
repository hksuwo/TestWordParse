package com.example.demo.controller;

import com.example.demo.entity.Exam;
import com.example.demo.entity.Question;
import com.example.demo.entity.R;
import com.example.demo.service.ExamService;
import com.example.demo.utils.WordParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @RequestMapping("/hello")
    public R hello() {
        List<Question> questionList = null;
        try {
            questionList = WordParseUtil.getQuestionList("D:\\others\\test03.docx");
        } catch (Exception e) {
            return R.error("试卷格式错误!");
        }
        return R.ok("试卷列表信息:").put("questionList", questionList);
    }





}
