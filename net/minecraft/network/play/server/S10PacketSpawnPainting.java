/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.item.EntityPainting;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ 
/*    */ public class S10PacketSpawnPainting
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityID;
/*    */   private BlockPos position;
/*    */   private EnumFacing facing;
/*    */   private String title;
/*    */   
/*    */   public S10PacketSpawnPainting() {}
/*    */   
/*    */   public S10PacketSpawnPainting(EntityPainting painting) {
/* 24 */     this.entityID = painting.getEntityId();
/* 25 */     this.position = painting.getHangingPosition();
/* 26 */     this.facing = painting.facingDirection;
/* 27 */     this.title = painting.art.title;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 32 */     this.entityID = buf.readVarIntFromBuffer();
/* 33 */     this.title = buf.readStringFromBuffer(EntityPainting.EnumArt.field_180001_A);
/* 34 */     this.position = buf.readBlockPos();
/* 35 */     this.facing = EnumFacing.getHorizontal(buf.readUnsignedByte());
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 40 */     buf.writeVarIntToBuffer(this.entityID);
/* 41 */     buf.writeString(this.title);
/* 42 */     buf.writeBlockPos(this.position);
/* 43 */     buf.writeByte(this.facing.getHorizontalIndex());
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 48 */     handler.handleSpawnPainting(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEntityID() {
/* 53 */     return this.entityID;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPosition() {
/* 58 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumFacing getFacing() {
/* 63 */     return this.facing;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 68 */     return this.title;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S10PacketSpawnPainting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */