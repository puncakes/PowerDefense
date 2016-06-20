package com.revengale.powerdefense.blocks;

import java.util.List;
import java.util.Random;

import com.revengale.powerdefense.PowerDefenseSoundEvents;
import com.revengale.powerdefense.PowerDefenseUtils;
import com.revengale.powerdefense.items.projectiles.EntityCustomArrow;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TurretBlockTileEntity extends TileEntity implements ITickable {

	private int ticksSinceLastChoice = 21;
    private ItemStack stack;

    public ItemStack getStack() {
        return stack;
    }
    public EntityLivingBase target = null;
    
    public float curBodyAngle = 0f;
    public float curGunAngle = 0f;
    
    public float curBodyAngleRad = 0f;
    public float curGunAngleRad = 0f;
    
    public float targetBodyAngle = 0f;
    public float targetGunAngle = 0f;
    
    public float targetBodyAngleRad = 0f;
    public float targetGunAngleRad = 0f;
    
    public Vec3d targetDir = Vec3d.ZERO;
    public Vec3d currentDir = Vec3d.ZERO;
    
    public float rotationDelta = 3.0f;
    
    public float leftGunScale = 1.0f;
    public float rightGunScale = 1.0f;
    private float recoil = 0.8f;
    private float recoilRecovery = 0.05f;
    
    private Random rand;
    
    //toggle projectile coming out of each barrel 1/-1
    private double barrelToggle = 1.0;
    
    boolean justLostTarget = false;
    private Vec3d center = null;
    
    public TurretBlockTileEntity() {
    	super();
    	MinecraftForge.EVENT_BUS.register(this);
    	rand = new Random();    	
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
        markDirty();
        if (worldObj != null) {
            IBlockState state = worldObj.getBlockState(getPos());
            worldObj.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        // Prepare a packet for syncing our TE to the client. Since we only have to sync the stack
        // and that's all we have we just write our entire NBT here. If you have a complex
        // tile entity that doesn't need to have all information on the client you can write
        // a more optimal NBT here.
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        // Here we get the packet from the server and read it into our client side tile entity
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("item")) {
            stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("item"));
        } else {
            stack = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (stack != null) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            stack.writeToNBT(tagCompound);
            compound.setTag("item", tagCompound);
        }
    }
    
    @SubscribeEvent
    public void onEntitySpawn (EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof EntityCustomArrow ) {
			EntityCustomArrow arrow = (EntityCustomArrow) event.getEntity();
	    	if(this.getPos().equals(arrow.getBlockFrom())) {
	    		barrelToggle *= -1;
	    		if(barrelToggle < 0) {
			   		leftGunScale = recoil;
			   	} else {
			   		rightGunScale = recoil;
			   	}
	    		worldObj.playSound((EntityPlayer)null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), PowerDefenseSoundEvents.turret_gun, SoundCategory.NEUTRAL, 0.25f, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	    	}
	    }
	}
    
    private boolean onTarget() {
    	float deg = (float) Math.atan2(Math.cos(targetBodyAngleRad-curBodyAngleRad), Math.sin(targetBodyAngleRad-curBodyAngleRad));
    	
    	if(Math.toDegrees(deg) < 15) {
    		return true;
    	}
    	return false;
    }

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		//center of block
		center = new Vec3d(this.getPos().getX(),this.getPos().getY(),this.getPos().getZ());
		center = center.add(new Vec3d(0.5, 0.35, 0.5));
		
		if(ticksSinceLastChoice > 20) {
			target = null;
			List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.getPos()).expand(20, 8, 20));
			
			double closest = Double.MAX_VALUE;
			for (int i = 0; i < entities.size(); i++) {
				EntityLivingBase elb = entities.get(i);
				if(elb instanceof EntityPlayer) {
					continue;
				}
				double dist = this.getPos().distanceSq(elb.posX, elb.posY, elb.posZ);
				if(dist < closest) {
					//if in line of site
					if(PowerDefenseUtils.rayTraceBlocks(worldObj, new Vec3d(center.xCoord, center.yCoord, center.zCoord), new Vec3d(elb.posX, elb.posY+elb.getEyeHeight(), elb.posZ), this.getPos()) == null) {
						target = elb;
						closest = dist;
					}
				}
			}
			ticksSinceLastChoice = 0;
		}
		
		if(target != null) {		
			
			double xOff = 0;  //(tBB.maxX - tBB.minX) / 2.0;
			double yOff = 0.5;//(tBB.maxY - tBB.minY) / 2.0;
			double zOff = 0;  //(tBB.maxZ - tBB.minZ) / 2.0;
			Vec3d dir = center.subtract(new Vec3d(target.posX+xOff, target.posY+yOff, target.posZ+zOff));
			targetDir = dir.normalize();
			
			double pitch = Math.asin(targetDir.yCoord);
			double yaw = Math.atan2(targetDir.xCoord, targetDir.zCoord);
			targetBodyAngleRad = (float) yaw;
		    targetGunAngleRad = (float) pitch;
			targetBodyAngle = (float) (yaw * 180.0 / Math.PI) - 90f;
			targetGunAngle = (float) (pitch * 180.0 / Math.PI);
			
			//float cba = (curBodyAngle - 180f) % 180;
			//float tba = (targetBodyAngle - 180f) % 180;
			
			boolean right = (curBodyAngle-targetBodyAngle+360)%360>180;
			
			//only shoot when on target
			if(onTarget() && (ticksSinceLastChoice == 0 || ticksSinceLastChoice == 5 || ticksSinceLastChoice == 10 || ticksSinceLastChoice == 15)) {
				//worldObj.playAuxSFXAtEntity(this, "random.bow", 0.5F, 0.4F);
				double xzLen = Math.cos(Math.toRadians(curGunAngle));
				double z = xzLen * Math.sin(Math.toRadians(curBodyAngle));
				double y = -Math.sin(Math.toRadians(curGunAngle));
				double x = -xzLen * Math.cos(Math.toRadians(-curBodyAngle));
			   
				//for use in client rendering (scaling for recoil)
				currentDir = new Vec3d(x,y,z);				
				
			   	if (!worldObj.isRemote)
			   	{
			   		EntityCustomArrow arrow = new EntityCustomArrow(worldObj);
			   		//add direction vector and perpendicular vector to place projectiles at the barrel
			   		arrow.setPosition(center.xCoord + x + (0.2 * barrelToggle * z), center.yCoord + y, center.zCoord + z - (0.2 * barrelToggle * x));
			   		arrow.setThrowableHeading(x, y, z, 2f, 2f);
			   		
			   		//recoil animation starts when the entity is spawned client side!
			   		//set some metadata to know which turret to animate client side
			   		arrow.setBlockFrom(this.getPos());
			   		worldObj.spawnEntityInWorld(arrow);
			   		
			   	}			   	
			   
			}
			
			if(right) {
				curBodyAngle = Math.min(curBodyAngle + rotationDelta, targetBodyAngle);
			} else {
				curBodyAngle = Math.max(curBodyAngle - rotationDelta, targetBodyAngle);
			}
			
			if(curGunAngle < targetGunAngle) {
				curGunAngle = Math.min(curGunAngle + rotationDelta, targetGunAngle);
			} else {
				curGunAngle = Math.max(curGunAngle - rotationDelta, targetGunAngle);
			}
			
			curBodyAngleRad = (float) Math.toRadians(curBodyAngle);
			curGunAngleRad = (float) Math.toRadians(curGunAngle);
		}

		leftGunScale = Math.min(leftGunScale + recoilRecovery, 1.0f);
		rightGunScale = Math.min(rightGunScale + recoilRecovery, 1.0f);
		ticksSinceLastChoice++;
		
	}
}
