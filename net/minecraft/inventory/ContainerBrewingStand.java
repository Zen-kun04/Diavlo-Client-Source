/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ 
/*     */ public class ContainerBrewingStand
/*     */   extends Container {
/*     */   private IInventory tileBrewingStand;
/*     */   private final Slot theSlot;
/*     */   private int brewTime;
/*     */   
/*     */   public ContainerBrewingStand(InventoryPlayer playerInventory, IInventory tileBrewingStandIn) {
/*  17 */     this.tileBrewingStand = tileBrewingStandIn;
/*  18 */     addSlotToContainer(new Potion(playerInventory.player, tileBrewingStandIn, 0, 56, 46));
/*  19 */     addSlotToContainer(new Potion(playerInventory.player, tileBrewingStandIn, 1, 79, 53));
/*  20 */     addSlotToContainer(new Potion(playerInventory.player, tileBrewingStandIn, 2, 102, 46));
/*  21 */     this.theSlot = addSlotToContainer(new Ingredient(tileBrewingStandIn, 3, 79, 17));
/*     */     
/*  23 */     for (int i = 0; i < 3; i++) {
/*     */       
/*  25 */       for (int j = 0; j < 9; j++)
/*     */       {
/*  27 */         addSlotToContainer(new Slot((IInventory)playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
/*     */       }
/*     */     } 
/*     */     
/*  31 */     for (int k = 0; k < 9; k++)
/*     */     {
/*  33 */       addSlotToContainer(new Slot((IInventory)playerInventory, k, 8 + k * 18, 142));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener) {
/*  39 */     super.onCraftGuiOpened(listener);
/*  40 */     listener.sendAllWindowProperties(this, this.tileBrewingStand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void detectAndSendChanges() {
/*  45 */     super.detectAndSendChanges();
/*     */     
/*  47 */     for (int i = 0; i < this.crafters.size(); i++) {
/*     */       
/*  49 */       ICrafting icrafting = this.crafters.get(i);
/*     */       
/*  51 */       if (this.brewTime != this.tileBrewingStand.getField(0))
/*     */       {
/*  53 */         icrafting.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
/*     */       }
/*     */     } 
/*     */     
/*  57 */     this.brewTime = this.tileBrewingStand.getField(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateProgressBar(int id, int data) {
/*  62 */     this.tileBrewingStand.setField(id, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn) {
/*  67 */     return this.tileBrewingStand.isUseableByPlayer(playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/*  72 */     ItemStack itemstack = null;
/*  73 */     Slot slot = this.inventorySlots.get(index);
/*     */     
/*  75 */     if (slot != null && slot.getHasStack()) {
/*     */       
/*  77 */       ItemStack itemstack1 = slot.getStack();
/*  78 */       itemstack = itemstack1.copy();
/*     */       
/*  80 */       if ((index < 0 || index > 2) && index != 3) {
/*     */         
/*  82 */         if (!this.theSlot.getHasStack() && this.theSlot.isItemValid(itemstack1))
/*     */         {
/*  84 */           if (!mergeItemStack(itemstack1, 3, 4, false))
/*     */           {
/*  86 */             return null;
/*     */           }
/*     */         }
/*  89 */         else if (Potion.canHoldPotion(itemstack))
/*     */         {
/*  91 */           if (!mergeItemStack(itemstack1, 0, 3, false))
/*     */           {
/*  93 */             return null;
/*     */           }
/*     */         }
/*  96 */         else if (index >= 4 && index < 31)
/*     */         {
/*  98 */           if (!mergeItemStack(itemstack1, 31, 40, false))
/*     */           {
/* 100 */             return null;
/*     */           }
/*     */         }
/* 103 */         else if (index >= 31 && index < 40)
/*     */         {
/* 105 */           if (!mergeItemStack(itemstack1, 4, 31, false))
/*     */           {
/* 107 */             return null;
/*     */           }
/*     */         }
/* 110 */         else if (!mergeItemStack(itemstack1, 4, 40, false))
/*     */         {
/* 112 */           return null;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 117 */         if (!mergeItemStack(itemstack1, 4, 40, true))
/*     */         {
/* 119 */           return null;
/*     */         }
/*     */         
/* 122 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       } 
/*     */       
/* 125 */       if (itemstack1.stackSize == 0) {
/*     */         
/* 127 */         slot.putStack((ItemStack)null);
/*     */       }
/*     */       else {
/*     */         
/* 131 */         slot.onSlotChanged();
/*     */       } 
/*     */       
/* 134 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 136 */         return null;
/*     */       }
/*     */       
/* 139 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     } 
/*     */     
/* 142 */     return itemstack;
/*     */   }
/*     */   
/*     */   class Ingredient
/*     */     extends Slot
/*     */   {
/*     */     public Ingredient(IInventory inventoryIn, int index, int xPosition, int yPosition) {
/* 149 */       super(inventoryIn, index, xPosition, yPosition);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isItemValid(ItemStack stack) {
/* 154 */       return (stack != null) ? stack.getItem().isPotionIngredient(stack) : false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getSlotStackLimit() {
/* 159 */       return 64;
/*     */     }
/*     */   }
/*     */   
/*     */   static class Potion
/*     */     extends Slot
/*     */   {
/*     */     private EntityPlayer player;
/*     */     
/*     */     public Potion(EntityPlayer playerIn, IInventory inventoryIn, int index, int xPosition, int yPosition) {
/* 169 */       super(inventoryIn, index, xPosition, yPosition);
/* 170 */       this.player = playerIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isItemValid(ItemStack stack) {
/* 175 */       return canHoldPotion(stack);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getSlotStackLimit() {
/* 180 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
/* 185 */       if (stack.getItem() == Items.potionitem && stack.getMetadata() > 0)
/*     */       {
/* 187 */         this.player.triggerAchievement((StatBase)AchievementList.potion);
/*     */       }
/*     */       
/* 190 */       super.onPickupFromSlot(playerIn, stack);
/*     */     }
/*     */ 
/*     */     
/*     */     public static boolean canHoldPotion(ItemStack stack) {
/* 195 */       return (stack != null && (stack.getItem() == Items.potionitem || stack.getItem() == Items.glass_bottle));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerBrewingStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */