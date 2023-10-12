/*     */ package net.minecraft.entity.player;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ReportedException;
/*     */ 
/*     */ public class InventoryPlayer implements IInventory {
/*  21 */   public ItemStack[] mainInventory = new ItemStack[36];
/*  22 */   public ItemStack[] armorInventory = new ItemStack[4];
/*     */   
/*     */   public int currentItem;
/*     */   public EntityPlayer player;
/*     */   private ItemStack itemStack;
/*     */   public boolean inventoryChanged;
/*     */   
/*     */   public InventoryPlayer(EntityPlayer playerIn) {
/*  30 */     this.player = playerIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCurrentItem() {
/*  35 */     return (this.currentItem < 9 && this.currentItem >= 0) ? this.mainInventory[this.currentItem] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getHotbarSize() {
/*  40 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getInventorySlotContainItem(Item itemIn) {
/*  45 */     for (int i = 0; i < this.mainInventory.length; i++) {
/*     */       
/*  47 */       if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemIn)
/*     */       {
/*  49 */         return i;
/*     */       }
/*     */     } 
/*     */     
/*  53 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getInventorySlotContainItemAndDamage(Item itemIn, int metadataIn) {
/*  58 */     for (int i = 0; i < this.mainInventory.length; i++) {
/*     */       
/*  60 */       if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemIn && this.mainInventory[i].getMetadata() == metadataIn)
/*     */       {
/*  62 */         return i;
/*     */       }
/*     */     } 
/*     */     
/*  66 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private int storeItemStack(ItemStack itemStackIn) {
/*  71 */     for (int i = 0; i < this.mainInventory.length; i++) {
/*     */       
/*  73 */       if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemStackIn.getItem() && this.mainInventory[i].isStackable() && (this.mainInventory[i]).stackSize < this.mainInventory[i].getMaxStackSize() && (this.mainInventory[i]).stackSize < getInventoryStackLimit() && (!this.mainInventory[i].getHasSubtypes() || this.mainInventory[i].getMetadata() == itemStackIn.getMetadata()) && ItemStack.areItemStackTagsEqual(this.mainInventory[i], itemStackIn))
/*     */       {
/*  75 */         return i;
/*     */       }
/*     */     } 
/*     */     
/*  79 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFirstEmptyStack() {
/*  84 */     for (int i = 0; i < this.mainInventory.length; i++) {
/*     */       
/*  86 */       if (this.mainInventory[i] == null)
/*     */       {
/*  88 */         return i;
/*     */       }
/*     */     } 
/*     */     
/*  92 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentItem(Item itemIn, int metadataIn, boolean isMetaSpecific, boolean p_146030_4_) {
/*  97 */     ItemStack itemstack = getCurrentItem();
/*  98 */     int i = isMetaSpecific ? getInventorySlotContainItemAndDamage(itemIn, metadataIn) : getInventorySlotContainItem(itemIn);
/*     */     
/* 100 */     if (i >= 0 && i < 9) {
/*     */       
/* 102 */       this.currentItem = i;
/*     */     }
/* 104 */     else if (p_146030_4_ && itemIn != null) {
/*     */       
/* 106 */       int j = getFirstEmptyStack();
/*     */       
/* 108 */       if (j >= 0 && j < 9)
/*     */       {
/* 110 */         this.currentItem = j;
/*     */       }
/*     */       
/* 113 */       if (itemstack == null || !itemstack.isItemEnchantable() || getInventorySlotContainItemAndDamage(itemstack.getItem(), itemstack.getItemDamage()) != this.currentItem) {
/*     */         
/* 115 */         int l, k = getInventorySlotContainItemAndDamage(itemIn, metadataIn);
/*     */ 
/*     */         
/* 118 */         if (k >= 0) {
/*     */           
/* 120 */           l = (this.mainInventory[k]).stackSize;
/* 121 */           this.mainInventory[k] = this.mainInventory[this.currentItem];
/*     */         }
/*     */         else {
/*     */           
/* 125 */           l = 1;
/*     */         } 
/*     */         
/* 128 */         this.mainInventory[this.currentItem] = new ItemStack(itemIn, l, metadataIn);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void changeCurrentItem(int direction) {
/* 135 */     if (direction > 0)
/*     */     {
/* 137 */       direction = 1;
/*     */     }
/*     */     
/* 140 */     if (direction < 0)
/*     */     {
/* 142 */       direction = -1;
/*     */     }
/*     */     
/* 145 */     for (this.currentItem -= direction; this.currentItem < 0; this.currentItem += 9);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     while (this.currentItem >= 9)
/*     */     {
/* 152 */       this.currentItem -= 9;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int clearMatchingItems(Item itemIn, int metadataIn, int removeCount, NBTTagCompound itemNBT) {
/* 158 */     int i = 0;
/*     */     
/* 160 */     for (int j = 0; j < this.mainInventory.length; j++) {
/*     */       
/* 162 */       ItemStack itemstack = this.mainInventory[j];
/*     */       
/* 164 */       if (itemstack != null && (itemIn == null || itemstack.getItem() == itemIn) && (metadataIn <= -1 || itemstack.getMetadata() == metadataIn) && (itemNBT == null || NBTUtil.func_181123_a((NBTBase)itemNBT, (NBTBase)itemstack.getTagCompound(), true))) {
/*     */         
/* 166 */         int k = (removeCount <= 0) ? itemstack.stackSize : Math.min(removeCount - i, itemstack.stackSize);
/* 167 */         i += k;
/*     */         
/* 169 */         if (removeCount != 0) {
/*     */           
/* 171 */           (this.mainInventory[j]).stackSize -= k;
/*     */           
/* 173 */           if ((this.mainInventory[j]).stackSize == 0)
/*     */           {
/* 175 */             this.mainInventory[j] = null;
/*     */           }
/*     */           
/* 178 */           if (removeCount > 0 && i >= removeCount)
/*     */           {
/* 180 */             return i;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 186 */     for (int l = 0; l < this.armorInventory.length; l++) {
/*     */       
/* 188 */       ItemStack itemstack1 = this.armorInventory[l];
/*     */       
/* 190 */       if (itemstack1 != null && (itemIn == null || itemstack1.getItem() == itemIn) && (metadataIn <= -1 || itemstack1.getMetadata() == metadataIn) && (itemNBT == null || NBTUtil.func_181123_a((NBTBase)itemNBT, (NBTBase)itemstack1.getTagCompound(), false))) {
/*     */         
/* 192 */         int j1 = (removeCount <= 0) ? itemstack1.stackSize : Math.min(removeCount - i, itemstack1.stackSize);
/* 193 */         i += j1;
/*     */         
/* 195 */         if (removeCount != 0) {
/*     */           
/* 197 */           (this.armorInventory[l]).stackSize -= j1;
/*     */           
/* 199 */           if ((this.armorInventory[l]).stackSize == 0)
/*     */           {
/* 201 */             this.armorInventory[l] = null;
/*     */           }
/*     */           
/* 204 */           if (removeCount > 0 && i >= removeCount)
/*     */           {
/* 206 */             return i;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     if (this.itemStack != null) {
/*     */       
/* 214 */       if (itemIn != null && this.itemStack.getItem() != itemIn)
/*     */       {
/* 216 */         return i;
/*     */       }
/*     */       
/* 219 */       if (metadataIn > -1 && this.itemStack.getMetadata() != metadataIn)
/*     */       {
/* 221 */         return i;
/*     */       }
/*     */       
/* 224 */       if (itemNBT != null && !NBTUtil.func_181123_a((NBTBase)itemNBT, (NBTBase)this.itemStack.getTagCompound(), false))
/*     */       {
/* 226 */         return i;
/*     */       }
/*     */       
/* 229 */       int i1 = (removeCount <= 0) ? this.itemStack.stackSize : Math.min(removeCount - i, this.itemStack.stackSize);
/* 230 */       i += i1;
/*     */       
/* 232 */       if (removeCount != 0) {
/*     */         
/* 234 */         this.itemStack.stackSize -= i1;
/*     */         
/* 236 */         if (this.itemStack.stackSize == 0)
/*     */         {
/* 238 */           this.itemStack = null;
/*     */         }
/*     */         
/* 241 */         if (removeCount > 0 && i >= removeCount)
/*     */         {
/* 243 */           return i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   private int storePartialItemStack(ItemStack itemStackIn) {
/* 253 */     Item item = itemStackIn.getItem();
/* 254 */     int i = itemStackIn.stackSize;
/* 255 */     int j = storeItemStack(itemStackIn);
/*     */     
/* 257 */     if (j < 0)
/*     */     {
/* 259 */       j = getFirstEmptyStack();
/*     */     }
/*     */     
/* 262 */     if (j < 0)
/*     */     {
/* 264 */       return i;
/*     */     }
/*     */ 
/*     */     
/* 268 */     if (this.mainInventory[j] == null) {
/*     */       
/* 270 */       this.mainInventory[j] = new ItemStack(item, 0, itemStackIn.getMetadata());
/*     */       
/* 272 */       if (itemStackIn.hasTagCompound())
/*     */       {
/* 274 */         this.mainInventory[j].setTagCompound((NBTTagCompound)itemStackIn.getTagCompound().copy());
/*     */       }
/*     */     } 
/*     */     
/* 278 */     int k = i;
/*     */     
/* 280 */     if (i > this.mainInventory[j].getMaxStackSize() - (this.mainInventory[j]).stackSize)
/*     */     {
/* 282 */       k = this.mainInventory[j].getMaxStackSize() - (this.mainInventory[j]).stackSize;
/*     */     }
/*     */     
/* 285 */     if (k > getInventoryStackLimit() - (this.mainInventory[j]).stackSize)
/*     */     {
/* 287 */       k = getInventoryStackLimit() - (this.mainInventory[j]).stackSize;
/*     */     }
/*     */     
/* 290 */     if (k == 0)
/*     */     {
/* 292 */       return i;
/*     */     }
/*     */ 
/*     */     
/* 296 */     i -= k;
/* 297 */     (this.mainInventory[j]).stackSize += k;
/* 298 */     (this.mainInventory[j]).animationsToGo = 5;
/* 299 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decrementAnimations() {
/* 306 */     for (int i = 0; i < this.mainInventory.length; i++) {
/*     */       
/* 308 */       if (this.mainInventory[i] != null)
/*     */       {
/* 310 */         this.mainInventory[i].updateAnimation(this.player.worldObj, (Entity)this.player, i, (this.currentItem == i));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean consumeInventoryItem(Item itemIn) {
/* 317 */     int i = getInventorySlotContainItem(itemIn);
/*     */     
/* 319 */     if (i < 0)
/*     */     {
/* 321 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 325 */     if (--(this.mainInventory[i]).stackSize <= 0)
/*     */     {
/* 327 */       this.mainInventory[i] = null;
/*     */     }
/*     */     
/* 330 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasItem(Item itemIn) {
/* 336 */     int i = getInventorySlotContainItem(itemIn);
/* 337 */     return (i >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addItemStackToInventory(final ItemStack itemStackIn) {
/* 342 */     if (itemStackIn != null && itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
/*     */       try {
/*     */         int i;
/*     */         
/* 346 */         if (itemStackIn.isItemDamaged()) {
/*     */           
/* 348 */           int j = getFirstEmptyStack();
/*     */           
/* 350 */           if (j >= 0) {
/*     */             
/* 352 */             this.mainInventory[j] = ItemStack.copyItemStack(itemStackIn);
/* 353 */             (this.mainInventory[j]).animationsToGo = 5;
/* 354 */             itemStackIn.stackSize = 0;
/* 355 */             return true;
/*     */           } 
/* 357 */           if (this.player.capabilities.isCreativeMode) {
/*     */             
/* 359 */             itemStackIn.stackSize = 0;
/* 360 */             return true;
/*     */           } 
/*     */ 
/*     */           
/* 364 */           return false;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         do {
/* 373 */           i = itemStackIn.stackSize;
/* 374 */           itemStackIn.stackSize = storePartialItemStack(itemStackIn);
/*     */         }
/* 376 */         while (itemStackIn.stackSize > 0 && itemStackIn.stackSize < i);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 382 */         if (itemStackIn.stackSize == i && this.player.capabilities.isCreativeMode) {
/*     */           
/* 384 */           itemStackIn.stackSize = 0;
/* 385 */           return true;
/*     */         } 
/*     */ 
/*     */         
/* 389 */         return (itemStackIn.stackSize < i);
/*     */ 
/*     */       
/*     */       }
/* 393 */       catch (Throwable throwable) {
/*     */         
/* 395 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
/* 396 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
/* 397 */         crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(itemStackIn.getItem())));
/* 398 */         crashreportcategory.addCrashSection("Item data", Integer.valueOf(itemStackIn.getMetadata()));
/* 399 */         crashreportcategory.addCrashSectionCallable("Item name", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/* 403 */                 return itemStackIn.getDisplayName();
/*     */               }
/*     */             });
/* 406 */         throw new ReportedException(crashreport);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 411 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/* 417 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/* 419 */     if (index >= this.mainInventory.length) {
/*     */       
/* 421 */       aitemstack = this.armorInventory;
/* 422 */       index -= this.mainInventory.length;
/*     */     } 
/*     */     
/* 425 */     if (aitemstack[index] != null) {
/*     */       
/* 427 */       if ((aitemstack[index]).stackSize <= count) {
/*     */         
/* 429 */         ItemStack itemstack1 = aitemstack[index];
/* 430 */         aitemstack[index] = null;
/* 431 */         return itemstack1;
/*     */       } 
/*     */ 
/*     */       
/* 435 */       ItemStack itemstack = aitemstack[index].splitStack(count);
/*     */       
/* 437 */       if ((aitemstack[index]).stackSize == 0)
/*     */       {
/* 439 */         aitemstack[index] = null;
/*     */       }
/*     */       
/* 442 */       return itemstack;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 447 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/* 453 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/* 455 */     if (index >= this.mainInventory.length) {
/*     */       
/* 457 */       aitemstack = this.armorInventory;
/* 458 */       index -= this.mainInventory.length;
/*     */     } 
/*     */     
/* 461 */     if (aitemstack[index] != null) {
/*     */       
/* 463 */       ItemStack itemstack = aitemstack[index];
/* 464 */       aitemstack[index] = null;
/* 465 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/* 469 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/* 475 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/* 477 */     if (index >= aitemstack.length) {
/*     */       
/* 479 */       index -= aitemstack.length;
/* 480 */       aitemstack = this.armorInventory;
/*     */     } 
/*     */     
/* 483 */     aitemstack[index] = stack;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getStrVsBlock(Block blockIn) {
/* 488 */     float f = 1.0F;
/*     */     
/* 490 */     if (this.mainInventory[this.currentItem] != null)
/*     */     {
/* 492 */       f *= this.mainInventory[this.currentItem].getStrVsBlock(blockIn);
/*     */     }
/*     */     
/* 495 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagList writeToNBT(NBTTagList nbtTagListIn) {
/* 500 */     for (int i = 0; i < this.mainInventory.length; i++) {
/*     */       
/* 502 */       if (this.mainInventory[i] != null) {
/*     */         
/* 504 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 505 */         nbttagcompound.setByte("Slot", (byte)i);
/* 506 */         this.mainInventory[i].writeToNBT(nbttagcompound);
/* 507 */         nbtTagListIn.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 511 */     for (int j = 0; j < this.armorInventory.length; j++) {
/*     */       
/* 513 */       if (this.armorInventory[j] != null) {
/*     */         
/* 515 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 516 */         nbttagcompound1.setByte("Slot", (byte)(j + 100));
/* 517 */         this.armorInventory[j].writeToNBT(nbttagcompound1);
/* 518 */         nbtTagListIn.appendTag((NBTBase)nbttagcompound1);
/*     */       } 
/*     */     } 
/*     */     
/* 522 */     return nbtTagListIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagList nbtTagListIn) {
/* 527 */     this.mainInventory = new ItemStack[36];
/* 528 */     this.armorInventory = new ItemStack[4];
/*     */     
/* 530 */     for (int i = 0; i < nbtTagListIn.tagCount(); i++) {
/*     */       
/* 532 */       NBTTagCompound nbttagcompound = nbtTagListIn.getCompoundTagAt(i);
/* 533 */       int j = nbttagcompound.getByte("Slot") & 0xFF;
/* 534 */       ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       
/* 536 */       if (itemstack != null) {
/*     */         
/* 538 */         if (j >= 0 && j < this.mainInventory.length)
/*     */         {
/* 540 */           this.mainInventory[j] = itemstack;
/*     */         }
/*     */         
/* 543 */         if (j >= 100 && j < this.armorInventory.length + 100)
/*     */         {
/* 545 */           this.armorInventory[j - 100] = itemstack;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/* 553 */     return this.mainInventory.length + 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/* 558 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/* 560 */     if (index >= aitemstack.length) {
/*     */       
/* 562 */       index -= aitemstack.length;
/* 563 */       aitemstack = this.armorInventory;
/*     */     } 
/*     */     
/* 566 */     return aitemstack[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 571 */     return "container.inventory";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/* 576 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getDisplayName() {
/* 581 */     return hasCustomName() ? (IChatComponent)new ChatComponentText(getName()) : (IChatComponent)new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 586 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHeldItemHarvest(Block blockIn) {
/* 591 */     if (blockIn.getMaterial().isToolNotRequired())
/*     */     {
/* 593 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 597 */     ItemStack itemstack = getStackInSlot(this.currentItem);
/* 598 */     return (itemstack != null) ? itemstack.canHarvestBlock(blockIn) : false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack armorItemInSlot(int slotIn) {
/* 604 */     return this.armorInventory[slotIn];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalArmorValue() {
/* 609 */     int i = 0;
/*     */     
/* 611 */     for (int j = 0; j < this.armorInventory.length; j++) {
/*     */       
/* 613 */       if (this.armorInventory[j] != null && this.armorInventory[j].getItem() instanceof ItemArmor) {
/*     */         
/* 615 */         int k = ((ItemArmor)this.armorInventory[j].getItem()).damageReduceAmount;
/* 616 */         i += k;
/*     */       } 
/*     */     } 
/*     */     
/* 620 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void damageArmor(float damage) {
/* 625 */     damage /= 4.0F;
/*     */     
/* 627 */     if (damage < 1.0F)
/*     */     {
/* 629 */       damage = 1.0F;
/*     */     }
/*     */     
/* 632 */     for (int i = 0; i < this.armorInventory.length; i++) {
/*     */       
/* 634 */       if (this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor) {
/*     */         
/* 636 */         this.armorInventory[i].damageItem((int)damage, this.player);
/*     */         
/* 638 */         if ((this.armorInventory[i]).stackSize == 0)
/*     */         {
/* 640 */           this.armorInventory[i] = null;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropAllItems() {
/* 648 */     for (int i = 0; i < this.mainInventory.length; i++) {
/*     */       
/* 650 */       if (this.mainInventory[i] != null) {
/*     */         
/* 652 */         this.player.dropItem(this.mainInventory[i], true, false);
/* 653 */         this.mainInventory[i] = null;
/*     */       } 
/*     */     } 
/*     */     
/* 657 */     for (int j = 0; j < this.armorInventory.length; j++) {
/*     */       
/* 659 */       if (this.armorInventory[j] != null) {
/*     */         
/* 661 */         this.player.dropItem(this.armorInventory[j], true, false);
/* 662 */         this.armorInventory[j] = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void markDirty() {
/* 669 */     this.inventoryChanged = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemStack(ItemStack itemStackIn) {
/* 674 */     this.itemStack = itemStackIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemStack() {
/* 679 */     return this.itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 684 */     return this.player.isDead ? false : ((player.getDistanceSqToEntity((Entity)this.player) <= 64.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasItemStack(ItemStack itemStackIn) {
/* 689 */     for (int i = 0; i < this.armorInventory.length; i++) {
/*     */       
/* 691 */       if (this.armorInventory[i] != null && this.armorInventory[i].isItemEqual(itemStackIn))
/*     */       {
/* 693 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 697 */     for (int j = 0; j < this.mainInventory.length; j++) {
/*     */       
/* 699 */       if (this.mainInventory[j] != null && this.mainInventory[j].isItemEqual(itemStackIn))
/*     */       {
/* 701 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 705 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void openInventory(EntityPlayer player) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeInventory(EntityPlayer player) {}
/*     */ 
/*     */   
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack) {
/* 718 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyInventory(InventoryPlayer playerInventory) {
/* 723 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/* 725 */       this.mainInventory[i] = ItemStack.copyItemStack(playerInventory.mainInventory[i]);
/*     */     }
/*     */     
/* 728 */     for (int j = 0; j < this.armorInventory.length; j++)
/*     */     {
/* 730 */       this.armorInventory[j] = ItemStack.copyItemStack(playerInventory.armorInventory[j]);
/*     */     }
/*     */     
/* 733 */     this.currentItem = playerInventory.currentItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 738 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 747 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 752 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/* 754 */       this.mainInventory[i] = null;
/*     */     }
/*     */     
/* 757 */     for (int j = 0; j < this.armorInventory.length; j++)
/*     */     {
/* 759 */       this.armorInventory[j] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\player\InventoryPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */