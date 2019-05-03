package cn.com.eship;

import cn.com.eship.context.ContextSetting;
import cn.com.eship.models.Behavior;
import cn.com.eship.models.CustomerGroup;
import cn.com.eship.models.Event;
import cn.com.eship.models.enums.GroupEngineType;
import cn.com.eship.models.enums.GroupType;
import cn.com.eship.repository.*;
import cn.com.eship.tasks.CustomerGroupTask;
import cn.com.eship.utils.SqlConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrmApplicationTests {
    @Autowired
    private CustomerGroupRepository customerGroupRepository;
    @Autowired
    private CustomerGroupTask customerGroupTask;

    @Test
    public void contextLoads2() throws Exception {
        CustomerGroup customerGroup = customerGroupRepository.findById("1519fd06e75048c0bd3a82405bc543f0").get();
        customerGroupTask.executeCustomerGroupTask(customerGroup);
    }

}
