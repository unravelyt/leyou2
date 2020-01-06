package com.leyou.item.service;

import com.leyou.common.pojo.ResultPage;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

public interface GoodsService {

    /**
     * 根据搜索条件分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    ResultPage<SpuBo> querySpuPageByCondition(String key,Boolean saleable,Integer page,Integer rows);

    /**
     * 添加商品
     * @param spuBo
     * @return
     */
    void saveGoods(SpuBo spuBo);

    /**
     * 根据spuid查询spuDetail
     * @param spuId
     * @return
     */
    SpuDetail querySpuDetailBySpuId(Long spuId);

    /**
     * 根据spuId查询skus
     * @param id
     * @return
     */
    List<Sku> querySkusBySpuId(Long id);

    /**
     * 更新商品
     * @param spuBo
     */
    void updateGoods(SpuBo spuBo);
}
