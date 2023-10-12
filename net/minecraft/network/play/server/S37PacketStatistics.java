/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.stats.StatBase;
/*    */ import net.minecraft.stats.StatList;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class S37PacketStatistics
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private Map<StatBase, Integer> field_148976_a;
/*    */   
/*    */   public S37PacketStatistics() {}
/*    */   
/*    */   public S37PacketStatistics(Map<StatBase, Integer> p_i45173_1_) {
/* 23 */     this.field_148976_a = p_i45173_1_;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 28 */     handler.handleStatistics(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 33 */     int i = buf.readVarIntFromBuffer();
/* 34 */     this.field_148976_a = Maps.newHashMap();
/*    */     
/* 36 */     for (int j = 0; j < i; j++) {
/*    */       
/* 38 */       StatBase statbase = StatList.getOneShotStat(buf.readStringFromBuffer(32767));
/* 39 */       int k = buf.readVarIntFromBuffer();
/*    */       
/* 41 */       if (statbase != null)
/*    */       {
/* 43 */         this.field_148976_a.put(statbase, Integer.valueOf(k));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 50 */     buf.writeVarIntToBuffer(this.field_148976_a.size());
/*    */     
/* 52 */     for (Map.Entry<StatBase, Integer> entry : this.field_148976_a.entrySet()) {
/*    */       
/* 54 */       buf.writeString(((StatBase)entry.getKey()).statId);
/* 55 */       buf.writeVarIntToBuffer(((Integer)entry.getValue()).intValue());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<StatBase, Integer> func_148974_c() {
/* 61 */     return this.field_148976_a;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S37PacketStatistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */