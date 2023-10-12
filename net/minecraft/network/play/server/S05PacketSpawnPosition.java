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
/*    */ public class S05PacketSpawnPosition
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private BlockPos spawnBlockPos;
/*    */   
/*    */   public S05PacketSpawnPosition() {}
/*    */   
/*    */   public S05PacketSpawnPosition(BlockPos spawnBlockPosIn) {
/* 19 */     this.spawnBlockPos = spawnBlockPosIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 24 */     this.spawnBlockPos = buf.readBlockPos();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 29 */     buf.writeBlockPos(this.spawnBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 34 */     handler.handleSpawnPosition(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getSpawnPos() {
/* 39 */     return this.spawnBlockPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S05PacketSpawnPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */