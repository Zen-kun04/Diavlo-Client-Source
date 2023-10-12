/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ public class S35PacketUpdateTileEntity
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private BlockPos blockPos;
/*    */   private int metadata;
/*    */   private NBTTagCompound nbt;
/*    */   
/*    */   public S35PacketUpdateTileEntity() {}
/*    */   
/*    */   public S35PacketUpdateTileEntity(BlockPos blockPosIn, int metadataIn, NBTTagCompound nbtIn) {
/* 22 */     this.blockPos = blockPosIn;
/* 23 */     this.metadata = metadataIn;
/* 24 */     this.nbt = nbtIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 29 */     this.blockPos = buf.readBlockPos();
/* 30 */     this.metadata = buf.readUnsignedByte();
/* 31 */     this.nbt = buf.readNBTTagCompoundFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 36 */     buf.writeBlockPos(this.blockPos);
/* 37 */     buf.writeByte((byte)this.metadata);
/* 38 */     buf.writeNBTTagCompoundToBuffer(this.nbt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 43 */     handler.handleUpdateTileEntity(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPos() {
/* 48 */     return this.blockPos;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTileEntityType() {
/* 53 */     return this.metadata;
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTTagCompound getNbtCompound() {
/* 58 */     return this.nbt;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S35PacketUpdateTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */