package cn.com.eship.models;

import cn.com.eship.utils.DateUtilsHelper;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dashboard_panel", schema = "urm", catalog = "")
public class DashboardPanel {
    private String id;
    private String panelName;
    private String cubeDsl;
    private String dateranger;
    private String daterangerText;
    private Integer sortNum;
    private String autodateType;
    private String dashboardId;
    private Dashboard dashboard;
    private String filter;
    private String analyticsType;

    @Id
    @Column(name = "id", nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "panel_name", nullable = true, length = 100)
    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

    @Basic
    @Column(name = "cube_dsl", nullable = true, length = -1)
    public String getCubeDsl() {
        return cubeDsl;
    }

    public void setCubeDsl(String cubeDsl) {
        this.cubeDsl = cubeDsl;
    }

    @Basic
    @Column(name = "dateranger", nullable = true, length = 100)
    public String getDateranger() {
        return dateranger;
    }

    public void setDateranger(String dateranger) {
        this.dateranger = dateranger;
    }

    @Basic
    @Column(name = "sort_num", nullable = true)
    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    @Basic
    @Column(name = "autodate_type", nullable = true, length = 10)
    public String getAutodateType() {
        return autodateType;
    }

    public void setAutodateType(String autodateType) {
        this.autodateType = autodateType;
    }


    @Transient
    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
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
    @Column(name = "analytics_type", nullable = true, length = 10)
    public String getAnalyticsType() {
        return analyticsType;
    }

    public void setAnalyticsType(String analyticsType) {
        this.analyticsType = analyticsType;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)//可选属性optional=false,表示company不能为空
    @JoinColumn(name = "dashboard_id")
    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }


    @Transient
    public String getDaterangerText() {
        String result = "";
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        switch (this.autodateType) {
            case "0": {
                //今日
                result = simpleDateFormat.format(today) + "," + simpleDateFormat.format(today);
                break;
            }
            case "1": {
                //昨日
                Date yestoday = DateUtils.addDays(today, -1);
                result = simpleDateFormat.format(yestoday) + "," + simpleDateFormat.format(yestoday);
                break;
            }
            case "2": {
                //7日
                Date dateTemp = DateUtils.addDays(today, -7);
                result = simpleDateFormat.format(dateTemp) + "," + simpleDateFormat.format(today);
                break;
            }
            case "3": {
                //30日
                Date dateTemp = DateUtils.addDays(today, -30);
                result = simpleDateFormat.format(dateTemp) + "," + simpleDateFormat.format(today);
                break;
            }
            case "4": {
                //本月
                List<String> dateranger = DateUtilsHelper.getThisMonth();
                result = dateranger.get(0) + "," + dateranger.get(1);
                break;
            }
            case "5": {
                //上月
                List<String> dateranger = DateUtilsHelper.getLastMonth();
                result = dateranger.get(0) + "," + dateranger.get(1);
                break;
            }
            case "6": {
                //自定义
                result = this.dateranger;
                break;
            }
        }
        return result;
    }

    public void setDaterangerText(String daterangerText) {
        this.daterangerText = daterangerText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardPanel that = (DashboardPanel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(panelName, that.panelName) &&
                Objects.equals(cubeDsl, that.cubeDsl) &&
                Objects.equals(dateranger, that.dateranger) &&
                Objects.equals(sortNum, that.sortNum) &&
                Objects.equals(autodateType, that.autodateType) &&
                Objects.equals(dashboardId, that.dashboardId) &&
                Objects.equals(filter, that.filter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, panelName, cubeDsl, dateranger, sortNum, autodateType, dashboardId, filter);
    }
}
