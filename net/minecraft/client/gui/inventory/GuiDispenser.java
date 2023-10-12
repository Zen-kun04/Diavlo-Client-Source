/*    */ package net.minecraft.client.gui.inventory;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerDispenser;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiDispenser extends GuiContainer {
/* 11 */   private static final ResourceLocation dispenserGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");
/*    */   
/*    */   private final InventoryPlayer playerInventory;
/*    */   public IInventory dispenserInventory;
/*    */   
/*    */   public GuiDispenser(InventoryPlayer playerInv, IInventory dispenserInv) {
/* 17 */     super((Container)new ContainerDispenser((IInventory)playerInv, dispenserInv));
/* 18 */     this.playerInventory = playerInv;
/* 19 */     this.dispenserInventory = dispenserInv;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/* 24 */     String s = this.dispenserInventory.getDisplayName().getUnformattedText();
/* 25 */     this.fontRendererObj.drawString(s, (this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2), 6.0D, 4210752);
/* 26 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, (this.ySize - 96 + 2), 4210752);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 31 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 32 */     this.mc.getTextureManager().bindTexture(dispenserGuiTextures);
/* 33 */     int i = (this.width - this.xSize) / 2;
/* 34 */     int j = (this.height - this.ySize) / 2;
/* 35 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\inventory\GuiDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */