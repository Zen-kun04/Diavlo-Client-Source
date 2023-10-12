/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntitySpawnPlacementRegistry;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.optifine.BlockPosM;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import net.optifine.reflect.ReflectorForge;
/*     */ 
/*     */ public final class SpawnerAnimals {
/*  32 */   private static final int MOB_COUNT_DIV = (int)Math.pow(17.0D, 2.0D);
/*  33 */   private final Set<ChunkCoordIntPair> eligibleChunksForSpawning = Sets.newHashSet();
/*  34 */   private Map<Class, EntityLiving> mapSampleEntitiesByClass = (Map)new HashMap<>();
/*  35 */   private int lastPlayerChunkX = Integer.MAX_VALUE;
/*  36 */   private int lastPlayerChunkZ = Integer.MAX_VALUE;
/*     */   
/*     */   private int countChunkPos;
/*     */   
/*     */   public int findChunksForSpawning(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean p_77192_4_) {
/*  41 */     if (!spawnHostileMobs && !spawnPeacefulMobs)
/*     */     {
/*  43 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*  47 */     boolean flag = true;
/*  48 */     EntityPlayer entityplayer = null;
/*     */     
/*  50 */     if (worldServerIn.playerEntities.size() == 1) {
/*     */       
/*  52 */       entityplayer = worldServerIn.playerEntities.get(0);
/*     */       
/*  54 */       if (this.eligibleChunksForSpawning.size() > 0 && entityplayer != null && entityplayer.chunkCoordX == this.lastPlayerChunkX && entityplayer.chunkCoordZ == this.lastPlayerChunkZ)
/*     */       {
/*  56 */         flag = false;
/*     */       }
/*     */     } 
/*     */     
/*  60 */     if (flag) {
/*     */       
/*  62 */       this.eligibleChunksForSpawning.clear();
/*  63 */       int i = 0;
/*     */       
/*  65 */       for (EntityPlayer entityplayer1 : worldServerIn.playerEntities) {
/*     */         
/*  67 */         if (!entityplayer1.isSpectator()) {
/*     */           
/*  69 */           int j = MathHelper.floor_double(entityplayer1.posX / 16.0D);
/*  70 */           int k = MathHelper.floor_double(entityplayer1.posZ / 16.0D);
/*  71 */           int l = 8;
/*     */           
/*  73 */           for (int i1 = -l; i1 <= l; i1++) {
/*     */             
/*  75 */             for (int j1 = -l; j1 <= l; j1++) {
/*     */               
/*  77 */               boolean flag1 = (i1 == -l || i1 == l || j1 == -l || j1 == l);
/*  78 */               ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i1 + j, j1 + k);
/*     */               
/*  80 */               if (!this.eligibleChunksForSpawning.contains(chunkcoordintpair)) {
/*     */                 
/*  82 */                 i++;
/*     */                 
/*  84 */                 if (!flag1 && worldServerIn.getWorldBorder().contains(chunkcoordintpair))
/*     */                 {
/*  86 */                   this.eligibleChunksForSpawning.add(chunkcoordintpair);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  94 */       this.countChunkPos = i;
/*     */       
/*  96 */       if (entityplayer != null) {
/*     */         
/*  98 */         this.lastPlayerChunkX = entityplayer.chunkCoordX;
/*  99 */         this.lastPlayerChunkZ = entityplayer.chunkCoordZ;
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     int j4 = 0;
/* 104 */     BlockPos blockpos2 = worldServerIn.getSpawnPoint();
/* 105 */     BlockPosM blockposm = new BlockPosM(0, 0, 0);
/* 106 */     new BlockPos.MutableBlockPos();
/*     */     
/* 108 */     for (EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
/*     */       
/* 110 */       if ((!enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs) && (enumcreaturetype.getPeacefulCreature() || spawnHostileMobs) && (!enumcreaturetype.getAnimal() || p_77192_4_)) {
/*     */         
/* 112 */         int k4 = Reflector.ForgeWorld_countEntities.exists() ? Reflector.callInt(worldServerIn, Reflector.ForgeWorld_countEntities, new Object[] { enumcreaturetype, Boolean.valueOf(true) }) : worldServerIn.countEntities(enumcreaturetype.getCreatureClass());
/* 113 */         int l4 = enumcreaturetype.getMaxNumberOfCreature() * this.countChunkPos / MOB_COUNT_DIV;
/*     */         
/* 115 */         if (k4 <= l4) {
/*     */           
/* 117 */           Collection<ChunkCoordIntPair> collection = this.eligibleChunksForSpawning;
/*     */           
/* 119 */           if (Reflector.ForgeHooksClient.exists()) {
/*     */             
/* 121 */             ArrayList<ChunkCoordIntPair> arraylist = Lists.newArrayList(collection);
/* 122 */             Collections.shuffle(arraylist);
/* 123 */             collection = arraylist;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 128 */           label118: for (ChunkCoordIntPair chunkcoordintpair1 : collection) {
/*     */             
/* 130 */             BlockPosM blockPosM = getRandomChunkPosition(worldServerIn, chunkcoordintpair1.chunkXPos, chunkcoordintpair1.chunkZPos, blockposm);
/* 131 */             int k1 = blockPosM.getX();
/* 132 */             int l1 = blockPosM.getY();
/* 133 */             int i2 = blockPosM.getZ();
/* 134 */             Block block = worldServerIn.getBlockState((BlockPos)blockPosM).getBlock();
/*     */             
/* 136 */             if (!block.isNormalCube()) {
/*     */               
/* 138 */               int j2 = 0;
/*     */               
/* 140 */               for (int k2 = 0; k2 < 3; k2++) {
/*     */                 
/* 142 */                 int l2 = k1;
/* 143 */                 int i3 = l1;
/* 144 */                 int j3 = i2;
/* 145 */                 int k3 = 6;
/* 146 */                 BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = null;
/* 147 */                 IEntityLivingData ientitylivingdata = null;
/*     */                 
/* 149 */                 for (int l3 = 0; l3 < 4; l3++) {
/*     */                   
/* 151 */                   l2 += worldServerIn.rand.nextInt(k3) - worldServerIn.rand.nextInt(k3);
/* 152 */                   i3 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1);
/* 153 */                   j3 += worldServerIn.rand.nextInt(k3) - worldServerIn.rand.nextInt(k3);
/* 154 */                   BlockPos blockpos1 = new BlockPos(l2, i3, j3);
/* 155 */                   float f = l2 + 0.5F;
/* 156 */                   float f1 = j3 + 0.5F;
/*     */                   
/* 158 */                   if (!worldServerIn.isAnyPlayerWithinRangeAt(f, i3, f1, 24.0D) && blockpos2.distanceSq(f, i3, f1) >= 576.0D) {
/*     */                     
/* 160 */                     if (biomegenbase$spawnlistentry == null) {
/*     */                       
/* 162 */                       biomegenbase$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos1);
/*     */                       
/* 164 */                       if (biomegenbase$spawnlistentry == null) {
/*     */                         break;
/*     */                       }
/*     */                     } 
/*     */ 
/*     */                     
/* 170 */                     if (worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biomegenbase$spawnlistentry, blockpos1) && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biomegenbase$spawnlistentry.entityClass), worldServerIn, blockpos1)) {
/*     */                       EntityLiving entityliving;
/*     */ 
/*     */ 
/*     */                       
/*     */                       try {
/* 176 */                         entityliving = this.mapSampleEntitiesByClass.get(biomegenbase$spawnlistentry.entityClass);
/*     */                         
/* 178 */                         if (entityliving == null)
/*     */                         {
/* 180 */                           entityliving = biomegenbase$spawnlistentry.entityClass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldServerIn });
/* 181 */                           this.mapSampleEntitiesByClass.put(biomegenbase$spawnlistentry.entityClass, entityliving);
/*     */                         }
/*     */                       
/* 184 */                       } catch (Exception exception1) {
/*     */                         
/* 186 */                         exception1.printStackTrace();
/* 187 */                         return j4;
/*     */                       } 
/*     */                       
/* 190 */                       entityliving.setLocationAndAngles(f, i3, f1, worldServerIn.rand.nextFloat() * 360.0F, 0.0F);
/* 191 */                       boolean flag2 = Reflector.ForgeEventFactory_canEntitySpawn.exists() ? ReflectorForge.canEntitySpawn(entityliving, worldServerIn, f, i3, f1) : ((entityliving.getCanSpawnHere() && entityliving.isNotColliding()));
/*     */                       
/* 193 */                       if (flag2) {
/*     */                         
/* 195 */                         this.mapSampleEntitiesByClass.remove(biomegenbase$spawnlistentry.entityClass);
/*     */                         
/* 197 */                         if (!ReflectorForge.doSpecialSpawn(entityliving, worldServerIn, f, i3, f1))
/*     */                         {
/* 199 */                           ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos((Entity)entityliving)), ientitylivingdata);
/*     */                         }
/*     */                         
/* 202 */                         if (entityliving.isNotColliding()) {
/*     */                           
/* 204 */                           j2++;
/* 205 */                           worldServerIn.spawnEntityInWorld((Entity)entityliving);
/*     */                         } 
/*     */                         
/* 208 */                         int i4 = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists() ? Reflector.callInt(Reflector.ForgeEventFactory_getMaxSpawnPackSize, new Object[] { entityliving }) : entityliving.getMaxSpawnedInChunk();
/*     */                         
/* 210 */                         if (j2 >= i4) {
/*     */                           continue label118;
/*     */                         }
/*     */                       } 
/*     */ 
/*     */                       
/* 216 */                       j4 += j2;
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 227 */     return j4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static BlockPos getRandomChunkPosition(World worldIn, int x, int z) {
/* 233 */     Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
/* 234 */     int i = x * 16 + worldIn.rand.nextInt(16);
/* 235 */     int j = z * 16 + worldIn.rand.nextInt(16);
/* 236 */     int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
/* 237 */     int l = worldIn.rand.nextInt((k > 0) ? k : (chunk.getTopFilledSegment() + 16 - 1));
/* 238 */     return new BlockPos(i, l, j);
/*     */   }
/*     */ 
/*     */   
/*     */   private static BlockPosM getRandomChunkPosition(World p_getRandomChunkPosition_0_, int p_getRandomChunkPosition_1_, int p_getRandomChunkPosition_2_, BlockPosM p_getRandomChunkPosition_3_) {
/* 243 */     Chunk chunk = p_getRandomChunkPosition_0_.getChunkFromChunkCoords(p_getRandomChunkPosition_1_, p_getRandomChunkPosition_2_);
/* 244 */     int i = p_getRandomChunkPosition_1_ * 16 + p_getRandomChunkPosition_0_.rand.nextInt(16);
/* 245 */     int j = p_getRandomChunkPosition_2_ * 16 + p_getRandomChunkPosition_0_.rand.nextInt(16);
/* 246 */     int k = MathHelper.roundUp(chunk.getHeightValue(i & 0xF, j & 0xF) + 1, 16);
/* 247 */     int l = p_getRandomChunkPosition_0_.rand.nextInt((k > 0) ? k : (chunk.getTopFilledSegment() + 16 - 1));
/* 248 */     p_getRandomChunkPosition_3_.setXyz(i, l, j);
/* 249 */     return p_getRandomChunkPosition_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos) {
/* 254 */     if (!worldIn.getWorldBorder().contains(pos))
/*     */     {
/* 256 */       return false;
/*     */     }
/* 258 */     if (spawnPlacementTypeIn == null)
/*     */     {
/* 260 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 264 */     Block block = worldIn.getBlockState(pos).getBlock();
/*     */     
/* 266 */     if (spawnPlacementTypeIn == EntityLiving.SpawnPlacementType.IN_WATER)
/*     */     {
/* 268 */       return (block.getMaterial().isLiquid() && worldIn.getBlockState(pos.down()).getBlock().getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube());
/*     */     }
/*     */ 
/*     */     
/* 272 */     BlockPos blockpos = pos.down();
/* 273 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/* 274 */     boolean flag = Reflector.ForgeBlock_canCreatureSpawn.exists() ? Reflector.callBoolean(iblockstate.getBlock(), Reflector.ForgeBlock_canCreatureSpawn, new Object[] { worldIn, blockpos, spawnPlacementTypeIn }) : World.doesBlockHaveSolidTopSurface(worldIn, blockpos);
/*     */     
/* 276 */     if (!flag)
/*     */     {
/* 278 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 282 */     Block block1 = worldIn.getBlockState(blockpos).getBlock();
/* 283 */     boolean flag1 = (block1 != Blocks.bedrock && block1 != Blocks.barrier);
/* 284 */     return (flag1 && !block.isNormalCube() && !block.getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void performWorldGenSpawning(World worldIn, BiomeGenBase biomeIn, int p_77191_2_, int p_77191_3_, int p_77191_4_, int p_77191_5_, Random randomIn) {
/* 292 */     List<BiomeGenBase.SpawnListEntry> list = biomeIn.getSpawnableList(EnumCreatureType.CREATURE);
/*     */     
/* 294 */     if (!list.isEmpty())
/*     */     {
/* 296 */       while (randomIn.nextFloat() < biomeIn.getSpawningChance()) {
/*     */         
/* 298 */         BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = (BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(worldIn.rand, list);
/* 299 */         int i = biomegenbase$spawnlistentry.minGroupCount + randomIn.nextInt(1 + biomegenbase$spawnlistentry.maxGroupCount - biomegenbase$spawnlistentry.minGroupCount);
/* 300 */         IEntityLivingData ientitylivingdata = null;
/* 301 */         int j = p_77191_2_ + randomIn.nextInt(p_77191_4_);
/* 302 */         int k = p_77191_3_ + randomIn.nextInt(p_77191_5_);
/* 303 */         int l = j;
/* 304 */         int i1 = k;
/*     */         
/* 306 */         for (int j1 = 0; j1 < i; j1++) {
/*     */           
/* 308 */           boolean flag = false;
/*     */           
/* 310 */           for (int k1 = 0; !flag && k1 < 4; k1++) {
/*     */             
/* 312 */             BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
/*     */             
/* 314 */             if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
/*     */               EntityLiving entityliving;
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 320 */                 entityliving = biomegenbase$spawnlistentry.entityClass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
/*     */               }
/* 322 */               catch (Exception exception1) {
/*     */                 
/* 324 */                 exception1.printStackTrace();
/*     */                 
/*     */                 continue;
/*     */               } 
/* 328 */               if (Reflector.ForgeEventFactory_canEntitySpawn.exists()) {
/*     */                 
/* 330 */                 Object object = Reflector.call(Reflector.ForgeEventFactory_canEntitySpawn, new Object[] { entityliving, worldIn, Float.valueOf(j + 0.5F), Integer.valueOf(blockpos.getY()), Float.valueOf(k + 0.5F) });
/*     */                 
/* 332 */                 if (object == ReflectorForge.EVENT_RESULT_DENY) {
/*     */                   continue;
/*     */                 }
/*     */               } 
/*     */ 
/*     */               
/* 338 */               entityliving.setLocationAndAngles((j + 0.5F), blockpos.getY(), (k + 0.5F), randomIn.nextFloat() * 360.0F, 0.0F);
/* 339 */               worldIn.spawnEntityInWorld((Entity)entityliving);
/* 340 */               ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos((Entity)entityliving)), ientitylivingdata);
/* 341 */               flag = true;
/*     */             } 
/*     */             
/* 344 */             j += randomIn.nextInt(5) - randomIn.nextInt(5);
/*     */             
/* 346 */             for (k += randomIn.nextInt(5) - randomIn.nextInt(5); j < p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_; k = i1 + randomIn.nextInt(5) - randomIn.nextInt(5))
/*     */             {
/* 348 */               j = l + randomIn.nextInt(5) - randomIn.nextInt(5);
/*     */             }
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\SpawnerAnimals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */