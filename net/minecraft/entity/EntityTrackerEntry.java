/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
/*     */ import net.minecraft.entity.item.EntityFallingBlock;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.item.EntityPainting;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityFireball;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S04PacketEntityEquipment;
/*     */ import net.minecraft.network.play.server.S0APacketUseBed;
/*     */ import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
/*     */ import net.minecraft.network.play.server.S0EPacketSpawnObject;
/*     */ import net.minecraft.network.play.server.S0FPacketSpawnMob;
/*     */ import net.minecraft.network.play.server.S10PacketSpawnPainting;
/*     */ import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
/*     */ import net.minecraft.network.play.server.S12PacketEntityVelocity;
/*     */ import net.minecraft.network.play.server.S14PacketEntity;
/*     */ import net.minecraft.network.play.server.S18PacketEntityTeleport;
/*     */ import net.minecraft.network.play.server.S19PacketEntityHeadLook;
/*     */ import net.minecraft.network.play.server.S1BPacketEntityAttach;
/*     */ import net.minecraft.network.play.server.S1CPacketEntityMetadata;
/*     */ import net.minecraft.network.play.server.S1DPacketEntityEffect;
/*     */ import net.minecraft.network.play.server.S20PacketEntityProperties;
/*     */ import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityTrackerEntry
/*     */ {
/*  65 */   private static final Logger logger = LogManager.getLogger();
/*     */   public Entity trackedEntity;
/*     */   public int trackingDistanceThreshold;
/*     */   public int updateFrequency;
/*     */   public int encodedPosX;
/*     */   public int encodedPosY;
/*     */   public int encodedPosZ;
/*     */   public int encodedRotationYaw;
/*     */   public int encodedRotationPitch;
/*     */   public int lastHeadMotion;
/*     */   public double lastTrackedEntityMotionX;
/*     */   public double lastTrackedEntityMotionY;
/*     */   public double motionZ;
/*     */   public int updateCounter;
/*     */   private double lastTrackedEntityPosX;
/*     */   private double lastTrackedEntityPosY;
/*     */   private double lastTrackedEntityPosZ;
/*     */   private boolean firstUpdateDone;
/*     */   private boolean sendVelocityUpdates;
/*     */   private int ticksSinceLastForcedTeleport;
/*     */   private Entity field_85178_v;
/*     */   private boolean ridingEntity;
/*     */   private boolean onGround;
/*     */   public boolean playerEntitiesUpdated;
/*  89 */   public Set<EntityPlayerMP> trackingPlayers = Sets.newHashSet();
/*     */ 
/*     */   
/*     */   public EntityTrackerEntry(Entity trackedEntityIn, int trackingDistanceThresholdIn, int updateFrequencyIn, boolean sendVelocityUpdatesIn) {
/*  93 */     this.trackedEntity = trackedEntityIn;
/*  94 */     this.trackingDistanceThreshold = trackingDistanceThresholdIn;
/*  95 */     this.updateFrequency = updateFrequencyIn;
/*  96 */     this.sendVelocityUpdates = sendVelocityUpdatesIn;
/*  97 */     this.encodedPosX = MathHelper.floor_double(trackedEntityIn.posX * 32.0D);
/*  98 */     this.encodedPosY = MathHelper.floor_double(trackedEntityIn.posY * 32.0D);
/*  99 */     this.encodedPosZ = MathHelper.floor_double(trackedEntityIn.posZ * 32.0D);
/* 100 */     this.encodedRotationYaw = MathHelper.floor_float(trackedEntityIn.rotationYaw * 256.0F / 360.0F);
/* 101 */     this.encodedRotationPitch = MathHelper.floor_float(trackedEntityIn.rotationPitch * 256.0F / 360.0F);
/* 102 */     this.lastHeadMotion = MathHelper.floor_float(trackedEntityIn.getRotationYawHead() * 256.0F / 360.0F);
/* 103 */     this.onGround = trackedEntityIn.onGround;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 108 */     return (p_equals_1_ instanceof EntityTrackerEntry) ? ((((EntityTrackerEntry)p_equals_1_).trackedEntity.getEntityId() == this.trackedEntity.getEntityId())) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     return this.trackedEntity.getEntityId();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePlayerList(List<EntityPlayer> players) {
/* 118 */     this.playerEntitiesUpdated = false;
/*     */     
/* 120 */     if (!this.firstUpdateDone || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0D) {
/*     */       
/* 122 */       this.lastTrackedEntityPosX = this.trackedEntity.posX;
/* 123 */       this.lastTrackedEntityPosY = this.trackedEntity.posY;
/* 124 */       this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
/* 125 */       this.firstUpdateDone = true;
/* 126 */       this.playerEntitiesUpdated = true;
/* 127 */       updatePlayerEntities(players);
/*     */     } 
/*     */     
/* 130 */     if (this.field_85178_v != this.trackedEntity.ridingEntity || (this.trackedEntity.ridingEntity != null && this.updateCounter % 60 == 0)) {
/*     */       
/* 132 */       this.field_85178_v = this.trackedEntity.ridingEntity;
/* 133 */       sendPacketToTrackedPlayers((Packet)new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
/*     */     } 
/*     */     
/* 136 */     if (this.trackedEntity instanceof EntityItemFrame && this.updateCounter % 10 == 0) {
/*     */       
/* 138 */       EntityItemFrame entityitemframe = (EntityItemFrame)this.trackedEntity;
/* 139 */       ItemStack itemstack = entityitemframe.getDisplayedItem();
/*     */       
/* 141 */       if (itemstack != null && itemstack.getItem() instanceof net.minecraft.item.ItemMap) {
/*     */         
/* 143 */         MapData mapdata = Items.filled_map.getMapData(itemstack, this.trackedEntity.worldObj);
/*     */         
/* 145 */         for (EntityPlayer entityplayer : players) {
/*     */           
/* 147 */           EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
/* 148 */           mapdata.updateVisiblePlayers((EntityPlayer)entityplayermp, itemstack);
/* 149 */           Packet packet = Items.filled_map.createMapDataPacket(itemstack, this.trackedEntity.worldObj, (EntityPlayer)entityplayermp);
/*     */           
/* 151 */           if (packet != null)
/*     */           {
/* 153 */             entityplayermp.playerNetServerHandler.sendPacket(packet);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 158 */       sendMetadataToAllAssociatedPlayers();
/*     */     } 
/*     */     
/* 161 */     if (this.updateCounter % this.updateFrequency == 0 || this.trackedEntity.isAirBorne || this.trackedEntity.getDataWatcher().hasObjectChanged()) {
/*     */       
/* 163 */       if (this.trackedEntity.ridingEntity == null) {
/*     */         S18PacketEntityTeleport s18PacketEntityTeleport;
/* 165 */         this.ticksSinceLastForcedTeleport++;
/* 166 */         int k = MathHelper.floor_double(this.trackedEntity.posX * 32.0D);
/* 167 */         int j1 = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
/* 168 */         int k1 = MathHelper.floor_double(this.trackedEntity.posZ * 32.0D);
/* 169 */         int l1 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
/* 170 */         int i2 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
/* 171 */         int j2 = k - this.encodedPosX;
/* 172 */         int k2 = j1 - this.encodedPosY;
/* 173 */         int i = k1 - this.encodedPosZ;
/* 174 */         Packet packet1 = null;
/* 175 */         boolean flag = (Math.abs(j2) >= 4 || Math.abs(k2) >= 4 || Math.abs(i) >= 4 || this.updateCounter % 60 == 0);
/* 176 */         boolean flag1 = (Math.abs(l1 - this.encodedRotationYaw) >= 4 || Math.abs(i2 - this.encodedRotationPitch) >= 4);
/*     */         
/* 178 */         if (this.updateCounter > 0 || this.trackedEntity instanceof EntityArrow)
/*     */         {
/* 180 */           if (j2 >= -128 && j2 < 128 && k2 >= -128 && k2 < 128 && i >= -128 && i < 128 && this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity && this.onGround == this.trackedEntity.onGround) {
/*     */             
/* 182 */             if ((!flag || !flag1) && !(this.trackedEntity instanceof EntityArrow)) {
/*     */               
/* 184 */               if (flag)
/*     */               {
/* 186 */                 S14PacketEntity.S15PacketEntityRelMove s15PacketEntityRelMove = new S14PacketEntity.S15PacketEntityRelMove(this.trackedEntity.getEntityId(), (byte)j2, (byte)k2, (byte)i, this.trackedEntity.onGround);
/*     */               }
/* 188 */               else if (flag1)
/*     */               {
/* 190 */                 S14PacketEntity.S16PacketEntityLook s16PacketEntityLook = new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)l1, (byte)i2, this.trackedEntity.onGround);
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 195 */               S14PacketEntity.S17PacketEntityLookMove s17PacketEntityLookMove = new S14PacketEntity.S17PacketEntityLookMove(this.trackedEntity.getEntityId(), (byte)j2, (byte)k2, (byte)i, (byte)l1, (byte)i2, this.trackedEntity.onGround);
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 200 */             this.onGround = this.trackedEntity.onGround;
/* 201 */             this.ticksSinceLastForcedTeleport = 0;
/* 202 */             s18PacketEntityTeleport = new S18PacketEntityTeleport(this.trackedEntity.getEntityId(), k, j1, k1, (byte)l1, (byte)i2, this.trackedEntity.onGround);
/*     */           } 
/*     */         }
/*     */         
/* 206 */         if (this.sendVelocityUpdates) {
/*     */           
/* 208 */           double d0 = this.trackedEntity.motionX - this.lastTrackedEntityMotionX;
/* 209 */           double d1 = this.trackedEntity.motionY - this.lastTrackedEntityMotionY;
/* 210 */           double d2 = this.trackedEntity.motionZ - this.motionZ;
/* 211 */           double d3 = 0.02D;
/* 212 */           double d4 = d0 * d0 + d1 * d1 + d2 * d2;
/*     */           
/* 214 */           if (d4 > d3 * d3 || (d4 > 0.0D && this.trackedEntity.motionX == 0.0D && this.trackedEntity.motionY == 0.0D && this.trackedEntity.motionZ == 0.0D)) {
/*     */             
/* 216 */             this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
/* 217 */             this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
/* 218 */             this.motionZ = this.trackedEntity.motionZ;
/* 219 */             sendPacketToTrackedPlayers((Packet)new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ));
/*     */           } 
/*     */         } 
/*     */         
/* 223 */         if (s18PacketEntityTeleport != null)
/*     */         {
/* 225 */           sendPacketToTrackedPlayers((Packet)s18PacketEntityTeleport);
/*     */         }
/*     */         
/* 228 */         sendMetadataToAllAssociatedPlayers();
/*     */         
/* 230 */         if (flag) {
/*     */           
/* 232 */           this.encodedPosX = k;
/* 233 */           this.encodedPosY = j1;
/* 234 */           this.encodedPosZ = k1;
/*     */         } 
/*     */         
/* 237 */         if (flag1) {
/*     */           
/* 239 */           this.encodedRotationYaw = l1;
/* 240 */           this.encodedRotationPitch = i2;
/*     */         } 
/*     */         
/* 243 */         this.ridingEntity = false;
/*     */       }
/*     */       else {
/*     */         
/* 247 */         int j = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
/* 248 */         int i1 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
/* 249 */         boolean flag2 = (Math.abs(j - this.encodedRotationYaw) >= 4 || Math.abs(i1 - this.encodedRotationPitch) >= 4);
/*     */         
/* 251 */         if (flag2) {
/*     */           
/* 253 */           sendPacketToTrackedPlayers((Packet)new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)j, (byte)i1, this.trackedEntity.onGround));
/* 254 */           this.encodedRotationYaw = j;
/* 255 */           this.encodedRotationPitch = i1;
/*     */         } 
/*     */         
/* 258 */         this.encodedPosX = MathHelper.floor_double(this.trackedEntity.posX * 32.0D);
/* 259 */         this.encodedPosY = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
/* 260 */         this.encodedPosZ = MathHelper.floor_double(this.trackedEntity.posZ * 32.0D);
/* 261 */         sendMetadataToAllAssociatedPlayers();
/* 262 */         this.ridingEntity = true;
/*     */       } 
/*     */       
/* 265 */       int l = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
/*     */       
/* 267 */       if (Math.abs(l - this.lastHeadMotion) >= 4) {
/*     */         
/* 269 */         sendPacketToTrackedPlayers((Packet)new S19PacketEntityHeadLook(this.trackedEntity, (byte)l));
/* 270 */         this.lastHeadMotion = l;
/*     */       } 
/*     */       
/* 273 */       this.trackedEntity.isAirBorne = false;
/*     */     } 
/*     */     
/* 276 */     this.updateCounter++;
/*     */     
/* 278 */     if (this.trackedEntity.velocityChanged) {
/*     */       
/* 280 */       func_151261_b((Packet)new S12PacketEntityVelocity(this.trackedEntity));
/* 281 */       this.trackedEntity.velocityChanged = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendMetadataToAllAssociatedPlayers() {
/* 287 */     DataWatcher datawatcher = this.trackedEntity.getDataWatcher();
/*     */     
/* 289 */     if (datawatcher.hasObjectChanged())
/*     */     {
/* 291 */       func_151261_b((Packet)new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), datawatcher, false));
/*     */     }
/*     */     
/* 294 */     if (this.trackedEntity instanceof EntityLivingBase) {
/*     */       
/* 296 */       ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
/* 297 */       Set<IAttributeInstance> set = serversideattributemap.getAttributeInstanceSet();
/*     */       
/* 299 */       if (!set.isEmpty())
/*     */       {
/* 301 */         func_151261_b((Packet)new S20PacketEntityProperties(this.trackedEntity.getEntityId(), set));
/*     */       }
/*     */       
/* 304 */       set.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendPacketToTrackedPlayers(Packet packetIn) {
/* 310 */     for (EntityPlayerMP entityplayermp : this.trackingPlayers)
/*     */     {
/* 312 */       entityplayermp.playerNetServerHandler.sendPacket(packetIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_151261_b(Packet packetIn) {
/* 318 */     sendPacketToTrackedPlayers(packetIn);
/*     */     
/* 320 */     if (this.trackedEntity instanceof EntityPlayerMP)
/*     */     {
/* 322 */       ((EntityPlayerMP)this.trackedEntity).playerNetServerHandler.sendPacket(packetIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendDestroyEntityPacketToTrackedPlayers() {
/* 328 */     for (EntityPlayerMP entityplayermp : this.trackingPlayers)
/*     */     {
/* 330 */       entityplayermp.removeEntity(this.trackedEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeFromTrackedPlayers(EntityPlayerMP playerMP) {
/* 336 */     if (this.trackingPlayers.contains(playerMP)) {
/*     */       
/* 338 */       playerMP.removeEntity(this.trackedEntity);
/* 339 */       this.trackingPlayers.remove(playerMP);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePlayerEntity(EntityPlayerMP playerMP) {
/* 345 */     if (playerMP != this.trackedEntity)
/*     */     {
/* 347 */       if (func_180233_c(playerMP)) {
/*     */         
/* 349 */         if (!this.trackingPlayers.contains(playerMP) && (isPlayerWatchingThisChunk(playerMP) || this.trackedEntity.forceSpawn)) {
/*     */           
/* 351 */           this.trackingPlayers.add(playerMP);
/* 352 */           Packet packet = createSpawnPacket();
/* 353 */           playerMP.playerNetServerHandler.sendPacket(packet);
/*     */           
/* 355 */           if (!this.trackedEntity.getDataWatcher().getIsBlank())
/*     */           {
/* 357 */             playerMP.playerNetServerHandler.sendPacket((Packet)new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), this.trackedEntity.getDataWatcher(), true));
/*     */           }
/*     */           
/* 360 */           NBTTagCompound nbttagcompound = this.trackedEntity.getNBTTagCompound();
/*     */           
/* 362 */           if (nbttagcompound != null)
/*     */           {
/* 364 */             playerMP.playerNetServerHandler.sendPacket((Packet)new S49PacketUpdateEntityNBT(this.trackedEntity.getEntityId(), nbttagcompound));
/*     */           }
/*     */           
/* 367 */           if (this.trackedEntity instanceof EntityLivingBase) {
/*     */             
/* 369 */             ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
/* 370 */             Collection<IAttributeInstance> collection = serversideattributemap.getWatchedAttributes();
/*     */             
/* 372 */             if (!collection.isEmpty())
/*     */             {
/* 374 */               playerMP.playerNetServerHandler.sendPacket((Packet)new S20PacketEntityProperties(this.trackedEntity.getEntityId(), collection));
/*     */             }
/*     */           } 
/*     */           
/* 378 */           this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
/* 379 */           this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
/* 380 */           this.motionZ = this.trackedEntity.motionZ;
/*     */           
/* 382 */           if (this.sendVelocityUpdates && !(packet instanceof S0FPacketSpawnMob))
/*     */           {
/* 384 */             playerMP.playerNetServerHandler.sendPacket((Packet)new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.trackedEntity.motionX, this.trackedEntity.motionY, this.trackedEntity.motionZ));
/*     */           }
/*     */           
/* 387 */           if (this.trackedEntity.ridingEntity != null)
/*     */           {
/* 389 */             playerMP.playerNetServerHandler.sendPacket((Packet)new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
/*     */           }
/*     */           
/* 392 */           if (this.trackedEntity instanceof EntityLiving && ((EntityLiving)this.trackedEntity).getLeashedToEntity() != null)
/*     */           {
/* 394 */             playerMP.playerNetServerHandler.sendPacket((Packet)new S1BPacketEntityAttach(1, this.trackedEntity, ((EntityLiving)this.trackedEntity).getLeashedToEntity()));
/*     */           }
/*     */           
/* 397 */           if (this.trackedEntity instanceof EntityLivingBase)
/*     */           {
/* 399 */             for (int i = 0; i < 5; i++) {
/*     */               
/* 401 */               ItemStack itemstack = ((EntityLivingBase)this.trackedEntity).getEquipmentInSlot(i);
/*     */               
/* 403 */               if (itemstack != null)
/*     */               {
/* 405 */                 playerMP.playerNetServerHandler.sendPacket((Packet)new S04PacketEntityEquipment(this.trackedEntity.getEntityId(), i, itemstack));
/*     */               }
/*     */             } 
/*     */           }
/*     */           
/* 410 */           if (this.trackedEntity instanceof EntityPlayer) {
/*     */             
/* 412 */             EntityPlayer entityplayer = (EntityPlayer)this.trackedEntity;
/*     */             
/* 414 */             if (entityplayer.isPlayerSleeping())
/*     */             {
/* 416 */               playerMP.playerNetServerHandler.sendPacket((Packet)new S0APacketUseBed(entityplayer, new BlockPos(this.trackedEntity)));
/*     */             }
/*     */           } 
/*     */           
/* 420 */           if (this.trackedEntity instanceof EntityLivingBase)
/*     */           {
/* 422 */             EntityLivingBase entitylivingbase = (EntityLivingBase)this.trackedEntity;
/*     */             
/* 424 */             for (PotionEffect potioneffect : entitylivingbase.getActivePotionEffects())
/*     */             {
/* 426 */               playerMP.playerNetServerHandler.sendPacket((Packet)new S1DPacketEntityEffect(this.trackedEntity.getEntityId(), potioneffect));
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/* 431 */       } else if (this.trackingPlayers.contains(playerMP)) {
/*     */         
/* 433 */         this.trackingPlayers.remove(playerMP);
/* 434 */         playerMP.removeEntity(this.trackedEntity);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_180233_c(EntityPlayerMP playerMP) {
/* 441 */     double d0 = playerMP.posX - (this.encodedPosX / 32);
/* 442 */     double d1 = playerMP.posZ - (this.encodedPosZ / 32);
/* 443 */     return (d0 >= -this.trackingDistanceThreshold && d0 <= this.trackingDistanceThreshold && d1 >= -this.trackingDistanceThreshold && d1 <= this.trackingDistanceThreshold && this.trackedEntity.isSpectatedByPlayer(playerMP));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isPlayerWatchingThisChunk(EntityPlayerMP playerMP) {
/* 448 */     return playerMP.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(playerMP, this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePlayerEntities(List<EntityPlayer> players) {
/* 453 */     for (int i = 0; i < players.size(); i++)
/*     */     {
/* 455 */       updatePlayerEntity((EntityPlayerMP)players.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Packet createSpawnPacket() {
/* 461 */     if (this.trackedEntity.isDead)
/*     */     {
/* 463 */       logger.warn("Fetching addPacket for removed entity");
/*     */     }
/*     */     
/* 466 */     if (this.trackedEntity instanceof net.minecraft.entity.item.EntityItem)
/*     */     {
/* 468 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 2, 1);
/*     */     }
/* 470 */     if (this.trackedEntity instanceof EntityPlayerMP)
/*     */     {
/* 472 */       return (Packet)new S0CPacketSpawnPlayer((EntityPlayer)this.trackedEntity);
/*     */     }
/* 474 */     if (this.trackedEntity instanceof EntityMinecart) {
/*     */       
/* 476 */       EntityMinecart entityminecart = (EntityMinecart)this.trackedEntity;
/* 477 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 10, entityminecart.getMinecartType().getNetworkID());
/*     */     } 
/* 479 */     if (this.trackedEntity instanceof net.minecraft.entity.item.EntityBoat)
/*     */     {
/* 481 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 1);
/*     */     }
/* 483 */     if (this.trackedEntity instanceof net.minecraft.entity.passive.IAnimals) {
/*     */       
/* 485 */       this.lastHeadMotion = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
/* 486 */       return (Packet)new S0FPacketSpawnMob((EntityLivingBase)this.trackedEntity);
/*     */     } 
/* 488 */     if (this.trackedEntity instanceof EntityFishHook) {
/*     */       
/* 490 */       EntityPlayer entityPlayer = ((EntityFishHook)this.trackedEntity).angler;
/* 491 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 90, (entityPlayer != null) ? entityPlayer.getEntityId() : this.trackedEntity.getEntityId());
/*     */     } 
/* 493 */     if (this.trackedEntity instanceof EntityArrow) {
/*     */       
/* 495 */       Entity entity = ((EntityArrow)this.trackedEntity).shootingEntity;
/* 496 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 60, (entity != null) ? entity.getEntityId() : this.trackedEntity.getEntityId());
/*     */     } 
/* 498 */     if (this.trackedEntity instanceof net.minecraft.entity.projectile.EntitySnowball)
/*     */     {
/* 500 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 61);
/*     */     }
/* 502 */     if (this.trackedEntity instanceof EntityPotion)
/*     */     {
/* 504 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 73, ((EntityPotion)this.trackedEntity).getPotionDamage());
/*     */     }
/* 506 */     if (this.trackedEntity instanceof net.minecraft.entity.item.EntityExpBottle)
/*     */     {
/* 508 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 75);
/*     */     }
/* 510 */     if (this.trackedEntity instanceof net.minecraft.entity.item.EntityEnderPearl)
/*     */     {
/* 512 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 65);
/*     */     }
/* 514 */     if (this.trackedEntity instanceof net.minecraft.entity.item.EntityEnderEye)
/*     */     {
/* 516 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 72);
/*     */     }
/* 518 */     if (this.trackedEntity instanceof net.minecraft.entity.item.EntityFireworkRocket)
/*     */     {
/* 520 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 76);
/*     */     }
/* 522 */     if (this.trackedEntity instanceof EntityFireball) {
/*     */       
/* 524 */       EntityFireball entityfireball = (EntityFireball)this.trackedEntity;
/* 525 */       S0EPacketSpawnObject s0epacketspawnobject2 = null;
/* 526 */       int i = 63;
/*     */       
/* 528 */       if (this.trackedEntity instanceof net.minecraft.entity.projectile.EntitySmallFireball) {
/*     */         
/* 530 */         i = 64;
/*     */       }
/* 532 */       else if (this.trackedEntity instanceof net.minecraft.entity.projectile.EntityWitherSkull) {
/*     */         
/* 534 */         i = 66;
/*     */       } 
/*     */       
/* 537 */       if (entityfireball.shootingEntity != null) {
/*     */         
/* 539 */         s0epacketspawnobject2 = new S0EPacketSpawnObject(this.trackedEntity, i, ((EntityFireball)this.trackedEntity).shootingEntity.getEntityId());
/*     */       }
/*     */       else {
/*     */         
/* 543 */         s0epacketspawnobject2 = new S0EPacketSpawnObject(this.trackedEntity, i, 0);
/*     */       } 
/*     */       
/* 546 */       s0epacketspawnobject2.setSpeedX((int)(entityfireball.accelerationX * 8000.0D));
/* 547 */       s0epacketspawnobject2.setSpeedY((int)(entityfireball.accelerationY * 8000.0D));
/* 548 */       s0epacketspawnobject2.setSpeedZ((int)(entityfireball.accelerationZ * 8000.0D));
/* 549 */       return (Packet)s0epacketspawnobject2;
/*     */     } 
/* 551 */     if (this.trackedEntity instanceof net.minecraft.entity.projectile.EntityEgg)
/*     */     {
/* 553 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 62);
/*     */     }
/* 555 */     if (this.trackedEntity instanceof net.minecraft.entity.item.EntityTNTPrimed)
/*     */     {
/* 557 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 50);
/*     */     }
/* 559 */     if (this.trackedEntity instanceof net.minecraft.entity.item.EntityEnderCrystal)
/*     */     {
/* 561 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 51);
/*     */     }
/* 563 */     if (this.trackedEntity instanceof EntityFallingBlock) {
/*     */       
/* 565 */       EntityFallingBlock entityfallingblock = (EntityFallingBlock)this.trackedEntity;
/* 566 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 70, Block.getStateId(entityfallingblock.getBlock()));
/*     */     } 
/* 568 */     if (this.trackedEntity instanceof net.minecraft.entity.item.EntityArmorStand)
/*     */     {
/* 570 */       return (Packet)new S0EPacketSpawnObject(this.trackedEntity, 78);
/*     */     }
/* 572 */     if (this.trackedEntity instanceof EntityPainting)
/*     */     {
/* 574 */       return (Packet)new S10PacketSpawnPainting((EntityPainting)this.trackedEntity);
/*     */     }
/* 576 */     if (this.trackedEntity instanceof EntityItemFrame) {
/*     */       
/* 578 */       EntityItemFrame entityitemframe = (EntityItemFrame)this.trackedEntity;
/* 579 */       S0EPacketSpawnObject s0epacketspawnobject1 = new S0EPacketSpawnObject(this.trackedEntity, 71, entityitemframe.facingDirection.getHorizontalIndex());
/* 580 */       BlockPos blockpos1 = entityitemframe.getHangingPosition();
/* 581 */       s0epacketspawnobject1.setX(MathHelper.floor_float((blockpos1.getX() * 32)));
/* 582 */       s0epacketspawnobject1.setY(MathHelper.floor_float((blockpos1.getY() * 32)));
/* 583 */       s0epacketspawnobject1.setZ(MathHelper.floor_float((blockpos1.getZ() * 32)));
/* 584 */       return (Packet)s0epacketspawnobject1;
/*     */     } 
/* 586 */     if (this.trackedEntity instanceof EntityLeashKnot) {
/*     */       
/* 588 */       EntityLeashKnot entityleashknot = (EntityLeashKnot)this.trackedEntity;
/* 589 */       S0EPacketSpawnObject s0epacketspawnobject = new S0EPacketSpawnObject(this.trackedEntity, 77);
/* 590 */       BlockPos blockpos = entityleashknot.getHangingPosition();
/* 591 */       s0epacketspawnobject.setX(MathHelper.floor_float((blockpos.getX() * 32)));
/* 592 */       s0epacketspawnobject.setY(MathHelper.floor_float((blockpos.getY() * 32)));
/* 593 */       s0epacketspawnobject.setZ(MathHelper.floor_float((blockpos.getZ() * 32)));
/* 594 */       return (Packet)s0epacketspawnobject;
/*     */     } 
/* 596 */     if (this.trackedEntity instanceof EntityXPOrb)
/*     */     {
/* 598 */       return (Packet)new S11PacketSpawnExperienceOrb((EntityXPOrb)this.trackedEntity);
/*     */     }
/*     */ 
/*     */     
/* 602 */     throw new IllegalArgumentException("Don't know how to add " + this.trackedEntity.getClass() + "!");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTrackedPlayerSymmetric(EntityPlayerMP playerMP) {
/* 608 */     if (this.trackingPlayers.contains(playerMP)) {
/*     */       
/* 610 */       this.trackingPlayers.remove(playerMP);
/* 611 */       playerMP.removeEntity(this.trackedEntity);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityTrackerEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */