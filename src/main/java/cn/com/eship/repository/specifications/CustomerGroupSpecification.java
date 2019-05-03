package cn.com.eship.repository.specifications;

import cn.com.eship.models.Channel;
import cn.com.eship.models.CustomerGroup;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerGroupSpecification implements Specification<CustomerGroup> {
    private CustomerGroup customerGroup;

    public CustomerGroupSpecification(CustomerGroup customerGroup) {
        this.customerGroup = customerGroup;
    }


    @Override
    public Predicate toPredicate(Root<CustomerGroup> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (customerGroup != null) {
            List<Predicate> predicates = new ArrayList<>();
            Path<Integer> channelId = root.join("channel").get("id");
            if (customerGroup.getChannelId() != null) {
                predicates.add(criteriaBuilder.equal(channelId, customerGroup.getChannelId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        return null;

    }
}