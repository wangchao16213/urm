package cn.com.eship.repository.specifications;

import cn.com.eship.models.CustomerField;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFieldSpecification implements Specification<CustomerField> {
    private CustomerField customerField;

    public CustomerFieldSpecification(CustomerField customerField) {
        this.customerField = customerField;
    }


    @Override
    public Predicate toPredicate(Root<CustomerField> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (customerField != null) {
            List<Predicate> predicates = new ArrayList<>();
            Path<Integer> customerTemplateId = root.join("customerTemplate").get("id");
            if (customerField.getCustomerTemplateId() != null) {
                predicates.add(criteriaBuilder.equal(customerTemplateId, customerField.getCustomerTemplateId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        return null;

    }
}