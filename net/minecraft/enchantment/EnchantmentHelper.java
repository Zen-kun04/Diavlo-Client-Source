/*     */ package net.minecraft.enchantment;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ 
/*     */ public class EnchantmentHelper
/*     */ {
/*  23 */   private static final Random enchantmentRand = new Random();
/*  24 */   private static final ModifierDamage enchantmentModifierDamage = new ModifierDamage();
/*  25 */   private static final ModifierLiving enchantmentModifierLiving = new ModifierLiving();
/*  26 */   private static final HurtIterator ENCHANTMENT_ITERATOR_HURT = new HurtIterator();
/*  27 */   private static final DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new DamageIterator();
/*     */ 
/*     */   
/*     */   public static int getEnchantmentLevel(int enchID, ItemStack stack) {
/*  31 */     if (stack == null)
/*     */     {
/*  33 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*  37 */     NBTTagList nbttaglist = stack.getEnchantmentTagList();
/*     */     
/*  39 */     if (nbttaglist == null)
/*     */     {
/*  41 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*  45 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/*  47 */       int j = nbttaglist.getCompoundTagAt(i).getShort("id");
/*  48 */       int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
/*     */       
/*  50 */       if (j == enchID)
/*     */       {
/*  52 */         return k;
/*     */       }
/*     */     } 
/*     */     
/*  56 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<Integer, Integer> getEnchantments(ItemStack stack) {
/*  63 */     Map<Integer, Integer> map = Maps.newLinkedHashMap();
/*  64 */     NBTTagList nbttaglist = (stack.getItem() == Items.enchanted_book) ? Items.enchanted_book.getEnchantments(stack) : stack.getEnchantmentTagList();
/*     */     
/*  66 */     if (nbttaglist != null)
/*     */     {
/*  68 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */         
/*  70 */         int j = nbttaglist.getCompoundTagAt(i).getShort("id");
/*  71 */         int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
/*  72 */         map.put(Integer.valueOf(j), Integer.valueOf(k));
/*     */       } 
/*     */     }
/*     */     
/*  76 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setEnchantments(Map<Integer, Integer> enchMap, ItemStack stack) {
/*  81 */     NBTTagList nbttaglist = new NBTTagList();
/*  82 */     Iterator<Integer> iterator = enchMap.keySet().iterator();
/*     */     
/*  84 */     while (iterator.hasNext()) {
/*     */       
/*  86 */       int i = ((Integer)iterator.next()).intValue();
/*  87 */       Enchantment enchantment = Enchantment.getEnchantmentById(i);
/*     */       
/*  89 */       if (enchantment != null) {
/*     */         
/*  91 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  92 */         nbttagcompound.setShort("id", (short)i);
/*  93 */         nbttagcompound.setShort("lvl", (short)((Integer)enchMap.get(Integer.valueOf(i))).intValue());
/*  94 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */         
/*  96 */         if (stack.getItem() == Items.enchanted_book)
/*     */         {
/*  98 */           Items.enchanted_book.addEnchantment(stack, new EnchantmentData(enchantment, ((Integer)enchMap.get(Integer.valueOf(i))).intValue()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     if (nbttaglist.tagCount() > 0) {
/*     */       
/* 105 */       if (stack.getItem() != Items.enchanted_book)
/*     */       {
/* 107 */         stack.setTagInfo("ench", (NBTBase)nbttaglist);
/*     */       }
/*     */     }
/* 110 */     else if (stack.hasTagCompound()) {
/*     */       
/* 112 */       stack.getTagCompound().removeTag("ench");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getMaxEnchantmentLevel(int enchID, ItemStack[] stacks) {
/* 118 */     if (stacks == null)
/*     */     {
/* 120 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 124 */     int i = 0;
/*     */     
/* 126 */     for (ItemStack itemstack : stacks) {
/*     */       
/* 128 */       int j = getEnchantmentLevel(enchID, itemstack);
/*     */       
/* 130 */       if (j > i)
/*     */       {
/* 132 */         i = j;
/*     */       }
/*     */     } 
/*     */     
/* 136 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void applyEnchantmentModifier(IModifier modifier, ItemStack stack) {
/* 142 */     if (stack != null) {
/*     */       
/* 144 */       NBTTagList nbttaglist = stack.getEnchantmentTagList();
/*     */       
/* 146 */       if (nbttaglist != null)
/*     */       {
/* 148 */         for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */           
/* 150 */           int j = nbttaglist.getCompoundTagAt(i).getShort("id");
/* 151 */           int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
/*     */           
/* 153 */           if (Enchantment.getEnchantmentById(j) != null)
/*     */           {
/* 155 */             modifier.calculateModifier(Enchantment.getEnchantmentById(j), k);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void applyEnchantmentModifierArray(IModifier modifier, ItemStack[] stacks) {
/* 164 */     for (ItemStack itemstack : stacks)
/*     */     {
/* 166 */       applyEnchantmentModifier(modifier, itemstack);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getEnchantmentModifierDamage(ItemStack[] stacks, DamageSource source) {
/* 172 */     enchantmentModifierDamage.damageModifier = 0;
/* 173 */     enchantmentModifierDamage.source = source;
/* 174 */     applyEnchantmentModifierArray(enchantmentModifierDamage, stacks);
/*     */     
/* 176 */     if (enchantmentModifierDamage.damageModifier > 25) {
/*     */       
/* 178 */       enchantmentModifierDamage.damageModifier = 25;
/*     */     }
/* 180 */     else if (enchantmentModifierDamage.damageModifier < 0) {
/*     */       
/* 182 */       enchantmentModifierDamage.damageModifier = 0;
/*     */     } 
/*     */     
/* 185 */     return (enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((enchantmentModifierDamage.damageModifier >> 1) + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getModifierForCreature(ItemStack p_152377_0_, EnumCreatureAttribute p_152377_1_) {
/* 190 */     enchantmentModifierLiving.livingModifier = 0.0F;
/* 191 */     enchantmentModifierLiving.entityLiving = p_152377_1_;
/* 192 */     applyEnchantmentModifier(enchantmentModifierLiving, p_152377_0_);
/* 193 */     return enchantmentModifierLiving.livingModifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void applyThornEnchantments(EntityLivingBase p_151384_0_, Entity p_151384_1_) {
/* 198 */     ENCHANTMENT_ITERATOR_HURT.attacker = p_151384_1_;
/* 199 */     ENCHANTMENT_ITERATOR_HURT.user = p_151384_0_;
/*     */     
/* 201 */     if (p_151384_0_ != null)
/*     */     {
/* 203 */       applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getInventory());
/*     */     }
/*     */     
/* 206 */     if (p_151384_1_ instanceof net.minecraft.entity.player.EntityPlayer)
/*     */     {
/* 208 */       applyEnchantmentModifier(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getHeldItem());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void applyArthropodEnchantments(EntityLivingBase p_151385_0_, Entity p_151385_1_) {
/* 214 */     ENCHANTMENT_ITERATOR_DAMAGE.user = p_151385_0_;
/* 215 */     ENCHANTMENT_ITERATOR_DAMAGE.target = p_151385_1_;
/*     */     
/* 217 */     if (p_151385_0_ != null)
/*     */     {
/* 219 */       applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getInventory());
/*     */     }
/*     */     
/* 222 */     if (p_151385_0_ instanceof net.minecraft.entity.player.EntityPlayer)
/*     */     {
/* 224 */       applyEnchantmentModifier(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getHeldItem());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getKnockbackModifier(EntityLivingBase player) {
/* 230 */     return getEnchantmentLevel(Enchantment.knockback.effectId, player.getHeldItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getFireAspectModifier(EntityLivingBase player) {
/* 235 */     return getEnchantmentLevel(Enchantment.fireAspect.effectId, player.getHeldItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getRespiration(Entity player) {
/* 240 */     return getMaxEnchantmentLevel(Enchantment.respiration.effectId, player.getInventory());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getDepthStriderModifier(Entity player) {
/* 245 */     return getMaxEnchantmentLevel(Enchantment.depthStrider.effectId, player.getInventory());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getEfficiencyModifier(EntityLivingBase player) {
/* 250 */     return getEnchantmentLevel(Enchantment.efficiency.effectId, player.getHeldItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean getSilkTouchModifier(EntityLivingBase player) {
/* 255 */     return (getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getFortuneModifier(EntityLivingBase player) {
/* 260 */     return getEnchantmentLevel(Enchantment.fortune.effectId, player.getHeldItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getLuckOfSeaModifier(EntityLivingBase player) {
/* 265 */     return getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, player.getHeldItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getLureModifier(EntityLivingBase player) {
/* 270 */     return getEnchantmentLevel(Enchantment.lure.effectId, player.getHeldItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getLootingModifier(EntityLivingBase player) {
/* 275 */     return getEnchantmentLevel(Enchantment.looting.effectId, player.getHeldItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean getAquaAffinityModifier(EntityLivingBase player) {
/* 280 */     return (getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, player.getInventory()) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemStack getEnchantedItem(Enchantment p_92099_0_, EntityLivingBase p_92099_1_) {
/* 285 */     for (ItemStack itemstack : p_92099_1_.getInventory()) {
/*     */       
/* 287 */       if (itemstack != null && getEnchantmentLevel(p_92099_0_.effectId, itemstack) > 0)
/*     */       {
/* 289 */         return itemstack;
/*     */       }
/*     */     } 
/*     */     
/* 293 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int calcItemStackEnchantability(Random rand, int enchantNum, int power, ItemStack stack) {
/* 298 */     Item item = stack.getItem();
/* 299 */     int i = item.getItemEnchantability();
/*     */     
/* 301 */     if (i <= 0)
/*     */     {
/* 303 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 307 */     if (power > 15)
/*     */     {
/* 309 */       power = 15;
/*     */     }
/*     */     
/* 312 */     int j = rand.nextInt(8) + 1 + (power >> 1) + rand.nextInt(power + 1);
/* 313 */     return (enchantNum == 0) ? Math.max(j / 3, 1) : ((enchantNum == 1) ? (j * 2 / 3 + 1) : Math.max(j, power * 2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack addRandomEnchantment(Random p_77504_0_, ItemStack p_77504_1_, int p_77504_2_) {
/* 319 */     List<EnchantmentData> list = buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
/* 320 */     boolean flag = (p_77504_1_.getItem() == Items.book);
/*     */     
/* 322 */     if (flag)
/*     */     {
/* 324 */       p_77504_1_.setItem((Item)Items.enchanted_book);
/*     */     }
/*     */     
/* 327 */     if (list != null)
/*     */     {
/* 329 */       for (EnchantmentData enchantmentdata : list) {
/*     */         
/* 331 */         if (flag) {
/*     */           
/* 333 */           Items.enchanted_book.addEnchantment(p_77504_1_, enchantmentdata);
/*     */           
/*     */           continue;
/*     */         } 
/* 337 */         p_77504_1_.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 342 */     return p_77504_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<EnchantmentData> buildEnchantmentList(Random randomIn, ItemStack itemStackIn, int p_77513_2_) {
/* 347 */     Item item = itemStackIn.getItem();
/* 348 */     int i = item.getItemEnchantability();
/*     */     
/* 350 */     if (i <= 0)
/*     */     {
/* 352 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 356 */     i /= 2;
/* 357 */     i = 1 + randomIn.nextInt((i >> 1) + 1) + randomIn.nextInt((i >> 1) + 1);
/* 358 */     int j = i + p_77513_2_;
/* 359 */     float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0F) * 0.15F;
/* 360 */     int k = (int)(j * (1.0F + f) + 0.5F);
/*     */     
/* 362 */     if (k < 1)
/*     */     {
/* 364 */       k = 1;
/*     */     }
/*     */     
/* 367 */     List<EnchantmentData> list = null;
/* 368 */     Map<Integer, EnchantmentData> map = mapEnchantmentData(k, itemStackIn);
/*     */     
/* 370 */     if (map != null && !map.isEmpty()) {
/*     */       
/* 372 */       EnchantmentData enchantmentdata = (EnchantmentData)WeightedRandom.getRandomItem(randomIn, map.values());
/*     */       
/* 374 */       if (enchantmentdata != null) {
/*     */         
/* 376 */         list = Lists.newArrayList();
/* 377 */         list.add(enchantmentdata);
/*     */         int l;
/* 379 */         for (l = k; randomIn.nextInt(50) <= l; l >>= 1) {
/*     */           
/* 381 */           Iterator<Integer> iterator = map.keySet().iterator();
/*     */           
/* 383 */           while (iterator.hasNext()) {
/*     */             
/* 385 */             Integer integer = iterator.next();
/* 386 */             boolean flag = true;
/*     */             
/* 388 */             for (EnchantmentData enchantmentdata1 : list) {
/*     */               
/* 390 */               if (!enchantmentdata1.enchantmentobj.canApplyTogether(Enchantment.getEnchantmentById(integer.intValue()))) {
/*     */                 
/* 392 */                 flag = false;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/* 397 */             if (!flag)
/*     */             {
/* 399 */               iterator.remove();
/*     */             }
/*     */           } 
/*     */           
/* 403 */           if (!map.isEmpty()) {
/*     */             
/* 405 */             EnchantmentData enchantmentdata2 = (EnchantmentData)WeightedRandom.getRandomItem(randomIn, map.values());
/* 406 */             list.add(enchantmentdata2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 412 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<Integer, EnchantmentData> mapEnchantmentData(int p_77505_0_, ItemStack p_77505_1_) {
/* 418 */     Item item = p_77505_1_.getItem();
/* 419 */     Map<Integer, EnchantmentData> map = null;
/* 420 */     boolean flag = (p_77505_1_.getItem() == Items.book);
/*     */     
/* 422 */     for (Enchantment enchantment : Enchantment.enchantmentsBookList) {
/*     */       
/* 424 */       if (enchantment != null && (enchantment.type.canEnchantItem(item) || flag))
/*     */       {
/* 426 */         for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++) {
/*     */           
/* 428 */           if (p_77505_0_ >= enchantment.getMinEnchantability(i) && p_77505_0_ <= enchantment.getMaxEnchantability(i)) {
/*     */             
/* 430 */             if (map == null)
/*     */             {
/* 432 */               map = Maps.newHashMap();
/*     */             }
/*     */             
/* 435 */             map.put(Integer.valueOf(enchantment.effectId), new EnchantmentData(enchantment, i));
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 441 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DamageIterator
/*     */     implements IModifier
/*     */   {
/*     */     public EntityLivingBase user;
/*     */     
/*     */     public Entity target;
/*     */     
/*     */     private DamageIterator() {}
/*     */     
/*     */     public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
/* 455 */       enchantmentIn.onEntityDamaged(this.user, this.target, enchantmentLevel);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class HurtIterator
/*     */     implements IModifier
/*     */   {
/*     */     public EntityLivingBase user;
/*     */     
/*     */     public Entity attacker;
/*     */     
/*     */     private HurtIterator() {}
/*     */     
/*     */     public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
/* 470 */       enchantmentIn.onUserHurt(this.user, this.attacker, enchantmentLevel);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static interface IModifier
/*     */   {
/*     */     void calculateModifier(Enchantment param1Enchantment, int param1Int);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ModifierDamage
/*     */     implements IModifier
/*     */   {
/*     */     public int damageModifier;
/*     */     public DamageSource source;
/*     */     
/*     */     private ModifierDamage() {}
/*     */     
/*     */     public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
/* 490 */       this.damageModifier += enchantmentIn.calcModifierDamage(enchantmentLevel, this.source);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ModifierLiving
/*     */     implements IModifier
/*     */   {
/*     */     public float livingModifier;
/*     */     
/*     */     public EnumCreatureAttribute entityLiving;
/*     */     
/*     */     private ModifierLiving() {}
/*     */     
/*     */     public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
/* 505 */       this.livingModifier += enchantmentIn.calcDamageByCreature(enchantmentLevel, this.entityLiving);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\enchantment\EnchantmentHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */