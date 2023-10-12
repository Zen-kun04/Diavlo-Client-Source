/*      */ package net.minecraft.entity.item;
/*      */ 
/*      */ import com.google.common.collect.Maps;
/*      */ import java.util.Map;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockRailBase;
/*      */ import net.minecraft.block.BlockRailPowered;
/*      */ import net.minecraft.block.properties.IProperty;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.EntityMinecartCommandBlock;
/*      */ import net.minecraft.entity.ai.EntityMinecartMobSpawner;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.IWorldNameable;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ 
/*      */ public abstract class EntityMinecart
/*      */   extends Entity implements IWorldNameable {
/*      */   private boolean isInReverse;
/*      */   private String entityName;
/*   37 */   private static final int[][][] matrix = new int[][][] { { { 0, 0, -1 }, { 0, 0, 1 } }, { { -1, 0, 0 }, { 1, 0, 0 } }, { { -1, -1, 0 }, { 1, 0, 0 } }, { { -1, 0, 0 }, { 1, -1, 0 } }, { { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1, 0, 0 } }, { { 0, 0, 1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { 1, 0, 0 } } };
/*      */   
/*      */   private int turnProgress;
/*      */   private double minecartX;
/*      */   private double minecartY;
/*      */   private double minecartZ;
/*      */   private double minecartYaw;
/*      */   private double minecartPitch;
/*      */   private double velocityX;
/*      */   private double velocityY;
/*      */   private double velocityZ;
/*      */   
/*      */   public EntityMinecart(World worldIn) {
/*   50 */     super(worldIn);
/*   51 */     this.preventEntitySpawning = true;
/*   52 */     setSize(0.98F, 0.7F);
/*      */   }
/*      */ 
/*      */   
/*      */   public static EntityMinecart getMinecart(World worldIn, double x, double y, double z, EnumMinecartType type) {
/*   57 */     switch (type) {
/*      */       
/*      */       case ASCENDING_EAST:
/*   60 */         return new EntityMinecartChest(worldIn, x, y, z);
/*      */       
/*      */       case ASCENDING_WEST:
/*   63 */         return new EntityMinecartFurnace(worldIn, x, y, z);
/*      */       
/*      */       case ASCENDING_NORTH:
/*   66 */         return new EntityMinecartTNT(worldIn, x, y, z);
/*      */       
/*      */       case ASCENDING_SOUTH:
/*   69 */         return (EntityMinecart)new EntityMinecartMobSpawner(worldIn, x, y, z);
/*      */       
/*      */       case null:
/*   72 */         return new EntityMinecartHopper(worldIn, x, y, z);
/*      */       
/*      */       case null:
/*   75 */         return (EntityMinecart)new EntityMinecartCommandBlock(worldIn, x, y, z);
/*      */     } 
/*      */     
/*   78 */     return new EntityMinecartEmpty(worldIn, x, y, z);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean canTriggerWalking() {
/*   84 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void entityInit() {
/*   89 */     this.dataWatcher.addObject(17, new Integer(0));
/*   90 */     this.dataWatcher.addObject(18, new Integer(1));
/*   91 */     this.dataWatcher.addObject(19, new Float(0.0F));
/*   92 */     this.dataWatcher.addObject(20, new Integer(0));
/*   93 */     this.dataWatcher.addObject(21, new Integer(6));
/*   94 */     this.dataWatcher.addObject(22, Byte.valueOf((byte)0));
/*      */   }
/*      */ 
/*      */   
/*      */   public AxisAlignedBB getCollisionBox(Entity entityIn) {
/*   99 */     return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
/*      */   }
/*      */ 
/*      */   
/*      */   public AxisAlignedBB getCollisionBoundingBox() {
/*  104 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBePushed() {
/*  109 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityMinecart(World worldIn, double x, double y, double z) {
/*  114 */     this(worldIn);
/*  115 */     setPosition(x, y, z);
/*  116 */     this.motionX = 0.0D;
/*  117 */     this.motionY = 0.0D;
/*  118 */     this.motionZ = 0.0D;
/*  119 */     this.prevPosX = x;
/*  120 */     this.prevPosY = y;
/*  121 */     this.prevPosZ = z;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getMountedYOffset() {
/*  126 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  131 */     if (!this.worldObj.isRemote && !this.isDead) {
/*      */       
/*  133 */       if (isEntityInvulnerable(source))
/*      */       {
/*  135 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  139 */       setRollingDirection(-getRollingDirection());
/*  140 */       setRollingAmplitude(10);
/*  141 */       setBeenAttacked();
/*  142 */       setDamage(getDamage() + amount * 10.0F);
/*  143 */       boolean flag = (source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode);
/*      */       
/*  145 */       if (flag || getDamage() > 40.0F) {
/*      */         
/*  147 */         if (this.riddenByEntity != null)
/*      */         {
/*  149 */           this.riddenByEntity.mountEntity((Entity)null);
/*      */         }
/*      */         
/*  152 */         if (flag && !hasCustomName()) {
/*      */           
/*  154 */           setDead();
/*      */         }
/*      */         else {
/*      */           
/*  158 */           killMinecart(source);
/*      */         } 
/*      */       } 
/*      */       
/*  162 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  167 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void killMinecart(DamageSource source) {
/*  173 */     setDead();
/*      */     
/*  175 */     if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
/*      */       
/*  177 */       ItemStack itemstack = new ItemStack(Items.minecart, 1);
/*      */       
/*  179 */       if (this.entityName != null)
/*      */       {
/*  181 */         itemstack.setStackDisplayName(this.entityName);
/*      */       }
/*      */       
/*  184 */       entityDropItem(itemstack, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void performHurtAnimation() {
/*  190 */     setRollingDirection(-getRollingDirection());
/*  191 */     setRollingAmplitude(10);
/*  192 */     setDamage(getDamage() + getDamage() * 10.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeCollidedWith() {
/*  197 */     return !this.isDead;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDead() {
/*  202 */     super.setDead();
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdate() {
/*  207 */     if (getRollingAmplitude() > 0)
/*      */     {
/*  209 */       setRollingAmplitude(getRollingAmplitude() - 1);
/*      */     }
/*      */     
/*  212 */     if (getDamage() > 0.0F)
/*      */     {
/*  214 */       setDamage(getDamage() - 1.0F);
/*      */     }
/*      */     
/*  217 */     if (this.posY < -64.0D)
/*      */     {
/*  219 */       kill();
/*      */     }
/*      */     
/*  222 */     if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
/*      */       
/*  224 */       this.worldObj.theProfiler.startSection("portal");
/*  225 */       MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
/*  226 */       int i = getMaxInPortalTime();
/*      */       
/*  228 */       if (this.inPortal) {
/*      */         
/*  230 */         if (minecraftserver.getAllowNether())
/*      */         {
/*  232 */           if (this.ridingEntity == null && this.portalCounter++ >= i) {
/*      */             int j;
/*  234 */             this.portalCounter = i;
/*  235 */             this.timeUntilPortal = getPortalCooldown();
/*      */ 
/*      */             
/*  238 */             if (this.worldObj.provider.getDimensionId() == -1) {
/*      */               
/*  240 */               j = 0;
/*      */             }
/*      */             else {
/*      */               
/*  244 */               j = -1;
/*      */             } 
/*      */             
/*  247 */             travelToDimension(j);
/*      */           } 
/*      */           
/*  250 */           this.inPortal = false;
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  255 */         if (this.portalCounter > 0)
/*      */         {
/*  257 */           this.portalCounter -= 4;
/*      */         }
/*      */         
/*  260 */         if (this.portalCounter < 0)
/*      */         {
/*  262 */           this.portalCounter = 0;
/*      */         }
/*      */       } 
/*      */       
/*  266 */       if (this.timeUntilPortal > 0)
/*      */       {
/*  268 */         this.timeUntilPortal--;
/*      */       }
/*      */       
/*  271 */       this.worldObj.theProfiler.endSection();
/*      */     } 
/*      */     
/*  274 */     if (this.worldObj.isRemote) {
/*      */       
/*  276 */       if (this.turnProgress > 0)
/*      */       {
/*  278 */         double d4 = this.posX + (this.minecartX - this.posX) / this.turnProgress;
/*  279 */         double d5 = this.posY + (this.minecartY - this.posY) / this.turnProgress;
/*  280 */         double d6 = this.posZ + (this.minecartZ - this.posZ) / this.turnProgress;
/*  281 */         double d1 = MathHelper.wrapAngleTo180_double(this.minecartYaw - this.rotationYaw);
/*  282 */         this.rotationYaw = (float)(this.rotationYaw + d1 / this.turnProgress);
/*  283 */         this.rotationPitch = (float)(this.rotationPitch + (this.minecartPitch - this.rotationPitch) / this.turnProgress);
/*  284 */         this.turnProgress--;
/*  285 */         setPosition(d4, d5, d6);
/*  286 */         setRotation(this.rotationYaw, this.rotationPitch);
/*      */       }
/*      */       else
/*      */       {
/*  290 */         setPosition(this.posX, this.posY, this.posZ);
/*  291 */         setRotation(this.rotationYaw, this.rotationPitch);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  296 */       this.prevPosX = this.posX;
/*  297 */       this.prevPosY = this.posY;
/*  298 */       this.prevPosZ = this.posZ;
/*  299 */       this.motionY -= 0.03999999910593033D;
/*  300 */       int k = MathHelper.floor_double(this.posX);
/*  301 */       int l = MathHelper.floor_double(this.posY);
/*  302 */       int i1 = MathHelper.floor_double(this.posZ);
/*      */       
/*  304 */       if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(k, l - 1, i1)))
/*      */       {
/*  306 */         l--;
/*      */       }
/*      */       
/*  309 */       BlockPos blockpos = new BlockPos(k, l, i1);
/*  310 */       IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/*      */       
/*  312 */       if (BlockRailBase.isRailBlock(iblockstate)) {
/*      */         
/*  314 */         func_180460_a(blockpos, iblockstate);
/*      */         
/*  316 */         if (iblockstate.getBlock() == Blocks.activator_rail)
/*      */         {
/*  318 */           onActivatorRailPass(k, l, i1, ((Boolean)iblockstate.getValue((IProperty)BlockRailPowered.POWERED)).booleanValue());
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  323 */         moveDerailedMinecart();
/*      */       } 
/*      */       
/*  326 */       doBlockCollisions();
/*  327 */       this.rotationPitch = 0.0F;
/*  328 */       double d0 = this.prevPosX - this.posX;
/*  329 */       double d2 = this.prevPosZ - this.posZ;
/*      */       
/*  331 */       if (d0 * d0 + d2 * d2 > 0.001D) {
/*      */         
/*  333 */         this.rotationYaw = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI);
/*      */         
/*  335 */         if (this.isInReverse)
/*      */         {
/*  337 */           this.rotationYaw += 180.0F;
/*      */         }
/*      */       } 
/*      */       
/*  341 */       double d3 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);
/*      */       
/*  343 */       if (d3 < -170.0D || d3 >= 170.0D) {
/*      */         
/*  345 */         this.rotationYaw += 180.0F;
/*  346 */         this.isInReverse = !this.isInReverse;
/*      */       } 
/*      */       
/*  349 */       setRotation(this.rotationYaw, this.rotationPitch);
/*      */       
/*  351 */       for (Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D))) {
/*      */         
/*  353 */         if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityMinecart)
/*      */         {
/*  355 */           entity.applyEntityCollision(this);
/*      */         }
/*      */       } 
/*      */       
/*  359 */       if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
/*      */         
/*  361 */         if (this.riddenByEntity.ridingEntity == this)
/*      */         {
/*  363 */           this.riddenByEntity.ridingEntity = null;
/*      */         }
/*      */         
/*  366 */         this.riddenByEntity = null;
/*      */       } 
/*      */       
/*  369 */       handleWaterMovement();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected double getMaximumSpeed() {
/*  375 */     return 0.4D;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {}
/*      */ 
/*      */   
/*      */   protected void moveDerailedMinecart() {
/*  384 */     double d0 = getMaximumSpeed();
/*  385 */     this.motionX = MathHelper.clamp_double(this.motionX, -d0, d0);
/*  386 */     this.motionZ = MathHelper.clamp_double(this.motionZ, -d0, d0);
/*      */     
/*  388 */     if (this.onGround) {
/*      */       
/*  390 */       this.motionX *= 0.5D;
/*  391 */       this.motionY *= 0.5D;
/*  392 */       this.motionZ *= 0.5D;
/*      */     } 
/*      */     
/*  395 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*      */     
/*  397 */     if (!this.onGround) {
/*      */       
/*  399 */       this.motionX *= 0.949999988079071D;
/*  400 */       this.motionY *= 0.949999988079071D;
/*  401 */       this.motionZ *= 0.949999988079071D;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void func_180460_a(BlockPos p_180460_1_, IBlockState p_180460_2_) {
/*  408 */     this.fallDistance = 0.0F;
/*  409 */     Vec3 vec3 = func_70489_a(this.posX, this.posY, this.posZ);
/*  410 */     this.posY = p_180460_1_.getY();
/*  411 */     boolean flag = false;
/*  412 */     boolean flag1 = false;
/*  413 */     BlockRailBase blockrailbase = (BlockRailBase)p_180460_2_.getBlock();
/*      */     
/*  415 */     if (blockrailbase == Blocks.golden_rail) {
/*      */       
/*  417 */       flag = ((Boolean)p_180460_2_.getValue((IProperty)BlockRailPowered.POWERED)).booleanValue();
/*  418 */       flag1 = !flag;
/*      */     } 
/*      */     
/*  421 */     double d0 = 0.0078125D;
/*  422 */     BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)p_180460_2_.getValue(blockrailbase.getShapeProperty());
/*      */     
/*  424 */     switch (blockrailbase$enumraildirection) {
/*      */       
/*      */       case ASCENDING_EAST:
/*  427 */         this.motionX -= 0.0078125D;
/*  428 */         this.posY++;
/*      */         break;
/*      */       
/*      */       case ASCENDING_WEST:
/*  432 */         this.motionX += 0.0078125D;
/*  433 */         this.posY++;
/*      */         break;
/*      */       
/*      */       case ASCENDING_NORTH:
/*  437 */         this.motionZ += 0.0078125D;
/*  438 */         this.posY++;
/*      */         break;
/*      */       
/*      */       case ASCENDING_SOUTH:
/*  442 */         this.motionZ -= 0.0078125D;
/*  443 */         this.posY++;
/*      */         break;
/*      */     } 
/*  446 */     int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
/*  447 */     double d1 = (aint[1][0] - aint[0][0]);
/*  448 */     double d2 = (aint[1][2] - aint[0][2]);
/*  449 */     double d3 = Math.sqrt(d1 * d1 + d2 * d2);
/*  450 */     double d4 = this.motionX * d1 + this.motionZ * d2;
/*      */     
/*  452 */     if (d4 < 0.0D) {
/*      */       
/*  454 */       d1 = -d1;
/*  455 */       d2 = -d2;
/*      */     } 
/*      */     
/*  458 */     double d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*      */     
/*  460 */     if (d5 > 2.0D)
/*      */     {
/*  462 */       d5 = 2.0D;
/*      */     }
/*      */     
/*  465 */     this.motionX = d5 * d1 / d3;
/*  466 */     this.motionZ = d5 * d2 / d3;
/*      */     
/*  468 */     if (this.riddenByEntity instanceof EntityLivingBase) {
/*      */       
/*  470 */       double d6 = ((EntityLivingBase)this.riddenByEntity).moveForward;
/*      */       
/*  472 */       if (d6 > 0.0D) {
/*      */         
/*  474 */         double d7 = -Math.sin((this.riddenByEntity.rotationYaw * 3.1415927F / 180.0F));
/*  475 */         double d8 = Math.cos((this.riddenByEntity.rotationYaw * 3.1415927F / 180.0F));
/*  476 */         double d9 = this.motionX * this.motionX + this.motionZ * this.motionZ;
/*      */         
/*  478 */         if (d9 < 0.01D) {
/*      */           
/*  480 */           this.motionX += d7 * 0.1D;
/*  481 */           this.motionZ += d8 * 0.1D;
/*  482 */           flag1 = false;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  487 */     if (flag1) {
/*      */       
/*  489 */       double d17 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*      */       
/*  491 */       if (d17 < 0.03D) {
/*      */         
/*  493 */         this.motionX *= 0.0D;
/*  494 */         this.motionY *= 0.0D;
/*  495 */         this.motionZ *= 0.0D;
/*      */       }
/*      */       else {
/*      */         
/*  499 */         this.motionX *= 0.5D;
/*  500 */         this.motionY *= 0.0D;
/*  501 */         this.motionZ *= 0.5D;
/*      */       } 
/*      */     } 
/*      */     
/*  505 */     double d18 = 0.0D;
/*  506 */     double d19 = p_180460_1_.getX() + 0.5D + aint[0][0] * 0.5D;
/*  507 */     double d20 = p_180460_1_.getZ() + 0.5D + aint[0][2] * 0.5D;
/*  508 */     double d21 = p_180460_1_.getX() + 0.5D + aint[1][0] * 0.5D;
/*  509 */     double d10 = p_180460_1_.getZ() + 0.5D + aint[1][2] * 0.5D;
/*  510 */     d1 = d21 - d19;
/*  511 */     d2 = d10 - d20;
/*      */     
/*  513 */     if (d1 == 0.0D) {
/*      */       
/*  515 */       this.posX = p_180460_1_.getX() + 0.5D;
/*  516 */       d18 = this.posZ - p_180460_1_.getZ();
/*      */     }
/*  518 */     else if (d2 == 0.0D) {
/*      */       
/*  520 */       this.posZ = p_180460_1_.getZ() + 0.5D;
/*  521 */       d18 = this.posX - p_180460_1_.getX();
/*      */     }
/*      */     else {
/*      */       
/*  525 */       double d11 = this.posX - d19;
/*  526 */       double d12 = this.posZ - d20;
/*  527 */       d18 = (d11 * d1 + d12 * d2) * 2.0D;
/*      */     } 
/*      */     
/*  530 */     this.posX = d19 + d1 * d18;
/*  531 */     this.posZ = d20 + d2 * d18;
/*  532 */     setPosition(this.posX, this.posY, this.posZ);
/*  533 */     double d22 = this.motionX;
/*  534 */     double d23 = this.motionZ;
/*      */     
/*  536 */     if (this.riddenByEntity != null) {
/*      */       
/*  538 */       d22 *= 0.75D;
/*  539 */       d23 *= 0.75D;
/*      */     } 
/*      */     
/*  542 */     double d13 = getMaximumSpeed();
/*  543 */     d22 = MathHelper.clamp_double(d22, -d13, d13);
/*  544 */     d23 = MathHelper.clamp_double(d23, -d13, d13);
/*  545 */     moveEntity(d22, 0.0D, d23);
/*      */     
/*  547 */     if (aint[0][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == aint[0][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == aint[0][2]) {
/*      */       
/*  549 */       setPosition(this.posX, this.posY + aint[0][1], this.posZ);
/*      */     }
/*  551 */     else if (aint[1][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == aint[1][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == aint[1][2]) {
/*      */       
/*  553 */       setPosition(this.posX, this.posY + aint[1][1], this.posZ);
/*      */     } 
/*      */     
/*  556 */     applyDrag();
/*  557 */     Vec3 vec31 = func_70489_a(this.posX, this.posY, this.posZ);
/*      */     
/*  559 */     if (vec31 != null && vec3 != null) {
/*      */       
/*  561 */       double d14 = (vec3.yCoord - vec31.yCoord) * 0.05D;
/*  562 */       d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*      */       
/*  564 */       if (d5 > 0.0D) {
/*      */         
/*  566 */         this.motionX = this.motionX / d5 * (d5 + d14);
/*  567 */         this.motionZ = this.motionZ / d5 * (d5 + d14);
/*      */       } 
/*      */       
/*  570 */       setPosition(this.posX, vec31.yCoord, this.posZ);
/*      */     } 
/*      */     
/*  573 */     int j = MathHelper.floor_double(this.posX);
/*  574 */     int i = MathHelper.floor_double(this.posZ);
/*      */     
/*  576 */     if (j != p_180460_1_.getX() || i != p_180460_1_.getZ()) {
/*      */       
/*  578 */       d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*  579 */       this.motionX = d5 * (j - p_180460_1_.getX());
/*  580 */       this.motionZ = d5 * (i - p_180460_1_.getZ());
/*      */     } 
/*      */     
/*  583 */     if (flag) {
/*      */       
/*  585 */       double d15 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*      */       
/*  587 */       if (d15 > 0.01D) {
/*      */         
/*  589 */         double d16 = 0.06D;
/*  590 */         this.motionX += this.motionX / d15 * d16;
/*  591 */         this.motionZ += this.motionZ / d15 * d16;
/*      */       }
/*  593 */       else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
/*      */         
/*  595 */         if (this.worldObj.getBlockState(p_180460_1_.west()).getBlock().isNormalCube())
/*      */         {
/*  597 */           this.motionX = 0.02D;
/*      */         }
/*  599 */         else if (this.worldObj.getBlockState(p_180460_1_.east()).getBlock().isNormalCube())
/*      */         {
/*  601 */           this.motionX = -0.02D;
/*      */         }
/*      */       
/*  604 */       } else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
/*      */         
/*  606 */         if (this.worldObj.getBlockState(p_180460_1_.north()).getBlock().isNormalCube()) {
/*      */           
/*  608 */           this.motionZ = 0.02D;
/*      */         }
/*  610 */         else if (this.worldObj.getBlockState(p_180460_1_.south()).getBlock().isNormalCube()) {
/*      */           
/*  612 */           this.motionZ = -0.02D;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyDrag() {
/*  620 */     if (this.riddenByEntity != null) {
/*      */       
/*  622 */       this.motionX *= 0.996999979019165D;
/*  623 */       this.motionY *= 0.0D;
/*  624 */       this.motionZ *= 0.996999979019165D;
/*      */     }
/*      */     else {
/*      */       
/*  628 */       this.motionX *= 0.9599999785423279D;
/*  629 */       this.motionY *= 0.0D;
/*  630 */       this.motionZ *= 0.9599999785423279D;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPosition(double x, double y, double z) {
/*  636 */     this.posX = x;
/*  637 */     this.posY = y;
/*  638 */     this.posZ = z;
/*  639 */     float f = this.width / 2.0F;
/*  640 */     float f1 = this.height;
/*  641 */     setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f1, z + f));
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, double p_70495_7_) {
/*  646 */     int i = MathHelper.floor_double(p_70495_1_);
/*  647 */     int j = MathHelper.floor_double(p_70495_3_);
/*  648 */     int k = MathHelper.floor_double(p_70495_5_);
/*      */     
/*  650 */     if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(i, j - 1, k)))
/*      */     {
/*  652 */       j--;
/*      */     }
/*      */     
/*  655 */     IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
/*      */     
/*  657 */     if (BlockRailBase.isRailBlock(iblockstate)) {
/*      */       
/*  659 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
/*  660 */       p_70495_3_ = j;
/*      */       
/*  662 */       if (blockrailbase$enumraildirection.isAscending())
/*      */       {
/*  664 */         p_70495_3_ = (j + 1);
/*      */       }
/*      */       
/*  667 */       int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
/*  668 */       double d0 = (aint[1][0] - aint[0][0]);
/*  669 */       double d1 = (aint[1][2] - aint[0][2]);
/*  670 */       double d2 = Math.sqrt(d0 * d0 + d1 * d1);
/*  671 */       d0 /= d2;
/*  672 */       d1 /= d2;
/*  673 */       p_70495_1_ += d0 * p_70495_7_;
/*  674 */       p_70495_5_ += d1 * p_70495_7_;
/*      */       
/*  676 */       if (aint[0][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[0][0] && MathHelper.floor_double(p_70495_5_) - k == aint[0][2]) {
/*      */         
/*  678 */         p_70495_3_ += aint[0][1];
/*      */       }
/*  680 */       else if (aint[1][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[1][0] && MathHelper.floor_double(p_70495_5_) - k == aint[1][2]) {
/*      */         
/*  682 */         p_70495_3_ += aint[1][1];
/*      */       } 
/*      */       
/*  685 */       return func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
/*      */     } 
/*      */ 
/*      */     
/*  689 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_) {
/*  695 */     int i = MathHelper.floor_double(p_70489_1_);
/*  696 */     int j = MathHelper.floor_double(p_70489_3_);
/*  697 */     int k = MathHelper.floor_double(p_70489_5_);
/*      */     
/*  699 */     if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(i, j - 1, k)))
/*      */     {
/*  701 */       j--;
/*      */     }
/*      */     
/*  704 */     IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
/*      */     
/*  706 */     if (BlockRailBase.isRailBlock(iblockstate)) {
/*      */       
/*  708 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
/*  709 */       int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
/*  710 */       double d0 = 0.0D;
/*  711 */       double d1 = i + 0.5D + aint[0][0] * 0.5D;
/*  712 */       double d2 = j + 0.0625D + aint[0][1] * 0.5D;
/*  713 */       double d3 = k + 0.5D + aint[0][2] * 0.5D;
/*  714 */       double d4 = i + 0.5D + aint[1][0] * 0.5D;
/*  715 */       double d5 = j + 0.0625D + aint[1][1] * 0.5D;
/*  716 */       double d6 = k + 0.5D + aint[1][2] * 0.5D;
/*  717 */       double d7 = d4 - d1;
/*  718 */       double d8 = (d5 - d2) * 2.0D;
/*  719 */       double d9 = d6 - d3;
/*      */       
/*  721 */       if (d7 == 0.0D) {
/*      */         
/*  723 */         p_70489_1_ = i + 0.5D;
/*  724 */         d0 = p_70489_5_ - k;
/*      */       }
/*  726 */       else if (d9 == 0.0D) {
/*      */         
/*  728 */         p_70489_5_ = k + 0.5D;
/*  729 */         d0 = p_70489_1_ - i;
/*      */       }
/*      */       else {
/*      */         
/*  733 */         double d10 = p_70489_1_ - d1;
/*  734 */         double d11 = p_70489_5_ - d3;
/*  735 */         d0 = (d10 * d7 + d11 * d9) * 2.0D;
/*      */       } 
/*      */       
/*  738 */       p_70489_1_ = d1 + d7 * d0;
/*  739 */       p_70489_3_ = d2 + d8 * d0;
/*  740 */       p_70489_5_ = d3 + d9 * d0;
/*      */       
/*  742 */       if (d8 < 0.0D)
/*      */       {
/*  744 */         p_70489_3_++;
/*      */       }
/*      */       
/*  747 */       if (d8 > 0.0D)
/*      */       {
/*  749 */         p_70489_3_ += 0.5D;
/*      */       }
/*      */       
/*  752 */       return new Vec3(p_70489_1_, p_70489_3_, p_70489_5_);
/*      */     } 
/*      */ 
/*      */     
/*  756 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  762 */     if (tagCompund.getBoolean("CustomDisplayTile")) {
/*      */       
/*  764 */       int i = tagCompund.getInteger("DisplayData");
/*      */       
/*  766 */       if (tagCompund.hasKey("DisplayTile", 8)) {
/*      */         
/*  768 */         Block block = Block.getBlockFromName(tagCompund.getString("DisplayTile"));
/*      */         
/*  770 */         if (block == null)
/*      */         {
/*  772 */           func_174899_a(Blocks.air.getDefaultState());
/*      */         }
/*      */         else
/*      */         {
/*  776 */           func_174899_a(block.getStateFromMeta(i));
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  781 */         Block block1 = Block.getBlockById(tagCompund.getInteger("DisplayTile"));
/*      */         
/*  783 */         if (block1 == null) {
/*      */           
/*  785 */           func_174899_a(Blocks.air.getDefaultState());
/*      */         }
/*      */         else {
/*      */           
/*  789 */           func_174899_a(block1.getStateFromMeta(i));
/*      */         } 
/*      */       } 
/*      */       
/*  793 */       setDisplayTileOffset(tagCompund.getInteger("DisplayOffset"));
/*      */     } 
/*      */     
/*  796 */     if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
/*      */     {
/*  798 */       this.entityName = tagCompund.getString("CustomName");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  804 */     if (hasDisplayTile()) {
/*      */       
/*  806 */       tagCompound.setBoolean("CustomDisplayTile", true);
/*  807 */       IBlockState iblockstate = getDisplayTile();
/*  808 */       ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(iblockstate.getBlock());
/*  809 */       tagCompound.setString("DisplayTile", (resourcelocation == null) ? "" : resourcelocation.toString());
/*  810 */       tagCompound.setInteger("DisplayData", iblockstate.getBlock().getMetaFromState(iblockstate));
/*  811 */       tagCompound.setInteger("DisplayOffset", getDisplayTileOffset());
/*      */     } 
/*      */     
/*  814 */     if (this.entityName != null && this.entityName.length() > 0)
/*      */     {
/*  816 */       tagCompound.setString("CustomName", this.entityName);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void applyEntityCollision(Entity entityIn) {
/*  822 */     if (!this.worldObj.isRemote)
/*      */     {
/*  824 */       if (!entityIn.noClip && !this.noClip)
/*      */       {
/*  826 */         if (entityIn != this.riddenByEntity) {
/*      */           
/*  828 */           if (entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityPlayer) && !(entityIn instanceof net.minecraft.entity.monster.EntityIronGolem) && getMinecartType() == EnumMinecartType.RIDEABLE && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && entityIn.ridingEntity == null)
/*      */           {
/*  830 */             entityIn.mountEntity(this);
/*      */           }
/*      */           
/*  833 */           double d0 = entityIn.posX - this.posX;
/*  834 */           double d1 = entityIn.posZ - this.posZ;
/*  835 */           double d2 = d0 * d0 + d1 * d1;
/*      */           
/*  837 */           if (d2 >= 9.999999747378752E-5D) {
/*      */             
/*  839 */             d2 = MathHelper.sqrt_double(d2);
/*  840 */             d0 /= d2;
/*  841 */             d1 /= d2;
/*  842 */             double d3 = 1.0D / d2;
/*      */             
/*  844 */             if (d3 > 1.0D)
/*      */             {
/*  846 */               d3 = 1.0D;
/*      */             }
/*      */             
/*  849 */             d0 *= d3;
/*  850 */             d1 *= d3;
/*  851 */             d0 *= 0.10000000149011612D;
/*  852 */             d1 *= 0.10000000149011612D;
/*  853 */             d0 *= (1.0F - this.entityCollisionReduction);
/*  854 */             d1 *= (1.0F - this.entityCollisionReduction);
/*  855 */             d0 *= 0.5D;
/*  856 */             d1 *= 0.5D;
/*      */             
/*  858 */             if (entityIn instanceof EntityMinecart) {
/*      */               
/*  860 */               double d4 = entityIn.posX - this.posX;
/*  861 */               double d5 = entityIn.posZ - this.posZ;
/*  862 */               Vec3 vec3 = (new Vec3(d4, 0.0D, d5)).normalize();
/*  863 */               Vec3 vec31 = (new Vec3(MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F), 0.0D, MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F))).normalize();
/*  864 */               double d6 = Math.abs(vec3.dotProduct(vec31));
/*      */               
/*  866 */               if (d6 < 0.800000011920929D) {
/*      */                 return;
/*      */               }
/*      */ 
/*      */               
/*  871 */               double d7 = entityIn.motionX + this.motionX;
/*  872 */               double d8 = entityIn.motionZ + this.motionZ;
/*      */               
/*  874 */               if (((EntityMinecart)entityIn).getMinecartType() == EnumMinecartType.FURNACE && getMinecartType() != EnumMinecartType.FURNACE)
/*      */               {
/*  876 */                 this.motionX *= 0.20000000298023224D;
/*  877 */                 this.motionZ *= 0.20000000298023224D;
/*  878 */                 addVelocity(entityIn.motionX - d0, 0.0D, entityIn.motionZ - d1);
/*  879 */                 entityIn.motionX *= 0.949999988079071D;
/*  880 */                 entityIn.motionZ *= 0.949999988079071D;
/*      */               }
/*  882 */               else if (((EntityMinecart)entityIn).getMinecartType() != EnumMinecartType.FURNACE && getMinecartType() == EnumMinecartType.FURNACE)
/*      */               {
/*  884 */                 entityIn.motionX *= 0.20000000298023224D;
/*  885 */                 entityIn.motionZ *= 0.20000000298023224D;
/*  886 */                 entityIn.addVelocity(this.motionX + d0, 0.0D, this.motionZ + d1);
/*  887 */                 this.motionX *= 0.949999988079071D;
/*  888 */                 this.motionZ *= 0.949999988079071D;
/*      */               }
/*      */               else
/*      */               {
/*  892 */                 d7 /= 2.0D;
/*  893 */                 d8 /= 2.0D;
/*  894 */                 this.motionX *= 0.20000000298023224D;
/*  895 */                 this.motionZ *= 0.20000000298023224D;
/*  896 */                 addVelocity(d7 - d0, 0.0D, d8 - d1);
/*  897 */                 entityIn.motionX *= 0.20000000298023224D;
/*  898 */                 entityIn.motionZ *= 0.20000000298023224D;
/*  899 */                 entityIn.addVelocity(d7 + d0, 0.0D, d8 + d1);
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/*  904 */               addVelocity(-d0, 0.0D, -d1);
/*  905 */               entityIn.addVelocity(d0 / 4.0D, 0.0D, d1 / 4.0D);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
/*  915 */     this.minecartX = x;
/*  916 */     this.minecartY = y;
/*  917 */     this.minecartZ = z;
/*  918 */     this.minecartYaw = yaw;
/*  919 */     this.minecartPitch = pitch;
/*  920 */     this.turnProgress = posRotationIncrements + 2;
/*  921 */     this.motionX = this.velocityX;
/*  922 */     this.motionY = this.velocityY;
/*  923 */     this.motionZ = this.velocityZ;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setVelocity(double x, double y, double z) {
/*  928 */     this.velocityX = this.motionX = x;
/*  929 */     this.velocityY = this.motionY = y;
/*  930 */     this.velocityZ = this.motionZ = z;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDamage(float p_70492_1_) {
/*  935 */     this.dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
/*      */   }
/*      */ 
/*      */   
/*      */   public float getDamage() {
/*  940 */     return this.dataWatcher.getWatchableObjectFloat(19);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRollingAmplitude(int p_70497_1_) {
/*  945 */     this.dataWatcher.updateObject(17, Integer.valueOf(p_70497_1_));
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRollingAmplitude() {
/*  950 */     return this.dataWatcher.getWatchableObjectInt(17);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRollingDirection(int p_70494_1_) {
/*  955 */     this.dataWatcher.updateObject(18, Integer.valueOf(p_70494_1_));
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRollingDirection() {
/*  960 */     return this.dataWatcher.getWatchableObjectInt(18);
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract EnumMinecartType getMinecartType();
/*      */   
/*      */   public IBlockState getDisplayTile() {
/*  967 */     return !hasDisplayTile() ? getDefaultDisplayTile() : Block.getStateById(getDataWatcher().getWatchableObjectInt(20));
/*      */   }
/*      */ 
/*      */   
/*      */   public IBlockState getDefaultDisplayTile() {
/*  972 */     return Blocks.air.getDefaultState();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDisplayTileOffset() {
/*  977 */     return !hasDisplayTile() ? getDefaultDisplayTileOffset() : getDataWatcher().getWatchableObjectInt(21);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDefaultDisplayTileOffset() {
/*  982 */     return 6;
/*      */   }
/*      */ 
/*      */   
/*      */   public void func_174899_a(IBlockState p_174899_1_) {
/*  987 */     getDataWatcher().updateObject(20, Integer.valueOf(Block.getStateId(p_174899_1_)));
/*  988 */     setHasDisplayTile(true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDisplayTileOffset(int p_94086_1_) {
/*  993 */     getDataWatcher().updateObject(21, Integer.valueOf(p_94086_1_));
/*  994 */     setHasDisplayTile(true);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasDisplayTile() {
/*  999 */     return (getDataWatcher().getWatchableObjectByte(22) == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHasDisplayTile(boolean p_94096_1_) {
/* 1004 */     getDataWatcher().updateObject(22, Byte.valueOf((byte)(p_94096_1_ ? 1 : 0)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCustomNameTag(String name) {
/* 1009 */     this.entityName = name;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1014 */     return (this.entityName != null) ? this.entityName : super.getName();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasCustomName() {
/* 1019 */     return (this.entityName != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCustomNameTag() {
/* 1024 */     return this.entityName;
/*      */   }
/*      */ 
/*      */   
/*      */   public IChatComponent getDisplayName() {
/* 1029 */     if (hasCustomName()) {
/*      */       
/* 1031 */       ChatComponentText chatcomponenttext = new ChatComponentText(this.entityName);
/* 1032 */       chatcomponenttext.getChatStyle().setChatHoverEvent(getHoverEvent());
/* 1033 */       chatcomponenttext.getChatStyle().setInsertion(getUniqueID().toString());
/* 1034 */       return (IChatComponent)chatcomponenttext;
/*      */     } 
/*      */ 
/*      */     
/* 1038 */     ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(getName(), new Object[0]);
/* 1039 */     chatcomponenttranslation.getChatStyle().setChatHoverEvent(getHoverEvent());
/* 1040 */     chatcomponenttranslation.getChatStyle().setInsertion(getUniqueID().toString());
/* 1041 */     return (IChatComponent)chatcomponenttranslation;
/*      */   }
/*      */ 
/*      */   
/*      */   public enum EnumMinecartType
/*      */   {
/* 1047 */     RIDEABLE(0, "MinecartRideable"),
/* 1048 */     CHEST(1, "MinecartChest"),
/* 1049 */     FURNACE(2, "MinecartFurnace"),
/* 1050 */     TNT(3, "MinecartTNT"),
/* 1051 */     SPAWNER(4, "MinecartSpawner"),
/* 1052 */     HOPPER(5, "MinecartHopper"),
/* 1053 */     COMMAND_BLOCK(6, "MinecartCommandBlock");
/*      */     
/* 1055 */     private static final Map<Integer, EnumMinecartType> ID_LOOKUP = Maps.newHashMap();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int networkID;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final String name;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 1082 */       for (EnumMinecartType entityminecart$enumminecarttype : values())
/*      */       {
/* 1084 */         ID_LOOKUP.put(Integer.valueOf(entityminecart$enumminecarttype.getNetworkID()), entityminecart$enumminecarttype);
/*      */       }
/*      */     }
/*      */     
/*      */     EnumMinecartType(int networkID, String name) {
/*      */       this.networkID = networkID;
/*      */       this.name = name;
/*      */     }
/*      */     
/*      */     public int getNetworkID() {
/*      */       return this.networkID;
/*      */     }
/*      */     
/*      */     public String getName() {
/*      */       return this.name;
/*      */     }
/*      */     
/*      */     public static EnumMinecartType byNetworkID(int id) {
/*      */       EnumMinecartType entityminecart$enumminecarttype = ID_LOOKUP.get(Integer.valueOf(id));
/*      */       return (entityminecart$enumminecarttype == null) ? RIDEABLE : entityminecart$enumminecarttype;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */