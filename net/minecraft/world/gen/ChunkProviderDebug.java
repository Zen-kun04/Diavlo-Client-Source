/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
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
/*     */ public class ChunkProviderDebug implements IChunkProvider {
/*  20 */   private static final List<IBlockState> field_177464_a = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkProviderDebug(World worldIn) {
/*  27 */     this.world = worldIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(int x, int z) {
/*  32 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/*     */     
/*  34 */     for (int i = 0; i < 16; i++) {
/*     */       
/*  36 */       for (int j = 0; j < 16; j++) {
/*     */         
/*  38 */         int k = x * 16 + i;
/*  39 */         int l = z * 16 + j;
/*  40 */         chunkprimer.setBlockState(i, 60, j, Blocks.barrier.getDefaultState());
/*  41 */         IBlockState iblockstate = func_177461_b(k, l);
/*     */         
/*  43 */         if (iblockstate != null)
/*     */         {
/*  45 */           chunkprimer.setBlockState(i, 70, j, iblockstate);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  50 */     Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
/*  51 */     chunk.generateSkylightMap();
/*  52 */     BiomeGenBase[] abiomegenbase = this.world.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, x * 16, z * 16, 16, 16);
/*  53 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/*  55 */     for (int i1 = 0; i1 < abyte.length; i1++)
/*     */     {
/*  57 */       abyte[i1] = (byte)(abiomegenbase[i1]).biomeID;
/*     */     }
/*     */     
/*  60 */     chunk.generateSkylightMap();
/*  61 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public static IBlockState func_177461_b(int p_177461_0_, int p_177461_1_) {
/*  66 */     IBlockState iblockstate = null;
/*     */     
/*  68 */     if (p_177461_0_ > 0 && p_177461_1_ > 0 && p_177461_0_ % 2 != 0 && p_177461_1_ % 2 != 0) {
/*     */       
/*  70 */       p_177461_0_ /= 2;
/*  71 */       p_177461_1_ /= 2;
/*     */       
/*  73 */       if (p_177461_0_ <= field_177462_b && p_177461_1_ <= field_181039_c) {
/*     */         
/*  75 */         int i = MathHelper.abs_int(p_177461_0_ * field_177462_b + p_177461_1_);
/*     */         
/*  77 */         if (i < field_177464_a.size())
/*     */         {
/*  79 */           iblockstate = field_177464_a.get(i);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  84 */     return iblockstate;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean chunkExists(int x, int z) {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void populate(IChunkProvider chunkProvider, int x, int z) {}
/*     */ 
/*     */   
/*     */   public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z) {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback) {
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveExtraData() {}
/*     */ 
/*     */   
/*     */   public boolean unloadQueuedChunks() {
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSave() {
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String makeString() {
/* 122 */     return "DebugLevelSource";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
/* 127 */     BiomeGenBase biomegenbase = this.world.getBiomeGenForCoords(pos);
/* 128 */     return biomegenbase.getSpawnableList(creatureType);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunkCount() {
/* 138 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateStructures(Chunk chunkIn, int x, int z) {}
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn) {
/* 147 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 152 */     for (Block block : Block.blockRegistry)
/*     */     {
/* 154 */       field_177464_a.addAll((Collection<? extends IBlockState>)block.getBlockState().getValidStates()); } 
/*     */   }
/*     */   
/* 157 */   private static final int field_177462_b = MathHelper.ceiling_float_int(MathHelper.sqrt_float(field_177464_a.size()));
/* 158 */   private static final int field_181039_c = MathHelper.ceiling_float_int(field_177464_a.size() / field_177462_b);
/*     */   private final World world;
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\ChunkProviderDebug.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */