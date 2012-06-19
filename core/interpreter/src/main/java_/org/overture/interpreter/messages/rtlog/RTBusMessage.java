package org.overture.interpreter.messages.rtlog;

import org.overture.interpreter.scheduler.MessagePacket;


public abstract class RTBusMessage extends RTMessage
{
	protected MessagePacket message;

	public RTBusMessage(MessagePacket message)
	{
		this.message = message;
	}
	
}