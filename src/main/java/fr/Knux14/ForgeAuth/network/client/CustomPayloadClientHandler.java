package fr.Knux14.ForgeAuth.network.client;

import fr.Knux14.ForgeAuth.client.gui.GuiLogin;
import fr.Knux14.ForgeAuth.network.Packet250CustomPayload;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class CustomPayloadClientHandler implements IMessageHandler<Packet250CustomPayload, IMessage> {

    @Override
    public IMessage onMessage(Packet250CustomPayload message, MessageContext ctx)
    {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(message.data));
        try {
            String type = inputStream.readUTF();
            GuiLogin gl = new GuiLogin(type);
            Minecraft.getMinecraft().displayGuiScreen(gl);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
