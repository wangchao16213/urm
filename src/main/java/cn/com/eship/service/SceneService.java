package cn.com.eship.service;

import cn.com.eship.models.BehaviorField;
import cn.com.eship.models.Channel;
import cn.com.eship.models.Event;

import java.util.List;

public interface SceneService {
    public List<Channel> findAllChannel();

    public List<Event> findEvents(Event event);

    public List<BehaviorField> findDimensionByChannelIdAndBehaviorType(String channelId,String behaviorType);

    public List<BehaviorField> findMeasureInEventLayerByChannelId(String channelId);

    public List<BehaviorField> findDimensionByEventId(String eventId);

    public List<BehaviorField> findMeasureInHitByChannelId(String channelId);
    public List<BehaviorField> findDimensionInHitByChannelId(String channelId);

    public List<BehaviorField> findMeasureForFunnel(String channelId);
}
