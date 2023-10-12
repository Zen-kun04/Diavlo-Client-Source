/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.culling.ICamera;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.src.Config;
/*     */ import net.optifine.shaders.Shaders;
/*     */ 
/*     */ public abstract class RenderLiving<T extends EntityLiving>
/*     */   extends RendererLivingEntity<T>
/*     */ {
/*     */   public RenderLiving(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
/*  20 */     super(rendermanagerIn, modelbaseIn, shadowsizeIn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRenderName(T entity) {
/*  25 */     return (super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || (entity.hasCustomName() && entity == this.renderManager.pointedEntity)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
/*  30 */     if (super.shouldRender(livingEntity, camera, camX, camY, camZ))
/*     */     {
/*  32 */       return true;
/*     */     }
/*  34 */     if (livingEntity.getLeashed() && livingEntity.getLeashedToEntity() != null) {
/*     */       
/*  36 */       Entity entity = livingEntity.getLeashedToEntity();
/*  37 */       return camera.isBoundingBoxInFrustum(entity.getEntityBoundingBox());
/*     */     } 
/*     */ 
/*     */     
/*  41 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
/*  47 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*  48 */     renderLeash(entity, x, y, z, entityYaw, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightmap(T entityLivingIn, float partialTicks) {
/*  53 */     int i = entityLivingIn.getBrightnessForRender(partialTicks);
/*  54 */     int j = i % 65536;
/*  55 */     int k = i / 65536;
/*  56 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private double interpolateValue(double start, double end, double pct) {
/*  61 */     return start + (end - start) * pct;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderLeash(T entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks) {
/*  66 */     if (!Config.isShaders() || !Shaders.isShadowPass) {
/*     */       
/*  68 */       Entity entity = entityLivingIn.getLeashedToEntity();
/*     */       
/*  70 */       if (entity != null) {
/*     */         
/*  72 */         y -= (1.6D - ((EntityLiving)entityLivingIn).height) * 0.5D;
/*  73 */         Tessellator tessellator = Tessellator.getInstance();
/*  74 */         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  75 */         double d0 = interpolateValue(entity.prevRotationYaw, entity.rotationYaw, (partialTicks * 0.5F)) * 0.01745329238474369D;
/*  76 */         double d1 = interpolateValue(entity.prevRotationPitch, entity.rotationPitch, (partialTicks * 0.5F)) * 0.01745329238474369D;
/*  77 */         double d2 = Math.cos(d0);
/*  78 */         double d3 = Math.sin(d0);
/*  79 */         double d4 = Math.sin(d1);
/*     */         
/*  81 */         if (entity instanceof net.minecraft.entity.EntityHanging) {
/*     */           
/*  83 */           d2 = 0.0D;
/*  84 */           d3 = 0.0D;
/*  85 */           d4 = -1.0D;
/*     */         } 
/*     */         
/*  88 */         double d5 = Math.cos(d1);
/*  89 */         double d6 = interpolateValue(entity.prevPosX, entity.posX, partialTicks) - d2 * 0.7D - d3 * 0.5D * d5;
/*  90 */         double d7 = interpolateValue(entity.prevPosY + entity.getEyeHeight() * 0.7D, entity.posY + entity.getEyeHeight() * 0.7D, partialTicks) - d4 * 0.5D - 0.25D;
/*  91 */         double d8 = interpolateValue(entity.prevPosZ, entity.posZ, partialTicks) - d3 * 0.7D + d2 * 0.5D * d5;
/*  92 */         double d9 = interpolateValue(((EntityLiving)entityLivingIn).prevRenderYawOffset, ((EntityLiving)entityLivingIn).renderYawOffset, partialTicks) * 0.01745329238474369D + 1.5707963267948966D;
/*  93 */         d2 = Math.cos(d9) * ((EntityLiving)entityLivingIn).width * 0.4D;
/*  94 */         d3 = Math.sin(d9) * ((EntityLiving)entityLivingIn).width * 0.4D;
/*  95 */         double d10 = interpolateValue(((EntityLiving)entityLivingIn).prevPosX, ((EntityLiving)entityLivingIn).posX, partialTicks) + d2;
/*  96 */         double d11 = interpolateValue(((EntityLiving)entityLivingIn).prevPosY, ((EntityLiving)entityLivingIn).posY, partialTicks);
/*  97 */         double d12 = interpolateValue(((EntityLiving)entityLivingIn).prevPosZ, ((EntityLiving)entityLivingIn).posZ, partialTicks) + d3;
/*  98 */         x += d2;
/*  99 */         z += d3;
/* 100 */         double d13 = (float)(d6 - d10);
/* 101 */         double d14 = (float)(d7 - d11);
/* 102 */         double d15 = (float)(d8 - d12);
/* 103 */         GlStateManager.disableTexture2D();
/* 104 */         GlStateManager.disableLighting();
/* 105 */         GlStateManager.disableCull();
/*     */         
/* 107 */         if (Config.isShaders())
/*     */         {
/* 109 */           Shaders.beginLeash();
/*     */         }
/*     */         
/* 112 */         int i = 24;
/* 113 */         double d16 = 0.025D;
/* 114 */         worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
/*     */         
/* 116 */         for (int j = 0; j <= 24; j++) {
/*     */           
/* 118 */           float f = 0.5F;
/* 119 */           float f1 = 0.4F;
/* 120 */           float f2 = 0.3F;
/*     */           
/* 122 */           if (j % 2 == 0) {
/*     */             
/* 124 */             f *= 0.7F;
/* 125 */             f1 *= 0.7F;
/* 126 */             f2 *= 0.7F;
/*     */           } 
/*     */           
/* 129 */           float f3 = j / 24.0F;
/* 130 */           worldrenderer.pos(x + d13 * f3 + 0.0D, y + d14 * (f3 * f3 + f3) * 0.5D + ((24.0F - j) / 18.0F + 0.125F), z + d15 * f3).color(f, f1, f2, 1.0F).endVertex();
/* 131 */           worldrenderer.pos(x + d13 * f3 + 0.025D, y + d14 * (f3 * f3 + f3) * 0.5D + ((24.0F - j) / 18.0F + 0.125F) + 0.025D, z + d15 * f3).color(f, f1, f2, 1.0F).endVertex();
/*     */         } 
/*     */         
/* 134 */         tessellator.draw();
/* 135 */         worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
/*     */         
/* 137 */         for (int k = 0; k <= 24; k++) {
/*     */           
/* 139 */           float f4 = 0.5F;
/* 140 */           float f5 = 0.4F;
/* 141 */           float f6 = 0.3F;
/*     */           
/* 143 */           if (k % 2 == 0) {
/*     */             
/* 145 */             f4 *= 0.7F;
/* 146 */             f5 *= 0.7F;
/* 147 */             f6 *= 0.7F;
/*     */           } 
/*     */           
/* 150 */           float f7 = k / 24.0F;
/* 151 */           worldrenderer.pos(x + d13 * f7 + 0.0D, y + d14 * (f7 * f7 + f7) * 0.5D + ((24.0F - k) / 18.0F + 0.125F) + 0.025D, z + d15 * f7).color(f4, f5, f6, 1.0F).endVertex();
/* 152 */           worldrenderer.pos(x + d13 * f7 + 0.025D, y + d14 * (f7 * f7 + f7) * 0.5D + ((24.0F - k) / 18.0F + 0.125F), z + d15 * f7 + 0.025D).color(f4, f5, f6, 1.0F).endVertex();
/*     */         } 
/*     */         
/* 155 */         tessellator.draw();
/*     */         
/* 157 */         if (Config.isShaders())
/*     */         {
/* 159 */           Shaders.endLeash();
/*     */         }
/*     */         
/* 162 */         GlStateManager.enableLighting();
/* 163 */         GlStateManager.enableTexture2D();
/* 164 */         GlStateManager.enableCull();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderLiving.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */