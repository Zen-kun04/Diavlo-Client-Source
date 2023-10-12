/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityFX
/*     */   extends Entity
/*     */ {
/*     */   protected int particleTextureIndexX;
/*     */   protected int particleTextureIndexY;
/*     */   protected float particleTextureJitterX;
/*     */   protected float particleTextureJitterY;
/*     */   protected int particleAge;
/*     */   protected int particleMaxAge;
/*     */   protected float particleScale;
/*     */   protected float particleGravity;
/*     */   protected float particleRed;
/*     */   protected float particleGreen;
/*     */   protected float particleBlue;
/*     */   protected float particleAlpha;
/*     */   protected TextureAtlasSprite particleIcon;
/*     */   public static double interpPosX;
/*     */   public static double interpPosY;
/*     */   public static double interpPosZ;
/*     */   
/*     */   protected EntityFX(World worldIn, double posXIn, double posYIn, double posZIn) {
/*  32 */     super(worldIn);
/*  33 */     this.particleAlpha = 1.0F;
/*  34 */     setSize(0.2F, 0.2F);
/*  35 */     setPosition(posXIn, posYIn, posZIn);
/*  36 */     this.lastTickPosX = this.prevPosX = posXIn;
/*  37 */     this.lastTickPosY = this.prevPosY = posYIn;
/*  38 */     this.lastTickPosZ = this.prevPosZ = posZIn;
/*  39 */     this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
/*  40 */     this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
/*  41 */     this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
/*  42 */     this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
/*  43 */     this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
/*  44 */     this.particleAge = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
/*  49 */     this(worldIn, xCoordIn, yCoordIn, zCoordIn);
/*  50 */     this.motionX = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
/*  51 */     this.motionY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
/*  52 */     this.motionZ = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
/*  53 */     float f = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
/*  54 */     float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
/*  55 */     this.motionX = this.motionX / f1 * f * 0.4000000059604645D;
/*  56 */     this.motionY = this.motionY / f1 * f * 0.4000000059604645D + 0.10000000149011612D;
/*  57 */     this.motionZ = this.motionZ / f1 * f * 0.4000000059604645D;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFX multiplyVelocity(float multiplier) {
/*  62 */     this.motionX *= multiplier;
/*  63 */     this.motionY = (this.motionY - 0.10000000149011612D) * multiplier + 0.10000000149011612D;
/*  64 */     this.motionZ *= multiplier;
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFX multipleParticleScaleBy(float scale) {
/*  70 */     setSize(0.2F * scale, 0.2F * scale);
/*  71 */     this.particleScale *= scale;
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRBGColorF(float particleRedIn, float particleGreenIn, float particleBlueIn) {
/*  77 */     this.particleRed = particleRedIn;
/*  78 */     this.particleGreen = particleGreenIn;
/*  79 */     this.particleBlue = particleBlueIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAlphaF(float alpha) {
/*  84 */     if (this.particleAlpha == 1.0F && alpha < 1.0F) {
/*     */       
/*  86 */       (Minecraft.getMinecraft()).effectRenderer.moveToAlphaLayer(this);
/*     */     }
/*  88 */     else if (this.particleAlpha < 1.0F && alpha == 1.0F) {
/*     */       
/*  90 */       (Minecraft.getMinecraft()).effectRenderer.moveToNoAlphaLayer(this);
/*     */     } 
/*     */     
/*  93 */     this.particleAlpha = alpha;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRedColorF() {
/*  98 */     return this.particleRed;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getGreenColorF() {
/* 103 */     return this.particleGreen;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBlueColorF() {
/* 108 */     return this.particleBlue;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAlpha() {
/* 113 */     return this.particleAlpha;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {}
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 127 */     this.prevPosX = this.posX;
/* 128 */     this.prevPosY = this.posY;
/* 129 */     this.prevPosZ = this.posZ;
/*     */     
/* 131 */     if (this.particleAge++ >= this.particleMaxAge)
/*     */     {
/* 133 */       setDead();
/*     */     }
/*     */     
/* 136 */     this.motionY -= 0.04D * this.particleGravity;
/* 137 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 138 */     this.motionX *= 0.9800000190734863D;
/* 139 */     this.motionY *= 0.9800000190734863D;
/* 140 */     this.motionZ *= 0.9800000190734863D;
/*     */     
/* 142 */     if (this.onGround) {
/*     */       
/* 144 */       this.motionX *= 0.699999988079071D;
/* 145 */       this.motionZ *= 0.699999988079071D;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 151 */     float f = this.particleTextureIndexX / 16.0F;
/* 152 */     float f1 = f + 0.0624375F;
/* 153 */     float f2 = this.particleTextureIndexY / 16.0F;
/* 154 */     float f3 = f2 + 0.0624375F;
/* 155 */     float f4 = 0.1F * this.particleScale;
/*     */     
/* 157 */     if (this.particleIcon != null) {
/*     */       
/* 159 */       f = this.particleIcon.getMinU();
/* 160 */       f1 = this.particleIcon.getMaxU();
/* 161 */       f2 = this.particleIcon.getMinV();
/* 162 */       f3 = this.particleIcon.getMaxV();
/*     */     } 
/*     */     
/* 165 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 166 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 167 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 168 */     int i = getBrightnessForRender(partialTicks);
/* 169 */     int j = i >> 16 & 0xFFFF;
/* 170 */     int k = i & 0xFFFF;
/* 171 */     worldRendererIn.pos((f5 - rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 - rotationYZ * f4 - rotationXZ * f4)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/* 172 */     worldRendererIn.pos((f5 - rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 - rotationYZ * f4 + rotationXZ * f4)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/* 173 */     worldRendererIn.pos((f5 + rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 + rotationYZ * f4 + rotationXZ * f4)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/* 174 */     worldRendererIn.pos((f5 + rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 + rotationYZ * f4 - rotationXZ * f4)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFXLayer() {
/* 179 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*     */ 
/*     */   
/*     */   public void setParticleIcon(TextureAtlasSprite icon) {
/* 192 */     int i = getFXLayer();
/*     */     
/* 194 */     if (i == 1) {
/*     */       
/* 196 */       this.particleIcon = icon;
/*     */     }
/*     */     else {
/*     */       
/* 200 */       throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParticleTextureIndex(int particleTextureIndex) {
/* 206 */     if (getFXLayer() != 0)
/*     */     {
/* 208 */       throw new RuntimeException("Invalid call to Particle.setMiscTex");
/*     */     }
/*     */ 
/*     */     
/* 212 */     this.particleTextureIndexX = particleTextureIndex % 16;
/* 213 */     this.particleTextureIndexY = particleTextureIndex / 16;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextTextureIndexX() {
/* 219 */     this.particleTextureIndexX++;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackWithItem() {
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 229 */     return getClass().getSimpleName() + ", Pos (" + this.posX + "," + this.posY + "," + this.posZ + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.particleAge;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */