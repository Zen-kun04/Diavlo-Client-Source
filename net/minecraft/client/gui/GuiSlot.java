/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public abstract class GuiSlot
/*     */ {
/*     */   protected final Minecraft mc;
/*     */   protected int width;
/*     */   protected int height;
/*     */   protected int top;
/*     */   protected int bottom;
/*     */   protected int right;
/*     */   protected int left;
/*     */   protected final int slotHeight;
/*     */   private int scrollUpButtonID;
/*     */   private int scrollDownButtonID;
/*     */   protected int mouseX;
/*     */   protected int mouseY;
/*     */   protected boolean field_148163_i = true;
/*  26 */   protected int initialClickY = -2;
/*     */   protected float scrollMultiplier;
/*     */   protected float amountScrolled;
/*  29 */   protected int selectedElement = -1;
/*     */   
/*     */   protected long lastClicked;
/*     */   protected boolean field_178041_q = true;
/*     */   protected boolean showSelectionBox = true;
/*     */   protected boolean hasListHeader;
/*     */   protected int headerPadding;
/*     */   private boolean enabled = true;
/*     */   
/*     */   public GuiSlot(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
/*  39 */     this.mc = mcIn;
/*  40 */     this.width = width;
/*  41 */     this.height = height;
/*  42 */     this.top = topIn;
/*  43 */     this.bottom = bottomIn;
/*  44 */     this.slotHeight = slotHeightIn;
/*  45 */     this.left = 0;
/*  46 */     this.right = width;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDimensions(int widthIn, int heightIn, int topIn, int bottomIn) {
/*  51 */     this.width = widthIn;
/*  52 */     this.height = heightIn;
/*  53 */     this.top = topIn;
/*  54 */     this.bottom = bottomIn;
/*  55 */     this.left = 0;
/*  56 */     this.right = widthIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setShowSelectionBox(boolean showSelectionBoxIn) {
/*  61 */     this.showSelectionBox = showSelectionBoxIn;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setHasListHeader(boolean hasListHeaderIn, int headerPaddingIn) {
/*  66 */     this.hasListHeader = hasListHeaderIn;
/*  67 */     this.headerPadding = headerPaddingIn;
/*     */     
/*  69 */     if (!hasListHeaderIn)
/*     */     {
/*  71 */       this.headerPadding = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract int getSize();
/*     */   
/*     */   protected abstract void elementClicked(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3);
/*     */   
/*     */   protected abstract boolean isSelected(int paramInt);
/*     */   
/*     */   protected int getContentHeight() {
/*  83 */     return getSize() * this.slotHeight + this.headerPadding;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void drawBackground();
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {}
/*     */ 
/*     */   
/*     */   protected abstract void drawSlot(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*     */ 
/*     */   
/*     */   protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {}
/*     */ 
/*     */   
/*     */   protected void func_148132_a(int p_148132_1_, int p_148132_2_) {}
/*     */ 
/*     */   
/*     */   protected void func_148142_b(int p_148142_1_, int p_148142_2_) {}
/*     */ 
/*     */   
/*     */   public int getSlotIndexFromScreenCoords(int p_148124_1_, int p_148124_2_) {
/* 108 */     int i = this.left + this.width / 2 - getListWidth() / 2;
/* 109 */     int j = this.left + this.width / 2 + getListWidth() / 2;
/* 110 */     int k = p_148124_2_ - this.top - this.headerPadding + (int)this.amountScrolled - 4;
/* 111 */     int l = k / this.slotHeight;
/* 112 */     return (p_148124_1_ < getScrollBarX() && p_148124_1_ >= i && p_148124_1_ <= j && l >= 0 && k >= 0 && l < getSize()) ? l : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerScrollButtons(int scrollUpButtonIDIn, int scrollDownButtonIDIn) {
/* 117 */     this.scrollUpButtonID = scrollUpButtonIDIn;
/* 118 */     this.scrollDownButtonID = scrollDownButtonIDIn;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void bindAmountScrolled() {
/* 123 */     this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0F, func_148135_f());
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_148135_f() {
/* 128 */     return Math.max(0, getContentHeight() - this.bottom - this.top - 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmountScrolled() {
/* 133 */     return (int)this.amountScrolled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMouseYWithinSlotBounds(int p_148141_1_) {
/* 138 */     return (p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scrollBy(int amount) {
/* 143 */     this.amountScrolled += amount;
/* 144 */     bindAmountScrolled();
/* 145 */     this.initialClickY = -2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void actionPerformed(GuiButton button) {
/* 150 */     if (button.enabled)
/*     */     {
/* 152 */       if (button.id == this.scrollUpButtonID) {
/*     */         
/* 154 */         this.amountScrolled -= (this.slotHeight * 2 / 3);
/* 155 */         this.initialClickY = -2;
/* 156 */         bindAmountScrolled();
/*     */       }
/* 158 */       else if (button.id == this.scrollDownButtonID) {
/*     */         
/* 160 */         this.amountScrolled += (this.slotHeight * 2 / 3);
/* 161 */         this.initialClickY = -2;
/* 162 */         bindAmountScrolled();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_) {
/* 169 */     if (this.field_178041_q) {
/*     */       
/* 171 */       this.mouseX = mouseXIn;
/* 172 */       this.mouseY = mouseYIn;
/* 173 */       drawBackground();
/* 174 */       int i = getScrollBarX();
/* 175 */       int j = i + 6;
/* 176 */       bindAmountScrolled();
/* 177 */       GlStateManager.disableLighting();
/* 178 */       GlStateManager.disableFog();
/* 179 */       Tessellator tessellator = Tessellator.getInstance();
/* 180 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 181 */       drawContainerBackground(tessellator);
/* 182 */       int k = this.left + this.width / 2 - getListWidth() / 2 + 2;
/* 183 */       int l = this.top + 4 - (int)this.amountScrolled;
/*     */       
/* 185 */       if (this.hasListHeader)
/*     */       {
/* 187 */         drawListHeader(k, l, tessellator);
/*     */       }
/*     */       
/* 190 */       drawSelectionBox(k, l, mouseXIn, mouseYIn);
/* 191 */       GlStateManager.disableDepth();
/* 192 */       int i1 = 4;
/* 193 */       overlayBackground(0, this.top, 255, 255);
/* 194 */       overlayBackground(this.bottom, this.height, 255, 255);
/* 195 */       GlStateManager.enableBlend();
/* 196 */       GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
/* 197 */       GlStateManager.disableAlpha();
/* 198 */       GlStateManager.shadeModel(7425);
/* 199 */       GlStateManager.disableTexture2D();
/* 200 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 201 */       worldrenderer.pos(this.left, (this.top + i1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
/* 202 */       worldrenderer.pos(this.right, (this.top + i1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
/* 203 */       worldrenderer.pos(this.right, this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 204 */       worldrenderer.pos(this.left, this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 205 */       tessellator.draw();
/* 206 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 207 */       worldrenderer.pos(this.left, this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 208 */       worldrenderer.pos(this.right, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 209 */       worldrenderer.pos(this.right, (this.bottom - i1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
/* 210 */       worldrenderer.pos(this.left, (this.bottom - i1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
/* 211 */       tessellator.draw();
/* 212 */       int j1 = func_148135_f();
/*     */       
/* 214 */       if (j1 > 0) {
/*     */         
/* 216 */         int k1 = (this.bottom - this.top) * (this.bottom - this.top) / getContentHeight();
/* 217 */         k1 = MathHelper.clamp_int(k1, 32, this.bottom - this.top - 8);
/* 218 */         int l1 = (int)this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;
/*     */         
/* 220 */         if (l1 < this.top)
/*     */         {
/* 222 */           l1 = this.top;
/*     */         }
/*     */         
/* 225 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 226 */         worldrenderer.pos(i, this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 227 */         worldrenderer.pos(j, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 228 */         worldrenderer.pos(j, this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 229 */         worldrenderer.pos(i, this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 230 */         tessellator.draw();
/* 231 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 232 */         worldrenderer.pos(i, (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 233 */         worldrenderer.pos(j, (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 234 */         worldrenderer.pos(j, l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 235 */         worldrenderer.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 236 */         tessellator.draw();
/* 237 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 238 */         worldrenderer.pos(i, (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
/* 239 */         worldrenderer.pos((j - 1), (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
/* 240 */         worldrenderer.pos((j - 1), l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
/* 241 */         worldrenderer.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
/* 242 */         tessellator.draw();
/*     */       } 
/*     */       
/* 245 */       func_148142_b(mouseXIn, mouseYIn);
/* 246 */       GlStateManager.enableTexture2D();
/* 247 */       GlStateManager.shadeModel(7424);
/* 248 */       GlStateManager.enableAlpha();
/* 249 */       GlStateManager.disableBlend();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() {
/* 255 */     if (isMouseYWithinSlotBounds(this.mouseY)) {
/*     */       
/* 257 */       if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
/*     */         
/* 259 */         int i = (this.width - getListWidth()) / 2;
/* 260 */         int j = (this.width + getListWidth()) / 2;
/* 261 */         int k = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
/* 262 */         int l = k / this.slotHeight;
/*     */         
/* 264 */         if (l < getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0) {
/*     */           
/* 266 */           elementClicked(l, false, this.mouseX, this.mouseY);
/* 267 */           this.selectedElement = l;
/*     */         }
/* 269 */         else if (this.mouseX >= i && this.mouseX <= j && k < 0) {
/*     */           
/* 271 */           func_148132_a(this.mouseX - i, this.mouseY - this.top + (int)this.amountScrolled - 4);
/*     */         } 
/*     */       } 
/*     */       
/* 275 */       if (Mouse.isButtonDown(0) && getEnabled()) {
/*     */         
/* 277 */         if (this.initialClickY != -1) {
/*     */           
/* 279 */           if (this.initialClickY >= 0)
/*     */           {
/* 281 */             this.amountScrolled -= (this.mouseY - this.initialClickY) * this.scrollMultiplier;
/* 282 */             this.initialClickY = this.mouseY;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 287 */           boolean flag1 = true;
/*     */           
/* 289 */           if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
/*     */             
/* 291 */             int j2 = (this.width - getListWidth()) / 2;
/* 292 */             int k2 = (this.width + getListWidth()) / 2;
/* 293 */             int l2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
/* 294 */             int i1 = l2 / this.slotHeight;
/*     */             
/* 296 */             if (i1 < getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i1 >= 0 && l2 >= 0) {
/*     */               
/* 298 */               boolean flag = (i1 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L);
/* 299 */               elementClicked(i1, flag, this.mouseX, this.mouseY);
/* 300 */               this.selectedElement = i1;
/* 301 */               this.lastClicked = Minecraft.getSystemTime();
/*     */             }
/* 303 */             else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
/*     */               
/* 305 */               func_148132_a(this.mouseX - j2, this.mouseY - this.top + (int)this.amountScrolled - 4);
/* 306 */               flag1 = false;
/*     */             } 
/*     */             
/* 309 */             int i3 = getScrollBarX();
/* 310 */             int j1 = i3 + 6;
/*     */             
/* 312 */             if (this.mouseX >= i3 && this.mouseX <= j1) {
/*     */               
/* 314 */               this.scrollMultiplier = -1.0F;
/* 315 */               int k1 = func_148135_f();
/*     */               
/* 317 */               if (k1 < 1)
/*     */               {
/* 319 */                 k1 = 1;
/*     */               }
/*     */               
/* 322 */               int l1 = (int)(((this.bottom - this.top) * (this.bottom - this.top)) / getContentHeight());
/* 323 */               l1 = MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
/* 324 */               this.scrollMultiplier /= (this.bottom - this.top - l1) / k1;
/*     */             }
/*     */             else {
/*     */               
/* 328 */               this.scrollMultiplier = 1.0F;
/*     */             } 
/*     */             
/* 331 */             if (flag1)
/*     */             {
/* 333 */               this.initialClickY = this.mouseY;
/*     */             }
/*     */             else
/*     */             {
/* 337 */               this.initialClickY = -2;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 342 */             this.initialClickY = -2;
/*     */           }
/*     */         
/*     */         } 
/*     */       } else {
/*     */         
/* 348 */         this.initialClickY = -1;
/*     */       } 
/*     */       
/* 351 */       int i2 = Mouse.getEventDWheel();
/*     */       
/* 353 */       if (i2 != 0) {
/*     */         
/* 355 */         if (i2 > 0) {
/*     */           
/* 357 */           i2 = -1;
/*     */         }
/* 359 */         else if (i2 < 0) {
/*     */           
/* 361 */           i2 = 1;
/*     */         } 
/*     */         
/* 364 */         this.amountScrolled += (i2 * this.slotHeight / 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnabled(boolean enabledIn) {
/* 371 */     this.enabled = enabledIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getEnabled() {
/* 376 */     return this.enabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getListWidth() {
/* 381 */     return 220;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int mouseXIn, int mouseYIn) {
/* 386 */     int i = getSize();
/* 387 */     Tessellator tessellator = Tessellator.getInstance();
/* 388 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*     */     
/* 390 */     for (int j = 0; j < i; j++) {
/*     */       
/* 392 */       int k = p_148120_2_ + j * this.slotHeight + this.headerPadding;
/* 393 */       int l = this.slotHeight - 4;
/*     */       
/* 395 */       if (k > this.bottom || k + l < this.top)
/*     */       {
/* 397 */         func_178040_a(j, p_148120_1_, k);
/*     */       }
/*     */       
/* 400 */       if (this.showSelectionBox && isSelected(j)) {
/*     */         
/* 402 */         int i1 = this.left + this.width / 2 - getListWidth() / 2;
/* 403 */         int j1 = this.left + this.width / 2 + getListWidth() / 2;
/* 404 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 405 */         GlStateManager.disableTexture2D();
/* 406 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 407 */         worldrenderer.pos(i1, (k + l + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 408 */         worldrenderer.pos(j1, (k + l + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 409 */         worldrenderer.pos(j1, (k - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 410 */         worldrenderer.pos(i1, (k - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 411 */         worldrenderer.pos((i1 + 1), (k + l + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 412 */         worldrenderer.pos((j1 - 1), (k + l + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 413 */         worldrenderer.pos((j1 - 1), (k - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 414 */         worldrenderer.pos((i1 + 1), (k - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 415 */         tessellator.draw();
/* 416 */         GlStateManager.enableTexture2D();
/*     */       } 
/*     */       
/* 419 */       if (!(this instanceof GuiResourcePackList) || (k >= this.top - this.slotHeight && k <= this.bottom))
/*     */       {
/* 421 */         drawSlot(j, p_148120_1_, k, l, mouseXIn, mouseYIn);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getScrollBarX() {
/* 428 */     return this.width / 2 + 124;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
/* 433 */     Tessellator tessellator = Tessellator.getInstance();
/* 434 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 435 */     this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
/* 436 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 437 */     float f = 32.0F;
/* 438 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 439 */     worldrenderer.pos(this.left, endY, 0.0D).tex(0.0D, (endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
/* 440 */     worldrenderer.pos((this.left + this.width), endY, 0.0D).tex((this.width / 32.0F), (endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
/* 441 */     worldrenderer.pos((this.left + this.width), startY, 0.0D).tex((this.width / 32.0F), (startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
/* 442 */     worldrenderer.pos(this.left, startY, 0.0D).tex(0.0D, (startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
/* 443 */     tessellator.draw();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSlotXBoundsFromLeft(int leftIn) {
/* 448 */     this.left = leftIn;
/* 449 */     this.right = leftIn + this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSlotHeight() {
/* 454 */     return this.slotHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawContainerBackground(Tessellator p_drawContainerBackground_1_) {
/* 459 */     WorldRenderer worldrenderer = p_drawContainerBackground_1_.getWorldRenderer();
/* 460 */     this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
/* 461 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 462 */     float f = 32.0F;
/* 463 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 464 */     worldrenderer.pos(this.left, this.bottom, 0.0D).tex((this.left / f), ((this.bottom + (int)this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
/* 465 */     worldrenderer.pos(this.right, this.bottom, 0.0D).tex((this.right / f), ((this.bottom + (int)this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
/* 466 */     worldrenderer.pos(this.right, this.top, 0.0D).tex((this.right / f), ((this.top + (int)this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
/* 467 */     worldrenderer.pos(this.left, this.top, 0.0D).tex((this.left / f), ((this.top + (int)this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
/* 468 */     p_drawContainerBackground_1_.draw();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */