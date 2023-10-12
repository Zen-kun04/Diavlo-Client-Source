/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ContainerWorkbench
/*     */   extends Container {
/*  13 */   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
/*  14 */   public IInventory craftResult = new InventoryCraftResult();
/*     */   
/*     */   private World worldObj;
/*     */   private BlockPos pos;
/*     */   
/*     */   public ContainerWorkbench(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
/*  20 */     this.worldObj = worldIn;
/*  21 */     this.pos = posIn;
/*  22 */     addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
/*     */     
/*  24 */     for (int i = 0; i < 3; i++) {
/*     */       
/*  26 */       for (int j = 0; j < 3; j++)
/*     */       {
/*  28 */         addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
/*     */       }
/*     */     } 
/*     */     
/*  32 */     for (int k = 0; k < 3; k++) {
/*     */       
/*  34 */       for (int i1 = 0; i1 < 9; i1++)
/*     */       {
/*  36 */         addSlotToContainer(new Slot((IInventory)playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
/*     */       }
/*     */     } 
/*     */     
/*  40 */     for (int l = 0; l < 9; l++)
/*     */     {
/*  42 */       addSlotToContainer(new Slot((IInventory)playerInventory, l, 8 + l * 18, 142));
/*     */     }
/*     */     
/*  45 */     onCraftMatrixChanged(this.craftMatrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn) {
/*  50 */     this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onContainerClosed(EntityPlayer playerIn) {
/*  55 */     super.onContainerClosed(playerIn);
/*     */     
/*  57 */     if (!this.worldObj.isRemote)
/*     */     {
/*  59 */       for (int i = 0; i < 9; i++) {
/*     */         
/*  61 */         ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
/*     */         
/*  63 */         if (itemstack != null)
/*     */         {
/*  65 */           playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn) {
/*  73 */     return (this.worldObj.getBlockState(this.pos).getBlock() != Blocks.crafting_table) ? false : ((playerIn.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/*  78 */     ItemStack itemstack = null;
/*  79 */     Slot slot = this.inventorySlots.get(index);
/*     */     
/*  81 */     if (slot != null && slot.getHasStack()) {
/*     */       
/*  83 */       ItemStack itemstack1 = slot.getStack();
/*  84 */       itemstack = itemstack1.copy();
/*     */       
/*  86 */       if (index == 0) {
/*     */         
/*  88 */         if (!mergeItemStack(itemstack1, 10, 46, true))
/*     */         {
/*  90 */           return null;
/*     */         }
/*     */         
/*  93 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/*  95 */       else if (index >= 10 && index < 37) {
/*     */         
/*  97 */         if (!mergeItemStack(itemstack1, 37, 46, false))
/*     */         {
/*  99 */           return null;
/*     */         }
/*     */       }
/* 102 */       else if (index >= 37 && index < 46) {
/*     */         
/* 104 */         if (!mergeItemStack(itemstack1, 10, 37, false))
/*     */         {
/* 106 */           return null;
/*     */         }
/*     */       }
/* 109 */       else if (!mergeItemStack(itemstack1, 10, 46, false)) {
/*     */         
/* 111 */         return null;
/*     */       } 
/*     */       
/* 114 */       if (itemstack1.stackSize == 0) {
/*     */         
/* 116 */         slot.putStack((ItemStack)null);
/*     */       }
/*     */       else {
/*     */         
/* 120 */         slot.onSlotChanged();
/*     */       } 
/*     */       
/* 123 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 125 */         return null;
/*     */       }
/*     */       
/* 128 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     } 
/*     */     
/* 131 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
/* 136 */     return (slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerWorkbench.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */