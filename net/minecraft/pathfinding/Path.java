/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ public class Path
/*     */ {
/*   5 */   private PathPoint[] pathPoints = new PathPoint[1024];
/*     */   
/*     */   private int count;
/*     */   
/*     */   public PathPoint addPoint(PathPoint point) {
/*  10 */     if (point.index >= 0)
/*     */     {
/*  12 */       throw new IllegalStateException("OW KNOWS!");
/*     */     }
/*     */ 
/*     */     
/*  16 */     if (this.count == this.pathPoints.length) {
/*     */       
/*  18 */       PathPoint[] apathpoint = new PathPoint[this.count << 1];
/*  19 */       System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
/*  20 */       this.pathPoints = apathpoint;
/*     */     } 
/*     */     
/*  23 */     this.pathPoints[this.count] = point;
/*  24 */     point.index = this.count;
/*  25 */     sortBack(this.count++);
/*  26 */     return point;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearPath() {
/*  32 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public PathPoint dequeue() {
/*  37 */     PathPoint pathpoint = this.pathPoints[0];
/*  38 */     this.pathPoints[0] = this.pathPoints[--this.count];
/*  39 */     this.pathPoints[this.count] = null;
/*     */     
/*  41 */     if (this.count > 0)
/*     */     {
/*  43 */       sortForward(0);
/*     */     }
/*     */     
/*  46 */     pathpoint.index = -1;
/*  47 */     return pathpoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public void changeDistance(PathPoint p_75850_1_, float p_75850_2_) {
/*  52 */     float f = p_75850_1_.distanceToTarget;
/*  53 */     p_75850_1_.distanceToTarget = p_75850_2_;
/*     */     
/*  55 */     if (p_75850_2_ < f) {
/*     */       
/*  57 */       sortBack(p_75850_1_.index);
/*     */     }
/*     */     else {
/*     */       
/*  61 */       sortForward(p_75850_1_.index);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void sortBack(int p_75847_1_) {
/*  67 */     PathPoint pathpoint = this.pathPoints[p_75847_1_];
/*     */ 
/*     */     
/*  70 */     for (float f = pathpoint.distanceToTarget; p_75847_1_ > 0; p_75847_1_ = i) {
/*     */       
/*  72 */       int i = p_75847_1_ - 1 >> 1;
/*  73 */       PathPoint pathpoint1 = this.pathPoints[i];
/*     */       
/*  75 */       if (f >= pathpoint1.distanceToTarget) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/*  80 */       this.pathPoints[p_75847_1_] = pathpoint1;
/*  81 */       pathpoint1.index = p_75847_1_;
/*     */     } 
/*     */     
/*  84 */     this.pathPoints[p_75847_1_] = pathpoint;
/*  85 */     pathpoint.index = p_75847_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   private void sortForward(int p_75846_1_) {
/*  90 */     PathPoint pathpoint = this.pathPoints[p_75846_1_];
/*  91 */     float f = pathpoint.distanceToTarget;
/*     */     while (true) {
/*     */       PathPoint pathpoint2;
/*     */       float f2;
/*  95 */       int i = 1 + (p_75846_1_ << 1);
/*  96 */       int j = i + 1;
/*     */       
/*  98 */       if (i >= this.count) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 103 */       PathPoint pathpoint1 = this.pathPoints[i];
/* 104 */       float f1 = pathpoint1.distanceToTarget;
/*     */ 
/*     */ 
/*     */       
/* 108 */       if (j >= this.count) {
/*     */         
/* 110 */         pathpoint2 = null;
/* 111 */         f2 = Float.POSITIVE_INFINITY;
/*     */       }
/*     */       else {
/*     */         
/* 115 */         pathpoint2 = this.pathPoints[j];
/* 116 */         f2 = pathpoint2.distanceToTarget;
/*     */       } 
/*     */       
/* 119 */       if (f1 < f2) {
/*     */         
/* 121 */         if (f1 >= f) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 126 */         this.pathPoints[p_75846_1_] = pathpoint1;
/* 127 */         pathpoint1.index = p_75846_1_;
/* 128 */         p_75846_1_ = i;
/*     */         
/*     */         continue;
/*     */       } 
/* 132 */       if (f2 >= f) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 137 */       this.pathPoints[p_75846_1_] = pathpoint2;
/* 138 */       pathpoint2.index = p_75846_1_;
/* 139 */       p_75846_1_ = j;
/*     */     } 
/*     */ 
/*     */     
/* 143 */     this.pathPoints[p_75846_1_] = pathpoint;
/* 144 */     pathpoint.index = p_75846_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathEmpty() {
/* 149 */     return (this.count == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\pathfinding\Path.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */