/*    */ package net.minecraft.world.storage;
/*    */ 
/*    */ import java.io.File;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.world.MinecraftException;
/*    */ import net.minecraft.world.WorldProvider;
/*    */ import net.minecraft.world.chunk.storage.IChunkLoader;
/*    */ 
/*    */ public class SaveHandlerMP
/*    */   implements ISaveHandler
/*    */ {
/*    */   public WorldInfo loadWorldInfo() {
/* 13 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void checkSessionLock() throws MinecraftException {}
/*    */ 
/*    */   
/*    */   public IChunkLoader getChunkLoader(WorldProvider provider) {
/* 22 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void saveWorldInfo(WorldInfo worldInformation) {}
/*    */ 
/*    */   
/*    */   public IPlayerFileData getPlayerNBTManager() {
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void flush() {}
/*    */ 
/*    */   
/*    */   public File getMapFileFromName(String mapName) {
/* 44 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getWorldDirectoryName() {
/* 49 */     return "none";
/*    */   }
/*    */ 
/*    */   
/*    */   public File getWorldDirectory() {
/* 54 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\SaveHandlerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */