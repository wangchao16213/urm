package cn.com.eship.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import cn.com.eship.context.ContextSetting;
import cn.com.eship.models.Behavior;
import cn.com.eship.models.BehaviorField;
import cn.com.eship.models.CustomerGroup;
import cn.com.eship.models.Event;
import cn.com.eship.models.Funnel;
import cn.com.eship.models.FunnelDetail;
import cn.com.eship.models.vo.EventAnalyticsVo;
import cn.com.eship.models.vo.FunnelAnalyticsVo;
import cn.com.eship.models.vo.HitAnalyticsVo;
import cn.com.eship.repository.BehaviorFieldRepository;
import cn.com.eship.repository.BehaviorRepository;
import cn.com.eship.repository.ChannelRepository;
import cn.com.eship.repository.CustomerGroupRepository;
import cn.com.eship.repository.DataWarehouseRepository;
import cn.com.eship.repository.EventRepository;
import cn.com.eship.repository.FunnelDetailRepository;
import cn.com.eship.repository.FunnelRepository;
import cn.com.eship.repository.specifications.FunnelSpecification;
import cn.com.eship.service.InsightService;
import cn.com.eship.utils.DateUtilsHelper;
import cn.com.eship.utils.SqlConstructor;

@EnableTransactionManagement()
@Service
public class InsightServiceImpl extends CommonServiceImpl implements InsightService {
    @Autowired
    private BehaviorRepository behaviorRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private DataWarehouseRepository dataWarehouseRepository;
    @Autowired
    private BehaviorFieldRepository behaviorFieldRepository;
    @Autowired
    private FunnelRepository funnelRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private FunnelDetailRepository funnelDetailRepository;
    @Autowired
    private CustomerGroupRepository customerGroupRepository;

    @Override
    public EventAnalyticsVo eventAnalytics(String channelId, String eventId, String dimension, String measureId, String chartType, String dateranger, String filter, String customerGroupId) {
        Map<String,String> eventDict = getEventDict(channelId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        EventAnalyticsVo eventAnalyticsVo = new EventAnalyticsVo();
        Behavior eventBehavior = behaviorRepository.findEventBehaviorByChannelId(channelId);
        SqlConstructor sqlConstructor = new SqlConstructor(eventBehavior);
        BehaviorField measure = behaviorFieldRepository.findById(measureId).get();
        if (StringUtils.isNotBlank(filter)) {
            sqlConstructor.filter(" AND " + filter);
        }
        if (StringUtils.isNotBlank(eventId)) {
            Event event = eventRepository.findById(eventId).get();
            sqlConstructor.filter(String.format(" AND %s.%s = '%s'", eventBehavior.getAlias(),
                    ContextSetting.COMMON_EVENT_DIMENSION, event.getEventName()));
        }

        if (StringUtils.isNotBlank(dateranger)) {
            sqlConstructor.filterDateranger(dateranger, ContextSetting.COMMON_DATE_DIMENSION);
        }
        //拼接客户群
        if (StringUtils.isNoneBlank(customerGroupId)) {
            CustomerGroup customerGroup = customerGroupRepository.findById(customerGroupId).get();
            BehaviorField keyField = behaviorFieldRepository.findKeyFieldByBehavioId(eventBehavior.getId());
            sqlConstructor.joinCustomerGroup(customerGroup, keyField);
        }
        boolean flag = false;
        if ("line".equals(chartType) || StringUtils.isBlank(chartType)) {
            //无维度无事件 显示按照时间和事件分组
            if (StringUtils.isBlank(dimension) && StringUtils.isBlank(eventId)) {
                flag = true;
                sqlConstructor.agg(ContextSetting.COMMON_EVENT_DIMENSION).agg(ContextSetting.COMMON_DATE_DIMENSION);
            }
            //有维度有事件 显示按照时间和维度进行分组
            else if (StringUtils.isNotBlank(dimension) && StringUtils.isNotBlank(eventId)) {
                sqlConstructor.agg(dimension).agg(ContextSetting.COMMON_DATE_DIMENSION);
            }
            //有事件无维度 显示按照时间进行分组
            else if (StringUtils.isBlank(dimension) && StringUtils.isNotBlank(eventId)) {
                flag = true;
                sqlConstructor.agg(ContextSetting.COMMON_EVENT_DIMENSION).agg(ContextSetting.COMMON_DATE_DIMENSION);
            }
            String sql = sqlConstructor.measure(measure.getFieldName()).getSql();
            List<List<Object>> dataset = dataWarehouseRepository.commonData(sql);

            Map<String, Object> seriesTemp = new HashMap<>();

            for (List<Object> dataTuple : dataset) {
                String key = flag?eventDict.get((String) dataTuple.get(0)):(String) dataTuple.get(0);
                Object seriesItem = seriesTemp.get(key);
                //TODO 设置字典
                if (seriesItem == null) {
                    seriesTemp.put(key, getDateSeq(dateranger));
                }
                ((Map<String, Object>) seriesTemp.get(key)).put(simpleDateFormat.format(dataTuple.get(1)), dataTuple.get(2));
            }

            List<String> datelist = DateUtilsHelper.getDateListByRangeer(dateranger);
            eventAnalyticsVo.getxAxis().put("data", datelist);
            eventAnalyticsVo.getLegend().put("data", seriesTemp.keySet());
            Set<String> keys = seriesTemp.keySet();
            for (String key : keys) {
                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.put("name", key);
                mapTemp.put("type", "line");
                mapTemp.put("data", ((Map) seriesTemp.get(key)).values());
                eventAnalyticsVo.getSeries().add(mapTemp);
            }

        } else if ("bar".equals(chartType)) {
            //无维度 无事件
            if (StringUtils.isBlank(dimension) && StringUtils.isBlank(eventId)) {
                flag = true;
                sqlConstructor.agg(ContextSetting.COMMON_EVENT_DIMENSION);
            }
            //有维度 有事件
            else if (StringUtils.isNotBlank(dimension) && StringUtils.isNotBlank(eventId)) {
                sqlConstructor.agg(dimension);
            }
            //无维度 有事件
            else if (StringUtils.isNotBlank(eventId) && StringUtils.isBlank(dimension)) {
                flag = true;
                sqlConstructor.agg(ContextSetting.COMMON_EVENT_DIMENSION);
            }
            String sql = sqlConstructor.measure(measure.getFieldName()).order(measure.getFieldName(),"DESC").limit("20").getSql();
            List<List<Object>> dataset = dataWarehouseRepository.commonData(sql);

            Map<String, Object> seriesTemp = new HashMap<>();
            for (List<Object> dataTuple : dataset) {
                //TODO 设置字典
                String key = flag?eventDict.get((String) dataTuple.get(0)):(String) dataTuple.get(0);
                seriesTemp.put(key, dataTuple.get(1));
            }
            eventAnalyticsVo.getxAxis().put("data", seriesTemp.keySet());
            List<Object> legendData = new ArrayList<>();
            legendData.add(measure.getFieldName());
            eventAnalyticsVo.getLegend().put("data", legendData);
            Map<String, Object> seriesItem = new HashMap<>();
            seriesItem.put("name", measure.getFieldLable());
            seriesItem.put("type", "bar");
            seriesItem.put("data", seriesTemp.values());
            eventAnalyticsVo.getSeries().add(seriesItem);
        }
        return eventAnalyticsVo;
    }

    @Override
    public HitAnalyticsVo hitAnalytics(String channelId, String dimension, String measureId, String chartType, String dateranger, String filter, String customerGroupId) {
        HitAnalyticsVo hitAnalyticsVo = new HitAnalyticsVo();
        BehaviorField measure = behaviorFieldRepository.findById(measureId).get();
        Behavior hitBehavior = behaviorRepository.findHitBehaviorByChannelId(channelId);
        SqlConstructor sqlConstructor = new SqlConstructor(hitBehavior);
        if (StringUtils.isNotBlank(filter)) {
            sqlConstructor.filter(" AND " + filter);
        }
        if (StringUtils.isNotBlank(dateranger)) {
            sqlConstructor.filterDateranger(dateranger, ContextSetting.COMMON_DATE_DIMENSION);
        }
        //拼接客户群
        if (StringUtils.isNoneBlank(customerGroupId)) {
            CustomerGroup customerGroup = customerGroupRepository.findById(customerGroupId).get();
            BehaviorField keyField = behaviorFieldRepository.findKeyFieldByBehavioId(hitBehavior.getId());
            sqlConstructor.joinCustomerGroup(customerGroup, keyField);
        }
        if ("line".equals(chartType) || chartType == null) {
            //无维度 按照时间分组
            if (StringUtils.isBlank(dimension)) {
                String sql = sqlConstructor.agg(ContextSetting.COMMON_DATE_DIMENSION).measure(measure.getFieldName()).getSql();
                List<List<Object>> dataset = dataWarehouseRepository.commonData(sql);
                Map<String, Object> seriesTemp = getDateSeq(dateranger);
                for (List<Object> dataTuple : dataset) {
                    seriesTemp.put(new SimpleDateFormat("yyyy-MM-dd").format(dataTuple.get(0)), dataTuple.get(1));
                }
                hitAnalyticsVo.getxAxis().put("data", seriesTemp.keySet());
                List<Object> legendData = new ArrayList<>();
                legendData.add(measure.getFieldName());
                hitAnalyticsVo.getLegend().put("data", legendData);
                Map<String, Object> seriesItem = new LinkedHashMap<>();
                seriesItem.put("name", measure.getFieldLable());
                seriesItem.put("type", "line");
                seriesItem.put("data", seriesTemp.values());
                hitAnalyticsVo.getSeries().add(seriesItem);
            } else {
                //有维度 按照时间和维度分组
                String sql = sqlConstructor.agg(dimension).agg(ContextSetting.COMMON_DATE_DIMENSION).measure(measure.getFieldName()).getSql();
                Map<String, Object> seriesTemp = new LinkedHashMap<>();
                List<List<Object>> dataset = dataWarehouseRepository.commonData(sql);
                for (List<Object> dataTuple : dataset) {
                    if (seriesTemp.get(dataTuple.get(0)) == null) {
                        seriesTemp.put((String) dataTuple.get(0), getDateSeq(dateranger));
                    }
                    ((Map<String, Object>) seriesTemp.get((String) dataTuple.get(0))).put(new SimpleDateFormat("yyyy-MM-dd").format(dataTuple.get(1)), dataTuple.get(2));
                }
                hitAnalyticsVo.getxAxis().put("data", DateUtilsHelper.getDateListByRangeer(dateranger));
                hitAnalyticsVo.getLegend().put("data", seriesTemp.keySet());
                for (Map.Entry<String, Object> entry : seriesTemp.entrySet()) {
                    Map<String, Object> seriesItem = new HashMap<>();
                    seriesItem.put("name", entry.getKey());
                    seriesItem.put("type", "line");
                    seriesItem.put("stack", measure.getFieldLable());
                    seriesItem.put("data", ((Map) entry.getValue()).values());
                    hitAnalyticsVo.getSeries().add(seriesItem);
                }
            }
        } else {
            if (StringUtils.isBlank(dimension)) {
                sqlConstructor.agg(ContextSetting.COMMON_DATE_DIMENSION);
            } else {
                sqlConstructor.agg(dimension);
            }
            String sql = sqlConstructor.measure(measure.getFieldName()).getSql();
            List<List<Object>> dataset = dataWarehouseRepository.commonData(sql);
            Map<String, Long> seriesTemp = new LinkedHashMap<>();
            for (List<Object> dataTuple : dataset) {
                if (dataTuple.get(0) instanceof java.sql.Date) {
                    seriesTemp.put(new SimpleDateFormat("yyyy-MM-dd").format(dataTuple.get(0)), (Long) dataTuple.get(1));
                } else {
                    seriesTemp.put((String) dataTuple.get(0), (Long) dataTuple.get(1));
                }
            }
            hitAnalyticsVo.getxAxis().put("data", seriesTemp.keySet());
            List<Object> legendData = new ArrayList<>();
            legendData.add(measure.getFieldName());
            hitAnalyticsVo.getLegend().put("data", legendData);
            Map<String, Object> seriesItem = new LinkedHashMap<>();
            seriesItem.put("name", measure.getFieldLable());
            seriesItem.put("type", "bar");
            seriesItem.put("data", seriesTemp.values());
            hitAnalyticsVo.getSeries().add(seriesItem);
        }


        return hitAnalyticsVo;
    }

    @Override
    public List<Funnel> findFunnelList(Funnel funnel) {
        FunnelSpecification funnelSpecification = new FunnelSpecification(funnel);
        return funnelRepository.findAll(funnelSpecification);
    }

    @Override
    public void saveFunnel(Funnel funnel, List<Map<String, Object>> funnelDetailList) {
        String funnelId = UUID.randomUUID().toString().replace("-", "");
        funnel.setId(funnelId);
        funnel.setCreateDate(new Date());
        funnel.setUpdateDate(new Date());
        funnel.setChannel(channelRepository.findById(funnel.getChannelId()).get());
        funnelRepository.save(funnel);
        Funnel funnel1 = funnelRepository.findById(funnelId).get();
        List<FunnelDetail> funnelDetails = new ArrayList<>();
        funnelDetailList.stream().forEach(funnelDetailMap -> {
            FunnelDetail funnelDetail = new FunnelDetail();
            funnelDetail.setEvent(eventRepository.findById((String) funnelDetailMap.get("eventId")).get());
            funnelDetail.setSortNum((Integer) funnelDetailMap.get("sortNum"));
            if (funnelDetailMap.get("stepName") != null && StringUtils.isNotBlank((String) funnelDetailMap.get("stepName"))) {
                funnelDetail.setStepName((String) funnelDetailMap.get("stepName"));
            } else {
                funnelDetail.setStepName(funnelDetail.getEvent().getEventLable());
            }
            funnelDetail.setId(UUID.randomUUID().toString().replaceAll("-",""));
            funnelDetail.setFunnel(funnel1);
            funnelDetail.setFilter((String) funnelDetailMap.get("filter"));
            funnelDetail.setFilterDsl((String) funnelDetailMap.get("filterDsl"));
            funnelDetails.add(funnelDetail);
        });
        funnelDetailRepository.saveAll(funnelDetails);
    }


    private Map<String, Object> getDateSeq(String dateranger) {
        List<String> datelist = DateUtilsHelper.getDateListByRangeer(dateranger);
        Map<String, Object> dateseqDict = new LinkedHashMap<>();
        for (String datestr : datelist) {
            dateseqDict.put(datestr, 0);
        }
        return dateseqDict;
    }


    @Transactional
    @Override
    public void deleteFunnel(String funnelId) {
        funnelDetailRepository.deleteAllByFunnelId(funnelId);
        funnelRepository.deleteById(funnelId);
    }

    @Override
    public List<FunnelDetail> findFunnelDetailListByFunnelId(String funnelId) {
        return funnelDetailRepository.findByFunnelId(funnelId, new Sort("sortNum"));
    }

    @Override
    public Funnel findFunnelById(String funnelId) {
        return funnelRepository.findById(funnelId).get();
    }

    @Override
    public FunnelAnalyticsVo funnelAnalytics(String funnelId, String measureId, String dateranger, String customerGroupId) {
        Funnel funnel = funnelRepository.findById(funnelId).get();
        List<FunnelDetail> funnelDetailList = funnelDetailRepository.findByFunnelId(funnelId, new Sort("sortNum"));
        Behavior eventBehavior = behaviorRepository.findEventBehaviorByChannelId(funnel.getChannel().getId());
        BehaviorField behaviorField = behaviorFieldRepository.findById(measureId).get();
        List<SqlConstructor> sqlConstructorList = new ArrayList<>();

        List<Object> legendData = new ArrayList<>();

        funnelDetailList.stream().forEach(funnelDetail -> {
            legendData.add(funnelDetail.getEvent().getEventLable());
            SqlConstructor sqlConstructor = new SqlConstructor(eventBehavior);
            sqlConstructor.filterDateranger(dateranger, ContextSetting.COMMON_DATE_DIMENSION);
            sqlConstructor.measure(behaviorField.getFieldName());
            sqlConstructor.filter(funnelDetail.getFilter());
            String eventName = funnelDetail.getEvent().getEventName();
            //拼接客户群
            if (StringUtils.isNoneBlank(customerGroupId)) {
                CustomerGroup customerGroup = customerGroupRepository.findById(customerGroupId).get();
                BehaviorField keyField = behaviorFieldRepository.findKeyFieldByBehavioId(eventBehavior.getId());
                sqlConstructor.joinCustomerGroup(customerGroup, keyField);
            }
            sqlConstructor.filter(String.format(" AND %s.%s='%s'", eventBehavior.getAlias(), ContextSetting.COMMON_EVENT_DIMENSION, eventName));
            sqlConstructorList.add(sqlConstructor);
        });
        StringBuffer sqlBuffer = new StringBuffer();
        for (int i = 0; i < sqlConstructorList.size(); i++) {
            if (i < sqlConstructorList.size() - 1) {
                sqlBuffer.append(sqlConstructorList.get(i).getSqlWithoutLimit()).append(" union ");
            } else {
                sqlBuffer.append(sqlConstructorList.get(i).getSqlWithoutLimit());
            }
        }
        List<List<Object>> dataset = dataWarehouseRepository.commonData(sqlBuffer.toString());

        FunnelAnalyticsVo funnelAnalyticsVo = new FunnelAnalyticsVo();
        funnelAnalyticsVo.getLegend().put("data", legendData);
        for (int i = 0; i < dataset.size(); i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", funnelDetailList.get(i).getStepName());
            map.put("value", dataset.get(i).get(0));
            funnelAnalyticsVo.getSeries().add(map);
        }
        return funnelAnalyticsVo;
    }



    private Map<String,String> getEventDict(String channelId){
        List<Event> events = eventRepository.findAllByChannelId(channelId);
        Map<String, String> dict = new HashMap<>();
        events.forEach(event -> {
            dict.put(event.getEventName(),event.getEventLable());
        });
        return dict;
    }
}
