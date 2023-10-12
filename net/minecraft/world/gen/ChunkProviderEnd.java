/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.BlockFalling;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ 
/*     */ public class ChunkProviderEnd
/*     */   implements IChunkProvider
/*     */ {
/*     */   private Random endRNG;
/*     */   private NoiseGeneratorOctaves noiseGen1;
/*     */   private NoiseGeneratorOctaves noiseGen2;
/*     */   private NoiseGeneratorOctaves noiseGen3;
/*     */   public NoiseGeneratorOctaves noiseGen4;
/*     */   public NoiseGeneratorOctaves noiseGen5;
/*     */   private World endWorld;
/*     */   private double[] densities;
/*     */   private BiomeGenBase[] biomesForGeneration;
/*     */   double[] noiseData1;
/*     */   double[] noiseData2;
/*     */   double[] noiseData3;
/*     */   double[] noiseData4;
/*     */   double[] noiseData5;
/*     */   
/*     */   public ChunkProviderEnd(World worldIn, long seed) {
/*  38 */     this.endWorld = worldIn;
/*  39 */     this.endRNG = new Random(seed);
/*  40 */     this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
/*  41 */     this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
/*  42 */     this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
/*  43 */     this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
/*  44 */     this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_180520_a(int p_180520_1_, int p_180520_2_, ChunkPrimer p_180520_3_) {
/*  49 */     int i = 2;
/*  50 */     int j = i + 1;
/*  51 */     int k = 33;
/*  52 */     int l = i + 1;
/*  53 */     this.densities = initializeNoiseField(this.densities, p_180520_1_ * i, 0, p_180520_2_ * i, j, k, l);
/*     */     
/*  55 */     for (int i1 = 0; i1 < i; i1++) {
/*     */       
/*  57 */       for (int j1 = 0; j1 < i; j1++) {
/*     */         
/*  59 */         for (int k1 = 0; k1 < 32; k1++) {
/*     */           
/*  61 */           double d0 = 0.25D;
/*  62 */           double d1 = this.densities[((i1 + 0) * l + j1 + 0) * k + k1 + 0];
/*  63 */           double d2 = this.densities[((i1 + 0) * l + j1 + 1) * k + k1 + 0];
/*  64 */           double d3 = this.densities[((i1 + 1) * l + j1 + 0) * k + k1 + 0];
/*  65 */           double d4 = this.densities[((i1 + 1) * l + j1 + 1) * k + k1 + 0];
/*  66 */           double d5 = (this.densities[((i1 + 0) * l + j1 + 0) * k + k1 + 1] - d1) * d0;
/*  67 */           double d6 = (this.densities[((i1 + 0) * l + j1 + 1) * k + k1 + 1] - d2) * d0;
/*  68 */           double d7 = (this.densities[((i1 + 1) * l + j1 + 0) * k + k1 + 1] - d3) * d0;
/*  69 */           double d8 = (this.densities[((i1 + 1) * l + j1 + 1) * k + k1 + 1] - d4) * d0;
/*     */           
/*  71 */           for (int l1 = 0; l1 < 4; l1++) {
/*     */             
/*  73 */             double d9 = 0.125D;
/*  74 */             double d10 = d1;
/*  75 */             double d11 = d2;
/*  76 */             double d12 = (d3 - d1) * d9;
/*  77 */             double d13 = (d4 - d2) * d9;
/*     */             
/*  79 */             for (int i2 = 0; i2 < 8; i2++) {
/*     */               
/*  81 */               double d14 = 0.125D;
/*  82 */               double d15 = d10;
/*  83 */               double d16 = (d11 - d10) * d14;
/*     */               
/*  85 */               for (int j2 = 0; j2 < 8; j2++) {
/*     */                 
/*  87 */                 IBlockState iblockstate = null;
/*     */                 
/*  89 */                 if (d15 > 0.0D)
/*     */                 {
/*  91 */                   iblockstate = Blocks.end_stone.getDefaultState();
/*     */                 }
/*     */                 
/*  94 */                 int k2 = i2 + i1 * 8;
/*  95 */                 int l2 = l1 + k1 * 4;
/*  96 */                 int i3 = j2 + j1 * 8;
/*  97 */                 p_180520_3_.setBlockState(k2, l2, i3, iblockstate);
/*  98 */                 d15 += d16;
/*     */               } 
/*     */               
/* 101 */               d10 += d12;
/* 102 */               d11 += d13;
/*     */             } 
/*     */             
/* 105 */             d1 += d5;
/* 106 */             d2 += d6;
/* 107 */             d3 += d7;
/* 108 */             d4 += d8;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_180519_a(ChunkPrimer p_180519_1_) {
/* 117 */     for (int i = 0; i < 16; i++) {
/*     */       
/* 119 */       for (int j = 0; j < 16; j++) {
/*     */         
/* 121 */         int k = 1;
/* 122 */         int l = -1;
/* 123 */         IBlockState iblockstate = Blocks.end_stone.getDefaultState();
/* 124 */         IBlockState iblockstate1 = Blocks.end_stone.getDefaultState();
/*     */         
/* 126 */         for (int i1 = 127; i1 >= 0; i1--) {
/*     */           
/* 128 */           IBlockState iblockstate2 = p_180519_1_.getBlockState(i, i1, j);
/*     */           
/* 130 */           if (iblockstate2.getBlock().getMaterial() == Material.air) {
/*     */             
/* 132 */             l = -1;
/*     */           }
/* 134 */           else if (iblockstate2.getBlock() == Blocks.stone) {
/*     */             
/* 136 */             if (l == -1) {
/*     */               
/* 138 */               if (k <= 0) {
/*     */                 
/* 140 */                 iblockstate = Blocks.air.getDefaultState();
/* 141 */                 iblockstate1 = Blocks.end_stone.getDefaultState();
/*     */               } 
/*     */               
/* 144 */               l = k;
/*     */               
/* 146 */               if (i1 >= 0)
/*     */               {
/* 148 */                 p_180519_1_.setBlockState(i, i1, j, iblockstate);
/*     */               }
/*     */               else
/*     */               {
/* 152 */                 p_180519_1_.setBlockState(i, i1, j, iblockstate1);
/*     */               }
/*     */             
/* 155 */             } else if (l > 0) {
/*     */               
/* 157 */               l--;
/* 158 */               p_180519_1_.setBlockState(i, i1, j, iblockstate1);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(int x, int z) {
/* 168 */     this.endRNG.setSeed(x * 341873128712L + z * 132897987541L);
/* 169 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/* 170 */     this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
/* 171 */     func_180520_a(x, z, chunkprimer);
/* 172 */     func_180519_a(chunkprimer);
/* 173 */     Chunk chunk = new Chunk(this.endWorld, chunkprimer, x, z);
/* 174 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/* 176 */     for (int i = 0; i < abyte.length; i++)
/*     */     {
/* 178 */       abyte[i] = (byte)(this.biomesForGeneration[i]).biomeID;
/*     */     }
/*     */     
/* 181 */     chunk.generateSkylightMap();
/* 182 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   private double[] initializeNoiseField(double[] p_73187_1_, int p_73187_2_, int p_73187_3_, int p_73187_4_, int p_73187_5_, int p_73187_6_, int p_73187_7_) {
/* 187 */     if (p_73187_1_ == null)
/*     */     {
/* 189 */       p_73187_1_ = new double[p_73187_5_ * p_73187_6_ * p_73187_7_];
/*     */     }
/*     */     
/* 192 */     double d0 = 684.412D;
/* 193 */     double d1 = 684.412D;
/* 194 */     this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 1.121D, 1.121D, 0.5D);
/* 195 */     this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 200.0D, 200.0D, 0.5D);
/* 196 */     d0 *= 2.0D;
/* 197 */     this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
/* 198 */     this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0, d1, d0);
/* 199 */     this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0, d1, d0);
/* 200 */     int i = 0;
/*     */     
/* 202 */     for (int j = 0; j < p_73187_5_; j++) {
/*     */       
/* 204 */       for (int k = 0; k < p_73187_7_; k++) {
/*     */         
/* 206 */         float f = (j + p_73187_2_) / 1.0F;
/* 207 */         float f1 = (k + p_73187_4_) / 1.0F;
/* 208 */         float f2 = 100.0F - MathHelper.sqrt_float(f * f + f1 * f1) * 8.0F;
/*     */         
/* 210 */         if (f2 > 80.0F)
/*     */         {
/* 212 */           f2 = 80.0F;
/*     */         }
/*     */         
/* 215 */         if (f2 < -100.0F)
/*     */         {
/* 217 */           f2 = -100.0F;
/*     */         }
/*     */         
/* 220 */         for (int l = 0; l < p_73187_6_; l++) {
/*     */           
/* 222 */           double d2 = 0.0D;
/* 223 */           double d3 = this.noiseData2[i] / 512.0D;
/* 224 */           double d4 = this.noiseData3[i] / 512.0D;
/* 225 */           double d5 = (this.noiseData1[i] / 10.0D + 1.0D) / 2.0D;
/*     */           
/* 227 */           if (d5 < 0.0D) {
/*     */             
/* 229 */             d2 = d3;
/*     */           }
/* 231 */           else if (d5 > 1.0D) {
/*     */             
/* 233 */             d2 = d4;
/*     */           }
/*     */           else {
/*     */             
/* 237 */             d2 = d3 + (d4 - d3) * d5;
/*     */           } 
/*     */           
/* 240 */           d2 -= 8.0D;
/* 241 */           d2 += f2;
/* 242 */           int i1 = 2;
/*     */           
/* 244 */           if (l > p_73187_6_ / 2 - i1) {
/*     */             
/* 246 */             double d6 = ((l - p_73187_6_ / 2 - i1) / 64.0F);
/* 247 */             d6 = MathHelper.clamp_double(d6, 0.0D, 1.0D);
/* 248 */             d2 = d2 * (1.0D - d6) + -3000.0D * d6;
/*     */           } 
/*     */           
/* 251 */           i1 = 8;
/*     */           
/* 253 */           if (l < i1) {
/*     */             
/* 255 */             double d7 = ((i1 - l) / (i1 - 1.0F));
/* 256 */             d2 = d2 * (1.0D - d7) + -30.0D * d7;
/*     */           } 
/*     */           
/* 259 */           p_73187_1_[i] = d2;
/* 260 */           i++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 265 */     return p_73187_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean chunkExists(int x, int z) {
/* 270 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void populate(IChunkProvider chunkProvider, int x, int z) {
/* 275 */     BlockFalling.fallInstantly = true;
/* 276 */     BlockPos blockpos = new BlockPos(x * 16, 0, z * 16);
/* 277 */     this.endWorld.getBiomeGenForCoords(blockpos.add(16, 0, 16)).decorate(this.endWorld, this.endWorld.rand, blockpos);
/* 278 */     BlockFalling.fallInstantly = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z) {
/* 283 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback) {
/* 288 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveExtraData() {}
/*     */ 
/*     */   
/*     */   public boolean unloadQueuedChunks() {
/* 297 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSave() {
/* 302 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String makeString() {
/* 307 */     return "RandomLevelSource";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
/* 312 */     return this.endWorld.getBiomeGenForCoords(pos).getSpawnableList(creatureType);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunkCount() {
/* 322 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateStructures(Chunk chunkIn, int x, int z) {}
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn) {
/* 331 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\ChunkProviderEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */