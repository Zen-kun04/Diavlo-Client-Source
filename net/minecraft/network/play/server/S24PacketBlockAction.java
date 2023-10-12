/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ public class S24PacketBlockAction
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private BlockPos blockPosition;
/*    */   private int instrument;
/*    */   private int pitch;
/*    */   private Block block;
/*    */   
/*    */   public S24PacketBlockAction() {}
/*    */   
/*    */   public S24PacketBlockAction(BlockPos blockPositionIn, Block blockIn, int instrumentIn, int pitchIn) {
/* 23 */     this.blockPosition = blockPositionIn;
/* 24 */     this.instrument = instrumentIn;
/* 25 */     this.pitch = pitchIn;
/* 26 */     this.block = blockIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 31 */     this.blockPosition = buf.readBlockPos();
/* 32 */     this.instrument = buf.readUnsignedByte();
/* 33 */     this.pitch = buf.readUnsignedByte();
/* 34 */     this.block = Block.getBlockById(buf.readVarIntFromBuffer() & 0xFFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 39 */     buf.writeBlockPos(this.blockPosition);
/* 40 */     buf.writeByte(this.instrument);
/* 41 */     buf.writeByte(this.pitch);
/* 42 */     buf.writeVarIntToBuffer(Block.getIdFromBlock(this.block) & 0xFFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 47 */     handler.handleBlockAction(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getBlockPosition() {
/* 52 */     return this.blockPosition;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getData1() {
/* 57 */     return this.instrument;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getData2() {
/* 62 */     return this.pitch;
/*    */   }
/*    */ 
/*    */   
/*    */   public Block getBlockType() {
/* 67 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S24PacketBlockAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */