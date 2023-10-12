/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.world.EnumDifficulty;
/*    */ import net.minecraft.world.WorldSettings;
/*    */ import net.minecraft.world.WorldType;
/*    */ 
/*    */ 
/*    */ public class S07PacketRespawn
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int dimensionID;
/*    */   private EnumDifficulty difficulty;
/*    */   private WorldSettings.GameType gameType;
/*    */   private WorldType worldType;
/*    */   
/*    */   public S07PacketRespawn() {}
/*    */   
/*    */   public S07PacketRespawn(int dimensionIDIn, EnumDifficulty difficultyIn, WorldType worldTypeIn, WorldSettings.GameType gameTypeIn) {
/* 24 */     this.dimensionID = dimensionIDIn;
/* 25 */     this.difficulty = difficultyIn;
/* 26 */     this.gameType = gameTypeIn;
/* 27 */     this.worldType = worldTypeIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 32 */     handler.handleRespawn(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 37 */     this.dimensionID = buf.readInt();
/* 38 */     this.difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
/* 39 */     this.gameType = WorldSettings.GameType.getByID(buf.readUnsignedByte());
/* 40 */     this.worldType = WorldType.parseWorldType(buf.readStringFromBuffer(16));
/*    */     
/* 42 */     if (this.worldType == null)
/*    */     {
/* 44 */       this.worldType = WorldType.DEFAULT;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 50 */     buf.writeInt(this.dimensionID);
/* 51 */     buf.writeByte(this.difficulty.getDifficultyId());
/* 52 */     buf.writeByte(this.gameType.getID());
/* 53 */     buf.writeString(this.worldType.getWorldTypeName());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDimensionID() {
/* 58 */     return this.dimensionID;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumDifficulty getDifficulty() {
/* 63 */     return this.difficulty;
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldSettings.GameType getGameType() {
/* 68 */     return this.gameType;
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldType getWorldType() {
/* 73 */     return this.worldType;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S07PacketRespawn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */