/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityPainting;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class RenderPainting extends Render<EntityPainting> {
/*  16 */   private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
/*     */ 
/*     */   
/*     */   public RenderPainting(RenderManager renderManagerIn) {
/*  20 */     super(renderManagerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRender(EntityPainting entity, double x, double y, double z, float entityYaw, float partialTicks) {
/*  25 */     GlStateManager.pushMatrix();
/*  26 */     GlStateManager.translate(x, y, z);
/*  27 */     GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
/*  28 */     GlStateManager.enableRescaleNormal();
/*  29 */     bindEntityTexture(entity);
/*  30 */     EntityPainting.EnumArt entitypainting$enumart = entity.art;
/*  31 */     float f = 0.0625F;
/*  32 */     GlStateManager.scale(f, f, f);
/*  33 */     renderPainting(entity, entitypainting$enumart.sizeX, entitypainting$enumart.sizeY, entitypainting$enumart.offsetX, entitypainting$enumart.offsetY);
/*  34 */     GlStateManager.disableRescaleNormal();
/*  35 */     GlStateManager.popMatrix();
/*  36 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(EntityPainting entity) {
/*  41 */     return KRISTOFFER_PAINTING_TEXTURE;
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderPainting(EntityPainting painting, int width, int height, int textureU, int textureV) {
/*  46 */     float f = -width / 2.0F;
/*  47 */     float f1 = -height / 2.0F;
/*  48 */     float f2 = 0.5F;
/*  49 */     float f3 = 0.75F;
/*  50 */     float f4 = 0.8125F;
/*  51 */     float f5 = 0.0F;
/*  52 */     float f6 = 0.0625F;
/*  53 */     float f7 = 0.75F;
/*  54 */     float f8 = 0.8125F;
/*  55 */     float f9 = 0.001953125F;
/*  56 */     float f10 = 0.001953125F;
/*  57 */     float f11 = 0.7519531F;
/*  58 */     float f12 = 0.7519531F;
/*  59 */     float f13 = 0.0F;
/*  60 */     float f14 = 0.0625F;
/*     */     
/*  62 */     for (int i = 0; i < width / 16; i++) {
/*     */       
/*  64 */       for (int j = 0; j < height / 16; j++) {
/*     */         
/*  66 */         float f15 = f + ((i + 1) * 16);
/*  67 */         float f16 = f + (i * 16);
/*  68 */         float f17 = f1 + ((j + 1) * 16);
/*  69 */         float f18 = f1 + (j * 16);
/*  70 */         setLightmap(painting, (f15 + f16) / 2.0F, (f17 + f18) / 2.0F);
/*  71 */         float f19 = (textureU + width - i * 16) / 256.0F;
/*  72 */         float f20 = (textureU + width - (i + 1) * 16) / 256.0F;
/*  73 */         float f21 = (textureV + height - j * 16) / 256.0F;
/*  74 */         float f22 = (textureV + height - (j + 1) * 16) / 256.0F;
/*  75 */         Tessellator tessellator = Tessellator.getInstance();
/*  76 */         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  77 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
/*  78 */         worldrenderer.pos(f15, f18, -f2).tex(f20, f21).normal(0.0F, 0.0F, -1.0F).endVertex();
/*  79 */         worldrenderer.pos(f16, f18, -f2).tex(f19, f21).normal(0.0F, 0.0F, -1.0F).endVertex();
/*  80 */         worldrenderer.pos(f16, f17, -f2).tex(f19, f22).normal(0.0F, 0.0F, -1.0F).endVertex();
/*  81 */         worldrenderer.pos(f15, f17, -f2).tex(f20, f22).normal(0.0F, 0.0F, -1.0F).endVertex();
/*  82 */         worldrenderer.pos(f15, f17, f2).tex(f3, f5).normal(0.0F, 0.0F, 1.0F).endVertex();
/*  83 */         worldrenderer.pos(f16, f17, f2).tex(f4, f5).normal(0.0F, 0.0F, 1.0F).endVertex();
/*  84 */         worldrenderer.pos(f16, f18, f2).tex(f4, f6).normal(0.0F, 0.0F, 1.0F).endVertex();
/*  85 */         worldrenderer.pos(f15, f18, f2).tex(f3, f6).normal(0.0F, 0.0F, 1.0F).endVertex();
/*  86 */         worldrenderer.pos(f15, f17, -f2).tex(f7, f9).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  87 */         worldrenderer.pos(f16, f17, -f2).tex(f8, f9).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  88 */         worldrenderer.pos(f16, f17, f2).tex(f8, f10).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  89 */         worldrenderer.pos(f15, f17, f2).tex(f7, f10).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  90 */         worldrenderer.pos(f15, f18, f2).tex(f7, f9).normal(0.0F, -1.0F, 0.0F).endVertex();
/*  91 */         worldrenderer.pos(f16, f18, f2).tex(f8, f9).normal(0.0F, -1.0F, 0.0F).endVertex();
/*  92 */         worldrenderer.pos(f16, f18, -f2).tex(f8, f10).normal(0.0F, -1.0F, 0.0F).endVertex();
/*  93 */         worldrenderer.pos(f15, f18, -f2).tex(f7, f10).normal(0.0F, -1.0F, 0.0F).endVertex();
/*  94 */         worldrenderer.pos(f15, f17, f2).tex(f12, f13).normal(-1.0F, 0.0F, 0.0F).endVertex();
/*  95 */         worldrenderer.pos(f15, f18, f2).tex(f12, f14).normal(-1.0F, 0.0F, 0.0F).endVertex();
/*  96 */         worldrenderer.pos(f15, f18, -f2).tex(f11, f14).normal(-1.0F, 0.0F, 0.0F).endVertex();
/*  97 */         worldrenderer.pos(f15, f17, -f2).tex(f11, f13).normal(-1.0F, 0.0F, 0.0F).endVertex();
/*  98 */         worldrenderer.pos(f16, f17, -f2).tex(f12, f13).normal(1.0F, 0.0F, 0.0F).endVertex();
/*  99 */         worldrenderer.pos(f16, f18, -f2).tex(f12, f14).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 100 */         worldrenderer.pos(f16, f18, f2).tex(f11, f14).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 101 */         worldrenderer.pos(f16, f17, f2).tex(f11, f13).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 102 */         tessellator.draw();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setLightmap(EntityPainting painting, float p_77008_2_, float p_77008_3_) {
/* 109 */     int i = MathHelper.floor_double(painting.posX);
/* 110 */     int j = MathHelper.floor_double(painting.posY + (p_77008_3_ / 16.0F));
/* 111 */     int k = MathHelper.floor_double(painting.posZ);
/* 112 */     EnumFacing enumfacing = painting.facingDirection;
/*     */     
/* 114 */     if (enumfacing == EnumFacing.NORTH)
/*     */     {
/* 116 */       i = MathHelper.floor_double(painting.posX + (p_77008_2_ / 16.0F));
/*     */     }
/*     */     
/* 119 */     if (enumfacing == EnumFacing.WEST)
/*     */     {
/* 121 */       k = MathHelper.floor_double(painting.posZ - (p_77008_2_ / 16.0F));
/*     */     }
/*     */     
/* 124 */     if (enumfacing == EnumFacing.SOUTH)
/*     */     {
/* 126 */       i = MathHelper.floor_double(painting.posX - (p_77008_2_ / 16.0F));
/*     */     }
/*     */     
/* 129 */     if (enumfacing == EnumFacing.EAST)
/*     */     {
/* 131 */       k = MathHelper.floor_double(painting.posZ + (p_77008_2_ / 16.0F));
/*     */     }
/*     */     
/* 134 */     int l = this.renderManager.worldObj.getCombinedLight(new BlockPos(i, j, k), 0);
/* 135 */     int i1 = l % 65536;
/* 136 */     int j1 = l / 65536;
/* 137 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
/* 138 */     GlStateManager.color(1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderPainting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */