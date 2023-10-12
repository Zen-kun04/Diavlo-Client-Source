/*      */ package net.minecraft.entity;
/*      */ 
/*      */ import java.util.UUID;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.ai.EntityAITasks;
/*      */ import net.minecraft.entity.ai.EntityJumpHelper;
/*      */ import net.minecraft.entity.ai.EntityLookHelper;
/*      */ import net.minecraft.entity.ai.EntityMoveHelper;
/*      */ import net.minecraft.entity.ai.EntitySenses;
/*      */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.monster.EntityGhast;
/*      */ import net.minecraft.entity.passive.EntityTameable;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemArmor;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.item.ItemSword;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagFloat;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.server.S1BPacketEntityAttach;
/*      */ import net.minecraft.pathfinding.PathNavigate;
/*      */ import net.minecraft.pathfinding.PathNavigateGround;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.src.Config;
/*      */ import net.minecraft.stats.AchievementList;
/*      */ import net.minecraft.stats.StatBase;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.optifine.reflect.Reflector;
/*      */ 
/*      */ public abstract class EntityLiving
/*      */   extends EntityLivingBase
/*      */ {
/*      */   public int livingSoundTime;
/*      */   protected int experienceValue;
/*      */   private EntityLookHelper lookHelper;
/*      */   protected EntityMoveHelper moveHelper;
/*      */   protected EntityJumpHelper jumpHelper;
/*      */   private EntityBodyHelper bodyHelper;
/*      */   protected PathNavigate navigator;
/*      */   protected final EntityAITasks tasks;
/*      */   protected final EntityAITasks targetTasks;
/*      */   private EntityLivingBase attackTarget;
/*      */   private EntitySenses senses;
/*   56 */   private ItemStack[] equipment = new ItemStack[5];
/*   57 */   protected float[] equipmentDropChances = new float[5];
/*      */   private boolean canPickUpLoot;
/*      */   private boolean persistenceRequired;
/*      */   private boolean isLeashed;
/*      */   private Entity leashedToEntity;
/*      */   private NBTTagCompound leashNBTTag;
/*   63 */   private UUID teamUuid = null;
/*   64 */   private String teamUuidString = null;
/*      */ 
/*      */   
/*      */   public EntityLiving(World worldIn) {
/*   68 */     super(worldIn);
/*   69 */     this.tasks = new EntityAITasks((worldIn != null && worldIn.theProfiler != null) ? worldIn.theProfiler : null);
/*   70 */     this.targetTasks = new EntityAITasks((worldIn != null && worldIn.theProfiler != null) ? worldIn.theProfiler : null);
/*   71 */     this.lookHelper = new EntityLookHelper(this);
/*   72 */     this.moveHelper = new EntityMoveHelper(this);
/*   73 */     this.jumpHelper = new EntityJumpHelper(this);
/*   74 */     this.bodyHelper = new EntityBodyHelper(this);
/*   75 */     this.navigator = getNewNavigator(worldIn);
/*   76 */     this.senses = new EntitySenses(this);
/*      */     
/*   78 */     for (int i = 0; i < this.equipmentDropChances.length; i++)
/*      */     {
/*   80 */       this.equipmentDropChances[i] = 0.085F;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyEntityAttributes() {
/*   86 */     super.applyEntityAttributes();
/*   87 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   protected PathNavigate getNewNavigator(World worldIn) {
/*   92 */     return (PathNavigate)new PathNavigateGround(this, worldIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityLookHelper getLookHelper() {
/*   97 */     return this.lookHelper;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityMoveHelper getMoveHelper() {
/*  102 */     return this.moveHelper;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityJumpHelper getJumpHelper() {
/*  107 */     return this.jumpHelper;
/*      */   }
/*      */ 
/*      */   
/*      */   public PathNavigate getNavigator() {
/*  112 */     return this.navigator;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntitySenses getEntitySenses() {
/*  117 */     return this.senses;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityLivingBase getAttackTarget() {
/*  122 */     return this.attackTarget;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
/*  127 */     this.attackTarget = entitylivingbaseIn;
/*  128 */     Reflector.callVoid(Reflector.ForgeHooks_onLivingSetAttackTarget, new Object[] { this, entitylivingbaseIn });
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
/*  133 */     return (cls != EntityGhast.class);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void eatGrassBonus() {}
/*      */ 
/*      */   
/*      */   protected void entityInit() {
/*  142 */     super.entityInit();
/*  143 */     this.dataWatcher.addObject(15, Byte.valueOf((byte)0));
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTalkInterval() {
/*  148 */     return 80;
/*      */   }
/*      */ 
/*      */   
/*      */   public void playLivingSound() {
/*  153 */     String s = getLivingSound();
/*      */     
/*  155 */     if (s != null)
/*      */     {
/*  157 */       playSound(s, getSoundVolume(), getSoundPitch());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEntityUpdate() {
/*  163 */     super.onEntityUpdate();
/*  164 */     this.worldObj.theProfiler.startSection("mobBaseTick");
/*      */     
/*  166 */     if (isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
/*      */       
/*  168 */       this.livingSoundTime = -getTalkInterval();
/*  169 */       playLivingSound();
/*      */     } 
/*      */     
/*  172 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getExperiencePoints(EntityPlayer player) {
/*  177 */     if (this.experienceValue > 0) {
/*      */       
/*  179 */       int i = this.experienceValue;
/*  180 */       ItemStack[] aitemstack = getInventory();
/*      */       
/*  182 */       for (int j = 0; j < aitemstack.length; j++) {
/*      */         
/*  184 */         if (aitemstack[j] != null && this.equipmentDropChances[j] <= 1.0F)
/*      */         {
/*  186 */           i += 1 + this.rand.nextInt(3);
/*      */         }
/*      */       } 
/*      */       
/*  190 */       return i;
/*      */     } 
/*      */ 
/*      */     
/*  194 */     return this.experienceValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void spawnExplosionParticle() {
/*  200 */     if (this.worldObj.isRemote) {
/*      */       
/*  202 */       for (int i = 0; i < 20; i++)
/*      */       {
/*  204 */         double d0 = this.rand.nextGaussian() * 0.02D;
/*  205 */         double d1 = this.rand.nextGaussian() * 0.02D;
/*  206 */         double d2 = this.rand.nextGaussian() * 0.02D;
/*  207 */         double d3 = 10.0D;
/*  208 */         this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width - d0 * d3, this.posY + (this.rand.nextFloat() * this.height) - d1 * d3, this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width - d2 * d3, d0, d1, d2, new int[0]);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  213 */       this.worldObj.setEntityState(this, (byte)20);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleStatusUpdate(byte id) {
/*  219 */     if (id == 20) {
/*      */       
/*  221 */       spawnExplosionParticle();
/*      */     }
/*      */     else {
/*      */       
/*  225 */       super.handleStatusUpdate(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdate() {
/*  231 */     if (Config.isSmoothWorld() && canSkipUpdate()) {
/*      */       
/*  233 */       onUpdateMinimal();
/*      */     }
/*      */     else {
/*      */       
/*  237 */       super.onUpdate();
/*      */       
/*  239 */       if (!this.worldObj.isRemote)
/*      */       {
/*  241 */         updateLeashedState();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected float updateDistance(float p_110146_1_, float p_110146_2_) {
/*  248 */     this.bodyHelper.updateRenderAngles();
/*  249 */     return p_110146_2_;
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getLivingSound() {
/*  254 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Item getDropItem() {
/*  259 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/*  264 */     Item item = getDropItem();
/*      */     
/*  266 */     if (item != null) {
/*      */       
/*  268 */       int i = this.rand.nextInt(3);
/*      */       
/*  270 */       if (lootingModifier > 0)
/*      */       {
/*  272 */         i += this.rand.nextInt(lootingModifier + 1);
/*      */       }
/*      */       
/*  275 */       for (int j = 0; j < i; j++)
/*      */       {
/*  277 */         dropItem(item, 1);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  284 */     super.writeEntityToNBT(tagCompound);
/*  285 */     tagCompound.setBoolean("CanPickUpLoot", canPickUpLoot());
/*  286 */     tagCompound.setBoolean("PersistenceRequired", this.persistenceRequired);
/*  287 */     NBTTagList nbttaglist = new NBTTagList();
/*      */     
/*  289 */     for (int i = 0; i < this.equipment.length; i++) {
/*      */       
/*  291 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*      */       
/*  293 */       if (this.equipment[i] != null)
/*      */       {
/*  295 */         this.equipment[i].writeToNBT(nbttagcompound);
/*      */       }
/*      */       
/*  298 */       nbttaglist.appendTag((NBTBase)nbttagcompound);
/*      */     } 
/*      */     
/*  301 */     tagCompound.setTag("Equipment", (NBTBase)nbttaglist);
/*  302 */     NBTTagList nbttaglist1 = new NBTTagList();
/*      */     
/*  304 */     for (int j = 0; j < this.equipmentDropChances.length; j++)
/*      */     {
/*  306 */       nbttaglist1.appendTag((NBTBase)new NBTTagFloat(this.equipmentDropChances[j]));
/*      */     }
/*      */     
/*  309 */     tagCompound.setTag("DropChances", (NBTBase)nbttaglist1);
/*  310 */     tagCompound.setBoolean("Leashed", this.isLeashed);
/*      */     
/*  312 */     if (this.leashedToEntity != null) {
/*      */       
/*  314 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*      */       
/*  316 */       if (this.leashedToEntity instanceof EntityLivingBase) {
/*      */         
/*  318 */         nbttagcompound1.setLong("UUIDMost", this.leashedToEntity.getUniqueID().getMostSignificantBits());
/*  319 */         nbttagcompound1.setLong("UUIDLeast", this.leashedToEntity.getUniqueID().getLeastSignificantBits());
/*      */       }
/*  321 */       else if (this.leashedToEntity instanceof EntityHanging) {
/*      */         
/*  323 */         BlockPos blockpos = ((EntityHanging)this.leashedToEntity).getHangingPosition();
/*  324 */         nbttagcompound1.setInteger("X", blockpos.getX());
/*  325 */         nbttagcompound1.setInteger("Y", blockpos.getY());
/*  326 */         nbttagcompound1.setInteger("Z", blockpos.getZ());
/*      */       } 
/*      */       
/*  329 */       tagCompound.setTag("Leash", (NBTBase)nbttagcompound1);
/*      */     } 
/*      */     
/*  332 */     if (isAIDisabled())
/*      */     {
/*  334 */       tagCompound.setBoolean("NoAI", isAIDisabled());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  340 */     super.readEntityFromNBT(tagCompund);
/*      */     
/*  342 */     if (tagCompund.hasKey("CanPickUpLoot", 1))
/*      */     {
/*  344 */       setCanPickUpLoot(tagCompund.getBoolean("CanPickUpLoot"));
/*      */     }
/*      */     
/*  347 */     this.persistenceRequired = tagCompund.getBoolean("PersistenceRequired");
/*      */     
/*  349 */     if (tagCompund.hasKey("Equipment", 9)) {
/*      */       
/*  351 */       NBTTagList nbttaglist = tagCompund.getTagList("Equipment", 10);
/*      */       
/*  353 */       for (int i = 0; i < this.equipment.length; i++)
/*      */       {
/*  355 */         this.equipment[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
/*      */       }
/*      */     } 
/*      */     
/*  359 */     if (tagCompund.hasKey("DropChances", 9)) {
/*      */       
/*  361 */       NBTTagList nbttaglist1 = tagCompund.getTagList("DropChances", 5);
/*      */       
/*  363 */       for (int j = 0; j < nbttaglist1.tagCount(); j++)
/*      */       {
/*  365 */         this.equipmentDropChances[j] = nbttaglist1.getFloatAt(j);
/*      */       }
/*      */     } 
/*      */     
/*  369 */     this.isLeashed = tagCompund.getBoolean("Leashed");
/*      */     
/*  371 */     if (this.isLeashed && tagCompund.hasKey("Leash", 10))
/*      */     {
/*  373 */       this.leashNBTTag = tagCompund.getCompoundTag("Leash");
/*      */     }
/*      */     
/*  376 */     setNoAI(tagCompund.getBoolean("NoAI"));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMoveForward(float p_70657_1_) {
/*  381 */     this.moveForward = p_70657_1_;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAIMoveSpeed(float speedIn) {
/*  386 */     super.setAIMoveSpeed(speedIn);
/*  387 */     setMoveForward(speedIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onLivingUpdate() {
/*  392 */     super.onLivingUpdate();
/*  393 */     this.worldObj.theProfiler.startSection("looting");
/*      */     
/*  395 */     if (!this.worldObj.isRemote && canPickUpLoot() && !this.dead && this.worldObj.getGameRules().getBoolean("mobGriefing"))
/*      */     {
/*  397 */       for (EntityItem entityitem : this.worldObj.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D))) {
/*      */         
/*  399 */         if (!entityitem.isDead && entityitem.getEntityItem() != null && !entityitem.cannotPickup())
/*      */         {
/*  401 */           updateEquipmentIfNeeded(entityitem);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*  406 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
/*  411 */     ItemStack itemstack = itemEntity.getEntityItem();
/*  412 */     int i = getArmorPosition(itemstack);
/*      */     
/*  414 */     if (i > -1) {
/*      */       
/*  416 */       boolean flag = true;
/*  417 */       ItemStack itemstack1 = getEquipmentInSlot(i);
/*      */       
/*  419 */       if (itemstack1 != null)
/*      */       {
/*  421 */         if (i == 0) {
/*      */           
/*  423 */           if (itemstack.getItem() instanceof ItemSword && !(itemstack1.getItem() instanceof ItemSword)) {
/*      */             
/*  425 */             flag = true;
/*      */           }
/*  427 */           else if (itemstack.getItem() instanceof ItemSword && itemstack1.getItem() instanceof ItemSword) {
/*      */             
/*  429 */             ItemSword itemsword = (ItemSword)itemstack.getItem();
/*  430 */             ItemSword itemsword1 = (ItemSword)itemstack1.getItem();
/*      */             
/*  432 */             if (itemsword.getDamageVsEntity() != itemsword1.getDamageVsEntity())
/*      */             {
/*  434 */               flag = (itemsword.getDamageVsEntity() > itemsword1.getDamageVsEntity());
/*      */             }
/*      */             else
/*      */             {
/*  438 */               flag = (itemstack.getMetadata() > itemstack1.getMetadata() || (itemstack.hasTagCompound() && !itemstack1.hasTagCompound()));
/*      */             }
/*      */           
/*  441 */           } else if (itemstack.getItem() instanceof net.minecraft.item.ItemBow && itemstack1.getItem() instanceof net.minecraft.item.ItemBow) {
/*      */             
/*  443 */             flag = (itemstack.hasTagCompound() && !itemstack1.hasTagCompound());
/*      */           }
/*      */           else {
/*      */             
/*  447 */             flag = false;
/*      */           }
/*      */         
/*  450 */         } else if (itemstack.getItem() instanceof ItemArmor && !(itemstack1.getItem() instanceof ItemArmor)) {
/*      */           
/*  452 */           flag = true;
/*      */         }
/*  454 */         else if (itemstack.getItem() instanceof ItemArmor && itemstack1.getItem() instanceof ItemArmor) {
/*      */           
/*  456 */           ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
/*  457 */           ItemArmor itemarmor1 = (ItemArmor)itemstack1.getItem();
/*      */           
/*  459 */           if (itemarmor.damageReduceAmount != itemarmor1.damageReduceAmount)
/*      */           {
/*  461 */             flag = (itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount);
/*      */           }
/*      */           else
/*      */           {
/*  465 */             flag = (itemstack.getMetadata() > itemstack1.getMetadata() || (itemstack.hasTagCompound() && !itemstack1.hasTagCompound()));
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  470 */           flag = false;
/*      */         } 
/*      */       }
/*      */       
/*  474 */       if (flag && func_175448_a(itemstack)) {
/*      */         
/*  476 */         if (itemstack1 != null && this.rand.nextFloat() - 0.1F < this.equipmentDropChances[i])
/*      */         {
/*  478 */           entityDropItem(itemstack1, 0.0F);
/*      */         }
/*      */         
/*  481 */         if (itemstack.getItem() == Items.diamond && itemEntity.getThrower() != null) {
/*      */           
/*  483 */           EntityPlayer entityplayer = this.worldObj.getPlayerEntityByName(itemEntity.getThrower());
/*      */           
/*  485 */           if (entityplayer != null)
/*      */           {
/*  487 */             entityplayer.triggerAchievement((StatBase)AchievementList.diamondsToYou);
/*      */           }
/*      */         } 
/*      */         
/*  491 */         setCurrentItemOrArmor(i, itemstack);
/*  492 */         this.equipmentDropChances[i] = 2.0F;
/*  493 */         this.persistenceRequired = true;
/*  494 */         onItemPickup((Entity)itemEntity, 1);
/*  495 */         itemEntity.setDead();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean func_175448_a(ItemStack stack) {
/*  502 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean canDespawn() {
/*  507 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void despawnEntity() {
/*  512 */     Object object = null;
/*  513 */     Object object1 = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
/*  514 */     Object object2 = Reflector.getFieldValue(Reflector.Event_Result_DENY);
/*      */     
/*  516 */     if (this.persistenceRequired) {
/*      */       
/*  518 */       this.entityAge = 0;
/*      */     }
/*  520 */     else if ((this.entityAge & 0x1F) == 31 && (object = Reflector.call(Reflector.ForgeEventFactory_canEntityDespawn, new Object[] { this })) != object1) {
/*      */       
/*  522 */       if (object == object2)
/*      */       {
/*  524 */         this.entityAge = 0;
/*      */       }
/*      */       else
/*      */       {
/*  528 */         setDead();
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  533 */       EntityPlayer entityPlayer = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
/*      */       
/*  535 */       if (entityPlayer != null) {
/*      */         
/*  537 */         double d0 = ((Entity)entityPlayer).posX - this.posX;
/*  538 */         double d1 = ((Entity)entityPlayer).posY - this.posY;
/*  539 */         double d2 = ((Entity)entityPlayer).posZ - this.posZ;
/*  540 */         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/*      */         
/*  542 */         if (canDespawn() && d3 > 16384.0D)
/*      */         {
/*  544 */           setDead();
/*      */         }
/*      */         
/*  547 */         if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && canDespawn()) {
/*      */           
/*  549 */           setDead();
/*      */         }
/*  551 */         else if (d3 < 1024.0D) {
/*      */           
/*  553 */           this.entityAge = 0;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void updateEntityActionState() {
/*  561 */     this.entityAge++;
/*  562 */     this.worldObj.theProfiler.startSection("checkDespawn");
/*  563 */     despawnEntity();
/*  564 */     this.worldObj.theProfiler.endSection();
/*  565 */     this.worldObj.theProfiler.startSection("sensing");
/*  566 */     this.senses.clearSensingCache();
/*  567 */     this.worldObj.theProfiler.endSection();
/*  568 */     this.worldObj.theProfiler.startSection("targetSelector");
/*  569 */     this.targetTasks.onUpdateTasks();
/*  570 */     this.worldObj.theProfiler.endSection();
/*  571 */     this.worldObj.theProfiler.startSection("goalSelector");
/*  572 */     this.tasks.onUpdateTasks();
/*  573 */     this.worldObj.theProfiler.endSection();
/*  574 */     this.worldObj.theProfiler.startSection("navigation");
/*  575 */     this.navigator.onUpdateNavigation();
/*  576 */     this.worldObj.theProfiler.endSection();
/*  577 */     this.worldObj.theProfiler.startSection("mob tick");
/*  578 */     updateAITasks();
/*  579 */     this.worldObj.theProfiler.endSection();
/*  580 */     this.worldObj.theProfiler.startSection("controls");
/*  581 */     this.worldObj.theProfiler.startSection("move");
/*  582 */     this.moveHelper.onUpdateMoveHelper();
/*  583 */     this.worldObj.theProfiler.endStartSection("look");
/*  584 */     this.lookHelper.onUpdateLook();
/*  585 */     this.worldObj.theProfiler.endStartSection("jump");
/*  586 */     this.jumpHelper.doJump();
/*  587 */     this.worldObj.theProfiler.endSection();
/*  588 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateAITasks() {}
/*      */ 
/*      */   
/*      */   public int getVerticalFaceSpeed() {
/*  597 */     return 40;
/*      */   }
/*      */ 
/*      */   
/*      */   public void faceEntity(Entity entityIn, float p_70625_2_, float p_70625_3_) {
/*  602 */     double d2, d0 = entityIn.posX - this.posX;
/*  603 */     double d1 = entityIn.posZ - this.posZ;
/*      */ 
/*      */     
/*  606 */     if (entityIn instanceof EntityLivingBase) {
/*      */       
/*  608 */       EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
/*  609 */       d2 = entitylivingbase.posY + entitylivingbase.getEyeHeight() - this.posY + getEyeHeight();
/*      */     }
/*      */     else {
/*      */       
/*  613 */       d2 = ((entityIn.getEntityBoundingBox()).minY + (entityIn.getEntityBoundingBox()).maxY) / 2.0D - this.posY + getEyeHeight();
/*      */     } 
/*      */     
/*  616 */     double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
/*  617 */     float f = (float)(MathHelper.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
/*  618 */     float f1 = (float)-(MathHelper.atan2(d2, d3) * 180.0D / Math.PI);
/*  619 */     this.rotationPitch = updateRotation(this.rotationPitch, f1, p_70625_3_);
/*  620 */     this.rotationYaw = updateRotation(this.rotationYaw, f, p_70625_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   private float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
/*  625 */     float f = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
/*      */     
/*  627 */     if (f > p_70663_3_)
/*      */     {
/*  629 */       f = p_70663_3_;
/*      */     }
/*      */     
/*  632 */     if (f < -p_70663_3_)
/*      */     {
/*  634 */       f = -p_70663_3_;
/*      */     }
/*      */     
/*  637 */     return p_70663_1_ + f;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getCanSpawnHere() {
/*  642 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNotColliding() {
/*  647 */     return (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(getEntityBoundingBox()));
/*      */   }
/*      */ 
/*      */   
/*      */   public float getRenderSizeModifier() {
/*  652 */     return 1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxSpawnedInChunk() {
/*  657 */     return 4;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxFallHeight() {
/*  662 */     if (getAttackTarget() == null)
/*      */     {
/*  664 */       return 3;
/*      */     }
/*      */ 
/*      */     
/*  668 */     int i = (int)(getHealth() - getMaxHealth() * 0.33F);
/*  669 */     i -= (3 - this.worldObj.getDifficulty().getDifficultyId()) * 4;
/*      */     
/*  671 */     if (i < 0)
/*      */     {
/*  673 */       i = 0;
/*      */     }
/*      */     
/*  676 */     return i + 3;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack getHeldItem() {
/*  682 */     return this.equipment[0];
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getEquipmentInSlot(int slotIn) {
/*  687 */     return this.equipment[slotIn];
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getCurrentArmor(int slotIn) {
/*  692 */     return this.equipment[slotIn + 1];
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
/*  697 */     this.equipment[slotIn] = stack;
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack[] getInventory() {
/*  702 */     return this.equipment;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
/*  707 */     for (int i = 0; i < (getInventory()).length; i++) {
/*      */       
/*  709 */       ItemStack itemstack = getEquipmentInSlot(i);
/*  710 */       boolean flag = (this.equipmentDropChances[i] > 1.0F);
/*      */       
/*  712 */       if (itemstack != null && (wasRecentlyHit || flag) && this.rand.nextFloat() - lootingModifier * 0.01F < this.equipmentDropChances[i]) {
/*      */         
/*  714 */         if (!flag && itemstack.isItemStackDamageable()) {
/*      */           
/*  716 */           int j = Math.max(itemstack.getMaxDamage() - 25, 1);
/*  717 */           int k = itemstack.getMaxDamage() - this.rand.nextInt(this.rand.nextInt(j) + 1);
/*      */           
/*  719 */           if (k > j)
/*      */           {
/*  721 */             k = j;
/*      */           }
/*      */           
/*  724 */           if (k < 1)
/*      */           {
/*  726 */             k = 1;
/*      */           }
/*      */           
/*  729 */           itemstack.setItemDamage(k);
/*      */         } 
/*      */         
/*  732 */         entityDropItem(itemstack, 0.0F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
/*  739 */     if (this.rand.nextFloat() < 0.15F * difficulty.getClampedAdditionalDifficulty()) {
/*      */       
/*  741 */       int i = this.rand.nextInt(2);
/*  742 */       float f = (this.worldObj.getDifficulty() == EnumDifficulty.HARD) ? 0.1F : 0.25F;
/*      */       
/*  744 */       if (this.rand.nextFloat() < 0.095F)
/*      */       {
/*  746 */         i++;
/*      */       }
/*      */       
/*  749 */       if (this.rand.nextFloat() < 0.095F)
/*      */       {
/*  751 */         i++;
/*      */       }
/*      */       
/*  754 */       if (this.rand.nextFloat() < 0.095F)
/*      */       {
/*  756 */         i++;
/*      */       }
/*      */       
/*  759 */       for (int j = 3; j >= 0; j--) {
/*      */         
/*  761 */         ItemStack itemstack = getCurrentArmor(j);
/*      */         
/*  763 */         if (j < 3 && this.rand.nextFloat() < f) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/*  768 */         if (itemstack == null) {
/*      */           
/*  770 */           Item item = getArmorItemForSlot(j + 1, i);
/*      */           
/*  772 */           if (item != null)
/*      */           {
/*  774 */             setCurrentItemOrArmor(j + 1, new ItemStack(item));
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getArmorPosition(ItemStack stack) {
/*  783 */     if (stack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && stack.getItem() != Items.skull) {
/*      */       
/*  785 */       if (stack.getItem() instanceof ItemArmor)
/*      */       {
/*  787 */         switch (((ItemArmor)stack.getItem()).armorType) {
/*      */           
/*      */           case 0:
/*  790 */             return 4;
/*      */           
/*      */           case 1:
/*  793 */             return 3;
/*      */           
/*      */           case 2:
/*  796 */             return 2;
/*      */           
/*      */           case 3:
/*  799 */             return 1;
/*      */         } 
/*      */       
/*      */       }
/*  803 */       return 0;
/*      */     } 
/*      */ 
/*      */     
/*  807 */     return 4;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Item getArmorItemForSlot(int armorSlot, int itemTier) {
/*  813 */     switch (armorSlot) {
/*      */       
/*      */       case 4:
/*  816 */         if (itemTier == 0)
/*      */         {
/*  818 */           return (Item)Items.leather_helmet;
/*      */         }
/*  820 */         if (itemTier == 1)
/*      */         {
/*  822 */           return (Item)Items.golden_helmet;
/*      */         }
/*  824 */         if (itemTier == 2)
/*      */         {
/*  826 */           return (Item)Items.chainmail_helmet;
/*      */         }
/*  828 */         if (itemTier == 3)
/*      */         {
/*  830 */           return (Item)Items.iron_helmet;
/*      */         }
/*  832 */         if (itemTier == 4)
/*      */         {
/*  834 */           return (Item)Items.diamond_helmet;
/*      */         }
/*      */       
/*      */       case 3:
/*  838 */         if (itemTier == 0)
/*      */         {
/*  840 */           return (Item)Items.leather_chestplate;
/*      */         }
/*  842 */         if (itemTier == 1)
/*      */         {
/*  844 */           return (Item)Items.golden_chestplate;
/*      */         }
/*  846 */         if (itemTier == 2)
/*      */         {
/*  848 */           return (Item)Items.chainmail_chestplate;
/*      */         }
/*  850 */         if (itemTier == 3)
/*      */         {
/*  852 */           return (Item)Items.iron_chestplate;
/*      */         }
/*  854 */         if (itemTier == 4)
/*      */         {
/*  856 */           return (Item)Items.diamond_chestplate;
/*      */         }
/*      */       
/*      */       case 2:
/*  860 */         if (itemTier == 0)
/*      */         {
/*  862 */           return (Item)Items.leather_leggings;
/*      */         }
/*  864 */         if (itemTier == 1)
/*      */         {
/*  866 */           return (Item)Items.golden_leggings;
/*      */         }
/*  868 */         if (itemTier == 2)
/*      */         {
/*  870 */           return (Item)Items.chainmail_leggings;
/*      */         }
/*  872 */         if (itemTier == 3)
/*      */         {
/*  874 */           return (Item)Items.iron_leggings;
/*      */         }
/*  876 */         if (itemTier == 4)
/*      */         {
/*  878 */           return (Item)Items.diamond_leggings;
/*      */         }
/*      */       
/*      */       case 1:
/*  882 */         if (itemTier == 0)
/*      */         {
/*  884 */           return (Item)Items.leather_boots;
/*      */         }
/*  886 */         if (itemTier == 1)
/*      */         {
/*  888 */           return (Item)Items.golden_boots;
/*      */         }
/*  890 */         if (itemTier == 2)
/*      */         {
/*  892 */           return (Item)Items.chainmail_boots;
/*      */         }
/*  894 */         if (itemTier == 3)
/*      */         {
/*  896 */           return (Item)Items.iron_boots;
/*      */         }
/*  898 */         if (itemTier == 4)
/*      */         {
/*  900 */           return (Item)Items.diamond_boots;
/*      */         }
/*      */         break;
/*      */     } 
/*  904 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficulty) {
/*  910 */     float f = difficulty.getClampedAdditionalDifficulty();
/*      */     
/*  912 */     if (getHeldItem() != null && this.rand.nextFloat() < 0.25F * f)
/*      */     {
/*  914 */       EnchantmentHelper.addRandomEnchantment(this.rand, getHeldItem(), (int)(5.0F + f * this.rand.nextInt(18)));
/*      */     }
/*      */     
/*  917 */     for (int i = 0; i < 4; i++) {
/*      */       
/*  919 */       ItemStack itemstack = getCurrentArmor(i);
/*      */       
/*  921 */       if (itemstack != null && this.rand.nextFloat() < 0.5F * f)
/*      */       {
/*  923 */         EnchantmentHelper.addRandomEnchantment(this.rand, itemstack, (int)(5.0F + f * this.rand.nextInt(18)));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/*  930 */     getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
/*  931 */     return livingdata;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeSteered() {
/*  936 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void enablePersistence() {
/*  941 */     this.persistenceRequired = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEquipmentDropChance(int slotIn, float chance) {
/*  946 */     this.equipmentDropChances[slotIn] = chance;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canPickUpLoot() {
/*  951 */     return this.canPickUpLoot;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPickUpLoot(boolean canPickup) {
/*  956 */     this.canPickUpLoot = canPickup;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNoDespawnRequired() {
/*  961 */     return this.persistenceRequired;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean interactFirst(EntityPlayer playerIn) {
/*  966 */     if (getLeashed() && getLeashedToEntity() == playerIn) {
/*      */       
/*  968 */       clearLeashed(true, !playerIn.capabilities.isCreativeMode);
/*  969 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  973 */     ItemStack itemstack = playerIn.inventory.getCurrentItem();
/*      */     
/*  975 */     if (itemstack != null && itemstack.getItem() == Items.lead && allowLeashing()) {
/*      */       
/*  977 */       if (!(this instanceof EntityTameable) || !((EntityTameable)this).isTamed()) {
/*      */         
/*  979 */         setLeashedToEntity((Entity)playerIn, true);
/*  980 */         itemstack.stackSize--;
/*  981 */         return true;
/*      */       } 
/*      */       
/*  984 */       if (((EntityTameable)this).isOwner((EntityLivingBase)playerIn)) {
/*      */         
/*  986 */         setLeashedToEntity((Entity)playerIn, true);
/*  987 */         itemstack.stackSize--;
/*  988 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/*  992 */     if (interact(playerIn))
/*      */     {
/*  994 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  998 */     return super.interactFirst(playerIn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean interact(EntityPlayer player) {
/* 1005 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateLeashedState() {
/* 1010 */     if (this.leashNBTTag != null)
/*      */     {
/* 1012 */       recreateLeash();
/*      */     }
/*      */     
/* 1015 */     if (this.isLeashed) {
/*      */       
/* 1017 */       if (!isEntityAlive())
/*      */       {
/* 1019 */         clearLeashed(true, true);
/*      */       }
/*      */       
/* 1022 */       if (this.leashedToEntity == null || this.leashedToEntity.isDead)
/*      */       {
/* 1024 */         clearLeashed(true, true);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearLeashed(boolean sendPacket, boolean dropLead) {
/* 1031 */     if (this.isLeashed) {
/*      */       
/* 1033 */       this.isLeashed = false;
/* 1034 */       this.leashedToEntity = null;
/*      */       
/* 1036 */       if (!this.worldObj.isRemote && dropLead)
/*      */       {
/* 1038 */         dropItem(Items.lead, 1);
/*      */       }
/*      */       
/* 1041 */       if (!this.worldObj.isRemote && sendPacket && this.worldObj instanceof WorldServer)
/*      */       {
/* 1043 */         ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, (Packet)new S1BPacketEntityAttach(1, this, (Entity)null));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean allowLeashing() {
/* 1050 */     return (!getLeashed() && !(this instanceof net.minecraft.entity.monster.IMob));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getLeashed() {
/* 1055 */     return this.isLeashed;
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity getLeashedToEntity() {
/* 1060 */     return this.leashedToEntity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLeashedToEntity(Entity entityIn, boolean sendAttachNotification) {
/* 1065 */     this.isLeashed = true;
/* 1066 */     this.leashedToEntity = entityIn;
/*      */     
/* 1068 */     if (!this.worldObj.isRemote && sendAttachNotification && this.worldObj instanceof WorldServer)
/*      */     {
/* 1070 */       ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, (Packet)new S1BPacketEntityAttach(1, this, this.leashedToEntity));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void recreateLeash() {
/* 1076 */     if (this.isLeashed && this.leashNBTTag != null)
/*      */     {
/* 1078 */       if (this.leashNBTTag.hasKey("UUIDMost", 4) && this.leashNBTTag.hasKey("UUIDLeast", 4)) {
/*      */         
/* 1080 */         UUID uuid = new UUID(this.leashNBTTag.getLong("UUIDMost"), this.leashNBTTag.getLong("UUIDLeast"));
/*      */         
/* 1082 */         for (EntityLivingBase entitylivingbase : this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(10.0D, 10.0D, 10.0D))) {
/*      */           
/* 1084 */           if (entitylivingbase.getUniqueID().equals(uuid)) {
/*      */             
/* 1086 */             this.leashedToEntity = entitylivingbase;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/* 1091 */       } else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
/*      */         
/* 1093 */         BlockPos blockpos = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
/* 1094 */         EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(this.worldObj, blockpos);
/*      */         
/* 1096 */         if (entityleashknot == null)
/*      */         {
/* 1098 */           entityleashknot = EntityLeashKnot.createKnot(this.worldObj, blockpos);
/*      */         }
/*      */         
/* 1101 */         this.leashedToEntity = entityleashknot;
/*      */       }
/*      */       else {
/*      */         
/* 1105 */         clearLeashed(false, true);
/*      */       } 
/*      */     }
/*      */     
/* 1109 */     this.leashNBTTag = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
/*      */     int i;
/* 1116 */     if (inventorySlot == 99) {
/*      */       
/* 1118 */       i = 0;
/*      */     }
/*      */     else {
/*      */       
/* 1122 */       i = inventorySlot - 100 + 1;
/*      */       
/* 1124 */       if (i < 0 || i >= this.equipment.length)
/*      */       {
/* 1126 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1130 */     if (itemStackIn == null || getArmorPosition(itemStackIn) == i || (i == 4 && itemStackIn.getItem() instanceof net.minecraft.item.ItemBlock)) {
/*      */       
/* 1132 */       setCurrentItemOrArmor(i, itemStackIn);
/* 1133 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1137 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isServerWorld() {
/* 1143 */     return (super.isServerWorld() && !isAIDisabled());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNoAI(boolean disable) {
/* 1148 */     this.dataWatcher.updateObject(15, Byte.valueOf((byte)(disable ? 1 : 0)));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAIDisabled() {
/* 1153 */     return (this.dataWatcher.getWatchableObjectByte(15) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean canSkipUpdate() {
/* 1158 */     if (isChild())
/*      */     {
/* 1160 */       return false;
/*      */     }
/* 1162 */     if (this.hurtTime > 0)
/*      */     {
/* 1164 */       return false;
/*      */     }
/* 1166 */     if (this.ticksExisted < 20)
/*      */     {
/* 1168 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1172 */     World world = getEntityWorld();
/*      */     
/* 1174 */     if (world == null)
/*      */     {
/* 1176 */       return false;
/*      */     }
/* 1178 */     if (world.playerEntities.size() != 1)
/*      */     {
/* 1180 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1184 */     Entity entity = world.playerEntities.get(0);
/* 1185 */     double d0 = Math.max(Math.abs(this.posX - entity.posX) - 16.0D, 0.0D);
/* 1186 */     double d1 = Math.max(Math.abs(this.posZ - entity.posZ) - 16.0D, 0.0D);
/* 1187 */     double d2 = d0 * d0 + d1 * d1;
/* 1188 */     return !isInRangeToRenderDist(d2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void onUpdateMinimal() {
/* 1195 */     this.entityAge++;
/*      */     
/* 1197 */     if (this instanceof net.minecraft.entity.monster.EntityMob) {
/*      */       
/* 1199 */       float f = getBrightness(1.0F);
/*      */       
/* 1201 */       if (f > 0.5F)
/*      */       {
/* 1203 */         this.entityAge += 2;
/*      */       }
/*      */     } 
/*      */     
/* 1207 */     despawnEntity();
/*      */   }
/*      */ 
/*      */   
/*      */   public Team getTeam() {
/* 1212 */     UUID uuid = getUniqueID();
/*      */     
/* 1214 */     if (this.teamUuid != uuid) {
/*      */       
/* 1216 */       this.teamUuid = uuid;
/* 1217 */       this.teamUuidString = uuid.toString();
/*      */     } 
/*      */     
/* 1220 */     return (Team)this.worldObj.getScoreboard().getPlayersTeam(this.teamUuidString);
/*      */   }
/*      */   
/*      */   public enum SpawnPlacementType
/*      */   {
/* 1225 */     ON_GROUND,
/* 1226 */     IN_AIR,
/* 1227 */     IN_WATER;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityLiving.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */