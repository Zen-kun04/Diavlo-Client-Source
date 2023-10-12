/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.enchantment.EnchantmentData;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ContainerEnchantment
/*     */   extends Container {
/*     */   public IInventory tableInventory;
/*     */   private World worldPointer;
/*     */   private BlockPos position;
/*     */   private Random rand;
/*     */   public int xpSeed;
/*     */   public int[] enchantLevels;
/*     */   public int[] enchantmentIds;
/*     */   
/*     */   public ContainerEnchantment(InventoryPlayer playerInv, World worldIn) {
/*  29 */     this(playerInv, worldIn, BlockPos.ORIGIN);
/*     */   }
/*     */ 
/*     */   
/*     */   public ContainerEnchantment(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
/*  34 */     this.tableInventory = new InventoryBasic("Enchant", true, 2)
/*     */       {
/*     */         public int getInventoryStackLimit()
/*     */         {
/*  38 */           return 64;
/*     */         }
/*     */         
/*     */         public void markDirty() {
/*  42 */           super.markDirty();
/*  43 */           ContainerEnchantment.this.onCraftMatrixChanged(this);
/*     */         }
/*     */       };
/*  46 */     this.rand = new Random();
/*  47 */     this.enchantLevels = new int[3];
/*  48 */     this.enchantmentIds = new int[] { -1, -1, -1 };
/*  49 */     this.worldPointer = worldIn;
/*  50 */     this.position = pos;
/*  51 */     this.xpSeed = playerInv.player.getXPSeed();
/*  52 */     addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47)
/*     */         {
/*     */           public boolean isItemValid(ItemStack stack)
/*     */           {
/*  56 */             return true;
/*     */           }
/*     */           
/*     */           public int getSlotStackLimit() {
/*  60 */             return 1;
/*     */           }
/*     */         });
/*  63 */     addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47)
/*     */         {
/*     */           public boolean isItemValid(ItemStack stack)
/*     */           {
/*  67 */             return (stack.getItem() == Items.dye && EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.BLUE);
/*     */           }
/*     */         });
/*     */     
/*  71 */     for (int i = 0; i < 3; i++) {
/*     */       
/*  73 */       for (int j = 0; j < 9; j++)
/*     */       {
/*  75 */         addSlotToContainer(new Slot((IInventory)playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
/*     */       }
/*     */     } 
/*     */     
/*  79 */     for (int k = 0; k < 9; k++)
/*     */     {
/*  81 */       addSlotToContainer(new Slot((IInventory)playerInv, k, 8 + k * 18, 142));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener) {
/*  87 */     super.onCraftGuiOpened(listener);
/*  88 */     listener.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
/*  89 */     listener.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
/*  90 */     listener.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
/*  91 */     listener.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
/*  92 */     listener.sendProgressBarUpdate(this, 4, this.enchantmentIds[0]);
/*  93 */     listener.sendProgressBarUpdate(this, 5, this.enchantmentIds[1]);
/*  94 */     listener.sendProgressBarUpdate(this, 6, this.enchantmentIds[2]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void detectAndSendChanges() {
/*  99 */     super.detectAndSendChanges();
/*     */     
/* 101 */     for (int i = 0; i < this.crafters.size(); i++) {
/*     */       
/* 103 */       ICrafting icrafting = this.crafters.get(i);
/* 104 */       icrafting.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
/* 105 */       icrafting.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
/* 106 */       icrafting.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
/* 107 */       icrafting.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
/* 108 */       icrafting.sendProgressBarUpdate(this, 4, this.enchantmentIds[0]);
/* 109 */       icrafting.sendProgressBarUpdate(this, 5, this.enchantmentIds[1]);
/* 110 */       icrafting.sendProgressBarUpdate(this, 6, this.enchantmentIds[2]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateProgressBar(int id, int data) {
/* 116 */     if (id >= 0 && id <= 2) {
/*     */       
/* 118 */       this.enchantLevels[id] = data;
/*     */     }
/* 120 */     else if (id == 3) {
/*     */       
/* 122 */       this.xpSeed = data;
/*     */     }
/* 124 */     else if (id >= 4 && id <= 6) {
/*     */       
/* 126 */       this.enchantmentIds[id - 4] = data;
/*     */     }
/*     */     else {
/*     */       
/* 130 */       super.updateProgressBar(id, data);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn) {
/* 136 */     if (inventoryIn == this.tableInventory) {
/*     */       
/* 138 */       ItemStack itemstack = inventoryIn.getStackInSlot(0);
/*     */       
/* 140 */       if (itemstack != null && itemstack.isItemEnchantable()) {
/*     */         
/* 142 */         if (!this.worldPointer.isRemote)
/*     */         {
/* 144 */           int l = 0;
/*     */           
/* 146 */           for (int j = -1; j <= 1; j++) {
/*     */             
/* 148 */             for (int k = -1; k <= 1; k++) {
/*     */               
/* 150 */               if ((j != 0 || k != 0) && this.worldPointer.isAirBlock(this.position.add(k, 0, j)) && this.worldPointer.isAirBlock(this.position.add(k, 1, j))) {
/*     */                 
/* 152 */                 if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j * 2)).getBlock() == Blocks.bookshelf)
/*     */                 {
/* 154 */                   l++;
/*     */                 }
/*     */                 
/* 157 */                 if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j * 2)).getBlock() == Blocks.bookshelf)
/*     */                 {
/* 159 */                   l++;
/*     */                 }
/*     */                 
/* 162 */                 if (k != 0 && j != 0) {
/*     */                   
/* 164 */                   if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j)).getBlock() == Blocks.bookshelf)
/*     */                   {
/* 166 */                     l++;
/*     */                   }
/*     */                   
/* 169 */                   if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j)).getBlock() == Blocks.bookshelf)
/*     */                   {
/* 171 */                     l++;
/*     */                   }
/*     */                   
/* 174 */                   if (this.worldPointer.getBlockState(this.position.add(k, 0, j * 2)).getBlock() == Blocks.bookshelf)
/*     */                   {
/* 176 */                     l++;
/*     */                   }
/*     */                   
/* 179 */                   if (this.worldPointer.getBlockState(this.position.add(k, 1, j * 2)).getBlock() == Blocks.bookshelf)
/*     */                   {
/* 181 */                     l++;
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 188 */           this.rand.setSeed(this.xpSeed);
/*     */           
/* 190 */           for (int i1 = 0; i1 < 3; i1++) {
/*     */             
/* 192 */             this.enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i1, l, itemstack);
/* 193 */             this.enchantmentIds[i1] = -1;
/*     */             
/* 195 */             if (this.enchantLevels[i1] < i1 + 1)
/*     */             {
/* 197 */               this.enchantLevels[i1] = 0;
/*     */             }
/*     */           } 
/*     */           
/* 201 */           for (int j1 = 0; j1 < 3; j1++) {
/*     */             
/* 203 */             if (this.enchantLevels[j1] > 0) {
/*     */               
/* 205 */               List<EnchantmentData> list = func_178148_a(itemstack, j1, this.enchantLevels[j1]);
/*     */               
/* 207 */               if (list != null && !list.isEmpty()) {
/*     */                 
/* 209 */                 EnchantmentData enchantmentdata = list.get(this.rand.nextInt(list.size()));
/* 210 */                 this.enchantmentIds[j1] = enchantmentdata.enchantmentobj.effectId | enchantmentdata.enchantmentLevel << 8;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 215 */           detectAndSendChanges();
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 220 */         for (int i = 0; i < 3; i++) {
/*     */           
/* 222 */           this.enchantLevels[i] = 0;
/* 223 */           this.enchantmentIds[i] = -1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean enchantItem(EntityPlayer playerIn, int id) {
/* 231 */     ItemStack itemstack = this.tableInventory.getStackInSlot(0);
/* 232 */     ItemStack itemstack1 = this.tableInventory.getStackInSlot(1);
/* 233 */     int i = id + 1;
/*     */     
/* 235 */     if ((itemstack1 == null || itemstack1.stackSize < i) && !playerIn.capabilities.isCreativeMode)
/*     */     {
/* 237 */       return false;
/*     */     }
/* 239 */     if (this.enchantLevels[id] > 0 && itemstack != null && ((playerIn.experienceLevel >= i && playerIn.experienceLevel >= this.enchantLevels[id]) || playerIn.capabilities.isCreativeMode)) {
/*     */       
/* 241 */       if (!this.worldPointer.isRemote) {
/*     */         
/* 243 */         List<EnchantmentData> list = func_178148_a(itemstack, id, this.enchantLevels[id]);
/* 244 */         boolean flag = (itemstack.getItem() == Items.book);
/*     */         
/* 246 */         if (list != null) {
/*     */           
/* 248 */           playerIn.removeExperienceLevel(i);
/*     */           
/* 250 */           if (flag)
/*     */           {
/* 252 */             itemstack.setItem((Item)Items.enchanted_book);
/*     */           }
/*     */           
/* 255 */           for (int j = 0; j < list.size(); j++) {
/*     */             
/* 257 */             EnchantmentData enchantmentdata = list.get(j);
/*     */             
/* 259 */             if (flag) {
/*     */               
/* 261 */               Items.enchanted_book.addEnchantment(itemstack, enchantmentdata);
/*     */             }
/*     */             else {
/*     */               
/* 265 */               itemstack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
/*     */             } 
/*     */           } 
/*     */           
/* 269 */           if (!playerIn.capabilities.isCreativeMode) {
/*     */             
/* 271 */             itemstack1.stackSize -= i;
/*     */             
/* 273 */             if (itemstack1.stackSize <= 0)
/*     */             {
/* 275 */               this.tableInventory.setInventorySlotContents(1, (ItemStack)null);
/*     */             }
/*     */           } 
/*     */           
/* 279 */           playerIn.triggerAchievement(StatList.field_181739_W);
/* 280 */           this.tableInventory.markDirty();
/* 281 */           this.xpSeed = playerIn.getXPSeed();
/* 282 */           onCraftMatrixChanged(this.tableInventory);
/*     */         } 
/*     */       } 
/*     */       
/* 286 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 290 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<EnchantmentData> func_178148_a(ItemStack stack, int p_178148_2_, int p_178148_3_) {
/* 296 */     this.rand.setSeed((this.xpSeed + p_178148_2_));
/* 297 */     List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, stack, p_178148_3_);
/*     */     
/* 299 */     if (stack.getItem() == Items.book && list != null && list.size() > 1)
/*     */     {
/* 301 */       list.remove(this.rand.nextInt(list.size()));
/*     */     }
/*     */     
/* 304 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLapisAmount() {
/* 309 */     ItemStack itemstack = this.tableInventory.getStackInSlot(1);
/* 310 */     return (itemstack == null) ? 0 : itemstack.stackSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onContainerClosed(EntityPlayer playerIn) {
/* 315 */     super.onContainerClosed(playerIn);
/*     */     
/* 317 */     if (!this.worldPointer.isRemote)
/*     */     {
/* 319 */       for (int i = 0; i < this.tableInventory.getSizeInventory(); i++) {
/*     */         
/* 321 */         ItemStack itemstack = this.tableInventory.removeStackFromSlot(i);
/*     */         
/* 323 */         if (itemstack != null)
/*     */         {
/* 325 */           playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn) {
/* 333 */     return (this.worldPointer.getBlockState(this.position).getBlock() != Blocks.enchanting_table) ? false : ((playerIn.getDistanceSq(this.position.getX() + 0.5D, this.position.getY() + 0.5D, this.position.getZ() + 0.5D) <= 64.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/* 338 */     ItemStack itemstack = null;
/* 339 */     Slot slot = this.inventorySlots.get(index);
/*     */     
/* 341 */     if (slot != null && slot.getHasStack()) {
/*     */       
/* 343 */       ItemStack itemstack1 = slot.getStack();
/* 344 */       itemstack = itemstack1.copy();
/*     */       
/* 346 */       if (index == 0) {
/*     */         
/* 348 */         if (!mergeItemStack(itemstack1, 2, 38, true))
/*     */         {
/* 350 */           return null;
/*     */         }
/*     */       }
/* 353 */       else if (index == 1) {
/*     */         
/* 355 */         if (!mergeItemStack(itemstack1, 2, 38, true))
/*     */         {
/* 357 */           return null;
/*     */         }
/*     */       }
/* 360 */       else if (itemstack1.getItem() == Items.dye && EnumDyeColor.byDyeDamage(itemstack1.getMetadata()) == EnumDyeColor.BLUE) {
/*     */         
/* 362 */         if (!mergeItemStack(itemstack1, 1, 2, true))
/*     */         {
/* 364 */           return null;
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 369 */         if (((Slot)this.inventorySlots.get(0)).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(itemstack1))
/*     */         {
/* 371 */           return null;
/*     */         }
/*     */         
/* 374 */         if (itemstack1.hasTagCompound() && itemstack1.stackSize == 1) {
/*     */           
/* 376 */           ((Slot)this.inventorySlots.get(0)).putStack(itemstack1.copy());
/* 377 */           itemstack1.stackSize = 0;
/*     */         }
/* 379 */         else if (itemstack1.stackSize >= 1) {
/*     */           
/* 381 */           ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
/* 382 */           itemstack1.stackSize--;
/*     */         } 
/*     */       } 
/*     */       
/* 386 */       if (itemstack1.stackSize == 0) {
/*     */         
/* 388 */         slot.putStack((ItemStack)null);
/*     */       }
/*     */       else {
/*     */         
/* 392 */         slot.onSlotChanged();
/*     */       } 
/*     */       
/* 395 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 397 */         return null;
/*     */       }
/*     */       
/* 400 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     } 
/*     */     
/* 403 */     return itemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */