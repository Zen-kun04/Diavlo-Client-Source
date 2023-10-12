/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public abstract class Container
/*     */ {
/*  16 */   public List<ItemStack> inventoryItemStacks = Lists.newArrayList();
/*  17 */   public List<Slot> inventorySlots = Lists.newArrayList();
/*     */   public int windowId;
/*     */   private short transactionID;
/*  20 */   private int dragMode = -1;
/*     */   private int dragEvent;
/*  22 */   private final Set<Slot> dragSlots = Sets.newHashSet();
/*  23 */   protected List<ICrafting> crafters = Lists.newArrayList();
/*  24 */   private Set<EntityPlayer> playerList = Sets.newHashSet();
/*     */ 
/*     */   
/*     */   protected Slot addSlotToContainer(Slot slotIn) {
/*  28 */     slotIn.slotNumber = this.inventorySlots.size();
/*  29 */     this.inventorySlots.add(slotIn);
/*  30 */     this.inventoryItemStacks.add((ItemStack)null);
/*  31 */     return slotIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener) {
/*  36 */     if (this.crafters.contains(listener))
/*     */     {
/*  38 */       throw new IllegalArgumentException("Listener already listening");
/*     */     }
/*     */ 
/*     */     
/*  42 */     this.crafters.add(listener);
/*  43 */     listener.updateCraftingInventory(this, getInventory());
/*  44 */     detectAndSendChanges();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCraftingFromCrafters(ICrafting listeners) {
/*  50 */     this.crafters.remove(listeners);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ItemStack> getInventory() {
/*  55 */     List<ItemStack> list = Lists.newArrayList();
/*     */     
/*  57 */     for (int i = 0; i < this.inventorySlots.size(); i++)
/*     */     {
/*  59 */       list.add(((Slot)this.inventorySlots.get(i)).getStack());
/*     */     }
/*     */     
/*  62 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void detectAndSendChanges() {
/*  67 */     for (int i = 0; i < this.inventorySlots.size(); i++) {
/*     */       
/*  69 */       ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
/*  70 */       ItemStack itemstack1 = this.inventoryItemStacks.get(i);
/*     */       
/*  72 */       if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
/*     */         
/*  74 */         itemstack1 = (itemstack == null) ? null : itemstack.copy();
/*  75 */         this.inventoryItemStacks.set(i, itemstack1);
/*     */         
/*  77 */         for (int j = 0; j < this.crafters.size(); j++)
/*     */         {
/*  79 */           ((ICrafting)this.crafters.get(j)).sendSlotContents(this, i, itemstack1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean enchantItem(EntityPlayer playerIn, int id) {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Slot getSlotFromInventory(IInventory inv, int slotIn) {
/*  92 */     for (int i = 0; i < this.inventorySlots.size(); i++) {
/*     */       
/*  94 */       Slot slot = this.inventorySlots.get(i);
/*     */       
/*  96 */       if (slot.isHere(inv, slotIn))
/*     */       {
/*  98 */         return slot;
/*     */       }
/*     */     } 
/*     */     
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Slot getSlot(int slotId) {
/* 107 */     return this.inventorySlots.get(slotId);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/* 112 */     Slot slot = this.inventorySlots.get(index);
/* 113 */     return (slot != null) ? slot.getStack() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
/* 118 */     ItemStack itemstack = null;
/* 119 */     InventoryPlayer inventoryplayer = playerIn.inventory;
/*     */     
/* 121 */     if (mode == 5) {
/*     */       
/* 123 */       int i = this.dragEvent;
/* 124 */       this.dragEvent = getDragEvent(clickedButton);
/*     */       
/* 126 */       if ((i != 1 || this.dragEvent != 2) && i != this.dragEvent) {
/*     */         
/* 128 */         resetDrag();
/*     */       }
/* 130 */       else if (inventoryplayer.getItemStack() == null) {
/*     */         
/* 132 */         resetDrag();
/*     */       }
/* 134 */       else if (this.dragEvent == 0) {
/*     */         
/* 136 */         this.dragMode = extractDragMode(clickedButton);
/*     */         
/* 138 */         if (isValidDragMode(this.dragMode, playerIn))
/*     */         {
/* 140 */           this.dragEvent = 1;
/* 141 */           this.dragSlots.clear();
/*     */         }
/*     */         else
/*     */         {
/* 145 */           resetDrag();
/*     */         }
/*     */       
/* 148 */       } else if (this.dragEvent == 1) {
/*     */         
/* 150 */         Slot slot = this.inventorySlots.get(slotId);
/*     */         
/* 152 */         if (slot != null && canAddItemToSlot(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && (inventoryplayer.getItemStack()).stackSize > this.dragSlots.size() && canDragIntoSlot(slot))
/*     */         {
/* 154 */           this.dragSlots.add(slot);
/*     */         }
/*     */       }
/* 157 */       else if (this.dragEvent == 2) {
/*     */         
/* 159 */         if (!this.dragSlots.isEmpty()) {
/*     */           
/* 161 */           ItemStack itemstack3 = inventoryplayer.getItemStack().copy();
/* 162 */           int j = (inventoryplayer.getItemStack()).stackSize;
/*     */           
/* 164 */           for (Slot slot1 : this.dragSlots) {
/*     */             
/* 166 */             if (slot1 != null && canAddItemToSlot(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && (inventoryplayer.getItemStack()).stackSize >= this.dragSlots.size() && canDragIntoSlot(slot1)) {
/*     */               
/* 168 */               ItemStack itemstack1 = itemstack3.copy();
/* 169 */               int k = slot1.getHasStack() ? (slot1.getStack()).stackSize : 0;
/* 170 */               computeStackSize(this.dragSlots, this.dragMode, itemstack1, k);
/*     */               
/* 172 */               if (itemstack1.stackSize > itemstack1.getMaxStackSize())
/*     */               {
/* 174 */                 itemstack1.stackSize = itemstack1.getMaxStackSize();
/*     */               }
/*     */               
/* 177 */               if (itemstack1.stackSize > slot1.getItemStackLimit(itemstack1))
/*     */               {
/* 179 */                 itemstack1.stackSize = slot1.getItemStackLimit(itemstack1);
/*     */               }
/*     */               
/* 182 */               j -= itemstack1.stackSize - k;
/* 183 */               slot1.putStack(itemstack1);
/*     */             } 
/*     */           } 
/*     */           
/* 187 */           itemstack3.stackSize = j;
/*     */           
/* 189 */           if (itemstack3.stackSize <= 0)
/*     */           {
/* 191 */             itemstack3 = null;
/*     */           }
/*     */           
/* 194 */           inventoryplayer.setItemStack(itemstack3);
/*     */         } 
/*     */         
/* 197 */         resetDrag();
/*     */       }
/*     */       else {
/*     */         
/* 201 */         resetDrag();
/*     */       }
/*     */     
/* 204 */     } else if (this.dragEvent != 0) {
/*     */       
/* 206 */       resetDrag();
/*     */     }
/* 208 */     else if ((mode == 0 || mode == 1) && (clickedButton == 0 || clickedButton == 1)) {
/*     */       
/* 210 */       if (slotId == -999) {
/*     */         
/* 212 */         if (inventoryplayer.getItemStack() != null) {
/*     */           
/* 214 */           if (clickedButton == 0) {
/*     */             
/* 216 */             playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
/* 217 */             inventoryplayer.setItemStack((ItemStack)null);
/*     */           } 
/*     */           
/* 220 */           if (clickedButton == 1)
/*     */           {
/* 222 */             playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);
/*     */             
/* 224 */             if ((inventoryplayer.getItemStack()).stackSize == 0)
/*     */             {
/* 226 */               inventoryplayer.setItemStack((ItemStack)null);
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/* 231 */       } else if (mode == 1) {
/*     */         
/* 233 */         if (slotId < 0)
/*     */         {
/* 235 */           return null;
/*     */         }
/*     */         
/* 238 */         Slot slot6 = this.inventorySlots.get(slotId);
/*     */         
/* 240 */         if (slot6 != null && slot6.canTakeStack(playerIn)) {
/*     */           
/* 242 */           ItemStack itemstack8 = transferStackInSlot(playerIn, slotId);
/*     */           
/* 244 */           if (itemstack8 != null)
/*     */           {
/* 246 */             Item item = itemstack8.getItem();
/* 247 */             itemstack = itemstack8.copy();
/*     */             
/* 249 */             if (slot6.getStack() != null && slot6.getStack().getItem() == item)
/*     */             {
/* 251 */               retrySlotClick(slotId, clickedButton, true, playerIn);
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/*     */       } else {
/*     */         
/* 258 */         if (slotId < 0)
/*     */         {
/* 260 */           return null;
/*     */         }
/*     */         
/* 263 */         Slot slot7 = this.inventorySlots.get(slotId);
/*     */         
/* 265 */         if (slot7 != null)
/*     */         {
/* 267 */           ItemStack itemstack9 = slot7.getStack();
/* 268 */           ItemStack itemstack10 = inventoryplayer.getItemStack();
/*     */           
/* 270 */           if (itemstack9 != null)
/*     */           {
/* 272 */             itemstack = itemstack9.copy();
/*     */           }
/*     */           
/* 275 */           if (itemstack9 == null) {
/*     */             
/* 277 */             if (itemstack10 != null && slot7.isItemValid(itemstack10))
/*     */             {
/* 279 */               int k2 = (clickedButton == 0) ? itemstack10.stackSize : 1;
/*     */               
/* 281 */               if (k2 > slot7.getItemStackLimit(itemstack10))
/*     */               {
/* 283 */                 k2 = slot7.getItemStackLimit(itemstack10);
/*     */               }
/*     */               
/* 286 */               if (itemstack10.stackSize >= k2)
/*     */               {
/* 288 */                 slot7.putStack(itemstack10.splitStack(k2));
/*     */               }
/*     */               
/* 291 */               if (itemstack10.stackSize == 0)
/*     */               {
/* 293 */                 inventoryplayer.setItemStack((ItemStack)null);
/*     */               }
/*     */             }
/*     */           
/* 297 */           } else if (slot7.canTakeStack(playerIn)) {
/*     */             
/* 299 */             if (itemstack10 == null) {
/*     */               
/* 301 */               int j2 = (clickedButton == 0) ? itemstack9.stackSize : ((itemstack9.stackSize + 1) / 2);
/* 302 */               ItemStack itemstack12 = slot7.decrStackSize(j2);
/* 303 */               inventoryplayer.setItemStack(itemstack12);
/*     */               
/* 305 */               if (itemstack9.stackSize == 0)
/*     */               {
/* 307 */                 slot7.putStack((ItemStack)null);
/*     */               }
/*     */               
/* 310 */               slot7.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
/*     */             }
/* 312 */             else if (slot7.isItemValid(itemstack10)) {
/*     */               
/* 314 */               if (itemstack9.getItem() == itemstack10.getItem() && itemstack9.getMetadata() == itemstack10.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack9, itemstack10))
/*     */               {
/* 316 */                 int i2 = (clickedButton == 0) ? itemstack10.stackSize : 1;
/*     */                 
/* 318 */                 if (i2 > slot7.getItemStackLimit(itemstack10) - itemstack9.stackSize)
/*     */                 {
/* 320 */                   i2 = slot7.getItemStackLimit(itemstack10) - itemstack9.stackSize;
/*     */                 }
/*     */                 
/* 323 */                 if (i2 > itemstack10.getMaxStackSize() - itemstack9.stackSize)
/*     */                 {
/* 325 */                   i2 = itemstack10.getMaxStackSize() - itemstack9.stackSize;
/*     */                 }
/*     */                 
/* 328 */                 itemstack10.splitStack(i2);
/*     */                 
/* 330 */                 if (itemstack10.stackSize == 0)
/*     */                 {
/* 332 */                   inventoryplayer.setItemStack((ItemStack)null);
/*     */                 }
/*     */                 
/* 335 */                 itemstack9.stackSize += i2;
/*     */               }
/* 337 */               else if (itemstack10.stackSize <= slot7.getItemStackLimit(itemstack10))
/*     */               {
/* 339 */                 slot7.putStack(itemstack10);
/* 340 */                 inventoryplayer.setItemStack(itemstack9);
/*     */               }
/*     */             
/* 343 */             } else if (itemstack9.getItem() == itemstack10.getItem() && itemstack10.getMaxStackSize() > 1 && (!itemstack9.getHasSubtypes() || itemstack9.getMetadata() == itemstack10.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack9, itemstack10)) {
/*     */               
/* 345 */               int l1 = itemstack9.stackSize;
/*     */               
/* 347 */               if (l1 > 0 && l1 + itemstack10.stackSize <= itemstack10.getMaxStackSize()) {
/*     */                 
/* 349 */                 itemstack10.stackSize += l1;
/* 350 */                 itemstack9 = slot7.decrStackSize(l1);
/*     */                 
/* 352 */                 if (itemstack9.stackSize == 0)
/*     */                 {
/* 354 */                   slot7.putStack((ItemStack)null);
/*     */                 }
/*     */                 
/* 357 */                 slot7.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 362 */           slot7.onSlotChanged();
/*     */         }
/*     */       
/*     */       } 
/* 366 */     } else if (mode == 2 && clickedButton >= 0 && clickedButton < 9) {
/*     */       
/* 368 */       Slot slot5 = this.inventorySlots.get(slotId);
/*     */       
/* 370 */       if (slot5.canTakeStack(playerIn)) {
/*     */         int i;
/* 372 */         ItemStack itemstack7 = inventoryplayer.getStackInSlot(clickedButton);
/* 373 */         boolean flag = (itemstack7 == null || (slot5.inventory == inventoryplayer && slot5.isItemValid(itemstack7)));
/* 374 */         int k1 = -1;
/*     */         
/* 376 */         if (!flag) {
/*     */           
/* 378 */           k1 = inventoryplayer.getFirstEmptyStack();
/* 379 */           i = flag | ((k1 > -1) ? 1 : 0);
/*     */         } 
/*     */         
/* 382 */         if (slot5.getHasStack() && i != 0) {
/*     */           
/* 384 */           ItemStack itemstack11 = slot5.getStack();
/* 385 */           inventoryplayer.setInventorySlotContents(clickedButton, itemstack11.copy());
/*     */           
/* 387 */           if ((slot5.inventory != inventoryplayer || !slot5.isItemValid(itemstack7)) && itemstack7 != null) {
/*     */             
/* 389 */             if (k1 > -1)
/*     */             {
/* 391 */               inventoryplayer.addItemStackToInventory(itemstack7);
/* 392 */               slot5.decrStackSize(itemstack11.stackSize);
/* 393 */               slot5.putStack((ItemStack)null);
/* 394 */               slot5.onPickupFromSlot(playerIn, itemstack11);
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 399 */             slot5.decrStackSize(itemstack11.stackSize);
/* 400 */             slot5.putStack(itemstack7);
/* 401 */             slot5.onPickupFromSlot(playerIn, itemstack11);
/*     */           }
/*     */         
/* 404 */         } else if (!slot5.getHasStack() && itemstack7 != null && slot5.isItemValid(itemstack7)) {
/*     */           
/* 406 */           inventoryplayer.setInventorySlotContents(clickedButton, (ItemStack)null);
/* 407 */           slot5.putStack(itemstack7);
/*     */         }
/*     */       
/*     */       } 
/* 411 */     } else if (mode == 3 && playerIn.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotId >= 0) {
/*     */       
/* 413 */       Slot slot4 = this.inventorySlots.get(slotId);
/*     */       
/* 415 */       if (slot4 != null && slot4.getHasStack())
/*     */       {
/* 417 */         ItemStack itemstack6 = slot4.getStack().copy();
/* 418 */         itemstack6.stackSize = itemstack6.getMaxStackSize();
/* 419 */         inventoryplayer.setItemStack(itemstack6);
/*     */       }
/*     */     
/* 422 */     } else if (mode == 4 && inventoryplayer.getItemStack() == null && slotId >= 0) {
/*     */       
/* 424 */       Slot slot3 = this.inventorySlots.get(slotId);
/*     */       
/* 426 */       if (slot3 != null && slot3.getHasStack() && slot3.canTakeStack(playerIn))
/*     */       {
/* 428 */         ItemStack itemstack5 = slot3.decrStackSize((clickedButton == 0) ? 1 : (slot3.getStack()).stackSize);
/* 429 */         slot3.onPickupFromSlot(playerIn, itemstack5);
/* 430 */         playerIn.dropPlayerItemWithRandomChoice(itemstack5, true);
/*     */       }
/*     */     
/* 433 */     } else if (mode == 6 && slotId >= 0) {
/*     */       
/* 435 */       Slot slot2 = this.inventorySlots.get(slotId);
/* 436 */       ItemStack itemstack4 = inventoryplayer.getItemStack();
/*     */       
/* 438 */       if (itemstack4 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(playerIn))) {
/*     */         
/* 440 */         int i1 = (clickedButton == 0) ? 0 : (this.inventorySlots.size() - 1);
/* 441 */         int j1 = (clickedButton == 0) ? 1 : -1;
/*     */         
/* 443 */         for (int l2 = 0; l2 < 2; l2++) {
/*     */           int i3;
/* 445 */           for (i3 = i1; i3 >= 0 && i3 < this.inventorySlots.size() && itemstack4.stackSize < itemstack4.getMaxStackSize(); i3 += j1) {
/*     */             
/* 447 */             Slot slot8 = this.inventorySlots.get(i3);
/*     */             
/* 449 */             if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack4, true) && slot8.canTakeStack(playerIn) && canMergeSlot(itemstack4, slot8) && (l2 != 0 || (slot8.getStack()).stackSize != slot8.getStack().getMaxStackSize())) {
/*     */               
/* 451 */               int l = Math.min(itemstack4.getMaxStackSize() - itemstack4.stackSize, (slot8.getStack()).stackSize);
/* 452 */               ItemStack itemstack2 = slot8.decrStackSize(l);
/* 453 */               itemstack4.stackSize += l;
/*     */               
/* 455 */               if (itemstack2.stackSize <= 0)
/*     */               {
/* 457 */                 slot8.putStack((ItemStack)null);
/*     */               }
/*     */               
/* 460 */               slot8.onPickupFromSlot(playerIn, itemstack2);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 466 */       detectAndSendChanges();
/*     */     } 
/*     */     
/* 469 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
/* 474 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn) {
/* 479 */     slotClick(slotId, clickedButton, 1, playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onContainerClosed(EntityPlayer playerIn) {
/* 484 */     InventoryPlayer inventoryplayer = playerIn.inventory;
/*     */     
/* 486 */     if (inventoryplayer.getItemStack() != null) {
/*     */       
/* 488 */       playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), false);
/* 489 */       inventoryplayer.setItemStack((ItemStack)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn) {
/* 495 */     detectAndSendChanges();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putStackInSlot(int slotID, ItemStack stack) {
/* 500 */     getSlot(slotID).putStack(stack);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putStacksInSlots(ItemStack[] p_75131_1_) {
/* 505 */     for (int i = 0; i < p_75131_1_.length; i++)
/*     */     {
/* 507 */       getSlot(i).putStack(p_75131_1_[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateProgressBar(int id, int data) {}
/*     */ 
/*     */   
/*     */   public short getNextTransactionID(InventoryPlayer p_75136_1_) {
/* 517 */     this.transactionID = (short)(this.transactionID + 1);
/* 518 */     return this.transactionID;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanCraft(EntityPlayer p_75129_1_) {
/* 523 */     return !this.playerList.contains(p_75129_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCanCraft(EntityPlayer p_75128_1_, boolean p_75128_2_) {
/* 528 */     if (p_75128_2_) {
/*     */       
/* 530 */       this.playerList.remove(p_75128_1_);
/*     */     }
/*     */     else {
/*     */       
/* 534 */       this.playerList.add(p_75128_1_);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean canInteractWith(EntityPlayer paramEntityPlayer);
/*     */   
/*     */   protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
/* 542 */     boolean flag = false;
/* 543 */     int i = startIndex;
/*     */     
/* 545 */     if (reverseDirection)
/*     */     {
/* 547 */       i = endIndex - 1;
/*     */     }
/*     */     
/* 550 */     if (stack.isStackable())
/*     */     {
/* 552 */       while (stack.stackSize > 0 && ((!reverseDirection && i < endIndex) || (reverseDirection && i >= startIndex))) {
/*     */         
/* 554 */         Slot slot = this.inventorySlots.get(i);
/* 555 */         ItemStack itemstack = slot.getStack();
/*     */         
/* 557 */         if (itemstack != null && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
/*     */           
/* 559 */           int j = itemstack.stackSize + stack.stackSize;
/*     */           
/* 561 */           if (j <= stack.getMaxStackSize()) {
/*     */             
/* 563 */             stack.stackSize = 0;
/* 564 */             itemstack.stackSize = j;
/* 565 */             slot.onSlotChanged();
/* 566 */             flag = true;
/*     */           }
/* 568 */           else if (itemstack.stackSize < stack.getMaxStackSize()) {
/*     */             
/* 570 */             stack.stackSize -= stack.getMaxStackSize() - itemstack.stackSize;
/* 571 */             itemstack.stackSize = stack.getMaxStackSize();
/* 572 */             slot.onSlotChanged();
/* 573 */             flag = true;
/*     */           } 
/*     */         } 
/*     */         
/* 577 */         if (reverseDirection) {
/*     */           
/* 579 */           i--;
/*     */           
/*     */           continue;
/*     */         } 
/* 583 */         i++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 588 */     if (stack.stackSize > 0) {
/*     */       
/* 590 */       if (reverseDirection) {
/*     */         
/* 592 */         i = endIndex - 1;
/*     */       }
/*     */       else {
/*     */         
/* 596 */         i = startIndex;
/*     */       } 
/*     */       
/* 599 */       while ((!reverseDirection && i < endIndex) || (reverseDirection && i >= startIndex)) {
/*     */         
/* 601 */         Slot slot1 = this.inventorySlots.get(i);
/* 602 */         ItemStack itemstack1 = slot1.getStack();
/*     */         
/* 604 */         if (itemstack1 == null) {
/*     */           
/* 606 */           slot1.putStack(stack.copy());
/* 607 */           slot1.onSlotChanged();
/* 608 */           stack.stackSize = 0;
/* 609 */           flag = true;
/*     */           
/*     */           break;
/*     */         } 
/* 613 */         if (reverseDirection) {
/*     */           
/* 615 */           i--;
/*     */           
/*     */           continue;
/*     */         } 
/* 619 */         i++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 624 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int extractDragMode(int p_94529_0_) {
/* 629 */     return p_94529_0_ >> 2 & 0x3;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getDragEvent(int p_94532_0_) {
/* 634 */     return p_94532_0_ & 0x3;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int func_94534_d(int p_94534_0_, int p_94534_1_) {
/* 639 */     return p_94534_0_ & 0x3 | (p_94534_1_ & 0x3) << 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isValidDragMode(int dragModeIn, EntityPlayer player) {
/* 644 */     return (dragModeIn == 0) ? true : ((dragModeIn == 1) ? true : ((dragModeIn == 2 && player.capabilities.isCreativeMode)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resetDrag() {
/* 649 */     this.dragEvent = 0;
/* 650 */     this.dragSlots.clear();
/*     */   }
/*     */   
/*     */   public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
/*     */     int i;
/* 655 */     boolean flag = (slotIn == null || !slotIn.getHasStack());
/*     */     
/* 657 */     if (slotIn != null && slotIn.getHasStack() && stack != null && stack.isItemEqual(slotIn.getStack()) && ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack))
/*     */     {
/* 659 */       i = flag | (((slotIn.getStack()).stackSize + (stackSizeMatters ? 0 : stack.stackSize) <= stack.getMaxStackSize()) ? 1 : 0);
/*     */     }
/*     */     
/* 662 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void computeStackSize(Set<Slot> p_94525_0_, int p_94525_1_, ItemStack p_94525_2_, int p_94525_3_) {
/* 667 */     switch (p_94525_1_) {
/*     */       
/*     */       case 0:
/* 670 */         p_94525_2_.stackSize = MathHelper.floor_float(p_94525_2_.stackSize / p_94525_0_.size());
/*     */         break;
/*     */       
/*     */       case 1:
/* 674 */         p_94525_2_.stackSize = 1;
/*     */         break;
/*     */       
/*     */       case 2:
/* 678 */         p_94525_2_.stackSize = p_94525_2_.getItem().getItemStackLimit();
/*     */         break;
/*     */     } 
/* 681 */     p_94525_2_.stackSize += p_94525_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canDragIntoSlot(Slot p_94531_1_) {
/* 686 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int calcRedstone(TileEntity te) {
/* 691 */     return (te instanceof IInventory) ? calcRedstoneFromInventory((IInventory)te) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int calcRedstoneFromInventory(IInventory inv) {
/* 696 */     if (inv == null)
/*     */     {
/* 698 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 702 */     int i = 0;
/* 703 */     float f = 0.0F;
/*     */     
/* 705 */     for (int j = 0; j < inv.getSizeInventory(); j++) {
/*     */       
/* 707 */       ItemStack itemstack = inv.getStackInSlot(j);
/*     */       
/* 709 */       if (itemstack != null) {
/*     */         
/* 711 */         f += itemstack.stackSize / Math.min(inv.getInventoryStackLimit(), itemstack.getMaxStackSize());
/* 712 */         i++;
/*     */       } 
/*     */     } 
/*     */     
/* 716 */     f /= inv.getSizeInventory();
/* 717 */     return MathHelper.floor_float(f * 14.0F) + ((i > 0) ? 1 : 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\Container.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */