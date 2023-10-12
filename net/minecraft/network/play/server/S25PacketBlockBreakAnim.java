/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ public class S25PacketBlockBreakAnim
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int breakerId;
/*    */   private BlockPos position;
/*    */   private int progress;
/*    */   
/*    */   public S25PacketBlockBreakAnim() {}
/*    */   
/*    */   public S25PacketBlockBreakAnim(int breakerId, BlockPos pos, int progress) {
/* 21 */     this.breakerId = breakerId;
/* 22 */     this.position = pos;
/* 23 */     this.progress = progress;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 28 */     this.breakerId = buf.readVarIntFromBuffer();
/* 29 */     this.position = buf.readBlockPos();
/* 30 */     this.progress = buf.readUnsignedByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 35 */     buf.writeVarIntToBuffer(this.breakerId);
/* 36 */     buf.writeBlockPos(this.position);
/* 37 */     buf.writeByte(this.progress);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 42 */     handler.handleBlockBreakAnim(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBreakerId() {
/* 47 */     return this.breakerId;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPosition() {
/* 52 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getProgress() {
/* 57 */     return this.progress;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S25PacketBlockBreakAnim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */