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
/*    */ public class S36PacketSignEditorOpen
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private BlockPos signPosition;
/*    */   
/*    */   public S36PacketSignEditorOpen() {}
/*    */   
/*    */   public S36PacketSignEditorOpen(BlockPos signPositionIn) {
/* 19 */     this.signPosition = signPositionIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 24 */     handler.handleSignEditorOpen(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 29 */     this.signPosition = buf.readBlockPos();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 34 */     buf.writeBlockPos(this.signPosition);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getSignPosition() {
/* 39 */     return this.signPosition;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S36PacketSignEditorOpen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */