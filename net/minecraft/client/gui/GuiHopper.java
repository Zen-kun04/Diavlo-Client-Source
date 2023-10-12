/*    */ package net.minecraft.client.gui;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerHopper;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiHopper extends GuiContainer {
/* 13 */   private static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
/*    */   
/*    */   private IInventory playerInventory;
/*    */   private IInventory hopperInventory;
/*    */   
/*    */   public GuiHopper(InventoryPlayer playerInv, IInventory hopperInv) {
/* 19 */     super((Container)new ContainerHopper(playerInv, hopperInv, (EntityPlayer)(Minecraft.getMinecraft()).thePlayer));
/* 20 */     this.playerInventory = (IInventory)playerInv;
/* 21 */     this.hopperInventory = hopperInv;
/* 22 */     this.allowUserInput = false;
/* 23 */     this.ySize = 133;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/* 28 */     this.fontRendererObj.drawString(this.hopperInventory.getDisplayName().getUnformattedText(), 8.0D, 6.0D, 4210752);
/* 29 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, (this.ySize - 96 + 2), 4210752);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 34 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 35 */     this.mc.getTextureManager().bindTexture(HOPPER_GUI_TEXTURE);
/* 36 */     int i = (this.width - this.xSize) / 2;
/* 37 */     int j = (this.height - this.ySize) / 2;
/* 38 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */