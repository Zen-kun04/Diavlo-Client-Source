/*     */ package net.minecraft.village;
/*     */ 
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ 
/*     */ public class MerchantRecipe
/*     */ {
/*     */   private ItemStack itemToBuy;
/*     */   private ItemStack secondItemToBuy;
/*     */   private ItemStack itemToSell;
/*     */   private int toolUses;
/*     */   private int maxTradeUses;
/*     */   private boolean rewardsExp;
/*     */   
/*     */   public MerchantRecipe(NBTTagCompound tagCompound) {
/*  18 */     readFromTags(tagCompound);
/*     */   }
/*     */ 
/*     */   
/*     */   public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell) {
/*  23 */     this(buy1, buy2, sell, 0, 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell, int toolUsesIn, int maxTradeUsesIn) {
/*  28 */     this.itemToBuy = buy1;
/*  29 */     this.secondItemToBuy = buy2;
/*  30 */     this.itemToSell = sell;
/*  31 */     this.toolUses = toolUsesIn;
/*  32 */     this.maxTradeUses = maxTradeUsesIn;
/*  33 */     this.rewardsExp = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public MerchantRecipe(ItemStack buy1, ItemStack sell) {
/*  38 */     this(buy1, (ItemStack)null, sell);
/*     */   }
/*     */ 
/*     */   
/*     */   public MerchantRecipe(ItemStack buy1, Item sellItem) {
/*  43 */     this(buy1, new ItemStack(sellItem));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemToBuy() {
/*  48 */     return this.itemToBuy;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getSecondItemToBuy() {
/*  53 */     return this.secondItemToBuy;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSecondItemToBuy() {
/*  58 */     return (this.secondItemToBuy != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemToSell() {
/*  63 */     return this.itemToSell;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getToolUses() {
/*  68 */     return this.toolUses;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTradeUses() {
/*  73 */     return this.maxTradeUses;
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementToolUses() {
/*  78 */     this.toolUses++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void increaseMaxTradeUses(int increment) {
/*  83 */     this.maxTradeUses += increment;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRecipeDisabled() {
/*  88 */     return (this.toolUses >= this.maxTradeUses);
/*     */   }
/*     */ 
/*     */   
/*     */   public void compensateToolUses() {
/*  93 */     this.toolUses = this.maxTradeUses;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getRewardsExp() {
/*  98 */     return this.rewardsExp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromTags(NBTTagCompound tagCompound) {
/* 103 */     NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("buy");
/* 104 */     this.itemToBuy = ItemStack.loadItemStackFromNBT(nbttagcompound);
/* 105 */     NBTTagCompound nbttagcompound1 = tagCompound.getCompoundTag("sell");
/* 106 */     this.itemToSell = ItemStack.loadItemStackFromNBT(nbttagcompound1);
/*     */     
/* 108 */     if (tagCompound.hasKey("buyB", 10))
/*     */     {
/* 110 */       this.secondItemToBuy = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("buyB"));
/*     */     }
/*     */     
/* 113 */     if (tagCompound.hasKey("uses", 99))
/*     */     {
/* 115 */       this.toolUses = tagCompound.getInteger("uses");
/*     */     }
/*     */     
/* 118 */     if (tagCompound.hasKey("maxUses", 99)) {
/*     */       
/* 120 */       this.maxTradeUses = tagCompound.getInteger("maxUses");
/*     */     }
/*     */     else {
/*     */       
/* 124 */       this.maxTradeUses = 7;
/*     */     } 
/*     */     
/* 127 */     if (tagCompound.hasKey("rewardExp", 1)) {
/*     */       
/* 129 */       this.rewardsExp = tagCompound.getBoolean("rewardExp");
/*     */     }
/*     */     else {
/*     */       
/* 133 */       this.rewardsExp = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound writeToTags() {
/* 139 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 140 */     nbttagcompound.setTag("buy", (NBTBase)this.itemToBuy.writeToNBT(new NBTTagCompound()));
/* 141 */     nbttagcompound.setTag("sell", (NBTBase)this.itemToSell.writeToNBT(new NBTTagCompound()));
/*     */     
/* 143 */     if (this.secondItemToBuy != null)
/*     */     {
/* 145 */       nbttagcompound.setTag("buyB", (NBTBase)this.secondItemToBuy.writeToNBT(new NBTTagCompound()));
/*     */     }
/*     */     
/* 148 */     nbttagcompound.setInteger("uses", this.toolUses);
/* 149 */     nbttagcompound.setInteger("maxUses", this.maxTradeUses);
/* 150 */     nbttagcompound.setBoolean("rewardExp", this.rewardsExp);
/* 151 */     return nbttagcompound;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\village\MerchantRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */