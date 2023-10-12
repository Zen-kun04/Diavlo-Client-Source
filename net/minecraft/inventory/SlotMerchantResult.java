/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.village.MerchantRecipe;
/*     */ 
/*     */ public class SlotMerchantResult
/*     */   extends Slot
/*     */ {
/*     */   private final InventoryMerchant theMerchantInventory;
/*     */   private EntityPlayer thePlayer;
/*     */   private int field_75231_g;
/*     */   private final IMerchant theMerchant;
/*     */   
/*     */   public SlotMerchantResult(EntityPlayer player, IMerchant merchant, InventoryMerchant merchantInventory, int slotIndex, int xPosition, int yPosition) {
/*  18 */     super(merchantInventory, slotIndex, xPosition, yPosition);
/*  19 */     this.thePlayer = player;
/*  20 */     this.theMerchant = merchant;
/*  21 */     this.theMerchantInventory = merchantInventory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemValid(ItemStack stack) {
/*  26 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int amount) {
/*  31 */     if (getHasStack())
/*     */     {
/*  33 */       this.field_75231_g += Math.min(amount, (getStack()).stackSize);
/*     */     }
/*     */     
/*  36 */     return super.decrStackSize(amount);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onCrafting(ItemStack stack, int amount) {
/*  41 */     this.field_75231_g += amount;
/*  42 */     onCrafting(stack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onCrafting(ItemStack stack) {
/*  47 */     stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75231_g);
/*  48 */     this.field_75231_g = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
/*  53 */     onCrafting(stack);
/*  54 */     MerchantRecipe merchantrecipe = this.theMerchantInventory.getCurrentRecipe();
/*     */     
/*  56 */     if (merchantrecipe != null) {
/*     */       
/*  58 */       ItemStack itemstack = this.theMerchantInventory.getStackInSlot(0);
/*  59 */       ItemStack itemstack1 = this.theMerchantInventory.getStackInSlot(1);
/*     */       
/*  61 */       if (doTrade(merchantrecipe, itemstack, itemstack1) || doTrade(merchantrecipe, itemstack1, itemstack)) {
/*     */         
/*  63 */         this.theMerchant.useRecipe(merchantrecipe);
/*  64 */         playerIn.triggerAchievement(StatList.timesTradedWithVillagerStat);
/*     */         
/*  66 */         if (itemstack != null && itemstack.stackSize <= 0)
/*     */         {
/*  68 */           itemstack = null;
/*     */         }
/*     */         
/*  71 */         if (itemstack1 != null && itemstack1.stackSize <= 0)
/*     */         {
/*  73 */           itemstack1 = null;
/*     */         }
/*     */         
/*  76 */         this.theMerchantInventory.setInventorySlotContents(0, itemstack);
/*  77 */         this.theMerchantInventory.setInventorySlotContents(1, itemstack1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean doTrade(MerchantRecipe trade, ItemStack firstItem, ItemStack secondItem) {
/*  84 */     ItemStack itemstack = trade.getItemToBuy();
/*  85 */     ItemStack itemstack1 = trade.getSecondItemToBuy();
/*     */     
/*  87 */     if (firstItem != null && firstItem.getItem() == itemstack.getItem()) {
/*     */       
/*  89 */       if (itemstack1 != null && secondItem != null && itemstack1.getItem() == secondItem.getItem()) {
/*     */         
/*  91 */         firstItem.stackSize -= itemstack.stackSize;
/*  92 */         secondItem.stackSize -= itemstack1.stackSize;
/*  93 */         return true;
/*     */       } 
/*     */       
/*  96 */       if (itemstack1 == null && secondItem == null) {
/*     */         
/*  98 */         firstItem.stackSize -= itemstack.stackSize;
/*  99 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\SlotMerchantResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */