/*    */ package net.minecraft.world.pathfinder;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.pathfinding.PathPoint;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ 
/*    */ public class SwimNodeProcessor
/*    */   extends NodeProcessor
/*    */ {
/*    */   public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn) {
/* 16 */     super.initProcessor(iblockaccessIn, entityIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public void postProcess() {
/* 21 */     super.postProcess();
/*    */   }
/*    */ 
/*    */   
/*    */   public PathPoint getPathPointTo(Entity entityIn) {
/* 26 */     return openPoint(MathHelper.floor_double((entityIn.getEntityBoundingBox()).minX), MathHelper.floor_double((entityIn.getEntityBoundingBox()).minY + 0.5D), MathHelper.floor_double((entityIn.getEntityBoundingBox()).minZ));
/*    */   }
/*    */ 
/*    */   
/*    */   public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double target) {
/* 31 */     return openPoint(MathHelper.floor_double(x - (entityIn.width / 2.0F)), MathHelper.floor_double(y + 0.5D), MathHelper.floor_double(target - (entityIn.width / 2.0F)));
/*    */   }
/*    */ 
/*    */   
/*    */   public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
/* 36 */     int i = 0;
/*    */     
/* 38 */     for (EnumFacing enumfacing : EnumFacing.values()) {
/*    */       
/* 40 */       PathPoint pathpoint = getSafePoint(entityIn, currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());
/*    */       
/* 42 */       if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
/*    */       {
/* 44 */         pathOptions[i++] = pathpoint;
/*    */       }
/*    */     } 
/*    */     
/* 48 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   private PathPoint getSafePoint(Entity entityIn, int x, int y, int z) {
/* 53 */     int i = func_176186_b(entityIn, x, y, z);
/* 54 */     return (i == -1) ? openPoint(x, y, z) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   private int func_176186_b(Entity entityIn, int x, int y, int z) {
/* 59 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*    */     
/* 61 */     for (int i = x; i < x + this.entitySizeX; i++) {
/*    */       
/* 63 */       for (int j = y; j < y + this.entitySizeY; j++) {
/*    */         
/* 65 */         for (int k = z; k < z + this.entitySizeZ; k++) {
/*    */           
/* 67 */           Block block = this.blockaccess.getBlockState((BlockPos)blockpos$mutableblockpos.set(i, j, k)).getBlock();
/*    */           
/* 69 */           if (block.getMaterial() != Material.water)
/*    */           {
/* 71 */             return 0;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 77 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\pathfinder\SwimNodeProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */