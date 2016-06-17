package com.revengale.powerdefense.renderer;

import com.revengale.powerdefense.items.projectiles.EntityCustomArrow;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCustomArrow extends ArrowRenderer<EntityCustomArrow>
{
    public static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");
    public static final ResourceLocation RES_TIPPED_ARROW = new ResourceLocation("textures/entity/projectiles/tipped_arrow.png");

    public RenderCustomArrow(RenderManager p_i46547_1_)
    {
        super(p_i46547_1_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityCustomArrow entity)
    {
        return entity.getColor() > 0 ? RES_TIPPED_ARROW : RES_ARROW;
    }
}