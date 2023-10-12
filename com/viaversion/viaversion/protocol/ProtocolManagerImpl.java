/*     */ package com.viaversion.viaversion.protocol;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Range;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.MappingDataLoader;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.ProtocolManager;
/*     */ import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
/*     */ import com.viaversion.viaversion.api.protocol.ProtocolPathKey;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.VersionedPacketTransformer;
/*     */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*     */ import com.viaversion.viaversion.api.protocol.version.ServerProtocolVersion;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectSortedMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.IntBidirectionalIterator;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
/*     */ import com.viaversion.viaversion.protocol.packet.PacketWrapperImpl;
/*     */ import com.viaversion.viaversion.protocol.packet.VersionedPacketTransformerImpl;
/*     */ import com.viaversion.viaversion.protocols.base.BaseProtocol;
/*     */ import com.viaversion.viaversion.protocols.base.BaseProtocol1_16;
/*     */ import com.viaversion.viaversion.protocols.base.BaseProtocol1_7;
/*     */ import com.viaversion.viaversion.protocols.protocol1_10to1_9_3.Protocol1_10To1_9_3_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_11_1to1_11.Protocol1_11_1To1_11;
/*     */ import com.viaversion.viaversion.protocols.protocol1_11to1_10.Protocol1_11To1_10;
/*     */ import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.Protocol1_12_1To1_12;
/*     */ import com.viaversion.viaversion.protocols.protocol1_12_2to1_12_1.Protocol1_12_2To1_12_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.Protocol1_12To1_11_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.Protocol1_13_2To1_13_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_14_1to1_14.Protocol1_14_1To1_14;
/*     */ import com.viaversion.viaversion.protocols.protocol1_14_2to1_14_1.Protocol1_14_2To1_14_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_14_3to1_14_2.Protocol1_14_3To1_14_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_14_4to1_14_3.Protocol1_14_4To1_14_3;
/*     */ import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_15_1to1_15.Protocol1_15_1To1_15;
/*     */ import com.viaversion.viaversion.protocols.protocol1_15_2to1_15_1.Protocol1_15_2To1_15_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_16_1to1_16.Protocol1_16_1To1_16;
/*     */ import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_16_3to1_16_2.Protocol1_16_3To1_16_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_16_4to1_16_3.Protocol1_16_4To1_16_3;
/*     */ import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_17_1to1_17.Protocol1_17_1To1_17;
/*     */ import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_18_2to1_18.Protocol1_18_2To1_18;
/*     */ import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.Protocol1_18To1_17_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.Protocol1_19_1To1_19;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.Protocol1_19_3To1_19_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.Protocol1_19_4To1_19_3;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.Protocol1_20_2To1_20;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20to1_19_4.Protocol1_20To1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9_1to1_9.Protocol1_9_1To1_9;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.Protocol1_9_3To1_9_1_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
/*     */ import com.viaversion.viaversion.util.Pair;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.function.Function;
/*     */ import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
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
/*     */ public class ProtocolManagerImpl
/*     */   implements ProtocolManager
/*     */ {
/* 109 */   private static final Protocol BASE_PROTOCOL = (Protocol)new BaseProtocol();
/*     */ 
/*     */   
/* 112 */   private final Int2ObjectMap<Int2ObjectMap<Protocol>> registryMap = (Int2ObjectMap<Int2ObjectMap<Protocol>>)new Int2ObjectOpenHashMap(32);
/* 113 */   private final Map<Class<? extends Protocol>, Protocol<?, ?, ?, ?>> protocols = new HashMap<>(64);
/* 114 */   private final Map<ProtocolPathKey, List<ProtocolPathEntry>> pathCache = new ConcurrentHashMap<>();
/* 115 */   private final Set<Integer> supportedVersions = new HashSet<>();
/* 116 */   private final List<Pair<Range<Integer>, Protocol>> baseProtocols = Lists.newCopyOnWriteArrayList();
/*     */   
/* 118 */   private final ReadWriteLock mappingLoaderLock = new ReentrantReadWriteLock();
/* 119 */   private Map<Class<? extends Protocol>, CompletableFuture<Void>> mappingLoaderFutures = new HashMap<>();
/*     */   
/*     */   private ThreadPoolExecutor mappingLoaderExecutor;
/*     */   private boolean mappingsLoaded;
/* 123 */   private ServerProtocolVersion serverProtocolVersion = new ServerProtocolVersionSingleton(-1);
/*     */   private int maxPathDeltaIncrease;
/* 125 */   private int maxProtocolPathSize = 50;
/*     */   
/*     */   public ProtocolManagerImpl() {
/* 128 */     ThreadFactory threadFactory = (new ThreadFactoryBuilder()).setNameFormat("Via-Mappingloader-%d").build();
/* 129 */     this.mappingLoaderExecutor = new ThreadPoolExecutor(12, 2147483647, 30L, TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory);
/* 130 */     this.mappingLoaderExecutor.allowCoreThreadTimeOut(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerProtocols() {
/* 135 */     registerBaseProtocol(BASE_PROTOCOL, Range.lessThan(Integer.valueOf(-2147483648)));
/* 136 */     registerBaseProtocol((Protocol)new BaseProtocol1_7(), Range.lessThan(Integer.valueOf(ProtocolVersion.v1_16.getVersion())));
/* 137 */     registerBaseProtocol((Protocol)new BaseProtocol1_16(), Range.atLeast(Integer.valueOf(ProtocolVersion.v1_16.getVersion())));
/*     */     
/* 139 */     registerProtocol((Protocol)new Protocol1_9To1_8(), ProtocolVersion.v1_9, ProtocolVersion.v1_8);
/* 140 */     registerProtocol((Protocol)new Protocol1_9_1To1_9(), Arrays.asList(new Integer[] { Integer.valueOf(ProtocolVersion.v1_9_1.getVersion()), Integer.valueOf(ProtocolVersion.v1_9_2.getVersion()) }, ), ProtocolVersion.v1_9.getVersion());
/* 141 */     registerProtocol((Protocol)new Protocol1_9_3To1_9_1_2(), ProtocolVersion.v1_9_3, ProtocolVersion.v1_9_2);
/*     */     
/* 143 */     registerProtocol((Protocol)new Protocol1_10To1_9_3_4(), ProtocolVersion.v1_10, ProtocolVersion.v1_9_3);
/*     */     
/* 145 */     registerProtocol((Protocol)new Protocol1_11To1_10(), ProtocolVersion.v1_11, ProtocolVersion.v1_10);
/* 146 */     registerProtocol((Protocol)new Protocol1_11_1To1_11(), ProtocolVersion.v1_11_1, ProtocolVersion.v1_11);
/*     */     
/* 148 */     registerProtocol((Protocol)new Protocol1_12To1_11_1(), ProtocolVersion.v1_12, ProtocolVersion.v1_11_1);
/* 149 */     registerProtocol((Protocol)new Protocol1_12_1To1_12(), ProtocolVersion.v1_12_1, ProtocolVersion.v1_12);
/* 150 */     registerProtocol((Protocol)new Protocol1_12_2To1_12_1(), ProtocolVersion.v1_12_2, ProtocolVersion.v1_12_1);
/*     */     
/* 152 */     registerProtocol((Protocol)new Protocol1_13To1_12_2(), ProtocolVersion.v1_13, ProtocolVersion.v1_12_2);
/* 153 */     registerProtocol((Protocol)new Protocol1_13_1To1_13(), ProtocolVersion.v1_13_1, ProtocolVersion.v1_13);
/* 154 */     registerProtocol((Protocol)new Protocol1_13_2To1_13_1(), ProtocolVersion.v1_13_2, ProtocolVersion.v1_13_1);
/*     */     
/* 156 */     registerProtocol((Protocol)new Protocol1_14To1_13_2(), ProtocolVersion.v1_14, ProtocolVersion.v1_13_2);
/* 157 */     registerProtocol((Protocol)new Protocol1_14_1To1_14(), ProtocolVersion.v1_14_1, ProtocolVersion.v1_14);
/* 158 */     registerProtocol((Protocol)new Protocol1_14_2To1_14_1(), ProtocolVersion.v1_14_2, ProtocolVersion.v1_14_1);
/* 159 */     registerProtocol((Protocol)new Protocol1_14_3To1_14_2(), ProtocolVersion.v1_14_3, ProtocolVersion.v1_14_2);
/* 160 */     registerProtocol((Protocol)new Protocol1_14_4To1_14_3(), ProtocolVersion.v1_14_4, ProtocolVersion.v1_14_3);
/*     */     
/* 162 */     registerProtocol((Protocol)new Protocol1_15To1_14_4(), ProtocolVersion.v1_15, ProtocolVersion.v1_14_4);
/* 163 */     registerProtocol((Protocol)new Protocol1_15_1To1_15(), ProtocolVersion.v1_15_1, ProtocolVersion.v1_15);
/* 164 */     registerProtocol((Protocol)new Protocol1_15_2To1_15_1(), ProtocolVersion.v1_15_2, ProtocolVersion.v1_15_1);
/*     */     
/* 166 */     registerProtocol((Protocol)new Protocol1_16To1_15_2(), ProtocolVersion.v1_16, ProtocolVersion.v1_15_2);
/* 167 */     registerProtocol((Protocol)new Protocol1_16_1To1_16(), ProtocolVersion.v1_16_1, ProtocolVersion.v1_16);
/* 168 */     registerProtocol((Protocol)new Protocol1_16_2To1_16_1(), ProtocolVersion.v1_16_2, ProtocolVersion.v1_16_1);
/* 169 */     registerProtocol((Protocol)new Protocol1_16_3To1_16_2(), ProtocolVersion.v1_16_3, ProtocolVersion.v1_16_2);
/* 170 */     registerProtocol((Protocol)new Protocol1_16_4To1_16_3(), ProtocolVersion.v1_16_4, ProtocolVersion.v1_16_3);
/*     */     
/* 172 */     registerProtocol((Protocol)new Protocol1_17To1_16_4(), ProtocolVersion.v1_17, ProtocolVersion.v1_16_4);
/* 173 */     registerProtocol((Protocol)new Protocol1_17_1To1_17(), ProtocolVersion.v1_17_1, ProtocolVersion.v1_17);
/*     */     
/* 175 */     registerProtocol((Protocol)new Protocol1_18To1_17_1(), ProtocolVersion.v1_18, ProtocolVersion.v1_17_1);
/* 176 */     registerProtocol((Protocol)new Protocol1_18_2To1_18(), ProtocolVersion.v1_18_2, ProtocolVersion.v1_18);
/*     */     
/* 178 */     registerProtocol((Protocol)new Protocol1_19To1_18_2(), ProtocolVersion.v1_19, ProtocolVersion.v1_18_2);
/* 179 */     registerProtocol((Protocol)new Protocol1_19_1To1_19(), ProtocolVersion.v1_19_1, ProtocolVersion.v1_19);
/* 180 */     registerProtocol((Protocol)new Protocol1_19_3To1_19_1(), ProtocolVersion.v1_19_3, ProtocolVersion.v1_19_1);
/* 181 */     registerProtocol((Protocol)new Protocol1_19_4To1_19_3(), ProtocolVersion.v1_19_4, ProtocolVersion.v1_19_3);
/*     */     
/* 183 */     registerProtocol((Protocol)new Protocol1_20To1_19_4(), ProtocolVersion.v1_20, ProtocolVersion.v1_19_4);
/* 184 */     registerProtocol((Protocol)new Protocol1_20_2To1_20(), ProtocolVersion.v1_20_2, ProtocolVersion.v1_20);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerProtocol(Protocol protocol, ProtocolVersion clientVersion, ProtocolVersion serverVersion) {
/* 189 */     registerProtocol(protocol, Collections.singletonList(Integer.valueOf(clientVersion.getVersion())), serverVersion.getVersion());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerProtocol(Protocol<?, ?, ?, ?> protocol, List<Integer> supportedClientVersion, int serverVersion) {
/* 195 */     protocol.initialize();
/*     */ 
/*     */     
/* 198 */     if (!this.pathCache.isEmpty()) {
/* 199 */       this.pathCache.clear();
/*     */     }
/*     */     
/* 202 */     this.protocols.put(protocol.getClass(), protocol);
/*     */     
/* 204 */     for (Iterator<Integer> iterator = supportedClientVersion.iterator(); iterator.hasNext(); ) { int clientVersion = ((Integer)iterator.next()).intValue();
/*     */       
/* 206 */       Preconditions.checkArgument((clientVersion != serverVersion));
/*     */       
/* 208 */       Int2ObjectMap<Protocol> protocolMap = (Int2ObjectMap<Protocol>)this.registryMap.computeIfAbsent(clientVersion, s -> new Int2ObjectOpenHashMap(2));
/* 209 */       protocolMap.put(serverVersion, protocol); }
/*     */ 
/*     */     
/* 212 */     protocol.register(Via.getManager().getProviders());
/* 213 */     if (Via.getManager().isInitialized()) {
/* 214 */       refreshVersions();
/*     */     }
/*     */     
/* 217 */     if (protocol.hasMappingDataToLoad()) {
/* 218 */       if (this.mappingLoaderExecutor != null) {
/*     */         
/* 220 */         addMappingLoaderFuture((Class)protocol.getClass(), protocol::loadMappingData);
/*     */       } else {
/*     */         
/* 223 */         protocol.loadMappingData();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerBaseProtocol(Protocol baseProtocol, Range<Integer> supportedProtocols) {
/* 230 */     Preconditions.checkArgument(baseProtocol.isBaseProtocol(), "Protocol is not a base protocol");
/* 231 */     baseProtocol.initialize();
/*     */     
/* 233 */     this.baseProtocols.add(new Pair(supportedProtocols, baseProtocol));
/* 234 */     baseProtocol.register(Via.getManager().getProviders());
/* 235 */     if (Via.getManager().isInitialized()) {
/* 236 */       refreshVersions();
/*     */     }
/*     */   }
/*     */   
/*     */   public void refreshVersions() {
/* 241 */     this.supportedVersions.clear();
/*     */     
/* 243 */     this.supportedVersions.add(Integer.valueOf(this.serverProtocolVersion.lowestSupportedVersion()));
/* 244 */     for (ProtocolVersion version : ProtocolVersion.getProtocols()) {
/* 245 */       List<ProtocolPathEntry> protocolPath = getProtocolPath(version.getVersion(), this.serverProtocolVersion.lowestSupportedVersion());
/* 246 */       if (protocolPath == null)
/*     */         continue; 
/* 248 */       this.supportedVersions.add(Integer.valueOf(version.getVersion()));
/* 249 */       for (ProtocolPathEntry pathEntry : protocolPath) {
/* 250 */         this.supportedVersions.add(Integer.valueOf(pathEntry.outputProtocolVersion()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ProtocolPathEntry> getProtocolPath(int clientVersion, int serverVersion) {
/* 257 */     if (clientVersion == serverVersion) return null;
/*     */ 
/*     */     
/* 260 */     ProtocolPathKey protocolKey = new ProtocolPathKeyImpl(clientVersion, serverVersion);
/* 261 */     List<ProtocolPathEntry> protocolList = this.pathCache.get(protocolKey);
/* 262 */     if (protocolList != null) {
/* 263 */       return protocolList;
/*     */     }
/*     */ 
/*     */     
/* 267 */     Int2ObjectSortedMap<Protocol> outputPath = getProtocolPath((Int2ObjectSortedMap<Protocol>)new Int2ObjectLinkedOpenHashMap(), clientVersion, serverVersion);
/* 268 */     if (outputPath == null) {
/* 269 */       return null;
/*     */     }
/*     */     
/* 272 */     List<ProtocolPathEntry> path = new ArrayList<>(outputPath.size());
/* 273 */     for (ObjectBidirectionalIterator<Int2ObjectMap.Entry<Protocol>> objectBidirectionalIterator = outputPath.int2ObjectEntrySet().iterator(); objectBidirectionalIterator.hasNext(); ) { Int2ObjectMap.Entry<Protocol> entry = objectBidirectionalIterator.next();
/* 274 */       path.add(new ProtocolPathEntryImpl(entry.getIntKey(), (Protocol<?, ?, ?, ?>)entry.getValue())); }
/*     */     
/* 276 */     this.pathCache.put(protocolKey, path);
/* 277 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <C extends ClientboundPacketType, S extends ServerboundPacketType> VersionedPacketTransformer<C, S> createPacketTransformer(ProtocolVersion inputVersion, Class<C> clientboundPacketsClass, Class<S> serverboundPacketsClass) {
/* 286 */     Preconditions.checkArgument((clientboundPacketsClass != ClientboundPacketType.class && serverboundPacketsClass != ServerboundPacketType.class));
/* 287 */     return (VersionedPacketTransformer<C, S>)new VersionedPacketTransformerImpl(inputVersion, clientboundPacketsClass, serverboundPacketsClass);
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
/*     */   private Int2ObjectSortedMap<Protocol> getProtocolPath(Int2ObjectSortedMap<Protocol> current, int clientVersion, int serverVersion) {
/* 299 */     if (current.size() > this.maxProtocolPathSize) return null;
/*     */ 
/*     */     
/* 302 */     Int2ObjectMap<Protocol> toServerProtocolMap = (Int2ObjectMap<Protocol>)this.registryMap.get(clientVersion);
/* 303 */     if (toServerProtocolMap == null) {
/* 304 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 308 */     Protocol protocol = (Protocol)toServerProtocolMap.get(serverVersion);
/* 309 */     if (protocol != null) {
/* 310 */       current.put(serverVersion, protocol);
/* 311 */       return current;
/*     */     } 
/*     */ 
/*     */     
/* 315 */     Int2ObjectSortedMap<Protocol> shortest = null;
/* 316 */     for (ObjectIterator<Int2ObjectMap.Entry<Protocol>> objectIterator = toServerProtocolMap.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry<Protocol> entry = objectIterator.next();
/*     */       
/* 318 */       int translatedToVersion = entry.getIntKey();
/* 319 */       if (current.containsKey(translatedToVersion)) {
/*     */         continue;
/*     */       }
/* 322 */       if (this.maxPathDeltaIncrease != -1 && Math.abs(serverVersion - translatedToVersion) - Math.abs(serverVersion - clientVersion) > this.maxPathDeltaIncrease) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 327 */       Int2ObjectLinkedOpenHashMap int2ObjectLinkedOpenHashMap = new Int2ObjectLinkedOpenHashMap((Int2ObjectMap)current);
/* 328 */       int2ObjectLinkedOpenHashMap.put(translatedToVersion, entry.getValue());
/*     */ 
/*     */       
/* 331 */       Int2ObjectSortedMap<Protocol> int2ObjectSortedMap = getProtocolPath((Int2ObjectSortedMap<Protocol>)int2ObjectLinkedOpenHashMap, translatedToVersion, serverVersion);
/* 332 */       if (int2ObjectSortedMap != null && (shortest == null || int2ObjectSortedMap.size() < shortest.size())) {
/* 333 */         shortest = int2ObjectSortedMap;
/*     */       } }
/*     */ 
/*     */     
/* 337 */     return shortest;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Protocol> T getProtocol(Class<T> protocolClass) {
/* 342 */     return (T)this.protocols.get(protocolClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public Protocol getProtocol(int clientVersion, int serverVersion) {
/* 347 */     Int2ObjectMap<Protocol> map = (Int2ObjectMap<Protocol>)this.registryMap.get(clientVersion);
/* 348 */     return (map != null) ? (Protocol)map.get(serverVersion) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Protocol getBaseProtocol(int serverVersion) {
/* 353 */     for (Pair<Range<Integer>, Protocol> rangeProtocol : (Iterable<Pair<Range<Integer>, Protocol>>)Lists.reverse(this.baseProtocols)) {
/* 354 */       if (((Range)rangeProtocol.key()).contains(Integer.valueOf(serverVersion))) {
/* 355 */         return (Protocol)rangeProtocol.value();
/*     */       }
/*     */     } 
/* 358 */     throw new IllegalStateException("No Base Protocol for " + serverVersion);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Protocol<?, ?, ?, ?>> getProtocols() {
/* 363 */     return Collections.unmodifiableCollection(this.protocols.values());
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerProtocolVersion getServerProtocolVersion() {
/* 368 */     return this.serverProtocolVersion;
/*     */   }
/*     */   
/*     */   public void setServerProtocol(ServerProtocolVersion serverProtocolVersion) {
/* 372 */     this.serverProtocolVersion = serverProtocolVersion;
/*     */     
/* 374 */     ProtocolRegistry.SERVER_PROTOCOL = serverProtocolVersion.lowestSupportedVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWorkingPipe() {
/* 379 */     for (ObjectIterator<Int2ObjectMap<Protocol>> objectIterator = this.registryMap.values().iterator(); objectIterator.hasNext(); ) { Int2ObjectMap<Protocol> map = objectIterator.next();
/* 380 */       for (IntBidirectionalIterator<Integer> intBidirectionalIterator = this.serverProtocolVersion.supportedVersions().iterator(); intBidirectionalIterator.hasNext(); ) { int protocolVersion = ((Integer)intBidirectionalIterator.next()).intValue();
/* 381 */         if (map.containsKey(protocolVersion)) {
/* 382 */           return true;
/*     */         } }
/*     */        }
/*     */     
/* 386 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<Integer> getSupportedVersions() {
/* 391 */     return Collections.unmodifiableSortedSet(new TreeSet<>(this.supportedVersions));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPathDeltaIncrease(int maxPathDeltaIncrease) {
/* 396 */     this.maxPathDeltaIncrease = Math.max(-1, maxPathDeltaIncrease);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPathDeltaIncrease() {
/* 401 */     return this.maxPathDeltaIncrease;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxProtocolPathSize() {
/* 406 */     return this.maxProtocolPathSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxProtocolPathSize(int maxProtocolPathSize) {
/* 411 */     this.maxProtocolPathSize = maxProtocolPathSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public Protocol getBaseProtocol() {
/* 416 */     return BASE_PROTOCOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public void completeMappingDataLoading(Class<? extends Protocol> protocolClass) throws Exception {
/* 421 */     if (this.mappingsLoaded)
/*     */       return; 
/* 423 */     CompletableFuture<Void> future = getMappingLoaderFuture(protocolClass);
/* 424 */     if (future != null)
/*     */     {
/* 426 */       future.get();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkForMappingCompletion() {
/* 432 */     this.mappingLoaderLock.readLock().lock();
/*     */     try {
/* 434 */       if (this.mappingsLoaded) {
/* 435 */         return false;
/*     */       }
/*     */       
/* 438 */       for (CompletableFuture<Void> future : this.mappingLoaderFutures.values()) {
/*     */         
/* 440 */         if (!future.isDone()) {
/* 441 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 445 */       shutdownLoaderExecutor();
/* 446 */       return true;
/*     */     } finally {
/* 448 */       this.mappingLoaderLock.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMappingLoaderFuture(Class<? extends Protocol> protocolClass, Runnable runnable) {
/* 454 */     CompletableFuture<Void> future = CompletableFuture.runAsync(runnable, this.mappingLoaderExecutor).exceptionally(mappingLoaderThrowable(protocolClass));
/*     */     
/* 456 */     this.mappingLoaderLock.writeLock().lock();
/*     */     try {
/* 458 */       this.mappingLoaderFutures.put(protocolClass, future);
/*     */     } finally {
/* 460 */       this.mappingLoaderLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMappingLoaderFuture(Class<? extends Protocol> protocolClass, Class<? extends Protocol> dependsOn, Runnable runnable) {
/* 467 */     CompletableFuture<Void> future = getMappingLoaderFuture(dependsOn).whenCompleteAsync((v, throwable) -> runnable.run(), this.mappingLoaderExecutor).exceptionally(mappingLoaderThrowable(protocolClass));
/*     */     
/* 469 */     this.mappingLoaderLock.writeLock().lock();
/*     */     try {
/* 471 */       this.mappingLoaderFutures.put(protocolClass, future);
/*     */     } finally {
/* 473 */       this.mappingLoaderLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> getMappingLoaderFuture(Class<? extends Protocol> protocolClass) {
/* 479 */     this.mappingLoaderLock.readLock().lock();
/*     */     try {
/* 481 */       return this.mappingsLoaded ? null : this.mappingLoaderFutures.get(protocolClass);
/*     */     } finally {
/* 483 */       this.mappingLoaderLock.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketWrapper createPacketWrapper(PacketType packetType, ByteBuf buf, UserConnection connection) {
/* 489 */     return (PacketWrapper)new PacketWrapperImpl(packetType, buf, connection);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public PacketWrapper createPacketWrapper(int packetId, ByteBuf buf, UserConnection connection) {
/* 495 */     return (PacketWrapper)new PacketWrapperImpl(packetId, buf, connection);
/*     */   }
/*     */   
/*     */   public void shutdownLoaderExecutor() {
/* 499 */     Preconditions.checkArgument(!this.mappingsLoaded);
/*     */ 
/*     */     
/* 502 */     Via.getPlatform().getLogger().info("Finished mapping loading, shutting down loader executor!");
/* 503 */     this.mappingsLoaded = true;
/* 504 */     this.mappingLoaderExecutor.shutdown();
/* 505 */     this.mappingLoaderExecutor = null;
/* 506 */     this.mappingLoaderFutures.clear();
/* 507 */     this.mappingLoaderFutures = null;
/*     */ 
/*     */     
/* 510 */     MappingDataLoader.clearCache();
/*     */   }
/*     */   
/*     */   private Function<Throwable, Void> mappingLoaderThrowable(Class<? extends Protocol> protocolClass) {
/* 514 */     return throwable -> {
/*     */         Via.getPlatform().getLogger().severe("Error during mapping loading of " + protocolClass.getSimpleName());
/*     */         throwable.printStackTrace();
/*     */         return null;
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocol\ProtocolManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */