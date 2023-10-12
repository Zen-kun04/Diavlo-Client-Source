/*     */ package net.minecraft.potion;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.StringUtils;
/*     */ 
/*     */ 
/*     */ public class Potion
/*     */ {
/*  22 */   public static final Potion[] potionTypes = new Potion[32];
/*  23 */   private static final Map<ResourceLocation, Potion> field_180150_I = Maps.newHashMap();
/*  24 */   public static final Potion field_180151_b = null;
/*  25 */   public static final Potion moveSpeed = (new Potion(1, new ResourceLocation("speed"), false, 8171462)).setPotionName("potion.moveSpeed").setIconIndex(0, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224D, 2);
/*  26 */   public static final Potion moveSlowdown = (new Potion(2, new ResourceLocation("slowness"), true, 5926017)).setPotionName("potion.moveSlowdown").setIconIndex(1, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448D, 2);
/*  27 */   public static final Potion digSpeed = (new Potion(3, new ResourceLocation("haste"), false, 14270531)).setPotionName("potion.digSpeed").setIconIndex(2, 0).setEffectiveness(1.5D);
/*  28 */   public static final Potion digSlowdown = (new Potion(4, new ResourceLocation("mining_fatigue"), true, 4866583)).setPotionName("potion.digSlowDown").setIconIndex(3, 0);
/*  29 */   public static final Potion damageBoost = (new PotionAttackDamage(5, new ResourceLocation("strength"), false, 9643043)).setPotionName("potion.damageBoost").setIconIndex(4, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5D, 2);
/*  30 */   public static final Potion heal = (new PotionHealth(6, new ResourceLocation("instant_health"), false, 16262179)).setPotionName("potion.heal");
/*  31 */   public static final Potion harm = (new PotionHealth(7, new ResourceLocation("instant_damage"), true, 4393481)).setPotionName("potion.harm");
/*  32 */   public static final Potion jump = (new Potion(8, new ResourceLocation("jump_boost"), false, 2293580)).setPotionName("potion.jump").setIconIndex(2, 1);
/*  33 */   public static final Potion confusion = (new Potion(9, new ResourceLocation("nausea"), true, 5578058)).setPotionName("potion.confusion").setIconIndex(3, 1).setEffectiveness(0.25D);
/*  34 */   public static final Potion regeneration = (new Potion(10, new ResourceLocation("regeneration"), false, 13458603)).setPotionName("potion.regeneration").setIconIndex(7, 0).setEffectiveness(0.25D);
/*  35 */   public static final Potion resistance = (new Potion(11, new ResourceLocation("resistance"), false, 10044730)).setPotionName("potion.resistance").setIconIndex(6, 1);
/*  36 */   public static final Potion fireResistance = (new Potion(12, new ResourceLocation("fire_resistance"), false, 14981690)).setPotionName("potion.fireResistance").setIconIndex(7, 1);
/*  37 */   public static final Potion waterBreathing = (new Potion(13, new ResourceLocation("water_breathing"), false, 3035801)).setPotionName("potion.waterBreathing").setIconIndex(0, 2);
/*  38 */   public static final Potion invisibility = (new Potion(14, new ResourceLocation("invisibility"), false, 8356754)).setPotionName("potion.invisibility").setIconIndex(0, 1);
/*  39 */   public static final Potion blindness = (new Potion(15, new ResourceLocation("blindness"), true, 2039587)).setPotionName("potion.blindness").setIconIndex(5, 1).setEffectiveness(0.25D);
/*  40 */   public static final Potion nightVision = (new Potion(16, new ResourceLocation("night_vision"), false, 2039713)).setPotionName("potion.nightVision").setIconIndex(4, 1);
/*  41 */   public static final Potion hunger = (new Potion(17, new ResourceLocation("hunger"), true, 5797459)).setPotionName("potion.hunger").setIconIndex(1, 1);
/*  42 */   public static final Potion weakness = (new PotionAttackDamage(18, new ResourceLocation("weakness"), true, 4738376)).setPotionName("potion.weakness").setIconIndex(5, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0D, 0);
/*  43 */   public static final Potion poison = (new Potion(19, new ResourceLocation("poison"), true, 5149489)).setPotionName("potion.poison").setIconIndex(6, 0).setEffectiveness(0.25D);
/*  44 */   public static final Potion wither = (new Potion(20, new ResourceLocation("wither"), true, 3484199)).setPotionName("potion.wither").setIconIndex(1, 2).setEffectiveness(0.25D);
/*  45 */   public static final Potion healthBoost = (new PotionHealthBoost(21, new ResourceLocation("health_boost"), false, 16284963)).setPotionName("potion.healthBoost").setIconIndex(2, 2).registerPotionAttributeModifier(SharedMonsterAttributes.maxHealth, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0D, 0);
/*  46 */   public static final Potion absorption = (new PotionAbsorption(22, new ResourceLocation("absorption"), false, 2445989)).setPotionName("potion.absorption").setIconIndex(2, 2);
/*  47 */   public static final Potion saturation = (new PotionHealth(23, new ResourceLocation("saturation"), false, 16262179)).setPotionName("potion.saturation");
/*  48 */   public static final Potion field_180153_z = null;
/*  49 */   public static final Potion field_180147_A = null;
/*  50 */   public static final Potion field_180148_B = null;
/*  51 */   public static final Potion field_180149_C = null;
/*  52 */   public static final Potion field_180143_D = null;
/*  53 */   public static final Potion field_180144_E = null;
/*  54 */   public static final Potion field_180145_F = null;
/*  55 */   public static final Potion field_180146_G = null;
/*     */   public final int id;
/*  57 */   private final Map<IAttribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
/*     */   private final boolean isBadEffect;
/*     */   private final int liquidColor;
/*  60 */   private String name = "";
/*  61 */   private int statusIconIndex = -1;
/*     */   
/*     */   private double effectiveness;
/*     */   private boolean usable;
/*     */   
/*     */   protected Potion(int potionID, ResourceLocation location, boolean badEffect, int potionColor) {
/*  67 */     this.id = potionID;
/*  68 */     potionTypes[potionID] = this;
/*  69 */     field_180150_I.put(location, this);
/*  70 */     this.isBadEffect = badEffect;
/*     */     
/*  72 */     if (badEffect) {
/*     */       
/*  74 */       this.effectiveness = 0.5D;
/*     */     }
/*     */     else {
/*     */       
/*  78 */       this.effectiveness = 1.0D;
/*     */     } 
/*     */     
/*  81 */     this.liquidColor = potionColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Potion getPotionFromResourceLocation(String location) {
/*  86 */     return field_180150_I.get(new ResourceLocation(location));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<ResourceLocation> getPotionLocations() {
/*  91 */     return field_180150_I.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Potion setIconIndex(int p_76399_1_, int p_76399_2_) {
/*  96 */     this.statusIconIndex = p_76399_1_ + p_76399_2_ * 8;
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId() {
/* 102 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {
/* 107 */     if (this.id == regeneration.id) {
/*     */       
/* 109 */       if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth())
/*     */       {
/* 111 */         entityLivingBaseIn.heal(1.0F);
/*     */       }
/*     */     }
/* 114 */     else if (this.id == poison.id) {
/*     */       
/* 116 */       if (entityLivingBaseIn.getHealth() > 1.0F)
/*     */       {
/* 118 */         entityLivingBaseIn.attackEntityFrom(DamageSource.magic, 1.0F);
/*     */       }
/*     */     }
/* 121 */     else if (this.id == wither.id) {
/*     */       
/* 123 */       entityLivingBaseIn.attackEntityFrom(DamageSource.wither, 1.0F);
/*     */     }
/* 125 */     else if (this.id == hunger.id && entityLivingBaseIn instanceof EntityPlayer) {
/*     */       
/* 127 */       ((EntityPlayer)entityLivingBaseIn).addExhaustion(0.025F * (p_76394_2_ + 1));
/*     */     }
/* 129 */     else if (this.id == saturation.id && entityLivingBaseIn instanceof EntityPlayer) {
/*     */       
/* 131 */       if (!entityLivingBaseIn.worldObj.isRemote)
/*     */       {
/* 133 */         ((EntityPlayer)entityLivingBaseIn).getFoodStats().addStats(p_76394_2_ + 1, 1.0F);
/*     */       }
/*     */     }
/* 136 */     else if ((this.id != heal.id || entityLivingBaseIn.isEntityUndead()) && (this.id != harm.id || !entityLivingBaseIn.isEntityUndead())) {
/*     */       
/* 138 */       if ((this.id == harm.id && !entityLivingBaseIn.isEntityUndead()) || (this.id == heal.id && entityLivingBaseIn.isEntityUndead()))
/*     */       {
/* 140 */         entityLivingBaseIn.attackEntityFrom(DamageSource.magic, (6 << p_76394_2_));
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 145 */       entityLivingBaseIn.heal(Math.max(4 << p_76394_2_, 0));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void affectEntity(Entity p_180793_1_, Entity p_180793_2_, EntityLivingBase entityLivingBaseIn, int p_180793_4_, double p_180793_5_) {
/* 151 */     if ((this.id != heal.id || entityLivingBaseIn.isEntityUndead()) && (this.id != harm.id || !entityLivingBaseIn.isEntityUndead())) {
/*     */       
/* 153 */       if ((this.id == harm.id && !entityLivingBaseIn.isEntityUndead()) || (this.id == heal.id && entityLivingBaseIn.isEntityUndead())) {
/*     */         
/* 155 */         int j = (int)(p_180793_5_ * (6 << p_180793_4_) + 0.5D);
/*     */         
/* 157 */         if (p_180793_1_ == null)
/*     */         {
/* 159 */           entityLivingBaseIn.attackEntityFrom(DamageSource.magic, j);
/*     */         }
/*     */         else
/*     */         {
/* 163 */           entityLivingBaseIn.attackEntityFrom(DamageSource.causeIndirectMagicDamage(p_180793_1_, p_180793_2_), j);
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 169 */       int i = (int)(p_180793_5_ * (4 << p_180793_4_) + 0.5D);
/* 170 */       entityLivingBaseIn.heal(i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInstant() {
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReady(int p_76397_1_, int p_76397_2_) {
/* 181 */     if (this.id == regeneration.id) {
/*     */       
/* 183 */       int k = 50 >> p_76397_2_;
/* 184 */       return (k > 0) ? ((p_76397_1_ % k == 0)) : true;
/*     */     } 
/* 186 */     if (this.id == poison.id) {
/*     */       
/* 188 */       int j = 25 >> p_76397_2_;
/* 189 */       return (j > 0) ? ((p_76397_1_ % j == 0)) : true;
/*     */     } 
/* 191 */     if (this.id == wither.id) {
/*     */       
/* 193 */       int i = 40 >> p_76397_2_;
/* 194 */       return (i > 0) ? ((p_76397_1_ % i == 0)) : true;
/*     */     } 
/*     */ 
/*     */     
/* 198 */     return (this.id == hunger.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Potion setPotionName(String nameIn) {
/* 204 */     this.name = nameIn;
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 210 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasStatusIcon() {
/* 215 */     return (this.statusIconIndex >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStatusIconIndex() {
/* 220 */     return this.statusIconIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBadEffect() {
/* 225 */     return this.isBadEffect;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getDurationString(PotionEffect effect) {
/* 230 */     if (effect.getIsPotionDurationMax())
/*     */     {
/* 232 */       return "**:**";
/*     */     }
/*     */ 
/*     */     
/* 236 */     int i = effect.getDuration();
/* 237 */     return StringUtils.ticksToElapsedTime(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Potion setEffectiveness(double effectivenessIn) {
/* 243 */     this.effectiveness = effectivenessIn;
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getEffectiveness() {
/* 249 */     return this.effectiveness;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsable() {
/* 254 */     return this.usable;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLiquidColor() {
/* 259 */     return this.liquidColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public Potion registerPotionAttributeModifier(IAttribute p_111184_1_, String p_111184_2_, double p_111184_3_, int p_111184_5_) {
/* 264 */     AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(p_111184_2_), getName(), p_111184_3_, p_111184_5_);
/* 265 */     this.attributeModifierMap.put(p_111184_1_, attributemodifier);
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<IAttribute, AttributeModifier> getAttributeModifierMap() {
/* 271 */     return this.attributeModifierMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, BaseAttributeMap p_111187_2_, int amplifier) {
/* 276 */     for (Map.Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
/*     */       
/* 278 */       IAttributeInstance iattributeinstance = p_111187_2_.getAttributeInstance(entry.getKey());
/*     */       
/* 280 */       if (iattributeinstance != null)
/*     */       {
/* 282 */         iattributeinstance.removeModifier(entry.getValue());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, BaseAttributeMap p_111185_2_, int amplifier) {
/* 289 */     for (Map.Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
/*     */       
/* 291 */       IAttributeInstance iattributeinstance = p_111185_2_.getAttributeInstance(entry.getKey());
/*     */       
/* 293 */       if (iattributeinstance != null) {
/*     */         
/* 295 */         AttributeModifier attributemodifier = entry.getValue();
/* 296 */         iattributeinstance.removeModifier(attributemodifier);
/* 297 */         iattributeinstance.applyModifier(new AttributeModifier(attributemodifier.getID(), getName() + " " + amplifier, getAttributeModifierAmount(amplifier, attributemodifier), attributemodifier.getOperation()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAttributeModifierAmount(int p_111183_1_, AttributeModifier modifier) {
/* 304 */     return modifier.getAmount() * (p_111183_1_ + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\potion\Potion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */