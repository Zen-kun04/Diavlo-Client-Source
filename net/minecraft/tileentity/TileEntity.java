/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockJukebox;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class TileEntity
/*     */ {
/*  20 */   private static final Logger logger = LogManager.getLogger();
/*  21 */   private static Map<String, Class<? extends TileEntity>> nameToClassMap = Maps.newHashMap();
/*  22 */   private static Map<Class<? extends TileEntity>, String> classToNameMap = Maps.newHashMap();
/*     */   protected World worldObj;
/*  24 */   protected BlockPos pos = BlockPos.ORIGIN;
/*     */   protected boolean tileEntityInvalid;
/*  26 */   private int blockMetadata = -1;
/*     */   
/*     */   protected Block blockType;
/*     */   
/*     */   private static void addMapping(Class<? extends TileEntity> cl, String id) {
/*  31 */     if (nameToClassMap.containsKey(id))
/*     */     {
/*  33 */       throw new IllegalArgumentException("Duplicate id: " + id);
/*     */     }
/*     */ 
/*     */     
/*  37 */     nameToClassMap.put(id, cl);
/*  38 */     classToNameMap.put(cl, id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public World getWorld() {
/*  44 */     return this.worldObj;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldObj(World worldIn) {
/*  49 */     this.worldObj = worldIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasWorldObj() {
/*  54 */     return (this.worldObj != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/*  59 */     this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/*  64 */     String s = classToNameMap.get(getClass());
/*     */     
/*  66 */     if (s == null)
/*     */     {
/*  68 */       throw new RuntimeException(getClass() + " is missing a mapping! This is a bug!");
/*     */     }
/*     */ 
/*     */     
/*  72 */     compound.setString("id", s);
/*  73 */     compound.setInteger("x", this.pos.getX());
/*  74 */     compound.setInteger("y", this.pos.getY());
/*  75 */     compound.setInteger("z", this.pos.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static TileEntity createAndLoadEntity(NBTTagCompound nbt) {
/*  81 */     TileEntity tileentity = null;
/*     */ 
/*     */     
/*     */     try {
/*  85 */       Class<? extends TileEntity> oclass = nameToClassMap.get(nbt.getString("id"));
/*     */       
/*  87 */       if (oclass != null)
/*     */       {
/*  89 */         tileentity = oclass.newInstance();
/*     */       }
/*     */     }
/*  92 */     catch (Exception exception) {
/*     */       
/*  94 */       exception.printStackTrace();
/*     */     } 
/*     */     
/*  97 */     if (tileentity != null) {
/*     */       
/*  99 */       tileentity.readFromNBT(nbt);
/*     */     }
/*     */     else {
/*     */       
/* 103 */       logger.warn("Skipping BlockEntity with id " + nbt.getString("id"));
/*     */     } 
/*     */     
/* 106 */     return tileentity;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBlockMetadata() {
/* 111 */     if (this.blockMetadata == -1) {
/*     */       
/* 113 */       IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
/* 114 */       this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
/*     */     } 
/*     */     
/* 117 */     return this.blockMetadata;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markDirty() {
/* 122 */     if (this.worldObj != null) {
/*     */       
/* 124 */       IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
/* 125 */       this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
/* 126 */       this.worldObj.markChunkDirty(this.pos, this);
/*     */       
/* 128 */       if (getBlockType() != Blocks.air)
/*     */       {
/* 130 */         this.worldObj.updateComparatorOutputLevel(this.pos, getBlockType());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDistanceSq(double x, double y, double z) {
/* 137 */     double d0 = this.pos.getX() + 0.5D - x;
/* 138 */     double d1 = this.pos.getY() + 0.5D - y;
/* 139 */     double d2 = this.pos.getZ() + 0.5D - z;
/* 140 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMaxRenderDistanceSquared() {
/* 145 */     return 4096.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getPos() {
/* 150 */     return this.pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public Block getBlockType() {
/* 155 */     if (this.blockType == null)
/*     */     {
/* 157 */       this.blockType = this.worldObj.getBlockState(this.pos).getBlock();
/*     */     }
/*     */     
/* 160 */     return this.blockType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet getDescriptionPacket() {
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInvalid() {
/* 170 */     return this.tileEntityInvalid;
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate() {
/* 175 */     this.tileEntityInvalid = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate() {
/* 180 */     this.tileEntityInvalid = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean receiveClientEvent(int id, int type) {
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateContainingBlockInfo() {
/* 190 */     this.blockType = null;
/* 191 */     this.blockMetadata = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInfoToCrashReport(CrashReportCategory reportCategory) {
/* 196 */     reportCategory.addCrashSectionCallable("Name", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 200 */             return (String)TileEntity.classToNameMap.get(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
/*     */           }
/*     */         });
/*     */     
/* 204 */     if (this.worldObj != null) {
/*     */       
/* 206 */       CrashReportCategory.addBlockInfo(reportCategory, this.pos, getBlockType(), getBlockMetadata());
/* 207 */       reportCategory.addCrashSectionCallable("Actual block type", new Callable<String>()
/*     */           {
/*     */             public String call() throws Exception
/*     */             {
/* 211 */               int i = Block.getIdFromBlock(TileEntity.this.worldObj.getBlockState(TileEntity.this.pos).getBlock());
/*     */ 
/*     */               
/*     */               try {
/* 215 */                 return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i), Block.getBlockById(i).getUnlocalizedName(), Block.getBlockById(i).getClass().getCanonicalName() });
/*     */               }
/* 217 */               catch (Throwable var3) {
/*     */                 
/* 219 */                 return "ID #" + i;
/*     */               } 
/*     */             }
/*     */           });
/* 223 */       reportCategory.addCrashSectionCallable("Actual block data value", new Callable<String>()
/*     */           {
/*     */             public String call() throws Exception
/*     */             {
/* 227 */               IBlockState iblockstate = TileEntity.this.worldObj.getBlockState(TileEntity.this.pos);
/* 228 */               int i = iblockstate.getBlock().getMetaFromState(iblockstate);
/*     */               
/* 230 */               if (i < 0)
/*     */               {
/* 232 */                 return "Unknown? (Got " + i + ")";
/*     */               }
/*     */ 
/*     */               
/* 236 */               String s = String.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
/* 237 */               return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s });
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPos(BlockPos posIn) {
/* 246 */     this.pos = posIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_183000_F() {
/* 251 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 256 */     addMapping((Class)TileEntityFurnace.class, "Furnace");
/* 257 */     addMapping((Class)TileEntityChest.class, "Chest");
/* 258 */     addMapping((Class)TileEntityEnderChest.class, "EnderChest");
/* 259 */     addMapping((Class)BlockJukebox.TileEntityJukebox.class, "RecordPlayer");
/* 260 */     addMapping((Class)TileEntityDispenser.class, "Trap");
/* 261 */     addMapping((Class)TileEntityDropper.class, "Dropper");
/* 262 */     addMapping((Class)TileEntitySign.class, "Sign");
/* 263 */     addMapping((Class)TileEntityMobSpawner.class, "MobSpawner");
/* 264 */     addMapping((Class)TileEntityNote.class, "Music");
/* 265 */     addMapping((Class)TileEntityPiston.class, "Piston");
/* 266 */     addMapping((Class)TileEntityBrewingStand.class, "Cauldron");
/* 267 */     addMapping((Class)TileEntityEnchantmentTable.class, "EnchantTable");
/* 268 */     addMapping((Class)TileEntityEndPortal.class, "Airportal");
/* 269 */     addMapping((Class)TileEntityCommandBlock.class, "Control");
/* 270 */     addMapping((Class)TileEntityBeacon.class, "Beacon");
/* 271 */     addMapping((Class)TileEntitySkull.class, "Skull");
/* 272 */     addMapping((Class)TileEntityDaylightDetector.class, "DLDetector");
/* 273 */     addMapping((Class)TileEntityHopper.class, "Hopper");
/* 274 */     addMapping((Class)TileEntityComparator.class, "Comparator");
/* 275 */     addMapping((Class)TileEntityFlowerPot.class, "FlowerPot");
/* 276 */     addMapping((Class)TileEntityBanner.class, "Banner");
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */