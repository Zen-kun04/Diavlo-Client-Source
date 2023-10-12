/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityDiggingFX
/*     */   extends EntityFX {
/*     */   private IBlockState sourceState;
/*     */   private BlockPos sourcePos;
/*     */   
/*     */   protected EntityDiggingFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state) {
/*  19 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*  20 */     this.sourceState = state;
/*  21 */     setParticleIcon(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
/*  22 */     this.particleGravity = (state.getBlock()).blockParticleGravity;
/*  23 */     this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
/*  24 */     this.particleScale /= 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDiggingFX setBlockPos(BlockPos pos) {
/*  29 */     this.sourcePos = pos;
/*     */     
/*  31 */     if (this.sourceState.getBlock() == Blocks.grass)
/*     */     {
/*  33 */       return this;
/*     */     }
/*     */ 
/*     */     
/*  37 */     int i = this.sourceState.getBlock().colorMultiplier((IBlockAccess)this.worldObj, pos);
/*  38 */     this.particleRed *= (i >> 16 & 0xFF) / 255.0F;
/*  39 */     this.particleGreen *= (i >> 8 & 0xFF) / 255.0F;
/*  40 */     this.particleBlue *= (i & 0xFF) / 255.0F;
/*  41 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityDiggingFX func_174845_l() {
/*  47 */     this.sourcePos = new BlockPos(this.posX, this.posY, this.posZ);
/*  48 */     Block block = this.sourceState.getBlock();
/*     */     
/*  50 */     if (block == Blocks.grass)
/*     */     {
/*  52 */       return this;
/*     */     }
/*     */ 
/*     */     
/*  56 */     int i = block.getRenderColor(this.sourceState);
/*  57 */     this.particleRed *= (i >> 16 & 0xFF) / 255.0F;
/*  58 */     this.particleGreen *= (i >> 8 & 0xFF) / 255.0F;
/*  59 */     this.particleBlue *= (i & 0xFF) / 255.0F;
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFXLayer() {
/*  66 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/*  71 */     float f = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
/*  72 */     float f1 = f + 0.015609375F;
/*  73 */     float f2 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
/*  74 */     float f3 = f2 + 0.015609375F;
/*  75 */     float f4 = 0.1F * this.particleScale;
/*     */     
/*  77 */     if (this.particleIcon != null) {
/*     */       
/*  79 */       f = this.particleIcon.getInterpolatedU((this.particleTextureJitterX / 4.0F * 16.0F));
/*  80 */       f1 = this.particleIcon.getInterpolatedU(((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
/*  81 */       f2 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY / 4.0F * 16.0F));
/*  82 */       f3 = this.particleIcon.getInterpolatedV(((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
/*     */     } 
/*     */     
/*  85 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/*  86 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/*  87 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/*  88 */     int i = getBrightnessForRender(partialTicks);
/*  89 */     int j = i >> 16 & 0xFFFF;
/*  90 */     int k = i & 0xFFFF;
/*  91 */     worldRendererIn.pos((f5 - rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 - rotationYZ * f4 - rotationXZ * f4)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*  92 */     worldRendererIn.pos((f5 - rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 - rotationYZ * f4 + rotationXZ * f4)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*  93 */     worldRendererIn.pos((f5 + rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 + rotationYZ * f4 + rotationXZ * f4)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*  94 */     worldRendererIn.pos((f5 + rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 + rotationYZ * f4 - rotationXZ * f4)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks) {
/*  99 */     int i = super.getBrightnessForRender(partialTicks);
/* 100 */     int j = 0;
/*     */     
/* 102 */     if (this.worldObj.isBlockLoaded(this.sourcePos))
/*     */     {
/* 104 */       j = this.worldObj.getCombinedLight(this.sourcePos, 0);
/*     */     }
/*     */     
/* 107 */     return (i == 0) ? j : i;
/*     */   }
/*     */   
/*     */   public static class Factory
/*     */     implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 114 */       return (new EntityDiggingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Block.getStateById(p_178902_15_[0]))).func_174845_l();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityDiggingFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */