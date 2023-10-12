/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ public class S2BPacketChangeGameState implements Packet<INetHandlerPlayClient> {
/* 10 */   public static final String[] MESSAGE_NAMES = new String[] { "tile.bed.notValid" };
/*    */   
/*    */   private int state;
/*    */   
/*    */   private float field_149141_c;
/*    */ 
/*    */   
/*    */   public S2BPacketChangeGameState() {}
/*    */   
/*    */   public S2BPacketChangeGameState(int stateIn, float p_i45194_2_) {
/* 20 */     this.state = stateIn;
/* 21 */     this.field_149141_c = p_i45194_2_;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 26 */     this.state = buf.readUnsignedByte();
/* 27 */     this.field_149141_c = buf.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 32 */     buf.writeByte(this.state);
/* 33 */     buf.writeFloat(this.field_149141_c);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 38 */     handler.handleChangeGameState(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getGameState() {
/* 43 */     return this.state;
/*    */   }
/*    */ 
/*    */   
/*    */   public float func_149137_d() {
/* 48 */     return this.field_149141_c;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S2BPacketChangeGameState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */