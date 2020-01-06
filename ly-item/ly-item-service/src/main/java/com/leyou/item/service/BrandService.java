package com.leyou.item.service;

import com.leyou.common.pojo.ResultPage;
import com.leyou.item.pojo.Brand;

import java.util.List;

public interface BrandService {

    /**
     * 根据搜索条件查询品牌分页
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    ResultPage<Brand> queryBrandsByCondition(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    void saveBrand(Brand brand, List<Long> cids);

    /**
     * 根据分类id查询该分类下的所有品牌
     * @param cid
     * @return
     */
    List<Brand> queryBrandsByCid(Long cid);
}
