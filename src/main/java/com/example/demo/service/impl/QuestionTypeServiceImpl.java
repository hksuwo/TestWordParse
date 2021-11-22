package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.QuestionType;
import com.example.demo.mapper.QuestionTypeMapper;
import com.example.demo.service.QuestionTypeService;
import com.example.demo.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionTypeServiceImpl extends ServiceImpl<QuestionTypeMapper,QuestionType> implements QuestionTypeService {

//    @Autowired
//    private QuestionTypeMapper questionTypeMapper;



    @Override
    public List<QuestionType> listWithTree() {
        //1.查出所有分类
        List<QuestionType> questionTypes = baseMapper.selectList(null);

        return questionTypes;
    }
}
