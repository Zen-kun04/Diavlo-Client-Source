/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentArrowKnockback
/*    */   extends Enchantment
/*    */ {
/*    */   public EnchantmentArrowKnockback(int enchID, ResourceLocation enchName, int enchWeight) {
/*  9 */     super(enchID, enchName, enchWeight, EnumEnchantmentType.BOW);
/* 10 */     setName("arrowKnockback");
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinEnchantability(int enchantmentLevel) {
/* 15 */     return 12 + (enchantmentLevel - 1) * 20;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxEnchantability(int enchantmentLevel) {
/* 20 */     return getMinEnchantability(enchantmentLevel) + 25;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 25 */     return 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\enchantment\EnchantmentArrowKnockback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */