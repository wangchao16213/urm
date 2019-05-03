package cn.com.eship.repository;

import cn.com.eship.models.CustomerGroup;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CustomerGroupRepository extends CrudRepository<CustomerGroup, String>, JpaSpecificationExecutor<CustomerGroup> {
}
