/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.scoreboard.Score;
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ import net.minecraft.scoreboard.Scoreboard;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class ChatComponentScore
/*    */   extends ChatComponentStyle {
/*    */   private final String name;
/*    */   private final String objective;
/* 12 */   private String value = "";
/*    */ 
/*    */   
/*    */   public ChatComponentScore(String nameIn, String objectiveIn) {
/* 16 */     this.name = nameIn;
/* 17 */     this.objective = objectiveIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 22 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getObjective() {
/* 27 */     return this.objective;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(String valueIn) {
/* 32 */     this.value = valueIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnformattedTextForChat() {
/* 37 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*    */     
/* 39 */     if (minecraftserver != null && minecraftserver.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value)) {
/*    */       
/* 41 */       Scoreboard scoreboard = minecraftserver.worldServerForDimension(0).getScoreboard();
/* 42 */       ScoreObjective scoreobjective = scoreboard.getObjective(this.objective);
/*    */       
/* 44 */       if (scoreboard.entityHasObjective(this.name, scoreobjective)) {
/*    */         
/* 46 */         Score score = scoreboard.getValueFromObjective(this.name, scoreobjective);
/* 47 */         setValue(String.format("%d", new Object[] { Integer.valueOf(score.getScorePoints()) }));
/*    */       }
/*    */       else {
/*    */         
/* 51 */         this.value = "";
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public ChatComponentScore createCopy() {
/* 60 */     ChatComponentScore chatcomponentscore = new ChatComponentScore(this.name, this.objective);
/* 61 */     chatcomponentscore.setValue(this.value);
/* 62 */     chatcomponentscore.setChatStyle(getChatStyle().createShallowCopy());
/*    */     
/* 64 */     for (IChatComponent ichatcomponent : getSiblings())
/*    */     {
/* 66 */       chatcomponentscore.appendSibling(ichatcomponent.createCopy());
/*    */     }
/*    */     
/* 69 */     return chatcomponentscore;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 74 */     if (this == p_equals_1_)
/*    */     {
/* 76 */       return true;
/*    */     }
/* 78 */     if (!(p_equals_1_ instanceof ChatComponentScore))
/*    */     {
/* 80 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 84 */     ChatComponentScore chatcomponentscore = (ChatComponentScore)p_equals_1_;
/* 85 */     return (this.name.equals(chatcomponentscore.name) && this.objective.equals(chatcomponentscore.objective) && super.equals(p_equals_1_));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 91 */     return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\ChatComponentScore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */