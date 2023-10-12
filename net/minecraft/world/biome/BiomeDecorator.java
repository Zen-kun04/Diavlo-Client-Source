/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.block.BlockStone;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings;
/*     */ import net.minecraft.world.gen.GeneratorBushFeature;
/*     */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenBigMushroom;
/*     */ import net.minecraft.world.gen.feature.WorldGenCactus;
/*     */ import net.minecraft.world.gen.feature.WorldGenClay;
/*     */ import net.minecraft.world.gen.feature.WorldGenDeadBush;
/*     */ import net.minecraft.world.gen.feature.WorldGenFlowers;
/*     */ import net.minecraft.world.gen.feature.WorldGenLiquids;
/*     */ import net.minecraft.world.gen.feature.WorldGenMinable;
/*     */ import net.minecraft.world.gen.feature.WorldGenPumpkin;
/*     */ import net.minecraft.world.gen.feature.WorldGenReed;
/*     */ import net.minecraft.world.gen.feature.WorldGenSand;
/*     */ import net.minecraft.world.gen.feature.WorldGenWaterlily;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ 
/*     */ public class BiomeDecorator
/*     */ {
/*     */   protected World currentWorld;
/*     */   protected Random randomGenerator;
/*  32 */   protected WorldGenerator clayGen = (WorldGenerator)new WorldGenClay(4); protected BlockPos field_180294_c; protected ChunkProviderSettings chunkProviderSettings;
/*  33 */   protected WorldGenerator sandGen = (WorldGenerator)new WorldGenSand((Block)Blocks.sand, 7);
/*  34 */   protected WorldGenerator gravelAsSandGen = (WorldGenerator)new WorldGenSand(Blocks.gravel, 6);
/*     */   protected WorldGenerator dirtGen;
/*     */   protected WorldGenerator gravelGen;
/*     */   protected WorldGenerator graniteGen;
/*     */   protected WorldGenerator dioriteGen;
/*     */   protected WorldGenerator andesiteGen;
/*     */   protected WorldGenerator coalGen;
/*     */   protected WorldGenerator ironGen;
/*     */   protected WorldGenerator goldGen;
/*     */   protected WorldGenerator redstoneGen;
/*     */   protected WorldGenerator diamondGen;
/*     */   protected WorldGenerator lapisGen;
/*  46 */   protected WorldGenFlowers yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
/*  47 */   protected WorldGenerator mushroomBrownGen = (WorldGenerator)new GeneratorBushFeature(Blocks.brown_mushroom);
/*  48 */   protected WorldGenerator mushroomRedGen = (WorldGenerator)new GeneratorBushFeature(Blocks.red_mushroom);
/*  49 */   protected WorldGenerator bigMushroomGen = (WorldGenerator)new WorldGenBigMushroom();
/*  50 */   protected WorldGenerator reedGen = (WorldGenerator)new WorldGenReed();
/*  51 */   protected WorldGenerator cactusGen = (WorldGenerator)new WorldGenCactus();
/*  52 */   protected WorldGenerator waterlilyGen = (WorldGenerator)new WorldGenWaterlily();
/*     */   protected int waterlilyPerChunk;
/*     */   protected int treesPerChunk;
/*  55 */   protected int flowersPerChunk = 2;
/*  56 */   protected int grassPerChunk = 1;
/*     */   protected int deadBushPerChunk;
/*     */   protected int mushroomsPerChunk;
/*     */   protected int reedsPerChunk;
/*     */   protected int cactiPerChunk;
/*  61 */   protected int sandPerChunk = 1;
/*  62 */   protected int sandPerChunk2 = 3;
/*  63 */   protected int clayPerChunk = 1;
/*     */   
/*     */   protected int bigMushroomsPerChunk;
/*     */   public boolean generateLakes = true;
/*     */   
/*     */   public void decorate(World worldIn, Random random, BiomeGenBase biome, BlockPos p_180292_4_) {
/*  69 */     if (this.currentWorld != null)
/*     */     {
/*  71 */       throw new RuntimeException("Already decorating");
/*     */     }
/*     */ 
/*     */     
/*  75 */     this.currentWorld = worldIn;
/*  76 */     String s = worldIn.getWorldInfo().getGeneratorOptions();
/*     */     
/*  78 */     if (s != null) {
/*     */       
/*  80 */       this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(s).func_177864_b();
/*     */     }
/*     */     else {
/*     */       
/*  84 */       this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory("").func_177864_b();
/*     */     } 
/*     */     
/*  87 */     this.randomGenerator = random;
/*  88 */     this.field_180294_c = p_180292_4_;
/*  89 */     this.dirtGen = (WorldGenerator)new WorldGenMinable(Blocks.dirt.getDefaultState(), this.chunkProviderSettings.dirtSize);
/*  90 */     this.gravelGen = (WorldGenerator)new WorldGenMinable(Blocks.gravel.getDefaultState(), this.chunkProviderSettings.gravelSize);
/*  91 */     this.graniteGen = (WorldGenerator)new WorldGenMinable(Blocks.stone.getDefaultState().withProperty((IProperty)BlockStone.VARIANT, (Comparable)BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
/*  92 */     this.dioriteGen = (WorldGenerator)new WorldGenMinable(Blocks.stone.getDefaultState().withProperty((IProperty)BlockStone.VARIANT, (Comparable)BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
/*  93 */     this.andesiteGen = (WorldGenerator)new WorldGenMinable(Blocks.stone.getDefaultState().withProperty((IProperty)BlockStone.VARIANT, (Comparable)BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
/*  94 */     this.coalGen = (WorldGenerator)new WorldGenMinable(Blocks.coal_ore.getDefaultState(), this.chunkProviderSettings.coalSize);
/*  95 */     this.ironGen = (WorldGenerator)new WorldGenMinable(Blocks.iron_ore.getDefaultState(), this.chunkProviderSettings.ironSize);
/*  96 */     this.goldGen = (WorldGenerator)new WorldGenMinable(Blocks.gold_ore.getDefaultState(), this.chunkProviderSettings.goldSize);
/*  97 */     this.redstoneGen = (WorldGenerator)new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), this.chunkProviderSettings.redstoneSize);
/*  98 */     this.diamondGen = (WorldGenerator)new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), this.chunkProviderSettings.diamondSize);
/*  99 */     this.lapisGen = (WorldGenerator)new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), this.chunkProviderSettings.lapisSize);
/* 100 */     genDecorations(biome);
/* 101 */     this.currentWorld = null;
/* 102 */     this.randomGenerator = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void genDecorations(BiomeGenBase biomeGenBaseIn) {
/* 108 */     generateOres();
/*     */     
/* 110 */     for (int i = 0; i < this.sandPerChunk2; i++) {
/*     */       
/* 112 */       int j = this.randomGenerator.nextInt(16) + 8;
/* 113 */       int k = this.randomGenerator.nextInt(16) + 8;
/* 114 */       this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(j, 0, k)));
/*     */     } 
/*     */     
/* 117 */     for (int i1 = 0; i1 < this.clayPerChunk; i1++) {
/*     */       
/* 119 */       int l1 = this.randomGenerator.nextInt(16) + 8;
/* 120 */       int i6 = this.randomGenerator.nextInt(16) + 8;
/* 121 */       this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(l1, 0, i6)));
/*     */     } 
/*     */     
/* 124 */     for (int j1 = 0; j1 < this.sandPerChunk; j1++) {
/*     */       
/* 126 */       int i2 = this.randomGenerator.nextInt(16) + 8;
/* 127 */       int j6 = this.randomGenerator.nextInt(16) + 8;
/* 128 */       this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(i2, 0, j6)));
/*     */     } 
/*     */     
/* 131 */     int k1 = this.treesPerChunk;
/*     */     
/* 133 */     if (this.randomGenerator.nextInt(10) == 0)
/*     */     {
/* 135 */       k1++;
/*     */     }
/*     */     
/* 138 */     for (int j2 = 0; j2 < k1; j2++) {
/*     */       
/* 140 */       int k6 = this.randomGenerator.nextInt(16) + 8;
/* 141 */       int l = this.randomGenerator.nextInt(16) + 8;
/* 142 */       WorldGenAbstractTree worldgenabstracttree = biomeGenBaseIn.genBigTreeChance(this.randomGenerator);
/* 143 */       worldgenabstracttree.func_175904_e();
/* 144 */       BlockPos blockpos = this.currentWorld.getHeight(this.field_180294_c.add(k6, 0, l));
/*     */       
/* 146 */       if (worldgenabstracttree.generate(this.currentWorld, this.randomGenerator, blockpos))
/*     */       {
/* 148 */         worldgenabstracttree.func_180711_a(this.currentWorld, this.randomGenerator, blockpos);
/*     */       }
/*     */     } 
/*     */     
/* 152 */     for (int k2 = 0; k2 < this.bigMushroomsPerChunk; k2++) {
/*     */       
/* 154 */       int l6 = this.randomGenerator.nextInt(16) + 8;
/* 155 */       int k10 = this.randomGenerator.nextInt(16) + 8;
/* 156 */       this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getHeight(this.field_180294_c.add(l6, 0, k10)));
/*     */     } 
/*     */     
/* 159 */     for (int l2 = 0; l2 < this.flowersPerChunk; l2++) {
/*     */       
/* 161 */       int i7 = this.randomGenerator.nextInt(16) + 8;
/* 162 */       int l10 = this.randomGenerator.nextInt(16) + 8;
/* 163 */       int j14 = this.currentWorld.getHeight(this.field_180294_c.add(i7, 0, l10)).getY() + 32;
/*     */       
/* 165 */       if (j14 > 0) {
/*     */         
/* 167 */         int k17 = this.randomGenerator.nextInt(j14);
/* 168 */         BlockPos blockpos1 = this.field_180294_c.add(i7, k17, l10);
/* 169 */         BlockFlower.EnumFlowerType blockflower$enumflowertype = biomeGenBaseIn.pickRandomFlower(this.randomGenerator, blockpos1);
/* 170 */         BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
/*     */         
/* 172 */         if (blockflower.getMaterial() != Material.air) {
/*     */           
/* 174 */           this.yellowFlowerGen.setGeneratedBlock(blockflower, blockflower$enumflowertype);
/* 175 */           this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, blockpos1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 180 */     for (int i3 = 0; i3 < this.grassPerChunk; i3++) {
/*     */       
/* 182 */       int j7 = this.randomGenerator.nextInt(16) + 8;
/* 183 */       int i11 = this.randomGenerator.nextInt(16) + 8;
/* 184 */       int k14 = this.currentWorld.getHeight(this.field_180294_c.add(j7, 0, i11)).getY() * 2;
/*     */       
/* 186 */       if (k14 > 0) {
/*     */         
/* 188 */         int l17 = this.randomGenerator.nextInt(k14);
/* 189 */         biomeGenBaseIn.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j7, l17, i11));
/*     */       } 
/*     */     } 
/*     */     
/* 193 */     for (int j3 = 0; j3 < this.deadBushPerChunk; j3++) {
/*     */       
/* 195 */       int k7 = this.randomGenerator.nextInt(16) + 8;
/* 196 */       int j11 = this.randomGenerator.nextInt(16) + 8;
/* 197 */       int l14 = this.currentWorld.getHeight(this.field_180294_c.add(k7, 0, j11)).getY() * 2;
/*     */       
/* 199 */       if (l14 > 0) {
/*     */         
/* 201 */         int i18 = this.randomGenerator.nextInt(l14);
/* 202 */         (new WorldGenDeadBush()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k7, i18, j11));
/*     */       } 
/*     */     } 
/*     */     
/* 206 */     for (int k3 = 0; k3 < this.waterlilyPerChunk; k3++) {
/*     */       
/* 208 */       int l7 = this.randomGenerator.nextInt(16) + 8;
/* 209 */       int k11 = this.randomGenerator.nextInt(16) + 8;
/* 210 */       int i15 = this.currentWorld.getHeight(this.field_180294_c.add(l7, 0, k11)).getY() * 2;
/*     */       
/* 212 */       if (i15 > 0) {
/*     */         
/* 214 */         int j18 = this.randomGenerator.nextInt(i15);
/*     */         
/*     */         BlockPos blockpos4;
/*     */         
/* 218 */         for (blockpos4 = this.field_180294_c.add(l7, j18, k11); blockpos4.getY() > 0; blockpos4 = blockpos7) {
/*     */           
/* 220 */           BlockPos blockpos7 = blockpos4.down();
/*     */           
/* 222 */           if (!this.currentWorld.isAirBlock(blockpos7)) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 228 */         this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, blockpos4);
/*     */       } 
/*     */     } 
/*     */     
/* 232 */     for (int l3 = 0; l3 < this.mushroomsPerChunk; l3++) {
/*     */       
/* 234 */       if (this.randomGenerator.nextInt(4) == 0) {
/*     */         
/* 236 */         int i8 = this.randomGenerator.nextInt(16) + 8;
/* 237 */         int l11 = this.randomGenerator.nextInt(16) + 8;
/* 238 */         BlockPos blockpos2 = this.currentWorld.getHeight(this.field_180294_c.add(i8, 0, l11));
/* 239 */         this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, blockpos2);
/*     */       } 
/*     */       
/* 242 */       if (this.randomGenerator.nextInt(8) == 0) {
/*     */         
/* 244 */         int j8 = this.randomGenerator.nextInt(16) + 8;
/* 245 */         int i12 = this.randomGenerator.nextInt(16) + 8;
/* 246 */         int j15 = this.currentWorld.getHeight(this.field_180294_c.add(j8, 0, i12)).getY() * 2;
/*     */         
/* 248 */         if (j15 > 0) {
/*     */           
/* 250 */           int k18 = this.randomGenerator.nextInt(j15);
/* 251 */           BlockPos blockpos5 = this.field_180294_c.add(j8, k18, i12);
/* 252 */           this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, blockpos5);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     if (this.randomGenerator.nextInt(4) == 0) {
/*     */       
/* 259 */       int i4 = this.randomGenerator.nextInt(16) + 8;
/* 260 */       int k8 = this.randomGenerator.nextInt(16) + 8;
/* 261 */       int j12 = this.currentWorld.getHeight(this.field_180294_c.add(i4, 0, k8)).getY() * 2;
/*     */       
/* 263 */       if (j12 > 0) {
/*     */         
/* 265 */         int k15 = this.randomGenerator.nextInt(j12);
/* 266 */         this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(i4, k15, k8));
/*     */       } 
/*     */     } 
/*     */     
/* 270 */     if (this.randomGenerator.nextInt(8) == 0) {
/*     */       
/* 272 */       int j4 = this.randomGenerator.nextInt(16) + 8;
/* 273 */       int l8 = this.randomGenerator.nextInt(16) + 8;
/* 274 */       int k12 = this.currentWorld.getHeight(this.field_180294_c.add(j4, 0, l8)).getY() * 2;
/*     */       
/* 276 */       if (k12 > 0) {
/*     */         
/* 278 */         int l15 = this.randomGenerator.nextInt(k12);
/* 279 */         this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j4, l15, l8));
/*     */       } 
/*     */     } 
/*     */     
/* 283 */     for (int k4 = 0; k4 < this.reedsPerChunk; k4++) {
/*     */       
/* 285 */       int i9 = this.randomGenerator.nextInt(16) + 8;
/* 286 */       int l12 = this.randomGenerator.nextInt(16) + 8;
/* 287 */       int i16 = this.currentWorld.getHeight(this.field_180294_c.add(i9, 0, l12)).getY() * 2;
/*     */       
/* 289 */       if (i16 > 0) {
/*     */         
/* 291 */         int l18 = this.randomGenerator.nextInt(i16);
/* 292 */         this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(i9, l18, l12));
/*     */       } 
/*     */     } 
/*     */     
/* 296 */     for (int l4 = 0; l4 < 10; l4++) {
/*     */       
/* 298 */       int j9 = this.randomGenerator.nextInt(16) + 8;
/* 299 */       int i13 = this.randomGenerator.nextInt(16) + 8;
/* 300 */       int j16 = this.currentWorld.getHeight(this.field_180294_c.add(j9, 0, i13)).getY() * 2;
/*     */       
/* 302 */       if (j16 > 0) {
/*     */         
/* 304 */         int i19 = this.randomGenerator.nextInt(j16);
/* 305 */         this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j9, i19, i13));
/*     */       } 
/*     */     } 
/*     */     
/* 309 */     if (this.randomGenerator.nextInt(32) == 0) {
/*     */       
/* 311 */       int i5 = this.randomGenerator.nextInt(16) + 8;
/* 312 */       int k9 = this.randomGenerator.nextInt(16) + 8;
/* 313 */       int j13 = this.currentWorld.getHeight(this.field_180294_c.add(i5, 0, k9)).getY() * 2;
/*     */       
/* 315 */       if (j13 > 0) {
/*     */         
/* 317 */         int k16 = this.randomGenerator.nextInt(j13);
/* 318 */         (new WorldGenPumpkin()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(i5, k16, k9));
/*     */       } 
/*     */     } 
/*     */     
/* 322 */     for (int j5 = 0; j5 < this.cactiPerChunk; j5++) {
/*     */       
/* 324 */       int l9 = this.randomGenerator.nextInt(16) + 8;
/* 325 */       int k13 = this.randomGenerator.nextInt(16) + 8;
/* 326 */       int l16 = this.currentWorld.getHeight(this.field_180294_c.add(l9, 0, k13)).getY() * 2;
/*     */       
/* 328 */       if (l16 > 0) {
/*     */         
/* 330 */         int j19 = this.randomGenerator.nextInt(l16);
/* 331 */         this.cactusGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(l9, j19, k13));
/*     */       } 
/*     */     } 
/*     */     
/* 335 */     if (this.generateLakes) {
/*     */       
/* 337 */       for (int k5 = 0; k5 < 50; k5++) {
/*     */         
/* 339 */         int i10 = this.randomGenerator.nextInt(16) + 8;
/* 340 */         int l13 = this.randomGenerator.nextInt(16) + 8;
/* 341 */         int i17 = this.randomGenerator.nextInt(248) + 8;
/*     */         
/* 343 */         if (i17 > 0) {
/*     */           
/* 345 */           int k19 = this.randomGenerator.nextInt(i17);
/* 346 */           BlockPos blockpos6 = this.field_180294_c.add(i10, k19, l13);
/* 347 */           (new WorldGenLiquids((Block)Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, blockpos6);
/*     */         } 
/*     */       } 
/*     */       
/* 351 */       for (int l5 = 0; l5 < 20; l5++) {
/*     */         
/* 353 */         int j10 = this.randomGenerator.nextInt(16) + 8;
/* 354 */         int i14 = this.randomGenerator.nextInt(16) + 8;
/* 355 */         int j17 = this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8);
/* 356 */         BlockPos blockpos3 = this.field_180294_c.add(j10, j17, i14);
/* 357 */         (new WorldGenLiquids((Block)Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, blockpos3);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void genStandardOre1(int blockCount, WorldGenerator generator, int minHeight, int maxHeight) {
/* 364 */     if (maxHeight < minHeight) {
/*     */       
/* 366 */       int i = minHeight;
/* 367 */       minHeight = maxHeight;
/* 368 */       maxHeight = i;
/*     */     }
/* 370 */     else if (maxHeight == minHeight) {
/*     */       
/* 372 */       if (minHeight < 255) {
/*     */         
/* 374 */         maxHeight++;
/*     */       }
/*     */       else {
/*     */         
/* 378 */         minHeight--;
/*     */       } 
/*     */     } 
/*     */     
/* 382 */     for (int j = 0; j < blockCount; j++) {
/*     */       
/* 384 */       BlockPos blockpos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(maxHeight - minHeight) + minHeight, this.randomGenerator.nextInt(16));
/* 385 */       generator.generate(this.currentWorld, this.randomGenerator, blockpos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void genStandardOre2(int blockCount, WorldGenerator generator, int centerHeight, int spread) {
/* 391 */     for (int i = 0; i < blockCount; i++) {
/*     */       
/* 393 */       BlockPos blockpos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(spread) + this.randomGenerator.nextInt(spread) + centerHeight - spread, this.randomGenerator.nextInt(16));
/* 394 */       generator.generate(this.currentWorld, this.randomGenerator, blockpos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateOres() {
/* 400 */     genStandardOre1(this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight);
/* 401 */     genStandardOre1(this.chunkProviderSettings.gravelCount, this.gravelGen, this.chunkProviderSettings.gravelMinHeight, this.chunkProviderSettings.gravelMaxHeight);
/* 402 */     genStandardOre1(this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, this.chunkProviderSettings.dioriteMaxHeight);
/* 403 */     genStandardOre1(this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, this.chunkProviderSettings.graniteMaxHeight);
/* 404 */     genStandardOre1(this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, this.chunkProviderSettings.andesiteMaxHeight);
/* 405 */     genStandardOre1(this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
/* 406 */     genStandardOre1(this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
/* 407 */     genStandardOre1(this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
/* 408 */     genStandardOre1(this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
/* 409 */     genStandardOre1(this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
/* 410 */     genStandardOre2(this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\biome\BiomeDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */