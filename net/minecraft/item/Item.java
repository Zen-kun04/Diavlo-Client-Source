/*     */ package net.minecraft.item;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDirt;
/*     */ import net.minecraft.block.BlockDoublePlant;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.block.BlockPlanks;
/*     */ import net.minecraft.block.BlockRedSandstone;
/*     */ import net.minecraft.block.BlockSand;
/*     */ import net.minecraft.block.BlockSilverfish;
/*     */ import net.minecraft.block.BlockStone;
/*     */ import net.minecraft.block.BlockStoneBrick;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.RegistryNamespaced;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class Item {
/*  31 */   public static final RegistryNamespaced<ResourceLocation, Item> itemRegistry = new RegistryNamespaced();
/*  32 */   private static final Map<Block, Item> BLOCK_TO_ITEM = Maps.newHashMap();
/*  33 */   protected static final UUID itemModifierUUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
/*     */   private CreativeTabs tabToDisplayOn;
/*  35 */   protected static Random itemRand = new Random();
/*  36 */   protected int maxStackSize = 64;
/*     */   
/*     */   private int maxDamage;
/*     */   protected boolean bFull3D;
/*     */   protected boolean hasSubtypes;
/*     */   private Item containerItem;
/*     */   private String potionEffect;
/*     */   private String unlocalizedName;
/*     */   
/*     */   public static int getIdFromItem(Item itemIn) {
/*  46 */     return (itemIn == null) ? 0 : itemRegistry.getIDForObject(itemIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Item getItemById(int id) {
/*  51 */     return (Item)itemRegistry.getObjectById(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Item getItemFromBlock(Block blockIn) {
/*  56 */     return BLOCK_TO_ITEM.get(blockIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Item getByNameOrId(String id) {
/*  61 */     Item item = (Item)itemRegistry.getObject(new ResourceLocation(id));
/*     */     
/*  63 */     if (item == null) {
/*     */       
/*     */       try {
/*     */         
/*  67 */         return getItemById(Integer.parseInt(id));
/*     */       }
/*  69 */       catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean updateItemStackNBT(NBTTagCompound nbt) {
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item setMaxStackSize(int maxStackSize) {
/*  85 */     this.maxStackSize = maxStackSize;
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getStrVsBlock(ItemStack stack, Block state) {
/*  96 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 101 */     return itemStackIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
/* 106 */     return stack;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getItemStackLimit() {
/* 111 */     return this.maxStackSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetadata(int damage) {
/* 116 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getHasSubtypes() {
/* 121 */     return this.hasSubtypes;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item setHasSubtypes(boolean hasSubtypes) {
/* 126 */     this.hasSubtypes = hasSubtypes;
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxDamage() {
/* 132 */     return this.maxDamage;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item setMaxDamage(int maxDamageIn) {
/* 137 */     this.maxDamage = maxDamageIn;
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDamageable() {
/* 143 */     return (this.maxDamage > 0 && !this.hasSubtypes);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
/* 153 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHarvestBlock(Block blockIn) {
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item setFull3D() {
/* 168 */     this.bFull3D = true;
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFull3D() {
/* 174 */     return this.bFull3D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRotateAroundWhenRendering() {
/* 179 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item setUnlocalizedName(String unlocalizedName) {
/* 184 */     this.unlocalizedName = unlocalizedName;
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedNameInefficiently(ItemStack stack) {
/* 190 */     String s = getUnlocalizedName(stack);
/* 191 */     return (s == null) ? "" : StatCollector.translateToLocal(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName() {
/* 196 */     return "item." + this.unlocalizedName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName(ItemStack stack) {
/* 201 */     return "item." + this.unlocalizedName;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item setContainerItem(Item containerItem) {
/* 206 */     this.containerItem = containerItem;
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getShareTag() {
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getContainerItem() {
/* 217 */     return this.containerItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasContainerItem() {
/* 222 */     return (this.containerItem != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColorFromItemStack(ItemStack stack, int renderPass) {
/* 227 */     return 16777215;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {}
/*     */ 
/*     */   
/*     */   public boolean isMap() {
/* 240 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumAction getItemUseAction(ItemStack stack) {
/* 245 */     return EnumAction.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxItemUseDuration(ItemStack stack) {
/* 250 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {}
/*     */ 
/*     */   
/*     */   protected Item setPotionEffect(String potionEffect) {
/* 259 */     this.potionEffect = potionEffect;
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPotionEffect(ItemStack stack) {
/* 265 */     return this.potionEffect;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPotionIngredient(ItemStack stack) {
/* 270 */     return (getPotionEffect(stack) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {}
/*     */ 
/*     */   
/*     */   public String getItemStackDisplayName(ItemStack stack) {
/* 279 */     return ("" + StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".name")).trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEffect(ItemStack stack) {
/* 284 */     return stack.isItemEnchanted();
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumRarity getRarity(ItemStack stack) {
/* 289 */     return stack.isItemEnchanted() ? EnumRarity.RARE : EnumRarity.COMMON;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemTool(ItemStack stack) {
/* 294 */     return (getItemStackLimit() == 1 && isDamageable());
/*     */   }
/*     */ 
/*     */   
/*     */   protected MovingObjectPosition getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
/* 299 */     float f = playerIn.rotationPitch;
/* 300 */     float f1 = playerIn.rotationYaw;
/* 301 */     double d0 = playerIn.posX;
/* 302 */     double d1 = playerIn.posY + playerIn.getEyeHeight();
/* 303 */     double d2 = playerIn.posZ;
/* 304 */     Vec3 vec3 = new Vec3(d0, d1, d2);
/* 305 */     float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
/* 306 */     float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
/* 307 */     float f4 = -MathHelper.cos(-f * 0.017453292F);
/* 308 */     float f5 = MathHelper.sin(-f * 0.017453292F);
/* 309 */     float f6 = f3 * f4;
/* 310 */     float f7 = f2 * f4;
/* 311 */     double d3 = 5.0D;
/* 312 */     Vec3 vec31 = vec3.addVector(f6 * d3, f5 * d3, f7 * d3);
/* 313 */     return worldIn.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getItemEnchantability() {
/* 318 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
/* 323 */     subItems.add(new ItemStack(itemIn, 1, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public CreativeTabs getCreativeTab() {
/* 328 */     return this.tabToDisplayOn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item setCreativeTab(CreativeTabs tab) {
/* 333 */     this.tabToDisplayOn = tab;
/* 334 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canItemEditBlocks() {
/* 339 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
/* 344 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
/* 349 */     return (Multimap<String, AttributeModifier>)HashMultimap.create();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void registerItems() {
/* 354 */     registerItemBlock(Blocks.stone, (new ItemMultiTexture(Blocks.stone, Blocks.stone, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 358 */               return BlockStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 360 */           })).setUnlocalizedName("stone"));
/* 361 */     registerItemBlock((Block)Blocks.grass, new ItemColored((Block)Blocks.grass, false));
/* 362 */     registerItemBlock(Blocks.dirt, (new ItemMultiTexture(Blocks.dirt, Blocks.dirt, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 366 */               return BlockDirt.DirtType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 368 */           })).setUnlocalizedName("dirt"));
/* 369 */     registerItemBlock(Blocks.cobblestone);
/* 370 */     registerItemBlock(Blocks.planks, (new ItemMultiTexture(Blocks.planks, Blocks.planks, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 374 */               return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 376 */           })).setUnlocalizedName("wood"));
/* 377 */     registerItemBlock(Blocks.sapling, (new ItemMultiTexture(Blocks.sapling, Blocks.sapling, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 381 */               return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 383 */           })).setUnlocalizedName("sapling"));
/* 384 */     registerItemBlock(Blocks.bedrock);
/* 385 */     registerItemBlock((Block)Blocks.sand, (new ItemMultiTexture((Block)Blocks.sand, (Block)Blocks.sand, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 389 */               return BlockSand.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 391 */           })).setUnlocalizedName("sand"));
/* 392 */     registerItemBlock(Blocks.gravel);
/* 393 */     registerItemBlock(Blocks.gold_ore);
/* 394 */     registerItemBlock(Blocks.iron_ore);
/* 395 */     registerItemBlock(Blocks.coal_ore);
/* 396 */     registerItemBlock(Blocks.log, (new ItemMultiTexture(Blocks.log, Blocks.log, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 400 */               return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 402 */           })).setUnlocalizedName("log"));
/* 403 */     registerItemBlock(Blocks.log2, (new ItemMultiTexture(Blocks.log2, Blocks.log2, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 407 */               return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata() + 4).getUnlocalizedName();
/*     */             }
/* 409 */           })).setUnlocalizedName("log"));
/* 410 */     registerItemBlock((Block)Blocks.leaves, (new ItemLeaves(Blocks.leaves)).setUnlocalizedName("leaves"));
/* 411 */     registerItemBlock((Block)Blocks.leaves2, (new ItemLeaves(Blocks.leaves2)).setUnlocalizedName("leaves"));
/* 412 */     registerItemBlock(Blocks.sponge, (new ItemMultiTexture(Blocks.sponge, Blocks.sponge, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 416 */               return ((p_apply_1_.getMetadata() & 0x1) == 1) ? "wet" : "dry";
/*     */             }
/* 418 */           })).setUnlocalizedName("sponge"));
/* 419 */     registerItemBlock(Blocks.glass);
/* 420 */     registerItemBlock(Blocks.lapis_ore);
/* 421 */     registerItemBlock(Blocks.lapis_block);
/* 422 */     registerItemBlock(Blocks.dispenser);
/* 423 */     registerItemBlock(Blocks.sandstone, (new ItemMultiTexture(Blocks.sandstone, Blocks.sandstone, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 427 */               return BlockSandStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 429 */           })).setUnlocalizedName("sandStone"));
/* 430 */     registerItemBlock(Blocks.noteblock);
/* 431 */     registerItemBlock(Blocks.golden_rail);
/* 432 */     registerItemBlock(Blocks.detector_rail);
/* 433 */     registerItemBlock((Block)Blocks.sticky_piston, new ItemPiston((Block)Blocks.sticky_piston));
/* 434 */     registerItemBlock(Blocks.web);
/* 435 */     registerItemBlock((Block)Blocks.tallgrass, (new ItemColored((Block)Blocks.tallgrass, true)).setSubtypeNames(new String[] { "shrub", "grass", "fern" }));
/* 436 */     registerItemBlock((Block)Blocks.deadbush);
/* 437 */     registerItemBlock((Block)Blocks.piston, new ItemPiston((Block)Blocks.piston));
/* 438 */     registerItemBlock(Blocks.wool, (new ItemCloth(Blocks.wool)).setUnlocalizedName("cloth"));
/* 439 */     registerItemBlock((Block)Blocks.yellow_flower, (new ItemMultiTexture((Block)Blocks.yellow_flower, (Block)Blocks.yellow_flower, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 443 */               return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.YELLOW, p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 445 */           })).setUnlocalizedName("flower"));
/* 446 */     registerItemBlock((Block)Blocks.red_flower, (new ItemMultiTexture((Block)Blocks.red_flower, (Block)Blocks.red_flower, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 450 */               return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 452 */           })).setUnlocalizedName("rose"));
/* 453 */     registerItemBlock((Block)Blocks.brown_mushroom);
/* 454 */     registerItemBlock((Block)Blocks.red_mushroom);
/* 455 */     registerItemBlock(Blocks.gold_block);
/* 456 */     registerItemBlock(Blocks.iron_block);
/* 457 */     registerItemBlock((Block)Blocks.stone_slab, (new ItemSlab((Block)Blocks.stone_slab, Blocks.stone_slab, Blocks.double_stone_slab)).setUnlocalizedName("stoneSlab"));
/* 458 */     registerItemBlock(Blocks.brick_block);
/* 459 */     registerItemBlock(Blocks.tnt);
/* 460 */     registerItemBlock(Blocks.bookshelf);
/* 461 */     registerItemBlock(Blocks.mossy_cobblestone);
/* 462 */     registerItemBlock(Blocks.obsidian);
/* 463 */     registerItemBlock(Blocks.torch);
/* 464 */     registerItemBlock(Blocks.mob_spawner);
/* 465 */     registerItemBlock(Blocks.oak_stairs);
/* 466 */     registerItemBlock((Block)Blocks.chest);
/* 467 */     registerItemBlock(Blocks.diamond_ore);
/* 468 */     registerItemBlock(Blocks.diamond_block);
/* 469 */     registerItemBlock(Blocks.crafting_table);
/* 470 */     registerItemBlock(Blocks.farmland);
/* 471 */     registerItemBlock(Blocks.furnace);
/* 472 */     registerItemBlock(Blocks.lit_furnace);
/* 473 */     registerItemBlock(Blocks.ladder);
/* 474 */     registerItemBlock(Blocks.rail);
/* 475 */     registerItemBlock(Blocks.stone_stairs);
/* 476 */     registerItemBlock(Blocks.lever);
/* 477 */     registerItemBlock(Blocks.stone_pressure_plate);
/* 478 */     registerItemBlock(Blocks.wooden_pressure_plate);
/* 479 */     registerItemBlock(Blocks.redstone_ore);
/* 480 */     registerItemBlock(Blocks.redstone_torch);
/* 481 */     registerItemBlock(Blocks.stone_button);
/* 482 */     registerItemBlock(Blocks.snow_layer, new ItemSnow(Blocks.snow_layer));
/* 483 */     registerItemBlock(Blocks.ice);
/* 484 */     registerItemBlock(Blocks.snow);
/* 485 */     registerItemBlock((Block)Blocks.cactus);
/* 486 */     registerItemBlock(Blocks.clay);
/* 487 */     registerItemBlock(Blocks.jukebox);
/* 488 */     registerItemBlock(Blocks.oak_fence);
/* 489 */     registerItemBlock(Blocks.spruce_fence);
/* 490 */     registerItemBlock(Blocks.birch_fence);
/* 491 */     registerItemBlock(Blocks.jungle_fence);
/* 492 */     registerItemBlock(Blocks.dark_oak_fence);
/* 493 */     registerItemBlock(Blocks.acacia_fence);
/* 494 */     registerItemBlock(Blocks.pumpkin);
/* 495 */     registerItemBlock(Blocks.netherrack);
/* 496 */     registerItemBlock(Blocks.soul_sand);
/* 497 */     registerItemBlock(Blocks.glowstone);
/* 498 */     registerItemBlock(Blocks.lit_pumpkin);
/* 499 */     registerItemBlock(Blocks.trapdoor);
/* 500 */     registerItemBlock(Blocks.monster_egg, (new ItemMultiTexture(Blocks.monster_egg, Blocks.monster_egg, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 504 */               return BlockSilverfish.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 506 */           })).setUnlocalizedName("monsterStoneEgg"));
/* 507 */     registerItemBlock(Blocks.stonebrick, (new ItemMultiTexture(Blocks.stonebrick, Blocks.stonebrick, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 511 */               return BlockStoneBrick.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 513 */           })).setUnlocalizedName("stonebricksmooth"));
/* 514 */     registerItemBlock(Blocks.brown_mushroom_block);
/* 515 */     registerItemBlock(Blocks.red_mushroom_block);
/* 516 */     registerItemBlock(Blocks.iron_bars);
/* 517 */     registerItemBlock(Blocks.glass_pane);
/* 518 */     registerItemBlock(Blocks.melon_block);
/* 519 */     registerItemBlock(Blocks.vine, new ItemColored(Blocks.vine, false));
/* 520 */     registerItemBlock(Blocks.oak_fence_gate);
/* 521 */     registerItemBlock(Blocks.spruce_fence_gate);
/* 522 */     registerItemBlock(Blocks.birch_fence_gate);
/* 523 */     registerItemBlock(Blocks.jungle_fence_gate);
/* 524 */     registerItemBlock(Blocks.dark_oak_fence_gate);
/* 525 */     registerItemBlock(Blocks.acacia_fence_gate);
/* 526 */     registerItemBlock(Blocks.brick_stairs);
/* 527 */     registerItemBlock(Blocks.stone_brick_stairs);
/* 528 */     registerItemBlock((Block)Blocks.mycelium);
/* 529 */     registerItemBlock(Blocks.waterlily, new ItemLilyPad(Blocks.waterlily));
/* 530 */     registerItemBlock(Blocks.nether_brick);
/* 531 */     registerItemBlock(Blocks.nether_brick_fence);
/* 532 */     registerItemBlock(Blocks.nether_brick_stairs);
/* 533 */     registerItemBlock(Blocks.enchanting_table);
/* 534 */     registerItemBlock(Blocks.end_portal_frame);
/* 535 */     registerItemBlock(Blocks.end_stone);
/* 536 */     registerItemBlock(Blocks.dragon_egg);
/* 537 */     registerItemBlock(Blocks.redstone_lamp);
/* 538 */     registerItemBlock((Block)Blocks.wooden_slab, (new ItemSlab((Block)Blocks.wooden_slab, Blocks.wooden_slab, Blocks.double_wooden_slab)).setUnlocalizedName("woodSlab"));
/* 539 */     registerItemBlock(Blocks.sandstone_stairs);
/* 540 */     registerItemBlock(Blocks.emerald_ore);
/* 541 */     registerItemBlock(Blocks.ender_chest);
/* 542 */     registerItemBlock((Block)Blocks.tripwire_hook);
/* 543 */     registerItemBlock(Blocks.emerald_block);
/* 544 */     registerItemBlock(Blocks.spruce_stairs);
/* 545 */     registerItemBlock(Blocks.birch_stairs);
/* 546 */     registerItemBlock(Blocks.jungle_stairs);
/* 547 */     registerItemBlock(Blocks.command_block);
/* 548 */     registerItemBlock((Block)Blocks.beacon);
/* 549 */     registerItemBlock(Blocks.cobblestone_wall, (new ItemMultiTexture(Blocks.cobblestone_wall, Blocks.cobblestone_wall, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 553 */               return BlockWall.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 555 */           })).setUnlocalizedName("cobbleWall"));
/* 556 */     registerItemBlock(Blocks.wooden_button);
/* 557 */     registerItemBlock(Blocks.anvil, (new ItemAnvilBlock(Blocks.anvil)).setUnlocalizedName("anvil"));
/* 558 */     registerItemBlock(Blocks.trapped_chest);
/* 559 */     registerItemBlock(Blocks.light_weighted_pressure_plate);
/* 560 */     registerItemBlock(Blocks.heavy_weighted_pressure_plate);
/* 561 */     registerItemBlock((Block)Blocks.daylight_detector);
/* 562 */     registerItemBlock(Blocks.redstone_block);
/* 563 */     registerItemBlock(Blocks.quartz_ore);
/* 564 */     registerItemBlock((Block)Blocks.hopper);
/* 565 */     registerItemBlock(Blocks.quartz_block, (new ItemMultiTexture(Blocks.quartz_block, Blocks.quartz_block, new String[] { "default", "chiseled", "lines" })).setUnlocalizedName("quartzBlock"));
/* 566 */     registerItemBlock(Blocks.quartz_stairs);
/* 567 */     registerItemBlock(Blocks.activator_rail);
/* 568 */     registerItemBlock(Blocks.dropper);
/* 569 */     registerItemBlock(Blocks.stained_hardened_clay, (new ItemCloth(Blocks.stained_hardened_clay)).setUnlocalizedName("clayHardenedStained"));
/* 570 */     registerItemBlock(Blocks.barrier);
/* 571 */     registerItemBlock(Blocks.iron_trapdoor);
/* 572 */     registerItemBlock(Blocks.hay_block);
/* 573 */     registerItemBlock(Blocks.carpet, (new ItemCloth(Blocks.carpet)).setUnlocalizedName("woolCarpet"));
/* 574 */     registerItemBlock(Blocks.hardened_clay);
/* 575 */     registerItemBlock(Blocks.coal_block);
/* 576 */     registerItemBlock(Blocks.packed_ice);
/* 577 */     registerItemBlock(Blocks.acacia_stairs);
/* 578 */     registerItemBlock(Blocks.dark_oak_stairs);
/* 579 */     registerItemBlock(Blocks.slime_block);
/* 580 */     registerItemBlock((Block)Blocks.double_plant, (new ItemDoublePlant((Block)Blocks.double_plant, (Block)Blocks.double_plant, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 584 */               return BlockDoublePlant.EnumPlantType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 586 */           })).setUnlocalizedName("doublePlant"));
/* 587 */     registerItemBlock((Block)Blocks.stained_glass, (new ItemCloth((Block)Blocks.stained_glass)).setUnlocalizedName("stainedGlass"));
/* 588 */     registerItemBlock((Block)Blocks.stained_glass_pane, (new ItemCloth((Block)Blocks.stained_glass_pane)).setUnlocalizedName("stainedGlassPane"));
/* 589 */     registerItemBlock(Blocks.prismarine, (new ItemMultiTexture(Blocks.prismarine, Blocks.prismarine, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 593 */               return BlockPrismarine.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 595 */           })).setUnlocalizedName("prismarine"));
/* 596 */     registerItemBlock(Blocks.sea_lantern);
/* 597 */     registerItemBlock(Blocks.red_sandstone, (new ItemMultiTexture(Blocks.red_sandstone, Blocks.red_sandstone, new Function<ItemStack, String>()
/*     */           {
/*     */             public String apply(ItemStack p_apply_1_)
/*     */             {
/* 601 */               return BlockRedSandstone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*     */             }
/* 603 */           })).setUnlocalizedName("redSandStone"));
/* 604 */     registerItemBlock(Blocks.red_sandstone_stairs);
/* 605 */     registerItemBlock((Block)Blocks.stone_slab2, (new ItemSlab((Block)Blocks.stone_slab2, Blocks.stone_slab2, Blocks.double_stone_slab2)).setUnlocalizedName("stoneSlab2"));
/* 606 */     registerItem(256, "iron_shovel", (new ItemSpade(ToolMaterial.IRON)).setUnlocalizedName("shovelIron"));
/* 607 */     registerItem(257, "iron_pickaxe", (new ItemPickaxe(ToolMaterial.IRON)).setUnlocalizedName("pickaxeIron"));
/* 608 */     registerItem(258, "iron_axe", (new ItemAxe(ToolMaterial.IRON)).setUnlocalizedName("hatchetIron"));
/* 609 */     registerItem(259, "flint_and_steel", (new ItemFlintAndSteel()).setUnlocalizedName("flintAndSteel"));
/* 610 */     registerItem(260, "apple", (new ItemFood(4, 0.3F, false)).setUnlocalizedName("apple"));
/* 611 */     registerItem(261, "bow", (new ItemBow()).setUnlocalizedName("bow"));
/* 612 */     registerItem(262, "arrow", (new Item()).setUnlocalizedName("arrow").setCreativeTab(CreativeTabs.tabCombat));
/* 613 */     registerItem(263, "coal", (new ItemCoal()).setUnlocalizedName("coal"));
/* 614 */     registerItem(264, "diamond", (new Item()).setUnlocalizedName("diamond").setCreativeTab(CreativeTabs.tabMaterials));
/* 615 */     registerItem(265, "iron_ingot", (new Item()).setUnlocalizedName("ingotIron").setCreativeTab(CreativeTabs.tabMaterials));
/* 616 */     registerItem(266, "gold_ingot", (new Item()).setUnlocalizedName("ingotGold").setCreativeTab(CreativeTabs.tabMaterials));
/* 617 */     registerItem(267, "iron_sword", (new ItemSword(ToolMaterial.IRON)).setUnlocalizedName("swordIron"));
/* 618 */     registerItem(268, "wooden_sword", (new ItemSword(ToolMaterial.WOOD)).setUnlocalizedName("swordWood"));
/* 619 */     registerItem(269, "wooden_shovel", (new ItemSpade(ToolMaterial.WOOD)).setUnlocalizedName("shovelWood"));
/* 620 */     registerItem(270, "wooden_pickaxe", (new ItemPickaxe(ToolMaterial.WOOD)).setUnlocalizedName("pickaxeWood"));
/* 621 */     registerItem(271, "wooden_axe", (new ItemAxe(ToolMaterial.WOOD)).setUnlocalizedName("hatchetWood"));
/* 622 */     registerItem(272, "stone_sword", (new ItemSword(ToolMaterial.STONE)).setUnlocalizedName("swordStone"));
/* 623 */     registerItem(273, "stone_shovel", (new ItemSpade(ToolMaterial.STONE)).setUnlocalizedName("shovelStone"));
/* 624 */     registerItem(274, "stone_pickaxe", (new ItemPickaxe(ToolMaterial.STONE)).setUnlocalizedName("pickaxeStone"));
/* 625 */     registerItem(275, "stone_axe", (new ItemAxe(ToolMaterial.STONE)).setUnlocalizedName("hatchetStone"));
/* 626 */     registerItem(276, "diamond_sword", (new ItemSword(ToolMaterial.EMERALD)).setUnlocalizedName("swordDiamond"));
/* 627 */     registerItem(277, "diamond_shovel", (new ItemSpade(ToolMaterial.EMERALD)).setUnlocalizedName("shovelDiamond"));
/* 628 */     registerItem(278, "diamond_pickaxe", (new ItemPickaxe(ToolMaterial.EMERALD)).setUnlocalizedName("pickaxeDiamond"));
/* 629 */     registerItem(279, "diamond_axe", (new ItemAxe(ToolMaterial.EMERALD)).setUnlocalizedName("hatchetDiamond"));
/* 630 */     registerItem(280, "stick", (new Item()).setFull3D().setUnlocalizedName("stick").setCreativeTab(CreativeTabs.tabMaterials));
/* 631 */     registerItem(281, "bowl", (new Item()).setUnlocalizedName("bowl").setCreativeTab(CreativeTabs.tabMaterials));
/* 632 */     registerItem(282, "mushroom_stew", (new ItemSoup(6)).setUnlocalizedName("mushroomStew"));
/* 633 */     registerItem(283, "golden_sword", (new ItemSword(ToolMaterial.GOLD)).setUnlocalizedName("swordGold"));
/* 634 */     registerItem(284, "golden_shovel", (new ItemSpade(ToolMaterial.GOLD)).setUnlocalizedName("shovelGold"));
/* 635 */     registerItem(285, "golden_pickaxe", (new ItemPickaxe(ToolMaterial.GOLD)).setUnlocalizedName("pickaxeGold"));
/* 636 */     registerItem(286, "golden_axe", (new ItemAxe(ToolMaterial.GOLD)).setUnlocalizedName("hatchetGold"));
/* 637 */     registerItem(287, "string", (new ItemReed(Blocks.tripwire)).setUnlocalizedName("string").setCreativeTab(CreativeTabs.tabMaterials));
/* 638 */     registerItem(288, "feather", (new Item()).setUnlocalizedName("feather").setCreativeTab(CreativeTabs.tabMaterials));
/* 639 */     registerItem(289, "gunpowder", (new Item()).setUnlocalizedName("sulphur").setPotionEffect("+14&13-13").setCreativeTab(CreativeTabs.tabMaterials));
/* 640 */     registerItem(290, "wooden_hoe", (new ItemHoe(ToolMaterial.WOOD)).setUnlocalizedName("hoeWood"));
/* 641 */     registerItem(291, "stone_hoe", (new ItemHoe(ToolMaterial.STONE)).setUnlocalizedName("hoeStone"));
/* 642 */     registerItem(292, "iron_hoe", (new ItemHoe(ToolMaterial.IRON)).setUnlocalizedName("hoeIron"));
/* 643 */     registerItem(293, "diamond_hoe", (new ItemHoe(ToolMaterial.EMERALD)).setUnlocalizedName("hoeDiamond"));
/* 644 */     registerItem(294, "golden_hoe", (new ItemHoe(ToolMaterial.GOLD)).setUnlocalizedName("hoeGold"));
/* 645 */     registerItem(295, "wheat_seeds", (new ItemSeeds(Blocks.wheat, Blocks.farmland)).setUnlocalizedName("seeds"));
/* 646 */     registerItem(296, "wheat", (new Item()).setUnlocalizedName("wheat").setCreativeTab(CreativeTabs.tabMaterials));
/* 647 */     registerItem(297, "bread", (new ItemFood(5, 0.6F, false)).setUnlocalizedName("bread"));
/* 648 */     registerItem(298, "leather_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 0)).setUnlocalizedName("helmetCloth"));
/* 649 */     registerItem(299, "leather_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 1)).setUnlocalizedName("chestplateCloth"));
/* 650 */     registerItem(300, "leather_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 2)).setUnlocalizedName("leggingsCloth"));
/* 651 */     registerItem(301, "leather_boots", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 3)).setUnlocalizedName("bootsCloth"));
/* 652 */     registerItem(302, "chainmail_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 0)).setUnlocalizedName("helmetChain"));
/* 653 */     registerItem(303, "chainmail_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 1)).setUnlocalizedName("chestplateChain"));
/* 654 */     registerItem(304, "chainmail_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 2)).setUnlocalizedName("leggingsChain"));
/* 655 */     registerItem(305, "chainmail_boots", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 3)).setUnlocalizedName("bootsChain"));
/* 656 */     registerItem(306, "iron_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 0)).setUnlocalizedName("helmetIron"));
/* 657 */     registerItem(307, "iron_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 1)).setUnlocalizedName("chestplateIron"));
/* 658 */     registerItem(308, "iron_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 2)).setUnlocalizedName("leggingsIron"));
/* 659 */     registerItem(309, "iron_boots", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 3)).setUnlocalizedName("bootsIron"));
/* 660 */     registerItem(310, "diamond_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 0)).setUnlocalizedName("helmetDiamond"));
/* 661 */     registerItem(311, "diamond_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 1)).setUnlocalizedName("chestplateDiamond"));
/* 662 */     registerItem(312, "diamond_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 2)).setUnlocalizedName("leggingsDiamond"));
/* 663 */     registerItem(313, "diamond_boots", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 3)).setUnlocalizedName("bootsDiamond"));
/* 664 */     registerItem(314, "golden_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 0)).setUnlocalizedName("helmetGold"));
/* 665 */     registerItem(315, "golden_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 1)).setUnlocalizedName("chestplateGold"));
/* 666 */     registerItem(316, "golden_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 2)).setUnlocalizedName("leggingsGold"));
/* 667 */     registerItem(317, "golden_boots", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 3)).setUnlocalizedName("bootsGold"));
/* 668 */     registerItem(318, "flint", (new Item()).setUnlocalizedName("flint").setCreativeTab(CreativeTabs.tabMaterials));
/* 669 */     registerItem(319, "porkchop", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("porkchopRaw"));
/* 670 */     registerItem(320, "cooked_porkchop", (new ItemFood(8, 0.8F, true)).setUnlocalizedName("porkchopCooked"));
/* 671 */     registerItem(321, "painting", (new ItemHangingEntity((Class)EntityPainting.class)).setUnlocalizedName("painting"));
/* 672 */     registerItem(322, "golden_apple", (new ItemAppleGold(4, 1.2F, false)).setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 5, 1, 1.0F).setUnlocalizedName("appleGold"));
/* 673 */     registerItem(323, "sign", (new ItemSign()).setUnlocalizedName("sign"));
/* 674 */     registerItem(324, "wooden_door", (new ItemDoor(Blocks.oak_door)).setUnlocalizedName("doorOak"));
/* 675 */     Item item = (new ItemBucket(Blocks.air)).setUnlocalizedName("bucket").setMaxStackSize(16);
/* 676 */     registerItem(325, "bucket", item);
/* 677 */     registerItem(326, "water_bucket", (new ItemBucket((Block)Blocks.flowing_water)).setUnlocalizedName("bucketWater").setContainerItem(item));
/* 678 */     registerItem(327, "lava_bucket", (new ItemBucket((Block)Blocks.flowing_lava)).setUnlocalizedName("bucketLava").setContainerItem(item));
/* 679 */     registerItem(328, "minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.RIDEABLE)).setUnlocalizedName("minecart"));
/* 680 */     registerItem(329, "saddle", (new ItemSaddle()).setUnlocalizedName("saddle"));
/* 681 */     registerItem(330, "iron_door", (new ItemDoor(Blocks.iron_door)).setUnlocalizedName("doorIron"));
/* 682 */     registerItem(331, "redstone", (new ItemRedstone()).setUnlocalizedName("redstone").setPotionEffect("-5+6-7"));
/* 683 */     registerItem(332, "snowball", (new ItemSnowball()).setUnlocalizedName("snowball"));
/* 684 */     registerItem(333, "boat", (new ItemBoat()).setUnlocalizedName("boat"));
/* 685 */     registerItem(334, "leather", (new Item()).setUnlocalizedName("leather").setCreativeTab(CreativeTabs.tabMaterials));
/* 686 */     registerItem(335, "milk_bucket", (new ItemBucketMilk()).setUnlocalizedName("milk").setContainerItem(item));
/* 687 */     registerItem(336, "brick", (new Item()).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabMaterials));
/* 688 */     registerItem(337, "clay_ball", (new Item()).setUnlocalizedName("clay").setCreativeTab(CreativeTabs.tabMaterials));
/* 689 */     registerItem(338, "reeds", (new ItemReed((Block)Blocks.reeds)).setUnlocalizedName("reeds").setCreativeTab(CreativeTabs.tabMaterials));
/* 690 */     registerItem(339, "paper", (new Item()).setUnlocalizedName("paper").setCreativeTab(CreativeTabs.tabMisc));
/* 691 */     registerItem(340, "book", (new ItemBook()).setUnlocalizedName("book").setCreativeTab(CreativeTabs.tabMisc));
/* 692 */     registerItem(341, "slime_ball", (new Item()).setUnlocalizedName("slimeball").setCreativeTab(CreativeTabs.tabMisc));
/* 693 */     registerItem(342, "chest_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.CHEST)).setUnlocalizedName("minecartChest"));
/* 694 */     registerItem(343, "furnace_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.FURNACE)).setUnlocalizedName("minecartFurnace"));
/* 695 */     registerItem(344, "egg", (new ItemEgg()).setUnlocalizedName("egg"));
/* 696 */     registerItem(345, "compass", (new Item()).setUnlocalizedName("compass").setCreativeTab(CreativeTabs.tabTools));
/* 697 */     registerItem(346, "fishing_rod", (new ItemFishingRod()).setUnlocalizedName("fishingRod"));
/* 698 */     registerItem(347, "clock", (new Item()).setUnlocalizedName("clock").setCreativeTab(CreativeTabs.tabTools));
/* 699 */     registerItem(348, "glowstone_dust", (new Item()).setUnlocalizedName("yellowDust").setPotionEffect("+5-6-7").setCreativeTab(CreativeTabs.tabMaterials));
/* 700 */     registerItem(349, "fish", (new ItemFishFood(false)).setUnlocalizedName("fish").setHasSubtypes(true));
/* 701 */     registerItem(350, "cooked_fish", (new ItemFishFood(true)).setUnlocalizedName("fish").setHasSubtypes(true));
/* 702 */     registerItem(351, "dye", (new ItemDye()).setUnlocalizedName("dyePowder"));
/* 703 */     registerItem(352, "bone", (new Item()).setUnlocalizedName("bone").setFull3D().setCreativeTab(CreativeTabs.tabMisc));
/* 704 */     registerItem(353, "sugar", (new Item()).setUnlocalizedName("sugar").setPotionEffect("-0+1-2-3&4-4+13").setCreativeTab(CreativeTabs.tabMaterials));
/* 705 */     registerItem(354, "cake", (new ItemReed(Blocks.cake)).setMaxStackSize(1).setUnlocalizedName("cake").setCreativeTab(CreativeTabs.tabFood));
/* 706 */     registerItem(355, "bed", (new ItemBed()).setMaxStackSize(1).setUnlocalizedName("bed"));
/* 707 */     registerItem(356, "repeater", (new ItemReed((Block)Blocks.unpowered_repeater)).setUnlocalizedName("diode").setCreativeTab(CreativeTabs.tabRedstone));
/* 708 */     registerItem(357, "cookie", (new ItemFood(2, 0.1F, false)).setUnlocalizedName("cookie"));
/* 709 */     registerItem(358, "filled_map", (new ItemMap()).setUnlocalizedName("map"));
/* 710 */     registerItem(359, "shears", (new ItemShears()).setUnlocalizedName("shears"));
/* 711 */     registerItem(360, "melon", (new ItemFood(2, 0.3F, false)).setUnlocalizedName("melon"));
/* 712 */     registerItem(361, "pumpkin_seeds", (new ItemSeeds(Blocks.pumpkin_stem, Blocks.farmland)).setUnlocalizedName("seeds_pumpkin"));
/* 713 */     registerItem(362, "melon_seeds", (new ItemSeeds(Blocks.melon_stem, Blocks.farmland)).setUnlocalizedName("seeds_melon"));
/* 714 */     registerItem(363, "beef", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("beefRaw"));
/* 715 */     registerItem(364, "cooked_beef", (new ItemFood(8, 0.8F, true)).setUnlocalizedName("beefCooked"));
/* 716 */     registerItem(365, "chicken", (new ItemFood(2, 0.3F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.3F).setUnlocalizedName("chickenRaw"));
/* 717 */     registerItem(366, "cooked_chicken", (new ItemFood(6, 0.6F, true)).setUnlocalizedName("chickenCooked"));
/* 718 */     registerItem(367, "rotten_flesh", (new ItemFood(4, 0.1F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.8F).setUnlocalizedName("rottenFlesh"));
/* 719 */     registerItem(368, "ender_pearl", (new ItemEnderPearl()).setUnlocalizedName("enderPearl"));
/* 720 */     registerItem(369, "blaze_rod", (new Item()).setUnlocalizedName("blazeRod").setCreativeTab(CreativeTabs.tabMaterials).setFull3D());
/* 721 */     registerItem(370, "ghast_tear", (new Item()).setUnlocalizedName("ghastTear").setPotionEffect("+0-1-2-3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/* 722 */     registerItem(371, "gold_nugget", (new Item()).setUnlocalizedName("goldNugget").setCreativeTab(CreativeTabs.tabMaterials));
/* 723 */     registerItem(372, "nether_wart", (new ItemSeeds(Blocks.nether_wart, Blocks.soul_sand)).setUnlocalizedName("netherStalkSeeds").setPotionEffect("+4"));
/* 724 */     registerItem(373, "potion", (new ItemPotion()).setUnlocalizedName("potion"));
/* 725 */     registerItem(374, "glass_bottle", (new ItemGlassBottle()).setUnlocalizedName("glassBottle"));
/* 726 */     registerItem(375, "spider_eye", (new ItemFood(2, 0.8F, false)).setPotionEffect(Potion.poison.id, 5, 0, 1.0F).setUnlocalizedName("spiderEye").setPotionEffect("-0-1+2-3&4-4+13"));
/* 727 */     registerItem(376, "fermented_spider_eye", (new Item()).setUnlocalizedName("fermentedSpiderEye").setPotionEffect("-0+3-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/* 728 */     registerItem(377, "blaze_powder", (new Item()).setUnlocalizedName("blazePowder").setPotionEffect("+0-1-2+3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/* 729 */     registerItem(378, "magma_cream", (new Item()).setUnlocalizedName("magmaCream").setPotionEffect("+0+1-2-3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/* 730 */     registerItem(379, "brewing_stand", (new ItemReed(Blocks.brewing_stand)).setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.tabBrewing));
/* 731 */     registerItem(380, "cauldron", (new ItemReed((Block)Blocks.cauldron)).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.tabBrewing));
/* 732 */     registerItem(381, "ender_eye", (new ItemEnderEye()).setUnlocalizedName("eyeOfEnder"));
/* 733 */     registerItem(382, "speckled_melon", (new Item()).setUnlocalizedName("speckledMelon").setPotionEffect("+0-1+2-3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/* 734 */     registerItem(383, "spawn_egg", (new ItemMonsterPlacer()).setUnlocalizedName("monsterPlacer"));
/* 735 */     registerItem(384, "experience_bottle", (new ItemExpBottle()).setUnlocalizedName("expBottle"));
/* 736 */     registerItem(385, "fire_charge", (new ItemFireball()).setUnlocalizedName("fireball"));
/* 737 */     registerItem(386, "writable_book", (new ItemWritableBook()).setUnlocalizedName("writingBook").setCreativeTab(CreativeTabs.tabMisc));
/* 738 */     registerItem(387, "written_book", (new ItemEditableBook()).setUnlocalizedName("writtenBook").setMaxStackSize(16));
/* 739 */     registerItem(388, "emerald", (new Item()).setUnlocalizedName("emerald").setCreativeTab(CreativeTabs.tabMaterials));
/* 740 */     registerItem(389, "item_frame", (new ItemHangingEntity((Class)EntityItemFrame.class)).setUnlocalizedName("frame"));
/* 741 */     registerItem(390, "flower_pot", (new ItemReed(Blocks.flower_pot)).setUnlocalizedName("flowerPot").setCreativeTab(CreativeTabs.tabDecorations));
/* 742 */     registerItem(391, "carrot", (new ItemSeedFood(3, 0.6F, Blocks.carrots, Blocks.farmland)).setUnlocalizedName("carrots"));
/* 743 */     registerItem(392, "potato", (new ItemSeedFood(1, 0.3F, Blocks.potatoes, Blocks.farmland)).setUnlocalizedName("potato"));
/* 744 */     registerItem(393, "baked_potato", (new ItemFood(5, 0.6F, false)).setUnlocalizedName("potatoBaked"));
/* 745 */     registerItem(394, "poisonous_potato", (new ItemFood(2, 0.3F, false)).setPotionEffect(Potion.poison.id, 5, 0, 0.6F).setUnlocalizedName("potatoPoisonous"));
/* 746 */     registerItem(395, "map", (new ItemEmptyMap()).setUnlocalizedName("emptyMap"));
/* 747 */     registerItem(396, "golden_carrot", (new ItemFood(6, 1.2F, false)).setUnlocalizedName("carrotGolden").setPotionEffect("-0+1+2-3+13&4-4").setCreativeTab(CreativeTabs.tabBrewing));
/* 748 */     registerItem(397, "skull", (new ItemSkull()).setUnlocalizedName("skull"));
/* 749 */     registerItem(398, "carrot_on_a_stick", (new ItemCarrotOnAStick()).setUnlocalizedName("carrotOnAStick"));
/* 750 */     registerItem(399, "nether_star", (new ItemSimpleFoiled()).setUnlocalizedName("netherStar").setCreativeTab(CreativeTabs.tabMaterials));
/* 751 */     registerItem(400, "pumpkin_pie", (new ItemFood(8, 0.3F, false)).setUnlocalizedName("pumpkinPie").setCreativeTab(CreativeTabs.tabFood));
/* 752 */     registerItem(401, "fireworks", (new ItemFirework()).setUnlocalizedName("fireworks"));
/* 753 */     registerItem(402, "firework_charge", (new ItemFireworkCharge()).setUnlocalizedName("fireworksCharge").setCreativeTab(CreativeTabs.tabMisc));
/* 754 */     registerItem(403, "enchanted_book", (new ItemEnchantedBook()).setMaxStackSize(1).setUnlocalizedName("enchantedBook"));
/* 755 */     registerItem(404, "comparator", (new ItemReed((Block)Blocks.unpowered_comparator)).setUnlocalizedName("comparator").setCreativeTab(CreativeTabs.tabRedstone));
/* 756 */     registerItem(405, "netherbrick", (new Item()).setUnlocalizedName("netherbrick").setCreativeTab(CreativeTabs.tabMaterials));
/* 757 */     registerItem(406, "quartz", (new Item()).setUnlocalizedName("netherquartz").setCreativeTab(CreativeTabs.tabMaterials));
/* 758 */     registerItem(407, "tnt_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.TNT)).setUnlocalizedName("minecartTnt"));
/* 759 */     registerItem(408, "hopper_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.HOPPER)).setUnlocalizedName("minecartHopper"));
/* 760 */     registerItem(409, "prismarine_shard", (new Item()).setUnlocalizedName("prismarineShard").setCreativeTab(CreativeTabs.tabMaterials));
/* 761 */     registerItem(410, "prismarine_crystals", (new Item()).setUnlocalizedName("prismarineCrystals").setCreativeTab(CreativeTabs.tabMaterials));
/* 762 */     registerItem(411, "rabbit", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("rabbitRaw"));
/* 763 */     registerItem(412, "cooked_rabbit", (new ItemFood(5, 0.6F, true)).setUnlocalizedName("rabbitCooked"));
/* 764 */     registerItem(413, "rabbit_stew", (new ItemSoup(10)).setUnlocalizedName("rabbitStew"));
/* 765 */     registerItem(414, "rabbit_foot", (new Item()).setUnlocalizedName("rabbitFoot").setPotionEffect("+0+1-2+3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/* 766 */     registerItem(415, "rabbit_hide", (new Item()).setUnlocalizedName("rabbitHide").setCreativeTab(CreativeTabs.tabMaterials));
/* 767 */     registerItem(416, "armor_stand", (new ItemArmorStand()).setUnlocalizedName("armorStand").setMaxStackSize(16));
/* 768 */     registerItem(417, "iron_horse_armor", (new Item()).setUnlocalizedName("horsearmormetal").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
/* 769 */     registerItem(418, "golden_horse_armor", (new Item()).setUnlocalizedName("horsearmorgold").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
/* 770 */     registerItem(419, "diamond_horse_armor", (new Item()).setUnlocalizedName("horsearmordiamond").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
/* 771 */     registerItem(420, "lead", (new ItemLead()).setUnlocalizedName("leash"));
/* 772 */     registerItem(421, "name_tag", (new ItemNameTag()).setUnlocalizedName("nameTag"));
/* 773 */     registerItem(422, "command_block_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.COMMAND_BLOCK)).setUnlocalizedName("minecartCommandBlock").setCreativeTab((CreativeTabs)null));
/* 774 */     registerItem(423, "mutton", (new ItemFood(2, 0.3F, true)).setUnlocalizedName("muttonRaw"));
/* 775 */     registerItem(424, "cooked_mutton", (new ItemFood(6, 0.8F, true)).setUnlocalizedName("muttonCooked"));
/* 776 */     registerItem(425, "banner", (new ItemBanner()).setUnlocalizedName("banner"));
/* 777 */     registerItem(427, "spruce_door", (new ItemDoor(Blocks.spruce_door)).setUnlocalizedName("doorSpruce"));
/* 778 */     registerItem(428, "birch_door", (new ItemDoor(Blocks.birch_door)).setUnlocalizedName("doorBirch"));
/* 779 */     registerItem(429, "jungle_door", (new ItemDoor(Blocks.jungle_door)).setUnlocalizedName("doorJungle"));
/* 780 */     registerItem(430, "acacia_door", (new ItemDoor(Blocks.acacia_door)).setUnlocalizedName("doorAcacia"));
/* 781 */     registerItem(431, "dark_oak_door", (new ItemDoor(Blocks.dark_oak_door)).setUnlocalizedName("doorDarkOak"));
/* 782 */     registerItem(2256, "record_13", (new ItemRecord("13")).setUnlocalizedName("record"));
/* 783 */     registerItem(2257, "record_cat", (new ItemRecord("cat")).setUnlocalizedName("record"));
/* 784 */     registerItem(2258, "record_blocks", (new ItemRecord("blocks")).setUnlocalizedName("record"));
/* 785 */     registerItem(2259, "record_chirp", (new ItemRecord("chirp")).setUnlocalizedName("record"));
/* 786 */     registerItem(2260, "record_far", (new ItemRecord("far")).setUnlocalizedName("record"));
/* 787 */     registerItem(2261, "record_mall", (new ItemRecord("mall")).setUnlocalizedName("record"));
/* 788 */     registerItem(2262, "record_mellohi", (new ItemRecord("mellohi")).setUnlocalizedName("record"));
/* 789 */     registerItem(2263, "record_stal", (new ItemRecord("stal")).setUnlocalizedName("record"));
/* 790 */     registerItem(2264, "record_strad", (new ItemRecord("strad")).setUnlocalizedName("record"));
/* 791 */     registerItem(2265, "record_ward", (new ItemRecord("ward")).setUnlocalizedName("record"));
/* 792 */     registerItem(2266, "record_11", (new ItemRecord("11")).setUnlocalizedName("record"));
/* 793 */     registerItem(2267, "record_wait", (new ItemRecord("wait")).setUnlocalizedName("record"));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void registerItemBlock(Block blockIn) {
/* 798 */     registerItemBlock(blockIn, new ItemBlock(blockIn));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void registerItemBlock(Block blockIn, Item itemIn) {
/* 803 */     registerItem(Block.getIdFromBlock(blockIn), (ResourceLocation)Block.blockRegistry.getNameForObject(blockIn), itemIn);
/* 804 */     BLOCK_TO_ITEM.put(blockIn, itemIn);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void registerItem(int id, String textualID, Item itemIn) {
/* 809 */     registerItem(id, new ResourceLocation(textualID), itemIn);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void registerItem(int id, ResourceLocation textualID, Item itemIn) {
/* 814 */     itemRegistry.register(id, textualID, itemIn);
/*     */   }
/*     */   
/*     */   public enum ToolMaterial
/*     */   {
/* 819 */     WOOD(0, 59, 2.0F, 0.0F, 15),
/* 820 */     STONE(1, 131, 4.0F, 1.0F, 5),
/* 821 */     IRON(2, 250, 6.0F, 2.0F, 14),
/* 822 */     EMERALD(3, 1561, 8.0F, 3.0F, 10),
/* 823 */     GOLD(0, 32, 12.0F, 0.0F, 22);
/*     */     
/*     */     private final int harvestLevel;
/*     */     
/*     */     private final int maxUses;
/*     */     private final float efficiencyOnProperMaterial;
/*     */     private final float damageVsEntity;
/*     */     private final int enchantability;
/*     */     
/*     */     ToolMaterial(int harvestLevel, int maxUses, float efficiency, float damageVsEntity, int enchantability) {
/* 833 */       this.harvestLevel = harvestLevel;
/* 834 */       this.maxUses = maxUses;
/* 835 */       this.efficiencyOnProperMaterial = efficiency;
/* 836 */       this.damageVsEntity = damageVsEntity;
/* 837 */       this.enchantability = enchantability;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxUses() {
/* 842 */       return this.maxUses;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getEfficiencyOnProperMaterial() {
/* 847 */       return this.efficiencyOnProperMaterial;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getDamageVsEntity() {
/* 852 */       return this.damageVsEntity;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getHarvestLevel() {
/* 857 */       return this.harvestLevel;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getEnchantability() {
/* 862 */       return this.enchantability;
/*     */     }
/*     */ 
/*     */     
/*     */     public Item getRepairItem() {
/* 867 */       return (this == WOOD) ? Item.getItemFromBlock(Blocks.planks) : ((this == STONE) ? Item.getItemFromBlock(Blocks.cobblestone) : ((this == GOLD) ? Items.gold_ingot : ((this == IRON) ? Items.iron_ingot : ((this == EMERALD) ? Items.diamond : null))));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\Item.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */