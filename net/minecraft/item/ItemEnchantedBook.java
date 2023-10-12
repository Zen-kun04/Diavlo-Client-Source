/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentData;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.WeightedRandomChestContent;
/*     */ 
/*     */ public class ItemEnchantedBook
/*     */   extends Item
/*     */ {
/*     */   public boolean hasEffect(ItemStack stack) {
/*  19 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemTool(ItemStack stack) {
/*  24 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumRarity getRarity(ItemStack stack) {
/*  29 */     return (getEnchantments(stack).tagCount() > 0) ? EnumRarity.UNCOMMON : super.getRarity(stack);
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagList getEnchantments(ItemStack stack) {
/*  34 */     NBTTagCompound nbttagcompound = stack.getTagCompound();
/*  35 */     return (nbttagcompound != null && nbttagcompound.hasKey("StoredEnchantments", 9)) ? (NBTTagList)nbttagcompound.getTag("StoredEnchantments") : new NBTTagList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
/*  40 */     super.addInformation(stack, playerIn, tooltip, advanced);
/*  41 */     NBTTagList nbttaglist = getEnchantments(stack);
/*     */     
/*  43 */     if (nbttaglist != null)
/*     */     {
/*  45 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */         
/*  47 */         int j = nbttaglist.getCompoundTagAt(i).getShort("id");
/*  48 */         int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
/*     */         
/*  50 */         if (Enchantment.getEnchantmentById(j) != null)
/*     */         {
/*  52 */           tooltip.add(Enchantment.getEnchantmentById(j).getTranslatedName(k));
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEnchantment(ItemStack stack, EnchantmentData enchantment) {
/*  60 */     NBTTagList nbttaglist = getEnchantments(stack);
/*  61 */     boolean flag = true;
/*     */     
/*  63 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/*  65 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/*     */       
/*  67 */       if (nbttagcompound.getShort("id") == enchantment.enchantmentobj.effectId) {
/*     */         
/*  69 */         if (nbttagcompound.getShort("lvl") < enchantment.enchantmentLevel)
/*     */         {
/*  71 */           nbttagcompound.setShort("lvl", (short)enchantment.enchantmentLevel);
/*     */         }
/*     */         
/*  74 */         flag = false;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  79 */     if (flag) {
/*     */       
/*  81 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  82 */       nbttagcompound1.setShort("id", (short)enchantment.enchantmentobj.effectId);
/*  83 */       nbttagcompound1.setShort("lvl", (short)enchantment.enchantmentLevel);
/*  84 */       nbttaglist.appendTag((NBTBase)nbttagcompound1);
/*     */     } 
/*     */     
/*  87 */     if (!stack.hasTagCompound())
/*     */     {
/*  89 */       stack.setTagCompound(new NBTTagCompound());
/*     */     }
/*     */     
/*  92 */     stack.getTagCompound().setTag("StoredEnchantments", (NBTBase)nbttaglist);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getEnchantedItemStack(EnchantmentData data) {
/*  97 */     ItemStack itemstack = new ItemStack(this);
/*  98 */     addEnchantment(itemstack, data);
/*  99 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void getAll(Enchantment enchantment, List<ItemStack> list) {
/* 104 */     for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++)
/*     */     {
/* 106 */       list.add(getEnchantedItemStack(new EnchantmentData(enchantment, i)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public WeightedRandomChestContent getRandom(Random rand) {
/* 112 */     return getRandom(rand, 1, 1, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public WeightedRandomChestContent getRandom(Random rand, int minChance, int maxChance, int weight) {
/* 117 */     ItemStack itemstack = new ItemStack(Items.book, 1, 0);
/* 118 */     EnchantmentHelper.addRandomEnchantment(rand, itemstack, 30);
/* 119 */     return new WeightedRandomChestContent(itemstack, minChance, maxChance, weight);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemEnchantedBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */