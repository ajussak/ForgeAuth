package fr.Knux14.ForgeAuth.proxy;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.relauncher.Side;
import fr.Knux14.ForgeAuth.Auth;
import fr.Knux14.ForgeAuth.network.Packet250CustomPayload;
import fr.Knux14.ForgeAuth.network.client.CustomPayloadClientHandler;

public class ClientProxy extends CommonProxy {

    public void registerRenderers()
   {
       Auth.network.registerMessage(CustomPayloadClientHandler.class, Packet250CustomPayload.class, 0, Side.CLIENT);
   }

}