/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapGenStronghold
/*     */   extends MapGenStructure
/*     */ {
/*  24 */   private ChunkCoordIntPair[] structureCoords = new ChunkCoordIntPair[3];
/*  25 */   private double field_82671_h = 32.0D;
/*  26 */   private int field_82672_i = 3;
/*  27 */   private List<BiomeGenBase> field_151546_e = Lists.newArrayList();
/*     */   public MapGenStronghold() {
/*  29 */     for (BiomeGenBase biomegenbase : BiomeGenBase.getBiomeGenArray()) {
/*     */       
/*  31 */       if (biomegenbase != null && biomegenbase.minHeight > 0.0F)
/*     */       {
/*  33 */         this.field_151546_e.add(biomegenbase); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean ranBiomeCheck;
/*     */   
/*     */   public MapGenStronghold(Map<String, String> p_i2068_1_) {
/*  40 */     this();
/*     */     
/*  42 */     for (Map.Entry<String, String> entry : p_i2068_1_.entrySet()) {
/*     */       
/*  44 */       if (((String)entry.getKey()).equals("distance")) {
/*     */         
/*  46 */         this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax(entry.getValue(), this.field_82671_h, 1.0D); continue;
/*     */       } 
/*  48 */       if (((String)entry.getKey()).equals("count")) {
/*     */         
/*  50 */         this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.structureCoords.length, 1)]; continue;
/*     */       } 
/*  52 */       if (((String)entry.getKey()).equals("spread"))
/*     */       {
/*  54 */         this.field_82672_i = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_82672_i, 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStructureName() {
/*  61 */     return "Stronghold";
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
/*  66 */     if (!this.ranBiomeCheck) {
/*     */       
/*  68 */       Random random = new Random();
/*  69 */       random.setSeed(this.worldObj.getSeed());
/*  70 */       double d0 = random.nextDouble() * Math.PI * 2.0D;
/*  71 */       int i = 1;
/*     */       
/*  73 */       for (int j = 0; j < this.structureCoords.length; j++) {
/*     */         
/*  75 */         double d1 = (1.25D * i + random.nextDouble()) * this.field_82671_h * i;
/*  76 */         int k = (int)Math.round(Math.cos(d0) * d1);
/*  77 */         int l = (int)Math.round(Math.sin(d0) * d1);
/*  78 */         BlockPos blockpos = this.worldObj.getWorldChunkManager().findBiomePosition((k << 4) + 8, (l << 4) + 8, 112, this.field_151546_e, random);
/*     */         
/*  80 */         if (blockpos != null) {
/*     */           
/*  82 */           k = blockpos.getX() >> 4;
/*  83 */           l = blockpos.getZ() >> 4;
/*     */         } 
/*     */         
/*  86 */         this.structureCoords[j] = new ChunkCoordIntPair(k, l);
/*  87 */         d0 += 6.283185307179586D * i / this.field_82672_i;
/*     */         
/*  89 */         if (j == this.field_82672_i) {
/*     */           
/*  91 */           i += 2 + random.nextInt(5);
/*  92 */           this.field_82672_i += 1 + random.nextInt(2);
/*     */         } 
/*     */       } 
/*     */       
/*  96 */       this.ranBiomeCheck = true;
/*     */     } 
/*     */     
/*  99 */     for (ChunkCoordIntPair chunkcoordintpair : this.structureCoords) {
/*     */       
/* 101 */       if (chunkX == chunkcoordintpair.chunkXPos && chunkZ == chunkcoordintpair.chunkZPos)
/*     */       {
/* 103 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<BlockPos> getCoordList() {
/* 112 */     List<BlockPos> list = Lists.newArrayList();
/*     */     
/* 114 */     for (ChunkCoordIntPair chunkcoordintpair : this.structureCoords) {
/*     */       
/* 116 */       if (chunkcoordintpair != null)
/*     */       {
/* 118 */         list.add(chunkcoordintpair.getCenterBlock(64));
/*     */       }
/*     */     } 
/*     */     
/* 122 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected StructureStart getStructureStart(int chunkX, int chunkZ) {
/*     */     Start mapgenstronghold$start;
/* 129 */     for (mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ); mapgenstronghold$start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)mapgenstronghold$start.getComponents().get(0)).strongholdPortalRoom == null; mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     return mapgenstronghold$start;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Start
/*     */     extends StructureStart
/*     */   {
/*     */     public Start() {}
/*     */ 
/*     */     
/*     */     public Start(World worldIn, Random p_i2067_2_, int p_i2067_3_, int p_i2067_4_) {
/* 145 */       super(p_i2067_3_, p_i2067_4_);
/* 146 */       StructureStrongholdPieces.prepareStructurePieces();
/* 147 */       StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, p_i2067_2_, (p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
/* 148 */       this.components.add(structurestrongholdpieces$stairs2);
/* 149 */       structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
/* 150 */       List<StructureComponent> list = structurestrongholdpieces$stairs2.field_75026_c;
/*     */       
/* 152 */       while (!list.isEmpty()) {
/*     */         
/* 154 */         int i = p_i2067_2_.nextInt(list.size());
/* 155 */         StructureComponent structurecomponent = list.remove(i);
/* 156 */         structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
/*     */       } 
/*     */       
/* 159 */       updateBoundingBox();
/* 160 */       markAvailableHeight(worldIn, p_i2067_2_, 10);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\structure\MapGenStronghold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */