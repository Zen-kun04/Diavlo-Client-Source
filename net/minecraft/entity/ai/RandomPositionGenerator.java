/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class RandomPositionGenerator
/*     */ {
/*  11 */   private static Vec3 staticVector = new Vec3(0.0D, 0.0D, 0.0D);
/*     */ 
/*     */   
/*     */   public static Vec3 findRandomTarget(EntityCreature entitycreatureIn, int xz, int y) {
/*  15 */     return findRandomTargetBlock(entitycreatureIn, xz, y, (Vec3)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vec3 findRandomTargetBlockTowards(EntityCreature entitycreatureIn, int xz, int y, Vec3 targetVec3) {
/*  20 */     staticVector = targetVec3.subtract(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ);
/*  21 */     return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vec3 findRandomTargetBlockAwayFrom(EntityCreature entitycreatureIn, int xz, int y, Vec3 targetVec3) {
/*  26 */     staticVector = (new Vec3(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ)).subtract(targetVec3);
/*  27 */     return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
/*     */   }
/*     */   
/*     */   private static Vec3 findRandomTargetBlock(EntityCreature entitycreatureIn, int xz, int y, Vec3 targetVec3) {
/*     */     boolean flag1;
/*  32 */     Random random = entitycreatureIn.getRNG();
/*  33 */     boolean flag = false;
/*  34 */     int i = 0;
/*  35 */     int j = 0;
/*  36 */     int k = 0;
/*  37 */     float f = -99999.0F;
/*     */ 
/*     */     
/*  40 */     if (entitycreatureIn.hasHome()) {
/*     */       
/*  42 */       double d0 = entitycreatureIn.getHomePosition().distanceSq(MathHelper.floor_double(entitycreatureIn.posX), MathHelper.floor_double(entitycreatureIn.posY), MathHelper.floor_double(entitycreatureIn.posZ)) + 4.0D;
/*  43 */       double d1 = (entitycreatureIn.getMaximumHomeDistance() + xz);
/*  44 */       flag1 = (d0 < d1 * d1);
/*     */     }
/*     */     else {
/*     */       
/*  48 */       flag1 = false;
/*     */     } 
/*     */     
/*  51 */     for (int j1 = 0; j1 < 10; j1++) {
/*     */       
/*  53 */       int l = random.nextInt(2 * xz + 1) - xz;
/*  54 */       int k1 = random.nextInt(2 * y + 1) - y;
/*  55 */       int i1 = random.nextInt(2 * xz + 1) - xz;
/*     */       
/*  57 */       if (targetVec3 == null || l * targetVec3.xCoord + i1 * targetVec3.zCoord >= 0.0D) {
/*     */         
/*  59 */         if (entitycreatureIn.hasHome() && xz > 1) {
/*     */           
/*  61 */           BlockPos blockpos = entitycreatureIn.getHomePosition();
/*     */           
/*  63 */           if (entitycreatureIn.posX > blockpos.getX()) {
/*     */             
/*  65 */             l -= random.nextInt(xz / 2);
/*     */           }
/*     */           else {
/*     */             
/*  69 */             l += random.nextInt(xz / 2);
/*     */           } 
/*     */           
/*  72 */           if (entitycreatureIn.posZ > blockpos.getZ()) {
/*     */             
/*  74 */             i1 -= random.nextInt(xz / 2);
/*     */           }
/*     */           else {
/*     */             
/*  78 */             i1 += random.nextInt(xz / 2);
/*     */           } 
/*     */         } 
/*     */         
/*  82 */         l += MathHelper.floor_double(entitycreatureIn.posX);
/*  83 */         k1 += MathHelper.floor_double(entitycreatureIn.posY);
/*  84 */         i1 += MathHelper.floor_double(entitycreatureIn.posZ);
/*  85 */         BlockPos blockpos1 = new BlockPos(l, k1, i1);
/*     */         
/*  87 */         if (!flag1 || entitycreatureIn.isWithinHomeDistanceFromPosition(blockpos1)) {
/*     */           
/*  89 */           float f1 = entitycreatureIn.getBlockPathWeight(blockpos1);
/*     */           
/*  91 */           if (f1 > f) {
/*     */             
/*  93 */             f = f1;
/*  94 */             i = l;
/*  95 */             j = k1;
/*  96 */             k = i1;
/*  97 */             flag = true;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     if (flag)
/*     */     {
/* 105 */       return new Vec3(i, j, k);
/*     */     }
/*     */ 
/*     */     
/* 109 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\RandomPositionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */