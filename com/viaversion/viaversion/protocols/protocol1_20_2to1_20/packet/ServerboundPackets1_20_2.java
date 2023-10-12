/*    */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ServerboundPackets1_20_2
/*    */   implements ServerboundPacketType
/*    */ {
/* 24 */   TELEPORT_CONFIRM,
/* 25 */   QUERY_BLOCK_NBT,
/* 26 */   SET_DIFFICULTY,
/* 27 */   CHAT_ACK,
/* 28 */   CHAT_COMMAND,
/* 29 */   CHAT_MESSAGE,
/* 30 */   CHAT_SESSION_UPDATE,
/* 31 */   CHUNK_BATCH_RECEIVED,
/* 32 */   CLIENT_STATUS,
/* 33 */   CLIENT_SETTINGS,
/* 34 */   TAB_COMPLETE,
/* 35 */   CONFIGURATION_ACKNOWLEDGED,
/* 36 */   CLICK_WINDOW_BUTTON,
/* 37 */   CLICK_WINDOW,
/* 38 */   CLOSE_WINDOW,
/* 39 */   PLUGIN_MESSAGE,
/* 40 */   EDIT_BOOK,
/* 41 */   ENTITY_NBT_REQUEST,
/* 42 */   INTERACT_ENTITY,
/* 43 */   GENERATE_JIGSAW,
/* 44 */   KEEP_ALIVE,
/* 45 */   LOCK_DIFFICULTY,
/* 46 */   PLAYER_POSITION,
/* 47 */   PLAYER_POSITION_AND_ROTATION,
/* 48 */   PLAYER_ROTATION,
/* 49 */   PLAYER_MOVEMENT,
/* 50 */   VEHICLE_MOVE,
/* 51 */   STEER_BOAT,
/* 52 */   PICK_ITEM,
/* 53 */   PING_REQUEST,
/* 54 */   CRAFT_RECIPE_REQUEST,
/* 55 */   PLAYER_ABILITIES,
/* 56 */   PLAYER_DIGGING,
/* 57 */   ENTITY_ACTION,
/* 58 */   STEER_VEHICLE,
/* 59 */   PONG,
/* 60 */   RECIPE_BOOK_DATA,
/* 61 */   SEEN_RECIPE,
/* 62 */   RENAME_ITEM,
/* 63 */   RESOURCE_PACK_STATUS,
/* 64 */   ADVANCEMENT_TAB,
/* 65 */   SELECT_TRADE,
/* 66 */   SET_BEACON_EFFECT,
/* 67 */   HELD_ITEM_CHANGE,
/* 68 */   UPDATE_COMMAND_BLOCK,
/* 69 */   UPDATE_COMMAND_BLOCK_MINECART,
/* 70 */   CREATIVE_INVENTORY_ACTION,
/* 71 */   UPDATE_JIGSAW_BLOCK,
/* 72 */   UPDATE_STRUCTURE_BLOCK,
/* 73 */   UPDATE_SIGN,
/* 74 */   ANIMATION,
/* 75 */   SPECTATE,
/* 76 */   PLAYER_BLOCK_PLACEMENT,
/* 77 */   USE_ITEM;
/*    */ 
/*    */   
/*    */   public int getId() {
/* 81 */     return ordinal();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 86 */     return name();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\packet\ServerboundPackets1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */