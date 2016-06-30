package com.revengale.powerdefense.EventHandlers;

import com.revengale.powerdefense.PowerDefenseSoundEvents;
import com.revengale.powerdefense.blocks.TurretBlockTileEntity;
import com.revengale.powerdefense.entities.particles.ParticleMuzzleFlash;
import com.revengale.powerdefense.entities.projectiles.EntityCustomArrow;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurretHandlerServer {
	
	public static final TurretHandlerServer instance = new TurretHandlerServer();
	
	@SubscribeEvent
    public void onEntitySpawn (EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof EntityCustomArrow ) {
			EntityCustomArrow arrow = (EntityCustomArrow) event.getEntity();
			
			//until i figure out custom entity spawning comment this out
			BlockPos blockFrom = arrow.getBlockFrom();
			World world = arrow.worldObj;
			//boolean test = world.isRemote;
				
			//whhaaat?
			if(world == null || blockFrom == null)
				return;
			
			TurretBlockTileEntity te = (TurretBlockTileEntity) world.getTileEntity(blockFrom);
			
			if(te == null)
				return;
    		te.barrelToggle *= -1;
    	}
    }
}
