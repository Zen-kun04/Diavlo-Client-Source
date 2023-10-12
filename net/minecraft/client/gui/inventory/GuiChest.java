/*    */ package net.minecraft.client.gui.inventory;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerChest;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiChest extends GuiContainer {
/* 11 */   private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
/*    */   
/*    */   private IInventory upperChestInventory;
/*    */   private IInventory lowerChestInventory;
/*    */   private int inventoryRows;
/*    */   
/*    */   public GuiChest(IInventory upperInv, IInventory lowerInv) {
/* 18 */     super((Container)new ContainerChest(upperInv, lowerInv, (EntityPlayer)(Minecraft.getMinecraft()).thePlayer));
/* 19 */     this.upperChestInventory = upperInv;
/* 20 */     this.lowerChestInventory = lowerInv;
/* 21 */     this.allowUserInput = false;
/* 22 */     int i = 222;
/* 23 */     int j = i - 108;
/* 24 */     this.inventoryRows = lowerInv.getSizeInventory() / 9;
/* 25 */     this.ySize = j + this.inventoryRows * 18;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/* 30 */     this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8.0D, 6.0D, 4210752);
/* 31 */     this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8.0D, (this.ySize - 96 + 2), 4210752);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 36 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 37 */     this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
/* 38 */     int i = (this.width - this.xSize) / 2;
/* 39 */     int j = (this.height - this.ySize) / 2;
/* 40 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
/* 41 */     drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\inventory\GuiChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */