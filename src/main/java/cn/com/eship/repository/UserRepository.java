package cn.com.eship.repository;

import cn.com.eship.models.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>, JpaSpecificationExecutor<User> {
    public User findByUserNameAndPassword(String username, String password);
}
