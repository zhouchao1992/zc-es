package com.zc.es.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zc
 * @since 2020-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OnlCgformIndex对象", description="")
public class OnlCgformIndex extends Model<OnlCgformIndex> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "主表id")
    private String cgformHeadId;

    @ApiModelProperty(value = "索引名称")
    private String indexName;

    @ApiModelProperty(value = "索引栏位")
    private String indexField;

    @ApiModelProperty(value = "索引类型")
    private String indexType;

    @ApiModelProperty(value = "创建人登录名称")
    private String createBy;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "更新人登录名称")
    private String updateBy;

    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    @ApiModelProperty(value = "是否同步数据库 N未同步 Y已同步")
    private String isDbSynch;

    @ApiModelProperty(value = "是否删除 0未删除 1删除")
    private Integer delFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
