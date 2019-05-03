package cn.com.eship.models.vo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FunnelAnalyticsVo {
    private Map<String,Object> legend;
    private List<Object> series;

    public FunnelAnalyticsVo(){
        this.legend = new LinkedHashMap<>();
        this.legend.put("data",new ArrayList<>());
        this.series = new ArrayList<>();
    }

    public Map<String, Object> getLegend() {
        return legend;
    }

    public void setLegend(Map<String, Object> legend) {
        this.legend = legend;
    }

    public List<Object> getSeries() {
        return series;
    }

    public void setSeries(List<Object> series) {
        this.series = series;
    }
}
