/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class BlockBed extends BlockDirectional {
/*  25 */   public static final PropertyEnum<EnumPartType> PART = PropertyEnum.create("part", EnumPartType.class);
/*  26 */   public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");
/*     */ 
/*     */   
/*     */   public BlockBed() {
/*  30 */     super(Material.cloth);
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)PART, EnumPartType.FOOT).withProperty((IProperty)OCCUPIED, Boolean.valueOf(false)));
/*  32 */     setBedBounds();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  37 */     if (worldIn.isRemote)
/*     */     {
/*  39 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  43 */     if (state.getValue((IProperty)PART) != EnumPartType.HEAD) {
/*     */       
/*  45 */       pos = pos.offset((EnumFacing)state.getValue((IProperty)FACING));
/*  46 */       state = worldIn.getBlockState(pos);
/*     */       
/*  48 */       if (state.getBlock() != this)
/*     */       {
/*  50 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  54 */     if (worldIn.provider.canRespawnHere() && worldIn.getBiomeGenForCoords(pos) != BiomeGenBase.hell) {
/*     */       
/*  56 */       if (((Boolean)state.getValue((IProperty)OCCUPIED)).booleanValue()) {
/*     */         
/*  58 */         EntityPlayer entityplayer = getPlayerInBed(worldIn, pos);
/*     */         
/*  60 */         if (entityplayer != null) {
/*     */           
/*  62 */           playerIn.addChatComponentMessage((IChatComponent)new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
/*  63 */           return true;
/*     */         } 
/*     */         
/*  66 */         state = state.withProperty((IProperty)OCCUPIED, Boolean.valueOf(false));
/*  67 */         worldIn.setBlockState(pos, state, 4);
/*     */       } 
/*     */       
/*  70 */       EntityPlayer.EnumStatus entityplayer$enumstatus = playerIn.trySleep(pos);
/*     */       
/*  72 */       if (entityplayer$enumstatus == EntityPlayer.EnumStatus.OK) {
/*     */         
/*  74 */         state = state.withProperty((IProperty)OCCUPIED, Boolean.valueOf(true));
/*  75 */         worldIn.setBlockState(pos, state, 4);
/*  76 */         return true;
/*     */       } 
/*     */ 
/*     */       
/*  80 */       if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
/*     */         
/*  82 */         playerIn.addChatComponentMessage((IChatComponent)new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
/*     */       }
/*  84 */       else if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_SAFE) {
/*     */         
/*  86 */         playerIn.addChatComponentMessage((IChatComponent)new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
/*     */       } 
/*     */       
/*  89 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  94 */     worldIn.setBlockToAir(pos);
/*  95 */     BlockPos blockpos = pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite());
/*     */     
/*  97 */     if (worldIn.getBlockState(blockpos).getBlock() == this)
/*     */     {
/*  99 */       worldIn.setBlockToAir(blockpos);
/*     */     }
/*     */     
/* 102 */     worldIn.newExplosion((Entity)null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5.0F, true, true);
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
/* 110 */     for (EntityPlayer entityplayer : worldIn.playerEntities) {
/*     */       
/* 112 */       if (entityplayer.isPlayerSleeping() && entityplayer.playerLocation.equals(pos))
/*     */       {
/* 114 */         return entityplayer;
/*     */       }
/*     */     } 
/*     */     
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 133 */     setBedBounds();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 138 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*     */     
/* 140 */     if (state.getValue((IProperty)PART) == EnumPartType.HEAD) {
/*     */       
/* 142 */       if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this)
/*     */       {
/* 144 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */     }
/* 147 */     else if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
/*     */       
/* 149 */       worldIn.setBlockToAir(pos);
/*     */       
/* 151 */       if (!worldIn.isRemote)
/*     */       {
/* 153 */         dropBlockAsItem(worldIn, pos, state, 0);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 160 */     return (state.getValue((IProperty)PART) == EnumPartType.HEAD) ? null : Items.bed;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setBedBounds() {
/* 165 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockPos getSafeExitLocation(World worldIn, BlockPos pos, int tries) {
/* 170 */     EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING);
/* 171 */     int i = pos.getX();
/* 172 */     int j = pos.getY();
/* 173 */     int k = pos.getZ();
/*     */     
/* 175 */     for (int l = 0; l <= 1; l++) {
/*     */       
/* 177 */       int i1 = i - enumfacing.getFrontOffsetX() * l - 1;
/* 178 */       int j1 = k - enumfacing.getFrontOffsetZ() * l - 1;
/* 179 */       int k1 = i1 + 2;
/* 180 */       int l1 = j1 + 2;
/*     */       
/* 182 */       for (int i2 = i1; i2 <= k1; i2++) {
/*     */         
/* 184 */         for (int j2 = j1; j2 <= l1; j2++) {
/*     */           
/* 186 */           BlockPos blockpos = new BlockPos(i2, j, j2);
/*     */           
/* 188 */           if (hasRoomForPlayer(worldIn, blockpos)) {
/*     */             
/* 190 */             if (tries <= 0)
/*     */             {
/* 192 */               return blockpos;
/*     */             }
/*     */             
/* 195 */             tries--;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos) {
/* 206 */     return (World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) && !worldIn.getBlockState(pos).getBlock().getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid());
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/* 211 */     if (state.getValue((IProperty)PART) == EnumPartType.FOOT)
/*     */     {
/* 213 */       super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMobilityFlag() {
/* 219 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 224 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 229 */     return Items.bed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
/* 234 */     if (player.capabilities.isCreativeMode && state.getValue((IProperty)PART) == EnumPartType.HEAD) {
/*     */       
/* 236 */       BlockPos blockpos = pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite());
/*     */       
/* 238 */       if (worldIn.getBlockState(blockpos).getBlock() == this)
/*     */       {
/* 240 */         worldIn.setBlockToAir(blockpos);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 247 */     EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
/* 248 */     return ((meta & 0x8) > 0) ? getDefaultState().withProperty((IProperty)PART, EnumPartType.HEAD).withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)OCCUPIED, Boolean.valueOf(((meta & 0x4) > 0))) : getDefaultState().withProperty((IProperty)PART, EnumPartType.FOOT).withProperty((IProperty)FACING, (Comparable)enumfacing);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/* 253 */     if (state.getValue((IProperty)PART) == EnumPartType.FOOT) {
/*     */       
/* 255 */       IBlockState iblockstate = worldIn.getBlockState(pos.offset((EnumFacing)state.getValue((IProperty)FACING)));
/*     */       
/* 257 */       if (iblockstate.getBlock() == this)
/*     */       {
/* 259 */         state = state.withProperty((IProperty)OCCUPIED, iblockstate.getValue((IProperty)OCCUPIED));
/*     */       }
/*     */     } 
/*     */     
/* 263 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 268 */     int i = 0;
/* 269 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
/*     */     
/* 271 */     if (state.getValue((IProperty)PART) == EnumPartType.HEAD) {
/*     */       
/* 273 */       i |= 0x8;
/*     */       
/* 275 */       if (((Boolean)state.getValue((IProperty)OCCUPIED)).booleanValue())
/*     */       {
/* 277 */         i |= 0x4;
/*     */       }
/*     */     } 
/*     */     
/* 281 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 286 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)PART, (IProperty)OCCUPIED });
/*     */   }
/*     */   
/*     */   public enum EnumPartType
/*     */     implements IStringSerializable {
/* 291 */     HEAD("head"),
/* 292 */     FOOT("foot");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     EnumPartType(String name) {
/* 298 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 303 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 308 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockBed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */