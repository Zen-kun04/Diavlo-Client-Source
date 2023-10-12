/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityFireball;
/*     */ import net.minecraft.world.Explosion;
/*     */ 
/*     */ public class DamageSource
/*     */ {
/*  12 */   public static DamageSource inFire = (new DamageSource("inFire")).setFireDamage();
/*  13 */   public static DamageSource lightningBolt = new DamageSource("lightningBolt");
/*  14 */   public static DamageSource onFire = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
/*  15 */   public static DamageSource lava = (new DamageSource("lava")).setFireDamage();
/*  16 */   public static DamageSource inWall = (new DamageSource("inWall")).setDamageBypassesArmor();
/*  17 */   public static DamageSource drown = (new DamageSource("drown")).setDamageBypassesArmor();
/*  18 */   public static DamageSource starve = (new DamageSource("starve")).setDamageBypassesArmor().setDamageIsAbsolute();
/*  19 */   public static DamageSource cactus = new DamageSource("cactus");
/*  20 */   public static DamageSource fall = (new DamageSource("fall")).setDamageBypassesArmor();
/*  21 */   public static DamageSource outOfWorld = (new DamageSource("outOfWorld")).setDamageBypassesArmor().setDamageAllowedInCreativeMode();
/*  22 */   public static DamageSource generic = (new DamageSource("generic")).setDamageBypassesArmor();
/*  23 */   public static DamageSource magic = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
/*  24 */   public static DamageSource wither = (new DamageSource("wither")).setDamageBypassesArmor();
/*  25 */   public static DamageSource anvil = new DamageSource("anvil");
/*  26 */   public static DamageSource fallingBlock = new DamageSource("fallingBlock");
/*     */   private boolean isUnblockable;
/*     */   private boolean isDamageAllowedInCreativeMode;
/*     */   private boolean damageIsAbsolute;
/*  30 */   private float hungerDamage = 0.3F;
/*     */   
/*     */   private boolean fireDamage;
/*     */   private boolean projectile;
/*     */   private boolean difficultyScaled;
/*     */   private boolean magicDamage;
/*     */   private boolean explosion;
/*     */   public String damageType;
/*     */   
/*     */   public static DamageSource causeMobDamage(EntityLivingBase mob) {
/*  40 */     return new EntityDamageSource("mob", (Entity)mob);
/*     */   }
/*     */ 
/*     */   
/*     */   public static DamageSource causePlayerDamage(EntityPlayer player) {
/*  45 */     return new EntityDamageSource("player", (Entity)player);
/*     */   }
/*     */ 
/*     */   
/*     */   public static DamageSource causeArrowDamage(EntityArrow arrow, Entity indirectEntityIn) {
/*  50 */     return (new EntityDamageSourceIndirect("arrow", (Entity)arrow, indirectEntityIn)).setProjectile();
/*     */   }
/*     */ 
/*     */   
/*     */   public static DamageSource causeFireballDamage(EntityFireball fireball, Entity indirectEntityIn) {
/*  55 */     return (indirectEntityIn == null) ? (new EntityDamageSourceIndirect("onFire", (Entity)fireball, (Entity)fireball)).setFireDamage().setProjectile() : (new EntityDamageSourceIndirect("fireball", (Entity)fireball, indirectEntityIn)).setFireDamage().setProjectile();
/*     */   }
/*     */ 
/*     */   
/*     */   public static DamageSource causeThrownDamage(Entity source, Entity indirectEntityIn) {
/*  60 */     return (new EntityDamageSourceIndirect("thrown", source, indirectEntityIn)).setProjectile();
/*     */   }
/*     */ 
/*     */   
/*     */   public static DamageSource causeIndirectMagicDamage(Entity source, Entity indirectEntityIn) {
/*  65 */     return (new EntityDamageSourceIndirect("indirectMagic", source, indirectEntityIn)).setDamageBypassesArmor().setMagicDamage();
/*     */   }
/*     */ 
/*     */   
/*     */   public static DamageSource causeThornsDamage(Entity source) {
/*  70 */     return (new EntityDamageSource("thorns", source)).setIsThornsDamage().setMagicDamage();
/*     */   }
/*     */ 
/*     */   
/*     */   public static DamageSource setExplosionSource(Explosion explosionIn) {
/*  75 */     return (explosionIn != null && explosionIn.getExplosivePlacedBy() != null) ? (new EntityDamageSource("explosion.player", (Entity)explosionIn.getExplosivePlacedBy())).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProjectile() {
/*  80 */     return this.projectile;
/*     */   }
/*     */ 
/*     */   
/*     */   public DamageSource setProjectile() {
/*  85 */     this.projectile = true;
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExplosion() {
/*  91 */     return this.explosion;
/*     */   }
/*     */ 
/*     */   
/*     */   public DamageSource setExplosion() {
/*  96 */     this.explosion = true;
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnblockable() {
/* 102 */     return this.isUnblockable;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHungerDamage() {
/* 107 */     return this.hungerDamage;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHarmInCreative() {
/* 112 */     return this.isDamageAllowedInCreativeMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDamageAbsolute() {
/* 117 */     return this.damageIsAbsolute;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DamageSource(String damageTypeIn) {
/* 122 */     this.damageType = damageTypeIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getSourceOfDamage() {
/* 127 */     return getEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getEntity() {
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DamageSource setDamageBypassesArmor() {
/* 137 */     this.isUnblockable = true;
/* 138 */     this.hungerDamage = 0.0F;
/* 139 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DamageSource setDamageAllowedInCreativeMode() {
/* 144 */     this.isDamageAllowedInCreativeMode = true;
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DamageSource setDamageIsAbsolute() {
/* 150 */     this.damageIsAbsolute = true;
/* 151 */     this.hungerDamage = 0.0F;
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DamageSource setFireDamage() {
/* 157 */     this.fireDamage = true;
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
/* 163 */     EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
/* 164 */     String s = "death.attack." + this.damageType;
/* 165 */     String s1 = s + ".player";
/* 166 */     return (entitylivingbase != null && StatCollector.canTranslate(s1)) ? new ChatComponentTranslation(s1, new Object[] { entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName() }) : new ChatComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFireDamage() {
/* 171 */     return this.fireDamage;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDamageType() {
/* 176 */     return this.damageType;
/*     */   }
/*     */ 
/*     */   
/*     */   public DamageSource setDifficultyScaled() {
/* 181 */     this.difficultyScaled = true;
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDifficultyScaled() {
/* 187 */     return this.difficultyScaled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMagicDamage() {
/* 192 */     return this.magicDamage;
/*     */   }
/*     */ 
/*     */   
/*     */   public DamageSource setMagicDamage() {
/* 197 */     this.magicDamage = true;
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCreativePlayer() {
/* 203 */     Entity entity = getEntity();
/* 204 */     return (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\DamageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */