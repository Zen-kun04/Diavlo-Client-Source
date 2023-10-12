/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.scoreboard.Score;
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ 
/*    */ public class S3CPacketUpdateScore implements Packet<INetHandlerPlayClient> {
/* 12 */   private String name = "";
/* 13 */   private String objective = "";
/*    */ 
/*    */   
/*    */   private int value;
/*    */ 
/*    */   
/*    */   private Action action;
/*    */ 
/*    */   
/*    */   public S3CPacketUpdateScore(Score scoreIn) {
/* 23 */     this.name = scoreIn.getPlayerName();
/* 24 */     this.objective = scoreIn.getObjective().getName();
/* 25 */     this.value = scoreIn.getScorePoints();
/* 26 */     this.action = Action.CHANGE;
/*    */   }
/*    */ 
/*    */   
/*    */   public S3CPacketUpdateScore(String nameIn) {
/* 31 */     this.name = nameIn;
/* 32 */     this.objective = "";
/* 33 */     this.value = 0;
/* 34 */     this.action = Action.REMOVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public S3CPacketUpdateScore(String nameIn, ScoreObjective objectiveIn) {
/* 39 */     this.name = nameIn;
/* 40 */     this.objective = objectiveIn.getName();
/* 41 */     this.value = 0;
/* 42 */     this.action = Action.REMOVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 47 */     this.name = buf.readStringFromBuffer(40);
/* 48 */     this.action = (Action)buf.readEnumValue(Action.class);
/* 49 */     this.objective = buf.readStringFromBuffer(16);
/*    */     
/* 51 */     if (this.action != Action.REMOVE)
/*    */     {
/* 53 */       this.value = buf.readVarIntFromBuffer();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 59 */     buf.writeString(this.name);
/* 60 */     buf.writeEnumValue(this.action);
/* 61 */     buf.writeString(this.objective);
/*    */     
/* 63 */     if (this.action != Action.REMOVE)
/*    */     {
/* 65 */       buf.writeVarIntToBuffer(this.value);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 71 */     handler.handleUpdateScore(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPlayerName() {
/* 76 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getObjectiveName() {
/* 81 */     return this.objective;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getScoreValue() {
/* 86 */     return this.value;
/*    */   }
/*    */   public S3CPacketUpdateScore() {}
/*    */   
/*    */   public Action getScoreAction() {
/* 91 */     return this.action;
/*    */   }
/*    */   
/*    */   public enum Action
/*    */   {
/* 96 */     CHANGE,
/* 97 */     REMOVE;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S3CPacketUpdateScore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */