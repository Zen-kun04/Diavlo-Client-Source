/*    */ package net.minecraft.client.gui.inventory;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerFurnace;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.tileentity.TileEntityFurnace;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiFurnace extends GuiContainer {
/* 12 */   private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
/*    */   
/*    */   private final InventoryPlayer playerInventory;
/*    */   private IInventory tileFurnace;
/*    */   
/*    */   public GuiFurnace(InventoryPlayer playerInv, IInventory furnaceInv) {
/* 18 */     super((Container)new ContainerFurnace(playerInv, furnaceInv));
/* 19 */     this.playerInventory = playerInv;
/* 20 */     this.tileFurnace = furnaceInv;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/* 25 */     String s = this.tileFurnace.getDisplayName().getUnformattedText();
/* 26 */     this.fontRendererObj.drawString(s, (this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2), 6.0D, 4210752);
/* 27 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, (this.ySize - 96 + 2), 4210752);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 32 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 33 */     this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
/* 34 */     int i = (this.width - this.xSize) / 2;
/* 35 */     int j = (this.height - this.ySize) / 2;
/* 36 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*    */     
/* 38 */     if (TileEntityFurnace.isBurning(this.tileFurnace)) {
/*    */       
/* 40 */       int k = getBurnLeftScaled(13);
/* 41 */       drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
/*    */     } 
/*    */     
/* 44 */     int l = getCookProgressScaled(24);
/* 45 */     drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
/*    */   }
/*    */ 
/*    */   
/*    */   private int getCookProgressScaled(int pixels) {
/* 50 */     int i = this.tileFurnace.getField(2);
/* 51 */     int j = this.tileFurnace.getField(3);
/* 52 */     return (j != 0 && i != 0) ? (i * pixels / j) : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   private int getBurnLeftScaled(int pixels) {
/* 57 */     int i = this.tileFurnace.getField(1);
/*    */     
/* 59 */     if (i == 0)
/*    */     {
/* 61 */       i = 200;
/*    */     }
/*    */     
/* 64 */     return this.tileFurnace.getField(0) * pixels / i;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\inventory\GuiFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */