/*     */ package net.optifine;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3i;
/*     */ 
/*     */ 
/*     */ public class BlockPosM
/*     */   extends BlockPos
/*     */ {
/*     */   private int mx;
/*     */   private int my;
/*     */   private int mz;
/*     */   private int level;
/*     */   private BlockPosM[] facings;
/*     */   private boolean needsUpdate;
/*     */   
/*     */   public BlockPosM(int x, int y, int z) {
/*  22 */     this(x, y, z, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPosM(double xIn, double yIn, double zIn) {
/*  27 */     this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPosM(int x, int y, int z, int level) {
/*  32 */     super(0, 0, 0);
/*  33 */     this.mx = x;
/*  34 */     this.my = y;
/*  35 */     this.mz = z;
/*  36 */     this.level = level;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX() {
/*  41 */     return this.mx;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/*  46 */     return this.my;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getZ() {
/*  51 */     return this.mz;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXyz(int x, int y, int z) {
/*  56 */     this.mx = x;
/*  57 */     this.my = y;
/*  58 */     this.mz = z;
/*  59 */     this.needsUpdate = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXyz(double xIn, double yIn, double zIn) {
/*  64 */     setXyz(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPosM set(Vec3i vec) {
/*  69 */     setXyz(vec.getX(), vec.getY(), vec.getZ());
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPosM set(int xIn, int yIn, int zIn) {
/*  75 */     setXyz(xIn, yIn, zIn);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos offsetMutable(EnumFacing facing) {
/*  81 */     return offset(facing);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos offset(EnumFacing facing) {
/*  86 */     if (this.level <= 0)
/*     */     {
/*  88 */       return super.offset(facing, 1);
/*     */     }
/*     */ 
/*     */     
/*  92 */     if (this.facings == null)
/*     */     {
/*  94 */       this.facings = new BlockPosM[EnumFacing.VALUES.length];
/*     */     }
/*     */     
/*  97 */     if (this.needsUpdate)
/*     */     {
/*  99 */       update();
/*     */     }
/*     */     
/* 102 */     int i = facing.getIndex();
/* 103 */     BlockPosM blockposm = this.facings[i];
/*     */     
/* 105 */     if (blockposm == null) {
/*     */       
/* 107 */       int j = this.mx + facing.getFrontOffsetX();
/* 108 */       int k = this.my + facing.getFrontOffsetY();
/* 109 */       int l = this.mz + facing.getFrontOffsetZ();
/* 110 */       blockposm = new BlockPosM(j, k, l, this.level - 1);
/* 111 */       this.facings[i] = blockposm;
/*     */     } 
/*     */     
/* 114 */     return blockposm;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos offset(EnumFacing facing, int n) {
/* 120 */     return (n == 1) ? offset(facing) : super.offset(facing, n);
/*     */   }
/*     */ 
/*     */   
/*     */   private void update() {
/* 125 */     for (int i = 0; i < 6; i++) {
/*     */       
/* 127 */       BlockPosM blockposm = this.facings[i];
/*     */       
/* 129 */       if (blockposm != null) {
/*     */         
/* 131 */         EnumFacing enumfacing = EnumFacing.VALUES[i];
/* 132 */         int j = this.mx + enumfacing.getFrontOffsetX();
/* 133 */         int k = this.my + enumfacing.getFrontOffsetY();
/* 134 */         int l = this.mz + enumfacing.getFrontOffsetZ();
/* 135 */         blockposm.setXyz(j, k, l);
/*     */       } 
/*     */     } 
/*     */     
/* 139 */     this.needsUpdate = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos toImmutable() {
/* 144 */     return new BlockPos(this.mx, this.my, this.mz);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Iterable getAllInBoxMutable(BlockPos from, BlockPos to) {
/* 149 */     final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
/* 150 */     final BlockPos blockpos1 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
/* 151 */     return new Iterable()
/*     */       {
/*     */         public Iterator iterator()
/*     */         {
/* 155 */           return (Iterator)new AbstractIterator()
/*     */             {
/* 157 */               private BlockPosM theBlockPosM = null;
/*     */               
/*     */               protected BlockPosM computeNext0() {
/* 160 */                 if (this.theBlockPosM == null) {
/*     */                   
/* 162 */                   this.theBlockPosM = new BlockPosM(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 3);
/* 163 */                   return this.theBlockPosM;
/*     */                 } 
/* 165 */                 if (this.theBlockPosM.equals(blockpos1))
/*     */                 {
/* 167 */                   return (BlockPosM)endOfData();
/*     */                 }
/*     */ 
/*     */                 
/* 171 */                 int i = this.theBlockPosM.getX();
/* 172 */                 int j = this.theBlockPosM.getY();
/* 173 */                 int k = this.theBlockPosM.getZ();
/*     */                 
/* 175 */                 if (i < blockpos1.getX()) {
/*     */                   
/* 177 */                   i++;
/*     */                 }
/* 179 */                 else if (j < blockpos1.getY()) {
/*     */                   
/* 181 */                   i = blockpos.getX();
/* 182 */                   j++;
/*     */                 }
/* 184 */                 else if (k < blockpos1.getZ()) {
/*     */                   
/* 186 */                   i = blockpos.getX();
/* 187 */                   j = blockpos.getY();
/* 188 */                   k++;
/*     */                 } 
/*     */                 
/* 191 */                 this.theBlockPosM.setXyz(i, j, k);
/* 192 */                 return this.theBlockPosM;
/*     */               }
/*     */ 
/*     */               
/*     */               protected Object computeNext() {
/* 197 */                 return computeNext0();
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\BlockPosM.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */