package com.revengale.powerdefense.blocks;

import org.lwjgl.opengl.GL11;

import com.revengale.powerdefense.PowerDefense;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TurretBlockTESR extends TileEntitySpecialRenderer<TurretBlockTileEntity> {

    private IModel body;
    private IBakedModel bakedBody;
    
    private IModel gunsRight;
    private IBakedModel bakedGunsRight;
    
    private IModel gunsLeft;
    private IBakedModel bakedGunsLeft;

    private IBakedModel getBakedBody() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedBody == null) {
            try {
            	body = ModelLoaderRegistry.getModel(new ResourceLocation(PowerDefense.MODID, "block/turretbody.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedBody = body.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedBody;
    }
    
    private IBakedModel getBakedGunsLeft() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedGunsLeft == null) {
            try {
            	gunsLeft = ModelLoaderRegistry.getModel(new ResourceLocation(PowerDefense.MODID, "block/turretgunsleft.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedGunsLeft = gunsLeft.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedGunsLeft;
    }
    
    private IBakedModel getBakedGunsRight() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedGunsRight == null) {
            try {
            	gunsRight = ModelLoaderRegistry.getModel(new ResourceLocation(PowerDefense.MODID, "block/turretgunsright.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedGunsRight = gunsRight.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedGunsRight;
    }


    @Override
    public void renderTileEntityAt(TurretBlockTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Render the rotating handles
        renderHandles(te);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

    private void renderHandles(TurretBlockTileEntity te) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(.5, 0.2, .5);
        //long angle = (System.currentTimeMillis() / 10) % 360;
        GlStateManager.rotate(te.curBodyAngle, 0, 1, 0);

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.locationBlocksTexture);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = te.getWorld();
        // Translate back to local view coordinates so that we can do the actual rendering here
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                world,
                getBakedBody(),
                world.getBlockState(te.getPos()),
                te.getPos(),
                Tessellator.getInstance().getBuffer(), false);
        
        tessellator.draw();
        
        GlStateManager.popMatrix();
        
        //left guns
        GlStateManager.pushMatrix();
        
        GlStateManager.translate(.5, 0.35, .5);

        GlStateManager.rotate(te.curBodyAngle, 0, 1, 0);
        GlStateManager.rotate(te.curGunAngle, 0, 0, 1);
        GlStateManager.scale(te.leftGunScale, 1, 1);
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
        
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                world,
                getBakedGunsLeft(),
                world.getBlockState(te.getPos()),
                te.getPos(),
                Tessellator.getInstance().getBuffer(), false);
        
        tessellator.draw();
        GlStateManager.popMatrix();
        
        //right guns
        GlStateManager.pushMatrix();
       
        GlStateManager.translate(.5, 0.35, .5);
        
        GlStateManager.rotate(te.curBodyAngle, 0, 1, 0);
        GlStateManager.rotate(te.curGunAngle, 0, 0, 1);        
        GlStateManager.scale(te.rightGunScale, 1, 1);
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
        
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                world,
                getBakedGunsRight(),
                world.getBlockState(te.getPos()),
                te.getPos(),
                Tessellator.getInstance().getBuffer(), false);
        
        tessellator.draw();        

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }


}
