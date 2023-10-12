/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum EnumEnchantmentType
/*    */ {
/* 12 */   ALL,
/* 13 */   ARMOR,
/* 14 */   ARMOR_FEET,
/* 15 */   ARMOR_LEGS,
/* 16 */   ARMOR_TORSO,
/* 17 */   ARMOR_HEAD,
/* 18 */   WEAPON,
/* 19 */   DIGGER,
/* 20 */   FISHING_ROD,
/* 21 */   BREAKABLE,
/* 22 */   BOW;
/*    */ 
/*    */   
/*    */   public boolean canEnchantItem(Item p_77557_1_) {
/* 26 */     if (this == ALL)
/*    */     {
/* 28 */       return true;
/*    */     }
/* 30 */     if (this == BREAKABLE && p_77557_1_.isDamageable())
/*    */     {
/* 32 */       return true;
/*    */     }
/* 34 */     if (p_77557_1_ instanceof ItemArmor) {
/*    */       
/* 36 */       if (this == ARMOR)
/*    */       {
/* 38 */         return true;
/*    */       }
/*    */ 
/*    */       
/* 42 */       ItemArmor itemarmor = (ItemArmor)p_77557_1_;
/* 43 */       return (itemarmor.armorType == 0) ? ((this == ARMOR_HEAD)) : ((itemarmor.armorType == 2) ? ((this == ARMOR_LEGS)) : ((itemarmor.armorType == 1) ? ((this == ARMOR_TORSO)) : ((itemarmor.armorType == 3) ? ((this == ARMOR_FEET)) : false)));
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 48 */     return (p_77557_1_ instanceof net.minecraft.item.ItemSword) ? ((this == WEAPON)) : ((p_77557_1_ instanceof net.minecraft.item.ItemTool) ? ((this == DIGGER)) : ((p_77557_1_ instanceof net.minecraft.item.ItemBow) ? ((this == BOW)) : ((p_77557_1_ instanceof net.minecraft.item.ItemFishingRod) ? ((this == FISHING_ROD)) : false)));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\enchantment\EnumEnchantmentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */