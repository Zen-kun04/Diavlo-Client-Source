/*     */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet;
/*     */ 
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ClientboundPackets1_20_2
/*     */   implements ClientboundPacketType
/*     */ {
/*  24 */   BUNDLE,
/*  25 */   SPAWN_ENTITY,
/*  26 */   SPAWN_EXPERIENCE_ORB,
/*  27 */   ENTITY_ANIMATION,
/*  28 */   STATISTICS,
/*  29 */   BLOCK_CHANGED_ACK,
/*  30 */   BLOCK_BREAK_ANIMATION,
/*  31 */   BLOCK_ENTITY_DATA,
/*  32 */   BLOCK_ACTION,
/*  33 */   BLOCK_CHANGE,
/*  34 */   BOSSBAR,
/*  35 */   SERVER_DIFFICULTY,
/*  36 */   CHUNK_BATCH_FINISHED,
/*  37 */   CHUNK_BATCH_START,
/*  38 */   CHUNK_BIOMES,
/*  39 */   CLEAR_TITLES,
/*  40 */   TAB_COMPLETE,
/*  41 */   DECLARE_COMMANDS,
/*  42 */   CLOSE_WINDOW,
/*  43 */   WINDOW_ITEMS,
/*  44 */   WINDOW_PROPERTY,
/*  45 */   SET_SLOT,
/*  46 */   COOLDOWN,
/*  47 */   CUSTOM_CHAT_COMPLETIONS,
/*  48 */   PLUGIN_MESSAGE,
/*  49 */   DAMAGE_EVENT,
/*  50 */   DELETE_CHAT_MESSAGE,
/*  51 */   DISCONNECT,
/*  52 */   DISGUISED_CHAT,
/*  53 */   ENTITY_STATUS,
/*  54 */   EXPLOSION,
/*  55 */   UNLOAD_CHUNK,
/*  56 */   GAME_EVENT,
/*  57 */   OPEN_HORSE_WINDOW,
/*  58 */   HIT_ANIMATION,
/*  59 */   WORLD_BORDER_INIT,
/*  60 */   KEEP_ALIVE,
/*  61 */   CHUNK_DATA,
/*  62 */   EFFECT,
/*  63 */   SPAWN_PARTICLE,
/*  64 */   UPDATE_LIGHT,
/*  65 */   JOIN_GAME,
/*  66 */   MAP_DATA,
/*  67 */   TRADE_LIST,
/*  68 */   ENTITY_POSITION,
/*  69 */   ENTITY_POSITION_AND_ROTATION,
/*  70 */   ENTITY_ROTATION,
/*  71 */   VEHICLE_MOVE,
/*  72 */   OPEN_BOOK,
/*  73 */   OPEN_WINDOW,
/*  74 */   OPEN_SIGN_EDITOR,
/*  75 */   PING,
/*  76 */   PONG_RESPONSE,
/*  77 */   CRAFT_RECIPE_RESPONSE,
/*  78 */   PLAYER_ABILITIES,
/*  79 */   PLAYER_CHAT,
/*  80 */   COMBAT_END,
/*  81 */   COMBAT_ENTER,
/*  82 */   COMBAT_KILL,
/*  83 */   PLAYER_INFO_REMOVE,
/*  84 */   PLAYER_INFO_UPDATE,
/*  85 */   FACE_PLAYER,
/*  86 */   PLAYER_POSITION,
/*  87 */   UNLOCK_RECIPES,
/*  88 */   REMOVE_ENTITIES,
/*  89 */   REMOVE_ENTITY_EFFECT,
/*  90 */   RESOURCE_PACK,
/*  91 */   RESPAWN,
/*  92 */   ENTITY_HEAD_LOOK,
/*  93 */   MULTI_BLOCK_CHANGE,
/*  94 */   SELECT_ADVANCEMENTS_TAB,
/*  95 */   SERVER_DATA,
/*  96 */   ACTIONBAR,
/*  97 */   WORLD_BORDER_CENTER,
/*  98 */   WORLD_BORDER_LERP_SIZE,
/*  99 */   WORLD_BORDER_SIZE,
/* 100 */   WORLD_BORDER_WARNING_DELAY,
/* 101 */   WORLD_BORDER_WARNING_DISTANCE,
/* 102 */   CAMERA,
/* 103 */   HELD_ITEM_CHANGE,
/* 104 */   UPDATE_VIEW_POSITION,
/* 105 */   UPDATE_VIEW_DISTANCE,
/* 106 */   SPAWN_POSITION,
/* 107 */   DISPLAY_SCOREBOARD,
/* 108 */   ENTITY_METADATA,
/* 109 */   ATTACH_ENTITY,
/* 110 */   ENTITY_VELOCITY,
/* 111 */   ENTITY_EQUIPMENT,
/* 112 */   SET_EXPERIENCE,
/* 113 */   UPDATE_HEALTH,
/* 114 */   SCOREBOARD_OBJECTIVE,
/* 115 */   SET_PASSENGERS,
/* 116 */   TEAMS,
/* 117 */   UPDATE_SCORE,
/* 118 */   SET_SIMULATION_DISTANCE,
/* 119 */   TITLE_SUBTITLE,
/* 120 */   TIME_UPDATE,
/* 121 */   TITLE_TEXT,
/* 122 */   TITLE_TIMES,
/* 123 */   ENTITY_SOUND,
/* 124 */   SOUND,
/* 125 */   START_CONFIGURATION,
/* 126 */   STOP_SOUND,
/* 127 */   SYSTEM_CHAT,
/* 128 */   TAB_LIST,
/* 129 */   NBT_QUERY,
/* 130 */   COLLECT_ITEM,
/* 131 */   ENTITY_TELEPORT,
/* 132 */   ADVANCEMENTS,
/* 133 */   ENTITY_PROPERTIES,
/* 134 */   ENTITY_EFFECT,
/* 135 */   DECLARE_RECIPES,
/* 136 */   TAGS;
/*     */ 
/*     */   
/*     */   public int getId() {
/* 140 */     return ordinal();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 145 */     return name();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\packet\ClientboundPackets1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */