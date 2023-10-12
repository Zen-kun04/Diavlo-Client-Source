/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class ContainerDispenser
/*    */   extends Container
/*    */ {
/*    */   private IInventory dispenserInventory;
/*    */   
/*    */   public ContainerDispenser(IInventory playerInventory, IInventory dispenserInventoryIn) {
/* 12 */     this.dispenserInventory = dispenserInventoryIn;
/*    */     
/* 14 */     for (int i = 0; i < 3; i++) {
/*    */       
/* 16 */       for (int j = 0; j < 3; j++)
/*    */       {
/* 18 */         addSlotToContainer(new Slot(dispenserInventoryIn, j + i * 3, 62 + j * 18, 17 + i * 18));
/*    */       }
/*    */     } 
/*    */     
/* 22 */     for (int k = 0; k < 3; k++) {
/*    */       
/* 24 */       for (int i1 = 0; i1 < 9; i1++)
/*    */       {
/* 26 */         addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
/*    */       }
/*    */     } 
/*    */     
/* 30 */     for (int l = 0; l < 9; l++)
/*    */     {
/* 32 */       addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canInteractWith(EntityPlayer playerIn) {
/* 38 */     return this.dispenserInventory.isUseableByPlayer(playerIn);
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
/* 51 */       if (index < 9) {
/*    */         
/* 53 */         if (!mergeItemStack(itemstack1, 9, 45, true))
/*    */         {
/* 55 */           return null;
/*    */         }
/*    */       }
/* 58 */       else if (!mergeItemStack(itemstack1, 0, 9, false)) {
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
/*    */       
/* 72 */       if (itemstack1.stackSize == itemstack.stackSize)
/*    */       {
/* 74 */         return null;
/*    */       }
/*    */       
/* 77 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*    */     } 
/*    */     
/* 80 */     return itemstack;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */