/*    */ package net.minecraft.scoreboard;
/*    */ 
/*    */ 
/*    */ public class ScoreObjective
/*    */ {
/*    */   private final Scoreboard theScoreboard;
/*    */   private final String name;
/*    */   private final IScoreObjectiveCriteria objectiveCriteria;
/*    */   private IScoreObjectiveCriteria.EnumRenderType renderType;
/*    */   private String displayName;
/*    */   
/*    */   public ScoreObjective(Scoreboard theScoreboardIn, String nameIn, IScoreObjectiveCriteria objectiveCriteriaIn) {
/* 13 */     this.theScoreboard = theScoreboardIn;
/* 14 */     this.name = nameIn;
/* 15 */     this.objectiveCriteria = objectiveCriteriaIn;
/* 16 */     this.displayName = nameIn;
/* 17 */     this.renderType = objectiveCriteriaIn.getRenderType();
/*    */   }
/*    */ 
/*    */   
/*    */   public Scoreboard getScoreboard() {
/* 22 */     return this.theScoreboard;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 27 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public IScoreObjectiveCriteria getCriteria() {
/* 32 */     return this.objectiveCriteria;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayName() {
/* 37 */     return this.displayName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDisplayName(String nameIn) {
/* 42 */     this.displayName = nameIn;
/* 43 */     this.theScoreboard.onObjectiveDisplayNameChanged(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
/* 48 */     return this.renderType;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRenderType(IScoreObjectiveCriteria.EnumRenderType type) {
/* 53 */     this.renderType = type;
/* 54 */     this.theScoreboard.onObjectiveDisplayNameChanged(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\scoreboard\ScoreObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */