/*    */ package com.viaversion.viaversion.api.protocol.packet.provider;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*    */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
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
/*    */ final class PacketTypeMapMap<P extends PacketType>
/*    */   implements PacketTypeMap<P>
/*    */ {
/*    */   private final Map<String, P> packetsByName;
/*    */   private final Int2ObjectMap<P> packetsById;
/*    */   
/*    */   PacketTypeMapMap(Map<String, P> packetsByName, Int2ObjectMap<P> packetsById) {
/* 37 */     this.packetsByName = packetsByName;
/* 38 */     this.packetsById = packetsById;
/*    */   }
/*    */ 
/*    */   
/*    */   public P typeByName(String packetTypeName) {
/* 43 */     return this.packetsByName.get(packetTypeName);
/*    */   }
/*    */ 
/*    */   
/*    */   public P typeById(int packetTypeId) {
/* 48 */     return (P)this.packetsById.get(packetTypeId);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<P> types() {
/* 53 */     return (Collection<P>)this.packetsById.values();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\protocol\packet\provider\PacketTypeMapMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */