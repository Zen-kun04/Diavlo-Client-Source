/*     */ package net.optifine.shaders.gui;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Properties;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiSlot;
/*     */ import net.minecraft.client.gui.GuiYesNo;
/*     */ import net.minecraft.client.gui.GuiYesNoCallback;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.src.Config;
/*     */ import net.optifine.Lang;
/*     */ import net.optifine.shaders.IShaderPack;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import net.optifine.util.ResUtils;
/*     */ 
/*     */ class GuiSlotShaders extends GuiSlot {
/*     */   private ArrayList shaderslist;
/*     */   private int selectedIndex;
/*  21 */   private long lastClickedCached = 0L;
/*     */   
/*     */   final GuiShaders shadersGui;
/*     */   
/*     */   public GuiSlotShaders(GuiShaders par1GuiShaders, int width, int height, int top, int bottom, int slotHeight) {
/*  26 */     super(par1GuiShaders.getMc(), width, height, top, bottom, slotHeight);
/*  27 */     this.shadersGui = par1GuiShaders;
/*  28 */     updateList();
/*  29 */     this.amountScrolled = 0.0F;
/*  30 */     int i = this.selectedIndex * slotHeight;
/*  31 */     int j = (bottom - top) / 2;
/*     */     
/*  33 */     if (i > j)
/*     */     {
/*  35 */       scrollBy(i - j);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getListWidth() {
/*  41 */     return this.width - 20;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateList() {
/*  46 */     this.shaderslist = Shaders.listOfShaders();
/*  47 */     this.selectedIndex = 0;
/*  48 */     int i = 0;
/*     */     
/*  50 */     for (int j = this.shaderslist.size(); i < j; i++) {
/*     */       
/*  52 */       if (((String)this.shaderslist.get(i)).equals(Shaders.currentShaderName)) {
/*     */         
/*  54 */         this.selectedIndex = i;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSize() {
/*  62 */     return this.shaderslist.size();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void elementClicked(int index, boolean doubleClicked, int mouseX, int mouseY) {
/*  67 */     if (index != this.selectedIndex || this.lastClicked != this.lastClickedCached) {
/*     */       
/*  69 */       String s = this.shaderslist.get(index);
/*  70 */       IShaderPack ishaderpack = Shaders.getShaderPack(s);
/*     */       
/*  72 */       if (checkCompatible(ishaderpack, index))
/*     */       {
/*  74 */         selectIndex(index);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void selectIndex(int index) {
/*  81 */     this.selectedIndex = index;
/*  82 */     this.lastClickedCached = this.lastClicked;
/*  83 */     Shaders.setShaderPack(this.shaderslist.get(index));
/*  84 */     Shaders.uninit();
/*  85 */     this.shadersGui.updateButtons();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkCompatible(IShaderPack sp, final int index) {
/*  90 */     if (sp == null)
/*     */     {
/*  92 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  96 */     InputStream inputstream = sp.getResourceAsStream("/shaders/shaders.properties");
/*  97 */     Properties properties = ResUtils.readProperties(inputstream, "Shaders");
/*     */     
/*  99 */     if (properties == null)
/*     */     {
/* 101 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 105 */     String s = "version.1.8.9";
/* 106 */     String s1 = properties.getProperty(s);
/*     */     
/* 108 */     if (s1 == null)
/*     */     {
/* 110 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 114 */     s1 = s1.trim();
/* 115 */     String s2 = "M6_pre2";
/* 116 */     int i = Config.compareRelease(s2, s1);
/*     */     
/* 118 */     if (i >= 0)
/*     */     {
/* 120 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 124 */     String s3 = ("HD_U_" + s1).replace('_', ' ');
/* 125 */     String s4 = I18n.format("of.message.shaders.nv1", new Object[] { s3 });
/* 126 */     String s5 = I18n.format("of.message.shaders.nv2", new Object[0]);
/* 127 */     GuiYesNoCallback guiyesnocallback = new GuiYesNoCallback()
/*     */       {
/*     */         public void confirmClicked(boolean result, int id)
/*     */         {
/* 131 */           if (result)
/*     */           {
/* 133 */             GuiSlotShaders.this.selectIndex(index);
/*     */           }
/*     */           
/* 136 */           GuiSlotShaders.this.mc.displayGuiScreen((GuiScreen)GuiSlotShaders.this.shadersGui);
/*     */         }
/*     */       };
/* 139 */     GuiYesNo guiyesno = new GuiYesNo(guiyesnocallback, s4, s5, 0);
/* 140 */     this.mc.displayGuiScreen((GuiScreen)guiyesno);
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSelected(int index) {
/* 150 */     return (index == this.selectedIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getScrollBarX() {
/* 155 */     return this.width - 6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getContentHeight() {
/* 160 */     return getSize() * 18;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawBackground() {}
/*     */ 
/*     */   
/*     */   protected void drawSlot(int index, int posX, int posY, int contentY, int mouseX, int mouseY) {
/* 169 */     String s = this.shaderslist.get(index);
/*     */     
/* 171 */     if (s.equals("OFF")) {
/*     */       
/* 173 */       s = Lang.get("of.options.shaders.packNone");
/*     */     }
/* 175 */     else if (s.equals("(internal)")) {
/*     */       
/* 177 */       s = Lang.get("of.options.shaders.packDefault");
/*     */     } 
/*     */     
/* 180 */     this.shadersGui.drawCenteredString(s, this.width / 2, posY + 1, 14737632);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSelectedIndex() {
/* 185 */     return this.selectedIndex;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\gui\GuiSlotShaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */