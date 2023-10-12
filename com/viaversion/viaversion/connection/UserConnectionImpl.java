/*     */ package com.viaversion.viaversion.connection;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.ProtocolInfo;
/*     */ import com.viaversion.viaversion.api.connection.StorableObject;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.Direction;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketTracker;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.exception.CancelException;
/*     */ import com.viaversion.viaversion.protocol.packet.PacketWrapperImpl;
/*     */ import com.viaversion.viaversion.util.ChatColorUtil;
/*     */ import com.viaversion.viaversion.util.PipelineUtil;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Function;
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
/*     */ public class UserConnectionImpl
/*     */   implements UserConnection
/*     */ {
/*  53 */   private static final AtomicLong IDS = new AtomicLong();
/*  54 */   private final long id = IDS.incrementAndGet();
/*  55 */   private final Map<Class<?>, StorableObject> storedObjects = new ConcurrentHashMap<>();
/*  56 */   private final Map<Class<? extends Protocol>, EntityTracker> entityTrackers = new HashMap<>();
/*  57 */   private final PacketTracker packetTracker = new PacketTracker(this);
/*  58 */   private final Set<UUID> passthroughTokens = Collections.newSetFromMap(CacheBuilder.newBuilder()
/*  59 */       .expireAfterWrite(10L, TimeUnit.SECONDS)
/*  60 */       .build().asMap());
/*  61 */   private final ProtocolInfo protocolInfo = new ProtocolInfoImpl(this);
/*     */   
/*     */   private final Channel channel;
/*     */   
/*     */   private final boolean clientSide;
/*     */   
/*     */   private boolean active = true;
/*     */   
/*     */   private boolean pendingDisconnect;
/*     */   
/*     */   private boolean packetLimiterEnabled = true;
/*     */ 
/*     */   
/*     */   public UserConnectionImpl(Channel channel, boolean clientSide) {
/*  75 */     this.channel = channel;
/*  76 */     this.clientSide = clientSide;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserConnectionImpl(Channel channel) {
/*  83 */     this(channel, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends StorableObject> T get(Class<T> objectClass) {
/*  88 */     return (T)this.storedObjects.get(objectClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(Class<? extends StorableObject> objectClass) {
/*  93 */     return this.storedObjects.containsKey(objectClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends StorableObject> T remove(Class<T> objectClass) {
/*  98 */     StorableObject object = this.storedObjects.remove(objectClass);
/*  99 */     if (object != null) {
/* 100 */       object.onRemove();
/*     */     }
/* 102 */     return (T)object;
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(StorableObject object) {
/* 107 */     StorableObject previousObject = this.storedObjects.put(object.getClass(), object);
/* 108 */     if (previousObject != null) {
/* 109 */       previousObject.onRemove();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<EntityTracker> getEntityTrackers() {
/* 115 */     return this.entityTrackers.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends EntityTracker> T getEntityTracker(Class<? extends Protocol> protocolClass) {
/* 120 */     return (T)this.entityTrackers.get(protocolClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntityTracker(Class<? extends Protocol> protocolClass, EntityTracker tracker) {
/* 125 */     if (!this.entityTrackers.containsKey(protocolClass)) {
/* 126 */       this.entityTrackers.put(protocolClass, tracker);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearStoredObjects(boolean isServerSwitch) {
/* 132 */     if (isServerSwitch) {
/* 133 */       this.storedObjects.values().removeIf(storableObject -> {
/*     */             if (storableObject.clearOnServerSwitch()) {
/*     */               storableObject.onRemove();
/*     */               return true;
/*     */             } 
/*     */             return false;
/*     */           });
/* 140 */       for (EntityTracker tracker : this.entityTrackers.values()) {
/* 141 */         tracker.clearEntities();
/* 142 */         tracker.trackClientEntity();
/*     */       } 
/*     */     } else {
/* 145 */       for (StorableObject object : this.storedObjects.values()) {
/* 146 */         object.onRemove();
/*     */       }
/* 148 */       this.storedObjects.clear();
/* 149 */       this.entityTrackers.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRawPacket(ByteBuf packet) {
/* 155 */     sendRawPacket(packet, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scheduleSendRawPacket(ByteBuf packet) {
/* 160 */     sendRawPacket(packet, false);
/*     */   }
/*     */   
/*     */   private void sendRawPacket(ByteBuf packet, boolean currentThread) {
/*     */     Runnable act;
/* 165 */     if (this.clientSide) {
/*     */       
/* 167 */       act = (() -> getChannel().pipeline().context(Via.getManager().getInjector().getDecoderName()).fireChannelRead(packet));
/*     */     } else {
/*     */       
/* 170 */       act = (() -> this.channel.pipeline().context(Via.getManager().getInjector().getEncoderName()).writeAndFlush(packet));
/*     */     } 
/* 172 */     if (currentThread) {
/* 173 */       act.run();
/*     */     } else {
/*     */       try {
/* 176 */         this.channel.eventLoop().submit(act);
/* 177 */       } catch (Throwable e) {
/* 178 */         packet.release();
/* 179 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture sendRawPacketFuture(ByteBuf packet) {
/* 186 */     if (this.clientSide) {
/*     */       
/* 188 */       getChannel().pipeline().context(Via.getManager().getInjector().getDecoderName()).fireChannelRead(packet);
/* 189 */       return getChannel().newSucceededFuture();
/*     */     } 
/* 191 */     return this.channel.pipeline().context(Via.getManager().getInjector().getEncoderName()).writeAndFlush(packet);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketTracker getPacketTracker() {
/* 197 */     return this.packetTracker;
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect(String reason) {
/* 202 */     if (!this.channel.isOpen() || this.pendingDisconnect)
/*     */       return; 
/* 204 */     this.pendingDisconnect = true;
/* 205 */     Via.getPlatform().runSync(() -> {
/*     */           if (!Via.getPlatform().disconnect(this, ChatColorUtil.translateAlternateColorCodes(reason))) {
/*     */             this.channel.close();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRawPacketToServer(ByteBuf packet) {
/* 214 */     if (this.clientSide) {
/* 215 */       sendRawPacketToServerClientSide(packet, true);
/*     */     } else {
/* 217 */       sendRawPacketToServerServerSide(packet, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void scheduleSendRawPacketToServer(ByteBuf packet) {
/* 223 */     if (this.clientSide) {
/* 224 */       sendRawPacketToServerClientSide(packet, false);
/*     */     } else {
/* 226 */       sendRawPacketToServerServerSide(packet, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendRawPacketToServerServerSide(ByteBuf packet, boolean currentThread) {
/* 231 */     ByteBuf buf = packet.alloc().buffer();
/*     */ 
/*     */     
/*     */     try {
/* 235 */       ChannelHandlerContext context = PipelineUtil.getPreviousContext(Via.getManager().getInjector().getDecoderName(), this.channel.pipeline());
/*     */       
/* 237 */       if (shouldTransformPacket()) {
/*     */         
/*     */         try {
/* 240 */           Type.VAR_INT.writePrimitive(buf, 1000);
/* 241 */           Type.UUID.write(buf, generatePassthroughToken());
/* 242 */         } catch (Exception shouldNotHappen) {
/* 243 */           throw new RuntimeException(shouldNotHappen);
/*     */         } 
/*     */       }
/*     */       
/* 247 */       buf.writeBytes(packet);
/* 248 */       Runnable act = () -> {
/*     */           if (context != null) {
/*     */             context.fireChannelRead(buf);
/*     */           } else {
/*     */             this.channel.pipeline().fireChannelRead(buf);
/*     */           } 
/*     */         };
/* 255 */       if (currentThread) {
/* 256 */         act.run();
/*     */       } else {
/*     */         try {
/* 259 */           this.channel.eventLoop().submit(act);
/* 260 */         } catch (Throwable t) {
/*     */           
/* 262 */           buf.release();
/* 263 */           throw t;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 267 */       packet.release();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendRawPacketToServerClientSide(ByteBuf packet, boolean currentThread) {
/* 272 */     Runnable act = () -> getChannel().pipeline().context(Via.getManager().getInjector().getEncoderName()).writeAndFlush(packet);
/*     */     
/* 274 */     if (currentThread) {
/* 275 */       act.run();
/*     */     } else {
/*     */       try {
/* 278 */         getChannel().eventLoop().submit(act);
/* 279 */       } catch (Throwable e) {
/* 280 */         e.printStackTrace();
/* 281 */         packet.release();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkServerboundPacket() {
/* 288 */     if (this.pendingDisconnect) {
/* 289 */       return false;
/*     */     }
/*     */     
/* 292 */     return (!this.packetLimiterEnabled || !this.packetTracker.incrementReceived() || !this.packetTracker.exceedsMaxPPS());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkClientboundPacket() {
/* 297 */     this.packetTracker.incrementSent();
/* 298 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldTransformPacket() {
/* 303 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   public void transformClientbound(ByteBuf buf, Function<Throwable, Exception> cancelSupplier) throws Exception {
/* 308 */     transform(buf, Direction.CLIENTBOUND, cancelSupplier);
/*     */   }
/*     */ 
/*     */   
/*     */   public void transformServerbound(ByteBuf buf, Function<Throwable, Exception> cancelSupplier) throws Exception {
/* 313 */     transform(buf, Direction.SERVERBOUND, cancelSupplier);
/*     */   }
/*     */   
/*     */   private void transform(ByteBuf buf, Direction direction, Function<Throwable, Exception> cancelSupplier) throws Exception {
/* 317 */     if (!buf.isReadable())
/*     */       return; 
/* 319 */     int id = Type.VAR_INT.readPrimitive(buf);
/* 320 */     if (id == 1000) {
/* 321 */       if (!this.passthroughTokens.remove(Type.UUID.read(buf))) {
/* 322 */         throw new IllegalArgumentException("Invalid token");
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 327 */     PacketWrapperImpl packetWrapperImpl = new PacketWrapperImpl(id, buf, this);
/* 328 */     State state = this.protocolInfo.getState(direction);
/*     */     try {
/* 330 */       this.protocolInfo.getPipeline().transform(direction, state, (PacketWrapper)packetWrapperImpl);
/* 331 */     } catch (CancelException ex) {
/* 332 */       throw (Exception)cancelSupplier.apply(ex);
/*     */     } 
/*     */     
/* 335 */     ByteBuf transformed = buf.alloc().buffer();
/*     */     try {
/* 337 */       packetWrapperImpl.writeToBuffer(transformed);
/* 338 */       buf.clear().writeBytes(transformed);
/*     */     } finally {
/* 340 */       transformed.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 346 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public Channel getChannel() {
/* 351 */     return this.channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolInfo getProtocolInfo() {
/* 356 */     return this.protocolInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Class<?>, StorableObject> getStoredObjects() {
/* 361 */     return this.storedObjects;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 366 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActive(boolean active) {
/* 371 */     this.active = active;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPendingDisconnect() {
/* 376 */     return this.pendingDisconnect;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPendingDisconnect(boolean pendingDisconnect) {
/* 381 */     this.pendingDisconnect = pendingDisconnect;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClientSide() {
/* 386 */     return this.clientSide;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldApplyBlockProtocol() {
/* 391 */     return !this.clientSide;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPacketLimiterEnabled() {
/* 396 */     return this.packetLimiterEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPacketLimiterEnabled(boolean packetLimiterEnabled) {
/* 401 */     this.packetLimiterEnabled = packetLimiterEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID generatePassthroughToken() {
/* 406 */     UUID token = UUID.randomUUID();
/* 407 */     this.passthroughTokens.add(token);
/* 408 */     return token;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 413 */     if (this == o) return true; 
/* 414 */     if (o == null || getClass() != o.getClass()) return false; 
/* 415 */     UserConnectionImpl that = (UserConnectionImpl)o;
/* 416 */     return (this.id == that.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 421 */     return Long.hashCode(this.id);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\connection\UserConnectionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */