/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class S0APacketUseBed
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int playerID;
/*    */   private BlockPos bedPos;
/*    */   
/*    */   public S0APacketUseBed() {}
/*    */   
/*    */   public S0APacketUseBed(EntityPlayer player, BlockPos bedPosIn) {
/* 22 */     this.playerID = player.getEntityId();
/* 23 */     this.bedPos = bedPosIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 28 */     this.playerID = buf.readVarIntFromBuffer();
/* 29 */     this.bedPos = buf.readBlockPos();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 34 */     buf.writeVarIntToBuffer(this.playerID);
/* 35 */     buf.writeBlockPos(this.bedPos);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 40 */     handler.handleUseBed(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityPlayer getPlayer(World worldIn) {
/* 45 */     return (EntityPlayer)worldIn.getEntityByID(this.playerID);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getBedPosition() {
/* 50 */     return this.bedPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S0APacketUseBed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */