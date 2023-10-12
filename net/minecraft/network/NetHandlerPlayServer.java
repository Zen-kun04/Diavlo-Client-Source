/*      */ package net.minecraft.network;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.primitives.Doubles;
/*      */ import com.google.common.primitives.Floats;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.GenericFutureListener;
/*      */ import java.io.IOException;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Future;
/*      */ import net.minecraft.command.ICommandSender;
/*      */ import net.minecraft.command.server.CommandBlockLogic;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityMinecartCommandBlock;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.passive.EntityHorse;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.inventory.ContainerBeacon;
/*      */ import net.minecraft.inventory.ContainerMerchant;
/*      */ import net.minecraft.inventory.ContainerRepair;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.inventory.Slot;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.item.ItemWritableBook;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagString;
/*      */ import net.minecraft.network.play.INetHandlerPlayServer;
/*      */ import net.minecraft.network.play.client.C00PacketKeepAlive;
/*      */ import net.minecraft.network.play.client.C01PacketChatMessage;
/*      */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*      */ import net.minecraft.network.play.client.C03PacketPlayer;
/*      */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*      */ import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
/*      */ import net.minecraft.network.play.client.C09PacketHeldItemChange;
/*      */ import net.minecraft.network.play.client.C0APacketAnimation;
/*      */ import net.minecraft.network.play.client.C0BPacketEntityAction;
/*      */ import net.minecraft.network.play.client.C0CPacketInput;
/*      */ import net.minecraft.network.play.client.C0DPacketCloseWindow;
/*      */ import net.minecraft.network.play.client.C0EPacketClickWindow;
/*      */ import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
/*      */ import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
/*      */ import net.minecraft.network.play.client.C11PacketEnchantItem;
/*      */ import net.minecraft.network.play.client.C12PacketUpdateSign;
/*      */ import net.minecraft.network.play.client.C13PacketPlayerAbilities;
/*      */ import net.minecraft.network.play.client.C14PacketTabComplete;
/*      */ import net.minecraft.network.play.client.C15PacketClientSettings;
/*      */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*      */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*      */ import net.minecraft.network.play.client.C18PacketSpectate;
/*      */ import net.minecraft.network.play.client.C19PacketResourcePackStatus;
/*      */ import net.minecraft.network.play.handlehand.CPacketAnimation;
/*      */ import net.minecraft.network.play.server.S02PacketChat;
/*      */ import net.minecraft.network.play.server.S08PacketPlayerPosLook;
/*      */ import net.minecraft.network.play.server.S23PacketBlockChange;
/*      */ import net.minecraft.network.play.server.S2FPacketSetSlot;
/*      */ import net.minecraft.network.play.server.S32PacketConfirmTransaction;
/*      */ import net.minecraft.network.play.server.S40PacketDisconnect;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.management.UserListBansEntry;
/*      */ import net.minecraft.stats.StatBase;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.tileentity.TileEntityCommandBlock;
/*      */ import net.minecraft.tileentity.TileEntitySign;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatAllowedCharacters;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.IThreadListener;
/*      */ import net.minecraft.util.IntHashMap;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class NetHandlerPlayServer implements INetHandlerPlayServer, ITickable {
/*   91 */   private static final Logger logger = LogManager.getLogger();
/*      */   public final NetworkManager netManager;
/*      */   private final MinecraftServer serverController;
/*      */   public EntityPlayerMP playerEntity;
/*      */   private int networkTickCount;
/*      */   private int field_175090_f;
/*      */   private int floatingTickCount;
/*      */   private boolean field_147366_g;
/*      */   private int field_147378_h;
/*      */   private long lastPingTime;
/*      */   private long lastSentPingPacket;
/*      */   private int chatSpamThresholdCount;
/*      */   private int itemDropThreshold;
/*  104 */   private IntHashMap<Short> field_147372_n = new IntHashMap();
/*      */   
/*      */   private double lastPosX;
/*      */   private double lastPosY;
/*      */   private double lastPosZ;
/*      */   private boolean hasMoved = true;
/*      */   
/*      */   public NetHandlerPlayServer(MinecraftServer server, NetworkManager networkManagerIn, EntityPlayerMP playerIn) {
/*  112 */     this.serverController = server;
/*  113 */     this.netManager = networkManagerIn;
/*  114 */     networkManagerIn.setNetHandler((INetHandler)this);
/*  115 */     this.playerEntity = playerIn;
/*  116 */     playerIn.playerNetServerHandler = this;
/*      */   }
/*      */ 
/*      */   
/*      */   public void update() {
/*  121 */     this.field_147366_g = false;
/*  122 */     this.networkTickCount++;
/*  123 */     this.serverController.theProfiler.startSection("keepAlive");
/*      */     
/*  125 */     if (this.networkTickCount - this.lastSentPingPacket > 40L) {
/*      */       
/*  127 */       this.lastSentPingPacket = this.networkTickCount;
/*  128 */       this.lastPingTime = currentTimeMillis();
/*  129 */       this.field_147378_h = (int)this.lastPingTime;
/*  130 */       sendPacket((Packet)new S00PacketKeepAlive(this.field_147378_h));
/*      */     } 
/*      */     
/*  133 */     this.serverController.theProfiler.endSection();
/*      */     
/*  135 */     if (this.chatSpamThresholdCount > 0)
/*      */     {
/*  137 */       this.chatSpamThresholdCount--;
/*      */     }
/*      */     
/*  140 */     if (this.itemDropThreshold > 0)
/*      */     {
/*  142 */       this.itemDropThreshold--;
/*      */     }
/*      */     
/*  145 */     if (this.playerEntity.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - this.playerEntity.getLastActiveTime() > (this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60))
/*      */     {
/*  147 */       kickPlayerFromServer("You have been idle for too long!");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public NetworkManager getNetworkManager() {
/*  153 */     return this.netManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public void kickPlayerFromServer(String reason) {
/*  158 */     final ChatComponentText chatcomponenttext = new ChatComponentText(reason);
/*  159 */     this.netManager.sendPacket((Packet)new S40PacketDisconnect((IChatComponent)chatcomponenttext), new GenericFutureListener<Future<? super Void>>()
/*      */         {
/*      */           public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception
/*      */           {
/*  163 */             NetHandlerPlayServer.this.netManager.closeChannel((IChatComponent)chatcomponenttext);
/*      */           }
/*      */         },  (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
/*  166 */     this.netManager.disableAutoRead();
/*  167 */     Futures.getUnchecked((Future)this.serverController.addScheduledTask(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/*  171 */               NetHandlerPlayServer.this.netManager.checkDisconnected();
/*      */             }
/*      */           }));
/*      */   }
/*      */ 
/*      */   
/*      */   public void processInput(C0CPacketInput packetIn) {
/*  178 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  179 */     this.playerEntity.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.getForwardSpeed(), packetIn.isJumping(), packetIn.isSneaking());
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean func_183006_b(C03PacketPlayer p_183006_1_) {
/*  184 */     return (!Doubles.isFinite(p_183006_1_.getPositionX()) || !Doubles.isFinite(p_183006_1_.getPositionY()) || !Doubles.isFinite(p_183006_1_.getPositionZ()) || !Floats.isFinite(p_183006_1_.getPitch()) || !Floats.isFinite(p_183006_1_.getYaw()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void processPlayer(C03PacketPlayer packetIn) {
/*  189 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*      */     
/*  191 */     if (func_183006_b(packetIn)) {
/*      */       
/*  193 */       kickPlayerFromServer("Invalid move packet received");
/*      */     }
/*      */     else {
/*      */       
/*  197 */       WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/*  198 */       this.field_147366_g = true;
/*      */       
/*  200 */       if (!this.playerEntity.playerConqueredTheEnd) {
/*      */         
/*  202 */         double d0 = this.playerEntity.posX;
/*  203 */         double d1 = this.playerEntity.posY;
/*  204 */         double d2 = this.playerEntity.posZ;
/*  205 */         double d3 = 0.0D;
/*  206 */         double d4 = packetIn.getPositionX() - this.lastPosX;
/*  207 */         double d5 = packetIn.getPositionY() - this.lastPosY;
/*  208 */         double d6 = packetIn.getPositionZ() - this.lastPosZ;
/*      */         
/*  210 */         if (packetIn.isMoving()) {
/*      */           
/*  212 */           d3 = d4 * d4 + d5 * d5 + d6 * d6;
/*      */           
/*  214 */           if (!this.hasMoved && d3 < 0.25D)
/*      */           {
/*  216 */             this.hasMoved = true;
/*      */           }
/*      */         } 
/*      */         
/*  220 */         if (this.hasMoved) {
/*      */           
/*  222 */           this.field_175090_f = this.networkTickCount;
/*      */           
/*  224 */           if (this.playerEntity.ridingEntity != null) {
/*      */             
/*  226 */             float f4 = this.playerEntity.rotationYaw;
/*  227 */             float f = this.playerEntity.rotationPitch;
/*  228 */             this.playerEntity.ridingEntity.updateRiderPosition();
/*  229 */             double d16 = this.playerEntity.posX;
/*  230 */             double d17 = this.playerEntity.posY;
/*  231 */             double d18 = this.playerEntity.posZ;
/*      */             
/*  233 */             if (packetIn.getRotating()) {
/*      */               
/*  235 */               f4 = packetIn.getYaw();
/*  236 */               f = packetIn.getPitch();
/*      */             } 
/*      */             
/*  239 */             this.playerEntity.onGround = packetIn.isOnGround();
/*  240 */             this.playerEntity.onUpdateEntity();
/*  241 */             this.playerEntity.setPositionAndRotation(d16, d17, d18, f4, f);
/*      */             
/*  243 */             if (this.playerEntity.ridingEntity != null)
/*      */             {
/*  245 */               this.playerEntity.ridingEntity.updateRiderPosition();
/*      */             }
/*      */             
/*  248 */             this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
/*      */             
/*  250 */             if (this.playerEntity.ridingEntity != null) {
/*      */               
/*  252 */               if (d3 > 4.0D) {
/*      */                 
/*  254 */                 Entity entity = this.playerEntity.ridingEntity;
/*  255 */                 this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S18PacketEntityTeleport(entity));
/*  256 */                 setPlayerLocation(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
/*      */               } 
/*      */               
/*  259 */               this.playerEntity.ridingEntity.isAirBorne = true;
/*      */             } 
/*      */             
/*  262 */             if (this.hasMoved) {
/*      */               
/*  264 */               this.lastPosX = this.playerEntity.posX;
/*  265 */               this.lastPosY = this.playerEntity.posY;
/*  266 */               this.lastPosZ = this.playerEntity.posZ;
/*      */             } 
/*      */             
/*  269 */             worldserver.updateEntity((Entity)this.playerEntity);
/*      */             
/*      */             return;
/*      */           } 
/*  273 */           if (this.playerEntity.isPlayerSleeping()) {
/*      */             
/*  275 */             this.playerEntity.onUpdateEntity();
/*  276 */             this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
/*  277 */             worldserver.updateEntity((Entity)this.playerEntity);
/*      */             
/*      */             return;
/*      */           } 
/*  281 */           double d7 = this.playerEntity.posY;
/*  282 */           this.lastPosX = this.playerEntity.posX;
/*  283 */           this.lastPosY = this.playerEntity.posY;
/*  284 */           this.lastPosZ = this.playerEntity.posZ;
/*  285 */           double d8 = this.playerEntity.posX;
/*  286 */           double d9 = this.playerEntity.posY;
/*  287 */           double d10 = this.playerEntity.posZ;
/*  288 */           float f1 = this.playerEntity.rotationYaw;
/*  289 */           float f2 = this.playerEntity.rotationPitch;
/*      */           
/*  291 */           if (packetIn.isMoving() && packetIn.getPositionY() == -999.0D)
/*      */           {
/*  293 */             packetIn.setMoving(false);
/*      */           }
/*      */           
/*  296 */           if (packetIn.isMoving()) {
/*      */             
/*  298 */             d8 = packetIn.getPositionX();
/*  299 */             d9 = packetIn.getPositionY();
/*  300 */             d10 = packetIn.getPositionZ();
/*      */             
/*  302 */             if (Math.abs(packetIn.getPositionX()) > 3.0E7D || Math.abs(packetIn.getPositionZ()) > 3.0E7D) {
/*      */               
/*  304 */               kickPlayerFromServer("Illegal position");
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*  309 */           if (packetIn.getRotating()) {
/*      */             
/*  311 */             f1 = packetIn.getYaw();
/*  312 */             f2 = packetIn.getPitch();
/*      */           } 
/*      */           
/*  315 */           this.playerEntity.onUpdateEntity();
/*  316 */           this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f1, f2);
/*      */           
/*  318 */           if (!this.hasMoved) {
/*      */             return;
/*      */           }
/*      */ 
/*      */           
/*  323 */           double d11 = d8 - this.playerEntity.posX;
/*  324 */           double d12 = d9 - this.playerEntity.posY;
/*  325 */           double d13 = d10 - this.playerEntity.posZ;
/*  326 */           double d14 = this.playerEntity.motionX * this.playerEntity.motionX + this.playerEntity.motionY * this.playerEntity.motionY + this.playerEntity.motionZ * this.playerEntity.motionZ;
/*  327 */           double d15 = d11 * d11 + d12 * d12 + d13 * d13;
/*      */           
/*  329 */           if (d15 - d14 > 100.0D && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(this.playerEntity.getName()))) {
/*      */             
/*  331 */             logger.warn(this.playerEntity.getName() + " moved too quickly! " + d11 + "," + d12 + "," + d13 + " (" + d11 + ", " + d12 + ", " + d13 + ")");
/*  332 */             setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
/*      */             
/*      */             return;
/*      */           } 
/*  336 */           float f3 = 0.0625F;
/*  337 */           boolean flag = worldserver.getCollidingBoundingBoxes((Entity)this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(f3, f3, f3)).isEmpty();
/*      */           
/*  339 */           if (this.playerEntity.onGround && !packetIn.isOnGround() && d12 > 0.0D)
/*      */           {
/*  341 */             this.playerEntity.jump();
/*      */           }
/*      */           
/*  344 */           this.playerEntity.moveEntity(d11, d12, d13);
/*  345 */           this.playerEntity.onGround = packetIn.isOnGround();
/*  346 */           d11 = d8 - this.playerEntity.posX;
/*  347 */           d12 = d9 - this.playerEntity.posY;
/*      */           
/*  349 */           if (d12 > -0.5D || d12 < 0.5D)
/*      */           {
/*  351 */             d12 = 0.0D;
/*      */           }
/*      */           
/*  354 */           d13 = d10 - this.playerEntity.posZ;
/*  355 */           d15 = d11 * d11 + d12 * d12 + d13 * d13;
/*  356 */           boolean flag1 = false;
/*      */           
/*  358 */           if (d15 > 0.0625D && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative()) {
/*      */             
/*  360 */             flag1 = true;
/*  361 */             logger.warn(this.playerEntity.getName() + " moved wrongly!");
/*      */           } 
/*      */           
/*  364 */           this.playerEntity.setPositionAndRotation(d8, d9, d10, f1, f2);
/*  365 */           this.playerEntity.addMovementStat(this.playerEntity.posX - d0, this.playerEntity.posY - d1, this.playerEntity.posZ - d2);
/*      */           
/*  367 */           if (!this.playerEntity.noClip) {
/*      */             
/*  369 */             boolean flag2 = worldserver.getCollidingBoundingBoxes((Entity)this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(f3, f3, f3)).isEmpty();
/*      */             
/*  371 */             if (flag && (flag1 || !flag2) && !this.playerEntity.isPlayerSleeping()) {
/*      */               
/*  373 */               setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, f1, f2);
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*  378 */           AxisAlignedBB axisalignedbb = this.playerEntity.getEntityBoundingBox().expand(f3, f3, f3).addCoord(0.0D, -0.55D, 0.0D);
/*      */           
/*  380 */           if (!this.serverController.isFlightAllowed() && !this.playerEntity.capabilities.allowFlying && !worldserver.checkBlockCollision(axisalignedbb)) {
/*      */             
/*  382 */             if (d12 >= -0.03125D) {
/*      */               
/*  384 */               this.floatingTickCount++;
/*      */               
/*  386 */               if (this.floatingTickCount > 80) {
/*      */                 
/*  388 */                 logger.warn(this.playerEntity.getName() + " was kicked for floating too long!");
/*  389 */                 kickPlayerFromServer("Flying is not enabled on this server");
/*      */ 
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } 
/*      */           } else {
/*  396 */             this.floatingTickCount = 0;
/*      */           } 
/*      */           
/*  399 */           this.playerEntity.onGround = packetIn.isOnGround();
/*  400 */           this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
/*  401 */           this.playerEntity.handleFalling(this.playerEntity.posY - d7, packetIn.isOnGround());
/*      */         }
/*  403 */         else if (this.networkTickCount - this.field_175090_f > 20) {
/*      */           
/*  405 */           setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPlayerLocation(double x, double y, double z, float yaw, float pitch) {
/*  413 */     setPlayerLocation(x, y, z, yaw, pitch, Collections.emptySet());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<S08PacketPlayerPosLook.EnumFlags> relativeSet) {
/*  418 */     this.hasMoved = false;
/*  419 */     this.lastPosX = x;
/*  420 */     this.lastPosY = y;
/*  421 */     this.lastPosZ = z;
/*      */     
/*  423 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.X))
/*      */     {
/*  425 */       this.lastPosX += this.playerEntity.posX;
/*      */     }
/*      */     
/*  428 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.Y))
/*      */     {
/*  430 */       this.lastPosY += this.playerEntity.posY;
/*      */     }
/*      */     
/*  433 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.Z))
/*      */     {
/*  435 */       this.lastPosZ += this.playerEntity.posZ;
/*      */     }
/*      */     
/*  438 */     float f = yaw;
/*  439 */     float f1 = pitch;
/*      */     
/*  441 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT))
/*      */     {
/*  443 */       f = yaw + this.playerEntity.rotationYaw;
/*      */     }
/*      */     
/*  446 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.X_ROT))
/*      */     {
/*  448 */       f1 = pitch + this.playerEntity.rotationPitch;
/*      */     }
/*      */     
/*  451 */     this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f, f1);
/*  452 */     this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S08PacketPlayerPosLook(x, y, z, yaw, pitch, relativeSet));
/*      */   }
/*      */   
/*      */   public void processPlayerDigging(C07PacketPlayerDigging packetIn) {
/*      */     double d0, d1, d2, d3;
/*  457 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  458 */     WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/*  459 */     BlockPos blockpos = packetIn.getPosition();
/*  460 */     this.playerEntity.markPlayerActive();
/*      */     
/*  462 */     switch (packetIn.getStatus()) {
/*      */       
/*      */       case PERFORM_RESPAWN:
/*  465 */         if (!this.playerEntity.isSpectator())
/*      */         {
/*  467 */           this.playerEntity.dropOneItem(false);
/*      */         }
/*      */         return;
/*      */ 
/*      */       
/*      */       case REQUEST_STATS:
/*  473 */         if (!this.playerEntity.isSpectator())
/*      */         {
/*  475 */           this.playerEntity.dropOneItem(true);
/*      */         }
/*      */         return;
/*      */ 
/*      */       
/*      */       case OPEN_INVENTORY_ACHIEVEMENT:
/*  481 */         this.playerEntity.stopUsingItem();
/*      */         return;
/*      */       
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*  487 */         d0 = this.playerEntity.posX - blockpos.getX() + 0.5D;
/*  488 */         d1 = this.playerEntity.posY - blockpos.getY() + 0.5D + 1.5D;
/*  489 */         d2 = this.playerEntity.posZ - blockpos.getZ() + 0.5D;
/*  490 */         d3 = d0 * d0 + d1 * d1 + d2 * d2;
/*      */         
/*  492 */         if (d3 > 36.0D) {
/*      */           return;
/*      */         }
/*      */         
/*  496 */         if (blockpos.getY() >= this.serverController.getBuildLimit()) {
/*      */           return;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  502 */         if (packetIn.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
/*      */           
/*  504 */           if (!this.serverController.isBlockProtected((World)worldserver, blockpos, (EntityPlayer)this.playerEntity) && worldserver.getWorldBorder().contains(blockpos))
/*      */           {
/*  506 */             this.playerEntity.theItemInWorldManager.onBlockClicked(blockpos, packetIn.getFacing());
/*      */           }
/*      */           else
/*      */           {
/*  510 */             this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S23PacketBlockChange((World)worldserver, blockpos));
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  515 */           if (packetIn.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
/*      */             
/*  517 */             this.playerEntity.theItemInWorldManager.blockRemoving(blockpos);
/*      */           }
/*  519 */           else if (packetIn.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
/*      */             
/*  521 */             this.playerEntity.theItemInWorldManager.cancelDestroyingBlock();
/*      */           } 
/*      */           
/*  524 */           if (worldserver.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
/*      */           {
/*  526 */             this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S23PacketBlockChange((World)worldserver, blockpos));
/*      */           }
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  534 */     throw new IllegalArgumentException("Invalid player action");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement packetIn) {
/*  540 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  541 */     WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/*  542 */     ItemStack itemstack = this.playerEntity.inventory.getCurrentItem();
/*  543 */     boolean flag = false;
/*  544 */     BlockPos blockpos = packetIn.getPosition();
/*  545 */     EnumFacing enumfacing = EnumFacing.getFront(packetIn.getPlacedBlockDirection());
/*  546 */     this.playerEntity.markPlayerActive();
/*      */     
/*  548 */     if (packetIn.getPlacedBlockDirection() == 255) {
/*      */       
/*  550 */       if (itemstack == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  555 */       this.playerEntity.theItemInWorldManager.tryUseItem((EntityPlayer)this.playerEntity, (World)worldserver, itemstack);
/*      */     }
/*  557 */     else if (blockpos.getY() < this.serverController.getBuildLimit() - 1 || (enumfacing != EnumFacing.UP && blockpos.getY() < this.serverController.getBuildLimit())) {
/*      */       
/*  559 */       if (this.hasMoved && this.playerEntity.getDistanceSq(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D) < 64.0D && !this.serverController.isBlockProtected((World)worldserver, blockpos, (EntityPlayer)this.playerEntity) && worldserver.getWorldBorder().contains(blockpos))
/*      */       {
/*  561 */         this.playerEntity.theItemInWorldManager.activateBlockOrUseItem((EntityPlayer)this.playerEntity, (World)worldserver, itemstack, blockpos, enumfacing, packetIn.getPlacedBlockOffsetX(), packetIn.getPlacedBlockOffsetY(), packetIn.getPlacedBlockOffsetZ());
/*      */       }
/*      */       
/*  564 */       flag = true;
/*      */     }
/*      */     else {
/*      */       
/*  568 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("build.tooHigh", new Object[] { Integer.valueOf(this.serverController.getBuildLimit()) });
/*  569 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/*  570 */       this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S02PacketChat((IChatComponent)chatcomponenttranslation));
/*  571 */       flag = true;
/*      */     } 
/*      */     
/*  574 */     if (flag) {
/*      */       
/*  576 */       this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S23PacketBlockChange((World)worldserver, blockpos));
/*  577 */       this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S23PacketBlockChange((World)worldserver, blockpos.offset(enumfacing)));
/*      */     } 
/*      */     
/*  580 */     itemstack = this.playerEntity.inventory.getCurrentItem();
/*      */     
/*  582 */     if (itemstack != null && itemstack.stackSize == 0) {
/*      */       
/*  584 */       this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
/*  585 */       itemstack = null;
/*      */     } 
/*      */     
/*  588 */     if (itemstack == null || itemstack.getMaxItemUseDuration() == 0) {
/*      */       
/*  590 */       this.playerEntity.isChangingQuantityOnly = true;
/*  591 */       this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.copyItemStack(this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]);
/*  592 */       Slot slot = this.playerEntity.openContainer.getSlotFromInventory((IInventory)this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
/*  593 */       this.playerEntity.openContainer.detectAndSendChanges();
/*  594 */       this.playerEntity.isChangingQuantityOnly = false;
/*      */       
/*  596 */       if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), packetIn.getStack()))
/*      */       {
/*  598 */         sendPacket((Packet)new S2FPacketSetSlot(this.playerEntity.openContainer.windowId, slot.slotNumber, this.playerEntity.inventory.getCurrentItem()));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSpectate(C18PacketSpectate packetIn) {
/*  605 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*      */     
/*  607 */     if (this.playerEntity.isSpectator()) {
/*      */       
/*  609 */       Entity entity = null;
/*      */       
/*  611 */       for (WorldServer worldserver : this.serverController.worldServers) {
/*      */         
/*  613 */         if (worldserver != null) {
/*      */           
/*  615 */           entity = packetIn.getEntity(worldserver);
/*      */           
/*  617 */           if (entity != null) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  624 */       if (entity != null) {
/*      */         
/*  626 */         this.playerEntity.setSpectatingEntity((Entity)this.playerEntity);
/*  627 */         this.playerEntity.mountEntity((Entity)null);
/*      */         
/*  629 */         if (entity.worldObj != this.playerEntity.worldObj) {
/*      */           
/*  631 */           WorldServer worldserver1 = this.playerEntity.getServerForPlayer();
/*  632 */           WorldServer worldserver2 = (WorldServer)entity.worldObj;
/*  633 */           this.playerEntity.dimension = entity.dimension;
/*  634 */           sendPacket((Packet)new S07PacketRespawn(this.playerEntity.dimension, worldserver1.getDifficulty(), worldserver1.getWorldInfo().getTerrainType(), this.playerEntity.theItemInWorldManager.getGameType()));
/*  635 */           worldserver1.removePlayerEntityDangerously((Entity)this.playerEntity);
/*  636 */           this.playerEntity.isDead = false;
/*  637 */           this.playerEntity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
/*      */           
/*  639 */           if (this.playerEntity.isEntityAlive()) {
/*      */             
/*  641 */             worldserver1.updateEntityWithOptionalForce((Entity)this.playerEntity, false);
/*  642 */             worldserver2.spawnEntityInWorld((Entity)this.playerEntity);
/*  643 */             worldserver2.updateEntityWithOptionalForce((Entity)this.playerEntity, false);
/*      */           } 
/*      */           
/*  646 */           this.playerEntity.setWorld((World)worldserver2);
/*  647 */           this.serverController.getConfigurationManager().preparePlayer(this.playerEntity, worldserver1);
/*  648 */           this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
/*  649 */           this.playerEntity.theItemInWorldManager.setWorld(worldserver2);
/*  650 */           this.serverController.getConfigurationManager().updateTimeAndWeatherForPlayer(this.playerEntity, worldserver2);
/*  651 */           this.serverController.getConfigurationManager().syncPlayerInventory(this.playerEntity);
/*      */         }
/*      */         else {
/*      */           
/*  655 */           this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleResourcePackStatus(C19PacketResourcePackStatus packetIn) {}
/*      */ 
/*      */   
/*      */   public void onDisconnect(IChatComponent reason) {
/*  667 */     logger.info(this.playerEntity.getName() + " lost connection: " + reason);
/*  668 */     this.serverController.refreshStatusNextTick();
/*  669 */     ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.left", new Object[] { this.playerEntity.getDisplayName() });
/*  670 */     chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
/*  671 */     this.serverController.getConfigurationManager().sendChatMsg((IChatComponent)chatcomponenttranslation);
/*  672 */     this.playerEntity.mountEntityAndWakeUp();
/*  673 */     this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);
/*      */     
/*  675 */     if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
/*      */       
/*  677 */       logger.info("Stopping singleplayer server as player logged out");
/*  678 */       this.serverController.initiateShutdown();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendPacket(final Packet packetIn) {
/*  684 */     if (packetIn instanceof S02PacketChat) {
/*      */       
/*  686 */       S02PacketChat s02packetchat = (S02PacketChat)packetIn;
/*  687 */       EntityPlayer.EnumChatVisibility entityplayer$enumchatvisibility = this.playerEntity.getChatVisibility();
/*      */       
/*  689 */       if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  694 */       if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM && !s02packetchat.isChat()) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  702 */       this.netManager.sendPacket(packetIn);
/*      */     }
/*  704 */     catch (Throwable throwable) {
/*      */       
/*  706 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
/*  707 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Packet being sent");
/*  708 */       crashreportcategory.addCrashSectionCallable("Packet class", new Callable<String>()
/*      */           {
/*      */             public String call() throws Exception
/*      */             {
/*  712 */               return packetIn.getClass().getCanonicalName();
/*      */             }
/*      */           });
/*  715 */       throw new ReportedException(crashreport);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void processHeldItemChange(C09PacketHeldItemChange packetIn) {
/*  721 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*      */     
/*  723 */     if (packetIn.getSlotId() >= 0 && packetIn.getSlotId() < InventoryPlayer.getHotbarSize()) {
/*      */       
/*  725 */       this.playerEntity.inventory.currentItem = packetIn.getSlotId();
/*  726 */       this.playerEntity.markPlayerActive();
/*      */     }
/*      */     else {
/*      */       
/*  730 */       logger.warn(this.playerEntity.getName() + " tried to set an invalid carried item");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void processChatMessage(C01PacketChatMessage packetIn) {
/*  736 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*      */     
/*  738 */     if (this.playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
/*      */       
/*  740 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
/*  741 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/*  742 */       sendPacket((Packet)new S02PacketChat((IChatComponent)chatcomponenttranslation));
/*      */     }
/*      */     else {
/*      */       
/*  746 */       this.playerEntity.markPlayerActive();
/*  747 */       String s = packetIn.getMessage();
/*  748 */       s = StringUtils.normalizeSpace(s);
/*      */       
/*  750 */       for (int i = 0; i < s.length(); i++) {
/*      */         
/*  752 */         if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i))) {
/*      */           
/*  754 */           kickPlayerFromServer("Illegal characters in chat");
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*  759 */       if (s.startsWith("/")) {
/*      */         
/*  761 */         handleSlashCommand(s);
/*      */       }
/*      */       else {
/*      */         
/*  765 */         ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("chat.type.text", new Object[] { this.playerEntity.getDisplayName(), s });
/*  766 */         this.serverController.getConfigurationManager().sendChatMsgImpl((IChatComponent)chatComponentTranslation, false);
/*      */       } 
/*      */       
/*  769 */       this.chatSpamThresholdCount += 20;
/*      */       
/*  771 */       if (this.chatSpamThresholdCount > 200 && !this.serverController.getConfigurationManager().canSendCommands(this.playerEntity.getGameProfile()))
/*      */       {
/*  773 */         kickPlayerFromServer("disconnect.spam");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void handleSlashCommand(String command) {
/*  780 */     this.serverController.getCommandManager().executeCommand((ICommandSender)this.playerEntity, command);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAnimation(C0APacketAnimation packetIn) {
/*  785 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  786 */     this.playerEntity.markPlayerActive();
/*  787 */     this.playerEntity.swingItem();
/*      */   }
/*      */ 
/*      */   
/*      */   public void processEntityAction(C0BPacketEntityAction packetIn) {
/*  792 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  793 */     this.playerEntity.markPlayerActive();
/*      */     
/*  795 */     switch (packetIn.getAction()) {
/*      */       
/*      */       case PERFORM_RESPAWN:
/*  798 */         this.playerEntity.setSneaking(true);
/*      */         return;
/*      */       
/*      */       case REQUEST_STATS:
/*  802 */         this.playerEntity.setSneaking(false);
/*      */         return;
/*      */       
/*      */       case OPEN_INVENTORY_ACHIEVEMENT:
/*  806 */         this.playerEntity.setSprinting(true);
/*      */         return;
/*      */       
/*      */       case null:
/*  810 */         this.playerEntity.setSprinting(false);
/*      */         return;
/*      */       
/*      */       case null:
/*  814 */         this.playerEntity.wakeUpPlayer(false, true, true);
/*  815 */         this.hasMoved = false;
/*      */         return;
/*      */       
/*      */       case null:
/*  819 */         if (this.playerEntity.ridingEntity instanceof EntityHorse)
/*      */         {
/*  821 */           ((EntityHorse)this.playerEntity.ridingEntity).setJumpPower(packetIn.getAuxData());
/*      */         }
/*      */         return;
/*      */ 
/*      */       
/*      */       case null:
/*  827 */         if (this.playerEntity.ridingEntity instanceof EntityHorse)
/*      */         {
/*  829 */           ((EntityHorse)this.playerEntity.ridingEntity).openGUI((EntityPlayer)this.playerEntity);
/*      */         }
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  835 */     throw new IllegalArgumentException("Invalid client command!");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void processUseEntity(C02PacketUseEntity packetIn) {
/*  841 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  842 */     WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/*  843 */     Entity entity = packetIn.getEntityFromWorld((World)worldserver);
/*  844 */     this.playerEntity.markPlayerActive();
/*      */     
/*  846 */     if (entity != null) {
/*      */       
/*  848 */       boolean flag = this.playerEntity.canEntityBeSeen(entity);
/*  849 */       double d0 = 36.0D;
/*      */       
/*  851 */       if (!flag)
/*      */       {
/*  853 */         d0 = 9.0D;
/*      */       }
/*      */       
/*  856 */       if (this.playerEntity.getDistanceSqToEntity(entity) < d0)
/*      */       {
/*  858 */         if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT) {
/*      */           
/*  860 */           this.playerEntity.interactWith(entity);
/*      */         }
/*  862 */         else if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT_AT) {
/*      */           
/*  864 */           entity.interactAt((EntityPlayer)this.playerEntity, packetIn.getHitVec());
/*      */         }
/*  866 */         else if (packetIn.getAction() == C02PacketUseEntity.Action.ATTACK) {
/*      */           
/*  868 */           if (entity instanceof EntityItem || entity instanceof net.minecraft.entity.item.EntityXPOrb || entity instanceof net.minecraft.entity.projectile.EntityArrow || entity == this.playerEntity) {
/*      */             
/*  870 */             kickPlayerFromServer("Attempting to attack an invalid entity");
/*  871 */             this.serverController.logWarning("Player " + this.playerEntity.getName() + " tried to attack an invalid entity");
/*      */             
/*      */             return;
/*      */           } 
/*  875 */           this.playerEntity.attackTargetEntityWithCurrentItem(entity);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void processClientStatus(C16PacketClientStatus packetIn) {
/*  883 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  884 */     this.playerEntity.markPlayerActive();
/*  885 */     C16PacketClientStatus.EnumState c16packetclientstatus$enumstate = packetIn.getStatus();
/*      */     
/*  887 */     switch (c16packetclientstatus$enumstate) {
/*      */       
/*      */       case PERFORM_RESPAWN:
/*  890 */         if (this.playerEntity.playerConqueredTheEnd) {
/*      */           
/*  892 */           this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, true); break;
/*      */         } 
/*  894 */         if (this.playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled()) {
/*      */           
/*  896 */           if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
/*      */             
/*  898 */             this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
/*  899 */             this.serverController.deleteWorldAndStopServer();
/*      */             
/*      */             break;
/*      */           } 
/*  903 */           UserListBansEntry userlistbansentry = new UserListBansEntry(this.playerEntity.getGameProfile(), (Date)null, "(You just lost the game)", (Date)null, "Death in Hardcore");
/*  904 */           this.serverController.getConfigurationManager().getBannedPlayers().addEntry((UserListEntry)userlistbansentry);
/*  905 */           this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/*  910 */         if (this.playerEntity.getHealth() > 0.0F) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*  915 */         this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, false);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case REQUEST_STATS:
/*  921 */         this.playerEntity.getStatFile().func_150876_a(this.playerEntity);
/*      */         break;
/*      */       
/*      */       case OPEN_INVENTORY_ACHIEVEMENT:
/*  925 */         this.playerEntity.triggerAchievement((StatBase)AchievementList.openInventory);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void processCloseWindow(C0DPacketCloseWindow packetIn) {
/*  931 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  932 */     this.playerEntity.closeContainer();
/*      */   }
/*      */ 
/*      */   
/*      */   public void processClickWindow(C0EPacketClickWindow packetIn) {
/*  937 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  938 */     this.playerEntity.markPlayerActive();
/*      */     
/*  940 */     if (this.playerEntity.openContainer.windowId == packetIn.getWindowId() && this.playerEntity.openContainer.getCanCraft((EntityPlayer)this.playerEntity))
/*      */     {
/*  942 */       if (this.playerEntity.isSpectator()) {
/*      */         
/*  944 */         List<ItemStack> list = Lists.newArrayList();
/*      */         
/*  946 */         for (int i = 0; i < this.playerEntity.openContainer.inventorySlots.size(); i++)
/*      */         {
/*  948 */           list.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(i)).getStack());
/*      */         }
/*      */         
/*  951 */         this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, list);
/*      */       }
/*      */       else {
/*      */         
/*  955 */         ItemStack itemstack = this.playerEntity.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getMode(), (EntityPlayer)this.playerEntity);
/*      */         
/*  957 */         if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), itemstack)) {
/*      */           
/*  959 */           this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
/*  960 */           this.playerEntity.isChangingQuantityOnly = true;
/*  961 */           this.playerEntity.openContainer.detectAndSendChanges();
/*  962 */           this.playerEntity.updateHeldItem();
/*  963 */           this.playerEntity.isChangingQuantityOnly = false;
/*      */         }
/*      */         else {
/*      */           
/*  967 */           this.field_147372_n.addKey(this.playerEntity.openContainer.windowId, Short.valueOf(packetIn.getActionNumber()));
/*  968 */           this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
/*  969 */           this.playerEntity.openContainer.setCanCraft((EntityPlayer)this.playerEntity, false);
/*  970 */           List<ItemStack> list1 = Lists.newArrayList();
/*      */           
/*  972 */           for (int j = 0; j < this.playerEntity.openContainer.inventorySlots.size(); j++)
/*      */           {
/*  974 */             list1.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(j)).getStack());
/*      */           }
/*      */           
/*  977 */           this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, list1);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void processEnchantItem(C11PacketEnchantItem packetIn) {
/*  985 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*  986 */     this.playerEntity.markPlayerActive();
/*      */     
/*  988 */     if (this.playerEntity.openContainer.windowId == packetIn.getWindowId() && this.playerEntity.openContainer.getCanCraft((EntityPlayer)this.playerEntity) && !this.playerEntity.isSpectator()) {
/*      */       
/*  990 */       this.playerEntity.openContainer.enchantItem((EntityPlayer)this.playerEntity, packetIn.getButton());
/*  991 */       this.playerEntity.openContainer.detectAndSendChanges();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void processCreativeInventoryAction(C10PacketCreativeInventoryAction packetIn) {
/*  997 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*      */     
/*  999 */     if (this.playerEntity.theItemInWorldManager.isCreative()) {
/*      */       
/* 1001 */       boolean flag = (packetIn.getSlotId() < 0);
/* 1002 */       ItemStack itemstack = packetIn.getStack();
/*      */       
/* 1004 */       if (itemstack != null && itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("BlockEntityTag", 10)) {
/*      */         
/* 1006 */         NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("BlockEntityTag");
/*      */         
/* 1008 */         if (nbttagcompound.hasKey("x") && nbttagcompound.hasKey("y") && nbttagcompound.hasKey("z")) {
/*      */           
/* 1010 */           BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
/* 1011 */           TileEntity tileentity = this.playerEntity.worldObj.getTileEntity(blockpos);
/*      */           
/* 1013 */           if (tileentity != null) {
/*      */             
/* 1015 */             NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 1016 */             tileentity.writeToNBT(nbttagcompound1);
/* 1017 */             nbttagcompound1.removeTag("x");
/* 1018 */             nbttagcompound1.removeTag("y");
/* 1019 */             nbttagcompound1.removeTag("z");
/* 1020 */             itemstack.setTagInfo("BlockEntityTag", (NBTBase)nbttagcompound1);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1025 */       boolean flag1 = (packetIn.getSlotId() >= 1 && packetIn.getSlotId() < 36 + InventoryPlayer.getHotbarSize());
/* 1026 */       boolean flag2 = (itemstack == null || itemstack.getItem() != null);
/* 1027 */       boolean flag3 = (itemstack == null || (itemstack.getMetadata() >= 0 && itemstack.stackSize <= 64 && itemstack.stackSize > 0));
/*      */       
/* 1029 */       if (flag1 && flag2 && flag3) {
/*      */         
/* 1031 */         if (itemstack == null) {
/*      */           
/* 1033 */           this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), (ItemStack)null);
/*      */         }
/*      */         else {
/*      */           
/* 1037 */           this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), itemstack);
/*      */         } 
/*      */         
/* 1040 */         this.playerEntity.inventoryContainer.setCanCraft((EntityPlayer)this.playerEntity, true);
/*      */       }
/* 1042 */       else if (flag && flag2 && flag3 && this.itemDropThreshold < 200) {
/*      */         
/* 1044 */         this.itemDropThreshold += 20;
/* 1045 */         EntityItem entityitem = this.playerEntity.dropPlayerItemWithRandomChoice(itemstack, true);
/*      */         
/* 1047 */         if (entityitem != null)
/*      */         {
/* 1049 */           entityitem.setAgeToCreativeDespawnTime();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void processConfirmTransaction(C0FPacketConfirmTransaction packetIn) {
/* 1057 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/* 1058 */     Short oshort = (Short)this.field_147372_n.lookup(this.playerEntity.openContainer.windowId);
/*      */     
/* 1060 */     if (oshort != null && packetIn.getUid() == oshort.shortValue() && this.playerEntity.openContainer.windowId == packetIn.getWindowId() && !this.playerEntity.openContainer.getCanCraft((EntityPlayer)this.playerEntity) && !this.playerEntity.isSpectator())
/*      */     {
/* 1062 */       this.playerEntity.openContainer.setCanCraft((EntityPlayer)this.playerEntity, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void processUpdateSign(C12PacketUpdateSign packetIn) {
/* 1068 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/* 1069 */     this.playerEntity.markPlayerActive();
/* 1070 */     WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/* 1071 */     BlockPos blockpos = packetIn.getPosition();
/*      */     
/* 1073 */     if (worldserver.isBlockLoaded(blockpos)) {
/*      */       
/* 1075 */       TileEntity tileentity = worldserver.getTileEntity(blockpos);
/*      */       
/* 1077 */       if (!(tileentity instanceof TileEntitySign)) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 1082 */       TileEntitySign tileentitysign = (TileEntitySign)tileentity;
/*      */       
/* 1084 */       if (!tileentitysign.getIsEditable() || tileentitysign.getPlayer() != this.playerEntity) {
/*      */         
/* 1086 */         this.serverController.logWarning("Player " + this.playerEntity.getName() + " just tried to change non-editable sign");
/*      */         
/*      */         return;
/*      */       } 
/* 1090 */       IChatComponent[] aichatcomponent = packetIn.getLines();
/*      */       
/* 1092 */       for (int i = 0; i < aichatcomponent.length; i++)
/*      */       {
/* 1094 */         tileentitysign.signText[i] = (IChatComponent)new ChatComponentText(EnumChatFormatting.getTextWithoutFormattingCodes(aichatcomponent[i].getUnformattedText()));
/*      */       }
/*      */       
/* 1097 */       tileentitysign.markDirty();
/* 1098 */       worldserver.markBlockForUpdate(blockpos);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void processKeepAlive(C00PacketKeepAlive packetIn) {
/* 1104 */     if (packetIn.getKey() == this.field_147378_h) {
/*      */       
/* 1106 */       int i = (int)(currentTimeMillis() - this.lastPingTime);
/* 1107 */       this.playerEntity.ping = (this.playerEntity.ping * 3 + i) / 4;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private long currentTimeMillis() {
/* 1113 */     return System.nanoTime() / 1000000L;
/*      */   }
/*      */ 
/*      */   
/*      */   public void processPlayerAbilities(C13PacketPlayerAbilities packetIn) {
/* 1118 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/* 1119 */     this.playerEntity.capabilities.isFlying = (packetIn.isFlying() && this.playerEntity.capabilities.allowFlying);
/*      */   }
/*      */ 
/*      */   
/*      */   public void processTabComplete(C14PacketTabComplete packetIn) {
/* 1124 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/* 1125 */     List<String> list = Lists.newArrayList();
/*      */     
/* 1127 */     for (String s : this.serverController.getTabCompletions((ICommandSender)this.playerEntity, packetIn.getMessage(), packetIn.getTargetBlock()))
/*      */     {
/* 1129 */       list.add(s);
/*      */     }
/*      */     
/* 1132 */     this.playerEntity.playerNetServerHandler.sendPacket((Packet)new S3APacketTabComplete(list.<String>toArray(new String[list.size()])));
/*      */   }
/*      */ 
/*      */   
/*      */   public void processClientSettings(C15PacketClientSettings packetIn) {
/* 1137 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/* 1138 */     this.playerEntity.handleClientSettings(packetIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public void processVanilla250Packet(C17PacketCustomPayload packetIn) {
/* 1143 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/*      */     
/* 1145 */     if ("MC|BEdit".equals(packetIn.getChannelName())) {
/*      */       
/* 1147 */       PacketBuffer packetbuffer3 = new PacketBuffer(Unpooled.wrappedBuffer(packetIn.getBufferData()));
/*      */ 
/*      */       
/*      */       try {
/* 1151 */         ItemStack itemstack1 = packetbuffer3.readItemStackFromBuffer();
/*      */         
/* 1153 */         if (itemstack1 != null) {
/*      */           
/* 1155 */           if (!ItemWritableBook.isNBTValid(itemstack1.getTagCompound()))
/*      */           {
/* 1157 */             throw new IOException("Invalid book tag!");
/*      */           }
/*      */           
/* 1160 */           ItemStack itemstack3 = this.playerEntity.inventory.getCurrentItem();
/*      */           
/* 1162 */           if (itemstack3 == null) {
/*      */             return;
/*      */           }
/*      */ 
/*      */           
/* 1167 */           if (itemstack1.getItem() == Items.writable_book && itemstack1.getItem() == itemstack3.getItem())
/*      */           {
/* 1169 */             itemstack3.setTagInfo("pages", (NBTBase)itemstack1.getTagCompound().getTagList("pages", 8));
/*      */           }
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/* 1175 */       } catch (Exception exception3) {
/*      */         
/* 1177 */         logger.error("Couldn't handle book info", exception3);
/*      */ 
/*      */         
/*      */         return;
/*      */       } finally {
/* 1182 */         packetbuffer3.release();
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 1187 */     if ("MC|BSign".equals(packetIn.getChannelName())) {
/*      */       
/* 1189 */       PacketBuffer packetbuffer2 = new PacketBuffer(Unpooled.wrappedBuffer(packetIn.getBufferData()));
/*      */ 
/*      */       
/*      */       try {
/* 1193 */         ItemStack itemstack = packetbuffer2.readItemStackFromBuffer();
/*      */         
/* 1195 */         if (itemstack != null) {
/*      */           
/* 1197 */           if (!ItemEditableBook.validBookTagContents(itemstack.getTagCompound()))
/*      */           {
/* 1199 */             throw new IOException("Invalid book tag!");
/*      */           }
/*      */           
/* 1202 */           ItemStack itemstack2 = this.playerEntity.inventory.getCurrentItem();
/*      */           
/* 1204 */           if (itemstack2 == null) {
/*      */             return;
/*      */           }
/*      */ 
/*      */           
/* 1209 */           if (itemstack.getItem() == Items.written_book && itemstack2.getItem() == Items.writable_book) {
/*      */             
/* 1211 */             itemstack2.setTagInfo("author", (NBTBase)new NBTTagString(this.playerEntity.getName()));
/* 1212 */             itemstack2.setTagInfo("title", (NBTBase)new NBTTagString(itemstack.getTagCompound().getString("title")));
/* 1213 */             itemstack2.setTagInfo("pages", (NBTBase)itemstack.getTagCompound().getTagList("pages", 8));
/* 1214 */             itemstack2.setItem(Items.written_book);
/*      */           } 
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/* 1220 */       } catch (Exception exception4) {
/*      */         
/* 1222 */         logger.error("Couldn't sign book", exception4);
/*      */ 
/*      */         
/*      */         return;
/*      */       } finally {
/* 1227 */         packetbuffer2.release();
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 1232 */     if ("MC|TrSel".equals(packetIn.getChannelName())) {
/*      */       
/*      */       try
/*      */       {
/* 1236 */         int i = packetIn.getBufferData().readInt();
/* 1237 */         Container container = this.playerEntity.openContainer;
/*      */         
/* 1239 */         if (container instanceof ContainerMerchant)
/*      */         {
/* 1241 */           ((ContainerMerchant)container).setCurrentRecipeIndex(i);
/*      */         }
/*      */       }
/* 1244 */       catch (Exception exception2)
/*      */       {
/* 1246 */         logger.error("Couldn't select trade", exception2);
/*      */       }
/*      */     
/* 1249 */     } else if ("MC|AdvCdm".equals(packetIn.getChannelName())) {
/*      */       
/* 1251 */       if (!this.serverController.isCommandBlockEnabled()) {
/*      */         
/* 1253 */         this.playerEntity.addChatMessage((IChatComponent)new ChatComponentTranslation("advMode.notEnabled", new Object[0]));
/*      */       }
/* 1255 */       else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode) {
/*      */         
/* 1257 */         PacketBuffer packetbuffer = packetIn.getBufferData();
/*      */ 
/*      */         
/*      */         try {
/* 1261 */           int j = packetbuffer.readByte();
/* 1262 */           CommandBlockLogic commandblocklogic = null;
/*      */           
/* 1264 */           if (j == 0) {
/*      */             
/* 1266 */             TileEntity tileentity = this.playerEntity.worldObj.getTileEntity(new BlockPos(packetbuffer.readInt(), packetbuffer.readInt(), packetbuffer.readInt()));
/*      */             
/* 1268 */             if (tileentity instanceof TileEntityCommandBlock)
/*      */             {
/* 1270 */               commandblocklogic = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
/*      */             }
/*      */           }
/* 1273 */           else if (j == 1) {
/*      */             
/* 1275 */             Entity entity = this.playerEntity.worldObj.getEntityByID(packetbuffer.readInt());
/*      */             
/* 1277 */             if (entity instanceof EntityMinecartCommandBlock)
/*      */             {
/* 1279 */               commandblocklogic = ((EntityMinecartCommandBlock)entity).getCommandBlockLogic();
/*      */             }
/*      */           } 
/*      */           
/* 1283 */           String s1 = packetbuffer.readStringFromBuffer(packetbuffer.readableBytes());
/* 1284 */           boolean flag = packetbuffer.readBoolean();
/*      */           
/* 1286 */           if (commandblocklogic != null)
/*      */           {
/* 1288 */             commandblocklogic.setCommand(s1);
/* 1289 */             commandblocklogic.setTrackOutput(flag);
/*      */             
/* 1291 */             if (!flag)
/*      */             {
/* 1293 */               commandblocklogic.setLastOutput((IChatComponent)null);
/*      */             }
/*      */             
/* 1296 */             commandblocklogic.updateCommand();
/* 1297 */             this.playerEntity.addChatMessage((IChatComponent)new ChatComponentTranslation("advMode.setCommand.success", new Object[] { s1 }));
/*      */           }
/*      */         
/* 1300 */         } catch (Exception exception1) {
/*      */           
/* 1302 */           logger.error("Couldn't set command block", exception1);
/*      */         }
/*      */         finally {
/*      */           
/* 1306 */           packetbuffer.release();
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1311 */         this.playerEntity.addChatMessage((IChatComponent)new ChatComponentTranslation("advMode.notAllowed", new Object[0]));
/*      */       }
/*      */     
/* 1314 */     } else if ("MC|Beacon".equals(packetIn.getChannelName())) {
/*      */       
/* 1316 */       if (this.playerEntity.openContainer instanceof ContainerBeacon) {
/*      */         
/*      */         try {
/*      */           
/* 1320 */           PacketBuffer packetbuffer1 = packetIn.getBufferData();
/* 1321 */           int k = packetbuffer1.readInt();
/* 1322 */           int l = packetbuffer1.readInt();
/* 1323 */           ContainerBeacon containerbeacon = (ContainerBeacon)this.playerEntity.openContainer;
/* 1324 */           Slot slot = containerbeacon.getSlot(0);
/*      */           
/* 1326 */           if (slot.getHasStack())
/*      */           {
/* 1328 */             slot.decrStackSize(1);
/* 1329 */             IInventory iinventory = containerbeacon.func_180611_e();
/* 1330 */             iinventory.setField(1, k);
/* 1331 */             iinventory.setField(2, l);
/* 1332 */             iinventory.markDirty();
/*      */           }
/*      */         
/* 1335 */         } catch (Exception exception) {
/*      */           
/* 1337 */           logger.error("Couldn't set beacon", exception);
/*      */         }
/*      */       
/*      */       }
/* 1341 */     } else if ("MC|ItemName".equals(packetIn.getChannelName()) && this.playerEntity.openContainer instanceof ContainerRepair) {
/*      */       
/* 1343 */       ContainerRepair containerrepair = (ContainerRepair)this.playerEntity.openContainer;
/*      */       
/* 1345 */       if (packetIn.getBufferData() != null && packetIn.getBufferData().readableBytes() >= 1) {
/*      */         
/* 1347 */         String s = ChatAllowedCharacters.filterAllowedCharacters(packetIn.getBufferData().readStringFromBuffer(32767));
/*      */         
/* 1349 */         if (s.length() <= 30)
/*      */         {
/* 1351 */           containerrepair.updateItemName(s);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1356 */         containerrepair.updateItemName("");
/*      */       } 
/*      */     } 
/*      */   }
/*      */   public void handleAnimation(CPacketAnimation packetIn) {
/* 1361 */     PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, (IThreadListener)this.playerEntity.getServerForPlayer());
/* 1362 */     this.playerEntity.markPlayerActive();
/* 1363 */     this.playerEntity.swingItem();
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\NetHandlerPlayServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */