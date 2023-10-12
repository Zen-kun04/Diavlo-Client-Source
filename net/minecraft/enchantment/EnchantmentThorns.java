/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ 
/*    */ public class EnchantmentThorns
/*    */   extends Enchantment
/*    */ {
/*    */   public EnchantmentThorns(int p_i45764_1_, ResourceLocation p_i45764_2_, int p_i45764_3_) {
/* 15 */     super(p_i45764_1_, p_i45764_2_, p_i45764_3_, EnumEnchantmentType.ARMOR_TORSO);
/* 16 */     setName("thorns");
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinEnchantability(int enchantmentLevel) {
/* 21 */     return 10 + 20 * (enchantmentLevel - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxEnchantability(int enchantmentLevel) {
/* 26 */     return super.getMinEnchantability(enchantmentLevel) + 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 31 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canApply(ItemStack stack) {
/* 36 */     return (stack.getItem() instanceof net.minecraft.item.ItemArmor) ? true : super.canApply(stack);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUserHurt(EntityLivingBase user, Entity attacker, int level) {
/* 41 */     Random random = user.getRNG();
/* 42 */     ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantment.thorns, user);
/*    */     
/* 44 */     if (func_92094_a(level, random)) {
/*    */       
/* 46 */       if (attacker != null) {
/*    */         
/* 48 */         attacker.attackEntityFrom(DamageSource.causeThornsDamage((Entity)user), func_92095_b(level, random));
/* 49 */         attacker.playSound("damage.thorns", 0.5F, 1.0F);
/*    */       } 
/*    */       
/* 52 */       if (itemstack != null)
/*    */       {
/* 54 */         itemstack.damageItem(3, user);
/*    */       }
/*    */     }
/* 57 */     else if (itemstack != null) {
/*    */       
/* 59 */       itemstack.damageItem(1, user);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean func_92094_a(int p_92094_0_, Random p_92094_1_) {
/* 65 */     return (p_92094_0_ <= 0) ? false : ((p_92094_1_.nextFloat() < 0.15F * p_92094_0_));
/*    */   }
/*    */ 
/*    */   
/*    */   public static int func_92095_b(int p_92095_0_, Random p_92095_1_) {
/* 70 */     return (p_92095_0_ > 10) ? (p_92095_0_ - 10) : (1 + p_92095_1_.nextInt(4));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\enchantment\EnchantmentThorns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */