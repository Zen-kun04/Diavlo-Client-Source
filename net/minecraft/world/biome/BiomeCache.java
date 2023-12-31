/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.LongHashMap;
/*    */ 
/*    */ public class BiomeCache
/*    */ {
/*    */   private final WorldChunkManager chunkManager;
/*    */   private long lastCleanupTime;
/* 12 */   private LongHashMap<Block> cacheMap = new LongHashMap();
/* 13 */   private List<Block> cache = Lists.newArrayList();
/*    */ 
/*    */   
/*    */   public BiomeCache(WorldChunkManager chunkManagerIn) {
/* 17 */     this.chunkManager = chunkManagerIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public Block getBiomeCacheBlock(int x, int z) {
/* 22 */     x >>= 4;
/* 23 */     z >>= 4;
/* 24 */     long i = x & 0xFFFFFFFFL | (z & 0xFFFFFFFFL) << 32L;
/* 25 */     Block biomecache$block = (Block)this.cacheMap.getValueByKey(i);
/*    */     
/* 27 */     if (biomecache$block == null) {
/*    */       
/* 29 */       biomecache$block = new Block(x, z);
/* 30 */       this.cacheMap.add(i, biomecache$block);
/* 31 */       this.cache.add(biomecache$block);
/*    */     } 
/*    */     
/* 34 */     biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
/* 35 */     return biomecache$block;
/*    */   }
/*    */ 
/*    */   
/*    */   public BiomeGenBase func_180284_a(int x, int z, BiomeGenBase p_180284_3_) {
/* 40 */     BiomeGenBase biomegenbase = getBiomeCacheBlock(x, z).getBiomeGenAt(x, z);
/* 41 */     return (biomegenbase == null) ? p_180284_3_ : biomegenbase;
/*    */   }
/*    */ 
/*    */   
/*    */   public void cleanupCache() {
/* 46 */     long i = MinecraftServer.getCurrentTimeMillis();
/* 47 */     long j = i - this.lastCleanupTime;
/*    */     
/* 49 */     if (j > 7500L || j < 0L) {
/*    */       
/* 51 */       this.lastCleanupTime = i;
/*    */       
/* 53 */       for (int k = 0; k < this.cache.size(); k++) {
/*    */         
/* 55 */         Block biomecache$block = this.cache.get(k);
/* 56 */         long l = i - biomecache$block.lastAccessTime;
/*    */         
/* 58 */         if (l > 30000L || l < 0L) {
/*    */           
/* 60 */           this.cache.remove(k--);
/* 61 */           long i1 = biomecache$block.xPosition & 0xFFFFFFFFL | (biomecache$block.zPosition & 0xFFFFFFFFL) << 32L;
/* 62 */           this.cacheMap.remove(i1);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public BiomeGenBase[] getCachedBiomes(int x, int z) {
/* 70 */     return (getBiomeCacheBlock(x, z)).biomes;
/*    */   }
/*    */   
/*    */   public class Block
/*    */   {
/* 75 */     public float[] rainfallValues = new float[256];
/* 76 */     public BiomeGenBase[] biomes = new BiomeGenBase[256];
/*    */     
/*    */     public int xPosition;
/*    */     public int zPosition;
/*    */     public long lastAccessTime;
/*    */     
/*    */     public Block(int x, int z) {
/* 83 */       this.xPosition = x;
/* 84 */       this.zPosition = z;
/* 85 */       BiomeCache.this.chunkManager.getRainfall(this.rainfallValues, x << 4, z << 4, 16, 16);
/* 86 */       BiomeCache.this.chunkManager.getBiomeGenAt(this.biomes, x << 4, z << 4, 16, 16, false);
/*    */     }
/*    */ 
/*    */     
/*    */     public BiomeGenBase getBiomeGenAt(int x, int z) {
/* 91 */       return this.biomes[x & 0xF | (z & 0xF) << 4];
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\biome\BiomeCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */