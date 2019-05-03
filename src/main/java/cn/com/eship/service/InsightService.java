package cn.com.eship.service;

import cn.com.eship.models.Funnel;
import cn.com.eship.models.FunnelDetail;
import cn.com.eship.models.vo.EventAnalyticsVo;
import cn.com.eship.models.vo.FunnelAnalyticsVo;
import cn.com.eship.models.vo.HitAnalyticsVo;

import java.util.List;
import java.util.Map;

public interface InsightService extends CommonService{
    public EventAnalyticsVo eventAnalytics(String channelId, String eventId, String dimension, String measureId, String chartType, String dateranger, String filter, String customerGroupId);

    public HitAnalyticsVo hitAnalytics(String channelId, String dimension, String measureId, String chartType, String dateranger, String filter, String customerGroupId);

    public List<Funnel> findFunnelList(Funnel funnel);

    public void saveFunnel(Funnel funnel, List<Map<String, Object>> funnelDetailList);

    public void deleteFunnel(String funnelId);

    public List<FunnelDetail> findFunnelDetailListByFunnelId(String funnelId);

    public Funnel findFunnelById(String funnelId);

    public FunnelAnalyticsVo funnelAnalytics(String funnelId, String measureId, String dateranger, String customerGroupId);
}
