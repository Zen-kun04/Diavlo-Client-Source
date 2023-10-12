/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ public class GuiLabel
/*    */   extends Gui {
/* 11 */   protected int field_146167_a = 200;
/* 12 */   protected int field_146161_f = 20;
/*    */   
/*    */   public int field_146162_g;
/*    */   public int field_146174_h;
/*    */   private List<String> field_146173_k;
/*    */   public int field_175204_i;
/*    */   private boolean centered;
/*    */   public boolean visible = true;
/*    */   private boolean labelBgEnabled;
/*    */   private int field_146168_n;
/*    */   private int field_146169_o;
/*    */   private int field_146166_p;
/*    */   private int field_146165_q;
/*    */   private FontRenderer fontRenderer;
/*    */   private int field_146163_s;
/*    */   
/*    */   public GuiLabel(FontRenderer fontRendererObj, int p_i45540_2_, int p_i45540_3_, int p_i45540_4_, int p_i45540_5_, int p_i45540_6_, int p_i45540_7_) {
/* 29 */     this.fontRenderer = fontRendererObj;
/* 30 */     this.field_175204_i = p_i45540_2_;
/* 31 */     this.field_146162_g = p_i45540_3_;
/* 32 */     this.field_146174_h = p_i45540_4_;
/* 33 */     this.field_146167_a = p_i45540_5_;
/* 34 */     this.field_146161_f = p_i45540_6_;
/* 35 */     this.field_146173_k = Lists.newArrayList();
/* 36 */     this.centered = false;
/* 37 */     this.labelBgEnabled = false;
/* 38 */     this.field_146168_n = p_i45540_7_;
/* 39 */     this.field_146169_o = -1;
/* 40 */     this.field_146166_p = -1;
/* 41 */     this.field_146165_q = -1;
/* 42 */     this.field_146163_s = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_175202_a(String p_175202_1_) {
/* 47 */     this.field_146173_k.add(I18n.format(p_175202_1_, new Object[0]));
/*    */   }
/*    */ 
/*    */   
/*    */   public GuiLabel setCentered() {
/* 52 */     this.centered = true;
/* 53 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawLabel(Minecraft mc, int mouseX, int mouseY) {
/* 58 */     if (this.visible) {
/*    */       
/* 60 */       GlStateManager.enableBlend();
/* 61 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 62 */       drawLabelBackground(mc, mouseX, mouseY);
/* 63 */       int i = this.field_146174_h + this.field_146161_f / 2 + this.field_146163_s / 2;
/* 64 */       int j = i - this.field_146173_k.size() * 10 / 2;
/*    */       
/* 66 */       for (int k = 0; k < this.field_146173_k.size(); k++) {
/*    */         
/* 68 */         if (this.centered) {
/*    */           
/* 70 */           drawCenteredString(this.fontRenderer, this.field_146173_k.get(k), this.field_146162_g + this.field_146167_a / 2, j + k * 10, this.field_146168_n);
/*    */         }
/*    */         else {
/*    */           
/* 74 */           drawString(this.fontRenderer, this.field_146173_k.get(k), this.field_146162_g, j + k * 10, this.field_146168_n);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawLabelBackground(Minecraft mcIn, int p_146160_2_, int p_146160_3_) {
/* 82 */     if (this.labelBgEnabled) {
/*    */       
/* 84 */       int i = this.field_146167_a + this.field_146163_s * 2;
/* 85 */       int j = this.field_146161_f + this.field_146163_s * 2;
/* 86 */       int k = this.field_146162_g - this.field_146163_s;
/* 87 */       int l = this.field_146174_h - this.field_146163_s;
/* 88 */       drawRect(k, l, (k + i), (l + j), this.field_146169_o);
/* 89 */       this; drawHorizontalLine(k, k + i, l, this.field_146166_p);
/* 90 */       this; drawHorizontalLine(k, k + i, l + j, this.field_146165_q);
/* 91 */       this; drawVerticalLine(k, l, l + j, this.field_146166_p);
/* 92 */       this; drawVerticalLine(k + i, l, l + j, this.field_146165_q);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */