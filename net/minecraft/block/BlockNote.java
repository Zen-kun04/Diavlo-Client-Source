/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityNote;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockNote
/*     */   extends BlockContainer {
/*  19 */   private static final List<String> INSTRUMENTS = Lists.newArrayList((Object[])new String[] { "harp", "bd", "snare", "hat", "bassattack" });
/*     */ 
/*     */   
/*     */   public BlockNote() {
/*  23 */     super(Material.wood);
/*  24 */     setCreativeTab(CreativeTabs.tabRedstone);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  29 */     boolean flag = worldIn.isBlockPowered(pos);
/*  30 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  32 */     if (tileentity instanceof TileEntityNote) {
/*     */       
/*  34 */       TileEntityNote tileentitynote = (TileEntityNote)tileentity;
/*     */       
/*  36 */       if (tileentitynote.previousRedstoneState != flag) {
/*     */         
/*  38 */         if (flag)
/*     */         {
/*  40 */           tileentitynote.triggerNote(worldIn, pos);
/*     */         }
/*     */         
/*  43 */         tileentitynote.previousRedstoneState = flag;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  50 */     if (worldIn.isRemote)
/*     */     {
/*  52 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  56 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  58 */     if (tileentity instanceof TileEntityNote) {
/*     */       
/*  60 */       TileEntityNote tileentitynote = (TileEntityNote)tileentity;
/*  61 */       tileentitynote.changePitch();
/*  62 */       tileentitynote.triggerNote(worldIn, pos);
/*  63 */       playerIn.triggerAchievement(StatList.field_181735_S);
/*     */     } 
/*     */     
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
/*  72 */     if (!worldIn.isRemote) {
/*     */       
/*  74 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  76 */       if (tileentity instanceof TileEntityNote) {
/*     */         
/*  78 */         ((TileEntityNote)tileentity).triggerNote(worldIn, pos);
/*  79 */         playerIn.triggerAchievement(StatList.field_181734_R);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/*  86 */     return (TileEntity)new TileEntityNote();
/*     */   }
/*     */ 
/*     */   
/*     */   private String getInstrument(int id) {
/*  91 */     if (id < 0 || id >= INSTRUMENTS.size())
/*     */     {
/*  93 */       id = 0;
/*     */     }
/*     */     
/*  96 */     return INSTRUMENTS.get(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
/* 101 */     float f = (float)Math.pow(2.0D, (eventParam - 12) / 12.0D);
/* 102 */     worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "note." + getInstrument(eventID), 3.0F, f);
/* 103 */     worldIn.spawnParticle(EnumParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, eventParam / 24.0D, 0.0D, 0.0D, new int[0]);
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/* 109 */     return 3;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockNote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */