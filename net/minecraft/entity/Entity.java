/*      */ package net.minecraft.entity;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockLiquid;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.block.state.pattern.BlockPattern;
/*      */ import net.minecraft.command.CommandResultStats;
/*      */ import net.minecraft.command.ICommandSender;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.enchantment.EnchantmentProtection;
/*      */ import net.minecraft.entity.effect.EntityLightningBolt;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.event.HoverEvent;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagDouble;
/*      */ import net.minecraft.nbt.NBTTagFloat;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.StatCollector;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.Explosion;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ 
/*      */ 
/*      */ public abstract class Entity
/*      */   implements ICommandSender
/*      */ {
/*   52 */   private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
/*      */   
/*      */   private static int nextEntityID;
/*      */   private int entityId;
/*      */   public double renderDistanceWeight;
/*      */   public boolean preventEntitySpawning;
/*      */   public Entity riddenByEntity;
/*      */   public Entity ridingEntity;
/*      */   public boolean forceSpawn;
/*      */   public World worldObj;
/*      */   public double prevPosX;
/*      */   public double prevPosY;
/*      */   public double prevPosZ;
/*      */   public double posX;
/*      */   public double posY;
/*      */   public double posZ;
/*      */   public double motionX;
/*      */   public double motionY;
/*      */   public double motionZ;
/*      */   public float rotationYaw;
/*      */   public float rotationPitch;
/*      */   public float prevRotationYaw;
/*      */   public float prevRotationPitch;
/*      */   private AxisAlignedBB boundingBox;
/*      */   public boolean onGround;
/*      */   public boolean isCollidedHorizontally;
/*      */   public boolean isCollidedVertically;
/*      */   public boolean isCollided;
/*      */   public boolean velocityChanged;
/*      */   protected boolean isInWeb;
/*      */   private boolean isOutsideBorder;
/*      */   public boolean isDead;
/*      */   public float width;
/*      */   public float height;
/*      */   public float prevDistanceWalkedModified;
/*      */   public float distanceWalkedModified;
/*      */   public float distanceWalkedOnStepModified;
/*      */   public float fallDistance;
/*      */   private int nextStepDistance;
/*      */   public double lastTickPosX;
/*      */   public double lastTickPosY;
/*      */   public double lastTickPosZ;
/*      */   public float stepHeight;
/*      */   public boolean noClip;
/*      */   public float entityCollisionReduction;
/*      */   protected Random rand;
/*      */   public int ticksExisted;
/*      */   public int fireResistance;
/*      */   private int fire;
/*      */   protected boolean inWater;
/*      */   public int hurtResistantTime;
/*      */   protected boolean firstUpdate;
/*      */   protected boolean isImmuneToFire;
/*      */   protected DataWatcher dataWatcher;
/*      */   private double entityRiderPitchDelta;
/*      */   private double entityRiderYawDelta;
/*      */   public boolean addedToChunk;
/*      */   public int chunkCoordX;
/*      */   public int chunkCoordY;
/*      */   public int chunkCoordZ;
/*      */   public int serverPosX;
/*      */   public int serverPosY;
/*      */   public int serverPosZ;
/*      */   public boolean ignoreFrustumCheck;
/*      */   public boolean isAirBorne;
/*      */   public int timeUntilPortal;
/*      */   protected boolean inPortal;
/*      */   protected int portalCounter;
/*      */   public int dimension;
/*      */   protected BlockPos lastPortalPos;
/*      */   protected Vec3 lastPortalVec;
/*      */   protected EnumFacing teleportDirection;
/*      */   private boolean invulnerable;
/*      */   protected UUID entityUniqueID;
/*      */   private final CommandResultStats cmdResultStats;
/*      */   
/*      */   public int getEntityId() {
/*  129 */     return this.entityId;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEntityId(int id) {
/*  134 */     this.entityId = id;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onKillCommand() {
/*  139 */     setDead();
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity(World worldIn) {
/*  144 */     this.entityId = nextEntityID++;
/*  145 */     this.renderDistanceWeight = 1.0D;
/*  146 */     this.boundingBox = ZERO_AABB;
/*  147 */     this.width = 0.6F;
/*  148 */     this.height = 1.8F;
/*  149 */     this.nextStepDistance = 1;
/*  150 */     this.rand = new Random();
/*  151 */     this.fireResistance = 1;
/*  152 */     this.firstUpdate = true;
/*  153 */     this.entityUniqueID = MathHelper.getRandomUuid(this.rand);
/*  154 */     this.cmdResultStats = new CommandResultStats();
/*  155 */     this.worldObj = worldIn;
/*  156 */     setPosition(0.0D, 0.0D, 0.0D);
/*      */     
/*  158 */     if (worldIn != null)
/*      */     {
/*  160 */       this.dimension = worldIn.provider.getDimensionId();
/*      */     }
/*      */     
/*  163 */     this.dataWatcher = new DataWatcher(this);
/*  164 */     this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
/*  165 */     this.dataWatcher.addObject(1, Short.valueOf((short)300));
/*  166 */     this.dataWatcher.addObject(3, Byte.valueOf((byte)0));
/*  167 */     this.dataWatcher.addObject(2, "");
/*  168 */     this.dataWatcher.addObject(4, Byte.valueOf((byte)0));
/*  169 */     entityInit();
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract void entityInit();
/*      */   
/*      */   public DataWatcher getDataWatcher() {
/*  176 */     return this.dataWatcher;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object p_equals_1_) {
/*  181 */     return (p_equals_1_ instanceof Entity) ? ((((Entity)p_equals_1_).entityId == this.entityId)) : false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  186 */     return this.entityId;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void preparePlayerToSpawn() {
/*  191 */     if (this.worldObj != null) {
/*      */       
/*  193 */       while (this.posY > 0.0D && this.posY < 256.0D) {
/*      */         
/*  195 */         setPosition(this.posX, this.posY, this.posZ);
/*      */         
/*  197 */         if (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/*  202 */         this.posY++;
/*      */       } 
/*      */       
/*  205 */       this.motionX = this.motionY = this.motionZ = 0.0D;
/*  206 */       this.rotationPitch = 0.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDead() {
/*  212 */     this.isDead = true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setSize(float width, float height) {
/*  217 */     if (width != this.width || height != this.height) {
/*      */       
/*  219 */       float f = this.width;
/*  220 */       this.width = width;
/*  221 */       this.height = height;
/*  222 */       setEntityBoundingBox(new AxisAlignedBB((getEntityBoundingBox()).minX, (getEntityBoundingBox()).minY, (getEntityBoundingBox()).minZ, (getEntityBoundingBox()).minX + this.width, (getEntityBoundingBox()).minY + this.height, (getEntityBoundingBox()).minZ + this.width));
/*      */       
/*  224 */       if (this.width > f && !this.firstUpdate && !this.worldObj.isRemote)
/*      */       {
/*  226 */         moveEntity((f - this.width), 0.0D, (f - this.width));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setRotation(float yaw, float pitch) {
/*  233 */     this.rotationYaw = yaw % 360.0F;
/*  234 */     this.rotationPitch = pitch % 360.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPosition(double x, double y, double z) {
/*  239 */     this.posX = x;
/*  240 */     this.posY = y;
/*  241 */     this.posZ = z;
/*  242 */     float f = this.width / 2.0F;
/*  243 */     float f1 = this.height;
/*  244 */     setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f1, z + f));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAngles(float yaw, float pitch) {
/*  249 */     float f = this.rotationPitch;
/*  250 */     float f1 = this.rotationYaw;
/*  251 */     this.rotationYaw = (float)(this.rotationYaw + yaw * 0.15D);
/*  252 */     this.rotationPitch = (float)(this.rotationPitch - pitch * 0.15D);
/*  253 */     this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0F, 90.0F);
/*  254 */     this.prevRotationPitch += this.rotationPitch - f;
/*  255 */     this.prevRotationYaw += this.rotationYaw - f1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdate() {
/*  260 */     onEntityUpdate();
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEntityUpdate() {
/*  265 */     this.worldObj.theProfiler.startSection("entityBaseTick");
/*      */     
/*  267 */     if (this.ridingEntity != null && this.ridingEntity.isDead)
/*      */     {
/*  269 */       this.ridingEntity = null;
/*      */     }
/*      */     
/*  272 */     this.prevDistanceWalkedModified = this.distanceWalkedModified;
/*  273 */     this.prevPosX = this.posX;
/*  274 */     this.prevPosY = this.posY;
/*  275 */     this.prevPosZ = this.posZ;
/*  276 */     this.prevRotationPitch = this.rotationPitch;
/*  277 */     this.prevRotationYaw = this.rotationYaw;
/*      */     
/*  279 */     if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
/*      */       
/*  281 */       this.worldObj.theProfiler.startSection("portal");
/*  282 */       MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
/*  283 */       int i = getMaxInPortalTime();
/*      */       
/*  285 */       if (this.inPortal) {
/*      */         
/*  287 */         if (minecraftserver.getAllowNether())
/*      */         {
/*  289 */           if (this.ridingEntity == null && this.portalCounter++ >= i) {
/*      */             int j;
/*  291 */             this.portalCounter = i;
/*  292 */             this.timeUntilPortal = getPortalCooldown();
/*      */ 
/*      */             
/*  295 */             if (this.worldObj.provider.getDimensionId() == -1) {
/*      */               
/*  297 */               j = 0;
/*      */             }
/*      */             else {
/*      */               
/*  301 */               j = -1;
/*      */             } 
/*      */             
/*  304 */             travelToDimension(j);
/*      */           } 
/*      */           
/*  307 */           this.inPortal = false;
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  312 */         if (this.portalCounter > 0)
/*      */         {
/*  314 */           this.portalCounter -= 4;
/*      */         }
/*      */         
/*  317 */         if (this.portalCounter < 0)
/*      */         {
/*  319 */           this.portalCounter = 0;
/*      */         }
/*      */       } 
/*      */       
/*  323 */       if (this.timeUntilPortal > 0)
/*      */       {
/*  325 */         this.timeUntilPortal--;
/*      */       }
/*      */       
/*  328 */       this.worldObj.theProfiler.endSection();
/*      */     } 
/*      */     
/*  331 */     spawnRunningParticles();
/*  332 */     handleWaterMovement();
/*      */     
/*  334 */     if (this.worldObj.isRemote) {
/*      */       
/*  336 */       this.fire = 0;
/*      */     }
/*  338 */     else if (this.fire > 0) {
/*      */       
/*  340 */       if (this.isImmuneToFire) {
/*      */         
/*  342 */         this.fire -= 4;
/*      */         
/*  344 */         if (this.fire < 0)
/*      */         {
/*  346 */           this.fire = 0;
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  351 */         if (this.fire % 20 == 0)
/*      */         {
/*  353 */           attackEntityFrom(DamageSource.onFire, 1.0F);
/*      */         }
/*      */         
/*  356 */         this.fire--;
/*      */       } 
/*      */     } 
/*      */     
/*  360 */     if (isInLava()) {
/*      */       
/*  362 */       setOnFireFromLava();
/*  363 */       this.fallDistance *= 0.5F;
/*      */     } 
/*      */     
/*  366 */     if (this.posY < -64.0D)
/*      */     {
/*  368 */       kill();
/*      */     }
/*      */     
/*  371 */     if (!this.worldObj.isRemote)
/*      */     {
/*  373 */       setFlag(0, (this.fire > 0));
/*      */     }
/*      */     
/*  376 */     this.firstUpdate = false;
/*  377 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxInPortalTime() {
/*  382 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setOnFireFromLava() {
/*  387 */     if (!this.isImmuneToFire) {
/*      */       
/*  389 */       attackEntityFrom(DamageSource.lava, 4.0F);
/*  390 */       setFire(15);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFire(int seconds) {
/*  396 */     int i = seconds * 20;
/*  397 */     i = EnchantmentProtection.getFireTimeForEntity(this, i);
/*      */     
/*  399 */     if (this.fire < i)
/*      */     {
/*  401 */       this.fire = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void extinguish() {
/*  407 */     this.fire = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void kill() {
/*  412 */     setDead();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOffsetPositionInLiquid(double x, double y, double z) {
/*  417 */     AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(x, y, z);
/*  418 */     return isLiquidPresentInAABB(axisalignedbb);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isLiquidPresentInAABB(AxisAlignedBB bb) {
/*  423 */     return (this.worldObj.getCollidingBoundingBoxes(this, bb).isEmpty() && !this.worldObj.isAnyLiquid(bb));
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveEntity(double x, double y, double z) {
/*  428 */     if (this.noClip) {
/*      */       
/*  430 */       setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
/*  431 */       resetPositionToBB();
/*      */     }
/*      */     else {
/*      */       
/*  435 */       this.worldObj.theProfiler.startSection("move");
/*  436 */       double d0 = this.posX;
/*  437 */       double d1 = this.posY;
/*  438 */       double d2 = this.posZ;
/*      */       
/*  440 */       if (this.isInWeb) {
/*      */         
/*  442 */         this.isInWeb = false;
/*  443 */         x *= 0.25D;
/*  444 */         y *= 0.05000000074505806D;
/*  445 */         z *= 0.25D;
/*  446 */         this.motionX = 0.0D;
/*  447 */         this.motionY = 0.0D;
/*  448 */         this.motionZ = 0.0D;
/*      */       } 
/*      */       
/*  451 */       double d3 = x;
/*  452 */       double d4 = y;
/*  453 */       double d5 = z;
/*  454 */       boolean flag = (this.onGround && isSneaking() && this instanceof EntityPlayer);
/*      */       
/*  456 */       if (flag) {
/*      */         double d6;
/*      */ 
/*      */         
/*  460 */         for (d6 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(x, -1.0D, 0.0D)).isEmpty(); d3 = x) {
/*      */           
/*  462 */           if (x < d6 && x >= -d6) {
/*      */             
/*  464 */             x = 0.0D;
/*      */           }
/*  466 */           else if (x > 0.0D) {
/*      */             
/*  468 */             x -= d6;
/*      */           }
/*      */           else {
/*      */             
/*  472 */             x += d6;
/*      */           } 
/*      */         } 
/*      */         
/*  476 */         for (; z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(0.0D, -1.0D, z)).isEmpty(); d5 = z) {
/*      */           
/*  478 */           if (z < d6 && z >= -d6) {
/*      */             
/*  480 */             z = 0.0D;
/*      */           }
/*  482 */           else if (z > 0.0D) {
/*      */             
/*  484 */             z -= d6;
/*      */           }
/*      */           else {
/*      */             
/*  488 */             z += d6;
/*      */           } 
/*      */         } 
/*      */         
/*  492 */         for (; x != 0.0D && z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(x, -1.0D, z)).isEmpty(); d5 = z) {
/*      */           
/*  494 */           if (x < d6 && x >= -d6) {
/*      */             
/*  496 */             x = 0.0D;
/*      */           }
/*  498 */           else if (x > 0.0D) {
/*      */             
/*  500 */             x -= d6;
/*      */           }
/*      */           else {
/*      */             
/*  504 */             x += d6;
/*      */           } 
/*      */           
/*  507 */           d3 = x;
/*      */           
/*  509 */           if (z < d6 && z >= -d6) {
/*      */             
/*  511 */             z = 0.0D;
/*      */           }
/*  513 */           else if (z > 0.0D) {
/*      */             
/*  515 */             z -= d6;
/*      */           }
/*      */           else {
/*      */             
/*  519 */             z += d6;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  524 */       List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().addCoord(x, y, z));
/*  525 */       AxisAlignedBB axisalignedbb = getEntityBoundingBox();
/*      */       
/*  527 */       for (AxisAlignedBB axisalignedbb1 : list1)
/*      */       {
/*  529 */         y = axisalignedbb1.calculateYOffset(getEntityBoundingBox(), y);
/*      */       }
/*      */       
/*  532 */       setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));
/*  533 */       boolean flag1 = (this.onGround || (d4 != y && d4 < 0.0D));
/*      */       
/*  535 */       for (AxisAlignedBB axisalignedbb2 : list1)
/*      */       {
/*  537 */         x = axisalignedbb2.calculateXOffset(getEntityBoundingBox(), x);
/*      */       }
/*      */       
/*  540 */       setEntityBoundingBox(getEntityBoundingBox().offset(x, 0.0D, 0.0D));
/*      */       
/*  542 */       for (AxisAlignedBB axisalignedbb13 : list1)
/*      */       {
/*  544 */         z = axisalignedbb13.calculateZOffset(getEntityBoundingBox(), z);
/*      */       }
/*      */       
/*  547 */       setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, 0.0D, z));
/*      */       
/*  549 */       if (this.stepHeight > 0.0F && flag1 && (d3 != x || d5 != z)) {
/*      */         
/*  551 */         double d11 = x;
/*  552 */         double d7 = y;
/*  553 */         double d8 = z;
/*  554 */         AxisAlignedBB axisalignedbb3 = getEntityBoundingBox();
/*  555 */         setEntityBoundingBox(axisalignedbb);
/*  556 */         y = this.stepHeight;
/*  557 */         List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().addCoord(d3, y, d5));
/*  558 */         AxisAlignedBB axisalignedbb4 = getEntityBoundingBox();
/*  559 */         AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0D, d5);
/*  560 */         double d9 = y;
/*      */         
/*  562 */         for (AxisAlignedBB axisalignedbb6 : list)
/*      */         {
/*  564 */           d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
/*      */         }
/*      */         
/*  567 */         axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);
/*  568 */         double d15 = d3;
/*      */         
/*  570 */         for (AxisAlignedBB axisalignedbb7 : list)
/*      */         {
/*  572 */           d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
/*      */         }
/*      */         
/*  575 */         axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);
/*  576 */         double d16 = d5;
/*      */         
/*  578 */         for (AxisAlignedBB axisalignedbb8 : list)
/*      */         {
/*  580 */           d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
/*      */         }
/*      */         
/*  583 */         axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);
/*  584 */         AxisAlignedBB axisalignedbb14 = getEntityBoundingBox();
/*  585 */         double d17 = y;
/*      */         
/*  587 */         for (AxisAlignedBB axisalignedbb9 : list)
/*      */         {
/*  589 */           d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
/*      */         }
/*      */         
/*  592 */         axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
/*  593 */         double d18 = d3;
/*      */         
/*  595 */         for (AxisAlignedBB axisalignedbb10 : list)
/*      */         {
/*  597 */           d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
/*      */         }
/*      */         
/*  600 */         axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
/*  601 */         double d19 = d5;
/*      */         
/*  603 */         for (AxisAlignedBB axisalignedbb11 : list)
/*      */         {
/*  605 */           d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
/*      */         }
/*      */         
/*  608 */         axisalignedbb14 = axisalignedbb14.offset(0.0D, 0.0D, d19);
/*  609 */         double d20 = d15 * d15 + d16 * d16;
/*  610 */         double d10 = d18 * d18 + d19 * d19;
/*      */         
/*  612 */         if (d20 > d10) {
/*      */           
/*  614 */           x = d15;
/*  615 */           z = d16;
/*  616 */           y = -d9;
/*  617 */           setEntityBoundingBox(axisalignedbb4);
/*      */         }
/*      */         else {
/*      */           
/*  621 */           x = d18;
/*  622 */           z = d19;
/*  623 */           y = -d17;
/*  624 */           setEntityBoundingBox(axisalignedbb14);
/*      */         } 
/*      */         
/*  627 */         for (AxisAlignedBB axisalignedbb12 : list)
/*      */         {
/*  629 */           y = axisalignedbb12.calculateYOffset(getEntityBoundingBox(), y);
/*      */         }
/*      */         
/*  632 */         setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));
/*      */         
/*  634 */         if (d11 * d11 + d8 * d8 >= x * x + z * z) {
/*      */           
/*  636 */           x = d11;
/*  637 */           y = d7;
/*  638 */           z = d8;
/*  639 */           setEntityBoundingBox(axisalignedbb3);
/*      */         } 
/*      */       } 
/*      */       
/*  643 */       this.worldObj.theProfiler.endSection();
/*  644 */       this.worldObj.theProfiler.startSection("rest");
/*  645 */       resetPositionToBB();
/*  646 */       this.isCollidedHorizontally = (d3 != x || d5 != z);
/*  647 */       this.isCollidedVertically = (d4 != y);
/*  648 */       this.onGround = (this.isCollidedVertically && d4 < 0.0D);
/*  649 */       this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
/*  650 */       int i = MathHelper.floor_double(this.posX);
/*  651 */       int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/*  652 */       int k = MathHelper.floor_double(this.posZ);
/*  653 */       BlockPos blockpos = new BlockPos(i, j, k);
/*  654 */       Block block1 = this.worldObj.getBlockState(blockpos).getBlock();
/*      */       
/*  656 */       if (block1.getMaterial() == Material.air) {
/*      */         
/*  658 */         Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();
/*      */         
/*  660 */         if (block instanceof net.minecraft.block.BlockFence || block instanceof net.minecraft.block.BlockWall || block instanceof net.minecraft.block.BlockFenceGate) {
/*      */           
/*  662 */           block1 = block;
/*  663 */           blockpos = blockpos.down();
/*      */         } 
/*      */       } 
/*      */       
/*  667 */       updateFallState(y, this.onGround, block1, blockpos);
/*      */       
/*  669 */       if (d3 != x)
/*      */       {
/*  671 */         this.motionX = 0.0D;
/*      */       }
/*      */       
/*  674 */       if (d5 != z)
/*      */       {
/*  676 */         this.motionZ = 0.0D;
/*      */       }
/*      */       
/*  679 */       if (d4 != y)
/*      */       {
/*  681 */         block1.onLanded(this.worldObj, this);
/*      */       }
/*      */       
/*  684 */       if (canTriggerWalking() && !flag && this.ridingEntity == null) {
/*      */         
/*  686 */         double d12 = this.posX - d0;
/*  687 */         double d13 = this.posY - d1;
/*  688 */         double d14 = this.posZ - d2;
/*      */         
/*  690 */         if (block1 != Blocks.ladder)
/*      */         {
/*  692 */           d13 = 0.0D;
/*      */         }
/*      */         
/*  695 */         if (block1 != null && this.onGround)
/*      */         {
/*  697 */           block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this);
/*      */         }
/*      */         
/*  700 */         this.distanceWalkedModified = (float)(this.distanceWalkedModified + MathHelper.sqrt_double(d12 * d12 + d14 * d14) * 0.6D);
/*  701 */         this.distanceWalkedOnStepModified = (float)(this.distanceWalkedOnStepModified + MathHelper.sqrt_double(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D);
/*      */         
/*  703 */         if (this.distanceWalkedOnStepModified > this.nextStepDistance && block1.getMaterial() != Material.air) {
/*      */           
/*  705 */           this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
/*      */           
/*  707 */           if (isInWater()) {
/*      */             
/*  709 */             float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;
/*      */             
/*  711 */             if (f > 1.0F)
/*      */             {
/*  713 */               f = 1.0F;
/*      */             }
/*      */             
/*  716 */             playSound(getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
/*      */           } 
/*      */           
/*  719 */           playStepSound(blockpos, block1);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/*  725 */         doBlockCollisions();
/*      */       }
/*  727 */       catch (Throwable throwable) {
/*      */         
/*  729 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
/*  730 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
/*  731 */         addEntityCrashInfo(crashreportcategory);
/*  732 */         throw new ReportedException(crashreport);
/*      */       } 
/*      */       
/*  735 */       boolean flag2 = isWet();
/*      */       
/*  737 */       if (this.worldObj.isFlammableWithin(getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D))) {
/*      */         
/*  739 */         dealFireDamage(1);
/*      */         
/*  741 */         if (!flag2)
/*      */         {
/*  743 */           this.fire++;
/*      */           
/*  745 */           if (this.fire == 0)
/*      */           {
/*  747 */             setFire(8);
/*      */           }
/*      */         }
/*      */       
/*  751 */       } else if (this.fire <= 0) {
/*      */         
/*  753 */         this.fire = -this.fireResistance;
/*      */       } 
/*      */       
/*  756 */       if (flag2 && this.fire > 0) {
/*      */         
/*  758 */         playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
/*  759 */         this.fire = -this.fireResistance;
/*      */       } 
/*      */       
/*  762 */       this.worldObj.theProfiler.endSection();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void resetPositionToBB() {
/*  768 */     this.posX = ((getEntityBoundingBox()).minX + (getEntityBoundingBox()).maxX) / 2.0D;
/*  769 */     this.posY = (getEntityBoundingBox()).minY;
/*  770 */     this.posZ = ((getEntityBoundingBox()).minZ + (getEntityBoundingBox()).maxZ) / 2.0D;
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getSwimSound() {
/*  775 */     return "game.neutral.swim";
/*      */   }
/*      */ 
/*      */   
/*      */   protected void doBlockCollisions() {
/*  780 */     BlockPos blockpos = new BlockPos((getEntityBoundingBox()).minX + 0.001D, (getEntityBoundingBox()).minY + 0.001D, (getEntityBoundingBox()).minZ + 0.001D);
/*  781 */     BlockPos blockpos1 = new BlockPos((getEntityBoundingBox()).maxX - 0.001D, (getEntityBoundingBox()).maxY - 0.001D, (getEntityBoundingBox()).maxZ - 0.001D);
/*      */     
/*  783 */     if (this.worldObj.isAreaLoaded(blockpos, blockpos1))
/*      */     {
/*  785 */       for (int i = blockpos.getX(); i <= blockpos1.getX(); i++) {
/*      */         
/*  787 */         for (int j = blockpos.getY(); j <= blockpos1.getY(); j++) {
/*      */           
/*  789 */           for (int k = blockpos.getZ(); k <= blockpos1.getZ(); k++) {
/*      */             
/*  791 */             BlockPos blockpos2 = new BlockPos(i, j, k);
/*  792 */             IBlockState iblockstate = this.worldObj.getBlockState(blockpos2);
/*      */ 
/*      */             
/*      */             try {
/*  796 */               iblockstate.getBlock().onEntityCollidedWithBlock(this.worldObj, blockpos2, iblockstate, this);
/*      */             }
/*  798 */             catch (Throwable throwable) {
/*      */               
/*  800 */               CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
/*  801 */               CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
/*  802 */               CrashReportCategory.addBlockInfo(crashreportcategory, blockpos2, iblockstate);
/*  803 */               throw new ReportedException(crashreport);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos pos, Block blockIn) {
/*  813 */     Block.SoundType block$soundtype = blockIn.stepSound;
/*      */     
/*  815 */     if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer) {
/*      */       
/*  817 */       block$soundtype = Blocks.snow_layer.stepSound;
/*  818 */       playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */     }
/*  820 */     else if (!blockIn.getMaterial().isLiquid()) {
/*      */       
/*  822 */       playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSound(String name, float volume, float pitch) {
/*  828 */     if (!isSilent())
/*      */     {
/*  830 */       this.worldObj.playSoundAtEntity(this, name, volume, pitch);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSilent() {
/*  836 */     return (this.dataWatcher.getWatchableObjectByte(4) == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSilent(boolean isSilent) {
/*  841 */     this.dataWatcher.updateObject(4, Byte.valueOf((byte)(isSilent ? 1 : 0)));
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean canTriggerWalking() {
/*  846 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
/*  851 */     if (onGroundIn) {
/*      */       
/*  853 */       if (this.fallDistance > 0.0F)
/*      */       {
/*  855 */         if (blockIn != null) {
/*      */           
/*  857 */           blockIn.onFallenUpon(this.worldObj, pos, this, this.fallDistance);
/*      */         }
/*      */         else {
/*      */           
/*  861 */           fall(this.fallDistance, 1.0F);
/*      */         } 
/*      */         
/*  864 */         this.fallDistance = 0.0F;
/*      */       }
/*      */     
/*  867 */     } else if (y < 0.0D) {
/*      */       
/*  869 */       this.fallDistance = (float)(this.fallDistance - y);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public AxisAlignedBB getCollisionBoundingBox() {
/*  875 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dealFireDamage(int amount) {
/*  880 */     if (!this.isImmuneToFire)
/*      */     {
/*  882 */       attackEntityFrom(DamageSource.inFire, amount);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isImmuneToFire() {
/*  888 */     return this.isImmuneToFire;
/*      */   }
/*      */ 
/*      */   
/*      */   public void fall(float distance, float damageMultiplier) {
/*  893 */     if (this.riddenByEntity != null)
/*      */     {
/*  895 */       this.riddenByEntity.fall(distance, damageMultiplier);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWet() {
/*  901 */     return (this.inWater || this.worldObj.isRainingAt(new BlockPos(this.posX, this.posY, this.posZ)) || this.worldObj.isRainingAt(new BlockPos(this.posX, this.posY + this.height, this.posZ)));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInWater() {
/*  906 */     return this.inWater;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean handleWaterMovement() {
/*  911 */     if (this.worldObj.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this)) {
/*      */       
/*  913 */       if (!this.inWater && !this.firstUpdate)
/*      */       {
/*  915 */         resetHeight();
/*      */       }
/*      */       
/*  918 */       this.fallDistance = 0.0F;
/*  919 */       this.inWater = true;
/*  920 */       this.fire = 0;
/*      */     }
/*      */     else {
/*      */       
/*  924 */       this.inWater = false;
/*      */     } 
/*      */     
/*  927 */     return this.inWater;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void resetHeight() {
/*  932 */     float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;
/*      */     
/*  934 */     if (f > 1.0F)
/*      */     {
/*  936 */       f = 1.0F;
/*      */     }
/*      */     
/*  939 */     playSound(getSplashSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
/*  940 */     float f1 = MathHelper.floor_double((getEntityBoundingBox()).minY);
/*      */     
/*  942 */     for (int i = 0; i < 1.0F + this.width * 20.0F; i++) {
/*      */       
/*  944 */       float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
/*  945 */       float f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
/*  946 */       this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f2, (f1 + 1.0F), this.posZ + f3, this.motionX, this.motionY - (this.rand.nextFloat() * 0.2F), this.motionZ, new int[0]);
/*      */     } 
/*      */     
/*  949 */     for (int j = 0; j < 1.0F + this.width * 20.0F; j++) {
/*      */       
/*  951 */       float f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
/*  952 */       float f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
/*  953 */       this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f4, (f1 + 1.0F), this.posZ + f5, this.motionX, this.motionY, this.motionZ, new int[0]);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void spawnRunningParticles() {
/*  959 */     if (isSprinting() && !isInWater())
/*      */     {
/*  961 */       createRunningParticles();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void createRunningParticles() {
/*  967 */     int i = MathHelper.floor_double(this.posX);
/*  968 */     int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/*  969 */     int k = MathHelper.floor_double(this.posZ);
/*  970 */     BlockPos blockpos = new BlockPos(i, j, k);
/*  971 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/*  972 */     Block block = iblockstate.getBlock();
/*      */     
/*  974 */     if (block.getRenderType() != -1)
/*      */     {
/*  976 */       this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, (getEntityBoundingBox()).minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, new int[] { Block.getStateId(iblockstate) });
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getSplashSound() {
/*  982 */     return "game.neutral.swim.splash";
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInsideOfMaterial(Material materialIn) {
/*  987 */     double d0 = this.posY + getEyeHeight();
/*  988 */     BlockPos blockpos = new BlockPos(this.posX, d0, this.posZ);
/*  989 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/*  990 */     Block block = iblockstate.getBlock();
/*      */     
/*  992 */     if (block.getMaterial() == materialIn) {
/*      */       
/*  994 */       float f = BlockLiquid.getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111F;
/*  995 */       float f1 = (blockpos.getY() + 1) - f;
/*  996 */       boolean flag = (d0 < f1);
/*  997 */       return (!flag && this instanceof EntityPlayer) ? false : flag;
/*      */     } 
/*      */ 
/*      */     
/* 1001 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInLava() {
/* 1007 */     return this.worldObj.isMaterialInBB(getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveFlying(float strafe, float forward, float friction) {
/* 1012 */     float f = strafe * strafe + forward * forward;
/*      */     
/* 1014 */     if (f >= 1.0E-4F) {
/*      */       
/* 1016 */       f = MathHelper.sqrt_float(f);
/*      */       
/* 1018 */       if (f < 1.0F)
/*      */       {
/* 1020 */         f = 1.0F;
/*      */       }
/*      */       
/* 1023 */       f = friction / f;
/* 1024 */       strafe *= f;
/* 1025 */       forward *= f;
/* 1026 */       float f1 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F);
/* 1027 */       float f2 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
/* 1028 */       this.motionX += (strafe * f2 - forward * f1);
/* 1029 */       this.motionZ += (forward * f2 + strafe * f1);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBrightnessForRender(float partialTicks) {
/* 1035 */     BlockPos blockpos = new BlockPos(this.posX, this.posY + getEyeHeight(), this.posZ);
/* 1036 */     return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getBrightness(float partialTicks) {
/* 1041 */     BlockPos blockpos = new BlockPos(this.posX, this.posY + getEyeHeight(), this.posZ);
/* 1042 */     return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getLightBrightness(blockpos) : 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWorld(World worldIn) {
/* 1047 */     this.worldObj = worldIn;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
/* 1052 */     this.prevPosX = this.posX = x;
/* 1053 */     this.prevPosY = this.posY = y;
/* 1054 */     this.prevPosZ = this.posZ = z;
/* 1055 */     this.prevRotationYaw = this.rotationYaw = yaw;
/* 1056 */     this.prevRotationPitch = this.rotationPitch = pitch;
/* 1057 */     double d0 = (this.prevRotationYaw - yaw);
/*      */     
/* 1059 */     if (d0 < -180.0D)
/*      */     {
/* 1061 */       this.prevRotationYaw += 360.0F;
/*      */     }
/*      */     
/* 1064 */     if (d0 >= 180.0D)
/*      */     {
/* 1066 */       this.prevRotationYaw -= 360.0F;
/*      */     }
/*      */     
/* 1069 */     setPosition(this.posX, this.posY, this.posZ);
/* 1070 */     setRotation(yaw, pitch);
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {
/* 1075 */     setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, rotationYawIn, rotationPitchIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
/* 1080 */     this.lastTickPosX = this.prevPosX = this.posX = x;
/* 1081 */     this.lastTickPosY = this.prevPosY = this.posY = y;
/* 1082 */     this.lastTickPosZ = this.prevPosZ = this.posZ = z;
/* 1083 */     this.rotationYaw = yaw;
/* 1084 */     this.rotationPitch = pitch;
/* 1085 */     setPosition(this.posX, this.posY, this.posZ);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getDistanceToEntity(Entity entityIn) {
/* 1090 */     float f = (float)(this.posX - entityIn.posX);
/* 1091 */     float f1 = (float)(this.posY - entityIn.posY);
/* 1092 */     float f2 = (float)(this.posZ - entityIn.posZ);
/* 1093 */     return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDistanceSq(double x, double y, double z) {
/* 1098 */     double d0 = this.posX - x;
/* 1099 */     double d1 = this.posY - y;
/* 1100 */     double d2 = this.posZ - z;
/* 1101 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDistanceSq(BlockPos pos) {
/* 1106 */     return pos.distanceSq(this.posX, this.posY, this.posZ);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDistanceSqToCenter(BlockPos pos) {
/* 1111 */     return pos.distanceSqToCenter(this.posX, this.posY, this.posZ);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDistance(double x, double y, double z) {
/* 1116 */     double d0 = this.posX - x;
/* 1117 */     double d1 = this.posY - y;
/* 1118 */     double d2 = this.posZ - z;
/* 1119 */     return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDistanceSqToEntity(Entity entityIn) {
/* 1124 */     double d0 = this.posX - entityIn.posX;
/* 1125 */     double d1 = this.posY - entityIn.posY;
/* 1126 */     double d2 = this.posZ - entityIn.posZ;
/* 1127 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onCollideWithPlayer(EntityPlayer entityIn) {}
/*      */ 
/*      */   
/*      */   public void applyEntityCollision(Entity entityIn) {
/* 1136 */     if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this)
/*      */     {
/* 1138 */       if (!entityIn.noClip && !this.noClip) {
/*      */         
/* 1140 */         double d0 = entityIn.posX - this.posX;
/* 1141 */         double d1 = entityIn.posZ - this.posZ;
/* 1142 */         double d2 = MathHelper.abs_max(d0, d1);
/*      */         
/* 1144 */         if (d2 >= 0.009999999776482582D) {
/*      */           
/* 1146 */           d2 = MathHelper.sqrt_double(d2);
/* 1147 */           d0 /= d2;
/* 1148 */           d1 /= d2;
/* 1149 */           double d3 = 1.0D / d2;
/*      */           
/* 1151 */           if (d3 > 1.0D)
/*      */           {
/* 1153 */             d3 = 1.0D;
/*      */           }
/*      */           
/* 1156 */           d0 *= d3;
/* 1157 */           d1 *= d3;
/* 1158 */           d0 *= 0.05000000074505806D;
/* 1159 */           d1 *= 0.05000000074505806D;
/* 1160 */           d0 *= (1.0F - this.entityCollisionReduction);
/* 1161 */           d1 *= (1.0F - this.entityCollisionReduction);
/*      */           
/* 1163 */           if (this.riddenByEntity == null)
/*      */           {
/* 1165 */             addVelocity(-d0, 0.0D, -d1);
/*      */           }
/*      */           
/* 1168 */           if (entityIn.riddenByEntity == null)
/*      */           {
/* 1170 */             entityIn.addVelocity(d0, 0.0D, d1);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addVelocity(double x, double y, double z) {
/* 1179 */     this.motionX += x;
/* 1180 */     this.motionY += y;
/* 1181 */     this.motionZ += z;
/* 1182 */     this.isAirBorne = true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setBeenAttacked() {
/* 1187 */     this.velocityChanged = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 1192 */     if (isEntityInvulnerable(source))
/*      */     {
/* 1194 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1198 */     setBeenAttacked();
/* 1199 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Vec3 getLook(float partialTicks) {
/* 1205 */     if (partialTicks == 1.0F)
/*      */     {
/* 1207 */       return getVectorForRotation(this.rotationPitch, this.rotationYaw);
/*      */     }
/*      */ 
/*      */     
/* 1211 */     float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
/* 1212 */     float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
/* 1213 */     return getVectorForRotation(f, f1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Vec3 getVectorForRotation(float pitch, float yaw) {
/* 1219 */     float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
/* 1220 */     float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
/* 1221 */     float f2 = -MathHelper.cos(-pitch * 0.017453292F);
/* 1222 */     float f3 = MathHelper.sin(-pitch * 0.017453292F);
/* 1223 */     return new Vec3((f1 * f2), f3, (f * f2));
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getPositionEyes(float partialTicks) {
/* 1228 */     if (partialTicks == 1.0F)
/*      */     {
/* 1230 */       return new Vec3(this.posX, this.posY + getEyeHeight(), this.posZ);
/*      */     }
/*      */ 
/*      */     
/* 1234 */     double d0 = this.prevPosX + (this.posX - this.prevPosX) * partialTicks;
/* 1235 */     double d1 = this.prevPosY + (this.posY - this.prevPosY) * partialTicks + getEyeHeight();
/* 1236 */     double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks;
/* 1237 */     return new Vec3(d0, d1, d2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public MovingObjectPosition rayTrace(double blockReachDistance, float partialTicks) {
/* 1243 */     Vec3 vec3 = getPositionEyes(partialTicks);
/* 1244 */     Vec3 vec31 = getLook(partialTicks);
/* 1245 */     Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
/* 1246 */     return this.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeCollidedWith() {
/* 1251 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBePushed() {
/* 1256 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addToPlayerScore(Entity entityIn, int amount) {}
/*      */ 
/*      */   
/*      */   public boolean isInRangeToRender3d(double x, double y, double z) {
/* 1265 */     double d0 = this.posX - x;
/* 1266 */     double d1 = this.posY - y;
/* 1267 */     double d2 = this.posZ - z;
/* 1268 */     double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/* 1269 */     return isInRangeToRenderDist(d3);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInRangeToRenderDist(double distance) {
/* 1274 */     double d0 = getEntityBoundingBox().getAverageEdgeLength();
/*      */     
/* 1276 */     if (Double.isNaN(d0))
/*      */     {
/* 1278 */       d0 = 1.0D;
/*      */     }
/*      */     
/* 1281 */     d0 = d0 * 64.0D * this.renderDistanceWeight;
/* 1282 */     return (distance < d0 * d0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean writeMountToNBT(NBTTagCompound tagCompund) {
/* 1287 */     String s = getEntityString();
/*      */     
/* 1289 */     if (!this.isDead && s != null) {
/*      */       
/* 1291 */       tagCompund.setString("id", s);
/* 1292 */       writeToNBT(tagCompund);
/* 1293 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1297 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean writeToNBTOptional(NBTTagCompound tagCompund) {
/* 1303 */     String s = getEntityString();
/*      */     
/* 1305 */     if (!this.isDead && s != null && this.riddenByEntity == null) {
/*      */       
/* 1307 */       tagCompund.setString("id", s);
/* 1308 */       writeToNBT(tagCompund);
/* 1309 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1313 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeToNBT(NBTTagCompound tagCompund) {
/*      */     try {
/* 1321 */       tagCompund.setTag("Pos", (NBTBase)newDoubleNBTList(new double[] { this.posX, this.posY, this.posZ }));
/* 1322 */       tagCompund.setTag("Motion", (NBTBase)newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
/* 1323 */       tagCompund.setTag("Rotation", (NBTBase)newFloatNBTList(new float[] { this.rotationYaw, this.rotationPitch }));
/* 1324 */       tagCompund.setFloat("FallDistance", this.fallDistance);
/* 1325 */       tagCompund.setShort("Fire", (short)this.fire);
/* 1326 */       tagCompund.setShort("Air", (short)getAir());
/* 1327 */       tagCompund.setBoolean("OnGround", this.onGround);
/* 1328 */       tagCompund.setInteger("Dimension", this.dimension);
/* 1329 */       tagCompund.setBoolean("Invulnerable", this.invulnerable);
/* 1330 */       tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
/* 1331 */       tagCompund.setLong("UUIDMost", getUniqueID().getMostSignificantBits());
/* 1332 */       tagCompund.setLong("UUIDLeast", getUniqueID().getLeastSignificantBits());
/*      */       
/* 1334 */       if (getCustomNameTag() != null && getCustomNameTag().length() > 0) {
/*      */         
/* 1336 */         tagCompund.setString("CustomName", getCustomNameTag());
/* 1337 */         tagCompund.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
/*      */       } 
/*      */       
/* 1340 */       this.cmdResultStats.writeStatsToNBT(tagCompund);
/*      */       
/* 1342 */       if (isSilent())
/*      */       {
/* 1344 */         tagCompund.setBoolean("Silent", isSilent());
/*      */       }
/*      */       
/* 1347 */       writeEntityToNBT(tagCompund);
/*      */       
/* 1349 */       if (this.ridingEntity != null)
/*      */       {
/* 1351 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/*      */         
/* 1353 */         if (this.ridingEntity.writeMountToNBT(nbttagcompound))
/*      */         {
/* 1355 */           tagCompund.setTag("Riding", (NBTBase)nbttagcompound);
/*      */         }
/*      */       }
/*      */     
/* 1359 */     } catch (Throwable throwable) {
/*      */       
/* 1361 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
/* 1362 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
/* 1363 */       addEntityCrashInfo(crashreportcategory);
/* 1364 */       throw new ReportedException(crashreport);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void readFromNBT(NBTTagCompound tagCompund) {
/*      */     try {
/* 1372 */       NBTTagList nbttaglist = tagCompund.getTagList("Pos", 6);
/* 1373 */       NBTTagList nbttaglist1 = tagCompund.getTagList("Motion", 6);
/* 1374 */       NBTTagList nbttaglist2 = tagCompund.getTagList("Rotation", 5);
/* 1375 */       this.motionX = nbttaglist1.getDoubleAt(0);
/* 1376 */       this.motionY = nbttaglist1.getDoubleAt(1);
/* 1377 */       this.motionZ = nbttaglist1.getDoubleAt(2);
/*      */       
/* 1379 */       if (Math.abs(this.motionX) > 10.0D)
/*      */       {
/* 1381 */         this.motionX = 0.0D;
/*      */       }
/*      */       
/* 1384 */       if (Math.abs(this.motionY) > 10.0D)
/*      */       {
/* 1386 */         this.motionY = 0.0D;
/*      */       }
/*      */       
/* 1389 */       if (Math.abs(this.motionZ) > 10.0D)
/*      */       {
/* 1391 */         this.motionZ = 0.0D;
/*      */       }
/*      */       
/* 1394 */       this.prevPosX = this.lastTickPosX = this.posX = nbttaglist.getDoubleAt(0);
/* 1395 */       this.prevPosY = this.lastTickPosY = this.posY = nbttaglist.getDoubleAt(1);
/* 1396 */       this.prevPosZ = this.lastTickPosZ = this.posZ = nbttaglist.getDoubleAt(2);
/* 1397 */       this.prevRotationYaw = this.rotationYaw = nbttaglist2.getFloatAt(0);
/* 1398 */       this.prevRotationPitch = this.rotationPitch = nbttaglist2.getFloatAt(1);
/* 1399 */       setRotationYawHead(this.rotationYaw);
/* 1400 */       setRenderYawOffset(this.rotationYaw);
/* 1401 */       this.fallDistance = tagCompund.getFloat("FallDistance");
/* 1402 */       this.fire = tagCompund.getShort("Fire");
/* 1403 */       setAir(tagCompund.getShort("Air"));
/* 1404 */       this.onGround = tagCompund.getBoolean("OnGround");
/* 1405 */       this.dimension = tagCompund.getInteger("Dimension");
/* 1406 */       this.invulnerable = tagCompund.getBoolean("Invulnerable");
/* 1407 */       this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");
/*      */       
/* 1409 */       if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4)) {
/*      */         
/* 1411 */         this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
/*      */       }
/* 1413 */       else if (tagCompund.hasKey("UUID", 8)) {
/*      */         
/* 1415 */         this.entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
/*      */       } 
/*      */       
/* 1418 */       setPosition(this.posX, this.posY, this.posZ);
/* 1419 */       setRotation(this.rotationYaw, this.rotationPitch);
/*      */       
/* 1421 */       if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
/*      */       {
/* 1423 */         setCustomNameTag(tagCompund.getString("CustomName"));
/*      */       }
/*      */       
/* 1426 */       setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
/* 1427 */       this.cmdResultStats.readStatsFromNBT(tagCompund);
/* 1428 */       setSilent(tagCompund.getBoolean("Silent"));
/* 1429 */       readEntityFromNBT(tagCompund);
/*      */       
/* 1431 */       if (shouldSetPosAfterLoading())
/*      */       {
/* 1433 */         setPosition(this.posX, this.posY, this.posZ);
/*      */       }
/*      */     }
/* 1436 */     catch (Throwable throwable) {
/*      */       
/* 1438 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
/* 1439 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
/* 1440 */       addEntityCrashInfo(crashreportcategory);
/* 1441 */       throw new ReportedException(crashreport);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean shouldSetPosAfterLoading() {
/* 1447 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final String getEntityString() {
/* 1452 */     return EntityList.getEntityString(this);
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract void readEntityFromNBT(NBTTagCompound paramNBTTagCompound);
/*      */ 
/*      */   
/*      */   protected abstract void writeEntityToNBT(NBTTagCompound paramNBTTagCompound);
/*      */ 
/*      */   
/*      */   public void onChunkLoad() {}
/*      */   
/*      */   protected NBTTagList newDoubleNBTList(double... numbers) {
/* 1465 */     NBTTagList nbttaglist = new NBTTagList();
/*      */     
/* 1467 */     for (double d0 : numbers)
/*      */     {
/* 1469 */       nbttaglist.appendTag((NBTBase)new NBTTagDouble(d0));
/*      */     }
/*      */     
/* 1472 */     return nbttaglist;
/*      */   }
/*      */ 
/*      */   
/*      */   protected NBTTagList newFloatNBTList(float... numbers) {
/* 1477 */     NBTTagList nbttaglist = new NBTTagList();
/*      */     
/* 1479 */     for (float f : numbers)
/*      */     {
/* 1481 */       nbttaglist.appendTag((NBTBase)new NBTTagFloat(f));
/*      */     }
/*      */     
/* 1484 */     return nbttaglist;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityItem dropItem(Item itemIn, int size) {
/* 1489 */     return dropItemWithOffset(itemIn, size, 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY) {
/* 1494 */     return entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityItem entityDropItem(ItemStack itemStackIn, float offsetY) {
/* 1499 */     if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
/*      */       
/* 1501 */       EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + offsetY, this.posZ, itemStackIn);
/* 1502 */       entityitem.setDefaultPickupDelay();
/* 1503 */       this.worldObj.spawnEntityInWorld((Entity)entityitem);
/* 1504 */       return entityitem;
/*      */     } 
/*      */ 
/*      */     
/* 1508 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEntityAlive() {
/* 1514 */     return !this.isDead;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEntityInsideOpaqueBlock() {
/* 1519 */     if (this.noClip)
/*      */     {
/* 1521 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1525 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(-2147483648, -2147483648, -2147483648);
/*      */     
/* 1527 */     for (int i = 0; i < 8; i++) {
/*      */       
/* 1529 */       int j = MathHelper.floor_double(this.posY + ((((i >> 0) % 2) - 0.5F) * 0.1F) + getEyeHeight());
/* 1530 */       int k = MathHelper.floor_double(this.posX + ((((i >> 1) % 2) - 0.5F) * this.width * 0.8F));
/* 1531 */       int l = MathHelper.floor_double(this.posZ + ((((i >> 2) % 2) - 0.5F) * this.width * 0.8F));
/*      */       
/* 1533 */       if (blockpos$mutableblockpos.getX() != k || blockpos$mutableblockpos.getY() != j || blockpos$mutableblockpos.getZ() != l) {
/*      */         
/* 1535 */         blockpos$mutableblockpos.set(k, j, l);
/*      */         
/* 1537 */         if (this.worldObj.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock().isVisuallyOpaque())
/*      */         {
/* 1539 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1544 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean interactFirst(EntityPlayer playerIn) {
/* 1550 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public AxisAlignedBB getCollisionBox(Entity entityIn) {
/* 1555 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRidden() {
/* 1560 */     if (this.ridingEntity.isDead) {
/*      */       
/* 1562 */       this.ridingEntity = null;
/*      */     }
/*      */     else {
/*      */       
/* 1566 */       this.motionX = 0.0D;
/* 1567 */       this.motionY = 0.0D;
/* 1568 */       this.motionZ = 0.0D;
/* 1569 */       onUpdate();
/*      */       
/* 1571 */       if (this.ridingEntity != null) {
/*      */         
/* 1573 */         this.ridingEntity.updateRiderPosition();
/* 1574 */         this.entityRiderYawDelta += (this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);
/*      */         
/* 1576 */         for (this.entityRiderPitchDelta += (this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1581 */         while (this.entityRiderYawDelta < -180.0D)
/*      */         {
/* 1583 */           this.entityRiderYawDelta += 360.0D;
/*      */         }
/*      */         
/* 1586 */         while (this.entityRiderPitchDelta >= 180.0D)
/*      */         {
/* 1588 */           this.entityRiderPitchDelta -= 360.0D;
/*      */         }
/*      */         
/* 1591 */         while (this.entityRiderPitchDelta < -180.0D)
/*      */         {
/* 1593 */           this.entityRiderPitchDelta += 360.0D;
/*      */         }
/*      */         
/* 1596 */         double d0 = this.entityRiderYawDelta * 0.5D;
/* 1597 */         double d1 = this.entityRiderPitchDelta * 0.5D;
/* 1598 */         float f = 10.0F;
/*      */         
/* 1600 */         if (d0 > f)
/*      */         {
/* 1602 */           d0 = f;
/*      */         }
/*      */         
/* 1605 */         if (d0 < -f)
/*      */         {
/* 1607 */           d0 = -f;
/*      */         }
/*      */         
/* 1610 */         if (d1 > f)
/*      */         {
/* 1612 */           d1 = f;
/*      */         }
/*      */         
/* 1615 */         if (d1 < -f)
/*      */         {
/* 1617 */           d1 = -f;
/*      */         }
/*      */         
/* 1620 */         this.entityRiderYawDelta -= d0;
/* 1621 */         this.entityRiderPitchDelta -= d1;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRiderPosition() {
/* 1628 */     if (this.riddenByEntity != null)
/*      */     {
/* 1630 */       this.riddenByEntity.setPosition(this.posX, this.posY + getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public double getYOffset() {
/* 1636 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getMountedYOffset() {
/* 1641 */     return this.height * 0.75D;
/*      */   }
/*      */ 
/*      */   
/*      */   public void mountEntity(Entity entityIn) {
/* 1646 */     this.entityRiderPitchDelta = 0.0D;
/* 1647 */     this.entityRiderYawDelta = 0.0D;
/*      */     
/* 1649 */     if (entityIn == null) {
/*      */       
/* 1651 */       if (this.ridingEntity != null) {
/*      */         
/* 1653 */         setLocationAndAngles(this.ridingEntity.posX, (this.ridingEntity.getEntityBoundingBox()).minY + this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
/* 1654 */         this.ridingEntity.riddenByEntity = null;
/*      */       } 
/*      */       
/* 1657 */       this.ridingEntity = null;
/*      */     }
/*      */     else {
/*      */       
/* 1661 */       if (this.ridingEntity != null)
/*      */       {
/* 1663 */         this.ridingEntity.riddenByEntity = null;
/*      */       }
/*      */       
/* 1666 */       if (entityIn != null)
/*      */       {
/* 1668 */         for (Entity entity = entityIn.ridingEntity; entity != null; entity = entity.ridingEntity) {
/*      */           
/* 1670 */           if (entity == this) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1677 */       this.ridingEntity = entityIn;
/* 1678 */       entityIn.riddenByEntity = this;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
/* 1684 */     setPosition(x, y, z);
/* 1685 */     setRotation(yaw, pitch);
/* 1686 */     List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().contract(0.03125D, 0.0D, 0.03125D));
/*      */     
/* 1688 */     if (!list.isEmpty()) {
/*      */       
/* 1690 */       double d0 = 0.0D;
/*      */       
/* 1692 */       for (AxisAlignedBB axisalignedbb : list) {
/*      */         
/* 1694 */         if (axisalignedbb.maxY > d0)
/*      */         {
/* 1696 */           d0 = axisalignedbb.maxY;
/*      */         }
/*      */       } 
/*      */       
/* 1700 */       y += d0 - (getEntityBoundingBox()).minY;
/* 1701 */       setPosition(x, y, z);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCollisionBorderSize() {
/* 1707 */     return 0.1F;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getLookVec() {
/* 1712 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPortal(BlockPos pos) {
/* 1717 */     if (this.timeUntilPortal > 0) {
/*      */       
/* 1719 */       this.timeUntilPortal = getPortalCooldown();
/*      */     }
/*      */     else {
/*      */       
/* 1723 */       if (!this.worldObj.isRemote && !pos.equals(this.lastPortalPos)) {
/*      */         
/* 1725 */         this.lastPortalPos = pos;
/* 1726 */         BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldObj, pos);
/* 1727 */         double d0 = (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? blockpattern$patternhelper.getPos().getZ() : blockpattern$patternhelper.getPos().getX();
/* 1728 */         double d1 = (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? this.posZ : this.posX;
/* 1729 */         d1 = Math.abs(MathHelper.func_181160_c(d1 - ((blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) ? true : false), d0, d0 - blockpattern$patternhelper.func_181118_d()));
/* 1730 */         double d2 = MathHelper.func_181160_c(this.posY - 1.0D, blockpattern$patternhelper.getPos().getY(), (blockpattern$patternhelper.getPos().getY() - blockpattern$patternhelper.func_181119_e()));
/* 1731 */         this.lastPortalVec = new Vec3(d1, d2, 0.0D);
/* 1732 */         this.teleportDirection = blockpattern$patternhelper.getFinger();
/*      */       } 
/*      */       
/* 1735 */       this.inPortal = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPortalCooldown() {
/* 1741 */     return 300;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setVelocity(double x, double y, double z) {
/* 1746 */     this.motionX = x;
/* 1747 */     this.motionY = y;
/* 1748 */     this.motionZ = z;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleStatusUpdate(byte id) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void performHurtAnimation() {}
/*      */ 
/*      */   
/*      */   public ItemStack[] getInventory() {
/* 1761 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {}
/*      */ 
/*      */   
/*      */   public boolean isBurning() {
/* 1770 */     boolean flag = (this.worldObj != null && this.worldObj.isRemote);
/* 1771 */     return (!this.isImmuneToFire && (this.fire > 0 || (flag && getFlag(0))));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRiding() {
/* 1776 */     return (this.ridingEntity != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSneaking() {
/* 1781 */     return getFlag(1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSneaking(boolean sneaking) {
/* 1786 */     setFlag(1, sneaking);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSprinting() {
/* 1791 */     return getFlag(3);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSprinting(boolean sprinting) {
/* 1796 */     setFlag(3, sprinting);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInvisible() {
/* 1801 */     return getFlag(5);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInvisibleToPlayer(EntityPlayer player) {
/* 1806 */     return player.isSpectator() ? false : isInvisible();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInvisible(boolean invisible) {
/* 1811 */     setFlag(5, invisible);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEating() {
/* 1816 */     return getFlag(4);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEating(boolean eating) {
/* 1821 */     setFlag(4, eating);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean getFlag(int flag) {
/* 1826 */     return ((this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setFlag(int flag, boolean set) {
/* 1831 */     byte b0 = this.dataWatcher.getWatchableObjectByte(0);
/*      */     
/* 1833 */     if (set) {
/*      */       
/* 1835 */       this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 | 1 << flag)));
/*      */     }
/*      */     else {
/*      */       
/* 1839 */       this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 & (1 << flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getAir() {
/* 1845 */     return this.dataWatcher.getWatchableObjectShort(1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAir(int air) {
/* 1850 */     this.dataWatcher.updateObject(1, Short.valueOf((short)air));
/*      */   }
/*      */ 
/*      */   
/*      */   public void onStruckByLightning(EntityLightningBolt lightningBolt) {
/* 1855 */     attackEntityFrom(DamageSource.lightningBolt, 5.0F);
/* 1856 */     this.fire++;
/*      */     
/* 1858 */     if (this.fire == 0)
/*      */     {
/* 1860 */       setFire(8);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onKillEntity(EntityLivingBase entityLivingIn) {}
/*      */ 
/*      */   
/*      */   protected boolean pushOutOfBlocks(double x, double y, double z) {
/* 1870 */     BlockPos blockpos = new BlockPos(x, y, z);
/* 1871 */     double d0 = x - blockpos.getX();
/* 1872 */     double d1 = y - blockpos.getY();
/* 1873 */     double d2 = z - blockpos.getZ();
/* 1874 */     List<AxisAlignedBB> list = this.worldObj.getCollisionBoxes(getEntityBoundingBox());
/*      */     
/* 1876 */     if (list.isEmpty() && !this.worldObj.isBlockFullCube(blockpos))
/*      */     {
/* 1878 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1882 */     int i = 3;
/* 1883 */     double d3 = 9999.0D;
/*      */     
/* 1885 */     if (!this.worldObj.isBlockFullCube(blockpos.west()) && d0 < d3) {
/*      */       
/* 1887 */       d3 = d0;
/* 1888 */       i = 0;
/*      */     } 
/*      */     
/* 1891 */     if (!this.worldObj.isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3) {
/*      */       
/* 1893 */       d3 = 1.0D - d0;
/* 1894 */       i = 1;
/*      */     } 
/*      */     
/* 1897 */     if (!this.worldObj.isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3) {
/*      */       
/* 1899 */       d3 = 1.0D - d1;
/* 1900 */       i = 3;
/*      */     } 
/*      */     
/* 1903 */     if (!this.worldObj.isBlockFullCube(blockpos.north()) && d2 < d3) {
/*      */       
/* 1905 */       d3 = d2;
/* 1906 */       i = 4;
/*      */     } 
/*      */     
/* 1909 */     if (!this.worldObj.isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3) {
/*      */       
/* 1911 */       d3 = 1.0D - d2;
/* 1912 */       i = 5;
/*      */     } 
/*      */     
/* 1915 */     float f = this.rand.nextFloat() * 0.2F + 0.1F;
/*      */     
/* 1917 */     if (i == 0)
/*      */     {
/* 1919 */       this.motionX = -f;
/*      */     }
/*      */     
/* 1922 */     if (i == 1)
/*      */     {
/* 1924 */       this.motionX = f;
/*      */     }
/*      */     
/* 1927 */     if (i == 3)
/*      */     {
/* 1929 */       this.motionY = f;
/*      */     }
/*      */     
/* 1932 */     if (i == 4)
/*      */     {
/* 1934 */       this.motionZ = -f;
/*      */     }
/*      */     
/* 1937 */     if (i == 5)
/*      */     {
/* 1939 */       this.motionZ = f;
/*      */     }
/*      */     
/* 1942 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInWeb() {
/* 1948 */     this.isInWeb = true;
/* 1949 */     this.fallDistance = 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1954 */     if (hasCustomName())
/*      */     {
/* 1956 */       return getCustomNameTag();
/*      */     }
/*      */ 
/*      */     
/* 1960 */     String s = EntityList.getEntityString(this);
/*      */     
/* 1962 */     if (s == null)
/*      */     {
/* 1964 */       s = "generic";
/*      */     }
/*      */     
/* 1967 */     return StatCollector.translateToLocal("entity." + s + ".name");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Entity[] getParts() {
/* 1973 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEntityEqual(Entity entityIn) {
/* 1978 */     return (this == entityIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getRotationYawHead() {
/* 1983 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRotationYawHead(float rotation) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRenderYawOffset(float offset) {}
/*      */ 
/*      */   
/*      */   public boolean canAttackWithItem() {
/* 1996 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hitByEntity(Entity entityIn) {
/* 2001 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 2006 */     return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", new Object[] { getClass().getSimpleName(), getName(), Integer.valueOf(this.entityId), (this.worldObj == null) ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) });
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEntityInvulnerable(DamageSource source) {
/* 2011 */     return (this.invulnerable && source != DamageSource.outOfWorld && !source.isCreativePlayer());
/*      */   }
/*      */ 
/*      */   
/*      */   public void copyLocationAndAnglesFrom(Entity entityIn) {
/* 2016 */     setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
/*      */   }
/*      */ 
/*      */   
/*      */   public void copyDataFromOld(Entity entityIn) {
/* 2021 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 2022 */     entityIn.writeToNBT(nbttagcompound);
/* 2023 */     readFromNBT(nbttagcompound);
/* 2024 */     this.timeUntilPortal = entityIn.timeUntilPortal;
/* 2025 */     this.lastPortalPos = entityIn.lastPortalPos;
/* 2026 */     this.lastPortalVec = entityIn.lastPortalVec;
/* 2027 */     this.teleportDirection = entityIn.teleportDirection;
/*      */   }
/*      */ 
/*      */   
/*      */   public void travelToDimension(int dimensionId) {
/* 2032 */     if (!this.worldObj.isRemote && !this.isDead) {
/*      */       
/* 2034 */       this.worldObj.theProfiler.startSection("changeDimension");
/* 2035 */       MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 2036 */       int i = this.dimension;
/* 2037 */       WorldServer worldserver = minecraftserver.worldServerForDimension(i);
/* 2038 */       WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionId);
/* 2039 */       this.dimension = dimensionId;
/*      */       
/* 2041 */       if (i == 1 && dimensionId == 1) {
/*      */         
/* 2043 */         worldserver1 = minecraftserver.worldServerForDimension(0);
/* 2044 */         this.dimension = 0;
/*      */       } 
/*      */       
/* 2047 */       this.worldObj.removeEntity(this);
/* 2048 */       this.isDead = false;
/* 2049 */       this.worldObj.theProfiler.startSection("reposition");
/* 2050 */       minecraftserver.getConfigurationManager().transferEntityToWorld(this, i, worldserver, worldserver1);
/* 2051 */       this.worldObj.theProfiler.endStartSection("reloading");
/* 2052 */       Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), (World)worldserver1);
/*      */       
/* 2054 */       if (entity != null) {
/*      */         
/* 2056 */         entity.copyDataFromOld(this);
/*      */         
/* 2058 */         if (i == 1 && dimensionId == 1) {
/*      */           
/* 2060 */           BlockPos blockpos = this.worldObj.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
/* 2061 */           entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
/*      */         } 
/*      */         
/* 2064 */         worldserver1.spawnEntityInWorld(entity);
/*      */       } 
/*      */       
/* 2067 */       this.isDead = true;
/* 2068 */       this.worldObj.theProfiler.endSection();
/* 2069 */       worldserver.resetUpdateEntityTick();
/* 2070 */       worldserver1.resetUpdateEntityTick();
/* 2071 */       this.worldObj.theProfiler.endSection();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
/* 2077 */     return blockStateIn.getBlock().getExplosionResistance(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_) {
/* 2082 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxFallHeight() {
/* 2087 */     return 3;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 func_181014_aG() {
/* 2092 */     return this.lastPortalVec;
/*      */   }
/*      */ 
/*      */   
/*      */   public EnumFacing getTeleportDirection() {
/* 2097 */     return this.teleportDirection;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doesEntityNotTriggerPressurePlate() {
/* 2102 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addEntityCrashInfo(CrashReportCategory category) {
/* 2107 */     category.addCrashSectionCallable("Entity Type", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2111 */             return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
/*      */           }
/*      */         });
/* 2114 */     category.addCrashSection("Entity ID", Integer.valueOf(this.entityId));
/* 2115 */     category.addCrashSectionCallable("Entity Name", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2119 */             return Entity.this.getName();
/*      */           }
/*      */         });
/* 2122 */     category.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) }));
/* 2123 */     category.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
/* 2124 */     category.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.motionX), Double.valueOf(this.motionY), Double.valueOf(this.motionZ) }));
/* 2125 */     category.addCrashSectionCallable("Entity's Rider", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2129 */             return Entity.this.riddenByEntity.toString();
/*      */           }
/*      */         });
/* 2132 */     category.addCrashSectionCallable("Entity's Vehicle", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2136 */             return Entity.this.ridingEntity.toString();
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canRenderOnFire() {
/* 2143 */     return isBurning();
/*      */   }
/*      */ 
/*      */   
/*      */   public UUID getUniqueID() {
/* 2148 */     return this.entityUniqueID;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPushedByWater() {
/* 2153 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public IChatComponent getDisplayName() {
/* 2158 */     ChatComponentText chatcomponenttext = new ChatComponentText(getName());
/* 2159 */     chatcomponenttext.getChatStyle().setChatHoverEvent(getHoverEvent());
/* 2160 */     chatcomponenttext.getChatStyle().setInsertion(getUniqueID().toString());
/* 2161 */     return (IChatComponent)chatcomponenttext;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCustomNameTag(String name) {
/* 2166 */     this.dataWatcher.updateObject(2, name);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCustomNameTag() {
/* 2171 */     return this.dataWatcher.getWatchableObjectString(2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasCustomName() {
/* 2176 */     return (this.dataWatcher.getWatchableObjectString(2).length() > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag) {
/* 2181 */     this.dataWatcher.updateObject(3, Byte.valueOf((byte)(alwaysRenderNameTag ? 1 : 0)));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getAlwaysRenderNameTag() {
/* 2186 */     return (this.dataWatcher.getWatchableObjectByte(3) == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPositionAndUpdate(double x, double y, double z) {
/* 2191 */     setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getAlwaysRenderNameTagForRender() {
/* 2196 */     return getAlwaysRenderNameTag();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onDataWatcherUpdate(int dataID) {}
/*      */ 
/*      */   
/*      */   public EnumFacing getHorizontalFacing() {
/* 2205 */     return EnumFacing.getHorizontal(MathHelper.floor_double((this.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3);
/*      */   }
/*      */ 
/*      */   
/*      */   protected HoverEvent getHoverEvent() {
/* 2210 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 2211 */     String s = EntityList.getEntityString(this);
/* 2212 */     nbttagcompound.setString("id", getUniqueID().toString());
/*      */     
/* 2214 */     if (s != null)
/*      */     {
/* 2216 */       nbttagcompound.setString("type", s);
/*      */     }
/*      */     
/* 2219 */     nbttagcompound.setString("name", getName());
/* 2220 */     return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, (IChatComponent)new ChatComponentText(nbttagcompound.toString()));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSpectatedByPlayer(EntityPlayerMP player) {
/* 2225 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public AxisAlignedBB getEntityBoundingBox() {
/* 2230 */     return this.boundingBox;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEntityBoundingBox(AxisAlignedBB bb) {
/* 2235 */     this.boundingBox = bb;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getEyeHeight() {
/* 2240 */     return this.height * 0.85F;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOutsideBorder() {
/* 2245 */     return this.isOutsideBorder;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOutsideBorder(boolean outsideBorder) {
/* 2250 */     this.isOutsideBorder = outsideBorder;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
/* 2255 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addChatMessage(IChatComponent component) {}
/*      */ 
/*      */   
/*      */   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/* 2264 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos getPosition() {
/* 2269 */     return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getPositionVector() {
/* 2274 */     return new Vec3(this.posX, this.posY, this.posZ);
/*      */   }
/*      */ 
/*      */   
/*      */   public World getEntityWorld() {
/* 2279 */     return this.worldObj;
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity getCommandSenderEntity() {
/* 2284 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean sendCommandFeedback() {
/* 2289 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCommandStat(CommandResultStats.Type type, int amount) {
/* 2294 */     this.cmdResultStats.setCommandStatScore(this, type, amount);
/*      */   }
/*      */ 
/*      */   
/*      */   public CommandResultStats getCommandStats() {
/* 2299 */     return this.cmdResultStats;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCommandStats(Entity entityIn) {
/* 2304 */     this.cmdResultStats.addAllStats(entityIn.getCommandStats());
/*      */   }
/*      */ 
/*      */   
/*      */   public NBTTagCompound getNBTTagCompound() {
/* 2309 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void clientUpdateEntityNBT(NBTTagCompound compound) {}
/*      */ 
/*      */   
/*      */   public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
/* 2318 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isImmuneToExplosions() {
/* 2323 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {
/* 2328 */     if (entityIn instanceof EntityLivingBase)
/*      */     {
/* 2330 */       EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entityIn, entityLivingBaseIn);
/*      */     }
/*      */     
/* 2333 */     EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\Entity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */