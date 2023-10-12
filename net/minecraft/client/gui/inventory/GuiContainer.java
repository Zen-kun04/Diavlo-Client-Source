/*     */ package net.minecraft.client.gui.inventory;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public abstract class GuiContainer extends GuiScreen {
/*  24 */   protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
/*  25 */   protected int xSize = 176;
/*  26 */   protected int ySize = 166;
/*     */   public Container inventorySlots;
/*     */   protected int guiLeft;
/*     */   protected int guiTop;
/*     */   private Slot theSlot;
/*     */   private Slot clickedSlot;
/*     */   private boolean isRightMouseClick;
/*     */   private ItemStack draggedStack;
/*     */   private int touchUpX;
/*     */   private int touchUpY;
/*     */   private Slot returningStackDestSlot;
/*     */   private long returningStackTime;
/*     */   private ItemStack returningStack;
/*     */   private Slot currentDragTargetSlot;
/*     */   private long dragItemDropDelay;
/*  41 */   protected final Set<Slot> dragSplittingSlots = Sets.newHashSet();
/*     */   
/*     */   protected boolean dragSplitting;
/*     */   private int dragSplittingLimit;
/*     */   private int dragSplittingButton;
/*     */   private boolean ignoreMouseUp;
/*     */   private int dragSplittingRemnant;
/*     */   private long lastClickTime;
/*     */   private Slot lastClickSlot;
/*     */   private int lastClickButton;
/*     */   private boolean doubleClick;
/*     */   private ItemStack shiftClickedSlot;
/*     */   
/*     */   public GuiContainer(Container inventorySlotsIn) {
/*  55 */     this.inventorySlots = inventorySlotsIn;
/*  56 */     this.ignoreMouseUp = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  61 */     super.initGui();
/*  62 */     this.mc.thePlayer.openContainer = this.inventorySlots;
/*  63 */     this.guiLeft = (this.width - this.xSize) / 2;
/*  64 */     this.guiTop = (this.height - this.ySize) / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  69 */     drawDefaultBackground();
/*  70 */     int i = this.guiLeft;
/*  71 */     int j = this.guiTop;
/*  72 */     drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
/*  73 */     GlStateManager.disableRescaleNormal();
/*  74 */     RenderHelper.disableStandardItemLighting();
/*  75 */     GlStateManager.disableLighting();
/*  76 */     GlStateManager.disableDepth();
/*  77 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*  78 */     RenderHelper.enableGUIStandardItemLighting();
/*  79 */     GlStateManager.pushMatrix();
/*  80 */     GlStateManager.translate(i, j, 0.0F);
/*  81 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  82 */     GlStateManager.enableRescaleNormal();
/*  83 */     this.theSlot = null;
/*  84 */     int k = 240;
/*  85 */     int l = 240;
/*  86 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, l / 1.0F);
/*  87 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*  89 */     for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); i1++) {
/*     */       
/*  91 */       Slot slot = this.inventorySlots.inventorySlots.get(i1);
/*  92 */       drawSlot(slot);
/*     */       
/*  94 */       if (isMouseOverSlot(slot, mouseX, mouseY) && slot.canBeHovered()) {
/*     */         
/*  96 */         this.theSlot = slot;
/*  97 */         GlStateManager.disableLighting();
/*  98 */         GlStateManager.disableDepth();
/*  99 */         int j1 = slot.xDisplayPosition;
/* 100 */         int k1 = slot.yDisplayPosition;
/* 101 */         GlStateManager.colorMask(true, true, true, false);
/* 102 */         drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
/* 103 */         GlStateManager.colorMask(true, true, true, true);
/* 104 */         GlStateManager.enableLighting();
/* 105 */         GlStateManager.enableDepth();
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     RenderHelper.disableStandardItemLighting();
/* 110 */     drawGuiContainerForegroundLayer(mouseX, mouseY);
/* 111 */     RenderHelper.enableGUIStandardItemLighting();
/* 112 */     InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
/* 113 */     ItemStack itemstack = (this.draggedStack == null) ? inventoryplayer.getItemStack() : this.draggedStack;
/*     */     
/* 115 */     if (itemstack != null) {
/*     */       
/* 117 */       int j2 = 8;
/* 118 */       int k2 = (this.draggedStack == null) ? 8 : 16;
/* 119 */       String s = null;
/*     */       
/* 121 */       if (this.draggedStack != null && this.isRightMouseClick) {
/*     */         
/* 123 */         itemstack = itemstack.copy();
/* 124 */         itemstack.stackSize = MathHelper.ceiling_float_int(itemstack.stackSize / 2.0F);
/*     */       }
/* 126 */       else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
/*     */         
/* 128 */         itemstack = itemstack.copy();
/* 129 */         itemstack.stackSize = this.dragSplittingRemnant;
/*     */         
/* 131 */         if (itemstack.stackSize == 0)
/*     */         {
/* 133 */           s = "" + EnumChatFormatting.YELLOW + "0";
/*     */         }
/*     */       } 
/*     */       
/* 137 */       drawItemStack(itemstack, mouseX - i - j2, mouseY - j - k2, s);
/*     */     } 
/*     */     
/* 140 */     if (this.returningStack != null) {
/*     */       
/* 142 */       float f = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
/*     */       
/* 144 */       if (f >= 1.0F) {
/*     */         
/* 146 */         f = 1.0F;
/* 147 */         this.returningStack = null;
/*     */       } 
/*     */       
/* 150 */       int l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
/* 151 */       int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
/* 152 */       int l1 = this.touchUpX + (int)(l2 * f);
/* 153 */       int i2 = this.touchUpY + (int)(i3 * f);
/* 154 */       drawItemStack(this.returningStack, l1, i2, (String)null);
/*     */     } 
/*     */     
/* 157 */     GlStateManager.popMatrix();
/*     */     
/* 159 */     if (inventoryplayer.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
/*     */       
/* 161 */       ItemStack itemstack1 = this.theSlot.getStack();
/* 162 */       renderToolTip(itemstack1, mouseX, mouseY);
/*     */     } 
/*     */     
/* 165 */     GlStateManager.enableLighting();
/* 166 */     GlStateManager.enableDepth();
/* 167 */     RenderHelper.enableStandardItemLighting();
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawItemStack(ItemStack stack, int x, int y, String altText) {
/* 172 */     GlStateManager.translate(0.0F, 0.0F, 32.0F);
/* 173 */     this.zLevel = 200.0F;
/* 174 */     this.itemRender.zLevel = 200.0F;
/* 175 */     this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
/* 176 */     this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, stack, x, y - ((this.draggedStack == null) ? 0 : 8), altText);
/* 177 */     this.zLevel = 0.0F;
/* 178 */     this.itemRender.zLevel = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}
/*     */ 
/*     */   
/*     */   protected abstract void drawGuiContainerBackgroundLayer(float paramFloat, int paramInt1, int paramInt2);
/*     */ 
/*     */   
/*     */   private void drawSlot(Slot slotIn) {
/* 189 */     int i = slotIn.xDisplayPosition;
/* 190 */     int j = slotIn.yDisplayPosition;
/* 191 */     ItemStack itemstack = slotIn.getStack();
/* 192 */     boolean flag = false;
/* 193 */     boolean flag1 = (slotIn == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick);
/* 194 */     ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
/* 195 */     String s = null;
/*     */     
/* 197 */     if (slotIn == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
/*     */       
/* 199 */       itemstack = itemstack.copy();
/* 200 */       itemstack.stackSize /= 2;
/*     */     }
/* 202 */     else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && itemstack1 != null) {
/*     */       
/* 204 */       if (this.dragSplittingSlots.size() == 1) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 209 */       if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
/*     */         
/* 211 */         itemstack = itemstack1.copy();
/* 212 */         flag = true;
/* 213 */         Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, (slotIn.getStack() == null) ? 0 : (slotIn.getStack()).stackSize);
/*     */         
/* 215 */         if (itemstack.stackSize > itemstack.getMaxStackSize()) {
/*     */           
/* 217 */           s = EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
/* 218 */           itemstack.stackSize = itemstack.getMaxStackSize();
/*     */         } 
/*     */         
/* 221 */         if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack))
/*     */         {
/* 223 */           s = EnumChatFormatting.YELLOW + "" + slotIn.getItemStackLimit(itemstack);
/* 224 */           itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 229 */         this.dragSplittingSlots.remove(slotIn);
/* 230 */         updateDragSplitting();
/*     */       } 
/*     */     } 
/*     */     
/* 234 */     this.zLevel = 100.0F;
/* 235 */     this.itemRender.zLevel = 100.0F;
/*     */     
/* 237 */     if (itemstack == null) {
/*     */       
/* 239 */       String s1 = slotIn.getSlotTexture();
/*     */       
/* 241 */       if (s1 != null) {
/*     */         
/* 243 */         TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(s1);
/* 244 */         GlStateManager.disableLighting();
/* 245 */         this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 246 */         drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
/* 247 */         GlStateManager.enableLighting();
/* 248 */         flag1 = true;
/*     */       } 
/*     */     } 
/*     */     
/* 252 */     if (!flag1) {
/*     */       
/* 254 */       if (flag)
/*     */       {
/* 256 */         drawRect(i, j, (i + 16), (j + 16), -2130706433);
/*     */       }
/*     */       
/* 259 */       GlStateManager.enableDepth();
/* 260 */       this.itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
/* 261 */       this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
/*     */     } 
/*     */     
/* 264 */     this.itemRender.zLevel = 0.0F;
/* 265 */     this.zLevel = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateDragSplitting() {
/* 270 */     ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
/*     */     
/* 272 */     if (itemstack != null && this.dragSplitting) {
/*     */       
/* 274 */       this.dragSplittingRemnant = itemstack.stackSize;
/*     */       
/* 276 */       for (Slot slot : this.dragSplittingSlots) {
/*     */         
/* 278 */         ItemStack itemstack1 = itemstack.copy();
/* 279 */         int i = (slot.getStack() == null) ? 0 : (slot.getStack()).stackSize;
/* 280 */         Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
/*     */         
/* 282 */         if (itemstack1.stackSize > itemstack1.getMaxStackSize())
/*     */         {
/* 284 */           itemstack1.stackSize = itemstack1.getMaxStackSize();
/*     */         }
/*     */         
/* 287 */         if (itemstack1.stackSize > slot.getItemStackLimit(itemstack1))
/*     */         {
/* 289 */           itemstack1.stackSize = slot.getItemStackLimit(itemstack1);
/*     */         }
/*     */         
/* 292 */         this.dragSplittingRemnant -= itemstack1.stackSize - i;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Slot getSlotAtPosition(int x, int y) {
/* 299 */     for (int i = 0; i < this.inventorySlots.inventorySlots.size(); i++) {
/*     */       
/* 301 */       Slot slot = this.inventorySlots.inventorySlots.get(i);
/*     */       
/* 303 */       if (isMouseOverSlot(slot, x, y))
/*     */       {
/* 305 */         return slot;
/*     */       }
/*     */     } 
/*     */     
/* 309 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 314 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 315 */     boolean flag = (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100);
/* 316 */     Slot slot = getSlotAtPosition(mouseX, mouseY);
/* 317 */     long i = Minecraft.getSystemTime();
/* 318 */     this.doubleClick = (this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == mouseButton);
/* 319 */     this.ignoreMouseUp = false;
/*     */     
/* 321 */     if (mouseButton == 0 || mouseButton == 1 || flag) {
/*     */       
/* 323 */       int j = this.guiLeft;
/* 324 */       int k = this.guiTop;
/* 325 */       boolean flag1 = (mouseX < j || mouseY < k || mouseX >= j + this.xSize || mouseY >= k + this.ySize);
/* 326 */       int l = -1;
/*     */       
/* 328 */       if (slot != null)
/*     */       {
/* 330 */         l = slot.slotNumber;
/*     */       }
/*     */       
/* 333 */       if (flag1)
/*     */       {
/* 335 */         l = -999;
/*     */       }
/*     */       
/* 338 */       if (this.mc.gameSettings.touchscreen && flag1 && this.mc.thePlayer.inventory.getItemStack() == null) {
/*     */         
/* 340 */         this.mc.displayGuiScreen((GuiScreen)null);
/*     */         
/*     */         return;
/*     */       } 
/* 344 */       if (l != -1)
/*     */       {
/* 346 */         if (this.mc.gameSettings.touchscreen) {
/*     */           
/* 348 */           if (slot != null && slot.getHasStack())
/*     */           {
/* 350 */             this.clickedSlot = slot;
/* 351 */             this.draggedStack = null;
/* 352 */             this.isRightMouseClick = (mouseButton == 1);
/*     */           }
/*     */           else
/*     */           {
/* 356 */             this.clickedSlot = null;
/*     */           }
/*     */         
/* 359 */         } else if (!this.dragSplitting) {
/*     */           
/* 361 */           if (this.mc.thePlayer.inventory.getItemStack() == null) {
/*     */             
/* 363 */             if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
/*     */               
/* 365 */               handleMouseClick(slot, l, mouseButton, 3);
/*     */             }
/*     */             else {
/*     */               
/* 369 */               boolean flag2 = (l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)));
/* 370 */               int i1 = 0;
/*     */               
/* 372 */               if (flag2) {
/*     */                 
/* 374 */                 this.shiftClickedSlot = (slot != null && slot.getHasStack()) ? slot.getStack() : null;
/* 375 */                 i1 = 1;
/*     */               }
/* 377 */               else if (l == -999) {
/*     */                 
/* 379 */                 i1 = 4;
/*     */               } 
/*     */               
/* 382 */               handleMouseClick(slot, l, mouseButton, i1);
/*     */             } 
/*     */             
/* 385 */             this.ignoreMouseUp = true;
/*     */           }
/*     */           else {
/*     */             
/* 389 */             this.dragSplitting = true;
/* 390 */             this.dragSplittingButton = mouseButton;
/* 391 */             this.dragSplittingSlots.clear();
/*     */             
/* 393 */             if (mouseButton == 0) {
/*     */               
/* 395 */               this.dragSplittingLimit = 0;
/*     */             }
/* 397 */             else if (mouseButton == 1) {
/*     */               
/* 399 */               this.dragSplittingLimit = 1;
/*     */             }
/* 401 */             else if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
/*     */               
/* 403 */               this.dragSplittingLimit = 2;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 410 */     this.lastClickSlot = slot;
/* 411 */     this.lastClickTime = i;
/* 412 */     this.lastClickButton = mouseButton;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
/* 417 */     Slot slot = getSlotAtPosition(mouseX, mouseY);
/* 418 */     ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
/*     */     
/* 420 */     if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
/*     */       
/* 422 */       if (clickedMouseButton == 0 || clickedMouseButton == 1)
/*     */       {
/* 424 */         if (this.draggedStack == null) {
/*     */           
/* 426 */           if (slot != this.clickedSlot && this.clickedSlot.getStack() != null)
/*     */           {
/* 428 */             this.draggedStack = this.clickedSlot.getStack().copy();
/*     */           }
/*     */         }
/* 431 */         else if (this.draggedStack.stackSize > 1 && slot != null && Container.canAddItemToSlot(slot, this.draggedStack, false)) {
/*     */           
/* 433 */           long i = Minecraft.getSystemTime();
/*     */           
/* 435 */           if (this.currentDragTargetSlot == slot) {
/*     */             
/* 437 */             if (i - this.dragItemDropDelay > 500L)
/*     */             {
/* 439 */               handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
/* 440 */               handleMouseClick(slot, slot.slotNumber, 1, 0);
/* 441 */               handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
/* 442 */               this.dragItemDropDelay = i + 750L;
/* 443 */               this.draggedStack.stackSize--;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 448 */             this.currentDragTargetSlot = slot;
/* 449 */             this.dragItemDropDelay = i;
/*     */           }
/*     */         
/*     */         } 
/*     */       }
/* 454 */     } else if (this.dragSplitting && slot != null && itemstack != null && itemstack.stackSize > this.dragSplittingSlots.size() && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot)) {
/*     */       
/* 456 */       this.dragSplittingSlots.add(slot);
/* 457 */       updateDragSplitting();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 463 */     Slot slot = getSlotAtPosition(mouseX, mouseY);
/* 464 */     int i = this.guiLeft;
/* 465 */     int j = this.guiTop;
/* 466 */     boolean flag = (mouseX < i || mouseY < j || mouseX >= i + this.xSize || mouseY >= j + this.ySize);
/* 467 */     int k = -1;
/*     */     
/* 469 */     if (slot != null)
/*     */     {
/* 471 */       k = slot.slotNumber;
/*     */     }
/*     */     
/* 474 */     if (flag)
/*     */     {
/* 476 */       k = -999;
/*     */     }
/*     */     
/* 479 */     if (this.doubleClick && slot != null && state == 0 && this.inventorySlots.canMergeSlot((ItemStack)null, slot)) {
/*     */       
/* 481 */       if (isShiftKeyDown()) {
/*     */         
/* 483 */         if (slot != null && slot.inventory != null && this.shiftClickedSlot != null)
/*     */         {
/* 485 */           for (Slot slot2 : this.inventorySlots.inventorySlots)
/*     */           {
/* 487 */             if (slot2 != null && slot2.canTakeStack((EntityPlayer)this.mc.thePlayer) && slot2.getHasStack() && slot2.inventory == slot.inventory && Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true))
/*     */             {
/* 489 */               handleMouseClick(slot2, slot2.slotNumber, state, 1);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/*     */       } else {
/*     */         
/* 496 */         handleMouseClick(slot, k, state, 6);
/*     */       } 
/*     */       
/* 499 */       this.doubleClick = false;
/* 500 */       this.lastClickTime = 0L;
/*     */     }
/*     */     else {
/*     */       
/* 504 */       if (this.dragSplitting && this.dragSplittingButton != state) {
/*     */         
/* 506 */         this.dragSplitting = false;
/* 507 */         this.dragSplittingSlots.clear();
/* 508 */         this.ignoreMouseUp = true;
/*     */         
/*     */         return;
/*     */       } 
/* 512 */       if (this.ignoreMouseUp) {
/*     */         
/* 514 */         this.ignoreMouseUp = false;
/*     */         
/*     */         return;
/*     */       } 
/* 518 */       if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
/*     */         
/* 520 */         if (state == 0 || state == 1)
/*     */         {
/* 522 */           if (this.draggedStack == null && slot != this.clickedSlot)
/*     */           {
/* 524 */             this.draggedStack = this.clickedSlot.getStack();
/*     */           }
/*     */           
/* 527 */           boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);
/*     */           
/* 529 */           if (k != -1 && this.draggedStack != null && flag2) {
/*     */             
/* 531 */             handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
/* 532 */             handleMouseClick(slot, k, 0, 0);
/*     */             
/* 534 */             if (this.mc.thePlayer.inventory.getItemStack() != null)
/*     */             {
/* 536 */               handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
/* 537 */               this.touchUpX = mouseX - i;
/* 538 */               this.touchUpY = mouseY - j;
/* 539 */               this.returningStackDestSlot = this.clickedSlot;
/* 540 */               this.returningStack = this.draggedStack;
/* 541 */               this.returningStackTime = Minecraft.getSystemTime();
/*     */             }
/*     */             else
/*     */             {
/* 545 */               this.returningStack = null;
/*     */             }
/*     */           
/* 548 */           } else if (this.draggedStack != null) {
/*     */             
/* 550 */             this.touchUpX = mouseX - i;
/* 551 */             this.touchUpY = mouseY - j;
/* 552 */             this.returningStackDestSlot = this.clickedSlot;
/* 553 */             this.returningStack = this.draggedStack;
/* 554 */             this.returningStackTime = Minecraft.getSystemTime();
/*     */           } 
/*     */           
/* 557 */           this.draggedStack = null;
/* 558 */           this.clickedSlot = null;
/*     */         }
/*     */       
/* 561 */       } else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
/*     */         
/* 563 */         handleMouseClick((Slot)null, -999, Container.func_94534_d(0, this.dragSplittingLimit), 5);
/*     */         
/* 565 */         for (Slot slot1 : this.dragSplittingSlots)
/*     */         {
/* 567 */           handleMouseClick(slot1, slot1.slotNumber, Container.func_94534_d(1, this.dragSplittingLimit), 5);
/*     */         }
/*     */         
/* 570 */         handleMouseClick((Slot)null, -999, Container.func_94534_d(2, this.dragSplittingLimit), 5);
/*     */       }
/* 572 */       else if (this.mc.thePlayer.inventory.getItemStack() != null) {
/*     */         
/* 574 */         if (state == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
/*     */           
/* 576 */           handleMouseClick(slot, k, state, 3);
/*     */         }
/*     */         else {
/*     */           
/* 580 */           boolean flag1 = (k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)));
/*     */           
/* 582 */           if (flag1)
/*     */           {
/* 584 */             this.shiftClickedSlot = (slot != null && slot.getHasStack()) ? slot.getStack() : null;
/*     */           }
/*     */           
/* 587 */           handleMouseClick(slot, k, state, flag1 ? 1 : 0);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 592 */     if (this.mc.thePlayer.inventory.getItemStack() == null)
/*     */     {
/* 594 */       this.lastClickTime = 0L;
/*     */     }
/*     */     
/* 597 */     this.dragSplitting = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
/* 602 */     return isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPointInRegion(int left, int top, int right, int bottom, int pointX, int pointY) {
/* 607 */     int i = this.guiLeft;
/* 608 */     int j = this.guiTop;
/* 609 */     pointX -= i;
/* 610 */     pointY -= j;
/* 611 */     return (pointX >= left - 1 && pointX < left + right + 1 && pointY >= top - 1 && pointY < top + bottom + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType) {
/* 616 */     if (slotIn != null)
/*     */     {
/* 618 */       slotId = slotIn.slotNumber;
/*     */     }
/*     */     
/* 621 */     this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, clickedButton, clickType, (EntityPlayer)this.mc.thePlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 626 */     if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
/*     */     {
/* 628 */       this.mc.thePlayer.closeScreen();
/*     */     }
/*     */     
/* 631 */     checkHotbarKeys(keyCode);
/*     */     
/* 633 */     if (this.theSlot != null && this.theSlot.getHasStack())
/*     */     {
/* 635 */       if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
/*     */         
/* 637 */         handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
/*     */       }
/* 639 */       else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
/*     */         
/* 641 */         handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkHotbarKeys(int keyCode) {
/* 648 */     if (this.mc.thePlayer.inventory.getItemStack() == null && this.theSlot != null)
/*     */     {
/* 650 */       for (int i = 0; i < 9; i++) {
/*     */         
/* 652 */         if (keyCode == this.mc.gameSettings.keyBindsHotbar[i].getKeyCode()) {
/*     */           
/* 654 */           handleMouseClick(this.theSlot, this.theSlot.slotNumber, i, 2);
/* 655 */           return true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 660 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 665 */     if (this.mc.thePlayer != null)
/*     */     {
/* 667 */       this.inventorySlots.onContainerClosed((EntityPlayer)this.mc.thePlayer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 673 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 678 */     super.updateScreen();
/*     */     
/* 680 */     if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
/*     */     {
/* 682 */       this.mc.thePlayer.closeScreen();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\inventory\GuiContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */