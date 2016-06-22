package com.revengale.powerdefense.proxies;

import com.revengale.powerdefense.PowerDefenseSoundEvents;
import com.revengale.powerdefense.EventHandlers.TurretHandler;
import com.revengale.powerdefense.items.Items;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    	Items.init();
    	PowerDefenseSoundEvents.registerSounds();
    	MinecraftForge.EVENT_BUS.register(TurretHandler.instance);
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}