/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.StringUtils;
/*     */ 
/*     */ public class TileEntitySkull extends TileEntity {
/*     */   private int skullType;
/*     */   private int skullRotation;
/*  18 */   private GameProfile playerProfile = null;
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/*  22 */     super.writeToNBT(compound);
/*  23 */     compound.setByte("SkullType", (byte)(this.skullType & 0xFF));
/*  24 */     compound.setByte("Rot", (byte)(this.skullRotation & 0xFF));
/*     */     
/*  26 */     if (this.playerProfile != null) {
/*     */       
/*  28 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  29 */       NBTUtil.writeGameProfile(nbttagcompound, this.playerProfile);
/*  30 */       compound.setTag("Owner", (NBTBase)nbttagcompound);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/*  36 */     super.readFromNBT(compound);
/*  37 */     this.skullType = compound.getByte("SkullType");
/*  38 */     this.skullRotation = compound.getByte("Rot");
/*     */     
/*  40 */     if (this.skullType == 3)
/*     */     {
/*  42 */       if (compound.hasKey("Owner", 10)) {
/*     */         
/*  44 */         this.playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
/*     */       }
/*  46 */       else if (compound.hasKey("ExtraType", 8)) {
/*     */         
/*  48 */         String s = compound.getString("ExtraType");
/*     */         
/*  50 */         if (!StringUtils.isNullOrEmpty(s)) {
/*     */           
/*  52 */           this.playerProfile = new GameProfile((UUID)null, s);
/*  53 */           updatePlayerProfile();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile getPlayerProfile() {
/*  61 */     return this.playerProfile;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet getDescriptionPacket() {
/*  66 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  67 */     writeToNBT(nbttagcompound);
/*  68 */     return (Packet)new S35PacketUpdateTileEntity(this.pos, 4, nbttagcompound);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setType(int type) {
/*  73 */     this.skullType = type;
/*  74 */     this.playerProfile = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlayerProfile(GameProfile playerProfile) {
/*  79 */     this.skullType = 3;
/*  80 */     this.playerProfile = playerProfile;
/*  81 */     updatePlayerProfile();
/*     */   }
/*     */ 
/*     */   
/*     */   private void updatePlayerProfile() {
/*  86 */     this.playerProfile = updateGameprofile(this.playerProfile);
/*  87 */     markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public static GameProfile updateGameprofile(GameProfile input) {
/*  92 */     if (input != null && !StringUtils.isNullOrEmpty(input.getName())) {
/*     */       
/*  94 */       if (input.isComplete() && input.getProperties().containsKey("textures"))
/*     */       {
/*  96 */         return input;
/*     */       }
/*  98 */       if (MinecraftServer.getServer() == null)
/*     */       {
/* 100 */         return input;
/*     */       }
/*     */ 
/*     */       
/* 104 */       GameProfile gameprofile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(input.getName());
/*     */       
/* 106 */       if (gameprofile == null)
/*     */       {
/* 108 */         return input;
/*     */       }
/*     */ 
/*     */       
/* 112 */       Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), null);
/*     */       
/* 114 */       if (property == null)
/*     */       {
/* 116 */         gameprofile = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(gameprofile, true);
/*     */       }
/*     */       
/* 119 */       return gameprofile;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     return input;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSkullType() {
/* 131 */     return this.skullType;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSkullRotation() {
/* 136 */     return this.skullRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSkullRotation(int rotation) {
/* 141 */     this.skullRotation = rotation;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntitySkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */