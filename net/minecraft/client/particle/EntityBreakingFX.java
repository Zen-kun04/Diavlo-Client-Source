/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityBreakingFX
/*    */   extends EntityFX
/*    */ {
/*    */   protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, Item p_i1195_8_) {
/* 15 */     this(worldIn, posXIn, posYIn, posZIn, p_i1195_8_, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, Item p_i1197_14_, int p_i1197_15_) {
/* 20 */     this(worldIn, posXIn, posYIn, posZIn, p_i1197_14_, p_i1197_15_);
/* 21 */     this.motionX *= 0.10000000149011612D;
/* 22 */     this.motionY *= 0.10000000149011612D;
/* 23 */     this.motionZ *= 0.10000000149011612D;
/* 24 */     this.motionX += xSpeedIn;
/* 25 */     this.motionY += ySpeedIn;
/* 26 */     this.motionZ += zSpeedIn;
/*    */   }
/*    */ 
/*    */   
/*    */   protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, Item p_i1196_8_, int p_i1196_9_) {
/* 31 */     super(worldIn, posXIn, posYIn, posZIn, 0.0D, 0.0D, 0.0D);
/* 32 */     setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i1196_8_, p_i1196_9_));
/* 33 */     this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
/* 34 */     this.particleGravity = Blocks.snow.blockParticleGravity;
/* 35 */     this.particleScale /= 2.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFXLayer() {
/* 40 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 45 */     float f = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
/* 46 */     float f1 = f + 0.015609375F;
/* 47 */     float f2 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
/* 48 */     float f3 = f2 + 0.015609375F;
/* 49 */     float f4 = 0.1F * this.particleScale;
/*    */     
/* 51 */     if (this.particleIcon != null) {
/*    */       
/* 53 */       f = this.particleIcon.getInterpolatedU((this.particleTextureJitterX / 4.0F * 16.0F));
/* 54 */       f1 = this.particleIcon.getInterpolatedU(((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
/* 55 */       f2 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY / 4.0F * 16.0F));
/* 56 */       f3 = this.particleIcon.getInterpolatedV(((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
/*    */     } 
/*    */     
/* 59 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 60 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 61 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 62 */     int i = getBrightnessForRender(partialTicks);
/* 63 */     int j = i >> 16 & 0xFFFF;
/* 64 */     int k = i & 0xFFFF;
/* 65 */     worldRendererIn.pos((f5 - rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 - rotationYZ * f4 - rotationXZ * f4)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 66 */     worldRendererIn.pos((f5 - rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 - rotationYZ * f4 + rotationXZ * f4)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 67 */     worldRendererIn.pos((f5 + rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 + rotationYZ * f4 + rotationXZ * f4)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 68 */     worldRendererIn.pos((f5 + rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 + rotationYZ * f4 - rotationXZ * f4)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 75 */       int i = (p_178902_15_.length > 1) ? p_178902_15_[1] : 0;
/* 76 */       return new EntityBreakingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Item.getItemById(p_178902_15_[0]), i);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SlimeFactory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 84 */       return new EntityBreakingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.slime_ball);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SnowballFactory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 92 */       return new EntityBreakingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.snowball);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityBreakingFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */