package fr.wascar.ForgeAuth.proxy;

import cpw.mods.fml.relauncher.Side;
import fr.wascar.ForgeAuth.ForgeAuth;
import fr.wascar.ForgeAuth.network.Packet250CustomPayload;
import fr.wascar.ForgeAuth.network.client.CustomPayloadClientHandler;

public class ClientProxy extends CommonProxy {

    public void registerRenderers()
   {
       ForgeAuth.network.registerMessage(CustomPayloadClientHandler.class, Packet250CustomPayload.class, 0, Side.CLIENT);
   }

}