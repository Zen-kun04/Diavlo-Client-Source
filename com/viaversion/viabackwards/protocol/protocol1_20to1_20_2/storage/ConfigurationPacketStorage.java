/*    */ package com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.storage;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.Protocol1_20To1_20_2;
/*    */ import com.viaversion.viaversion.api.connection.StorableObject;
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*    */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public final class ConfigurationPacketStorage
/*    */   implements StorableObject
/*    */ {
/* 34 */   private final List<QueuedPacket> rawPackets = new ArrayList<>();
/*    */   private CompoundTag registry;
/*    */   private String[] enabledFeatures;
/*    */   private boolean finished;
/*    */   
/*    */   public CompoundTag registry() {
/* 40 */     Preconditions.checkNotNull(this.registry);
/* 41 */     return this.registry;
/*    */   }
/*    */   
/*    */   public void setRegistry(CompoundTag registry) {
/* 45 */     this.registry = registry;
/*    */   }
/*    */   
/*    */   public String[] enabledFeatures() {
/* 49 */     Preconditions.checkNotNull(this.enabledFeatures);
/* 50 */     return this.enabledFeatures;
/*    */   }
/*    */   
/*    */   public void setEnabledFeatures(String[] enabledFeatures) {
/* 54 */     this.enabledFeatures = enabledFeatures;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addRawPacket(PacketWrapper wrapper, PacketType type) throws Exception {
/* 59 */     ByteBuf buf = Unpooled.buffer();
/*    */     
/* 61 */     wrapper.setId(-1);
/* 62 */     wrapper.writeToBuffer(buf);
/* 63 */     this.rawPackets.add(new QueuedPacket(buf, type));
/*    */   }
/*    */   
/*    */   public void sendQueuedPackets(UserConnection connection) throws Exception {
/* 67 */     for (QueuedPacket queuedPacket : this.rawPackets) {
/*    */       try {
/* 69 */         PacketWrapper packet = PacketWrapper.create(queuedPacket.packetType(), queuedPacket.buf(), connection);
/* 70 */         packet.send(Protocol1_20To1_20_2.class);
/*    */       } finally {
/* 72 */         queuedPacket.buf().release();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isFinished() {
/* 78 */     return this.finished;
/*    */   }
/*    */   
/*    */   public void setFinished(boolean finished) {
/* 82 */     this.finished = finished;
/*    */   }
/*    */   
/*    */   public static final class QueuedPacket {
/*    */     private final ByteBuf buf;
/*    */     private final PacketType packetType;
/*    */     
/*    */     public QueuedPacket(ByteBuf buf, PacketType packetType) {
/* 90 */       this.buf = buf;
/* 91 */       this.packetType = packetType;
/*    */     }
/*    */     
/*    */     public ByteBuf buf() {
/* 95 */       return this.buf;
/*    */     }
/*    */     
/*    */     public PacketType packetType() {
/* 99 */       return this.packetType;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_20to1_20_2\storage\ConfigurationPacketStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */