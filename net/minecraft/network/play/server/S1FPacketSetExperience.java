/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S1FPacketSetExperience
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private float field_149401_a;
/*    */   private int totalExperience;
/*    */   private int level;
/*    */   
/*    */   public S1FPacketSetExperience() {}
/*    */   
/*    */   public S1FPacketSetExperience(float p_i45222_1_, int totalExperienceIn, int levelIn) {
/* 20 */     this.field_149401_a = p_i45222_1_;
/* 21 */     this.totalExperience = totalExperienceIn;
/* 22 */     this.level = levelIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 27 */     this.field_149401_a = buf.readFloat();
/* 28 */     this.level = buf.readVarIntFromBuffer();
/* 29 */     this.totalExperience = buf.readVarIntFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 34 */     buf.writeFloat(this.field_149401_a);
/* 35 */     buf.writeVarIntToBuffer(this.level);
/* 36 */     buf.writeVarIntToBuffer(this.totalExperience);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 41 */     handler.handleSetExperience(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public float func_149397_c() {
/* 46 */     return this.field_149401_a;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTotalExperience() {
/* 51 */     return this.totalExperience;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLevel() {
/* 56 */     return this.level;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S1FPacketSetExperience.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */