/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.village.MerchantRecipe;
/*     */ import net.minecraft.village.MerchantRecipeList;
/*     */ 
/*     */ public class InventoryMerchant
/*     */   implements IInventory {
/*     */   private final IMerchant theMerchant;
/*  15 */   private ItemStack[] theInventory = new ItemStack[3];
/*     */   
/*     */   private final EntityPlayer thePlayer;
/*     */   private MerchantRecipe currentRecipe;
/*     */   private int currentRecipeIndex;
/*     */   
/*     */   public InventoryMerchant(EntityPlayer thePlayerIn, IMerchant theMerchantIn) {
/*  22 */     this.thePlayer = thePlayerIn;
/*  23 */     this.theMerchant = theMerchantIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/*  28 */     return this.theInventory.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  33 */     return this.theInventory[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  38 */     if (this.theInventory[index] != null) {
/*     */       
/*  40 */       if (index == 2) {
/*     */         
/*  42 */         ItemStack itemstack2 = this.theInventory[index];
/*  43 */         this.theInventory[index] = null;
/*  44 */         return itemstack2;
/*     */       } 
/*  46 */       if ((this.theInventory[index]).stackSize <= count) {
/*     */         
/*  48 */         ItemStack itemstack1 = this.theInventory[index];
/*  49 */         this.theInventory[index] = null;
/*     */         
/*  51 */         if (inventoryResetNeededOnSlotChange(index))
/*     */         {
/*  53 */           resetRecipeAndSlots();
/*     */         }
/*     */         
/*  56 */         return itemstack1;
/*     */       } 
/*     */ 
/*     */       
/*  60 */       ItemStack itemstack = this.theInventory[index].splitStack(count);
/*     */       
/*  62 */       if ((this.theInventory[index]).stackSize == 0)
/*     */       {
/*  64 */         this.theInventory[index] = null;
/*     */       }
/*     */       
/*  67 */       if (inventoryResetNeededOnSlotChange(index))
/*     */       {
/*  69 */         resetRecipeAndSlots();
/*     */       }
/*     */       
/*  72 */       return itemstack;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean inventoryResetNeededOnSlotChange(int p_70469_1_) {
/*  83 */     return (p_70469_1_ == 0 || p_70469_1_ == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/*  88 */     if (this.theInventory[index] != null) {
/*     */       
/*  90 */       ItemStack itemstack = this.theInventory[index];
/*  91 */       this.theInventory[index] = null;
/*  92 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/* 102 */     this.theInventory[index] = stack;
/*     */     
/* 104 */     if (stack != null && stack.stackSize > getInventoryStackLimit())
/*     */     {
/* 106 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/* 109 */     if (inventoryResetNeededOnSlotChange(index))
/*     */     {
/* 111 */       resetRecipeAndSlots();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 117 */     return "mob.villager";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getDisplayName() {
/* 127 */     return hasCustomName() ? (IChatComponent)new ChatComponentText(getName()) : (IChatComponent)new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 132 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 137 */     return (this.theMerchant.getCustomer() == player);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void openInventory(EntityPlayer player) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeInventory(EntityPlayer player) {}
/*     */ 
/*     */   
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack) {
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markDirty() {
/* 155 */     resetRecipeAndSlots();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetRecipeAndSlots() {
/* 160 */     this.currentRecipe = null;
/* 161 */     ItemStack itemstack = this.theInventory[0];
/* 162 */     ItemStack itemstack1 = this.theInventory[1];
/*     */     
/* 164 */     if (itemstack == null) {
/*     */       
/* 166 */       itemstack = itemstack1;
/* 167 */       itemstack1 = null;
/*     */     } 
/*     */     
/* 170 */     if (itemstack == null) {
/*     */       
/* 172 */       setInventorySlotContents(2, (ItemStack)null);
/*     */     }
/*     */     else {
/*     */       
/* 176 */       MerchantRecipeList merchantrecipelist = this.theMerchant.getRecipes(this.thePlayer);
/*     */       
/* 178 */       if (merchantrecipelist != null) {
/*     */         
/* 180 */         MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, this.currentRecipeIndex);
/*     */         
/* 182 */         if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
/*     */           
/* 184 */           this.currentRecipe = merchantrecipe;
/* 185 */           setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
/*     */         }
/* 187 */         else if (itemstack1 != null) {
/*     */           
/* 189 */           merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, this.currentRecipeIndex);
/*     */           
/* 191 */           if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled())
/*     */           {
/* 193 */             this.currentRecipe = merchantrecipe;
/* 194 */             setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
/*     */           }
/*     */           else
/*     */           {
/* 198 */             setInventorySlotContents(2, (ItemStack)null);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 203 */           setInventorySlotContents(2, (ItemStack)null);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 208 */     this.theMerchant.verifySellingItem(getStackInSlot(2));
/*     */   }
/*     */ 
/*     */   
/*     */   public MerchantRecipe getCurrentRecipe() {
/* 213 */     return this.currentRecipe;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentRecipeIndex(int currentRecipeIndexIn) {
/* 218 */     this.currentRecipeIndex = currentRecipeIndexIn;
/* 219 */     resetRecipeAndSlots();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 224 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 233 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 238 */     for (int i = 0; i < this.theInventory.length; i++)
/*     */     {
/* 240 */       this.theInventory[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\InventoryMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */