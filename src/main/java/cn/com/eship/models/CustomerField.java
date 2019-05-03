package cn.com.eship.models;

import cn.com.eship.models.enums.CustomerFieldType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "customer_field", schema = "urm")
public class CustomerField {
    private String id;
    private String fieldName;
    private String fieldLable;
    private String alias;
    private String fieldType;
    private String fieldTypeText;
    private Integer sortNum;
    private String fieldDesc;
    private byte isKey;
    private String contactType;
    private String fieldFormat;
    private String fieldDictKey;
    private CustomerTemplate customerTemplate;
    private String customerTemplateId;


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
    @Column(name = "field_desc", nullable = true, length = -1)
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
    @Column(name = "contact_type", nullable = true, length = 100)
    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
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

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "customer_template_id")
    public CustomerTemplate getCustomerTemplate() {
        return customerTemplate;
    }

    public void setCustomerTemplate(CustomerTemplate customerTemplate) {
        this.customerTemplate = customerTemplate;
    }

    @Transient
    public String getCustomerTemplateId() {
        return customerTemplateId;
    }

    public void setCustomerTemplateId(String customerTemplateId) {
        this.customerTemplateId = customerTemplateId;
    }

    @Transient
    public String getFullFieldName() {
        return this.customerTemplate.getAlias() + "." + fieldName;
    }

    @Transient
    public String getFieldTypeText() {
        if (StringUtils.isNotBlank(fieldType)) {
            switch (fieldType) {
                case CustomerFieldType
                        .TEXT: {
                    this.fieldTypeText = "文本";
                    break;
                }
                case CustomerFieldType
                        .NUMBER: {
                    this.fieldTypeText = "数值";
                    break;
                }
                case CustomerFieldType
                        .BOOLEAN: {
                    this.fieldTypeText = "布尔";
                    break;
                }
                case CustomerFieldType
                        .DATE: {
                    this.fieldTypeText = "时间";
                    break;
                }
                case CustomerFieldType
                        .AGGREGATION: {
                    this.fieldTypeText = "集合";
                    break;
                }
            }
        }
        return fieldTypeText;
    }

    public void setFieldTypeText(String fieldTypeText) {
        this.fieldTypeText = fieldTypeText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerField that = (CustomerField) o;
        return id == that.id &&
                isKey == that.isKey &&
                Objects.equals(fieldName, that.fieldName) &&
                Objects.equals(fieldLable, that.fieldLable) &&
                Objects.equals(alias, that.alias) &&
                Objects.equals(fieldType, that.fieldType) &&
                Objects.equals(sortNum, that.sortNum) &&
                Objects.equals(fieldDesc, that.fieldDesc) &&
                Objects.equals(contactType, that.contactType) &&
                Objects.equals(fieldFormat, that.fieldFormat) &&
                Objects.equals(fieldDictKey, that.fieldDictKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fieldName, fieldLable, alias, fieldType, sortNum, fieldDesc, isKey, contactType, fieldFormat, fieldDictKey);
    }
}
