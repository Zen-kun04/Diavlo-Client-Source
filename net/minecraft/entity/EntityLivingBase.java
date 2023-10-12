/*      */ package net.minecraft.entity;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.collect.Maps;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*      */ import net.minecraft.entity.ai.attributes.IAttribute;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.passive.EntityWolf;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemArmor;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagFloat;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.nbt.NBTTagShort;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.server.S04PacketEntityEquipment;
/*      */ import net.minecraft.network.play.server.S0BPacketAnimation;
/*      */ import net.minecraft.network.play.server.S0DPacketCollectItem;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.potion.PotionHelper;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.CombatTracker;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EntitySelectors;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.IBlockAccess;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ 
/*      */ public abstract class EntityLivingBase
/*      */   extends Entity
/*      */ {
/*   56 */   private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
/*   57 */   private static final AttributeModifier sprintingSpeedBoostModifier = (new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
/*      */   private BaseAttributeMap attributeMap;
/*   59 */   private final CombatTracker _combatTracker = new CombatTracker(this);
/*   60 */   private final Map<Integer, PotionEffect> activePotionsMap = Maps.newHashMap();
/*   61 */   private final ItemStack[] previousEquipment = new ItemStack[5];
/*      */   public boolean isSwingInProgress;
/*      */   public int swingProgressInt;
/*      */   public int arrowHitTimer;
/*      */   public int hurtTime;
/*      */   public int maxHurtTime;
/*      */   public float attackedAtYaw;
/*      */   public int deathTime;
/*      */   public float prevSwingProgress;
/*      */   public float swingProgress;
/*      */   public float prevLimbSwingAmount;
/*      */   public float limbSwingAmount;
/*      */   public float limbSwing;
/*   74 */   public int maxHurtResistantTime = 20;
/*      */   public float prevCameraPitch;
/*      */   public float cameraPitch;
/*      */   public float randomUnused2;
/*      */   public float randomUnused1;
/*      */   public float renderYawOffset;
/*      */   public float prevRenderYawOffset;
/*      */   public float rotationYawHead;
/*      */   public float prevRotationYawHead;
/*   83 */   public float jumpMovementFactor = 0.02F;
/*      */   
/*      */   protected EntityPlayer attackingPlayer;
/*      */   protected int recentlyHit;
/*      */   protected boolean dead;
/*      */   protected int entityAge;
/*      */   protected float prevOnGroundSpeedFactor;
/*      */   protected float onGroundSpeedFactor;
/*      */   protected float movedDistance;
/*      */   protected float prevMovedDistance;
/*      */   protected float unused180;
/*      */   protected int scoreValue;
/*      */   protected float lastDamage;
/*      */   protected boolean isJumping;
/*      */   public float moveStrafing;
/*      */   public float moveForward;
/*      */   protected float randomYawVelocity;
/*      */   protected int newPosRotationIncrements;
/*      */   protected double newPosX;
/*      */   protected double newPosY;
/*      */   protected double newPosZ;
/*      */   protected double newRotationYaw;
/*      */   protected double newRotationPitch;
/*      */   private boolean potionsNeedUpdate = true;
/*      */   private EntityLivingBase entityLivingToAttack;
/*      */   private int revengeTimer;
/*      */   private EntityLivingBase lastAttacker;
/*      */   private int lastAttackerTime;
/*      */   private float landMovementFactor;
/*      */   private int jumpTicks;
/*      */   private float absorptionAmount;
/*      */   
/*      */   public void onKillCommand() {
/*  116 */     attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityLivingBase(World worldIn) {
/*  121 */     super(worldIn);
/*  122 */     applyEntityAttributes();
/*  123 */     setHealth(getMaxHealth());
/*  124 */     this.preventEntitySpawning = true;
/*  125 */     this.randomUnused1 = (float)((Math.random() + 1.0D) * 0.009999999776482582D);
/*  126 */     setPosition(this.posX, this.posY, this.posZ);
/*  127 */     this.randomUnused2 = (float)Math.random() * 12398.0F;
/*  128 */     this.rotationYaw = (float)(Math.random() * Math.PI * 2.0D);
/*  129 */     this.rotationYawHead = this.rotationYaw;
/*  130 */     this.stepHeight = 0.6F;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void entityInit() {
/*  135 */     this.dataWatcher.addObject(7, Integer.valueOf(0));
/*  136 */     this.dataWatcher.addObject(8, Byte.valueOf((byte)0));
/*  137 */     this.dataWatcher.addObject(9, Byte.valueOf((byte)0));
/*  138 */     this.dataWatcher.addObject(6, Float.valueOf(1.0F));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyEntityAttributes() {
/*  143 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
/*  144 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
/*  145 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
/*  150 */     if (!isInWater())
/*      */     {
/*  152 */       handleWaterMovement();
/*      */     }
/*      */     
/*  155 */     if (!this.worldObj.isRemote && this.fallDistance > 3.0F && onGroundIn) {
/*      */       
/*  157 */       IBlockState iblockstate = this.worldObj.getBlockState(pos);
/*  158 */       Block block = iblockstate.getBlock();
/*  159 */       float f = MathHelper.ceiling_float_int(this.fallDistance - 3.0F);
/*      */       
/*  161 */       if (block.getMaterial() != Material.air) {
/*      */         
/*  163 */         double d0 = Math.min(0.2F + f / 15.0F, 10.0F);
/*      */         
/*  165 */         if (d0 > 2.5D)
/*      */         {
/*  167 */           d0 = 2.5D;
/*      */         }
/*      */         
/*  170 */         int i = (int)(150.0D * d0);
/*  171 */         ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(iblockstate) });
/*      */       } 
/*      */     } 
/*      */     
/*  175 */     super.updateFallState(y, onGroundIn, blockIn, pos);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBreatheUnderwater() {
/*  180 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEntityUpdate() {
/*  185 */     this.prevSwingProgress = this.swingProgress;
/*  186 */     super.onEntityUpdate();
/*  187 */     this.worldObj.theProfiler.startSection("livingEntityBaseTick");
/*  188 */     boolean flag = this instanceof EntityPlayer;
/*      */     
/*  190 */     if (isEntityAlive())
/*      */     {
/*  192 */       if (isEntityInsideOpaqueBlock()) {
/*      */         
/*  194 */         attackEntityFrom(DamageSource.inWall, 1.0F);
/*      */       }
/*  196 */       else if (flag && !this.worldObj.getWorldBorder().contains(getEntityBoundingBox())) {
/*      */         
/*  198 */         double d0 = this.worldObj.getWorldBorder().getClosestDistance(this) + this.worldObj.getWorldBorder().getDamageBuffer();
/*      */         
/*  200 */         if (d0 < 0.0D)
/*      */         {
/*  202 */           attackEntityFrom(DamageSource.inWall, Math.max(1, MathHelper.floor_double(-d0 * this.worldObj.getWorldBorder().getDamageAmount())));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*  207 */     if (isImmuneToFire() || this.worldObj.isRemote)
/*      */     {
/*  209 */       extinguish();
/*      */     }
/*      */     
/*  212 */     boolean flag1 = (flag && ((EntityPlayer)this).capabilities.disableDamage);
/*      */     
/*  214 */     if (isEntityAlive())
/*      */     {
/*  216 */       if (isInsideOfMaterial(Material.water)) {
/*      */         
/*  218 */         if (!canBreatheUnderwater() && !isPotionActive(Potion.waterBreathing.id) && !flag1) {
/*      */           
/*  220 */           setAir(decreaseAirSupply(getAir()));
/*      */           
/*  222 */           if (getAir() == -20) {
/*      */             
/*  224 */             setAir(0);
/*      */             
/*  226 */             for (int i = 0; i < 8; i++) {
/*      */               
/*  228 */               float f = this.rand.nextFloat() - this.rand.nextFloat();
/*  229 */               float f1 = this.rand.nextFloat() - this.rand.nextFloat();
/*  230 */               float f2 = this.rand.nextFloat() - this.rand.nextFloat();
/*  231 */               this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f, this.posY + f1, this.posZ + f2, this.motionX, this.motionY, this.motionZ, new int[0]);
/*      */             } 
/*      */             
/*  234 */             attackEntityFrom(DamageSource.drown, 2.0F);
/*      */           } 
/*      */         } 
/*      */         
/*  238 */         if (!this.worldObj.isRemote && isRiding() && this.ridingEntity instanceof EntityLivingBase)
/*      */         {
/*  240 */           mountEntity((Entity)null);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  245 */         setAir(300);
/*      */       } 
/*      */     }
/*      */     
/*  249 */     if (isEntityAlive() && isWet())
/*      */     {
/*  251 */       extinguish();
/*      */     }
/*      */     
/*  254 */     this.prevCameraPitch = this.cameraPitch;
/*      */     
/*  256 */     if (this.hurtTime > 0)
/*      */     {
/*  258 */       this.hurtTime--;
/*      */     }
/*      */     
/*  261 */     if (this.hurtResistantTime > 0 && !(this instanceof net.minecraft.entity.player.EntityPlayerMP))
/*      */     {
/*  263 */       this.hurtResistantTime--;
/*      */     }
/*      */     
/*  266 */     if (getHealth() <= 0.0F)
/*      */     {
/*  268 */       onDeathUpdate();
/*      */     }
/*      */     
/*  271 */     if (this.recentlyHit > 0) {
/*      */       
/*  273 */       this.recentlyHit--;
/*      */     }
/*      */     else {
/*      */       
/*  277 */       this.attackingPlayer = null;
/*      */     } 
/*      */     
/*  280 */     if (this.lastAttacker != null && !this.lastAttacker.isEntityAlive())
/*      */     {
/*  282 */       this.lastAttacker = null;
/*      */     }
/*      */     
/*  285 */     if (this.entityLivingToAttack != null)
/*      */     {
/*  287 */       if (!this.entityLivingToAttack.isEntityAlive()) {
/*      */         
/*  289 */         setRevengeTarget((EntityLivingBase)null);
/*      */       }
/*  291 */       else if (this.ticksExisted - this.revengeTimer > 100) {
/*      */         
/*  293 */         setRevengeTarget((EntityLivingBase)null);
/*      */       } 
/*      */     }
/*      */     
/*  297 */     updatePotionEffects();
/*  298 */     this.prevMovedDistance = this.movedDistance;
/*  299 */     this.prevRenderYawOffset = this.renderYawOffset;
/*  300 */     this.prevRotationYawHead = this.rotationYawHead;
/*  301 */     this.prevRotationYaw = this.rotationYaw;
/*  302 */     this.prevRotationPitch = this.rotationPitch;
/*  303 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isChild() {
/*  308 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onDeathUpdate() {
/*  313 */     this.deathTime++;
/*      */     
/*  315 */     if (this.deathTime == 20) {
/*      */       
/*  317 */       if (!this.worldObj.isRemote && (this.recentlyHit > 0 || isPlayer()) && canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")) {
/*      */         
/*  319 */         int i = getExperiencePoints(this.attackingPlayer);
/*      */         
/*  321 */         while (i > 0) {
/*      */           
/*  323 */           int j = EntityXPOrb.getXPSplit(i);
/*  324 */           i -= j;
/*  325 */           this.worldObj.spawnEntityInWorld((Entity)new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
/*      */         } 
/*      */       } 
/*      */       
/*  329 */       setDead();
/*      */       
/*  331 */       for (int k = 0; k < 20; k++) {
/*      */         
/*  333 */         double d2 = this.rand.nextGaussian() * 0.02D;
/*  334 */         double d0 = this.rand.nextGaussian() * 0.02D;
/*  335 */         double d1 = this.rand.nextGaussian() * 0.02D;
/*  336 */         this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d2, d0, d1, new int[0]);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean canDropLoot() {
/*  343 */     return !isChild();
/*      */   }
/*      */ 
/*      */   
/*      */   protected int decreaseAirSupply(int p_70682_1_) {
/*  348 */     int i = EnchantmentHelper.getRespiration(this);
/*  349 */     return (i > 0 && this.rand.nextInt(i + 1) > 0) ? p_70682_1_ : (p_70682_1_ - 1);
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getExperiencePoints(EntityPlayer player) {
/*  354 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isPlayer() {
/*  359 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public Random getRNG() {
/*  364 */     return this.rand;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityLivingBase getAITarget() {
/*  369 */     return this.entityLivingToAttack;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRevengeTimer() {
/*  374 */     return this.revengeTimer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRevengeTarget(EntityLivingBase livingBase) {
/*  379 */     this.entityLivingToAttack = livingBase;
/*  380 */     this.revengeTimer = this.ticksExisted;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityLivingBase getLastAttacker() {
/*  385 */     return this.lastAttacker;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLastAttackerTime() {
/*  390 */     return this.lastAttackerTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLastAttacker(Entity entityIn) {
/*  395 */     if (entityIn instanceof EntityLivingBase) {
/*      */       
/*  397 */       this.lastAttacker = (EntityLivingBase)entityIn;
/*      */     }
/*      */     else {
/*      */       
/*  401 */       this.lastAttacker = null;
/*      */     } 
/*      */     
/*  404 */     this.lastAttackerTime = this.ticksExisted;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getAge() {
/*  409 */     return this.entityAge;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  414 */     tagCompound.setFloat("HealF", getHealth());
/*  415 */     tagCompound.setShort("Health", (short)(int)Math.ceil(getHealth()));
/*  416 */     tagCompound.setShort("HurtTime", (short)this.hurtTime);
/*  417 */     tagCompound.setInteger("HurtByTimestamp", this.revengeTimer);
/*  418 */     tagCompound.setShort("DeathTime", (short)this.deathTime);
/*  419 */     tagCompound.setFloat("AbsorptionAmount", getAbsorptionAmount());
/*      */     
/*  421 */     for (ItemStack itemstack : getInventory()) {
/*      */       
/*  423 */       if (itemstack != null)
/*      */       {
/*  425 */         this.attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
/*      */       }
/*      */     } 
/*      */     
/*  429 */     tagCompound.setTag("Attributes", (NBTBase)SharedMonsterAttributes.writeBaseAttributeMapToNBT(getAttributeMap()));
/*      */     
/*  431 */     for (ItemStack itemstack1 : getInventory()) {
/*      */       
/*  433 */       if (itemstack1 != null)
/*      */       {
/*  435 */         this.attributeMap.applyAttributeModifiers(itemstack1.getAttributeModifiers());
/*      */       }
/*      */     } 
/*      */     
/*  439 */     if (!this.activePotionsMap.isEmpty()) {
/*      */       
/*  441 */       NBTTagList nbttaglist = new NBTTagList();
/*      */       
/*  443 */       for (PotionEffect potioneffect : this.activePotionsMap.values())
/*      */       {
/*  445 */         nbttaglist.appendTag((NBTBase)potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
/*      */       }
/*      */       
/*  448 */       tagCompound.setTag("ActiveEffects", (NBTBase)nbttaglist);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  454 */     setAbsorptionAmount(tagCompund.getFloat("AbsorptionAmount"));
/*      */     
/*  456 */     if (tagCompund.hasKey("Attributes", 9) && this.worldObj != null && !this.worldObj.isRemote)
/*      */     {
/*  458 */       SharedMonsterAttributes.setAttributeModifiers(getAttributeMap(), tagCompund.getTagList("Attributes", 10));
/*      */     }
/*      */     
/*  461 */     if (tagCompund.hasKey("ActiveEffects", 9)) {
/*      */       
/*  463 */       NBTTagList nbttaglist = tagCompund.getTagList("ActiveEffects", 10);
/*      */       
/*  465 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*      */         
/*  467 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/*  468 */         PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
/*      */         
/*  470 */         if (potioneffect != null)
/*      */         {
/*  472 */           this.activePotionsMap.put(Integer.valueOf(potioneffect.getPotionID()), potioneffect);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  477 */     if (tagCompund.hasKey("HealF", 99)) {
/*      */       
/*  479 */       setHealth(tagCompund.getFloat("HealF"));
/*      */     }
/*      */     else {
/*      */       
/*  483 */       NBTBase nbtbase = tagCompund.getTag("Health");
/*      */       
/*  485 */       if (nbtbase == null) {
/*      */         
/*  487 */         setHealth(getMaxHealth());
/*      */       }
/*  489 */       else if (nbtbase.getId() == 5) {
/*      */         
/*  491 */         setHealth(((NBTTagFloat)nbtbase).getFloat());
/*      */       }
/*  493 */       else if (nbtbase.getId() == 2) {
/*      */         
/*  495 */         setHealth(((NBTTagShort)nbtbase).getShort());
/*      */       } 
/*      */     } 
/*      */     
/*  499 */     this.hurtTime = tagCompund.getShort("HurtTime");
/*  500 */     this.deathTime = tagCompund.getShort("DeathTime");
/*  501 */     this.revengeTimer = tagCompund.getInteger("HurtByTimestamp");
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updatePotionEffects() {
/*  506 */     Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
/*      */     
/*  508 */     while (iterator.hasNext()) {
/*      */       
/*  510 */       Integer integer = iterator.next();
/*  511 */       PotionEffect potioneffect = this.activePotionsMap.get(integer);
/*      */       
/*  513 */       if (!potioneffect.onUpdate(this)) {
/*      */         
/*  515 */         if (!this.worldObj.isRemote) {
/*      */           
/*  517 */           iterator.remove();
/*  518 */           onFinishedPotionEffect(potioneffect);
/*      */         }  continue;
/*      */       } 
/*  521 */       if (potioneffect.getDuration() % 600 == 0)
/*      */       {
/*  523 */         onChangedPotionEffect(potioneffect, false);
/*      */       }
/*      */     } 
/*      */     
/*  527 */     if (this.potionsNeedUpdate) {
/*      */       
/*  529 */       if (!this.worldObj.isRemote)
/*      */       {
/*  531 */         updatePotionMetadata();
/*      */       }
/*      */       
/*  534 */       this.potionsNeedUpdate = false;
/*      */     } 
/*      */     
/*  537 */     int i = this.dataWatcher.getWatchableObjectInt(7);
/*  538 */     boolean flag1 = (this.dataWatcher.getWatchableObjectByte(8) > 0);
/*      */     
/*  540 */     if (i > 0) {
/*      */       int j;
/*  542 */       boolean flag = false;
/*      */       
/*  544 */       if (!isInvisible()) {
/*      */         
/*  546 */         flag = this.rand.nextBoolean();
/*      */       }
/*      */       else {
/*      */         
/*  550 */         flag = (this.rand.nextInt(15) == 0);
/*      */       } 
/*      */       
/*  553 */       if (flag1)
/*      */       {
/*  555 */         j = flag & ((this.rand.nextInt(5) == 0) ? 1 : 0);
/*      */       }
/*      */       
/*  558 */       if (j != 0 && i > 0) {
/*      */         
/*  560 */         double d0 = (i >> 16 & 0xFF) / 255.0D;
/*  561 */         double d1 = (i >> 8 & 0xFF) / 255.0D;
/*  562 */         double d2 = (i >> 0 & 0xFF) / 255.0D;
/*  563 */         this.worldObj.spawnParticle(flag1 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, d0, d1, d2, new int[0]);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updatePotionMetadata() {
/*  570 */     if (this.activePotionsMap.isEmpty()) {
/*      */       
/*  572 */       resetPotionEffectMetadata();
/*  573 */       setInvisible(false);
/*      */     }
/*      */     else {
/*      */       
/*  577 */       int i = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
/*  578 */       this.dataWatcher.updateObject(8, Byte.valueOf((byte)(PotionHelper.getAreAmbient(this.activePotionsMap.values()) ? 1 : 0)));
/*  579 */       this.dataWatcher.updateObject(7, Integer.valueOf(i));
/*  580 */       setInvisible(isPotionActive(Potion.invisibility.id));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void resetPotionEffectMetadata() {
/*  586 */     this.dataWatcher.updateObject(8, Byte.valueOf((byte)0));
/*  587 */     this.dataWatcher.updateObject(7, Integer.valueOf(0));
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearActivePotions() {
/*  592 */     Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
/*      */     
/*  594 */     while (iterator.hasNext()) {
/*      */       
/*  596 */       Integer integer = iterator.next();
/*  597 */       PotionEffect potioneffect = this.activePotionsMap.get(integer);
/*      */       
/*  599 */       if (!this.worldObj.isRemote) {
/*      */         
/*  601 */         iterator.remove();
/*  602 */         onFinishedPotionEffect(potioneffect);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Collection<PotionEffect> getActivePotionEffects() {
/*  609 */     return this.activePotionsMap.values();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPotionActive(int potionId) {
/*  614 */     return this.activePotionsMap.containsKey(Integer.valueOf(potionId));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPotionActive(Potion potionIn) {
/*  619 */     return this.activePotionsMap.containsKey(Integer.valueOf(potionIn.id));
/*      */   }
/*      */ 
/*      */   
/*      */   public PotionEffect getActivePotionEffect(Potion potionIn) {
/*  624 */     return this.activePotionsMap.get(Integer.valueOf(potionIn.id));
/*      */   }
/*      */ 
/*      */   
/*      */   public void addPotionEffect(PotionEffect potioneffectIn) {
/*  629 */     if (isPotionApplicable(potioneffectIn))
/*      */     {
/*  631 */       if (this.activePotionsMap.containsKey(Integer.valueOf(potioneffectIn.getPotionID()))) {
/*      */         
/*  633 */         ((PotionEffect)this.activePotionsMap.get(Integer.valueOf(potioneffectIn.getPotionID()))).combine(potioneffectIn);
/*  634 */         onChangedPotionEffect(this.activePotionsMap.get(Integer.valueOf(potioneffectIn.getPotionID())), true);
/*      */       }
/*      */       else {
/*      */         
/*  638 */         this.activePotionsMap.put(Integer.valueOf(potioneffectIn.getPotionID()), potioneffectIn);
/*  639 */         onNewPotionEffect(potioneffectIn);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPotionApplicable(PotionEffect potioneffectIn) {
/*  646 */     if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
/*      */       
/*  648 */       int i = potioneffectIn.getPotionID();
/*      */       
/*  650 */       if (i == Potion.regeneration.id || i == Potion.poison.id)
/*      */       {
/*  652 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  656 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEntityUndead() {
/*  661 */     return (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removePotionEffectClient(int potionId) {
/*  666 */     this.activePotionsMap.remove(Integer.valueOf(potionId));
/*      */   }
/*      */ 
/*      */   
/*      */   public void removePotionEffect(int potionId) {
/*  671 */     PotionEffect potioneffect = this.activePotionsMap.remove(Integer.valueOf(potionId));
/*      */     
/*  673 */     if (potioneffect != null)
/*      */     {
/*  675 */       onFinishedPotionEffect(potioneffect);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onNewPotionEffect(PotionEffect id) {
/*  681 */     this.potionsNeedUpdate = true;
/*      */     
/*  683 */     if (!this.worldObj.isRemote)
/*      */     {
/*  685 */       Potion.potionTypes[id.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_) {
/*  691 */     this.potionsNeedUpdate = true;
/*      */     
/*  693 */     if (p_70695_2_ && !this.worldObj.isRemote) {
/*      */       
/*  695 */       Potion.potionTypes[id.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), id.getAmplifier());
/*  696 */       Potion.potionTypes[id.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onFinishedPotionEffect(PotionEffect effect) {
/*  702 */     this.potionsNeedUpdate = true;
/*      */     
/*  704 */     if (!this.worldObj.isRemote)
/*      */     {
/*  706 */       Potion.potionTypes[effect.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), effect.getAmplifier());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void heal(float healAmount) {
/*  712 */     float f = getHealth();
/*      */     
/*  714 */     if (f > 0.0F)
/*      */     {
/*  716 */       setHealth(f + healAmount);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getHealth() {
/*  722 */     return this.dataWatcher.getWatchableObjectFloat(6);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHealth(float health) {
/*  727 */     this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(health, 0.0F, getMaxHealth())));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  732 */     if (isEntityInvulnerable(source))
/*      */     {
/*  734 */       return false;
/*      */     }
/*  736 */     if (this.worldObj.isRemote)
/*      */     {
/*  738 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  742 */     this.entityAge = 0;
/*      */     
/*  744 */     if (getHealth() <= 0.0F)
/*      */     {
/*  746 */       return false;
/*      */     }
/*  748 */     if (source.isFireDamage() && isPotionActive(Potion.fireResistance))
/*      */     {
/*  750 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  754 */     if ((source == DamageSource.anvil || source == DamageSource.fallingBlock) && getEquipmentInSlot(4) != null) {
/*      */       
/*  756 */       getEquipmentInSlot(4).damageItem((int)(amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), this);
/*  757 */       amount *= 0.75F;
/*      */     } 
/*      */     
/*  760 */     this.limbSwingAmount = 1.5F;
/*  761 */     boolean flag = true;
/*      */     
/*  763 */     if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F) {
/*      */       
/*  765 */       if (amount <= this.lastDamage)
/*      */       {
/*  767 */         return false;
/*      */       }
/*      */       
/*  770 */       damageEntity(source, amount - this.lastDamage);
/*  771 */       this.lastDamage = amount;
/*  772 */       flag = false;
/*      */     }
/*      */     else {
/*      */       
/*  776 */       this.lastDamage = amount;
/*  777 */       this.hurtResistantTime = this.maxHurtResistantTime;
/*  778 */       damageEntity(source, amount);
/*  779 */       this.hurtTime = this.maxHurtTime = 10;
/*      */     } 
/*      */     
/*  782 */     this.attackedAtYaw = 0.0F;
/*  783 */     Entity entity = source.getEntity();
/*      */     
/*  785 */     if (entity != null) {
/*      */       
/*  787 */       if (entity instanceof EntityLivingBase)
/*      */       {
/*  789 */         setRevengeTarget((EntityLivingBase)entity);
/*      */       }
/*      */       
/*  792 */       if (entity instanceof EntityPlayer) {
/*      */         
/*  794 */         this.recentlyHit = 100;
/*  795 */         this.attackingPlayer = (EntityPlayer)entity;
/*      */       }
/*  797 */       else if (entity instanceof EntityWolf) {
/*      */         
/*  799 */         EntityWolf entitywolf = (EntityWolf)entity;
/*      */         
/*  801 */         if (entitywolf.isTamed()) {
/*      */           
/*  803 */           this.recentlyHit = 100;
/*  804 */           this.attackingPlayer = null;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  809 */     if (flag) {
/*      */       
/*  811 */       this.worldObj.setEntityState(this, (byte)2);
/*      */       
/*  813 */       if (source != DamageSource.drown)
/*      */       {
/*  815 */         setBeenAttacked();
/*      */       }
/*      */       
/*  818 */       if (entity != null) {
/*      */         
/*  820 */         double d1 = entity.posX - this.posX;
/*      */         
/*      */         double d0;
/*  823 */         for (d0 = entity.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
/*      */         {
/*  825 */           d1 = (Math.random() - Math.random()) * 0.01D;
/*      */         }
/*      */         
/*  828 */         this.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * 180.0D / Math.PI - this.rotationYaw);
/*  829 */         knockBack(entity, amount, d1, d0);
/*      */       }
/*      */       else {
/*      */         
/*  833 */         this.attackedAtYaw = ((int)(Math.random() * 2.0D) * 180);
/*      */       } 
/*      */     } 
/*      */     
/*  837 */     if (getHealth() <= 0.0F) {
/*      */       
/*  839 */       String s = getDeathSound();
/*      */       
/*  841 */       if (flag && s != null)
/*      */       {
/*  843 */         playSound(s, getSoundVolume(), getSoundPitch());
/*      */       }
/*      */       
/*  846 */       onDeath(source);
/*      */     }
/*      */     else {
/*      */       
/*  850 */       String s1 = getHurtSound();
/*      */       
/*  852 */       if (flag && s1 != null)
/*      */       {
/*  854 */         playSound(s1, getSoundVolume(), getSoundPitch());
/*      */       }
/*      */     } 
/*      */     
/*  858 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renderBrokenItemStack(ItemStack stack) {
/*  865 */     playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);
/*      */     
/*  867 */     for (int i = 0; i < 5; i++) {
/*      */       
/*  869 */       Vec3 vec3 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
/*  870 */       vec3 = vec3.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
/*  871 */       vec3 = vec3.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
/*  872 */       double d0 = -this.rand.nextFloat() * 0.6D - 0.3D;
/*  873 */       Vec3 vec31 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
/*  874 */       vec31 = vec31.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
/*  875 */       vec31 = vec31.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
/*  876 */       vec31 = vec31.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
/*  877 */       this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(stack.getItem()) });
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDeath(DamageSource cause) {
/*  883 */     Entity entity = cause.getEntity();
/*  884 */     EntityLivingBase entitylivingbase = getAttackingEntity();
/*      */     
/*  886 */     if (this.scoreValue >= 0 && entitylivingbase != null)
/*      */     {
/*  888 */       entitylivingbase.addToPlayerScore(this, this.scoreValue);
/*      */     }
/*      */     
/*  891 */     if (entity != null)
/*      */     {
/*  893 */       entity.onKillEntity(this);
/*      */     }
/*      */     
/*  896 */     this.dead = true;
/*  897 */     getCombatTracker().reset();
/*      */     
/*  899 */     if (!this.worldObj.isRemote) {
/*      */       
/*  901 */       int i = 0;
/*      */       
/*  903 */       if (entity instanceof EntityPlayer)
/*      */       {
/*  905 */         i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
/*      */       }
/*      */       
/*  908 */       if (canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")) {
/*      */         
/*  910 */         dropFewItems((this.recentlyHit > 0), i);
/*  911 */         dropEquipment((this.recentlyHit > 0), i);
/*      */         
/*  913 */         if (this.recentlyHit > 0 && this.rand.nextFloat() < 0.025F + i * 0.01F)
/*      */         {
/*  915 */           addRandomDrop();
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  920 */     this.worldObj.setEntityState(this, (byte)3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {}
/*      */ 
/*      */   
/*      */   public void knockBack(Entity entityIn, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
/*  929 */     if (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()) {
/*      */       
/*  931 */       this.isAirBorne = true;
/*  932 */       float f = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_ + p_70653_5_ * p_70653_5_);
/*  933 */       float f1 = 0.4F;
/*  934 */       this.motionX /= 2.0D;
/*  935 */       this.motionY /= 2.0D;
/*  936 */       this.motionZ /= 2.0D;
/*  937 */       this.motionX -= p_70653_3_ / f * f1;
/*  938 */       this.motionY += f1;
/*  939 */       this.motionZ -= p_70653_5_ / f * f1;
/*      */       
/*  941 */       if (this.motionY > 0.4000000059604645D)
/*      */       {
/*  943 */         this.motionY = 0.4000000059604645D;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getHurtSound() {
/*  950 */     return "game.neutral.hurt";
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getDeathSound() {
/*  955 */     return "game.neutral.die";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addRandomDrop() {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {}
/*      */ 
/*      */   
/*      */   public boolean isOnLadder() {
/*  968 */     int i = MathHelper.floor_double(this.posX);
/*  969 */     int j = MathHelper.floor_double((getEntityBoundingBox()).minY);
/*  970 */     int k = MathHelper.floor_double(this.posZ);
/*  971 */     Block block = this.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock();
/*  972 */     return ((block == Blocks.ladder || block == Blocks.vine) && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).isSpectator()));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEntityAlive() {
/*  977 */     return (!this.isDead && getHealth() > 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fall(float distance, float damageMultiplier) {
/*  982 */     super.fall(distance, damageMultiplier);
/*  983 */     PotionEffect potioneffect = getActivePotionEffect(Potion.jump);
/*  984 */     float f = (potioneffect != null) ? (potioneffect.getAmplifier() + 1) : 0.0F;
/*  985 */     int i = MathHelper.ceiling_float_int((distance - 3.0F - f) * damageMultiplier);
/*      */     
/*  987 */     if (i > 0) {
/*      */       
/*  989 */       playSound(getFallSoundString(i), 1.0F, 1.0F);
/*  990 */       attackEntityFrom(DamageSource.fall, i);
/*  991 */       int j = MathHelper.floor_double(this.posX);
/*  992 */       int k = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/*  993 */       int l = MathHelper.floor_double(this.posZ);
/*  994 */       Block block = this.worldObj.getBlockState(new BlockPos(j, k, l)).getBlock();
/*      */       
/*  996 */       if (block.getMaterial() != Material.air) {
/*      */         
/*  998 */         Block.SoundType block$soundtype = block.stepSound;
/*  999 */         playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.5F, block$soundtype.getFrequency() * 0.75F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getFallSoundString(int damageValue) {
/* 1006 */     return (damageValue > 4) ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
/*      */   }
/*      */ 
/*      */   
/*      */   public void performHurtAnimation() {
/* 1011 */     this.hurtTime = this.maxHurtTime = 10;
/* 1012 */     this.attackedAtYaw = 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTotalArmorValue() {
/* 1017 */     int i = 0;
/*      */     
/* 1019 */     for (ItemStack itemstack : getInventory()) {
/*      */       
/* 1021 */       if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
/*      */         
/* 1023 */         int j = ((ItemArmor)itemstack.getItem()).damageReduceAmount;
/* 1024 */         i += j;
/*      */       } 
/*      */     } 
/*      */     
/* 1028 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void damageArmor(float p_70675_1_) {}
/*      */ 
/*      */   
/*      */   protected float applyArmorCalculations(DamageSource source, float damage) {
/* 1037 */     if (!source.isUnblockable()) {
/*      */       
/* 1039 */       int i = 25 - getTotalArmorValue();
/* 1040 */       float f = damage * i;
/* 1041 */       damageArmor(damage);
/* 1042 */       damage = f / 25.0F;
/*      */     } 
/*      */     
/* 1045 */     return damage;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float applyPotionDamageCalculations(DamageSource source, float damage) {
/* 1050 */     if (source.isDamageAbsolute())
/*      */     {
/* 1052 */       return damage;
/*      */     }
/*      */ 
/*      */     
/* 1056 */     if (isPotionActive(Potion.resistance) && source != DamageSource.outOfWorld) {
/*      */       
/* 1058 */       int i = (getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
/* 1059 */       int j = 25 - i;
/* 1060 */       float f = damage * j;
/* 1061 */       damage = f / 25.0F;
/*      */     } 
/*      */     
/* 1064 */     if (damage <= 0.0F)
/*      */     {
/* 1066 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     
/* 1070 */     int k = EnchantmentHelper.getEnchantmentModifierDamage(getInventory(), source);
/*      */     
/* 1072 */     if (k > 20)
/*      */     {
/* 1074 */       k = 20;
/*      */     }
/*      */     
/* 1077 */     if (k > 0 && k <= 20) {
/*      */       
/* 1079 */       int l = 25 - k;
/* 1080 */       float f1 = damage * l;
/* 1081 */       damage = f1 / 25.0F;
/*      */     } 
/*      */     
/* 1084 */     return damage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void damageEntity(DamageSource damageSrc, float damageAmount) {
/* 1091 */     if (!isEntityInvulnerable(damageSrc)) {
/*      */       
/* 1093 */       damageAmount = applyArmorCalculations(damageSrc, damageAmount);
/* 1094 */       damageAmount = applyPotionDamageCalculations(damageSrc, damageAmount);
/* 1095 */       float f = damageAmount;
/* 1096 */       damageAmount = Math.max(damageAmount - getAbsorptionAmount(), 0.0F);
/* 1097 */       setAbsorptionAmount(getAbsorptionAmount() - f - damageAmount);
/*      */       
/* 1099 */       if (damageAmount != 0.0F) {
/*      */         
/* 1101 */         float f1 = getHealth();
/* 1102 */         setHealth(f1 - damageAmount);
/* 1103 */         getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
/* 1104 */         setAbsorptionAmount(getAbsorptionAmount() - damageAmount);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public CombatTracker getCombatTracker() {
/* 1111 */     return this._combatTracker;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityLivingBase getAttackingEntity() {
/* 1116 */     return (this._combatTracker.func_94550_c() != null) ? this._combatTracker.func_94550_c() : ((this.attackingPlayer != null) ? (EntityLivingBase)this.attackingPlayer : ((this.entityLivingToAttack != null) ? this.entityLivingToAttack : null));
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getMaxHealth() {
/* 1121 */     return (float)getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getArrowCountInEntity() {
/* 1126 */     return this.dataWatcher.getWatchableObjectByte(9);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setArrowCountInEntity(int count) {
/* 1131 */     this.dataWatcher.updateObject(9, Byte.valueOf((byte)count));
/*      */   }
/*      */ 
/*      */   
/*      */   private int getArmSwingAnimationEnd() {
/* 1136 */     return isPotionActive(Potion.digSpeed) ? (6 - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1) : (isPotionActive(Potion.digSlowdown) ? (6 + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
/*      */   }
/*      */ 
/*      */   
/*      */   public void swingItem() {
/* 1141 */     if (!this.isSwingInProgress || this.swingProgressInt >= getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
/*      */       
/* 1143 */       this.swingProgressInt = -1;
/* 1144 */       this.isSwingInProgress = true;
/*      */       
/* 1146 */       if (this.worldObj instanceof WorldServer)
/*      */       {
/*      */         
/* 1149 */         ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, (Packet)new S0BPacketAnimation(this, 0));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleStatusUpdate(byte id) {
/* 1156 */     if (id == 2) {
/*      */       
/* 1158 */       this.limbSwingAmount = 1.5F;
/* 1159 */       this.hurtResistantTime = this.maxHurtResistantTime;
/* 1160 */       this.hurtTime = this.maxHurtTime = 10;
/* 1161 */       this.attackedAtYaw = 0.0F;
/* 1162 */       String s = getHurtSound();
/*      */       
/* 1164 */       if (s != null)
/*      */       {
/* 1166 */         playSound(getHurtSound(), getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*      */       }
/*      */       
/* 1169 */       attackEntityFrom(DamageSource.generic, 0.0F);
/*      */     }
/* 1171 */     else if (id == 3) {
/*      */       
/* 1173 */       String s1 = getDeathSound();
/*      */       
/* 1175 */       if (s1 != null)
/*      */       {
/* 1177 */         playSound(getDeathSound(), getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*      */       }
/*      */       
/* 1180 */       setHealth(0.0F);
/* 1181 */       onDeath(DamageSource.generic);
/*      */     }
/*      */     else {
/*      */       
/* 1185 */       super.handleStatusUpdate(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void kill() {
/* 1191 */     attackEntityFrom(DamageSource.outOfWorld, 4.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateArmSwingProgress() {
/* 1196 */     int i = getArmSwingAnimationEnd();
/*      */     
/* 1198 */     if (this.isSwingInProgress) {
/*      */       
/* 1200 */       this.swingProgressInt++;
/*      */       
/* 1202 */       if (this.swingProgressInt >= i)
/*      */       {
/* 1204 */         this.swingProgressInt = 0;
/* 1205 */         this.isSwingInProgress = false;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1210 */       this.swingProgressInt = 0;
/*      */     } 
/*      */     
/* 1213 */     this.swingProgress = this.swingProgressInt / i;
/*      */   }
/*      */ 
/*      */   
/*      */   public IAttributeInstance getEntityAttribute(IAttribute attribute) {
/* 1218 */     return getAttributeMap().getAttributeInstance(attribute);
/*      */   }
/*      */ 
/*      */   
/*      */   public BaseAttributeMap getAttributeMap() {
/* 1223 */     if (this.attributeMap == null)
/*      */     {
/* 1225 */       this.attributeMap = (BaseAttributeMap)new ServersideAttributeMap();
/*      */     }
/*      */     
/* 1228 */     return this.attributeMap;
/*      */   }
/*      */ 
/*      */   
/*      */   public EnumCreatureAttribute getCreatureAttribute() {
/* 1233 */     return EnumCreatureAttribute.UNDEFINED;
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract ItemStack getHeldItem();
/*      */   
/*      */   public abstract ItemStack getEquipmentInSlot(int paramInt);
/*      */   
/*      */   public abstract ItemStack getCurrentArmor(int paramInt);
/*      */   
/*      */   public abstract void setCurrentItemOrArmor(int paramInt, ItemStack paramItemStack);
/*      */   
/*      */   public void setSprinting(boolean sprinting) {
/* 1246 */     super.setSprinting(sprinting);
/* 1247 */     IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/*      */     
/* 1249 */     if (iattributeinstance.getModifier(sprintingSpeedBoostModifierUUID) != null)
/*      */     {
/* 1251 */       iattributeinstance.removeModifier(sprintingSpeedBoostModifier);
/*      */     }
/*      */     
/* 1254 */     if (sprinting)
/*      */     {
/* 1256 */       iattributeinstance.applyModifier(sprintingSpeedBoostModifier);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract ItemStack[] getInventory();
/*      */   
/*      */   protected float getSoundVolume() {
/* 1264 */     return 1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getSoundPitch() {
/* 1269 */     return isChild() ? ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F) : ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isMovementBlocked() {
/* 1274 */     return (getHealth() <= 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void dismountEntity(Entity entityIn) {
/* 1279 */     double d0 = entityIn.posX;
/* 1280 */     double d1 = (entityIn.getEntityBoundingBox()).minY + entityIn.height;
/* 1281 */     double d2 = entityIn.posZ;
/* 1282 */     int i = 1;
/*      */     
/* 1284 */     for (int j = -i; j <= i; j++) {
/*      */       
/* 1286 */       for (int k = -i; k < i; k++) {
/*      */         
/* 1288 */         if (j != 0 || k != 0) {
/*      */           
/* 1290 */           int l = (int)(this.posX + j);
/* 1291 */           int i1 = (int)(this.posZ + k);
/* 1292 */           AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(j, 1.0D, k);
/*      */           
/* 1294 */           if (this.worldObj.getCollisionBoxes(axisalignedbb).isEmpty()) {
/*      */             
/* 1296 */             if (World.doesBlockHaveSolidTopSurface((IBlockAccess)this.worldObj, new BlockPos(l, (int)this.posY, i1))) {
/*      */               
/* 1298 */               setPositionAndUpdate(this.posX + j, this.posY + 1.0D, this.posZ + k);
/*      */               
/*      */               return;
/*      */             } 
/* 1302 */             if (World.doesBlockHaveSolidTopSurface((IBlockAccess)this.worldObj, new BlockPos(l, (int)this.posY - 1, i1)) || this.worldObj.getBlockState(new BlockPos(l, (int)this.posY - 1, i1)).getBlock().getMaterial() == Material.water) {
/*      */               
/* 1304 */               d0 = this.posX + j;
/* 1305 */               d1 = this.posY + 1.0D;
/* 1306 */               d2 = this.posZ + k;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1313 */     setPositionAndUpdate(d0, d1, d2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getAlwaysRenderNameTagForRender() {
/* 1318 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getJumpUpwardsMotion() {
/* 1323 */     return 0.42F;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void jump() {
/* 1328 */     this.motionY = getJumpUpwardsMotion();
/*      */     
/* 1330 */     if (isPotionActive(Potion.jump))
/*      */     {
/* 1332 */       this.motionY += ((getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
/*      */     }
/*      */     
/* 1335 */     if (isSprinting()) {
/*      */       
/* 1337 */       float f = this.rotationYaw * 0.017453292F;
/* 1338 */       this.motionX -= (MathHelper.sin(f) * 0.2F);
/* 1339 */       this.motionZ += (MathHelper.cos(f) * 0.2F);
/*      */     } 
/*      */     
/* 1342 */     this.isAirBorne = true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateAITick() {
/* 1347 */     this.motionY += 0.03999999910593033D;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void handleJumpLava() {
/* 1352 */     this.motionY += 0.03999999910593033D;
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveEntityWithHeading(float strafe, float forward) {
/* 1357 */     if (isServerWorld())
/*      */     {
/* 1359 */       if (!isInWater() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) {
/*      */         
/* 1361 */         if (!isInLava() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying))
/*      */         {
/* 1363 */           float f5, f4 = 0.91F;
/*      */           
/* 1365 */           if (this.onGround)
/*      */           {
/* 1367 */             f4 = (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double((getEntityBoundingBox()).minY) - 1, MathHelper.floor_double(this.posZ))).getBlock()).slipperiness * 0.91F;
/*      */           }
/*      */           
/* 1370 */           float f = 0.16277136F / f4 * f4 * f4;
/*      */ 
/*      */           
/* 1373 */           if (this.onGround) {
/*      */             
/* 1375 */             f5 = getAIMoveSpeed() * f;
/*      */           }
/*      */           else {
/*      */             
/* 1379 */             f5 = this.jumpMovementFactor;
/*      */           } 
/*      */           
/* 1382 */           moveFlying(strafe, forward, f5);
/* 1383 */           f4 = 0.91F;
/*      */           
/* 1385 */           if (this.onGround)
/*      */           {
/* 1387 */             f4 = (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double((getEntityBoundingBox()).minY) - 1, MathHelper.floor_double(this.posZ))).getBlock()).slipperiness * 0.91F;
/*      */           }
/*      */           
/* 1390 */           if (isOnLadder()) {
/*      */             
/* 1392 */             float f6 = 0.15F;
/* 1393 */             this.motionX = MathHelper.clamp_double(this.motionX, -f6, f6);
/* 1394 */             this.motionZ = MathHelper.clamp_double(this.motionZ, -f6, f6);
/* 1395 */             this.fallDistance = 0.0F;
/*      */             
/* 1397 */             if (this.motionY < -0.15D)
/*      */             {
/* 1399 */               this.motionY = -0.15D;
/*      */             }
/*      */             
/* 1402 */             boolean flag = (isSneaking() && this instanceof EntityPlayer);
/*      */             
/* 1404 */             if (flag && this.motionY < 0.0D)
/*      */             {
/* 1406 */               this.motionY = 0.0D;
/*      */             }
/*      */           } 
/*      */           
/* 1410 */           moveEntity(this.motionX, this.motionY, this.motionZ);
/*      */           
/* 1412 */           if (this.isCollidedHorizontally && isOnLadder())
/*      */           {
/* 1414 */             this.motionY = 0.2D;
/*      */           }
/*      */           
/* 1417 */           if (this.worldObj.isRemote && (!this.worldObj.isBlockLoaded(new BlockPos((int)this.posX, 0, (int)this.posZ)) || !this.worldObj.getChunkFromBlockCoords(new BlockPos((int)this.posX, 0, (int)this.posZ)).isLoaded())) {
/*      */             
/* 1419 */             if (this.posY > 0.0D)
/*      */             {
/* 1421 */               this.motionY = -0.1D;
/*      */             }
/*      */             else
/*      */             {
/* 1425 */               this.motionY = 0.0D;
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 1430 */             this.motionY -= 0.08D;
/*      */           } 
/*      */           
/* 1433 */           this.motionY *= 0.9800000190734863D;
/* 1434 */           this.motionX *= f4;
/* 1435 */           this.motionZ *= f4;
/*      */         }
/*      */         else
/*      */         {
/* 1439 */           double d1 = this.posY;
/* 1440 */           moveFlying(strafe, forward, 0.02F);
/* 1441 */           moveEntity(this.motionX, this.motionY, this.motionZ);
/* 1442 */           this.motionX *= 0.5D;
/* 1443 */           this.motionY *= 0.5D;
/* 1444 */           this.motionZ *= 0.5D;
/* 1445 */           this.motionY -= 0.02D;
/*      */           
/* 1447 */           if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d1, this.motionZ))
/*      */           {
/* 1449 */             this.motionY = 0.30000001192092896D;
/*      */           }
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1455 */         double d0 = this.posY;
/* 1456 */         float f1 = 0.8F;
/* 1457 */         float f2 = 0.02F;
/* 1458 */         float f3 = EnchantmentHelper.getDepthStriderModifier(this);
/*      */         
/* 1460 */         if (f3 > 3.0F)
/*      */         {
/* 1462 */           f3 = 3.0F;
/*      */         }
/*      */         
/* 1465 */         if (!this.onGround)
/*      */         {
/* 1467 */           f3 *= 0.5F;
/*      */         }
/*      */         
/* 1470 */         if (f3 > 0.0F) {
/*      */           
/* 1472 */           f1 += (0.54600006F - f1) * f3 / 3.0F;
/* 1473 */           f2 += (getAIMoveSpeed() * 1.0F - f2) * f3 / 3.0F;
/*      */         } 
/*      */         
/* 1476 */         moveFlying(strafe, forward, f2);
/* 1477 */         moveEntity(this.motionX, this.motionY, this.motionZ);
/* 1478 */         this.motionX *= f1;
/* 1479 */         this.motionY *= 0.800000011920929D;
/* 1480 */         this.motionZ *= f1;
/* 1481 */         this.motionY -= 0.02D;
/*      */         
/* 1483 */         if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ))
/*      */         {
/* 1485 */           this.motionY = 0.30000001192092896D;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1490 */     this.prevLimbSwingAmount = this.limbSwingAmount;
/* 1491 */     double d2 = this.posX - this.prevPosX;
/* 1492 */     double d3 = this.posZ - this.prevPosZ;
/* 1493 */     float f7 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4.0F;
/*      */     
/* 1495 */     if (f7 > 1.0F)
/*      */     {
/* 1497 */       f7 = 1.0F;
/*      */     }
/*      */     
/* 1500 */     this.limbSwingAmount += (f7 - this.limbSwingAmount) * 0.4F;
/* 1501 */     this.limbSwing += this.limbSwingAmount;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getAIMoveSpeed() {
/* 1506 */     return this.landMovementFactor;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAIMoveSpeed(float speedIn) {
/* 1511 */     this.landMovementFactor = speedIn;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean attackEntityAsMob(Entity entityIn) {
/* 1516 */     setLastAttacker(entityIn);
/* 1517 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPlayerSleeping() {
/* 1522 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdate() {
/* 1527 */     super.onUpdate();
/*      */     
/* 1529 */     if (!this.worldObj.isRemote) {
/*      */       
/* 1531 */       int i = getArrowCountInEntity();
/*      */       
/* 1533 */       if (i > 0) {
/*      */         
/* 1535 */         if (this.arrowHitTimer <= 0)
/*      */         {
/* 1537 */           this.arrowHitTimer = 20 * (30 - i);
/*      */         }
/*      */         
/* 1540 */         this.arrowHitTimer--;
/*      */         
/* 1542 */         if (this.arrowHitTimer <= 0)
/*      */         {
/* 1544 */           setArrowCountInEntity(i - 1);
/*      */         }
/*      */       } 
/*      */       
/* 1548 */       for (int j = 0; j < 5; j++) {
/*      */         
/* 1550 */         ItemStack itemstack = this.previousEquipment[j];
/* 1551 */         ItemStack itemstack1 = getEquipmentInSlot(j);
/*      */         
/* 1553 */         if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
/*      */           
/* 1555 */           ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, (Packet)new S04PacketEntityEquipment(getEntityId(), j, itemstack1));
/*      */           
/* 1557 */           if (itemstack != null)
/*      */           {
/* 1559 */             this.attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
/*      */           }
/*      */           
/* 1562 */           if (itemstack1 != null)
/*      */           {
/* 1564 */             this.attributeMap.applyAttributeModifiers(itemstack1.getAttributeModifiers());
/*      */           }
/*      */           
/* 1567 */           this.previousEquipment[j] = (itemstack1 == null) ? null : itemstack1.copy();
/*      */         } 
/*      */       } 
/*      */       
/* 1571 */       if (this.ticksExisted % 20 == 0)
/*      */       {
/* 1573 */         getCombatTracker().reset();
/*      */       }
/*      */     } 
/*      */     
/* 1577 */     onLivingUpdate();
/* 1578 */     double d0 = this.posX - this.prevPosX;
/* 1579 */     double d1 = this.posZ - this.prevPosZ;
/* 1580 */     float f = (float)(d0 * d0 + d1 * d1);
/* 1581 */     float f1 = this.renderYawOffset;
/* 1582 */     float f2 = 0.0F;
/* 1583 */     this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
/* 1584 */     float f3 = 0.0F;
/*      */     
/* 1586 */     if (f > 0.0025000002F) {
/*      */       
/* 1588 */       f3 = 1.0F;
/* 1589 */       f2 = (float)Math.sqrt(f) * 3.0F;
/* 1590 */       f1 = (float)MathHelper.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
/*      */     } 
/*      */     
/* 1593 */     if (this.swingProgress > 0.0F)
/*      */     {
/* 1595 */       f1 = this.rotationYaw;
/*      */     }
/*      */     
/* 1598 */     if (!this.onGround)
/*      */     {
/* 1600 */       f3 = 0.0F;
/*      */     }
/*      */     
/* 1603 */     this.onGroundSpeedFactor += (f3 - this.onGroundSpeedFactor) * 0.3F;
/* 1604 */     this.worldObj.theProfiler.startSection("headTurn");
/* 1605 */     f2 = updateDistance(f1, f2);
/* 1606 */     this.worldObj.theProfiler.endSection();
/* 1607 */     this.worldObj.theProfiler.startSection("rangeChecks");
/*      */     
/* 1609 */     while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*      */     {
/* 1611 */       this.prevRotationYaw -= 360.0F;
/*      */     }
/*      */     
/* 1614 */     while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*      */     {
/* 1616 */       this.prevRotationYaw += 360.0F;
/*      */     }
/*      */     
/* 1619 */     while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F)
/*      */     {
/* 1621 */       this.prevRenderYawOffset -= 360.0F;
/*      */     }
/*      */     
/* 1624 */     while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F)
/*      */     {
/* 1626 */       this.prevRenderYawOffset += 360.0F;
/*      */     }
/*      */     
/* 1629 */     while (this.rotationPitch - this.prevRotationPitch < -180.0F)
/*      */     {
/* 1631 */       this.prevRotationPitch -= 360.0F;
/*      */     }
/*      */     
/* 1634 */     while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*      */     {
/* 1636 */       this.prevRotationPitch += 360.0F;
/*      */     }
/*      */     
/* 1639 */     while (this.rotationYawHead - this.prevRotationYawHead < -180.0F)
/*      */     {
/* 1641 */       this.prevRotationYawHead -= 360.0F;
/*      */     }
/*      */     
/* 1644 */     while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F)
/*      */     {
/* 1646 */       this.prevRotationYawHead += 360.0F;
/*      */     }
/*      */     
/* 1649 */     this.worldObj.theProfiler.endSection();
/* 1650 */     this.movedDistance += f2;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float updateDistance(float p_110146_1_, float p_110146_2_) {
/* 1655 */     float f = MathHelper.wrapAngleTo180_float(p_110146_1_ - this.renderYawOffset);
/* 1656 */     this.renderYawOffset += f * 0.3F;
/* 1657 */     float f1 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
/* 1658 */     boolean flag = (f1 < -90.0F || f1 >= 90.0F);
/*      */     
/* 1660 */     if (f1 < -75.0F)
/*      */     {
/* 1662 */       f1 = -75.0F;
/*      */     }
/*      */     
/* 1665 */     if (f1 >= 75.0F)
/*      */     {
/* 1667 */       f1 = 75.0F;
/*      */     }
/*      */     
/* 1670 */     this.renderYawOffset = this.rotationYaw - f1;
/*      */     
/* 1672 */     if (f1 * f1 > 2500.0F)
/*      */     {
/* 1674 */       this.renderYawOffset += f1 * 0.2F;
/*      */     }
/*      */     
/* 1677 */     if (flag)
/*      */     {
/* 1679 */       p_110146_2_ *= -1.0F;
/*      */     }
/*      */     
/* 1682 */     return p_110146_2_;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onLivingUpdate() {
/* 1687 */     if (this.jumpTicks > 0)
/*      */     {
/* 1689 */       this.jumpTicks--;
/*      */     }
/*      */     
/* 1692 */     if (this.newPosRotationIncrements > 0) {
/*      */       
/* 1694 */       double d0 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
/* 1695 */       double d1 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
/* 1696 */       double d2 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
/* 1697 */       double d3 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
/* 1698 */       this.rotationYaw = (float)(this.rotationYaw + d3 / this.newPosRotationIncrements);
/* 1699 */       this.rotationPitch = (float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
/* 1700 */       this.newPosRotationIncrements--;
/* 1701 */       setPosition(d0, d1, d2);
/* 1702 */       setRotation(this.rotationYaw, this.rotationPitch);
/*      */     }
/* 1704 */     else if (!isServerWorld()) {
/*      */       
/* 1706 */       this.motionX *= 0.98D;
/* 1707 */       this.motionY *= 0.98D;
/* 1708 */       this.motionZ *= 0.98D;
/*      */     } 
/*      */     
/* 1711 */     if (Math.abs(this.motionX) < 0.005D)
/*      */     {
/* 1713 */       this.motionX = 0.0D;
/*      */     }
/*      */     
/* 1716 */     if (Math.abs(this.motionY) < 0.005D)
/*      */     {
/* 1718 */       this.motionY = 0.0D;
/*      */     }
/*      */     
/* 1721 */     if (Math.abs(this.motionZ) < 0.005D)
/*      */     {
/* 1723 */       this.motionZ = 0.0D;
/*      */     }
/*      */     
/* 1726 */     this.worldObj.theProfiler.startSection("ai");
/*      */     
/* 1728 */     if (isMovementBlocked()) {
/*      */       
/* 1730 */       this.isJumping = false;
/* 1731 */       this.moveStrafing = 0.0F;
/* 1732 */       this.moveForward = 0.0F;
/* 1733 */       this.randomYawVelocity = 0.0F;
/*      */     }
/* 1735 */     else if (isServerWorld()) {
/*      */       
/* 1737 */       this.worldObj.theProfiler.startSection("newAi");
/* 1738 */       updateEntityActionState();
/* 1739 */       this.worldObj.theProfiler.endSection();
/*      */     } 
/*      */     
/* 1742 */     this.worldObj.theProfiler.endSection();
/* 1743 */     this.worldObj.theProfiler.startSection("jump");
/*      */     
/* 1745 */     if (this.isJumping) {
/*      */       
/* 1747 */       if (isInWater())
/*      */       {
/* 1749 */         updateAITick();
/*      */       }
/* 1751 */       else if (isInLava())
/*      */       {
/* 1753 */         handleJumpLava();
/*      */       }
/* 1755 */       else if (this.onGround && this.jumpTicks == 0)
/*      */       {
/* 1757 */         jump();
/* 1758 */         this.jumpTicks = 10;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1763 */       this.jumpTicks = 0;
/*      */     } 
/*      */     
/* 1766 */     this.worldObj.theProfiler.endSection();
/* 1767 */     this.worldObj.theProfiler.startSection("travel");
/* 1768 */     this.moveStrafing *= 0.98F;
/* 1769 */     this.moveForward *= 0.98F;
/* 1770 */     this.randomYawVelocity *= 0.9F;
/* 1771 */     moveEntityWithHeading(this.moveStrafing, this.moveForward);
/* 1772 */     this.worldObj.theProfiler.endSection();
/* 1773 */     this.worldObj.theProfiler.startSection("push");
/*      */     
/* 1775 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1777 */       collideWithNearbyEntities();
/*      */     }
/*      */     
/* 1780 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateEntityActionState() {}
/*      */ 
/*      */   
/*      */   protected void collideWithNearbyEntities() {
/* 1789 */     List<Entity> list = this.worldObj.getEntitiesInAABBexcluding(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
/*      */           {
/*      */             public boolean apply(Entity p_apply_1_)
/*      */             {
/* 1793 */               return p_apply_1_.canBePushed();
/*      */             }
/*      */           }));
/*      */     
/* 1797 */     if (!list.isEmpty())
/*      */     {
/* 1799 */       for (int i = 0; i < list.size(); i++) {
/*      */         
/* 1801 */         Entity entity = list.get(i);
/* 1802 */         collideWithEntity(entity);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void collideWithEntity(Entity entityIn) {
/* 1809 */     entityIn.applyEntityCollision(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void mountEntity(Entity entityIn) {
/* 1814 */     if (this.ridingEntity != null && entityIn == null) {
/*      */       
/* 1816 */       if (!this.worldObj.isRemote)
/*      */       {
/* 1818 */         dismountEntity(this.ridingEntity);
/*      */       }
/*      */       
/* 1821 */       if (this.ridingEntity != null)
/*      */       {
/* 1823 */         this.ridingEntity.riddenByEntity = null;
/*      */       }
/*      */       
/* 1826 */       this.ridingEntity = null;
/*      */     }
/*      */     else {
/*      */       
/* 1830 */       super.mountEntity(entityIn);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRidden() {
/* 1836 */     super.updateRidden();
/* 1837 */     this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
/* 1838 */     this.onGroundSpeedFactor = 0.0F;
/* 1839 */     this.fallDistance = 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
/* 1844 */     this.newPosX = x;
/* 1845 */     this.newPosY = y;
/* 1846 */     this.newPosZ = z;
/* 1847 */     this.newRotationYaw = yaw;
/* 1848 */     this.newRotationPitch = pitch;
/* 1849 */     this.newPosRotationIncrements = posRotationIncrements;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setJumping(boolean jumping) {
/* 1854 */     this.isJumping = jumping;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
/* 1859 */     if (!p_71001_1_.isDead && !this.worldObj.isRemote) {
/*      */       
/* 1861 */       EntityTracker entitytracker = ((WorldServer)this.worldObj).getEntityTracker();
/*      */       
/* 1863 */       if (p_71001_1_ instanceof net.minecraft.entity.item.EntityItem)
/*      */       {
/* 1865 */         entitytracker.sendToAllTrackingEntity(p_71001_1_, (Packet)new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
/*      */       }
/*      */       
/* 1868 */       if (p_71001_1_ instanceof net.minecraft.entity.projectile.EntityArrow)
/*      */       {
/* 1870 */         entitytracker.sendToAllTrackingEntity(p_71001_1_, (Packet)new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
/*      */       }
/*      */       
/* 1873 */       if (p_71001_1_ instanceof EntityXPOrb)
/*      */       {
/* 1875 */         entitytracker.sendToAllTrackingEntity(p_71001_1_, (Packet)new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canEntityBeSeen(Entity entityIn) {
/* 1882 */     return (this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + getEyeHeight(), this.posZ), new Vec3(entityIn.posX, entityIn.posY + entityIn.getEyeHeight(), entityIn.posZ)) == null);
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getLookVec() {
/* 1887 */     return getLook(1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getLook(float partialTicks) {
/* 1892 */     if (partialTicks == 1.0F)
/*      */     {
/* 1894 */       return getVectorForRotation(this.rotationPitch, this.rotationYawHead);
/*      */     }
/*      */ 
/*      */     
/* 1898 */     float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
/* 1899 */     float f1 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
/* 1900 */     return getVectorForRotation(f, f1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSwingProgress(float partialTickTime) {
/* 1906 */     float f = this.swingProgress - this.prevSwingProgress;
/*      */     
/* 1908 */     if (f < 0.0F)
/*      */     {
/* 1910 */       f++;
/*      */     }
/*      */     
/* 1913 */     return this.prevSwingProgress + f * partialTickTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isServerWorld() {
/* 1918 */     return !this.worldObj.isRemote;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeCollidedWith() {
/* 1923 */     return !this.isDead;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBePushed() {
/* 1928 */     return !this.isDead;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setBeenAttacked() {
/* 1933 */     this.velocityChanged = (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue());
/*      */   }
/*      */ 
/*      */   
/*      */   public float getRotationYawHead() {
/* 1938 */     return this.rotationYawHead;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRotationYawHead(float rotation) {
/* 1943 */     this.rotationYawHead = rotation;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRenderYawOffset(float offset) {
/* 1948 */     this.renderYawOffset = offset;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getAbsorptionAmount() {
/* 1953 */     return this.absorptionAmount;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAbsorptionAmount(float amount) {
/* 1958 */     if (amount < 0.0F)
/*      */     {
/* 1960 */       amount = 0.0F;
/*      */     }
/*      */     
/* 1963 */     this.absorptionAmount = amount;
/*      */   }
/*      */ 
/*      */   
/*      */   public Team getTeam() {
/* 1968 */     return (Team)this.worldObj.getScoreboard().getPlayersTeam(getUniqueID().toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOnSameTeam(EntityLivingBase otherEntity) {
/* 1973 */     return isOnTeam(otherEntity.getTeam());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOnTeam(Team teamIn) {
/* 1978 */     return (getTeam() != null) ? getTeam().isSameTeam(teamIn) : false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendEnterCombat() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendEndCombat() {}
/*      */ 
/*      */   
/*      */   protected void markPotionsDirty() {
/* 1991 */     this.potionsNeedUpdate = true;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */