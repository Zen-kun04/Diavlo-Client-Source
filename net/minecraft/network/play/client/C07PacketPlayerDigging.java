/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ 
/*    */ public class C07PacketPlayerDigging
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private BlockPos position;
/*    */   private EnumFacing facing;
/*    */   private Action status;
/*    */   
/*    */   public C07PacketPlayerDigging() {}
/*    */   
/*    */   public C07PacketPlayerDigging(Action statusIn, BlockPos posIn, EnumFacing facingIn) {
/* 22 */     this.status = statusIn;
/* 23 */     this.position = posIn;
/* 24 */     this.facing = facingIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 29 */     this.status = (Action)buf.readEnumValue(Action.class);
/* 30 */     this.position = buf.readBlockPos();
/* 31 */     this.facing = EnumFacing.getFront(buf.readUnsignedByte());
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 36 */     buf.writeEnumValue(this.status);
/* 37 */     buf.writeBlockPos(this.position);
/* 38 */     buf.writeByte(this.facing.getIndex());
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 43 */     handler.processPlayerDigging(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPosition() {
/* 48 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumFacing getFacing() {
/* 53 */     return this.facing;
/*    */   }
/*    */ 
/*    */   
/*    */   public Action getStatus() {
/* 58 */     return this.status;
/*    */   }
/*    */   
/*    */   public enum Action
/*    */   {
/* 63 */     START_DESTROY_BLOCK,
/* 64 */     ABORT_DESTROY_BLOCK,
/* 65 */     STOP_DESTROY_BLOCK,
/* 66 */     DROP_ALL_ITEMS,
/* 67 */     DROP_ITEM,
/* 68 */     RELEASE_USE_ITEM;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C07PacketPlayerDigging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */