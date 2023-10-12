/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.RenderHelper;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityLargeExplodeFX
/*    */   extends EntityFX {
/* 17 */   private static final ResourceLocation EXPLOSION_TEXTURE = new ResourceLocation("textures/entity/explosion.png");
/* 18 */   private static final VertexFormat field_181549_az = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
/*    */   
/*    */   private int field_70581_a;
/*    */   private int field_70584_aq;
/*    */   private TextureManager theRenderEngine;
/*    */   private float field_70582_as;
/*    */   
/*    */   protected EntityLargeExplodeFX(TextureManager renderEngine, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1213_9_, double p_i1213_11_, double p_i1213_13_) {
/* 26 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 27 */     this.theRenderEngine = renderEngine;
/* 28 */     this.field_70584_aq = 6 + this.rand.nextInt(4);
/* 29 */     this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.6F + 0.4F;
/* 30 */     this.field_70582_as = 1.0F - (float)p_i1213_9_ * 0.5F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 35 */     int i = (int)((this.field_70581_a + partialTicks) * 15.0F / this.field_70584_aq);
/*    */     
/* 37 */     if (i <= 15) {
/*    */       
/* 39 */       this.theRenderEngine.bindTexture(EXPLOSION_TEXTURE);
/* 40 */       float f = (i % 4) / 4.0F;
/* 41 */       float f1 = f + 0.24975F;
/* 42 */       float f2 = (i / 4) / 4.0F;
/* 43 */       float f3 = f2 + 0.24975F;
/* 44 */       float f4 = 2.0F * this.field_70582_as;
/* 45 */       float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 46 */       float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 47 */       float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 48 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 49 */       GlStateManager.disableLighting();
/* 50 */       RenderHelper.disableStandardItemLighting();
/* 51 */       worldRendererIn.begin(7, field_181549_az);
/* 52 */       worldRendererIn.pos((f5 - rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 - rotationYZ * f4 - rotationXZ * f4)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 53 */       worldRendererIn.pos((f5 - rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 - rotationYZ * f4 + rotationXZ * f4)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 54 */       worldRendererIn.pos((f5 + rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 + rotationYZ * f4 + rotationXZ * f4)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 55 */       worldRendererIn.pos((f5 + rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 + rotationYZ * f4 - rotationXZ * f4)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 56 */       Tessellator.getInstance().draw();
/* 57 */       GlStateManager.enableLighting();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBrightnessForRender(float partialTicks) {
/* 63 */     return 61680;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 68 */     this.prevPosX = this.posX;
/* 69 */     this.prevPosY = this.posY;
/* 70 */     this.prevPosZ = this.posZ;
/* 71 */     this.field_70581_a++;
/*    */     
/* 73 */     if (this.field_70581_a == this.field_70584_aq)
/*    */     {
/* 75 */       setDead();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFXLayer() {
/* 81 */     return 3;
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 88 */       return new EntityLargeExplodeFX(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityLargeExplodeFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */