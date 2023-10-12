/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ 
/*    */ public class MovingObjectPosition
/*    */ {
/*    */   private BlockPos blockPos;
/*    */   public MovingObjectType typeOfHit;
/*    */   public EnumFacing sideHit;
/*    */   public Vec3 hitVec;
/*    */   public Entity entityHit;
/*    */   
/*    */   public MovingObjectPosition(Vec3 hitVecIn, EnumFacing facing, BlockPos blockPosIn) {
/* 15 */     this(MovingObjectType.BLOCK, hitVecIn, facing, blockPosIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public MovingObjectPosition(Vec3 p_i45552_1_, EnumFacing facing) {
/* 20 */     this(MovingObjectType.BLOCK, p_i45552_1_, facing, BlockPos.ORIGIN);
/*    */   }
/*    */ 
/*    */   
/*    */   public MovingObjectPosition(Entity entityIn) {
/* 25 */     this(entityIn, new Vec3(entityIn.posX, entityIn.posY, entityIn.posZ));
/*    */   }
/*    */ 
/*    */   
/*    */   public MovingObjectPosition(MovingObjectType typeOfHitIn, Vec3 hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn) {
/* 30 */     this.typeOfHit = typeOfHitIn;
/* 31 */     this.blockPos = blockPosIn;
/* 32 */     this.sideHit = sideHitIn;
/* 33 */     this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
/*    */   }
/*    */ 
/*    */   
/*    */   public MovingObjectPosition(Entity entityHitIn, Vec3 hitVecIn) {
/* 38 */     this.typeOfHit = MovingObjectType.ENTITY;
/* 39 */     this.entityHit = entityHitIn;
/* 40 */     this.hitVec = hitVecIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getBlockPos() {
/* 45 */     return this.blockPos;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
/*    */   }
/*    */   
/*    */   public enum MovingObjectType
/*    */   {
/* 55 */     MISS,
/* 56 */     BLOCK,
/* 57 */     ENTITY;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\MovingObjectPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */