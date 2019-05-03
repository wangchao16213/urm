package cn.com.eship.repository.specifications;

import cn.com.eship.models.Channel;
import cn.com.eship.models.Event;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ChannelSpecification implements Specification<Channel> {
    private Channel channel;

    public ChannelSpecification(Channel channel) {
        this.channel = channel;
    }


    @Override
    public Predicate toPredicate(Root<Channel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (channel != null) {
            List<Predicate> predicates = new ArrayList<>();
            Path<Integer> id = root.get("id");
            Path<String> channelName = root.get("channelName");
            if (StringUtils.isNotBlank(channel.getChannelName())) {
                predicates.add(criteriaBuilder.like(channelName, "%" + channel.getChannelName() + "%"));
            }
            if (channel.getId() != null) {
                predicates.add(criteriaBuilder.equal(id, channel.getId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        return null;

    }
}