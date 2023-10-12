/*     */ package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.MappingDataLoader;
/*     */ import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_8;
/*     */ import com.viaversion.viaversion.api.minecraft.BlockFace;
/*     */ import com.viaversion.viaversion.api.minecraft.Position;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
/*     */ import com.viaversion.viaversion.api.platform.providers.Provider;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.PacketBlockConnectionProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.UserBlockData;
/*     */ import com.viaversion.viaversion.util.Key;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class ConnectionData
/*     */ {
/*     */   public static BlockConnectionProvider blockConnectionProvider;
/*  56 */   static final Object2IntMap<String> KEY_TO_ID = (Object2IntMap<String>)new Object2IntOpenHashMap(8582, 0.99F);
/*  57 */   static final IntSet OCCLUDING_STATES = (IntSet)new IntOpenHashSet(377, 0.99F);
/*  58 */   static Int2ObjectMap<ConnectionHandler> connectionHandlerMap = (Int2ObjectMap<ConnectionHandler>)new Int2ObjectOpenHashMap();
/*  59 */   static Int2ObjectMap<BlockData> blockConnectionData = (Int2ObjectMap<BlockData>)new Int2ObjectOpenHashMap();
/*  60 */   private static final BlockChangeRecord1_8[] EMPTY_RECORDS = new BlockChangeRecord1_8[0];
/*     */   
/*     */   static {
/*  63 */     KEY_TO_ID.defaultReturnValue(-1);
/*     */   }
/*     */   
/*     */   public static void update(UserConnection user, Position position) throws Exception {
/*  67 */     Boolean inSync = null;
/*     */     
/*  69 */     for (BlockFace face : BlockFace.values()) {
/*  70 */       Position pos = position.getRelative(face);
/*  71 */       int blockState = blockConnectionProvider.getBlockData(user, pos.x(), pos.y(), pos.z());
/*  72 */       ConnectionHandler handler = (ConnectionHandler)connectionHandlerMap.get(blockState);
/*  73 */       if (handler == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  77 */       int newBlockState = handler.connect(user, pos, blockState);
/*  78 */       if (newBlockState == blockState) {
/*  79 */         if (inSync == null) {
/*  80 */           inSync = Boolean.valueOf(blockConnectionProvider.storesBlocks(user, position));
/*     */         }
/*     */         
/*  83 */         if (inSync.booleanValue()) {
/*     */           continue;
/*     */         }
/*     */       } 
/*     */       
/*  88 */       updateBlockStorage(user, pos.x(), pos.y(), pos.z(), newBlockState);
/*     */       
/*  90 */       PacketWrapper blockUpdatePacket = PacketWrapper.create((PacketType)ClientboundPackets1_13.BLOCK_CHANGE, null, user);
/*  91 */       blockUpdatePacket.write(Type.POSITION, pos);
/*  92 */       blockUpdatePacket.write((Type)Type.VAR_INT, Integer.valueOf(newBlockState));
/*  93 */       blockUpdatePacket.send(Protocol1_13To1_12_2.class);
/*     */       continue;
/*     */     } 
/*     */   }
/*     */   public static void updateBlockStorage(UserConnection userConnection, int x, int y, int z, int blockState) {
/*  98 */     if (!needStoreBlocks())
/*  99 */       return;  if (isWelcome(blockState)) {
/* 100 */       blockConnectionProvider.storeBlock(userConnection, x, y, z, blockState);
/*     */     } else {
/* 102 */       blockConnectionProvider.removeBlock(userConnection, x, y, z);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void clearBlockStorage(UserConnection connection) {
/* 107 */     if (!needStoreBlocks())
/* 108 */       return;  blockConnectionProvider.clearStorage(connection);
/*     */   }
/*     */   
/*     */   public static void markModified(UserConnection connection, Position pos) {
/* 112 */     if (!needStoreBlocks())
/* 113 */       return;  blockConnectionProvider.modifiedBlock(connection, pos);
/*     */   }
/*     */   
/*     */   public static boolean needStoreBlocks() {
/* 117 */     return blockConnectionProvider.storesBlocks(null, null);
/*     */   }
/*     */   
/*     */   public static void connectBlocks(UserConnection user, Chunk chunk) {
/* 121 */     int xOff = chunk.getX() << 4;
/* 122 */     int zOff = chunk.getZ() << 4;
/*     */     
/* 124 */     for (int s = 0; s < (chunk.getSections()).length; s++) {
/* 125 */       ChunkSection section = chunk.getSections()[s];
/* 126 */       if (section != null) {
/*     */ 
/*     */ 
/*     */         
/* 130 */         DataPalette blocks = section.palette(PaletteType.BLOCKS);
/*     */         
/* 132 */         boolean willConnect = false;
/* 133 */         for (int p = 0; p < blocks.size(); p++) {
/* 134 */           int id = blocks.idByIndex(p);
/* 135 */           if (connects(id)) {
/* 136 */             willConnect = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 140 */         if (willConnect) {
/*     */ 
/*     */ 
/*     */           
/* 144 */           int yOff = s << 4;
/*     */           
/* 146 */           for (int idx = 0; idx < 4096; idx++) {
/* 147 */             int id = blocks.idAt(idx);
/* 148 */             ConnectionHandler handler = getConnectionHandler(id);
/* 149 */             if (handler != null) {
/*     */ 
/*     */ 
/*     */               
/* 153 */               Position position = new Position(xOff + ChunkSection.xFromIndex(idx), yOff + ChunkSection.yFromIndex(idx), zOff + ChunkSection.zFromIndex(idx));
/* 154 */               int connectedId = handler.connect(user, position, id);
/* 155 */               if (connectedId != id) {
/* 156 */                 blocks.setIdAt(idx, connectedId);
/* 157 */                 updateBlockStorage(user, position.x(), position.y(), position.z(), connectedId);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }  } public static void init() {
/* 164 */     if (!Via.getConfig().isServersideBlockConnections()) {
/*     */       return;
/*     */     }
/*     */     
/* 168 */     Via.getPlatform().getLogger().info("Loading block connection mappings ...");
/* 169 */     ListTag blockStates = (ListTag)MappingDataLoader.loadNBT("blockstates-1.13.nbt").get("blockstates");
/* 170 */     for (int id = 0; id < blockStates.size(); id++) {
/* 171 */       String key = (String)blockStates.get(id).getValue();
/* 172 */       KEY_TO_ID.put(key, id);
/*     */     } 
/*     */     
/* 175 */     connectionHandlerMap = (Int2ObjectMap<ConnectionHandler>)new Int2ObjectOpenHashMap(3650, 0.99F);
/*     */     
/* 177 */     if (!Via.getConfig().isReduceBlockStorageMemory()) {
/* 178 */       blockConnectionData = (Int2ObjectMap<BlockData>)new Int2ObjectOpenHashMap(2048);
/*     */       
/* 180 */       ListTag blockConnectionMappings = (ListTag)MappingDataLoader.loadNBT("blockConnections.nbt").get("data");
/* 181 */       for (Tag blockTag : blockConnectionMappings) {
/* 182 */         CompoundTag blockCompoundTag = (CompoundTag)blockTag;
/* 183 */         BlockData blockData = new BlockData();
/* 184 */         for (Map.Entry<String, Tag> entry : (Iterable<Map.Entry<String, Tag>>)blockCompoundTag.entrySet()) {
/* 185 */           String key = entry.getKey();
/* 186 */           if (key.equals("id") || key.equals("ids")) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */           
/* 191 */           boolean[] attachingFaces = new boolean[4];
/* 192 */           ByteArrayTag connections = (ByteArrayTag)entry.getValue();
/* 193 */           for (byte blockFaceId : connections.getValue()) {
/* 194 */             attachingFaces[blockFaceId] = true;
/*     */           }
/*     */           
/* 197 */           int connectionTypeId = Integer.parseInt(key);
/* 198 */           blockData.put(connectionTypeId, attachingFaces);
/*     */         } 
/*     */         
/* 201 */         NumberTag idTag = (NumberTag)blockCompoundTag.get("id");
/* 202 */         if (idTag != null) {
/* 203 */           blockConnectionData.put(idTag.asInt(), blockData); continue;
/*     */         } 
/* 205 */         IntArrayTag idsTag = (IntArrayTag)blockCompoundTag.get("ids");
/* 206 */         for (int i : idsTag.getValue()) {
/* 207 */           blockConnectionData.put(i, blockData);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 213 */     for (String state : occludingBlockStates()) {
/* 214 */       OCCLUDING_STATES.add(KEY_TO_ID.getInt(state));
/*     */     }
/*     */     
/* 217 */     List<ConnectorInitAction> initActions = new ArrayList<>();
/* 218 */     initActions.add(PumpkinConnectionHandler.init());
/* 219 */     initActions.addAll(BasicFenceConnectionHandler.init());
/* 220 */     initActions.add(NetherFenceConnectionHandler.init());
/* 221 */     initActions.addAll(WallConnectionHandler.init());
/* 222 */     initActions.add(MelonConnectionHandler.init());
/* 223 */     initActions.addAll(GlassConnectionHandler.init());
/* 224 */     initActions.add(ChestConnectionHandler.init());
/* 225 */     initActions.add(DoorConnectionHandler.init());
/* 226 */     initActions.add(RedstoneConnectionHandler.init());
/* 227 */     initActions.add(StairConnectionHandler.init());
/* 228 */     initActions.add(FlowerConnectionHandler.init());
/* 229 */     initActions.addAll(ChorusPlantConnectionHandler.init());
/* 230 */     initActions.add(TripwireConnectionHandler.init());
/* 231 */     initActions.add(SnowyGrassConnectionHandler.init());
/* 232 */     initActions.add(FireConnectionHandler.init());
/* 233 */     if (Via.getConfig().isVineClimbFix()) {
/* 234 */       initActions.add(VineConnectionHandler.init());
/*     */     }
/*     */     
/* 237 */     for (ObjectIterator<String> objectIterator = KEY_TO_ID.keySet().iterator(); objectIterator.hasNext(); ) { String key = objectIterator.next();
/* 238 */       WrappedBlockData wrappedBlockData = WrappedBlockData.fromString(key);
/* 239 */       for (ConnectorInitAction action : initActions) {
/* 240 */         action.check(wrappedBlockData);
/*     */       } }
/*     */ 
/*     */     
/* 244 */     if (Via.getConfig().getBlockConnectionMethod().equalsIgnoreCase("packet")) {
/* 245 */       blockConnectionProvider = (BlockConnectionProvider)new PacketBlockConnectionProvider();
/* 246 */       Via.getManager().getProviders().register(BlockConnectionProvider.class, (Provider)blockConnectionProvider);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isWelcome(int blockState) {
/* 251 */     return (blockConnectionData.containsKey(blockState) || connectionHandlerMap.containsKey(blockState));
/*     */   }
/*     */   
/*     */   public static boolean connects(int blockState) {
/* 255 */     return connectionHandlerMap.containsKey(blockState);
/*     */   }
/*     */   
/*     */   public static int connect(UserConnection user, Position position, int blockState) {
/* 259 */     ConnectionHandler handler = (ConnectionHandler)connectionHandlerMap.get(blockState);
/* 260 */     return (handler != null) ? handler.connect(user, position, blockState) : blockState;
/*     */   }
/*     */   
/*     */   public static ConnectionHandler getConnectionHandler(int blockstate) {
/* 264 */     return (ConnectionHandler)connectionHandlerMap.get(blockstate);
/*     */   }
/*     */   
/*     */   public static int getId(String key) {
/* 268 */     return KEY_TO_ID.getOrDefault(Key.stripMinecraftNamespace(key), -1);
/*     */   }
/*     */   
/*     */   private static String[] occludingBlockStates() {
/* 272 */     return new String[] { "stone", "granite", "polished_granite", "diorite", "polished_diorite", "andesite", "polished_andesite", "grass_block[snowy=false]", "dirt", "coarse_dirt", "podzol[snowy=false]", "cobblestone", "oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks", "bedrock", "sand", "red_sand", "gravel", "gold_ore", "iron_ore", "coal_ore", "oak_log[axis=x]", "oak_log[axis=y]", "oak_log[axis=z]", "spruce_log[axis=x]", "spruce_log[axis=y]", "spruce_log[axis=z]", "birch_log[axis=x]", "birch_log[axis=y]", "birch_log[axis=z]", "jungle_log[axis=x]", "jungle_log[axis=y]", "jungle_log[axis=z]", "acacia_log[axis=x]", "acacia_log[axis=y]", "acacia_log[axis=z]", "dark_oak_log[axis=x]", "dark_oak_log[axis=y]", "dark_oak_log[axis=z]", "oak_wood[axis=y]", "spruce_wood[axis=y]", "birch_wood[axis=y]", "jungle_wood[axis=y]", "acacia_wood[axis=y]", "dark_oak_wood[axis=y]", "sponge", "wet_sponge", "lapis_ore", "lapis_block", "dispenser[facing=north,triggered=true]", "dispenser[facing=north,triggered=false]", "dispenser[facing=east,triggered=true]", "dispenser[facing=east,triggered=false]", "dispenser[facing=south,triggered=true]", "dispenser[facing=south,triggered=false]", "dispenser[facing=west,triggered=true]", "dispenser[facing=west,triggered=false]", "dispenser[facing=up,triggered=true]", "dispenser[facing=up,triggered=false]", "dispenser[facing=down,triggered=true]", "dispenser[facing=down,triggered=false]", "sandstone", "chiseled_sandstone", "cut_sandstone", "note_block[instrument=harp,note=0,powered=false]", "white_wool", "orange_wool", "magenta_wool", "light_blue_wool", "yellow_wool", "lime_wool", "pink_wool", "gray_wool", "light_gray_wool", "cyan_wool", "purple_wool", "blue_wool", "brown_wool", "green_wool", "red_wool", "black_wool", "gold_block", "iron_block", "bricks", "bookshelf", "mossy_cobblestone", "obsidian", "spawner", "diamond_ore", "diamond_block", "crafting_table", "furnace[facing=north,lit=true]", "furnace[facing=north,lit=false]", "furnace[facing=south,lit=true]", "furnace[facing=south,lit=false]", "furnace[facing=west,lit=true]", "furnace[facing=west,lit=false]", "furnace[facing=east,lit=true]", "furnace[facing=east,lit=false]", "redstone_ore[lit=true]", "redstone_ore[lit=false]", "snow_block", "clay", "jukebox[has_record=true]", "jukebox[has_record=false]", "netherrack", "soul_sand", "carved_pumpkin[facing=north]", "carved_pumpkin[facing=south]", "carved_pumpkin[facing=west]", "carved_pumpkin[facing=east]", "jack_o_lantern[facing=north]", "jack_o_lantern[facing=south]", "jack_o_lantern[facing=west]", "jack_o_lantern[facing=east]", "infested_stone", "infested_cobblestone", "infested_stone_bricks", "infested_mossy_stone_bricks", "infested_cracked_stone_bricks", "infested_chiseled_stone_bricks", "stone_bricks", "mossy_stone_bricks", "cracked_stone_bricks", "chiseled_stone_bricks", "brown_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]", "brown_mushroom_block[down=false,east=true,north=true,south=false,up=true,west=false]", "brown_mushroom_block[down=false,east=true,north=false,south=true,up=true,west=false]", "brown_mushroom_block[down=false,east=true,north=false,south=false,up=true,west=false]", "brown_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=true]", "brown_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=false]", "brown_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=true]", "brown_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=false]", "brown_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=true]", "brown_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=false]", "brown_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=false]", "red_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]", "red_mushroom_block[down=false,east=true,north=true,south=false,up=true,west=false]", "red_mushroom_block[down=false,east=true,north=false,south=true,up=true,west=false]", "red_mushroom_block[down=false,east=true,north=false,south=false,up=true,west=false]", "red_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=true]", "red_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=false]", "red_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=true]", "red_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=false]", "red_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=true]", "red_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=false]", "red_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=false]", "mushroom_stem[down=true,east=true,north=true,south=true,up=true,west=true]", "mushroom_stem[down=false,east=true,north=true,south=true,up=false,west=true]", "melon", "mycelium[snowy=false]", "nether_bricks", "end_stone", "redstone_lamp[lit=true]", "redstone_lamp[lit=false]", "emerald_ore", "emerald_block", "command_block[conditional=true,facing=north]", "command_block[conditional=true,facing=east]", "command_block[conditional=true,facing=south]", "command_block[conditional=true,facing=west]", "command_block[conditional=true,facing=up]", "command_block[conditional=true,facing=down]", "command_block[conditional=false,facing=north]", "command_block[conditional=false,facing=east]", "command_block[conditional=false,facing=south]", "command_block[conditional=false,facing=west]", "command_block[conditional=false,facing=up]", "command_block[conditional=false,facing=down]", "nether_quartz_ore", "quartz_block", "chiseled_quartz_block", "quartz_pillar[axis=x]", "quartz_pillar[axis=y]", "quartz_pillar[axis=z]", "dropper[facing=north,triggered=true]", "dropper[facing=north,triggered=false]", "dropper[facing=east,triggered=true]", "dropper[facing=east,triggered=false]", "dropper[facing=south,triggered=true]", "dropper[facing=south,triggered=false]", "dropper[facing=west,triggered=true]", "dropper[facing=west,triggered=false]", "dropper[facing=up,triggered=true]", "dropper[facing=up,triggered=false]", "dropper[facing=down,triggered=true]", "dropper[facing=down,triggered=false]", "white_terracotta", "orange_terracotta", "magenta_terracotta", "light_blue_terracotta", "yellow_terracotta", "lime_terracotta", "pink_terracotta", "gray_terracotta", "light_gray_terracotta", "cyan_terracotta", "purple_terracotta", "blue_terracotta", "brown_terracotta", "green_terracotta", "red_terracotta", "black_terracotta", "slime_block", "barrier", "prismarine", "prismarine_bricks", "dark_prismarine", "hay_block[axis=x]", "hay_block[axis=y]", "hay_block[axis=z]", "terracotta", "coal_block", "packed_ice", "red_sandstone", "chiseled_red_sandstone", "cut_red_sandstone", "oak_slab[type=double,waterlogged=false]", "spruce_slab[type=double,waterlogged=false]", "birch_slab[type=double,waterlogged=false]", "jungle_slab[type=double,waterlogged=false]", "acacia_slab[type=double,waterlogged=false]", "dark_oak_slab[type=double,waterlogged=false]", "stone_slab[type=double,waterlogged=false]", "sandstone_slab[type=double,waterlogged=false]", "petrified_oak_slab[type=double,waterlogged=false]", "cobblestone_slab[type=double,waterlogged=false]", "brick_slab[type=double,waterlogged=false]", "stone_brick_slab[type=double,waterlogged=false]", "nether_brick_slab[type=double,waterlogged=false]", "quartz_slab[type=double,waterlogged=false]", "red_sandstone_slab[type=double,waterlogged=false]", "purpur_slab[type=double,waterlogged=false]", "smooth_stone", "smooth_sandstone", "smooth_quartz", "smooth_red_sandstone", "purpur_block", "purpur_pillar[axis=x]", "purpur_pillar[axis=y]", "purpur_pillar[axis=z]", "end_stone_bricks", "repeating_command_block[conditional=true,facing=north]", "repeating_command_block[conditional=true,facing=east]", "repeating_command_block[conditional=true,facing=south]", "repeating_command_block[conditional=true,facing=west]", "repeating_command_block[conditional=true,facing=up]", "repeating_command_block[conditional=true,facing=down]", "repeating_command_block[conditional=false,facing=north]", "repeating_command_block[conditional=false,facing=east]", "repeating_command_block[conditional=false,facing=south]", "repeating_command_block[conditional=false,facing=west]", "repeating_command_block[conditional=false,facing=up]", "repeating_command_block[conditional=false,facing=down]", "chain_command_block[conditional=true,facing=north]", "chain_command_block[conditional=true,facing=east]", "chain_command_block[conditional=true,facing=south]", "chain_command_block[conditional=true,facing=west]", "chain_command_block[conditional=true,facing=up]", "chain_command_block[conditional=true,facing=down]", "chain_command_block[conditional=false,facing=north]", "chain_command_block[conditional=false,facing=east]", "chain_command_block[conditional=false,facing=south]", "chain_command_block[conditional=false,facing=west]", "chain_command_block[conditional=false,facing=up]", "chain_command_block[conditional=false,facing=down]", "magma_block", "nether_wart_block", "red_nether_bricks", "bone_block[axis=x]", "bone_block[axis=y]", "bone_block[axis=z]", "white_glazed_terracotta[facing=north]", "white_glazed_terracotta[facing=south]", "white_glazed_terracotta[facing=west]", "white_glazed_terracotta[facing=east]", "orange_glazed_terracotta[facing=north]", "orange_glazed_terracotta[facing=south]", "orange_glazed_terracotta[facing=west]", "orange_glazed_terracotta[facing=east]", "magenta_glazed_terracotta[facing=north]", "magenta_glazed_terracotta[facing=south]", "magenta_glazed_terracotta[facing=west]", "magenta_glazed_terracotta[facing=east]", "light_blue_glazed_terracotta[facing=north]", "light_blue_glazed_terracotta[facing=south]", "light_blue_glazed_terracotta[facing=west]", "light_blue_glazed_terracotta[facing=east]", "yellow_glazed_terracotta[facing=north]", "yellow_glazed_terracotta[facing=south]", "yellow_glazed_terracotta[facing=west]", "yellow_glazed_terracotta[facing=east]", "lime_glazed_terracotta[facing=north]", "lime_glazed_terracotta[facing=south]", "lime_glazed_terracotta[facing=west]", "lime_glazed_terracotta[facing=east]", "pink_glazed_terracotta[facing=north]", "pink_glazed_terracotta[facing=south]", "pink_glazed_terracotta[facing=west]", "pink_glazed_terracotta[facing=east]", "gray_glazed_terracotta[facing=north]", "gray_glazed_terracotta[facing=south]", "gray_glazed_terracotta[facing=west]", "gray_glazed_terracotta[facing=east]", "light_gray_glazed_terracotta[facing=north]", "light_gray_glazed_terracotta[facing=south]", "light_gray_glazed_terracotta[facing=west]", "light_gray_glazed_terracotta[facing=east]", "cyan_glazed_terracotta[facing=north]", "cyan_glazed_terracotta[facing=south]", "cyan_glazed_terracotta[facing=west]", "cyan_glazed_terracotta[facing=east]", "purple_glazed_terracotta[facing=north]", "purple_glazed_terracotta[facing=south]", "purple_glazed_terracotta[facing=west]", "purple_glazed_terracotta[facing=east]", "blue_glazed_terracotta[facing=north]", "blue_glazed_terracotta[facing=south]", "blue_glazed_terracotta[facing=west]", "blue_glazed_terracotta[facing=east]", "brown_glazed_terracotta[facing=north]", "brown_glazed_terracotta[facing=south]", "brown_glazed_terracotta[facing=west]", "brown_glazed_terracotta[facing=east]", "green_glazed_terracotta[facing=north]", "green_glazed_terracotta[facing=south]", "green_glazed_terracotta[facing=west]", "green_glazed_terracotta[facing=east]", "red_glazed_terracotta[facing=north]", "red_glazed_terracotta[facing=south]", "red_glazed_terracotta[facing=west]", "red_glazed_terracotta[facing=east]", "black_glazed_terracotta[facing=north]", "black_glazed_terracotta[facing=south]", "black_glazed_terracotta[facing=west]", "black_glazed_terracotta[facing=east]", "white_concrete", "orange_concrete", "magenta_concrete", "light_blue_concrete", "yellow_concrete", "lime_concrete", "pink_concrete", "gray_concrete", "light_gray_concrete", "cyan_concrete", "purple_concrete", "blue_concrete", "brown_concrete", "green_concrete", "red_concrete", "black_concrete", "white_concrete_powder", "orange_concrete_powder", "magenta_concrete_powder", "light_blue_concrete_powder", "yellow_concrete_powder", "lime_concrete_powder", "pink_concrete_powder", "gray_concrete_powder", "light_gray_concrete_powder", "cyan_concrete_powder", "purple_concrete_powder", "blue_concrete_powder", "brown_concrete_powder", "green_concrete_powder", "red_concrete_powder", "black_concrete_powder", "structure_block[mode=save]", "structure_block[mode=load]", "structure_block[mode=corner]", "structure_block[mode=data]", "glowstone" };
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
/*     */   public static Object2IntMap<String> getKeyToId() {
/* 660 */     return KEY_TO_ID;
/*     */   }
/*     */   
/*     */   public static final class NeighbourUpdater {
/*     */     private final UserConnection user;
/*     */     private final UserBlockData userBlockData;
/*     */     
/*     */     public NeighbourUpdater(UserConnection user) {
/* 668 */       this.user = user;
/* 669 */       this.userBlockData = ConnectionData.blockConnectionProvider.forUser(user);
/*     */     }
/*     */     
/*     */     public void updateChunkSectionNeighbours(int chunkX, int chunkZ, int chunkSectionY) throws Exception {
/* 673 */       int chunkMinY = chunkSectionY << 4;
/* 674 */       List<BlockChangeRecord1_8> updates = new ArrayList<>();
/* 675 */       for (int chunkDeltaX = -1; chunkDeltaX <= 1; chunkDeltaX++) {
/* 676 */         for (int chunkDeltaZ = -1; chunkDeltaZ <= 1; chunkDeltaZ++) {
/* 677 */           int distance = Math.abs(chunkDeltaX) + Math.abs(chunkDeltaZ);
/* 678 */           if (distance != 0) {
/*     */             
/* 680 */             int chunkMinX = chunkX + chunkDeltaX << 4;
/* 681 */             int chunkMinZ = chunkZ + chunkDeltaZ << 4;
/* 682 */             if (distance == 2) {
/* 683 */               for (int blockY = chunkMinY; blockY < chunkMinY + 16; blockY++) {
/* 684 */                 int blockPosX = (chunkDeltaX == 1) ? 0 : 15;
/* 685 */                 int blockPosZ = (chunkDeltaZ == 1) ? 0 : 15;
/* 686 */                 updateBlock(chunkMinX + blockPosX, blockY, chunkMinZ + blockPosZ, updates);
/*     */               } 
/*     */             } else {
/* 689 */               for (int blockY = chunkMinY; blockY < chunkMinY + 16; blockY++) {
/*     */                 int xStart; int xEnd; int zStart;
/*     */                 int zEnd;
/* 692 */                 if (chunkDeltaX == 1) {
/* 693 */                   xStart = 0;
/* 694 */                   xEnd = 2;
/* 695 */                   zStart = 0;
/* 696 */                   zEnd = 16;
/* 697 */                 } else if (chunkDeltaX == -1) {
/* 698 */                   xStart = 14;
/* 699 */                   xEnd = 16;
/* 700 */                   zStart = 0;
/* 701 */                   zEnd = 16;
/* 702 */                 } else if (chunkDeltaZ == 1) {
/* 703 */                   xStart = 0;
/* 704 */                   xEnd = 16;
/* 705 */                   zStart = 0;
/* 706 */                   zEnd = 2;
/*     */                 } else {
/* 708 */                   xStart = 0;
/* 709 */                   xEnd = 16;
/* 710 */                   zStart = 14;
/* 711 */                   zEnd = 16;
/*     */                 } 
/* 713 */                 for (int blockX = xStart; blockX < xEnd; blockX++) {
/* 714 */                   for (int blockZ = zStart; blockZ < zEnd; blockZ++) {
/* 715 */                     updateBlock(chunkMinX + blockX, blockY, chunkMinZ + blockZ, updates);
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             
/* 721 */             if (!updates.isEmpty()) {
/* 722 */               PacketWrapper wrapper = PacketWrapper.create((PacketType)ClientboundPackets1_13.MULTI_BLOCK_CHANGE, null, this.user);
/* 723 */               wrapper.write((Type)Type.INT, Integer.valueOf(chunkX + chunkDeltaX));
/* 724 */               wrapper.write((Type)Type.INT, Integer.valueOf(chunkZ + chunkDeltaZ));
/* 725 */               wrapper.write(Type.BLOCK_CHANGE_RECORD_ARRAY, updates.toArray(ConnectionData.EMPTY_RECORDS));
/* 726 */               wrapper.send(Protocol1_13To1_12_2.class);
/* 727 */               updates.clear();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     private void updateBlock(int x, int y, int z, List<BlockChangeRecord1_8> records) {
/* 734 */       int blockState = this.userBlockData.getBlockData(x, y, z);
/* 735 */       ConnectionHandler handler = ConnectionData.getConnectionHandler(blockState);
/* 736 */       if (handler == null) {
/*     */         return;
/*     */       }
/*     */       
/* 740 */       Position pos = new Position(x, y, z);
/* 741 */       int newBlockState = handler.connect(this.user, pos, blockState);
/* 742 */       if (blockState != newBlockState || !ConnectionData.blockConnectionProvider.storesBlocks(this.user, null)) {
/* 743 */         records.add(new BlockChangeRecord1_8(x & 0xF, y, z & 0xF, newBlockState));
/* 744 */         ConnectionData.updateBlockStorage(this.user, x, y, z, newBlockState);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface ConnectorInitAction {
/*     */     void check(WrappedBlockData param1WrappedBlockData);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_13to1_12_2\blockconnections\ConnectionData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */