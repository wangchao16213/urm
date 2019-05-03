package cn.com.eship.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "funnel", schema = "urm", catalog = "")
public class Funnel {
    private String id;
    private String funnelName;
    private String funnelDesc;
    private Date createDate;
    private Date updateDate;
    private Channel channel;
    private String channelId;

    @Id
    @Column(name = "id", length = 32, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "funnel_name", nullable = false, length = 300)
    public String getFunnelName() {
        return funnelName;
    }

    public void setFunnelName(String funnelName) {
        this.funnelName = funnelName;
    }

    @Basic
    @Column(name = "funnel_desc", nullable = true, length = -1)
    public String getFunnelDesc() {
        return funnelDesc;
    }

    public void setFunnelDesc(String funnelDesc) {
        this.funnelDesc = funnelDesc;
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

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "channel_id")
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
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
        Funnel funnel = (Funnel) o;
        return id == funnel.id &&
                Objects.equals(funnelName, funnel.funnelName) &&
                Objects.equals(funnelDesc, funnel.funnelDesc) &&
                Objects.equals(createDate, funnel.createDate) &&
                Objects.equals(updateDate, funnel.updateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, funnelName, funnelDesc, createDate, updateDate);
    }
}
