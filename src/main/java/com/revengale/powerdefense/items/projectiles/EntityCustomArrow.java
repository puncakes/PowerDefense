package com.revengale.powerdefense.items.projectiles;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;
import com.revengale.powerdefense.renderer.RenderCustomArrow;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCustomArrow extends CustomProjectile implements IEntityAdditionalSpawnData
{
    private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityCustomArrow.class, DataSerializers.VARINT);
    private PotionType potion = PotionTypes.EMPTY;
    private final Set<PotionEffect> customPotionEffects = Sets.<PotionEffect>newHashSet();
    
    private BlockPos cameFrom = null;
    
    public static final String name = "customarrow";
    public static void init() {
    	
    }
    
    @SideOnly(Side.CLIENT)
    public static void setupRenderer() {
    	//RenderingRegistry.registerEntityRenderingHandler(EntityCustomArrow.class, new RenderArrow<EntityCustomArrow>(Minecraft.getMinecraft().getRenderManager()));
    	RenderingRegistry.registerEntityRenderingHandler(EntityCustomArrow.class, new RenderCustomArrow(Minecraft.getMinecraft().getRenderManager()));
    }
    
    /*public SPacketSpawnObject customSpawn(Entity entityIn, int typeIn, int dataIn)
    {
        this.entityId = entityIn.getEntityId();
        this.uniqueId = entityIn.getUniqueID();
        this.x = entityIn.posX;
        this.y = entityIn.posY;
        this.z = entityIn.posZ;
        this.pitch = MathHelper.floor_float(entityIn.rotationPitch * 256.0F / 360.0F);
        this.yaw = MathHelper.floor_float(entityIn.rotationYaw * 256.0F / 360.0F);
        this.type = typeIn;
        this.data = dataIn;
        double d0 = 3.9D;
        this.speedX = (int)(MathHelper.clamp_double(entityIn.motionX, -3.9D, 3.9D) * 8000.0D);
        this.speedY = (int)(MathHelper.clamp_double(entityIn.motionY, -3.9D, 3.9D) * 8000.0D);
        this.speedZ = (int)(MathHelper.clamp_double(entityIn.motionZ, -3.9D, 3.9D) * 8000.0D);
    }*/
    
    public void setBlockFrom(BlockPos pos) {
    	cameFrom = pos;
    }
    
    public BlockPos getBlockFrom() {
    	return cameFrom;
    }

    public EntityCustomArrow(World worldIn)
    {
        super(worldIn);
    }

    public EntityCustomArrow(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntityCustomArrow(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
    }

    public void setPotionEffect(ItemStack stack)
    {
        if (stack.getItem() == Items.TIPPED_ARROW)
        {
            this.potion = PotionUtils.getPotionTypeFromNBT(stack.getTagCompound());
            Collection<PotionEffect> collection = PotionUtils.getFullEffectsFromItem(stack);

            if (!collection.isEmpty())
            {
                for (PotionEffect potioneffect : collection)
                {
                    this.customPotionEffects.add(new PotionEffect(potioneffect));
                }
            }

            this.dataManager.set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, collection))));
        }
        else if (stack.getItem() == Items.ARROW)
        {
            this.potion = PotionTypes.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(COLOR, Integer.valueOf(0));
        }
    }

    public void addEffect(PotionEffect effect)
    {
        this.customPotionEffects.add(effect);
        this.getDataManager().set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects))));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(COLOR, Integer.valueOf(0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.worldObj.isRemote)
        {
            if (this.inGround)
            {
                if (this.field_184552_b % 5 == 0)
                {
                    this.spawnPotionParticles(1);
                }
            }
            else
            {
                this.spawnPotionParticles(2);
            }
        }
        else if (this.inGround && this.field_184552_b != 0 && !this.customPotionEffects.isEmpty() && this.field_184552_b >= 600)
        {
            this.worldObj.setEntityState(this, (byte)0);
            this.potion = PotionTypes.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(COLOR, Integer.valueOf(0));
        }
    }

    private void spawnPotionParticles(int particleCount)
    {
        int i = this.getColor();

        if (i != 0 && particleCount > 0)
        {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i >> 0 & 255) / 255.0D;

            for (int j = 0; j < particleCount; ++j)
            {
                this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, d0, d1, d2, new int[0]);
            }
        }
    }

    public int getColor()
    {
        return ((Integer)this.dataManager.get(COLOR)).intValue();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);

        if (this.potion != PotionTypes.EMPTY && this.potion != null)
        {
            tagCompound.setString("Potion", ((ResourceLocation)PotionType.REGISTRY.getNameForObject(this.potion)).toString());
        }

        if (!this.customPotionEffects.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : this.customPotionEffects)
            {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            tagCompound.setTag("CustomPotionEffects", nbttaglist);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);        

        if (tagCompund.hasKey("Potion", 8))
        {
            this.potion = PotionUtils.getPotionTypeFromNBT(tagCompund);
        }

        for (PotionEffect potioneffect : PotionUtils.getFullEffectsFromTag(tagCompund))
        {
            this.addEffect(potioneffect);
        }

        if (this.potion != PotionTypes.EMPTY || !this.customPotionEffects.isEmpty())
        {
            this.dataManager.set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects))));
        }
    }

    protected void arrowHit(EntityLivingBase living)
    {
        super.arrowHit(living);

        for (PotionEffect potioneffect : this.potion.getEffects())
        {
            living.addPotionEffect(new PotionEffect(potioneffect.getPotion(), potioneffect.getDuration() / 8, potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
        }

        if (!this.customPotionEffects.isEmpty())
        {
            for (PotionEffect potioneffect1 : this.customPotionEffects)
            {
                living.addPotionEffect(potioneffect1);
            }
        }
    }

    protected ItemStack getArrowStack()
    {
        if (this.customPotionEffects.isEmpty() && this.potion == PotionTypes.EMPTY)
        {
            return new ItemStack(Items.ARROW);
        }
        else
        {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.addPotionToItemStack(itemstack, this.potion);
            PotionUtils.appendEffects(itemstack, this.customPotionEffects);
            return itemstack;
        }
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 0)
        {
            int i = this.getColor();

            if (i > 0)
            {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i >> 0 & 255) / 255.0D;

                for (int j = 0; j < 20; ++j)
                {
                    this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, d0, d1, d2, new int[0]);
                }
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		// TODO Auto-generated method stub
		if(this.cameFrom != null) {
			buffer.writeInt(cameFrom.getX());
			buffer.writeInt(cameFrom.getY());
			buffer.writeInt(cameFrom.getZ());
        }
		
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		// TODO Auto-generated method stub
		
		int x = additionalData.readInt();
        int y = additionalData.readInt();
        int z = additionalData.readInt();
        
        cameFrom = new BlockPos(x,y,z);
		
	}
}