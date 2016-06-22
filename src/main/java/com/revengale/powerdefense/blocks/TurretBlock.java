package com.revengale.powerdefense.blocks;

import com.revengale.powerdefense.PowerDefense;
import com.revengale.powerdefense.blocks.itempedestal.PedestalTESR;
import com.revengale.powerdefense.blocks.itempedestal.PedestalTileEntity;
import com.revengale.powerdefense.items.TurretItem;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurretBlock extends Block implements ITileEntityProvider {
	
	public static final TurretBlock instance = new TurretBlock();
    public static final String name = "Turret";
	
	public static void init() {
		instance.setUnlocalizedName(name);
		instance.setRegistryName(name);
		instance.setCreativeTab(CreativeTabs.MISC);
		GameRegistry.register(instance);
		GameRegistry.registerTileEntity(TurretBlockTileEntity.class, name);
	}
	
	@SideOnly(Side.CLIENT)
	public static void initClient() {
		//Item item = Item.getItemFromBlock(instance);
		ModelLoader.setCustomModelResourceLocation(TurretItem.instance, 0, new ModelResourceLocation(PowerDefense.MODID + ":" + "Turret", "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(TurretBlockTileEntity.class, new TurretBlockTESR());
	}
	
	@Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TurretBlockTileEntity();
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }
	
	public void setupRenderer() {
		
	}

	public TurretBlock(Material materialIn) {
		super(materialIn);
		// TODO Auto-generated constructor stub
	}
	
	public TurretBlock() {
		super(Material.ROCK);
	}

	//@Override
	//public EnumBlockRenderType getRenderType(IBlockState state) {
		// TODO Auto-generated method stub
	//	return EnumBlockRenderType.INVISIBLE;
	//}
	
	

}
