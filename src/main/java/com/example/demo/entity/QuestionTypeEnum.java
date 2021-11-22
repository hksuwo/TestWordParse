package com.example.demo.entity;

/**
 * @author qiaoh
 */

@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum QuestionTypeEnum {
    COMMON_SENSE_JUDGEMENT(1, "常识判断"),
    UNDERSTANDING_AND_EXPRESSION(2, "言语理解与表达"),
    QUANTITY_RELATIONSHIP(3, "数量关系"),
    JUDGEMENT_AND_REASONING(4, "判断推理"),
    DATA_ANALYSIS(5, "资料分析"),
    TOPIC(6, "【题文】"),
    CHOICE(7, "【选项】"),
    ANSWER(8, "【答案】"),
    ANALYSIS(9, "【解析】"),
    FINISH(10, "【结束】"),
    //标识符正则
    IDENTIFIER_REGEX(11, "【题文】|【选项】|【答案】|【解析】|【结束】"),
    //大题正则,如一、常识判断(每题0.8分)
    QUESTION_TYPE_REGEX(12, "([一二三四五六七八九十]{1,3})([、.]{1})(常识判断|言语理解与表达|数量关系|判断推理|资料分析)([（(])(每题\\d+(\\.\\d+)?分)([)）])"),
    //分数正则,如0.8
    SCORE_REGEX(13, "\\d+(\\.\\d+)?"),
    //题目类型正则
    TYPE_REGEX(14, "常识判断|言语理解与表达|数量关系|判断推理|资料分析");


    private Integer code;
    private String type;

    QuestionTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
