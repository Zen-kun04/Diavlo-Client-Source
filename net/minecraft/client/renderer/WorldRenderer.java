/*      */ package net.minecraft.client.renderer;
/*      */ 
/*      */ import com.google.common.primitives.Floats;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.ShortBuffer;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Comparator;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*      */ import net.minecraft.client.renderer.vertex.VertexFormatElement;
/*      */ import net.minecraft.src.Config;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumWorldBlockLayer;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.optifine.SmartAnimations;
/*      */ import net.optifine.render.RenderEnv;
/*      */ import net.optifine.shaders.SVertexBuilder;
/*      */ import net.optifine.util.TextureUtils;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ 
/*      */ public class WorldRenderer
/*      */ {
/*      */   private ByteBuffer byteBuffer;
/*      */   public IntBuffer rawIntBuffer;
/*      */   private ShortBuffer rawShortBuffer;
/*      */   public FloatBuffer rawFloatBuffer;
/*      */   public int vertexCount;
/*      */   private VertexFormatElement vertexFormatElement;
/*      */   private int vertexFormatIndex;
/*      */   private boolean noColor;
/*      */   public int drawMode;
/*      */   private double xOffset;
/*      */   private double yOffset;
/*      */   private double zOffset;
/*      */   private VertexFormat vertexFormat;
/*      */   private boolean isDrawing;
/*   43 */   private EnumWorldBlockLayer blockLayer = null;
/*   44 */   private boolean[] drawnIcons = new boolean[256];
/*   45 */   private TextureAtlasSprite[] quadSprites = null;
/*   46 */   private TextureAtlasSprite[] quadSpritesPrev = null;
/*   47 */   private TextureAtlasSprite quadSprite = null;
/*      */   public SVertexBuilder sVertexBuilder;
/*   49 */   public RenderEnv renderEnv = null;
/*   50 */   public BitSet animatedSprites = null;
/*   51 */   public BitSet animatedSpritesCached = new BitSet();
/*      */   
/*      */   private boolean modeTriangles = false;
/*      */   private ByteBuffer byteBufferTriangles;
/*      */   
/*      */   public WorldRenderer(int bufferSizeIn) {
/*   57 */     this.byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn * 4);
/*   58 */     this.rawIntBuffer = this.byteBuffer.asIntBuffer();
/*   59 */     this.rawShortBuffer = this.byteBuffer.asShortBuffer();
/*   60 */     this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
/*   61 */     SVertexBuilder.initVertexBuilder(this);
/*      */   }
/*      */ 
/*      */   
/*      */   private void growBuffer(int p_181670_1_) {
/*   66 */     if (p_181670_1_ > this.rawIntBuffer.remaining()) {
/*      */       
/*   68 */       int i = this.byteBuffer.capacity();
/*   69 */       int j = i % 2097152;
/*   70 */       int k = j + (((this.rawIntBuffer.position() + p_181670_1_) * 4 - j) / 2097152 + 1) * 2097152;
/*   71 */       LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + i + " bytes, new size " + k + " bytes.");
/*   72 */       int l = this.rawIntBuffer.position();
/*   73 */       ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(k);
/*   74 */       this.byteBuffer.position(0);
/*   75 */       bytebuffer.put(this.byteBuffer);
/*   76 */       bytebuffer.rewind();
/*   77 */       this.byteBuffer = bytebuffer;
/*   78 */       this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
/*   79 */       this.rawIntBuffer = this.byteBuffer.asIntBuffer();
/*   80 */       this.rawIntBuffer.position(l);
/*   81 */       this.rawShortBuffer = this.byteBuffer.asShortBuffer();
/*   82 */       this.rawShortBuffer.position(l << 1);
/*      */       
/*   84 */       if (this.quadSprites != null) {
/*      */         
/*   86 */         TextureAtlasSprite[] atextureatlassprite = this.quadSprites;
/*   87 */         int i1 = getBufferQuadSize();
/*   88 */         this.quadSprites = new TextureAtlasSprite[i1];
/*   89 */         System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, Math.min(atextureatlassprite.length, this.quadSprites.length));
/*   90 */         this.quadSpritesPrev = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sortVertexData(float p_181674_1_, float p_181674_2_, float p_181674_3_) {
/*   97 */     int i = this.vertexCount / 4;
/*   98 */     final float[] afloat = new float[i];
/*      */     
/*  100 */     for (int j = 0; j < i; j++)
/*      */     {
/*  102 */       afloat[j] = getDistanceSq(this.rawFloatBuffer, (float)(p_181674_1_ + this.xOffset), (float)(p_181674_2_ + this.yOffset), (float)(p_181674_3_ + this.zOffset), this.vertexFormat.getIntegerSize(), j * this.vertexFormat.getNextOffset());
/*      */     }
/*      */     
/*  105 */     Integer[] ainteger = new Integer[i];
/*      */     
/*  107 */     for (int k = 0; k < ainteger.length; k++)
/*      */     {
/*  109 */       ainteger[k] = Integer.valueOf(k);
/*      */     }
/*      */     
/*  112 */     Arrays.sort(ainteger, new Comparator<Integer>()
/*      */         {
/*      */           public int compare(Integer p_compare_1_, Integer p_compare_2_)
/*      */           {
/*  116 */             return Floats.compare(afloat[p_compare_2_.intValue()], afloat[p_compare_1_.intValue()]);
/*      */           }
/*      */         });
/*  119 */     BitSet bitset = new BitSet();
/*  120 */     int l = this.vertexFormat.getNextOffset();
/*  121 */     int[] aint = new int[l];
/*      */     
/*  123 */     for (int l1 = 0; (l1 = bitset.nextClearBit(l1)) < ainteger.length; l1++) {
/*      */       
/*  125 */       int i1 = ainteger[l1].intValue();
/*      */       
/*  127 */       if (i1 != l1) {
/*      */         
/*  129 */         this.rawIntBuffer.limit(i1 * l + l);
/*  130 */         this.rawIntBuffer.position(i1 * l);
/*  131 */         this.rawIntBuffer.get(aint);
/*  132 */         int j1 = i1;
/*      */         int k1;
/*  134 */         for (k1 = ainteger[i1].intValue(); j1 != l1; k1 = ainteger[k1].intValue()) {
/*      */           
/*  136 */           this.rawIntBuffer.limit(k1 * l + l);
/*  137 */           this.rawIntBuffer.position(k1 * l);
/*  138 */           IntBuffer intbuffer = this.rawIntBuffer.slice();
/*  139 */           this.rawIntBuffer.limit(j1 * l + l);
/*  140 */           this.rawIntBuffer.position(j1 * l);
/*  141 */           this.rawIntBuffer.put(intbuffer);
/*  142 */           bitset.set(j1);
/*  143 */           j1 = k1;
/*      */         } 
/*      */         
/*  146 */         this.rawIntBuffer.limit(l1 * l + l);
/*  147 */         this.rawIntBuffer.position(l1 * l);
/*  148 */         this.rawIntBuffer.put(aint);
/*      */       } 
/*      */       
/*  151 */       bitset.set(l1);
/*      */     } 
/*      */     
/*  154 */     this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
/*  155 */     this.rawIntBuffer.position(getBufferSize());
/*      */     
/*  157 */     if (this.quadSprites != null) {
/*      */       
/*  159 */       TextureAtlasSprite[] atextureatlassprite = new TextureAtlasSprite[this.vertexCount / 4];
/*  160 */       int i2 = this.vertexFormat.getNextOffset() / 4 * 4;
/*      */       
/*  162 */       for (int j2 = 0; j2 < ainteger.length; j2++) {
/*      */         
/*  164 */         int k2 = ainteger[j2].intValue();
/*  165 */         atextureatlassprite[j2] = this.quadSprites[k2];
/*      */       } 
/*      */       
/*  168 */       System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public State getVertexState() {
/*  174 */     this.rawIntBuffer.rewind();
/*  175 */     int i = getBufferSize();
/*  176 */     this.rawIntBuffer.limit(i);
/*  177 */     int[] aint = new int[i];
/*  178 */     this.rawIntBuffer.get(aint);
/*  179 */     this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
/*  180 */     this.rawIntBuffer.position(i);
/*  181 */     TextureAtlasSprite[] atextureatlassprite = null;
/*      */     
/*  183 */     if (this.quadSprites != null) {
/*      */       
/*  185 */       int j = this.vertexCount / 4;
/*  186 */       atextureatlassprite = new TextureAtlasSprite[j];
/*  187 */       System.arraycopy(this.quadSprites, 0, atextureatlassprite, 0, j);
/*      */     } 
/*      */     
/*  190 */     return new State(aint, new VertexFormat(this.vertexFormat), atextureatlassprite);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBufferSize() {
/*  195 */     return this.vertexCount * this.vertexFormat.getIntegerSize();
/*      */   }
/*      */ 
/*      */   
/*      */   private static float getDistanceSq(FloatBuffer p_181665_0_, float p_181665_1_, float p_181665_2_, float p_181665_3_, int p_181665_4_, int p_181665_5_) {
/*  200 */     float f = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 0);
/*  201 */     float f1 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 1);
/*  202 */     float f2 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 2);
/*  203 */     float f3 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 0);
/*  204 */     float f4 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 1);
/*  205 */     float f5 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 2);
/*  206 */     float f6 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 0);
/*  207 */     float f7 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 1);
/*  208 */     float f8 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 2);
/*  209 */     float f9 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 0);
/*  210 */     float f10 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 1);
/*  211 */     float f11 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 2);
/*  212 */     float f12 = (f + f3 + f6 + f9) * 0.25F - p_181665_1_;
/*  213 */     float f13 = (f1 + f4 + f7 + f10) * 0.25F - p_181665_2_;
/*  214 */     float f14 = (f2 + f5 + f8 + f11) * 0.25F - p_181665_3_;
/*  215 */     return f12 * f12 + f13 * f13 + f14 * f14;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setVertexState(State state) {
/*  220 */     this.rawIntBuffer.clear();
/*  221 */     growBuffer((state.getRawBuffer()).length);
/*  222 */     this.rawIntBuffer.put(state.getRawBuffer());
/*  223 */     this.vertexCount = state.getVertexCount();
/*  224 */     this.vertexFormat = new VertexFormat(state.getVertexFormat());
/*      */     
/*  226 */     if (state.stateQuadSprites != null) {
/*      */       
/*  228 */       if (this.quadSprites == null)
/*      */       {
/*  230 */         this.quadSprites = this.quadSpritesPrev;
/*      */       }
/*      */       
/*  233 */       if (this.quadSprites == null || this.quadSprites.length < getBufferQuadSize())
/*      */       {
/*  235 */         this.quadSprites = new TextureAtlasSprite[getBufferQuadSize()];
/*      */       }
/*      */       
/*  238 */       TextureAtlasSprite[] atextureatlassprite = state.stateQuadSprites;
/*  239 */       System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
/*      */     }
/*      */     else {
/*      */       
/*  243 */       if (this.quadSprites != null)
/*      */       {
/*  245 */         this.quadSpritesPrev = this.quadSprites;
/*      */       }
/*      */       
/*  248 */       this.quadSprites = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void reset() {
/*  254 */     this.vertexCount = 0;
/*  255 */     this.vertexFormatElement = null;
/*  256 */     this.vertexFormatIndex = 0;
/*  257 */     this.quadSprite = null;
/*      */     
/*  259 */     if (SmartAnimations.isActive()) {
/*      */       
/*  261 */       if (this.animatedSprites == null)
/*      */       {
/*  263 */         this.animatedSprites = this.animatedSpritesCached;
/*      */       }
/*      */       
/*  266 */       this.animatedSprites.clear();
/*      */     }
/*  268 */     else if (this.animatedSprites != null) {
/*      */       
/*  270 */       this.animatedSprites = null;
/*      */     } 
/*      */     
/*  273 */     this.modeTriangles = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void begin(int glMode, VertexFormat format) {
/*  278 */     if (this.isDrawing)
/*      */     {
/*  280 */       throw new IllegalStateException("Already building!");
/*      */     }
/*      */ 
/*      */     
/*  284 */     this.isDrawing = true;
/*  285 */     reset();
/*  286 */     this.drawMode = glMode;
/*  287 */     this.vertexFormat = format;
/*  288 */     this.vertexFormatElement = format.getElement(this.vertexFormatIndex);
/*  289 */     this.noColor = false;
/*  290 */     this.byteBuffer.limit(this.byteBuffer.capacity());
/*      */     
/*  292 */     if (Config.isShaders())
/*      */     {
/*  294 */       SVertexBuilder.endSetVertexFormat(this);
/*      */     }
/*      */     
/*  297 */     if (Config.isMultiTexture()) {
/*      */       
/*  299 */       if (this.blockLayer != null)
/*      */       {
/*  301 */         if (this.quadSprites == null)
/*      */         {
/*  303 */           this.quadSprites = this.quadSpritesPrev;
/*      */         }
/*      */         
/*  306 */         if (this.quadSprites == null || this.quadSprites.length < getBufferQuadSize())
/*      */         {
/*  308 */           this.quadSprites = new TextureAtlasSprite[getBufferQuadSize()];
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  314 */       if (this.quadSprites != null)
/*      */       {
/*  316 */         this.quadSpritesPrev = this.quadSprites;
/*      */       }
/*      */       
/*  319 */       this.quadSprites = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public WorldRenderer tex(double u, double v) {
/*  326 */     if (this.quadSprite != null && this.quadSprites != null) {
/*      */       
/*  328 */       u = this.quadSprite.toSingleU((float)u);
/*  329 */       v = this.quadSprite.toSingleV((float)v);
/*  330 */       this.quadSprites[this.vertexCount / 4] = this.quadSprite;
/*      */     } 
/*      */     
/*  333 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
/*      */     
/*  335 */     switch (this.vertexFormatElement.getType()) {
/*      */       
/*      */       case FLOAT:
/*  338 */         this.byteBuffer.putFloat(i, (float)u);
/*  339 */         this.byteBuffer.putFloat(i + 4, (float)v);
/*      */         break;
/*      */       
/*      */       case UINT:
/*      */       case INT:
/*  344 */         this.byteBuffer.putInt(i, (int)u);
/*  345 */         this.byteBuffer.putInt(i + 4, (int)v);
/*      */         break;
/*      */       
/*      */       case USHORT:
/*      */       case SHORT:
/*  350 */         this.byteBuffer.putShort(i, (short)(int)v);
/*  351 */         this.byteBuffer.putShort(i + 2, (short)(int)u);
/*      */         break;
/*      */       
/*      */       case UBYTE:
/*      */       case BYTE:
/*  356 */         this.byteBuffer.put(i, (byte)(int)v);
/*  357 */         this.byteBuffer.put(i + 1, (byte)(int)u);
/*      */         break;
/*      */     } 
/*  360 */     nextVertexFormatIndex();
/*  361 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldRenderer lightmap(int p_181671_1_, int p_181671_2_) {
/*  366 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
/*      */     
/*  368 */     switch (this.vertexFormatElement.getType()) {
/*      */       
/*      */       case FLOAT:
/*  371 */         this.byteBuffer.putFloat(i, p_181671_1_);
/*  372 */         this.byteBuffer.putFloat(i + 4, p_181671_2_);
/*      */         break;
/*      */       
/*      */       case UINT:
/*      */       case INT:
/*  377 */         this.byteBuffer.putInt(i, p_181671_1_);
/*  378 */         this.byteBuffer.putInt(i + 4, p_181671_2_);
/*      */         break;
/*      */       
/*      */       case USHORT:
/*      */       case SHORT:
/*  383 */         this.byteBuffer.putShort(i, (short)p_181671_2_);
/*  384 */         this.byteBuffer.putShort(i + 2, (short)p_181671_1_);
/*      */         break;
/*      */       
/*      */       case UBYTE:
/*      */       case BYTE:
/*  389 */         this.byteBuffer.put(i, (byte)p_181671_2_);
/*  390 */         this.byteBuffer.put(i + 1, (byte)p_181671_1_);
/*      */         break;
/*      */     } 
/*  393 */     nextVertexFormatIndex();
/*  394 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_) {
/*  399 */     int i = (this.vertexCount - 4) * this.vertexFormat.getIntegerSize() + this.vertexFormat.getUvOffsetById(1) / 4;
/*  400 */     int j = this.vertexFormat.getNextOffset() >> 2;
/*  401 */     this.rawIntBuffer.put(i, p_178962_1_);
/*  402 */     this.rawIntBuffer.put(i + j, p_178962_2_);
/*  403 */     this.rawIntBuffer.put(i + j * 2, p_178962_3_);
/*  404 */     this.rawIntBuffer.put(i + j * 3, p_178962_4_);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putPosition(double x, double y, double z) {
/*  409 */     int i = this.vertexFormat.getIntegerSize();
/*  410 */     int j = (this.vertexCount - 4) * i;
/*      */     
/*  412 */     for (int k = 0; k < 4; k++) {
/*      */       
/*  414 */       int l = j + k * i;
/*  415 */       int i1 = l + 1;
/*  416 */       int j1 = i1 + 1;
/*  417 */       this.rawIntBuffer.put(l, Float.floatToRawIntBits((float)(x + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(l))));
/*  418 */       this.rawIntBuffer.put(i1, Float.floatToRawIntBits((float)(y + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(i1))));
/*  419 */       this.rawIntBuffer.put(j1, Float.floatToRawIntBits((float)(z + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(j1))));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getColorIndex(int p_78909_1_) {
/*  425 */     return ((this.vertexCount - p_78909_1_) * this.vertexFormat.getNextOffset() + this.vertexFormat.getColorOffset()) / 4;
/*      */   }
/*      */ 
/*      */   
/*      */   public void putColorMultiplier(float red, float green, float blue, int p_178978_4_) {
/*  430 */     int i = getColorIndex(p_178978_4_);
/*  431 */     int j = -1;
/*      */     
/*  433 */     if (!this.noColor) {
/*      */       
/*  435 */       j = this.rawIntBuffer.get(i);
/*      */       
/*  437 */       if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
/*      */         
/*  439 */         int k = (int)((j & 0xFF) * red);
/*  440 */         int l = (int)((j >> 8 & 0xFF) * green);
/*  441 */         int i1 = (int)((j >> 16 & 0xFF) * blue);
/*  442 */         j &= 0xFF000000;
/*  443 */         j = j | i1 << 16 | l << 8 | k;
/*      */       }
/*      */       else {
/*      */         
/*  447 */         int j1 = (int)((j >> 24 & 0xFF) * red);
/*  448 */         int k1 = (int)((j >> 16 & 0xFF) * green);
/*  449 */         int l1 = (int)((j >> 8 & 0xFF) * blue);
/*  450 */         j &= 0xFF;
/*  451 */         j = j | j1 << 24 | k1 << 16 | l1 << 8;
/*      */       } 
/*      */     } 
/*      */     
/*  455 */     this.rawIntBuffer.put(i, j);
/*      */   }
/*      */ 
/*      */   
/*      */   private void putColor(int argb, int p_178988_2_) {
/*  460 */     int i = getColorIndex(p_178988_2_);
/*  461 */     int j = argb >> 16 & 0xFF;
/*  462 */     int k = argb >> 8 & 0xFF;
/*  463 */     int l = argb & 0xFF;
/*  464 */     int i1 = argb >> 24 & 0xFF;
/*  465 */     putColorRGBA(i, j, k, l, i1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putColorRGB_F(float red, float green, float blue, int p_178994_4_) {
/*  470 */     int i = getColorIndex(p_178994_4_);
/*  471 */     int j = MathHelper.clamp_int((int)(red * 255.0F), 0, 255);
/*  472 */     int k = MathHelper.clamp_int((int)(green * 255.0F), 0, 255);
/*  473 */     int l = MathHelper.clamp_int((int)(blue * 255.0F), 0, 255);
/*  474 */     putColorRGBA(i, j, k, l, 255);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putColorRGBA(int index, int red, int p_178972_3_, int p_178972_4_, int p_178972_5_) {
/*  479 */     if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
/*      */       
/*  481 */       this.rawIntBuffer.put(index, p_178972_5_ << 24 | p_178972_4_ << 16 | p_178972_3_ << 8 | red);
/*      */     }
/*      */     else {
/*      */       
/*  485 */       this.rawIntBuffer.put(index, red << 24 | p_178972_3_ << 16 | p_178972_4_ << 8 | p_178972_5_);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void noColor() {
/*  491 */     this.noColor = true;
/*      */   }
/*      */   
/*      */   public WorldRenderer color(float red, float green, float blue, float alpha) {
/*  495 */     return color((int)(red * 255.0F), (int)(green * 255.0F), (int)(blue * 255.0F), (int)(alpha * 255.0F));
/*      */   }
/*      */   
/*      */   public WorldRenderer color(int colorHex) {
/*  499 */     return color(colorHex >> 16 & 0xFF, colorHex >> 8 & 0xFF, colorHex & 0xFF, colorHex >> 24 & 0xFF);
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldRenderer color(int red, int green, int blue, int alpha) {
/*  504 */     if (this.noColor)
/*      */     {
/*  506 */       return this;
/*      */     }
/*      */ 
/*      */     
/*  510 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
/*      */     
/*  512 */     switch (this.vertexFormatElement.getType()) {
/*      */       
/*      */       case FLOAT:
/*  515 */         this.byteBuffer.putFloat(i, red / 255.0F);
/*  516 */         this.byteBuffer.putFloat(i + 4, green / 255.0F);
/*  517 */         this.byteBuffer.putFloat(i + 8, blue / 255.0F);
/*  518 */         this.byteBuffer.putFloat(i + 12, alpha / 255.0F);
/*      */         break;
/*      */       
/*      */       case UINT:
/*      */       case INT:
/*  523 */         this.byteBuffer.putFloat(i, red);
/*  524 */         this.byteBuffer.putFloat(i + 4, green);
/*  525 */         this.byteBuffer.putFloat(i + 8, blue);
/*  526 */         this.byteBuffer.putFloat(i + 12, alpha);
/*      */         break;
/*      */       
/*      */       case USHORT:
/*      */       case SHORT:
/*  531 */         this.byteBuffer.putShort(i, (short)red);
/*  532 */         this.byteBuffer.putShort(i + 2, (short)green);
/*  533 */         this.byteBuffer.putShort(i + 4, (short)blue);
/*  534 */         this.byteBuffer.putShort(i + 6, (short)alpha);
/*      */         break;
/*      */       
/*      */       case UBYTE:
/*      */       case BYTE:
/*  539 */         if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
/*      */           
/*  541 */           this.byteBuffer.put(i, (byte)red);
/*  542 */           this.byteBuffer.put(i + 1, (byte)green);
/*  543 */           this.byteBuffer.put(i + 2, (byte)blue);
/*  544 */           this.byteBuffer.put(i + 3, (byte)alpha);
/*      */           
/*      */           break;
/*      */         } 
/*  548 */         this.byteBuffer.put(i, (byte)alpha);
/*  549 */         this.byteBuffer.put(i + 1, (byte)blue);
/*  550 */         this.byteBuffer.put(i + 2, (byte)green);
/*  551 */         this.byteBuffer.put(i + 3, (byte)red);
/*      */         break;
/*      */     } 
/*      */     
/*  555 */     nextVertexFormatIndex();
/*  556 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addVertexData(int[] vertexData) {
/*  562 */     if (Config.isShaders())
/*      */     {
/*  564 */       SVertexBuilder.beginAddVertexData(this, vertexData);
/*      */     }
/*      */     
/*  567 */     growBuffer(vertexData.length);
/*  568 */     this.rawIntBuffer.position(getBufferSize());
/*  569 */     this.rawIntBuffer.put(vertexData);
/*  570 */     this.vertexCount += vertexData.length / this.vertexFormat.getIntegerSize();
/*      */     
/*  572 */     if (Config.isShaders())
/*      */     {
/*  574 */       SVertexBuilder.endAddVertexData(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void endVertex() {
/*  580 */     this.vertexCount++;
/*  581 */     growBuffer(this.vertexFormat.getIntegerSize());
/*  582 */     this.vertexFormatIndex = 0;
/*  583 */     this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
/*      */     
/*  585 */     if (Config.isShaders())
/*      */     {
/*  587 */       SVertexBuilder.endAddVertex(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldRenderer pos(double x, double y, double z) {
/*  593 */     if (Config.isShaders())
/*      */     {
/*  595 */       SVertexBuilder.beginAddVertex(this);
/*      */     }
/*      */     
/*  598 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
/*      */     
/*  600 */     switch (this.vertexFormatElement.getType()) {
/*      */       
/*      */       case FLOAT:
/*  603 */         this.byteBuffer.putFloat(i, (float)(x + this.xOffset));
/*  604 */         this.byteBuffer.putFloat(i + 4, (float)(y + this.yOffset));
/*  605 */         this.byteBuffer.putFloat(i + 8, (float)(z + this.zOffset));
/*      */         break;
/*      */       
/*      */       case UINT:
/*      */       case INT:
/*  610 */         this.byteBuffer.putInt(i, Float.floatToRawIntBits((float)(x + this.xOffset)));
/*  611 */         this.byteBuffer.putInt(i + 4, Float.floatToRawIntBits((float)(y + this.yOffset)));
/*  612 */         this.byteBuffer.putInt(i + 8, Float.floatToRawIntBits((float)(z + this.zOffset)));
/*      */         break;
/*      */       
/*      */       case USHORT:
/*      */       case SHORT:
/*  617 */         this.byteBuffer.putShort(i, (short)(int)(x + this.xOffset));
/*  618 */         this.byteBuffer.putShort(i + 2, (short)(int)(y + this.yOffset));
/*  619 */         this.byteBuffer.putShort(i + 4, (short)(int)(z + this.zOffset));
/*      */         break;
/*      */       
/*      */       case UBYTE:
/*      */       case BYTE:
/*  624 */         this.byteBuffer.put(i, (byte)(int)(x + this.xOffset));
/*  625 */         this.byteBuffer.put(i + 1, (byte)(int)(y + this.yOffset));
/*  626 */         this.byteBuffer.put(i + 2, (byte)(int)(z + this.zOffset));
/*      */         break;
/*      */     } 
/*  629 */     nextVertexFormatIndex();
/*  630 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public void putNormal(float x, float y, float z) {
/*  635 */     int i = (byte)(int)(x * 127.0F) & 0xFF;
/*  636 */     int j = (byte)(int)(y * 127.0F) & 0xFF;
/*  637 */     int k = (byte)(int)(z * 127.0F) & 0xFF;
/*  638 */     int l = i | j << 8 | k << 16;
/*  639 */     int i1 = this.vertexFormat.getNextOffset() >> 2;
/*  640 */     int j1 = (this.vertexCount - 4) * i1 + this.vertexFormat.getNormalOffset() / 4;
/*  641 */     this.rawIntBuffer.put(j1, l);
/*  642 */     this.rawIntBuffer.put(j1 + i1, l);
/*  643 */     this.rawIntBuffer.put(j1 + i1 * 2, l);
/*  644 */     this.rawIntBuffer.put(j1 + i1 * 3, l);
/*      */   }
/*      */ 
/*      */   
/*      */   private void nextVertexFormatIndex() {
/*  649 */     this.vertexFormatIndex++;
/*  650 */     this.vertexFormatIndex %= this.vertexFormat.getElementCount();
/*  651 */     this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
/*      */     
/*  653 */     if (this.vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING)
/*      */     {
/*  655 */       nextVertexFormatIndex();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldRenderer normal(float p_181663_1_, float p_181663_2_, float p_181663_3_) {
/*  661 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
/*      */     
/*  663 */     switch (this.vertexFormatElement.getType()) {
/*      */       
/*      */       case FLOAT:
/*  666 */         this.byteBuffer.putFloat(i, p_181663_1_);
/*  667 */         this.byteBuffer.putFloat(i + 4, p_181663_2_);
/*  668 */         this.byteBuffer.putFloat(i + 8, p_181663_3_);
/*      */         break;
/*      */       
/*      */       case UINT:
/*      */       case INT:
/*  673 */         this.byteBuffer.putInt(i, (int)p_181663_1_);
/*  674 */         this.byteBuffer.putInt(i + 4, (int)p_181663_2_);
/*  675 */         this.byteBuffer.putInt(i + 8, (int)p_181663_3_);
/*      */         break;
/*      */       
/*      */       case USHORT:
/*      */       case SHORT:
/*  680 */         this.byteBuffer.putShort(i, (short)((int)(p_181663_1_ * 32767.0F) & 0xFFFF));
/*  681 */         this.byteBuffer.putShort(i + 2, (short)((int)(p_181663_2_ * 32767.0F) & 0xFFFF));
/*  682 */         this.byteBuffer.putShort(i + 4, (short)((int)(p_181663_3_ * 32767.0F) & 0xFFFF));
/*      */         break;
/*      */       
/*      */       case UBYTE:
/*      */       case BYTE:
/*  687 */         this.byteBuffer.put(i, (byte)((int)(p_181663_1_ * 127.0F) & 0xFF));
/*  688 */         this.byteBuffer.put(i + 1, (byte)((int)(p_181663_2_ * 127.0F) & 0xFF));
/*  689 */         this.byteBuffer.put(i + 2, (byte)((int)(p_181663_3_ * 127.0F) & 0xFF));
/*      */         break;
/*      */     } 
/*  692 */     nextVertexFormatIndex();
/*  693 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTranslation(double x, double y, double z) {
/*  698 */     this.xOffset = x;
/*  699 */     this.yOffset = y;
/*  700 */     this.zOffset = z;
/*      */   }
/*      */ 
/*      */   
/*      */   public void finishDrawing() {
/*  705 */     if (!this.isDrawing)
/*      */     {
/*  707 */       throw new IllegalStateException("Not building!");
/*      */     }
/*      */ 
/*      */     
/*  711 */     this.isDrawing = false;
/*  712 */     this.byteBuffer.position(0);
/*  713 */     this.byteBuffer.limit(getBufferSize() * 4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuffer getByteBuffer() {
/*  719 */     return this.modeTriangles ? this.byteBufferTriangles : this.byteBuffer;
/*      */   }
/*      */ 
/*      */   
/*      */   public VertexFormat getVertexFormat() {
/*  724 */     return this.vertexFormat;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getVertexCount() {
/*  729 */     return this.modeTriangles ? (this.vertexCount / 4 * 6) : this.vertexCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDrawMode() {
/*  734 */     return this.modeTriangles ? 4 : this.drawMode;
/*      */   }
/*      */ 
/*      */   
/*      */   public void putColor4(int argb) {
/*  739 */     for (int i = 0; i < 4; i++)
/*      */     {
/*  741 */       putColor(argb, i + 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void putColorRGB_F4(float red, float green, float blue) {
/*  747 */     for (int i = 0; i < 4; i++)
/*      */     {
/*  749 */       putColorRGB_F(red, green, blue, i + 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void putSprite(TextureAtlasSprite p_putSprite_1_) {
/*  755 */     if (this.animatedSprites != null && p_putSprite_1_ != null && p_putSprite_1_.getAnimationIndex() >= 0)
/*      */     {
/*  757 */       this.animatedSprites.set(p_putSprite_1_.getAnimationIndex());
/*      */     }
/*      */     
/*  760 */     if (this.quadSprites != null) {
/*      */       
/*  762 */       int i = this.vertexCount / 4;
/*  763 */       this.quadSprites[i - 1] = p_putSprite_1_;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSprite(TextureAtlasSprite p_setSprite_1_) {
/*  769 */     if (this.animatedSprites != null && p_setSprite_1_ != null && p_setSprite_1_.getAnimationIndex() >= 0)
/*      */     {
/*  771 */       this.animatedSprites.set(p_setSprite_1_.getAnimationIndex());
/*      */     }
/*      */     
/*  774 */     if (this.quadSprites != null)
/*      */     {
/*  776 */       this.quadSprite = p_setSprite_1_;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isMultiTexture() {
/*  782 */     return (this.quadSprites != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void drawMultiTexture() {
/*  787 */     if (this.quadSprites != null) {
/*      */       
/*  789 */       int i = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();
/*      */       
/*  791 */       if (this.drawnIcons.length <= i)
/*      */       {
/*  793 */         this.drawnIcons = new boolean[i + 1];
/*      */       }
/*      */       
/*  796 */       Arrays.fill(this.drawnIcons, false);
/*  797 */       int j = 0;
/*  798 */       int k = -1;
/*  799 */       int l = this.vertexCount / 4;
/*      */       
/*  801 */       for (int i1 = 0; i1 < l; i1++) {
/*      */         
/*  803 */         TextureAtlasSprite textureatlassprite = this.quadSprites[i1];
/*      */         
/*  805 */         if (textureatlassprite != null) {
/*      */           
/*  807 */           int j1 = textureatlassprite.getIndexInMap();
/*      */           
/*  809 */           if (!this.drawnIcons[j1])
/*      */           {
/*  811 */             if (textureatlassprite == TextureUtils.iconGrassSideOverlay) {
/*      */               
/*  813 */               if (k < 0)
/*      */               {
/*  815 */                 k = i1;
/*      */               }
/*      */             }
/*      */             else {
/*      */               
/*  820 */               i1 = drawForIcon(textureatlassprite, i1) - 1;
/*  821 */               j++;
/*      */               
/*  823 */               if (this.blockLayer != EnumWorldBlockLayer.TRANSLUCENT)
/*      */               {
/*  825 */                 this.drawnIcons[j1] = true;
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  832 */       if (k >= 0) {
/*      */         
/*  834 */         drawForIcon(TextureUtils.iconGrassSideOverlay, k);
/*  835 */         j++;
/*      */       } 
/*      */       
/*  838 */       if (j > 0);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int drawForIcon(TextureAtlasSprite p_drawForIcon_1_, int p_drawForIcon_2_) {
/*  847 */     GL11.glBindTexture(3553, p_drawForIcon_1_.glSpriteTextureId);
/*  848 */     int i = -1;
/*  849 */     int j = -1;
/*  850 */     int k = this.vertexCount / 4;
/*      */     
/*  852 */     for (int l = p_drawForIcon_2_; l < k; l++) {
/*      */       
/*  854 */       TextureAtlasSprite textureatlassprite = this.quadSprites[l];
/*      */       
/*  856 */       if (textureatlassprite == p_drawForIcon_1_) {
/*      */         
/*  858 */         if (j < 0)
/*      */         {
/*  860 */           j = l;
/*      */         }
/*      */       }
/*  863 */       else if (j >= 0) {
/*      */         
/*  865 */         draw(j, l);
/*      */         
/*  867 */         if (this.blockLayer == EnumWorldBlockLayer.TRANSLUCENT)
/*      */         {
/*  869 */           return l;
/*      */         }
/*      */         
/*  872 */         j = -1;
/*      */         
/*  874 */         if (i < 0)
/*      */         {
/*  876 */           i = l;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  881 */     if (j >= 0)
/*      */     {
/*  883 */       draw(j, k);
/*      */     }
/*      */     
/*  886 */     if (i < 0)
/*      */     {
/*  888 */       i = k;
/*      */     }
/*      */     
/*  891 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   private void draw(int p_draw_1_, int p_draw_2_) {
/*  896 */     int i = p_draw_2_ - p_draw_1_;
/*      */     
/*  898 */     if (i > 0) {
/*      */       
/*  900 */       int j = p_draw_1_ * 4;
/*  901 */       int k = i * 4;
/*  902 */       GL11.glDrawArrays(this.drawMode, j, k);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlockLayer(EnumWorldBlockLayer p_setBlockLayer_1_) {
/*  908 */     this.blockLayer = p_setBlockLayer_1_;
/*      */     
/*  910 */     if (p_setBlockLayer_1_ == null) {
/*      */       
/*  912 */       if (this.quadSprites != null)
/*      */       {
/*  914 */         this.quadSpritesPrev = this.quadSprites;
/*      */       }
/*      */       
/*  917 */       this.quadSprites = null;
/*  918 */       this.quadSprite = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getBufferQuadSize() {
/*  924 */     int i = this.rawIntBuffer.capacity() * 4 / this.vertexFormat.getIntegerSize() * 4;
/*  925 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public RenderEnv getRenderEnv(IBlockState p_getRenderEnv_1_, BlockPos p_getRenderEnv_2_) {
/*  930 */     if (this.renderEnv == null) {
/*      */       
/*  932 */       this.renderEnv = new RenderEnv(p_getRenderEnv_1_, p_getRenderEnv_2_);
/*  933 */       return this.renderEnv;
/*      */     } 
/*      */ 
/*      */     
/*  937 */     this.renderEnv.reset(p_getRenderEnv_1_, p_getRenderEnv_2_);
/*  938 */     return this.renderEnv;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDrawing() {
/*  944 */     return this.isDrawing;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getXOffset() {
/*  949 */     return this.xOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getYOffset() {
/*  954 */     return this.yOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getZOffset() {
/*  959 */     return this.zOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   public EnumWorldBlockLayer getBlockLayer() {
/*  964 */     return this.blockLayer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void putColorMultiplierRgba(float p_putColorMultiplierRgba_1_, float p_putColorMultiplierRgba_2_, float p_putColorMultiplierRgba_3_, float p_putColorMultiplierRgba_4_, int p_putColorMultiplierRgba_5_) {
/*  969 */     int i = getColorIndex(p_putColorMultiplierRgba_5_);
/*  970 */     int j = -1;
/*      */     
/*  972 */     if (!this.noColor) {
/*      */       
/*  974 */       j = this.rawIntBuffer.get(i);
/*      */       
/*  976 */       if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
/*      */         
/*  978 */         int k = (int)((j & 0xFF) * p_putColorMultiplierRgba_1_);
/*  979 */         int l = (int)((j >> 8 & 0xFF) * p_putColorMultiplierRgba_2_);
/*  980 */         int i1 = (int)((j >> 16 & 0xFF) * p_putColorMultiplierRgba_3_);
/*  981 */         int j1 = (int)((j >> 24 & 0xFF) * p_putColorMultiplierRgba_4_);
/*  982 */         j = j1 << 24 | i1 << 16 | l << 8 | k;
/*      */       }
/*      */       else {
/*      */         
/*  986 */         int k1 = (int)((j >> 24 & 0xFF) * p_putColorMultiplierRgba_1_);
/*  987 */         int l1 = (int)((j >> 16 & 0xFF) * p_putColorMultiplierRgba_2_);
/*  988 */         int i2 = (int)((j >> 8 & 0xFF) * p_putColorMultiplierRgba_3_);
/*  989 */         int j2 = (int)((j & 0xFF) * p_putColorMultiplierRgba_4_);
/*  990 */         j = k1 << 24 | l1 << 16 | i2 << 8 | j2;
/*      */       } 
/*      */     } 
/*      */     
/*  994 */     this.rawIntBuffer.put(i, j);
/*      */   }
/*      */ 
/*      */   
/*      */   public void quadsToTriangles() {
/*  999 */     if (this.drawMode == 7) {
/*      */       
/* 1001 */       if (this.byteBufferTriangles == null)
/*      */       {
/* 1003 */         this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
/*      */       }
/*      */       
/* 1006 */       if (this.byteBufferTriangles.capacity() < this.byteBuffer.capacity() * 2)
/*      */       {
/* 1008 */         this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
/*      */       }
/*      */       
/* 1011 */       int i = this.vertexFormat.getNextOffset();
/* 1012 */       int j = this.byteBuffer.limit();
/* 1013 */       this.byteBuffer.rewind();
/* 1014 */       this.byteBufferTriangles.clear();
/*      */       
/* 1016 */       for (int k = 0; k < this.vertexCount; k += 4) {
/*      */         
/* 1018 */         this.byteBuffer.limit((k + 3) * i);
/* 1019 */         this.byteBuffer.position(k * i);
/* 1020 */         this.byteBufferTriangles.put(this.byteBuffer);
/* 1021 */         this.byteBuffer.limit((k + 1) * i);
/* 1022 */         this.byteBuffer.position(k * i);
/* 1023 */         this.byteBufferTriangles.put(this.byteBuffer);
/* 1024 */         this.byteBuffer.limit((k + 2 + 2) * i);
/* 1025 */         this.byteBuffer.position((k + 2) * i);
/* 1026 */         this.byteBufferTriangles.put(this.byteBuffer);
/*      */       } 
/*      */       
/* 1029 */       this.byteBuffer.limit(j);
/* 1030 */       this.byteBuffer.rewind();
/* 1031 */       this.byteBufferTriangles.flip();
/* 1032 */       this.modeTriangles = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isColorDisabled() {
/* 1038 */     return this.noColor;
/*      */   }
/*      */ 
/*      */   
/*      */   public class State
/*      */   {
/*      */     private final int[] stateRawBuffer;
/*      */     private final VertexFormat stateVertexFormat;
/*      */     private TextureAtlasSprite[] stateQuadSprites;
/*      */     
/*      */     public State(int[] p_i1_2_, VertexFormat p_i1_3_, TextureAtlasSprite[] p_i1_4_) {
/* 1049 */       this.stateRawBuffer = p_i1_2_;
/* 1050 */       this.stateVertexFormat = p_i1_3_;
/* 1051 */       this.stateQuadSprites = p_i1_4_;
/*      */     }
/*      */ 
/*      */     
/*      */     public State(int[] buffer, VertexFormat format) {
/* 1056 */       this.stateRawBuffer = buffer;
/* 1057 */       this.stateVertexFormat = format;
/*      */     }
/*      */ 
/*      */     
/*      */     public int[] getRawBuffer() {
/* 1062 */       return this.stateRawBuffer;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getVertexCount() {
/* 1067 */       return this.stateRawBuffer.length / this.stateVertexFormat.getIntegerSize();
/*      */     }
/*      */ 
/*      */     
/*      */     public VertexFormat getVertexFormat() {
/* 1072 */       return this.stateVertexFormat;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\WorldRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */