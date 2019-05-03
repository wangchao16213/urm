package cn.com.eship.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "scene_event_ref", schema = "urm")
public class SceneEventRef {
    private String id;
    private String eventFilter;
    private Event event;
    private Scene scene;

    @Id
    @Column(name = "id", nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "event_filter", nullable = true, length = -1)
    public String getEventFilter() {
        return eventFilter;
    }

    public void setEventFilter(String eventFilter) {
        this.eventFilter = eventFilter;
    }


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "event_id")
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "scene_id")
    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SceneEventRef that = (SceneEventRef) o;
        return id == that.id &&
                Objects.equals(eventFilter, that.eventFilter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventFilter);
    }
}
