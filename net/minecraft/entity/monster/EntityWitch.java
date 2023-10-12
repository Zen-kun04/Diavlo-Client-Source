/*     */ package net.minecraft.entity.monster;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIArrowAttack;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityWitch extends EntityMob implements IRangedAttackMob {
/*  32 */   private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
/*  33 */   private static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
/*  34 */   private static final Item[] witchDrops = new Item[] { Items.glowstone_dust, Items.sugar, Items.redstone, Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick };
/*     */   
/*     */   private int witchAttackTimer;
/*     */   
/*     */   public EntityWitch(World worldIn) {
/*  39 */     super(worldIn);
/*  40 */     setSize(0.6F, 1.95F);
/*  41 */     this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  42 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
/*  43 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIWander(this, 1.0D));
/*  44 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  45 */     this.tasks.addTask(3, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  46 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
/*  47 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  52 */     super.entityInit();
/*  53 */     getDataWatcher().addObject(21, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAggressive(boolean aggressive) {
/*  73 */     getDataWatcher().updateObject(21, Byte.valueOf((byte)(aggressive ? 1 : 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getAggressive() {
/*  78 */     return (getDataWatcher().getWatchableObjectByte(21) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  83 */     super.applyEntityAttributes();
/*  84 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0D);
/*  85 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/*  90 */     if (!this.worldObj.isRemote) {
/*     */       
/*  92 */       if (getAggressive()) {
/*     */         
/*  94 */         if (this.witchAttackTimer-- <= 0)
/*     */         {
/*  96 */           setAggressive(false);
/*  97 */           ItemStack itemstack = getHeldItem();
/*  98 */           setCurrentItemOrArmor(0, (ItemStack)null);
/*     */           
/* 100 */           if (itemstack != null && itemstack.getItem() == Items.potionitem) {
/*     */             
/* 102 */             List<PotionEffect> list = Items.potionitem.getEffects(itemstack);
/*     */             
/* 104 */             if (list != null)
/*     */             {
/* 106 */               for (PotionEffect potioneffect : list)
/*     */               {
/* 108 */                 addPotionEffect(new PotionEffect(potioneffect));
/*     */               }
/*     */             }
/*     */           } 
/*     */           
/* 113 */           getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(MODIFIER);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 118 */         int i = -1;
/*     */         
/* 120 */         if (this.rand.nextFloat() < 0.15F && isInsideOfMaterial(Material.water) && !isPotionActive(Potion.waterBreathing)) {
/*     */           
/* 122 */           i = 8237;
/*     */         }
/* 124 */         else if (this.rand.nextFloat() < 0.15F && isBurning() && !isPotionActive(Potion.fireResistance)) {
/*     */           
/* 126 */           i = 16307;
/*     */         }
/* 128 */         else if (this.rand.nextFloat() < 0.05F && getHealth() < getMaxHealth()) {
/*     */           
/* 130 */           i = 16341;
/*     */         }
/* 132 */         else if (this.rand.nextFloat() < 0.25F && getAttackTarget() != null && !isPotionActive(Potion.moveSpeed) && getAttackTarget().getDistanceSqToEntity((Entity)this) > 121.0D) {
/*     */           
/* 134 */           i = 16274;
/*     */         }
/* 136 */         else if (this.rand.nextFloat() < 0.25F && getAttackTarget() != null && !isPotionActive(Potion.moveSpeed) && getAttackTarget().getDistanceSqToEntity((Entity)this) > 121.0D) {
/*     */           
/* 138 */           i = 16274;
/*     */         } 
/*     */         
/* 141 */         if (i > -1) {
/*     */           
/* 143 */           setCurrentItemOrArmor(0, new ItemStack((Item)Items.potionitem, 1, i));
/* 144 */           this.witchAttackTimer = getHeldItem().getMaxItemUseDuration();
/* 145 */           setAggressive(true);
/* 146 */           IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/* 147 */           iattributeinstance.removeModifier(MODIFIER);
/* 148 */           iattributeinstance.applyModifier(MODIFIER);
/*     */         } 
/*     */       } 
/*     */       
/* 152 */       if (this.rand.nextFloat() < 7.5E-4F)
/*     */       {
/* 154 */         this.worldObj.setEntityState((Entity)this, (byte)15);
/*     */       }
/*     */     } 
/*     */     
/* 158 */     super.onLivingUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 163 */     if (id == 15) {
/*     */       
/* 165 */       for (int i = 0; i < this.rand.nextInt(35) + 10; i++)
/*     */       {
/* 167 */         this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, (getEntityBoundingBox()).maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 172 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected float applyPotionDamageCalculations(DamageSource source, float damage) {
/* 178 */     damage = super.applyPotionDamageCalculations(source, damage);
/*     */     
/* 180 */     if (source.getEntity() == this)
/*     */     {
/* 182 */       damage = 0.0F;
/*     */     }
/*     */     
/* 185 */     if (source.isMagicDamage())
/*     */     {
/* 187 */       damage = (float)(damage * 0.15D);
/*     */     }
/*     */     
/* 190 */     return damage;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 195 */     int i = this.rand.nextInt(3) + 1;
/*     */     
/* 197 */     for (int j = 0; j < i; j++) {
/*     */       
/* 199 */       int k = this.rand.nextInt(3);
/* 200 */       Item item = witchDrops[this.rand.nextInt(witchDrops.length)];
/*     */       
/* 202 */       if (lootingModifier > 0)
/*     */       {
/* 204 */         k += this.rand.nextInt(lootingModifier + 1);
/*     */       }
/*     */       
/* 207 */       for (int l = 0; l < k; l++)
/*     */       {
/* 209 */         dropItem(item, 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
/* 216 */     if (!getAggressive()) {
/*     */       
/* 218 */       EntityPotion entitypotion = new EntityPotion(this.worldObj, (EntityLivingBase)this, 32732);
/* 219 */       double d0 = target.posY + target.getEyeHeight() - 1.100000023841858D;
/* 220 */       entitypotion.rotationPitch -= -20.0F;
/* 221 */       double d1 = target.posX + target.motionX - this.posX;
/* 222 */       double d2 = d0 - this.posY;
/* 223 */       double d3 = target.posZ + target.motionZ - this.posZ;
/* 224 */       float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
/*     */       
/* 226 */       if (f >= 8.0F && !target.isPotionActive(Potion.moveSlowdown)) {
/*     */         
/* 228 */         entitypotion.setPotionDamage(32698);
/*     */       }
/* 230 */       else if (target.getHealth() >= 8.0F && !target.isPotionActive(Potion.poison)) {
/*     */         
/* 232 */         entitypotion.setPotionDamage(32660);
/*     */       }
/* 234 */       else if (f <= 3.0F && !target.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25F) {
/*     */         
/* 236 */         entitypotion.setPotionDamage(32696);
/*     */       } 
/*     */       
/* 239 */       entitypotion.setThrowableHeading(d1, d2 + (f * 0.2F), d3, 0.75F, 8.0F);
/* 240 */       this.worldObj.spawnEntityInWorld((Entity)entitypotion);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 246 */     return 1.62F;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityWitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */