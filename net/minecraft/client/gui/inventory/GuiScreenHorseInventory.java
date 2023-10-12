/*    */ package net.minecraft.client.gui.inventory;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityHorse;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerHorseInventory;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiScreenHorseInventory extends GuiContainer {
/* 12 */   private static final ResourceLocation horseGuiTextures = new ResourceLocation("textures/gui/container/horse.png");
/*    */   
/*    */   private IInventory playerInventory;
/*    */   private IInventory horseInventory;
/*    */   private EntityHorse horseEntity;
/*    */   private float mousePosx;
/*    */   private float mousePosY;
/*    */   
/*    */   public GuiScreenHorseInventory(IInventory playerInv, IInventory horseInv, EntityHorse horse) {
/* 21 */     super((Container)new ContainerHorseInventory(playerInv, horseInv, horse, (EntityPlayer)(Minecraft.getMinecraft()).thePlayer));
/* 22 */     this.playerInventory = playerInv;
/* 23 */     this.horseInventory = horseInv;
/* 24 */     this.horseEntity = horse;
/* 25 */     this.allowUserInput = false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/* 30 */     this.fontRendererObj.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8.0D, 6.0D, 4210752);
/* 31 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, (this.ySize - 96 + 2), 4210752);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 36 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 37 */     this.mc.getTextureManager().bindTexture(horseGuiTextures);
/* 38 */     int i = (this.width - this.xSize) / 2;
/* 39 */     int j = (this.height - this.ySize) / 2;
/* 40 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*    */     
/* 42 */     if (this.horseEntity.isChested())
/*    */     {
/* 44 */       drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, 90, 54);
/*    */     }
/*    */     
/* 47 */     if (this.horseEntity.canWearArmor())
/*    */     {
/* 49 */       drawTexturedModalRect(i + 7, j + 35, 0, this.ySize + 54, 18, 18);
/*    */     }
/*    */     
/* 52 */     GuiInventory.drawEntityOnScreen(i + 51, j + 60, 17, (i + 51) - this.mousePosx, (j + 75 - 50) - this.mousePosY, (EntityLivingBase)this.horseEntity);
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 57 */     this.mousePosx = mouseX;
/* 58 */     this.mousePosY = mouseY;
/* 59 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\inventory\GuiScreenHorseInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */