package com.example.a12.module_datas;

import java.util.ArrayList;
import java.util.List;

public class ItemResultBean {
    public int id;
    public int itemId;
    public int homeworkId;
    public int homeworkResultId;
    public int questionId;
    public int questionParentId;
    public String status; //答题结果
    public String teacherSay;
    public String questionType;

    public List<ItemResultBean> items;
    public ArrayList<String> answer; //我的结果
}
