package cn.com.eship.models;

import cn.com.eship.models.enums.BehaviorFieldType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "behavior_field", schema = "urm")
public class BehaviorField {
    private String id;
    private String fieldName;
    private String fieldLable;
    private String alias;
    private String fieldType;
    private String fieldTypeText;
    private Integer sortNum;
    private String fieldDesc;
    private byte isKey;
    private byte aggFlag;
    private String fieldFormat;
    private String fieldDictKey;
    private String measureType;
    private Behavior behavior;

    @Id
    @Column(name = "id", nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "field_name", nullable = false, length = 100)
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Basic
    @Column(name = "field_lable", nullable = false, length = 100)
    public String getFieldLable() {
        return fieldLable;
    }

    public void setFieldLable(String fieldLable) {
        this.fieldLable = fieldLable;
    }

    @Basic
    @Column(name = "alias", nullable = true, length = 10)
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Basic
    @Column(name = "field_type", nullable = false, length = 100)
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Basic
    @Column(name = "sort_num", nullable = true)
    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    @Basic
    @Column(name = "field_desc", nullable = true, length = 1000)
    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    @Basic
    @Column(name = "is_key", nullable = false)
    public byte getIsKey() {
        return isKey;
    }

    public void setIsKey(byte isKey) {
        this.isKey = isKey;
    }

    @Basic
    @Column(name = "agg_flag", nullable = false)
    public byte getAggFlag() {
        return aggFlag;
    }

    public void setAggFlag(byte aggFlag) {
        this.aggFlag = aggFlag;
    }

    @Basic
    @Column(name = "field_format", nullable = true, length = 100)
    public String getFieldFormat() {
        return fieldFormat;
    }

    public void setFieldFormat(String fieldFormat) {
        this.fieldFormat = fieldFormat;
    }

    @Basic
    @Column(name = "field_dict_key", nullable = true, length = 100)
    public String getFieldDictKey() {
        return fieldDictKey;
    }

    public void setFieldDictKey(String fieldDictKey) {
        this.fieldDictKey = fieldDictKey;
    }

    @Basic
    @Column(name = "measure_type", nullable = true, length = 100)
    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "behavior_id")
    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }


    @Transient
    public String getFullFieldName() {
        if (StringUtils.isBlank(this.behavior.getAlias())) {
            return behavior.getTbName() + "." + fieldName;
        }
        return behavior.getAlias() + "." + fieldName;
    }



    @Transient
    public String getFieldTypeText() {
        String result = null;
        if (StringUtils.isNotBlank(fieldType)) {
            switch (fieldType) {
                case BehaviorFieldType
                        .TEXT: {
                    result = "文本";
                    break;
                }
                case BehaviorFieldType
                        .NUMBER: {
                    result = "数值";
                    break;

                }
                case BehaviorFieldType
                        .BOOLEAN: {
                    result = "逻辑";
                    break;

                }
                case BehaviorFieldType
                        .DATE: {
                    result = "时间";
                    break;

                }
            }
        }
        return result;
    }

    public void setFieldTypeText(String fieldTypeText) {
        this.fieldTypeText = fieldTypeText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BehaviorField that = (BehaviorField) o;
        return id == that.id &&
                isKey == that.isKey &&
                aggFlag == that.aggFlag &&
                Objects.equals(fieldName, that.fieldName) &&
                Objects.equals(fieldLable, that.fieldLable) &&
                Objects.equals(alias, that.alias) &&
                Objects.equals(fieldType, that.fieldType) &&
                Objects.equals(sortNum, that.sortNum) &&
                Objects.equals(fieldDesc, that.fieldDesc) &&
                Objects.equals(fieldFormat, that.fieldFormat) &&
                Objects.equals(fieldDictKey, that.fieldDictKey) &&
                Objects.equals(measureType, that.measureType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fieldName, fieldLable, alias, fieldType, sortNum, fieldDesc, isKey, aggFlag, fieldFormat, fieldDictKey, measureType);
    }
}
