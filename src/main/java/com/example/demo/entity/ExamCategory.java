package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
public class ExamCategory {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 试卷分类名
     */
    private String categoryName;

    /**
     * 父节点id
     */
    private Integer parentId;

    /**
     * 逻辑删除位【0删除，1存在】
     */
    private Integer showStatus;

    /**
     * 结点等级
     */
    private Integer categoryLevel;

    /**
     * 结点位置
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 试卷分类的子分类列表
     */
    @TableField(exist = false)
    private List<ExamCategory> childCategory;

    public Integer getSort() {
        return sort == null ? 0 : sort;
    }


}
