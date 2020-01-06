package com.leyou.item.service;


import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

public interface SpecificationService {

    /**
     * 根据分类id查询该ID下所有的参数组
     * @param cid
     * @return
     */
    List<SpecGroup> querySpecGroupByCid(Long cid);

    /**
     * 根据规格组id查询规格参数
     * @param cid
     * @param gid
     * @param generic
     * @param searching
     * @return
     */
    List<SpecParam> querySpecParamByCondition(Long cid,Long gid,Boolean generic,Boolean searching);
}
