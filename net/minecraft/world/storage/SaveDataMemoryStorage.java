/*    */ package net.minecraft.world.storage;
/*    */ 
/*    */ import net.minecraft.world.WorldSavedData;
/*    */ 
/*    */ public class SaveDataMemoryStorage
/*    */   extends MapStorage
/*    */ {
/*    */   public SaveDataMemoryStorage() {
/*  9 */     super((ISaveHandler)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldSavedData loadData(Class<? extends WorldSavedData> clazz, String dataIdentifier) {
/* 14 */     return this.loadedDataMap.get(dataIdentifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(String dataIdentifier, WorldSavedData data) {
/* 19 */     this.loadedDataMap.put(dataIdentifier, data);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void saveAllData() {}
/*    */ 
/*    */   
/*    */   public int getUniqueDataId(String key) {
/* 28 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\SaveDataMemoryStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */