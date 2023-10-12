/*    */ package net.minecraft.world.chunk.storage;
/*    */ 
/*    */ import java.io.File;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.world.WorldProvider;
/*    */ import net.minecraft.world.storage.SaveHandler;
/*    */ import net.minecraft.world.storage.ThreadedFileIOBase;
/*    */ import net.minecraft.world.storage.WorldInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnvilSaveHandler
/*    */   extends SaveHandler
/*    */ {
/*    */   public AnvilSaveHandler(File savesDirectory, String directoryName, boolean storePlayerdata) {
/* 16 */     super(savesDirectory, directoryName, storePlayerdata);
/*    */   }
/*    */ 
/*    */   
/*    */   public IChunkLoader getChunkLoader(WorldProvider provider) {
/* 21 */     File file1 = getWorldDirectory();
/*    */     
/* 23 */     if (provider instanceof net.minecraft.world.WorldProviderHell) {
/*    */       
/* 25 */       File file3 = new File(file1, "DIM-1");
/* 26 */       file3.mkdirs();
/* 27 */       return new AnvilChunkLoader(file3);
/*    */     } 
/* 29 */     if (provider instanceof net.minecraft.world.WorldProviderEnd) {
/*    */       
/* 31 */       File file2 = new File(file1, "DIM1");
/* 32 */       file2.mkdirs();
/* 33 */       return new AnvilChunkLoader(file2);
/*    */     } 
/*    */ 
/*    */     
/* 37 */     return new AnvilChunkLoader(file1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound) {
/* 43 */     worldInformation.setSaveVersion(19133);
/* 44 */     super.saveWorldInfoWithPlayer(worldInformation, tagCompound);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void flush() {
/*    */     try {
/* 51 */       ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
/*    */     }
/* 53 */     catch (InterruptedException interruptedexception) {
/*    */       
/* 55 */       interruptedexception.printStackTrace();
/*    */     } 
/*    */     
/* 58 */     RegionFileCache.clearRegionFileReferences();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\chunk\storage\AnvilSaveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */