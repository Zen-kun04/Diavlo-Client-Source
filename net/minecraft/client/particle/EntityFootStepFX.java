/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityFootStepFX
/*    */   extends EntityFX {
/* 16 */   private static final ResourceLocation FOOTPRINT_TEXTURE = new ResourceLocation("textures/particle/footprint.png");
/*    */   
/*    */   private int footstepAge;
/*    */   private int footstepMaxAge;
/*    */   private TextureManager currentFootSteps;
/*    */   
/*    */   protected EntityFootStepFX(TextureManager currentFootStepsIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
/* 23 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 24 */     this.currentFootSteps = currentFootStepsIn;
/* 25 */     this.motionX = this.motionY = this.motionZ = 0.0D;
/* 26 */     this.footstepMaxAge = 200;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 31 */     float f = (this.footstepAge + partialTicks) / this.footstepMaxAge;
/* 32 */     f *= f;
/* 33 */     float f1 = 2.0F - f * 2.0F;
/*    */     
/* 35 */     if (f1 > 1.0F)
/*    */     {
/* 37 */       f1 = 1.0F;
/*    */     }
/*    */     
/* 40 */     f1 *= 0.2F;
/* 41 */     GlStateManager.disableLighting();
/* 42 */     float f2 = 0.125F;
/* 43 */     float f3 = (float)(this.posX - interpPosX);
/* 44 */     float f4 = (float)(this.posY - interpPosY);
/* 45 */     float f5 = (float)(this.posZ - interpPosZ);
/* 46 */     float f6 = this.worldObj.getLightBrightness(new BlockPos(this));
/* 47 */     this.currentFootSteps.bindTexture(FOOTPRINT_TEXTURE);
/* 48 */     GlStateManager.enableBlend();
/* 49 */     GlStateManager.blendFunc(770, 771);
/* 50 */     worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 51 */     worldRendererIn.pos((f3 - 0.125F), f4, (f5 + 0.125F)).tex(0.0D, 1.0D).color(f6, f6, f6, f1).endVertex();
/* 52 */     worldRendererIn.pos((f3 + 0.125F), f4, (f5 + 0.125F)).tex(1.0D, 1.0D).color(f6, f6, f6, f1).endVertex();
/* 53 */     worldRendererIn.pos((f3 + 0.125F), f4, (f5 - 0.125F)).tex(1.0D, 0.0D).color(f6, f6, f6, f1).endVertex();
/* 54 */     worldRendererIn.pos((f3 - 0.125F), f4, (f5 - 0.125F)).tex(0.0D, 0.0D).color(f6, f6, f6, f1).endVertex();
/* 55 */     Tessellator.getInstance().draw();
/* 56 */     GlStateManager.disableBlend();
/* 57 */     GlStateManager.enableLighting();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 62 */     this.footstepAge++;
/*    */     
/* 64 */     if (this.footstepAge == this.footstepMaxAge)
/*    */     {
/* 66 */       setDead();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFXLayer() {
/* 72 */     return 3;
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 79 */       return new EntityFootStepFX(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityFootStepFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */