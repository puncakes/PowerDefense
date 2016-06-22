package com.revengale.powerdefense.EventHandlers;

import com.revengale.powerdefense.PowerDefenseSoundEvents;
import com.revengale.powerdefense.blocks.TurretBlockTileEntity;
import com.revengale.powerdefense.items.projectiles.EntityCustomArrow;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TurretHandler {
	
	public static final TurretHandler instance = new TurretHandler();
	
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
    		//need to update barrel toggle server side
    		
    		//just client stuff
    		if(world.isRemote)
    		{		    		
	    		if(te.barrelToggle < 0) {
			   		te.leftGunScale = te.recoil;
			   	} else {
			   		te.rightGunScale = te.recoil;
			   	}
    		}
	    		
	    	world.playSound((EntityPlayer)null, blockFrom.getX(), blockFrom.getY(), blockFrom.getZ(), PowerDefenseSoundEvents.turret_gun, SoundCategory.NEUTRAL, 0.25f, 1.2F / (te.rand.nextFloat() * 0.2F + 0.9F));
    	}
    }
}
