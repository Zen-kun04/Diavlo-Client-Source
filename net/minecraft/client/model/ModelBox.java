/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ 
/*     */ 
/*     */ public class ModelBox
/*     */ {
/*     */   private PositionTextureVertex[] vertexPositions;
/*     */   private TexturedQuad[] quadList;
/*     */   public final float posX1;
/*     */   public final float posY1;
/*     */   public final float posZ1;
/*     */   public final float posX2;
/*     */   public final float posY2;
/*     */   public final float posZ2;
/*     */   public String boxName;
/*     */   
/*     */   public ModelBox(ModelRenderer renderer, int p_i46359_2_, int p_i46359_3_, float p_i46359_4_, float p_i46359_5_, float p_i46359_6_, int p_i46359_7_, int p_i46359_8_, int p_i46359_9_, float p_i46359_10_) {
/*  19 */     this(renderer, p_i46359_2_, p_i46359_3_, p_i46359_4_, p_i46359_5_, p_i46359_6_, p_i46359_7_, p_i46359_8_, p_i46359_9_, p_i46359_10_, renderer.mirror);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelBox(ModelRenderer p_i0_1_, int[][] p_i0_2_, float p_i0_3_, float p_i0_4_, float p_i0_5_, float p_i0_6_, float p_i0_7_, float p_i0_8_, float p_i0_9_, boolean p_i0_10_) {
/*  24 */     this.posX1 = p_i0_3_;
/*  25 */     this.posY1 = p_i0_4_;
/*  26 */     this.posZ1 = p_i0_5_;
/*  27 */     this.posX2 = p_i0_3_ + p_i0_6_;
/*  28 */     this.posY2 = p_i0_4_ + p_i0_7_;
/*  29 */     this.posZ2 = p_i0_5_ + p_i0_8_;
/*  30 */     this.vertexPositions = new PositionTextureVertex[8];
/*  31 */     this.quadList = new TexturedQuad[6];
/*  32 */     float f = p_i0_3_ + p_i0_6_;
/*  33 */     float f1 = p_i0_4_ + p_i0_7_;
/*  34 */     float f2 = p_i0_5_ + p_i0_8_;
/*  35 */     p_i0_3_ -= p_i0_9_;
/*  36 */     p_i0_4_ -= p_i0_9_;
/*  37 */     p_i0_5_ -= p_i0_9_;
/*  38 */     f += p_i0_9_;
/*  39 */     f1 += p_i0_9_;
/*  40 */     f2 += p_i0_9_;
/*     */     
/*  42 */     if (p_i0_10_) {
/*     */       
/*  44 */       float f3 = f;
/*  45 */       f = p_i0_3_;
/*  46 */       p_i0_3_ = f3;
/*     */     } 
/*     */     
/*  49 */     PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i0_3_, p_i0_4_, p_i0_5_, 0.0F, 0.0F);
/*  50 */     PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, p_i0_4_, p_i0_5_, 0.0F, 8.0F);
/*  51 */     PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1, p_i0_5_, 8.0F, 8.0F);
/*  52 */     PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(p_i0_3_, f1, p_i0_5_, 8.0F, 0.0F);
/*  53 */     PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(p_i0_3_, p_i0_4_, f2, 0.0F, 0.0F);
/*  54 */     PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, p_i0_4_, f2, 0.0F, 8.0F);
/*  55 */     PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
/*  56 */     PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(p_i0_3_, f1, f2, 8.0F, 0.0F);
/*  57 */     this.vertexPositions[0] = positiontexturevertex7;
/*  58 */     this.vertexPositions[1] = positiontexturevertex;
/*  59 */     this.vertexPositions[2] = positiontexturevertex1;
/*  60 */     this.vertexPositions[3] = positiontexturevertex2;
/*  61 */     this.vertexPositions[4] = positiontexturevertex3;
/*  62 */     this.vertexPositions[5] = positiontexturevertex4;
/*  63 */     this.vertexPositions[6] = positiontexturevertex5;
/*  64 */     this.vertexPositions[7] = positiontexturevertex6;
/*  65 */     this.quadList[0] = makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5 }, p_i0_2_[4], false, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
/*  66 */     this.quadList[1] = makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2 }, p_i0_2_[5], false, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
/*  67 */     this.quadList[2] = makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex }, p_i0_2_[1], true, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
/*  68 */     this.quadList[3] = makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5 }, p_i0_2_[0], true, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
/*  69 */     this.quadList[4] = makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1 }, p_i0_2_[2], false, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
/*  70 */     this.quadList[5] = makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6 }, p_i0_2_[3], false, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
/*     */     
/*  72 */     if (p_i0_10_)
/*     */     {
/*  74 */       for (TexturedQuad texturedquad : this.quadList)
/*     */       {
/*  76 */         texturedquad.flipFace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private TexturedQuad makeTexturedQuad(PositionTextureVertex[] p_makeTexturedQuad_1_, int[] p_makeTexturedQuad_2_, boolean p_makeTexturedQuad_3_, float p_makeTexturedQuad_4_, float p_makeTexturedQuad_5_) {
/*  83 */     return (p_makeTexturedQuad_2_ == null) ? null : (p_makeTexturedQuad_3_ ? new TexturedQuad(p_makeTexturedQuad_1_, p_makeTexturedQuad_2_[2], p_makeTexturedQuad_2_[3], p_makeTexturedQuad_2_[0], p_makeTexturedQuad_2_[1], p_makeTexturedQuad_4_, p_makeTexturedQuad_5_) : new TexturedQuad(p_makeTexturedQuad_1_, p_makeTexturedQuad_2_[0], p_makeTexturedQuad_2_[1], p_makeTexturedQuad_2_[2], p_makeTexturedQuad_2_[3], p_makeTexturedQuad_4_, p_makeTexturedQuad_5_));
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelBox(ModelRenderer renderer, int textureX, int textureY, float p_i46301_4_, float p_i46301_5_, float p_i46301_6_, int p_i46301_7_, int p_i46301_8_, int p_i46301_9_, float p_i46301_10_, boolean p_i46301_11_) {
/*  88 */     this.posX1 = p_i46301_4_;
/*  89 */     this.posY1 = p_i46301_5_;
/*  90 */     this.posZ1 = p_i46301_6_;
/*  91 */     this.posX2 = p_i46301_4_ + p_i46301_7_;
/*  92 */     this.posY2 = p_i46301_5_ + p_i46301_8_;
/*  93 */     this.posZ2 = p_i46301_6_ + p_i46301_9_;
/*  94 */     this.vertexPositions = new PositionTextureVertex[8];
/*  95 */     this.quadList = new TexturedQuad[6];
/*  96 */     float f = p_i46301_4_ + p_i46301_7_;
/*  97 */     float f1 = p_i46301_5_ + p_i46301_8_;
/*  98 */     float f2 = p_i46301_6_ + p_i46301_9_;
/*  99 */     p_i46301_4_ -= p_i46301_10_;
/* 100 */     p_i46301_5_ -= p_i46301_10_;
/* 101 */     p_i46301_6_ -= p_i46301_10_;
/* 102 */     f += p_i46301_10_;
/* 103 */     f1 += p_i46301_10_;
/* 104 */     f2 += p_i46301_10_;
/*     */     
/* 106 */     if (p_i46301_11_) {
/*     */       
/* 108 */       float f3 = f;
/* 109 */       f = p_i46301_4_;
/* 110 */       p_i46301_4_ = f3;
/*     */     } 
/*     */     
/* 113 */     PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, p_i46301_6_, 0.0F, 0.0F);
/* 114 */     PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, p_i46301_5_, p_i46301_6_, 0.0F, 8.0F);
/* 115 */     PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1, p_i46301_6_, 8.0F, 8.0F);
/* 116 */     PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(p_i46301_4_, f1, p_i46301_6_, 8.0F, 0.0F);
/* 117 */     PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, f2, 0.0F, 0.0F);
/* 118 */     PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, p_i46301_5_, f2, 0.0F, 8.0F);
/* 119 */     PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
/* 120 */     PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(p_i46301_4_, f1, f2, 8.0F, 0.0F);
/* 121 */     this.vertexPositions[0] = positiontexturevertex7;
/* 122 */     this.vertexPositions[1] = positiontexturevertex;
/* 123 */     this.vertexPositions[2] = positiontexturevertex1;
/* 124 */     this.vertexPositions[3] = positiontexturevertex2;
/* 125 */     this.vertexPositions[4] = positiontexturevertex3;
/* 126 */     this.vertexPositions[5] = positiontexturevertex4;
/* 127 */     this.vertexPositions[6] = positiontexturevertex5;
/* 128 */     this.vertexPositions[7] = positiontexturevertex6;
/* 129 */     this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5 }, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
/* 130 */     this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2 }, textureX, textureY + p_i46301_9_, textureX + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
/* 131 */     this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex }, textureX + p_i46301_9_, textureY, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, renderer.textureWidth, renderer.textureHeight);
/* 132 */     this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5 }, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_7_, textureY, renderer.textureWidth, renderer.textureHeight);
/* 133 */     this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1 }, textureX + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
/* 134 */     this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6 }, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
/*     */     
/* 136 */     if (p_i46301_11_)
/*     */     {
/* 138 */       for (int i = 0; i < this.quadList.length; i++)
/*     */       {
/* 140 */         this.quadList[i].flipFace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(WorldRenderer renderer, float scale) {
/* 147 */     for (int i = 0; i < this.quadList.length; i++) {
/*     */       
/* 149 */       TexturedQuad texturedquad = this.quadList[i];
/*     */       
/* 151 */       if (texturedquad != null)
/*     */       {
/* 153 */         texturedquad.draw(renderer, scale);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelBox setBoxName(String name) {
/* 160 */     this.boxName = name;
/* 161 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */