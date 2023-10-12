/*     */ package rip.diavlo.base.utils;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class McUtils
/*     */ {
/*     */   public static void drawAbox(BlockPos blockPos) {
/*  19 */     double x = blockPos.getX() - (Minecraft.getMinecraft().getRenderManager()).renderPosX;
/*     */ 
/*     */     
/*  22 */     double y = blockPos.getY() - (Minecraft.getMinecraft().getRenderManager()).renderPosY;
/*     */ 
/*     */     
/*  25 */     double z = blockPos.getZ() - (Minecraft.getMinecraft().getRenderManager()).renderPosZ;
/*     */     
/*  27 */     GL11.glBlendFunc(770, 771);
/*  28 */     GL11.glEnable(3042);
/*  29 */     GL11.glLineWidth(2.0F);
/*  30 */     GL11.glColor4d(0.0D, 0.0D, 1.0D, 0.15000000596046448D);
/*  31 */     GL11.glDisable(3553);
/*  32 */     GL11.glDisable(2929);
/*  33 */     GL11.glDepthMask(false);
/*  34 */     RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 2.0D, z + 1.0D), 50, 78, 168, 10);
/*  35 */     GL11.glEnable(3553);
/*  36 */     GL11.glEnable(2929);
/*  37 */     GL11.glDepthMask(true);
/*  38 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<EntityPlayer> players() {
/*  43 */     return MinecraftUtil.mc.theWorld.playerEntities;
/*     */   }
/*     */   
/*     */   public static double getDifference(double a, double b) {
/*  47 */     if (a > b) {
/*  48 */       return a - b;
/*     */     }
/*  50 */     return b - a;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static EntityPlayer getClosestPlayer(double x, double y, double z, double distance) {
/*  56 */     EntityPlayer entityplayer = null;
/*  57 */     double d = distance;
/*     */     
/*  59 */     for (EntityPlayer p : MinecraftUtil.mc.theWorld.playerEntities) {
/*  60 */       double temp = p.getDistance(MinecraftUtil.mc.thePlayer.posX, MinecraftUtil.mc.thePlayer.posY, MinecraftUtil.mc.thePlayer.posZ);
/*  61 */       if (temp < d && !MinecraftUtil.mc.thePlayer.getName().equalsIgnoreCase(p.getName())) {
/*  62 */         d = temp;
/*  63 */         entityplayer = p;
/*     */       } 
/*     */     } 
/*  66 */     return entityplayer;
/*     */   }
/*     */ 
/*     */   
/*     */   public static EntityPlayer getClosestPlayerToEntity(Entity entityIn, double distance) {
/*  71 */     return getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance);
/*     */   }
/*     */   
/*     */   public static EntityPlayer getCloseEntityInRange(float range) {
/*  75 */     List<EntityPlayer> listaPlayers = players();
/*     */     
/*  77 */     int count = 0;
/*     */     
/*  79 */     for (int i = 1; i < listaPlayers.size(); i++) {
/*     */       
/*  81 */       String playerName = listaPlayers.toString().split("EntityOtherPlayerMP\\['")[i].split("'/")[0];
/*  82 */       EntityPlayer playerentity = MinecraftUtil.mc.theWorld.getPlayerEntityByName(playerName);
/*     */       
/*  84 */       double hisPos_x = (MinecraftUtil.mc.theWorld.getPlayerEntityByName(playerName)).posX;
/*  85 */       double hisPos_z = (MinecraftUtil.mc.theWorld.getPlayerEntityByName(playerName)).posZ;
/*     */       
/*  87 */       double posx_difference = getDifference(MinecraftUtil.mc.thePlayer.posX, hisPos_x);
/*  88 */       double posz_difference = getDifference(MinecraftUtil.mc.thePlayer.posZ, hisPos_z);
/*     */       
/*  90 */       if (posz_difference <= range && posx_difference <= range) {
/*  91 */         return playerentity;
/*     */       }
/*     */       
/*  94 */       count += 10;
/*     */     } 
/*     */     
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public static double getDirection() {
/* 101 */     float rotationYaw = MinecraftUtil.mc.thePlayer.rotationYaw;
/*     */     
/* 103 */     if (MinecraftUtil.mc.thePlayer.moveForward < 0.0F) {
/* 104 */       rotationYaw += 180.0F;
/*     */     }
/* 106 */     float forward = 1.0F;
/* 107 */     if (MinecraftUtil.mc.thePlayer.moveForward < 0.0F) {
/* 108 */       forward = -0.5F;
/* 109 */     } else if (MinecraftUtil.mc.thePlayer.moveForward > 0.0F) {
/* 110 */       forward = 0.5F;
/*     */     } 
/* 112 */     if (MinecraftUtil.mc.thePlayer.moveStrafing > 0.0F) {
/* 113 */       rotationYaw -= 90.0F * forward;
/*     */     }
/* 115 */     if (MinecraftUtil.mc.thePlayer.moveStrafing < 0.0F) {
/* 116 */       rotationYaw += 90.0F * forward;
/*     */     }
/* 118 */     return Math.toRadians(rotationYaw);
/*     */   }
/*     */   
/*     */   public static void strafe(float speed) {
/* 122 */     if (MinecraftUtil.mc.thePlayer.moveForward < 0.0F) {
/*     */       return;
/*     */     }
/*     */     
/* 126 */     double yaw = getDirection();
/* 127 */     MinecraftUtil.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
/* 128 */     MinecraftUtil.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\McUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */