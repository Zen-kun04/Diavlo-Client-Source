/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.EmptyChunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ChunkProviderClient
/*     */   implements IChunkProvider {
/*  20 */   private static final Logger logger = LogManager.getLogger();
/*     */   private Chunk blankChunk;
/*  22 */   private LongHashMap<Chunk> chunkMapping = new LongHashMap();
/*  23 */   private List<Chunk> chunkListing = Lists.newArrayList();
/*     */   
/*     */   private World worldObj;
/*     */   
/*     */   public ChunkProviderClient(World worldIn) {
/*  28 */     this.blankChunk = (Chunk)new EmptyChunk(worldIn, 0, 0);
/*  29 */     this.worldObj = worldIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean chunkExists(int x, int z) {
/*  34 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void unloadChunk(int x, int z) {
/*  39 */     Chunk chunk = provideChunk(x, z);
/*     */     
/*  41 */     if (!chunk.isEmpty())
/*     */     {
/*  43 */       chunk.onChunkUnload();
/*     */     }
/*     */     
/*  46 */     this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(x, z));
/*  47 */     this.chunkListing.remove(chunk);
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk loadChunk(int chunkX, int chunkZ) {
/*  52 */     Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);
/*  53 */     this.chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ), chunk);
/*  54 */     this.chunkListing.add(chunk);
/*  55 */     chunk.setChunkLoaded(true);
/*  56 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(int x, int z) {
/*  61 */     Chunk chunk = (Chunk)this.chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
/*  62 */     return (chunk == null) ? this.blankChunk : chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback) {
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveExtraData() {}
/*     */ 
/*     */   
/*     */   public boolean unloadQueuedChunks() {
/*  76 */     long i = System.currentTimeMillis();
/*     */     
/*  78 */     for (Chunk chunk : this.chunkListing)
/*     */     {
/*  80 */       chunk.func_150804_b((System.currentTimeMillis() - i > 5L));
/*     */     }
/*     */     
/*  83 */     if (System.currentTimeMillis() - i > 100L)
/*     */     {
/*  85 */       logger.info("Warning: Clientside chunk ticking took {} ms", new Object[] { Long.valueOf(System.currentTimeMillis() - i) });
/*     */     }
/*     */     
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSave() {
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void populate(IChunkProvider chunkProvider, int x, int z) {}
/*     */ 
/*     */   
/*     */   public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z) {
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String makeString() {
/* 107 */     return "MultiplayerChunkCache: " + this.chunkMapping.getNumHashElements() + ", " + this.chunkListing.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunkCount() {
/* 122 */     return this.chunkListing.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateStructures(Chunk chunkIn, int x, int z) {}
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn) {
/* 131 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\multiplayer\ChunkProviderClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */