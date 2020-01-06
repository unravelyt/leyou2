package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.ResultPage;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public ResultPage<Brand> queryBrandsByCondition(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //分页
        PageHelper.startPage(page,rows);
        //初始化example对象
        Example example = new Example(Brand.class);
        //根据key模糊查询
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);
        //包装成PageInfo,并返回
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return new ResultPage<Brand>(pageInfo.getTotal(), null, pageInfo.getList());
    }

    @Transactional
    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        //新增brand表
        brandMapper.insertSelective(brand);

        //新增brand_category表
        cids.forEach(cid -> brandMapper.insertBrandCategory(cid,brand.getId()));

    }

    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return brandMapper.selectBrandsByCategoryId(cid);
    }



}
