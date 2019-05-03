package cn.com.eship.repository.specifications;

import cn.com.eship.models.SysDict;
import cn.com.eship.models.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface SysDictRepository extends CrudRepository<SysDict, String>, JpaSpecificationExecutor<SysDict> {
}
