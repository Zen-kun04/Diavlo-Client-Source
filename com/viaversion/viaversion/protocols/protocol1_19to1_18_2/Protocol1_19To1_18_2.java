/*     */ package com.viaversion.viaversion.protocols.protocol1_19to1_18_2;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.StorableObject;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.MappingData;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_19Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.platform.providers.Provider;
/*     */ import com.viaversion.viaversion.api.platform.providers.ViaProviders;
/*     */ import com.viaversion.viaversion.api.protocol.AbstractProtocol;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.rewriter.EntityRewriter;
/*     */ import com.viaversion.viaversion.api.rewriter.ItemRewriter;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_19;
/*     */ import com.viaversion.viaversion.data.entity.EntityTrackerBase;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
/*     */ import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
/*     */ import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
/*     */ import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.data.MappingData;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets.EntityPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets.InventoryPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets.WorldPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.provider.AckSequenceProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.storage.DimensionRegistryStorage;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.storage.NonceStorage;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.storage.SequenceStorage;
/*     */ import com.viaversion.viaversion.rewriter.CommandRewriter;
/*     */ import com.viaversion.viaversion.rewriter.SoundRewriter;
/*     */ import com.viaversion.viaversion.rewriter.StatisticsRewriter;
/*     */ import com.viaversion.viaversion.rewriter.TagRewriter;
/*     */ import com.viaversion.viaversion.util.CipherUtil;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Protocol1_19To1_18_2
/*     */   extends AbstractProtocol<ClientboundPackets1_18, ClientboundPackets1_19, ServerboundPackets1_17, ServerboundPackets1_19>
/*     */ {
/*  55 */   public static final MappingData MAPPINGS = new MappingData();
/*  56 */   private final EntityPackets entityRewriter = new EntityPackets(this);
/*  57 */   private final InventoryPackets itemRewriter = new InventoryPackets(this);
/*     */   
/*     */   public Protocol1_19To1_18_2() {
/*  60 */     super(ClientboundPackets1_18.class, ClientboundPackets1_19.class, ServerboundPackets1_17.class, ServerboundPackets1_19.class);
/*     */   }
/*     */   
/*     */   public static boolean isTextComponentNull(JsonElement element) {
/*  64 */     return (element == null || element.isJsonNull() || (element.isJsonArray() && element.getAsJsonArray().size() == 0));
/*     */   }
/*     */   
/*     */   public static JsonElement mapTextComponentIfNull(JsonElement component) {
/*  68 */     if (!isTextComponentNull(component)) {
/*  69 */       return component;
/*     */     }
/*  71 */     return (JsonElement)ChatRewriter.emptyComponent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/*  77 */     TagRewriter<ClientboundPackets1_18> tagRewriter = new TagRewriter((Protocol)this);
/*  78 */     tagRewriter.registerGeneric((ClientboundPacketType)ClientboundPackets1_18.TAGS);
/*     */     
/*  80 */     this.entityRewriter.register();
/*  81 */     this.itemRewriter.register();
/*  82 */     WorldPackets.register(this);
/*     */     
/*  84 */     cancelClientbound((ClientboundPacketType)ClientboundPackets1_18.ADD_VIBRATION_SIGNAL);
/*     */     
/*  86 */     final SoundRewriter<ClientboundPackets1_18> soundRewriter = new SoundRewriter((Protocol)this);
/*  87 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.SOUND, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  90 */             map((Type)Type.VAR_INT);
/*  91 */             map((Type)Type.VAR_INT);
/*  92 */             map((Type)Type.INT);
/*  93 */             map((Type)Type.INT);
/*  94 */             map((Type)Type.INT);
/*  95 */             map((Type)Type.FLOAT);
/*  96 */             map((Type)Type.FLOAT);
/*  97 */             handler(wrapper -> wrapper.write((Type)Type.LONG, Long.valueOf(Protocol1_19To1_18_2.randomLong())));
/*  98 */             handler(soundRewriter.getSoundHandler());
/*     */           }
/*     */         });
/* 101 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.ENTITY_SOUND, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 104 */             map((Type)Type.VAR_INT);
/* 105 */             map((Type)Type.VAR_INT);
/* 106 */             map((Type)Type.VAR_INT);
/* 107 */             map((Type)Type.FLOAT);
/* 108 */             map((Type)Type.FLOAT);
/* 109 */             handler(wrapper -> wrapper.write((Type)Type.LONG, Long.valueOf(Protocol1_19To1_18_2.randomLong())));
/* 110 */             handler(soundRewriter.getSoundHandler());
/*     */           }
/*     */         });
/* 113 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.NAMED_SOUND, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 116 */             map(Type.STRING);
/* 117 */             map((Type)Type.VAR_INT);
/* 118 */             map((Type)Type.INT);
/* 119 */             map((Type)Type.INT);
/* 120 */             map((Type)Type.INT);
/* 121 */             map((Type)Type.FLOAT);
/* 122 */             map((Type)Type.FLOAT);
/* 123 */             handler(wrapper -> wrapper.write((Type)Type.LONG, Long.valueOf(Protocol1_19To1_18_2.randomLong())));
/*     */           }
/*     */         });
/*     */     
/* 127 */     (new StatisticsRewriter((Protocol)this)).register((ClientboundPacketType)ClientboundPackets1_18.STATISTICS);
/*     */     
/* 129 */     PacketHandler singleNullTextComponentMapper = wrapper -> wrapper.write(Type.COMPONENT, mapTextComponentIfNull((JsonElement)wrapper.read(Type.COMPONENT)));
/*     */ 
/*     */     
/* 132 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.TITLE_TEXT, singleNullTextComponentMapper);
/* 133 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.TITLE_SUBTITLE, singleNullTextComponentMapper);
/* 134 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.ACTIONBAR, singleNullTextComponentMapper);
/* 135 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.SCOREBOARD_OBJECTIVE, wrapper -> {
/*     */           wrapper.passthrough(Type.STRING);
/*     */           byte action = ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue();
/*     */           if (action == 0 || action == 2) {
/*     */             wrapper.write(Type.COMPONENT, mapTextComponentIfNull((JsonElement)wrapper.read(Type.COMPONENT)));
/*     */           }
/*     */         });
/* 142 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.TEAMS, wrapper -> {
/*     */           wrapper.passthrough(Type.STRING);
/*     */           
/*     */           byte action = ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue();
/*     */           if (action == 0 || action == 2) {
/*     */             wrapper.write(Type.COMPONENT, mapTextComponentIfNull((JsonElement)wrapper.read(Type.COMPONENT)));
/*     */             wrapper.passthrough((Type)Type.BYTE);
/*     */             wrapper.passthrough(Type.STRING);
/*     */             wrapper.passthrough(Type.STRING);
/*     */             wrapper.passthrough((Type)Type.VAR_INT);
/*     */             wrapper.write(Type.COMPONENT, mapTextComponentIfNull((JsonElement)wrapper.read(Type.COMPONENT)));
/*     */             wrapper.write(Type.COMPONENT, mapTextComponentIfNull((JsonElement)wrapper.read(Type.COMPONENT)));
/*     */           } 
/*     */         });
/* 156 */     CommandRewriter<ClientboundPackets1_18> commandRewriter = new CommandRewriter((Protocol)this);
/* 157 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.DECLARE_COMMANDS, wrapper -> {
/*     */           int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           
/*     */           for (int i = 0; i < size; i++) {
/*     */             byte flags = ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue();
/*     */             
/*     */             wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
/*     */             
/*     */             if ((flags & 0x8) != 0) {
/*     */               wrapper.passthrough((Type)Type.VAR_INT);
/*     */             }
/*     */             
/*     */             int nodeType = flags & 0x3;
/*     */             
/*     */             if (nodeType == 1 || nodeType == 2) {
/*     */               wrapper.passthrough(Type.STRING);
/*     */             }
/*     */             
/*     */             if (nodeType == 2) {
/*     */               String argumentType = (String)wrapper.read(Type.STRING);
/*     */               
/*     */               int argumentTypeId = MAPPINGS.getArgumentTypeMappings().mappedId(argumentType);
/*     */               if (argumentTypeId == -1) {
/*     */                 Via.getPlatform().getLogger().warning("Unknown command argument type: " + argumentType);
/*     */               }
/*     */               wrapper.write((Type)Type.VAR_INT, Integer.valueOf(argumentTypeId));
/*     */               commandRewriter.handleArgument(wrapper, argumentType);
/*     */               if ((flags & 0x10) != 0) {
/*     */                 wrapper.passthrough(Type.STRING);
/*     */               }
/*     */             } 
/*     */           } 
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */         });
/* 191 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_18.CHAT_MESSAGE, ClientboundPackets1_19.SYSTEM_CHAT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 194 */             map(Type.COMPONENT);
/* 195 */             handler(wrapper -> {
/*     */                   int type = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */                   wrapper.write((Type)Type.VAR_INT, Integer.valueOf((type == 0) ? 1 : type));
/*     */                 });
/* 199 */             read(Type.UUID);
/*     */           }
/*     */         });
/*     */     
/* 203 */     registerServerbound(ServerboundPackets1_19.CHAT_MESSAGE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 206 */             map(Type.STRING);
/* 207 */             read((Type)Type.LONG);
/* 208 */             read((Type)Type.LONG);
/* 209 */             read(Type.BYTE_ARRAY_PRIMITIVE);
/* 210 */             read((Type)Type.BOOLEAN);
/*     */           }
/*     */         });
/* 213 */     registerServerbound(ServerboundPackets1_19.CHAT_COMMAND, (ServerboundPacketType)ServerboundPackets1_17.CHAT_MESSAGE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 216 */             map(Type.STRING);
/* 217 */             read((Type)Type.LONG);
/* 218 */             read((Type)Type.LONG);
/* 219 */             handler(wrapper -> {
/*     */                   String command = (String)wrapper.get(Type.STRING, 0);
/*     */                   
/*     */                   wrapper.set(Type.STRING, 0, "/" + command);
/*     */                   int signatures = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                   for (int i = 0; i < signatures; i++) {
/*     */                     wrapper.read(Type.STRING);
/*     */                     wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
/*     */                   } 
/*     */                 });
/* 229 */             read((Type)Type.BOOLEAN);
/*     */           }
/*     */         });
/* 232 */     cancelServerbound(ServerboundPackets1_19.CHAT_PREVIEW);
/*     */ 
/*     */     
/* 235 */     registerClientbound(State.LOGIN, ClientboundLoginPackets.GAME_PROFILE.getId(), ClientboundLoginPackets.GAME_PROFILE.getId(), (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 238 */             map(Type.UUID);
/* 239 */             map(Type.STRING);
/* 240 */             create((Type)Type.VAR_INT, Integer.valueOf(0));
/*     */           }
/*     */         });
/*     */     
/* 244 */     registerClientbound(State.LOGIN, ClientboundLoginPackets.HELLO.getId(), ClientboundLoginPackets.HELLO.getId(), (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 247 */             map(Type.STRING);
/* 248 */             handler(wrapper -> {
/*     */                   byte[] publicKey = (byte[])wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
/*     */                   
/*     */                   byte[] nonce = (byte[])wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
/*     */                   wrapper.user().put((StorableObject)new NonceStorage(CipherUtil.encryptNonce(publicKey, nonce)));
/*     */                 });
/*     */           }
/*     */         });
/* 256 */     registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO.getId(), ServerboundLoginPackets.HELLO.getId(), (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 259 */             map(Type.STRING);
/* 260 */             read(Type.OPTIONAL_PROFILE_KEY);
/*     */           }
/*     */         });
/*     */     
/* 264 */     registerServerbound(State.LOGIN, ServerboundLoginPackets.ENCRYPTION_KEY.getId(), ServerboundLoginPackets.ENCRYPTION_KEY.getId(), (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 267 */             map(Type.BYTE_ARRAY_PRIMITIVE);
/* 268 */             handler(wrapper -> {
/*     */                   if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                     wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
/*     */                   } else {
/*     */                     NonceStorage nonceStorage = (NonceStorage)wrapper.user().remove(NonceStorage.class);
/*     */                     if (nonceStorage == null) {
/*     */                       throw new IllegalArgumentException("Server sent nonce is missing");
/*     */                     }
/*     */                     wrapper.read((Type)Type.LONG);
/*     */                     wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
/*     */                     wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, nonceStorage.nonce());
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long randomLong() {
/* 289 */     return ThreadLocalRandom.current().nextLong();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onMappingDataLoaded() {
/* 294 */     super.onMappingDataLoaded();
/* 295 */     Types1_19.PARTICLE.filler((Protocol)this)
/* 296 */       .reader("block", ParticleType.Readers.BLOCK)
/* 297 */       .reader("block_marker", ParticleType.Readers.BLOCK)
/* 298 */       .reader("dust", ParticleType.Readers.DUST)
/* 299 */       .reader("falling_dust", ParticleType.Readers.BLOCK)
/* 300 */       .reader("dust_color_transition", ParticleType.Readers.DUST_TRANSITION)
/* 301 */       .reader("item", ParticleType.Readers.VAR_INT_ITEM)
/* 302 */       .reader("vibration", ParticleType.Readers.VIBRATION1_19)
/* 303 */       .reader("sculk_charge", ParticleType.Readers.SCULK_CHARGE)
/* 304 */       .reader("shriek", ParticleType.Readers.SHRIEK);
/* 305 */     Entity1_19Types.initialize((Protocol)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void register(ViaProviders providers) {
/* 310 */     providers.register(AckSequenceProvider.class, (Provider)new AckSequenceProvider());
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(UserConnection user) {
/* 315 */     if (!user.has(DimensionRegistryStorage.class)) {
/* 316 */       user.put((StorableObject)new DimensionRegistryStorage());
/*     */     }
/* 318 */     user.put((StorableObject)new SequenceStorage());
/* 319 */     addEntityTracker(user, (EntityTracker)new EntityTrackerBase(user, (EntityType)Entity1_19Types.PLAYER));
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingData getMappingData() {
/* 324 */     return MAPPINGS;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityPackets getEntityRewriter() {
/* 329 */     return this.entityRewriter;
/*     */   }
/*     */ 
/*     */   
/*     */   public InventoryPackets getItemRewriter() {
/* 334 */     return this.itemRewriter;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_19to1_18_2\Protocol1_19To1_18_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */