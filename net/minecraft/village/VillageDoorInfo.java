/*     */ package net.minecraft.village;
/*     */ 
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.Vec3i;
/*     */ 
/*     */ public class VillageDoorInfo
/*     */ {
/*     */   private final BlockPos doorBlockPos;
/*     */   private final BlockPos insideBlock;
/*     */   private final EnumFacing insideDirection;
/*     */   private int lastActivityTimestamp;
/*     */   private boolean isDetachedFromVillageFlag;
/*     */   private int doorOpeningRestrictionCounter;
/*     */   
/*     */   public VillageDoorInfo(BlockPos pos, int p_i45871_2_, int p_i45871_3_, int p_i45871_4_) {
/*  17 */     this(pos, getFaceDirection(p_i45871_2_, p_i45871_3_), p_i45871_4_);
/*     */   }
/*     */ 
/*     */   
/*     */   private static EnumFacing getFaceDirection(int deltaX, int deltaZ) {
/*  22 */     return (deltaX < 0) ? EnumFacing.WEST : ((deltaX > 0) ? EnumFacing.EAST : ((deltaZ < 0) ? EnumFacing.NORTH : EnumFacing.SOUTH));
/*     */   }
/*     */ 
/*     */   
/*     */   public VillageDoorInfo(BlockPos pos, EnumFacing facing, int timestamp) {
/*  27 */     this.doorBlockPos = pos;
/*  28 */     this.insideDirection = facing;
/*  29 */     this.insideBlock = pos.offset(facing, 2);
/*  30 */     this.lastActivityTimestamp = timestamp;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDistanceSquared(int x, int y, int z) {
/*  35 */     return (int)this.doorBlockPos.distanceSq(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDistanceToDoorBlockSq(BlockPos pos) {
/*  40 */     return (int)pos.distanceSq((Vec3i)getDoorBlockPos());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDistanceToInsideBlockSq(BlockPos pos) {
/*  45 */     return (int)this.insideBlock.distanceSq((Vec3i)pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_179850_c(BlockPos pos) {
/*  50 */     int i = pos.getX() - this.doorBlockPos.getX();
/*  51 */     int j = pos.getZ() - this.doorBlockPos.getY();
/*  52 */     return (i * this.insideDirection.getFrontOffsetX() + j * this.insideDirection.getFrontOffsetZ() >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetDoorOpeningRestrictionCounter() {
/*  57 */     this.doorOpeningRestrictionCounter = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementDoorOpeningRestrictionCounter() {
/*  62 */     this.doorOpeningRestrictionCounter++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDoorOpeningRestrictionCounter() {
/*  67 */     return this.doorOpeningRestrictionCounter;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getDoorBlockPos() {
/*  72 */     return this.doorBlockPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getInsideBlockPos() {
/*  77 */     return this.insideBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInsideOffsetX() {
/*  82 */     return this.insideDirection.getFrontOffsetX() * 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInsideOffsetZ() {
/*  87 */     return this.insideDirection.getFrontOffsetZ() * 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInsidePosY() {
/*  92 */     return this.lastActivityTimestamp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_179849_a(int timestamp) {
/*  97 */     this.lastActivityTimestamp = timestamp;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsDetachedFromVillageFlag() {
/* 102 */     return this.isDetachedFromVillageFlag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsDetachedFromVillageFlag(boolean detached) {
/* 107 */     this.isDetachedFromVillageFlag = detached;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\village\VillageDoorInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */