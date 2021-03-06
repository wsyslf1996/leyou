package com.leyouxianggou.item.mapper;

import com.leyouxianggou.item.Brand;
import com.leyouxianggou.item.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>, IdListMapper<Brand,Long> {
    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    void deleteCategoryBrandByBid(@Param("bid") Long bid);

    @Select("SELECT * from tb_brand a WHERE a.id IN (SELECT b.brand_id from tb_category_brand b WHERE b.category_id = #{cid})")
    List<Brand> queryBrandByCid(Long cid);
}
