/*     */ package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets;
/*     */ 
/*     */ import com.viaversion.viabackwards.ViaBackwards;
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.entities.storage.EntityData;
/*     */ import com.viaversion.viabackwards.api.entities.storage.EntityPositionHandler;
/*     */ import com.viaversion.viabackwards.api.rewriters.EntityRewriterBase;
/*     */ import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.DifficultyStorage;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.EntityPositionStorage1_14;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.minecraft.Position;
/*     */ import com.viaversion.viaversion.api.minecraft.VillagerData;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.Particle;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_13_2;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_14;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
/*     */ import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaHandler;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityPackets1_14
/*     */   extends LegacyEntityRewriter<ClientboundPackets1_14, Protocol1_13_2To1_14>
/*     */ {
/*     */   private EntityPositionHandler positionHandler;
/*     */   
/*     */   public EntityPackets1_14(Protocol1_13_2To1_14 protocol) {
/*  51 */     super((BackwardsProtocol)protocol, Types1_13_2.META_TYPES.optionalComponentType, Types1_13_2.META_TYPES.booleanType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTrackedEntity(PacketWrapper wrapper, int entityId, EntityType type) throws Exception {
/*  57 */     super.addTrackedEntity(wrapper, entityId, type);
/*     */ 
/*     */     
/*  60 */     if (type == Entity1_14Types.PAINTING) {
/*  61 */       Position position = (Position)wrapper.get(Type.POSITION, 0);
/*  62 */       this.positionHandler.cacheEntityPosition(wrapper, position.x(), position.y(), position.z(), true, false);
/*  63 */     } else if (wrapper.getId() != ClientboundPackets1_14.JOIN_GAME.getId()) {
/*  64 */       this.positionHandler.cacheEntityPosition(wrapper, true, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/*  70 */     this.positionHandler = new EntityPositionHandler((EntityRewriterBase)this, EntityPositionStorage1_14.class, EntityPositionStorage1_14::new);
/*     */     
/*  72 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.ENTITY_STATUS, wrapper -> {
/*     */           int entityId = ((Integer)wrapper.passthrough((Type)Type.INT)).intValue();
/*     */           
/*     */           byte status = ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue();
/*     */           if (status != 3) {
/*     */             return;
/*     */           }
/*     */           EntityTracker tracker = tracker(wrapper.user());
/*     */           EntityType entityType = tracker.entityType(entityId);
/*     */           if (entityType != Entity1_14Types.PLAYER) {
/*     */             return;
/*     */           }
/*     */           for (int i = 0; i <= 5; i++) {
/*     */             PacketWrapper equipmentPacket = wrapper.create((PacketType)ClientboundPackets1_13.ENTITY_EQUIPMENT);
/*     */             equipmentPacket.write((Type)Type.VAR_INT, Integer.valueOf(entityId));
/*     */             equipmentPacket.write((Type)Type.VAR_INT, Integer.valueOf(i));
/*     */             equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, null);
/*     */             equipmentPacket.send(Protocol1_13_2To1_14.class);
/*     */           } 
/*     */         });
/*  92 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.ENTITY_TELEPORT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  95 */             map((Type)Type.VAR_INT);
/*  96 */             map((Type)Type.DOUBLE);
/*  97 */             map((Type)Type.DOUBLE);
/*  98 */             map((Type)Type.DOUBLE);
/*  99 */             handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, false, false));
/*     */           }
/*     */         });
/*     */     
/* 103 */     PacketHandlers relativeMoveHandler = new PacketHandlers()
/*     */       {
/*     */         public void register() {
/* 106 */           map((Type)Type.VAR_INT);
/* 107 */           map((Type)Type.SHORT);
/* 108 */           map((Type)Type.SHORT);
/* 109 */           map((Type)Type.SHORT);
/* 110 */           handler(wrapper -> {
/*     */                 double x = ((Short)wrapper.get((Type)Type.SHORT, 0)).shortValue() / 4096.0D;
/*     */                 double y = ((Short)wrapper.get((Type)Type.SHORT, 1)).shortValue() / 4096.0D;
/*     */                 double z = ((Short)wrapper.get((Type)Type.SHORT, 2)).shortValue() / 4096.0D;
/*     */                 EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, x, y, z, false, true);
/*     */               });
/*     */         }
/*     */       };
/* 118 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.ENTITY_POSITION, (PacketHandler)relativeMoveHandler);
/* 119 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.ENTITY_POSITION_AND_ROTATION, (PacketHandler)relativeMoveHandler);
/*     */     
/* 121 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.SPAWN_ENTITY, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 124 */             map((Type)Type.VAR_INT);
/* 125 */             map(Type.UUID);
/* 126 */             map((Type)Type.VAR_INT, (Type)Type.BYTE);
/* 127 */             map((Type)Type.DOUBLE);
/* 128 */             map((Type)Type.DOUBLE);
/* 129 */             map((Type)Type.DOUBLE);
/* 130 */             map((Type)Type.BYTE);
/* 131 */             map((Type)Type.BYTE);
/* 132 */             map((Type)Type.INT);
/* 133 */             map((Type)Type.SHORT);
/* 134 */             map((Type)Type.SHORT);
/* 135 */             map((Type)Type.SHORT);
/*     */             
/* 137 */             handler(EntityPackets1_14.this.getObjectTrackerHandler());
/*     */             
/* 139 */             handler(wrapper -> {
/*     */                   // Byte code:
/*     */                   //   0: aload_1
/*     */                   //   1: getstatic com/viaversion/viaversion/api/type/Type.BYTE : Lcom/viaversion/viaversion/api/type/types/ByteType;
/*     */                   //   4: iconst_0
/*     */                   //   5: invokeinterface get : (Lcom/viaversion/viaversion/api/type/Type;I)Ljava/lang/Object;
/*     */                   //   10: checkcast java/lang/Byte
/*     */                   //   13: invokevirtual byteValue : ()B
/*     */                   //   16: istore_2
/*     */                   //   17: aload_0
/*     */                   //   18: getfield this$0 : Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14;
/*     */                   //   21: iload_2
/*     */                   //   22: invokevirtual newEntityId : (I)I
/*     */                   //   25: istore_3
/*     */                   //   26: iload_3
/*     */                   //   27: iconst_0
/*     */                   //   28: invokestatic getTypeFromId : (IZ)Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$EntityType;
/*     */                   //   31: astore #4
/*     */                   //   33: aload #4
/*     */                   //   35: getstatic com/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$EntityType.MINECART_ABSTRACT : Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$EntityType;
/*     */                   //   38: invokevirtual isOrHasParent : (Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Z
/*     */                   //   41: ifeq -> 157
/*     */                   //   44: getstatic com/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$ObjectType.MINECART : Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$ObjectType;
/*     */                   //   47: astore #5
/*     */                   //   49: iconst_0
/*     */                   //   50: istore #6
/*     */                   //   52: getstatic com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14$11.$SwitchMap$com$viaversion$viaversion$api$minecraft$entities$Entity1_13Types$EntityType : [I
/*     */                   //   55: aload #4
/*     */                   //   57: invokevirtual ordinal : ()I
/*     */                   //   60: iaload
/*     */                   //   61: tableswitch default -> 134, 1 -> 100, 2 -> 106, 3 -> 112, 4 -> 118, 5 -> 124, 6 -> 130
/*     */                   //   100: iconst_1
/*     */                   //   101: istore #6
/*     */                   //   103: goto -> 134
/*     */                   //   106: iconst_2
/*     */                   //   107: istore #6
/*     */                   //   109: goto -> 134
/*     */                   //   112: iconst_3
/*     */                   //   113: istore #6
/*     */                   //   115: goto -> 134
/*     */                   //   118: iconst_4
/*     */                   //   119: istore #6
/*     */                   //   121: goto -> 134
/*     */                   //   124: iconst_5
/*     */                   //   125: istore #6
/*     */                   //   127: goto -> 134
/*     */                   //   130: bipush #6
/*     */                   //   132: istore #6
/*     */                   //   134: iload #6
/*     */                   //   136: ifeq -> 154
/*     */                   //   139: aload_1
/*     */                   //   140: getstatic com/viaversion/viaversion/api/type/Type.INT : Lcom/viaversion/viaversion/api/type/types/IntType;
/*     */                   //   143: iconst_0
/*     */                   //   144: iload #6
/*     */                   //   146: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */                   //   149: invokeinterface set : (Lcom/viaversion/viaversion/api/type/Type;ILjava/lang/Object;)V
/*     */                   //   154: goto -> 171
/*     */                   //   157: aload #4
/*     */                   //   159: invokestatic fromEntityType : (Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$EntityType;)Ljava/util/Optional;
/*     */                   //   162: aconst_null
/*     */                   //   163: invokevirtual orElse : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */                   //   166: checkcast com/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$ObjectType
/*     */                   //   169: astore #5
/*     */                   //   171: aload #5
/*     */                   //   173: ifnonnull -> 177
/*     */                   //   176: return
/*     */                   //   177: aload_1
/*     */                   //   178: getstatic com/viaversion/viaversion/api/type/Type.BYTE : Lcom/viaversion/viaversion/api/type/types/ByteType;
/*     */                   //   181: iconst_0
/*     */                   //   182: aload #5
/*     */                   //   184: invokevirtual getId : ()I
/*     */                   //   187: i2b
/*     */                   //   188: invokestatic valueOf : (B)Ljava/lang/Byte;
/*     */                   //   191: invokeinterface set : (Lcom/viaversion/viaversion/api/type/Type;ILjava/lang/Object;)V
/*     */                   //   196: aload_1
/*     */                   //   197: getstatic com/viaversion/viaversion/api/type/Type.INT : Lcom/viaversion/viaversion/api/type/types/IntType;
/*     */                   //   200: iconst_0
/*     */                   //   201: invokeinterface get : (Lcom/viaversion/viaversion/api/type/Type;I)Ljava/lang/Object;
/*     */                   //   206: checkcast java/lang/Integer
/*     */                   //   209: invokevirtual intValue : ()I
/*     */                   //   212: istore #6
/*     */                   //   214: aload #5
/*     */                   //   216: getstatic com/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$ObjectType.FALLING_BLOCK : Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$ObjectType;
/*     */                   //   219: if_acmpne -> 278
/*     */                   //   222: aload_1
/*     */                   //   223: getstatic com/viaversion/viaversion/api/type/Type.INT : Lcom/viaversion/viaversion/api/type/types/IntType;
/*     */                   //   226: iconst_0
/*     */                   //   227: invokeinterface get : (Lcom/viaversion/viaversion/api/type/Type;I)Ljava/lang/Object;
/*     */                   //   232: checkcast java/lang/Integer
/*     */                   //   235: invokevirtual intValue : ()I
/*     */                   //   238: istore #7
/*     */                   //   240: aload_0
/*     */                   //   241: getfield this$0 : Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14;
/*     */                   //   244: invokestatic access$200 : (Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14;)Lcom/viaversion/viaversion/api/protocol/Protocol;
/*     */                   //   247: checkcast com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14
/*     */                   //   250: invokevirtual getMappingData : ()Lcom/viaversion/viabackwards/api/data/BackwardsMappings;
/*     */                   //   253: iload #7
/*     */                   //   255: invokevirtual getNewBlockStateId : (I)I
/*     */                   //   258: istore #8
/*     */                   //   260: aload_1
/*     */                   //   261: getstatic com/viaversion/viaversion/api/type/Type.INT : Lcom/viaversion/viaversion/api/type/types/IntType;
/*     */                   //   264: iconst_0
/*     */                   //   265: iload #8
/*     */                   //   267: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */                   //   270: invokeinterface set : (Lcom/viaversion/viaversion/api/type/Type;ILjava/lang/Object;)V
/*     */                   //   275: goto -> 306
/*     */                   //   278: aload #4
/*     */                   //   280: getstatic com/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$EntityType.ABSTRACT_ARROW : Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$EntityType;
/*     */                   //   283: invokevirtual isOrHasParent : (Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Z
/*     */                   //   286: ifeq -> 306
/*     */                   //   289: aload_1
/*     */                   //   290: getstatic com/viaversion/viaversion/api/type/Type.INT : Lcom/viaversion/viaversion/api/type/types/IntType;
/*     */                   //   293: iconst_0
/*     */                   //   294: iload #6
/*     */                   //   296: iconst_1
/*     */                   //   297: iadd
/*     */                   //   298: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */                   //   301: invokeinterface set : (Lcom/viaversion/viaversion/api/type/Type;ILjava/lang/Object;)V
/*     */                   //   306: return
/*     */                   // Line number table:
/*     */                   //   Java source line number -> byte code offset
/*     */                   //   #140	-> 0
/*     */                   //   #141	-> 17
/*     */                   //   #142	-> 26
/*     */                   //   #144	-> 33
/*     */                   //   #145	-> 44
/*     */                   //   #146	-> 49
/*     */                   //   #147	-> 52
/*     */                   //   #149	-> 100
/*     */                   //   #150	-> 103
/*     */                   //   #152	-> 106
/*     */                   //   #153	-> 109
/*     */                   //   #155	-> 112
/*     */                   //   #156	-> 115
/*     */                   //   #158	-> 118
/*     */                   //   #159	-> 121
/*     */                   //   #161	-> 124
/*     */                   //   #162	-> 127
/*     */                   //   #164	-> 130
/*     */                   //   #167	-> 134
/*     */                   //   #168	-> 139
/*     */                   //   #169	-> 154
/*     */                   //   #170	-> 157
/*     */                   //   #173	-> 171
/*     */                   //   #175	-> 177
/*     */                   //   #177	-> 196
/*     */                   //   #178	-> 214
/*     */                   //   #179	-> 222
/*     */                   //   #180	-> 240
/*     */                   //   #181	-> 260
/*     */                   //   #182	-> 275
/*     */                   //   #183	-> 289
/*     */                   //   #185	-> 306
/*     */                   // Local variable table:
/*     */                   //   start	length	slot	name	descriptor
/*     */                   //   52	102	6	data	I
/*     */                   //   49	108	5	objectType	Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$ObjectType;
/*     */                   //   240	35	7	blockState	I
/*     */                   //   260	15	8	combined	I
/*     */                   //   0	307	0	this	Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14$3;
/*     */                   //   0	307	1	wrapper	Lcom/viaversion/viaversion/api/protocol/packet/PacketWrapper;
/*     */                   //   17	290	2	id	I
/*     */                   //   26	281	3	mappedId	I
/*     */                   //   33	274	4	entityType	Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$EntityType;
/*     */                   //   171	136	5	objectType	Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$ObjectType;
/*     */                   //   214	93	6	data	I
/*     */                 });
/*     */           }
/*     */         });
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
/* 189 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.SPAWN_MOB, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 192 */             map((Type)Type.VAR_INT);
/* 193 */             map(Type.UUID);
/* 194 */             map((Type)Type.VAR_INT);
/* 195 */             map((Type)Type.DOUBLE);
/* 196 */             map((Type)Type.DOUBLE);
/* 197 */             map((Type)Type.DOUBLE);
/* 198 */             map((Type)Type.BYTE);
/* 199 */             map((Type)Type.BYTE);
/* 200 */             map((Type)Type.BYTE);
/* 201 */             map((Type)Type.SHORT);
/* 202 */             map((Type)Type.SHORT);
/* 203 */             map((Type)Type.SHORT);
/* 204 */             map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
/*     */             
/* 206 */             handler(wrapper -> {
/*     */                   int type = ((Integer)wrapper.get((Type)Type.VAR_INT, 1)).intValue();
/*     */                   
/*     */                   EntityType entityType = Entity1_14Types.getTypeFromId(type);
/*     */                   
/*     */                   EntityPackets1_14.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), entityType);
/*     */                   
/*     */                   int oldId = EntityPackets1_14.this.newEntityId(type);
/*     */                   if (oldId == -1) {
/*     */                     EntityData entityData = EntityPackets1_14.this.entityDataForType(entityType);
/*     */                     if (entityData == null) {
/*     */                       ViaBackwards.getPlatform().getLogger().warning("Could not find 1.13.2 entity type for 1.14 entity type " + type + "/" + entityType);
/*     */                       wrapper.cancel();
/*     */                     } else {
/*     */                       wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(entityData.replacementId()));
/*     */                     } 
/*     */                   } else {
/*     */                     wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(oldId));
/*     */                   } 
/*     */                 });
/* 226 */             handler(EntityPackets1_14.this.getMobSpawnRewriter(Types1_13_2.METADATA_LIST));
/*     */           }
/*     */         });
/*     */     
/* 230 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.SPAWN_EXPERIENCE_ORB, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 233 */             map((Type)Type.VAR_INT);
/* 234 */             map((Type)Type.DOUBLE);
/* 235 */             map((Type)Type.DOUBLE);
/* 236 */             map((Type)Type.DOUBLE);
/* 237 */             handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), (EntityType)Entity1_14Types.EXPERIENCE_ORB));
/*     */           }
/*     */         });
/*     */     
/* 241 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.SPAWN_GLOBAL_ENTITY, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 244 */             map((Type)Type.VAR_INT);
/* 245 */             map((Type)Type.BYTE);
/* 246 */             map((Type)Type.DOUBLE);
/* 247 */             map((Type)Type.DOUBLE);
/* 248 */             map((Type)Type.DOUBLE);
/* 249 */             handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), (EntityType)Entity1_14Types.LIGHTNING_BOLT));
/*     */           }
/*     */         });
/*     */     
/* 253 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PAINTING, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 256 */             map((Type)Type.VAR_INT);
/* 257 */             map(Type.UUID);
/* 258 */             map((Type)Type.VAR_INT);
/* 259 */             map(Type.POSITION1_14, Type.POSITION);
/* 260 */             map((Type)Type.BYTE);
/*     */ 
/*     */             
/* 263 */             handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue(), (EntityType)Entity1_14Types.PAINTING));
/*     */           }
/*     */         });
/*     */     
/* 267 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PLAYER, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 270 */             map((Type)Type.VAR_INT);
/* 271 */             map(Type.UUID);
/* 272 */             map((Type)Type.DOUBLE);
/* 273 */             map((Type)Type.DOUBLE);
/* 274 */             map((Type)Type.DOUBLE);
/* 275 */             map((Type)Type.BYTE);
/* 276 */             map((Type)Type.BYTE);
/* 277 */             map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
/*     */             
/* 279 */             handler(EntityPackets1_14.this.getTrackerAndMetaHandler(Types1_13_2.METADATA_LIST, (EntityType)Entity1_14Types.PLAYER));
/* 280 */             handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, true, false));
/*     */           }
/*     */         });
/*     */     
/* 284 */     registerRemoveEntities((ClientboundPacketType)ClientboundPackets1_14.DESTROY_ENTITIES);
/* 285 */     registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
/*     */     
/* 287 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.JOIN_GAME, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 290 */             map((Type)Type.INT);
/* 291 */             map((Type)Type.UNSIGNED_BYTE);
/* 292 */             map((Type)Type.INT);
/*     */             
/* 294 */             handler(EntityPackets1_14.this.getTrackerHandler((EntityType)Entity1_14Types.PLAYER, (Type)Type.INT));
/* 295 */             handler(EntityPackets1_14.this.getDimensionHandler(1));
/* 296 */             handler(wrapper -> {
/*     */                   short difficulty = (short)((DifficultyStorage)wrapper.user().get(DifficultyStorage.class)).getDifficulty();
/*     */ 
/*     */                   
/*     */                   wrapper.write((Type)Type.UNSIGNED_BYTE, Short.valueOf(difficulty));
/*     */ 
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.UNSIGNED_BYTE);
/*     */ 
/*     */                   
/*     */                   wrapper.passthrough(Type.STRING);
/*     */                   
/*     */                   wrapper.read((Type)Type.VAR_INT);
/*     */                 });
/*     */           }
/*     */         });
/*     */     
/* 313 */     ((Protocol1_13_2To1_14)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_14.RESPAWN, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 316 */             map((Type)Type.INT);
/*     */             
/* 318 */             handler(wrapper -> {
/*     */                   ClientWorld clientWorld = (ClientWorld)wrapper.user().get(ClientWorld.class);
/*     */                   int dimensionId = ((Integer)wrapper.get((Type)Type.INT, 0)).intValue();
/*     */                   clientWorld.setEnvironment(dimensionId);
/*     */                   short difficulty = (short)((DifficultyStorage)wrapper.user().get(DifficultyStorage.class)).getDifficulty();
/*     */                   wrapper.write((Type)Type.UNSIGNED_BYTE, Short.valueOf(difficulty));
/*     */                   ((ChunkLightStorage)wrapper.user().get(ChunkLightStorage.class)).clear();
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerRewrites() {
/* 334 */     mapTypes((EntityType[])Entity1_14Types.values(), Entity1_13Types.EntityType.class);
/*     */     
/* 336 */     mapEntityTypeWithData((EntityType)Entity1_14Types.CAT, (EntityType)Entity1_14Types.OCELOT).jsonName();
/* 337 */     mapEntityTypeWithData((EntityType)Entity1_14Types.TRADER_LLAMA, (EntityType)Entity1_14Types.LLAMA).jsonName();
/* 338 */     mapEntityTypeWithData((EntityType)Entity1_14Types.FOX, (EntityType)Entity1_14Types.WOLF).jsonName();
/* 339 */     mapEntityTypeWithData((EntityType)Entity1_14Types.PANDA, (EntityType)Entity1_14Types.POLAR_BEAR).jsonName();
/* 340 */     mapEntityTypeWithData((EntityType)Entity1_14Types.PILLAGER, (EntityType)Entity1_14Types.VILLAGER).jsonName();
/* 341 */     mapEntityTypeWithData((EntityType)Entity1_14Types.WANDERING_TRADER, (EntityType)Entity1_14Types.VILLAGER).jsonName();
/* 342 */     mapEntityTypeWithData((EntityType)Entity1_14Types.RAVAGER, (EntityType)Entity1_14Types.COW).jsonName();
/*     */     
/* 344 */     filter().handler((event, meta) -> {
/*     */           int typeId = meta.metaType().typeId();
/*     */           
/*     */           if (typeId <= 15) {
/*     */             meta.setMetaType(Types1_13_2.META_TYPES.byId(typeId));
/*     */           }
/*     */         });
/* 351 */     registerMetaTypeHandler(Types1_13_2.META_TYPES.itemType, Types1_13_2.META_TYPES.blockStateType, null, null, Types1_13_2.META_TYPES.componentType, Types1_13_2.META_TYPES.optionalComponentType);
/*     */ 
/*     */     
/* 354 */     filter().type((EntityType)Entity1_14Types.PILLAGER).cancel(15);
/*     */     
/* 356 */     filter().type((EntityType)Entity1_14Types.FOX).cancel(15);
/* 357 */     filter().type((EntityType)Entity1_14Types.FOX).cancel(16);
/* 358 */     filter().type((EntityType)Entity1_14Types.FOX).cancel(17);
/* 359 */     filter().type((EntityType)Entity1_14Types.FOX).cancel(18);
/*     */     
/* 361 */     filter().type((EntityType)Entity1_14Types.PANDA).cancel(15);
/* 362 */     filter().type((EntityType)Entity1_14Types.PANDA).cancel(16);
/* 363 */     filter().type((EntityType)Entity1_14Types.PANDA).cancel(17);
/* 364 */     filter().type((EntityType)Entity1_14Types.PANDA).cancel(18);
/* 365 */     filter().type((EntityType)Entity1_14Types.PANDA).cancel(19);
/* 366 */     filter().type((EntityType)Entity1_14Types.PANDA).cancel(20);
/*     */     
/* 368 */     filter().type((EntityType)Entity1_14Types.CAT).cancel(18);
/* 369 */     filter().type((EntityType)Entity1_14Types.CAT).cancel(19);
/* 370 */     filter().type((EntityType)Entity1_14Types.CAT).cancel(20);
/*     */     
/* 372 */     filter().handler((event, meta) -> {
/*     */           EntityType type = event.entityType();
/*     */           if (type == null)
/*     */             return; 
/*     */           if (type.isOrHasParent((EntityType)Entity1_14Types.ABSTRACT_ILLAGER_BASE) || type == Entity1_14Types.RAVAGER || type == Entity1_14Types.WITCH) {
/*     */             int index = event.index();
/*     */             if (index == 14) {
/*     */               event.cancel();
/*     */             } else if (index > 14) {
/*     */               event.setIndex(index - 1);
/*     */             } 
/*     */           } 
/*     */         });
/* 385 */     filter().type((EntityType)Entity1_14Types.AREA_EFFECT_CLOUD).index(10).handler((event, meta) -> rewriteParticle((Particle)meta.getValue()));
/*     */ 
/*     */ 
/*     */     
/* 389 */     filter().type((EntityType)Entity1_14Types.FIREWORK_ROCKET).index(8).handler((event, meta) -> {
/*     */           meta.setMetaType(Types1_13_2.META_TYPES.varIntType);
/*     */           
/*     */           Integer value = (Integer)meta.getValue();
/*     */           if (value == null) {
/*     */             meta.setValue(Integer.valueOf(0));
/*     */           }
/*     */         });
/* 397 */     filter().filterFamily((EntityType)Entity1_14Types.ABSTRACT_ARROW).removeIndex(9);
/*     */     
/* 399 */     filter().type((EntityType)Entity1_14Types.VILLAGER).cancel(15);
/*     */     
/* 401 */     MetaHandler villagerDataHandler = (event, meta) -> {
/*     */         VillagerData villagerData = (VillagerData)meta.getValue();
/*     */         
/*     */         meta.setTypeAndValue(Types1_13_2.META_TYPES.varIntType, Integer.valueOf(villagerDataToProfession(villagerData)));
/*     */         if (meta.id() == 16) {
/*     */           event.setIndex(15);
/*     */         }
/*     */       };
/* 409 */     filter().type((EntityType)Entity1_14Types.ZOMBIE_VILLAGER).index(18).handler(villagerDataHandler);
/* 410 */     filter().type((EntityType)Entity1_14Types.VILLAGER).index(16).handler(villagerDataHandler);
/*     */ 
/*     */     
/* 413 */     filter().filterFamily((EntityType)Entity1_14Types.ABSTRACT_SKELETON).index(13).handler((event, meta) -> {
/*     */           byte value = ((Byte)meta.getValue()).byteValue();
/*     */           if ((value & 0x4) != 0) {
/*     */             event.createExtraMeta(new Metadata(14, Types1_13_2.META_TYPES.booleanType, Boolean.valueOf(true)));
/*     */           }
/*     */         });
/* 419 */     filter().filterFamily((EntityType)Entity1_14Types.ZOMBIE).index(13).handler((event, meta) -> {
/*     */           byte value = ((Byte)meta.getValue()).byteValue();
/*     */           
/*     */           if ((value & 0x4) != 0) {
/*     */             event.createExtraMeta(new Metadata(16, Types1_13_2.META_TYPES.booleanType, Boolean.valueOf(true)));
/*     */           }
/*     */         });
/* 426 */     filter().filterFamily((EntityType)Entity1_14Types.ZOMBIE).addIndex(16);
/*     */ 
/*     */     
/* 429 */     filter().filterFamily((EntityType)Entity1_14Types.LIVINGENTITY).handler((event, meta) -> {
/*     */           int index = event.index();
/*     */           
/*     */           if (index == 12) {
/*     */             Position position = (Position)meta.getValue();
/*     */             
/*     */             if (position != null) {
/*     */               PacketWrapper wrapper = PacketWrapper.create((PacketType)ClientboundPackets1_13.USE_BED, null, event.user());
/*     */               wrapper.write((Type)Type.VAR_INT, Integer.valueOf(event.entityId()));
/*     */               wrapper.write(Type.POSITION, position);
/*     */               try {
/*     */                 wrapper.scheduleSend(Protocol1_13_2To1_14.class);
/* 441 */               } catch (Exception ex) {
/*     */                 ex.printStackTrace();
/*     */               } 
/*     */             } 
/*     */ 
/*     */             
/*     */             event.cancel();
/*     */           } else if (index > 12) {
/*     */             event.setIndex(index - 1);
/*     */           } 
/*     */         });
/*     */     
/* 453 */     filter().removeIndex(6);
/*     */     
/* 455 */     filter().type((EntityType)Entity1_14Types.OCELOT).index(13).handler((event, meta) -> {
/*     */           event.setIndex(15);
/*     */           
/*     */           meta.setTypeAndValue(Types1_13_2.META_TYPES.varIntType, Integer.valueOf(0));
/*     */         });
/* 460 */     filter().type((EntityType)Entity1_14Types.CAT).handler((event, meta) -> {
/*     */           if (event.index() == 15) {
/*     */             meta.setValue(Integer.valueOf(1));
/*     */           } else if (event.index() == 13) {
/*     */             meta.setValue(Byte.valueOf((byte)(((Byte)meta.getValue()).byteValue() & 0x4)));
/*     */           } 
/*     */         });
/*     */     
/* 468 */     filter().handler((event, meta) -> {
/*     */           if (meta.metaType().typeId() > 15) {
/*     */             throw new IllegalArgumentException("Unhandled metadata: " + meta);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public int villagerDataToProfession(VillagerData data) {
/* 476 */     switch (data.profession()) {
/*     */       case 1:
/*     */       case 10:
/*     */       case 13:
/*     */       case 14:
/* 481 */         return 3;
/*     */       case 2:
/*     */       case 8:
/* 484 */         return 4;
/*     */       case 3:
/*     */       case 9:
/* 487 */         return 1;
/*     */       case 4:
/* 489 */         return 2;
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 12:
/* 494 */         return 0;
/*     */     } 
/*     */ 
/*     */     
/* 498 */     return 5;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityType typeFromId(int typeId) {
/* 504 */     return Entity1_14Types.getTypeFromId(typeId);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_13_2to1_14\packets\EntityPackets1_14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */