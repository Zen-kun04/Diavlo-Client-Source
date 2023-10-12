/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.feature.WorldGenDungeons;
/*     */ import net.minecraft.world.gen.feature.WorldGenLakes;
/*     */ import net.minecraft.world.gen.structure.MapGenMineshaft;
/*     */ import net.minecraft.world.gen.structure.MapGenScatteredFeature;
/*     */ import net.minecraft.world.gen.structure.MapGenStronghold;
/*     */ import net.minecraft.world.gen.structure.MapGenStructure;
/*     */ import net.minecraft.world.gen.structure.MapGenVillage;
/*     */ import net.minecraft.world.gen.structure.StructureOceanMonument;
/*     */ 
/*     */ public class ChunkProviderFlat implements IChunkProvider {
/*     */   private World worldObj;
/*     */   private Random random;
/*  31 */   private final IBlockState[] cachedBlockIDs = new IBlockState[256];
/*     */   private final FlatGeneratorInfo flatWorldGenInfo;
/*  33 */   private final List<MapGenStructure> structureGenerators = Lists.newArrayList();
/*     */   
/*     */   private final boolean hasDecoration;
/*     */   private final boolean hasDungeons;
/*     */   private WorldGenLakes waterLakeGenerator;
/*     */   private WorldGenLakes lavaLakeGenerator;
/*     */   
/*     */   public ChunkProviderFlat(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings) {
/*  41 */     this.worldObj = worldIn;
/*  42 */     this.random = new Random(seed);
/*  43 */     this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(flatGeneratorSettings);
/*     */     
/*  45 */     if (generateStructures) {
/*     */       
/*  47 */       Map<String, Map<String, String>> map = this.flatWorldGenInfo.getWorldFeatures();
/*     */       
/*  49 */       if (map.containsKey("village")) {
/*     */         
/*  51 */         Map<String, String> map1 = map.get("village");
/*     */         
/*  53 */         if (!map1.containsKey("size"))
/*     */         {
/*  55 */           map1.put("size", "1");
/*     */         }
/*     */         
/*  58 */         this.structureGenerators.add(new MapGenVillage(map1));
/*     */       } 
/*     */       
/*  61 */       if (map.containsKey("biome_1"))
/*     */       {
/*  63 */         this.structureGenerators.add(new MapGenScatteredFeature(map.get("biome_1")));
/*     */       }
/*     */       
/*  66 */       if (map.containsKey("mineshaft"))
/*     */       {
/*  68 */         this.structureGenerators.add(new MapGenMineshaft(map.get("mineshaft")));
/*     */       }
/*     */       
/*  71 */       if (map.containsKey("stronghold"))
/*     */       {
/*  73 */         this.structureGenerators.add(new MapGenStronghold(map.get("stronghold")));
/*     */       }
/*     */       
/*  76 */       if (map.containsKey("oceanmonument"))
/*     */       {
/*  78 */         this.structureGenerators.add(new StructureOceanMonument(map.get("oceanmonument")));
/*     */       }
/*     */     } 
/*     */     
/*  82 */     if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake"))
/*     */     {
/*  84 */       this.waterLakeGenerator = new WorldGenLakes((Block)Blocks.water);
/*     */     }
/*     */     
/*  87 */     if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake"))
/*     */     {
/*  89 */       this.lavaLakeGenerator = new WorldGenLakes((Block)Blocks.lava);
/*     */     }
/*     */     
/*  92 */     this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
/*  93 */     int j = 0;
/*  94 */     int k = 0;
/*  95 */     boolean flag = true;
/*     */     
/*  97 */     for (FlatLayerInfo flatlayerinfo : this.flatWorldGenInfo.getFlatLayers()) {
/*     */       
/*  99 */       for (int i = flatlayerinfo.getMinY(); i < flatlayerinfo.getMinY() + flatlayerinfo.getLayerCount(); i++) {
/*     */         
/* 101 */         IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
/*     */         
/* 103 */         if (iblockstate.getBlock() != Blocks.air) {
/*     */           
/* 105 */           flag = false;
/* 106 */           this.cachedBlockIDs[i] = iblockstate;
/*     */         } 
/*     */       } 
/*     */       
/* 110 */       if (flatlayerinfo.getLayerMaterial().getBlock() == Blocks.air) {
/*     */         
/* 112 */         k += flatlayerinfo.getLayerCount();
/*     */         
/*     */         continue;
/*     */       } 
/* 116 */       j += flatlayerinfo.getLayerCount() + k;
/* 117 */       k = 0;
/*     */     } 
/*     */ 
/*     */     
/* 121 */     worldIn.setSeaLevel(j);
/* 122 */     this.hasDecoration = flag ? false : this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration");
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(int x, int z) {
/* 127 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/*     */     
/* 129 */     for (int i = 0; i < this.cachedBlockIDs.length; i++) {
/*     */       
/* 131 */       IBlockState iblockstate = this.cachedBlockIDs[i];
/*     */       
/* 133 */       if (iblockstate != null)
/*     */       {
/* 135 */         for (int j = 0; j < 16; j++) {
/*     */           
/* 137 */           for (int k = 0; k < 16; k++)
/*     */           {
/* 139 */             chunkprimer.setBlockState(j, i, k, iblockstate);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 145 */     for (MapGenBase mapgenbase : this.structureGenerators)
/*     */     {
/* 147 */       mapgenbase.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 150 */     Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
/* 151 */     BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, x * 16, z * 16, 16, 16);
/* 152 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/* 154 */     for (int l = 0; l < abyte.length; l++)
/*     */     {
/* 156 */       abyte[l] = (byte)(abiomegenbase[l]).biomeID;
/*     */     }
/*     */     
/* 159 */     chunk.generateSkylightMap();
/* 160 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean chunkExists(int x, int z) {
/* 165 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void populate(IChunkProvider chunkProvider, int x, int z) {
/* 170 */     int i = x * 16;
/* 171 */     int j = z * 16;
/* 172 */     BlockPos blockpos = new BlockPos(i, 0, j);
/* 173 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(i + 16, 0, j + 16));
/* 174 */     boolean flag = false;
/* 175 */     this.random.setSeed(this.worldObj.getSeed());
/* 176 */     long k = this.random.nextLong() / 2L * 2L + 1L;
/* 177 */     long l = this.random.nextLong() / 2L * 2L + 1L;
/* 178 */     this.random.setSeed(x * k + z * l ^ this.worldObj.getSeed());
/* 179 */     ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x, z);
/*     */     
/* 181 */     for (MapGenStructure mapgenstructure : this.structureGenerators) {
/*     */       
/* 183 */       boolean flag1 = mapgenstructure.generateStructure(this.worldObj, this.random, chunkcoordintpair);
/*     */       
/* 185 */       if (mapgenstructure instanceof MapGenVillage)
/*     */       {
/* 187 */         flag |= flag1;
/*     */       }
/*     */     } 
/*     */     
/* 191 */     if (this.waterLakeGenerator != null && !flag && this.random.nextInt(4) == 0)
/*     */     {
/* 193 */       this.waterLakeGenerator.generate(this.worldObj, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
/*     */     }
/*     */     
/* 196 */     if (this.lavaLakeGenerator != null && !flag && this.random.nextInt(8) == 0) {
/*     */       
/* 198 */       BlockPos blockpos1 = blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);
/*     */       
/* 200 */       if (blockpos1.getY() < this.worldObj.getSeaLevel() || this.random.nextInt(10) == 0)
/*     */       {
/* 202 */         this.lavaLakeGenerator.generate(this.worldObj, this.random, blockpos1);
/*     */       }
/*     */     } 
/*     */     
/* 206 */     if (this.hasDungeons)
/*     */     {
/* 208 */       for (int i1 = 0; i1 < 8; i1++)
/*     */       {
/* 210 */         (new WorldGenDungeons()).generate(this.worldObj, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
/*     */       }
/*     */     }
/*     */     
/* 214 */     if (this.hasDecoration)
/*     */     {
/* 216 */       biomegenbase.decorate(this.worldObj, this.random, blockpos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z) {
/* 222 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback) {
/* 227 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveExtraData() {}
/*     */ 
/*     */   
/*     */   public boolean unloadQueuedChunks() {
/* 236 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSave() {
/* 241 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String makeString() {
/* 246 */     return "FlatLevelSource";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
/* 251 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
/* 252 */     return biomegenbase.getSpawnableList(creatureType);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
/* 257 */     if ("Stronghold".equals(structureName))
/*     */     {
/* 259 */       for (MapGenStructure mapgenstructure : this.structureGenerators) {
/*     */         
/* 261 */         if (mapgenstructure instanceof MapGenStronghold)
/*     */         {
/* 263 */           return mapgenstructure.getClosestStrongholdPos(worldIn, position);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 268 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunkCount() {
/* 273 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateStructures(Chunk chunkIn, int x, int z) {
/* 278 */     for (MapGenStructure mapgenstructure : this.structureGenerators)
/*     */     {
/* 280 */       mapgenstructure.generate(this, this.worldObj, x, z, (ChunkPrimer)null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn) {
/* 286 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\ChunkProviderFlat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */