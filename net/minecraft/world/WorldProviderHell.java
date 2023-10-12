/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.biome.BiomeGenBase;
/*    */ import net.minecraft.world.biome.WorldChunkManager;
/*    */ import net.minecraft.world.biome.WorldChunkManagerHell;
/*    */ import net.minecraft.world.border.WorldBorder;
/*    */ import net.minecraft.world.chunk.IChunkProvider;
/*    */ import net.minecraft.world.gen.ChunkProviderHell;
/*    */ 
/*    */ public class WorldProviderHell
/*    */   extends WorldProvider {
/*    */   public void registerWorldChunkManager() {
/* 14 */     this.worldChunkMgr = (WorldChunkManager)new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F);
/* 15 */     this.isHellWorld = true;
/* 16 */     this.hasNoSky = true;
/* 17 */     this.dimensionId = -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
/* 22 */     return new Vec3(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void generateLightBrightnessTable() {
/* 27 */     float f = 0.1F;
/*    */     
/* 29 */     for (int i = 0; i <= 15; i++) {
/*    */       
/* 31 */       float f1 = 1.0F - i / 15.0F;
/* 32 */       this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public IChunkProvider createChunkGenerator() {
/* 38 */     return (IChunkProvider)new ChunkProviderHell(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSurfaceWorld() {
/* 43 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCoordinateBeSpawn(int x, int z) {
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public float calculateCelestialAngle(long worldTime, float partialTicks) {
/* 53 */     return 0.5F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canRespawnHere() {
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doesXZShowFog(int x, int z) {
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDimensionName() {
/* 68 */     return "Nether";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getInternalNameSuffix() {
/* 73 */     return "_nether";
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldBorder getWorldBorder() {
/* 78 */     return new WorldBorder()
/*    */       {
/*    */         public double getCenterX()
/*    */         {
/* 82 */           return super.getCenterX() / 8.0D;
/*    */         }
/*    */         
/*    */         public double getCenterZ() {
/* 86 */           return super.getCenterZ() / 8.0D;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\WorldProviderHell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */