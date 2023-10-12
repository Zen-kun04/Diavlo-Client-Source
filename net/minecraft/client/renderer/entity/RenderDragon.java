/*     */ package net.minecraft.client.renderer.entity;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelDragon;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerEnderDragonDeath;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.boss.EntityDragon;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class RenderDragon extends RenderLiving<EntityDragon> {
/*  18 */   private static final ResourceLocation enderDragonCrystalBeamTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
/*  19 */   private static final ResourceLocation enderDragonExplodingTextures = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
/*  20 */   private static final ResourceLocation enderDragonTextures = new ResourceLocation("textures/entity/enderdragon/dragon.png");
/*     */   
/*     */   protected ModelDragon modelDragon;
/*     */   
/*     */   public RenderDragon(RenderManager renderManagerIn) {
/*  25 */     super(renderManagerIn, (ModelBase)new ModelDragon(0.0F), 0.5F);
/*  26 */     this.modelDragon = (ModelDragon)this.mainModel;
/*  27 */     addLayer(new LayerEnderDragonEyes(this));
/*  28 */     addLayer(new LayerEnderDragonDeath());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void rotateCorpse(EntityDragon bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
/*  33 */     float f = (float)bat.getMovementOffsets(7, partialTicks)[0];
/*  34 */     float f1 = (float)(bat.getMovementOffsets(5, partialTicks)[1] - bat.getMovementOffsets(10, partialTicks)[1]);
/*  35 */     GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
/*  36 */     GlStateManager.rotate(f1 * 10.0F, 1.0F, 0.0F, 0.0F);
/*  37 */     GlStateManager.translate(0.0F, 0.0F, 1.0F);
/*     */     
/*  39 */     if (bat.deathTime > 0) {
/*     */       
/*  41 */       float f2 = (bat.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
/*  42 */       f2 = MathHelper.sqrt_float(f2);
/*     */       
/*  44 */       if (f2 > 1.0F)
/*     */       {
/*  46 */         f2 = 1.0F;
/*     */       }
/*     */       
/*  49 */       GlStateManager.rotate(f2 * getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderModel(EntityDragon entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor) {
/*  55 */     if (entitylivingbaseIn.deathTicks > 0) {
/*     */       
/*  57 */       float f = entitylivingbaseIn.deathTicks / 200.0F;
/*  58 */       GlStateManager.depthFunc(515);
/*  59 */       GlStateManager.enableAlpha();
/*  60 */       GlStateManager.alphaFunc(516, f);
/*  61 */       bindTexture(enderDragonExplodingTextures);
/*  62 */       this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
/*  63 */       GlStateManager.alphaFunc(516, 0.1F);
/*  64 */       GlStateManager.depthFunc(514);
/*     */     } 
/*     */     
/*  67 */     bindEntityTexture(entitylivingbaseIn);
/*  68 */     this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
/*     */     
/*  70 */     if (entitylivingbaseIn.hurtTime > 0) {
/*     */       
/*  72 */       GlStateManager.depthFunc(514);
/*  73 */       GlStateManager.disableTexture2D();
/*  74 */       GlStateManager.enableBlend();
/*  75 */       GlStateManager.blendFunc(770, 771);
/*  76 */       GlStateManager.color(1.0F, 0.0F, 0.0F, 0.5F);
/*  77 */       this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
/*  78 */       GlStateManager.enableTexture2D();
/*  79 */       GlStateManager.disableBlend();
/*  80 */       GlStateManager.depthFunc(515);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRender(EntityDragon entity, double x, double y, double z, float entityYaw, float partialTicks) {
/*  86 */     BossStatus.setBossStatus((IBossDisplayData)entity, false);
/*  87 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */     
/*  89 */     if (entity.healingEnderCrystal != null)
/*     */     {
/*  91 */       drawRechargeRay(entity, x, y, z, partialTicks);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawRechargeRay(EntityDragon dragon, double p_180574_2_, double p_180574_4_, double p_180574_6_, float p_180574_8_) {
/*  97 */     float f = dragon.healingEnderCrystal.innerRotation + p_180574_8_;
/*  98 */     float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
/*  99 */     f1 = (f1 * f1 + f1) * 0.2F;
/* 100 */     float f2 = (float)(dragon.healingEnderCrystal.posX - dragon.posX - (dragon.prevPosX - dragon.posX) * (1.0F - p_180574_8_));
/* 101 */     float f3 = (float)(f1 + dragon.healingEnderCrystal.posY - 1.0D - dragon.posY - (dragon.prevPosY - dragon.posY) * (1.0F - p_180574_8_));
/* 102 */     float f4 = (float)(dragon.healingEnderCrystal.posZ - dragon.posZ - (dragon.prevPosZ - dragon.posZ) * (1.0F - p_180574_8_));
/* 103 */     float f5 = MathHelper.sqrt_float(f2 * f2 + f4 * f4);
/* 104 */     float f6 = MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4);
/* 105 */     GlStateManager.pushMatrix();
/* 106 */     GlStateManager.translate((float)p_180574_2_, (float)p_180574_4_ + 2.0F, (float)p_180574_6_);
/* 107 */     GlStateManager.rotate((float)-Math.atan2(f4, f2) * 180.0F / 3.1415927F - 90.0F, 0.0F, 1.0F, 0.0F);
/* 108 */     GlStateManager.rotate((float)-Math.atan2(f5, f3) * 180.0F / 3.1415927F - 90.0F, 1.0F, 0.0F, 0.0F);
/* 109 */     Tessellator tessellator = Tessellator.getInstance();
/* 110 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 111 */     RenderHelper.disableStandardItemLighting();
/* 112 */     GlStateManager.disableCull();
/* 113 */     bindTexture(enderDragonCrystalBeamTextures);
/* 114 */     GlStateManager.shadeModel(7425);
/* 115 */     float f7 = 0.0F - (dragon.ticksExisted + p_180574_8_) * 0.01F;
/* 116 */     float f8 = MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4) / 32.0F - (dragon.ticksExisted + p_180574_8_) * 0.01F;
/* 117 */     worldrenderer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 118 */     int i = 8;
/*     */     
/* 120 */     for (int j = 0; j <= 8; j++) {
/*     */       
/* 122 */       float f9 = MathHelper.sin((j % 8) * 3.1415927F * 2.0F / 8.0F) * 0.75F;
/* 123 */       float f10 = MathHelper.cos((j % 8) * 3.1415927F * 2.0F / 8.0F) * 0.75F;
/* 124 */       float f11 = (j % 8) * 1.0F / 8.0F;
/* 125 */       worldrenderer.pos((f9 * 0.2F), (f10 * 0.2F), 0.0D).tex(f11, f8).color(0, 0, 0, 255).endVertex();
/* 126 */       worldrenderer.pos(f9, f10, f6).tex(f11, f7).color(255, 255, 255, 255).endVertex();
/*     */     } 
/*     */     
/* 129 */     tessellator.draw();
/* 130 */     GlStateManager.enableCull();
/* 131 */     GlStateManager.shadeModel(7424);
/* 132 */     RenderHelper.enableStandardItemLighting();
/* 133 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(EntityDragon entity) {
/* 138 */     return enderDragonTextures;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */