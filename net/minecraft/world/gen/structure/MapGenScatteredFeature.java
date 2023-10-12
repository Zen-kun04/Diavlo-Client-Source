/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.monster.EntityWitch;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class MapGenScatteredFeature
/*     */   extends MapGenStructure
/*     */ {
/*  17 */   private static final List<BiomeGenBase> biomelist = Arrays.asList(new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland });
/*     */   
/*     */   private List<BiomeGenBase.SpawnListEntry> scatteredFeatureSpawnList;
/*     */   private int maxDistanceBetweenScatteredFeatures;
/*     */   private int minDistanceBetweenScatteredFeatures;
/*     */   
/*     */   public MapGenScatteredFeature() {
/*  24 */     this.scatteredFeatureSpawnList = Lists.newArrayList();
/*  25 */     this.maxDistanceBetweenScatteredFeatures = 32;
/*  26 */     this.minDistanceBetweenScatteredFeatures = 8;
/*  27 */     this.scatteredFeatureSpawnList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 1, 1, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   public MapGenScatteredFeature(Map<String, String> p_i2061_1_) {
/*  32 */     this();
/*     */     
/*  34 */     for (Map.Entry<String, String> entry : p_i2061_1_.entrySet()) {
/*     */       
/*  36 */       if (((String)entry.getKey()).equals("distance"))
/*     */       {
/*  38 */         this.maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.maxDistanceBetweenScatteredFeatures, this.minDistanceBetweenScatteredFeatures + 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStructureName() {
/*  45 */     return "Temple";
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
/*  50 */     int i = chunkX;
/*  51 */     int j = chunkZ;
/*     */     
/*  53 */     if (chunkX < 0)
/*     */     {
/*  55 */       chunkX -= this.maxDistanceBetweenScatteredFeatures - 1;
/*     */     }
/*     */     
/*  58 */     if (chunkZ < 0)
/*     */     {
/*  60 */       chunkZ -= this.maxDistanceBetweenScatteredFeatures - 1;
/*     */     }
/*     */     
/*  63 */     int k = chunkX / this.maxDistanceBetweenScatteredFeatures;
/*  64 */     int l = chunkZ / this.maxDistanceBetweenScatteredFeatures;
/*  65 */     Random random = this.worldObj.setRandomSeed(k, l, 14357617);
/*  66 */     k *= this.maxDistanceBetweenScatteredFeatures;
/*  67 */     l *= this.maxDistanceBetweenScatteredFeatures;
/*  68 */     k += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
/*  69 */     l += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
/*     */     
/*  71 */     if (i == k && j == l) {
/*     */       
/*  73 */       BiomeGenBase biomegenbase = this.worldObj.getWorldChunkManager().getBiomeGenerator(new BlockPos(i * 16 + 8, 0, j * 16 + 8));
/*     */       
/*  75 */       if (biomegenbase == null)
/*     */       {
/*  77 */         return false;
/*     */       }
/*     */       
/*  80 */       for (BiomeGenBase biomegenbase1 : biomelist) {
/*     */         
/*  82 */         if (biomegenbase == biomegenbase1)
/*     */         {
/*  84 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected StructureStart getStructureStart(int chunkX, int chunkZ) {
/*  94 */     return new Start(this.worldObj, this.rand, chunkX, chunkZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_175798_a(BlockPos p_175798_1_) {
/*  99 */     StructureStart structurestart = func_175797_c(p_175798_1_);
/*     */     
/* 101 */     if (structurestart != null && structurestart instanceof Start && !structurestart.components.isEmpty()) {
/*     */       
/* 103 */       StructureComponent structurecomponent = structurestart.components.getFirst();
/* 104 */       return structurecomponent instanceof ComponentScatteredFeaturePieces.SwampHut;
/*     */     } 
/*     */ 
/*     */     
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getScatteredFeatureSpawnList() {
/* 114 */     return this.scatteredFeatureSpawnList;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Start
/*     */     extends StructureStart
/*     */   {
/*     */     public Start() {}
/*     */ 
/*     */     
/*     */     public Start(World worldIn, Random p_i2060_2_, int p_i2060_3_, int p_i2060_4_) {
/* 125 */       super(p_i2060_3_, p_i2060_4_);
/* 126 */       BiomeGenBase biomegenbase = worldIn.getBiomeGenForCoords(new BlockPos(p_i2060_3_ * 16 + 8, 0, p_i2060_4_ * 16 + 8));
/*     */       
/* 128 */       if (biomegenbase != BiomeGenBase.jungle && biomegenbase != BiomeGenBase.jungleHills) {
/*     */         
/* 130 */         if (biomegenbase == BiomeGenBase.swampland)
/*     */         {
/* 132 */           ComponentScatteredFeaturePieces.SwampHut componentscatteredfeaturepieces$swamphut = new ComponentScatteredFeaturePieces.SwampHut(p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
/* 133 */           this.components.add(componentscatteredfeaturepieces$swamphut);
/*     */         }
/* 135 */         else if (biomegenbase == BiomeGenBase.desert || biomegenbase == BiomeGenBase.desertHills)
/*     */         {
/* 137 */           ComponentScatteredFeaturePieces.DesertPyramid componentscatteredfeaturepieces$desertpyramid = new ComponentScatteredFeaturePieces.DesertPyramid(p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
/* 138 */           this.components.add(componentscatteredfeaturepieces$desertpyramid);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 143 */         ComponentScatteredFeaturePieces.JunglePyramid componentscatteredfeaturepieces$junglepyramid = new ComponentScatteredFeaturePieces.JunglePyramid(p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
/* 144 */         this.components.add(componentscatteredfeaturepieces$junglepyramid);
/*     */       } 
/*     */       
/* 147 */       updateBoundingBox();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\structure\MapGenScatteredFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */