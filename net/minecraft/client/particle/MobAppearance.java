/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityGuardian;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class MobAppearance
/*    */   extends EntityFX
/*    */ {
/*    */   private EntityLivingBase entity;
/*    */   
/*    */   protected MobAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
/* 20 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 21 */     this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
/* 22 */     this.motionX = this.motionY = this.motionZ = 0.0D;
/* 23 */     this.particleGravity = 0.0F;
/* 24 */     this.particleMaxAge = 30;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFXLayer() {
/* 29 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 34 */     super.onUpdate();
/*    */     
/* 36 */     if (this.entity == null) {
/*    */       
/* 38 */       EntityGuardian entityguardian = new EntityGuardian(this.worldObj);
/* 39 */       entityguardian.setElder();
/* 40 */       this.entity = (EntityLivingBase)entityguardian;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 46 */     if (this.entity != null) {
/*    */       
/* 48 */       RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
/* 49 */       rendermanager.setRenderPosition(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
/* 50 */       float f = 0.42553192F;
/* 51 */       float f1 = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 52 */       GlStateManager.depthMask(true);
/* 53 */       GlStateManager.enableBlend();
/* 54 */       GlStateManager.enableDepth();
/* 55 */       GlStateManager.blendFunc(770, 771);
/* 56 */       float f2 = 240.0F;
/* 57 */       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f2, f2);
/* 58 */       GlStateManager.pushMatrix();
/* 59 */       float f3 = 0.05F + 0.5F * MathHelper.sin(f1 * 3.1415927F);
/* 60 */       GlStateManager.color(1.0F, 1.0F, 1.0F, f3);
/* 61 */       GlStateManager.translate(0.0F, 1.8F, 0.0F);
/* 62 */       GlStateManager.rotate(180.0F - entityIn.rotationYaw, 0.0F, 1.0F, 0.0F);
/* 63 */       GlStateManager.rotate(60.0F - 150.0F * f1 - entityIn.rotationPitch, 1.0F, 0.0F, 0.0F);
/* 64 */       GlStateManager.translate(0.0F, -0.4F, -1.5F);
/* 65 */       GlStateManager.scale(f, f, f);
/* 66 */       this.entity.rotationYaw = this.entity.prevRotationYaw = 0.0F;
/* 67 */       this.entity.rotationYawHead = this.entity.prevRotationYawHead = 0.0F;
/* 68 */       rendermanager.renderEntityWithPosYaw((Entity)this.entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
/* 69 */       GlStateManager.popMatrix();
/* 70 */       GlStateManager.enableDepth();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 78 */       return new MobAppearance(worldIn, xCoordIn, yCoordIn, zCoordIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\MobAppearance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */