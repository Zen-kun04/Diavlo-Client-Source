/*     */ package net.minecraft.item;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemBow extends Item {
/*  14 */   public static final String[] bowPullIconNameArray = new String[] { "pulling_0", "pulling_1", "pulling_2" };
/*     */ 
/*     */   
/*     */   public ItemBow() {
/*  18 */     this.maxStackSize = 1;
/*  19 */     setMaxDamage(384);
/*  20 */     setCreativeTab(CreativeTabs.tabCombat);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
/*  25 */     boolean flag = (playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0);
/*     */     
/*  27 */     if (flag || playerIn.inventory.hasItem(Items.arrow)) {
/*     */       
/*  29 */       int i = getMaxItemUseDuration(stack) - timeLeft;
/*  30 */       float f = i / 20.0F;
/*  31 */       f = (f * f + f * 2.0F) / 3.0F;
/*     */       
/*  33 */       if (f < 0.1D) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  38 */       if (f > 1.0F)
/*     */       {
/*  40 */         f = 1.0F;
/*     */       }
/*     */       
/*  43 */       EntityArrow entityarrow = new EntityArrow(worldIn, (EntityLivingBase)playerIn, f * 2.0F);
/*     */       
/*  45 */       if (f == 1.0F)
/*     */       {
/*  47 */         entityarrow.setIsCritical(true);
/*     */       }
/*     */       
/*  50 */       int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
/*     */       
/*  52 */       if (j > 0)
/*     */       {
/*  54 */         entityarrow.setDamage(entityarrow.getDamage() + j * 0.5D + 0.5D);
/*     */       }
/*     */       
/*  57 */       int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
/*     */       
/*  59 */       if (k > 0)
/*     */       {
/*  61 */         entityarrow.setKnockbackStrength(k);
/*     */       }
/*     */       
/*  64 */       if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
/*     */       {
/*  66 */         entityarrow.setFire(100);
/*     */       }
/*     */       
/*  69 */       stack.damageItem(1, (EntityLivingBase)playerIn);
/*  70 */       worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
/*     */       
/*  72 */       if (flag) {
/*     */         
/*  74 */         entityarrow.canBePickedUp = 2;
/*     */       }
/*     */       else {
/*     */         
/*  78 */         playerIn.inventory.consumeInventoryItem(Items.arrow);
/*     */       } 
/*     */       
/*  81 */       playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*     */       
/*  83 */       if (!worldIn.isRemote)
/*     */       {
/*  85 */         worldIn.spawnEntityInWorld((Entity)entityarrow);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
/*  92 */     return stack;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxItemUseDuration(ItemStack stack) {
/*  97 */     return 72000;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumAction getItemUseAction(ItemStack stack) {
/* 102 */     return EnumAction.BOW;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 107 */     if (playerIn.capabilities.isCreativeMode || playerIn.inventory.hasItem(Items.arrow))
/*     */     {
/* 109 */       playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/*     */     }
/*     */     
/* 112 */     return itemStackIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getItemEnchantability() {
/* 117 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemBow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */