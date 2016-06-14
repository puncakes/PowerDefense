package com.revengale.powerdefense.items;

import com.revengale.powerdefense.PowerDefense;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class Items {

	public static TurretBlock turretBlock;
	
	public static TurretItem turretItem;
	public static final void init() {
		//turret
		turretItem = new TurretItem();
		turretItem.init();
		turretBlock = new TurretBlock(Material.rock);
		turretBlock.init();
		
	}
	
	public static final void setupClients() {
		turretItem.setupRenderer();
		turretBlock.initClient();
		
		
	}
}
