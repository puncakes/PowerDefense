package com.revengale.powerdefense.items;

import com.revengale.powerdefense.PowerDefense;
import com.revengale.powerdefense.blocks.TurretBlock;
import com.revengale.powerdefense.blocks.itempedestal.PedestalBlock;
import com.revengale.powerdefense.items.projectiles.EntityCustomArrow;

import net.minecraftforge.fml.common.registry.EntityRegistry;

public final class Items {

	public static TurretBlock turretBlock;
	
	public static TurretItem turretItem;
	public static final void init() {
		int entityId = 0;
		//turret
		TurretItem.init();
		TurretBlock.init();
		PedestalBlock.init();
		
		//Entities
		EntityRegistry.registerModEntity(EntityCustomArrow.class, EntityCustomArrow.name, entityId++, PowerDefense.instance, 80, 3, false);
		
		
	}
	
	public static final void setupClients() {
		TurretItem.setupRenderer();
		TurretBlock.initClient();
		PedestalBlock.initModel();
		
		//Entity Renderer setup
		EntityCustomArrow.setupRenderer();
		
		
	}
}
