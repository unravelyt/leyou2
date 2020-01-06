package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    /**
     * 新增category_brand中间表
     * @param cid
     * @param id
     */
    @Insert("insert into tb_category_brand(category_id, brand_id) values(#{cid}, #{id})")
    void insertBrandCategory(Long cid, Long id);

    /**
     * 根据分类id,两表显式内连接查询品牌
     * @param cid
     * @return
     */
    @Select("select b.* from tb_brand b join tb_category_brand cb on cb.category_id = #{cid} where cb.brand_id = b.id")
    List<Brand> selectBrandsByCategoryId(Long cid);
}
