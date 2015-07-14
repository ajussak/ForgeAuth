package fr.wascar.ForgeAuth.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class Packet250CustomPayload implements IMessage {

    public int length;
    public byte data[];

    @Override
    public void fromBytes(ByteBuf buf)
    {
        length = buf.readShort();
        if (length > 0 && length < 32767)
        {
            data = new byte[length];
            buf.readBytes(data);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeShort((short) length);
        if (data != null)
            buf.writeBytes(data);
    }
}
