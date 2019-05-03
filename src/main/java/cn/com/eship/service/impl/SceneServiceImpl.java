package cn.com.eship.service.impl;

import cn.com.eship.models.BehaviorField;
import cn.com.eship.models.Channel;
import cn.com.eship.models.Event;
import cn.com.eship.repository.BehaviorFieldRepository;
import cn.com.eship.repository.ChannelRepository;
import cn.com.eship.repository.EventRepository;
import cn.com.eship.repository.specifications.EventSpecification;
import cn.com.eship.service.SceneService;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SceneServiceImpl implements SceneService {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private BehaviorFieldRepository behaviorFieldRepository;

    @Override
    public List<Channel> findAllChannel() {
        List<Channel> channelList = IteratorUtils.toList(channelRepository.findAll().iterator());
        return channelList;
    }

    @Override
    public List<Event> findEvents(Event event) {
        EventSpecification eventSpecification = new EventSpecification(event);
        return eventRepository.findAll(eventSpecification);
    }

    @Override
    public List<BehaviorField> findDimensionByChannelIdAndBehaviorType(String channelId, String behaviorType) {
        return behaviorFieldRepository.findDimensionByChannelIdAndBehaviorType(channelId, behaviorType);
    }

    @Override
    public List<BehaviorField> findMeasureInEventLayerByChannelId(String channelId) {
        return behaviorFieldRepository.findMeasureByChannelIdInEventLayer(channelId);
    }

    @Override
    public List<BehaviorField> findDimensionByEventId(String eventId) {
        if (eventId != null) {
            return eventRepository.findById(eventId).get().getBehaviorFields().stream().filter(behaviorField -> behaviorField.getAggFlag() == 0).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<BehaviorField> findMeasureInHitByChannelId(String channelId) {
        return behaviorFieldRepository.findMeasureInHitByChannelId(channelId);
    }

    @Override
    public List<BehaviorField> findDimensionInHitByChannelId(String channelId) {
        return behaviorFieldRepository.findDimensionInHitByChannelId(channelId);
    }

    @Override
    public List<BehaviorField> findMeasureForFunnel(String channelId) {
        return behaviorFieldRepository.findMeasureForFunnel(channelId);
    }
}
