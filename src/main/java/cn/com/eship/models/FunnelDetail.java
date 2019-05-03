package cn.com.eship.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "funnel_detail", schema = "urm")
public class FunnelDetail {
    private String id;
    private int sortNum;
    private String filterDsl;
    private String filter;
    private Event event;
    private String stepName;
    private Funnel funnel;

    @Id
    @Column(name = "id", nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "sort_num", nullable = false)
    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    @Basic
    @Column(name = "filter_dsl", nullable = true, length = -1)
    public String getFilterDsl() {
        return filterDsl;
    }

    public void setFilterDsl(String filterDsl) {
        this.filterDsl = filterDsl;
    }

    @Basic
    @Column(name = "filter", nullable = true, length = -1)
    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Basic
    @Column(name = "step_name", nullable = true, length = 100)
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "event_id")
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST}, optional = false)
//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "funnel_id")
    public Funnel getFunnel() {
        return funnel;
    }

    public void setFunnel(Funnel funnel) {
        this.funnel = funnel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunnelDetail that = (FunnelDetail) o;
        return id == that.id &&
                sortNum == that.sortNum &&
                Objects.equals(filterDsl, that.filterDsl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sortNum, filterDsl);
    }
}
