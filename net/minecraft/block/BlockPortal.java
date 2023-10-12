/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.BlockWorldState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemMonsterPlacer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPortal
/*     */   extends BlockBreakable {
/*  26 */   public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, (Enum[])new EnumFacing.Axis[] { EnumFacing.Axis.X, EnumFacing.Axis.Z });
/*     */ 
/*     */   
/*     */   public BlockPortal() {
/*  30 */     super(Material.portal, false);
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AXIS, (Comparable)EnumFacing.Axis.X));
/*  32 */     setTickRandomly(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  37 */     super.updateTick(worldIn, pos, state, rand);
/*     */     
/*  39 */     if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getBoolean("doMobSpawning") && rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId()) {
/*     */       
/*  41 */       int i = pos.getY();
/*     */       
/*     */       BlockPos blockpos;
/*  44 */       for (blockpos = pos; !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, blockpos) && blockpos.getY() > 0; blockpos = blockpos.down());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  49 */       if (i > 0 && !worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube()) {
/*     */         
/*  51 */         Entity entity = ItemMonsterPlacer.spawnCreature(worldIn, 57, blockpos.getX() + 0.5D, blockpos.getY() + 1.1D, blockpos.getZ() + 0.5D);
/*     */         
/*  53 */         if (entity != null)
/*     */         {
/*  55 */           entity.timeUntilPortal = entity.getPortalCooldown();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  68 */     EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)worldIn.getBlockState(pos).getValue((IProperty)AXIS);
/*  69 */     float f = 0.125F;
/*  70 */     float f1 = 0.125F;
/*     */     
/*  72 */     if (enumfacing$axis == EnumFacing.Axis.X)
/*     */     {
/*  74 */       f = 0.5F;
/*     */     }
/*     */     
/*  77 */     if (enumfacing$axis == EnumFacing.Axis.Z)
/*     */     {
/*  79 */       f1 = 0.5F;
/*     */     }
/*     */     
/*  82 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getMetaForAxis(EnumFacing.Axis axis) {
/*  87 */     return (axis == EnumFacing.Axis.X) ? 1 : ((axis == EnumFacing.Axis.Z) ? 2 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_176548_d(World worldIn, BlockPos p_176548_2_) {
/*  97 */     Size blockportal$size = new Size(worldIn, p_176548_2_, EnumFacing.Axis.X);
/*     */     
/*  99 */     if (blockportal$size.func_150860_b() && blockportal$size.field_150864_e == 0) {
/*     */       
/* 101 */       blockportal$size.func_150859_c();
/* 102 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 106 */     Size blockportal$size1 = new Size(worldIn, p_176548_2_, EnumFacing.Axis.Z);
/*     */     
/* 108 */     if (blockportal$size1.func_150860_b() && blockportal$size1.field_150864_e == 0) {
/*     */       
/* 110 */       blockportal$size1.func_150859_c();
/* 111 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 122 */     EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)state.getValue((IProperty)AXIS);
/*     */     
/* 124 */     if (enumfacing$axis == EnumFacing.Axis.X) {
/*     */       
/* 126 */       Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
/*     */       
/* 128 */       if (!blockportal$size.func_150860_b() || blockportal$size.field_150864_e < blockportal$size.field_150868_h * blockportal$size.field_150862_g)
/*     */       {
/* 130 */         worldIn.setBlockState(pos, Blocks.air.getDefaultState());
/*     */       }
/*     */     }
/* 133 */     else if (enumfacing$axis == EnumFacing.Axis.Z) {
/*     */       
/* 135 */       Size blockportal$size1 = new Size(worldIn, pos, EnumFacing.Axis.Z);
/*     */       
/* 137 */       if (!blockportal$size1.func_150860_b() || blockportal$size1.field_150864_e < blockportal$size1.field_150868_h * blockportal$size1.field_150862_g)
/*     */       {
/* 139 */         worldIn.setBlockState(pos, Blocks.air.getDefaultState());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 146 */     EnumFacing.Axis enumfacing$axis = null;
/* 147 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/* 149 */     if (worldIn.getBlockState(pos).getBlock() == this) {
/*     */       
/* 151 */       enumfacing$axis = (EnumFacing.Axis)iblockstate.getValue((IProperty)AXIS);
/*     */       
/* 153 */       if (enumfacing$axis == null)
/*     */       {
/* 155 */         return false;
/*     */       }
/*     */       
/* 158 */       if (enumfacing$axis == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST)
/*     */       {
/* 160 */         return false;
/*     */       }
/*     */       
/* 163 */       if (enumfacing$axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH)
/*     */       {
/* 165 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 169 */     boolean flag = (worldIn.getBlockState(pos.west()).getBlock() == this && worldIn.getBlockState(pos.west(2)).getBlock() != this);
/* 170 */     boolean flag1 = (worldIn.getBlockState(pos.east()).getBlock() == this && worldIn.getBlockState(pos.east(2)).getBlock() != this);
/* 171 */     boolean flag2 = (worldIn.getBlockState(pos.north()).getBlock() == this && worldIn.getBlockState(pos.north(2)).getBlock() != this);
/* 172 */     boolean flag3 = (worldIn.getBlockState(pos.south()).getBlock() == this && worldIn.getBlockState(pos.south(2)).getBlock() != this);
/* 173 */     boolean flag4 = (flag || flag1 || enumfacing$axis == EnumFacing.Axis.X);
/* 174 */     boolean flag5 = (flag2 || flag3 || enumfacing$axis == EnumFacing.Axis.Z);
/* 175 */     return (flag4 && side == EnumFacing.WEST) ? true : ((flag4 && side == EnumFacing.EAST) ? true : ((flag5 && side == EnumFacing.NORTH) ? true : ((flag5 && side == EnumFacing.SOUTH))));
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/* 180 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 185 */     return EnumWorldBlockLayer.TRANSLUCENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
/* 190 */     if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null)
/*     */     {
/* 192 */       entityIn.setPortal(pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 198 */     if (rand.nextInt(100) == 0)
/*     */     {
/* 200 */       worldIn.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "portal.portal", 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
/*     */     }
/*     */     
/* 203 */     for (int i = 0; i < 4; i++) {
/*     */       
/* 205 */       double d0 = (pos.getX() + rand.nextFloat());
/* 206 */       double d1 = (pos.getY() + rand.nextFloat());
/* 207 */       double d2 = (pos.getZ() + rand.nextFloat());
/* 208 */       double d3 = (rand.nextFloat() - 0.5D) * 0.5D;
/* 209 */       double d4 = (rand.nextFloat() - 0.5D) * 0.5D;
/* 210 */       double d5 = (rand.nextFloat() - 0.5D) * 0.5D;
/* 211 */       int j = rand.nextInt(2) * 2 - 1;
/*     */       
/* 213 */       if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this) {
/*     */         
/* 215 */         d0 = pos.getX() + 0.5D + 0.25D * j;
/* 216 */         d3 = (rand.nextFloat() * 2.0F * j);
/*     */       }
/*     */       else {
/*     */         
/* 220 */         d2 = pos.getZ() + 0.5D + 0.25D * j;
/* 221 */         d5 = (rand.nextFloat() * 2.0F * j);
/*     */       } 
/*     */       
/* 224 */       worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 235 */     return getDefaultState().withProperty((IProperty)AXIS, ((meta & 0x3) == 2) ? (Comparable)EnumFacing.Axis.Z : (Comparable)EnumFacing.Axis.X);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 240 */     return getMetaForAxis((EnumFacing.Axis)state.getValue((IProperty)AXIS));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 245 */     return new BlockState(this, new IProperty[] { (IProperty)AXIS });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPattern.PatternHelper func_181089_f(World p_181089_1_, BlockPos p_181089_2_) {
/* 250 */     EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
/* 251 */     Size blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.X);
/* 252 */     LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.func_181627_a(p_181089_1_, true);
/*     */     
/* 254 */     if (!blockportal$size.func_150860_b()) {
/*     */       
/* 256 */       enumfacing$axis = EnumFacing.Axis.X;
/* 257 */       blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.Z);
/*     */     } 
/*     */     
/* 260 */     if (!blockportal$size.func_150860_b())
/*     */     {
/* 262 */       return new BlockPattern.PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
/*     */     }
/*     */ 
/*     */     
/* 266 */     int[] aint = new int[(EnumFacing.AxisDirection.values()).length];
/* 267 */     EnumFacing enumfacing = blockportal$size.field_150866_c.rotateYCCW();
/* 268 */     BlockPos blockpos = blockportal$size.field_150861_f.up(blockportal$size.func_181100_a() - 1);
/*     */     
/* 270 */     for (EnumFacing.AxisDirection enumfacing$axisdirection : EnumFacing.AxisDirection.values()) {
/*     */       
/* 272 */       BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper((enumfacing.getAxisDirection() == enumfacing$axisdirection) ? blockpos : blockpos.offset(blockportal$size.field_150866_c, blockportal$size.func_181101_b() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
/*     */       
/* 274 */       for (int i = 0; i < blockportal$size.func_181101_b(); i++) {
/*     */         
/* 276 */         for (int j = 0; j < blockportal$size.func_181100_a(); j++) {
/*     */           
/* 278 */           BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);
/*     */           
/* 280 */           if (blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getBlock().getMaterial() != Material.air)
/*     */           {
/* 282 */             aint[enumfacing$axisdirection.ordinal()] = aint[enumfacing$axisdirection.ordinal()] + 1;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 288 */     EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;
/*     */     
/* 290 */     for (EnumFacing.AxisDirection enumfacing$axisdirection2 : EnumFacing.AxisDirection.values()) {
/*     */       
/* 292 */       if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()])
/*     */       {
/* 294 */         enumfacing$axisdirection1 = enumfacing$axisdirection2;
/*     */       }
/*     */     } 
/*     */     
/* 298 */     return new BlockPattern.PatternHelper((enumfacing.getAxisDirection() == enumfacing$axisdirection1) ? blockpos : blockpos.offset(blockportal$size.field_150866_c, blockportal$size.func_181101_b() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Size
/*     */   {
/*     */     private final World world;
/*     */     private final EnumFacing.Axis axis;
/*     */     private final EnumFacing field_150866_c;
/*     */     private final EnumFacing field_150863_d;
/* 308 */     private int field_150864_e = 0;
/*     */     
/*     */     private BlockPos field_150861_f;
/*     */     private int field_150862_g;
/*     */     private int field_150868_h;
/*     */     
/*     */     public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_) {
/* 315 */       this.world = worldIn;
/* 316 */       this.axis = p_i45694_3_;
/*     */       
/* 318 */       if (p_i45694_3_ == EnumFacing.Axis.X) {
/*     */         
/* 320 */         this.field_150863_d = EnumFacing.EAST;
/* 321 */         this.field_150866_c = EnumFacing.WEST;
/*     */       }
/*     */       else {
/*     */         
/* 325 */         this.field_150863_d = EnumFacing.NORTH;
/* 326 */         this.field_150866_c = EnumFacing.SOUTH;
/*     */       } 
/*     */       
/* 329 */       for (BlockPos blockpos = p_i45694_2_; p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 && func_150857_a(worldIn.getBlockState(p_i45694_2_.down()).getBlock()); p_i45694_2_ = p_i45694_2_.down());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 334 */       int i = func_180120_a(p_i45694_2_, this.field_150863_d) - 1;
/*     */       
/* 336 */       if (i >= 0) {
/*     */         
/* 338 */         this.field_150861_f = p_i45694_2_.offset(this.field_150863_d, i);
/* 339 */         this.field_150868_h = func_180120_a(this.field_150861_f, this.field_150866_c);
/*     */         
/* 341 */         if (this.field_150868_h < 2 || this.field_150868_h > 21) {
/*     */           
/* 343 */           this.field_150861_f = null;
/* 344 */           this.field_150868_h = 0;
/*     */         } 
/*     */       } 
/*     */       
/* 348 */       if (this.field_150861_f != null)
/*     */       {
/* 350 */         this.field_150862_g = func_150858_a();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected int func_180120_a(BlockPos p_180120_1_, EnumFacing p_180120_2_) {
/*     */       int i;
/* 358 */       for (i = 0; i < 22; i++) {
/*     */         
/* 360 */         BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);
/*     */         
/* 362 */         if (!func_150857_a(this.world.getBlockState(blockpos).getBlock()) || this.world.getBlockState(blockpos.down()).getBlock() != Blocks.obsidian) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 368 */       Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
/* 369 */       return (block == Blocks.obsidian) ? i : 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int func_181100_a() {
/* 374 */       return this.field_150862_g;
/*     */     }
/*     */ 
/*     */     
/*     */     public int func_181101_b() {
/* 379 */       return this.field_150868_h;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected int func_150858_a() {
/* 386 */       label38: for (this.field_150862_g = 0; this.field_150862_g < 21; this.field_150862_g++) {
/*     */         
/* 388 */         for (int i = 0; i < this.field_150868_h; i++) {
/*     */           
/* 390 */           BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i).up(this.field_150862_g);
/* 391 */           Block block = this.world.getBlockState(blockpos).getBlock();
/*     */           
/* 393 */           if (!func_150857_a(block)) {
/*     */             break label38;
/*     */           }
/*     */ 
/*     */           
/* 398 */           if (block == Blocks.portal)
/*     */           {
/* 400 */             this.field_150864_e++;
/*     */           }
/*     */           
/* 403 */           if (i == 0) {
/*     */             
/* 405 */             block = this.world.getBlockState(blockpos.offset(this.field_150863_d)).getBlock();
/*     */             
/* 407 */             if (block != Blocks.obsidian)
/*     */             {
/*     */               break label38;
/*     */             }
/*     */           }
/* 412 */           else if (i == this.field_150868_h - 1) {
/*     */             
/* 414 */             block = this.world.getBlockState(blockpos.offset(this.field_150866_c)).getBlock();
/*     */             
/* 416 */             if (block != Blocks.obsidian) {
/*     */               break label38;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 424 */       for (int j = 0; j < this.field_150868_h; j++) {
/*     */         
/* 426 */         if (this.world.getBlockState(this.field_150861_f.offset(this.field_150866_c, j).up(this.field_150862_g)).getBlock() != Blocks.obsidian) {
/*     */           
/* 428 */           this.field_150862_g = 0;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 433 */       if (this.field_150862_g <= 21 && this.field_150862_g >= 3)
/*     */       {
/* 435 */         return this.field_150862_g;
/*     */       }
/*     */ 
/*     */       
/* 439 */       this.field_150861_f = null;
/* 440 */       this.field_150868_h = 0;
/* 441 */       this.field_150862_g = 0;
/* 442 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean func_150857_a(Block p_150857_1_) {
/* 448 */       return (p_150857_1_.blockMaterial == Material.air || p_150857_1_ == Blocks.fire || p_150857_1_ == Blocks.portal);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean func_150860_b() {
/* 453 */       return (this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21);
/*     */     }
/*     */ 
/*     */     
/*     */     public void func_150859_c() {
/* 458 */       for (int i = 0; i < this.field_150868_h; i++) {
/*     */         
/* 460 */         BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i);
/*     */         
/* 462 */         for (int j = 0; j < this.field_150862_g; j++)
/*     */         {
/* 464 */           this.world.setBlockState(blockpos.up(j), Blocks.portal.getDefaultState().withProperty((IProperty)BlockPortal.AXIS, (Comparable)this.axis), 2);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockPortal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */