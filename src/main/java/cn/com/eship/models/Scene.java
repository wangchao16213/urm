package cn.com.eship.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "scene", schema = "urm")
public class Scene {
    private String id;
    private String sceneName;
    private String sceneStatus;
    private Date createDate;
    private String sceneDesc;
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
    @Column(name = "scene_name", nullable = false, length = 100)
    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    @Basic
    @Column(name = "scene_status", nullable = false, length = 50)
    public String getSceneStatus() {
        return sceneStatus;
    }

    public void setSceneStatus(String sceneStatus) {
        this.sceneStatus = sceneStatus;
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
    @Column(name = "scene_desc", nullable = true, length = -1)
    public String getSceneDesc() {
        return sceneDesc;
    }

    public void setSceneDesc(String sceneDesc) {
        this.sceneDesc = sceneDesc;
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
        Scene scene = (Scene) o;
        return id == scene.id &&
                Objects.equals(sceneName, scene.sceneName) &&
                Objects.equals(sceneStatus, scene.sceneStatus) &&
                Objects.equals(createDate, scene.createDate) &&
                Objects.equals(sceneDesc, scene.sceneDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sceneName, sceneStatus, createDate, sceneDesc);
    }
}
