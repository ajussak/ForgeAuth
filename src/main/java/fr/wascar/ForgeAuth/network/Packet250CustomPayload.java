package fr.wascar.ForgeAuth.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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
