package cn.com.eship.repository;

import cn.com.eship.models.Event;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, String>, JpaSpecificationExecutor<Event> {
}
