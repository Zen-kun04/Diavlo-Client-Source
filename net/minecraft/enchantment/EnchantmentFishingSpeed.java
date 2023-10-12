/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentFishingSpeed
/*    */   extends Enchantment
/*    */ {
/*    */   protected EnchantmentFishingSpeed(int enchID, ResourceLocation enchName, int enchWeight, EnumEnchantmentType enchType) {
/*  9 */     super(enchID, enchName, enchWeight, enchType);
/* 10 */     setName("fishingSpeed");
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinEnchantability(int enchantmentLevel) {
/* 15 */     return 15 + (enchantmentLevel - 1) * 9;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxEnchantability(int enchantmentLevel) {
/* 20 */     return super.getMinEnchantability(enchantmentLevel) + 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 25 */     return 3;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\enchantment\EnchantmentFishingSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */