package cn.com.eship.models;

import cn.com.eship.models.enums.EventStatus;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "event", schema = "urm")
public class Event {
    private String id;
    private String eventName;
    private String eventLable;
    private String eventStatus;
    private String eventStatusText;
    private Date createDate;
    private String eventDesc;
    private Channel channel;
    private String channelId;
    private List<BehaviorField> behaviorFields;
    private String script;
    private String cron;

    @Id
    @Column(name = "id", nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "event_name", nullable = false, length = 50)
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Basic
    @Column(name = "event_lable", nullable = false, length = 50)
    public String getEventLable() {
        return eventLable;
    }

    public void setEventLable(String eventLable) {
        this.eventLable = eventLable;
    }

    @Basic
    @Column(name = "event_status", nullable = false, length = 50)
    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
        if (StringUtils.isNotBlank(this.eventStatus)) {
            switch (this.eventStatus) {
                case EventStatus.AVAILABLE: {
                    this.eventStatusText = "可用";
                    break;
                }
                case EventStatus.UNAVAILABLE: {
                    this.eventStatusText = "不可用";
                    break;
                }
            }
        }
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
    @Column(name = "event_desc", nullable = true, length = 1000)
    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "channel_id")
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @ManyToMany
    @JoinTable(
            name = "event_behavior_field_list",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "behaviorfield_id"))
    public List<BehaviorField> getBehaviorFields() {
        return behaviorFields;
    }

    public void setBehaviorFields(List<BehaviorField> behaviorFields) {
        this.behaviorFields = behaviorFields;
    }


    @Transient
    public String getEventStatusText() {
        return eventStatusText;
    }

    public void setEventStatusText(String eventStatusText) {
        this.eventStatusText = eventStatusText;
    }

    @Basic
    @Column(name = "script", nullable = true, length = -1)
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Basic
    @Column(name = "cron", nullable = true, length = 20)
    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Transient
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id &&
                Objects.equals(eventName, event.eventName) &&
                Objects.equals(eventLable, event.eventLable) &&
                Objects.equals(eventStatus, event.eventStatus) &&
                Objects.equals(createDate, event.createDate) &&
                Objects.equals(eventDesc, event.eventDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventName, eventLable, eventStatus, createDate, eventDesc);
    }
}
