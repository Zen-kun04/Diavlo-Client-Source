/*     */ package net.minecraft.client.gui;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerMerchant;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.village.MerchantRecipe;
/*     */ import net.minecraft.village.MerchantRecipeList;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GuiMerchant extends GuiContainer {
/*  26 */   private static final Logger logger = LogManager.getLogger();
/*  27 */   private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
/*     */   
/*     */   private IMerchant merchant;
/*     */   private MerchantButton nextButton;
/*     */   private MerchantButton previousButton;
/*     */   private int selectedMerchantRecipe;
/*     */   private IChatComponent chatComponent;
/*     */   
/*     */   public GuiMerchant(InventoryPlayer p_i45500_1_, IMerchant p_i45500_2_, World worldIn) {
/*  36 */     super((Container)new ContainerMerchant(p_i45500_1_, p_i45500_2_, worldIn));
/*  37 */     this.merchant = p_i45500_2_;
/*  38 */     this.chatComponent = p_i45500_2_.getDisplayName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  43 */     super.initGui();
/*  44 */     int i = (this.width - this.xSize) / 2;
/*  45 */     int j = (this.height - this.ySize) / 2;
/*  46 */     this.buttonList.add(this.nextButton = new MerchantButton(1, i + 120 + 27, j + 24 - 1, true));
/*  47 */     this.buttonList.add(this.previousButton = new MerchantButton(2, i + 36 - 19, j + 24 - 1, false));
/*  48 */     this.nextButton.enabled = false;
/*  49 */     this.previousButton.enabled = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/*  54 */     String s = this.chatComponent.getUnformattedText();
/*  55 */     this.fontRendererObj.drawString(s, (this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2), 6.0D, 4210752);
/*  56 */     this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8.0D, (this.ySize - 96 + 2), 4210752);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  61 */     super.updateScreen();
/*  62 */     MerchantRecipeList merchantrecipelist = this.merchant.getRecipes((EntityPlayer)this.mc.thePlayer);
/*     */     
/*  64 */     if (merchantrecipelist != null) {
/*     */       
/*  66 */       this.nextButton.enabled = (this.selectedMerchantRecipe < merchantrecipelist.size() - 1);
/*  67 */       this.previousButton.enabled = (this.selectedMerchantRecipe > 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  73 */     boolean flag = false;
/*     */     
/*  75 */     if (button == this.nextButton) {
/*     */       
/*  77 */       this.selectedMerchantRecipe++;
/*  78 */       MerchantRecipeList merchantrecipelist = this.merchant.getRecipes((EntityPlayer)this.mc.thePlayer);
/*     */       
/*  80 */       if (merchantrecipelist != null && this.selectedMerchantRecipe >= merchantrecipelist.size())
/*     */       {
/*  82 */         this.selectedMerchantRecipe = merchantrecipelist.size() - 1;
/*     */       }
/*     */       
/*  85 */       flag = true;
/*     */     }
/*  87 */     else if (button == this.previousButton) {
/*     */       
/*  89 */       this.selectedMerchantRecipe--;
/*     */       
/*  91 */       if (this.selectedMerchantRecipe < 0)
/*     */       {
/*  93 */         this.selectedMerchantRecipe = 0;
/*     */       }
/*     */       
/*  96 */       flag = true;
/*     */     } 
/*     */     
/*  99 */     if (flag) {
/*     */       
/* 101 */       ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
/* 102 */       PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
/* 103 */       packetbuffer.writeInt(this.selectedMerchantRecipe);
/* 104 */       this.mc.getNetHandler().addToSendQueue((Packet)new C17PacketCustomPayload("MC|TrSel", packetbuffer));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 110 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 111 */     this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
/* 112 */     int i = (this.width - this.xSize) / 2;
/* 113 */     int j = (this.height - this.ySize) / 2;
/* 114 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/* 115 */     MerchantRecipeList merchantrecipelist = this.merchant.getRecipes((EntityPlayer)this.mc.thePlayer);
/*     */     
/* 117 */     if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
/*     */       
/* 119 */       int k = this.selectedMerchantRecipe;
/*     */       
/* 121 */       if (k < 0 || k >= merchantrecipelist.size()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 126 */       MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);
/*     */       
/* 128 */       if (merchantrecipe.isRecipeDisabled()) {
/*     */         
/* 130 */         this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
/* 131 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 132 */         GlStateManager.disableLighting();
/* 133 */         drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
/* 134 */         drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 141 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 142 */     MerchantRecipeList merchantrecipelist = this.merchant.getRecipes((EntityPlayer)this.mc.thePlayer);
/*     */     
/* 144 */     if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
/*     */       
/* 146 */       int i = (this.width - this.xSize) / 2;
/* 147 */       int j = (this.height - this.ySize) / 2;
/* 148 */       int k = this.selectedMerchantRecipe;
/* 149 */       MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);
/* 150 */       ItemStack itemstack = merchantrecipe.getItemToBuy();
/* 151 */       ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
/* 152 */       ItemStack itemstack2 = merchantrecipe.getItemToSell();
/* 153 */       GlStateManager.pushMatrix();
/* 154 */       RenderHelper.enableGUIStandardItemLighting();
/* 155 */       GlStateManager.disableLighting();
/* 156 */       GlStateManager.enableRescaleNormal();
/* 157 */       GlStateManager.enableColorMaterial();
/* 158 */       GlStateManager.enableLighting();
/* 159 */       this.itemRender.zLevel = 100.0F;
/* 160 */       this.itemRender.renderItemAndEffectIntoGUI(itemstack, i + 36, j + 24);
/* 161 */       this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, i + 36, j + 24);
/*     */       
/* 163 */       if (itemstack1 != null) {
/*     */         
/* 165 */         this.itemRender.renderItemAndEffectIntoGUI(itemstack1, i + 62, j + 24);
/* 166 */         this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack1, i + 62, j + 24);
/*     */       } 
/*     */       
/* 169 */       this.itemRender.renderItemAndEffectIntoGUI(itemstack2, i + 120, j + 24);
/* 170 */       this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack2, i + 120, j + 24);
/* 171 */       this.itemRender.zLevel = 0.0F;
/* 172 */       GlStateManager.disableLighting();
/*     */       
/* 174 */       if (isPointInRegion(36, 24, 16, 16, mouseX, mouseY) && itemstack != null) {
/*     */         
/* 176 */         renderToolTip(itemstack, mouseX, mouseY);
/*     */       }
/* 178 */       else if (itemstack1 != null && isPointInRegion(62, 24, 16, 16, mouseX, mouseY) && itemstack1 != null) {
/*     */         
/* 180 */         renderToolTip(itemstack1, mouseX, mouseY);
/*     */       }
/* 182 */       else if (itemstack2 != null && isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && itemstack2 != null) {
/*     */         
/* 184 */         renderToolTip(itemstack2, mouseX, mouseY);
/*     */       }
/* 186 */       else if (merchantrecipe.isRecipeDisabled() && (isPointInRegion(83, 21, 28, 21, mouseX, mouseY) || isPointInRegion(83, 51, 28, 21, mouseX, mouseY))) {
/*     */         
/* 188 */         drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), mouseX, mouseY);
/*     */       } 
/*     */       
/* 191 */       GlStateManager.popMatrix();
/* 192 */       GlStateManager.enableLighting();
/* 193 */       GlStateManager.enableDepth();
/* 194 */       RenderHelper.enableStandardItemLighting();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IMerchant getMerchant() {
/* 200 */     return this.merchant;
/*     */   }
/*     */   
/*     */   static class MerchantButton
/*     */     extends GuiButton
/*     */   {
/*     */     private final boolean field_146157_o;
/*     */     
/*     */     public MerchantButton(int buttonID, int x, int y, boolean p_i1095_4_) {
/* 209 */       super(buttonID, x, y, 12, 19, "");
/* 210 */       this.field_146157_o = p_i1095_4_;
/*     */     }
/*     */ 
/*     */     
/*     */     public void drawButton(Minecraft mc, int mouseX, int mouseY) {
/* 215 */       if (this.visible) {
/*     */         
/* 217 */         mc.getTextureManager().bindTexture(GuiMerchant.MERCHANT_GUI_TEXTURE);
/* 218 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 219 */         boolean flag = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
/* 220 */         int i = 0;
/* 221 */         int j = 176;
/*     */         
/* 223 */         if (!this.enabled) {
/*     */           
/* 225 */           j += this.width * 2;
/*     */         }
/* 227 */         else if (flag) {
/*     */           
/* 229 */           j += this.width;
/*     */         } 
/*     */         
/* 232 */         if (!this.field_146157_o)
/*     */         {
/* 234 */           i += this.height;
/*     */         }
/*     */         
/* 237 */         drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */