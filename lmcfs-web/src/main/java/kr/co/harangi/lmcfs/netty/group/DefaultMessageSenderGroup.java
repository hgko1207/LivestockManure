package kr.co.harangi.lmcfs.netty.group;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kr.co.harangi.lmcfs.netty.listener.MessageSender;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultMessageSenderGroup implements MessageSenderGroup {
    
    private ConcurrentMap<String, MessageSender> allMessageSenders = new ConcurrentHashMap<String, MessageSender>();
    
	@Override
	public void addMessageSender(String key, MessageSender messageSender) {
		if (key == null) {
			return;
		}
		
		allMessageSenders.put(key, messageSender);
        log.info("addChannel {}", allMessageSenders);
	}

	@Override
	public void removeMesssageSender(String key) {
		if (key == null) {
			return;
		}
		
		allMessageSenders.remove(key);
        log.info("removeChannel {}", allMessageSenders);
	}

	@Override
	public void removeAllMessageSenders() {
		for (MessageSender each : allMessageSenders.values()) {
			each.forceClose();
    	}
		
		allMessageSenders.clear();
	}

	@Override
	public int getMessageSenderSize() {
		return allMessageSenders.size();
	}

	@Override
	public void write(String key, UsnOutgoingMessage msg) {
		MessageSender messageSender = allMessageSenders.get(key);
        if (messageSender != null) {
        	log.debug("{} Send Message : {}", key, msg);
            messageSender.sendSyncMessage(msg);
        }
	}

	@Override
	public void writeAsync(String key, UsnOutgoingMessage msg) {
		MessageSender messageSender = allMessageSenders.get(key);
        if (messageSender != null) {
        	log.debug("{} Send Message : {}", key, msg);
            messageSender.sendAsyncMessage(msg);
        } else {
        	log.warn("Not Exist ID : {}", key);
        }
	}
	
	@Override
	public void writeToAll(UsnOutgoingMessage msg) {
        for (String key : allMessageSenders.keySet()) {
        	log.debug("{} Send Message : {}", key, msg);
            allMessageSenders.get(key).sendSyncMessage(msg);
        }
	}
}
