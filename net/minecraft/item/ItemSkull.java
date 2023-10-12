/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSkull;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntitySkull;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemSkull extends Item {
/*  25 */   private static final String[] skullTypes = new String[] { "skeleton", "wither", "zombie", "char", "creeper" };
/*     */ 
/*     */   
/*     */   public ItemSkull() {
/*  29 */     setCreativeTab(CreativeTabs.tabDecorations);
/*  30 */     setMaxDamage(0);
/*  31 */     setHasSubtypes(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  36 */     if (side == EnumFacing.DOWN)
/*     */     {
/*  38 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  42 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  43 */     Block block = iblockstate.getBlock();
/*  44 */     boolean flag = block.isReplaceable(worldIn, pos);
/*     */     
/*  46 */     if (!flag) {
/*     */       
/*  48 */       if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid())
/*     */       {
/*  50 */         return false;
/*     */       }
/*     */       
/*  53 */       pos = pos.offset(side);
/*     */     } 
/*     */     
/*  56 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*     */     {
/*  58 */       return false;
/*     */     }
/*  60 */     if (!Blocks.skull.canPlaceBlockAt(worldIn, pos))
/*     */     {
/*  62 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  66 */     if (!worldIn.isRemote) {
/*     */       
/*  68 */       worldIn.setBlockState(pos, Blocks.skull.getDefaultState().withProperty((IProperty)BlockSkull.FACING, (Comparable)side), 3);
/*  69 */       int i = 0;
/*     */       
/*  71 */       if (side == EnumFacing.UP)
/*     */       {
/*  73 */         i = MathHelper.floor_double((playerIn.rotationYaw * 16.0F / 360.0F) + 0.5D) & 0xF;
/*     */       }
/*     */       
/*  76 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  78 */       if (tileentity instanceof TileEntitySkull) {
/*     */         
/*  80 */         TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
/*     */         
/*  82 */         if (stack.getMetadata() == 3) {
/*     */           
/*  84 */           GameProfile gameprofile = null;
/*     */           
/*  86 */           if (stack.hasTagCompound()) {
/*     */             
/*  88 */             NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */             
/*  90 */             if (nbttagcompound.hasKey("SkullOwner", 10)) {
/*     */               
/*  92 */               gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
/*     */             }
/*  94 */             else if (nbttagcompound.hasKey("SkullOwner", 8) && nbttagcompound.getString("SkullOwner").length() > 0) {
/*     */               
/*  96 */               gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
/*     */             } 
/*     */           } 
/*     */           
/* 100 */           tileentityskull.setPlayerProfile(gameprofile);
/*     */         }
/*     */         else {
/*     */           
/* 104 */           tileentityskull.setType(stack.getMetadata());
/*     */         } 
/*     */         
/* 107 */         tileentityskull.setSkullRotation(i);
/* 108 */         Blocks.skull.checkWitherSpawn(worldIn, pos, tileentityskull);
/*     */       } 
/*     */       
/* 111 */       stack.stackSize--;
/*     */     } 
/*     */     
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
/* 121 */     for (int i = 0; i < skullTypes.length; i++)
/*     */     {
/* 123 */       subItems.add(new ItemStack(itemIn, 1, i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetadata(int damage) {
/* 129 */     return damage;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName(ItemStack stack) {
/* 134 */     int i = stack.getMetadata();
/*     */     
/* 136 */     if (i < 0 || i >= skullTypes.length)
/*     */     {
/* 138 */       i = 0;
/*     */     }
/*     */     
/* 141 */     return getUnlocalizedName() + "." + skullTypes[i];
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemStackDisplayName(ItemStack stack) {
/* 146 */     if (stack.getMetadata() == 3 && stack.hasTagCompound()) {
/*     */       
/* 148 */       if (stack.getTagCompound().hasKey("SkullOwner", 8))
/*     */       {
/* 150 */         return StatCollector.translateToLocalFormatted("item.skull.player.name", new Object[] { stack.getTagCompound().getString("SkullOwner") });
/*     */       }
/*     */       
/* 153 */       if (stack.getTagCompound().hasKey("SkullOwner", 10)) {
/*     */         
/* 155 */         NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("SkullOwner");
/*     */         
/* 157 */         if (nbttagcompound.hasKey("Name", 8))
/*     */         {
/* 159 */           return StatCollector.translateToLocalFormatted("item.skull.player.name", new Object[] { nbttagcompound.getString("Name") });
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     return super.getItemStackDisplayName(stack);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean updateItemStackNBT(NBTTagCompound nbt) {
/* 169 */     super.updateItemStackNBT(nbt);
/*     */     
/* 171 */     if (nbt.hasKey("SkullOwner", 8) && nbt.getString("SkullOwner").length() > 0) {
/*     */       
/* 173 */       GameProfile gameprofile = new GameProfile((UUID)null, nbt.getString("SkullOwner"));
/* 174 */       gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
/* 175 */       nbt.setTag("SkullOwner", (NBTBase)NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
/* 176 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 180 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */