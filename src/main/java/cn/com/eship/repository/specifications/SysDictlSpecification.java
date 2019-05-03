package cn.com.eship.repository.specifications;

import cn.com.eship.models.SysDict;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SysDictlSpecification implements Specification<SysDict> {
    private SysDict sysDict;

    public SysDictlSpecification(SysDict sysDict) {
        this.sysDict = sysDict;
    }


    @Override
    public Predicate toPredicate(Root<SysDict> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (sysDict != null) {
            List<Predicate> predicates = new ArrayList<>();
            Path<String> dictKey = root.get("dictKey");
            if (StringUtils.isNotBlank(sysDict.getDictKey())) {
                predicates.add(criteriaBuilder.like(dictKey, "%" + sysDict.getDictKey() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        return null;
    }
}