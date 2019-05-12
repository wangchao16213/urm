package cn.com.eship.models;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "behavior", schema = "urm")
public class Behavior {
    private String id;
    private String behaviorName;
    private String behaviorDesc;
    private Date createDate;
    private Date updateDate;
    private String tbName;
    private String namespace;
    private String alias;
    private String behaviorType;
    private String synStatus;
    private Channel channel;
    private String fullTableName;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "behavior_name", nullable = false, length = 50)
    public String getBehaviorName() {
        return behaviorName;
    }

    public void setBehaviorName(String behaviorName) {
        this.behaviorName = behaviorName;
    }

    @Basic
    @Column(name = "behavior_desc", nullable = true, length = -1)
    public String getBehaviorDesc() {
        return behaviorDesc;
    }

    public void setBehaviorDesc(String behaviorDesc) {
        this.behaviorDesc = behaviorDesc;
    }

    @Basic
    @Column(name = "create_date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "update_date", nullable = false)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
    @Column(name = "alias", nullable = false, length = 10)
    public String getAlias() {
        if (StringUtils.isBlank(alias)) {
            this.alias = this.tbName;
        }
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Basic
    @Column(name = "behavior_type", nullable = false, length = 100)
    public String getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType) {
        this.behaviorType = behaviorType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Behavior behavior = (Behavior) o;
        return id == behavior.id &&
                Objects.equals(behaviorName, behavior.behaviorName) &&
                Objects.equals(behaviorDesc, behavior.behaviorDesc) &&
                Objects.equals(createDate, behavior.createDate) &&
                Objects.equals(updateDate, behavior.updateDate) &&
                Objects.equals(tbName, behavior.tbName) &&
                Objects.equals(namespace, behavior.namespace) &&
                Objects.equals(alias, behavior.alias) &&
                Objects.equals(behaviorType, behavior.behaviorType) &&
                Objects.equals(synStatus, behavior.synStatus);
    }


    @Transient
    public String getFullTableName() {
        if (StringUtils.isBlank(this.getAlias())) {
            return getNamespace() + "." + getTbName() + " " + getTbName();
        }
        return getNamespace() + "." + getTbName() + " " + getAlias();
    }

    public void setFullTableName(String fullTableName) {
        this.fullTableName = fullTableName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, behaviorName, behaviorDesc, createDate, updateDate, tbName, namespace, alias, behaviorType, synStatus);
    }
}
