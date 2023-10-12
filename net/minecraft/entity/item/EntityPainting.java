/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityHanging;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityPainting
/*     */   extends EntityHanging
/*     */ {
/*     */   public EnumArt art;
/*     */   
/*     */   public EntityPainting(World worldIn) {
/*  21 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityPainting(World worldIn, BlockPos pos, EnumFacing facing) {
/*  26 */     super(worldIn, pos);
/*  27 */     List<EnumArt> list = Lists.newArrayList();
/*     */     
/*  29 */     for (EnumArt entitypainting$enumart : EnumArt.values()) {
/*     */       
/*  31 */       this.art = entitypainting$enumart;
/*  32 */       updateFacingWithBoundingBox(facing);
/*     */       
/*  34 */       if (onValidSurface())
/*     */       {
/*  36 */         list.add(entitypainting$enumart);
/*     */       }
/*     */     } 
/*     */     
/*  40 */     if (!list.isEmpty())
/*     */     {
/*  42 */       this.art = list.get(this.rand.nextInt(list.size()));
/*     */     }
/*     */     
/*  45 */     updateFacingWithBoundingBox(facing);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityPainting(World worldIn, BlockPos pos, EnumFacing facing, String title) {
/*  50 */     this(worldIn, pos, facing);
/*     */     
/*  52 */     for (EnumArt entitypainting$enumart : EnumArt.values()) {
/*     */       
/*  54 */       if (entitypainting$enumart.title.equals(title)) {
/*     */         
/*  56 */         this.art = entitypainting$enumart;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  61 */     updateFacingWithBoundingBox(facing);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  66 */     tagCompound.setString("Motive", this.art.title);
/*  67 */     super.writeEntityToNBT(tagCompound);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  72 */     String s = tagCompund.getString("Motive");
/*     */     
/*  74 */     for (EnumArt entitypainting$enumart : EnumArt.values()) {
/*     */       
/*  76 */       if (entitypainting$enumart.title.equals(s))
/*     */       {
/*  78 */         this.art = entitypainting$enumart;
/*     */       }
/*     */     } 
/*     */     
/*  82 */     if (this.art == null)
/*     */     {
/*  84 */       this.art = EnumArt.KEBAB;
/*     */     }
/*     */     
/*  87 */     super.readEntityFromNBT(tagCompund);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidthPixels() {
/*  92 */     return this.art.sizeX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeightPixels() {
/*  97 */     return this.art.sizeY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBroken(Entity brokenEntity) {
/* 102 */     if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
/*     */       
/* 104 */       if (brokenEntity instanceof EntityPlayer) {
/*     */         
/* 106 */         EntityPlayer entityplayer = (EntityPlayer)brokenEntity;
/*     */         
/* 108 */         if (entityplayer.capabilities.isCreativeMode) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 114 */       entityDropItem(new ItemStack(Items.painting), 0.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
/* 120 */     BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
/* 121 */     setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
/* 126 */     BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
/* 127 */     setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*     */   }
/*     */   
/*     */   public enum EnumArt
/*     */   {
/* 132 */     KEBAB("Kebab", 16, 16, 0, 0),
/* 133 */     AZTEC("Aztec", 16, 16, 16, 0),
/* 134 */     ALBAN("Alban", 16, 16, 32, 0),
/* 135 */     AZTEC_2("Aztec2", 16, 16, 48, 0),
/* 136 */     BOMB("Bomb", 16, 16, 64, 0),
/* 137 */     PLANT("Plant", 16, 16, 80, 0),
/* 138 */     WASTELAND("Wasteland", 16, 16, 96, 0),
/* 139 */     POOL("Pool", 32, 16, 0, 32),
/* 140 */     COURBET("Courbet", 32, 16, 32, 32),
/* 141 */     SEA("Sea", 32, 16, 64, 32),
/* 142 */     SUNSET("Sunset", 32, 16, 96, 32),
/* 143 */     CREEBET("Creebet", 32, 16, 128, 32),
/* 144 */     WANDERER("Wanderer", 16, 32, 0, 64),
/* 145 */     GRAHAM("Graham", 16, 32, 16, 64),
/* 146 */     MATCH("Match", 32, 32, 0, 128),
/* 147 */     BUST("Bust", 32, 32, 32, 128),
/* 148 */     STAGE("Stage", 32, 32, 64, 128),
/* 149 */     VOID("Void", 32, 32, 96, 128),
/* 150 */     SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128),
/* 151 */     WITHER("Wither", 32, 32, 160, 128),
/* 152 */     FIGHTERS("Fighters", 64, 32, 0, 96),
/* 153 */     POINTER("Pointer", 64, 64, 0, 192),
/* 154 */     PIGSCENE("Pigscene", 64, 64, 64, 192),
/* 155 */     BURNING_SKULL("BurningSkull", 64, 64, 128, 192),
/* 156 */     SKELETON("Skeleton", 64, 48, 192, 64),
/* 157 */     DONKEY_KONG("DonkeyKong", 64, 48, 192, 112);
/*     */     
/* 159 */     public static final int field_180001_A = "SkullAndRoses".length();
/*     */     
/*     */     public final String title;
/*     */     
/*     */     public final int sizeX;
/*     */     
/*     */     public final int sizeY;
/*     */     
/*     */     EnumArt(String titleIn, int width, int height, int textureU, int textureV) {
/* 168 */       this.title = titleIn;
/* 169 */       this.sizeX = width;
/* 170 */       this.sizeY = height;
/* 171 */       this.offsetX = textureU;
/* 172 */       this.offsetY = textureV;
/*     */     }
/*     */     
/*     */     public final int offsetX;
/*     */     public final int offsetY;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityPainting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */