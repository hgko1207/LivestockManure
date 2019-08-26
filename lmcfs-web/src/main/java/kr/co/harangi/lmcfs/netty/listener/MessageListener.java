package kr.co.harangi.lmcfs.netty.listener;

import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;

public interface MessageListener {

	void connectionStateChanged(boolean isConnected);

	void messageReceived(UsnIncomingMessage message);
}
