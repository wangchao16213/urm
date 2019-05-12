package cn.com.eship.repository;

import cn.com.eship.models.Event;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, String>, JpaSpecificationExecutor<Event> {
    @Query("select event from Event event where event.channel.id = ?1")
    public List<Event> findAllByChannelId(String channelId);
}
