package com.revengale.powerdefense.items;

import com.revengale.powerdefense.blocks.itempedestal.PedestalBlock;

public final class Items {

	public static TurretBlock turretBlock;
	
	public static TurretItem turretItem;
	public static final void init() {
		//turret
		//TurretItem.init();
		TurretBlock.init();
		PedestalBlock.init();
		
		
	}
	
	public static final void setupClients() {
		//TurretItem.setupRenderer();
		TurretBlock.initClient();
		PedestalBlock.initModel();
		
		
	}
}
