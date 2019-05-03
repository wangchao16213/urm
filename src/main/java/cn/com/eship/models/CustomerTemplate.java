package cn.com.eship.models;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "customer_template", schema = "urm")
public class CustomerTemplate {
    private String id;
    private String templateName;
    private String templateLable;
    private String tbName;
    private String namespace;
    private String templateDesc;
    private Date createDate;
    private String alias;
    private String synStatus;
    private Channel channel;

    @Id
    @Column(name = "id", nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "template_name", nullable = false, length = 100)
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Basic
    @Column(name = "template_lable", nullable = false, length = 100)
    public String getTemplateLable() {
        return templateLable;
    }

    public void setTemplateLable(String templateLable) {
        this.templateLable = templateLable;
    }

    @Basic
    @Column(name = "tb_name", nullable = false, length = 100)
    public String getTbName() {
        return tbName;
    }

    public void setTbName(String tbName) {
        this.tbName = tbName;
    }

    @Basic
    @Column(name = "namespace", nullable = false, length = 100)
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Basic
    @Column(name = "template_desc", nullable = true, length = -1)
    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    @Basic
    @Column(name = "create_date", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "alias", nullable = false, length = 10)
    public String getAlias() {
        if (StringUtils.isNotBlank(alias)) {
            return tbName;
        }
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Basic
    @Column(name = "syn_status", nullable = false, length = 100)
    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "channel_id")
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }


    @Transient
    public String getFullTableName() {
        String tableName = this.namespace + "." + this.tbName;
        return tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerTemplate that = (CustomerTemplate) o;
        return id == that.id &&
                Objects.equals(templateName, that.templateName) &&
                Objects.equals(templateLable, that.templateLable) &&
                Objects.equals(tbName, that.tbName) &&
                Objects.equals(namespace, that.namespace) &&
                Objects.equals(templateDesc, that.templateDesc) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(alias, that.alias) &&
                Objects.equals(synStatus, that.synStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, templateName, templateLable, tbName, namespace, templateDesc, createDate, alias, synStatus);
    }
}
