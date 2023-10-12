/*     */ package net.minecraft.client.gui;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerRepair;
/*     */ import net.minecraft.inventory.ICrafting;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiRepair extends GuiContainer implements ICrafting {
/*  25 */   private static final ResourceLocation anvilResource = new ResourceLocation("textures/gui/container/anvil.png");
/*     */   
/*     */   private ContainerRepair anvil;
/*     */   private GuiTextField nameField;
/*     */   private InventoryPlayer playerInventory;
/*     */   
/*     */   public GuiRepair(InventoryPlayer inventoryIn, World worldIn) {
/*  32 */     super((Container)new ContainerRepair(inventoryIn, worldIn, (EntityPlayer)(Minecraft.getMinecraft()).thePlayer));
/*  33 */     this.playerInventory = inventoryIn;
/*  34 */     this.anvil = (ContainerRepair)this.inventorySlots;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  39 */     super.initGui();
/*  40 */     Keyboard.enableRepeatEvents(true);
/*  41 */     int i = (this.width - this.xSize) / 2;
/*  42 */     int j = (this.height - this.ySize) / 2;
/*  43 */     this.nameField = new GuiTextField(0, this.fontRendererObj, i + 62, j + 24, 103, 12);
/*  44 */     this.nameField.setTextColor(-1);
/*  45 */     this.nameField.setDisabledTextColour(-1);
/*  46 */     this.nameField.setEnableBackgroundDrawing(false);
/*  47 */     this.nameField.setMaxStringLength(30);
/*  48 */     this.inventorySlots.removeCraftingFromCrafters(this);
/*  49 */     this.inventorySlots.onCraftGuiOpened(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/*  54 */     super.onGuiClosed();
/*  55 */     Keyboard.enableRepeatEvents(false);
/*  56 */     this.inventorySlots.removeCraftingFromCrafters(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/*  61 */     GlStateManager.disableLighting();
/*  62 */     GlStateManager.disableBlend();
/*  63 */     this.fontRendererObj.drawString(I18n.format("container.repair", new Object[0]), 60.0D, 6.0D, 4210752);
/*     */     
/*  65 */     if (this.anvil.maximumCost > 0) {
/*     */       
/*  67 */       int i = 8453920;
/*  68 */       boolean flag = true;
/*  69 */       String s = I18n.format("container.repair.cost", new Object[] { Integer.valueOf(this.anvil.maximumCost) });
/*     */       
/*  71 */       if (this.anvil.maximumCost >= 40 && !this.mc.thePlayer.capabilities.isCreativeMode) {
/*     */         
/*  73 */         s = I18n.format("container.repair.expensive", new Object[0]);
/*  74 */         i = 16736352;
/*     */       }
/*  76 */       else if (!this.anvil.getSlot(2).getHasStack()) {
/*     */         
/*  78 */         flag = false;
/*     */       }
/*  80 */       else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player)) {
/*     */         
/*  82 */         i = 16736352;
/*     */       } 
/*     */       
/*  85 */       if (flag) {
/*     */         
/*  87 */         int j = 0xFF000000 | (i & 0xFCFCFC) >> 2 | i & 0xFF000000;
/*  88 */         int k = this.xSize - 8 - this.fontRendererObj.getStringWidth(s);
/*  89 */         int l = 67;
/*     */         
/*  91 */         if (this.fontRendererObj.getUnicodeFlag()) {
/*     */           
/*  93 */           drawRect((k - 3), (l - 2), (this.xSize - 7), (l + 10), -16777216);
/*  94 */           drawRect((k - 2), (l - 1), (this.xSize - 8), (l + 9), -12895429);
/*     */         }
/*     */         else {
/*     */           
/*  98 */           this.fontRendererObj.drawString(s, k, (l + 1), j);
/*  99 */           this.fontRendererObj.drawString(s, (k + 1), l, j);
/* 100 */           this.fontRendererObj.drawString(s, (k + 1), (l + 1), j);
/*     */         } 
/*     */         
/* 103 */         this.fontRendererObj.drawString(s, k, l, i);
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     GlStateManager.enableLighting();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 112 */     if (this.nameField.textboxKeyTyped(typedChar, keyCode)) {
/*     */       
/* 114 */       renameItem();
/*     */     }
/*     */     else {
/*     */       
/* 118 */       super.keyTyped(typedChar, keyCode);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void renameItem() {
/* 124 */     String s = this.nameField.getText();
/* 125 */     Slot slot = this.anvil.getSlot(0);
/*     */     
/* 127 */     if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName()))
/*     */     {
/* 129 */       s = "";
/*     */     }
/*     */     
/* 132 */     this.anvil.updateItemName(s);
/* 133 */     this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C17PacketCustomPayload("MC|ItemName", (new PacketBuffer(Unpooled.buffer())).writeString(s)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 138 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 139 */     this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 144 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 145 */     GlStateManager.disableLighting();
/* 146 */     GlStateManager.disableBlend();
/* 147 */     this.nameField.drawTextBox();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 152 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 153 */     this.mc.getTextureManager().bindTexture(anvilResource);
/* 154 */     int i = (this.width - this.xSize) / 2;
/* 155 */     int j = (this.height - this.ySize) / 2;
/* 156 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/* 157 */     drawTexturedModalRect(i + 59, j + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
/*     */     
/* 159 */     if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack())
/*     */     {
/* 161 */       drawTexturedModalRect(i + 99, j + 45, this.xSize, 0, 28, 21);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
/* 167 */     sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
/* 172 */     if (slotInd == 0) {
/*     */       
/* 174 */       this.nameField.setText((stack == null) ? "" : stack.getDisplayName());
/* 175 */       this.nameField.setEnabled((stack != null));
/*     */       
/* 177 */       if (stack != null)
/*     */       {
/* 179 */         renameItem();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {}
/*     */   
/*     */   public void sendAllWindowProperties(Container p_175173_1_, IInventory p_175173_2_) {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiRepair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */