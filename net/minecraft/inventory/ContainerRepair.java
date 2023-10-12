/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.BlockAnvil;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ContainerRepair extends Container {
/*  22 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   private IInventory outputSlot;
/*     */   private IInventory inputSlots;
/*     */   private World theWorld;
/*     */   private BlockPos selfPosition;
/*     */   public int maximumCost;
/*     */   private int materialCost;
/*     */   private String repairedItemName;
/*     */   private final EntityPlayer thePlayer;
/*     */   
/*     */   public ContainerRepair(InventoryPlayer playerInventory, World worldIn, EntityPlayer player) {
/*  34 */     this(playerInventory, worldIn, BlockPos.ORIGIN, player);
/*     */   }
/*     */ 
/*     */   
/*     */   public ContainerRepair(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player) {
/*  39 */     this.outputSlot = new InventoryCraftResult();
/*  40 */     this.inputSlots = new InventoryBasic("Repair", true, 2)
/*     */       {
/*     */         public void markDirty()
/*     */         {
/*  44 */           super.markDirty();
/*  45 */           ContainerRepair.this.onCraftMatrixChanged(this);
/*     */         }
/*     */       };
/*  48 */     this.selfPosition = blockPosIn;
/*  49 */     this.theWorld = worldIn;
/*  50 */     this.thePlayer = player;
/*  51 */     addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
/*  52 */     addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
/*  53 */     addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47)
/*     */         {
/*     */           public boolean isItemValid(ItemStack stack)
/*     */           {
/*  57 */             return false;
/*     */           }
/*     */           
/*     */           public boolean canTakeStack(EntityPlayer playerIn) {
/*  61 */             return ((playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && getHasStack());
/*     */           }
/*     */           
/*     */           public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
/*  65 */             if (!playerIn.capabilities.isCreativeMode)
/*     */             {
/*  67 */               playerIn.addExperienceLevel(-ContainerRepair.this.maximumCost);
/*     */             }
/*     */             
/*  70 */             ContainerRepair.this.inputSlots.setInventorySlotContents(0, (ItemStack)null);
/*     */             
/*  72 */             if (ContainerRepair.this.materialCost > 0) {
/*     */               
/*  74 */               ItemStack itemstack = ContainerRepair.this.inputSlots.getStackInSlot(1);
/*     */               
/*  76 */               if (itemstack != null && itemstack.stackSize > ContainerRepair.this.materialCost)
/*     */               {
/*  78 */                 itemstack.stackSize -= ContainerRepair.this.materialCost;
/*  79 */                 ContainerRepair.this.inputSlots.setInventorySlotContents(1, itemstack);
/*     */               }
/*     */               else
/*     */               {
/*  83 */                 ContainerRepair.this.inputSlots.setInventorySlotContents(1, (ItemStack)null);
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/*  88 */               ContainerRepair.this.inputSlots.setInventorySlotContents(1, (ItemStack)null);
/*     */             } 
/*     */             
/*  91 */             ContainerRepair.this.maximumCost = 0;
/*  92 */             IBlockState iblockstate = worldIn.getBlockState(blockPosIn);
/*     */             
/*  94 */             if (!playerIn.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == Blocks.anvil && playerIn.getRNG().nextFloat() < 0.12F) {
/*     */               
/*  96 */               int l = ((Integer)iblockstate.getValue((IProperty)BlockAnvil.DAMAGE)).intValue();
/*  97 */               l++;
/*     */               
/*  99 */               if (l > 2)
/*     */               {
/* 101 */                 worldIn.setBlockToAir(blockPosIn);
/* 102 */                 worldIn.playAuxSFX(1020, blockPosIn, 0);
/*     */               }
/*     */               else
/*     */               {
/* 106 */                 worldIn.setBlockState(blockPosIn, iblockstate.withProperty((IProperty)BlockAnvil.DAMAGE, Integer.valueOf(l)), 2);
/* 107 */                 worldIn.playAuxSFX(1021, blockPosIn, 0);
/*     */               }
/*     */             
/* 110 */             } else if (!worldIn.isRemote) {
/*     */               
/* 112 */               worldIn.playAuxSFX(1021, blockPosIn, 0);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 117 */     for (int i = 0; i < 3; i++) {
/*     */       
/* 119 */       for (int j = 0; j < 9; j++)
/*     */       {
/* 121 */         addSlotToContainer(new Slot((IInventory)playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
/*     */       }
/*     */     } 
/*     */     
/* 125 */     for (int k = 0; k < 9; k++)
/*     */     {
/* 127 */       addSlotToContainer(new Slot((IInventory)playerInventory, k, 8 + k * 18, 142));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn) {
/* 133 */     super.onCraftMatrixChanged(inventoryIn);
/*     */     
/* 135 */     if (inventoryIn == this.inputSlots)
/*     */     {
/* 137 */       updateRepairOutput();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRepairOutput() {
/* 143 */     int i = 0;
/* 144 */     int j = 1;
/* 145 */     int k = 1;
/* 146 */     int l = 1;
/* 147 */     int i1 = 2;
/* 148 */     int j1 = 1;
/* 149 */     int k1 = 1;
/* 150 */     ItemStack itemstack = this.inputSlots.getStackInSlot(0);
/* 151 */     this.maximumCost = 1;
/* 152 */     int l1 = 0;
/* 153 */     int i2 = 0;
/* 154 */     int j2 = 0;
/*     */     
/* 156 */     if (itemstack == null) {
/*     */       
/* 158 */       this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
/* 159 */       this.maximumCost = 0;
/*     */     }
/*     */     else {
/*     */       
/* 163 */       ItemStack itemstack1 = itemstack.copy();
/* 164 */       ItemStack itemstack2 = this.inputSlots.getStackInSlot(1);
/* 165 */       Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
/* 166 */       boolean flag = false;
/* 167 */       i2 = i2 + itemstack.getRepairCost() + ((itemstack2 == null) ? 0 : itemstack2.getRepairCost());
/* 168 */       this.materialCost = 0;
/*     */       
/* 170 */       if (itemstack2 != null) {
/*     */         
/* 172 */         flag = (itemstack2.getItem() == Items.enchanted_book && Items.enchanted_book.getEnchantments(itemstack2).tagCount() > 0);
/*     */         
/* 174 */         if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
/*     */           
/* 176 */           int j4 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
/*     */           
/* 178 */           if (j4 <= 0) {
/*     */             
/* 180 */             this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
/* 181 */             this.maximumCost = 0;
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/*     */           int l4;
/* 187 */           for (l4 = 0; j4 > 0 && l4 < itemstack2.stackSize; l4++) {
/*     */             
/* 189 */             int j5 = itemstack1.getItemDamage() - j4;
/* 190 */             itemstack1.setItemDamage(j5);
/* 191 */             l1++;
/* 192 */             j4 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
/*     */           } 
/*     */           
/* 195 */           this.materialCost = l4;
/*     */         }
/*     */         else {
/*     */           
/* 199 */           if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
/*     */             
/* 201 */             this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
/* 202 */             this.maximumCost = 0;
/*     */             
/*     */             return;
/*     */           } 
/* 206 */           if (itemstack1.isItemStackDamageable() && !flag) {
/*     */             
/* 208 */             int k2 = itemstack.getMaxDamage() - itemstack.getItemDamage();
/* 209 */             int l2 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
/* 210 */             int i3 = l2 + itemstack1.getMaxDamage() * 12 / 100;
/* 211 */             int j3 = k2 + i3;
/* 212 */             int k3 = itemstack1.getMaxDamage() - j3;
/*     */             
/* 214 */             if (k3 < 0)
/*     */             {
/* 216 */               k3 = 0;
/*     */             }
/*     */             
/* 219 */             if (k3 < itemstack1.getMetadata()) {
/*     */               
/* 221 */               itemstack1.setItemDamage(k3);
/* 222 */               l1 += 2;
/*     */             } 
/*     */           } 
/*     */           
/* 226 */           Map<Integer, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
/* 227 */           Iterator<Integer> iterator1 = map1.keySet().iterator();
/*     */           
/* 229 */           while (iterator1.hasNext()) {
/*     */             
/* 231 */             int i5 = ((Integer)iterator1.next()).intValue();
/* 232 */             Enchantment enchantment = Enchantment.getEnchantmentById(i5);
/*     */             
/* 234 */             if (enchantment != null) {
/*     */               
/* 236 */               int i6, k5 = map.containsKey(Integer.valueOf(i5)) ? ((Integer)map.get(Integer.valueOf(i5))).intValue() : 0;
/* 237 */               int l3 = ((Integer)map1.get(Integer.valueOf(i5))).intValue();
/*     */ 
/*     */               
/* 240 */               if (k5 == l3) {
/*     */ 
/*     */                 
/* 243 */                 i6 = ++l3;
/*     */               }
/*     */               else {
/*     */                 
/* 247 */                 i6 = Math.max(l3, k5);
/*     */               } 
/*     */               
/* 250 */               l3 = i6;
/* 251 */               boolean flag1 = enchantment.canApply(itemstack);
/*     */               
/* 253 */               if (this.thePlayer.capabilities.isCreativeMode || itemstack.getItem() == Items.enchanted_book)
/*     */               {
/* 255 */                 flag1 = true;
/*     */               }
/*     */               
/* 258 */               Iterator<Integer> iterator = map.keySet().iterator();
/*     */               
/* 260 */               while (iterator.hasNext()) {
/*     */                 
/* 262 */                 int i4 = ((Integer)iterator.next()).intValue();
/*     */                 
/* 264 */                 if (i4 != i5 && !enchantment.canApplyTogether(Enchantment.getEnchantmentById(i4))) {
/*     */                   
/* 266 */                   flag1 = false;
/* 267 */                   l1++;
/*     */                 } 
/*     */               } 
/*     */               
/* 271 */               if (flag1) {
/*     */                 
/* 273 */                 if (l3 > enchantment.getMaxLevel())
/*     */                 {
/* 275 */                   l3 = enchantment.getMaxLevel();
/*     */                 }
/*     */                 
/* 278 */                 map.put(Integer.valueOf(i5), Integer.valueOf(l3));
/* 279 */                 int l5 = 0;
/*     */                 
/* 281 */                 switch (enchantment.getWeight()) {
/*     */                   
/*     */                   case 1:
/* 284 */                     l5 = 8;
/*     */                     break;
/*     */                   
/*     */                   case 2:
/* 288 */                     l5 = 4;
/*     */                     break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   case 5:
/* 300 */                     l5 = 2;
/*     */                     break;
/*     */                   
/*     */                   case 10:
/* 304 */                     l5 = 1;
/*     */                     break;
/*     */                 } 
/* 307 */                 if (flag)
/*     */                 {
/* 309 */                   l5 = Math.max(1, l5 / 2);
/*     */                 }
/*     */                 
/* 312 */                 l1 += l5 * l3;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 319 */       if (StringUtils.isBlank(this.repairedItemName)) {
/*     */         
/* 321 */         if (itemstack.hasDisplayName())
/*     */         {
/* 323 */           j2 = 1;
/* 324 */           l1 += j2;
/* 325 */           itemstack1.clearCustomName();
/*     */         }
/*     */       
/* 328 */       } else if (!this.repairedItemName.equals(itemstack.getDisplayName())) {
/*     */         
/* 330 */         j2 = 1;
/* 331 */         l1 += j2;
/* 332 */         itemstack1.setStackDisplayName(this.repairedItemName);
/*     */       } 
/*     */       
/* 335 */       this.maximumCost = i2 + l1;
/*     */       
/* 337 */       if (l1 <= 0)
/*     */       {
/* 339 */         itemstack1 = null;
/*     */       }
/*     */       
/* 342 */       if (j2 == l1 && j2 > 0 && this.maximumCost >= 40)
/*     */       {
/* 344 */         this.maximumCost = 39;
/*     */       }
/*     */       
/* 347 */       if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode)
/*     */       {
/* 349 */         itemstack1 = null;
/*     */       }
/*     */       
/* 352 */       if (itemstack1 != null) {
/*     */         
/* 354 */         int k4 = itemstack1.getRepairCost();
/*     */         
/* 356 */         if (itemstack2 != null && k4 < itemstack2.getRepairCost())
/*     */         {
/* 358 */           k4 = itemstack2.getRepairCost();
/*     */         }
/*     */         
/* 361 */         k4 = k4 * 2 + 1;
/* 362 */         itemstack1.setRepairCost(k4);
/* 363 */         EnchantmentHelper.setEnchantments(map, itemstack1);
/*     */       } 
/*     */       
/* 366 */       this.outputSlot.setInventorySlotContents(0, itemstack1);
/* 367 */       detectAndSendChanges();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener) {
/* 373 */     super.onCraftGuiOpened(listener);
/* 374 */     listener.sendProgressBarUpdate(this, 0, this.maximumCost);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateProgressBar(int id, int data) {
/* 379 */     if (id == 0)
/*     */     {
/* 381 */       this.maximumCost = data;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onContainerClosed(EntityPlayer playerIn) {
/* 387 */     super.onContainerClosed(playerIn);
/*     */     
/* 389 */     if (!this.theWorld.isRemote)
/*     */     {
/* 391 */       for (int i = 0; i < this.inputSlots.getSizeInventory(); i++) {
/*     */         
/* 393 */         ItemStack itemstack = this.inputSlots.removeStackFromSlot(i);
/*     */         
/* 395 */         if (itemstack != null)
/*     */         {
/* 397 */           playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn) {
/* 405 */     return (this.theWorld.getBlockState(this.selfPosition).getBlock() != Blocks.anvil) ? false : ((playerIn.getDistanceSq(this.selfPosition.getX() + 0.5D, this.selfPosition.getY() + 0.5D, this.selfPosition.getZ() + 0.5D) <= 64.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/* 410 */     ItemStack itemstack = null;
/* 411 */     Slot slot = this.inventorySlots.get(index);
/*     */     
/* 413 */     if (slot != null && slot.getHasStack()) {
/*     */       
/* 415 */       ItemStack itemstack1 = slot.getStack();
/* 416 */       itemstack = itemstack1.copy();
/*     */       
/* 418 */       if (index == 2) {
/*     */         
/* 420 */         if (!mergeItemStack(itemstack1, 3, 39, true))
/*     */         {
/* 422 */           return null;
/*     */         }
/*     */         
/* 425 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/* 427 */       else if (index != 0 && index != 1) {
/*     */         
/* 429 */         if (index >= 3 && index < 39 && !mergeItemStack(itemstack1, 0, 2, false))
/*     */         {
/* 431 */           return null;
/*     */         }
/*     */       }
/* 434 */       else if (!mergeItemStack(itemstack1, 3, 39, false)) {
/*     */         
/* 436 */         return null;
/*     */       } 
/*     */       
/* 439 */       if (itemstack1.stackSize == 0) {
/*     */         
/* 441 */         slot.putStack((ItemStack)null);
/*     */       }
/*     */       else {
/*     */         
/* 445 */         slot.onSlotChanged();
/*     */       } 
/*     */       
/* 448 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 450 */         return null;
/*     */       }
/*     */       
/* 453 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     } 
/*     */     
/* 456 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateItemName(String newName) {
/* 461 */     this.repairedItemName = newName;
/*     */     
/* 463 */     if (getSlot(2).getHasStack()) {
/*     */       
/* 465 */       ItemStack itemstack = getSlot(2).getStack();
/*     */       
/* 467 */       if (StringUtils.isBlank(newName)) {
/*     */         
/* 469 */         itemstack.clearCustomName();
/*     */       }
/*     */       else {
/*     */         
/* 473 */         itemstack.setStackDisplayName(this.repairedItemName);
/*     */       } 
/*     */     } 
/*     */     
/* 477 */     updateRepairOutput();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerRepair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */