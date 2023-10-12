/*      */ package net.minecraft.client.network;
/*      */ import com.google.common.util.concurrent.FutureCallback;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import java.io.File;
/*      */ import java.util.List;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.multiplayer.ServerData;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.effect.EntityLightningBolt;
/*      */ import net.minecraft.entity.item.EntityFallingBlock;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.network.INetHandler;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.PacketThreadUtil;
/*      */ import net.minecraft.network.play.client.C19PacketResourcePackStatus;
/*      */ import net.minecraft.network.play.server.S01PacketJoinGame;
/*      */ import net.minecraft.network.play.server.S07PacketRespawn;
/*      */ import net.minecraft.network.play.server.S08PacketPlayerPosLook;
/*      */ import net.minecraft.network.play.server.S0BPacketAnimation;
/*      */ import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
/*      */ import net.minecraft.network.play.server.S0EPacketSpawnObject;
/*      */ import net.minecraft.network.play.server.S0FPacketSpawnMob;
/*      */ import net.minecraft.network.play.server.S10PacketSpawnPainting;
/*      */ import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
/*      */ import net.minecraft.network.play.server.S12PacketEntityVelocity;
/*      */ import net.minecraft.network.play.server.S14PacketEntity;
/*      */ import net.minecraft.network.play.server.S18PacketEntityTeleport;
/*      */ import net.minecraft.network.play.server.S1BPacketEntityAttach;
/*      */ import net.minecraft.network.play.server.S1DPacketEntityEffect;
/*      */ import net.minecraft.network.play.server.S20PacketEntityProperties;
/*      */ import net.minecraft.network.play.server.S21PacketChunkData;
/*      */ import net.minecraft.network.play.server.S24PacketBlockAction;
/*      */ import net.minecraft.network.play.server.S26PacketMapChunkBulk;
/*      */ import net.minecraft.network.play.server.S27PacketExplosion;
/*      */ import net.minecraft.network.play.server.S28PacketEffect;
/*      */ import net.minecraft.network.play.server.S29PacketSoundEffect;
/*      */ import net.minecraft.network.play.server.S2APacketParticles;
/*      */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*      */ import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
/*      */ import net.minecraft.network.play.server.S2DPacketOpenWindow;
/*      */ import net.minecraft.network.play.server.S2FPacketSetSlot;
/*      */ import net.minecraft.network.play.server.S30PacketWindowItems;
/*      */ import net.minecraft.network.play.server.S32PacketConfirmTransaction;
/*      */ import net.minecraft.network.play.server.S33PacketUpdateSign;
/*      */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*      */ import net.minecraft.network.play.server.S38PacketPlayerListItem;
/*      */ import net.minecraft.network.play.server.S39PacketPlayerAbilities;
/*      */ import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
/*      */ import net.minecraft.network.play.server.S3CPacketUpdateScore;
/*      */ import net.minecraft.network.play.server.S3EPacketTeams;
/*      */ import net.minecraft.network.play.server.S3FPacketCustomPayload;
/*      */ import net.minecraft.network.play.server.S45PacketTitle;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.stats.StatBase;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.tileentity.TileEntitySign;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.IThreadListener;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ 
/*      */ public class NetHandlerPlayClient implements INetHandlerPlayClient {
/*   74 */   private static final Logger logger = LogManager.getLogger();
/*      */   private final NetworkManager netManager;
/*      */   private final GameProfile profile;
/*      */   private final GuiScreen guiScreenServer;
/*      */   private Minecraft gameController;
/*      */   private WorldClient clientWorldController;
/*      */   private boolean doneLoadingTerrain;
/*   81 */   private final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
/*   82 */   public int currentServerMaxPlayers = 20;
/*      */   private boolean field_147308_k = false;
/*   84 */   private final Random avRandomizer = new Random();
/*      */ 
/*      */   
/*      */   public NetHandlerPlayClient(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager p_i46300_3_, GameProfile p_i46300_4_) {
/*   88 */     this.gameController = mcIn;
/*   89 */     this.guiScreenServer = p_i46300_2_;
/*   90 */     this.netManager = p_i46300_3_;
/*   91 */     this.profile = p_i46300_4_;
/*      */   }
/*      */ 
/*      */   
/*      */   public void cleanup() {
/*   96 */     this.clientWorldController = null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleJoinGame(S01PacketJoinGame packetIn) {
/*  101 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  102 */     this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
/*  103 */     this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), this.gameController.mcProfiler);
/*  104 */     this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
/*  105 */     this.gameController.loadWorld(this.clientWorldController);
/*  106 */     this.gameController.thePlayer.dimension = packetIn.getDimension();
/*  107 */     this.gameController.displayGuiScreen((GuiScreen)new GuiDownloadTerrain(this));
/*  108 */     this.gameController.thePlayer.setEntityId(packetIn.getEntityId());
/*  109 */     this.currentServerMaxPlayers = packetIn.getMaxPlayers();
/*  110 */     this.gameController.thePlayer.setReducedDebug(packetIn.isReducedDebugInfo());
/*  111 */     this.gameController.playerController.setGameType(packetIn.getGameType());
/*  112 */     this.gameController.gameSettings.sendSettingsToServer();
/*  113 */     this.netManager.sendPacket((Packet)new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));
/*      */   }
/*      */   
/*      */   public void handleSpawnObject(S0EPacketSpawnObject packetIn) {
/*      */     EntityFallingBlock entityFallingBlock;
/*  118 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  119 */     double d0 = packetIn.getX() / 32.0D;
/*  120 */     double d1 = packetIn.getY() / 32.0D;
/*  121 */     double d2 = packetIn.getZ() / 32.0D;
/*  122 */     Entity entity = null;
/*      */     
/*  124 */     if (packetIn.getType() == 10) {
/*      */       
/*  126 */       EntityMinecart entityMinecart = EntityMinecart.getMinecart((World)this.clientWorldController, d0, d1, d2, EntityMinecart.EnumMinecartType.byNetworkID(packetIn.func_149009_m()));
/*      */     }
/*  128 */     else if (packetIn.getType() == 90) {
/*      */       
/*  130 */       Entity entity1 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
/*      */       
/*  132 */       if (entity1 instanceof EntityPlayer)
/*      */       {
/*  134 */         EntityFishHook entityFishHook = new EntityFishHook((World)this.clientWorldController, d0, d1, d2, (EntityPlayer)entity1);
/*      */       }
/*      */       
/*  137 */       packetIn.func_149002_g(0);
/*      */     }
/*  139 */     else if (packetIn.getType() == 60) {
/*      */       
/*  141 */       EntityArrow entityArrow = new EntityArrow((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*  143 */     else if (packetIn.getType() == 61) {
/*      */       
/*  145 */       EntitySnowball entitySnowball = new EntitySnowball((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*  147 */     else if (packetIn.getType() == 71) {
/*      */       
/*  149 */       EntityItemFrame entityItemFrame = new EntityItemFrame((World)this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)), EnumFacing.getHorizontal(packetIn.func_149009_m()));
/*  150 */       packetIn.func_149002_g(0);
/*      */     }
/*  152 */     else if (packetIn.getType() == 77) {
/*      */       
/*  154 */       EntityLeashKnot entityLeashKnot = new EntityLeashKnot((World)this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)));
/*  155 */       packetIn.func_149002_g(0);
/*      */     }
/*  157 */     else if (packetIn.getType() == 65) {
/*      */       
/*  159 */       EntityEnderPearl entityEnderPearl = new EntityEnderPearl((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*  161 */     else if (packetIn.getType() == 72) {
/*      */       
/*  163 */       EntityEnderEye entityEnderEye = new EntityEnderEye((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*  165 */     else if (packetIn.getType() == 76) {
/*      */       
/*  167 */       EntityFireworkRocket entityFireworkRocket = new EntityFireworkRocket((World)this.clientWorldController, d0, d1, d2, (ItemStack)null);
/*      */     }
/*  169 */     else if (packetIn.getType() == 63) {
/*      */       
/*  171 */       EntityLargeFireball entityLargeFireball = new EntityLargeFireball((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
/*  172 */       packetIn.func_149002_g(0);
/*      */     }
/*  174 */     else if (packetIn.getType() == 64) {
/*      */       
/*  176 */       EntitySmallFireball entitySmallFireball = new EntitySmallFireball((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
/*  177 */       packetIn.func_149002_g(0);
/*      */     }
/*  179 */     else if (packetIn.getType() == 66) {
/*      */       
/*  181 */       EntityWitherSkull entityWitherSkull = new EntityWitherSkull((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
/*  182 */       packetIn.func_149002_g(0);
/*      */     }
/*  184 */     else if (packetIn.getType() == 62) {
/*      */       
/*  186 */       EntityEgg entityEgg = new EntityEgg((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*  188 */     else if (packetIn.getType() == 73) {
/*      */       
/*  190 */       EntityPotion entityPotion = new EntityPotion((World)this.clientWorldController, d0, d1, d2, packetIn.func_149009_m());
/*  191 */       packetIn.func_149002_g(0);
/*      */     }
/*  193 */     else if (packetIn.getType() == 75) {
/*      */       
/*  195 */       EntityExpBottle entityExpBottle = new EntityExpBottle((World)this.clientWorldController, d0, d1, d2);
/*  196 */       packetIn.func_149002_g(0);
/*      */     }
/*  198 */     else if (packetIn.getType() == 1) {
/*      */       
/*  200 */       EntityBoat entityBoat = new EntityBoat((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*  202 */     else if (packetIn.getType() == 50) {
/*      */       
/*  204 */       EntityTNTPrimed entityTNTPrimed = new EntityTNTPrimed((World)this.clientWorldController, d0, d1, d2, (EntityLivingBase)null);
/*      */     }
/*  206 */     else if (packetIn.getType() == 78) {
/*      */       
/*  208 */       EntityArmorStand entityArmorStand = new EntityArmorStand((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*  210 */     else if (packetIn.getType() == 51) {
/*      */       
/*  212 */       EntityEnderCrystal entityEnderCrystal = new EntityEnderCrystal((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*  214 */     else if (packetIn.getType() == 2) {
/*      */       
/*  216 */       EntityItem entityItem = new EntityItem((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*  218 */     else if (packetIn.getType() == 70) {
/*      */       
/*  220 */       entityFallingBlock = new EntityFallingBlock((World)this.clientWorldController, d0, d1, d2, Block.getStateById(packetIn.func_149009_m() & 0xFFFF));
/*  221 */       packetIn.func_149002_g(0);
/*      */     } 
/*      */     
/*  224 */     if (entityFallingBlock != null) {
/*      */       
/*  226 */       ((Entity)entityFallingBlock).serverPosX = packetIn.getX();
/*  227 */       ((Entity)entityFallingBlock).serverPosY = packetIn.getY();
/*  228 */       ((Entity)entityFallingBlock).serverPosZ = packetIn.getZ();
/*  229 */       ((Entity)entityFallingBlock).rotationPitch = (packetIn.getPitch() * 360) / 256.0F;
/*  230 */       ((Entity)entityFallingBlock).rotationYaw = (packetIn.getYaw() * 360) / 256.0F;
/*  231 */       Entity[] aentity = entityFallingBlock.getParts();
/*      */       
/*  233 */       if (aentity != null) {
/*      */         
/*  235 */         int i = packetIn.getEntityID() - entityFallingBlock.getEntityId();
/*      */         
/*  237 */         for (int j = 0; j < aentity.length; j++)
/*      */         {
/*  239 */           aentity[j].setEntityId(aentity[j].getEntityId() + i);
/*      */         }
/*      */       } 
/*      */       
/*  243 */       entityFallingBlock.setEntityId(packetIn.getEntityID());
/*  244 */       this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityFallingBlock);
/*      */       
/*  246 */       if (packetIn.func_149009_m() > 0) {
/*      */         
/*  248 */         if (packetIn.getType() == 60) {
/*      */           
/*  250 */           Entity entity2 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
/*      */           
/*  252 */           if (entity2 instanceof EntityLivingBase && entityFallingBlock instanceof EntityArrow)
/*      */           {
/*  254 */             ((EntityArrow)entityFallingBlock).shootingEntity = entity2;
/*      */           }
/*      */         } 
/*      */         
/*  258 */         entityFallingBlock.setVelocity(packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packetIn) {
/*  265 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  266 */     EntityXPOrb entityXPOrb = new EntityXPOrb((World)this.clientWorldController, packetIn.getX() / 32.0D, packetIn.getY() / 32.0D, packetIn.getZ() / 32.0D, packetIn.getXPValue());
/*  267 */     ((Entity)entityXPOrb).serverPosX = packetIn.getX();
/*  268 */     ((Entity)entityXPOrb).serverPosY = packetIn.getY();
/*  269 */     ((Entity)entityXPOrb).serverPosZ = packetIn.getZ();
/*  270 */     ((Entity)entityXPOrb).rotationYaw = 0.0F;
/*  271 */     ((Entity)entityXPOrb).rotationPitch = 0.0F;
/*  272 */     entityXPOrb.setEntityId(packetIn.getEntityID());
/*  273 */     this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityXPOrb);
/*      */   }
/*      */   
/*      */   public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packetIn) {
/*      */     EntityLightningBolt entityLightningBolt;
/*  278 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  279 */     double d0 = packetIn.func_149051_d() / 32.0D;
/*  280 */     double d1 = packetIn.func_149050_e() / 32.0D;
/*  281 */     double d2 = packetIn.func_149049_f() / 32.0D;
/*  282 */     Entity entity = null;
/*      */     
/*  284 */     if (packetIn.func_149053_g() == 1)
/*      */     {
/*  286 */       entityLightningBolt = new EntityLightningBolt((World)this.clientWorldController, d0, d1, d2);
/*      */     }
/*      */     
/*  289 */     if (entityLightningBolt != null) {
/*      */       
/*  291 */       ((Entity)entityLightningBolt).serverPosX = packetIn.func_149051_d();
/*  292 */       ((Entity)entityLightningBolt).serverPosY = packetIn.func_149050_e();
/*  293 */       ((Entity)entityLightningBolt).serverPosZ = packetIn.func_149049_f();
/*  294 */       ((Entity)entityLightningBolt).rotationYaw = 0.0F;
/*  295 */       ((Entity)entityLightningBolt).rotationPitch = 0.0F;
/*  296 */       entityLightningBolt.setEntityId(packetIn.func_149052_c());
/*  297 */       this.clientWorldController.addWeatherEffect((Entity)entityLightningBolt);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSpawnPainting(S10PacketSpawnPainting packetIn) {
/*  303 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  304 */     EntityPainting entitypainting = new EntityPainting((World)this.clientWorldController, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
/*  305 */     this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entitypainting);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityVelocity(S12PacketEntityVelocity packetIn) {
/*  310 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  311 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
/*      */     
/*  313 */     if (entity != null)
/*      */     {
/*  315 */       entity.setVelocity(packetIn.getMotionX() / 8000.0D, packetIn.getMotionY() / 8000.0D, packetIn.getMotionZ() / 8000.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityMetadata(S1CPacketEntityMetadata packetIn) {
/*  321 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  322 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/*  324 */     if (entity != null && packetIn.func_149376_c() != null)
/*      */     {
/*  326 */       entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn) {
/*  332 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  333 */     double d0 = packetIn.getX() / 32.0D;
/*  334 */     double d1 = packetIn.getY() / 32.0D;
/*  335 */     double d2 = packetIn.getZ() / 32.0D;
/*  336 */     float f = (packetIn.getYaw() * 360) / 256.0F;
/*  337 */     float f1 = (packetIn.getPitch() * 360) / 256.0F;
/*  338 */     EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP((World)this.gameController.theWorld, getPlayerInfo(packetIn.getPlayer()).getGameProfile());
/*  339 */     entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = (entityotherplayermp.serverPosX = packetIn.getX());
/*  340 */     entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = (entityotherplayermp.serverPosY = packetIn.getY());
/*  341 */     entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = (entityotherplayermp.serverPosZ = packetIn.getZ());
/*  342 */     int i = packetIn.getCurrentItemID();
/*      */     
/*  344 */     if (i == 0) {
/*      */       
/*  346 */       entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
/*      */     }
/*      */     else {
/*      */       
/*  350 */       entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(Item.getItemById(i), 1, 0);
/*      */     } 
/*      */     
/*  353 */     entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
/*  354 */     this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityotherplayermp);
/*  355 */     List<DataWatcher.WatchableObject> list = packetIn.func_148944_c();
/*      */     
/*  357 */     if (list != null)
/*      */     {
/*  359 */       entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityTeleport(S18PacketEntityTeleport packetIn) {
/*  365 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  366 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/*  368 */     if (entity != null) {
/*      */       
/*  370 */       entity.serverPosX = packetIn.getX();
/*  371 */       entity.serverPosY = packetIn.getY();
/*  372 */       entity.serverPosZ = packetIn.getZ();
/*  373 */       double d0 = entity.serverPosX / 32.0D;
/*  374 */       double d1 = entity.serverPosY / 32.0D;
/*  375 */       double d2 = entity.serverPosZ / 32.0D;
/*  376 */       float f = (packetIn.getYaw() * 360) / 256.0F;
/*  377 */       float f1 = (packetIn.getPitch() * 360) / 256.0F;
/*      */       
/*  379 */       if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D) {
/*      */         
/*  381 */         entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
/*      */       }
/*      */       else {
/*      */         
/*  385 */         entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
/*      */       } 
/*      */       
/*  388 */       entity.onGround = packetIn.getOnGround();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleHeldItemChange(S09PacketHeldItemChange packetIn) {
/*  394 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/*  396 */     if (packetIn.getHeldItemHotbarIndex() >= 0 && packetIn.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize())
/*      */     {
/*  398 */       this.gameController.thePlayer.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityMovement(S14PacketEntity packetIn) {
/*  404 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  405 */     Entity entity = packetIn.getEntity((World)this.clientWorldController);
/*      */     
/*  407 */     if (entity != null) {
/*      */       
/*  409 */       entity.serverPosX += packetIn.func_149062_c();
/*  410 */       entity.serverPosY += packetIn.func_149061_d();
/*  411 */       entity.serverPosZ += packetIn.func_149064_e();
/*  412 */       double d0 = entity.serverPosX / 32.0D;
/*  413 */       double d1 = entity.serverPosY / 32.0D;
/*  414 */       double d2 = entity.serverPosZ / 32.0D;
/*  415 */       float f = packetIn.func_149060_h() ? ((packetIn.func_149066_f() * 360) / 256.0F) : entity.rotationYaw;
/*  416 */       float f1 = packetIn.func_149060_h() ? ((packetIn.func_149063_g() * 360) / 256.0F) : entity.rotationPitch;
/*  417 */       entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, false);
/*  418 */       entity.onGround = packetIn.getOnGround();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn) {
/*  424 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  425 */     Entity entity = packetIn.getEntity((World)this.clientWorldController);
/*      */     
/*  427 */     if (entity != null) {
/*      */       
/*  429 */       float f = (packetIn.getYaw() * 360) / 256.0F;
/*  430 */       entity.setRotationYawHead(f);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDestroyEntities(S13PacketDestroyEntities packetIn) {
/*  436 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/*  438 */     for (int i = 0; i < (packetIn.getEntityIDs()).length; i++)
/*      */     {
/*  440 */       this.clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn) {
/*  446 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  447 */     EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
/*  448 */     double d0 = packetIn.getX();
/*  449 */     double d1 = packetIn.getY();
/*  450 */     double d2 = packetIn.getZ();
/*  451 */     float f = packetIn.getYaw();
/*  452 */     float f1 = packetIn.getPitch();
/*      */     
/*  454 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
/*      */       
/*  456 */       d0 += ((EntityPlayer)entityPlayerSP).posX;
/*      */     }
/*      */     else {
/*      */       
/*  460 */       ((EntityPlayer)entityPlayerSP).motionX = 0.0D;
/*      */     } 
/*      */     
/*  463 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
/*      */       
/*  465 */       d1 += ((EntityPlayer)entityPlayerSP).posY;
/*      */     }
/*      */     else {
/*      */       
/*  469 */       ((EntityPlayer)entityPlayerSP).motionY = 0.0D;
/*      */     } 
/*      */     
/*  472 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
/*      */       
/*  474 */       d2 += ((EntityPlayer)entityPlayerSP).posZ;
/*      */     }
/*      */     else {
/*      */       
/*  478 */       ((EntityPlayer)entityPlayerSP).motionZ = 0.0D;
/*      */     } 
/*      */     
/*  481 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT))
/*      */     {
/*  483 */       f1 += ((EntityPlayer)entityPlayerSP).rotationPitch;
/*      */     }
/*      */     
/*  486 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT))
/*      */     {
/*  488 */       f += ((EntityPlayer)entityPlayerSP).rotationYaw;
/*      */     }
/*      */     
/*  491 */     entityPlayerSP.setPositionAndRotation(d0, d1, d2, f, f1);
/*  492 */     this.netManager.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(((EntityPlayer)entityPlayerSP).posX, (entityPlayerSP.getEntityBoundingBox()).minY, ((EntityPlayer)entityPlayerSP).posZ, ((EntityPlayer)entityPlayerSP).rotationYaw, ((EntityPlayer)entityPlayerSP).rotationPitch, false));
/*      */     
/*  494 */     if (!this.doneLoadingTerrain) {
/*      */       
/*  496 */       this.gameController.thePlayer.prevPosX = this.gameController.thePlayer.posX;
/*  497 */       this.gameController.thePlayer.prevPosY = this.gameController.thePlayer.posY;
/*  498 */       this.gameController.thePlayer.prevPosZ = this.gameController.thePlayer.posZ;
/*  499 */       this.doneLoadingTerrain = true;
/*  500 */       this.gameController.displayGuiScreen((GuiScreen)null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn) {
/*  506 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/*  508 */     for (S22PacketMultiBlockChange.BlockUpdateData s22packetmultiblockchange$blockupdatedata : packetIn.getChangedBlocks())
/*      */     {
/*  510 */       this.clientWorldController.invalidateRegionAndSetBlock(s22packetmultiblockchange$blockupdatedata.getPos(), s22packetmultiblockchange$blockupdatedata.getBlockState());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChunkData(S21PacketChunkData packetIn) {
/*  516 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/*  518 */     if (packetIn.func_149274_i()) {
/*      */       
/*  520 */       if (packetIn.getExtractedSize() == 0) {
/*      */         
/*  522 */         this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), false);
/*      */         
/*      */         return;
/*      */       } 
/*  526 */       this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
/*      */     } 
/*      */     
/*  529 */     this.clientWorldController.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
/*  530 */     Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());
/*  531 */     chunk.fillChunk(packetIn.getExtractedDataBytes(), packetIn.getExtractedSize(), packetIn.func_149274_i());
/*  532 */     this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
/*      */     
/*  534 */     if (!packetIn.func_149274_i() || !(this.clientWorldController.provider instanceof net.minecraft.world.WorldProviderSurface))
/*      */     {
/*  536 */       chunk.resetRelightChecks();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBlockChange(S23PacketBlockChange packetIn) {
/*  542 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  543 */     this.clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDisconnect(S40PacketDisconnect packetIn) {
/*  548 */     this.netManager.closeChannel(packetIn.getReason());
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisconnect(IChatComponent reason) {
/*  553 */     this.gameController.loadWorld((WorldClient)null);
/*      */     
/*  555 */     if (this.guiScreenServer != null) {
/*      */       
/*  557 */       this.gameController.displayGuiScreen((GuiScreen)new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
/*      */     }
/*      */     else {
/*      */       
/*  561 */       this.gameController.displayGuiScreen((GuiScreen)new GuiDisconnected((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()), "disconnect.lost", reason));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addToSendQueue(Packet p_147297_1_) {
/*  567 */     this.netManager.sendPacket(p_147297_1_);
/*      */   }
/*      */   
/*      */   public void handleCollectItem(S0DPacketCollectItem packetIn) {
/*      */     EntityPlayerSP entityPlayerSP;
/*  572 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  573 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
/*  574 */     EntityLivingBase entitylivingbase = (EntityLivingBase)this.clientWorldController.getEntityByID(packetIn.getEntityID());
/*      */     
/*  576 */     if (entitylivingbase == null)
/*      */     {
/*  578 */       entityPlayerSP = this.gameController.thePlayer;
/*      */     }
/*      */     
/*  581 */     if (entity != null) {
/*      */       
/*  583 */       if (entity instanceof EntityXPOrb) {
/*      */         
/*  585 */         this.clientWorldController.playSoundAtEntity(entity, "random.orb", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*      */       }
/*      */       else {
/*      */         
/*  589 */         this.clientWorldController.playSoundAtEntity(entity, "random.pop", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*      */       } 
/*      */       
/*  592 */       this.gameController.effectRenderer.addEffect((EntityFX)new EntityPickupFX((World)this.clientWorldController, entity, (Entity)entityPlayerSP, 0.5F));
/*  593 */       this.clientWorldController.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChat(S02PacketChat packetIn) {
/*  599 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/*  601 */     if (packetIn.getType() == 2) {
/*      */       
/*  603 */       this.gameController.ingameGUI.setRecordPlaying(packetIn.getChatComponent(), false);
/*      */     }
/*      */     else {
/*      */       
/*  607 */       this.gameController.ingameGUI.getChatGUI().printChatMessage(packetIn.getChatComponent());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAnimation(S0BPacketAnimation packetIn) {
/*  613 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  614 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
/*      */     
/*  616 */     if (entity != null)
/*      */     {
/*  618 */       if (packetIn.getAnimationType() == 0) {
/*      */         
/*  620 */         EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
/*  621 */         entitylivingbase.swingItem();
/*      */       }
/*  623 */       else if (packetIn.getAnimationType() == 1) {
/*      */         
/*  625 */         entity.performHurtAnimation();
/*      */       }
/*  627 */       else if (packetIn.getAnimationType() == 2) {
/*      */         
/*  629 */         EntityPlayer entityplayer = (EntityPlayer)entity;
/*  630 */         entityplayer.wakeUpPlayer(false, false, false);
/*      */       }
/*  632 */       else if (packetIn.getAnimationType() == 4) {
/*      */         
/*  634 */         this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
/*      */       }
/*  636 */       else if (packetIn.getAnimationType() == 5) {
/*      */         
/*  638 */         this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUseBed(S0APacketUseBed packetIn) {
/*  645 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  646 */     packetIn.getPlayer((World)this.clientWorldController).trySleep(packetIn.getBedPosition());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSpawnMob(S0FPacketSpawnMob packetIn) {
/*  651 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  652 */     double d0 = packetIn.getX() / 32.0D;
/*  653 */     double d1 = packetIn.getY() / 32.0D;
/*  654 */     double d2 = packetIn.getZ() / 32.0D;
/*  655 */     float f = (packetIn.getYaw() * 360) / 256.0F;
/*  656 */     float f1 = (packetIn.getPitch() * 360) / 256.0F;
/*  657 */     EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.getEntityType(), (World)this.gameController.theWorld);
/*  658 */     entitylivingbase.serverPosX = packetIn.getX();
/*  659 */     entitylivingbase.serverPosY = packetIn.getY();
/*  660 */     entitylivingbase.serverPosZ = packetIn.getZ();
/*  661 */     entitylivingbase.renderYawOffset = entitylivingbase.rotationYawHead = (packetIn.getHeadPitch() * 360) / 256.0F;
/*  662 */     Entity[] aentity = entitylivingbase.getParts();
/*      */     
/*  664 */     if (aentity != null) {
/*      */       
/*  666 */       int i = packetIn.getEntityID() - entitylivingbase.getEntityId();
/*      */       
/*  668 */       for (int j = 0; j < aentity.length; j++)
/*      */       {
/*  670 */         aentity[j].setEntityId(aentity[j].getEntityId() + i);
/*      */       }
/*      */     } 
/*      */     
/*  674 */     entitylivingbase.setEntityId(packetIn.getEntityID());
/*  675 */     entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
/*  676 */     entitylivingbase.motionX = (packetIn.getVelocityX() / 8000.0F);
/*  677 */     entitylivingbase.motionY = (packetIn.getVelocityY() / 8000.0F);
/*  678 */     entitylivingbase.motionZ = (packetIn.getVelocityZ() / 8000.0F);
/*  679 */     this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entitylivingbase);
/*  680 */     List<DataWatcher.WatchableObject> list = packetIn.func_149027_c();
/*      */     
/*  682 */     if (list != null)
/*      */     {
/*  684 */       entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTimeUpdate(S03PacketTimeUpdate packetIn) {
/*  690 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  691 */     this.gameController.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
/*  692 */     this.gameController.theWorld.setWorldTime(packetIn.getWorldTime());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSpawnPosition(S05PacketSpawnPosition packetIn) {
/*  697 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  698 */     this.gameController.thePlayer.setSpawnPoint(packetIn.getSpawnPos(), true);
/*  699 */     this.gameController.theWorld.getWorldInfo().setSpawn(packetIn.getSpawnPos());
/*      */   }
/*      */   
/*      */   public void handleEntityAttach(S1BPacketEntityAttach packetIn) {
/*      */     EntityPlayerSP entityPlayerSP;
/*  704 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  705 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*  706 */     Entity entity1 = this.clientWorldController.getEntityByID(packetIn.getVehicleEntityId());
/*      */     
/*  708 */     if (packetIn.getLeash() == 0) {
/*      */       
/*  710 */       boolean flag = false;
/*      */       
/*  712 */       if (packetIn.getEntityId() == this.gameController.thePlayer.getEntityId()) {
/*      */         
/*  714 */         entityPlayerSP = this.gameController.thePlayer;
/*      */         
/*  716 */         if (entity1 instanceof EntityBoat)
/*      */         {
/*  718 */           ((EntityBoat)entity1).setIsBoatEmpty(false);
/*      */         }
/*      */         
/*  721 */         flag = (((Entity)entityPlayerSP).ridingEntity == null && entity1 != null);
/*      */       }
/*  723 */       else if (entity1 instanceof EntityBoat) {
/*      */         
/*  725 */         ((EntityBoat)entity1).setIsBoatEmpty(true);
/*      */       } 
/*      */       
/*  728 */       if (entityPlayerSP == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  733 */       entityPlayerSP.mountEntity(entity1);
/*      */       
/*  735 */       if (flag)
/*      */       {
/*  737 */         GameSettings gamesettings = this.gameController.gameSettings;
/*  738 */         this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode()) }), false);
/*      */       }
/*      */     
/*  741 */     } else if (packetIn.getLeash() == 1 && entityPlayerSP instanceof EntityLiving) {
/*      */       
/*  743 */       if (entity1 != null) {
/*      */         
/*  745 */         ((EntityLiving)entityPlayerSP).setLeashedToEntity(entity1, false);
/*      */       }
/*      */       else {
/*      */         
/*  749 */         ((EntityLiving)entityPlayerSP).clearLeashed(false, false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityStatus(S19PacketEntityStatus packetIn) {
/*  756 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  757 */     Entity entity = packetIn.getEntity((World)this.clientWorldController);
/*      */     
/*  759 */     if (entity != null)
/*      */     {
/*  761 */       if (packetIn.getOpCode() == 21) {
/*      */         
/*  763 */         this.gameController.getSoundHandler().playSound((ISound)new GuardianSound((EntityGuardian)entity));
/*      */       }
/*      */       else {
/*      */         
/*  767 */         entity.handleStatusUpdate(packetIn.getOpCode());
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUpdateHealth(S06PacketUpdateHealth packetIn) {
/*  774 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  775 */     this.gameController.thePlayer.setPlayerSPHealth(packetIn.getHealth());
/*  776 */     this.gameController.thePlayer.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
/*  777 */     this.gameController.thePlayer.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetExperience(S1FPacketSetExperience packetIn) {
/*  782 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  783 */     this.gameController.thePlayer.setXPStats(packetIn.func_149397_c(), packetIn.getTotalExperience(), packetIn.getLevel());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRespawn(S07PacketRespawn packetIn) {
/*  788 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/*  790 */     if (packetIn.getDimensionID() != this.gameController.thePlayer.dimension) {
/*      */       
/*  792 */       this.doneLoadingTerrain = false;
/*  793 */       Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/*  794 */       this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, this.gameController.theWorld.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()), packetIn.getDimensionID(), packetIn.getDifficulty(), this.gameController.mcProfiler);
/*  795 */       this.clientWorldController.setWorldScoreboard(scoreboard);
/*  796 */       this.gameController.loadWorld(this.clientWorldController);
/*  797 */       this.gameController.thePlayer.dimension = packetIn.getDimensionID();
/*  798 */       this.gameController.displayGuiScreen((GuiScreen)new GuiDownloadTerrain(this));
/*      */     } 
/*      */     
/*  801 */     this.gameController.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
/*  802 */     this.gameController.playerController.setGameType(packetIn.getGameType());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleExplosion(S27PacketExplosion packetIn) {
/*  807 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  808 */     Explosion explosion = new Explosion((World)this.gameController.theWorld, (Entity)null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
/*  809 */     explosion.doExplosionB(true);
/*  810 */     this.gameController.thePlayer.motionX += packetIn.func_149149_c();
/*  811 */     this.gameController.thePlayer.motionY += packetIn.func_149144_d();
/*  812 */     this.gameController.thePlayer.motionZ += packetIn.func_149147_e();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleOpenWindow(S2DPacketOpenWindow packetIn) {
/*  817 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  818 */     EntityPlayerSP entityplayersp = this.gameController.thePlayer;
/*      */     
/*  820 */     if ("minecraft:container".equals(packetIn.getGuiId())) {
/*      */       
/*  822 */       entityplayersp.displayGUIChest((IInventory)new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
/*  823 */       entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */     }
/*  825 */     else if ("minecraft:villager".equals(packetIn.getGuiId())) {
/*      */       
/*  827 */       entityplayersp.displayVillagerTradeGui((IMerchant)new NpcMerchant((EntityPlayer)entityplayersp, packetIn.getWindowTitle()));
/*  828 */       entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */     }
/*  830 */     else if ("EntityHorse".equals(packetIn.getGuiId())) {
/*      */       
/*  832 */       Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */       
/*  834 */       if (entity instanceof EntityHorse)
/*      */       {
/*  836 */         entityplayersp.displayGUIHorse((EntityHorse)entity, (IInventory)new AnimalChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
/*  837 */         entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */       }
/*      */     
/*  840 */     } else if (!packetIn.hasSlots()) {
/*      */       
/*  842 */       entityplayersp.displayGui((IInteractionObject)new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
/*  843 */       entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */     }
/*      */     else {
/*      */       
/*  847 */       ContainerLocalMenu containerlocalmenu = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
/*  848 */       entityplayersp.displayGUIChest((IInventory)containerlocalmenu);
/*  849 */       entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetSlot(S2FPacketSetSlot packetIn) {
/*  855 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  856 */     EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
/*      */     
/*  858 */     if (packetIn.func_149175_c() == -1) {
/*      */       
/*  860 */       ((EntityPlayer)entityPlayerSP).inventory.setItemStack(packetIn.func_149174_e());
/*      */     }
/*      */     else {
/*      */       
/*  864 */       boolean flag = false;
/*      */       
/*  866 */       if (this.gameController.currentScreen instanceof GuiContainerCreative) {
/*      */         
/*  868 */         GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.gameController.currentScreen;
/*  869 */         flag = (guicontainercreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex());
/*      */       } 
/*      */       
/*  872 */       if (packetIn.func_149175_c() == 0 && packetIn.func_149173_d() >= 36 && packetIn.func_149173_d() < 45) {
/*      */         
/*  874 */         ItemStack itemstack = ((EntityPlayer)entityPlayerSP).inventoryContainer.getSlot(packetIn.func_149173_d()).getStack();
/*      */         
/*  876 */         if (packetIn.func_149174_e() != null && (itemstack == null || itemstack.stackSize < (packetIn.func_149174_e()).stackSize))
/*      */         {
/*  878 */           (packetIn.func_149174_e()).animationsToGo = 5;
/*      */         }
/*      */         
/*  881 */         ((EntityPlayer)entityPlayerSP).inventoryContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
/*      */       }
/*  883 */       else if (packetIn.func_149175_c() == ((EntityPlayer)entityPlayerSP).openContainer.windowId && (packetIn.func_149175_c() != 0 || !flag)) {
/*      */         
/*  885 */         ((EntityPlayer)entityPlayerSP).openContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn) {
/*  892 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  893 */     Container container = null;
/*  894 */     EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
/*      */     
/*  896 */     if (packetIn.getWindowId() == 0) {
/*      */       
/*  898 */       container = ((EntityPlayer)entityPlayerSP).inventoryContainer;
/*      */     }
/*  900 */     else if (packetIn.getWindowId() == ((EntityPlayer)entityPlayerSP).openContainer.windowId) {
/*      */       
/*  902 */       container = ((EntityPlayer)entityPlayerSP).openContainer;
/*      */     } 
/*      */     
/*  905 */     if (container != null && !packetIn.func_148888_e())
/*      */     {
/*  907 */       addToSendQueue((Packet)new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleWindowItems(S30PacketWindowItems packetIn) {
/*  913 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  914 */     EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
/*      */     
/*  916 */     if (packetIn.func_148911_c() == 0) {
/*      */       
/*  918 */       ((EntityPlayer)entityPlayerSP).inventoryContainer.putStacksInSlots(packetIn.getItemStacks());
/*      */     }
/*  920 */     else if (packetIn.func_148911_c() == ((EntityPlayer)entityPlayerSP).openContainer.windowId) {
/*      */       
/*  922 */       ((EntityPlayer)entityPlayerSP).openContainer.putStacksInSlots(packetIn.getItemStacks());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn) {
/*      */     TileEntitySign tileEntitySign;
/*  928 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  929 */     TileEntity tileentity = this.clientWorldController.getTileEntity(packetIn.getSignPosition());
/*      */     
/*  931 */     if (!(tileentity instanceof TileEntitySign)) {
/*      */       
/*  933 */       tileEntitySign = new TileEntitySign();
/*  934 */       tileEntitySign.setWorldObj((World)this.clientWorldController);
/*  935 */       tileEntitySign.setPos(packetIn.getSignPosition());
/*      */     } 
/*      */     
/*  938 */     this.gameController.thePlayer.openEditSign(tileEntitySign);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUpdateSign(S33PacketUpdateSign packetIn) {
/*  943 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  944 */     boolean flag = false;
/*      */     
/*  946 */     if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
/*      */       
/*  948 */       TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
/*      */       
/*  950 */       if (tileentity instanceof TileEntitySign) {
/*      */         
/*  952 */         TileEntitySign tileentitysign = (TileEntitySign)tileentity;
/*      */         
/*  954 */         if (tileentitysign.getIsEditable()) {
/*      */           
/*  956 */           System.arraycopy(packetIn.getLines(), 0, tileentitysign.signText, 0, 4);
/*  957 */           tileentitysign.markDirty();
/*      */         } 
/*      */         
/*  960 */         flag = true;
/*      */       } 
/*      */     } 
/*      */     
/*  964 */     if (!flag && this.gameController.thePlayer != null)
/*      */     {
/*  966 */       this.gameController.thePlayer.addChatMessage((IChatComponent)new ChatComponentText("Unable to locate sign at " + packetIn.getPos().getX() + ", " + packetIn.getPos().getY() + ", " + packetIn.getPos().getZ()));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn) {
/*  972 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/*  974 */     if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
/*      */       
/*  976 */       TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
/*  977 */       int i = packetIn.getTileEntityType();
/*      */       
/*  979 */       if ((i == 1 && tileentity instanceof net.minecraft.tileentity.TileEntityMobSpawner) || (i == 2 && tileentity instanceof net.minecraft.tileentity.TileEntityCommandBlock) || (i == 3 && tileentity instanceof net.minecraft.tileentity.TileEntityBeacon) || (i == 4 && tileentity instanceof net.minecraft.tileentity.TileEntitySkull) || (i == 5 && tileentity instanceof net.minecraft.tileentity.TileEntityFlowerPot) || (i == 6 && tileentity instanceof net.minecraft.tileentity.TileEntityBanner))
/*      */       {
/*  981 */         tileentity.readFromNBT(packetIn.getNbtCompound());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleWindowProperty(S31PacketWindowProperty packetIn) {
/*  988 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*  989 */     EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
/*      */     
/*  991 */     if (((EntityPlayer)entityPlayerSP).openContainer != null && ((EntityPlayer)entityPlayerSP).openContainer.windowId == packetIn.getWindowId())
/*      */     {
/*  993 */       ((EntityPlayer)entityPlayerSP).openContainer.updateProgressBar(packetIn.getVarIndex(), packetIn.getVarValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityEquipment(S04PacketEntityEquipment packetIn) {
/*  999 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1000 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
/*      */     
/* 1002 */     if (entity != null)
/*      */     {
/* 1004 */       entity.setCurrentItemOrArmor(packetIn.getEquipmentSlot(), packetIn.getItemStack());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCloseWindow(S2EPacketCloseWindow packetIn) {
/* 1010 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1011 */     this.gameController.thePlayer.closeScreenAndDropStack();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBlockAction(S24PacketBlockAction packetIn) {
/* 1016 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1017 */     this.gameController.theWorld.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn) {
/* 1022 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1023 */     this.gameController.theWorld.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMapChunkBulk(S26PacketMapChunkBulk packetIn) {
/* 1028 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/* 1030 */     for (int i = 0; i < packetIn.getChunkCount(); i++) {
/*      */       
/* 1032 */       int j = packetIn.getChunkX(i);
/* 1033 */       int k = packetIn.getChunkZ(i);
/* 1034 */       this.clientWorldController.doPreChunk(j, k, true);
/* 1035 */       this.clientWorldController.invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
/* 1036 */       Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(j, k);
/* 1037 */       chunk.fillChunk(packetIn.getChunkBytes(i), packetIn.getChunkSize(i), true);
/* 1038 */       this.clientWorldController.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
/*      */       
/* 1040 */       if (!(this.clientWorldController.provider instanceof net.minecraft.world.WorldProviderSurface))
/*      */       {
/* 1042 */         chunk.resetRelightChecks();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChangeGameState(S2BPacketChangeGameState packetIn) {
/* 1049 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1050 */     EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
/* 1051 */     int i = packetIn.getGameState();
/* 1052 */     float f = packetIn.func_149137_d();
/* 1053 */     int j = MathHelper.floor_float(f + 0.5F);
/*      */     
/* 1055 */     if (i >= 0 && i < S2BPacketChangeGameState.MESSAGE_NAMES.length && S2BPacketChangeGameState.MESSAGE_NAMES[i] != null)
/*      */     {
/* 1057 */       entityPlayerSP.addChatComponentMessage((IChatComponent)new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]));
/*      */     }
/*      */     
/* 1060 */     if (i == 1) {
/*      */       
/* 1062 */       this.clientWorldController.getWorldInfo().setRaining(true);
/* 1063 */       this.clientWorldController.setRainStrength(0.0F);
/*      */     }
/* 1065 */     else if (i == 2) {
/*      */       
/* 1067 */       this.clientWorldController.getWorldInfo().setRaining(false);
/* 1068 */       this.clientWorldController.setRainStrength(1.0F);
/*      */     }
/* 1070 */     else if (i == 3) {
/*      */       
/* 1072 */       this.gameController.playerController.setGameType(WorldSettings.GameType.getByID(j));
/*      */     }
/* 1074 */     else if (i == 4) {
/*      */       
/* 1076 */       this.gameController.displayGuiScreen((GuiScreen)new GuiWinGame());
/*      */     }
/* 1078 */     else if (i == 5) {
/*      */       
/* 1080 */       GameSettings gamesettings = this.gameController.gameSettings;
/*      */       
/* 1082 */       if (f == 0.0F)
/*      */       {
/* 1084 */         this.gameController.displayGuiScreen((GuiScreen)new GuiScreenDemo());
/*      */       }
/* 1086 */       else if (f == 101.0F)
/*      */       {
/* 1088 */         this.gameController.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentTranslation("demo.help.movement", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode()) }));
/*      */       }
/* 1090 */       else if (f == 102.0F)
/*      */       {
/* 1092 */         this.gameController.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentTranslation("demo.help.jump", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode()) }));
/*      */       }
/* 1094 */       else if (f == 103.0F)
/*      */       {
/* 1096 */         this.gameController.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentTranslation("demo.help.inventory", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode()) }));
/*      */       }
/*      */     
/* 1099 */     } else if (i == 6) {
/*      */       
/* 1101 */       this.clientWorldController.playSound(((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY + entityPlayerSP.getEyeHeight(), ((EntityPlayer)entityPlayerSP).posZ, "random.successful_hit", 0.18F, 0.45F, false);
/*      */     }
/* 1103 */     else if (i == 7) {
/*      */       
/* 1105 */       this.clientWorldController.setRainStrength(f);
/*      */     }
/* 1107 */     else if (i == 8) {
/*      */       
/* 1109 */       this.clientWorldController.setThunderStrength(f);
/*      */     }
/* 1111 */     else if (i == 10) {
/*      */       
/* 1113 */       this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, ((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY, ((EntityPlayer)entityPlayerSP).posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/* 1114 */       this.clientWorldController.playSound(((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY, ((EntityPlayer)entityPlayerSP).posZ, "mob.guardian.curse", 1.0F, 1.0F, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMaps(S34PacketMaps packetIn) {
/* 1120 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1121 */     MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), (World)this.gameController.theWorld);
/* 1122 */     packetIn.setMapdataTo(mapdata);
/* 1123 */     this.gameController.entityRenderer.getMapItemRenderer().updateMapTexture(mapdata);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEffect(S28PacketEffect packetIn) {
/* 1128 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/* 1130 */     if (packetIn.isSoundServerwide()) {
/*      */       
/* 1132 */       this.gameController.theWorld.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
/*      */     }
/*      */     else {
/*      */       
/* 1136 */       this.gameController.theWorld.playAuxSFX(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleStatistics(S37PacketStatistics packetIn) {
/* 1142 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1143 */     boolean flag = false;
/*      */     
/* 1145 */     for (Map.Entry<StatBase, Integer> entry : (Iterable<Map.Entry<StatBase, Integer>>)packetIn.func_148974_c().entrySet()) {
/*      */       
/* 1147 */       StatBase statbase = entry.getKey();
/* 1148 */       int i = ((Integer)entry.getValue()).intValue();
/*      */       
/* 1150 */       if (statbase.isAchievement() && i > 0) {
/*      */         
/* 1152 */         if (this.field_147308_k && this.gameController.thePlayer.getStatFileWriter().readStat(statbase) == 0) {
/*      */           
/* 1154 */           Achievement achievement = (Achievement)statbase;
/* 1155 */           this.gameController.guiAchievement.displayAchievement(achievement);
/*      */           
/* 1157 */           if (statbase == AchievementList.openInventory) {
/*      */             
/* 1159 */             this.gameController.gameSettings.showInventoryAchievementHint = false;
/* 1160 */             this.gameController.gameSettings.saveOptions();
/*      */           } 
/*      */         } 
/*      */         
/* 1164 */         flag = true;
/*      */       } 
/*      */       
/* 1167 */       this.gameController.thePlayer.getStatFileWriter().unlockAchievement((EntityPlayer)this.gameController.thePlayer, statbase, i);
/*      */     } 
/*      */     
/* 1170 */     if (!this.field_147308_k && !flag && this.gameController.gameSettings.showInventoryAchievementHint)
/*      */     {
/* 1172 */       this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
/*      */     }
/*      */     
/* 1175 */     this.field_147308_k = true;
/*      */     
/* 1177 */     if (this.gameController.currentScreen instanceof IProgressMeter)
/*      */     {
/* 1179 */       ((IProgressMeter)this.gameController.currentScreen).doneLoading();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityEffect(S1DPacketEntityEffect packetIn) {
/* 1185 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1186 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/* 1188 */     if (entity instanceof EntityLivingBase) {
/*      */       
/* 1190 */       PotionEffect potioneffect = new PotionEffect(packetIn.getEffectId(), packetIn.getDuration(), packetIn.getAmplifier(), false, packetIn.func_179707_f());
/* 1191 */       potioneffect.setPotionDurationMax(packetIn.func_149429_c());
/* 1192 */       ((EntityLivingBase)entity).addPotionEffect(potioneffect);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCombatEvent(S42PacketCombatEvent packetIn) {
/* 1198 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1199 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.field_179775_c);
/* 1200 */     EntityLivingBase entitylivingbase = (entity instanceof EntityLivingBase) ? (EntityLivingBase)entity : null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleServerDifficulty(S41PacketServerDifficulty packetIn) {
/* 1206 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1207 */     this.gameController.theWorld.getWorldInfo().setDifficulty(packetIn.getDifficulty());
/* 1208 */     this.gameController.theWorld.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCamera(S43PacketCamera packetIn) {
/* 1213 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1214 */     Entity entity = packetIn.getEntity((World)this.clientWorldController);
/*      */     
/* 1216 */     if (entity != null)
/*      */     {
/* 1218 */       this.gameController.setRenderViewEntity(entity);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleWorldBorder(S44PacketWorldBorder packetIn) {
/* 1224 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1225 */     packetIn.func_179788_a(this.clientWorldController.getWorldBorder());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleTitle(S45PacketTitle packetIn) {
/* 1231 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1232 */     S45PacketTitle.Type s45packettitle$type = packetIn.getType();
/* 1233 */     String s = null;
/* 1234 */     String s1 = null;
/* 1235 */     String s2 = (packetIn.getMessage() != null) ? packetIn.getMessage().getFormattedText() : "";
/*      */     
/* 1237 */     switch (s45packettitle$type) {
/*      */       
/*      */       case ADD_PLAYER:
/* 1240 */         s = s2;
/*      */         break;
/*      */       
/*      */       case UPDATE_GAME_MODE:
/* 1244 */         s1 = s2;
/*      */         break;
/*      */       
/*      */       case UPDATE_LATENCY:
/* 1248 */         this.gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
/* 1249 */         this.gameController.ingameGUI.setDefaultTitlesTimes();
/*      */         return;
/*      */     } 
/*      */     
/* 1253 */     this.gameController.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn) {
/* 1258 */     if (!this.netManager.isLocalChannel())
/*      */     {
/* 1260 */       this.netManager.setCompressionTreshold(packetIn.getThreshold());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn) {
/* 1266 */     this.gameController.ingameGUI.getTabList().setHeader((packetIn.getHeader().getFormattedText().length() == 0) ? null : packetIn.getHeader());
/* 1267 */     this.gameController.ingameGUI.getTabList().setFooter((packetIn.getFooter().getFormattedText().length() == 0) ? null : packetIn.getFooter());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn) {
/* 1272 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1273 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/* 1275 */     if (entity instanceof EntityLivingBase)
/*      */     {
/* 1277 */       ((EntityLivingBase)entity).removePotionEffectClient(packetIn.getEffectId());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handlePlayerListItem(S38PacketPlayerListItem packetIn) {
/* 1284 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/* 1286 */     for (S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata : packetIn.getEntries()) {
/*      */       
/* 1288 */       if (packetIn.getAction() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
/*      */         
/* 1290 */         this.playerInfoMap.remove(s38packetplayerlistitem$addplayerdata.getProfile().getId());
/*      */         
/*      */         continue;
/*      */       } 
/* 1294 */       NetworkPlayerInfo networkplayerinfo = this.playerInfoMap.get(s38packetplayerlistitem$addplayerdata.getProfile().getId());
/*      */       
/* 1296 */       if (packetIn.getAction() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
/*      */         
/* 1298 */         networkplayerinfo = new NetworkPlayerInfo(s38packetplayerlistitem$addplayerdata);
/* 1299 */         this.playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
/*      */       } 
/*      */       
/* 1302 */       if (networkplayerinfo != null)
/*      */       {
/* 1304 */         switch (packetIn.getAction()) {
/*      */           
/*      */           case ADD_PLAYER:
/* 1307 */             networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
/* 1308 */             networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
/*      */ 
/*      */           
/*      */           case UPDATE_GAME_MODE:
/* 1312 */             networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
/*      */ 
/*      */           
/*      */           case UPDATE_LATENCY:
/* 1316 */             networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
/*      */ 
/*      */           
/*      */           case UPDATE_DISPLAY_NAME:
/* 1320 */             networkplayerinfo.setDisplayName(s38packetplayerlistitem$addplayerdata.getDisplayName());
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleKeepAlive(S00PacketKeepAlive packetIn) {
/* 1329 */     addToSendQueue((Packet)new C00PacketKeepAlive(packetIn.func_149134_c()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn) {
/* 1334 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1335 */     EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
/* 1336 */     ((EntityPlayer)entityPlayerSP).capabilities.isFlying = packetIn.isFlying();
/* 1337 */     ((EntityPlayer)entityPlayerSP).capabilities.isCreativeMode = packetIn.isCreativeMode();
/* 1338 */     ((EntityPlayer)entityPlayerSP).capabilities.disableDamage = packetIn.isInvulnerable();
/* 1339 */     ((EntityPlayer)entityPlayerSP).capabilities.allowFlying = packetIn.isAllowFlying();
/* 1340 */     ((EntityPlayer)entityPlayerSP).capabilities.setFlySpeed(packetIn.getFlySpeed());
/* 1341 */     ((EntityPlayer)entityPlayerSP).capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTabComplete(S3APacketTabComplete packetIn) {
/* 1346 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1347 */     String[] astring = packetIn.func_149630_c();
/*      */     
/* 1349 */     if (this.gameController.currentScreen instanceof GuiChat) {
/*      */       
/* 1351 */       GuiChat guichat = (GuiChat)this.gameController.currentScreen;
/* 1352 */       guichat.onAutocompleteResponse(astring);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSoundEffect(S29PacketSoundEffect packetIn) {
/* 1358 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1359 */     this.gameController.theWorld.playSound(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSoundName(), packetIn.getVolume(), packetIn.getPitch(), false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleResourcePack(S48PacketResourcePackSend packetIn) {
/* 1364 */     final String s = packetIn.getURL();
/* 1365 */     final String s1 = packetIn.getHash();
/*      */     
/* 1367 */     if (s.startsWith("level://")) {
/*      */       
/* 1369 */       String s2 = s.substring("level://".length());
/* 1370 */       File file1 = new File(this.gameController.mcDataDir, "saves");
/* 1371 */       File file2 = new File(file1, s2);
/*      */       
/* 1373 */       if (file2.isFile())
/*      */       {
/* 1375 */         this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
/* 1376 */         Futures.addCallback(this.gameController.getResourcePackRepository().setResourcePackInstance(file2), new FutureCallback<Object>()
/*      */             {
/*      */               public void onSuccess(Object p_onSuccess_1_)
/*      */               {
/* 1380 */                 NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
/*      */               }
/*      */               
/*      */               public void onFailure(Throwable p_onFailure_1_) {
/* 1384 */                 NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
/*      */               }
/*      */             });
/*      */       }
/*      */       else
/*      */       {
/* 1390 */         this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1395 */     else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
/*      */       
/* 1397 */       this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
/* 1398 */       Futures.addCallback(this.gameController.getResourcePackRepository().downloadResourcePack(s, s1), new FutureCallback<Object>()
/*      */           {
/*      */             public void onSuccess(Object p_onSuccess_1_)
/*      */             {
/* 1402 */               NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
/*      */             }
/*      */             
/*      */             public void onFailure(Throwable p_onFailure_1_) {
/* 1406 */               NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
/*      */             }
/*      */           });
/*      */     }
/* 1410 */     else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
/*      */       
/* 1412 */       this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
/*      */     }
/*      */     else {
/*      */       
/* 1416 */       this.gameController.addScheduledTask(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/* 1420 */               NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen)new GuiYesNo(new GuiYesNoCallback()
/*      */                     {
/*      */                       public void confirmClicked(boolean result, int id)
/*      */                       {
/* 1424 */                         NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
/*      */                         
/* 1426 */                         if (result) {
/*      */                           
/* 1428 */                           if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null)
/*      */                           {
/* 1430 */                             NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.ENABLED);
/*      */                           }
/*      */                           
/* 1433 */                           NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
/* 1434 */                           Futures.addCallback(NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(s, s1), new FutureCallback<Object>()
/*      */                               {
/*      */                                 public void onSuccess(Object p_onSuccess_1_)
/*      */                                 {
/* 1438 */                                   NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
/*      */                                 }
/*      */                                 
/*      */                                 public void onFailure(Throwable p_onFailure_1_) {
/* 1442 */                                   NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
/*      */                                 }
/*      */                               });
/*      */                         }
/*      */                         else {
/*      */                           
/* 1448 */                           if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null)
/*      */                           {
/* 1450 */                             NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.DISABLED);
/*      */                           }
/*      */                           
/* 1453 */                           NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
/*      */                         } 
/*      */                         
/* 1456 */                         ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
/* 1457 */                         NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen)null);
/*      */                       }
/* 1459 */                     }I18n.format("multiplayer.texturePrompt.line1", new Object[0]), I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
/*      */             }
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn) {
/* 1468 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1469 */     Entity entity = packetIn.getEntity((World)this.clientWorldController);
/*      */     
/* 1471 */     if (entity != null)
/*      */     {
/* 1473 */       entity.clientUpdateEntityNBT(packetIn.getTagCompound());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCustomPayload(S3FPacketCustomPayload packetIn) {
/* 1479 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/* 1481 */     if ("MC|TrList".equals(packetIn.getChannelName())) {
/*      */       
/* 1483 */       PacketBuffer packetbuffer = packetIn.getBufferData();
/*      */ 
/*      */       
/*      */       try {
/* 1487 */         int i = packetbuffer.readInt();
/* 1488 */         GuiScreen guiscreen = this.gameController.currentScreen;
/*      */         
/* 1490 */         if (guiscreen != null && guiscreen instanceof GuiMerchant && i == this.gameController.thePlayer.openContainer.windowId)
/*      */         {
/* 1492 */           IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
/* 1493 */           MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
/* 1494 */           imerchant.setRecipes(merchantrecipelist);
/*      */         }
/*      */       
/* 1497 */       } catch (IOException ioexception) {
/*      */         
/* 1499 */         logger.error("Couldn't load trade info", ioexception);
/*      */       }
/*      */       finally {
/*      */         
/* 1503 */         packetbuffer.release();
/*      */       }
/*      */     
/* 1506 */     } else if ("MC|Brand".equals(packetIn.getChannelName())) {
/*      */       
/* 1508 */       this.gameController.thePlayer.setClientBrand(packetIn.getBufferData().readStringFromBuffer(32767));
/*      */     }
/* 1510 */     else if ("MC|BOpen".equals(packetIn.getChannelName())) {
/*      */       
/* 1512 */       ItemStack itemstack = this.gameController.thePlayer.getCurrentEquippedItem();
/*      */       
/* 1514 */       if (itemstack != null && itemstack.getItem() == Items.written_book)
/*      */       {
/* 1516 */         this.gameController.displayGuiScreen((GuiScreen)new GuiScreenBook((EntityPlayer)this.gameController.thePlayer, itemstack, false));
/*      */       }
/*      */     }
/* 1519 */     else if ("REGISTER".equals(packetIn.getChannelName())) {
/* 1520 */       String channels = packetIn.getBufferData().toString(Charsets.UTF_8);
/* 1521 */       String[] channel = channels.split("\000");
/*      */       
/* 1523 */       ChatUtil.print("&cIncoming channels:", false);
/* 1524 */       for (String s : channel) {
/* 1525 */         ChatUtil.print("&e- " + s, false);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn) {
/* 1533 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1534 */     Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/*      */     
/* 1536 */     if (packetIn.func_149338_e() == 0) {
/*      */       
/* 1538 */       ScoreObjective scoreobjective = scoreboard.addScoreObjective(packetIn.func_149339_c(), IScoreObjectiveCriteria.DUMMY);
/* 1539 */       scoreobjective.setDisplayName(packetIn.func_149337_d());
/* 1540 */       scoreobjective.setRenderType(packetIn.func_179817_d());
/*      */     }
/*      */     else {
/*      */       
/* 1544 */       ScoreObjective scoreobjective1 = scoreboard.getObjective(packetIn.func_149339_c());
/*      */       
/* 1546 */       if (packetIn.func_149338_e() == 1) {
/*      */         
/* 1548 */         scoreboard.removeObjective(scoreobjective1);
/*      */       }
/* 1550 */       else if (packetIn.func_149338_e() == 2) {
/*      */         
/* 1552 */         scoreobjective1.setDisplayName(packetIn.func_149337_d());
/* 1553 */         scoreobjective1.setRenderType(packetIn.func_179817_d());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUpdateScore(S3CPacketUpdateScore packetIn) {
/* 1560 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1561 */     Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/* 1562 */     ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());
/*      */     
/* 1564 */     if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.CHANGE) {
/*      */       
/* 1566 */       Score score = scoreboard.getValueFromObjective(packetIn.getPlayerName(), scoreobjective);
/* 1567 */       score.setScorePoints(packetIn.getScoreValue());
/*      */     }
/* 1569 */     else if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.REMOVE) {
/*      */       
/* 1571 */       if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName())) {
/*      */         
/* 1573 */         scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), (ScoreObjective)null);
/*      */       }
/* 1575 */       else if (scoreobjective != null) {
/*      */         
/* 1577 */         scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn) {
/* 1584 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1585 */     Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/*      */     
/* 1587 */     if (packetIn.func_149370_d().length() == 0) {
/*      */       
/* 1589 */       scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), (ScoreObjective)null);
/*      */     }
/*      */     else {
/*      */       
/* 1593 */       ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.func_149370_d());
/* 1594 */       scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), scoreobjective);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleTeams(S3EPacketTeams packetIn) {
/*      */     ScorePlayerTeam scoreplayerteam;
/* 1600 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1601 */     Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/*      */ 
/*      */     
/* 1604 */     if (packetIn.getAction() == 0) {
/*      */       
/* 1606 */       scoreplayerteam = scoreboard.createTeam(packetIn.getName());
/*      */     }
/*      */     else {
/*      */       
/* 1610 */       scoreplayerteam = scoreboard.getTeam(packetIn.getName());
/*      */     } 
/*      */     
/* 1613 */     if (packetIn.getAction() == 0 || packetIn.getAction() == 2) {
/*      */       
/*      */       try {
/* 1616 */         scoreplayerteam.setTeamName(packetIn.getDisplayName());
/* 1617 */         scoreplayerteam.setNamePrefix(packetIn.getPrefix());
/* 1618 */         scoreplayerteam.setNameSuffix(packetIn.getSuffix());
/* 1619 */         scoreplayerteam.setChatFormat(EnumChatFormatting.func_175744_a(packetIn.getColor()));
/* 1620 */         scoreplayerteam.func_98298_a(packetIn.getFriendlyFlags());
/* 1621 */         Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(packetIn.getNameTagVisibility());
/* 1622 */         if (team$enumvisible != null)
/*      */         {
/* 1624 */           scoreplayerteam.setNameTagVisibility(team$enumvisible);
/*      */         }
/* 1626 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */     
/* 1630 */     if (packetIn.getAction() == 0 || packetIn.getAction() == 3)
/*      */     {
/* 1632 */       for (String s : packetIn.getPlayers())
/*      */       {
/* 1634 */         scoreboard.addPlayerToTeam(s, packetIn.getName());
/*      */       }
/*      */     }
/*      */     
/* 1638 */     if (packetIn.getAction() == 4)
/*      */     {
/* 1640 */       for (String s1 : packetIn.getPlayers())
/*      */       {
/* 1642 */         scoreboard.removePlayerFromTeam(s1, scoreplayerteam);
/*      */       }
/*      */     }
/*      */     
/* 1646 */     if (packetIn.getAction() == 1)
/*      */     {
/* 1648 */       scoreboard.removeTeam(scoreplayerteam);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleParticles(S2APacketParticles packetIn) {
/* 1654 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/*      */     
/* 1656 */     if (packetIn.getParticleCount() == 0) {
/*      */       
/* 1658 */       double d0 = (packetIn.getParticleSpeed() * packetIn.getXOffset());
/* 1659 */       double d2 = (packetIn.getParticleSpeed() * packetIn.getYOffset());
/* 1660 */       double d4 = (packetIn.getParticleSpeed() * packetIn.getZOffset());
/*      */ 
/*      */       
/*      */       try {
/* 1664 */         this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4, packetIn.getParticleArgs());
/*      */       }
/* 1666 */       catch (Throwable var17) {
/*      */         
/* 1668 */         logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1673 */       for (int i = 0; i < packetIn.getParticleCount(); i++) {
/*      */         
/* 1675 */         double d1 = this.avRandomizer.nextGaussian() * packetIn.getXOffset();
/* 1676 */         double d3 = this.avRandomizer.nextGaussian() * packetIn.getYOffset();
/* 1677 */         double d5 = this.avRandomizer.nextGaussian() * packetIn.getZOffset();
/* 1678 */         double d6 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
/* 1679 */         double d7 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
/* 1680 */         double d8 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
/*      */ 
/*      */         
/*      */         try {
/* 1684 */           this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3, packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
/*      */         }
/* 1686 */         catch (Throwable var16) {
/*      */           
/* 1688 */           logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityProperties(S20PacketEntityProperties packetIn) {
/* 1697 */     PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
/* 1698 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/* 1700 */     if (entity != null) {
/*      */       
/* 1702 */       if (!(entity instanceof EntityLivingBase))
/*      */       {
/* 1704 */         throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
/*      */       }
/*      */ 
/*      */       
/* 1708 */       BaseAttributeMap baseattributemap = ((EntityLivingBase)entity).getAttributeMap();
/*      */       
/* 1710 */       for (S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot : packetIn.func_149441_d()) {
/*      */         
/* 1712 */         IAttributeInstance iattributeinstance = baseattributemap.getAttributeInstanceByName(s20packetentityproperties$snapshot.func_151409_a());
/*      */         
/* 1714 */         if (iattributeinstance == null)
/*      */         {
/* 1716 */           iattributeinstance = baseattributemap.registerAttribute((IAttribute)new RangedAttribute((IAttribute)null, s20packetentityproperties$snapshot.func_151409_a(), 0.0D, 2.2250738585072014E-308D, Double.MAX_VALUE));
/*      */         }
/*      */         
/* 1719 */         iattributeinstance.setBaseValue(s20packetentityproperties$snapshot.func_151410_b());
/* 1720 */         iattributeinstance.removeAllModifiers();
/*      */         
/* 1722 */         for (AttributeModifier attributemodifier : s20packetentityproperties$snapshot.func_151408_c())
/*      */         {
/* 1724 */           iattributeinstance.applyModifier(attributemodifier);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public NetworkManager getNetworkManager() {
/* 1733 */     return this.netManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
/* 1738 */     return this.playerInfoMap.values();
/*      */   }
/*      */ 
/*      */   
/*      */   public NetworkPlayerInfo getPlayerInfo(UUID p_175102_1_) {
/* 1743 */     return this.playerInfoMap.get(p_175102_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public NetworkPlayerInfo getPlayerInfo(String p_175104_1_) {
/* 1748 */     for (NetworkPlayerInfo networkplayerinfo : this.playerInfoMap.values()) {
/*      */       
/* 1750 */       if (networkplayerinfo.getGameProfile().getName().equals(p_175104_1_))
/*      */       {
/* 1752 */         return networkplayerinfo;
/*      */       }
/*      */     } 
/*      */     
/* 1756 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public GameProfile getGameProfile() {
/* 1761 */     return this.profile;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\network\NetHandlerPlayClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */