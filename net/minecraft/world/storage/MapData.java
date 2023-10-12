/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S34PacketMaps;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec4b;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldSavedData;
/*     */ 
/*     */ public class MapData
/*     */   extends WorldSavedData {
/*     */   public int xCenter;
/*     */   public int zCenter;
/*     */   public byte dimension;
/*     */   public byte scale;
/*  26 */   public byte[] colors = new byte[16384];
/*  27 */   public List<MapInfo> playersArrayList = Lists.newArrayList();
/*  28 */   private Map<EntityPlayer, MapInfo> playersHashMap = Maps.newHashMap();
/*  29 */   public Map<String, Vec4b> mapDecorations = Maps.newLinkedHashMap();
/*     */ 
/*     */   
/*     */   public MapData(String mapname) {
/*  33 */     super(mapname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void calculateMapCenter(double x, double z, int mapScale) {
/*  38 */     int i = 128 * (1 << mapScale);
/*  39 */     int j = MathHelper.floor_double((x + 64.0D) / i);
/*  40 */     int k = MathHelper.floor_double((z + 64.0D) / i);
/*  41 */     this.xCenter = j * i + i / 2 - 64;
/*  42 */     this.zCenter = k * i + i / 2 - 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound nbt) {
/*  47 */     this.dimension = nbt.getByte("dimension");
/*  48 */     this.xCenter = nbt.getInteger("xCenter");
/*  49 */     this.zCenter = nbt.getInteger("zCenter");
/*  50 */     this.scale = nbt.getByte("scale");
/*  51 */     this.scale = (byte)MathHelper.clamp_int(this.scale, 0, 4);
/*  52 */     int i = nbt.getShort("width");
/*  53 */     int j = nbt.getShort("height");
/*     */     
/*  55 */     if (i == 128 && j == 128) {
/*     */       
/*  57 */       this.colors = nbt.getByteArray("colors");
/*     */     }
/*     */     else {
/*     */       
/*  61 */       byte[] abyte = nbt.getByteArray("colors");
/*  62 */       this.colors = new byte[16384];
/*  63 */       int k = (128 - i) / 2;
/*  64 */       int l = (128 - j) / 2;
/*     */       
/*  66 */       for (int i1 = 0; i1 < j; i1++) {
/*     */         
/*  68 */         int j1 = i1 + l;
/*     */         
/*  70 */         if (j1 >= 0 || j1 < 128)
/*     */         {
/*  72 */           for (int k1 = 0; k1 < i; k1++) {
/*     */             
/*  74 */             int l1 = k1 + k;
/*     */             
/*  76 */             if (l1 >= 0 || l1 < 128)
/*     */             {
/*  78 */               this.colors[l1 + j1 * 128] = abyte[k1 + i1 * i];
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound nbt) {
/*  88 */     nbt.setByte("dimension", this.dimension);
/*  89 */     nbt.setInteger("xCenter", this.xCenter);
/*  90 */     nbt.setInteger("zCenter", this.zCenter);
/*  91 */     nbt.setByte("scale", this.scale);
/*  92 */     nbt.setShort("width", (short)128);
/*  93 */     nbt.setShort("height", (short)128);
/*  94 */     nbt.setByteArray("colors", this.colors);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateVisiblePlayers(EntityPlayer player, ItemStack mapStack) {
/*  99 */     if (!this.playersHashMap.containsKey(player)) {
/*     */       
/* 101 */       MapInfo mapdata$mapinfo = new MapInfo(player);
/* 102 */       this.playersHashMap.put(player, mapdata$mapinfo);
/* 103 */       this.playersArrayList.add(mapdata$mapinfo);
/*     */     } 
/*     */     
/* 106 */     if (!player.inventory.hasItemStack(mapStack))
/*     */     {
/* 108 */       this.mapDecorations.remove(player.getName());
/*     */     }
/*     */     
/* 111 */     for (int i = 0; i < this.playersArrayList.size(); i++) {
/*     */       
/* 113 */       MapInfo mapdata$mapinfo1 = this.playersArrayList.get(i);
/*     */       
/* 115 */       if (!mapdata$mapinfo1.entityplayerObj.isDead && (mapdata$mapinfo1.entityplayerObj.inventory.hasItemStack(mapStack) || mapStack.isOnItemFrame())) {
/*     */         
/* 117 */         if (!mapStack.isOnItemFrame() && mapdata$mapinfo1.entityplayerObj.dimension == this.dimension)
/*     */         {
/* 119 */           updateDecorations(0, mapdata$mapinfo1.entityplayerObj.worldObj, mapdata$mapinfo1.entityplayerObj.getName(), mapdata$mapinfo1.entityplayerObj.posX, mapdata$mapinfo1.entityplayerObj.posZ, mapdata$mapinfo1.entityplayerObj.rotationYaw);
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 124 */         this.playersHashMap.remove(mapdata$mapinfo1.entityplayerObj);
/* 125 */         this.playersArrayList.remove(mapdata$mapinfo1);
/*     */       } 
/*     */     } 
/*     */     
/* 129 */     if (mapStack.isOnItemFrame()) {
/*     */       
/* 131 */       EntityItemFrame entityitemframe = mapStack.getItemFrame();
/* 132 */       BlockPos blockpos = entityitemframe.getHangingPosition();
/* 133 */       updateDecorations(1, player.worldObj, "frame-" + entityitemframe.getEntityId(), blockpos.getX(), blockpos.getZ(), (entityitemframe.facingDirection.getHorizontalIndex() * 90));
/*     */     } 
/*     */     
/* 136 */     if (mapStack.hasTagCompound() && mapStack.getTagCompound().hasKey("Decorations", 9)) {
/*     */       
/* 138 */       NBTTagList nbttaglist = mapStack.getTagCompound().getTagList("Decorations", 10);
/*     */       
/* 140 */       for (int j = 0; j < nbttaglist.tagCount(); j++) {
/*     */         
/* 142 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
/*     */         
/* 144 */         if (!this.mapDecorations.containsKey(nbttagcompound.getString("id")))
/*     */         {
/* 146 */           updateDecorations(nbttagcompound.getByte("type"), player.worldObj, nbttagcompound.getString("id"), nbttagcompound.getDouble("x"), nbttagcompound.getDouble("z"), nbttagcompound.getDouble("rot"));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateDecorations(int type, World worldIn, String entityIdentifier, double worldX, double worldZ, double rotation) {
/*     */     byte b2;
/* 154 */     int i = 1 << this.scale;
/* 155 */     float f = (float)(worldX - this.xCenter) / i;
/* 156 */     float f1 = (float)(worldZ - this.zCenter) / i;
/* 157 */     byte b0 = (byte)(int)((f * 2.0F) + 0.5D);
/* 158 */     byte b1 = (byte)(int)((f1 * 2.0F) + 0.5D);
/* 159 */     int j = 63;
/*     */ 
/*     */     
/* 162 */     if (f >= -j && f1 >= -j && f <= j && f1 <= j) {
/*     */       
/* 164 */       rotation += (rotation < 0.0D) ? -8.0D : 8.0D;
/* 165 */       b2 = (byte)(int)(rotation * 16.0D / 360.0D);
/*     */       
/* 167 */       if (this.dimension < 0)
/*     */       {
/* 169 */         int k = (int)(worldIn.getWorldInfo().getWorldTime() / 10L);
/* 170 */         b2 = (byte)(k * k * 34187121 + k * 121 >> 15 & 0xF);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 175 */       if (Math.abs(f) >= 320.0F || Math.abs(f1) >= 320.0F) {
/*     */         
/* 177 */         this.mapDecorations.remove(entityIdentifier);
/*     */         
/*     */         return;
/*     */       } 
/* 181 */       type = 6;
/* 182 */       b2 = 0;
/*     */       
/* 184 */       if (f <= -j)
/*     */       {
/* 186 */         b0 = (byte)(int)((j * 2) + 2.5D);
/*     */       }
/*     */       
/* 189 */       if (f1 <= -j)
/*     */       {
/* 191 */         b1 = (byte)(int)((j * 2) + 2.5D);
/*     */       }
/*     */       
/* 194 */       if (f >= j)
/*     */       {
/* 196 */         b0 = (byte)(j * 2 + 1);
/*     */       }
/*     */       
/* 199 */       if (f1 >= j)
/*     */       {
/* 201 */         b1 = (byte)(j * 2 + 1);
/*     */       }
/*     */     } 
/*     */     
/* 205 */     this.mapDecorations.put(entityIdentifier, new Vec4b((byte)type, b0, b1, b2));
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet getMapPacket(ItemStack mapStack, World worldIn, EntityPlayer player) {
/* 210 */     MapInfo mapdata$mapinfo = this.playersHashMap.get(player);
/* 211 */     return (mapdata$mapinfo == null) ? null : mapdata$mapinfo.getPacket(mapStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateMapData(int x, int y) {
/* 216 */     markDirty();
/*     */     
/* 218 */     for (MapInfo mapdata$mapinfo : this.playersArrayList)
/*     */     {
/* 220 */       mapdata$mapinfo.update(x, y);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public MapInfo getMapInfo(EntityPlayer player) {
/* 226 */     MapInfo mapdata$mapinfo = this.playersHashMap.get(player);
/*     */     
/* 228 */     if (mapdata$mapinfo == null) {
/*     */       
/* 230 */       mapdata$mapinfo = new MapInfo(player);
/* 231 */       this.playersHashMap.put(player, mapdata$mapinfo);
/* 232 */       this.playersArrayList.add(mapdata$mapinfo);
/*     */     } 
/*     */     
/* 235 */     return mapdata$mapinfo;
/*     */   }
/*     */   
/*     */   public class MapInfo
/*     */   {
/*     */     public final EntityPlayer entityplayerObj;
/*     */     private boolean field_176105_d = true;
/* 242 */     private int minX = 0;
/* 243 */     private int minY = 0;
/* 244 */     private int maxX = 127;
/* 245 */     private int maxY = 127;
/*     */     
/*     */     private int field_176109_i;
/*     */     public int field_82569_d;
/*     */     
/*     */     public MapInfo(EntityPlayer player) {
/* 251 */       this.entityplayerObj = player;
/*     */     }
/*     */ 
/*     */     
/*     */     public Packet getPacket(ItemStack stack) {
/* 256 */       if (this.field_176105_d) {
/*     */         
/* 258 */         this.field_176105_d = false;
/* 259 */         return (Packet)new S34PacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.mapDecorations.values(), MapData.this.colors, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY);
/*     */       } 
/*     */ 
/*     */       
/* 263 */       return (this.field_176109_i++ % 5 == 0) ? (Packet)new S34PacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.mapDecorations.values(), MapData.this.colors, 0, 0, 0, 0) : null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void update(int x, int y) {
/* 269 */       if (this.field_176105_d) {
/*     */         
/* 271 */         this.minX = Math.min(this.minX, x);
/* 272 */         this.minY = Math.min(this.minY, y);
/* 273 */         this.maxX = Math.max(this.maxX, x);
/* 274 */         this.maxY = Math.max(this.maxY, y);
/*     */       }
/*     */       else {
/*     */         
/* 278 */         this.field_176105_d = true;
/* 279 */         this.minX = x;
/* 280 */         this.minY = y;
/* 281 */         this.maxX = x;
/* 282 */         this.maxY = y;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\MapData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */