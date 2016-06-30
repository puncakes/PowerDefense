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

public class TurretHandlerClient {
	
	public static final TurretHandlerClient instance = new TurretHandlerClient();
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
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
    		
    		if(te.barrelToggle < 0) {
		   		te.leftGunScale = te.recoil;
		   	} else {
		   		te.rightGunScale = te.recoil;
		   	}
    		
    		//not sure why > works?
	    	if(te.barrelToggle > 0) {	    		
	    		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleMuzzleFlash(world, te.leftBarrelTip.xCoord, te.leftBarrelTip.yCoord, te.leftBarrelTip.zCoord, 0, 0, 0));
	    	} else {
	    		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleMuzzleFlash(world, te.rightBarrelTip.xCoord, te.rightBarrelTip.yCoord, te.rightBarrelTip.zCoord, 0, 0, 0));
	    	}		
    		//sound will be sent from server (so other players can hear it too)
	    	world.playSound((EntityPlayer)null, blockFrom.getX(), blockFrom.getY(), blockFrom.getZ(), PowerDefenseSoundEvents.turret_gun, SoundCategory.NEUTRAL, 0.25f, 1.2F / (te.rand.nextFloat() * 0.2F + 0.9F));
    	}
    }
}
