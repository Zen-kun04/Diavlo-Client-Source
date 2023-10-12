/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S3APacketTabComplete
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private String[] matches;
/*    */   
/*    */   public S3APacketTabComplete() {}
/*    */   
/*    */   public S3APacketTabComplete(String[] matchesIn) {
/* 18 */     this.matches = matchesIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 23 */     this.matches = new String[buf.readVarIntFromBuffer()];
/*    */     
/* 25 */     for (int i = 0; i < this.matches.length; i++)
/*    */     {
/* 27 */       this.matches[i] = buf.readStringFromBuffer(32767);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 33 */     buf.writeVarIntToBuffer(this.matches.length);
/*    */     
/* 35 */     for (String s : this.matches)
/*    */     {
/* 37 */       buf.writeString(s);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 43 */     handler.handleTabComplete(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] func_149630_c() {
/* 48 */     return this.matches;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S3APacketTabComplete.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */