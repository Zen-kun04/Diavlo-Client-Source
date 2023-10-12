/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.StitcherException;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class Stitcher
/*     */ {
/*     */   private final int mipmapLevelStitcher;
/*  14 */   private final Set<Holder> setStitchHolders = Sets.newHashSetWithExpectedSize(256);
/*  15 */   private final List<Slot> stitchSlots = Lists.newArrayListWithCapacity(256);
/*     */   
/*     */   private int currentWidth;
/*     */   private int currentHeight;
/*     */   private final int maxWidth;
/*     */   private final int maxHeight;
/*     */   private final boolean forcePowerOf2;
/*     */   private final int maxTileDimension;
/*     */   
/*     */   public Stitcher(int maxTextureWidth, int maxTextureHeight, boolean p_i45095_3_, int p_i45095_4_, int mipmapLevel) {
/*  25 */     this.mipmapLevelStitcher = mipmapLevel;
/*  26 */     this.maxWidth = maxTextureWidth;
/*  27 */     this.maxHeight = maxTextureHeight;
/*  28 */     this.forcePowerOf2 = p_i45095_3_;
/*  29 */     this.maxTileDimension = p_i45095_4_;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCurrentWidth() {
/*  34 */     return this.currentWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCurrentHeight() {
/*  39 */     return this.currentHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addSprite(TextureAtlasSprite p_110934_1_) {
/*  44 */     Holder stitcher$holder = new Holder(p_110934_1_, this.mipmapLevelStitcher);
/*     */     
/*  46 */     if (this.maxTileDimension > 0)
/*     */     {
/*  48 */       stitcher$holder.setNewDimension(this.maxTileDimension);
/*     */     }
/*     */     
/*  51 */     this.setStitchHolders.add(stitcher$holder);
/*     */   }
/*     */ 
/*     */   
/*     */   public void doStitch() {
/*  56 */     Holder[] astitcher$holder = this.setStitchHolders.<Holder>toArray(new Holder[this.setStitchHolders.size()]);
/*  57 */     Arrays.sort((Object[])astitcher$holder);
/*     */     
/*  59 */     for (Holder stitcher$holder : astitcher$holder) {
/*     */       
/*  61 */       if (!allocateSlot(stitcher$holder)) {
/*     */         
/*  63 */         String s = String.format("Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", new Object[] { stitcher$holder.getAtlasSprite().getIconName(), Integer.valueOf(stitcher$holder.getAtlasSprite().getIconWidth()), Integer.valueOf(stitcher$holder.getAtlasSprite().getIconHeight()), Integer.valueOf(this.currentWidth), Integer.valueOf(this.currentHeight), Integer.valueOf(this.maxWidth), Integer.valueOf(this.maxHeight) });
/*  64 */         throw new StitcherException(stitcher$holder, s);
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     if (this.forcePowerOf2) {
/*     */       
/*  70 */       this.currentWidth = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
/*  71 */       this.currentHeight = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<TextureAtlasSprite> getStichSlots() {
/*  77 */     List<Slot> list = Lists.newArrayList();
/*     */     
/*  79 */     for (Slot stitcher$slot : this.stitchSlots)
/*     */     {
/*  81 */       stitcher$slot.getAllStitchSlots(list);
/*     */     }
/*     */     
/*  84 */     List<TextureAtlasSprite> list1 = Lists.newArrayList();
/*     */     
/*  86 */     for (Slot stitcher$slot1 : list) {
/*     */       
/*  88 */       Holder stitcher$holder = stitcher$slot1.getStitchHolder();
/*  89 */       TextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();
/*  90 */       textureatlassprite.initSprite(this.currentWidth, this.currentHeight, stitcher$slot1.getOriginX(), stitcher$slot1.getOriginY(), stitcher$holder.isRotated());
/*  91 */       list1.add(textureatlassprite);
/*     */     } 
/*     */     
/*  94 */     return list1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getMipmapDimension(int p_147969_0_, int p_147969_1_) {
/*  99 */     return (p_147969_0_ >> p_147969_1_) + (((p_147969_0_ & (1 << p_147969_1_) - 1) == 0) ? 0 : 1) << p_147969_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean allocateSlot(Holder p_94310_1_) {
/* 104 */     for (int i = 0; i < this.stitchSlots.size(); i++) {
/*     */       
/* 106 */       if (((Slot)this.stitchSlots.get(i)).addSlot(p_94310_1_))
/*     */       {
/* 108 */         return true;
/*     */       }
/*     */       
/* 111 */       p_94310_1_.rotate();
/*     */       
/* 113 */       if (((Slot)this.stitchSlots.get(i)).addSlot(p_94310_1_))
/*     */       {
/* 115 */         return true;
/*     */       }
/*     */       
/* 118 */       p_94310_1_.rotate();
/*     */     } 
/*     */     
/* 121 */     return expandAndAllocateSlot(p_94310_1_);
/*     */   }
/*     */   private boolean expandAndAllocateSlot(Holder p_94311_1_) {
/*     */     boolean flag1;
/*     */     Slot stitcher$slot;
/* 126 */     int i = Math.min(p_94311_1_.getWidth(), p_94311_1_.getHeight());
/* 127 */     boolean flag = (this.currentWidth == 0 && this.currentHeight == 0);
/*     */ 
/*     */     
/* 130 */     if (this.forcePowerOf2) {
/*     */       
/* 132 */       int j = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
/* 133 */       int k = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
/* 134 */       int l = MathHelper.roundUpToPowerOfTwo(this.currentWidth + i);
/* 135 */       int i1 = MathHelper.roundUpToPowerOfTwo(this.currentHeight + i);
/* 136 */       boolean flag2 = (l <= this.maxWidth);
/* 137 */       boolean flag3 = (i1 <= this.maxHeight);
/*     */       
/* 139 */       if (!flag2 && !flag3)
/*     */       {
/* 141 */         return false;
/*     */       }
/*     */       
/* 144 */       boolean flag4 = (j != l);
/* 145 */       boolean flag5 = (k != i1);
/*     */       
/* 147 */       if (flag4 ^ flag5)
/*     */       {
/* 149 */         flag1 = !flag4;
/*     */       }
/*     */       else
/*     */       {
/* 153 */         flag1 = (flag2 && j <= k);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 158 */       boolean flag6 = (this.currentWidth + i <= this.maxWidth);
/* 159 */       boolean flag7 = (this.currentHeight + i <= this.maxHeight);
/*     */       
/* 161 */       if (!flag6 && !flag7)
/*     */       {
/* 163 */         return false;
/*     */       }
/*     */       
/* 166 */       flag1 = (flag6 && (flag || this.currentWidth <= this.currentHeight));
/*     */     } 
/*     */     
/* 169 */     int j1 = Math.max(p_94311_1_.getWidth(), p_94311_1_.getHeight());
/*     */     
/* 171 */     if (MathHelper.roundUpToPowerOfTwo((!flag1 ? this.currentHeight : this.currentWidth) + j1) > (!flag1 ? this.maxHeight : this.maxWidth))
/*     */     {
/* 173 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     if (flag1) {
/*     */       
/* 181 */       if (p_94311_1_.getWidth() > p_94311_1_.getHeight())
/*     */       {
/* 183 */         p_94311_1_.rotate();
/*     */       }
/*     */       
/* 186 */       if (this.currentHeight == 0)
/*     */       {
/* 188 */         this.currentHeight = p_94311_1_.getHeight();
/*     */       }
/*     */       
/* 191 */       stitcher$slot = new Slot(this.currentWidth, 0, p_94311_1_.getWidth(), this.currentHeight);
/* 192 */       this.currentWidth += p_94311_1_.getWidth();
/*     */     }
/*     */     else {
/*     */       
/* 196 */       stitcher$slot = new Slot(0, this.currentHeight, this.currentWidth, p_94311_1_.getHeight());
/* 197 */       this.currentHeight += p_94311_1_.getHeight();
/*     */     } 
/*     */     
/* 200 */     stitcher$slot.addSlot(p_94311_1_);
/* 201 */     this.stitchSlots.add(stitcher$slot);
/* 202 */     return true;
/*     */   }
/*     */   
/*     */   public static class Holder
/*     */     implements Comparable<Holder>
/*     */   {
/*     */     private final TextureAtlasSprite theTexture;
/*     */     private final int width;
/*     */     private final int height;
/*     */     private final int mipmapLevelHolder;
/*     */     private boolean rotated;
/* 213 */     private float scaleFactor = 1.0F;
/*     */ 
/*     */     
/*     */     public Holder(TextureAtlasSprite p_i45094_1_, int p_i45094_2_) {
/* 217 */       this.theTexture = p_i45094_1_;
/* 218 */       this.width = p_i45094_1_.getIconWidth();
/* 219 */       this.height = p_i45094_1_.getIconHeight();
/* 220 */       this.mipmapLevelHolder = p_i45094_2_;
/* 221 */       this.rotated = (Stitcher.getMipmapDimension(this.height, p_i45094_2_) > Stitcher.getMipmapDimension(this.width, p_i45094_2_));
/*     */     }
/*     */ 
/*     */     
/*     */     public TextureAtlasSprite getAtlasSprite() {
/* 226 */       return this.theTexture;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getWidth() {
/* 231 */       return this.rotated ? Stitcher.getMipmapDimension((int)(this.height * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.getMipmapDimension((int)(this.width * this.scaleFactor), this.mipmapLevelHolder);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getHeight() {
/* 236 */       return this.rotated ? Stitcher.getMipmapDimension((int)(this.width * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.getMipmapDimension((int)(this.height * this.scaleFactor), this.mipmapLevelHolder);
/*     */     }
/*     */ 
/*     */     
/*     */     public void rotate() {
/* 241 */       this.rotated = !this.rotated;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRotated() {
/* 246 */       return this.rotated;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setNewDimension(int p_94196_1_) {
/* 251 */       if (this.width > p_94196_1_ && this.height > p_94196_1_)
/*     */       {
/* 253 */         this.scaleFactor = p_94196_1_ / Math.min(this.width, this.height);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 259 */       return "Holder{width=" + this.width + ", height=" + this.height + '}';
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(Holder p_compareTo_1_) {
/*     */       int i;
/* 266 */       if (getHeight() == p_compareTo_1_.getHeight()) {
/*     */         
/* 268 */         if (getWidth() == p_compareTo_1_.getWidth()) {
/*     */           
/* 270 */           if (this.theTexture.getIconName() == null)
/*     */           {
/* 272 */             return (p_compareTo_1_.theTexture.getIconName() == null) ? 0 : -1;
/*     */           }
/*     */           
/* 275 */           return this.theTexture.getIconName().compareTo(p_compareTo_1_.theTexture.getIconName());
/*     */         } 
/*     */         
/* 278 */         i = (getWidth() < p_compareTo_1_.getWidth()) ? 1 : -1;
/*     */       }
/*     */       else {
/*     */         
/* 282 */         i = (getHeight() < p_compareTo_1_.getHeight()) ? 1 : -1;
/*     */       } 
/*     */       
/* 285 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Slot
/*     */   {
/*     */     private final int originX;
/*     */     private final int originY;
/*     */     private final int width;
/*     */     private final int height;
/*     */     private List<Slot> subSlots;
/*     */     private Stitcher.Holder holder;
/*     */     
/*     */     public Slot(int p_i1277_1_, int p_i1277_2_, int widthIn, int heightIn) {
/* 300 */       this.originX = p_i1277_1_;
/* 301 */       this.originY = p_i1277_2_;
/* 302 */       this.width = widthIn;
/* 303 */       this.height = heightIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public Stitcher.Holder getStitchHolder() {
/* 308 */       return this.holder;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getOriginX() {
/* 313 */       return this.originX;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getOriginY() {
/* 318 */       return this.originY;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addSlot(Stitcher.Holder holderIn) {
/* 323 */       if (this.holder != null)
/*     */       {
/* 325 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 329 */       int i = holderIn.getWidth();
/* 330 */       int j = holderIn.getHeight();
/*     */       
/* 332 */       if (i <= this.width && j <= this.height) {
/*     */         
/* 334 */         if (i == this.width && j == this.height) {
/*     */           
/* 336 */           this.holder = holderIn;
/* 337 */           return true;
/*     */         } 
/*     */ 
/*     */         
/* 341 */         if (this.subSlots == null) {
/*     */           
/* 343 */           this.subSlots = Lists.newArrayListWithCapacity(1);
/* 344 */           this.subSlots.add(new Slot(this.originX, this.originY, i, j));
/* 345 */           int k = this.width - i;
/* 346 */           int l = this.height - j;
/*     */           
/* 348 */           if (l > 0 && k > 0) {
/*     */             
/* 350 */             int i1 = Math.max(this.height, k);
/* 351 */             int j1 = Math.max(this.width, l);
/*     */             
/* 353 */             if (i1 >= j1)
/*     */             {
/* 355 */               this.subSlots.add(new Slot(this.originX, this.originY + j, i, l));
/* 356 */               this.subSlots.add(new Slot(this.originX + i, this.originY, k, this.height));
/*     */             }
/*     */             else
/*     */             {
/* 360 */               this.subSlots.add(new Slot(this.originX + i, this.originY, k, j));
/* 361 */               this.subSlots.add(new Slot(this.originX, this.originY + j, this.width, l));
/*     */             }
/*     */           
/* 364 */           } else if (k == 0) {
/*     */             
/* 366 */             this.subSlots.add(new Slot(this.originX, this.originY + j, i, l));
/*     */           }
/* 368 */           else if (l == 0) {
/*     */             
/* 370 */             this.subSlots.add(new Slot(this.originX + i, this.originY, k, j));
/*     */           } 
/*     */         } 
/*     */         
/* 374 */         for (Slot stitcher$slot : this.subSlots) {
/*     */           
/* 376 */           if (stitcher$slot.addSlot(holderIn))
/*     */           {
/* 378 */             return true;
/*     */           }
/*     */         } 
/*     */         
/* 382 */         return false;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 387 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void getAllStitchSlots(List<Slot> p_94184_1_) {
/* 394 */       if (this.holder != null) {
/*     */         
/* 396 */         p_94184_1_.add(this);
/*     */       }
/* 398 */       else if (this.subSlots != null) {
/*     */         
/* 400 */         for (Slot stitcher$slot : this.subSlots)
/*     */         {
/* 402 */           stitcher$slot.getAllStitchSlots(p_94184_1_);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 409 */       return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\texture\Stitcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */