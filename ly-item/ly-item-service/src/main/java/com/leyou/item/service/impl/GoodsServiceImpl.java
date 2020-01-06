package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.ResultPage;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Override
    public ResultPage<SpuBo> querySpuPageByCondition(String key, Boolean saleable, Integer page, Integer rows) {

        //初始化example
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //根据key模糊查询，查询title或者品牌
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title", "%"+key+"%");
        }
        //上下架
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        //开启分页
        PageHelper.startPage(page,rows);
        //查询spu
        List<Spu> spuList = spuMapper.selectByExample(example);

        //将spu转化为spuBo
        List<SpuBo> spuBos = spuList.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            List<String> strings = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setCname(StringUtils.join(strings, "/"));
            spuBo.setBname(brand.getName());
            return spuBo;
        }).collect(Collectors.toList());

        //返回ResultPage
        PageInfo<Spu> pageInfo = new PageInfo<>(spuList);
        return new ResultPage<>(pageInfo.getTotal(), pageInfo.getPages(),spuBos);
    }

    @Transactional
    @Override
    public void saveGoods(SpuBo spuBo) {
        //添加Spu
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime()); //商品的数据回显了
        spuMapper.insertSelective(spuBo);
        //添加SpuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        spuDetailMapper.insertSelective(spuDetail);
        //添加Sku和stock
        spuBo.getSkus().forEach(sku -> {
            //添加Sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insertSelective(sku);
            //添加stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insertSelective(stock);
        });
    }

    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    @Override
    public List<Sku> querySkusBySpuId(Long id) {
        //查询spu
        Sku skuParam = new Sku();
        skuParam.setSpuId(id);
        List<Sku> skuList = skuMapper.select(skuParam);
        //查询库存
        skuList.forEach(sku -> {
            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skuList;
    }

    @Override
    public void updateGoods(SpuBo spuBo) {
        //1 先删除stock 然后删除sku
        Sku skuParam = new Sku();
        skuParam.setSpuId(spuBo.getId());
        List<Sku> skuList = skuMapper.select(skuParam);
        //获取sku的id,删除stock
        List<Long> skuId = skuList.stream().map(sku -> sku.getId()).collect(Collectors.toList());
        stockMapper.deleteByIdList(skuId);
        skuMapper.delete(skuParam);
        //2 新增sku和stock
        spuBo.getSkus().forEach(sku -> {
            //添加Sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insertSelective(sku);
            //添加stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insertSelective(stock);
        });

        //3 更新spu
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        spuMapper.updateByPrimaryKeySelective(spuBo);
        //4 更新spuDetail
        spuDetailMapper.updateByPrimaryKey(spuBo.getSpuDetail());

    }
}
