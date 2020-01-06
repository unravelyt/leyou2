package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_spec_param")
public class SpecParam {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    @Column(name = "`numeric`") //numeric是mysql的关键词，用`numeric`替换占
    private Boolean numeric; //是否为数字类型的参数
    private String unit;     //单位
    private Boolean generic; //是否为sku通用属性
    private Boolean searching; //是否可用于搜索
    private String segments; //数值类型参数，分段


}
