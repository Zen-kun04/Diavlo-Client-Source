/*    */ package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets;
/*    */ 
/*    */ import com.viaversion.viabackwards.ViaBackwards;
/*    */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*    */ import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
/*    */ import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
/*    */ import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.EntityPositionStorage1_14;
/*    */ import com.viaversion.viaversion.api.data.entity.StoredEntityData;
/*    */ import com.viaversion.viaversion.api.protocol.Protocol;
/*    */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*    */ import com.viaversion.viaversion.api.rewriter.RewriterBase;
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
/*    */ import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
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
/*    */ public class SoundPackets1_14
/*    */   extends RewriterBase<Protocol1_13_2To1_14>
/*    */ {
/*    */   public SoundPackets1_14(Protocol1_13_2To1_14 protocol) {
/* 34 */     super((Protocol)protocol);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void registerPackets() {
/* 39 */     SoundRewriter<ClientboundPackets1_14> soundRewriter = new SoundRewriter((BackwardsProtocol)this.protocol);
/* 40 */     soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_14.SOUND);
/* 41 */     soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_14.NAMED_SOUND);
/* 42 */     soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_14.STOP_SOUND);
/*    */ 
/*    */     
/* 45 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.ENTITY_SOUND, null, wrapper -> {
/*    */           wrapper.cancel();
/*    */           int soundId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*    */           int newId = ((Protocol1_13_2To1_14)this.protocol).getMappingData().getSoundMappings().getNewId(soundId);
/*    */           if (newId == -1)
/*    */             return; 
/*    */           int category = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*    */           int entityId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*    */           StoredEntityData storedEntity = wrapper.user().getEntityTracker(((Protocol1_13_2To1_14)this.protocol).getClass()).entityData(entityId);
/*    */           EntityPositionStorage1_14 entityStorage;
/*    */           if (storedEntity == null || (entityStorage = (EntityPositionStorage1_14)storedEntity.get(EntityPositionStorage1_14.class)) == null) {
/*    */             ViaBackwards.getPlatform().getLogger().warning("Untracked entity with id " + entityId);
/*    */             return;
/*    */           } 
/*    */           float volume = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
/*    */           float pitch = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
/*    */           int x = (int)(entityStorage.getX() * 8.0D);
/*    */           int y = (int)(entityStorage.getY() * 8.0D);
/*    */           int z = (int)(entityStorage.getZ() * 8.0D);
/*    */           PacketWrapper soundPacket = wrapper.create((PacketType)ClientboundPackets1_13.SOUND);
/*    */           soundPacket.write((Type)Type.VAR_INT, Integer.valueOf(newId));
/*    */           soundPacket.write((Type)Type.VAR_INT, Integer.valueOf(category));
/*    */           soundPacket.write((Type)Type.INT, Integer.valueOf(x));
/*    */           soundPacket.write((Type)Type.INT, Integer.valueOf(y));
/*    */           soundPacket.write((Type)Type.INT, Integer.valueOf(z));
/*    */           soundPacket.write((Type)Type.FLOAT, Float.valueOf(volume));
/*    */           soundPacket.write((Type)Type.FLOAT, Float.valueOf(pitch));
/*    */           soundPacket.send(Protocol1_13_2To1_14.class);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_13_2to1_14\packets\SoundPackets1_14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */