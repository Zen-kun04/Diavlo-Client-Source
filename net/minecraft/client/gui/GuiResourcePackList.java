/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.resources.ResourcePackListEntry;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ 
/*    */ public abstract class GuiResourcePackList
/*    */   extends GuiListExtended
/*    */ {
/*    */   protected final Minecraft mc;
/*    */   protected final List<ResourcePackListEntry> field_148204_l;
/*    */   
/*    */   public GuiResourcePackList(Minecraft mcIn, int p_i45055_2_, int p_i45055_3_, List<ResourcePackListEntry> p_i45055_4_) {
/* 16 */     super(mcIn, p_i45055_2_, p_i45055_3_, 32, p_i45055_3_ - 55 + 4, 36);
/* 17 */     this.mc = mcIn;
/* 18 */     this.field_148204_l = p_i45055_4_;
/* 19 */     this.field_148163_i = false;
/* 20 */     setHasListHeader(true, (int)(mcIn.fontRendererObj.FONT_HEIGHT * 1.5F));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
/* 26 */     String s = EnumChatFormatting.UNDERLINE + "" + EnumChatFormatting.BOLD + getListHeader();
/* 27 */     this.mc.fontRendererObj.drawString(s, (p_148129_1_ + this.width / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2), Math.min(this.top + 3, p_148129_2_), 16777215);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ResourcePackListEntry> getList() {
/* 34 */     return this.field_148204_l;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSize() {
/* 39 */     return getList().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourcePackListEntry getListEntry(int index) {
/* 44 */     return getList().get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getListWidth() {
/* 49 */     return this.width;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getScrollBarX() {
/* 54 */     return this.right - 6;
/*    */   }
/*    */   
/*    */   protected abstract String getListHeader();
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiResourcePackList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */