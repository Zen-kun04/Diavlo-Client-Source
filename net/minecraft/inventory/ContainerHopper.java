/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class ContainerHopper
/*    */   extends Container
/*    */ {
/*    */   private final IInventory hopperInventory;
/*    */   
/*    */   public ContainerHopper(InventoryPlayer playerInventory, IInventory hopperInventoryIn, EntityPlayer player) {
/* 13 */     this.hopperInventory = hopperInventoryIn;
/* 14 */     hopperInventoryIn.openInventory(player);
/* 15 */     int i = 51;
/*    */     
/* 17 */     for (int j = 0; j < hopperInventoryIn.getSizeInventory(); j++)
/*    */     {
/* 19 */       addSlotToContainer(new Slot(hopperInventoryIn, j, 44 + j * 18, 20));
/*    */     }
/*    */     
/* 22 */     for (int l = 0; l < 3; l++) {
/*    */       
/* 24 */       for (int k = 0; k < 9; k++)
/*    */       {
/* 26 */         addSlotToContainer(new Slot((IInventory)playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + i));
/*    */       }
/*    */     } 
/*    */     
/* 30 */     for (int i1 = 0; i1 < 9; i1++)
/*    */     {
/* 32 */       addSlotToContainer(new Slot((IInventory)playerInventory, i1, 8 + i1 * 18, 58 + i));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canInteractWith(EntityPlayer playerIn) {
/* 38 */     return this.hopperInventory.isUseableByPlayer(playerIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/* 43 */     ItemStack itemstack = null;
/* 44 */     Slot slot = this.inventorySlots.get(index);
/*    */     
/* 46 */     if (slot != null && slot.getHasStack()) {
/*    */       
/* 48 */       ItemStack itemstack1 = slot.getStack();
/* 49 */       itemstack = itemstack1.copy();
/*    */       
/* 51 */       if (index < this.hopperInventory.getSizeInventory()) {
/*    */         
/* 53 */         if (!mergeItemStack(itemstack1, this.hopperInventory.getSizeInventory(), this.inventorySlots.size(), true))
/*    */         {
/* 55 */           return null;
/*    */         }
/*    */       }
/* 58 */       else if (!mergeItemStack(itemstack1, 0, this.hopperInventory.getSizeInventory(), false)) {
/*    */         
/* 60 */         return null;
/*    */       } 
/*    */       
/* 63 */       if (itemstack1.stackSize == 0) {
/*    */         
/* 65 */         slot.putStack((ItemStack)null);
/*    */       }
/*    */       else {
/*    */         
/* 69 */         slot.onSlotChanged();
/*    */       } 
/*    */     } 
/*    */     
/* 73 */     return itemstack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onContainerClosed(EntityPlayer playerIn) {
/* 78 */     super.onContainerClosed(playerIn);
/* 79 */     this.hopperInventory.closeInventory(playerIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */