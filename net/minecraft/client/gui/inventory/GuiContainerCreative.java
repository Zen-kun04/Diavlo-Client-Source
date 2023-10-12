/*     */ package net.minecraft.client.gui.inventory;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.achievement.GuiAchievements;
/*     */ import net.minecraft.client.gui.achievement.GuiStats;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.InventoryEffectRenderer;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryBasic;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class GuiContainerCreative extends InventoryEffectRenderer {
/*  37 */   private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
/*  38 */   private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
/*  39 */   private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
/*     */   
/*     */   private float currentScroll;
/*     */   private boolean isScrolling;
/*     */   private boolean wasClicking;
/*     */   private GuiTextField searchField;
/*     */   private List<Slot> field_147063_B;
/*     */   private Slot field_147064_C;
/*     */   private boolean field_147057_D;
/*     */   private CreativeCrafting field_147059_E;
/*     */   
/*     */   public GuiContainerCreative(EntityPlayer p_i1088_1_) {
/*  51 */     super(new ContainerCreative(p_i1088_1_));
/*  52 */     p_i1088_1_.openContainer = this.inventorySlots;
/*  53 */     this.allowUserInput = true;
/*  54 */     this.ySize = 136;
/*  55 */     this.xSize = 195;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  60 */     if (!this.mc.playerController.isInCreativeMode())
/*     */     {
/*  62 */       this.mc.displayGuiScreen((GuiScreen)new GuiInventory((EntityPlayer)this.mc.thePlayer));
/*     */     }
/*     */     
/*  65 */     updateActivePotionEffects();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType) {
/*  70 */     this.field_147057_D = true;
/*  71 */     boolean flag = (clickType == 1);
/*  72 */     clickType = (slotId == -999 && clickType == 0) ? 4 : clickType;
/*     */     
/*  74 */     if (slotIn == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && clickType != 5) {
/*     */       
/*  76 */       InventoryPlayer inventoryplayer1 = this.mc.thePlayer.inventory;
/*     */       
/*  78 */       if (inventoryplayer1.getItemStack() != null) {
/*     */         
/*  80 */         if (clickedButton == 0) {
/*     */           
/*  82 */           this.mc.thePlayer.dropPlayerItemWithRandomChoice(inventoryplayer1.getItemStack(), true);
/*  83 */           this.mc.playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
/*  84 */           inventoryplayer1.setItemStack((ItemStack)null);
/*     */         } 
/*     */         
/*  87 */         if (clickedButton == 1)
/*     */         {
/*  89 */           ItemStack itemstack5 = inventoryplayer1.getItemStack().splitStack(1);
/*  90 */           this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack5, true);
/*  91 */           this.mc.playerController.sendPacketDropItem(itemstack5);
/*     */           
/*  93 */           if ((inventoryplayer1.getItemStack()).stackSize == 0)
/*     */           {
/*  95 */             inventoryplayer1.setItemStack((ItemStack)null);
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 100 */     } else if (slotIn == this.field_147064_C && flag) {
/*     */       
/* 102 */       for (int j = 0; j < this.mc.thePlayer.inventoryContainer.getInventory().size(); j++)
/*     */       {
/* 104 */         this.mc.playerController.sendSlotPacket((ItemStack)null, j);
/*     */       }
/*     */     }
/* 107 */     else if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
/*     */       
/* 109 */       if (slotIn == this.field_147064_C)
/*     */       {
/* 111 */         this.mc.thePlayer.inventory.setItemStack((ItemStack)null);
/*     */       }
/* 113 */       else if (clickType == 4 && slotIn != null && slotIn.getHasStack())
/*     */       {
/* 115 */         ItemStack itemstack = slotIn.decrStackSize((clickedButton == 0) ? 1 : slotIn.getStack().getMaxStackSize());
/* 116 */         this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack, true);
/* 117 */         this.mc.playerController.sendPacketDropItem(itemstack);
/*     */       }
/* 119 */       else if (clickType == 4 && this.mc.thePlayer.inventory.getItemStack() != null)
/*     */       {
/* 121 */         this.mc.thePlayer.dropPlayerItemWithRandomChoice(this.mc.thePlayer.inventory.getItemStack(), true);
/* 122 */         this.mc.playerController.sendPacketDropItem(this.mc.thePlayer.inventory.getItemStack());
/* 123 */         this.mc.thePlayer.inventory.setItemStack((ItemStack)null);
/*     */       }
/*     */       else
/*     */       {
/* 127 */         this.mc.thePlayer.inventoryContainer.slotClick((slotIn == null) ? slotId : ((CreativeSlot)slotIn).slot.slotNumber, clickedButton, clickType, (EntityPlayer)this.mc.thePlayer);
/* 128 */         this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
/*     */       }
/*     */     
/* 131 */     } else if (clickType != 5 && slotIn.inventory == field_147060_v) {
/*     */       
/* 133 */       InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
/* 134 */       ItemStack itemstack1 = inventoryplayer.getItemStack();
/* 135 */       ItemStack itemstack2 = slotIn.getStack();
/*     */       
/* 137 */       if (clickType == 2) {
/*     */         
/* 139 */         if (itemstack2 != null && clickedButton >= 0 && clickedButton < 9) {
/*     */           
/* 141 */           ItemStack itemstack7 = itemstack2.copy();
/* 142 */           itemstack7.stackSize = itemstack7.getMaxStackSize();
/* 143 */           this.mc.thePlayer.inventory.setInventorySlotContents(clickedButton, itemstack7);
/* 144 */           this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 150 */       if (clickType == 3) {
/*     */         
/* 152 */         if (inventoryplayer.getItemStack() == null && slotIn.getHasStack()) {
/*     */           
/* 154 */           ItemStack itemstack6 = slotIn.getStack().copy();
/* 155 */           itemstack6.stackSize = itemstack6.getMaxStackSize();
/* 156 */           inventoryplayer.setItemStack(itemstack6);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 162 */       if (clickType == 4) {
/*     */         
/* 164 */         if (itemstack2 != null) {
/*     */           
/* 166 */           ItemStack itemstack3 = itemstack2.copy();
/* 167 */           itemstack3.stackSize = (clickedButton == 0) ? 1 : itemstack3.getMaxStackSize();
/* 168 */           this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack3, true);
/* 169 */           this.mc.playerController.sendPacketDropItem(itemstack3);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 175 */       if (itemstack1 != null && itemstack2 != null && itemstack1.isItemEqual(itemstack2)) {
/*     */         
/* 177 */         if (clickedButton == 0) {
/*     */           
/* 179 */           if (flag)
/*     */           {
/* 181 */             itemstack1.stackSize = itemstack1.getMaxStackSize();
/*     */           }
/* 183 */           else if (itemstack1.stackSize < itemstack1.getMaxStackSize())
/*     */           {
/* 185 */             itemstack1.stackSize++;
/*     */           }
/*     */         
/* 188 */         } else if (itemstack1.stackSize <= 1) {
/*     */           
/* 190 */           inventoryplayer.setItemStack((ItemStack)null);
/*     */         }
/*     */         else {
/*     */           
/* 194 */           itemstack1.stackSize--;
/*     */         }
/*     */       
/* 197 */       } else if (itemstack2 != null && itemstack1 == null) {
/*     */         
/* 199 */         inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack2));
/* 200 */         itemstack1 = inventoryplayer.getItemStack();
/*     */         
/* 202 */         if (flag)
/*     */         {
/* 204 */           itemstack1.stackSize = itemstack1.getMaxStackSize();
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 209 */         inventoryplayer.setItemStack((ItemStack)null);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 214 */       this.inventorySlots.slotClick((slotIn == null) ? slotId : slotIn.slotNumber, clickedButton, clickType, (EntityPlayer)this.mc.thePlayer);
/*     */       
/* 216 */       if (Container.getDragEvent(clickedButton) == 2) {
/*     */         
/* 218 */         for (int i = 0; i < 9; i++)
/*     */         {
/* 220 */           this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + i).getStack(), 36 + i);
/*     */         }
/*     */       }
/* 223 */       else if (slotIn != null) {
/*     */         
/* 225 */         ItemStack itemstack4 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
/* 226 */         this.mc.playerController.sendSlotPacket(itemstack4, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateActivePotionEffects() {
/* 233 */     int i = this.guiLeft;
/* 234 */     super.updateActivePotionEffects();
/*     */     
/* 236 */     if (this.searchField != null && this.guiLeft != i)
/*     */     {
/* 238 */       this.searchField.xPosition = this.guiLeft + 82;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/* 244 */     if (this.mc.playerController.isInCreativeMode()) {
/*     */       
/* 246 */       super.initGui();
/* 247 */       this.buttonList.clear();
/* 248 */       Keyboard.enableRepeatEvents(true);
/* 249 */       this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
/* 250 */       this.searchField.setMaxStringLength(15);
/* 251 */       this.searchField.setEnableBackgroundDrawing(false);
/* 252 */       this.searchField.setVisible(false);
/* 253 */       this.searchField.setTextColor(16777215);
/* 254 */       int i = selectedTabIndex;
/* 255 */       selectedTabIndex = -1;
/* 256 */       setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
/* 257 */       this.field_147059_E = new CreativeCrafting(this.mc);
/* 258 */       this.mc.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
/*     */     }
/*     */     else {
/*     */       
/* 262 */       this.mc.displayGuiScreen((GuiScreen)new GuiInventory((EntityPlayer)this.mc.thePlayer));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 268 */     super.onGuiClosed();
/*     */     
/* 270 */     if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null)
/*     */     {
/* 272 */       this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
/*     */     }
/*     */     
/* 275 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 280 */     if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
/*     */       
/* 282 */       if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
/*     */       {
/* 284 */         setCurrentCreativeTab(CreativeTabs.tabAllSearch);
/*     */       }
/*     */       else
/*     */       {
/* 288 */         super.keyTyped(typedChar, keyCode);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 293 */       if (this.field_147057_D) {
/*     */         
/* 295 */         this.field_147057_D = false;
/* 296 */         this.searchField.setText("");
/*     */       } 
/*     */       
/* 299 */       if (!checkHotbarKeys(keyCode))
/*     */       {
/* 301 */         if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
/*     */           
/* 303 */           updateCreativeSearch();
/*     */         }
/*     */         else {
/*     */           
/* 307 */           super.keyTyped(typedChar, keyCode);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateCreativeSearch() {
/* 315 */     ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
/* 316 */     guicontainercreative$containercreative.itemList.clear();
/*     */     
/* 318 */     for (Item item : Item.itemRegistry) {
/*     */       
/* 320 */       if (item != null && item.getCreativeTab() != null)
/*     */       {
/* 322 */         item.getSubItems(item, (CreativeTabs)null, guicontainercreative$containercreative.itemList);
/*     */       }
/*     */     } 
/*     */     
/* 326 */     for (Enchantment enchantment : Enchantment.enchantmentsBookList) {
/*     */       
/* 328 */       if (enchantment != null && enchantment.type != null)
/*     */       {
/* 330 */         Items.enchanted_book.getAll(enchantment, guicontainercreative$containercreative.itemList);
/*     */       }
/*     */     } 
/*     */     
/* 334 */     Iterator<ItemStack> iterator = guicontainercreative$containercreative.itemList.iterator();
/* 335 */     String s1 = this.searchField.getText().toLowerCase();
/*     */     
/* 337 */     while (iterator.hasNext()) {
/*     */       
/* 339 */       ItemStack itemstack = iterator.next();
/* 340 */       boolean flag = false;
/*     */       
/* 342 */       for (String s : itemstack.getTooltip((EntityPlayer)this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips)) {
/*     */         
/* 344 */         if (EnumChatFormatting.getTextWithoutFormattingCodes(s).toLowerCase().contains(s1)) {
/*     */           
/* 346 */           flag = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 351 */       if (!flag)
/*     */       {
/* 353 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */     
/* 357 */     this.currentScroll = 0.0F;
/* 358 */     guicontainercreative$containercreative.scrollTo(0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/* 363 */     CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
/*     */     
/* 365 */     if (creativetabs.drawInForegroundOfTab()) {
/*     */       
/* 367 */       GlStateManager.disableBlend();
/* 368 */       this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8.0D, 6.0D, 4210752);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 374 */     if (mouseButton == 0) {
/*     */       
/* 376 */       int i = mouseX - this.guiLeft;
/* 377 */       int j = mouseY - this.guiTop;
/*     */       
/* 379 */       for (CreativeTabs creativetabs : CreativeTabs.creativeTabArray) {
/*     */         
/* 381 */         if (func_147049_a(creativetabs, i, j)) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 388 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 393 */     if (state == 0) {
/*     */       
/* 395 */       int i = mouseX - this.guiLeft;
/* 396 */       int j = mouseY - this.guiTop;
/*     */       
/* 398 */       for (CreativeTabs creativetabs : CreativeTabs.creativeTabArray) {
/*     */         
/* 400 */         if (func_147049_a(creativetabs, i, j)) {
/*     */           
/* 402 */           setCurrentCreativeTab(creativetabs);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 408 */     super.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean needsScrollBars() {
/* 413 */     return (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).func_148328_e());
/*     */   }
/*     */ 
/*     */   
/*     */   private void setCurrentCreativeTab(CreativeTabs p_147050_1_) {
/* 418 */     int i = selectedTabIndex;
/* 419 */     selectedTabIndex = p_147050_1_.getTabIndex();
/* 420 */     ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
/* 421 */     this.dragSplittingSlots.clear();
/* 422 */     guicontainercreative$containercreative.itemList.clear();
/* 423 */     p_147050_1_.displayAllReleventItems(guicontainercreative$containercreative.itemList);
/*     */     
/* 425 */     if (p_147050_1_ == CreativeTabs.tabInventory) {
/*     */       
/* 427 */       Container container = this.mc.thePlayer.inventoryContainer;
/*     */       
/* 429 */       if (this.field_147063_B == null)
/*     */       {
/* 431 */         this.field_147063_B = guicontainercreative$containercreative.inventorySlots;
/*     */       }
/*     */       
/* 434 */       guicontainercreative$containercreative.inventorySlots = Lists.newArrayList();
/*     */       
/* 436 */       for (int j = 0; j < container.inventorySlots.size(); j++) {
/*     */         
/* 438 */         Slot slot = new CreativeSlot(container.inventorySlots.get(j), j);
/* 439 */         guicontainercreative$containercreative.inventorySlots.add(slot);
/*     */         
/* 441 */         if (j >= 5 && j < 9) {
/*     */           
/* 443 */           int j1 = j - 5;
/* 444 */           int k1 = j1 / 2;
/* 445 */           int l1 = j1 % 2;
/* 446 */           slot.xDisplayPosition = 9 + k1 * 54;
/* 447 */           slot.yDisplayPosition = 6 + l1 * 27;
/*     */         }
/* 449 */         else if (j >= 0 && j < 5) {
/*     */           
/* 451 */           slot.yDisplayPosition = -2000;
/* 452 */           slot.xDisplayPosition = -2000;
/*     */         }
/* 454 */         else if (j < container.inventorySlots.size()) {
/*     */           
/* 456 */           int k = j - 9;
/* 457 */           int l = k % 9;
/* 458 */           int i1 = k / 9;
/* 459 */           slot.xDisplayPosition = 9 + l * 18;
/*     */           
/* 461 */           if (j >= 36) {
/*     */             
/* 463 */             slot.yDisplayPosition = 112;
/*     */           }
/*     */           else {
/*     */             
/* 467 */             slot.yDisplayPosition = 54 + i1 * 18;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 472 */       this.field_147064_C = new Slot((IInventory)field_147060_v, 0, 173, 112);
/* 473 */       guicontainercreative$containercreative.inventorySlots.add(this.field_147064_C);
/*     */     }
/* 475 */     else if (i == CreativeTabs.tabInventory.getTabIndex()) {
/*     */       
/* 477 */       guicontainercreative$containercreative.inventorySlots = this.field_147063_B;
/* 478 */       this.field_147063_B = null;
/*     */     } 
/*     */     
/* 481 */     if (this.searchField != null)
/*     */     {
/* 483 */       if (p_147050_1_ == CreativeTabs.tabAllSearch) {
/*     */         
/* 485 */         this.searchField.setVisible(true);
/* 486 */         this.searchField.setCanLoseFocus(false);
/* 487 */         this.searchField.setFocused(true);
/* 488 */         this.searchField.setText("");
/* 489 */         updateCreativeSearch();
/*     */       }
/*     */       else {
/*     */         
/* 493 */         this.searchField.setVisible(false);
/* 494 */         this.searchField.setCanLoseFocus(true);
/* 495 */         this.searchField.setFocused(false);
/*     */       } 
/*     */     }
/*     */     
/* 499 */     this.currentScroll = 0.0F;
/* 500 */     guicontainercreative$containercreative.scrollTo(0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/* 505 */     super.handleMouseInput();
/* 506 */     int i = Mouse.getEventDWheel();
/*     */     
/* 508 */     if (i != 0 && needsScrollBars()) {
/*     */       
/* 510 */       int j = ((ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5;
/*     */       
/* 512 */       if (i > 0)
/*     */       {
/* 514 */         i = 1;
/*     */       }
/*     */       
/* 517 */       if (i < 0)
/*     */       {
/* 519 */         i = -1;
/*     */       }
/*     */       
/* 522 */       this.currentScroll = (float)(this.currentScroll - i / j);
/* 523 */       this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
/* 524 */       ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 530 */     boolean flag = Mouse.isButtonDown(0);
/* 531 */     int i = this.guiLeft;
/* 532 */     int j = this.guiTop;
/* 533 */     int k = i + 175;
/* 534 */     int l = j + 18;
/* 535 */     int i1 = k + 14;
/* 536 */     int j1 = l + 112;
/*     */     
/* 538 */     if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i1 && mouseY < j1)
/*     */     {
/* 540 */       this.isScrolling = needsScrollBars();
/*     */     }
/*     */     
/* 543 */     if (!flag)
/*     */     {
/* 545 */       this.isScrolling = false;
/*     */     }
/*     */     
/* 548 */     this.wasClicking = flag;
/*     */     
/* 550 */     if (this.isScrolling) {
/*     */       
/* 552 */       this.currentScroll = ((mouseY - l) - 7.5F) / ((j1 - l) - 15.0F);
/* 553 */       this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
/* 554 */       ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
/*     */     } 
/*     */     
/* 557 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     
/* 559 */     for (CreativeTabs creativetabs : CreativeTabs.creativeTabArray) {
/*     */       
/* 561 */       if (renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 567 */     if (this.field_147064_C != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, mouseX, mouseY))
/*     */     {
/* 569 */       drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
/*     */     }
/*     */     
/* 572 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 573 */     GlStateManager.disableLighting();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderToolTip(ItemStack stack, int x, int y) {
/* 578 */     if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex()) {
/*     */       
/* 580 */       List<String> list = stack.getTooltip((EntityPlayer)this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
/* 581 */       CreativeTabs creativetabs = stack.getItem().getCreativeTab();
/*     */       
/* 583 */       if (creativetabs == null && stack.getItem() == Items.enchanted_book) {
/*     */         
/* 585 */         Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(stack);
/*     */         
/* 587 */         if (map.size() == 1) {
/*     */           
/* 589 */           Enchantment enchantment = Enchantment.getEnchantmentById(((Integer)map.keySet().iterator().next()).intValue());
/*     */           
/* 591 */           for (CreativeTabs creativetabs1 : CreativeTabs.creativeTabArray) {
/*     */             
/* 593 */             if (creativetabs1.hasRelevantEnchantmentType(enchantment.type)) {
/*     */               
/* 595 */               creativetabs = creativetabs1;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 602 */       if (creativetabs != null)
/*     */       {
/* 604 */         list.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE + I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]));
/*     */       }
/*     */       
/* 607 */       for (int i = 0; i < list.size(); i++) {
/*     */         
/* 609 */         if (i == 0) {
/*     */           
/* 611 */           list.set(i, (stack.getRarity()).rarityColor + (String)list.get(i));
/*     */         }
/*     */         else {
/*     */           
/* 615 */           list.set(i, EnumChatFormatting.GRAY + (String)list.get(i));
/*     */         } 
/*     */       } 
/*     */       
/* 619 */       drawHoveringText(list, x, y);
/*     */     }
/*     */     else {
/*     */       
/* 623 */       super.renderToolTip(stack, x, y);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 629 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 630 */     RenderHelper.enableGUIStandardItemLighting();
/* 631 */     CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
/*     */     
/* 633 */     for (CreativeTabs creativetabs1 : CreativeTabs.creativeTabArray) {
/*     */       
/* 635 */       this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
/*     */       
/* 637 */       if (creativetabs1.getTabIndex() != selectedTabIndex)
/*     */       {
/* 639 */         func_147051_a(creativetabs1);
/*     */       }
/*     */     } 
/*     */     
/* 643 */     this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
/* 644 */     drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
/* 645 */     this.searchField.drawTextBox();
/* 646 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 647 */     int i = this.guiLeft + 175;
/* 648 */     int j = this.guiTop + 18;
/* 649 */     int k = j + 112;
/* 650 */     this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
/*     */     
/* 652 */     if (creativetabs.shouldHidePlayerInventory())
/*     */     {
/* 654 */       drawTexturedModalRect(i, j + (int)((k - j - 17) * this.currentScroll), 232 + (needsScrollBars() ? 0 : 12), 0, 12, 15);
/*     */     }
/*     */     
/* 657 */     func_147051_a(creativetabs);
/*     */     
/* 659 */     if (creativetabs == CreativeTabs.tabInventory)
/*     */     {
/* 661 */       GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, (this.guiLeft + 43 - mouseX), (this.guiTop + 45 - 30 - mouseY), (EntityLivingBase)this.mc.thePlayer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_) {
/* 667 */     int i = p_147049_1_.getTabColumn();
/* 668 */     int j = 28 * i;
/* 669 */     int k = 0;
/*     */     
/* 671 */     if (i == 5) {
/*     */       
/* 673 */       j = this.xSize - 28 + 2;
/*     */     }
/* 675 */     else if (i > 0) {
/*     */       
/* 677 */       j += i;
/*     */     } 
/*     */     
/* 680 */     if (p_147049_1_.isTabInFirstRow()) {
/*     */       
/* 682 */       k -= 32;
/*     */     }
/*     */     else {
/*     */       
/* 686 */       k += this.ySize;
/*     */     } 
/*     */     
/* 689 */     return (p_147049_2_ >= j && p_147049_2_ <= j + 28 && p_147049_3_ >= k && p_147049_3_ <= k + 32);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean renderCreativeInventoryHoveringText(CreativeTabs p_147052_1_, int p_147052_2_, int p_147052_3_) {
/* 694 */     int i = p_147052_1_.getTabColumn();
/* 695 */     int j = 28 * i;
/* 696 */     int k = 0;
/*     */     
/* 698 */     if (i == 5) {
/*     */       
/* 700 */       j = this.xSize - 28 + 2;
/*     */     }
/* 702 */     else if (i > 0) {
/*     */       
/* 704 */       j += i;
/*     */     } 
/*     */     
/* 707 */     if (p_147052_1_.isTabInFirstRow()) {
/*     */       
/* 709 */       k -= 32;
/*     */     }
/*     */     else {
/*     */       
/* 713 */       k += this.ySize;
/*     */     } 
/*     */     
/* 716 */     if (isPointInRegion(j + 3, k + 3, 23, 27, p_147052_2_, p_147052_3_)) {
/*     */       
/* 718 */       drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
/* 719 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 723 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_147051_a(CreativeTabs p_147051_1_) {
/* 729 */     boolean flag = (p_147051_1_.getTabIndex() == selectedTabIndex);
/* 730 */     boolean flag1 = p_147051_1_.isTabInFirstRow();
/* 731 */     int i = p_147051_1_.getTabColumn();
/* 732 */     int j = i * 28;
/* 733 */     int k = 0;
/* 734 */     int l = this.guiLeft + 28 * i;
/* 735 */     int i1 = this.guiTop;
/* 736 */     int j1 = 32;
/*     */     
/* 738 */     if (flag)
/*     */     {
/* 740 */       k += 32;
/*     */     }
/*     */     
/* 743 */     if (i == 5) {
/*     */       
/* 745 */       l = this.guiLeft + this.xSize - 28;
/*     */     }
/* 747 */     else if (i > 0) {
/*     */       
/* 749 */       l += i;
/*     */     } 
/*     */     
/* 752 */     if (flag1) {
/*     */       
/* 754 */       i1 -= 28;
/*     */     }
/*     */     else {
/*     */       
/* 758 */       k += 64;
/* 759 */       i1 += this.ySize - 4;
/*     */     } 
/*     */     
/* 762 */     GlStateManager.disableLighting();
/* 763 */     drawTexturedModalRect(l, i1, j, k, 28, j1);
/* 764 */     this.zLevel = 100.0F;
/* 765 */     this.itemRender.zLevel = 100.0F;
/* 766 */     l += 6;
/* 767 */     i1 = i1 + 8 + (flag1 ? 1 : -1);
/* 768 */     GlStateManager.enableLighting();
/* 769 */     GlStateManager.enableRescaleNormal();
/* 770 */     ItemStack itemstack = p_147051_1_.getIconItemStack();
/* 771 */     this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
/* 772 */     this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i1);
/* 773 */     GlStateManager.disableLighting();
/* 774 */     this.itemRender.zLevel = 0.0F;
/* 775 */     this.zLevel = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 780 */     if (button.id == 0)
/*     */     {
/* 782 */       this.mc.displayGuiScreen((GuiScreen)new GuiAchievements((GuiScreen)this, this.mc.thePlayer.getStatFileWriter()));
/*     */     }
/*     */     
/* 785 */     if (button.id == 1)
/*     */     {
/* 787 */       this.mc.displayGuiScreen((GuiScreen)new GuiStats((GuiScreen)this, this.mc.thePlayer.getStatFileWriter()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSelectedTabIndex() {
/* 793 */     return selectedTabIndex;
/*     */   }
/*     */   
/*     */   static class ContainerCreative
/*     */     extends Container {
/* 798 */     public List<ItemStack> itemList = Lists.newArrayList();
/*     */ 
/*     */     
/*     */     public ContainerCreative(EntityPlayer p_i1086_1_) {
/* 802 */       InventoryPlayer inventoryplayer = p_i1086_1_.inventory;
/*     */       
/* 804 */       for (int i = 0; i < 5; i++) {
/*     */         
/* 806 */         for (int j = 0; j < 9; j++)
/*     */         {
/* 808 */           addSlotToContainer(new Slot((IInventory)GuiContainerCreative.field_147060_v, i * 9 + j, 9 + j * 18, 18 + i * 18));
/*     */         }
/*     */       } 
/*     */       
/* 812 */       for (int k = 0; k < 9; k++)
/*     */       {
/* 814 */         addSlotToContainer(new Slot((IInventory)inventoryplayer, k, 9 + k * 18, 112));
/*     */       }
/*     */       
/* 817 */       scrollTo(0.0F);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canInteractWith(EntityPlayer playerIn) {
/* 822 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void scrollTo(float p_148329_1_) {
/* 827 */       int i = (this.itemList.size() + 9 - 1) / 9 - 5;
/* 828 */       int j = (int)((p_148329_1_ * i) + 0.5D);
/*     */       
/* 830 */       if (j < 0)
/*     */       {
/* 832 */         j = 0;
/*     */       }
/*     */       
/* 835 */       for (int k = 0; k < 5; k++) {
/*     */         
/* 837 */         for (int l = 0; l < 9; l++) {
/*     */           
/* 839 */           int i1 = l + (k + j) * 9;
/*     */           
/* 841 */           if (i1 >= 0 && i1 < this.itemList.size()) {
/*     */             
/* 843 */             GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, this.itemList.get(i1));
/*     */           }
/*     */           else {
/*     */             
/* 847 */             GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, (ItemStack)null);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean func_148328_e() {
/* 855 */       return (this.itemList.size() > 45);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn) {}
/*     */ 
/*     */     
/*     */     public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/* 864 */       if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
/*     */         
/* 866 */         Slot slot = this.inventorySlots.get(index);
/*     */         
/* 868 */         if (slot != null && slot.getHasStack())
/*     */         {
/* 870 */           slot.putStack((ItemStack)null);
/*     */         }
/*     */       } 
/*     */       
/* 874 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
/* 879 */       return (slotIn.yDisplayPosition > 90);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canDragIntoSlot(Slot p_94531_1_) {
/* 884 */       return (p_94531_1_.inventory instanceof InventoryPlayer || (p_94531_1_.yDisplayPosition > 90 && p_94531_1_.xDisplayPosition <= 162));
/*     */     }
/*     */   }
/*     */   
/*     */   class CreativeSlot
/*     */     extends Slot
/*     */   {
/*     */     private final Slot slot;
/*     */     
/*     */     public CreativeSlot(Slot p_i46313_2_, int p_i46313_3_) {
/* 894 */       super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
/* 895 */       this.slot = p_i46313_2_;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
/* 900 */       this.slot.onPickupFromSlot(playerIn, stack);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isItemValid(ItemStack stack) {
/* 905 */       return this.slot.isItemValid(stack);
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack getStack() {
/* 910 */       return this.slot.getStack();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getHasStack() {
/* 915 */       return this.slot.getHasStack();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putStack(ItemStack stack) {
/* 920 */       this.slot.putStack(stack);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSlotChanged() {
/* 925 */       this.slot.onSlotChanged();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getSlotStackLimit() {
/* 930 */       return this.slot.getSlotStackLimit();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getItemStackLimit(ItemStack stack) {
/* 935 */       return this.slot.getItemStackLimit(stack);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSlotTexture() {
/* 940 */       return this.slot.getSlotTexture();
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack decrStackSize(int amount) {
/* 945 */       return this.slot.decrStackSize(amount);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHere(IInventory inv, int slotIn) {
/* 950 */       return this.slot.isHere(inv, slotIn);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\inventory\GuiContainerCreative.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */