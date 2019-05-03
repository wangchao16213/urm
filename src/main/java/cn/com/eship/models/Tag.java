package cn.com.eship.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tag", schema = "urm")
public class Tag {
    private String id;
    private String tagName;
    private String tagDesc;
    private String tagGroupId;
    private TagGroup tagGroup;

    @Id
    @Column(name = "id", nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tag_name", nullable = true, length = 100)
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Basic
    @Column(name = "tag_desc", nullable = true, length = -1)
    public String getTagDesc() {
        return tagDesc;
    }

    public void setTagDesc(String tagDesc) {
        this.tagDesc = tagDesc;
    }


    @Transient
    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "tag_group_id")
    public TagGroup getTagGroup() {
        return tagGroup;
    }

    public void setTagGroup(TagGroup tagGroup) {
        this.tagGroup = tagGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) &&
                Objects.equals(tagName, tag.tagName) &&
                Objects.equals(tagDesc, tag.tagDesc) &&
                Objects.equals(tagGroupId, tag.tagGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagName, tagDesc, tagGroupId);
    }
}
