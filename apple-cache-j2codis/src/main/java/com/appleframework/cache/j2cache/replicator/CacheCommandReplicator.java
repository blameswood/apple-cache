package com.appleframework.cache.j2cache.replicator;

import org.apache.log4j.Logger;
import org.jgroups.JChannel;
import org.jgroups.Message;

public class CacheCommandReplicator {
	
	private static Logger logger = Logger.getLogger(CacheCommandReplicator.class);
	
	public static void replicate(String regsion, Command command) {
		try {
			/**
			 * ������ָ��Channelʹ�õ�Э��ջ������ǿյģ���ʹ��Ĭ�ϵ�Э��ջ��
			 * λ��JGroups�����udp.xml������������һ����ð�ŷָ����ַ����� ����һ��XML�ļ�����XML�ļ��ﶨ��Э��ջ��
			 */
			logger.warn("����ͬ������");
			// ����һ��ͨ��
			JChannel channel = new JChannel();
			// ����һ��Ⱥ
			channel.connect(regsion);
			// �����¼�
			// �����Message�ĵ�һ�������Ƿ��Ͷ˵�ַ
			// �ڶ����ǽ��ն˵�ַ
			// �������Ƿ��͵��ַ���
			// ����μ�jgroup send API
			Message msg = new Message(null, null, command);
			// ����
			channel.send(msg);
			// �ر�ͨ��
			channel.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
