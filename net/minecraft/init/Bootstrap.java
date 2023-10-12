/*     */ package net.minecraft.init;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDispenser;
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.BlockPumpkin;
/*     */ import net.minecraft.block.BlockSkull;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
/*     */ import net.minecraft.dispenser.BehaviorProjectileDispense;
/*     */ import net.minecraft.dispenser.IBlockSource;
/*     */ import net.minecraft.dispenser.IPosition;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IProjectile;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.item.EntityExpBottle;
/*     */ import net.minecraft.entity.item.EntityFireworkRocket;
/*     */ import net.minecraft.entity.item.EntityTNTPrimed;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.entity.projectile.EntitySnowball;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBucket;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntitySkull;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class Bootstrap {
/*  38 */   private static final PrintStream SYSOUT = System.out;
/*     */   private static boolean alreadyRegistered = false;
/*  40 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   public static boolean isRegistered() {
/*  44 */     return alreadyRegistered;
/*     */   }
/*     */ 
/*     */   
/*     */   static void registerDispenserBehaviors() {
/*  49 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense()
/*     */         {
/*     */           protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */           {
/*  53 */             EntityArrow entityarrow = new EntityArrow(worldIn, position.getX(), position.getY(), position.getZ());
/*  54 */             entityarrow.canBePickedUp = 1;
/*  55 */             return (IProjectile)entityarrow;
/*     */           }
/*     */         });
/*  58 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense()
/*     */         {
/*     */           protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */           {
/*  62 */             return (IProjectile)new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
/*     */           }
/*     */         });
/*  65 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense()
/*     */         {
/*     */           protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */           {
/*  69 */             return (IProjectile)new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
/*     */           }
/*     */         });
/*  72 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense()
/*     */         {
/*     */           protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */           {
/*  76 */             return (IProjectile)new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
/*     */           }
/*     */           
/*     */           protected float func_82498_a() {
/*  80 */             return super.func_82498_a() * 0.5F;
/*     */           }
/*     */           
/*     */           protected float func_82500_b() {
/*  84 */             return super.func_82500_b() * 1.25F;
/*     */           }
/*     */         });
/*  87 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem()
/*     */         {
/*  89 */           private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();
/*     */           
/*     */           public ItemStack dispense(IBlockSource source, final ItemStack stack) {
/*  92 */             return ItemPotion.isSplash(stack.getMetadata()) ? (new BehaviorProjectileDispense()
/*     */               {
/*     */                 protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */                 {
/*  96 */                   return (IProjectile)new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
/*     */                 }
/*     */                 
/*     */                 protected float func_82498_a() {
/* 100 */                   return super.func_82498_a() * 0.5F;
/*     */                 }
/*     */                 
/*     */                 protected float func_82500_b() {
/* 104 */                   return super.func_82500_b() * 1.25F;
/*     */                 }
/* 106 */               }).dispense(source, stack) : this.field_150843_b.dispense(source, stack);
/*     */           }
/*     */         });
/* 109 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem()
/*     */         {
/*     */           public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*     */           {
/* 113 */             EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 114 */             double d0 = source.getX() + enumfacing.getFrontOffsetX();
/* 115 */             double d1 = (source.getBlockPos().getY() + 0.2F);
/* 116 */             double d2 = source.getZ() + enumfacing.getFrontOffsetZ();
/* 117 */             Entity entity = ItemMonsterPlacer.spawnCreature(source.getWorld(), stack.getMetadata(), d0, d1, d2);
/*     */             
/* 119 */             if (entity instanceof EntityLivingBase && stack.hasDisplayName())
/*     */             {
/* 121 */               ((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
/*     */             }
/*     */             
/* 124 */             stack.splitStack(1);
/* 125 */             return stack;
/*     */           }
/*     */         });
/* 128 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem()
/*     */         {
/*     */           public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*     */           {
/* 132 */             EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 133 */             double d0 = source.getX() + enumfacing.getFrontOffsetX();
/* 134 */             double d1 = (source.getBlockPos().getY() + 0.2F);
/* 135 */             double d2 = source.getZ() + enumfacing.getFrontOffsetZ();
/* 136 */             EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(source.getWorld(), d0, d1, d2, stack);
/* 137 */             source.getWorld().spawnEntityInWorld((Entity)entityfireworkrocket);
/* 138 */             stack.splitStack(1);
/* 139 */             return stack;
/*     */           }
/*     */           
/*     */           protected void playDispenseSound(IBlockSource source) {
/* 143 */             source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
/*     */           }
/*     */         });
/* 146 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem()
/*     */         {
/*     */           public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*     */           {
/* 150 */             EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 151 */             IPosition iposition = BlockDispenser.getDispensePosition(source);
/* 152 */             double d0 = iposition.getX() + (enumfacing.getFrontOffsetX() * 0.3F);
/* 153 */             double d1 = iposition.getY() + (enumfacing.getFrontOffsetY() * 0.3F);
/* 154 */             double d2 = iposition.getZ() + (enumfacing.getFrontOffsetZ() * 0.3F);
/* 155 */             World world = source.getWorld();
/* 156 */             Random random = world.rand;
/* 157 */             double d3 = random.nextGaussian() * 0.05D + enumfacing.getFrontOffsetX();
/* 158 */             double d4 = random.nextGaussian() * 0.05D + enumfacing.getFrontOffsetY();
/* 159 */             double d5 = random.nextGaussian() * 0.05D + enumfacing.getFrontOffsetZ();
/* 160 */             world.spawnEntityInWorld((Entity)new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5));
/* 161 */             stack.splitStack(1);
/* 162 */             return stack;
/*     */           }
/*     */           
/*     */           protected void playDispenseSound(IBlockSource source) {
/* 166 */             source.getWorld().playAuxSFX(1009, source.getBlockPos(), 0);
/*     */           }
/*     */         });
/* 169 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem()
/*     */         {
/* 171 */           private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();
/*     */           public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/*     */             double d3;
/* 174 */             EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 175 */             World world = source.getWorld();
/* 176 */             double d0 = source.getX() + (enumfacing.getFrontOffsetX() * 1.125F);
/* 177 */             double d1 = source.getY() + (enumfacing.getFrontOffsetY() * 1.125F);
/* 178 */             double d2 = source.getZ() + (enumfacing.getFrontOffsetZ() * 1.125F);
/* 179 */             BlockPos blockpos = source.getBlockPos().offset(enumfacing);
/* 180 */             Material material = world.getBlockState(blockpos).getBlock().getMaterial();
/*     */ 
/*     */             
/* 183 */             if (Material.water.equals(material)) {
/*     */               
/* 185 */               d3 = 1.0D;
/*     */             }
/*     */             else {
/*     */               
/* 189 */               if (!Material.air.equals(material) || !Material.water.equals(world.getBlockState(blockpos.down()).getBlock().getMaterial()))
/*     */               {
/* 191 */                 return this.field_150842_b.dispense(source, stack);
/*     */               }
/*     */               
/* 194 */               d3 = 0.0D;
/*     */             } 
/*     */             
/* 197 */             EntityBoat entityboat = new EntityBoat(world, d0, d1 + d3, d2);
/* 198 */             world.spawnEntityInWorld((Entity)entityboat);
/* 199 */             stack.splitStack(1);
/* 200 */             return stack;
/*     */           }
/*     */           
/*     */           protected void playDispenseSound(IBlockSource source) {
/* 204 */             source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */           }
/*     */         });
/* 207 */     BehaviorDefaultDispenseItem behaviorDefaultDispenseItem = new BehaviorDefaultDispenseItem()
/*     */       {
/* 209 */         private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();
/*     */         
/*     */         public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 212 */           ItemBucket itembucket = (ItemBucket)stack.getItem();
/* 213 */           BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/*     */           
/* 215 */           if (itembucket.tryPlaceContainedLiquid(source.getWorld(), blockpos)) {
/*     */             
/* 217 */             stack.setItem(Items.bucket);
/* 218 */             stack.stackSize = 1;
/* 219 */             return stack;
/*     */           } 
/*     */ 
/*     */           
/* 223 */           return this.field_150841_b.dispense(source, stack);
/*     */         }
/*     */       };
/*     */     
/* 227 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, behaviorDefaultDispenseItem);
/* 228 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, behaviorDefaultDispenseItem);
/* 229 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem()
/*     */         {
/* 231 */           private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();
/*     */           public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/*     */             Item item;
/* 234 */             World world = source.getWorld();
/* 235 */             BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/* 236 */             IBlockState iblockstate = world.getBlockState(blockpos);
/* 237 */             Block block = iblockstate.getBlock();
/* 238 */             Material material = block.getMaterial();
/*     */ 
/*     */             
/* 241 */             if (Material.water.equals(material) && block instanceof BlockLiquid && ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0) {
/*     */               
/* 243 */               item = Items.water_bucket;
/*     */             }
/*     */             else {
/*     */               
/* 247 */               if (!Material.lava.equals(material) || !(block instanceof BlockLiquid) || ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() != 0)
/*     */               {
/* 249 */                 return super.dispenseStack(source, stack);
/*     */               }
/*     */               
/* 252 */               item = Items.lava_bucket;
/*     */             } 
/*     */             
/* 255 */             world.setBlockToAir(blockpos);
/*     */             
/* 257 */             if (--stack.stackSize == 0) {
/*     */               
/* 259 */               stack.setItem(item);
/* 260 */               stack.stackSize = 1;
/*     */             }
/* 262 */             else if (((TileEntityDispenser)source.getBlockTileEntity()).addItemStack(new ItemStack(item)) < 0) {
/*     */               
/* 264 */               this.field_150840_b.dispense(source, new ItemStack(item));
/*     */             } 
/*     */             
/* 267 */             return stack;
/*     */           }
/*     */         });
/* 270 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem()
/*     */         {
/*     */           private boolean field_150839_b = true;
/*     */           
/*     */           protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 275 */             World world = source.getWorld();
/* 276 */             BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/*     */             
/* 278 */             if (world.isAirBlock(blockpos)) {
/*     */               
/* 280 */               world.setBlockState(blockpos, Blocks.fire.getDefaultState());
/*     */               
/* 282 */               if (stack.attemptDamageItem(1, world.rand))
/*     */               {
/* 284 */                 stack.stackSize = 0;
/*     */               }
/*     */             }
/* 287 */             else if (world.getBlockState(blockpos).getBlock() == Blocks.tnt) {
/*     */               
/* 289 */               Blocks.tnt.onBlockDestroyedByPlayer(world, blockpos, Blocks.tnt.getDefaultState().withProperty((IProperty)BlockTNT.EXPLODE, Boolean.valueOf(true)));
/* 290 */               world.setBlockToAir(blockpos);
/*     */             }
/*     */             else {
/*     */               
/* 294 */               this.field_150839_b = false;
/*     */             } 
/*     */             
/* 297 */             return stack;
/*     */           }
/*     */           
/*     */           protected void playDispenseSound(IBlockSource source) {
/* 301 */             if (this.field_150839_b) {
/*     */               
/* 303 */               source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */             }
/*     */             else {
/*     */               
/* 307 */               source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
/*     */             } 
/*     */           }
/*     */         });
/* 311 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem()
/*     */         {
/*     */           private boolean field_150838_b = true;
/*     */           
/*     */           protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 316 */             if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(stack.getMetadata())) {
/*     */               
/* 318 */               World world = source.getWorld();
/* 319 */               BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/*     */               
/* 321 */               if (ItemDye.applyBonemeal(stack, world, blockpos)) {
/*     */                 
/* 323 */                 if (!world.isRemote)
/*     */                 {
/* 325 */                   world.playAuxSFX(2005, blockpos, 0);
/*     */                 }
/*     */               }
/*     */               else {
/*     */                 
/* 330 */                 this.field_150838_b = false;
/*     */               } 
/*     */               
/* 333 */               return stack;
/*     */             } 
/*     */ 
/*     */             
/* 337 */             return super.dispenseStack(source, stack);
/*     */           }
/*     */ 
/*     */           
/*     */           protected void playDispenseSound(IBlockSource source) {
/* 342 */             if (this.field_150838_b) {
/*     */               
/* 344 */               source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */             }
/*     */             else {
/*     */               
/* 348 */               source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
/*     */             } 
/*     */           }
/*     */         });
/* 352 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem()
/*     */         {
/*     */           protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*     */           {
/* 356 */             World world = source.getWorld();
/* 357 */             BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/* 358 */             EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D, (EntityLivingBase)null);
/* 359 */             world.spawnEntityInWorld((Entity)entitytntprimed);
/* 360 */             world.playSoundAtEntity((Entity)entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
/* 361 */             stack.stackSize--;
/* 362 */             return stack;
/*     */           }
/*     */         });
/* 365 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.skull, new BehaviorDefaultDispenseItem()
/*     */         {
/*     */           private boolean field_179240_b = true;
/*     */           
/*     */           protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 370 */             World world = source.getWorld();
/* 371 */             EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 372 */             BlockPos blockpos = source.getBlockPos().offset(enumfacing);
/* 373 */             BlockSkull blockskull = Blocks.skull;
/*     */             
/* 375 */             if (world.isAirBlock(blockpos) && blockskull.canDispenserPlace(world, blockpos, stack)) {
/*     */               
/* 377 */               if (!world.isRemote)
/*     */               {
/* 379 */                 world.setBlockState(blockpos, blockskull.getDefaultState().withProperty((IProperty)BlockSkull.FACING, (Comparable)EnumFacing.UP), 3);
/* 380 */                 TileEntity tileentity = world.getTileEntity(blockpos);
/*     */                 
/* 382 */                 if (tileentity instanceof TileEntitySkull) {
/*     */                   
/* 384 */                   if (stack.getMetadata() == 3) {
/*     */                     
/* 386 */                     GameProfile gameprofile = null;
/*     */                     
/* 388 */                     if (stack.hasTagCompound()) {
/*     */                       
/* 390 */                       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */                       
/* 392 */                       if (nbttagcompound.hasKey("SkullOwner", 10)) {
/*     */                         
/* 394 */                         gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
/*     */                       }
/* 396 */                       else if (nbttagcompound.hasKey("SkullOwner", 8)) {
/*     */                         
/* 398 */                         String s = nbttagcompound.getString("SkullOwner");
/*     */                         
/* 400 */                         if (!StringUtils.isNullOrEmpty(s))
/*     */                         {
/* 402 */                           gameprofile = new GameProfile((UUID)null, s);
/*     */                         }
/*     */                       } 
/*     */                     } 
/*     */                     
/* 407 */                     ((TileEntitySkull)tileentity).setPlayerProfile(gameprofile);
/*     */                   }
/*     */                   else {
/*     */                     
/* 411 */                     ((TileEntitySkull)tileentity).setType(stack.getMetadata());
/*     */                   } 
/*     */                   
/* 414 */                   ((TileEntitySkull)tileentity).setSkullRotation(enumfacing.getOpposite().getHorizontalIndex() * 4);
/* 415 */                   Blocks.skull.checkWitherSpawn(world, blockpos, (TileEntitySkull)tileentity);
/*     */                 } 
/*     */                 
/* 418 */                 stack.stackSize--;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 423 */               this.field_179240_b = false;
/*     */             } 
/*     */             
/* 426 */             return stack;
/*     */           }
/*     */           
/*     */           protected void playDispenseSound(IBlockSource source) {
/* 430 */             if (this.field_179240_b) {
/*     */               
/* 432 */               source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */             }
/*     */             else {
/*     */               
/* 436 */               source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
/*     */             } 
/*     */           }
/*     */         });
/* 440 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.pumpkin), new BehaviorDefaultDispenseItem()
/*     */         {
/*     */           private boolean field_179241_b = true;
/*     */           
/*     */           protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 445 */             World world = source.getWorld();
/* 446 */             BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/* 447 */             BlockPumpkin blockpumpkin = (BlockPumpkin)Blocks.pumpkin;
/*     */             
/* 449 */             if (world.isAirBlock(blockpos) && blockpumpkin.canDispenserPlace(world, blockpos)) {
/*     */               
/* 451 */               if (!world.isRemote)
/*     */               {
/* 453 */                 world.setBlockState(blockpos, blockpumpkin.getDefaultState(), 3);
/*     */               }
/*     */               
/* 456 */               stack.stackSize--;
/*     */             }
/*     */             else {
/*     */               
/* 460 */               this.field_179241_b = false;
/*     */             } 
/*     */             
/* 463 */             return stack;
/*     */           }
/*     */           
/*     */           protected void playDispenseSound(IBlockSource source) {
/* 467 */             if (this.field_179241_b) {
/*     */               
/* 469 */               source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */             }
/*     */             else {
/*     */               
/* 473 */               source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void register() {
/* 481 */     if (!alreadyRegistered) {
/*     */       
/* 483 */       alreadyRegistered = true;
/*     */       
/* 485 */       if (LOGGER.isDebugEnabled())
/*     */       {
/* 487 */         redirectOutputToLog();
/*     */       }
/*     */       
/* 490 */       Block.registerBlocks();
/* 491 */       BlockFire.init();
/* 492 */       Item.registerItems();
/* 493 */       StatList.init();
/* 494 */       registerDispenserBehaviors();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void redirectOutputToLog() {
/* 500 */     System.setErr((PrintStream)new LoggingPrintStream("STDERR", System.err));
/* 501 */     System.setOut((PrintStream)new LoggingPrintStream("STDOUT", SYSOUT));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void printToSYSOUT(String p_179870_0_) {
/* 506 */     SYSOUT.println(p_179870_0_);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\init\Bootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */