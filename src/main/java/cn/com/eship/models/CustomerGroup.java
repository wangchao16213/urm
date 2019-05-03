package cn.com.eship.models;

import cn.com.eship.models.enums.GroupType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "customer_group", schema = "urm", catalog = "")
public class CustomerGroup {
    private String id;
    private String groupName;
    private String groupDesc;
    private Date createDate;
    private String channelId;
    private Channel channel;
    private String groupStatus;
    private String groupType;
    private String groupTypeText;
    private String cronExpression;
    private String groupEngineType;
    private String groupEngineTypeText;
    private String groupDsl;

    @Id
    @Column(name = "id", nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "group_name", nullable = true, length = 100)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Basic
    @Column(name = "group_desc", nullable = true, length = -1)
    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    @Basic
    @Column(name = "create_date", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    @Transient
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Basic
    @Column(name = "group_status", nullable = true, length = 5)
    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    @Basic
    @Column(name = "group_type", nullable = true, length = 5)
    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    @Basic
    @Column(name = "cron_expression", nullable = true, length = 100)
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Basic
    @Column(name = "group_engine_type", nullable = true, length = 5)
    public String getGroupEngineType() {
        return groupEngineType;
    }

    public void setGroupEngineType(String groupEngineType) {
        this.groupEngineType = groupEngineType;
    }

    @Basic
    @Column(name = "group_dsl", nullable = true, length = -1)
    public String getGroupDsl() {
        return groupDsl;
    }

    public void setGroupDsl(String groupDsl) {
        this.groupDsl = groupDsl;
    }

    @Transient
    public String getGroupEngineTypeText() {
        if (StringUtils.isNotBlank(this.groupEngineType)){
            switch (this.groupEngineType){

            }
        }
        return groupEngineTypeText;
    }

    public void setGroupEngineTypeText(String groupEngineTypeText) {
        this.groupEngineTypeText = groupEngineTypeText;
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
    public String getGroupTypeText() {
        String result = "";
        if (StringUtils.isNotBlank(this.groupType)) {
            switch (this.groupType) {
                case GroupType.STATIC: {
                    result = "静态";
                    break;
                }
                case GroupType.SCHEDULED: {
                    result = "调度";
                    break;
                }
            }
        }
        return result;
    }

    public void setGroupTypeText(String groupTypeText) {
        this.groupTypeText = groupTypeText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerGroup that = (CustomerGroup) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(groupDesc, that.groupDesc) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(groupStatus, that.groupStatus) &&
                Objects.equals(groupType, that.groupType) &&
                Objects.equals(cronExpression, that.cronExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, groupDesc, createDate, channelId, groupStatus, groupType, cronExpression);
    }
}
