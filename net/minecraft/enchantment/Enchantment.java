/*     */ package net.minecraft.enchantment;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.StatCollector;
/*     */ 
/*     */ public abstract class Enchantment
/*     */ {
/*  18 */   private static final Enchantment[] enchantmentsList = new Enchantment[256];
/*     */   public static final Enchantment[] enchantmentsBookList;
/*  20 */   private static final Map<ResourceLocation, Enchantment> locationEnchantments = Maps.newHashMap();
/*  21 */   public static final Enchantment protection = new EnchantmentProtection(0, new ResourceLocation("protection"), 10, 0);
/*  22 */   public static final Enchantment fireProtection = new EnchantmentProtection(1, new ResourceLocation("fire_protection"), 5, 1);
/*  23 */   public static final Enchantment featherFalling = new EnchantmentProtection(2, new ResourceLocation("feather_falling"), 5, 2);
/*  24 */   public static final Enchantment blastProtection = new EnchantmentProtection(3, new ResourceLocation("blast_protection"), 2, 3);
/*  25 */   public static final Enchantment projectileProtection = new EnchantmentProtection(4, new ResourceLocation("projectile_protection"), 5, 4);
/*  26 */   public static final Enchantment respiration = new EnchantmentOxygen(5, new ResourceLocation("respiration"), 2);
/*  27 */   public static final Enchantment aquaAffinity = new EnchantmentWaterWorker(6, new ResourceLocation("aqua_affinity"), 2);
/*  28 */   public static final Enchantment thorns = new EnchantmentThorns(7, new ResourceLocation("thorns"), 1);
/*  29 */   public static final Enchantment depthStrider = new EnchantmentWaterWalker(8, new ResourceLocation("depth_strider"), 2);
/*  30 */   public static final Enchantment sharpness = new EnchantmentDamage(16, new ResourceLocation("sharpness"), 10, 0);
/*  31 */   public static final Enchantment smite = new EnchantmentDamage(17, new ResourceLocation("smite"), 5, 1);
/*  32 */   public static final Enchantment baneOfArthropods = new EnchantmentDamage(18, new ResourceLocation("bane_of_arthropods"), 5, 2);
/*  33 */   public static final Enchantment knockback = new EnchantmentKnockback(19, new ResourceLocation("knockback"), 5);
/*  34 */   public static final Enchantment fireAspect = new EnchantmentFireAspect(20, new ResourceLocation("fire_aspect"), 2);
/*  35 */   public static final Enchantment looting = new EnchantmentLootBonus(21, new ResourceLocation("looting"), 2, EnumEnchantmentType.WEAPON);
/*  36 */   public static final Enchantment efficiency = new EnchantmentDigging(32, new ResourceLocation("efficiency"), 10);
/*  37 */   public static final Enchantment silkTouch = new EnchantmentUntouching(33, new ResourceLocation("silk_touch"), 1);
/*  38 */   public static final Enchantment unbreaking = new EnchantmentDurability(34, new ResourceLocation("unbreaking"), 5);
/*  39 */   public static final Enchantment fortune = new EnchantmentLootBonus(35, new ResourceLocation("fortune"), 2, EnumEnchantmentType.DIGGER);
/*  40 */   public static final Enchantment power = new EnchantmentArrowDamage(48, new ResourceLocation("power"), 10);
/*  41 */   public static final Enchantment punch = new EnchantmentArrowKnockback(49, new ResourceLocation("punch"), 2);
/*  42 */   public static final Enchantment flame = new EnchantmentArrowFire(50, new ResourceLocation("flame"), 2);
/*  43 */   public static final Enchantment infinity = new EnchantmentArrowInfinite(51, new ResourceLocation("infinity"), 1);
/*  44 */   public static final Enchantment luckOfTheSea = new EnchantmentLootBonus(61, new ResourceLocation("luck_of_the_sea"), 2, EnumEnchantmentType.FISHING_ROD);
/*  45 */   public static final Enchantment lure = new EnchantmentFishingSpeed(62, new ResourceLocation("lure"), 2, EnumEnchantmentType.FISHING_ROD);
/*     */   
/*     */   public final int effectId;
/*     */   private final int weight;
/*     */   public EnumEnchantmentType type;
/*     */   protected String name;
/*     */   
/*     */   public static Enchantment getEnchantmentById(int enchID) {
/*  53 */     return (enchID >= 0 && enchID < enchantmentsList.length) ? enchantmentsList[enchID] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Enchantment(int enchID, ResourceLocation enchName, int enchWeight, EnumEnchantmentType enchType) {
/*  58 */     this.effectId = enchID;
/*  59 */     this.weight = enchWeight;
/*  60 */     this.type = enchType;
/*     */     
/*  62 */     if (enchantmentsList[enchID] != null)
/*     */     {
/*  64 */       throw new IllegalArgumentException("Duplicate enchantment id!");
/*     */     }
/*     */ 
/*     */     
/*  68 */     enchantmentsList[enchID] = this;
/*  69 */     locationEnchantments.put(enchName, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Enchantment getEnchantmentByLocation(String location) {
/*  75 */     return locationEnchantments.get(new ResourceLocation(location));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<ResourceLocation> func_181077_c() {
/*  80 */     return locationEnchantments.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeight() {
/*  85 */     return this.weight;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinLevel() {
/*  90 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxLevel() {
/*  95 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinEnchantability(int enchantmentLevel) {
/* 100 */     return 1 + enchantmentLevel * 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxEnchantability(int enchantmentLevel) {
/* 105 */     return getMinEnchantability(enchantmentLevel) + 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public int calcModifierDamage(int level, DamageSource source) {
/* 110 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
/* 115 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canApplyTogether(Enchantment ench) {
/* 120 */     return (this != ench);
/*     */   }
/*     */ 
/*     */   
/*     */   public Enchantment setName(String enchName) {
/* 125 */     this.name = enchName;
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 131 */     return "enchantment." + this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTranslatedName(int level) {
/* 136 */     String s = StatCollector.translateToLocal(getName());
/* 137 */     return s + " " + StatCollector.translateToLocal("enchantment.level." + level);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canApply(ItemStack stack) {
/* 142 */     return this.type.canEnchantItem(stack.getItem());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUserHurt(EntityLivingBase user, Entity attacker, int level) {}
/*     */ 
/*     */   
/*     */   static {
/* 155 */     List<Enchantment> list = Lists.newArrayList();
/*     */     
/* 157 */     for (Enchantment enchantment : enchantmentsList) {
/*     */       
/* 159 */       if (enchantment != null)
/*     */       {
/* 161 */         list.add(enchantment);
/*     */       }
/*     */     } 
/*     */     
/* 165 */     enchantmentsBookList = list.<Enchantment>toArray(new Enchantment[list.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\enchantment\Enchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */