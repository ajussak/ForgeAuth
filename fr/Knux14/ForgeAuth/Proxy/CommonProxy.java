package fr.Knux14.ForgeAuth.Proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.Knux14.ForgeAuth.Auth;
import fr.Knux14.ForgeAuth.Vars;
import fr.Knux14.ForgeAuth.Events.CancelledEvents;
import fr.Knux14.ForgeAuth.Events.onPlayerJoin;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;

public class CommonProxy {

	public void registerRenderers() {
		Auth.print("Registering proxies");
//		Auth.instance.modEnabled = !Minecraft.getMinecraft().getIntegratedServer().isServerInOnlineMode();
		Auth.print("Mod " + (Vars.modEnabled ? "enabled" : "disabled") + " because onlinemode = " + !Vars.modEnabled);
		GameRegistry.registerPlayerTracker(new onPlayerJoin());
		MinecraftForge.EVENT_BUS.register(new CancelledEvents());
	}
}