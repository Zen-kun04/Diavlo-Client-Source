/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Random;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class StructureStart {
/*  13 */   protected LinkedList<StructureComponent> components = new LinkedList<>();
/*     */   
/*     */   protected StructureBoundingBox boundingBox;
/*     */   
/*     */   private int chunkPosX;
/*     */   
/*     */   private int chunkPosZ;
/*     */   
/*     */   public StructureStart() {}
/*     */   
/*     */   public StructureStart(int chunkX, int chunkZ) {
/*  24 */     this.chunkPosX = chunkX;
/*  25 */     this.chunkPosZ = chunkZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureBoundingBox getBoundingBox() {
/*  30 */     return this.boundingBox;
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkedList<StructureComponent> getComponents() {
/*  35 */     return this.components;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb) {
/*  40 */     Iterator<StructureComponent> iterator = this.components.iterator();
/*     */     
/*  42 */     while (iterator.hasNext()) {
/*     */       
/*  44 */       StructureComponent structurecomponent = iterator.next();
/*     */       
/*  46 */       if (structurecomponent.getBoundingBox().intersectsWith(structurebb) && !structurecomponent.addComponentParts(worldIn, rand, structurebb))
/*     */       {
/*  48 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateBoundingBox() {
/*  55 */     this.boundingBox = StructureBoundingBox.getNewBoundingBox();
/*     */     
/*  57 */     for (StructureComponent structurecomponent : this.components)
/*     */     {
/*  59 */       this.boundingBox.expandTo(structurecomponent.getBoundingBox());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound writeStructureComponentsToNBT(int chunkX, int chunkZ) {
/*  65 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  66 */     nbttagcompound.setString("id", MapGenStructureIO.getStructureStartName(this));
/*  67 */     nbttagcompound.setInteger("ChunkX", chunkX);
/*  68 */     nbttagcompound.setInteger("ChunkZ", chunkZ);
/*  69 */     nbttagcompound.setTag("BB", (NBTBase)this.boundingBox.toNBTTagIntArray());
/*  70 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/*  72 */     for (StructureComponent structurecomponent : this.components)
/*     */     {
/*  74 */       nbttaglist.appendTag((NBTBase)structurecomponent.createStructureBaseNBT());
/*     */     }
/*     */     
/*  77 */     nbttagcompound.setTag("Children", (NBTBase)nbttaglist);
/*  78 */     writeToNBT(nbttagcompound);
/*  79 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound tagCompound) {}
/*     */ 
/*     */   
/*     */   public void readStructureComponentsFromNBT(World worldIn, NBTTagCompound tagCompound) {
/*  88 */     this.chunkPosX = tagCompound.getInteger("ChunkX");
/*  89 */     this.chunkPosZ = tagCompound.getInteger("ChunkZ");
/*     */     
/*  91 */     if (tagCompound.hasKey("BB"))
/*     */     {
/*  93 */       this.boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
/*     */     }
/*     */     
/*  96 */     NBTTagList nbttaglist = tagCompound.getTagList("Children", 10);
/*     */     
/*  98 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/* 100 */       this.components.add(MapGenStructureIO.getStructureComponent(nbttaglist.getCompoundTagAt(i), worldIn));
/*     */     }
/*     */     
/* 103 */     readFromNBT(tagCompound);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound tagCompound) {}
/*     */ 
/*     */   
/*     */   protected void markAvailableHeight(World worldIn, Random rand, int p_75067_3_) {
/* 112 */     int i = worldIn.getSeaLevel() - p_75067_3_;
/* 113 */     int j = this.boundingBox.getYSize() + 1;
/*     */     
/* 115 */     if (j < i)
/*     */     {
/* 117 */       j += rand.nextInt(i - j);
/*     */     }
/*     */     
/* 120 */     int k = j - this.boundingBox.maxY;
/* 121 */     this.boundingBox.offset(0, k, 0);
/*     */     
/* 123 */     for (StructureComponent structurecomponent : this.components)
/*     */     {
/* 125 */       structurecomponent.func_181138_a(0, k, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setRandomHeight(World worldIn, Random rand, int p_75070_3_, int p_75070_4_) {
/* 131 */     int i = p_75070_4_ - p_75070_3_ + 1 - this.boundingBox.getYSize();
/* 132 */     int j = 1;
/*     */     
/* 134 */     if (i > 1) {
/*     */       
/* 136 */       j = p_75070_3_ + rand.nextInt(i);
/*     */     }
/*     */     else {
/*     */       
/* 140 */       j = p_75070_3_;
/*     */     } 
/*     */     
/* 143 */     int k = j - this.boundingBox.minY;
/* 144 */     this.boundingBox.offset(0, k, 0);
/*     */     
/* 146 */     for (StructureComponent structurecomponent : this.components)
/*     */     {
/* 148 */       structurecomponent.func_181138_a(0, k, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSizeableStructure() {
/* 154 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_175788_a(ChunkCoordIntPair pair) {
/* 159 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_175787_b(ChunkCoordIntPair pair) {}
/*     */ 
/*     */   
/*     */   public int getChunkPosX() {
/* 168 */     return this.chunkPosX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChunkPosZ() {
/* 173 */     return this.chunkPosZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\structure\StructureStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */