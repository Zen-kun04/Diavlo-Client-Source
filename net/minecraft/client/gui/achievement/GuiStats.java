/*     */ package net.minecraft.client.gui.achievement;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiSlot;
/*     */ import net.minecraft.client.gui.IProgressMeter;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.stats.StatCrafting;
/*     */ import net.minecraft.stats.StatFileWriter;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class GuiStats extends GuiScreen implements IProgressMeter {
/*  34 */   protected String screenTitle = "Select world";
/*     */   protected GuiScreen parentScreen;
/*     */   private StatsGeneral generalStats;
/*     */   private StatsItem itemStats;
/*     */   private StatsBlock blockStats;
/*     */   private StatsMobsList mobStats;
/*     */   private StatFileWriter field_146546_t;
/*     */   private GuiSlot displaySlot;
/*     */   private boolean doesGuiPauseGame = true;
/*     */   
/*     */   public GuiStats(GuiScreen p_i1071_1_, StatFileWriter p_i1071_2_) {
/*  45 */     this.parentScreen = p_i1071_1_;
/*  46 */     this.field_146546_t = p_i1071_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  51 */     this.screenTitle = I18n.format("gui.stats", new Object[0]);
/*  52 */     this.doesGuiPauseGame = true;
/*  53 */     this.mc.getNetHandler().addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  58 */     super.handleMouseInput();
/*     */     
/*  60 */     if (this.displaySlot != null)
/*     */     {
/*  62 */       this.displaySlot.handleMouseInput();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175366_f() {
/*  68 */     this.generalStats = new StatsGeneral(this.mc);
/*  69 */     this.generalStats.registerScrollButtons(1, 1);
/*  70 */     this.itemStats = new StatsItem(this.mc);
/*  71 */     this.itemStats.registerScrollButtons(1, 1);
/*  72 */     this.blockStats = new StatsBlock(this.mc);
/*  73 */     this.blockStats.registerScrollButtons(1, 1);
/*  74 */     this.mobStats = new StatsMobsList(this.mc);
/*  75 */     this.mobStats.registerScrollButtons(1, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createButtons() {
/*  80 */     this.buttonList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
/*  81 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 160, this.height - 52, 80, 20, I18n.format("stat.generalButton", new Object[0])));
/*     */     GuiButton guibutton;
/*  83 */     this.buttonList.add(guibutton = new GuiButton(2, this.width / 2 - 80, this.height - 52, 80, 20, I18n.format("stat.blocksButton", new Object[0])));
/*     */     GuiButton guibutton1;
/*  85 */     this.buttonList.add(guibutton1 = new GuiButton(3, this.width / 2, this.height - 52, 80, 20, I18n.format("stat.itemsButton", new Object[0])));
/*     */     GuiButton guibutton2;
/*  87 */     this.buttonList.add(guibutton2 = new GuiButton(4, this.width / 2 + 80, this.height - 52, 80, 20, I18n.format("stat.mobsButton", new Object[0])));
/*     */     
/*  89 */     if (this.blockStats.getSize() == 0)
/*     */     {
/*  91 */       guibutton.enabled = false;
/*     */     }
/*     */     
/*  94 */     if (this.itemStats.getSize() == 0)
/*     */     {
/*  96 */       guibutton1.enabled = false;
/*     */     }
/*     */     
/*  99 */     if (this.mobStats.getSize() == 0)
/*     */     {
/* 101 */       guibutton2.enabled = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 107 */     if (button.enabled)
/*     */     {
/* 109 */       if (button.id == 0) {
/*     */         
/* 111 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/* 113 */       else if (button.id == 1) {
/*     */         
/* 115 */         this.displaySlot = this.generalStats;
/*     */       }
/* 117 */       else if (button.id == 3) {
/*     */         
/* 119 */         this.displaySlot = this.itemStats;
/*     */       }
/* 121 */       else if (button.id == 2) {
/*     */         
/* 123 */         this.displaySlot = this.blockStats;
/*     */       }
/* 125 */       else if (button.id == 4) {
/*     */         
/* 127 */         this.displaySlot = this.mobStats;
/*     */       }
/*     */       else {
/*     */         
/* 131 */         this.displaySlot.actionPerformed(button);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 138 */     if (this.doesGuiPauseGame) {
/*     */       
/* 140 */       drawDefaultBackground();
/* 141 */       drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), this.width / 2, this.height / 2, 16777215);
/* 142 */       drawCenteredString(this.fontRendererObj, lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % lanSearchStates.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
/*     */     }
/*     */     else {
/*     */       
/* 146 */       this.displaySlot.drawScreen(mouseX, mouseY, partialTicks);
/* 147 */       drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 20, 16777215);
/* 148 */       super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doneLoading() {
/* 154 */     if (this.doesGuiPauseGame) {
/*     */       
/* 156 */       func_175366_f();
/* 157 */       createButtons();
/* 158 */       this.displaySlot = this.generalStats;
/* 159 */       this.doesGuiPauseGame = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 165 */     return !this.doesGuiPauseGame;
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawStatsScreen(int p_146521_1_, int p_146521_2_, Item p_146521_3_) {
/* 170 */     drawButtonBackground(p_146521_1_ + 1, p_146521_2_ + 1);
/* 171 */     GlStateManager.enableRescaleNormal();
/* 172 */     RenderHelper.enableGUIStandardItemLighting();
/* 173 */     this.itemRender.renderItemIntoGUI(new ItemStack(p_146521_3_, 1, 0), p_146521_1_ + 2, p_146521_2_ + 2);
/* 174 */     RenderHelper.disableStandardItemLighting();
/* 175 */     GlStateManager.disableRescaleNormal();
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawButtonBackground(int p_146531_1_, int p_146531_2_) {
/* 180 */     drawSprite(p_146531_1_, p_146531_2_, 0, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawSprite(int p_146527_1_, int p_146527_2_, int p_146527_3_, int p_146527_4_) {
/* 185 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 186 */     this.mc.getTextureManager().bindTexture(statIcons);
/* 187 */     float f = 0.0078125F;
/* 188 */     float f1 = 0.0078125F;
/* 189 */     int i = 18;
/* 190 */     int j = 18;
/* 191 */     Tessellator tessellator = Tessellator.getInstance();
/* 192 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 193 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 194 */     worldrenderer.pos((p_146527_1_ + 0), (p_146527_2_ + 18), this.zLevel).tex(((p_146527_3_ + 0) * 0.0078125F), ((p_146527_4_ + 18) * 0.0078125F)).endVertex();
/* 195 */     worldrenderer.pos((p_146527_1_ + 18), (p_146527_2_ + 18), this.zLevel).tex(((p_146527_3_ + 18) * 0.0078125F), ((p_146527_4_ + 18) * 0.0078125F)).endVertex();
/* 196 */     worldrenderer.pos((p_146527_1_ + 18), (p_146527_2_ + 0), this.zLevel).tex(((p_146527_3_ + 18) * 0.0078125F), ((p_146527_4_ + 0) * 0.0078125F)).endVertex();
/* 197 */     worldrenderer.pos((p_146527_1_ + 0), (p_146527_2_ + 0), this.zLevel).tex(((p_146527_3_ + 0) * 0.0078125F), ((p_146527_4_ + 0) * 0.0078125F)).endVertex();
/* 198 */     tessellator.draw();
/*     */   }
/*     */   
/*     */   abstract class Stats
/*     */     extends GuiSlot {
/* 203 */     protected int field_148218_l = -1;
/*     */     protected List<StatCrafting> statsHolder;
/*     */     protected Comparator<StatCrafting> statSorter;
/* 206 */     protected int field_148217_o = -1;
/*     */     
/*     */     protected int field_148215_p;
/*     */     
/*     */     protected Stats(Minecraft mcIn) {
/* 211 */       super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 20);
/* 212 */       setShowSelectionBox(false);
/* 213 */       setHasListHeader(true, 20);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
/*     */ 
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 222 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawBackground() {
/* 227 */       GuiStats.this.drawDefaultBackground();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
/* 232 */       if (!Mouse.isButtonDown(0))
/*     */       {
/* 234 */         this.field_148218_l = -1;
/*     */       }
/*     */       
/* 237 */       if (this.field_148218_l == 0) {
/*     */         
/* 239 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 0);
/*     */       }
/*     */       else {
/*     */         
/* 243 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 18);
/*     */       } 
/*     */       
/* 246 */       if (this.field_148218_l == 1) {
/*     */         
/* 248 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 0);
/*     */       }
/*     */       else {
/*     */         
/* 252 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 18);
/*     */       } 
/*     */       
/* 255 */       if (this.field_148218_l == 2) {
/*     */         
/* 257 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 0);
/*     */       }
/*     */       else {
/*     */         
/* 261 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 18);
/*     */       } 
/*     */       
/* 264 */       if (this.field_148217_o != -1) {
/*     */         
/* 266 */         int i = 79;
/* 267 */         int j = 18;
/*     */         
/* 269 */         if (this.field_148217_o == 1) {
/*     */           
/* 271 */           i = 129;
/*     */         }
/* 273 */         else if (this.field_148217_o == 2) {
/*     */           
/* 275 */           i = 179;
/*     */         } 
/*     */         
/* 278 */         if (this.field_148215_p == 1)
/*     */         {
/* 280 */           j = 36;
/*     */         }
/*     */         
/* 283 */         GuiStats.this.drawSprite(p_148129_1_ + i, p_148129_2_ + 1, j, 0);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void func_148132_a(int p_148132_1_, int p_148132_2_) {
/* 289 */       this.field_148218_l = -1;
/*     */       
/* 291 */       if (p_148132_1_ >= 79 && p_148132_1_ < 115) {
/*     */         
/* 293 */         this.field_148218_l = 0;
/*     */       }
/* 295 */       else if (p_148132_1_ >= 129 && p_148132_1_ < 165) {
/*     */         
/* 297 */         this.field_148218_l = 1;
/*     */       }
/* 299 */       else if (p_148132_1_ >= 179 && p_148132_1_ < 215) {
/*     */         
/* 301 */         this.field_148218_l = 2;
/*     */       } 
/*     */       
/* 304 */       if (this.field_148218_l >= 0) {
/*     */         
/* 306 */         func_148212_h(this.field_148218_l);
/* 307 */         this.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected final int getSize() {
/* 313 */       return this.statsHolder.size();
/*     */     }
/*     */ 
/*     */     
/*     */     protected final StatCrafting func_148211_c(int p_148211_1_) {
/* 318 */       return this.statsHolder.get(p_148211_1_);
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract String func_148210_b(int param1Int);
/*     */     
/*     */     protected void func_148209_a(StatBase p_148209_1_, int p_148209_2_, int p_148209_3_, boolean p_148209_4_) {
/* 325 */       if (p_148209_1_ != null) {
/*     */         
/* 327 */         String s = p_148209_1_.format(GuiStats.this.field_146546_t.readStat(p_148209_1_));
/* 328 */         GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, p_148209_2_ - GuiStats.this.fontRendererObj.getStringWidth(s), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
/*     */       }
/*     */       else {
/*     */         
/* 332 */         String s1 = "-";
/* 333 */         GuiStats.this.drawString(GuiStats.this.fontRendererObj, s1, p_148209_2_ - GuiStats.this.fontRendererObj.getStringWidth(s1), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void func_148142_b(int p_148142_1_, int p_148142_2_) {
/* 339 */       if (p_148142_2_ >= this.top && p_148142_2_ <= this.bottom) {
/*     */         
/* 341 */         int i = getSlotIndexFromScreenCoords(p_148142_1_, p_148142_2_);
/* 342 */         int j = this.width / 2 - 92 - 16;
/*     */         
/* 344 */         if (i >= 0) {
/*     */           
/* 346 */           if (p_148142_1_ < j + 40 || p_148142_1_ > j + 40 + 20) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/* 351 */           StatCrafting statcrafting = func_148211_c(i);
/* 352 */           func_148213_a(statcrafting, p_148142_1_, p_148142_2_);
/*     */         }
/*     */         else {
/*     */           
/* 356 */           String s = "";
/*     */           
/* 358 */           if (p_148142_1_ >= j + 115 - 18 && p_148142_1_ <= j + 115) {
/*     */             
/* 360 */             s = func_148210_b(0);
/*     */           }
/* 362 */           else if (p_148142_1_ >= j + 165 - 18 && p_148142_1_ <= j + 165) {
/*     */             
/* 364 */             s = func_148210_b(1);
/*     */           }
/*     */           else {
/*     */             
/* 368 */             if (p_148142_1_ < j + 215 - 18 || p_148142_1_ > j + 215) {
/*     */               return;
/*     */             }
/*     */ 
/*     */             
/* 373 */             s = func_148210_b(2);
/*     */           } 
/*     */           
/* 376 */           s = ("" + I18n.format(s, new Object[0])).trim();
/*     */           
/* 378 */           if (s.length() > 0) {
/*     */             
/* 380 */             int k = p_148142_1_ + 12;
/* 381 */             int l = p_148142_2_ - 12;
/* 382 */             int i1 = GuiStats.this.fontRendererObj.getStringWidth(s);
/* 383 */             GuiStats.this.drawGradientRect(k - 3, l - 3, k + i1 + 3, l + 8 + 3, -1073741824, -1073741824);
/* 384 */             GuiStats.this.fontRendererObj.drawStringWithShadow(s, k, l, -1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void func_148213_a(StatCrafting p_148213_1_, int p_148213_2_, int p_148213_3_) {
/* 392 */       if (p_148213_1_ != null) {
/*     */         
/* 394 */         Item item = p_148213_1_.func_150959_a();
/* 395 */         ItemStack itemstack = new ItemStack(item);
/* 396 */         String s = itemstack.getUnlocalizedName();
/* 397 */         String s1 = ("" + I18n.format(s + ".name", new Object[0])).trim();
/*     */         
/* 399 */         if (s1.length() > 0) {
/*     */           
/* 401 */           int i = p_148213_2_ + 12;
/* 402 */           int j = p_148213_3_ - 12;
/* 403 */           int k = GuiStats.this.fontRendererObj.getStringWidth(s1);
/* 404 */           GuiStats.this.drawGradientRect(i - 3, j - 3, i + k + 3, j + 8 + 3, -1073741824, -1073741824);
/* 405 */           GuiStats.this.fontRendererObj.drawStringWithShadow(s1, i, j, -1);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void func_148212_h(int p_148212_1_) {
/* 412 */       if (p_148212_1_ != this.field_148217_o) {
/*     */         
/* 414 */         this.field_148217_o = p_148212_1_;
/* 415 */         this.field_148215_p = -1;
/*     */       }
/* 417 */       else if (this.field_148215_p == -1) {
/*     */         
/* 419 */         this.field_148215_p = 1;
/*     */       }
/*     */       else {
/*     */         
/* 423 */         this.field_148217_o = -1;
/* 424 */         this.field_148215_p = 0;
/*     */       } 
/*     */       
/* 427 */       Collections.sort(this.statsHolder, this.statSorter);
/*     */     }
/*     */   }
/*     */   
/*     */   class StatsBlock
/*     */     extends Stats
/*     */   {
/*     */     public StatsBlock(Minecraft mcIn) {
/* 435 */       super(mcIn);
/* 436 */       this.statsHolder = Lists.newArrayList();
/*     */       
/* 438 */       for (StatCrafting statcrafting : StatList.objectMineStats) {
/*     */         
/* 440 */         boolean flag = false;
/* 441 */         int i = Item.getIdFromItem(statcrafting.func_150959_a());
/*     */         
/* 443 */         if (GuiStats.this.field_146546_t.readStat((StatBase)statcrafting) > 0) {
/*     */           
/* 445 */           flag = true;
/*     */         }
/* 447 */         else if (StatList.objectUseStats[i] != null && GuiStats.this.field_146546_t.readStat(StatList.objectUseStats[i]) > 0) {
/*     */           
/* 449 */           flag = true;
/*     */         }
/* 451 */         else if (StatList.objectCraftStats[i] != null && GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[i]) > 0) {
/*     */           
/* 453 */           flag = true;
/*     */         } 
/*     */         
/* 456 */         if (flag)
/*     */         {
/* 458 */           this.statsHolder.add(statcrafting);
/*     */         }
/*     */       } 
/*     */       
/* 462 */       this.statSorter = new Comparator<StatCrafting>()
/*     */         {
/*     */           public int compare(StatCrafting p_compare_1_, StatCrafting p_compare_2_)
/*     */           {
/* 466 */             int j = Item.getIdFromItem(p_compare_1_.func_150959_a());
/* 467 */             int k = Item.getIdFromItem(p_compare_2_.func_150959_a());
/* 468 */             StatBase statbase = null;
/* 469 */             StatBase statbase1 = null;
/*     */             
/* 471 */             if (GuiStats.StatsBlock.this.field_148217_o == 2) {
/*     */               
/* 473 */               statbase = StatList.mineBlockStatArray[j];
/* 474 */               statbase1 = StatList.mineBlockStatArray[k];
/*     */             }
/* 476 */             else if (GuiStats.StatsBlock.this.field_148217_o == 0) {
/*     */               
/* 478 */               statbase = StatList.objectCraftStats[j];
/* 479 */               statbase1 = StatList.objectCraftStats[k];
/*     */             }
/* 481 */             else if (GuiStats.StatsBlock.this.field_148217_o == 1) {
/*     */               
/* 483 */               statbase = StatList.objectUseStats[j];
/* 484 */               statbase1 = StatList.objectUseStats[k];
/*     */             } 
/*     */             
/* 487 */             if (statbase != null || statbase1 != null) {
/*     */               
/* 489 */               if (statbase == null)
/*     */               {
/* 491 */                 return 1;
/*     */               }
/*     */               
/* 494 */               if (statbase1 == null)
/*     */               {
/* 496 */                 return -1;
/*     */               }
/*     */               
/* 499 */               int l = GuiStats.this.field_146546_t.readStat(statbase);
/* 500 */               int i1 = GuiStats.this.field_146546_t.readStat(statbase1);
/*     */               
/* 502 */               if (l != i1)
/*     */               {
/* 504 */                 return (l - i1) * GuiStats.StatsBlock.this.field_148215_p;
/*     */               }
/*     */             } 
/*     */             
/* 508 */             return j - k;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
/* 515 */       super.drawListHeader(p_148129_1_, p_148129_2_, p_148129_3_);
/*     */       
/* 517 */       if (this.field_148218_l == 0) {
/*     */         
/* 519 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1, 18, 18);
/*     */       }
/*     */       else {
/*     */         
/* 523 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 18, 18);
/*     */       } 
/*     */       
/* 526 */       if (this.field_148218_l == 1) {
/*     */         
/* 528 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1, 36, 18);
/*     */       }
/*     */       else {
/*     */         
/* 532 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 36, 18);
/*     */       } 
/*     */       
/* 535 */       if (this.field_148218_l == 2) {
/*     */         
/* 537 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1, 54, 18);
/*     */       }
/*     */       else {
/*     */         
/* 541 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 54, 18);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 547 */       StatCrafting statcrafting = func_148211_c(entryID);
/* 548 */       Item item = statcrafting.func_150959_a();
/* 549 */       GuiStats.this.drawStatsScreen(p_180791_2_ + 40, p_180791_3_, item);
/* 550 */       int i = Item.getIdFromItem(item);
/* 551 */       func_148209_a(StatList.objectCraftStats[i], p_180791_2_ + 115, p_180791_3_, (entryID % 2 == 0));
/* 552 */       func_148209_a(StatList.objectUseStats[i], p_180791_2_ + 165, p_180791_3_, (entryID % 2 == 0));
/* 553 */       func_148209_a((StatBase)statcrafting, p_180791_2_ + 215, p_180791_3_, (entryID % 2 == 0));
/*     */     }
/*     */ 
/*     */     
/*     */     protected String func_148210_b(int p_148210_1_) {
/* 558 */       return (p_148210_1_ == 0) ? "stat.crafted" : ((p_148210_1_ == 1) ? "stat.used" : "stat.mined");
/*     */     }
/*     */   }
/*     */   
/*     */   class StatsGeneral
/*     */     extends GuiSlot
/*     */   {
/*     */     public StatsGeneral(Minecraft mcIn) {
/* 566 */       super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 10);
/* 567 */       setShowSelectionBox(false);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getSize() {
/* 572 */       return StatList.generalStats.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
/*     */ 
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 581 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getContentHeight() {
/* 586 */       return getSize() * 10;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawBackground() {
/* 591 */       GuiStats.this.drawDefaultBackground();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 596 */       StatBase statbase = StatList.generalStats.get(entryID);
/* 597 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, statbase.getStatName().getUnformattedText(), p_180791_2_ + 2, p_180791_3_ + 1, (entryID % 2 == 0) ? 16777215 : 9474192);
/* 598 */       String s = statbase.format(GuiStats.this.field_146546_t.readStat(statbase));
/* 599 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, p_180791_2_ + 2 + 213 - GuiStats.this.fontRendererObj.getStringWidth(s), p_180791_3_ + 1, (entryID % 2 == 0) ? 16777215 : 9474192);
/*     */     }
/*     */   }
/*     */   
/*     */   class StatsItem
/*     */     extends Stats
/*     */   {
/*     */     public StatsItem(Minecraft mcIn) {
/* 607 */       super(mcIn);
/* 608 */       this.statsHolder = Lists.newArrayList();
/*     */       
/* 610 */       for (StatCrafting statcrafting : StatList.itemStats) {
/*     */         
/* 612 */         boolean flag = false;
/* 613 */         int i = Item.getIdFromItem(statcrafting.func_150959_a());
/*     */         
/* 615 */         if (GuiStats.this.field_146546_t.readStat((StatBase)statcrafting) > 0) {
/*     */           
/* 617 */           flag = true;
/*     */         }
/* 619 */         else if (StatList.objectBreakStats[i] != null && GuiStats.this.field_146546_t.readStat(StatList.objectBreakStats[i]) > 0) {
/*     */           
/* 621 */           flag = true;
/*     */         }
/* 623 */         else if (StatList.objectCraftStats[i] != null && GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[i]) > 0) {
/*     */           
/* 625 */           flag = true;
/*     */         } 
/*     */         
/* 628 */         if (flag)
/*     */         {
/* 630 */           this.statsHolder.add(statcrafting);
/*     */         }
/*     */       } 
/*     */       
/* 634 */       this.statSorter = new Comparator<StatCrafting>()
/*     */         {
/*     */           public int compare(StatCrafting p_compare_1_, StatCrafting p_compare_2_)
/*     */           {
/* 638 */             int j = Item.getIdFromItem(p_compare_1_.func_150959_a());
/* 639 */             int k = Item.getIdFromItem(p_compare_2_.func_150959_a());
/* 640 */             StatBase statbase = null;
/* 641 */             StatBase statbase1 = null;
/*     */             
/* 643 */             if (GuiStats.StatsItem.this.field_148217_o == 0) {
/*     */               
/* 645 */               statbase = StatList.objectBreakStats[j];
/* 646 */               statbase1 = StatList.objectBreakStats[k];
/*     */             }
/* 648 */             else if (GuiStats.StatsItem.this.field_148217_o == 1) {
/*     */               
/* 650 */               statbase = StatList.objectCraftStats[j];
/* 651 */               statbase1 = StatList.objectCraftStats[k];
/*     */             }
/* 653 */             else if (GuiStats.StatsItem.this.field_148217_o == 2) {
/*     */               
/* 655 */               statbase = StatList.objectUseStats[j];
/* 656 */               statbase1 = StatList.objectUseStats[k];
/*     */             } 
/*     */             
/* 659 */             if (statbase != null || statbase1 != null) {
/*     */               
/* 661 */               if (statbase == null)
/*     */               {
/* 663 */                 return 1;
/*     */               }
/*     */               
/* 666 */               if (statbase1 == null)
/*     */               {
/* 668 */                 return -1;
/*     */               }
/*     */               
/* 671 */               int l = GuiStats.this.field_146546_t.readStat(statbase);
/* 672 */               int i1 = GuiStats.this.field_146546_t.readStat(statbase1);
/*     */               
/* 674 */               if (l != i1)
/*     */               {
/* 676 */                 return (l - i1) * GuiStats.StatsItem.this.field_148215_p;
/*     */               }
/*     */             } 
/*     */             
/* 680 */             return j - k;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
/* 687 */       super.drawListHeader(p_148129_1_, p_148129_2_, p_148129_3_);
/*     */       
/* 689 */       if (this.field_148218_l == 0) {
/*     */         
/* 691 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1, 72, 18);
/*     */       }
/*     */       else {
/*     */         
/* 695 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 72, 18);
/*     */       } 
/*     */       
/* 698 */       if (this.field_148218_l == 1) {
/*     */         
/* 700 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1, 18, 18);
/*     */       }
/*     */       else {
/*     */         
/* 704 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 18, 18);
/*     */       } 
/*     */       
/* 707 */       if (this.field_148218_l == 2) {
/*     */         
/* 709 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1, 36, 18);
/*     */       }
/*     */       else {
/*     */         
/* 713 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 36, 18);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 719 */       StatCrafting statcrafting = func_148211_c(entryID);
/* 720 */       Item item = statcrafting.func_150959_a();
/* 721 */       GuiStats.this.drawStatsScreen(p_180791_2_ + 40, p_180791_3_, item);
/* 722 */       int i = Item.getIdFromItem(item);
/* 723 */       func_148209_a(StatList.objectBreakStats[i], p_180791_2_ + 115, p_180791_3_, (entryID % 2 == 0));
/* 724 */       func_148209_a(StatList.objectCraftStats[i], p_180791_2_ + 165, p_180791_3_, (entryID % 2 == 0));
/* 725 */       func_148209_a((StatBase)statcrafting, p_180791_2_ + 215, p_180791_3_, (entryID % 2 == 0));
/*     */     }
/*     */ 
/*     */     
/*     */     protected String func_148210_b(int p_148210_1_) {
/* 730 */       return (p_148210_1_ == 1) ? "stat.crafted" : ((p_148210_1_ == 2) ? "stat.used" : "stat.depleted");
/*     */     }
/*     */   }
/*     */   
/*     */   class StatsMobsList
/*     */     extends GuiSlot {
/* 736 */     private final List<EntityList.EntityEggInfo> field_148222_l = Lists.newArrayList();
/*     */ 
/*     */     
/*     */     public StatsMobsList(Minecraft mcIn) {
/* 740 */       super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, GuiStats.this.fontRendererObj.FONT_HEIGHT * 4);
/* 741 */       setShowSelectionBox(false);
/*     */       
/* 743 */       for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.entityEggs.values()) {
/*     */         
/* 745 */         if (GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151512_d) > 0 || GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151513_e) > 0)
/*     */         {
/* 747 */           this.field_148222_l.add(entitylist$entityegginfo);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getSize() {
/* 754 */       return this.field_148222_l.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
/*     */ 
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 763 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getContentHeight() {
/* 768 */       return getSize() * GuiStats.this.fontRendererObj.FONT_HEIGHT * 4;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawBackground() {
/* 773 */       GuiStats.this.drawDefaultBackground();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 778 */       EntityList.EntityEggInfo entitylist$entityegginfo = this.field_148222_l.get(entryID);
/* 779 */       String s = I18n.format("entity." + EntityList.getStringFromID(entitylist$entityegginfo.spawnedID) + ".name", new Object[0]);
/* 780 */       int i = GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151512_d);
/* 781 */       int j = GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151513_e);
/* 782 */       String s1 = I18n.format("stat.entityKills", new Object[] { Integer.valueOf(i), s });
/* 783 */       String s2 = I18n.format("stat.entityKilledBy", new Object[] { s, Integer.valueOf(j) });
/*     */       
/* 785 */       if (i == 0)
/*     */       {
/* 787 */         s1 = I18n.format("stat.entityKills.none", new Object[] { s });
/*     */       }
/*     */       
/* 790 */       if (j == 0)
/*     */       {
/* 792 */         s2 = I18n.format("stat.entityKilledBy.none", new Object[] { s });
/*     */       }
/*     */       
/* 795 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, p_180791_2_ + 2 - 10, p_180791_3_ + 1, 16777215);
/* 796 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, s1, p_180791_2_ + 2, p_180791_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT, (i == 0) ? 6316128 : 9474192);
/* 797 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, s2, p_180791_2_ + 2, p_180791_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT * 2, (j == 0) ? 6316128 : 9474192);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\achievement\GuiStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */