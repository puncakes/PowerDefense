package com.revengale.powerdefense.blocks;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class TurretBlockTileEntity extends TileEntity implements ITickable {

	private int ticksSinceLastChoice = 21;
    private ItemStack stack;

    public ItemStack getStack() {
        return stack;
    }
    public EntityLivingBase target = null;
    public float BodyAngle = 0f;

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

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		if(ticksSinceLastChoice > 20) {
			target = null;
			List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getRenderBoundingBox().expand(10, 10, 10));
			
			double closest = Double.MAX_VALUE;
			for (int i = 0; i < entities.size(); i++) {
				EntityLivingBase elb = entities.get(i);
				double dist = this.getPos().distanceSq(elb.posX, elb.posY, elb.posZ);
				if(dist < closest) {
					target = elb;
					closest = dist;
				}
			}
			ticksSinceLastChoice = 0;
		}
		
		if(target != null) {
			//center of block
			Vec3d center = new Vec3d(this.getPos().getX(),this.getPos().getY(),this.getPos().getZ());
			center = center.add(new Vec3d(0.5, 0.5, 0.5));
			
			Vec3d dir = center.subtract(new Vec3d(target.posX, target.posY, target.posZ));
			dir = dir.normalize();
			
			double yaw = Math.atan2(dir.xCoord, dir.zCoord);
			BodyAngle = (float) (yaw * 180.0 / Math.PI) + 90f;
			ticksSinceLastChoice++;
		}
		
	}
}
