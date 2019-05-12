package cn.com.eship.repository;

import cn.com.eship.models.CustomerField;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerFieldRepository extends CrudRepository<CustomerField, String>, JpaSpecificationExecutor<CustomerField> {
    @Query("select customerField from CustomerField customerField where customerField.customerTemplate.channel.id = ?1")
    public List<CustomerField> findByChannelId(String channelId, Sort sort);

    @Query("SELECT customerField FROM CustomerField customerField where customerField.customerTemplate.channel.id = ?1 and customerField.isKey = '1'")
    public CustomerField findPrimariField(String channelId);
}
