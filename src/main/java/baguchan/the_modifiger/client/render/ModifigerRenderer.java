package baguchan.the_modifiger.client.render;

import baguchan.the_modifiger.TheModifiger;
import baguchan.the_modifiger.client.ModModelLayers;
import baguchan.the_modifiger.client.model.ModifigerModel;
import baguchan.the_modifiger.entity.Modifiger;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModifigerRenderer<T extends Modifiger> extends MobRenderer<T, ModifigerModel<T>> {
    private static final ResourceLocation ILLAGER = new ResourceLocation(TheModifiger.MODID, "textures/entity/modifiger.png");

    public ModifigerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModifigerModel<>(renderManagerIn.bakeLayer(ModModelLayers.MODIFIGER)), 0.5F);/*
		this.addLayer(new CustomHeadLayer<>(this, renderManagerIn.getModelSet(), renderManagerIn.getItemInHandRenderer()));
		this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));*/
    }

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return ILLAGER;
    }
}