/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ public class GuiListButton
/*    */   extends GuiButton
/*    */ {
/*    */   private boolean field_175216_o;
/*    */   private String localizationStr;
/*    */   private final GuiPageButtonList.GuiResponder guiResponder;
/*    */   
/*    */   public GuiListButton(GuiPageButtonList.GuiResponder responder, int p_i45539_2_, int p_i45539_3_, int p_i45539_4_, String p_i45539_5_, boolean p_i45539_6_) {
/* 14 */     super(p_i45539_2_, p_i45539_3_, p_i45539_4_, 150, 20, "");
/* 15 */     this.localizationStr = p_i45539_5_;
/* 16 */     this.field_175216_o = p_i45539_6_;
/* 17 */     this.displayString = buildDisplayString();
/* 18 */     this.guiResponder = responder;
/*    */   }
/*    */ 
/*    */   
/*    */   private String buildDisplayString() {
/* 23 */     return I18n.format(this.localizationStr, new Object[0]) + ": " + (this.field_175216_o ? I18n.format("gui.yes", new Object[0]) : I18n.format("gui.no", new Object[0]));
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_175212_b(boolean p_175212_1_) {
/* 28 */     this.field_175216_o = p_175212_1_;
/* 29 */     this.displayString = buildDisplayString();
/* 30 */     this.guiResponder.func_175321_a(this.id, p_175212_1_);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 35 */     if (super.mousePressed(mc, mouseX, mouseY)) {
/*    */       
/* 37 */       this.field_175216_o = !this.field_175216_o;
/* 38 */       this.displayString = buildDisplayString();
/* 39 */       this.guiResponder.func_175321_a(this.id, this.field_175216_o);
/* 40 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 44 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiListButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */