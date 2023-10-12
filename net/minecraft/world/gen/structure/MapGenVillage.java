/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class MapGenVillage
/*     */   extends MapGenStructure
/*     */ {
/*  15 */   public static final List<BiomeGenBase> villageSpawnBiomes = Arrays.asList(new BiomeGenBase[] { BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna });
/*     */ 
/*     */ 
/*     */   
/*     */   private int terrainType;
/*     */ 
/*     */   
/*  22 */   private int field_82665_g = 32;
/*  23 */   private int field_82666_h = 8;
/*     */   
/*     */   public MapGenVillage() {}
/*     */   
/*     */   public MapGenVillage(Map<String, String> p_i2093_1_) {
/*  28 */     this();
/*     */     
/*  30 */     for (Map.Entry<String, String> entry : p_i2093_1_.entrySet()) {
/*     */       
/*  32 */       if (((String)entry.getKey()).equals("size")) {
/*     */         
/*  34 */         this.terrainType = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.terrainType, 0); continue;
/*     */       } 
/*  36 */       if (((String)entry.getKey()).equals("distance"))
/*     */       {
/*  38 */         this.field_82665_g = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_82665_g, this.field_82666_h + 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStructureName() {
/*  45 */     return "Village";
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
/*  50 */     int i = chunkX;
/*  51 */     int j = chunkZ;
/*     */     
/*  53 */     if (chunkX < 0)
/*     */     {
/*  55 */       chunkX -= this.field_82665_g - 1;
/*     */     }
/*     */     
/*  58 */     if (chunkZ < 0)
/*     */     {
/*  60 */       chunkZ -= this.field_82665_g - 1;
/*     */     }
/*     */     
/*  63 */     int k = chunkX / this.field_82665_g;
/*  64 */     int l = chunkZ / this.field_82665_g;
/*  65 */     Random random = this.worldObj.setRandomSeed(k, l, 10387312);
/*  66 */     k *= this.field_82665_g;
/*  67 */     l *= this.field_82665_g;
/*  68 */     k += random.nextInt(this.field_82665_g - this.field_82666_h);
/*  69 */     l += random.nextInt(this.field_82665_g - this.field_82666_h);
/*     */     
/*  71 */     if (i == k && j == l) {
/*     */       
/*  73 */       boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, villageSpawnBiomes);
/*     */       
/*  75 */       if (flag)
/*     */       {
/*  77 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected StructureStart getStructureStart(int chunkX, int chunkZ) {
/*  86 */     return new Start(this.worldObj, this.rand, chunkX, chunkZ, this.terrainType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Start
/*     */     extends StructureStart
/*     */   {
/*     */     private boolean hasMoreThanTwoComponents;
/*     */ 
/*     */     
/*     */     public Start() {}
/*     */     
/*     */     public Start(World worldIn, Random rand, int x, int z, int size) {
/*  99 */       super(x, z);
/* 100 */       List<StructureVillagePieces.PieceWeight> list = StructureVillagePieces.getStructureVillageWeightedPieceList(rand, size);
/* 101 */       StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(worldIn.getWorldChunkManager(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, size);
/* 102 */       this.components.add(structurevillagepieces$start);
/* 103 */       structurevillagepieces$start.buildComponent(structurevillagepieces$start, this.components, rand);
/* 104 */       List<StructureComponent> list1 = structurevillagepieces$start.field_74930_j;
/* 105 */       List<StructureComponent> list2 = structurevillagepieces$start.field_74932_i;
/*     */       
/* 107 */       while (!list1.isEmpty() || !list2.isEmpty()) {
/*     */         
/* 109 */         if (list1.isEmpty()) {
/*     */           
/* 111 */           int i = rand.nextInt(list2.size());
/* 112 */           StructureComponent structurecomponent = list2.remove(i);
/* 113 */           structurecomponent.buildComponent(structurevillagepieces$start, this.components, rand);
/*     */           
/*     */           continue;
/*     */         } 
/* 117 */         int j = rand.nextInt(list1.size());
/* 118 */         StructureComponent structurecomponent2 = list1.remove(j);
/* 119 */         structurecomponent2.buildComponent(structurevillagepieces$start, this.components, rand);
/*     */       } 
/*     */ 
/*     */       
/* 123 */       updateBoundingBox();
/* 124 */       int k = 0;
/*     */       
/* 126 */       for (StructureComponent structurecomponent1 : this.components) {
/*     */         
/* 128 */         if (!(structurecomponent1 instanceof StructureVillagePieces.Road))
/*     */         {
/* 130 */           k++;
/*     */         }
/*     */       } 
/*     */       
/* 134 */       this.hasMoreThanTwoComponents = (k > 2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSizeableStructure() {
/* 139 */       return this.hasMoreThanTwoComponents;
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeToNBT(NBTTagCompound tagCompound) {
/* 144 */       super.writeToNBT(tagCompound);
/* 145 */       tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
/*     */     }
/*     */ 
/*     */     
/*     */     public void readFromNBT(NBTTagCompound tagCompound) {
/* 150 */       super.readFromNBT(tagCompound);
/* 151 */       this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\structure\MapGenVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */