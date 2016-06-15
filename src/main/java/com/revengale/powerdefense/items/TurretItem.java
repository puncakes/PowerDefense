package com.revengale.powerdefense.items;

import com.revengale.powerdefense.PowerDefense;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TurretItem extends Item {
	
	public static final TurretItem instance = new TurretItem();
    public static final String name = "Turret";


	public static void init() {

		instance.setUnlocalizedName("Turret");
		instance.setRegistryName("Turret");
		instance.setCreativeTab(CreativeTabs.tabMisc);
		GameRegistry.register(instance);
	}
	
	public static void setupRenderer() {
		System.out.println("Setting turret item textures!!!!!");
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation mrl = new ModelResourceLocation(PowerDefense.MODID + ":" + "Turret", "inventory");
		renderItem.getItemModelMesher().register(instance, 0, mrl);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		
		playerIn.swingArm(EnumHand.MAIN_HAND);
		worldIn.setBlockState(pos, new TurretBlock().getDefaultState());
		
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	

}
