package com.revengale.powerdefense.items;

import com.revengale.powerdefense.PowerDefense;
import com.revengale.powerdefense.blocks.TurretBlock;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TurretItem extends Item {
	
	public static final TurretItem instance = new TurretItem();
    public static final String name = "Turret";


	public static void init() {

		instance.setUnlocalizedName("Turret");
		instance.setRegistryName("Turret");
		instance.setCreativeTab(CreativeTabs.MISC);
		GameRegistry.register(instance);
	}
	
	public static void setupRenderer() {
		System.out.println("Setting turret item textures!!!!!");
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation mrl = new ModelResourceLocation(PowerDefense.MODID + ":" + "Turret", "inventory");
		renderItem.getItemModelMesher().register(instance, 0, mrl);
	}
	
	public static boolean setTileEntityNBT(World worldIn, EntityPlayer pos, BlockPos stack, ItemStack stackIn)
    {
        MinecraftServer minecraftserver = worldIn.getMinecraftServer();

        if (minecraftserver == null)
        {
            return false;
        }
        else
        {
            if (stackIn.hasTagCompound() && stackIn.getTagCompound().hasKey("BlockEntityTag", 10))
            {
                TileEntity tileentity = worldIn.getTileEntity(stack);

                if (tileentity != null)
                {
                    if (!worldIn.isRemote && (pos == null || !minecraftserver.getPlayerList().canSendCommands(pos.getGameProfile())))
                    {
                        return false;
                    }

                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttagcompound.copy();
                    tileentity.writeToNBT(nbttagcompound);
                    NBTTagCompound nbttagcompound2 = (NBTTagCompound)stackIn.getTagCompound().getTag("BlockEntityTag");
                    nbttagcompound.merge(nbttagcompound2);
                    nbttagcompound.setInteger("x", stack.getX());
                    nbttagcompound.setInteger("y", stack.getY());
                    nbttagcompound.setInteger("z", stack.getZ());

                    if (!nbttagcompound.equals(nbttagcompound1))
                    {
                        tileentity.readFromNBT(nbttagcompound);
                        tileentity.markDirty();
                        return true;
                    }
                }
            }

            return false;
        }
    }
	
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == TurretBlock.instance)
        {
            setTileEntityNBT(world, player, pos, stack);
            TurretBlock.instance.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
    }

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		
		IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

        if (stack.stackSize != 0 && playerIn.canPlayerEdit(pos, facing, stack) && worldIn.canBlockBePlaced(TurretBlock.instance, pos, false, facing, (Entity)null, stack))
        {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate1 = TurretBlock.instance.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, i, playerIn);

            if (placeBlockAt(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                SoundEvent soundtype = TurretBlock.instance.getSoundType().getStepSound();
                worldIn.playSound(playerIn, pos, soundtype, SoundCategory.BLOCKS,  1.0F, 0.8F);
                --stack.stackSize;
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
	}
	
	

}
