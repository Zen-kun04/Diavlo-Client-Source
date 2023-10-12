/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.biome.BiomeGenBase;
/*    */ import net.minecraft.world.biome.WorldChunkManager;
/*    */ import net.minecraft.world.biome.WorldChunkManagerHell;
/*    */ import net.minecraft.world.chunk.IChunkProvider;
/*    */ import net.minecraft.world.gen.ChunkProviderEnd;
/*    */ 
/*    */ public class WorldProviderEnd
/*    */   extends WorldProvider {
/*    */   public void registerWorldChunkManager() {
/* 15 */     this.worldChunkMgr = (WorldChunkManager)new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F);
/* 16 */     this.dimensionId = 1;
/* 17 */     this.hasNoSky = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public IChunkProvider createChunkGenerator() {
/* 22 */     return (IChunkProvider)new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
/*    */   }
/*    */ 
/*    */   
/*    */   public float calculateCelestialAngle(long worldTime, float partialTicks) {
/* 27 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
/* 32 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
/* 37 */     int i = 10518688;
/* 38 */     float f = MathHelper.cos(p_76562_1_ * 3.1415927F * 2.0F) * 2.0F + 0.5F;
/* 39 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 40 */     float f1 = (i >> 16 & 0xFF) / 255.0F;
/* 41 */     float f2 = (i >> 8 & 0xFF) / 255.0F;
/* 42 */     float f3 = (i & 0xFF) / 255.0F;
/* 43 */     f1 *= f * 0.0F + 0.15F;
/* 44 */     f2 *= f * 0.0F + 0.15F;
/* 45 */     f3 *= f * 0.0F + 0.15F;
/* 46 */     return new Vec3(f1, f2, f3);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSkyColored() {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canRespawnHere() {
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSurfaceWorld() {
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getCloudHeight() {
/* 66 */     return 8.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCoordinateBeSpawn(int x, int z) {
/* 71 */     return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getSpawnCoordinate() {
/* 76 */     return new BlockPos(100, 50, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAverageGroundLevel() {
/* 81 */     return 50;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doesXZShowFog(int x, int z) {
/* 86 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDimensionName() {
/* 91 */     return "The End";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getInternalNameSuffix() {
/* 96 */     return "_end";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\WorldProviderEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */