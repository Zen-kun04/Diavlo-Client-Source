/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.EnumCreatureAttribute;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentDamage
/*    */   extends Enchantment
/*    */ {
/* 14 */   private static final String[] protectionName = new String[] { "all", "undead", "arthropods" };
/* 15 */   private static final int[] baseEnchantability = new int[] { 1, 5, 5 };
/* 16 */   private static final int[] levelEnchantability = new int[] { 11, 8, 8 };
/* 17 */   private static final int[] thresholdEnchantability = new int[] { 20, 20, 20 };
/*    */   
/*    */   public final int damageType;
/*    */   
/*    */   public EnchantmentDamage(int enchID, ResourceLocation enchName, int enchWeight, int classification) {
/* 22 */     super(enchID, enchName, enchWeight, EnumEnchantmentType.WEAPON);
/* 23 */     this.damageType = classification;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinEnchantability(int enchantmentLevel) {
/* 28 */     return baseEnchantability[this.damageType] + (enchantmentLevel - 1) * levelEnchantability[this.damageType];
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxEnchantability(int enchantmentLevel) {
/* 33 */     return getMinEnchantability(enchantmentLevel) + thresholdEnchantability[this.damageType];
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 38 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
/* 43 */     return (this.damageType == 0) ? (level * 1.25F) : ((this.damageType == 1 && creatureType == EnumCreatureAttribute.UNDEAD) ? (level * 2.5F) : ((this.damageType == 2 && creatureType == EnumCreatureAttribute.ARTHROPOD) ? (level * 2.5F) : 0.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 48 */     return "enchantment.damage." + protectionName[this.damageType];
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canApplyTogether(Enchantment ench) {
/* 53 */     return !(ench instanceof EnchantmentDamage);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canApply(ItemStack stack) {
/* 58 */     return (stack.getItem() instanceof net.minecraft.item.ItemAxe) ? true : super.canApply(stack);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
/* 63 */     if (target instanceof EntityLivingBase) {
/*    */       
/* 65 */       EntityLivingBase entitylivingbase = (EntityLivingBase)target;
/*    */       
/* 67 */       if (this.damageType == 2 && entitylivingbase.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
/*    */         
/* 69 */         int i = 20 + user.getRNG().nextInt(10 * level);
/* 70 */         entitylivingbase.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, i, 3));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\enchantment\EnchantmentDamage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */