/*    */ package net.minecraft.pathfinding;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class PathNavigateClimber
/*    */   extends PathNavigateGround
/*    */ {
/*    */   private BlockPos targetPosition;
/*    */   
/*    */   public PathNavigateClimber(EntityLiving entityLivingIn, World worldIn) {
/* 15 */     super(entityLivingIn, worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public PathEntity getPathToPos(BlockPos pos) {
/* 20 */     this.targetPosition = pos;
/* 21 */     return super.getPathToPos(pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public PathEntity getPathToEntityLiving(Entity entityIn) {
/* 26 */     this.targetPosition = new BlockPos(entityIn);
/* 27 */     return super.getPathToEntityLiving(entityIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
/* 32 */     PathEntity pathentity = getPathToEntityLiving(entityIn);
/*    */     
/* 34 */     if (pathentity != null)
/*    */     {
/* 36 */       return setPath(pathentity, speedIn);
/*    */     }
/*    */ 
/*    */     
/* 40 */     this.targetPosition = new BlockPos(entityIn);
/* 41 */     this.speed = speedIn;
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onUpdateNavigation() {
/* 48 */     if (!noPath()) {
/*    */       
/* 50 */       super.onUpdateNavigation();
/*    */ 
/*    */     
/*    */     }
/* 54 */     else if (this.targetPosition != null) {
/*    */       
/* 56 */       double d0 = (this.theEntity.width * this.theEntity.width);
/*    */       
/* 58 */       if (this.theEntity.getDistanceSqToCenter(this.targetPosition) >= d0 && (this.theEntity.posY <= this.targetPosition.getY() || this.theEntity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor_double(this.theEntity.posY), this.targetPosition.getZ())) >= d0)) {
/*    */         
/* 60 */         this.theEntity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
/*    */       }
/*    */       else {
/*    */         
/* 64 */         this.targetPosition = null;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\pathfinding\PathNavigateClimber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */