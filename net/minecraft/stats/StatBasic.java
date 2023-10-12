/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class StatBasic
/*    */   extends StatBase
/*    */ {
/*    */   public StatBasic(String statIdIn, IChatComponent statNameIn, IStatType typeIn) {
/*  9 */     super(statIdIn, statNameIn, typeIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public StatBasic(String statIdIn, IChatComponent statNameIn) {
/* 14 */     super(statIdIn, statNameIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public StatBase registerStat() {
/* 19 */     super.registerStat();
/* 20 */     StatList.generalStats.add(this);
/* 21 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\stats\StatBasic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */