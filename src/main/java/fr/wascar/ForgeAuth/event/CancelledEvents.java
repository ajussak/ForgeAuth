package fr.wascar.ForgeAuth.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.wascar.ForgeAuth.ForgeAuth;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class CancelledEvents {
	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent e) {
		cancel(e);
	}

	@SubscribeEvent
	public void playerHurt(LivingHurtEvent e) {
		cancel(e);
	}

	@SubscribeEvent
	public void playerAttack(AttackEntityEvent e) {
		cancel(e);
	}

	@SubscribeEvent
	public void playerInteractEntity(EntityInteractEvent e) {
		cancel(e);
	}
	
	@SubscribeEvent
	public void minecartInterract(MinecartCollisionEvent e) {
		cancel (e);
	}

	@SubscribeEvent
	public void playerFillBucket(FillBucketEvent e) {
		cancel(e);
	}

	@SubscribeEvent
	public void playerItemPickup(EntityItemPickupEvent e) {
		cancel(e);
	}

	@SubscribeEvent
	public void playerChat(ServerChatEvent e) {
		if (!(ForgeAuth.players.get(e.player)))
			e.setCanceled(true);
	}

	@SubscribeEvent
	public void playerCmd(CommandEvent e) {
		if (ForgeAuth.modEnabled) {
			if (((e.sender instanceof EntityPlayer)) && (!(ForgeAuth.players.get(e.sender))))
				e.setCanceled(true);
		}
	}

	private void cancel(EntityEvent e) {
		if (ForgeAuth.modEnabled) {
			if (((e.entity instanceof EntityPlayer)) && (!(ForgeAuth.players.get((e.entity)))))
				e.setCanceled(true);
		}
	}
}