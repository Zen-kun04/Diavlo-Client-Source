/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.optifine.entity.model.anim.ModelUpdater;
/*     */ import net.optifine.model.ModelSprite;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ public class ModelRenderer
/*     */ {
/*     */   public float textureWidth;
/*     */   public float textureHeight;
/*     */   private int textureOffsetX;
/*     */   private int textureOffsetY;
/*     */   public float rotationPointX;
/*     */   public float rotationPointY;
/*     */   public float rotationPointZ;
/*     */   public float rotateAngleX;
/*     */   public float rotateAngleY;
/*     */   public float rotateAngleZ;
/*     */   private boolean compiled;
/*     */   private int displayList;
/*     */   public boolean mirror;
/*     */   public boolean showModel;
/*     */   public boolean isHidden;
/*     */   public List<ModelBox> cubeList;
/*     */   public List<ModelRenderer> childModels;
/*     */   public final String boxName;
/*     */   private ModelBase baseModel;
/*     */   public float offsetX;
/*     */   public float offsetY;
/*     */   public float offsetZ;
/*     */   public List spriteList;
/*     */   public boolean mirrorV;
/*     */   public float scaleX;
/*     */   public float scaleY;
/*     */   public float scaleZ;
/*     */   private int countResetDisplayList;
/*     */   private ResourceLocation textureLocation;
/*     */   private String id;
/*     */   private ModelUpdater modelUpdater;
/*     */   private RenderGlobal renderGlobal;
/*     */   
/*     */   public ModelRenderer(ModelBase model, String boxNameIn) {
/*  55 */     this.spriteList = new ArrayList();
/*  56 */     this.mirrorV = false;
/*  57 */     this.scaleX = 1.0F;
/*  58 */     this.scaleY = 1.0F;
/*  59 */     this.scaleZ = 1.0F;
/*  60 */     this.textureLocation = null;
/*  61 */     this.id = null;
/*  62 */     this.renderGlobal = Config.getRenderGlobal();
/*  63 */     this.textureWidth = 64.0F;
/*  64 */     this.textureHeight = 32.0F;
/*  65 */     this.showModel = true;
/*  66 */     this.cubeList = Lists.newArrayList();
/*  67 */     this.baseModel = model;
/*  68 */     model.boxList.add(this);
/*  69 */     this.boxName = boxNameIn;
/*  70 */     setTextureSize(model.textureWidth, model.textureHeight);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelRenderer(ModelBase model) {
/*  75 */     this(model, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelRenderer(ModelBase model, int texOffX, int texOffY) {
/*  80 */     this(model);
/*  81 */     setTextureOffset(texOffX, texOffY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChild(ModelRenderer renderer) {
/*  86 */     if (this.childModels == null)
/*     */     {
/*  88 */       this.childModels = Lists.newArrayList();
/*     */     }
/*     */     
/*  91 */     this.childModels.add(renderer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelRenderer setTextureOffset(int x, int y) {
/*  96 */     this.textureOffsetX = x;
/*  97 */     this.textureOffsetY = y;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth) {
/* 103 */     partName = this.boxName + "." + partName;
/* 104 */     TextureOffset textureoffset = this.baseModel.getTextureOffset(partName);
/* 105 */     setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
/* 106 */     this.cubeList.add((new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F)).setBoxName(partName));
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth) {
/* 112 */     this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F));
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelRenderer addBox(float p_178769_1_, float p_178769_2_, float p_178769_3_, int p_178769_4_, int p_178769_5_, int p_178769_6_, boolean p_178769_7_) {
/* 118 */     this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_178769_1_, p_178769_2_, p_178769_3_, p_178769_4_, p_178769_5_, p_178769_6_, 0.0F, p_178769_7_));
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int width, int height, int depth, float scaleFactor) {
/* 124 */     this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, width, height, depth, scaleFactor));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
/* 129 */     this.rotationPointX = rotationPointXIn;
/* 130 */     this.rotationPointY = rotationPointYIn;
/* 131 */     this.rotationPointZ = rotationPointZIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(float p_78785_1_) {
/* 136 */     if (!this.isHidden && this.showModel) {
/*     */       
/* 138 */       checkResetDisplayList();
/*     */       
/* 140 */       if (!this.compiled)
/*     */       {
/* 142 */         compileDisplayList(p_78785_1_);
/*     */       }
/*     */       
/* 145 */       int i = 0;
/*     */       
/* 147 */       if (this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
/*     */         
/* 149 */         if (this.renderGlobal.renderOverlayEyes) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 154 */         i = GlStateManager.getBoundTexture();
/* 155 */         Config.getTextureManager().bindTexture(this.textureLocation);
/*     */       } 
/*     */       
/* 158 */       if (this.modelUpdater != null)
/*     */       {
/* 160 */         this.modelUpdater.update();
/*     */       }
/*     */       
/* 163 */       boolean flag = (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F);
/* 164 */       GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
/*     */       
/* 166 */       if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
/*     */         
/* 168 */         if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
/*     */         {
/* 170 */           if (flag)
/*     */           {
/* 172 */             GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
/*     */           }
/*     */           
/* 175 */           GlStateManager.callList(this.displayList);
/*     */           
/* 177 */           if (this.childModels != null)
/*     */           {
/* 179 */             for (int l = 0; l < this.childModels.size(); l++)
/*     */             {
/* 181 */               ((ModelRenderer)this.childModels.get(l)).render(p_78785_1_);
/*     */             }
/*     */           }
/*     */           
/* 185 */           if (flag)
/*     */           {
/* 187 */             GlStateManager.scale(1.0F / this.scaleX, 1.0F / this.scaleY, 1.0F / this.scaleZ);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 192 */           GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
/*     */           
/* 194 */           if (flag)
/*     */           {
/* 196 */             GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
/*     */           }
/*     */           
/* 199 */           GlStateManager.callList(this.displayList);
/*     */           
/* 201 */           if (this.childModels != null)
/*     */           {
/* 203 */             for (int k = 0; k < this.childModels.size(); k++)
/*     */             {
/* 205 */               ((ModelRenderer)this.childModels.get(k)).render(p_78785_1_);
/*     */             }
/*     */           }
/*     */           
/* 209 */           if (flag)
/*     */           {
/* 211 */             GlStateManager.scale(1.0F / this.scaleX, 1.0F / this.scaleY, 1.0F / this.scaleZ);
/*     */           }
/*     */           
/* 214 */           GlStateManager.translate(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 219 */         GlStateManager.pushMatrix();
/* 220 */         GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
/*     */         
/* 222 */         if (this.rotateAngleZ != 0.0F)
/*     */         {
/* 224 */           GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */         }
/*     */         
/* 227 */         if (this.rotateAngleY != 0.0F)
/*     */         {
/* 229 */           GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */         }
/*     */         
/* 232 */         if (this.rotateAngleX != 0.0F)
/*     */         {
/* 234 */           GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */         }
/*     */         
/* 237 */         if (flag)
/*     */         {
/* 239 */           GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
/*     */         }
/*     */         
/* 242 */         GlStateManager.callList(this.displayList);
/*     */         
/* 244 */         if (this.childModels != null)
/*     */         {
/* 246 */           for (int j = 0; j < this.childModels.size(); j++)
/*     */           {
/* 248 */             ((ModelRenderer)this.childModels.get(j)).render(p_78785_1_);
/*     */           }
/*     */         }
/*     */         
/* 252 */         GlStateManager.popMatrix();
/*     */       } 
/*     */       
/* 255 */       GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
/*     */       
/* 257 */       if (i != 0)
/*     */       {
/* 259 */         GlStateManager.bindTexture(i);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderWithRotation(float p_78791_1_) {
/* 266 */     if (!this.isHidden && this.showModel) {
/*     */       
/* 268 */       checkResetDisplayList();
/*     */       
/* 270 */       if (!this.compiled)
/*     */       {
/* 272 */         compileDisplayList(p_78791_1_);
/*     */       }
/*     */       
/* 275 */       int i = 0;
/*     */       
/* 277 */       if (this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
/*     */         
/* 279 */         if (this.renderGlobal.renderOverlayEyes) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 284 */         i = GlStateManager.getBoundTexture();
/* 285 */         Config.getTextureManager().bindTexture(this.textureLocation);
/*     */       } 
/*     */       
/* 288 */       if (this.modelUpdater != null)
/*     */       {
/* 290 */         this.modelUpdater.update();
/*     */       }
/*     */       
/* 293 */       boolean flag = (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F);
/* 294 */       GlStateManager.pushMatrix();
/* 295 */       GlStateManager.translate(this.rotationPointX * p_78791_1_, this.rotationPointY * p_78791_1_, this.rotationPointZ * p_78791_1_);
/*     */       
/* 297 */       if (this.rotateAngleY != 0.0F)
/*     */       {
/* 299 */         GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */       }
/*     */       
/* 302 */       if (this.rotateAngleX != 0.0F)
/*     */       {
/* 304 */         GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */       }
/*     */       
/* 307 */       if (this.rotateAngleZ != 0.0F)
/*     */       {
/* 309 */         GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */       }
/*     */       
/* 312 */       if (flag)
/*     */       {
/* 314 */         GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
/*     */       }
/*     */       
/* 317 */       GlStateManager.callList(this.displayList);
/*     */       
/* 319 */       if (this.childModels != null)
/*     */       {
/* 321 */         for (int j = 0; j < this.childModels.size(); j++)
/*     */         {
/* 323 */           ((ModelRenderer)this.childModels.get(j)).render(p_78791_1_);
/*     */         }
/*     */       }
/*     */       
/* 327 */       GlStateManager.popMatrix();
/*     */       
/* 329 */       if (i != 0)
/*     */       {
/* 331 */         GlStateManager.bindTexture(i);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRender(float scale) {
/* 338 */     if (!this.isHidden && this.showModel) {
/*     */       
/* 340 */       checkResetDisplayList();
/*     */       
/* 342 */       if (!this.compiled)
/*     */       {
/* 344 */         compileDisplayList(scale);
/*     */       }
/*     */       
/* 347 */       if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
/*     */         
/* 349 */         if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
/*     */         {
/* 351 */           GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 356 */         GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
/*     */         
/* 358 */         if (this.rotateAngleZ != 0.0F)
/*     */         {
/* 360 */           GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */         }
/*     */         
/* 363 */         if (this.rotateAngleY != 0.0F)
/*     */         {
/* 365 */           GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */         }
/*     */         
/* 368 */         if (this.rotateAngleX != 0.0F)
/*     */         {
/* 370 */           GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void compileDisplayList(float scale) {
/* 378 */     if (this.displayList == 0)
/*     */     {
/* 380 */       this.displayList = GLAllocation.generateDisplayLists(1);
/*     */     }
/*     */     
/* 383 */     GL11.glNewList(this.displayList, 4864);
/* 384 */     WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
/*     */     
/* 386 */     for (int i = 0; i < this.cubeList.size(); i++)
/*     */     {
/* 388 */       ((ModelBox)this.cubeList.get(i)).render(worldrenderer, scale);
/*     */     }
/*     */     
/* 391 */     for (int j = 0; j < this.spriteList.size(); j++) {
/*     */       
/* 393 */       ModelSprite modelsprite = this.spriteList.get(j);
/* 394 */       modelsprite.render(Tessellator.getInstance(), scale);
/*     */     } 
/*     */     
/* 397 */     GL11.glEndList();
/* 398 */     this.compiled = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn) {
/* 403 */     this.textureWidth = textureWidthIn;
/* 404 */     this.textureHeight = textureHeightIn;
/* 405 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addSprite(float p_addSprite_1_, float p_addSprite_2_, float p_addSprite_3_, int p_addSprite_4_, int p_addSprite_5_, int p_addSprite_6_, float p_addSprite_7_) {
/* 410 */     this.spriteList.add(new ModelSprite(this, this.textureOffsetX, this.textureOffsetY, p_addSprite_1_, p_addSprite_2_, p_addSprite_3_, p_addSprite_4_, p_addSprite_5_, p_addSprite_6_, p_addSprite_7_));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCompiled() {
/* 415 */     return this.compiled;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDisplayList() {
/* 420 */     return this.displayList;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkResetDisplayList() {
/* 425 */     if (this.countResetDisplayList != Shaders.countResetDisplayLists) {
/*     */       
/* 427 */       this.compiled = false;
/* 428 */       this.countResetDisplayList = Shaders.countResetDisplayLists;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getTextureLocation() {
/* 434 */     return this.textureLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTextureLocation(ResourceLocation p_setTextureLocation_1_) {
/* 439 */     this.textureLocation = p_setTextureLocation_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/* 444 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(String p_setId_1_) {
/* 449 */     this.id = p_setId_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBox(int[][] p_addBox_1_, float p_addBox_2_, float p_addBox_3_, float p_addBox_4_, float p_addBox_5_, float p_addBox_6_, float p_addBox_7_, float p_addBox_8_) {
/* 454 */     this.cubeList.add(new ModelBox(this, p_addBox_1_, p_addBox_2_, p_addBox_3_, p_addBox_4_, p_addBox_5_, p_addBox_6_, p_addBox_7_, p_addBox_8_, this.mirror));
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelRenderer getChild(String p_getChild_1_) {
/* 459 */     if (p_getChild_1_ == null)
/*     */     {
/* 461 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 465 */     if (this.childModels != null)
/*     */     {
/* 467 */       for (int i = 0; i < this.childModels.size(); i++) {
/*     */         
/* 469 */         ModelRenderer modelrenderer = this.childModels.get(i);
/*     */         
/* 471 */         if (p_getChild_1_.equals(modelrenderer.getId()))
/*     */         {
/* 473 */           return modelrenderer;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 478 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelRenderer getChildDeep(String p_getChildDeep_1_) {
/* 484 */     if (p_getChildDeep_1_ == null)
/*     */     {
/* 486 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 490 */     ModelRenderer modelrenderer = getChild(p_getChildDeep_1_);
/*     */     
/* 492 */     if (modelrenderer != null)
/*     */     {
/* 494 */       return modelrenderer;
/*     */     }
/*     */ 
/*     */     
/* 498 */     if (this.childModels != null)
/*     */     {
/* 500 */       for (int i = 0; i < this.childModels.size(); i++) {
/*     */         
/* 502 */         ModelRenderer modelrenderer1 = this.childModels.get(i);
/* 503 */         ModelRenderer modelrenderer2 = modelrenderer1.getChildDeep(p_getChildDeep_1_);
/*     */         
/* 505 */         if (modelrenderer2 != null)
/*     */         {
/* 507 */           return modelrenderer2;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 512 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModelUpdater(ModelUpdater p_setModelUpdater_1_) {
/* 519 */     this.modelUpdater = p_setModelUpdater_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 524 */     StringBuffer stringbuffer = new StringBuffer();
/* 525 */     stringbuffer.append("id: " + this.id + ", boxes: " + ((this.cubeList != null) ? (String)Integer.valueOf(this.cubeList.size()) : null) + ", submodels: " + ((this.childModels != null) ? (String)Integer.valueOf(this.childModels.size()) : null));
/* 526 */     return stringbuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */