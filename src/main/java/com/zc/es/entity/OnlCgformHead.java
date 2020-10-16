package com.zc.es.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value="OnlCgformHead对象", description="")
public class OnlCgformHead extends Model<OnlCgformHead> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表类型: 0单表、1主表、2附表")
    private Integer tableType;

    @ApiModelProperty(value = "表版本")
    private Integer tableVersion;

    @ApiModelProperty(value = "表说明")
    private String tableTxt;

    @ApiModelProperty(value = "是否带checkbox")
    private String isCheckbox;

    @ApiModelProperty(value = "同步数据库状态")
    private String isDbSynch;

    @ApiModelProperty(value = "是否分页")
    private String isPage;

    @ApiModelProperty(value = "是否是树")
    private String isTree;

    @ApiModelProperty(value = "主键生成序列")
    private String idSequence;

    @ApiModelProperty(value = "主键类型")
    private String idType;

    @ApiModelProperty(value = "查询模式")
    private String queryMode;

    @ApiModelProperty(value = "映射关系 0一对多  1一对一")
    private Integer relationType;

    @ApiModelProperty(value = "子表")
    private String subTableStr;

    @ApiModelProperty(value = "附表排序序号")
    private Integer tabOrderNum;

    @ApiModelProperty(value = "树形表单父id")
    private String treeParentIdField;

    @ApiModelProperty(value = "树表主键字段")
    private String treeIdField;

    @ApiModelProperty(value = "树开表单列字段")
    private String treeFieldname;

    @ApiModelProperty(value = "表单分类")
    private String formCategory;

    @ApiModelProperty(value = "PC表单模板")
    private String formTemplate;

    @ApiModelProperty(value = "表单模板样式(移动端)")
    private String formTemplateMobile;

    @ApiModelProperty(value = "是否有横向滚动条")
    private Integer scroll;

    @ApiModelProperty(value = "复制版本号")
    private Integer copyVersion;

    @ApiModelProperty(value = "复制表类型1为复制表 0为原始表")
    private Integer copyType;

    @ApiModelProperty(value = "原始表ID")
    private String physicId;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "主题模板")
    private String themeTemplate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
