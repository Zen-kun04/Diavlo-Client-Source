/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ 
/*    */ 
/*    */ public class S3DPacketDisplayScoreboard
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int position;
/*    */   private String scoreName;
/*    */   
/*    */   public S3DPacketDisplayScoreboard() {}
/*    */   
/*    */   public S3DPacketDisplayScoreboard(int positionIn, ScoreObjective scoreIn) {
/* 20 */     this.position = positionIn;
/*    */     
/* 22 */     if (scoreIn == null) {
/*    */       
/* 24 */       this.scoreName = "";
/*    */     }
/*    */     else {
/*    */       
/* 28 */       this.scoreName = scoreIn.getName();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 34 */     this.position = buf.readByte();
/* 35 */     this.scoreName = buf.readStringFromBuffer(16);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 40 */     buf.writeByte(this.position);
/* 41 */     buf.writeString(this.scoreName);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 46 */     handler.handleDisplayScoreboard(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149371_c() {
/* 51 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_149370_d() {
/* 56 */     return this.scoreName;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S3DPacketDisplayScoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */