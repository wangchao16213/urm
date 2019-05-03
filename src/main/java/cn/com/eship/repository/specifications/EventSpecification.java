package cn.com.eship.repository.specifications;

import cn.com.eship.models.Event;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification implements Specification<Event> {
    private Event event;

    public EventSpecification(Event event) {
        this.event = event;
    }


    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (event != null) {
            List<Predicate> predicates = new ArrayList<>();
            Path<Integer> channelId = root.join("channel").get("id");
            Path<String> eventLable = root.get("eventLable");
            Path<String> eventName = root.get("eventName");
            if (event.getChannelId() != null) {
                predicates.add(criteriaBuilder.equal(channelId, event.getChannelId()));
            }
            if (StringUtils.isNotBlank(event.getEventLable())) {
                predicates.add(criteriaBuilder.like(eventLable, "%" + event.getEventLable() + "%"));
            }
            if (StringUtils.isNotBlank(event.getEventName())) {
                predicates.add(criteriaBuilder.like(eventName, "%" + event.getEventName() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        return null;

    }
}