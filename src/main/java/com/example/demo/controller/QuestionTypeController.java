package com.example.demo.controller;

import com.example.demo.entity.QuestionType;
import com.example.demo.entity.R;
import com.example.demo.service.QuestionTypeService;
import com.example.demo.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/questionType")
public class QuestionTypeController {

    @Autowired
    private QuestionTypeService questionTypeService;


    @RequestMapping("/hello")
    public String hello() {
        return "Hello";
    }

    /**
     * 查询所有分类,bi并组装成父子结构
     *
     * @return
     */
    @RequestMapping("/list/tree")
    public R list() {

        List<QuestionType> questionTypes = questionTypeService.listWithTree();

        return R.ok().put("page", questionTypes);
    }

}
