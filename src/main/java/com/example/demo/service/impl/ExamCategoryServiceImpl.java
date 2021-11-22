package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Exam;
import com.example.demo.entity.ExamCategory;
import com.example.demo.mapper.ExamCategoryMapper;
import com.example.demo.service.ExamCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamCategoryServiceImpl extends ServiceImpl<ExamCategoryMapper, ExamCategory> implements ExamCategoryService {
    @Override
    public List<ExamCategory> listWithTree() {
        //1.查询所有分类
        List<ExamCategory> categories = baseMapper.selectList(null);

        //2.1 找到所有的一级分类
        List<ExamCategory> level1Categories = categories.stream().filter(category ->
                category.getParentId() == 0
        ).map((childCategory) -> {
            childCategory.setChildCategory(getChildCategories(childCategory, categories));
            return childCategory;
        }).sorted((childCategory1, childCategory2) -> {
            return childCategory1.getSort() - childCategory2.getSort();
        }).collect(Collectors.toList());


        return level1Categories;
    }

    //递归查找所有菜单的子菜单
    private List<ExamCategory> getChildCategories(ExamCategory root, List<ExamCategory> all) {
        List<ExamCategory> childCategories = all.stream().filter(category -> {
            return category.getParentId() == root.getId();
        }).map(examCategory -> {
            //1.找到子分类
            examCategory.setChildCategory(getChildCategories(examCategory, all));
            return examCategory;
        }).sorted((childCategory1, childCategory2) -> {
            //2.分类排序
            return childCategory1.getSort() - childCategory2.getSort();
        }).collect(Collectors.toList());

        return childCategories;
    }
}
