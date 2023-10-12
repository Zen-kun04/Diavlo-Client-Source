/*    */ package net.minecraft.client.gui.inventory;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerBrewingStand;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiBrewingStand extends GuiContainer {
/* 11 */   private static final ResourceLocation brewingStandGuiTextures = new ResourceLocation("textures/gui/container/brewing_stand.png");
/*    */   
/*    */   private final InventoryPlayer playerInventory;
/*    */   private IInventory tileBrewingStand;
/*    */   
/*    */   public GuiBrewingStand(InventoryPlayer playerInv, IInventory p_i45506_2_) {
/* 17 */     super((Container)new ContainerBrewingStand(playerInv, p_i45506_2_));
/* 18 */     this.playerInventory = playerInv;
/* 19 */     this.tileBrewingStand = p_i45506_2_;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/* 24 */     String s = this.tileBrewingStand.getDisplayName().getUnformattedText();
/* 25 */     this.fontRendererObj.drawString(s, (this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2), 6.0D, 4210752);
/* 26 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, (this.ySize - 96 + 2), 4210752);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 31 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 32 */     this.mc.getTextureManager().bindTexture(brewingStandGuiTextures);
/* 33 */     int i = (this.width - this.xSize) / 2;
/* 34 */     int j = (this.height - this.ySize) / 2;
/* 35 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/* 36 */     int k = this.tileBrewingStand.getField(0);
/*    */     
/* 38 */     if (k > 0) {
/*    */       
/* 40 */       int l = (int)(28.0F * (1.0F - k / 400.0F));
/*    */       
/* 42 */       if (l > 0)
/*    */       {
/* 44 */         drawTexturedModalRect(i + 97, j + 16, 176, 0, 9, l);
/*    */       }
/*    */       
/* 47 */       int i1 = k / 2 % 7;
/*    */       
/* 49 */       switch (i1) {
/*    */         
/*    */         case 0:
/* 52 */           l = 29;
/*    */           break;
/*    */         
/*    */         case 1:
/* 56 */           l = 24;
/*    */           break;
/*    */         
/*    */         case 2:
/* 60 */           l = 20;
/*    */           break;
/*    */         
/*    */         case 3:
/* 64 */           l = 16;
/*    */           break;
/*    */         
/*    */         case 4:
/* 68 */           l = 11;
/*    */           break;
/*    */         
/*    */         case 5:
/* 72 */           l = 6;
/*    */           break;
/*    */         
/*    */         case 6:
/* 76 */           l = 0;
/*    */           break;
/*    */       } 
/* 79 */       if (l > 0)
/*    */       {
/* 81 */         drawTexturedModalRect(i + 65, j + 14 + 29 - l, 185, 29 - l, 12, l);
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\inventory\GuiBrewingStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */