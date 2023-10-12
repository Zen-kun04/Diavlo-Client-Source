/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ 
/*     */ public class ChunkCache
/*     */   implements IBlockAccess
/*     */ {
/*     */   protected int chunkX;
/*     */   protected int chunkZ;
/*     */   protected Chunk[][] chunkArray;
/*     */   protected boolean hasExtendedLevels;
/*     */   protected World worldObj;
/*     */   
/*     */   public ChunkCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn) {
/*  22 */     this.worldObj = worldIn;
/*  23 */     this.chunkX = posFromIn.getX() - subIn >> 4;
/*  24 */     this.chunkZ = posFromIn.getZ() - subIn >> 4;
/*  25 */     int i = posToIn.getX() + subIn >> 4;
/*  26 */     int j = posToIn.getZ() + subIn >> 4;
/*  27 */     this.chunkArray = new Chunk[i - this.chunkX + 1][j - this.chunkZ + 1];
/*  28 */     this.hasExtendedLevels = true;
/*     */     
/*  30 */     for (int k = this.chunkX; k <= i; k++) {
/*     */       
/*  32 */       for (int l = this.chunkZ; l <= j; l++)
/*     */       {
/*  34 */         this.chunkArray[k - this.chunkX][l - this.chunkZ] = worldIn.getChunkFromChunkCoords(k, l);
/*     */       }
/*     */     } 
/*     */     
/*  38 */     for (int i1 = posFromIn.getX() >> 4; i1 <= posToIn.getX() >> 4; i1++) {
/*     */       
/*  40 */       for (int j1 = posFromIn.getZ() >> 4; j1 <= posToIn.getZ() >> 4; j1++) {
/*     */         
/*  42 */         Chunk chunk = this.chunkArray[i1 - this.chunkX][j1 - this.chunkZ];
/*     */         
/*  44 */         if (chunk != null && !chunk.getAreLevelsEmpty(posFromIn.getY(), posToIn.getY()))
/*     */         {
/*  46 */           this.hasExtendedLevels = false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean extendedLevelsInChunkCache() {
/*  54 */     return this.hasExtendedLevels;
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity getTileEntity(BlockPos pos) {
/*  59 */     int i = (pos.getX() >> 4) - this.chunkX;
/*  60 */     int j = (pos.getZ() >> 4) - this.chunkZ;
/*  61 */     return this.chunkArray[i][j].getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCombinedLight(BlockPos pos, int lightValue) {
/*  66 */     int i = getLightForExt(EnumSkyBlock.SKY, pos);
/*  67 */     int j = getLightForExt(EnumSkyBlock.BLOCK, pos);
/*     */     
/*  69 */     if (j < lightValue)
/*     */     {
/*  71 */       j = lightValue;
/*     */     }
/*     */     
/*  74 */     return i << 20 | j << 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getBlockState(BlockPos pos) {
/*  79 */     if (pos.getY() >= 0 && pos.getY() < 256) {
/*     */       
/*  81 */       int i = (pos.getX() >> 4) - this.chunkX;
/*  82 */       int j = (pos.getZ() >> 4) - this.chunkZ;
/*     */       
/*  84 */       if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < (this.chunkArray[i]).length) {
/*     */         
/*  86 */         Chunk chunk = this.chunkArray[i][j];
/*     */         
/*  88 */         if (chunk != null)
/*     */         {
/*  90 */           return chunk.getBlockState(pos);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     return Blocks.air.getDefaultState();
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
/* 100 */     return this.worldObj.getBiomeGenForCoords(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getLightForExt(EnumSkyBlock p_175629_1_, BlockPos pos) {
/* 105 */     if (p_175629_1_ == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky())
/*     */     {
/* 107 */       return 0;
/*     */     }
/* 109 */     if (pos.getY() >= 0 && pos.getY() < 256) {
/*     */       
/* 111 */       if (getBlockState(pos).getBlock().getUseNeighborBrightness()) {
/*     */         
/* 113 */         int l = 0;
/*     */         
/* 115 */         for (EnumFacing enumfacing : EnumFacing.values()) {
/*     */           
/* 117 */           int k = getLightFor(p_175629_1_, pos.offset(enumfacing));
/*     */           
/* 119 */           if (k > l)
/*     */           {
/* 121 */             l = k;
/*     */           }
/*     */           
/* 124 */           if (l >= 15)
/*     */           {
/* 126 */             return l;
/*     */           }
/*     */         } 
/*     */         
/* 130 */         return l;
/*     */       } 
/*     */ 
/*     */       
/* 134 */       int i = (pos.getX() >> 4) - this.chunkX;
/* 135 */       int j = (pos.getZ() >> 4) - this.chunkZ;
/* 136 */       return this.chunkArray[i][j].getLightFor(p_175629_1_, pos);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 141 */     return p_175629_1_.defaultLightValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAirBlock(BlockPos pos) {
/* 147 */     return (getBlockState(pos).getBlock().getMaterial() == Material.air);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightFor(EnumSkyBlock p_175628_1_, BlockPos pos) {
/* 152 */     if (pos.getY() >= 0 && pos.getY() < 256) {
/*     */       
/* 154 */       int i = (pos.getX() >> 4) - this.chunkX;
/* 155 */       int j = (pos.getZ() >> 4) - this.chunkZ;
/* 156 */       return this.chunkArray[i][j].getLightFor(p_175628_1_, pos);
/*     */     } 
/*     */ 
/*     */     
/* 160 */     return p_175628_1_.defaultLightValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStrongPower(BlockPos pos, EnumFacing direction) {
/* 166 */     IBlockState iblockstate = getBlockState(pos);
/* 167 */     return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldType getWorldType() {
/* 172 */     return this.worldObj.getWorldType();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\ChunkCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */