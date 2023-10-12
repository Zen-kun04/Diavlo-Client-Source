/*     */ package net.minecraft.village;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ 
/*     */ 
/*     */ public class MerchantRecipeList
/*     */   extends ArrayList<MerchantRecipe>
/*     */ {
/*     */   public MerchantRecipeList() {}
/*     */   
/*     */   public MerchantRecipeList(NBTTagCompound compound) {
/*  19 */     readRecipiesFromTags(compound);
/*     */   }
/*     */ 
/*     */   
/*     */   public MerchantRecipe canRecipeBeUsed(ItemStack p_77203_1_, ItemStack p_77203_2_, int p_77203_3_) {
/*  24 */     if (p_77203_3_ > 0 && p_77203_3_ < size()) {
/*     */       
/*  26 */       MerchantRecipe merchantrecipe1 = get(p_77203_3_);
/*  27 */       return (!func_181078_a(p_77203_1_, merchantrecipe1.getItemToBuy()) || ((p_77203_2_ != null || merchantrecipe1.hasSecondItemToBuy()) && (!merchantrecipe1.hasSecondItemToBuy() || !func_181078_a(p_77203_2_, merchantrecipe1.getSecondItemToBuy()))) || p_77203_1_.stackSize < (merchantrecipe1.getItemToBuy()).stackSize || (merchantrecipe1.hasSecondItemToBuy() && p_77203_2_.stackSize < (merchantrecipe1.getSecondItemToBuy()).stackSize)) ? null : merchantrecipe1;
/*     */     } 
/*     */ 
/*     */     
/*  31 */     for (int i = 0; i < size(); i++) {
/*     */       
/*  33 */       MerchantRecipe merchantrecipe = get(i);
/*     */       
/*  35 */       if (func_181078_a(p_77203_1_, merchantrecipe.getItemToBuy()) && p_77203_1_.stackSize >= (merchantrecipe.getItemToBuy()).stackSize && ((!merchantrecipe.hasSecondItemToBuy() && p_77203_2_ == null) || (merchantrecipe.hasSecondItemToBuy() && func_181078_a(p_77203_2_, merchantrecipe.getSecondItemToBuy()) && p_77203_2_.stackSize >= (merchantrecipe.getSecondItemToBuy()).stackSize)))
/*     */       {
/*  37 */         return merchantrecipe;
/*     */       }
/*     */     } 
/*     */     
/*  41 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean func_181078_a(ItemStack p_181078_1_, ItemStack p_181078_2_) {
/*  47 */     return (ItemStack.areItemsEqual(p_181078_1_, p_181078_2_) && (!p_181078_2_.hasTagCompound() || (p_181078_1_.hasTagCompound() && NBTUtil.func_181123_a((NBTBase)p_181078_2_.getTagCompound(), (NBTBase)p_181078_1_.getTagCompound(), false))));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToBuf(PacketBuffer buffer) {
/*  52 */     buffer.writeByte((byte)(size() & 0xFF));
/*     */     
/*  54 */     for (int i = 0; i < size(); i++) {
/*     */       
/*  56 */       MerchantRecipe merchantrecipe = get(i);
/*  57 */       buffer.writeItemStackToBuffer(merchantrecipe.getItemToBuy());
/*  58 */       buffer.writeItemStackToBuffer(merchantrecipe.getItemToSell());
/*  59 */       ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
/*  60 */       buffer.writeBoolean((itemstack != null));
/*     */       
/*  62 */       if (itemstack != null)
/*     */       {
/*  64 */         buffer.writeItemStackToBuffer(itemstack);
/*     */       }
/*     */       
/*  67 */       buffer.writeBoolean(merchantrecipe.isRecipeDisabled());
/*  68 */       buffer.writeInt(merchantrecipe.getToolUses());
/*  69 */       buffer.writeInt(merchantrecipe.getMaxTradeUses());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static MerchantRecipeList readFromBuf(PacketBuffer buffer) throws IOException {
/*  75 */     MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
/*  76 */     int i = buffer.readByte() & 0xFF;
/*     */     
/*  78 */     for (int j = 0; j < i; j++) {
/*     */       
/*  80 */       ItemStack itemstack = buffer.readItemStackFromBuffer();
/*  81 */       ItemStack itemstack1 = buffer.readItemStackFromBuffer();
/*  82 */       ItemStack itemstack2 = null;
/*     */       
/*  84 */       if (buffer.readBoolean())
/*     */       {
/*  86 */         itemstack2 = buffer.readItemStackFromBuffer();
/*     */       }
/*     */       
/*  89 */       boolean flag = buffer.readBoolean();
/*  90 */       int k = buffer.readInt();
/*  91 */       int l = buffer.readInt();
/*  92 */       MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack2, itemstack1, k, l);
/*     */       
/*  94 */       if (flag)
/*     */       {
/*  96 */         merchantrecipe.compensateToolUses();
/*     */       }
/*     */       
/*  99 */       merchantrecipelist.add(merchantrecipe);
/*     */     } 
/*     */     
/* 102 */     return merchantrecipelist;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readRecipiesFromTags(NBTTagCompound compound) {
/* 107 */     NBTTagList nbttaglist = compound.getTagList("Recipes", 10);
/*     */     
/* 109 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 111 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 112 */       add(new MerchantRecipe(nbttagcompound));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getRecipiesAsTags() {
/* 118 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 119 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 121 */     for (int i = 0; i < size(); i++) {
/*     */       
/* 123 */       MerchantRecipe merchantrecipe = get(i);
/* 124 */       nbttaglist.appendTag((NBTBase)merchantrecipe.writeToTags());
/*     */     } 
/*     */     
/* 127 */     nbttagcompound.setTag("Recipes", (NBTBase)nbttaglist);
/* 128 */     return nbttagcompound;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\village\MerchantRecipeList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */