/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.FurnaceRecipes;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
/*     */ 
/*     */ public class ContainerFurnace
/*     */   extends Container
/*     */ {
/*     */   private final IInventory tileFurnace;
/*     */   private int cookTime;
/*     */   private int totalCookTime;
/*     */   private int furnaceBurnTime;
/*     */   private int currentItemBurnTime;
/*     */   
/*     */   public ContainerFurnace(InventoryPlayer playerInventory, IInventory furnaceInventory) {
/*  19 */     this.tileFurnace = furnaceInventory;
/*  20 */     addSlotToContainer(new Slot(furnaceInventory, 0, 56, 17));
/*  21 */     addSlotToContainer(new SlotFurnaceFuel(furnaceInventory, 1, 56, 53));
/*  22 */     addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 116, 35));
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
/*     */   public void onCraftGuiOpened(ICrafting listener) {
/*  40 */     super.onCraftGuiOpened(listener);
/*  41 */     listener.sendAllWindowProperties(this, this.tileFurnace);
/*     */   }
/*     */ 
/*     */   
/*     */   public void detectAndSendChanges() {
/*  46 */     super.detectAndSendChanges();
/*     */     
/*  48 */     for (int i = 0; i < this.crafters.size(); i++) {
/*     */       
/*  50 */       ICrafting icrafting = this.crafters.get(i);
/*     */       
/*  52 */       if (this.cookTime != this.tileFurnace.getField(2))
/*     */       {
/*  54 */         icrafting.sendProgressBarUpdate(this, 2, this.tileFurnace.getField(2));
/*     */       }
/*     */       
/*  57 */       if (this.furnaceBurnTime != this.tileFurnace.getField(0))
/*     */       {
/*  59 */         icrafting.sendProgressBarUpdate(this, 0, this.tileFurnace.getField(0));
/*     */       }
/*     */       
/*  62 */       if (this.currentItemBurnTime != this.tileFurnace.getField(1))
/*     */       {
/*  64 */         icrafting.sendProgressBarUpdate(this, 1, this.tileFurnace.getField(1));
/*     */       }
/*     */       
/*  67 */       if (this.totalCookTime != this.tileFurnace.getField(3))
/*     */       {
/*  69 */         icrafting.sendProgressBarUpdate(this, 3, this.tileFurnace.getField(3));
/*     */       }
/*     */     } 
/*     */     
/*  73 */     this.cookTime = this.tileFurnace.getField(2);
/*  74 */     this.furnaceBurnTime = this.tileFurnace.getField(0);
/*  75 */     this.currentItemBurnTime = this.tileFurnace.getField(1);
/*  76 */     this.totalCookTime = this.tileFurnace.getField(3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateProgressBar(int id, int data) {
/*  81 */     this.tileFurnace.setField(id, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn) {
/*  86 */     return this.tileFurnace.isUseableByPlayer(playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/*  91 */     ItemStack itemstack = null;
/*  92 */     Slot slot = this.inventorySlots.get(index);
/*     */     
/*  94 */     if (slot != null && slot.getHasStack()) {
/*     */       
/*  96 */       ItemStack itemstack1 = slot.getStack();
/*  97 */       itemstack = itemstack1.copy();
/*     */       
/*  99 */       if (index == 2) {
/*     */         
/* 101 */         if (!mergeItemStack(itemstack1, 3, 39, true))
/*     */         {
/* 103 */           return null;
/*     */         }
/*     */         
/* 106 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/* 108 */       else if (index != 1 && index != 0) {
/*     */         
/* 110 */         if (FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null)
/*     */         {
/* 112 */           if (!mergeItemStack(itemstack1, 0, 1, false))
/*     */           {
/* 114 */             return null;
/*     */           }
/*     */         }
/* 117 */         else if (TileEntityFurnace.isItemFuel(itemstack1))
/*     */         {
/* 119 */           if (!mergeItemStack(itemstack1, 1, 2, false))
/*     */           {
/* 121 */             return null;
/*     */           }
/*     */         }
/* 124 */         else if (index >= 3 && index < 30)
/*     */         {
/* 126 */           if (!mergeItemStack(itemstack1, 30, 39, false))
/*     */           {
/* 128 */             return null;
/*     */           }
/*     */         }
/* 131 */         else if (index >= 30 && index < 39 && !mergeItemStack(itemstack1, 3, 30, false))
/*     */         {
/* 133 */           return null;
/*     */         }
/*     */       
/* 136 */       } else if (!mergeItemStack(itemstack1, 3, 39, false)) {
/*     */         
/* 138 */         return null;
/*     */       } 
/*     */       
/* 141 */       if (itemstack1.stackSize == 0) {
/*     */         
/* 143 */         slot.putStack((ItemStack)null);
/*     */       }
/*     */       else {
/*     */         
/* 147 */         slot.onSlotChanged();
/*     */       } 
/*     */       
/* 150 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 152 */         return null;
/*     */       }
/*     */       
/* 155 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     } 
/*     */     
/* 158 */     return itemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */