/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelMinecart;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class RenderMinecart<T extends EntityMinecart> extends Render<T> {
/*  16 */   private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");
/*  17 */   protected ModelBase modelMinecart = (ModelBase)new ModelMinecart();
/*     */ 
/*     */   
/*     */   public RenderMinecart(RenderManager renderManagerIn) {
/*  21 */     super(renderManagerIn);
/*  22 */     this.shadowSize = 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
/*  27 */     GlStateManager.pushMatrix();
/*  28 */     bindEntityTexture(entity);
/*  29 */     long i = entity.getEntityId() * 493286711L;
/*  30 */     i = i * i * 4392167121L + i * 98761L;
/*  31 */     float f = (((float)(i >> 16L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  32 */     float f1 = (((float)(i >> 20L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  33 */     float f2 = (((float)(i >> 24L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  34 */     GlStateManager.translate(f, f1, f2);
/*  35 */     double d0 = ((EntityMinecart)entity).lastTickPosX + (((EntityMinecart)entity).posX - ((EntityMinecart)entity).lastTickPosX) * partialTicks;
/*  36 */     double d1 = ((EntityMinecart)entity).lastTickPosY + (((EntityMinecart)entity).posY - ((EntityMinecart)entity).lastTickPosY) * partialTicks;
/*  37 */     double d2 = ((EntityMinecart)entity).lastTickPosZ + (((EntityMinecart)entity).posZ - ((EntityMinecart)entity).lastTickPosZ) * partialTicks;
/*  38 */     double d3 = 0.30000001192092896D;
/*  39 */     Vec3 vec3 = entity.func_70489_a(d0, d1, d2);
/*  40 */     float f3 = ((EntityMinecart)entity).prevRotationPitch + (((EntityMinecart)entity).rotationPitch - ((EntityMinecart)entity).prevRotationPitch) * partialTicks;
/*     */     
/*  42 */     if (vec3 != null) {
/*     */       
/*  44 */       Vec3 vec31 = entity.func_70495_a(d0, d1, d2, d3);
/*  45 */       Vec3 vec32 = entity.func_70495_a(d0, d1, d2, -d3);
/*     */       
/*  47 */       if (vec31 == null)
/*     */       {
/*  49 */         vec31 = vec3;
/*     */       }
/*     */       
/*  52 */       if (vec32 == null)
/*     */       {
/*  54 */         vec32 = vec3;
/*     */       }
/*     */       
/*  57 */       x += vec3.xCoord - d0;
/*  58 */       y += (vec31.yCoord + vec32.yCoord) / 2.0D - d1;
/*  59 */       z += vec3.zCoord - d2;
/*  60 */       Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);
/*     */       
/*  62 */       if (vec33.lengthVector() != 0.0D) {
/*     */         
/*  64 */         vec33 = vec33.normalize();
/*  65 */         entityYaw = (float)(Math.atan2(vec33.zCoord, vec33.xCoord) * 180.0D / Math.PI);
/*  66 */         f3 = (float)(Math.atan(vec33.yCoord) * 73.0D);
/*     */       } 
/*     */     } 
/*     */     
/*  70 */     GlStateManager.translate((float)x, (float)y + 0.375F, (float)z);
/*  71 */     GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
/*  72 */     GlStateManager.rotate(-f3, 0.0F, 0.0F, 1.0F);
/*  73 */     float f5 = entity.getRollingAmplitude() - partialTicks;
/*  74 */     float f6 = entity.getDamage() - partialTicks;
/*     */     
/*  76 */     if (f6 < 0.0F)
/*     */     {
/*  78 */       f6 = 0.0F;
/*     */     }
/*     */     
/*  81 */     if (f5 > 0.0F)
/*     */     {
/*  83 */       GlStateManager.rotate(MathHelper.sin(f5) * f5 * f6 / 10.0F * entity.getRollingDirection(), 1.0F, 0.0F, 0.0F);
/*     */     }
/*     */     
/*  86 */     int j = entity.getDisplayTileOffset();
/*  87 */     IBlockState iblockstate = entity.getDisplayTile();
/*     */     
/*  89 */     if (iblockstate.getBlock().getRenderType() != -1) {
/*     */       
/*  91 */       GlStateManager.pushMatrix();
/*  92 */       bindTexture(TextureMap.locationBlocksTexture);
/*  93 */       float f4 = 0.75F;
/*  94 */       GlStateManager.scale(f4, f4, f4);
/*  95 */       GlStateManager.translate(-0.5F, (j - 8) / 16.0F, 0.5F);
/*  96 */       func_180560_a(entity, partialTicks, iblockstate);
/*  97 */       GlStateManager.popMatrix();
/*  98 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  99 */       bindEntityTexture(entity);
/*     */     } 
/*     */     
/* 102 */     GlStateManager.scale(-1.0F, -1.0F, 1.0F);
/* 103 */     this.modelMinecart.render((Entity)entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/* 104 */     GlStateManager.popMatrix();
/* 105 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(T entity) {
/* 110 */     return minecartTextures;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_180560_a(T minecart, float partialTicks, IBlockState state) {
/* 115 */     GlStateManager.pushMatrix();
/* 116 */     Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state, minecart.getBrightness(partialTicks));
/* 117 */     GlStateManager.popMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */