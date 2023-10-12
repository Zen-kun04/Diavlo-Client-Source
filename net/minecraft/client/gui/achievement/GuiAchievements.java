/*     */ package net.minecraft.client.gui.achievement;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiOptionButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.IProgressMeter;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*     */ import net.minecraft.stats.Achievement;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatFileWriter;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class GuiAchievements extends GuiScreen implements IProgressMeter {
/*  28 */   private static final int field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
/*  29 */   private static final int field_146571_z = AchievementList.minDisplayRow * 24 - 112;
/*  30 */   private static final int field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
/*  31 */   private static final int field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
/*  32 */   private static final ResourceLocation ACHIEVEMENT_BACKGROUND = new ResourceLocation("textures/gui/achievement/achievement_background.png");
/*     */   protected GuiScreen parentScreen;
/*  34 */   protected int field_146555_f = 256;
/*  35 */   protected int field_146557_g = 202;
/*     */   protected int field_146563_h;
/*     */   protected int field_146564_i;
/*  38 */   protected float field_146570_r = 1.0F;
/*     */   
/*     */   protected double field_146569_s;
/*     */   protected double field_146568_t;
/*     */   protected double field_146567_u;
/*     */   protected double field_146566_v;
/*     */   protected double field_146565_w;
/*     */   protected double field_146573_x;
/*     */   private int field_146554_D;
/*     */   private StatFileWriter statFileWriter;
/*     */   private boolean loadingAchievements = true;
/*     */   
/*     */   public GuiAchievements(GuiScreen parentScreenIn, StatFileWriter statFileWriterIn) {
/*  51 */     this.parentScreen = parentScreenIn;
/*  52 */     this.statFileWriter = statFileWriterIn;
/*  53 */     int i = 141;
/*  54 */     int j = 141;
/*  55 */     this.field_146569_s = this.field_146567_u = this.field_146565_w = (AchievementList.openInventory.displayColumn * 24 - i / 2 - 12);
/*  56 */     this.field_146568_t = this.field_146566_v = this.field_146573_x = (AchievementList.openInventory.displayRow * 24 - j / 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  61 */     this.mc.getNetHandler().addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
/*  62 */     this.buttonList.clear();
/*  63 */     this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  68 */     if (!this.loadingAchievements)
/*     */     {
/*  70 */       if (button.id == 1)
/*     */       {
/*  72 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  79 */     if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
/*     */       
/*  81 */       this.mc.displayGuiScreen((GuiScreen)null);
/*  82 */       this.mc.setIngameFocus();
/*     */     }
/*     */     else {
/*     */       
/*  86 */       super.keyTyped(typedChar, keyCode);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  92 */     if (this.loadingAchievements) {
/*     */       
/*  94 */       drawDefaultBackground();
/*  95 */       drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), this.width / 2, this.height / 2, 16777215);
/*  96 */       drawCenteredString(this.fontRendererObj, lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % lanSearchStates.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
/*     */     }
/*     */     else {
/*     */       
/* 100 */       if (Mouse.isButtonDown(0)) {
/*     */         
/* 102 */         int i = (this.width - this.field_146555_f) / 2;
/* 103 */         int j = (this.height - this.field_146557_g) / 2;
/* 104 */         int k = i + 8;
/* 105 */         int l = j + 17;
/*     */         
/* 107 */         if ((this.field_146554_D == 0 || this.field_146554_D == 1) && mouseX >= k && mouseX < k + 224 && mouseY >= l && mouseY < l + 155)
/*     */         {
/* 109 */           if (this.field_146554_D == 0) {
/*     */             
/* 111 */             this.field_146554_D = 1;
/*     */           }
/*     */           else {
/*     */             
/* 115 */             this.field_146567_u -= ((mouseX - this.field_146563_h) * this.field_146570_r);
/* 116 */             this.field_146566_v -= ((mouseY - this.field_146564_i) * this.field_146570_r);
/* 117 */             this.field_146565_w = this.field_146569_s = this.field_146567_u;
/* 118 */             this.field_146573_x = this.field_146568_t = this.field_146566_v;
/*     */           } 
/*     */           
/* 121 */           this.field_146563_h = mouseX;
/* 122 */           this.field_146564_i = mouseY;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 127 */         this.field_146554_D = 0;
/*     */       } 
/*     */       
/* 130 */       int i1 = Mouse.getDWheel();
/* 131 */       float f3 = this.field_146570_r;
/*     */       
/* 133 */       if (i1 < 0) {
/*     */         
/* 135 */         this.field_146570_r += 0.25F;
/*     */       }
/* 137 */       else if (i1 > 0) {
/*     */         
/* 139 */         this.field_146570_r -= 0.25F;
/*     */       } 
/*     */       
/* 142 */       this.field_146570_r = MathHelper.clamp_float(this.field_146570_r, 1.0F, 2.0F);
/*     */       
/* 144 */       if (this.field_146570_r != f3) {
/*     */         
/* 146 */         float f5 = f3 - this.field_146570_r;
/* 147 */         float f4 = f3 * this.field_146555_f;
/* 148 */         float f = f3 * this.field_146557_g;
/* 149 */         float f1 = this.field_146570_r * this.field_146555_f;
/* 150 */         float f2 = this.field_146570_r * this.field_146557_g;
/* 151 */         this.field_146567_u -= ((f1 - f4) * 0.5F);
/* 152 */         this.field_146566_v -= ((f2 - f) * 0.5F);
/* 153 */         this.field_146565_w = this.field_146569_s = this.field_146567_u;
/* 154 */         this.field_146573_x = this.field_146568_t = this.field_146566_v;
/*     */       } 
/*     */       
/* 157 */       if (this.field_146565_w < field_146572_y)
/*     */       {
/* 159 */         this.field_146565_w = field_146572_y;
/*     */       }
/*     */       
/* 162 */       if (this.field_146573_x < field_146571_z)
/*     */       {
/* 164 */         this.field_146573_x = field_146571_z;
/*     */       }
/*     */       
/* 167 */       if (this.field_146565_w >= field_146559_A)
/*     */       {
/* 169 */         this.field_146565_w = (field_146559_A - 1);
/*     */       }
/*     */       
/* 172 */       if (this.field_146573_x >= field_146560_B)
/*     */       {
/* 174 */         this.field_146573_x = (field_146560_B - 1);
/*     */       }
/*     */       
/* 177 */       drawDefaultBackground();
/* 178 */       drawAchievementScreen(mouseX, mouseY, partialTicks);
/* 179 */       GlStateManager.disableLighting();
/* 180 */       GlStateManager.disableDepth();
/* 181 */       drawTitle();
/* 182 */       GlStateManager.enableLighting();
/* 183 */       GlStateManager.enableDepth();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doneLoading() {
/* 189 */     if (this.loadingAchievements)
/*     */     {
/* 191 */       this.loadingAchievements = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 197 */     if (!this.loadingAchievements) {
/*     */       
/* 199 */       this.field_146569_s = this.field_146567_u;
/* 200 */       this.field_146568_t = this.field_146566_v;
/* 201 */       double d0 = this.field_146565_w - this.field_146567_u;
/* 202 */       double d1 = this.field_146573_x - this.field_146566_v;
/*     */       
/* 204 */       if (d0 * d0 + d1 * d1 < 4.0D) {
/*     */         
/* 206 */         this.field_146567_u += d0;
/* 207 */         this.field_146566_v += d1;
/*     */       }
/*     */       else {
/*     */         
/* 211 */         this.field_146567_u += d0 * 0.85D;
/* 212 */         this.field_146566_v += d1 * 0.85D;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawTitle() {
/* 219 */     int i = (this.width - this.field_146555_f) / 2;
/* 220 */     int j = (this.height - this.field_146557_g) / 2;
/* 221 */     this.fontRendererObj.drawString(I18n.format("gui.achievements", new Object[0]), (i + 15), (j + 5), 4210752);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawAchievementScreen(int p_146552_1_, int p_146552_2_, float p_146552_3_) {
/* 226 */     int i = MathHelper.floor_double(this.field_146569_s + (this.field_146567_u - this.field_146569_s) * p_146552_3_);
/* 227 */     int j = MathHelper.floor_double(this.field_146568_t + (this.field_146566_v - this.field_146568_t) * p_146552_3_);
/*     */     
/* 229 */     if (i < field_146572_y)
/*     */     {
/* 231 */       i = field_146572_y;
/*     */     }
/*     */     
/* 234 */     if (j < field_146571_z)
/*     */     {
/* 236 */       j = field_146571_z;
/*     */     }
/*     */     
/* 239 */     if (i >= field_146559_A)
/*     */     {
/* 241 */       i = field_146559_A - 1;
/*     */     }
/*     */     
/* 244 */     if (j >= field_146560_B)
/*     */     {
/* 246 */       j = field_146560_B - 1;
/*     */     }
/*     */     
/* 249 */     int k = (this.width - this.field_146555_f) / 2;
/* 250 */     int l = (this.height - this.field_146557_g) / 2;
/* 251 */     int i1 = k + 16;
/* 252 */     int j1 = l + 17;
/* 253 */     this.zLevel = 0.0F;
/* 254 */     GlStateManager.depthFunc(518);
/* 255 */     GlStateManager.pushMatrix();
/* 256 */     GlStateManager.translate(i1, j1, -200.0F);
/* 257 */     GlStateManager.scale(1.0F / this.field_146570_r, 1.0F / this.field_146570_r, 0.0F);
/* 258 */     GlStateManager.enableTexture2D();
/* 259 */     GlStateManager.disableLighting();
/* 260 */     GlStateManager.enableRescaleNormal();
/* 261 */     GlStateManager.enableColorMaterial();
/* 262 */     int k1 = i + 288 >> 4;
/* 263 */     int l1 = j + 288 >> 4;
/* 264 */     int i2 = (i + 288) % 16;
/* 265 */     int j2 = (j + 288) % 16;
/* 266 */     int k2 = 4;
/* 267 */     int l2 = 8;
/* 268 */     int i3 = 10;
/* 269 */     int j3 = 22;
/* 270 */     int k3 = 37;
/* 271 */     Random random = new Random();
/* 272 */     float f = 16.0F / this.field_146570_r;
/* 273 */     float f1 = 16.0F / this.field_146570_r;
/*     */     
/* 275 */     for (int l3 = 0; l3 * f - j2 < 155.0F; l3++) {
/*     */       
/* 277 */       float f2 = 0.6F - (l1 + l3) / 25.0F * 0.3F;
/* 278 */       GlStateManager.color(f2, f2, f2, 1.0F);
/*     */       
/* 280 */       for (int i4 = 0; i4 * f1 - i2 < 224.0F; i4++) {
/*     */         
/* 282 */         random.setSeed((this.mc.getSession().getPlayerID().hashCode() + k1 + i4 + (l1 + l3) * 16));
/* 283 */         int j4 = random.nextInt(1 + l1 + l3) + (l1 + l3) / 2;
/* 284 */         TextureAtlasSprite textureatlassprite = func_175371_a((Block)Blocks.sand);
/*     */         
/* 286 */         if (j4 <= 37 && l1 + l3 != 35) {
/*     */           
/* 288 */           if (j4 == 22) {
/*     */             
/* 290 */             if (random.nextInt(2) == 0)
/*     */             {
/* 292 */               textureatlassprite = func_175371_a(Blocks.diamond_ore);
/*     */             }
/*     */             else
/*     */             {
/* 296 */               textureatlassprite = func_175371_a(Blocks.redstone_ore);
/*     */             }
/*     */           
/* 299 */           } else if (j4 == 10) {
/*     */             
/* 301 */             textureatlassprite = func_175371_a(Blocks.iron_ore);
/*     */           }
/* 303 */           else if (j4 == 8) {
/*     */             
/* 305 */             textureatlassprite = func_175371_a(Blocks.coal_ore);
/*     */           }
/* 307 */           else if (j4 > 4) {
/*     */             
/* 309 */             textureatlassprite = func_175371_a(Blocks.stone);
/*     */           }
/* 311 */           else if (j4 > 0) {
/*     */             
/* 313 */             textureatlassprite = func_175371_a(Blocks.dirt);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 318 */           Block block = Blocks.bedrock;
/* 319 */           textureatlassprite = func_175371_a(block);
/*     */         } 
/*     */         
/* 322 */         this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 323 */         drawTexturedModalRect(i4 * 16 - i2, l3 * 16 - j2, textureatlassprite, 16, 16);
/*     */       } 
/*     */     } 
/*     */     
/* 327 */     GlStateManager.enableDepth();
/* 328 */     GlStateManager.depthFunc(515);
/* 329 */     this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
/*     */     
/* 331 */     for (int j5 = 0; j5 < AchievementList.achievementList.size(); j5++) {
/*     */       
/* 333 */       Achievement achievement1 = AchievementList.achievementList.get(j5);
/*     */       
/* 335 */       if (achievement1.parentAchievement != null) {
/*     */         
/* 337 */         int k5 = achievement1.displayColumn * 24 - i + 11;
/* 338 */         int l5 = achievement1.displayRow * 24 - j + 11;
/* 339 */         int j6 = achievement1.parentAchievement.displayColumn * 24 - i + 11;
/* 340 */         int k6 = achievement1.parentAchievement.displayRow * 24 - j + 11;
/* 341 */         boolean flag = this.statFileWriter.hasAchievementUnlocked(achievement1);
/* 342 */         boolean flag1 = this.statFileWriter.canUnlockAchievement(achievement1);
/* 343 */         int k4 = this.statFileWriter.func_150874_c(achievement1);
/*     */         
/* 345 */         if (k4 <= 4) {
/*     */           
/* 347 */           int l4 = -16777216;
/*     */           
/* 349 */           if (flag) {
/*     */             
/* 351 */             l4 = -6250336;
/*     */           }
/* 353 */           else if (flag1) {
/*     */             
/* 355 */             l4 = -16711936;
/*     */           } 
/*     */           
/* 358 */           this; drawHorizontalLine(k5, j6, l5, l4);
/* 359 */           this; drawVerticalLine(j6, l5, k6, l4);
/*     */           
/* 361 */           if (k5 > j6) {
/*     */             
/* 363 */             drawTexturedModalRect(k5 - 11 - 7, l5 - 5, 114, 234, 7, 11);
/*     */           }
/* 365 */           else if (k5 < j6) {
/*     */             
/* 367 */             drawTexturedModalRect(k5 + 11, l5 - 5, 107, 234, 7, 11);
/*     */           }
/* 369 */           else if (l5 > k6) {
/*     */             
/* 371 */             drawTexturedModalRect(k5 - 5, l5 - 11 - 7, 96, 234, 11, 7);
/*     */           }
/* 373 */           else if (l5 < k6) {
/*     */             
/* 375 */             drawTexturedModalRect(k5 - 5, l5 + 11, 96, 241, 11, 7);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 381 */     Achievement achievement = null;
/* 382 */     float f3 = (p_146552_1_ - i1) * this.field_146570_r;
/* 383 */     float f4 = (p_146552_2_ - j1) * this.field_146570_r;
/* 384 */     RenderHelper.enableGUIStandardItemLighting();
/* 385 */     GlStateManager.disableLighting();
/* 386 */     GlStateManager.enableRescaleNormal();
/* 387 */     GlStateManager.enableColorMaterial();
/*     */     
/* 389 */     for (int i6 = 0; i6 < AchievementList.achievementList.size(); i6++) {
/*     */       
/* 391 */       Achievement achievement2 = AchievementList.achievementList.get(i6);
/* 392 */       int l6 = achievement2.displayColumn * 24 - i;
/* 393 */       int j7 = achievement2.displayRow * 24 - j;
/*     */       
/* 395 */       if (l6 >= -24 && j7 >= -24 && l6 <= 224.0F * this.field_146570_r && j7 <= 155.0F * this.field_146570_r) {
/*     */         
/* 397 */         int l7 = this.statFileWriter.func_150874_c(achievement2);
/*     */         
/* 399 */         if (this.statFileWriter.hasAchievementUnlocked(achievement2)) {
/*     */           
/* 401 */           float f5 = 0.75F;
/* 402 */           GlStateManager.color(f5, f5, f5, 1.0F);
/*     */         }
/* 404 */         else if (this.statFileWriter.canUnlockAchievement(achievement2)) {
/*     */           
/* 406 */           float f6 = 1.0F;
/* 407 */           GlStateManager.color(f6, f6, f6, 1.0F);
/*     */         }
/* 409 */         else if (l7 < 3) {
/*     */           
/* 411 */           float f7 = 0.3F;
/* 412 */           GlStateManager.color(f7, f7, f7, 1.0F);
/*     */         }
/* 414 */         else if (l7 == 3) {
/*     */           
/* 416 */           float f8 = 0.2F;
/* 417 */           GlStateManager.color(f8, f8, f8, 1.0F);
/*     */         }
/*     */         else {
/*     */           
/* 421 */           if (l7 != 4) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */           
/* 426 */           float f9 = 0.1F;
/* 427 */           GlStateManager.color(f9, f9, f9, 1.0F);
/*     */         } 
/*     */         
/* 430 */         this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
/*     */         
/* 432 */         if (achievement2.getSpecial()) {
/*     */           
/* 434 */           drawTexturedModalRect(l6 - 2, j7 - 2, 26, 202, 26, 26);
/*     */         }
/*     */         else {
/*     */           
/* 438 */           drawTexturedModalRect(l6 - 2, j7 - 2, 0, 202, 26, 26);
/*     */         } 
/*     */         
/* 441 */         if (!this.statFileWriter.canUnlockAchievement(achievement2)) {
/*     */           
/* 443 */           float f10 = 0.1F;
/* 444 */           GlStateManager.color(f10, f10, f10, 1.0F);
/* 445 */           this.itemRender.isNotRenderingEffectsInGUI(false);
/*     */         } 
/*     */         
/* 448 */         GlStateManager.enableLighting();
/* 449 */         GlStateManager.enableCull();
/* 450 */         this.itemRender.renderItemAndEffectIntoGUI(achievement2.theItemStack, l6 + 3, j7 + 3);
/* 451 */         GlStateManager.blendFunc(770, 771);
/* 452 */         GlStateManager.disableLighting();
/*     */         
/* 454 */         if (!this.statFileWriter.canUnlockAchievement(achievement2))
/*     */         {
/* 456 */           this.itemRender.isNotRenderingEffectsInGUI(true);
/*     */         }
/*     */         
/* 459 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */         
/* 461 */         if (f3 >= l6 && f3 <= (l6 + 22) && f4 >= j7 && f4 <= (j7 + 22))
/*     */         {
/* 463 */           achievement = achievement2;
/*     */         }
/*     */       } 
/*     */       continue;
/*     */     } 
/* 468 */     GlStateManager.disableDepth();
/* 469 */     GlStateManager.enableBlend();
/* 470 */     GlStateManager.popMatrix();
/* 471 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 472 */     this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
/* 473 */     drawTexturedModalRect(k, l, 0, 0, this.field_146555_f, this.field_146557_g);
/* 474 */     this.zLevel = 0.0F;
/* 475 */     GlStateManager.depthFunc(515);
/* 476 */     GlStateManager.disableDepth();
/* 477 */     GlStateManager.enableTexture2D();
/* 478 */     super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);
/*     */     
/* 480 */     if (achievement != null) {
/*     */       
/* 482 */       String s = achievement.getStatName().getUnformattedText();
/* 483 */       String s1 = achievement.getDescription();
/* 484 */       int i7 = p_146552_1_ + 12;
/* 485 */       int k7 = p_146552_2_ - 4;
/* 486 */       int i8 = this.statFileWriter.func_150874_c(achievement);
/*     */       
/* 488 */       if (this.statFileWriter.canUnlockAchievement(achievement)) {
/*     */         
/* 490 */         int j8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
/* 491 */         int i9 = this.fontRendererObj.splitStringWidth(s1, j8);
/*     */         
/* 493 */         if (this.statFileWriter.hasAchievementUnlocked(achievement))
/*     */         {
/* 495 */           i9 += 12;
/*     */         }
/*     */         
/* 498 */         drawGradientRect(i7 - 3, k7 - 3, i7 + j8 + 3, k7 + i9 + 3 + 12, -1073741824, -1073741824);
/* 499 */         this.fontRendererObj.drawSplitString(s1, i7, k7 + 12, j8, -6250336);
/*     */         
/* 501 */         if (this.statFileWriter.hasAchievementUnlocked(achievement))
/*     */         {
/* 503 */           this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken", new Object[0]), i7, (k7 + i9 + 4), -7302913);
/*     */         }
/*     */       }
/* 506 */       else if (i8 == 3) {
/*     */         
/* 508 */         s = I18n.format("achievement.unknown", new Object[0]);
/* 509 */         int k8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
/* 510 */         String s2 = (new ChatComponentTranslation("achievement.requires", new Object[] { achievement.parentAchievement.getStatName() })).getUnformattedText();
/* 511 */         int i5 = this.fontRendererObj.splitStringWidth(s2, k8);
/* 512 */         drawGradientRect(i7 - 3, k7 - 3, i7 + k8 + 3, k7 + i5 + 12 + 3, -1073741824, -1073741824);
/* 513 */         this.fontRendererObj.drawSplitString(s2, i7, k7 + 12, k8, -9416624);
/*     */       }
/* 515 */       else if (i8 < 3) {
/*     */         
/* 517 */         int l8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
/* 518 */         String s3 = (new ChatComponentTranslation("achievement.requires", new Object[] { achievement.parentAchievement.getStatName() })).getUnformattedText();
/* 519 */         int j9 = this.fontRendererObj.splitStringWidth(s3, l8);
/* 520 */         drawGradientRect(i7 - 3, k7 - 3, i7 + l8 + 3, k7 + j9 + 12 + 3, -1073741824, -1073741824);
/* 521 */         this.fontRendererObj.drawSplitString(s3, i7, k7 + 12, l8, -9416624);
/*     */       }
/*     */       else {
/*     */         
/* 525 */         s = null;
/*     */       } 
/*     */       
/* 528 */       if (s != null)
/*     */       {
/* 530 */         this.fontRendererObj.drawStringWithShadow(s, i7, k7, this.statFileWriter.canUnlockAchievement(achievement) ? (achievement.getSpecial() ? -128 : -1) : (achievement.getSpecial() ? -8355776 : -8355712));
/*     */       }
/*     */     } 
/*     */     
/* 534 */     GlStateManager.enableDepth();
/* 535 */     GlStateManager.enableLighting();
/* 536 */     RenderHelper.disableStandardItemLighting();
/*     */   }
/*     */ 
/*     */   
/*     */   private TextureAtlasSprite func_175371_a(Block p_175371_1_) {
/* 541 */     return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(p_175371_1_.getDefaultState());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 546 */     return !this.loadingAchievements;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\achievement\GuiAchievements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */