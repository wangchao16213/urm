package cn.com.eship.controller;

import cn.com.eship.models.BehaviorField;
import cn.com.eship.models.Channel;
import cn.com.eship.models.Event;
import cn.com.eship.service.SceneService;
import cn.com.eship.utils.QueryBuilderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("scene")
public class SceneController {
    @Autowired
    private SceneService sceneService;

    @RequestMapping("scenePage")
    public String scenePage(Model model) {
        List<Channel> channelList = sceneService.findAllChannel();
        model.addAttribute("channelList", channelList);
        return "scene/sceneList";
    }


    @RequestMapping("getEventsByChannelId")
    @ResponseBody
    public List<Event> getEventsByChannelId(String channelId) {
        Event event = new Event();
        event.setChannelId(channelId);
        List<Event> eventList = sceneService.findEvents(event);
        return eventList;
    }


    @RequestMapping("getFilterByChannelIdInEvent")
    @ResponseBody
    public List<Map<String, Object>> getFilterByChannelIdInEvent(String channelId) {
        List<BehaviorField> behaviorFieldList = sceneService.findDimensionByChannelIdAndBehaviorType(channelId,"2");
        List<Map<String, Object>> filters = new ArrayList<>();
        behaviorFieldList.forEach(behaviorField -> {
            filters.add(QueryBuilderHelper.getFilterItem(behaviorField));
        });
        return filters;
    }


    @RequestMapping("getFilterByChannelIdInHit")
    @ResponseBody
    public List<Map<String, Object>> getFilterByChannelIdInHit(String channelId) {
        List<BehaviorField> behaviorFieldList = sceneService.findDimensionByChannelIdAndBehaviorType(channelId,"0");
        List<Map<String, Object>> filters = new ArrayList<>();
        behaviorFieldList.forEach(behaviorField -> {
            filters.add(QueryBuilderHelper.getFilterItem(behaviorField));
        });
        return filters;
    }


    /**
     * 获取对应渠道的事件层的量度
     *
     * @return
     */
    @RequestMapping("getMeasureInEventLayerByChannelId")
    @ResponseBody
    public List<BehaviorField> getMeasureInEventLayerByChannelId(String channelId) {
        return sceneService.findMeasureInEventLayerByChannelId(channelId);
    }

    @RequestMapping("getDimensionByEventId")
    @ResponseBody
    public List<BehaviorField> getDimensionByEventId(String eventId) {
        return sceneService.findDimensionByEventId(eventId);
    }

    @RequestMapping("getMeasureInHitByChannelId")
    @ResponseBody
    public List<BehaviorField> getMeasureInHitByChannelId(String channelId) {
        return sceneService.findMeasureInHitByChannelId(channelId);
    }

    @RequestMapping("getDimensionInHitByChannelId")
    @ResponseBody
    public List<BehaviorField> getDimensionInHitByChannelId(String channelId) {
        return sceneService.findDimensionInHitByChannelId(channelId);
    }
}
