package cn.com.eship.models.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAnalyticsVo {
    private Map<String, Object> legend;
    private Map<String, Object> xAxis;
    private List<Object> series;


    public EventAnalyticsVo() {
        this.legend = new HashMap<>();
        this.xAxis = new HashMap<>();
        this.series = new ArrayList<>();
        this.legend.put("data", new ArrayList<String>());
        this.xAxis.put("type", "category");
        this.xAxis.put("data", new ArrayList<>());
    }


    public Map<String, Object> getLegend() {
        return legend;
    }

    public void setLegend(Map<String, Object> legend) {
        this.legend = legend;
    }

    public Map<String, Object> getxAxis() {
        return xAxis;
    }

    public void setxAxis(Map<String, Object> xAxis) {
        this.xAxis = xAxis;
    }

    public List<Object> getSeries() {
        return series;
    }

    public void setSeries(List<Object> series) {
        this.series = series;
    }
}
