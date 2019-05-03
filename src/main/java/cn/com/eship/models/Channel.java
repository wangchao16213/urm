package cn.com.eship.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "channel", schema = "urm")
public class Channel {
    private String id;
    private String channelName;
    private String channelDesc;
    private String catalog;
    private String schema;

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
    @Column(name = "channel_name", nullable = false, length = 50)
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Basic
    @Column(name = "channel_desc", nullable = true, length = -1)
    public String getChannelDesc() {
        return channelDesc;
    }

    public void setChannelDesc(String channelDesc) {
        this.channelDesc = channelDesc;
    }

    @Basic
    @Column(name = "catalog", nullable = true, length = 32)
    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    @Basic
    @Column(name = "schema", nullable = true, length = 100)
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return id == channel.id &&
                Objects.equals(channelName, channel.channelName) &&
                Objects.equals(channelDesc, channel.channelDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channelName, channelDesc);
    }
}
