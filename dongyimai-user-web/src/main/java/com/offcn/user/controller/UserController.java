package com.offcn.user.controller;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.dongyimai.pojo.TbUser;
import com.dongyimai.user.service.UserService;

import com.dongyimai.entity.PageResult;
import com.dongyimai.entity.Result;

import javax.jms.*;


/**
 * 用户表controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination smsDestination;

	public boolean checkCode(String mobile,String code){
		//1、从redis中取得验证码
		String sysCode = (String)redisTemplate.boundHashOps("smsCode").get(mobile);
		if(code!=null && code.length()>0){
			if(code.equals(sysCode)){
				return true;
			}
		}
		return false;
	}

	@RequestMapping("/sendSysCode")
	public Result sendSysCode(final String mobile){

		try {
			//1、生成验证码
			final String sysCode = (int)(Math.random()*10000) +"";
			System.out.println("syscode:"+sysCode);
			//2、以手机号码为key 以验证码为value 存入redis 方便后期验证码验证
			redisTemplate.boundHashOps("smsCode").put(mobile,sysCode);


			//3、发送短信
			jmsTemplate.send(smsDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {

					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("mobile",mobile);
					mapMessage.setString("code",sysCode);

					return mapMessage;
				}
			});

			return new Result(true,"短信发送成功");
		} catch (JmsException e) {
			e.printStackTrace();
			return new Result(false,"短信发送失败");
		}
	}

	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String code){

		boolean flag = checkCode(user.getPhone(),code);

		if(flag){
			try {
				user.setCreated(new Date());
				user.setUpdated(new Date());
				user.setSourceType("1");
				user.setStatus("1");
				user.setPassword(DigestUtils.md5Hex(user.getPassword()));

				userService.add(user);
				return new Result(true, "增加成功");
			} catch (Exception e) {
				e.printStackTrace();
				return new Result(false, "增加失败");
			}
		}else{
			return new Result(false,"验证码错误");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		try {
			userService.update(user);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbUser findOne(Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}
	
}
