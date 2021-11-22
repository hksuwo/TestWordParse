package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.QuestionType;
import com.example.demo.utils.PageUtils;

import java.util.List;
import java.util.Map;

public interface QuestionTypeService extends IService<QuestionType> {


    /**
     * 查询所有分类,bi并组装成父子结构
     *
     * @return
     */
    List<QuestionType> listWithTree();
}
