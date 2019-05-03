package cn.com.eship.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "dashboard",schema = "urm")
public class Dashboard implements Serializable {
    private String id;
    private String dashboardName;
    private String channelId;
    private Channel channel;
    private Date createDate;

    @Id
    @Column(name = "id", nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "dashboard_name", nullable = true, length = 100)
    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }


    @Transient
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "channel_id")
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Basic
    @Column(name = "create_date", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dashboard dashbarod = (Dashboard) o;
        return Objects.equals(id, dashbarod.id) &&
                Objects.equals(dashboardName, dashbarod.dashboardName) &&
                Objects.equals(channelId, dashbarod.channelId) &&
                Objects.equals(createDate, dashbarod.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dashboardName, channelId, createDate);
    }
}
