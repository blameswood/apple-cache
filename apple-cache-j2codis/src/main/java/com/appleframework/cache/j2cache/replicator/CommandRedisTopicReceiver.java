package com.appleframework.cache.j2cache.replicator;

import org.apache.log4j.Logger;

import com.appleframework.cache.core.replicator.Command;
import com.appleframework.cache.core.replicator.CommandReceiver;
import com.appleframework.cache.j2cache.utils.SerializeUtility;
import com.appleframework.cache.redis.factory.PoolFactory;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class CommandRedisTopicReceiver extends BinaryJedisPubSub implements CommandReceiver {
	
	protected final static Logger logger = Logger.getLogger(CommandRedisTopicReceiver.class);
	
	private String name = "J2_CACHE_MANAGER";
	
	private PoolFactory poolFactory;
		
	private CommandRedisTopicProcesser commandProcesser;
	
	private Thread threadSubscribe;
		
	public void init() {
		threadSubscribe = new Thread(new Runnable() {
			@Override
			public void run() {
				JedisPool jedisPool = poolFactory.getWritePool();
				Jedis jedis = jedisPool.getResource();
				try {
					logger.warn("The subscribe channel is " + name);
					jedis.subscribe(commandProcesser, name.getBytes());
				} catch (Exception e) {
					logger.error("Subscribing failed.", e);
				}
			}
		});
		threadSubscribe.start();
	}

	@Override
	public void onMessage(Command command) {
		commandProcesser.onProcess(command);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	// ȡ�ö��ĵ���Ϣ��Ĵ���   
	public void onMessage(byte[] channel, byte[] message) {
		Object omessage = SerializeUtility.unserialize(message);
		Object ochannel = SerializeUtility.unserialize(channel);
		try {
	        logger.info("ȡ�ö��ĵ���Ϣ��Ĵ��� : " + ochannel + "=" + omessage.toString());  
			if (omessage instanceof Command) {
				Command command = (Command)omessage;
				this.onMessage(command);
			} else if (omessage instanceof String) {
				logger.warn(omessage.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
    // ��ʼ������ʱ��Ĵ���    
    public void onSubscribe(byte[] channel, int subscribedChannels) {  
        logger.info("��ʼ������ʱ��Ĵ��� : " + channel + "=" + subscribedChannels);  
    }  
  
	@Override
    // ȡ������ʱ��Ĵ���    
    public void onUnsubscribe(byte[] channel, int subscribedChannels) {  
        logger.info("ȡ������ʱ��Ĵ��� : " + channel + "=" + subscribedChannels);  
    }  
  
	@Override
    // ��ʼ�������ʽ�ķ�ʽ����ʱ��Ĵ���    
    public void onPSubscribe(byte[] pattern, int subscribedChannels) {  
        logger.info("��ʼ�������ʽ�ķ�ʽ����ʱ��Ĵ��� : " + pattern + "=" + subscribedChannels);  
    }  
  
	@Override
    // ȡ�������ʽ�ķ�ʽ����ʱ��Ĵ���    
    public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {  
        logger.info(" ȡ�������ʽ�ķ�ʽ����ʱ��Ĵ��� : " + pattern + "=" + subscribedChannels);  
    }  
  
	@Override
    // ȡ�ð����ʽ�ķ�ʽ���ĵ���Ϣ��Ĵ���    
    public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {  
        logger.info("ȡ�ð����ʽ�ķ�ʽ���ĵ���Ϣ��Ĵ��� :" + pattern + "=" + channel + "=" + message);  
    }  


	public void setPoolFactory(PoolFactory poolFactory) {
		this.poolFactory = poolFactory;
	}

	public void setCommandProcesser(CommandRedisTopicProcesser commandProcesser) {
		this.commandProcesser = commandProcesser;
	}
}
