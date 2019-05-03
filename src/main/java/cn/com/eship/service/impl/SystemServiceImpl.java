package cn.com.eship.service.impl;

import cn.com.eship.context.RespStatus;
import cn.com.eship.models.Channel;
import cn.com.eship.models.SysDict;
import cn.com.eship.models.User;
import cn.com.eship.models.vo.RespModel;
import cn.com.eship.repository.ChannelRepository;
import cn.com.eship.repository.UserRepository;
import cn.com.eship.repository.specifications.ChannelSpecification;
import cn.com.eship.repository.specifications.SysDictRepository;
import cn.com.eship.repository.specifications.SysDictlSpecification;
import cn.com.eship.service.SystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@EnableTransactionManagement
@Service
public class SystemServiceImpl implements SystemService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SysDictRepository sysDictRepository;
	@Autowired
	private ChannelRepository channelRepository;

	@Override
	public User validatLogin(User user) {
		if (user != null) {
			return userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
		}
		return null;
	}

	@Override
	public List<SysDict> findSysDictList(SysDict sysDict) {
		return sysDictRepository.findAll(new SysDictlSpecification(sysDict));
	}

	@Override
	public SysDict findSysDictById(String dictId) {
		SysDict sysDict = sysDictRepository.findById(dictId).get();
		return sysDict;
	}

	@Override
	public void saveSysDict(SysDict sysDict) {
		sysDictRepository.save(sysDict);
	}

	@Transactional
	@Override
	public void deleteSysDict(String dictId) {
		sysDictRepository.deleteById(dictId);

	}

	@Transactional
	@Override
	public void editSysDict(SysDict sysDict) {
		SysDict sysDictEntity = sysDictRepository.findById(sysDict.getId()).get();
		BeanUtils.copyProperties(sysDict, sysDictEntity);
		sysDictRepository.save(sysDictEntity);
	}

	@Override
	public List<Channel> findChannelList(Channel channel) {
		ChannelSpecification channelSpecification = new ChannelSpecification(channel);
		return channelRepository.findAll(channelSpecification);
	}

	@Override
	public void saveChannel(Channel channel) {
		channelRepository.save(channel);
	}

	@Override
	public Channel findChannelById(String channelId) {
		Channel channel = channelRepository.findById(channelId).get();
		return channel;
	}

	@Transactional
	@Override
	public void editChannel(Channel channel) {
		Channel channelEntity = channelRepository.findById(channel.getId()).get();
		BeanUtils.copyProperties(channel, channelEntity);
		channelRepository.save(channelEntity);
	}

	@Transactional
	@Override
	public RespModel deleteChannel(String channelId) {
		RespModel respModel = new RespModel(RespStatus.SUCCESSFUl, "", RespStatus.SUCCESSFUl);
		try {
			channelRepository.deleteById(channelId);
		} catch (Exception e) {
		}
		return null;
	}
}
