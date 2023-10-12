/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class Barrier
/*    */   extends EntityFX
/*    */ {
/*    */   protected Barrier(World worldIn, double p_i46286_2_, double p_i46286_4_, double p_i46286_6_, Item p_i46286_8_) {
/* 14 */     super(worldIn, p_i46286_2_, p_i46286_4_, p_i46286_6_, 0.0D, 0.0D, 0.0D);
/* 15 */     setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i46286_8_));
/* 16 */     this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
/* 17 */     this.motionX = this.motionY = this.motionZ = 0.0D;
/* 18 */     this.particleGravity = 0.0F;
/* 19 */     this.particleMaxAge = 80;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFXLayer() {
/* 24 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 29 */     float f = this.particleIcon.getMinU();
/* 30 */     float f1 = this.particleIcon.getMaxU();
/* 31 */     float f2 = this.particleIcon.getMinV();
/* 32 */     float f3 = this.particleIcon.getMaxV();
/* 33 */     float f4 = 0.5F;
/* 34 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 35 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 36 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 37 */     int i = getBrightnessForRender(partialTicks);
/* 38 */     int j = i >> 16 & 0xFFFF;
/* 39 */     int k = i & 0xFFFF;
/* 40 */     worldRendererIn.pos((f5 - rotationX * 0.5F - rotationXY * 0.5F), (f6 - rotationZ * 0.5F), (f7 - rotationYZ * 0.5F - rotationXZ * 0.5F)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 41 */     worldRendererIn.pos((f5 - rotationX * 0.5F + rotationXY * 0.5F), (f6 + rotationZ * 0.5F), (f7 - rotationYZ * 0.5F + rotationXZ * 0.5F)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 42 */     worldRendererIn.pos((f5 + rotationX * 0.5F + rotationXY * 0.5F), (f6 + rotationZ * 0.5F), (f7 + rotationYZ * 0.5F + rotationXZ * 0.5F)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 43 */     worldRendererIn.pos((f5 + rotationX * 0.5F - rotationXY * 0.5F), (f6 - rotationZ * 0.5F), (f7 + rotationYZ * 0.5F - rotationXZ * 0.5F)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 50 */       return new Barrier(worldIn, xCoordIn, yCoordIn, zCoordIn, Item.getItemFromBlock(Blocks.barrier));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\Barrier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */