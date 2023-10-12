/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAIFleeSun
/*    */   extends EntityAIBase
/*    */ {
/*    */   private EntityCreature theCreature;
/*    */   private double shelterX;
/*    */   private double shelterY;
/*    */   private double shelterZ;
/*    */   private double movementSpeed;
/*    */   private World theWorld;
/*    */   
/*    */   public EntityAIFleeSun(EntityCreature theCreatureIn, double movementSpeedIn) {
/* 20 */     this.theCreature = theCreatureIn;
/* 21 */     this.movementSpeed = movementSpeedIn;
/* 22 */     this.theWorld = theCreatureIn.worldObj;
/* 23 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 28 */     if (!this.theWorld.isDaytime())
/*    */     {
/* 30 */       return false;
/*    */     }
/* 32 */     if (!this.theCreature.isBurning())
/*    */     {
/* 34 */       return false;
/*    */     }
/* 36 */     if (!this.theWorld.canSeeSky(new BlockPos(this.theCreature.posX, (this.theCreature.getEntityBoundingBox()).minY, this.theCreature.posZ)))
/*    */     {
/* 38 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 42 */     Vec3 vec3 = findPossibleShelter();
/*    */     
/* 44 */     if (vec3 == null)
/*    */     {
/* 46 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 50 */     this.shelterX = vec3.xCoord;
/* 51 */     this.shelterY = vec3.yCoord;
/* 52 */     this.shelterZ = vec3.zCoord;
/* 53 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 60 */     return !this.theCreature.getNavigator().noPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 65 */     this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
/*    */   }
/*    */ 
/*    */   
/*    */   private Vec3 findPossibleShelter() {
/* 70 */     Random random = this.theCreature.getRNG();
/* 71 */     BlockPos blockpos = new BlockPos(this.theCreature.posX, (this.theCreature.getEntityBoundingBox()).minY, this.theCreature.posZ);
/*    */     
/* 73 */     for (int i = 0; i < 10; i++) {
/*    */       
/* 75 */       BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
/*    */       
/* 77 */       if (!this.theWorld.canSeeSky(blockpos1) && this.theCreature.getBlockPathWeight(blockpos1) < 0.0F)
/*    */       {
/* 79 */         return new Vec3(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
/*    */       }
/*    */     } 
/*    */     
/* 83 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIFleeSun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */