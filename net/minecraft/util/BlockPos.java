/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.entity.Entity;
/*     */ 
/*     */ public class BlockPos
/*     */   extends Vec3i
/*     */ {
/*  10 */   public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
/*  11 */   private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
/*  12 */   private static final int NUM_Z_BITS = NUM_X_BITS;
/*  13 */   private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
/*  14 */   private static final int Y_SHIFT = 0 + NUM_Z_BITS;
/*  15 */   private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
/*  16 */   private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
/*  17 */   private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
/*  18 */   private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;
/*     */ 
/*     */   
/*     */   public BlockPos(int x, int y, int z) {
/*  22 */     super(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos(double x, double y, double z) {
/*  27 */     super(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos(Entity source) {
/*  32 */     this(source.posX, source.posY, source.posZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos(Vec3 source) {
/*  37 */     this(source.xCoord, source.yCoord, source.zCoord);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos(Vec3i source) {
/*  42 */     this(source.getX(), source.getY(), source.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos add(double x, double y, double z) {
/*  47 */     return (x == 0.0D && y == 0.0D && z == 0.0D) ? this : new BlockPos(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos add(int x, int y, int z) {
/*  52 */     return (x == 0 && y == 0 && z == 0) ? this : new BlockPos(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos add(Vec3i vec) {
/*  57 */     return (vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0) ? this : new BlockPos(getX() + vec.getX(), getY() + vec.getY(), getZ() + vec.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos subtract(Vec3i vec) {
/*  62 */     return (vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0) ? this : new BlockPos(getX() - vec.getX(), getY() - vec.getY(), getZ() - vec.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos up() {
/*  67 */     return up(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos up(int n) {
/*  72 */     return offset(EnumFacing.UP, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos down() {
/*  77 */     return down(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos down(int n) {
/*  82 */     return offset(EnumFacing.DOWN, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos north() {
/*  87 */     return north(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos north(int n) {
/*  92 */     return offset(EnumFacing.NORTH, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos south() {
/*  97 */     return south(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos south(int n) {
/* 102 */     return offset(EnumFacing.SOUTH, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos west() {
/* 107 */     return west(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos west(int n) {
/* 112 */     return offset(EnumFacing.WEST, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos east() {
/* 117 */     return east(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos east(int n) {
/* 122 */     return offset(EnumFacing.EAST, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos offset(EnumFacing facing) {
/* 127 */     return offset(facing, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos offset(EnumFacing facing, int n) {
/* 132 */     return (n == 0) ? this : new BlockPos(getX() + facing.getFrontOffsetX() * n, getY() + facing.getFrontOffsetY() * n, getZ() + facing.getFrontOffsetZ() * n);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos crossProduct(Vec3i vec) {
/* 137 */     return new BlockPos(getY() * vec.getZ() - getZ() * vec.getY(), getZ() * vec.getX() - getX() * vec.getZ(), getX() * vec.getY() - getY() * vec.getX());
/*     */   }
/*     */ 
/*     */   
/*     */   public long toLong() {
/* 142 */     return (getX() & X_MASK) << X_SHIFT | (getY() & Y_MASK) << Y_SHIFT | (getZ() & Z_MASK) << 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockPos fromLong(long serialized) {
/* 147 */     int i = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
/* 148 */     int j = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
/* 149 */     int k = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
/* 150 */     return new BlockPos(i, j, k);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Iterable<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
/* 155 */     final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
/* 156 */     final BlockPos blockpos1 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
/* 157 */     return new Iterable<BlockPos>()
/*     */       {
/*     */         public Iterator<BlockPos> iterator()
/*     */         {
/* 161 */           return (Iterator<BlockPos>)new AbstractIterator<BlockPos>()
/*     */             {
/* 163 */               private BlockPos lastReturned = null;
/*     */               
/*     */               protected BlockPos computeNext() {
/* 166 */                 if (this.lastReturned == null) {
/*     */                   
/* 168 */                   this.lastReturned = blockpos;
/* 169 */                   return this.lastReturned;
/*     */                 } 
/* 171 */                 if (this.lastReturned.equals(blockpos1))
/*     */                 {
/* 173 */                   return (BlockPos)endOfData();
/*     */                 }
/*     */ 
/*     */                 
/* 177 */                 int i = this.lastReturned.getX();
/* 178 */                 int j = this.lastReturned.getY();
/* 179 */                 int k = this.lastReturned.getZ();
/*     */                 
/* 181 */                 if (i < blockpos1.getX()) {
/*     */                   
/* 183 */                   i++;
/*     */                 }
/* 185 */                 else if (j < blockpos1.getY()) {
/*     */                   
/* 187 */                   i = blockpos.getX();
/* 188 */                   j++;
/*     */                 }
/* 190 */                 else if (k < blockpos1.getZ()) {
/*     */                   
/* 192 */                   i = blockpos.getX();
/* 193 */                   j = blockpos.getY();
/* 194 */                   k++;
/*     */                 } 
/*     */                 
/* 197 */                 this.lastReturned = new BlockPos(i, j, k);
/* 198 */                 return this.lastReturned;
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Iterable<MutableBlockPos> getAllInBoxMutable(BlockPos from, BlockPos to) {
/* 208 */     final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
/* 209 */     final BlockPos blockpos1 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
/* 210 */     return new Iterable<MutableBlockPos>()
/*     */       {
/*     */         public Iterator<BlockPos.MutableBlockPos> iterator()
/*     */         {
/* 214 */           return (Iterator<BlockPos.MutableBlockPos>)new AbstractIterator<BlockPos.MutableBlockPos>()
/*     */             {
/* 216 */               private BlockPos.MutableBlockPos theBlockPos = null;
/*     */               
/*     */               protected BlockPos.MutableBlockPos computeNext() {
/* 219 */                 if (this.theBlockPos == null) {
/*     */                   
/* 221 */                   this.theBlockPos = new BlockPos.MutableBlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/* 222 */                   return this.theBlockPos;
/*     */                 } 
/* 224 */                 if (this.theBlockPos.equals(blockpos1))
/*     */                 {
/* 226 */                   return (BlockPos.MutableBlockPos)endOfData();
/*     */                 }
/*     */ 
/*     */                 
/* 230 */                 int i = this.theBlockPos.getX();
/* 231 */                 int j = this.theBlockPos.getY();
/* 232 */                 int k = this.theBlockPos.getZ();
/*     */                 
/* 234 */                 if (i < blockpos1.getX()) {
/*     */                   
/* 236 */                   i++;
/*     */                 }
/* 238 */                 else if (j < blockpos1.getY()) {
/*     */                   
/* 240 */                   i = blockpos.getX();
/* 241 */                   j++;
/*     */                 }
/* 243 */                 else if (k < blockpos1.getZ()) {
/*     */                   
/* 245 */                   i = blockpos.getX();
/* 246 */                   j = blockpos.getY();
/* 247 */                   k++;
/*     */                 } 
/*     */                 
/* 250 */                 this.theBlockPos.x = i;
/* 251 */                 this.theBlockPos.y = j;
/* 252 */                 this.theBlockPos.z = k;
/* 253 */                 return this.theBlockPos;
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class MutableBlockPos
/*     */     extends BlockPos
/*     */   {
/*     */     private int x;
/*     */     private int y;
/*     */     private int z;
/*     */     
/*     */     public MutableBlockPos() {
/* 269 */       this(0, 0, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public MutableBlockPos(int x_, int y_, int z_) {
/* 274 */       super(0, 0, 0);
/* 275 */       this.x = x_;
/* 276 */       this.y = y_;
/* 277 */       this.z = z_;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getX() {
/* 282 */       return this.x;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getY() {
/* 287 */       return this.y;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getZ() {
/* 292 */       return this.z;
/*     */     }
/*     */ 
/*     */     
/*     */     public MutableBlockPos set(int xIn, int yIn, int zIn) {
/* 297 */       this.x = xIn;
/* 298 */       this.y = yIn;
/* 299 */       this.z = zIn;
/* 300 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\BlockPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */