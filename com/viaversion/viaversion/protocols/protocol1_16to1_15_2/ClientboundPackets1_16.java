/*     */ package com.viaversion.viaversion.protocols.protocol1_16to1_15_2;
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
/*     */ public enum ClientboundPackets1_16
/*     */   implements ClientboundPacketType
/*     */ {
/*  24 */   SPAWN_ENTITY,
/*  25 */   SPAWN_EXPERIENCE_ORB,
/*  26 */   SPAWN_MOB,
/*  27 */   SPAWN_PAINTING,
/*  28 */   SPAWN_PLAYER,
/*  29 */   ENTITY_ANIMATION,
/*  30 */   STATISTICS,
/*  31 */   ACKNOWLEDGE_PLAYER_DIGGING,
/*  32 */   BLOCK_BREAK_ANIMATION,
/*  33 */   BLOCK_ENTITY_DATA,
/*  34 */   BLOCK_ACTION,
/*  35 */   BLOCK_CHANGE,
/*  36 */   BOSSBAR,
/*  37 */   SERVER_DIFFICULTY,
/*  38 */   CHAT_MESSAGE,
/*  39 */   MULTI_BLOCK_CHANGE,
/*  40 */   TAB_COMPLETE,
/*  41 */   DECLARE_COMMANDS,
/*  42 */   WINDOW_CONFIRMATION,
/*  43 */   CLOSE_WINDOW,
/*  44 */   WINDOW_ITEMS,
/*  45 */   WINDOW_PROPERTY,
/*  46 */   SET_SLOT,
/*  47 */   COOLDOWN,
/*  48 */   PLUGIN_MESSAGE,
/*  49 */   NAMED_SOUND,
/*  50 */   DISCONNECT,
/*  51 */   ENTITY_STATUS,
/*  52 */   EXPLOSION,
/*  53 */   UNLOAD_CHUNK,
/*  54 */   GAME_EVENT,
/*  55 */   OPEN_HORSE_WINDOW,
/*  56 */   KEEP_ALIVE,
/*  57 */   CHUNK_DATA,
/*  58 */   EFFECT,
/*  59 */   SPAWN_PARTICLE,
/*  60 */   UPDATE_LIGHT,
/*  61 */   JOIN_GAME,
/*  62 */   MAP_DATA,
/*  63 */   TRADE_LIST,
/*  64 */   ENTITY_POSITION,
/*  65 */   ENTITY_POSITION_AND_ROTATION,
/*  66 */   ENTITY_ROTATION,
/*  67 */   ENTITY_MOVEMENT,
/*  68 */   VEHICLE_MOVE,
/*  69 */   OPEN_BOOK,
/*  70 */   OPEN_WINDOW,
/*  71 */   OPEN_SIGN_EDITOR,
/*  72 */   CRAFT_RECIPE_RESPONSE,
/*  73 */   PLAYER_ABILITIES,
/*  74 */   COMBAT_EVENT,
/*  75 */   PLAYER_INFO,
/*  76 */   FACE_PLAYER,
/*  77 */   PLAYER_POSITION,
/*  78 */   UNLOCK_RECIPES,
/*  79 */   DESTROY_ENTITIES,
/*  80 */   REMOVE_ENTITY_EFFECT,
/*  81 */   RESOURCE_PACK,
/*  82 */   RESPAWN,
/*  83 */   ENTITY_HEAD_LOOK,
/*  84 */   SELECT_ADVANCEMENTS_TAB,
/*  85 */   WORLD_BORDER,
/*  86 */   CAMERA,
/*  87 */   HELD_ITEM_CHANGE,
/*  88 */   UPDATE_VIEW_POSITION,
/*  89 */   UPDATE_VIEW_DISTANCE,
/*  90 */   SPAWN_POSITION,
/*  91 */   DISPLAY_SCOREBOARD,
/*  92 */   ENTITY_METADATA,
/*  93 */   ATTACH_ENTITY,
/*  94 */   ENTITY_VELOCITY,
/*  95 */   ENTITY_EQUIPMENT,
/*  96 */   SET_EXPERIENCE,
/*  97 */   UPDATE_HEALTH,
/*  98 */   SCOREBOARD_OBJECTIVE,
/*  99 */   SET_PASSENGERS,
/* 100 */   TEAMS,
/* 101 */   UPDATE_SCORE,
/* 102 */   TIME_UPDATE,
/* 103 */   TITLE,
/* 104 */   ENTITY_SOUND,
/* 105 */   SOUND,
/* 106 */   STOP_SOUND,
/* 107 */   TAB_LIST,
/* 108 */   NBT_QUERY,
/* 109 */   COLLECT_ITEM,
/* 110 */   ENTITY_TELEPORT,
/* 111 */   ADVANCEMENTS,
/* 112 */   ENTITY_PROPERTIES,
/* 113 */   ENTITY_EFFECT,
/* 114 */   DECLARE_RECIPES,
/* 115 */   TAGS;
/*     */ 
/*     */   
/*     */   public int getId() {
/* 119 */     return ordinal();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 124 */     return name();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_16to1_15_2\ClientboundPackets1_16.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */