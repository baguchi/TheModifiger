package baguchan.the_modifiger.client.model;// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchan.the_modifiger.client.aniamtion.ModifigerAnimations;
import baguchan.the_modifiger.entity.Modifiger;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModifigerModel<T extends Modifiger> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;

    public ModifigerModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("everything").getChild("body");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition everything = partdefinition.addOrReplaceChild("everything", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition left_leg = everything.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -12.0F, 0.0F));

        PartDefinition right_leg = everything.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

        PartDefinition body = everything.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(16, 38).addBox(-4.0F, -11.75F, -2.5F, 8.0F, 15.0F, 6.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition righteyebrows = head.addOrReplaceChild("righteyebrows", CubeListBuilder.create(), PartPose.offset(-2.5F, -4.9604F, -4.7696F));

        PartDefinition righteyebrows_r1 = righteyebrows.addOrReplaceChild("righteyebrows_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, -1.0F, -0.5902F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.0902F, 0.0F, 3.1416F, 0.0F));

        PartDefinition lefteyebrows = head.addOrReplaceChild("lefteyebrows", CubeListBuilder.create(), PartPose.offset(2.5F, -4.9604F, -3.5196F));

        PartDefinition lefteyebrows_r1 = lefteyebrows.addOrReplaceChild("lefteyebrows_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -0.5902F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0902F, 0.0F, 3.1416F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -10.0F, 0.0F));

        PartDefinition leftHand = left_arm.addOrReplaceChild("leftHand", CubeListBuilder.create(), PartPose.offset(1.0F, 9.5F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -10.0F, 0.0F));

        PartDefinition box_right = body.addOrReplaceChild("box_right", CubeListBuilder.create().texOffs(0, 61).addBox(-6.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 1.0F, 0.0F));

        PartDefinition box_left = body.addOrReplaceChild("box_left", CubeListBuilder.create().texOffs(0, 61).addBox(-1.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 128);
    }
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.animate(entity.idleAnimationState, ModifigerAnimations.idle, ageInTicks);
        this.animate(entity.buildingAnimationState, ModifigerAnimations.building, ageInTicks);
        this.animateWalk(ModifigerAnimations.walk, limbSwing, limbSwingAmount, 1.0F, 2.0F);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}