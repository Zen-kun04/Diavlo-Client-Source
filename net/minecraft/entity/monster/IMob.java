/*    */ package net.minecraft.entity.monster;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.IAnimals;
/*    */ 
/*    */ public interface IMob
/*    */   extends IAnimals {
/*  9 */   public static final Predicate<Entity> mobSelector = new Predicate<Entity>()
/*    */     {
/*    */       public boolean apply(Entity p_apply_1_)
/*    */       {
/* 13 */         return p_apply_1_ instanceof IMob;
/*    */       }
/*    */     };
/* 16 */   public static final Predicate<Entity> VISIBLE_MOB_SELECTOR = new Predicate<Entity>()
/*    */     {
/*    */       public boolean apply(Entity p_apply_1_)
/*    */       {
/* 20 */         return (p_apply_1_ instanceof IMob && !p_apply_1_.isInvisible());
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\IMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */