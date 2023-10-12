/*     */ package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
/*     */ import com.viaversion.viaversion.api.minecraft.Position;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.Particle;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionHandler;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.NamedSoundRewriter;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.ParticleRewriter;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.PaintingProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockStorage;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
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
/*     */ public class WorldPackets
/*     */ {
/*  58 */   private static final IntSet VALID_BIOMES = (IntSet)new IntOpenHashSet(70, 0.99F);
/*     */   
/*     */   static {
/*     */     int i;
/*  62 */     for (i = 0; i < 50; i++) {
/*  63 */       VALID_BIOMES.add(i);
/*     */     }
/*  65 */     VALID_BIOMES.add(127);
/*  66 */     for (i = 129; i <= 134; i++) {
/*  67 */       VALID_BIOMES.add(i);
/*     */     }
/*  69 */     VALID_BIOMES.add(140);
/*  70 */     VALID_BIOMES.add(149);
/*  71 */     VALID_BIOMES.add(151);
/*  72 */     for (i = 155; i <= 158; i++) {
/*  73 */       VALID_BIOMES.add(i);
/*     */     }
/*  75 */     for (i = 160; i <= 167; i++) {
/*  76 */       VALID_BIOMES.add(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void register(Protocol1_13To1_12_2 protocol) {
/*  82 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.SPAWN_PAINTING, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  85 */             map((Type)Type.VAR_INT);
/*  86 */             map(Type.UUID);
/*     */             
/*  88 */             handler(wrapper -> {
/*     */                   PaintingProvider provider = (PaintingProvider)Via.getManager().getProviders().get(PaintingProvider.class);
/*     */                   
/*     */                   String motive = (String)wrapper.read(Type.STRING);
/*     */                   
/*     */                   Optional<Integer> id = provider.getIntByIdentifier(motive);
/*     */                   
/*     */                   if (!id.isPresent() && (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug())) {
/*     */                     Via.getPlatform().getLogger().warning("Could not find painting motive: " + motive + " falling back to default (0)");
/*     */                   }
/*     */                   wrapper.write((Type)Type.VAR_INT, id.orElse(Integer.valueOf(0)));
/*     */                 });
/*     */           }
/*     */         });
/* 102 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.BLOCK_ENTITY_DATA, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 105 */             map(Type.POSITION);
/* 106 */             map((Type)Type.UNSIGNED_BYTE);
/* 107 */             map(Type.NBT);
/*     */             
/* 109 */             handler(wrapper -> {
/*     */                   Position position = (Position)wrapper.get(Type.POSITION, 0);
/*     */                   
/*     */                   short action = ((Short)wrapper.get((Type)Type.UNSIGNED_BYTE, 0)).shortValue();
/*     */                   
/*     */                   CompoundTag tag = (CompoundTag)wrapper.get(Type.NBT, 0);
/*     */                   
/*     */                   BlockEntityProvider provider = (BlockEntityProvider)Via.getManager().getProviders().get(BlockEntityProvider.class);
/*     */                   
/*     */                   int newId = provider.transform(wrapper.user(), position, tag, true);
/*     */                   if (newId != -1) {
/*     */                     BlockStorage storage = (BlockStorage)wrapper.user().get(BlockStorage.class);
/*     */                     BlockStorage.ReplacementData replacementData = storage.get(position);
/*     */                     if (replacementData != null) {
/*     */                       replacementData.setReplacement(newId);
/*     */                     }
/*     */                   } 
/*     */                   if (action == 5) {
/*     */                     wrapper.cancel();
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/* 132 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.BLOCK_ACTION, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 135 */             map(Type.POSITION);
/* 136 */             map((Type)Type.UNSIGNED_BYTE);
/* 137 */             map((Type)Type.UNSIGNED_BYTE);
/* 138 */             map((Type)Type.VAR_INT);
/* 139 */             handler(wrapper -> {
/*     */                   Position pos = (Position)wrapper.get(Type.POSITION, 0);
/*     */                   
/*     */                   short action = ((Short)wrapper.get((Type)Type.UNSIGNED_BYTE, 0)).shortValue();
/*     */                   
/*     */                   short param = ((Short)wrapper.get((Type)Type.UNSIGNED_BYTE, 1)).shortValue();
/*     */                   int blockId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   if (blockId == 25) {
/*     */                     blockId = 73;
/*     */                   } else if (blockId == 33) {
/*     */                     blockId = 99;
/*     */                   } else if (blockId == 29) {
/*     */                     blockId = 92;
/*     */                   } else if (blockId == 54) {
/*     */                     blockId = 142;
/*     */                   } else if (blockId == 146) {
/*     */                     blockId = 305;
/*     */                   } else if (blockId == 130) {
/*     */                     blockId = 249;
/*     */                   } else if (blockId == 138) {
/*     */                     blockId = 257;
/*     */                   } else if (blockId == 52) {
/*     */                     blockId = 140;
/*     */                   } else if (blockId == 209) {
/*     */                     blockId = 472;
/*     */                   } else if (blockId >= 219 && blockId <= 234) {
/*     */                     blockId = blockId - 219 + 483;
/*     */                   } 
/*     */                   if (blockId == 73) {
/*     */                     PacketWrapper blockChange = wrapper.create((PacketType)ClientboundPackets1_13.BLOCK_CHANGE);
/*     */                     blockChange.write(Type.POSITION, pos);
/*     */                     blockChange.write((Type)Type.VAR_INT, Integer.valueOf(249 + action * 24 * 2 + param * 2));
/*     */                     blockChange.send(Protocol1_13To1_12_2.class);
/*     */                   } 
/*     */                   wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(blockId));
/*     */                 });
/*     */           }
/*     */         });
/* 177 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.BLOCK_CHANGE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 180 */             map(Type.POSITION);
/* 181 */             map((Type)Type.VAR_INT);
/* 182 */             handler(wrapper -> {
/*     */                   Position position = (Position)wrapper.get(Type.POSITION, 0);
/*     */                   
/*     */                   int newId = WorldPackets.toNewId(((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue());
/*     */                   
/*     */                   UserConnection userConnection = wrapper.user();
/*     */                   
/*     */                   if (Via.getConfig().isServersideBlockConnections()) {
/*     */                     newId = ConnectionData.connect(userConnection, position, newId);
/*     */                     
/*     */                     ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), newId);
/*     */                   } 
/*     */                   
/*     */                   wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(WorldPackets.checkStorage(wrapper.user(), position, newId)));
/*     */                   if (Via.getConfig().isServersideBlockConnections()) {
/*     */                     wrapper.send(Protocol1_13To1_12_2.class);
/*     */                     wrapper.cancel();
/*     */                     ConnectionData.update(userConnection, position);
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 204 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.MULTI_BLOCK_CHANGE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 207 */             map((Type)Type.INT);
/* 208 */             map((Type)Type.INT);
/* 209 */             map(Type.BLOCK_CHANGE_RECORD_ARRAY);
/* 210 */             handler(wrapper -> {
/*     */                   int chunkX = ((Integer)wrapper.get((Type)Type.INT, 0)).intValue();
/*     */                   
/*     */                   int chunkZ = ((Integer)wrapper.get((Type)Type.INT, 1)).intValue();
/*     */                   
/*     */                   UserConnection userConnection = wrapper.user();
/*     */                   
/*     */                   BlockChangeRecord[] records = (BlockChangeRecord[])wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0);
/*     */                   
/*     */                   for (BlockChangeRecord record : records) {
/*     */                     int newBlock = WorldPackets.toNewId(record.getBlockId());
/*     */                     
/*     */                     Position position = new Position(record.getSectionX() + (chunkX << 4), record.getY(), record.getSectionZ() + (chunkZ << 4));
/*     */                     
/*     */                     record.setBlockId(WorldPackets.checkStorage(wrapper.user(), position, newBlock));
/*     */                     
/*     */                     if (Via.getConfig().isServersideBlockConnections()) {
/*     */                       ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), newBlock);
/*     */                     }
/*     */                   } 
/*     */                   
/*     */                   if (Via.getConfig().isServersideBlockConnections()) {
/*     */                     for (BlockChangeRecord record : records) {
/*     */                       int blockState = record.getBlockId();
/*     */                       
/*     */                       Position position = new Position(record.getSectionX() + chunkX * 16, record.getY(), record.getSectionZ() + chunkZ * 16);
/*     */                       
/*     */                       ConnectionHandler handler = ConnectionData.getConnectionHandler(blockState);
/*     */                       
/*     */                       if (handler != null) {
/*     */                         blockState = handler.connect(userConnection, position, blockState);
/*     */                         
/*     */                         record.setBlockId(blockState);
/*     */                         
/*     */                         ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), blockState);
/*     */                       } 
/*     */                     } 
/*     */                     
/*     */                     wrapper.send(Protocol1_13To1_12_2.class);
/*     */                     
/*     */                     wrapper.cancel();
/*     */                     
/*     */                     for (BlockChangeRecord record : records) {
/*     */                       Position position = new Position(record.getSectionX() + chunkX * 16, record.getY(), record.getSectionZ() + chunkZ * 16);
/*     */                       
/*     */                       ConnectionData.update(userConnection, position);
/*     */                     } 
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/*     */     
/* 262 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.EXPLOSION, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 265 */             if (!Via.getConfig().isServersideBlockConnections()) {
/*     */               return;
/*     */             }
/*     */             
/* 269 */             map((Type)Type.FLOAT);
/* 270 */             map((Type)Type.FLOAT);
/* 271 */             map((Type)Type.FLOAT);
/* 272 */             map((Type)Type.FLOAT);
/* 273 */             map((Type)Type.INT);
/*     */             
/* 275 */             handler(wrapper -> {
/*     */                   UserConnection userConnection = wrapper.user();
/*     */                   
/*     */                   int x = (int)Math.floor(((Float)wrapper.get((Type)Type.FLOAT, 0)).floatValue());
/*     */                   
/*     */                   int y = (int)Math.floor(((Float)wrapper.get((Type)Type.FLOAT, 1)).floatValue());
/*     */                   
/*     */                   int z = (int)Math.floor(((Float)wrapper.get((Type)Type.FLOAT, 2)).floatValue());
/*     */                   
/*     */                   int recordCount = ((Integer)wrapper.get((Type)Type.INT, 0)).intValue();
/*     */                   
/*     */                   Position[] records = new Position[recordCount];
/*     */                   
/*     */                   int i;
/*     */                   
/*     */                   for (i = 0; i < recordCount; i++) {
/*     */                     Position position = new Position(x + ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue(), (short)(y + ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue()), z + ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue());
/*     */                     
/*     */                     records[i] = position;
/*     */                     
/*     */                     ConnectionData.updateBlockStorage(userConnection, position.x(), position.y(), position.z(), 0);
/*     */                   } 
/*     */                   wrapper.send(Protocol1_13To1_12_2.class);
/*     */                   wrapper.cancel();
/*     */                   for (i = 0; i < recordCount; i++) {
/*     */                     ConnectionData.update(userConnection, records[i]);
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/* 305 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.UNLOAD_CHUNK, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 308 */             if (Via.getConfig().isServersideBlockConnections()) {
/* 309 */               handler(wrapper -> {
/*     */                     int x = ((Integer)wrapper.passthrough((Type)Type.INT)).intValue();
/*     */                     
/*     */                     int z = ((Integer)wrapper.passthrough((Type)Type.INT)).intValue();
/*     */                     ConnectionData.blockConnectionProvider.unloadChunk(wrapper.user(), x, z);
/*     */                   });
/*     */             }
/*     */           }
/*     */         });
/* 318 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.NAMED_SOUND, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 321 */             map(Type.STRING);
/* 322 */             handler(wrapper -> {
/*     */                   String sound = ((String)wrapper.get(Type.STRING, 0)).replace("minecraft:", "");
/*     */                   
/*     */                   String newSoundId = NamedSoundRewriter.getNewId(sound);
/*     */                   wrapper.set(Type.STRING, 0, newSoundId);
/*     */                 });
/*     */           }
/*     */         });
/* 330 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.CHUNK_DATA, wrapper -> {
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: invokeinterface user : ()Lcom/viaversion/viaversion/api/connection/UserConnection;
/*     */           //   6: ldc_w com/viaversion/viaversion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld
/*     */           //   9: invokeinterface get : (Ljava/lang/Class;)Lcom/viaversion/viaversion/api/connection/StorableObject;
/*     */           //   14: checkcast com/viaversion/viaversion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld
/*     */           //   17: astore_1
/*     */           //   18: aload_0
/*     */           //   19: invokeinterface user : ()Lcom/viaversion/viaversion/api/connection/UserConnection;
/*     */           //   24: ldc com/viaversion/viaversion/protocols/protocol1_13to1_12_2/storage/BlockStorage
/*     */           //   26: invokeinterface get : (Ljava/lang/Class;)Lcom/viaversion/viaversion/api/connection/StorableObject;
/*     */           //   31: checkcast com/viaversion/viaversion/protocols/protocol1_13to1_12_2/storage/BlockStorage
/*     */           //   34: astore_2
/*     */           //   35: new com/viaversion/viaversion/protocols/protocol1_9_3to1_9_1_2/types/Chunk1_9_3_4Type
/*     */           //   38: dup
/*     */           //   39: aload_1
/*     */           //   40: invokespecial <init> : (Lcom/viaversion/viaversion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld;)V
/*     */           //   43: astore_3
/*     */           //   44: new com/viaversion/viaversion/protocols/protocol1_13to1_12_2/types/Chunk1_13Type
/*     */           //   47: dup
/*     */           //   48: aload_1
/*     */           //   49: invokespecial <init> : (Lcom/viaversion/viaversion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld;)V
/*     */           //   52: astore #4
/*     */           //   54: aload_0
/*     */           //   55: aload_3
/*     */           //   56: invokeinterface read : (Lcom/viaversion/viaversion/api/type/Type;)Ljava/lang/Object;
/*     */           //   61: checkcast com/viaversion/viaversion/api/minecraft/chunks/Chunk
/*     */           //   64: astore #5
/*     */           //   66: aload_0
/*     */           //   67: aload #4
/*     */           //   69: aload #5
/*     */           //   71: invokeinterface write : (Lcom/viaversion/viaversion/api/type/Type;Ljava/lang/Object;)V
/*     */           //   76: iconst_0
/*     */           //   77: istore #6
/*     */           //   79: iload #6
/*     */           //   81: aload #5
/*     */           //   83: invokeinterface getSections : ()[Lcom/viaversion/viaversion/api/minecraft/chunks/ChunkSection;
/*     */           //   88: arraylength
/*     */           //   89: if_icmpge -> 571
/*     */           //   92: aload #5
/*     */           //   94: invokeinterface getSections : ()[Lcom/viaversion/viaversion/api/minecraft/chunks/ChunkSection;
/*     */           //   99: iload #6
/*     */           //   101: aaload
/*     */           //   102: astore #7
/*     */           //   104: aload #7
/*     */           //   106: ifnonnull -> 112
/*     */           //   109: goto -> 565
/*     */           //   112: aload #7
/*     */           //   114: getstatic com/viaversion/viaversion/api/minecraft/chunks/PaletteType.BLOCKS : Lcom/viaversion/viaversion/api/minecraft/chunks/PaletteType;
/*     */           //   117: invokeinterface palette : (Lcom/viaversion/viaversion/api/minecraft/chunks/PaletteType;)Lcom/viaversion/viaversion/api/minecraft/chunks/DataPalette;
/*     */           //   122: astore #8
/*     */           //   124: iconst_0
/*     */           //   125: istore #9
/*     */           //   127: iload #9
/*     */           //   129: aload #8
/*     */           //   131: invokeinterface size : ()I
/*     */           //   136: if_icmpge -> 174
/*     */           //   139: aload #8
/*     */           //   141: iload #9
/*     */           //   143: invokeinterface idByIndex : (I)I
/*     */           //   148: istore #10
/*     */           //   150: iload #10
/*     */           //   152: invokestatic toNewId : (I)I
/*     */           //   155: istore #11
/*     */           //   157: aload #8
/*     */           //   159: iload #9
/*     */           //   161: iload #11
/*     */           //   163: invokeinterface setIdByIndex : (II)V
/*     */           //   168: iinc #9, 1
/*     */           //   171: goto -> 127
/*     */           //   174: aload #5
/*     */           //   176: invokeinterface isFullChunk : ()Z
/*     */           //   181: ifeq -> 238
/*     */           //   184: iconst_0
/*     */           //   185: istore #9
/*     */           //   187: iconst_0
/*     */           //   188: istore #10
/*     */           //   190: iload #10
/*     */           //   192: aload #8
/*     */           //   194: invokeinterface size : ()I
/*     */           //   199: if_icmpge -> 230
/*     */           //   202: aload_2
/*     */           //   203: aload #8
/*     */           //   205: iload #10
/*     */           //   207: invokeinterface idByIndex : (I)I
/*     */           //   212: invokevirtual isWelcome : (I)Z
/*     */           //   215: ifeq -> 224
/*     */           //   218: iconst_1
/*     */           //   219: istore #9
/*     */           //   221: goto -> 230
/*     */           //   224: iinc #10, 1
/*     */           //   227: goto -> 190
/*     */           //   230: iload #9
/*     */           //   232: ifne -> 238
/*     */           //   235: goto -> 352
/*     */           //   238: iconst_0
/*     */           //   239: istore #9
/*     */           //   241: iload #9
/*     */           //   243: sipush #4096
/*     */           //   246: if_icmpge -> 352
/*     */           //   249: aload #8
/*     */           //   251: iload #9
/*     */           //   253: invokeinterface idAt : (I)I
/*     */           //   258: istore #10
/*     */           //   260: new com/viaversion/viaversion/api/minecraft/Position
/*     */           //   263: dup
/*     */           //   264: iload #9
/*     */           //   266: invokestatic xFromIndex : (I)I
/*     */           //   269: aload #5
/*     */           //   271: invokeinterface getX : ()I
/*     */           //   276: iconst_4
/*     */           //   277: ishl
/*     */           //   278: iadd
/*     */           //   279: iload #9
/*     */           //   281: invokestatic yFromIndex : (I)I
/*     */           //   284: iload #6
/*     */           //   286: iconst_4
/*     */           //   287: ishl
/*     */           //   288: iadd
/*     */           //   289: iload #9
/*     */           //   291: invokestatic zFromIndex : (I)I
/*     */           //   294: aload #5
/*     */           //   296: invokeinterface getZ : ()I
/*     */           //   301: iconst_4
/*     */           //   302: ishl
/*     */           //   303: iadd
/*     */           //   304: invokespecial <init> : (III)V
/*     */           //   307: astore #11
/*     */           //   309: aload_2
/*     */           //   310: iload #10
/*     */           //   312: invokevirtual isWelcome : (I)Z
/*     */           //   315: ifeq -> 329
/*     */           //   318: aload_2
/*     */           //   319: aload #11
/*     */           //   321: iload #10
/*     */           //   323: invokevirtual store : (Lcom/viaversion/viaversion/api/minecraft/Position;I)V
/*     */           //   326: goto -> 346
/*     */           //   329: aload #5
/*     */           //   331: invokeinterface isFullChunk : ()Z
/*     */           //   336: ifne -> 346
/*     */           //   339: aload_2
/*     */           //   340: aload #11
/*     */           //   342: invokevirtual remove : (Lcom/viaversion/viaversion/api/minecraft/Position;)Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/storage/BlockStorage$ReplacementData;
/*     */           //   345: pop
/*     */           //   346: iinc #9, 1
/*     */           //   349: goto -> 241
/*     */           //   352: invokestatic getConfig : ()Lcom/viaversion/viaversion/api/configuration/ViaVersionConfig;
/*     */           //   355: invokeinterface isServersideBlockConnections : ()Z
/*     */           //   360: ifeq -> 565
/*     */           //   363: invokestatic needStoreBlocks : ()Z
/*     */           //   366: ifne -> 372
/*     */           //   369: goto -> 565
/*     */           //   372: aload #5
/*     */           //   374: invokeinterface isFullChunk : ()Z
/*     */           //   379: ifne -> 410
/*     */           //   382: getstatic com/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/ConnectionData.blockConnectionProvider : Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/providers/BlockConnectionProvider;
/*     */           //   385: aload_0
/*     */           //   386: invokeinterface user : ()Lcom/viaversion/viaversion/api/connection/UserConnection;
/*     */           //   391: aload #5
/*     */           //   393: invokeinterface getX : ()I
/*     */           //   398: iload #6
/*     */           //   400: aload #5
/*     */           //   402: invokeinterface getZ : ()I
/*     */           //   407: invokevirtual unloadChunkSection : (Lcom/viaversion/viaversion/api/connection/UserConnection;III)V
/*     */           //   410: iconst_0
/*     */           //   411: istore #9
/*     */           //   413: iconst_0
/*     */           //   414: istore #10
/*     */           //   416: iload #10
/*     */           //   418: aload #8
/*     */           //   420: invokeinterface size : ()I
/*     */           //   425: if_icmpge -> 455
/*     */           //   428: aload #8
/*     */           //   430: iload #10
/*     */           //   432: invokeinterface idByIndex : (I)I
/*     */           //   437: invokestatic isWelcome : (I)Z
/*     */           //   440: ifeq -> 449
/*     */           //   443: iconst_1
/*     */           //   444: istore #9
/*     */           //   446: goto -> 455
/*     */           //   449: iinc #10, 1
/*     */           //   452: goto -> 416
/*     */           //   455: iload #9
/*     */           //   457: ifne -> 463
/*     */           //   460: goto -> 565
/*     */           //   463: iconst_0
/*     */           //   464: istore #10
/*     */           //   466: iload #10
/*     */           //   468: sipush #4096
/*     */           //   471: if_icmpge -> 565
/*     */           //   474: aload #8
/*     */           //   476: iload #10
/*     */           //   478: invokeinterface idAt : (I)I
/*     */           //   483: istore #11
/*     */           //   485: iload #11
/*     */           //   487: invokestatic isWelcome : (I)Z
/*     */           //   490: ifeq -> 559
/*     */           //   493: iload #10
/*     */           //   495: invokestatic xFromIndex : (I)I
/*     */           //   498: aload #5
/*     */           //   500: invokeinterface getX : ()I
/*     */           //   505: iconst_4
/*     */           //   506: ishl
/*     */           //   507: iadd
/*     */           //   508: istore #12
/*     */           //   510: iload #10
/*     */           //   512: invokestatic yFromIndex : (I)I
/*     */           //   515: iload #6
/*     */           //   517: iconst_4
/*     */           //   518: ishl
/*     */           //   519: iadd
/*     */           //   520: istore #13
/*     */           //   522: iload #10
/*     */           //   524: invokestatic zFromIndex : (I)I
/*     */           //   527: aload #5
/*     */           //   529: invokeinterface getZ : ()I
/*     */           //   534: iconst_4
/*     */           //   535: ishl
/*     */           //   536: iadd
/*     */           //   537: istore #14
/*     */           //   539: getstatic com/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/ConnectionData.blockConnectionProvider : Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/providers/BlockConnectionProvider;
/*     */           //   542: aload_0
/*     */           //   543: invokeinterface user : ()Lcom/viaversion/viaversion/api/connection/UserConnection;
/*     */           //   548: iload #12
/*     */           //   550: iload #13
/*     */           //   552: iload #14
/*     */           //   554: iload #11
/*     */           //   556: invokevirtual storeBlock : (Lcom/viaversion/viaversion/api/connection/UserConnection;IIII)V
/*     */           //   559: iinc #10, 1
/*     */           //   562: goto -> 466
/*     */           //   565: iinc #6, 1
/*     */           //   568: goto -> 79
/*     */           //   571: aload #5
/*     */           //   573: invokeinterface isBiomeData : ()Z
/*     */           //   578: ifeq -> 712
/*     */           //   581: ldc_w -2147483648
/*     */           //   584: istore #6
/*     */           //   586: iconst_0
/*     */           //   587: istore #7
/*     */           //   589: iload #7
/*     */           //   591: sipush #256
/*     */           //   594: if_icmpge -> 712
/*     */           //   597: aload #5
/*     */           //   599: invokeinterface getBiomeData : ()[I
/*     */           //   604: iload #7
/*     */           //   606: iaload
/*     */           //   607: istore #8
/*     */           //   609: getstatic com/viaversion/viaversion/protocols/protocol1_13to1_12_2/packets/WorldPackets.VALID_BIOMES : Lcom/viaversion/viaversion/libs/fastutil/ints/IntSet;
/*     */           //   612: iload #8
/*     */           //   614: invokeinterface contains : (I)Z
/*     */           //   619: ifne -> 706
/*     */           //   622: iload #8
/*     */           //   624: sipush #255
/*     */           //   627: if_icmpeq -> 695
/*     */           //   630: iload #6
/*     */           //   632: iload #8
/*     */           //   634: if_icmpeq -> 695
/*     */           //   637: invokestatic getConfig : ()Lcom/viaversion/viaversion/api/configuration/ViaVersionConfig;
/*     */           //   640: invokeinterface isSuppressConversionWarnings : ()Z
/*     */           //   645: ifeq -> 659
/*     */           //   648: invokestatic getManager : ()Lcom/viaversion/viaversion/api/ViaManager;
/*     */           //   651: invokeinterface isDebug : ()Z
/*     */           //   656: ifeq -> 691
/*     */           //   659: invokestatic getPlatform : ()Lcom/viaversion/viaversion/api/platform/ViaPlatform;
/*     */           //   662: invokeinterface getLogger : ()Ljava/util/logging/Logger;
/*     */           //   667: new java/lang/StringBuilder
/*     */           //   670: dup
/*     */           //   671: invokespecial <init> : ()V
/*     */           //   674: ldc_w 'Received invalid biome id '
/*     */           //   677: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   680: iload #8
/*     */           //   682: invokevirtual append : (I)Ljava/lang/StringBuilder;
/*     */           //   685: invokevirtual toString : ()Ljava/lang/String;
/*     */           //   688: invokevirtual warning : (Ljava/lang/String;)V
/*     */           //   691: iload #8
/*     */           //   693: istore #6
/*     */           //   695: aload #5
/*     */           //   697: invokeinterface getBiomeData : ()[I
/*     */           //   702: iload #7
/*     */           //   704: iconst_1
/*     */           //   705: iastore
/*     */           //   706: iinc #7, 1
/*     */           //   709: goto -> 589
/*     */           //   712: invokestatic getManager : ()Lcom/viaversion/viaversion/api/ViaManager;
/*     */           //   715: invokeinterface getProviders : ()Lcom/viaversion/viaversion/api/platform/providers/ViaProviders;
/*     */           //   720: ldc_w com/viaversion/viaversion/protocols/protocol1_13to1_12_2/providers/BlockEntityProvider
/*     */           //   723: invokevirtual get : (Ljava/lang/Class;)Lcom/viaversion/viaversion/api/platform/providers/Provider;
/*     */           //   726: checkcast com/viaversion/viaversion/protocols/protocol1_13to1_12_2/providers/BlockEntityProvider
/*     */           //   729: astore #6
/*     */           //   731: aload #5
/*     */           //   733: invokeinterface getBlockEntities : ()Ljava/util/List;
/*     */           //   738: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */           //   743: astore #7
/*     */           //   745: aload #7
/*     */           //   747: invokeinterface hasNext : ()Z
/*     */           //   752: ifeq -> 976
/*     */           //   755: aload #7
/*     */           //   757: invokeinterface next : ()Ljava/lang/Object;
/*     */           //   762: checkcast com/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag
/*     */           //   765: astore #8
/*     */           //   767: aload #6
/*     */           //   769: aload_0
/*     */           //   770: invokeinterface user : ()Lcom/viaversion/viaversion/api/connection/UserConnection;
/*     */           //   775: aconst_null
/*     */           //   776: aload #8
/*     */           //   778: iconst_0
/*     */           //   779: invokevirtual transform : (Lcom/viaversion/viaversion/api/connection/UserConnection;Lcom/viaversion/viaversion/api/minecraft/Position;Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;Z)I
/*     */           //   782: istore #9
/*     */           //   784: iload #9
/*     */           //   786: iconst_m1
/*     */           //   787: if_icmpeq -> 916
/*     */           //   790: aload #8
/*     */           //   792: ldc_w 'x'
/*     */           //   795: invokevirtual get : (Ljava/lang/String;)Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/Tag;
/*     */           //   798: checkcast com/viaversion/viaversion/libs/opennbt/tag/builtin/NumberTag
/*     */           //   801: invokevirtual asInt : ()I
/*     */           //   804: istore #10
/*     */           //   806: aload #8
/*     */           //   808: ldc_w 'y'
/*     */           //   811: invokevirtual get : (Ljava/lang/String;)Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/Tag;
/*     */           //   814: checkcast com/viaversion/viaversion/libs/opennbt/tag/builtin/NumberTag
/*     */           //   817: invokevirtual asInt : ()I
/*     */           //   820: istore #11
/*     */           //   822: aload #8
/*     */           //   824: ldc_w 'z'
/*     */           //   827: invokevirtual get : (Ljava/lang/String;)Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/Tag;
/*     */           //   830: checkcast com/viaversion/viaversion/libs/opennbt/tag/builtin/NumberTag
/*     */           //   833: invokevirtual asInt : ()I
/*     */           //   836: istore #12
/*     */           //   838: new com/viaversion/viaversion/api/minecraft/Position
/*     */           //   841: dup
/*     */           //   842: iload #10
/*     */           //   844: iload #11
/*     */           //   846: i2s
/*     */           //   847: iload #12
/*     */           //   849: invokespecial <init> : (ISI)V
/*     */           //   852: astore #13
/*     */           //   854: aload_2
/*     */           //   855: aload #13
/*     */           //   857: invokevirtual get : (Lcom/viaversion/viaversion/api/minecraft/Position;)Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/storage/BlockStorage$ReplacementData;
/*     */           //   860: astore #14
/*     */           //   862: aload #14
/*     */           //   864: ifnull -> 874
/*     */           //   867: aload #14
/*     */           //   869: iload #9
/*     */           //   871: invokevirtual setReplacement : (I)V
/*     */           //   874: aload #5
/*     */           //   876: invokeinterface getSections : ()[Lcom/viaversion/viaversion/api/minecraft/chunks/ChunkSection;
/*     */           //   881: iload #11
/*     */           //   883: iconst_4
/*     */           //   884: ishr
/*     */           //   885: aaload
/*     */           //   886: getstatic com/viaversion/viaversion/api/minecraft/chunks/PaletteType.BLOCKS : Lcom/viaversion/viaversion/api/minecraft/chunks/PaletteType;
/*     */           //   889: invokeinterface palette : (Lcom/viaversion/viaversion/api/minecraft/chunks/PaletteType;)Lcom/viaversion/viaversion/api/minecraft/chunks/DataPalette;
/*     */           //   894: iload #10
/*     */           //   896: bipush #15
/*     */           //   898: iand
/*     */           //   899: iload #11
/*     */           //   901: bipush #15
/*     */           //   903: iand
/*     */           //   904: iload #12
/*     */           //   906: bipush #15
/*     */           //   908: iand
/*     */           //   909: iload #9
/*     */           //   911: invokeinterface setIdAt : (IIII)V
/*     */           //   916: aload #8
/*     */           //   918: ldc_w 'id'
/*     */           //   921: invokevirtual get : (Ljava/lang/String;)Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/Tag;
/*     */           //   924: astore #10
/*     */           //   926: aload #10
/*     */           //   928: instanceof com/viaversion/viaversion/libs/opennbt/tag/builtin/StringTag
/*     */           //   931: ifeq -> 973
/*     */           //   934: aload #10
/*     */           //   936: checkcast com/viaversion/viaversion/libs/opennbt/tag/builtin/StringTag
/*     */           //   939: invokevirtual getValue : ()Ljava/lang/String;
/*     */           //   942: astore #11
/*     */           //   944: aload #11
/*     */           //   946: ldc_w 'minecraft:noteblock'
/*     */           //   949: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   952: ifne -> 966
/*     */           //   955: aload #11
/*     */           //   957: ldc_w 'minecraft:flower_pot'
/*     */           //   960: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   963: ifeq -> 973
/*     */           //   966: aload #7
/*     */           //   968: invokeinterface remove : ()V
/*     */           //   973: goto -> 745
/*     */           //   976: invokestatic getConfig : ()Lcom/viaversion/viaversion/api/configuration/ViaVersionConfig;
/*     */           //   979: invokeinterface isServersideBlockConnections : ()Z
/*     */           //   984: ifeq -> 1090
/*     */           //   987: aload_0
/*     */           //   988: invokeinterface user : ()Lcom/viaversion/viaversion/api/connection/UserConnection;
/*     */           //   993: aload #5
/*     */           //   995: invokestatic connectBlocks : (Lcom/viaversion/viaversion/api/connection/UserConnection;Lcom/viaversion/viaversion/api/minecraft/chunks/Chunk;)V
/*     */           //   998: aload_0
/*     */           //   999: ldc com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2
/*     */           //   1001: invokeinterface send : (Ljava/lang/Class;)V
/*     */           //   1006: aload_0
/*     */           //   1007: invokeinterface cancel : ()V
/*     */           //   1012: new com/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/ConnectionData$NeighbourUpdater
/*     */           //   1015: dup
/*     */           //   1016: aload_0
/*     */           //   1017: invokeinterface user : ()Lcom/viaversion/viaversion/api/connection/UserConnection;
/*     */           //   1022: invokespecial <init> : (Lcom/viaversion/viaversion/api/connection/UserConnection;)V
/*     */           //   1025: astore #8
/*     */           //   1027: iconst_0
/*     */           //   1028: istore #9
/*     */           //   1030: iload #9
/*     */           //   1032: aload #5
/*     */           //   1034: invokeinterface getSections : ()[Lcom/viaversion/viaversion/api/minecraft/chunks/ChunkSection;
/*     */           //   1039: arraylength
/*     */           //   1040: if_icmpge -> 1090
/*     */           //   1043: aload #5
/*     */           //   1045: invokeinterface getSections : ()[Lcom/viaversion/viaversion/api/minecraft/chunks/ChunkSection;
/*     */           //   1050: iload #9
/*     */           //   1052: aaload
/*     */           //   1053: astore #10
/*     */           //   1055: aload #10
/*     */           //   1057: ifnonnull -> 1063
/*     */           //   1060: goto -> 1084
/*     */           //   1063: aload #8
/*     */           //   1065: aload #5
/*     */           //   1067: invokeinterface getX : ()I
/*     */           //   1072: aload #5
/*     */           //   1074: invokeinterface getZ : ()I
/*     */           //   1079: iload #9
/*     */           //   1081: invokevirtual updateChunkSectionNeighbours : (III)V
/*     */           //   1084: iinc #9, 1
/*     */           //   1087: goto -> 1030
/*     */           //   1090: return
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #331	-> 0
/*     */           //   #332	-> 18
/*     */           //   #334	-> 35
/*     */           //   #335	-> 44
/*     */           //   #336	-> 54
/*     */           //   #337	-> 66
/*     */           //   #339	-> 76
/*     */           //   #340	-> 92
/*     */           //   #341	-> 104
/*     */           //   #342	-> 112
/*     */           //   #344	-> 124
/*     */           //   #345	-> 139
/*     */           //   #346	-> 150
/*     */           //   #347	-> 157
/*     */           //   #344	-> 168
/*     */           //   #352	-> 174
/*     */           //   #353	-> 184
/*     */           //   #354	-> 187
/*     */           //   #355	-> 202
/*     */           //   #356	-> 218
/*     */           //   #357	-> 221
/*     */           //   #354	-> 224
/*     */           //   #360	-> 230
/*     */           //   #362	-> 238
/*     */           //   #363	-> 249
/*     */           //   #364	-> 260
/*     */           //   #365	-> 309
/*     */           //   #366	-> 318
/*     */           //   #367	-> 329
/*     */           //   #368	-> 339
/*     */           //   #362	-> 346
/*     */           //   #375	-> 352
/*     */           //   #376	-> 369
/*     */           //   #379	-> 372
/*     */           //   #380	-> 382
/*     */           //   #383	-> 410
/*     */           //   #384	-> 413
/*     */           //   #385	-> 428
/*     */           //   #386	-> 443
/*     */           //   #387	-> 446
/*     */           //   #384	-> 449
/*     */           //   #390	-> 455
/*     */           //   #391	-> 460
/*     */           //   #394	-> 463
/*     */           //   #395	-> 474
/*     */           //   #396	-> 485
/*     */           //   #397	-> 493
/*     */           //   #398	-> 510
/*     */           //   #399	-> 522
/*     */           //   #400	-> 539
/*     */           //   #394	-> 559
/*     */           //   #339	-> 565
/*     */           //   #407	-> 571
/*     */           //   #408	-> 581
/*     */           //   #409	-> 586
/*     */           //   #410	-> 597
/*     */           //   #411	-> 609
/*     */           //   #412	-> 622
/*     */           //   #414	-> 637
/*     */           //   #415	-> 659
/*     */           //   #417	-> 691
/*     */           //   #419	-> 695
/*     */           //   #409	-> 706
/*     */           //   #425	-> 712
/*     */           //   #426	-> 731
/*     */           //   #427	-> 745
/*     */           //   #428	-> 755
/*     */           //   #429	-> 767
/*     */           //   #430	-> 784
/*     */           //   #431	-> 790
/*     */           //   #432	-> 806
/*     */           //   #433	-> 822
/*     */           //   #435	-> 838
/*     */           //   #437	-> 854
/*     */           //   #438	-> 862
/*     */           //   #439	-> 867
/*     */           //   #442	-> 874
/*     */           //   #445	-> 916
/*     */           //   #446	-> 926
/*     */           //   #448	-> 934
/*     */           //   #449	-> 944
/*     */           //   #450	-> 966
/*     */           //   #453	-> 973
/*     */           //   #455	-> 976
/*     */           //   #456	-> 987
/*     */           //   #458	-> 998
/*     */           //   #459	-> 1006
/*     */           //   #460	-> 1012
/*     */           //   #461	-> 1027
/*     */           //   #462	-> 1043
/*     */           //   #463	-> 1055
/*     */           //   #464	-> 1060
/*     */           //   #467	-> 1063
/*     */           //   #461	-> 1084
/*     */           //   #470	-> 1090
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   150	18	10	old	I
/*     */           //   157	11	11	newId	I
/*     */           //   127	47	9	p	I
/*     */           //   190	40	10	p	I
/*     */           //   187	51	9	willSave	Z
/*     */           //   260	86	10	id	I
/*     */           //   309	37	11	position	Lcom/viaversion/viaversion/api/minecraft/Position;
/*     */           //   241	111	9	idx	I
/*     */           //   416	39	10	p	I
/*     */           //   510	49	12	globalX	I
/*     */           //   522	37	13	globalY	I
/*     */           //   539	20	14	globalZ	I
/*     */           //   485	74	11	id	I
/*     */           //   466	99	10	idx	I
/*     */           //   413	152	9	willSave	Z
/*     */           //   104	461	7	section	Lcom/viaversion/viaversion/api/minecraft/chunks/ChunkSection;
/*     */           //   124	441	8	blocks	Lcom/viaversion/viaversion/api/minecraft/chunks/DataPalette;
/*     */           //   79	492	6	s	I
/*     */           //   609	97	8	biome	I
/*     */           //   589	123	7	i	I
/*     */           //   586	126	6	latestBiomeWarn	I
/*     */           //   806	110	10	x	I
/*     */           //   822	94	11	y	I
/*     */           //   838	78	12	z	I
/*     */           //   854	62	13	position	Lcom/viaversion/viaversion/api/minecraft/Position;
/*     */           //   862	54	14	replacementData	Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/storage/BlockStorage$ReplacementData;
/*     */           //   944	29	11	id	Ljava/lang/String;
/*     */           //   767	206	8	tag	Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;
/*     */           //   784	189	9	newId	I
/*     */           //   926	47	10	idTag	Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/Tag;
/*     */           //   1055	29	10	section	Lcom/viaversion/viaversion/api/minecraft/chunks/ChunkSection;
/*     */           //   1030	60	9	i	I
/*     */           //   1027	63	8	updater	Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/ConnectionData$NeighbourUpdater;
/*     */           //   0	1091	0	wrapper	Lcom/viaversion/viaversion/api/protocol/packet/PacketWrapper;
/*     */           //   18	1073	1	clientWorld	Lcom/viaversion/viaversion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld;
/*     */           //   35	1056	2	storage	Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/storage/BlockStorage;
/*     */           //   44	1047	3	type	Lcom/viaversion/viaversion/protocols/protocol1_9_3to1_9_1_2/types/Chunk1_9_3_4Type;
/*     */           //   54	1037	4	type1_13	Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/types/Chunk1_13Type;
/*     */           //   66	1025	5	chunk	Lcom/viaversion/viaversion/api/minecraft/chunks/Chunk;
/*     */           //   731	360	6	provider	Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/providers/BlockEntityProvider;
/*     */           //   745	346	7	iterator	Ljava/util/Iterator;
/*     */           // Local variable type table:
/*     */           //   start	length	slot	name	signature
/*     */           //   745	346	7	iterator	Ljava/util/Iterator<Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;>;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 472 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_12_1.SPAWN_PARTICLE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 475 */             map((Type)Type.INT);
/* 476 */             map((Type)Type.BOOLEAN);
/* 477 */             map((Type)Type.FLOAT);
/* 478 */             map((Type)Type.FLOAT);
/* 479 */             map((Type)Type.FLOAT);
/* 480 */             map((Type)Type.FLOAT);
/* 481 */             map((Type)Type.FLOAT);
/* 482 */             map((Type)Type.FLOAT);
/* 483 */             map((Type)Type.FLOAT);
/* 484 */             map((Type)Type.INT);
/*     */             
/* 486 */             handler(wrapper -> {
/*     */                   int particleId = ((Integer)wrapper.get((Type)Type.INT, 0)).intValue();
/*     */                   
/*     */                   int dataCount = 0;
/*     */                   
/*     */                   if (particleId == 37 || particleId == 38 || particleId == 46) {
/*     */                     dataCount = 1;
/*     */                   } else if (particleId == 36) {
/*     */                     dataCount = 2;
/*     */                   } 
/*     */                   
/*     */                   Integer[] data = new Integer[dataCount];
/*     */                   
/*     */                   for (int i = 0; i < data.length; i++) {
/*     */                     data[i] = (Integer)wrapper.read((Type)Type.VAR_INT);
/*     */                   }
/*     */                   
/*     */                   Particle particle = ParticleRewriter.rewriteParticle(particleId, data);
/*     */                   
/*     */                   if (particle == null || particle.getId() == -1) {
/*     */                     wrapper.cancel();
/*     */                     
/*     */                     return;
/*     */                   } 
/*     */                   
/*     */                   if (particle.getId() == 11) {
/*     */                     int count = ((Integer)wrapper.get((Type)Type.INT, 1)).intValue();
/*     */                     
/*     */                     float speed = ((Float)wrapper.get((Type)Type.FLOAT, 6)).floatValue();
/*     */                     
/*     */                     if (count == 0) {
/*     */                       wrapper.set((Type)Type.INT, 1, Integer.valueOf(1));
/*     */                       
/*     */                       wrapper.set((Type)Type.FLOAT, 6, Float.valueOf(0.0F));
/*     */                       
/*     */                       List<Particle.ParticleData> arguments = particle.getArguments();
/*     */                       
/*     */                       for (int j = 0; j < 3; j++) {
/*     */                         float colorValue = ((Float)wrapper.get((Type)Type.FLOAT, j + 3)).floatValue() * speed;
/*     */                         
/*     */                         if (colorValue == 0.0F && j == 0) {
/*     */                           colorValue = 1.0F;
/*     */                         }
/*     */                         
/*     */                         ((Particle.ParticleData)arguments.get(j)).setValue(Float.valueOf(colorValue));
/*     */                         wrapper.set((Type)Type.FLOAT, j + 3, Float.valueOf(0.0F));
/*     */                       } 
/*     */                     } 
/*     */                   } 
/*     */                   wrapper.set((Type)Type.INT, 0, Integer.valueOf(particle.getId()));
/*     */                   for (Particle.ParticleData particleData : particle.getArguments()) {
/*     */                     wrapper.write(particleData.getType(), particleData.getValue());
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/* 542 */     protocol.registerServerbound((ServerboundPacketType)ServerboundPackets1_13.PLAYER_BLOCK_PLACEMENT, wrapper -> {
/*     */           Position pos = (Position)wrapper.passthrough(Type.POSITION);
/*     */           
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           wrapper.passthrough((Type)Type.FLOAT);
/*     */           wrapper.passthrough((Type)Type.FLOAT);
/*     */           wrapper.passthrough((Type)Type.FLOAT);
/*     */           if (Via.getConfig().isServersideBlockConnections() && ConnectionData.needStoreBlocks()) {
/*     */             ConnectionData.markModified(wrapper.user(), pos);
/*     */           }
/*     */         });
/* 554 */     protocol.registerServerbound((ServerboundPacketType)ServerboundPackets1_13.PLAYER_DIGGING, wrapper -> {
/*     */           int status = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           Position pos = (Position)wrapper.passthrough(Type.POSITION);
/*     */           wrapper.passthrough((Type)Type.UNSIGNED_BYTE);
/*     */           if (status == 0 && Via.getConfig().isServersideBlockConnections() && ConnectionData.needStoreBlocks()) {
/*     */             ConnectionData.markModified(wrapper.user(), pos);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toNewId(int oldId) {
/* 569 */     if (oldId < 0) {
/* 570 */       oldId = 0;
/*     */     }
/* 572 */     int newId = Protocol1_13To1_12_2.MAPPINGS.getBlockMappings().getNewId(oldId);
/* 573 */     if (newId != -1) {
/* 574 */       return newId;
/*     */     }
/* 576 */     newId = Protocol1_13To1_12_2.MAPPINGS.getBlockMappings().getNewId(oldId & 0xFFFFFFF0);
/* 577 */     if (newId != -1) {
/* 578 */       if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
/* 579 */         Via.getPlatform().getLogger().warning("Missing block " + oldId);
/*     */       }
/* 581 */       return newId;
/*     */     } 
/* 583 */     if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
/* 584 */       Via.getPlatform().getLogger().warning("Missing block completely " + oldId);
/*     */     }
/*     */     
/* 587 */     return 1;
/*     */   }
/*     */   
/*     */   private static int checkStorage(UserConnection user, Position position, int newId) {
/* 591 */     BlockStorage storage = (BlockStorage)user.get(BlockStorage.class);
/* 592 */     if (storage.contains(position)) {
/* 593 */       BlockStorage.ReplacementData data = storage.get(position);
/*     */       
/* 595 */       if (data.getOriginal() == newId) {
/* 596 */         if (data.getReplacement() != -1) {
/* 597 */           return data.getReplacement();
/*     */         }
/*     */       } else {
/* 600 */         storage.remove(position);
/*     */         
/* 602 */         if (storage.isWelcome(newId))
/* 603 */           storage.store(position, newId); 
/*     */       } 
/* 605 */     } else if (storage.isWelcome(newId)) {
/* 606 */       storage.store(position, newId);
/* 607 */     }  return newId;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_13to1_12_2\packets\WorldPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */