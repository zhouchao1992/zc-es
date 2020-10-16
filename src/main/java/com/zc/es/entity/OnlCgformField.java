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
@ApiModel(value="OnlCgformField对象", description="")
public class OnlCgformField extends Model<OnlCgformField> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "表ID")
    private String cgformHeadId;

    @ApiModelProperty(value = "字段名字")
    private String dbFieldName;

    @ApiModelProperty(value = "字段备注")
    private String dbFieldTxt;

    @ApiModelProperty(value = "原字段名")
    private String dbFieldNameOld;

    @ApiModelProperty(value = "是否主键 0否 1是")
    private Boolean dbIsKey;

    @ApiModelProperty(value = "是否允许为空0否 1是")
    private Boolean dbIsNull;

    @ApiModelProperty(value = "数据库字段类型")
    private String dbType;

    @ApiModelProperty(value = "数据库字段长度")
    private Integer dbLength;

    @ApiModelProperty(value = "小数点")
    private Integer dbPointLength;

    @ApiModelProperty(value = "表字段默认值")
    private String dbDefaultVal;

    @ApiModelProperty(value = "字典code")
    private String dictField;

    @ApiModelProperty(value = "字典表")
    private String dictTable;

    @ApiModelProperty(value = "字典Text")
    private String dictText;

    @ApiModelProperty(value = "表单控件类型")
    private String fieldShowType;

    @ApiModelProperty(value = "跳转URL")
    private String fieldHref;

    @ApiModelProperty(value = "表单控件长度")
    private Integer fieldLength;

    @ApiModelProperty(value = "表单字段校验规则")
    private String fieldValidType;

    @ApiModelProperty(value = "字段是否必填")
    private String fieldMustInput;

    @ApiModelProperty(value = "扩展参数JSON")
    private String fieldExtendJson;

    @ApiModelProperty(value = "控件默认值，不同的表达式展示不同的结果。")
    private String fieldDefaultValue;

    @ApiModelProperty(value = "是否查询条件0否 1是")
    private Boolean isQuery;

    @ApiModelProperty(value = "表单是否显示0否 1是")
    private Boolean isShowForm;

    @ApiModelProperty(value = "列表是否显示0否 1是")
    private Boolean isShowList;

    @ApiModelProperty(value = "是否是只读（1是 0否）")
    private Boolean isReadOnly;

    @ApiModelProperty(value = "查询模式")
    private String queryMode;

    @ApiModelProperty(value = "外键主表名")
    private String mainTable;

    @ApiModelProperty(value = "外键主键字段")
    private String mainField;

    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "自定义值转换器")
    private String converter;

    @ApiModelProperty(value = "查询默认值")
    private String queryDefVal;

    @ApiModelProperty(value = "查询配置字典text")
    private String queryDictText;

    @ApiModelProperty(value = "查询配置字典code")
    private String queryDictField;

    @ApiModelProperty(value = "查询配置字典table")
    private String queryDictTable;

    @ApiModelProperty(value = "查询显示控件")
    private String queryShowType;

    @ApiModelProperty(value = "是否启用查询配置1是0否")
    private String queryConfigFlag;

    @ApiModelProperty(value = "查询字段校验类型")
    private String queryValidType;

    @ApiModelProperty(value = "查询字段是否必填1是0否")
    private String queryMustInput;

    @ApiModelProperty(value = "是否支持排序1是0否")
    private String sortFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
