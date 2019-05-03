package cn.com.eship.repository.specifications;

import cn.com.eship.models.Event;
import cn.com.eship.models.Funnel;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class FunnelSpecification implements Specification<Funnel> {
    private Funnel funnel;

    public FunnelSpecification(Funnel funnel) {
        this.funnel = funnel;
    }


    @Override
    public Predicate toPredicate(Root<Funnel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (funnel != null) {
            List<Predicate> predicates = new ArrayList<>();
            Path<Integer> channelId = root.join("channel").get("id");
            if (funnel.getChannelId() != null) {
                predicates.add(criteriaBuilder.equal(channelId, funnel.getChannelId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        return null;

    }
}