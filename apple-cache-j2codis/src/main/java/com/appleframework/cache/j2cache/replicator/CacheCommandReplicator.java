package com.appleframework.cache.j2cache.replicator;

import org.apache.log4j.Logger;
import org.jgroups.JChannel;
import org.jgroups.Message;

public class CacheCommandReplicator {
	
	private static Logger logger = Logger.getLogger(CacheCommandReplicator.class);
	
	private String name = "J2_CACHE_MANAGER";

	private JChannel channel;	
	
	public void setName(String name) {
		this.name = name;
	}

	public void init() {
		try {
			channel = new JChannel();
			channel.connect(name);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void destroy() {
		channel.close();
	}
	
	public void replicate(Command command) {
		try {
			/**
			 * ������ָ��Channelʹ�õ�Э��ջ������ǿյģ���ʹ��Ĭ�ϵ�Э��ջ��
			 * λ��JGroups�����udp.xml������������һ����ð�ŷָ����ַ����� ����һ��XML�ļ�����XML�ļ��ﶨ��Э��ջ��
			 */
			logger.warn("����ͬ������");
			// ����һ��ͨ��
			
			// �����¼�
			// �����Message�ĵ�һ�������Ƿ��Ͷ˵�ַ
			// �ڶ����ǽ��ն˵�ַ
			// �������Ƿ��͵��ַ���
			// ����μ�jgroup send API
			Message msg = new Message(null, null, command);
			// ����
			channel.send(msg);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
