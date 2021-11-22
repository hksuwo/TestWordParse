package com.example.demo.controller;


import com.example.demo.entity.ExamCategory;
import com.example.demo.entity.R;
import com.example.demo.service.ExamCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/examCategory")
public class ExamCategoryController {

    @Autowired
    private ExamCategoryService examCategoryService;


    /**
     * 查询所有试卷类型,并组装成树形结构
     *
     * @return
     */
    @RequestMapping("/list/tree")
    public R listWithTree() {
        List<ExamCategory> list = examCategoryService.listWithTree();
        return R.ok().put("list", list);
    }
}
