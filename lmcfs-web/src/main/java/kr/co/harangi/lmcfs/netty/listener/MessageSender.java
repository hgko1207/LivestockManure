package kr.co.harangi.lmcfs.netty.listener;

import java.util.concurrent.Future;

import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;

public interface MessageSender {

	boolean isConnected();
	
	boolean sendSyncMessage(UsnOutgoingMessage packet);
	
	Future<Boolean> sendAsyncMessage(UsnOutgoingMessage packet);
	
	void forceClose();
}
