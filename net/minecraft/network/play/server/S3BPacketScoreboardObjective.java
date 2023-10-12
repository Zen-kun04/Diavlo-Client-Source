/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ 
/*    */ 
/*    */ public class S3BPacketScoreboardObjective
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private String objectiveName;
/*    */   private String objectiveValue;
/*    */   private IScoreObjectiveCriteria.EnumRenderType type;
/*    */   private int field_149342_c;
/*    */   
/*    */   public S3BPacketScoreboardObjective() {}
/*    */   
/*    */   public S3BPacketScoreboardObjective(ScoreObjective p_i45224_1_, int p_i45224_2_) {
/* 23 */     this.objectiveName = p_i45224_1_.getName();
/* 24 */     this.objectiveValue = p_i45224_1_.getDisplayName();
/* 25 */     this.type = p_i45224_1_.getCriteria().getRenderType();
/* 26 */     this.field_149342_c = p_i45224_2_;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 31 */     this.objectiveName = buf.readStringFromBuffer(16);
/* 32 */     this.field_149342_c = buf.readByte();
/*    */     
/* 34 */     if (this.field_149342_c == 0 || this.field_149342_c == 2) {
/*    */       
/* 36 */       this.objectiveValue = buf.readStringFromBuffer(32);
/* 37 */       this.type = IScoreObjectiveCriteria.EnumRenderType.func_178795_a(buf.readStringFromBuffer(16));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 43 */     buf.writeString(this.objectiveName);
/* 44 */     buf.writeByte(this.field_149342_c);
/*    */     
/* 46 */     if (this.field_149342_c == 0 || this.field_149342_c == 2) {
/*    */       
/* 48 */       buf.writeString(this.objectiveValue);
/* 49 */       buf.writeString(this.type.func_178796_a());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 55 */     handler.handleScoreboardObjective(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_149339_c() {
/* 60 */     return this.objectiveName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_149337_d() {
/* 65 */     return this.objectiveValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149338_e() {
/* 70 */     return this.field_149342_c;
/*    */   }
/*    */ 
/*    */   
/*    */   public IScoreObjectiveCriteria.EnumRenderType func_179817_d() {
/* 75 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S3BPacketScoreboardObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */