package fr.Knux14.ForgeAuth.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class Packet250CustomPayload implements IMessage {

    public int length;
    public byte data[];

    public Packet250CustomPayload() { }

    public Packet250CustomPayload(byte[] message)
    {
        this.length = message.length;
        this.data = message;
    }

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
