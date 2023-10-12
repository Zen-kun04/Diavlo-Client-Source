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
/*    */ public class S28PacketEffect
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int soundType;
/*    */   private BlockPos soundPos;
/*    */   private int soundData;
/*    */   private boolean serverWide;
/*    */   
/*    */   public S28PacketEffect() {}
/*    */   
/*    */   public S28PacketEffect(int soundTypeIn, BlockPos soundPosIn, int soundDataIn, boolean serverWideIn) {
/* 22 */     this.soundType = soundTypeIn;
/* 23 */     this.soundPos = soundPosIn;
/* 24 */     this.soundData = soundDataIn;
/* 25 */     this.serverWide = serverWideIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 30 */     this.soundType = buf.readInt();
/* 31 */     this.soundPos = buf.readBlockPos();
/* 32 */     this.soundData = buf.readInt();
/* 33 */     this.serverWide = buf.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 38 */     buf.writeInt(this.soundType);
/* 39 */     buf.writeBlockPos(this.soundPos);
/* 40 */     buf.writeInt(this.soundData);
/* 41 */     buf.writeBoolean(this.serverWide);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 46 */     handler.handleEffect(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSoundServerwide() {
/* 51 */     return this.serverWide;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSoundType() {
/* 56 */     return this.soundType;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSoundData() {
/* 61 */     return this.soundData;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getSoundPos() {
/* 66 */     return this.soundPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S28PacketEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */