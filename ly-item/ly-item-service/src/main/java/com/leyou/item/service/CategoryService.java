package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 根据父id查询子分类
     * @param pid
     * @return
     */
    List<Category> queryCategoryListByPid(Long pid);

    /**
     * 根据分类id集合查询分类名称
     * @param ids
     * @return
     */
    List<String> queryNamesByIds(List<Long> ids);
}
