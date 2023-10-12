/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ 
/*     */ public class GuiSlider
/*     */   extends GuiButton {
/*   9 */   private float sliderPosition = 1.0F;
/*     */   
/*     */   public boolean isMouseDown;
/*     */   private String name;
/*     */   private final float min;
/*     */   private final float max;
/*     */   private final GuiPageButtonList.GuiResponder responder;
/*     */   private FormatHelper formatHelper;
/*     */   
/*     */   public GuiSlider(GuiPageButtonList.GuiResponder guiResponder, int idIn, int x, int y, String name, float min, float max, float defaultValue, FormatHelper formatter) {
/*  19 */     super(idIn, x, y, 150, 20, "");
/*  20 */     this.name = name;
/*  21 */     this.min = min;
/*  22 */     this.max = max;
/*  23 */     this.sliderPosition = (defaultValue - min) / (max - min);
/*  24 */     this.formatHelper = formatter;
/*  25 */     this.responder = guiResponder;
/*  26 */     this.displayString = getDisplayString();
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_175220_c() {
/*  31 */     return this.min + (this.max - this.min) * this.sliderPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175218_a(float p_175218_1_, boolean p_175218_2_) {
/*  36 */     this.sliderPosition = (p_175218_1_ - this.min) / (this.max - this.min);
/*  37 */     this.displayString = getDisplayString();
/*     */     
/*  39 */     if (p_175218_2_)
/*     */     {
/*  41 */       this.responder.onTick(this.id, func_175220_c());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_175217_d() {
/*  47 */     return this.sliderPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getDisplayString() {
/*  52 */     return (this.formatHelper == null) ? (I18n.format(this.name, new Object[0]) + ": " + func_175220_c()) : this.formatHelper.getText(this.id, I18n.format(this.name, new Object[0]), func_175220_c());
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getHoverState(boolean mouseOver) {
/*  57 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
/*  62 */     if (this.visible) {
/*     */       
/*  64 */       if (this.isMouseDown) {
/*     */         
/*  66 */         this.sliderPosition = (mouseX - this.xPosition + 4) / (this.width - 8);
/*     */         
/*  68 */         if (this.sliderPosition < 0.0F)
/*     */         {
/*  70 */           this.sliderPosition = 0.0F;
/*     */         }
/*     */         
/*  73 */         if (this.sliderPosition > 1.0F)
/*     */         {
/*  75 */           this.sliderPosition = 1.0F;
/*     */         }
/*     */         
/*  78 */         this.displayString = getDisplayString();
/*  79 */         this.responder.onTick(this.id, func_175220_c());
/*     */       } 
/*     */       
/*  82 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  83 */       drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
/*  84 */       drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175219_a(float p_175219_1_) {
/*  90 */     this.sliderPosition = p_175219_1_;
/*  91 */     this.displayString = getDisplayString();
/*  92 */     this.responder.onTick(this.id, func_175220_c());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/*  97 */     if (super.mousePressed(mc, mouseX, mouseY)) {
/*     */       
/*  99 */       this.sliderPosition = (mouseX - this.xPosition + 4) / (this.width - 8);
/*     */       
/* 101 */       if (this.sliderPosition < 0.0F)
/*     */       {
/* 103 */         this.sliderPosition = 0.0F;
/*     */       }
/*     */       
/* 106 */       if (this.sliderPosition > 1.0F)
/*     */       {
/* 108 */         this.sliderPosition = 1.0F;
/*     */       }
/*     */       
/* 111 */       this.displayString = getDisplayString();
/* 112 */       this.responder.onTick(this.id, func_175220_c());
/* 113 */       this.isMouseDown = true;
/* 114 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY) {
/* 124 */     this.isMouseDown = false;
/*     */   }
/*     */   
/*     */   public static interface FormatHelper {
/*     */     String getText(int param1Int, String param1String, float param1Float);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */