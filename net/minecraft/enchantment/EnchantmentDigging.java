/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentDigging
/*    */   extends Enchantment
/*    */ {
/*    */   protected EnchantmentDigging(int enchID, ResourceLocation enchName, int enchWeight) {
/* 11 */     super(enchID, enchName, enchWeight, EnumEnchantmentType.DIGGER);
/* 12 */     setName("digging");
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinEnchantability(int enchantmentLevel) {
/* 17 */     return 1 + 10 * (enchantmentLevel - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxEnchantability(int enchantmentLevel) {
/* 22 */     return super.getMinEnchantability(enchantmentLevel) + 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 27 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canApply(ItemStack stack) {
/* 32 */     return (stack.getItem() == Items.shears) ? true : super.canApply(stack);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\enchantment\EnchantmentDigging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */