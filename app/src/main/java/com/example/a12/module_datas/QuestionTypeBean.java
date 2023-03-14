package com.example.a12.module_datas;

import java.io.Serializable;

public enum QuestionTypeBean implements Serializable {
    /**
     *  choice 多选
     *  determine 判断
     *  essay 问答
     *  fill 填空
     *  material 材料
     *  single_choice 单选
     *  uncertain_choice 不定项
     */
    choice("多选题"),
    determine("判断题"),
    essay("问答题"),
    fill("填空题"),
    material("材料题"),
    single_choice("单选题"),
    uncertain_choice("不定项题"),
    empty("");

    public String name;

    QuestionTypeBean(String name)
    {
        this.name = name;
    }

    public String title()
    {
        return this.name;
    }

    public static QuestionTypeBean value(String typeName)
    {
        QuestionTypeBean type;
        try {
            type =  valueOf(typeName);
        }catch (Exception e) {
            type = empty;
        }
        return type;
    }
}

