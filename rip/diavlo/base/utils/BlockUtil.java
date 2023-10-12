/*     */ package rip.diavlo.base.utils;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityEgg;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockUtil {
/*  19 */   private static Minecraft mc = Minecraft.getMinecraft();
/*     */ 
/*     */   
/*     */   public static float[] getDirectionToBlock(int var0, int var1, int var2, EnumFacing var3) {
/*  23 */     EntityEgg var4 = new EntityEgg((World)mc.theWorld);
/*  24 */     var4.posX = var0 + 0.5D;
/*  25 */     var4.posY = var1 + 0.5D;
/*  26 */     var4.posZ = var2 + 0.5D;
/*  27 */     var4.posX += var3.getDirectionVec().getX() * 0.25D;
/*  28 */     var4.posY += var3.getDirectionVec().getY() * 0.25D;
/*  29 */     var4.posZ += var3.getDirectionVec().getZ() * 0.25D;
/*  30 */     return getDirectionToEntity((Entity)var4);
/*     */   }
/*     */ 
/*     */   
/*     */   private static float[] getDirectionToEntity(Entity var0) {
/*  35 */     return new float[] { getYaw(var0) + mc.thePlayer.rotationYaw, getPitch(var0) + mc.thePlayer.rotationPitch };
/*     */   }
/*     */ 
/*     */   
/*     */   public static float[] getRotationNeededForBlock(EntityPlayer paramEntityPlayer, BlockPos pos) {
/*  40 */     double d1 = pos.getX() - paramEntityPlayer.posX;
/*  41 */     double d2 = pos.getY() + 0.5D - paramEntityPlayer.posY + paramEntityPlayer.getEyeHeight();
/*  42 */     double d3 = pos.getZ() - paramEntityPlayer.posZ;
/*  43 */     double d4 = Math.sqrt(d1 * d1 + d3 * d3);
/*  44 */     float f1 = (float)(Math.atan2(d3, d1) * 180.0D / Math.PI) - 90.0F;
/*  45 */     float f2 = (float)-(Math.atan2(d2, d4) * 180.0D / Math.PI);
/*  46 */     return new float[] { f1, f2 };
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getYaw(Entity var0) {
/*  51 */     double var5, var1 = var0.posX - mc.thePlayer.posX;
/*  52 */     double var3 = var0.posZ - mc.thePlayer.posZ;
/*     */     
/*  54 */     if (var3 < 0.0D && var1 < 0.0D) {
/*     */       
/*  56 */       var5 = 90.0D + Math.toDegrees(Math.atan(var3 / var1));
/*     */ 
/*     */     
/*     */     }
/*  60 */     else if (var3 < 0.0D && var1 > 0.0D) {
/*  61 */       var5 = -90.0D + Math.toDegrees(Math.atan(var3 / var1));
/*     */     } else {
/*  63 */       var5 = Math.toDegrees(-Math.atan(var1 / var3));
/*     */     } 
/*     */     
/*  66 */     return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)var5));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getPitch(Entity var0) {
/*  71 */     double var1 = var0.posX - mc.thePlayer.posX;
/*  72 */     double var3 = var0.posZ - mc.thePlayer.posZ;
/*  73 */     double var5 = var0.posY - 1.6D + var0.getEyeHeight() - mc.thePlayer.posY;
/*  74 */     double var7 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
/*  75 */     double var9 = -Math.toDegrees(Math.atan(var5 / var7));
/*  76 */     return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)var9);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockPos[] getBlocksAround(EntityPlayer pl, int r, boolean mbv) {
/*  81 */     long px = (long)pl.posX;
/*  82 */     long py = (long)pl.posY;
/*  83 */     long pz = (long)pl.posZ;
/*  84 */     long ex = px + r;
/*  85 */     long ey = py + r;
/*  86 */     long ez = pz + r;
/*  87 */     long sx = px - r;
/*  88 */     long sy = py - r;
/*  89 */     long sz = pz - r;
/*  90 */     List<BlockPos> b = new ArrayList<>(); long x;
/*  91 */     for (x = sx; x < ex; x++) {
/*  92 */       long y; for (y = sy; y < ey; y++) {
/*  93 */         long z; for (z = sz; z < ez; z++) {
/*     */           
/*  95 */           BlockPos pos = new BlockPos(x, y, z);
/*  96 */           if (!mc.theWorld.isAirBlock(pos) && (!mbv || isBlockVisible(pos))) {
/*  97 */             b.add(pos);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 102 */     return b.<BlockPos>toArray(new BlockPos[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isBlockVisible(BlockPos pos) {
/* 107 */     WorldClient worldClient = mc.theWorld;
/* 108 */     EntityPlayerSP p = mc.thePlayer;
/* 109 */     int i = pos.getX();
/* 110 */     int j = pos.getY();
/* 111 */     int k = pos.getZ();
/* 112 */     double d = p.posX;
/* 113 */     double e = p.posY + p.getEyeHeight();
/* 114 */     double f = p.posZ;
/* 115 */     boolean b = worldClient.isAirBlock(new BlockPos(i - 1, j, k));
/* 116 */     boolean c = worldClient.isAirBlock(new BlockPos(i + 1, j, k));
/* 117 */     boolean g = worldClient.isAirBlock(new BlockPos(i, j, k - 1));
/* 118 */     boolean h = worldClient.isAirBlock(new BlockPos(i, j, k + 1));
/* 119 */     boolean l = worldClient.isAirBlock(new BlockPos(i, j + 1, k));
/* 120 */     boolean m = worldClient.isAirBlock(new BlockPos(i, j - 1, k));
/* 121 */     return ((l && e > j) || (m && e < j) || (b && d < i) || (c && d > i) || (g && f < k) || (h && f > k));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void breakBlock(BlockPos block) {
/* 126 */     mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, block, EnumFacing.UP));
/* 127 */     mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block, EnumFacing.UP));
/*     */   }
/*     */ 
/*     */   
/*     */   static void applyRotations(float[] rotations) {
/* 132 */     mc.thePlayer.rotationYaw = rotations[0];
/* 133 */     mc.thePlayer.rotationPitch = rotations[1] + 8.1F;
/*     */   }
/*     */ 
/*     */   
/*     */   static float[] getRotationsNeeded(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/* 138 */     double x = Random.randDouble(minX, maxX) - mc.thePlayer.posX;
/* 139 */     double y = Random.randDouble(minY, maxY) - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
/* 140 */     double z = Random.randDouble(minZ, maxZ) - mc.thePlayer.posZ;
/* 141 */     return new float[] { mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float((float)(Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + 
/* 142 */         MathHelper.wrapAngleTo180_float((float)-(Math.atan2(y, sqrt((float)(x * x + z * z))) * 180.0D / Math.PI) - mc.thePlayer.rotationPitch) };
/*     */   }
/*     */ 
/*     */   
/*     */   public static void faceCoords(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/* 147 */     applyRotations(getRotationsNeeded(minX, minY, minZ, maxX, maxY, maxZ));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void faceBlock(BlockPos p) {
/* 152 */     faceCoords(p.getX(), p.getY(), p.getZ(), (p.getX() + 1), (p.getY() + 1), (p.getZ() + 1));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float sqrt(float value) {
/* 157 */     return (float)Math.sqrt(value);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\BlockUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */