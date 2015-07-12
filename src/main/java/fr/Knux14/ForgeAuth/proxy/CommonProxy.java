package fr.Knux14.ForgeAuth.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import fr.Knux14.ForgeAuth.Auth;
import fr.Knux14.ForgeAuth.Vars;
import fr.Knux14.ForgeAuth.event.CancelledEvents;
import fr.Knux14.ForgeAuth.event.onPlayerJoin;
import fr.Knux14.ForgeAuth.network.Packet250CustomPayload;
import fr.Knux14.ForgeAuth.network.server.CustomPayloadServerHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void registerRenderers()
	{
		Auth.print("Registering proxies");
//		Auth.instance.modEnabled = !Minecraft.getMinecraft().getIntegratedServer().isServerInOnlineMode();
		Auth.print("Mod " + (Vars.modEnabled ? "enabled" : "disabled") + " because onlinemode = " + !Vars.modEnabled);
		FMLCommonHandler.instance().bus().register(new onPlayerJoin());
		MinecraftForge.EVENT_BUS.register(new CancelledEvents());
        Auth.network.registerMessage(CustomPayloadServerHandler.class, Packet250CustomPayload.class, 0, Side.SERVER);
	}

}