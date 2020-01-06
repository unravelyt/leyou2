package com.leyou.common.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ResultPage<T> {

    private Long total; //总条数
    private Integer totalPage; //总页数
    private List<T> items; // 数据集合

    public ResultPage() {
    }

    public ResultPage(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
