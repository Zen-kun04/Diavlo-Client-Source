/*     */ package net.minecraft.server.management;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S21PacketChunkData;
/*     */ import net.minecraft.network.play.server.S22PacketMultiBlockChange;
/*     */ import net.minecraft.network.play.server.S23PacketBlockChange;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.optifine.ChunkPosComparator;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class PlayerManager
/*     */ {
/*  34 */   private static final Logger pmLogger = LogManager.getLogger();
/*     */   private final WorldServer theWorldServer;
/*  36 */   private final List<EntityPlayerMP> players = Lists.newArrayList();
/*  37 */   private final LongHashMap<PlayerInstance> playerInstances = new LongHashMap();
/*  38 */   private final List<PlayerInstance> playerInstancesToUpdate = Lists.newArrayList();
/*  39 */   private final List<PlayerInstance> playerInstanceList = Lists.newArrayList();
/*     */   private int playerViewRadius;
/*     */   private long previousTotalWorldTime;
/*  42 */   private final int[][] xzDirectionsConst = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
/*  43 */   private final Map<EntityPlayerMP, Set<ChunkCoordIntPair>> mapPlayerPendingEntries = new HashMap<>();
/*     */ 
/*     */   
/*     */   public PlayerManager(WorldServer serverWorld) {
/*  47 */     this.theWorldServer = serverWorld;
/*  48 */     setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldServer getWorldServer() {
/*  53 */     return this.theWorldServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePlayerInstances() {
/*  58 */     Set<Map.Entry<EntityPlayerMP, Set<ChunkCoordIntPair>>> set = this.mapPlayerPendingEntries.entrySet();
/*  59 */     Iterator<Map.Entry<EntityPlayerMP, Set<ChunkCoordIntPair>>> iterator = set.iterator();
/*     */     
/*  61 */     while (iterator.hasNext()) {
/*     */       
/*  63 */       Map.Entry<EntityPlayerMP, Set<ChunkCoordIntPair>> entry = iterator.next();
/*  64 */       Set<ChunkCoordIntPair> set1 = entry.getValue();
/*     */       
/*  66 */       if (!set1.isEmpty()) {
/*     */         
/*  68 */         EntityPlayerMP entityplayermp = entry.getKey();
/*     */         
/*  70 */         if (entityplayermp.worldObj != this.theWorldServer) {
/*     */           
/*  72 */           iterator.remove();
/*     */           
/*     */           continue;
/*     */         } 
/*  76 */         int i = this.playerViewRadius / 3 + 1;
/*     */         
/*  78 */         if (!Config.isLazyChunkLoading())
/*     */         {
/*  80 */           i = this.playerViewRadius * 2 + 1;
/*     */         }
/*     */         
/*  83 */         for (ChunkCoordIntPair chunkcoordintpair : getNearest(set1, entityplayermp, i)) {
/*     */           
/*  85 */           PlayerInstance playermanager$playerinstance = getPlayerInstance(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos, true);
/*  86 */           playermanager$playerinstance.addPlayer(entityplayermp);
/*  87 */           set1.remove(chunkcoordintpair);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  93 */     long j = this.theWorldServer.getTotalWorldTime();
/*     */     
/*  95 */     if (j - this.previousTotalWorldTime > 8000L) {
/*     */       
/*  97 */       this.previousTotalWorldTime = j;
/*     */       
/*  99 */       for (int k = 0; k < this.playerInstanceList.size(); k++)
/*     */       {
/* 101 */         PlayerInstance playermanager$playerinstance1 = this.playerInstanceList.get(k);
/* 102 */         playermanager$playerinstance1.onUpdate();
/* 103 */         playermanager$playerinstance1.processChunk();
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 108 */       for (int l = 0; l < this.playerInstancesToUpdate.size(); l++) {
/*     */         
/* 110 */         PlayerInstance playermanager$playerinstance2 = this.playerInstancesToUpdate.get(l);
/* 111 */         playermanager$playerinstance2.onUpdate();
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     this.playerInstancesToUpdate.clear();
/*     */     
/* 117 */     if (this.players.isEmpty()) {
/*     */       
/* 119 */       WorldProvider worldprovider = this.theWorldServer.provider;
/*     */       
/* 121 */       if (!worldprovider.canRespawnHere())
/*     */       {
/* 123 */         this.theWorldServer.theChunkProviderServer.unloadAllChunks();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPlayerInstance(int chunkX, int chunkZ) {
/* 130 */     long i = chunkX + 2147483647L | chunkZ + 2147483647L << 32L;
/* 131 */     return (this.playerInstances.getValueByKey(i) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private PlayerInstance getPlayerInstance(int chunkX, int chunkZ, boolean createIfAbsent) {
/* 136 */     long i = chunkX + 2147483647L | chunkZ + 2147483647L << 32L;
/* 137 */     PlayerInstance playermanager$playerinstance = (PlayerInstance)this.playerInstances.getValueByKey(i);
/*     */     
/* 139 */     if (playermanager$playerinstance == null && createIfAbsent) {
/*     */       
/* 141 */       playermanager$playerinstance = new PlayerInstance(chunkX, chunkZ);
/* 142 */       this.playerInstances.add(i, playermanager$playerinstance);
/* 143 */       this.playerInstanceList.add(playermanager$playerinstance);
/*     */     } 
/*     */     
/* 146 */     return playermanager$playerinstance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markBlockForUpdate(BlockPos pos) {
/* 151 */     int i = pos.getX() >> 4;
/* 152 */     int j = pos.getZ() >> 4;
/* 153 */     PlayerInstance playermanager$playerinstance = getPlayerInstance(i, j, false);
/*     */     
/* 155 */     if (playermanager$playerinstance != null)
/*     */     {
/* 157 */       playermanager$playerinstance.flagChunkForUpdate(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPlayer(EntityPlayerMP player) {
/* 163 */     int i = (int)player.posX >> 4;
/* 164 */     int j = (int)player.posZ >> 4;
/* 165 */     player.managedPosX = player.posX;
/* 166 */     player.managedPosZ = player.posZ;
/* 167 */     int k = Math.min(this.playerViewRadius, 8);
/* 168 */     int l = i - k;
/* 169 */     int i1 = i + k;
/* 170 */     int j1 = j - k;
/* 171 */     int k1 = j + k;
/* 172 */     Set<ChunkCoordIntPair> set = getPendingEntriesSafe(player);
/*     */     
/* 174 */     for (int l1 = i - this.playerViewRadius; l1 <= i + this.playerViewRadius; l1++) {
/*     */       
/* 176 */       for (int i2 = j - this.playerViewRadius; i2 <= j + this.playerViewRadius; i2++) {
/*     */         
/* 178 */         if (l1 >= l && l1 <= i1 && i2 >= j1 && i2 <= k1) {
/*     */           
/* 180 */           getPlayerInstance(l1, i2, true).addPlayer(player);
/*     */         }
/*     */         else {
/*     */           
/* 184 */           set.add(new ChunkCoordIntPair(l1, i2));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 189 */     this.players.add(player);
/* 190 */     filterChunkLoadQueue(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void filterChunkLoadQueue(EntityPlayerMP player) {
/* 195 */     List<ChunkCoordIntPair> list = Lists.newArrayList(player.loadedChunks);
/* 196 */     int i = 0;
/* 197 */     int j = this.playerViewRadius;
/* 198 */     int k = (int)player.posX >> 4;
/* 199 */     int l = (int)player.posZ >> 4;
/* 200 */     int i1 = 0;
/* 201 */     int j1 = 0;
/* 202 */     ChunkCoordIntPair chunkcoordintpair = (getPlayerInstance(k, l, true)).chunkCoords;
/* 203 */     player.loadedChunks.clear();
/*     */     
/* 205 */     if (list.contains(chunkcoordintpair))
/*     */     {
/* 207 */       player.loadedChunks.add(chunkcoordintpair);
/*     */     }
/*     */     
/* 210 */     for (int k1 = 1; k1 <= j * 2; k1++) {
/*     */       
/* 212 */       for (int l1 = 0; l1 < 2; l1++) {
/*     */         
/* 214 */         int[] aint = this.xzDirectionsConst[i++ % 4];
/*     */         
/* 216 */         for (int i2 = 0; i2 < k1; i2++) {
/*     */           
/* 218 */           i1 += aint[0];
/* 219 */           j1 += aint[1];
/* 220 */           chunkcoordintpair = (getPlayerInstance(k + i1, l + j1, true)).chunkCoords;
/*     */           
/* 222 */           if (list.contains(chunkcoordintpair))
/*     */           {
/* 224 */             player.loadedChunks.add(chunkcoordintpair);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 230 */     i %= 4;
/*     */     
/* 232 */     for (int j2 = 0; j2 < j * 2; j2++) {
/*     */       
/* 234 */       i1 += this.xzDirectionsConst[i][0];
/* 235 */       j1 += this.xzDirectionsConst[i][1];
/* 236 */       chunkcoordintpair = (getPlayerInstance(k + i1, l + j1, true)).chunkCoords;
/*     */       
/* 238 */       if (list.contains(chunkcoordintpair))
/*     */       {
/* 240 */         player.loadedChunks.add(chunkcoordintpair);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayer(EntityPlayerMP player) {
/* 247 */     this.mapPlayerPendingEntries.remove(player);
/* 248 */     int i = (int)player.managedPosX >> 4;
/* 249 */     int j = (int)player.managedPosZ >> 4;
/*     */     
/* 251 */     for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; k++) {
/*     */       
/* 253 */       for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; l++) {
/*     */         
/* 255 */         PlayerInstance playermanager$playerinstance = getPlayerInstance(k, l, false);
/*     */         
/* 257 */         if (playermanager$playerinstance != null)
/*     */         {
/* 259 */           playermanager$playerinstance.removePlayer(player);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 264 */     this.players.remove(player);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean overlaps(int x1, int z1, int x2, int z2, int radius) {
/* 269 */     int i = x1 - x2;
/* 270 */     int j = z1 - z2;
/* 271 */     return (i >= -radius && i <= radius) ? ((j >= -radius && j <= radius)) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateMountedMovingPlayer(EntityPlayerMP player) {
/* 276 */     int i = (int)player.posX >> 4;
/* 277 */     int j = (int)player.posZ >> 4;
/* 278 */     double d0 = player.managedPosX - player.posX;
/* 279 */     double d1 = player.managedPosZ - player.posZ;
/* 280 */     double d2 = d0 * d0 + d1 * d1;
/*     */     
/* 282 */     if (d2 >= 64.0D) {
/*     */       
/* 284 */       int k = (int)player.managedPosX >> 4;
/* 285 */       int l = (int)player.managedPosZ >> 4;
/* 286 */       int i1 = this.playerViewRadius;
/* 287 */       int j1 = i - k;
/* 288 */       int k1 = j - l;
/*     */       
/* 290 */       if (j1 != 0 || k1 != 0) {
/*     */         
/* 292 */         Set<ChunkCoordIntPair> set = getPendingEntriesSafe(player);
/*     */         
/* 294 */         for (int l1 = i - i1; l1 <= i + i1; l1++) {
/*     */           
/* 296 */           for (int i2 = j - i1; i2 <= j + i1; i2++) {
/*     */             
/* 298 */             if (!overlaps(l1, i2, k, l, i1))
/*     */             {
/* 300 */               if (Config.isLazyChunkLoading()) {
/*     */                 
/* 302 */                 set.add(new ChunkCoordIntPair(l1, i2));
/*     */               }
/*     */               else {
/*     */                 
/* 306 */                 getPlayerInstance(l1, i2, true).addPlayer(player);
/*     */               } 
/*     */             }
/*     */             
/* 310 */             if (!overlaps(l1 - j1, i2 - k1, i, j, i1)) {
/*     */               
/* 312 */               set.remove(new ChunkCoordIntPair(l1 - j1, i2 - k1));
/* 313 */               PlayerInstance playermanager$playerinstance = getPlayerInstance(l1 - j1, i2 - k1, false);
/*     */               
/* 315 */               if (playermanager$playerinstance != null)
/*     */               {
/* 317 */                 playermanager$playerinstance.removePlayer(player);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 323 */         filterChunkLoadQueue(player);
/* 324 */         player.managedPosX = player.posX;
/* 325 */         player.managedPosZ = player.posZ;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ) {
/* 332 */     PlayerInstance playermanager$playerinstance = getPlayerInstance(chunkX, chunkZ, false);
/* 333 */     return (playermanager$playerinstance != null && playermanager$playerinstance.playersWatchingChunk.contains(player) && !player.loadedChunks.contains(playermanager$playerinstance.chunkCoords));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlayerViewRadius(int radius) {
/* 338 */     radius = MathHelper.clamp_int(radius, 3, 64);
/*     */     
/* 340 */     if (radius != this.playerViewRadius) {
/*     */       
/* 342 */       int i = radius - this.playerViewRadius;
/*     */       
/* 344 */       for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players)) {
/*     */         
/* 346 */         int j = (int)entityplayermp.posX >> 4;
/* 347 */         int k = (int)entityplayermp.posZ >> 4;
/* 348 */         Set<ChunkCoordIntPair> set = getPendingEntriesSafe(entityplayermp);
/*     */         
/* 350 */         if (i > 0) {
/*     */           
/* 352 */           for (int j1 = j - radius; j1 <= j + radius; j1++) {
/*     */             
/* 354 */             for (int k1 = k - radius; k1 <= k + radius; k1++) {
/*     */               
/* 356 */               if (Config.isLazyChunkLoading()) {
/*     */                 
/* 358 */                 set.add(new ChunkCoordIntPair(j1, k1));
/*     */               }
/*     */               else {
/*     */                 
/* 362 */                 PlayerInstance playermanager$playerinstance1 = getPlayerInstance(j1, k1, true);
/*     */                 
/* 364 */                 if (!playermanager$playerinstance1.playersWatchingChunk.contains(entityplayermp))
/*     */                 {
/* 366 */                   playermanager$playerinstance1.addPlayer(entityplayermp);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/* 374 */         for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; l++) {
/*     */           
/* 376 */           for (int i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; i1++) {
/*     */             
/* 378 */             if (!overlaps(l, i1, j, k, radius)) {
/*     */               
/* 380 */               set.remove(new ChunkCoordIntPair(l, i1));
/* 381 */               PlayerInstance playermanager$playerinstance = getPlayerInstance(l, i1, true);
/*     */               
/* 383 */               if (playermanager$playerinstance != null)
/*     */               {
/* 385 */                 playermanager$playerinstance.removePlayer(entityplayermp);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 393 */       this.playerViewRadius = radius;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getFurthestViewableBlock(int distance) {
/* 399 */     return distance * 16 - 16;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private PriorityQueue<ChunkCoordIntPair> getNearest(Set<ChunkCoordIntPair> p_getNearest_1_, EntityPlayerMP p_getNearest_2_, int p_getNearest_3_) {
/*     */     float f;
/* 406 */     for (f = p_getNearest_2_.rotationYaw + 90.0F; f <= -180.0F; f += 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 411 */     while (f > 180.0F)
/*     */     {
/* 413 */       f -= 360.0F;
/*     */     }
/*     */     
/* 416 */     double d0 = f * 0.017453292519943295D;
/* 417 */     double d1 = p_getNearest_2_.rotationPitch;
/* 418 */     double d2 = d1 * 0.017453292519943295D;
/* 419 */     ChunkPosComparator chunkposcomparator = new ChunkPosComparator(p_getNearest_2_.chunkCoordX, p_getNearest_2_.chunkCoordZ, d0, d2);
/* 420 */     Comparator<ChunkCoordIntPair> comparator = Collections.reverseOrder((Comparator<ChunkCoordIntPair>)chunkposcomparator);
/* 421 */     PriorityQueue<ChunkCoordIntPair> priorityqueue = new PriorityQueue<>(p_getNearest_3_, comparator);
/*     */     
/* 423 */     for (ChunkCoordIntPair chunkcoordintpair : p_getNearest_1_) {
/*     */       
/* 425 */       if (priorityqueue.size() < p_getNearest_3_) {
/*     */         
/* 427 */         priorityqueue.add(chunkcoordintpair);
/*     */         
/*     */         continue;
/*     */       } 
/* 431 */       ChunkCoordIntPair chunkcoordintpair1 = priorityqueue.peek();
/*     */       
/* 433 */       if (chunkposcomparator.compare(chunkcoordintpair, chunkcoordintpair1) < 0) {
/*     */         
/* 435 */         priorityqueue.remove();
/* 436 */         priorityqueue.add(chunkcoordintpair);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 441 */     return priorityqueue;
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<ChunkCoordIntPair> getPendingEntriesSafe(EntityPlayerMP p_getPendingEntriesSafe_1_) {
/* 446 */     Set<ChunkCoordIntPair> set = this.mapPlayerPendingEntries.get(p_getPendingEntriesSafe_1_);
/*     */     
/* 448 */     if (set != null)
/*     */     {
/* 450 */       return set;
/*     */     }
/*     */ 
/*     */     
/* 454 */     int i = Math.min(this.playerViewRadius, 8);
/* 455 */     int j = this.playerViewRadius * 2 + 1;
/* 456 */     int k = i * 2 + 1;
/* 457 */     int l = j * j - k * k;
/* 458 */     l = Math.max(l, 16);
/* 459 */     HashSet<ChunkCoordIntPair> hashset = new HashSet(l);
/* 460 */     this.mapPlayerPendingEntries.put(p_getPendingEntriesSafe_1_, hashset);
/* 461 */     return hashset;
/*     */   }
/*     */ 
/*     */   
/*     */   class PlayerInstance
/*     */   {
/* 467 */     private final List<EntityPlayerMP> playersWatchingChunk = Lists.newArrayList();
/*     */     private final ChunkCoordIntPair chunkCoords;
/* 469 */     private short[] locationOfBlockChange = new short[64];
/*     */     
/*     */     private int numBlocksToUpdate;
/*     */     private int flagsYAreasToUpdate;
/*     */     private long previousWorldTime;
/*     */     
/*     */     public PlayerInstance(int chunkX, int chunkZ) {
/* 476 */       this.chunkCoords = new ChunkCoordIntPair(chunkX, chunkZ);
/* 477 */       (PlayerManager.this.getWorldServer()).theChunkProviderServer.loadChunk(chunkX, chunkZ);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addPlayer(EntityPlayerMP player) {
/* 482 */       if (this.playersWatchingChunk.contains(player)) {
/*     */         
/* 484 */         PlayerManager.pmLogger.debug("Failed to add player. {} already is in chunk {}, {}", new Object[] { player, Integer.valueOf(this.chunkCoords.chunkXPos), Integer.valueOf(this.chunkCoords.chunkZPos) });
/*     */       }
/*     */       else {
/*     */         
/* 488 */         if (this.playersWatchingChunk.isEmpty())
/*     */         {
/* 490 */           this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
/*     */         }
/*     */         
/* 493 */         this.playersWatchingChunk.add(player);
/* 494 */         player.loadedChunks.add(this.chunkCoords);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void removePlayer(EntityPlayerMP player) {
/* 500 */       if (this.playersWatchingChunk.contains(player)) {
/*     */         
/* 502 */         Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
/*     */         
/* 504 */         if (chunk.isPopulated())
/*     */         {
/* 506 */           player.playerNetServerHandler.sendPacket((Packet)new S21PacketChunkData(chunk, true, 0));
/*     */         }
/*     */         
/* 509 */         this.playersWatchingChunk.remove(player);
/* 510 */         player.loadedChunks.remove(this.chunkCoords);
/*     */         
/* 512 */         if (this.playersWatchingChunk.isEmpty()) {
/*     */           
/* 514 */           long i = this.chunkCoords.chunkXPos + 2147483647L | this.chunkCoords.chunkZPos + 2147483647L << 32L;
/* 515 */           increaseInhabitedTime(chunk);
/* 516 */           PlayerManager.this.playerInstances.remove(i);
/* 517 */           PlayerManager.this.playerInstanceList.remove(this);
/*     */           
/* 519 */           if (this.numBlocksToUpdate > 0)
/*     */           {
/* 521 */             PlayerManager.this.playerInstancesToUpdate.remove(this);
/*     */           }
/*     */           
/* 524 */           (PlayerManager.this.getWorldServer()).theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void processChunk() {
/* 531 */       increaseInhabitedTime(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
/*     */     }
/*     */ 
/*     */     
/*     */     private void increaseInhabitedTime(Chunk theChunk) {
/* 536 */       theChunk.setInhabitedTime(theChunk.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
/* 537 */       this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
/*     */     }
/*     */ 
/*     */     
/*     */     public void flagChunkForUpdate(int x, int y, int z) {
/* 542 */       if (this.numBlocksToUpdate == 0)
/*     */       {
/* 544 */         PlayerManager.this.playerInstancesToUpdate.add(this);
/*     */       }
/*     */       
/* 547 */       this.flagsYAreasToUpdate |= 1 << y >> 4;
/*     */       
/* 549 */       if (this.numBlocksToUpdate < 64) {
/*     */         
/* 551 */         short short1 = (short)(x << 12 | z << 8 | y);
/*     */         
/* 553 */         for (int i = 0; i < this.numBlocksToUpdate; i++) {
/*     */           
/* 555 */           if (this.locationOfBlockChange[i] == short1) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 561 */         this.locationOfBlockChange[this.numBlocksToUpdate++] = short1;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void sendToAllPlayersWatchingChunk(Packet thePacket) {
/* 567 */       for (int i = 0; i < this.playersWatchingChunk.size(); i++) {
/*     */         
/* 569 */         EntityPlayerMP entityplayermp = this.playersWatchingChunk.get(i);
/*     */         
/* 571 */         if (!entityplayermp.loadedChunks.contains(this.chunkCoords))
/*     */         {
/* 573 */           entityplayermp.playerNetServerHandler.sendPacket(thePacket);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onUpdate() {
/* 580 */       if (this.numBlocksToUpdate != 0) {
/*     */         
/* 582 */         if (this.numBlocksToUpdate == 1) {
/*     */           
/* 584 */           int k1 = (this.locationOfBlockChange[0] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
/* 585 */           int i2 = this.locationOfBlockChange[0] & 0xFF;
/* 586 */           int k2 = (this.locationOfBlockChange[0] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
/* 587 */           BlockPos blockpos = new BlockPos(k1, i2, k2);
/* 588 */           sendToAllPlayersWatchingChunk((Packet)new S23PacketBlockChange((World)PlayerManager.this.theWorldServer, blockpos));
/*     */           
/* 590 */           if (PlayerManager.this.theWorldServer.getBlockState(blockpos).getBlock().hasTileEntity())
/*     */           {
/* 592 */             sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos));
/*     */           }
/*     */         }
/* 595 */         else if (this.numBlocksToUpdate != 64) {
/*     */           
/* 597 */           sendToAllPlayersWatchingChunk((Packet)new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));
/*     */           
/* 599 */           for (int j1 = 0; j1 < this.numBlocksToUpdate; j1++)
/*     */           {
/* 601 */             int l1 = (this.locationOfBlockChange[j1] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
/* 602 */             int j2 = this.locationOfBlockChange[j1] & 0xFF;
/* 603 */             int l2 = (this.locationOfBlockChange[j1] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
/* 604 */             BlockPos blockpos1 = new BlockPos(l1, j2, l2);
/*     */             
/* 606 */             if (PlayerManager.this.theWorldServer.getBlockState(blockpos1).getBlock().hasTileEntity())
/*     */             {
/* 608 */               sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos1));
/*     */             }
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 614 */           int i = this.chunkCoords.chunkXPos * 16;
/* 615 */           int j = this.chunkCoords.chunkZPos * 16;
/* 616 */           sendToAllPlayersWatchingChunk((Packet)new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));
/*     */           
/* 618 */           for (int k = 0; k < 16; k++) {
/*     */             
/* 620 */             if ((this.flagsYAreasToUpdate & 1 << k) != 0) {
/*     */               
/* 622 */               int l = k << 4;
/* 623 */               List<TileEntity> list = PlayerManager.this.theWorldServer.getTileEntitiesIn(i, l, j, i + 16, l + 16, j + 16);
/*     */               
/* 625 */               for (int i1 = 0; i1 < list.size(); i1++)
/*     */               {
/* 627 */                 sendTileToAllPlayersWatchingChunk(list.get(i1));
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 633 */         this.numBlocksToUpdate = 0;
/* 634 */         this.flagsYAreasToUpdate = 0;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void sendTileToAllPlayersWatchingChunk(TileEntity theTileEntity) {
/* 640 */       if (theTileEntity != null) {
/*     */         
/* 642 */         Packet packet = theTileEntity.getDescriptionPacket();
/*     */         
/* 644 */         if (packet != null)
/*     */         {
/* 646 */           sendToAllPlayersWatchingChunk(packet);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\management\PlayerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */