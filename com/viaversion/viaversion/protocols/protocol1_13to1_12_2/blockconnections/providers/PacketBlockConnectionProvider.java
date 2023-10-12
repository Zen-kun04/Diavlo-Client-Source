/*    */ package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers;
/*    */ 
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import com.viaversion.viaversion.api.minecraft.Position;
/*    */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockConnectionStorage;
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
/*    */ 
/*    */ 
/*    */ public class PacketBlockConnectionProvider
/*    */   extends BlockConnectionProvider
/*    */ {
/*    */   public void storeBlock(UserConnection connection, int x, int y, int z, int blockState) {
/* 29 */     ((BlockConnectionStorage)connection.get(BlockConnectionStorage.class)).store(x, y, z, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeBlock(UserConnection connection, int x, int y, int z) {
/* 34 */     ((BlockConnectionStorage)connection.get(BlockConnectionStorage.class)).remove(x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBlockData(UserConnection connection, int x, int y, int z) {
/* 39 */     return ((BlockConnectionStorage)connection.get(BlockConnectionStorage.class)).get(x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearStorage(UserConnection connection) {
/* 44 */     ((BlockConnectionStorage)connection.get(BlockConnectionStorage.class)).clear();
/*    */   }
/*    */   
/*    */   public void modifiedBlock(UserConnection connection, Position position) {
/* 48 */     ((BlockConnectionStorage)connection.get(BlockConnectionStorage.class)).markModified(position);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unloadChunk(UserConnection connection, int x, int z) {
/* 53 */     ((BlockConnectionStorage)connection.get(BlockConnectionStorage.class)).unloadChunk(x, z);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unloadChunkSection(UserConnection connection, int chunkX, int chunkY, int chunkZ) {
/* 58 */     ((BlockConnectionStorage)connection.get(BlockConnectionStorage.class)).unloadSection(chunkX, chunkY, chunkZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean storesBlocks(UserConnection connection, Position pos) {
/* 63 */     if (pos == null || connection == null) return true;
/*    */     
/* 65 */     return !((BlockConnectionStorage)connection.get(BlockConnectionStorage.class)).recentlyModified(pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public UserBlockData forUser(UserConnection connection) {
/* 70 */     BlockConnectionStorage storage = (BlockConnectionStorage)connection.get(BlockConnectionStorage.class);
/* 71 */     return (x, y, z) -> storage.get(x, y, z);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_13to1_12_2\blockconnections\providers\PacketBlockConnectionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */