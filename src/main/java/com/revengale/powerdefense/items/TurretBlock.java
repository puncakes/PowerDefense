package com.revengale.powerdefense.items;

import com.revengale.powerdefense.PowerDefense;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurretBlock extends Block {
	
	public void init() {
		this.setUnlocalizedName("Dome");
		this.setRegistryName("Dome");
		GameRegistry.register(this);
	}
	
	public void initClient() {
		ModelLoader.setCustomModelResourceLocation(new TurretItem(), 0, new ModelResourceLocation(PowerDefense.MODID + ":" + "Dome", "inventory"));
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
		super(Material.rock);
	}

	//@Override
	//public EnumBlockRenderType getRenderType(IBlockState state) {
		// TODO Auto-generated method stub
	//	return EnumBlockRenderType.INVISIBLE;
	//}
	
	

}
