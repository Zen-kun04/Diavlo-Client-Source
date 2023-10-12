/*     */ package net.minecraft.client.gui.inventory;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.achievement.GuiAchievements;
/*     */ import net.minecraft.client.gui.achievement.GuiStats;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.InventoryEffectRenderer;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import rip.diavlo.base.events.render.RenderUtils;
/*     */ 
/*     */ public class GuiInventory extends InventoryEffectRenderer {
/*     */   private float oldMouseX;
/*     */   private float oldMouseY;
/*     */   
/*     */   public GuiInventory(EntityPlayer p_i1094_1_) {
/*  25 */     super(p_i1094_1_.inventoryContainer);
/*  26 */     this.allowUserInput = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  31 */     if (this.mc.playerController.isInCreativeMode())
/*     */     {
/*  33 */       this.mc.displayGuiScreen((GuiScreen)new GuiContainerCreative((EntityPlayer)this.mc.thePlayer));
/*     */     }
/*     */     
/*  36 */     updateActivePotionEffects();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  41 */     this.buttonList.clear();
/*     */     
/*  43 */     if (this.mc.playerController.isInCreativeMode()) {
/*     */       
/*  45 */       this.mc.displayGuiScreen((GuiScreen)new GuiContainerCreative((EntityPlayer)this.mc.thePlayer));
/*     */     }
/*     */     else {
/*     */       
/*  49 */       super.initGui();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/*  55 */     this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 86.0D, 16.0D, 4210752);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  60 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*  61 */     this.oldMouseX = mouseX;
/*  62 */     this.oldMouseY = mouseY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/*  67 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  68 */     this.mc.getTextureManager().bindTexture(inventoryBackground);
/*  69 */     int i = this.guiLeft;
/*  70 */     int j = this.guiTop;
/*  71 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*  72 */     RenderUtils.drawEntityOnScreen(i + 51, j + 75, 30, 0.0F, 0.0F, 
/*  73 */         (EntityLivingBase)(Minecraft.getMinecraft()).thePlayer, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
/*  78 */     GlStateManager.enableColorMaterial();
/*  79 */     GlStateManager.pushMatrix();
/*  80 */     GlStateManager.translate(posX, posY, 50.0F);
/*  81 */     GlStateManager.scale(-scale, scale, scale);
/*  82 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/*  83 */     float f = ent.renderYawOffset;
/*  84 */     float f1 = ent.rotationYaw;
/*  85 */     float f2 = ent.rotationPitch;
/*  86 */     float f3 = ent.prevRotationYawHead;
/*  87 */     float f4 = ent.rotationYawHead;
/*  88 */     GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
/*  89 */     RenderHelper.enableStandardItemLighting();
/*  90 */     GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
/*  91 */     GlStateManager.rotate(-((float)Math.atan((mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
/*  92 */     ent.renderYawOffset = (float)Math.atan((mouseX / 40.0F)) * 20.0F;
/*  93 */     ent.rotationYaw = (float)Math.atan((mouseX / 40.0F)) * 40.0F;
/*  94 */     ent.rotationPitch = -((float)Math.atan((mouseY / 40.0F))) * 20.0F;
/*  95 */     ent.rotationYawHead = ent.rotationYaw;
/*  96 */     ent.prevRotationYawHead = ent.rotationYaw;
/*  97 */     GlStateManager.translate(0.0F, 0.0F, 0.0F);
/*  98 */     RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
/*  99 */     rendermanager.setPlayerViewY(180.0F);
/* 100 */     rendermanager.setRenderShadow(false);
/* 101 */     rendermanager.renderEntityWithPosYaw((Entity)ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
/* 102 */     rendermanager.setRenderShadow(true);
/* 103 */     ent.renderYawOffset = f;
/* 104 */     ent.rotationYaw = f1;
/* 105 */     ent.rotationPitch = f2;
/* 106 */     ent.prevRotationYawHead = f3;
/* 107 */     ent.rotationYawHead = f4;
/* 108 */     GlStateManager.popMatrix();
/* 109 */     RenderHelper.disableStandardItemLighting();
/* 110 */     GlStateManager.disableRescaleNormal();
/* 111 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 112 */     GlStateManager.disableTexture2D();
/* 113 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 118 */     if (button.id == 0)
/*     */     {
/* 120 */       this.mc.displayGuiScreen((GuiScreen)new GuiAchievements((GuiScreen)this, this.mc.thePlayer.getStatFileWriter()));
/*     */     }
/*     */     
/* 123 */     if (button.id == 1)
/*     */     {
/* 125 */       this.mc.displayGuiScreen((GuiScreen)new GuiStats((GuiScreen)this, this.mc.thePlayer.getStatFileWriter()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\inventory\GuiInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */