/*     */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.storage;
/*     */ 
/*     */ import com.viaversion.viaversion.api.connection.StorableObject;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.Protocol1_20_2To1_20;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurationState
/*     */   implements StorableObject
/*     */ {
/*  37 */   private final List<QueuedPacket> packetQueue = new ArrayList<>();
/*  38 */   private BridgePhase bridgePhase = BridgePhase.NONE;
/*     */   private QueuedPacket joinGamePacket;
/*     */   private boolean queuedJoinGame;
/*     */   private CompoundTag lastDimensionRegistry;
/*     */   private ClientInformation clientInformation;
/*     */   
/*     */   public BridgePhase bridgePhase() {
/*  45 */     return this.bridgePhase;
/*     */   }
/*     */   
/*     */   public void setBridgePhase(BridgePhase bridgePhase) {
/*  49 */     this.bridgePhase = bridgePhase;
/*     */   }
/*     */   
/*     */   public CompoundTag lastDimensionRegistry() {
/*  53 */     return this.lastDimensionRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setLastDimensionRegistry(CompoundTag dimensionRegistry) {
/*  63 */     boolean equals = Objects.equals(this.lastDimensionRegistry, dimensionRegistry);
/*  64 */     this.lastDimensionRegistry = dimensionRegistry;
/*  65 */     return !equals;
/*     */   }
/*     */   
/*     */   public void setClientInformation(ClientInformation clientInformation) {
/*  69 */     this.clientInformation = clientInformation;
/*     */   }
/*     */   
/*     */   public void addPacketToQueue(PacketWrapper wrapper, boolean clientbound) throws Exception {
/*  73 */     this.packetQueue.add(toQueuedPacket(wrapper, clientbound, false));
/*     */   }
/*     */ 
/*     */   
/*     */   private QueuedPacket toQueuedPacket(PacketWrapper wrapper, boolean clientbound, boolean skipCurrentPipeline) throws Exception {
/*  78 */     ByteBuf copy = Unpooled.buffer();
/*  79 */     PacketType packetType = wrapper.getPacketType();
/*  80 */     int packetId = wrapper.getId();
/*     */ 
/*     */     
/*  83 */     wrapper.setId(-1);
/*  84 */     wrapper.writeToBuffer(copy);
/*  85 */     return new QueuedPacket(copy, clientbound, packetType, packetId, skipCurrentPipeline);
/*     */   }
/*     */   
/*     */   public void setJoinGamePacket(PacketWrapper wrapper) throws Exception {
/*  89 */     this.joinGamePacket = toQueuedPacket(wrapper, true, true);
/*  90 */     this.queuedJoinGame = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean clearOnServerSwitch() {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove() {
/* 100 */     for (QueuedPacket packet : this.packetQueue) {
/* 101 */       packet.buf().release();
/*     */     }
/* 103 */     if (this.joinGamePacket != null) {
/* 104 */       this.joinGamePacket.buf().release();
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendQueuedPackets(UserConnection connection) throws Exception {
/* 109 */     boolean hasJoinGamePacket = (this.joinGamePacket != null);
/* 110 */     if (hasJoinGamePacket) {
/* 111 */       this.packetQueue.add(0, this.joinGamePacket);
/* 112 */       this.joinGamePacket = null;
/*     */     } 
/*     */     
/* 115 */     PacketWrapper clientInformationPacket = clientInformationPacket(connection);
/* 116 */     if (clientInformationPacket != null) {
/* 117 */       this.packetQueue.add(hasJoinGamePacket ? 1 : 0, toQueuedPacket(clientInformationPacket, false, true));
/*     */     }
/*     */     
/* 120 */     QueuedPacket[] queuedPackets = this.packetQueue.<QueuedPacket>toArray(new QueuedPacket[0]);
/* 121 */     this.packetQueue.clear();
/*     */     
/* 123 */     for (QueuedPacket packet : queuedPackets) {
/*     */       
/*     */       try { PacketWrapper queuedWrapper;
/* 126 */         if (packet.packetType() != null) {
/* 127 */           queuedWrapper = PacketWrapper.create(packet.packetType(), packet.buf(), connection);
/*     */         } else {
/*     */           
/* 130 */           queuedWrapper = PacketWrapper.create(packet.packetId(), packet.buf(), connection);
/*     */         } 
/*     */         
/* 133 */         if (packet.clientbound()) {
/* 134 */           queuedWrapper.send(Protocol1_20_2To1_20.class, packet.skipCurrentPipeline());
/*     */         } else {
/* 136 */           queuedWrapper.sendToServer(Protocol1_20_2To1_20.class, packet.skipCurrentPipeline());
/*     */         } 
/*     */         
/* 139 */         packet.buf().release(); } finally { packet.buf().release(); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clear() {
/* 145 */     this.packetQueue.clear();
/* 146 */     this.bridgePhase = BridgePhase.NONE;
/* 147 */     this.queuedJoinGame = false;
/*     */   }
/*     */   
/*     */   public boolean queuedOrSentJoinGame() {
/* 151 */     return this.queuedJoinGame;
/*     */   }
/*     */   
/*     */   public enum BridgePhase {
/* 155 */     NONE, PROFILE_SENT, CONFIGURATION, REENTERING_CONFIGURATION;
/*     */   }
/*     */   
/*     */   public PacketWrapper clientInformationPacket(UserConnection connection) {
/* 159 */     if (this.clientInformation == null)
/*     */     {
/* 161 */       return null;
/*     */     }
/*     */     
/* 164 */     PacketWrapper settingsPacket = PacketWrapper.create((PacketType)ServerboundPackets1_19_4.CLIENT_SETTINGS, connection);
/* 165 */     settingsPacket.write(Type.STRING, this.clientInformation.language);
/* 166 */     settingsPacket.write((Type)Type.BYTE, Byte.valueOf(this.clientInformation.viewDistance));
/* 167 */     settingsPacket.write((Type)Type.VAR_INT, Integer.valueOf(this.clientInformation.chatVisibility));
/* 168 */     settingsPacket.write((Type)Type.BOOLEAN, Boolean.valueOf(this.clientInformation.showChatColors));
/* 169 */     settingsPacket.write((Type)Type.UNSIGNED_BYTE, Short.valueOf(this.clientInformation.modelCustomization));
/* 170 */     settingsPacket.write((Type)Type.VAR_INT, Integer.valueOf(this.clientInformation.mainHand));
/* 171 */     settingsPacket.write((Type)Type.BOOLEAN, Boolean.valueOf(this.clientInformation.textFiltering));
/* 172 */     settingsPacket.write((Type)Type.BOOLEAN, Boolean.valueOf(this.clientInformation.allowListing));
/* 173 */     return settingsPacket;
/*     */   }
/*     */   
/*     */   public static final class QueuedPacket
/*     */   {
/*     */     private final ByteBuf buf;
/*     */     private final boolean clientbound;
/*     */     private final PacketType packetType;
/*     */     private final int packetId;
/*     */     private final boolean skipCurrentPipeline;
/*     */     
/*     */     private QueuedPacket(ByteBuf buf, boolean clientbound, PacketType packetType, int packetId, boolean skipCurrentPipeline) {
/* 185 */       this.buf = buf;
/* 186 */       this.clientbound = clientbound;
/* 187 */       this.packetType = packetType;
/* 188 */       this.packetId = packetId;
/* 189 */       this.skipCurrentPipeline = skipCurrentPipeline;
/*     */     }
/*     */     
/*     */     public ByteBuf buf() {
/* 193 */       return this.buf;
/*     */     }
/*     */     
/*     */     public boolean clientbound() {
/* 197 */       return this.clientbound;
/*     */     }
/*     */     
/*     */     public int packetId() {
/* 201 */       return this.packetId;
/*     */     }
/*     */     
/*     */     public PacketType packetType() {
/* 205 */       return this.packetType;
/*     */     }
/*     */     
/*     */     public boolean skipCurrentPipeline() {
/* 209 */       return this.skipCurrentPipeline;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 214 */       return "QueuedPacket{buf=" + this.buf + ", clientbound=" + this.clientbound + ", packetType=" + this.packetType + ", packetId=" + this.packetId + ", skipCurrentPipeline=" + this.skipCurrentPipeline + '}';
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ClientInformation
/*     */   {
/*     */     private final String language;
/*     */     
/*     */     private final byte viewDistance;
/*     */     
/*     */     private final int chatVisibility;
/*     */     
/*     */     private final boolean showChatColors;
/*     */     
/*     */     private final short modelCustomization;
/*     */     
/*     */     private final int mainHand;
/*     */     
/*     */     private final boolean textFiltering;
/*     */     private final boolean allowListing;
/*     */     
/*     */     public ClientInformation(String language, byte viewDistance, int chatVisibility, boolean showChatColors, short modelCustomization, int mainHand, boolean textFiltering, boolean allowListing) {
/* 237 */       this.language = language;
/* 238 */       this.viewDistance = viewDistance;
/* 239 */       this.chatVisibility = chatVisibility;
/* 240 */       this.showChatColors = showChatColors;
/* 241 */       this.modelCustomization = modelCustomization;
/* 242 */       this.mainHand = mainHand;
/* 243 */       this.textFiltering = textFiltering;
/* 244 */       this.allowListing = allowListing;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\storage\ConfigurationState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */