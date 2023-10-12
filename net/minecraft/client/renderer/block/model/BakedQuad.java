/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraftforge.client.model.pipeline.IVertexConsumer;
/*     */ import net.minecraftforge.client.model.pipeline.IVertexProducer;
/*     */ import net.optifine.model.QuadBounds;
/*     */ import net.optifine.reflect.Reflector;
/*     */ 
/*     */ public class BakedQuad
/*     */   implements IVertexProducer {
/*     */   protected int[] vertexData;
/*     */   protected final int tintIndex;
/*     */   protected EnumFacing face;
/*     */   protected TextureAtlasSprite sprite;
/*  18 */   private int[] vertexDataSingle = null;
/*     */   
/*     */   private QuadBounds quadBounds;
/*     */   private boolean quadEmissiveChecked;
/*     */   private BakedQuad quadEmissive;
/*     */   
/*     */   public BakedQuad(int[] p_i3_1_, int p_i3_2_, EnumFacing p_i3_3_, TextureAtlasSprite p_i3_4_) {
/*  25 */     this.vertexData = p_i3_1_;
/*  26 */     this.tintIndex = p_i3_2_;
/*  27 */     this.face = p_i3_3_;
/*  28 */     this.sprite = p_i3_4_;
/*  29 */     fixVertexData();
/*     */   }
/*     */ 
/*     */   
/*     */   public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn) {
/*  34 */     this.vertexData = vertexDataIn;
/*  35 */     this.tintIndex = tintIndexIn;
/*  36 */     this.face = faceIn;
/*  37 */     fixVertexData();
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite getSprite() {
/*  42 */     if (this.sprite == null)
/*     */     {
/*  44 */       this.sprite = getSpriteByUv(getVertexData());
/*     */     }
/*     */     
/*  47 */     return this.sprite;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getVertexData() {
/*  52 */     fixVertexData();
/*  53 */     return this.vertexData;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasTintIndex() {
/*  58 */     return (this.tintIndex != -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTintIndex() {
/*  63 */     return this.tintIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumFacing getFace() {
/*  68 */     if (this.face == null)
/*     */     {
/*  70 */       this.face = FaceBakery.getFacingFromVertexData(getVertexData());
/*     */     }
/*     */     
/*  73 */     return this.face;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getVertexDataSingle() {
/*  78 */     if (this.vertexDataSingle == null)
/*     */     {
/*  80 */       this.vertexDataSingle = makeVertexDataSingle(getVertexData(), getSprite());
/*     */     }
/*     */     
/*  83 */     return this.vertexDataSingle;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[] makeVertexDataSingle(int[] p_makeVertexDataSingle_0_, TextureAtlasSprite p_makeVertexDataSingle_1_) {
/*  88 */     int[] aint = (int[])p_makeVertexDataSingle_0_.clone();
/*  89 */     int i = aint.length / 4;
/*     */     
/*  91 */     for (int j = 0; j < 4; j++) {
/*     */       
/*  93 */       int k = j * i;
/*  94 */       float f = Float.intBitsToFloat(aint[k + 4]);
/*  95 */       float f1 = Float.intBitsToFloat(aint[k + 4 + 1]);
/*  96 */       float f2 = p_makeVertexDataSingle_1_.toSingleU(f);
/*  97 */       float f3 = p_makeVertexDataSingle_1_.toSingleV(f1);
/*  98 */       aint[k + 4] = Float.floatToRawIntBits(f2);
/*  99 */       aint[k + 4 + 1] = Float.floatToRawIntBits(f3);
/*     */     } 
/*     */     
/* 102 */     return aint;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pipe(IVertexConsumer p_pipe_1_) {
/* 107 */     Reflector.callVoid(Reflector.LightUtil_putBakedQuad, new Object[] { p_pipe_1_, this });
/*     */   }
/*     */ 
/*     */   
/*     */   private static TextureAtlasSprite getSpriteByUv(int[] p_getSpriteByUv_0_) {
/* 112 */     float f = 1.0F;
/* 113 */     float f1 = 1.0F;
/* 114 */     float f2 = 0.0F;
/* 115 */     float f3 = 0.0F;
/* 116 */     int i = p_getSpriteByUv_0_.length / 4;
/*     */     
/* 118 */     for (int j = 0; j < 4; j++) {
/*     */       
/* 120 */       int k = j * i;
/* 121 */       float f4 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4]);
/* 122 */       float f5 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4 + 1]);
/* 123 */       f = Math.min(f, f4);
/* 124 */       f1 = Math.min(f1, f5);
/* 125 */       f2 = Math.max(f2, f4);
/* 126 */       f3 = Math.max(f3, f5);
/*     */     } 
/*     */     
/* 129 */     float f6 = (f + f2) / 2.0F;
/* 130 */     float f7 = (f1 + f3) / 2.0F;
/* 131 */     TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV(f6, f7);
/* 132 */     return textureatlassprite;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fixVertexData() {
/* 137 */     if (Config.isShaders()) {
/*     */       
/* 139 */       if (this.vertexData.length == 28)
/*     */       {
/* 141 */         this.vertexData = expandVertexData(this.vertexData);
/*     */       }
/*     */     }
/* 144 */     else if (this.vertexData.length == 56) {
/*     */       
/* 146 */       this.vertexData = compactVertexData(this.vertexData);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[] expandVertexData(int[] p_expandVertexData_0_) {
/* 152 */     int i = p_expandVertexData_0_.length / 4;
/* 153 */     int j = i * 2;
/* 154 */     int[] aint = new int[j * 4];
/*     */     
/* 156 */     for (int k = 0; k < 4; k++)
/*     */     {
/* 158 */       System.arraycopy(p_expandVertexData_0_, k * i, aint, k * j, i);
/*     */     }
/*     */     
/* 161 */     return aint;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[] compactVertexData(int[] p_compactVertexData_0_) {
/* 166 */     int i = p_compactVertexData_0_.length / 4;
/* 167 */     int j = i / 2;
/* 168 */     int[] aint = new int[j * 4];
/*     */     
/* 170 */     for (int k = 0; k < 4; k++)
/*     */     {
/* 172 */       System.arraycopy(p_compactVertexData_0_, k * i, aint, k * j, j);
/*     */     }
/*     */     
/* 175 */     return aint;
/*     */   }
/*     */ 
/*     */   
/*     */   public QuadBounds getQuadBounds() {
/* 180 */     if (this.quadBounds == null)
/*     */     {
/* 182 */       this.quadBounds = new QuadBounds(getVertexData());
/*     */     }
/*     */     
/* 185 */     return this.quadBounds;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMidX() {
/* 190 */     QuadBounds quadbounds = getQuadBounds();
/* 191 */     return (quadbounds.getMaxX() + quadbounds.getMinX()) / 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMidY() {
/* 196 */     QuadBounds quadbounds = getQuadBounds();
/* 197 */     return ((quadbounds.getMaxY() + quadbounds.getMinY()) / 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMidZ() {
/* 202 */     QuadBounds quadbounds = getQuadBounds();
/* 203 */     return ((quadbounds.getMaxZ() + quadbounds.getMinZ()) / 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFaceQuad() {
/* 208 */     QuadBounds quadbounds = getQuadBounds();
/* 209 */     return quadbounds.isFaceQuad(this.face);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullQuad() {
/* 214 */     QuadBounds quadbounds = getQuadBounds();
/* 215 */     return quadbounds.isFullQuad(this.face);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullFaceQuad() {
/* 220 */     return (isFullQuad() && isFaceQuad());
/*     */   }
/*     */ 
/*     */   
/*     */   public BakedQuad getQuadEmissive() {
/* 225 */     if (this.quadEmissiveChecked)
/*     */     {
/* 227 */       return this.quadEmissive;
/*     */     }
/*     */ 
/*     */     
/* 231 */     if (this.quadEmissive == null && this.sprite != null && this.sprite.spriteEmissive != null)
/*     */     {
/* 233 */       this.quadEmissive = new BreakingFour(this, this.sprite.spriteEmissive);
/*     */     }
/*     */     
/* 236 */     this.quadEmissiveChecked = true;
/* 237 */     return this.quadEmissive;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 243 */     return "vertex: " + (this.vertexData.length / 7) + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\block\model\BakedQuad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */