/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ContainerMerchant
/*     */   extends Container
/*     */ {
/*     */   private IMerchant theMerchant;
/*     */   private InventoryMerchant merchantInventory;
/*     */   private final World theWorld;
/*     */   
/*     */   public ContainerMerchant(InventoryPlayer playerInventory, IMerchant merchant, World worldIn) {
/*  17 */     this.theMerchant = merchant;
/*  18 */     this.theWorld = worldIn;
/*  19 */     this.merchantInventory = new InventoryMerchant(playerInventory.player, merchant);
/*  20 */     addSlotToContainer(new Slot(this.merchantInventory, 0, 36, 53));
/*  21 */     addSlotToContainer(new Slot(this.merchantInventory, 1, 62, 53));
/*  22 */     addSlotToContainer(new SlotMerchantResult(playerInventory.player, merchant, this.merchantInventory, 2, 120, 53));
/*     */     
/*  24 */     for (int i = 0; i < 3; i++) {
/*     */       
/*  26 */       for (int j = 0; j < 9; j++)
/*     */       {
/*  28 */         addSlotToContainer(new Slot((IInventory)playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
/*     */       }
/*     */     } 
/*     */     
/*  32 */     for (int k = 0; k < 9; k++)
/*     */     {
/*  34 */       addSlotToContainer(new Slot((IInventory)playerInventory, k, 8 + k * 18, 142));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public InventoryMerchant getMerchantInventory() {
/*  40 */     return this.merchantInventory;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener) {
/*  45 */     super.onCraftGuiOpened(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void detectAndSendChanges() {
/*  50 */     super.detectAndSendChanges();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn) {
/*  55 */     this.merchantInventory.resetRecipeAndSlots();
/*  56 */     super.onCraftMatrixChanged(inventoryIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentRecipeIndex(int currentRecipeIndex) {
/*  61 */     this.merchantInventory.setCurrentRecipeIndex(currentRecipeIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateProgressBar(int id, int data) {}
/*     */ 
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn) {
/*  70 */     return (this.theMerchant.getCustomer() == playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/*  75 */     ItemStack itemstack = null;
/*  76 */     Slot slot = this.inventorySlots.get(index);
/*     */     
/*  78 */     if (slot != null && slot.getHasStack()) {
/*     */       
/*  80 */       ItemStack itemstack1 = slot.getStack();
/*  81 */       itemstack = itemstack1.copy();
/*     */       
/*  83 */       if (index == 2) {
/*     */         
/*  85 */         if (!mergeItemStack(itemstack1, 3, 39, true))
/*     */         {
/*  87 */           return null;
/*     */         }
/*     */         
/*  90 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/*  92 */       else if (index != 0 && index != 1) {
/*     */         
/*  94 */         if (index >= 3 && index < 30)
/*     */         {
/*  96 */           if (!mergeItemStack(itemstack1, 30, 39, false))
/*     */           {
/*  98 */             return null;
/*     */           }
/*     */         }
/* 101 */         else if (index >= 30 && index < 39 && !mergeItemStack(itemstack1, 3, 30, false))
/*     */         {
/* 103 */           return null;
/*     */         }
/*     */       
/* 106 */       } else if (!mergeItemStack(itemstack1, 3, 39, false)) {
/*     */         
/* 108 */         return null;
/*     */       } 
/*     */       
/* 111 */       if (itemstack1.stackSize == 0) {
/*     */         
/* 113 */         slot.putStack((ItemStack)null);
/*     */       }
/*     */       else {
/*     */         
/* 117 */         slot.onSlotChanged();
/*     */       } 
/*     */       
/* 120 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 122 */         return null;
/*     */       }
/*     */       
/* 125 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     } 
/*     */     
/* 128 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onContainerClosed(EntityPlayer playerIn) {
/* 133 */     super.onContainerClosed(playerIn);
/* 134 */     this.theMerchant.setCustomer((EntityPlayer)null);
/* 135 */     super.onContainerClosed(playerIn);
/*     */     
/* 137 */     if (!this.theWorld.isRemote) {
/*     */       
/* 139 */       ItemStack itemstack = this.merchantInventory.removeStackFromSlot(0);
/*     */       
/* 141 */       if (itemstack != null)
/*     */       {
/* 143 */         playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       }
/*     */       
/* 146 */       itemstack = this.merchantInventory.removeStackFromSlot(1);
/*     */       
/* 148 */       if (itemstack != null)
/*     */       {
/* 150 */         playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */