/*    */ package com.viaversion.viaversion.api.protocol.packet.provider;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class PacketTypeArrayMap<P extends PacketType>
/*    */   implements PacketTypeMap<P>
/*    */ {
/*    */   private final Map<String, P> packetsByName;
/*    */   private final P[] packets;
/*    */   
/*    */   PacketTypeArrayMap(Map<String, P> packetsByName, P[] packets) {
/* 37 */     this.packetsByName = packetsByName;
/* 38 */     this.packets = packets;
/*    */   }
/*    */ 
/*    */   
/*    */   public P typeByName(String packetTypeName) {
/* 43 */     return this.packetsByName.get(packetTypeName);
/*    */   }
/*    */ 
/*    */   
/*    */   public P typeById(int packetTypeId) {
/* 48 */     return (packetTypeId >= 0 && packetTypeId < this.packets.length) ? this.packets[packetTypeId] : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<P> types() {
/* 53 */     return Arrays.asList(this.packets);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\protocol\packet\provider\PacketTypeArrayMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */