package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.ExamCategory;

import java.util.List;

public interface ExamCategoryService extends IService<ExamCategory> {
    List<ExamCategory> listWithTree();
}
