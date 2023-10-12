/*    */ package com.viaversion.viabackwards.protocol.protocol1_9_1_2to1_9_3_4.chunks;
/*    */ 
/*    */ import com.viaversion.viabackwards.protocol.protocol1_9_1_2to1_9_3_4.Protocol1_9_1_2To1_9_3_4;
/*    */ import com.viaversion.viaversion.api.Via;
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import com.viaversion.viaversion.api.minecraft.Position;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*    */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
/*    */ import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
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
/*    */ public class BlockEntity
/*    */ {
/* 35 */   private static final Map<String, Integer> types = new HashMap<>();
/*    */   
/*    */   static {
/* 38 */     types.put("MobSpawner", Integer.valueOf(1));
/* 39 */     types.put("Control", Integer.valueOf(2));
/* 40 */     types.put("Beacon", Integer.valueOf(3));
/* 41 */     types.put("Skull", Integer.valueOf(4));
/* 42 */     types.put("FlowerPot", Integer.valueOf(5));
/* 43 */     types.put("Banner", Integer.valueOf(6));
/* 44 */     types.put("UNKNOWN", Integer.valueOf(7));
/* 45 */     types.put("EndGateway", Integer.valueOf(8));
/* 46 */     types.put("Sign", Integer.valueOf(9));
/*    */   }
/*    */   
/*    */   public static void handle(List<CompoundTag> tags, UserConnection connection) {
/* 50 */     for (CompoundTag tag : tags) {
/*    */       try {
/* 52 */         if (!tag.contains("id")) {
/* 53 */           throw new Exception("NBT tag not handled because the id key is missing");
/*    */         }
/* 55 */         String id = (String)tag.get("id").getValue();
/* 56 */         if (!types.containsKey(id)) {
/* 57 */           throw new Exception("Not handled id: " + id);
/*    */         }
/* 59 */         int newId = ((Integer)types.get(id)).intValue();
/* 60 */         if (newId == -1) {
/*    */           continue;
/*    */         }
/* 63 */         int x = ((NumberTag)tag.get("x")).asInt();
/* 64 */         int y = ((NumberTag)tag.get("y")).asInt();
/* 65 */         int z = ((NumberTag)tag.get("z")).asInt();
/*    */         
/* 67 */         Position pos = new Position(x, (short)y, z);
/*    */         
/* 69 */         updateBlockEntity(pos, (short)newId, tag, connection);
/* 70 */       } catch (Exception e) {
/* 71 */         if (Via.getManager().isDebug()) {
/* 72 */           Via.getPlatform().getLogger().warning("Block Entity: " + e.getMessage() + ": " + tag);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void updateBlockEntity(Position pos, short id, CompoundTag tag, UserConnection connection) throws Exception {
/* 79 */     PacketWrapper wrapper = PacketWrapper.create((PacketType)ClientboundPackets1_9_3.BLOCK_ENTITY_DATA, null, connection);
/* 80 */     wrapper.write(Type.POSITION, pos);
/* 81 */     wrapper.write((Type)Type.UNSIGNED_BYTE, Short.valueOf(id));
/* 82 */     wrapper.write(Type.NBT, tag);
/* 83 */     wrapper.scheduleSend(Protocol1_9_1_2To1_9_3_4.class, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_9_1_2to1_9_3_4\chunks\BlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */