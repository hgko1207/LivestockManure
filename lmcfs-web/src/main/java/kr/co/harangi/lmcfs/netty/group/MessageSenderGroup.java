package kr.co.harangi.lmcfs.netty.group;

import kr.co.harangi.lmcfs.netty.listener.MessageSender;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;

/**
 * Netty를 통해서 접속된 {@link MessageSender}의 리스트를
 * 관리하고, 메시지를 전송할 때 사용한다.
 * 
 * @author hgko
 */
public interface MessageSenderGroup {

    void addMessageSender(String key, MessageSender messageSender);

    void removeMesssageSender(String key);
    
    void removeAllMessageSenders();

    int getMessageSenderSize();

	void write(String key, UsnOutgoingMessage msg);
	
	void writeAsync(String key, UsnOutgoingMessage msg);

    void writeToAll(UsnOutgoingMessage msg);
}
