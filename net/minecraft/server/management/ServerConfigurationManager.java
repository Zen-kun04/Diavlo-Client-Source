/*     */ package net.minecraft.server.management;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.File;
/*     */ import java.net.SocketAddress;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.server.S01PacketJoinGame;
/*     */ import net.minecraft.network.play.server.S02PacketChat;
/*     */ import net.minecraft.network.play.server.S03PacketTimeUpdate;
/*     */ import net.minecraft.network.play.server.S05PacketSpawnPosition;
/*     */ import net.minecraft.network.play.server.S07PacketRespawn;
/*     */ import net.minecraft.network.play.server.S09PacketHeldItemChange;
/*     */ import net.minecraft.network.play.server.S1DPacketEntityEffect;
/*     */ import net.minecraft.network.play.server.S1FPacketSetExperience;
/*     */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*     */ import net.minecraft.network.play.server.S38PacketPlayerListItem;
/*     */ import net.minecraft.network.play.server.S39PacketPlayerAbilities;
/*     */ import net.minecraft.network.play.server.S3EPacketTeams;
/*     */ import net.minecraft.network.play.server.S3FPacketCustomPayload;
/*     */ import net.minecraft.network.play.server.S41PacketServerDifficulty;
/*     */ import net.minecraft.network.play.server.S44PacketWorldBorder;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.ServerScoreboard;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.stats.StatisticsFile;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.border.IBorderListener;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ import net.minecraft.world.demo.DemoWorldManager;
/*     */ import net.minecraft.world.storage.IPlayerFileData;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class ServerConfigurationManager
/*     */ {
/*  65 */   public static final File FILE_PLAYERBANS = new File("banned-players.json");
/*  66 */   public static final File FILE_IPBANS = new File("banned-ips.json");
/*  67 */   public static final File FILE_OPS = new File("ops.json");
/*  68 */   public static final File FILE_WHITELIST = new File("whitelist.json");
/*  69 */   private static final Logger logger = LogManager.getLogger();
/*  70 */   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
/*     */   private final MinecraftServer mcServer;
/*  72 */   private final List<EntityPlayerMP> playerEntityList = Lists.newArrayList();
/*  73 */   private final Map<UUID, EntityPlayerMP> uuidToPlayerMap = Maps.newHashMap();
/*     */   
/*     */   private final UserListBans bannedPlayers;
/*     */   private final BanList bannedIPs;
/*     */   private final UserListOps ops;
/*     */   private final UserListWhitelist whiteListedPlayers;
/*     */   private final Map<UUID, StatisticsFile> playerStatFiles;
/*     */   private IPlayerFileData playerNBTManagerObj;
/*     */   private boolean whiteListEnforced;
/*     */   protected int maxPlayers;
/*     */   private int viewDistance;
/*     */   private WorldSettings.GameType gameType;
/*     */   private boolean commandsAllowedForAll;
/*     */   private int playerPingIndex;
/*     */   
/*     */   public ServerConfigurationManager(MinecraftServer server) {
/*  89 */     this.bannedPlayers = new UserListBans(FILE_PLAYERBANS);
/*  90 */     this.bannedIPs = new BanList(FILE_IPBANS);
/*  91 */     this.ops = new UserListOps(FILE_OPS);
/*  92 */     this.whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
/*  93 */     this.playerStatFiles = Maps.newHashMap();
/*  94 */     this.mcServer = server;
/*  95 */     this.bannedPlayers.setLanServer(false);
/*  96 */     this.bannedIPs.setLanServer(false);
/*  97 */     this.maxPlayers = 8;
/*     */   }
/*     */   
/*     */   public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn) {
/*     */     ChatComponentTranslation chatcomponenttranslation;
/* 102 */     GameProfile gameprofile = playerIn.getGameProfile();
/* 103 */     PlayerProfileCache playerprofilecache = this.mcServer.getPlayerProfileCache();
/* 104 */     GameProfile gameprofile1 = playerprofilecache.getProfileByUUID(gameprofile.getId());
/* 105 */     String s = (gameprofile1 == null) ? gameprofile.getName() : gameprofile1.getName();
/* 106 */     playerprofilecache.addEntry(gameprofile);
/* 107 */     NBTTagCompound nbttagcompound = readPlayerDataFromFile(playerIn);
/* 108 */     playerIn.setWorld((World)this.mcServer.worldServerForDimension(playerIn.dimension));
/* 109 */     playerIn.theItemInWorldManager.setWorld((WorldServer)playerIn.worldObj);
/* 110 */     String s1 = "local";
/*     */     
/* 112 */     if (netManager.getRemoteAddress() != null)
/*     */     {
/* 114 */       s1 = netManager.getRemoteAddress().toString();
/*     */     }
/*     */     
/* 117 */     logger.info(playerIn.getName() + "[" + s1 + "] logged in with entity id " + playerIn.getEntityId() + " at (" + playerIn.posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")");
/* 118 */     WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
/* 119 */     WorldInfo worldinfo = worldserver.getWorldInfo();
/* 120 */     BlockPos blockpos = worldserver.getSpawnPoint();
/* 121 */     setPlayerGameTypeBasedOnOther(playerIn, (EntityPlayerMP)null, (World)worldserver);
/* 122 */     NetHandlerPlayServer nethandlerplayserver = new NetHandlerPlayServer(this.mcServer, netManager, playerIn);
/* 123 */     nethandlerplayserver.sendPacket((Packet)new S01PacketJoinGame(playerIn.getEntityId(), playerIn.theItemInWorldManager.getGameType(), worldinfo.isHardcoreModeEnabled(), worldserver.provider.getDimensionId(), worldserver.getDifficulty(), getMaxPlayers(), worldinfo.getTerrainType(), worldserver.getGameRules().getBoolean("reducedDebugInfo")));
/* 124 */     nethandlerplayserver.sendPacket((Packet)new S3FPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(getServerInstance().getServerModName())));
/* 125 */     nethandlerplayserver.sendPacket((Packet)new S41PacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
/* 126 */     nethandlerplayserver.sendPacket((Packet)new S05PacketSpawnPosition(blockpos));
/* 127 */     nethandlerplayserver.sendPacket((Packet)new S39PacketPlayerAbilities(playerIn.capabilities));
/* 128 */     nethandlerplayserver.sendPacket((Packet)new S09PacketHeldItemChange(playerIn.inventory.currentItem));
/* 129 */     playerIn.getStatFile().func_150877_d();
/* 130 */     playerIn.getStatFile().sendAchievements(playerIn);
/* 131 */     sendScoreboard((ServerScoreboard)worldserver.getScoreboard(), playerIn);
/* 132 */     this.mcServer.refreshStatusNextTick();
/*     */ 
/*     */     
/* 135 */     if (!playerIn.getName().equalsIgnoreCase(s)) {
/*     */       
/* 137 */       chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined.renamed", new Object[] { playerIn.getDisplayName(), s });
/*     */     }
/*     */     else {
/*     */       
/* 141 */       chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined", new Object[] { playerIn.getDisplayName() });
/*     */     } 
/*     */     
/* 144 */     chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
/* 145 */     sendChatMsg((IChatComponent)chatcomponenttranslation);
/* 146 */     playerLoggedIn(playerIn);
/* 147 */     nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
/* 148 */     updateTimeAndWeatherForPlayer(playerIn, worldserver);
/*     */     
/* 150 */     if (this.mcServer.getResourcePackUrl().length() > 0)
/*     */     {
/* 152 */       playerIn.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
/*     */     }
/*     */     
/* 155 */     for (PotionEffect potioneffect : playerIn.getActivePotionEffects())
/*     */     {
/* 157 */       nethandlerplayserver.sendPacket((Packet)new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect));
/*     */     }
/*     */     
/* 160 */     playerIn.addSelfToInternalCraftingInventory();
/*     */     
/* 162 */     if (nbttagcompound != null && nbttagcompound.hasKey("Riding", 10)) {
/*     */       
/* 164 */       Entity entity = EntityList.createEntityFromNBT(nbttagcompound.getCompoundTag("Riding"), (World)worldserver);
/*     */       
/* 166 */       if (entity != null) {
/*     */         
/* 168 */         entity.forceSpawn = true;
/* 169 */         worldserver.spawnEntityInWorld(entity);
/* 170 */         playerIn.mountEntity(entity);
/* 171 */         entity.forceSpawn = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendScoreboard(ServerScoreboard scoreboardIn, EntityPlayerMP playerIn) {
/* 178 */     Set<ScoreObjective> set = Sets.newHashSet();
/*     */     
/* 180 */     for (ScorePlayerTeam scoreplayerteam : scoreboardIn.getTeams())
/*     */     {
/* 182 */       playerIn.playerNetServerHandler.sendPacket((Packet)new S3EPacketTeams(scoreplayerteam, 0));
/*     */     }
/*     */     
/* 185 */     for (int i = 0; i < 19; i++) {
/*     */       
/* 187 */       ScoreObjective scoreobjective = scoreboardIn.getObjectiveInDisplaySlot(i);
/*     */       
/* 189 */       if (scoreobjective != null && !set.contains(scoreobjective)) {
/*     */         
/* 191 */         for (Packet packet : scoreboardIn.func_96550_d(scoreobjective))
/*     */         {
/* 193 */           playerIn.playerNetServerHandler.sendPacket(packet);
/*     */         }
/*     */         
/* 196 */         set.add(scoreobjective);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlayerManager(WorldServer[] worldServers) {
/* 203 */     this.playerNBTManagerObj = worldServers[0].getSaveHandler().getPlayerNBTManager();
/* 204 */     worldServers[0].getWorldBorder().addListener(new IBorderListener()
/*     */         {
/*     */           public void onSizeChanged(WorldBorder border, double newSize)
/*     */           {
/* 208 */             ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_SIZE));
/*     */           }
/*     */           
/*     */           public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time) {
/* 212 */             ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.LERP_SIZE));
/*     */           }
/*     */           
/*     */           public void onCenterChanged(WorldBorder border, double x, double z) {
/* 216 */             ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_CENTER));
/*     */           }
/*     */           
/*     */           public void onWarningTimeChanged(WorldBorder border, int newTime) {
/* 220 */             ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_TIME));
/*     */           }
/*     */           
/*     */           public void onWarningDistanceChanged(WorldBorder border, int newDistance) {
/* 224 */             ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
/*     */           }
/*     */ 
/*     */           
/*     */           public void onDamageAmountChanged(WorldBorder border, double newAmount) {}
/*     */ 
/*     */           
/*     */           public void onDamageBufferChanged(WorldBorder border, double newSize) {}
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void preparePlayer(EntityPlayerMP playerIn, WorldServer worldIn) {
/* 237 */     WorldServer worldserver = playerIn.getServerForPlayer();
/*     */     
/* 239 */     if (worldIn != null)
/*     */     {
/* 241 */       worldIn.getPlayerManager().removePlayer(playerIn);
/*     */     }
/*     */     
/* 244 */     worldserver.getPlayerManager().addPlayer(playerIn);
/* 245 */     worldserver.theChunkProviderServer.loadChunk((int)playerIn.posX >> 4, (int)playerIn.posZ >> 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityViewDistance() {
/* 250 */     return PlayerManager.getFurthestViewableBlock(getViewDistance());
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP playerIn) {
/* 255 */     NBTTagCompound nbttagcompound1, nbttagcompound = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
/*     */ 
/*     */     
/* 258 */     if (playerIn.getName().equals(this.mcServer.getServerOwner()) && nbttagcompound != null) {
/*     */       
/* 260 */       playerIn.readFromNBT(nbttagcompound);
/* 261 */       nbttagcompound1 = nbttagcompound;
/* 262 */       logger.debug("loading single player");
/*     */     }
/*     */     else {
/*     */       
/* 266 */       nbttagcompound1 = this.playerNBTManagerObj.readPlayerData((EntityPlayer)playerIn);
/*     */     } 
/*     */     
/* 269 */     return nbttagcompound1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writePlayerData(EntityPlayerMP playerIn) {
/* 274 */     this.playerNBTManagerObj.writePlayerData((EntityPlayer)playerIn);
/* 275 */     StatisticsFile statisticsfile = this.playerStatFiles.get(playerIn.getUniqueID());
/*     */     
/* 277 */     if (statisticsfile != null)
/*     */     {
/* 279 */       statisticsfile.saveStatFile();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerLoggedIn(EntityPlayerMP playerIn) {
/* 285 */     this.playerEntityList.add(playerIn);
/* 286 */     this.uuidToPlayerMap.put(playerIn.getUniqueID(), playerIn);
/* 287 */     sendPacketToAllPlayers((Packet)new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { playerIn }));
/* 288 */     WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
/* 289 */     worldserver.spawnEntityInWorld((Entity)playerIn);
/* 290 */     preparePlayer(playerIn, (WorldServer)null);
/*     */     
/* 292 */     for (int i = 0; i < this.playerEntityList.size(); i++) {
/*     */       
/* 294 */       EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
/* 295 */       playerIn.playerNetServerHandler.sendPacket((Packet)new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { entityplayermp }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverUpdateMountedMovingPlayer(EntityPlayerMP playerIn) {
/* 301 */     playerIn.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerLoggedOut(EntityPlayerMP playerIn) {
/* 306 */     playerIn.triggerAchievement(StatList.leaveGameStat);
/* 307 */     writePlayerData(playerIn);
/* 308 */     WorldServer worldserver = playerIn.getServerForPlayer();
/*     */     
/* 310 */     if (playerIn.ridingEntity != null) {
/*     */       
/* 312 */       worldserver.removePlayerEntityDangerously(playerIn.ridingEntity);
/* 313 */       logger.debug("removing player mount");
/*     */     } 
/*     */     
/* 316 */     worldserver.removeEntity((Entity)playerIn);
/* 317 */     worldserver.getPlayerManager().removePlayer(playerIn);
/* 318 */     this.playerEntityList.remove(playerIn);
/* 319 */     UUID uuid = playerIn.getUniqueID();
/* 320 */     EntityPlayerMP entityplayermp = this.uuidToPlayerMap.get(uuid);
/*     */     
/* 322 */     if (entityplayermp == playerIn) {
/*     */       
/* 324 */       this.uuidToPlayerMap.remove(uuid);
/* 325 */       this.playerStatFiles.remove(uuid);
/*     */     } 
/*     */     
/* 328 */     sendPacketToAllPlayers((Packet)new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[] { playerIn }));
/*     */   }
/*     */ 
/*     */   
/*     */   public String allowUserToConnect(SocketAddress address, GameProfile profile) {
/* 333 */     if (this.bannedPlayers.isBanned(profile)) {
/*     */       
/* 335 */       UserListBansEntry userlistbansentry = this.bannedPlayers.getEntry(profile);
/* 336 */       String s1 = "You are banned from this server!\nReason: " + userlistbansentry.getBanReason();
/*     */       
/* 338 */       if (userlistbansentry.getBanEndDate() != null)
/*     */       {
/* 340 */         s1 = s1 + "\nYour ban will be removed on " + dateFormat.format(userlistbansentry.getBanEndDate());
/*     */       }
/*     */       
/* 343 */       return s1;
/*     */     } 
/* 345 */     if (!canJoin(profile))
/*     */     {
/* 347 */       return "You are not white-listed on this server!";
/*     */     }
/* 349 */     if (this.bannedIPs.isBanned(address)) {
/*     */       
/* 351 */       IPBanEntry ipbanentry = this.bannedIPs.getBanEntry(address);
/* 352 */       String s = "Your IP address is banned from this server!\nReason: " + ipbanentry.getBanReason();
/*     */       
/* 354 */       if (ipbanentry.getBanEndDate() != null)
/*     */       {
/* 356 */         s = s + "\nYour ban will be removed on " + dateFormat.format(ipbanentry.getBanEndDate());
/*     */       }
/*     */       
/* 359 */       return s;
/*     */     } 
/*     */ 
/*     */     
/* 363 */     return (this.playerEntityList.size() >= this.maxPlayers && !bypassesPlayerLimit(profile)) ? "The server is full!" : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityPlayerMP createPlayerForUser(GameProfile profile) {
/*     */     ItemInWorldManager iteminworldmanager;
/* 369 */     UUID uuid = EntityPlayer.getUUID(profile);
/* 370 */     List<EntityPlayerMP> list = Lists.newArrayList();
/*     */     
/* 372 */     for (int i = 0; i < this.playerEntityList.size(); i++) {
/*     */       
/* 374 */       EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
/*     */       
/* 376 */       if (entityplayermp.getUniqueID().equals(uuid))
/*     */       {
/* 378 */         list.add(entityplayermp);
/*     */       }
/*     */     } 
/*     */     
/* 382 */     EntityPlayerMP entityplayermp2 = this.uuidToPlayerMap.get(profile.getId());
/*     */     
/* 384 */     if (entityplayermp2 != null && !list.contains(entityplayermp2))
/*     */     {
/* 386 */       list.add(entityplayermp2);
/*     */     }
/*     */     
/* 389 */     for (EntityPlayerMP entityplayermp1 : list)
/*     */     {
/* 391 */       entityplayermp1.playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 396 */     if (this.mcServer.isDemo()) {
/*     */       
/* 398 */       DemoWorldManager demoWorldManager = new DemoWorldManager((World)this.mcServer.worldServerForDimension(0));
/*     */     }
/*     */     else {
/*     */       
/* 402 */       iteminworldmanager = new ItemInWorldManager((World)this.mcServer.worldServerForDimension(0));
/*     */     } 
/*     */     
/* 405 */     return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), profile, iteminworldmanager);
/*     */   }
/*     */   
/*     */   public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd) {
/*     */     ItemInWorldManager iteminworldmanager;
/* 410 */     playerIn.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(playerIn);
/* 411 */     playerIn.getServerForPlayer().getEntityTracker().untrackEntity((Entity)playerIn);
/* 412 */     playerIn.getServerForPlayer().getPlayerManager().removePlayer(playerIn);
/* 413 */     this.playerEntityList.remove(playerIn);
/* 414 */     this.mcServer.worldServerForDimension(playerIn.dimension).removePlayerEntityDangerously((Entity)playerIn);
/* 415 */     BlockPos blockpos = playerIn.getBedLocation();
/* 416 */     boolean flag = playerIn.isSpawnForced();
/* 417 */     playerIn.dimension = dimension;
/*     */ 
/*     */     
/* 420 */     if (this.mcServer.isDemo()) {
/*     */       
/* 422 */       DemoWorldManager demoWorldManager = new DemoWorldManager((World)this.mcServer.worldServerForDimension(playerIn.dimension));
/*     */     }
/*     */     else {
/*     */       
/* 426 */       iteminworldmanager = new ItemInWorldManager((World)this.mcServer.worldServerForDimension(playerIn.dimension));
/*     */     } 
/*     */     
/* 429 */     EntityPlayerMP entityplayermp = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(playerIn.dimension), playerIn.getGameProfile(), iteminworldmanager);
/* 430 */     entityplayermp.playerNetServerHandler = playerIn.playerNetServerHandler;
/* 431 */     entityplayermp.clonePlayer((EntityPlayer)playerIn, conqueredEnd);
/* 432 */     entityplayermp.setEntityId(playerIn.getEntityId());
/* 433 */     entityplayermp.setCommandStats((Entity)playerIn);
/* 434 */     WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
/* 435 */     setPlayerGameTypeBasedOnOther(entityplayermp, playerIn, (World)worldserver);
/*     */     
/* 437 */     if (blockpos != null) {
/*     */       
/* 439 */       BlockPos blockpos1 = EntityPlayer.getBedSpawnLocation((World)this.mcServer.worldServerForDimension(playerIn.dimension), blockpos, flag);
/*     */       
/* 441 */       if (blockpos1 != null) {
/*     */         
/* 443 */         entityplayermp.setLocationAndAngles((blockpos1.getX() + 0.5F), (blockpos1.getY() + 0.1F), (blockpos1.getZ() + 0.5F), 0.0F, 0.0F);
/* 444 */         entityplayermp.setSpawnPoint(blockpos, flag);
/*     */       }
/*     */       else {
/*     */         
/* 448 */         entityplayermp.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(0, 0.0F));
/*     */       } 
/*     */     } 
/*     */     
/* 452 */     worldserver.theChunkProviderServer.loadChunk((int)entityplayermp.posX >> 4, (int)entityplayermp.posZ >> 4);
/*     */     
/* 454 */     while (!worldserver.getCollidingBoundingBoxes((Entity)entityplayermp, entityplayermp.getEntityBoundingBox()).isEmpty() && entityplayermp.posY < 256.0D)
/*     */     {
/* 456 */       entityplayermp.setPosition(entityplayermp.posX, entityplayermp.posY + 1.0D, entityplayermp.posZ);
/*     */     }
/*     */     
/* 459 */     entityplayermp.playerNetServerHandler.sendPacket((Packet)new S07PacketRespawn(entityplayermp.dimension, entityplayermp.worldObj.getDifficulty(), entityplayermp.worldObj.getWorldInfo().getTerrainType(), entityplayermp.theItemInWorldManager.getGameType()));
/* 460 */     BlockPos blockpos2 = worldserver.getSpawnPoint();
/* 461 */     entityplayermp.playerNetServerHandler.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
/* 462 */     entityplayermp.playerNetServerHandler.sendPacket((Packet)new S05PacketSpawnPosition(blockpos2));
/* 463 */     entityplayermp.playerNetServerHandler.sendPacket((Packet)new S1FPacketSetExperience(entityplayermp.experience, entityplayermp.experienceTotal, entityplayermp.experienceLevel));
/* 464 */     updateTimeAndWeatherForPlayer(entityplayermp, worldserver);
/* 465 */     worldserver.getPlayerManager().addPlayer(entityplayermp);
/* 466 */     worldserver.spawnEntityInWorld((Entity)entityplayermp);
/* 467 */     this.playerEntityList.add(entityplayermp);
/* 468 */     this.uuidToPlayerMap.put(entityplayermp.getUniqueID(), entityplayermp);
/* 469 */     entityplayermp.addSelfToInternalCraftingInventory();
/* 470 */     entityplayermp.setHealth(entityplayermp.getHealth());
/* 471 */     return entityplayermp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void transferPlayerToDimension(EntityPlayerMP playerIn, int dimension) {
/* 476 */     int i = playerIn.dimension;
/* 477 */     WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
/* 478 */     playerIn.dimension = dimension;
/* 479 */     WorldServer worldserver1 = this.mcServer.worldServerForDimension(playerIn.dimension);
/* 480 */     playerIn.playerNetServerHandler.sendPacket((Packet)new S07PacketRespawn(playerIn.dimension, playerIn.worldObj.getDifficulty(), playerIn.worldObj.getWorldInfo().getTerrainType(), playerIn.theItemInWorldManager.getGameType()));
/* 481 */     worldserver.removePlayerEntityDangerously((Entity)playerIn);
/* 482 */     playerIn.isDead = false;
/* 483 */     transferEntityToWorld((Entity)playerIn, i, worldserver, worldserver1);
/* 484 */     preparePlayer(playerIn, worldserver);
/* 485 */     playerIn.playerNetServerHandler.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
/* 486 */     playerIn.theItemInWorldManager.setWorld(worldserver1);
/* 487 */     updateTimeAndWeatherForPlayer(playerIn, worldserver1);
/* 488 */     syncPlayerInventory(playerIn);
/*     */     
/* 490 */     for (PotionEffect potioneffect : playerIn.getActivePotionEffects())
/*     */     {
/* 492 */       playerIn.playerNetServerHandler.sendPacket((Packet)new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void transferEntityToWorld(Entity entityIn, int p_82448_2_, WorldServer oldWorldIn, WorldServer toWorldIn) {
/* 498 */     double d0 = entityIn.posX;
/* 499 */     double d1 = entityIn.posZ;
/* 500 */     double d2 = 8.0D;
/* 501 */     float f = entityIn.rotationYaw;
/* 502 */     oldWorldIn.theProfiler.startSection("moving");
/*     */     
/* 504 */     if (entityIn.dimension == -1) {
/*     */       
/* 506 */       d0 = MathHelper.clamp_double(d0 / d2, toWorldIn.getWorldBorder().minX() + 16.0D, toWorldIn.getWorldBorder().maxX() - 16.0D);
/* 507 */       d1 = MathHelper.clamp_double(d1 / d2, toWorldIn.getWorldBorder().minZ() + 16.0D, toWorldIn.getWorldBorder().maxZ() - 16.0D);
/* 508 */       entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
/*     */       
/* 510 */       if (entityIn.isEntityAlive())
/*     */       {
/* 512 */         oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
/*     */       }
/*     */     }
/* 515 */     else if (entityIn.dimension == 0) {
/*     */       
/* 517 */       d0 = MathHelper.clamp_double(d0 * d2, toWorldIn.getWorldBorder().minX() + 16.0D, toWorldIn.getWorldBorder().maxX() - 16.0D);
/* 518 */       d1 = MathHelper.clamp_double(d1 * d2, toWorldIn.getWorldBorder().minZ() + 16.0D, toWorldIn.getWorldBorder().maxZ() - 16.0D);
/* 519 */       entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
/*     */       
/* 521 */       if (entityIn.isEntityAlive())
/*     */       {
/* 523 */         oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
/*     */       }
/*     */     } else {
/*     */       BlockPos blockpos;
/*     */ 
/*     */ 
/*     */       
/* 530 */       if (p_82448_2_ == 1) {
/*     */         
/* 532 */         blockpos = toWorldIn.getSpawnPoint();
/*     */       }
/*     */       else {
/*     */         
/* 536 */         blockpos = toWorldIn.getSpawnCoordinate();
/*     */       } 
/*     */       
/* 539 */       d0 = blockpos.getX();
/* 540 */       entityIn.posY = blockpos.getY();
/* 541 */       d1 = blockpos.getZ();
/* 542 */       entityIn.setLocationAndAngles(d0, entityIn.posY, d1, 90.0F, 0.0F);
/*     */       
/* 544 */       if (entityIn.isEntityAlive())
/*     */       {
/* 546 */         oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
/*     */       }
/*     */     } 
/*     */     
/* 550 */     oldWorldIn.theProfiler.endSection();
/*     */     
/* 552 */     if (p_82448_2_ != 1) {
/*     */       
/* 554 */       oldWorldIn.theProfiler.startSection("placing");
/* 555 */       d0 = MathHelper.clamp_int((int)d0, -29999872, 29999872);
/* 556 */       d1 = MathHelper.clamp_int((int)d1, -29999872, 29999872);
/*     */       
/* 558 */       if (entityIn.isEntityAlive()) {
/*     */         
/* 560 */         entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
/* 561 */         toWorldIn.getDefaultTeleporter().placeInPortal(entityIn, f);
/* 562 */         toWorldIn.spawnEntityInWorld(entityIn);
/* 563 */         toWorldIn.updateEntityWithOptionalForce(entityIn, false);
/*     */       } 
/*     */       
/* 566 */       oldWorldIn.theProfiler.endSection();
/*     */     } 
/*     */     
/* 569 */     entityIn.setWorld((World)toWorldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 574 */     if (++this.playerPingIndex > 600) {
/*     */       
/* 576 */       sendPacketToAllPlayers((Packet)new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
/* 577 */       this.playerPingIndex = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendPacketToAllPlayers(Packet packetIn) {
/* 583 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*     */     {
/* 585 */       ((EntityPlayerMP)this.playerEntityList.get(i)).playerNetServerHandler.sendPacket(packetIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendPacketToAllPlayersInDimension(Packet packetIn, int dimension) {
/* 591 */     for (int i = 0; i < this.playerEntityList.size(); i++) {
/*     */       
/* 593 */       EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
/*     */       
/* 595 */       if (entityplayermp.dimension == dimension)
/*     */       {
/* 597 */         entityplayermp.playerNetServerHandler.sendPacket(packetIn);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendMessageToAllTeamMembers(EntityPlayer player, IChatComponent message) {
/* 604 */     Team team = player.getTeam();
/*     */     
/* 606 */     if (team != null)
/*     */     {
/* 608 */       for (String s : team.getMembershipCollection()) {
/*     */         
/* 610 */         EntityPlayerMP entityplayermp = getPlayerByUsername(s);
/*     */         
/* 612 */         if (entityplayermp != null && entityplayermp != player)
/*     */         {
/* 614 */           entityplayermp.addChatMessage(message);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendMessageToTeamOrEvryPlayer(EntityPlayer player, IChatComponent message) {
/* 622 */     Team team = player.getTeam();
/*     */     
/* 624 */     if (team == null) {
/*     */       
/* 626 */       sendChatMsg(message);
/*     */     }
/*     */     else {
/*     */       
/* 630 */       for (int i = 0; i < this.playerEntityList.size(); i++) {
/*     */         
/* 632 */         EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
/*     */         
/* 634 */         if (entityplayermp.getTeam() != team)
/*     */         {
/* 636 */           entityplayermp.addChatMessage(message);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String func_181058_b(boolean p_181058_1_) {
/* 644 */     String s = "";
/* 645 */     List<EntityPlayerMP> list = Lists.newArrayList(this.playerEntityList);
/*     */     
/* 647 */     for (int i = 0; i < list.size(); i++) {
/*     */       
/* 649 */       if (i > 0)
/*     */       {
/* 651 */         s = s + ", ";
/*     */       }
/*     */       
/* 654 */       s = s + ((EntityPlayerMP)list.get(i)).getName();
/*     */       
/* 656 */       if (p_181058_1_)
/*     */       {
/* 658 */         s = s + " (" + ((EntityPlayerMP)list.get(i)).getUniqueID().toString() + ")";
/*     */       }
/*     */     } 
/*     */     
/* 662 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAllUsernames() {
/* 667 */     String[] astring = new String[this.playerEntityList.size()];
/*     */     
/* 669 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*     */     {
/* 671 */       astring[i] = ((EntityPlayerMP)this.playerEntityList.get(i)).getName();
/*     */     }
/*     */     
/* 674 */     return astring;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile[] getAllProfiles() {
/* 679 */     GameProfile[] agameprofile = new GameProfile[this.playerEntityList.size()];
/*     */     
/* 681 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*     */     {
/* 683 */       agameprofile[i] = ((EntityPlayerMP)this.playerEntityList.get(i)).getGameProfile();
/*     */     }
/*     */     
/* 686 */     return agameprofile;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserListBans getBannedPlayers() {
/* 691 */     return this.bannedPlayers;
/*     */   }
/*     */ 
/*     */   
/*     */   public BanList getBannedIPs() {
/* 696 */     return this.bannedIPs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOp(GameProfile profile) {
/* 701 */     this.ops.addEntry(new UserListOpsEntry(profile, this.mcServer.getOpPermissionLevel(), this.ops.bypassesPlayerLimit(profile)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeOp(GameProfile profile) {
/* 706 */     this.ops.removeEntry(profile);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canJoin(GameProfile profile) {
/* 711 */     return (!this.whiteListEnforced || this.ops.hasEntry(profile) || this.whiteListedPlayers.hasEntry(profile));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSendCommands(GameProfile profile) {
/* 716 */     return (this.ops.hasEntry(profile) || (this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(profile.getName())) || this.commandsAllowedForAll);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityPlayerMP getPlayerByUsername(String username) {
/* 721 */     for (EntityPlayerMP entityplayermp : this.playerEntityList) {
/*     */       
/* 723 */       if (entityplayermp.getName().equalsIgnoreCase(username))
/*     */       {
/* 725 */         return entityplayermp;
/*     */       }
/*     */     } 
/*     */     
/* 729 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendToAllNear(double x, double y, double z, double radius, int dimension, Packet packetIn) {
/* 734 */     sendToAllNearExcept((EntityPlayer)null, x, y, z, radius, dimension, packetIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendToAllNearExcept(EntityPlayer p_148543_1_, double x, double y, double z, double radius, int dimension, Packet p_148543_11_) {
/* 739 */     for (int i = 0; i < this.playerEntityList.size(); i++) {
/*     */       
/* 741 */       EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
/*     */       
/* 743 */       if (entityplayermp != p_148543_1_ && entityplayermp.dimension == dimension) {
/*     */         
/* 745 */         double d0 = x - entityplayermp.posX;
/* 746 */         double d1 = y - entityplayermp.posY;
/* 747 */         double d2 = z - entityplayermp.posZ;
/*     */         
/* 749 */         if (d0 * d0 + d1 * d1 + d2 * d2 < radius * radius)
/*     */         {
/* 751 */           entityplayermp.playerNetServerHandler.sendPacket(p_148543_11_);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveAllPlayerData() {
/* 759 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*     */     {
/* 761 */       writePlayerData(this.playerEntityList.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addWhitelistedPlayer(GameProfile profile) {
/* 767 */     this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(profile));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayerFromWhitelist(GameProfile profile) {
/* 772 */     this.whiteListedPlayers.removeEntry(profile);
/*     */   }
/*     */ 
/*     */   
/*     */   public UserListWhitelist getWhitelistedPlayers() {
/* 777 */     return this.whiteListedPlayers;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getWhitelistedPlayerNames() {
/* 782 */     return this.whiteListedPlayers.getKeys();
/*     */   }
/*     */ 
/*     */   
/*     */   public UserListOps getOppedPlayers() {
/* 787 */     return this.ops;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getOppedPlayerNames() {
/* 792 */     return this.ops.getKeys();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadWhiteList() {}
/*     */ 
/*     */   
/*     */   public void updateTimeAndWeatherForPlayer(EntityPlayerMP playerIn, WorldServer worldIn) {
/* 801 */     WorldBorder worldborder = this.mcServer.worldServers[0].getWorldBorder();
/* 802 */     playerIn.playerNetServerHandler.sendPacket((Packet)new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.INITIALIZE));
/* 803 */     playerIn.playerNetServerHandler.sendPacket((Packet)new S03PacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getBoolean("doDaylightCycle")));
/*     */     
/* 805 */     if (worldIn.isRaining()) {
/*     */       
/* 807 */       playerIn.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(1, 0.0F));
/* 808 */       playerIn.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(7, worldIn.getRainStrength(1.0F)));
/* 809 */       playerIn.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(8, worldIn.getThunderStrength(1.0F)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void syncPlayerInventory(EntityPlayerMP playerIn) {
/* 815 */     playerIn.sendContainerToPlayer(playerIn.inventoryContainer);
/* 816 */     playerIn.setPlayerHealthUpdated();
/* 817 */     playerIn.playerNetServerHandler.sendPacket((Packet)new S09PacketHeldItemChange(playerIn.inventory.currentItem));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCurrentPlayerCount() {
/* 822 */     return this.playerEntityList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPlayers() {
/* 827 */     return this.maxPlayers;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAvailablePlayerDat() {
/* 832 */     return this.mcServer.worldServers[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWhiteListEnabled(boolean whitelistEnabled) {
/* 837 */     this.whiteListEnforced = whitelistEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<EntityPlayerMP> getPlayersMatchingAddress(String address) {
/* 842 */     List<EntityPlayerMP> list = Lists.newArrayList();
/*     */     
/* 844 */     for (EntityPlayerMP entityplayermp : this.playerEntityList) {
/*     */       
/* 846 */       if (entityplayermp.getPlayerIP().equals(address))
/*     */       {
/* 848 */         list.add(entityplayermp);
/*     */       }
/*     */     } 
/*     */     
/* 852 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getViewDistance() {
/* 857 */     return this.viewDistance;
/*     */   }
/*     */ 
/*     */   
/*     */   public MinecraftServer getServerInstance() {
/* 862 */     return this.mcServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getHostPlayerData() {
/* 867 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameType(WorldSettings.GameType p_152604_1_) {
/* 872 */     this.gameType = p_152604_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setPlayerGameTypeBasedOnOther(EntityPlayerMP p_72381_1_, EntityPlayerMP p_72381_2_, World worldIn) {
/* 877 */     if (p_72381_2_ != null) {
/*     */       
/* 879 */       p_72381_1_.theItemInWorldManager.setGameType(p_72381_2_.theItemInWorldManager.getGameType());
/*     */     }
/* 881 */     else if (this.gameType != null) {
/*     */       
/* 883 */       p_72381_1_.theItemInWorldManager.setGameType(this.gameType);
/*     */     } 
/*     */     
/* 886 */     p_72381_1_.theItemInWorldManager.initializeGameType(worldIn.getWorldInfo().getGameType());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCommandsAllowedForAll(boolean p_72387_1_) {
/* 891 */     this.commandsAllowedForAll = p_72387_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAllPlayers() {
/* 896 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*     */     {
/* 898 */       ((EntityPlayerMP)this.playerEntityList.get(i)).playerNetServerHandler.kickPlayerFromServer("Server closed");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendChatMsgImpl(IChatComponent component, boolean isChat) {
/* 904 */     this.mcServer.addChatMessage(component);
/* 905 */     byte b0 = (byte)(isChat ? 1 : 0);
/* 906 */     sendPacketToAllPlayers((Packet)new S02PacketChat(component, b0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendChatMsg(IChatComponent component) {
/* 911 */     sendChatMsgImpl(component, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public StatisticsFile getPlayerStatsFile(EntityPlayer playerIn) {
/* 916 */     UUID uuid = playerIn.getUniqueID();
/* 917 */     StatisticsFile statisticsfile = (uuid == null) ? null : this.playerStatFiles.get(uuid);
/*     */     
/* 919 */     if (statisticsfile == null) {
/*     */       
/* 921 */       File file1 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
/* 922 */       File file2 = new File(file1, uuid.toString() + ".json");
/*     */       
/* 924 */       if (!file2.exists()) {
/*     */         
/* 926 */         File file3 = new File(file1, playerIn.getName() + ".json");
/*     */         
/* 928 */         if (file3.exists() && file3.isFile())
/*     */         {
/* 930 */           file3.renameTo(file2);
/*     */         }
/*     */       } 
/*     */       
/* 934 */       statisticsfile = new StatisticsFile(this.mcServer, file2);
/* 935 */       statisticsfile.readStatFile();
/* 936 */       this.playerStatFiles.put(uuid, statisticsfile);
/*     */     } 
/*     */     
/* 939 */     return statisticsfile;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setViewDistance(int distance) {
/* 944 */     this.viewDistance = distance;
/*     */     
/* 946 */     if (this.mcServer.worldServers != null)
/*     */     {
/* 948 */       for (WorldServer worldserver : this.mcServer.worldServers) {
/*     */         
/* 950 */         if (worldserver != null)
/*     */         {
/* 952 */           worldserver.getPlayerManager().setPlayerViewRadius(distance);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<EntityPlayerMP> getPlayerList() {
/* 960 */     return this.playerEntityList;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityPlayerMP getPlayerByUUID(UUID playerUUID) {
/* 965 */     return this.uuidToPlayerMap.get(playerUUID);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean bypassesPlayerLimit(GameProfile p_183023_1_) {
/* 970 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\management\ServerConfigurationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */