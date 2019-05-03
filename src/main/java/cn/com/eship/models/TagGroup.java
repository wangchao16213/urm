package cn.com.eship.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tag_group", schema = "urm")
public class TagGroup {
    private String id;
    private String tagGroupName;
    private String tagGroupDesc;
    private String channelId;
    private Channel channel;

    @Id
    @Column(name = "id", nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tag_group_name", nullable = true, length = 100)
    public String getTagGroupName() {
        return tagGroupName;
    }

    public void setTagGroupName(String tagGroupName) {
        this.tagGroupName = tagGroupName;
    }

    @Basic
    @Column(name = "tag_group_desc", nullable = true, length = -1)
    public String getTagGroupDesc() {
        return tagGroupDesc;
    }

    public void setTagGroupDesc(String tagGroupDesc) {
        this.tagGroupDesc = tagGroupDesc;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagGroup tagGroup = (TagGroup) o;
        return Objects.equals(id, tagGroup.id) &&
                Objects.equals(tagGroupName, tagGroup.tagGroupName) &&
                Objects.equals(tagGroupDesc, tagGroup.tagGroupDesc) &&
                Objects.equals(channelId, tagGroup.channelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagGroupName, tagGroupDesc, channelId);
    }
}
