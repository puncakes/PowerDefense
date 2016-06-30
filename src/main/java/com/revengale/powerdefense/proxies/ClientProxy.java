 package com.revengale.powerdefense.proxies;

import com.revengale.powerdefense.PowerDefense;
import com.revengale.powerdefense.EventHandlers.ClientRegisterHandler;
import com.revengale.powerdefense.EventHandlers.TurretHandlerClient;
import com.revengale.powerdefense.items.Items;

import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		// TODO Auto-generated method stub
		super.preInit(e);
		OBJLoader.INSTANCE.addDomain(PowerDefense.MODID);
		MinecraftForge.EVENT_BUS.register(ClientRegisterHandler.instance);
		MinecraftForge.EVENT_BUS.register(TurretHandlerClient.instance);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		// TODO Auto-generated method stub
		super.init(e);
		OBJLoader.INSTANCE.addDomain(PowerDefense.MODID);
		Items.setupClients();
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		// TODO Auto-generated method stub
		super.postInit(e);
	}

}
