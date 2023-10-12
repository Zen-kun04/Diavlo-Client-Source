/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ 
/*     */ public class ContainerPlayer
/*     */   extends Container {
/*  14 */   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
/*  15 */   public IInventory craftResult = new InventoryCraftResult();
/*     */   
/*     */   public boolean isLocalWorld;
/*     */   private final EntityPlayer thePlayer;
/*     */   
/*     */   public ContainerPlayer(InventoryPlayer playerInventory, boolean localWorld, EntityPlayer player) {
/*  21 */     this.isLocalWorld = localWorld;
/*  22 */     this.thePlayer = player;
/*  23 */     addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 144, 36));
/*     */     
/*  25 */     for (int i = 0; i < 2; i++) {
/*     */       
/*  27 */       for (int j = 0; j < 2; j++)
/*     */       {
/*  29 */         addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 88 + j * 18, 26 + i * 18));
/*     */       }
/*     */     } 
/*     */     
/*  33 */     for (int k = 0; k < 4; k++) {
/*     */       
/*  35 */       final int k_f = k;
/*  36 */       addSlotToContainer(new Slot((IInventory)playerInventory, playerInventory.getSizeInventory() - 1 - k, 8, 8 + k * 18)
/*     */           {
/*     */             public int getSlotStackLimit()
/*     */             {
/*  40 */               return 1;
/*     */             }
/*     */             
/*     */             public boolean isItemValid(ItemStack stack) {
/*  44 */               return (stack == null) ? false : ((stack.getItem() instanceof ItemArmor) ? ((((ItemArmor)stack.getItem()).armorType == k_f)) : ((stack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && stack.getItem() != Items.skull) ? false : ((k_f == 0))));
/*     */             }
/*     */             
/*     */             public String getSlotTexture() {
/*  48 */               return ItemArmor.EMPTY_SLOT_NAMES[k_f];
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/*  53 */     for (int l = 0; l < 3; l++) {
/*     */       
/*  55 */       for (int j1 = 0; j1 < 9; j1++)
/*     */       {
/*  57 */         addSlotToContainer(new Slot((IInventory)playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
/*     */       }
/*     */     } 
/*     */     
/*  61 */     for (int i1 = 0; i1 < 9; i1++)
/*     */     {
/*  63 */       addSlotToContainer(new Slot((IInventory)playerInventory, i1, 8 + i1 * 18, 142));
/*     */     }
/*     */     
/*  66 */     onCraftMatrixChanged(this.craftMatrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn) {
/*  71 */     this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onContainerClosed(EntityPlayer playerIn) {
/*  76 */     super.onContainerClosed(playerIn);
/*     */     
/*  78 */     for (int i = 0; i < 4; i++) {
/*     */       
/*  80 */       ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
/*     */       
/*  82 */       if (itemstack != null)
/*     */       {
/*  84 */         playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       }
/*     */     } 
/*     */     
/*  88 */     this.craftResult.setInventorySlotContents(0, (ItemStack)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn) {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/*  98 */     ItemStack itemstack = null;
/*  99 */     Slot slot = this.inventorySlots.get(index);
/*     */     
/* 101 */     if (slot != null && slot.getHasStack()) {
/*     */       
/* 103 */       ItemStack itemstack1 = slot.getStack();
/* 104 */       itemstack = itemstack1.copy();
/*     */       
/* 106 */       if (index == 0) {
/*     */         
/* 108 */         if (!mergeItemStack(itemstack1, 9, 45, true))
/*     */         {
/* 110 */           return null;
/*     */         }
/*     */         
/* 113 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/* 115 */       else if (index >= 1 && index < 5) {
/*     */         
/* 117 */         if (!mergeItemStack(itemstack1, 9, 45, false))
/*     */         {
/* 119 */           return null;
/*     */         }
/*     */       }
/* 122 */       else if (index >= 5 && index < 9) {
/*     */         
/* 124 */         if (!mergeItemStack(itemstack1, 9, 45, false))
/*     */         {
/* 126 */           return null;
/*     */         }
/*     */       }
/* 129 */       else if (itemstack.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack()) {
/*     */         
/* 131 */         int i = 5 + ((ItemArmor)itemstack.getItem()).armorType;
/*     */         
/* 133 */         if (!mergeItemStack(itemstack1, i, i + 1, false))
/*     */         {
/* 135 */           return null;
/*     */         }
/*     */       }
/* 138 */       else if (index >= 9 && index < 36) {
/*     */         
/* 140 */         if (!mergeItemStack(itemstack1, 36, 45, false))
/*     */         {
/* 142 */           return null;
/*     */         }
/*     */       }
/* 145 */       else if (index >= 36 && index < 45) {
/*     */         
/* 147 */         if (!mergeItemStack(itemstack1, 9, 36, false))
/*     */         {
/* 149 */           return null;
/*     */         }
/*     */       }
/* 152 */       else if (!mergeItemStack(itemstack1, 9, 45, false)) {
/*     */         
/* 154 */         return null;
/*     */       } 
/*     */       
/* 157 */       if (itemstack1.stackSize == 0) {
/*     */         
/* 159 */         slot.putStack((ItemStack)null);
/*     */       }
/*     */       else {
/*     */         
/* 163 */         slot.onSlotChanged();
/*     */       } 
/*     */       
/* 166 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 168 */         return null;
/*     */       }
/*     */       
/* 171 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     } 
/*     */     
/* 174 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
/* 179 */     return (slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */