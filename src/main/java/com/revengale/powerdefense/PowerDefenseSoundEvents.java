package com.revengale.powerdefense;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PowerDefenseSoundEvents {
	public static SoundEvent turret_gun;

	/**
	 * Register the {@link SoundEvent}s.
	 */
	public static void registerSounds() {
		System.out.println("Registering...");
		turret_gun = registerSound("turret.gun");
		System.out.println("Done...");
	}

	/**
	 * Register a {@link SoundEvent}.
	 *
	 * @param soundName The SoundEvent's name without the testmod3 prefix
	 * @return The SoundEvent
	 */
	private static SoundEvent registerSound(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(PowerDefense.MODID, soundName);
		return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
	}
}