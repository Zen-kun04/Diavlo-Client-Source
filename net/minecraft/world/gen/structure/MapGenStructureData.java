/*    */ package net.minecraft.world.gen.structure;
/*    */ 
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.world.WorldSavedData;
/*    */ 
/*    */ public class MapGenStructureData extends WorldSavedData {
/*  8 */   private NBTTagCompound tagCompound = new NBTTagCompound();
/*    */ 
/*    */   
/*    */   public MapGenStructureData(String name) {
/* 12 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readFromNBT(NBTTagCompound nbt) {
/* 17 */     this.tagCompound = nbt.getCompoundTag("Features");
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeToNBT(NBTTagCompound nbt) {
/* 22 */     nbt.setTag("Features", (NBTBase)this.tagCompound);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeInstance(NBTTagCompound tagCompoundIn, int chunkX, int chunkZ) {
/* 27 */     this.tagCompound.setTag(formatChunkCoords(chunkX, chunkZ), (NBTBase)tagCompoundIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String formatChunkCoords(int chunkX, int chunkZ) {
/* 32 */     return "[" + chunkX + "," + chunkZ + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTTagCompound getTagCompound() {
/* 37 */     return this.tagCompound;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\structure\MapGenStructureData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */