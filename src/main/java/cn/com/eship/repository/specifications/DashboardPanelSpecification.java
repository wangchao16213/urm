package cn.com.eship.repository.specifications;

import cn.com.eship.models.DashboardPanel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardPanelSpecification implements Specification<DashboardPanel> {
    private DashboardPanel dashboardPanel;

    public DashboardPanelSpecification(DashboardPanel dashboardPanel) {
        this.dashboardPanel = dashboardPanel;
    }

    @Override
    public Predicate toPredicate(Root<DashboardPanel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (dashboardPanel != null) {
            List<Predicate> predicates = new ArrayList<>();
            Path<String> dashboardId = root.join("dashboard").get("id");
            if (StringUtils.isNotBlank(dashboardPanel.getDashboardId())) {
                predicates.add(criteriaBuilder.equal(dashboardId, dashboardPanel.getDashboardId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        return null;

    }
}