package com.revengale.powerdefense.EventHandlers;

import com.revengale.powerdefense.PowerDefense;
import com.revengale.powerdefense.entities.particles.ParticleUtils;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientRegisterHandler {
	
	public static final ClientRegisterHandler instance = new ClientRegisterHandler();
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerTextures(TextureStitchEvent.Pre event) {
		ParticleUtils.SPRITE_MUZZLE_FLASH = event.getMap().registerSprite(new ResourceLocation(PowerDefense.MODID + ":" + "particles/muzzleflash"));		
	}
}
