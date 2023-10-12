/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ 
/*    */ public class EntitySenses
/*    */ {
/*    */   EntityLiving entityObj;
/* 11 */   List<Entity> seenEntities = Lists.newArrayList();
/* 12 */   List<Entity> unseenEntities = Lists.newArrayList();
/*    */ 
/*    */   
/*    */   public EntitySenses(EntityLiving entityObjIn) {
/* 16 */     this.entityObj = entityObjIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearSensingCache() {
/* 21 */     this.seenEntities.clear();
/* 22 */     this.unseenEntities.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSee(Entity entityIn) {
/* 27 */     if (this.seenEntities.contains(entityIn))
/*    */     {
/* 29 */       return true;
/*    */     }
/* 31 */     if (this.unseenEntities.contains(entityIn))
/*    */     {
/* 33 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 37 */     this.entityObj.worldObj.theProfiler.startSection("canSee");
/* 38 */     boolean flag = this.entityObj.canEntityBeSeen(entityIn);
/* 39 */     this.entityObj.worldObj.theProfiler.endSection();
/*    */     
/* 41 */     if (flag) {
/*    */       
/* 43 */       this.seenEntities.add(entityIn);
/*    */     }
/*    */     else {
/*    */       
/* 47 */       this.unseenEntities.add(entityIn);
/*    */     } 
/*    */     
/* 50 */     return flag;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntitySenses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */