package cn.com.eship.repository.specifications;

import cn.com.eship.models.Dashboard;
import cn.com.eship.models.DashboardPanel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardSpecification implements Specification<Dashboard> {
    private Dashboard dashboard;

    public DashboardSpecification(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public Predicate toPredicate(Root<Dashboard> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (dashboard != null) {
            List<Predicate> predicates = new ArrayList<>();
            Path<Integer> channelId = root.join("channel").get("id");
            Path<String> dashboardName = root.get("dashboardName");
            if (dashboard.getChannelId() != null) {
                predicates.add(criteriaBuilder.equal(channelId, dashboard.getChannelId()));
            }

            if (StringUtils.isNotBlank(dashboard.getDashboardName())) {
                predicates.add(criteriaBuilder.like(dashboardName, "%" + dashboard.getDashboardName() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        return null;

    }
}