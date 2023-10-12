/*     */ package com.viaversion.viaversion.protocol.packet;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.ProtocolInfo;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.Direction;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.TypeConverter;
/*     */ import com.viaversion.viaversion.exception.CancelException;
/*     */ import com.viaversion.viaversion.exception.InformativeException;
/*     */ import com.viaversion.viaversion.util.PipelineUtil;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class PacketWrapperImpl
/*     */   implements PacketWrapper
/*     */ {
/*  47 */   private static final Protocol[] PROTOCOL_ARRAY = new Protocol[0];
/*     */   
/*  49 */   private final Deque<PacketValue> readableObjects = new ArrayDeque<>();
/*  50 */   private final List<PacketValue> packetValues = new ArrayList<>();
/*     */   
/*     */   private final ByteBuf inputBuffer;
/*     */   
/*     */   private final UserConnection userConnection;
/*     */   
/*     */   private boolean send = true;
/*     */   private PacketType packetType;
/*     */   private int id;
/*     */   
/*     */   public PacketWrapperImpl(int packetId, ByteBuf inputBuffer, UserConnection userConnection) {
/*  61 */     this.id = packetId;
/*  62 */     this.inputBuffer = inputBuffer;
/*  63 */     this.userConnection = userConnection;
/*     */   }
/*     */   
/*     */   public PacketWrapperImpl(PacketType packetType, ByteBuf inputBuffer, UserConnection userConnection) {
/*  67 */     this.packetType = packetType;
/*  68 */     this.id = (packetType != null) ? packetType.getId() : -1;
/*  69 */     this.inputBuffer = inputBuffer;
/*  70 */     this.userConnection = userConnection;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(Type<T> type, int index) throws Exception {
/*  75 */     int currentIndex = 0;
/*  76 */     for (PacketValue packetValue : this.packetValues) {
/*  77 */       if (packetValue.type() != type) {
/*     */         continue;
/*     */       }
/*  80 */       if (currentIndex == index)
/*     */       {
/*  82 */         return (T)packetValue.value();
/*     */       }
/*  84 */       currentIndex++;
/*     */     } 
/*  86 */     throw createInformativeException(new ArrayIndexOutOfBoundsException("Could not find type " + type.getTypeName() + " at " + index), type, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean is(Type type, int index) {
/*  91 */     int currentIndex = 0;
/*  92 */     for (PacketValue packetValue : this.packetValues) {
/*  93 */       if (packetValue.type() != type) {
/*     */         continue;
/*     */       }
/*  96 */       if (currentIndex == index) {
/*  97 */         return true;
/*     */       }
/*  99 */       currentIndex++;
/*     */     } 
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable(Type type, int index) {
/* 106 */     int currentIndex = 0;
/* 107 */     for (PacketValue packetValue : this.readableObjects) {
/* 108 */       if (packetValue.type().getBaseClass() != type.getBaseClass()) {
/*     */         continue;
/*     */       }
/* 111 */       if (currentIndex == index) {
/* 112 */         return true;
/*     */       }
/* 114 */       currentIndex++;
/*     */     } 
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> void set(Type<T> type, int index, T value) throws Exception {
/* 122 */     int currentIndex = 0;
/* 123 */     for (PacketValue packetValue : this.packetValues) {
/* 124 */       if (packetValue.type() != type) {
/*     */         continue;
/*     */       }
/* 127 */       if (currentIndex == index) {
/* 128 */         packetValue.setValue(attemptTransform(type, value));
/*     */         return;
/*     */       } 
/* 131 */       currentIndex++;
/*     */     } 
/* 133 */     throw createInformativeException(new ArrayIndexOutOfBoundsException("Could not find type " + type.getTypeName() + " at " + index), type, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T read(Type<T> type) throws Exception {
/* 138 */     if (type == Type.NOTHING) {
/* 139 */       return null;
/*     */     }
/*     */     
/* 142 */     if (this.readableObjects.isEmpty()) {
/* 143 */       Preconditions.checkNotNull(this.inputBuffer, "This packet does not have an input buffer.");
/*     */       
/*     */       try {
/* 146 */         return (T)type.read(this.inputBuffer);
/* 147 */       } catch (Exception e) {
/* 148 */         throw createInformativeException(e, type, this.packetValues.size() + 1);
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     PacketValue readValue = this.readableObjects.poll();
/* 153 */     Type<T> readType = readValue.type();
/* 154 */     if (readType == type || (type
/* 155 */       .getBaseClass() == readType.getBaseClass() && type
/* 156 */       .getOutputClass() == readType.getOutputClass()))
/*     */     {
/* 158 */       return (T)readValue.value(); } 
/* 159 */     if (readType == Type.NOTHING) {
/* 160 */       return read(type);
/*     */     }
/* 162 */     throw createInformativeException(new IOException("Unable to read type " + type.getTypeName() + ", found " + readValue.type().getTypeName()), type, this.readableObjects.size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> void write(Type<T> type, T value) {
/* 168 */     this.packetValues.add(new PacketValue(type, attemptTransform(type, value)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object attemptTransform(Type<?> expectedType, Object value) {
/* 179 */     if (value != null && !expectedType.getOutputClass().isAssignableFrom(value.getClass())) {
/*     */       
/* 181 */       if (expectedType instanceof TypeConverter) {
/* 182 */         return ((TypeConverter)expectedType).from(value);
/*     */       }
/*     */       
/* 185 */       Via.getPlatform().getLogger().warning("Possible type mismatch: " + value.getClass().getName() + " -> " + expectedType.getOutputClass());
/*     */     } 
/* 187 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T passthrough(Type<T> type) throws Exception {
/* 192 */     T value = read(type);
/* 193 */     write(type, value);
/* 194 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void passthroughAll() throws Exception {
/* 200 */     this.packetValues.addAll(this.readableObjects);
/* 201 */     this.readableObjects.clear();
/*     */     
/* 203 */     if (this.inputBuffer.isReadable()) {
/* 204 */       passthrough(Type.REMAINING_BYTES);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToBuffer(ByteBuf buffer) throws Exception {
/* 210 */     if (this.id != -1) {
/* 211 */       Type.VAR_INT.writePrimitive(buffer, this.id);
/*     */     }
/* 213 */     if (!this.readableObjects.isEmpty()) {
/* 214 */       this.packetValues.addAll(this.readableObjects);
/* 215 */       this.readableObjects.clear();
/*     */     } 
/*     */     
/* 218 */     int index = 0;
/* 219 */     for (PacketValue packetValue : this.packetValues) {
/*     */       try {
/* 221 */         packetValue.type().write(buffer, packetValue.value());
/* 222 */       } catch (Exception e) {
/* 223 */         throw createInformativeException(e, packetValue.type(), index);
/*     */       } 
/* 225 */       index++;
/*     */     } 
/* 227 */     writeRemaining(buffer);
/*     */   }
/*     */   
/*     */   private InformativeException createInformativeException(Exception cause, Type<?> type, int index) {
/* 231 */     return (new InformativeException(cause))
/* 232 */       .set("Index", Integer.valueOf(index))
/* 233 */       .set("Type", type.getTypeName())
/* 234 */       .set("Packet ID", Integer.valueOf(this.id))
/* 235 */       .set("Packet Type", this.packetType)
/* 236 */       .set("Data", this.packetValues);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearInputBuffer() {
/* 241 */     if (this.inputBuffer != null) {
/* 242 */       this.inputBuffer.clear();
/*     */     }
/* 244 */     this.readableObjects.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearPacket() {
/* 249 */     clearInputBuffer();
/* 250 */     this.packetValues.clear();
/*     */   }
/*     */   
/*     */   private void writeRemaining(ByteBuf output) {
/* 254 */     if (this.inputBuffer != null) {
/* 255 */       output.writeBytes(this.inputBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void send(Class<? extends Protocol> protocol, boolean skipCurrentPipeline) throws Exception {
/* 261 */     send0(protocol, skipCurrentPipeline, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scheduleSend(Class<? extends Protocol> protocol, boolean skipCurrentPipeline) throws Exception {
/* 266 */     send0(protocol, skipCurrentPipeline, false);
/*     */   }
/*     */   
/*     */   private void send0(Class<? extends Protocol> protocol, boolean skipCurrentPipeline, boolean currentThread) throws Exception {
/* 270 */     if (isCancelled()) {
/*     */       return;
/*     */     }
/*     */     
/* 274 */     UserConnection connection = user();
/* 275 */     if (currentThread) {
/*     */       try {
/* 277 */         ByteBuf output = constructPacket(protocol, skipCurrentPipeline, Direction.CLIENTBOUND);
/* 278 */         connection.sendRawPacket(output);
/* 279 */       } catch (Exception e) {
/* 280 */         if (!PipelineUtil.containsCause(e, CancelException.class)) {
/* 281 */           throw e;
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 287 */     connection.getChannel().eventLoop().submit(() -> {
/*     */           try {
/*     */             ByteBuf output = constructPacket(protocol, skipCurrentPipeline, Direction.CLIENTBOUND);
/*     */             connection.sendRawPacket(output);
/* 291 */           } catch (RuntimeException e) {
/*     */             if (!PipelineUtil.containsCause(e, CancelException.class)) {
/*     */               throw e;
/*     */             }
/* 295 */           } catch (Exception e) {
/*     */             if (!PipelineUtil.containsCause(e, CancelException.class)) {
/*     */               throw new RuntimeException(e);
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuf constructPacket(Class<? extends Protocol> packetProtocol, boolean skipCurrentPipeline, Direction direction) throws Exception {
/* 312 */     ProtocolInfo protocolInfo = user().getProtocolInfo();
/* 313 */     List<Protocol> pipes = (direction == Direction.SERVERBOUND) ? protocolInfo.getPipeline().pipes() : protocolInfo.getPipeline().reversedPipes();
/* 314 */     List<Protocol> protocols = new ArrayList<>();
/* 315 */     int index = -1;
/* 316 */     for (int i = 0; i < pipes.size(); i++) {
/*     */       
/* 318 */       Protocol protocol = pipes.get(i);
/* 319 */       if (protocol.isBaseProtocol()) {
/* 320 */         protocols.add(protocol);
/*     */       }
/*     */       
/* 323 */       if (protocol.getClass() == packetProtocol) {
/* 324 */         index = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 329 */     if (index == -1)
/*     */     {
/* 331 */       throw new NoSuchElementException(packetProtocol.getCanonicalName());
/*     */     }
/*     */     
/* 334 */     if (skipCurrentPipeline) {
/* 335 */       index = Math.min(index + 1, pipes.size());
/*     */     }
/*     */ 
/*     */     
/* 339 */     protocols.addAll(pipes.subList(index, pipes.size()));
/*     */ 
/*     */     
/* 342 */     resetReader();
/*     */ 
/*     */     
/* 345 */     apply(direction, protocolInfo.getState(direction), 0, protocols);
/* 346 */     ByteBuf output = (this.inputBuffer == null) ? user().getChannel().alloc().buffer() : this.inputBuffer.alloc().buffer();
/*     */     try {
/* 348 */       writeToBuffer(output);
/* 349 */       return output.retain();
/*     */     } finally {
/* 351 */       output.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture sendFuture(Class<? extends Protocol> packetProtocol) throws Exception {
/* 357 */     if (!isCancelled()) {
/* 358 */       ByteBuf output = constructPacket(packetProtocol, true, Direction.CLIENTBOUND);
/* 359 */       return user().sendRawPacketFuture(output);
/*     */     } 
/* 361 */     return user().getChannel().newFailedFuture(new Exception("Cancelled packet"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRaw() throws Exception {
/* 366 */     sendRaw(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scheduleSendRaw() throws Exception {
/* 371 */     sendRaw(false);
/*     */   }
/*     */   
/*     */   private void sendRaw(boolean currentThread) throws Exception {
/* 375 */     if (isCancelled())
/*     */       return; 
/* 377 */     ByteBuf output = (this.inputBuffer == null) ? user().getChannel().alloc().buffer() : this.inputBuffer.alloc().buffer();
/*     */     try {
/* 379 */       writeToBuffer(output);
/* 380 */       if (currentThread) {
/* 381 */         user().sendRawPacket(output.retain());
/*     */       } else {
/* 383 */         user().scheduleSendRawPacket(output.retain());
/*     */       } 
/*     */     } finally {
/* 386 */       output.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketWrapperImpl create(int packetId) {
/* 392 */     return new PacketWrapperImpl(packetId, null, user());
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketWrapperImpl create(int packetId, PacketHandler handler) throws Exception {
/* 397 */     PacketWrapperImpl wrapper = create(packetId);
/* 398 */     handler.handle(wrapper);
/* 399 */     return wrapper;
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketWrapperImpl apply(Direction direction, State state, int index, List<Protocol> pipeline, boolean reverse) throws Exception {
/* 404 */     Protocol[] array = pipeline.<Protocol>toArray(PROTOCOL_ARRAY);
/* 405 */     return apply(direction, state, reverse ? (array.length - 1) : index, array, reverse);
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketWrapperImpl apply(Direction direction, State state, int index, List<Protocol> pipeline) throws Exception {
/* 410 */     return apply(direction, state, index, pipeline.<Protocol>toArray(PROTOCOL_ARRAY), false);
/*     */   }
/*     */ 
/*     */   
/*     */   private PacketWrapperImpl apply(Direction direction, State state, int index, Protocol[] pipeline, boolean reverse) throws Exception {
/* 415 */     State updatedState = state;
/* 416 */     if (reverse) {
/* 417 */       for (int i = index; i >= 0; i--) {
/* 418 */         pipeline[i].transform(direction, updatedState, this);
/* 419 */         resetReader();
/* 420 */         if (this.packetType != null) {
/* 421 */           updatedState = this.packetType.state();
/*     */         }
/*     */       } 
/*     */     } else {
/* 425 */       for (int i = index; i < pipeline.length; i++) {
/* 426 */         pipeline[i].transform(direction, updatedState, this);
/* 427 */         resetReader();
/* 428 */         if (this.packetType != null) {
/* 429 */           updatedState = this.packetType.state();
/*     */         }
/*     */       } 
/*     */     } 
/* 433 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/* 438 */     return !this.send;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCancelled(boolean cancel) {
/* 443 */     this.send = !cancel;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserConnection user() {
/* 448 */     return this.userConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetReader() {
/* 454 */     for (int i = this.packetValues.size() - 1; i >= 0; i--) {
/* 455 */       this.readableObjects.addFirst(this.packetValues.get(i));
/*     */     }
/* 457 */     this.packetValues.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendToServerRaw() throws Exception {
/* 462 */     sendToServerRaw(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scheduleSendToServerRaw() throws Exception {
/* 467 */     sendToServerRaw(false);
/*     */   }
/*     */   
/*     */   private void sendToServerRaw(boolean currentThread) throws Exception {
/* 471 */     if (isCancelled()) {
/*     */       return;
/*     */     }
/*     */     
/* 475 */     ByteBuf output = (this.inputBuffer == null) ? user().getChannel().alloc().buffer() : this.inputBuffer.alloc().buffer();
/*     */     try {
/* 477 */       writeToBuffer(output);
/* 478 */       if (currentThread) {
/* 479 */         user().sendRawPacketToServer(output.retain());
/*     */       } else {
/* 481 */         user().scheduleSendRawPacketToServer(output.retain());
/*     */       } 
/*     */     } finally {
/* 484 */       output.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendToServer(Class<? extends Protocol> protocol, boolean skipCurrentPipeline) throws Exception {
/* 490 */     sendToServer0(protocol, skipCurrentPipeline, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scheduleSendToServer(Class<? extends Protocol> protocol, boolean skipCurrentPipeline) throws Exception {
/* 495 */     sendToServer0(protocol, skipCurrentPipeline, false);
/*     */   }
/*     */   
/*     */   private void sendToServer0(Class<? extends Protocol> protocol, boolean skipCurrentPipeline, boolean currentThread) throws Exception {
/* 499 */     if (isCancelled()) {
/*     */       return;
/*     */     }
/*     */     
/* 503 */     UserConnection connection = user();
/* 504 */     if (currentThread) {
/*     */       try {
/* 506 */         ByteBuf output = constructPacket(protocol, skipCurrentPipeline, Direction.SERVERBOUND);
/* 507 */         connection.sendRawPacketToServer(output);
/* 508 */       } catch (Exception e) {
/* 509 */         if (!PipelineUtil.containsCause(e, CancelException.class)) {
/* 510 */           throw e;
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 516 */     connection.getChannel().eventLoop().submit(() -> {
/*     */           try {
/*     */             ByteBuf output = constructPacket(protocol, skipCurrentPipeline, Direction.SERVERBOUND);
/*     */             connection.sendRawPacketToServer(output);
/* 520 */           } catch (RuntimeException e) {
/*     */             if (!PipelineUtil.containsCause(e, CancelException.class)) {
/*     */               throw e;
/*     */             }
/* 524 */           } catch (Exception e) {
/*     */             if (!PipelineUtil.containsCause(e, CancelException.class)) {
/*     */               throw new RuntimeException(e);
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketType getPacketType() {
/* 534 */     return this.packetType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPacketType(PacketType packetType) {
/* 539 */     this.packetType = packetType;
/* 540 */     this.id = (packetType != null) ? packetType.getId() : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId() {
/* 545 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setId(int id) {
/* 552 */     this.packetType = null;
/* 553 */     this.id = id;
/*     */   }
/*     */   
/*     */   public ByteBuf getInputBuffer() {
/* 557 */     return this.inputBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 562 */     return "PacketWrapper{type=" + this.packetType + ", id=" + this.id + ", values=" + this.packetValues + ", readable=" + this.readableObjects + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class PacketValue
/*     */   {
/*     */     private final Type type;
/*     */     
/*     */     private Object value;
/*     */ 
/*     */     
/*     */     private PacketValue(Type type, Object value) {
/* 575 */       this.type = type;
/* 576 */       this.value = value;
/*     */     }
/*     */     
/*     */     public Type type() {
/* 580 */       return this.type;
/*     */     }
/*     */     
/*     */     public Object value() {
/* 584 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setValue(Object value) {
/* 588 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 593 */       if (this == o) return true; 
/* 594 */       if (o == null || getClass() != o.getClass()) return false; 
/* 595 */       PacketValue that = (PacketValue)o;
/* 596 */       if (!this.type.equals(that.type)) return false; 
/* 597 */       return Objects.equals(this.value, that.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 602 */       int result = this.type.hashCode();
/* 603 */       result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
/* 604 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 609 */       return "{" + this.type + ": " + this.value + "}";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocol\packet\PacketWrapperImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */