/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.pathfinder.NodeProcessor;
/*     */ 
/*     */ public class PathFinder
/*     */ {
/*  10 */   private Path path = new Path();
/*  11 */   private PathPoint[] pathOptions = new PathPoint[32];
/*     */   
/*     */   private NodeProcessor nodeProcessor;
/*     */   
/*     */   public PathFinder(NodeProcessor nodeProcessorIn) {
/*  16 */     this.nodeProcessor = nodeProcessorIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityFrom, Entity entityTo, float dist) {
/*  21 */     return createEntityPathTo(blockaccess, entityFrom, entityTo.posX, (entityTo.getEntityBoundingBox()).minY, entityTo.posZ, dist);
/*     */   }
/*     */ 
/*     */   
/*     */   public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, BlockPos targetPos, float dist) {
/*  26 */     return createEntityPathTo(blockaccess, entityIn, (targetPos.getX() + 0.5F), (targetPos.getY() + 0.5F), (targetPos.getZ() + 0.5F), dist);
/*     */   }
/*     */ 
/*     */   
/*     */   private PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, double x, double y, double z, float distance) {
/*  31 */     this.path.clearPath();
/*  32 */     this.nodeProcessor.initProcessor(blockaccess, entityIn);
/*  33 */     PathPoint pathpoint = this.nodeProcessor.getPathPointTo(entityIn);
/*  34 */     PathPoint pathpoint1 = this.nodeProcessor.getPathPointToCoords(entityIn, x, y, z);
/*  35 */     PathEntity pathentity = addToPath(entityIn, pathpoint, pathpoint1, distance);
/*  36 */     this.nodeProcessor.postProcess();
/*  37 */     return pathentity;
/*     */   }
/*     */ 
/*     */   
/*     */   private PathEntity addToPath(Entity entityIn, PathPoint pathpointStart, PathPoint pathpointEnd, float maxDistance) {
/*  42 */     pathpointStart.totalPathDistance = 0.0F;
/*  43 */     pathpointStart.distanceToNext = pathpointStart.distanceToSquared(pathpointEnd);
/*  44 */     pathpointStart.distanceToTarget = pathpointStart.distanceToNext;
/*  45 */     this.path.clearPath();
/*  46 */     this.path.addPoint(pathpointStart);
/*  47 */     PathPoint pathpoint = pathpointStart;
/*     */     
/*  49 */     while (!this.path.isPathEmpty()) {
/*     */       
/*  51 */       PathPoint pathpoint1 = this.path.dequeue();
/*     */       
/*  53 */       if (pathpoint1.equals(pathpointEnd))
/*     */       {
/*  55 */         return createEntityPath(pathpointStart, pathpointEnd);
/*     */       }
/*     */       
/*  58 */       if (pathpoint1.distanceToSquared(pathpointEnd) < pathpoint.distanceToSquared(pathpointEnd))
/*     */       {
/*  60 */         pathpoint = pathpoint1;
/*     */       }
/*     */       
/*  63 */       pathpoint1.visited = true;
/*  64 */       int i = this.nodeProcessor.findPathOptions(this.pathOptions, entityIn, pathpoint1, pathpointEnd, maxDistance);
/*     */       
/*  66 */       for (int j = 0; j < i; j++) {
/*     */         
/*  68 */         PathPoint pathpoint2 = this.pathOptions[j];
/*  69 */         float f = pathpoint1.totalPathDistance + pathpoint1.distanceToSquared(pathpoint2);
/*     */         
/*  71 */         if (f < maxDistance * 2.0F && (!pathpoint2.isAssigned() || f < pathpoint2.totalPathDistance)) {
/*     */           
/*  73 */           pathpoint2.previous = pathpoint1;
/*  74 */           pathpoint2.totalPathDistance = f;
/*  75 */           pathpoint2.distanceToNext = pathpoint2.distanceToSquared(pathpointEnd);
/*     */           
/*  77 */           if (pathpoint2.isAssigned()) {
/*     */             
/*  79 */             this.path.changeDistance(pathpoint2, pathpoint2.totalPathDistance + pathpoint2.distanceToNext);
/*     */           }
/*     */           else {
/*     */             
/*  83 */             pathpoint2.distanceToTarget = pathpoint2.totalPathDistance + pathpoint2.distanceToNext;
/*  84 */             this.path.addPoint(pathpoint2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     if (pathpoint == pathpointStart)
/*     */     {
/*  92 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  96 */     return createEntityPath(pathpointStart, pathpoint);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private PathEntity createEntityPath(PathPoint start, PathPoint end) {
/* 102 */     int i = 1;
/*     */     
/* 104 */     for (PathPoint pathpoint = end; pathpoint.previous != null; pathpoint = pathpoint.previous)
/*     */     {
/* 106 */       i++;
/*     */     }
/*     */     
/* 109 */     PathPoint[] apathpoint = new PathPoint[i];
/* 110 */     PathPoint pathpoint1 = end;
/* 111 */     i--;
/*     */     
/* 113 */     for (apathpoint[i] = end; pathpoint1.previous != null; apathpoint[i] = pathpoint1) {
/*     */       
/* 115 */       pathpoint1 = pathpoint1.previous;
/* 116 */       i--;
/*     */     } 
/*     */     
/* 119 */     return new PathEntity(apathpoint);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\pathfinding\PathFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */