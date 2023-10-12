/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.gen.MapGenBase;
/*     */ 
/*     */ public abstract class MapGenStructure extends MapGenBase {
/*     */   private MapGenStructureData structureData;
/*  23 */   protected Map<Long, StructureStart> structureMap = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   public abstract String getStructureName();
/*     */   
/*     */   protected final void recursiveGenerate(World worldIn, final int chunkX, final int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn) {
/*  29 */     initializeStructureData(worldIn);
/*     */     
/*  31 */     if (!this.structureMap.containsKey(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ)))) {
/*     */       
/*  33 */       this.rand.nextInt();
/*     */ 
/*     */       
/*     */       try {
/*  37 */         if (canSpawnStructureAtCoords(chunkX, chunkZ))
/*     */         {
/*  39 */           StructureStart structurestart = getStructureStart(chunkX, chunkZ);
/*  40 */           this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ)), structurestart);
/*  41 */           setStructureStart(chunkX, chunkZ, structurestart);
/*     */         }
/*     */       
/*  44 */       } catch (Throwable throwable) {
/*     */         
/*  46 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception preparing structure feature");
/*  47 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
/*  48 */         crashreportcategory.addCrashSectionCallable("Is feature chunk", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/*  52 */                 return MapGenStructure.this.canSpawnStructureAtCoords(chunkX, chunkZ) ? "True" : "False";
/*     */               }
/*     */             });
/*  55 */         crashreportcategory.addCrashSection("Chunk location", String.format("%d,%d", new Object[] { Integer.valueOf(chunkX), Integer.valueOf(chunkZ) }));
/*  56 */         crashreportcategory.addCrashSectionCallable("Chunk pos hash", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/*  60 */                 return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
/*     */               }
/*     */             });
/*  63 */         crashreportcategory.addCrashSectionCallable("Structure type", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/*  67 */                 return MapGenStructure.this.getClass().getCanonicalName();
/*     */               }
/*     */             });
/*  70 */         throw new ReportedException(crashreport);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean generateStructure(World worldIn, Random randomIn, ChunkCoordIntPair chunkCoord) {
/*  77 */     initializeStructureData(worldIn);
/*  78 */     int i = (chunkCoord.chunkXPos << 4) + 8;
/*  79 */     int j = (chunkCoord.chunkZPos << 4) + 8;
/*  80 */     boolean flag = false;
/*     */     
/*  82 */     for (StructureStart structurestart : this.structureMap.values()) {
/*     */       
/*  84 */       if (structurestart.isSizeableStructure() && structurestart.func_175788_a(chunkCoord) && structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15)) {
/*     */         
/*  86 */         structurestart.generateStructure(worldIn, randomIn, new StructureBoundingBox(i, j, i + 15, j + 15));
/*  87 */         structurestart.func_175787_b(chunkCoord);
/*  88 */         flag = true;
/*  89 */         setStructureStart(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_175795_b(BlockPos pos) {
/*  98 */     initializeStructureData(this.worldObj);
/*  99 */     return (func_175797_c(pos) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StructureStart func_175797_c(BlockPos pos) {
/* 106 */     for (StructureStart structurestart : this.structureMap.values()) {
/*     */       
/* 108 */       if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().isVecInside((Vec3i)pos)) {
/*     */         
/* 110 */         Iterator<StructureComponent> iterator = structurestart.getComponents().iterator();
/*     */ 
/*     */ 
/*     */         
/* 114 */         while (iterator.hasNext()) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 119 */           StructureComponent structurecomponent = iterator.next();
/*     */           
/* 121 */           if (structurecomponent.getBoundingBox().isVecInside((Vec3i)pos))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 127 */             return structurestart; } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPositionInStructure(World worldIn, BlockPos pos) {
/* 136 */     initializeStructureData(worldIn);
/*     */     
/* 138 */     for (StructureStart structurestart : this.structureMap.values()) {
/*     */       
/* 140 */       if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().isVecInside((Vec3i)pos))
/*     */       {
/* 142 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos) {
/* 151 */     this.worldObj = worldIn;
/* 152 */     initializeStructureData(worldIn);
/* 153 */     this.rand.setSeed(worldIn.getSeed());
/* 154 */     long i = this.rand.nextLong();
/* 155 */     long j = this.rand.nextLong();
/* 156 */     long k = (pos.getX() >> 4) * i;
/* 157 */     long l = (pos.getZ() >> 4) * j;
/* 158 */     this.rand.setSeed(k ^ l ^ worldIn.getSeed());
/* 159 */     recursiveGenerate(worldIn, pos.getX() >> 4, pos.getZ() >> 4, 0, 0, (ChunkPrimer)null);
/* 160 */     double d0 = Double.MAX_VALUE;
/* 161 */     BlockPos blockpos = null;
/*     */     
/* 163 */     for (StructureStart structurestart : this.structureMap.values()) {
/*     */       
/* 165 */       if (structurestart.isSizeableStructure()) {
/*     */         
/* 167 */         StructureComponent structurecomponent = structurestart.getComponents().get(0);
/* 168 */         BlockPos blockpos1 = structurecomponent.getBoundingBoxCenter();
/* 169 */         double d1 = blockpos1.distanceSq((Vec3i)pos);
/*     */         
/* 171 */         if (d1 < d0) {
/*     */           
/* 173 */           d0 = d1;
/* 174 */           blockpos = blockpos1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 179 */     if (blockpos != null)
/*     */     {
/* 181 */       return blockpos;
/*     */     }
/*     */ 
/*     */     
/* 185 */     List<BlockPos> list = getCoordList();
/*     */     
/* 187 */     if (list != null) {
/*     */       
/* 189 */       BlockPos blockpos2 = null;
/*     */       
/* 191 */       for (BlockPos blockpos3 : list) {
/*     */         
/* 193 */         double d2 = blockpos3.distanceSq((Vec3i)pos);
/*     */         
/* 195 */         if (d2 < d0) {
/*     */           
/* 197 */           d0 = d2;
/* 198 */           blockpos2 = blockpos3;
/*     */         } 
/*     */       } 
/*     */       
/* 202 */       return blockpos2;
/*     */     } 
/*     */ 
/*     */     
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<BlockPos> getCoordList() {
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void initializeStructureData(World worldIn) {
/* 218 */     if (this.structureData == null) {
/*     */       
/* 220 */       this.structureData = (MapGenStructureData)worldIn.loadItemData(MapGenStructureData.class, getStructureName());
/*     */       
/* 222 */       if (this.structureData == null) {
/*     */         
/* 224 */         this.structureData = new MapGenStructureData(getStructureName());
/* 225 */         worldIn.setItemData(getStructureName(), this.structureData);
/*     */       }
/*     */       else {
/*     */         
/* 229 */         NBTTagCompound nbttagcompound = this.structureData.getTagCompound();
/*     */         
/* 231 */         for (String s : nbttagcompound.getKeySet()) {
/*     */           
/* 233 */           NBTBase nbtbase = nbttagcompound.getTag(s);
/*     */           
/* 235 */           if (nbtbase.getId() == 10) {
/*     */             
/* 237 */             NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbtbase;
/*     */             
/* 239 */             if (nbttagcompound1.hasKey("ChunkX") && nbttagcompound1.hasKey("ChunkZ")) {
/*     */               
/* 241 */               int i = nbttagcompound1.getInteger("ChunkX");
/* 242 */               int j = nbttagcompound1.getInteger("ChunkZ");
/* 243 */               StructureStart structurestart = MapGenStructureIO.getStructureStart(nbttagcompound1, worldIn);
/*     */               
/* 245 */               if (structurestart != null)
/*     */               {
/* 247 */                 this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)), structurestart);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setStructureStart(int chunkX, int chunkZ, StructureStart start) {
/* 258 */     this.structureData.writeInstance(start.writeStructureComponentsToNBT(chunkX, chunkZ), chunkX, chunkZ);
/* 259 */     this.structureData.markDirty();
/*     */   }
/*     */   
/*     */   protected abstract boolean canSpawnStructureAtCoords(int paramInt1, int paramInt2);
/*     */   
/*     */   protected abstract StructureStart getStructureStart(int paramInt1, int paramInt2);
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\structure\MapGenStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */