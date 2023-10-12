/*     */ package com.viaversion.viaversion.api.protocol;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.Direction;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.protocol.packet.mapping.PacketMapping;
/*     */ import com.viaversion.viaversion.api.protocol.packet.mapping.PacketMappings;
/*     */ import com.viaversion.viaversion.api.protocol.packet.provider.PacketTypeMap;
/*     */ import com.viaversion.viaversion.api.protocol.packet.provider.PacketTypesProvider;
/*     */ import com.viaversion.viaversion.api.protocol.packet.provider.SimplePacketTypesProvider;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.rewriter.Rewriter;
/*     */ import com.viaversion.viaversion.exception.CancelException;
/*     */ import com.viaversion.viaversion.exception.InformativeException;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.logging.Level;
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
/*     */ public abstract class AbstractProtocol<CU extends ClientboundPacketType, CM extends ClientboundPacketType, SM extends ServerboundPacketType, SU extends ServerboundPacketType>
/*     */   implements Protocol<CU, CM, SM, SU>
/*     */ {
/*     */   protected final Class<CU> unmappedClientboundPacketType;
/*     */   protected final Class<CM> mappedClientboundPacketType;
/*     */   protected final Class<SM> mappedServerboundPacketType;
/*     */   protected final Class<SU> unmappedServerboundPacketType;
/*     */   protected final PacketTypesProvider<CU, CM, SM, SU> packetTypesProvider;
/*     */   protected final PacketMappings clientboundMappings;
/*     */   protected final PacketMappings serverboundMappings;
/*  70 */   private final Map<Class<?>, Object> storedObjects = new HashMap<>();
/*     */   private boolean initialized;
/*     */   
/*     */   @Deprecated
/*     */   protected AbstractProtocol() {
/*  75 */     this((Class<CU>)null, (Class<CM>)null, (Class<SM>)null, (Class<SU>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractProtocol(Class<CU> unmappedClientboundPacketType, Class<CM> mappedClientboundPacketType, Class<SM> mappedServerboundPacketType, Class<SU> unmappedServerboundPacketType) {
/*  84 */     this.unmappedClientboundPacketType = unmappedClientboundPacketType;
/*  85 */     this.mappedClientboundPacketType = mappedClientboundPacketType;
/*  86 */     this.mappedServerboundPacketType = mappedServerboundPacketType;
/*  87 */     this.unmappedServerboundPacketType = unmappedServerboundPacketType;
/*  88 */     this.packetTypesProvider = createPacketTypesProvider();
/*  89 */     this.clientboundMappings = createClientboundPacketMappings();
/*  90 */     this.serverboundMappings = createServerboundPacketMappings();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void initialize() {
/*  95 */     Preconditions.checkArgument(!this.initialized, "Protocol has already been initialized");
/*  96 */     this.initialized = true;
/*     */     
/*  98 */     registerPackets();
/*  99 */     registerConfigurationChangeHandlers();
/*     */ 
/*     */     
/* 102 */     if (this.unmappedClientboundPacketType != null && this.mappedClientboundPacketType != null && this.unmappedClientboundPacketType != this.mappedClientboundPacketType)
/*     */     {
/* 104 */       registerPacketIdChanges(this.packetTypesProvider
/* 105 */           .unmappedClientboundPacketTypes(), this.packetTypesProvider
/* 106 */           .mappedClientboundPacketTypes(), this::hasRegisteredClientbound, this::registerClientbound);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 111 */     if (this.mappedServerboundPacketType != null && this.unmappedServerboundPacketType != null && this.mappedServerboundPacketType != this.unmappedServerboundPacketType)
/*     */     {
/* 113 */       registerPacketIdChanges(this.packetTypesProvider
/* 114 */           .unmappedServerboundPacketTypes(), this.packetTypesProvider
/* 115 */           .mappedServerboundPacketTypes(), this::hasRegisteredServerbound, this::registerServerbound);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerConfigurationChangeHandlers() {
/* 126 */     SU configurationAcknowledgedPacket = configurationAcknowledgedPacket();
/* 127 */     if (configurationAcknowledgedPacket != null) {
/* 128 */       registerServerbound(configurationAcknowledgedPacket, setClientStateHandler(State.CONFIGURATION));
/*     */     }
/*     */     
/* 131 */     CU startConfigurationPacket = startConfigurationPacket();
/* 132 */     if (startConfigurationPacket != null) {
/* 133 */       registerClientbound(startConfigurationPacket, setServerStateHandler(State.CONFIGURATION));
/*     */     }
/*     */     
/* 136 */     ServerboundPacketType finishConfigurationPacket = serverboundFinishConfigurationPacket();
/* 137 */     if (finishConfigurationPacket != null) {
/* 138 */       int id = finishConfigurationPacket.getId();
/* 139 */       registerServerbound(State.CONFIGURATION, id, id, setClientStateHandler(State.PLAY));
/*     */     } 
/*     */     
/* 142 */     ClientboundPacketType clientboundFinishConfigurationPacket = clientboundFinishConfigurationPacket();
/* 143 */     if (clientboundFinishConfigurationPacket != null) {
/* 144 */       int id = clientboundFinishConfigurationPacket.getId();
/* 145 */       registerClientbound(State.CONFIGURATION, id, id, setServerStateHandler(State.PLAY));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <U extends PacketType, M extends PacketType> void registerPacketIdChanges(Map<State, PacketTypeMap<U>> unmappedPacketTypes, Map<State, PacketTypeMap<M>> mappedPacketTypes, Predicate<U> registeredPredicate, BiConsumer<U, M> registerConsumer) {
/* 155 */     for (Map.Entry<State, PacketTypeMap<M>> entry : mappedPacketTypes.entrySet()) {
/* 156 */       PacketTypeMap<M> mappedTypes = entry.getValue();
/* 157 */       for (PacketType packetType1 : ((PacketTypeMap)unmappedPacketTypes.get(entry.getKey())).types()) {
/* 158 */         PacketType packetType2 = mappedTypes.typeByName(packetType1.getName());
/* 159 */         if (packetType2 == null) {
/*     */           
/* 161 */           Preconditions.checkArgument(registeredPredicate.test((U)packetType1), "Packet %s in %s has no mapping - it needs to be manually cancelled or remapped", new Object[] { packetType1, 
/* 162 */                 getClass() });
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 167 */         if (packetType1.getId() != packetType2.getId() && !registeredPredicate.test((U)packetType1)) {
/* 168 */           registerConsumer.accept((U)packetType1, (M)packetType2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void loadMappingData() {
/* 176 */     getMappingData().load();
/* 177 */     onMappingDataLoaded();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/* 184 */     callRegister((Rewriter<?>)getEntityRewriter());
/* 185 */     callRegister((Rewriter<?>)getItemRewriter());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onMappingDataLoaded() {
/* 194 */     callOnMappingDataLoaded((Rewriter<?>)getEntityRewriter());
/* 195 */     callOnMappingDataLoaded((Rewriter<?>)getItemRewriter());
/*     */   }
/*     */   
/*     */   private void callRegister(Rewriter<?> rewriter) {
/* 199 */     if (rewriter != null) {
/* 200 */       rewriter.register();
/*     */     }
/*     */   }
/*     */   
/*     */   private void callOnMappingDataLoaded(Rewriter<?> rewriter) {
/* 205 */     if (rewriter != null) {
/* 206 */       rewriter.onMappingDataLoaded();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addEntityTracker(UserConnection connection, EntityTracker tracker) {
/* 211 */     connection.addEntityTracker(getClass(), tracker);
/*     */   }
/*     */   
/*     */   protected PacketTypesProvider<CU, CM, SM, SU> createPacketTypesProvider() {
/* 215 */     return (PacketTypesProvider<CU, CM, SM, SU>)new SimplePacketTypesProvider(
/* 216 */         packetTypeMap((Class)this.unmappedClientboundPacketType), 
/* 217 */         packetTypeMap((Class)this.mappedClientboundPacketType), 
/* 218 */         packetTypeMap((Class)this.mappedServerboundPacketType), 
/* 219 */         packetTypeMap((Class)this.unmappedServerboundPacketType));
/*     */   }
/*     */ 
/*     */   
/*     */   protected PacketMappings createClientboundPacketMappings() {
/* 224 */     return PacketMappings.arrayMappings();
/*     */   }
/*     */   
/*     */   protected PacketMappings createServerboundPacketMappings() {
/* 228 */     return PacketMappings.arrayMappings();
/*     */   }
/*     */   
/*     */   private <P extends PacketType> Map<State, PacketTypeMap<P>> packetTypeMap(Class<P> packetTypeClass) {
/* 232 */     if (packetTypeClass != null) {
/* 233 */       Map<State, PacketTypeMap<P>> map = new EnumMap<>(State.class);
/* 234 */       map.put(State.PLAY, PacketTypeMap.of(packetTypeClass));
/* 235 */       return map;
/*     */     } 
/* 237 */     return Collections.emptyMap();
/*     */   }
/*     */   
/*     */   protected SU configurationAcknowledgedPacket() {
/* 241 */     Map<State, PacketTypeMap<SU>> packetTypes = this.packetTypesProvider.unmappedServerboundPacketTypes();
/* 242 */     PacketTypeMap<SU> packetTypeMap = packetTypes.get(State.PLAY);
/* 243 */     return (packetTypeMap != null) ? (SU)packetTypeMap.typeByName("CONFIGURATION_ACKNOWLEDGED") : null;
/*     */   }
/*     */   
/*     */   protected CU startConfigurationPacket() {
/* 247 */     Map<State, PacketTypeMap<CU>> packetTypes = this.packetTypesProvider.unmappedClientboundPacketTypes();
/* 248 */     PacketTypeMap<CU> packetTypeMap = packetTypes.get(State.PLAY);
/* 249 */     return (packetTypeMap != null) ? (CU)packetTypeMap.typeByName("START_CONFIGURATION") : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ServerboundPacketType serverboundFinishConfigurationPacket() {
/* 254 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientboundPacketType clientboundFinishConfigurationPacket() {
/* 259 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerServerbound(State state, int unmappedPacketId, int mappedPacketId, PacketHandler handler, boolean override) {
/* 266 */     Preconditions.checkArgument((unmappedPacketId != -1), "Unmapped packet id cannot be -1");
/* 267 */     PacketMapping packetMapping = PacketMapping.of(mappedPacketId, handler);
/* 268 */     if (!override && this.serverboundMappings.hasMapping(state, unmappedPacketId)) {
/* 269 */       Via.getPlatform().getLogger().log(Level.WARNING, unmappedPacketId + " already registered! If this override is intentional, set override to true. Stacktrace: ", new Exception());
/*     */     }
/*     */     
/* 272 */     this.serverboundMappings.addMapping(state, unmappedPacketId, packetMapping);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancelServerbound(State state, int unmappedPacketId) {
/* 277 */     registerServerbound(state, unmappedPacketId, unmappedPacketId, PacketWrapper::cancel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerClientbound(State state, int unmappedPacketId, int mappedPacketId, PacketHandler handler, boolean override) {
/* 282 */     Preconditions.checkArgument((unmappedPacketId != -1), "Unmapped packet id cannot be -1");
/* 283 */     PacketMapping packetMapping = PacketMapping.of(mappedPacketId, handler);
/* 284 */     if (!override && this.clientboundMappings.hasMapping(state, unmappedPacketId)) {
/* 285 */       Via.getPlatform().getLogger().log(Level.WARNING, unmappedPacketId + " already registered! If override is intentional, set override to true. Stacktrace: ", new Exception());
/*     */     }
/*     */     
/* 288 */     this.clientboundMappings.addMapping(state, unmappedPacketId, packetMapping);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancelClientbound(State state, int unmappedPacketId) {
/* 293 */     registerClientbound(state, unmappedPacketId, unmappedPacketId, PacketWrapper::cancel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerClientbound(CU packetType, PacketHandler handler) {
/* 300 */     PacketTypeMap<CM> mappedPacketTypes = (PacketTypeMap<CM>)this.packetTypesProvider.mappedClientboundPacketTypes().get(packetType.state());
/* 301 */     ClientboundPacketType clientboundPacketType = mappedPacketType(packetType, mappedPacketTypes, this.unmappedClientboundPacketType, this.mappedClientboundPacketType);
/* 302 */     registerClientbound(packetType, (CM)clientboundPacketType, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerClientbound(CU packetType, CM mappedPacketType, PacketHandler handler, boolean override) {
/* 307 */     register(this.clientboundMappings, (PacketType)packetType, (PacketType)mappedPacketType, (Class)this.unmappedClientboundPacketType, (Class)this.mappedClientboundPacketType, handler, override);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancelClientbound(CU packetType) {
/* 312 */     registerClientbound(packetType, (CM)null, PacketWrapper::cancel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerServerbound(SU packetType, PacketHandler handler) {
/* 317 */     PacketTypeMap<SM> mappedPacketTypes = (PacketTypeMap<SM>)this.packetTypesProvider.mappedServerboundPacketTypes().get(packetType.state());
/* 318 */     ServerboundPacketType serverboundPacketType = mappedPacketType(packetType, mappedPacketTypes, this.unmappedServerboundPacketType, this.mappedServerboundPacketType);
/* 319 */     registerServerbound(packetType, (SM)serverboundPacketType, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerServerbound(SU packetType, SM mappedPacketType, PacketHandler handler, boolean override) {
/* 324 */     register(this.serverboundMappings, (PacketType)packetType, (PacketType)mappedPacketType, (Class)this.unmappedServerboundPacketType, (Class)this.mappedServerboundPacketType, handler, override);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancelServerbound(SU packetType) {
/* 329 */     registerServerbound(packetType, (SM)null, PacketWrapper::cancel);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void register(PacketMappings packetMappings, PacketType packetType, PacketType mappedPacketType, Class<? extends PacketType> unmappedPacketClass, Class<? extends PacketType> mappedPacketClass, PacketHandler handler, boolean override) {
/* 335 */     checkPacketType(packetType, (unmappedPacketClass == null || unmappedPacketClass.isInstance(packetType)));
/* 336 */     if (mappedPacketType != null) {
/* 337 */       checkPacketType(mappedPacketType, (mappedPacketClass == null || mappedPacketClass.isInstance(mappedPacketType)));
/* 338 */       Preconditions.checkArgument((packetType.state() == mappedPacketType.state()), "Packet type state does not match mapped packet type state");
/*     */       
/* 340 */       Preconditions.checkArgument((packetType.direction() == mappedPacketType.direction()), "Packet type direction does not match mapped packet type state");
/*     */     } 
/*     */ 
/*     */     
/* 344 */     PacketMapping packetMapping = PacketMapping.of(mappedPacketType, handler);
/* 345 */     if (!override && packetMappings.hasMapping(packetType)) {
/* 346 */       Via.getPlatform().getLogger().log(Level.WARNING, packetType + " already registered! If override is intentional, set override to true. Stacktrace: ", new Exception());
/*     */     }
/*     */     
/* 349 */     packetMappings.addMapping(packetType, packetMapping);
/*     */   }
/*     */   
/*     */   private static <U extends PacketType, M extends PacketType> M mappedPacketType(U packetType, PacketTypeMap<M> mappedTypes, Class<U> unmappedPacketTypeClass, Class<M> mappedPacketTypeClass) {
/* 353 */     Preconditions.checkNotNull(packetType);
/* 354 */     checkPacketType((PacketType)packetType, (unmappedPacketTypeClass == null || unmappedPacketTypeClass.isInstance(packetType)));
/* 355 */     if (unmappedPacketTypeClass == mappedPacketTypeClass)
/*     */     {
/* 357 */       return (M)packetType;
/*     */     }
/*     */     
/* 360 */     Preconditions.checkNotNull(mappedTypes, "Mapped packet types not provided for state %s of type class %s", new Object[] { packetType.state(), mappedPacketTypeClass });
/* 361 */     PacketType packetType1 = mappedTypes.typeByName(packetType.getName());
/* 362 */     if (packetType1 != null) {
/* 363 */       return (M)packetType1;
/*     */     }
/* 365 */     throw new IllegalArgumentException("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " could not be automatically mapped!");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasRegisteredClientbound(State state, int unmappedPacketId) {
/* 370 */     return this.clientboundMappings.hasMapping(state, unmappedPacketId);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasRegisteredServerbound(State state, int unmappedPacketId) {
/* 375 */     return this.serverboundMappings.hasMapping(state, unmappedPacketId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void transform(Direction direction, State state, PacketWrapper packetWrapper) throws Exception {
/* 380 */     PacketMappings mappings = (direction == Direction.CLIENTBOUND) ? this.clientboundMappings : this.serverboundMappings;
/* 381 */     int unmappedId = packetWrapper.getId();
/* 382 */     PacketMapping packetMapping = mappings.mappedPacket(state, unmappedId);
/* 383 */     if (packetMapping == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 388 */     packetMapping.applyType(packetWrapper);
/* 389 */     PacketHandler handler = packetMapping.handler();
/* 390 */     if (handler != null) {
/*     */       try {
/* 392 */         handler.handle(packetWrapper);
/* 393 */       } catch (CancelException e) {
/*     */         
/* 395 */         throw e;
/* 396 */       } catch (InformativeException e) {
/*     */         
/* 398 */         e.addSource(handler.getClass());
/* 399 */         throwRemapError(direction, state, unmappedId, packetWrapper.getId(), e);
/*     */         return;
/* 401 */       } catch (Exception e) {
/*     */         
/* 403 */         InformativeException ex = new InformativeException(e);
/* 404 */         ex.addSource(handler.getClass());
/* 405 */         throwRemapError(direction, state, unmappedId, packetWrapper.getId(), ex);
/*     */         
/*     */         return;
/*     */       } 
/* 409 */       if (packetWrapper.isCancelled()) {
/* 410 */         throw CancelException.generate();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void throwRemapError(Direction direction, State state, int unmappedPacketId, int mappedPacketId, InformativeException e) throws InformativeException {
/* 417 */     if (state != State.PLAY && direction == Direction.SERVERBOUND && !Via.getManager().debugHandler().enabled()) {
/* 418 */       e.setShouldBePrinted(false);
/* 419 */       throw e;
/*     */     } 
/*     */     
/* 422 */     PacketType packetType = (direction == Direction.CLIENTBOUND) ? (PacketType)unmappedClientboundPacketType(state, unmappedPacketId) : (PacketType)unmappedServerboundPacketType(state, unmappedPacketId);
/* 423 */     if (packetType != null) {
/* 424 */       Via.getPlatform().getLogger().warning("ERROR IN " + getClass().getSimpleName() + " IN REMAP OF " + packetType + " (" + toNiceHex(unmappedPacketId) + ")");
/*     */     } else {
/* 426 */       Via.getPlatform().getLogger().warning("ERROR IN " + getClass().getSimpleName() + " IN REMAP OF " + 
/* 427 */           toNiceHex(unmappedPacketId) + "->" + toNiceHex(mappedPacketId));
/*     */     } 
/* 429 */     throw e;
/*     */   }
/*     */   
/*     */   private CU unmappedClientboundPacketType(State state, int packetId) {
/* 433 */     PacketTypeMap<CU> map = (PacketTypeMap<CU>)this.packetTypesProvider.unmappedClientboundPacketTypes().get(state);
/* 434 */     return (map != null) ? (CU)map.typeById(packetId) : null;
/*     */   }
/*     */   
/*     */   private SU unmappedServerboundPacketType(State state, int packetId) {
/* 438 */     PacketTypeMap<SU> map = (PacketTypeMap<SU>)this.packetTypesProvider.unmappedServerboundPacketTypes().get(state);
/* 439 */     return (map != null) ? (SU)map.typeById(packetId) : null;
/*     */   }
/*     */   
/*     */   public static String toNiceHex(int id) {
/* 443 */     String hex = Integer.toHexString(id).toUpperCase();
/* 444 */     return ((hex.length() == 1) ? "0x0" : "0x") + hex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkPacketType(PacketType packetType, boolean isValid) {
/* 453 */     if (!isValid) {
/* 454 */       throw new IllegalArgumentException("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " is taken from the wrong packet types class");
/*     */     }
/*     */   }
/*     */   
/*     */   protected PacketHandler setClientStateHandler(State state) {
/* 459 */     return wrapper -> wrapper.user().getProtocolInfo().setClientState(state);
/*     */   }
/*     */   
/*     */   protected PacketHandler setServerStateHandler(State state) {
/* 463 */     return wrapper -> wrapper.user().getProtocolInfo().setClientState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketTypesProvider<CU, CM, SM, SU> getPacketTypesProvider() {
/* 468 */     return this.packetTypesProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(Class<T> objectClass) {
/* 474 */     return (T)this.storedObjects.get(objectClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(Object object) {
/* 479 */     this.storedObjects.put(object.getClass(), object);
/*     */   }
/*     */   
/*     */   public PacketTypesProvider<CU, CM, SM, SU> packetTypesProvider() {
/* 483 */     return this.packetTypesProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 488 */     return "Protocol:" + getClass().getSimpleName();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\protocol\AbstractProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */