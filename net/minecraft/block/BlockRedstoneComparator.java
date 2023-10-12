/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityComparator;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneComparator
/*     */   extends BlockRedstoneDiode implements ITileEntityProvider {
/*  31 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*  32 */   public static final PropertyEnum<Mode> MODE = PropertyEnum.create("mode", Mode.class);
/*     */ 
/*     */   
/*     */   public BlockRedstoneComparator(boolean powered) {
/*  36 */     super(powered);
/*  37 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)MODE, Mode.COMPARE));
/*  38 */     this.isBlockContainer = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalizedName() {
/*  43 */     return StatCollector.translateToLocal("item.comparator.name");
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  48 */     return Items.comparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/*  53 */     return Items.comparator;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDelay(IBlockState state) {
/*  58 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected IBlockState getPoweredState(IBlockState unpoweredState) {
/*  63 */     Boolean obool = (Boolean)unpoweredState.getValue((IProperty)POWERED);
/*  64 */     Mode blockredstonecomparator$mode = (Mode)unpoweredState.getValue((IProperty)MODE);
/*  65 */     EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue((IProperty)FACING);
/*  66 */     return Blocks.powered_comparator.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)POWERED, obool).withProperty((IProperty)MODE, blockredstonecomparator$mode);
/*     */   }
/*     */ 
/*     */   
/*     */   protected IBlockState getUnpoweredState(IBlockState poweredState) {
/*  71 */     Boolean obool = (Boolean)poweredState.getValue((IProperty)POWERED);
/*  72 */     Mode blockredstonecomparator$mode = (Mode)poweredState.getValue((IProperty)MODE);
/*  73 */     EnumFacing enumfacing = (EnumFacing)poweredState.getValue((IProperty)FACING);
/*  74 */     return Blocks.unpowered_comparator.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)POWERED, obool).withProperty((IProperty)MODE, blockredstonecomparator$mode);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPowered(IBlockState state) {
/*  79 */     return (this.isRepeaterPowered || ((Boolean)state.getValue((IProperty)POWERED)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
/*  84 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*  85 */     return (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private int calculateOutput(World worldIn, BlockPos pos, IBlockState state) {
/*  90 */     return (state.getValue((IProperty)MODE) == Mode.SUBTRACT) ? Math.max(calculateInputStrength(worldIn, pos, state) - getPowerOnSides((IBlockAccess)worldIn, pos, state), 0) : calculateInputStrength(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
/*  95 */     int i = calculateInputStrength(worldIn, pos, state);
/*     */     
/*  97 */     if (i >= 15)
/*     */     {
/*  99 */       return true;
/*     */     }
/* 101 */     if (i == 0)
/*     */     {
/* 103 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 107 */     int j = getPowerOnSides((IBlockAccess)worldIn, pos, state);
/* 108 */     return (j == 0) ? true : ((i >= j));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
/* 114 */     int i = super.calculateInputStrength(worldIn, pos, state);
/* 115 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 116 */     BlockPos blockpos = pos.offset(enumfacing);
/* 117 */     Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */     
/* 119 */     if (block.hasComparatorInputOverride()) {
/*     */       
/* 121 */       i = block.getComparatorInputOverride(worldIn, blockpos);
/*     */     }
/* 123 */     else if (i < 15 && block.isNormalCube()) {
/*     */       
/* 125 */       blockpos = blockpos.offset(enumfacing);
/* 126 */       block = worldIn.getBlockState(blockpos).getBlock();
/*     */       
/* 128 */       if (block.hasComparatorInputOverride()) {
/*     */         
/* 130 */         i = block.getComparatorInputOverride(worldIn, blockpos);
/*     */       }
/* 132 */       else if (block.getMaterial() == Material.air) {
/*     */         
/* 134 */         EntityItemFrame entityitemframe = findItemFrame(worldIn, enumfacing, blockpos);
/*     */         
/* 136 */         if (entityitemframe != null)
/*     */         {
/* 138 */           i = entityitemframe.func_174866_q();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   private EntityItemFrame findItemFrame(World worldIn, final EnumFacing facing, BlockPos pos) {
/* 148 */     List<EntityItemFrame> list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1)), new Predicate<Entity>()
/*     */         {
/*     */           public boolean apply(Entity p_apply_1_)
/*     */           {
/* 152 */             return (p_apply_1_ != null && p_apply_1_.getHorizontalFacing() == facing);
/*     */           }
/*     */         });
/* 155 */     return (list.size() == 1) ? list.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 160 */     if (!playerIn.capabilities.allowEdit)
/*     */     {
/* 162 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 166 */     state = state.cycleProperty((IProperty)MODE);
/* 167 */     worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, (state.getValue((IProperty)MODE) == Mode.SUBTRACT) ? 0.55F : 0.5F);
/* 168 */     worldIn.setBlockState(pos, state, 2);
/* 169 */     onStateChange(worldIn, pos, state);
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
/* 176 */     if (!worldIn.isBlockTickPending(pos, this)) {
/*     */       
/* 178 */       int i = calculateOutput(worldIn, pos, state);
/* 179 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/* 180 */       int j = (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
/*     */       
/* 182 */       if (i != j || isPowered(state) != shouldBePowered(worldIn, pos, state))
/*     */       {
/* 184 */         if (isFacingTowardsRepeater(worldIn, pos, state)) {
/*     */           
/* 186 */           worldIn.updateBlockTick(pos, this, 2, -1);
/*     */         }
/*     */         else {
/*     */           
/* 190 */           worldIn.updateBlockTick(pos, this, 2, 0);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void onStateChange(World worldIn, BlockPos pos, IBlockState state) {
/* 198 */     int i = calculateOutput(worldIn, pos, state);
/* 199 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 200 */     int j = 0;
/*     */     
/* 202 */     if (tileentity instanceof TileEntityComparator) {
/*     */       
/* 204 */       TileEntityComparator tileentitycomparator = (TileEntityComparator)tileentity;
/* 205 */       j = tileentitycomparator.getOutputSignal();
/* 206 */       tileentitycomparator.setOutputSignal(i);
/*     */     } 
/*     */     
/* 209 */     if (j != i || state.getValue((IProperty)MODE) == Mode.COMPARE) {
/*     */       
/* 211 */       boolean flag1 = shouldBePowered(worldIn, pos, state);
/* 212 */       boolean flag = isPowered(state);
/*     */       
/* 214 */       if (flag && !flag1) {
/*     */         
/* 216 */         worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(false)), 2);
/*     */       }
/* 218 */       else if (!flag && flag1) {
/*     */         
/* 220 */         worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)), 2);
/*     */       } 
/*     */       
/* 223 */       notifyNeighbors(worldIn, pos, state);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 229 */     if (this.isRepeaterPowered)
/*     */     {
/* 231 */       worldIn.setBlockState(pos, getUnpoweredState(state).withProperty((IProperty)POWERED, Boolean.valueOf(true)), 4);
/*     */     }
/*     */     
/* 234 */     onStateChange(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 239 */     super.onBlockAdded(worldIn, pos, state);
/* 240 */     worldIn.setTileEntity(pos, createNewTileEntity(worldIn, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 245 */     super.breakBlock(worldIn, pos, state);
/* 246 */     worldIn.removeTileEntity(pos);
/* 247 */     notifyNeighbors(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
/* 252 */     super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
/* 253 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 254 */     return (tileentity == null) ? false : tileentity.receiveClientEvent(eventID, eventParam);
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 259 */     return (TileEntity)new TileEntityComparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 264 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0))).withProperty((IProperty)MODE, ((meta & 0x4) > 0) ? Mode.SUBTRACT : Mode.COMPARE);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 269 */     int i = 0;
/* 270 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
/*     */     
/* 272 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/* 274 */       i |= 0x8;
/*     */     }
/*     */     
/* 277 */     if (state.getValue((IProperty)MODE) == Mode.SUBTRACT)
/*     */     {
/* 279 */       i |= 0x4;
/*     */     }
/*     */     
/* 282 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 287 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)MODE, (IProperty)POWERED });
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 292 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite()).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)MODE, Mode.COMPARE);
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */     implements IStringSerializable {
/* 297 */     COMPARE("compare"),
/* 298 */     SUBTRACT("subtract");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     Mode(String name) {
/* 304 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 309 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 314 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRedstoneComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */