package com.leyouxianggou.item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "tb_spu")
@Data
public class Spu {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable;// 是否上架
    private Boolean valid;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间

    @JsonIgnore
    private Date lastUpdateTime;// 最后修改时间
    //加上@Transient注解的字段在查询时不会添加到sql中
    @Transient
    private String cname;

    @Transient
    private String bname;

    @Transient
    private SpuDetail spuDetail;

    @Transient
    private List<Sku> skus;

}
