/*     */ package net.minecraft.init;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockBeacon;
/*     */ import net.minecraft.block.BlockBush;
/*     */ import net.minecraft.block.BlockCactus;
/*     */ import net.minecraft.block.BlockCauldron;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.block.BlockDaylightDetector;
/*     */ import net.minecraft.block.BlockDeadBush;
/*     */ import net.minecraft.block.BlockDoublePlant;
/*     */ import net.minecraft.block.BlockDynamicLiquid;
/*     */ import net.minecraft.block.BlockFire;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.block.BlockGrass;
/*     */ import net.minecraft.block.BlockHopper;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockMycelium;
/*     */ import net.minecraft.block.BlockPistonBase;
/*     */ import net.minecraft.block.BlockPistonExtension;
/*     */ import net.minecraft.block.BlockPistonMoving;
/*     */ import net.minecraft.block.BlockPortal;
/*     */ import net.minecraft.block.BlockRedstoneComparator;
/*     */ import net.minecraft.block.BlockRedstoneRepeater;
/*     */ import net.minecraft.block.BlockRedstoneWire;
/*     */ import net.minecraft.block.BlockReed;
/*     */ import net.minecraft.block.BlockSand;
/*     */ import net.minecraft.block.BlockSkull;
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.block.BlockStainedGlass;
/*     */ import net.minecraft.block.BlockStainedGlassPane;
/*     */ import net.minecraft.block.BlockStaticLiquid;
/*     */ import net.minecraft.block.BlockTallGrass;
/*     */ import net.minecraft.block.BlockTripWireHook;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Blocks
/*     */ {
/*     */   private static Block getRegisteredBlock(String blockName) {
/* 209 */     return (Block)Block.blockRegistry.getObject(new ResourceLocation(blockName));
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 214 */     if (!Bootstrap.isRegistered())
/*     */     {
/* 216 */       throw new RuntimeException("Accessed Blocks before Bootstrap!");
/*     */     }
/*     */   }
/*     */   
/* 220 */   public static final Block air = getRegisteredBlock("air");
/* 221 */   public static final Block stone = getRegisteredBlock("stone");
/* 222 */   public static final BlockGrass grass = (BlockGrass)getRegisteredBlock("grass");
/* 223 */   public static final Block dirt = getRegisteredBlock("dirt");
/* 224 */   public static final Block cobblestone = getRegisteredBlock("cobblestone");
/* 225 */   public static final Block planks = getRegisteredBlock("planks");
/* 226 */   public static final Block sapling = getRegisteredBlock("sapling");
/* 227 */   public static final Block bedrock = getRegisteredBlock("bedrock");
/* 228 */   public static final BlockDynamicLiquid flowing_water = (BlockDynamicLiquid)getRegisteredBlock("flowing_water");
/* 229 */   public static final BlockStaticLiquid water = (BlockStaticLiquid)getRegisteredBlock("water");
/* 230 */   public static final BlockDynamicLiquid flowing_lava = (BlockDynamicLiquid)getRegisteredBlock("flowing_lava");
/* 231 */   public static final BlockStaticLiquid lava = (BlockStaticLiquid)getRegisteredBlock("lava");
/* 232 */   public static final BlockSand sand = (BlockSand)getRegisteredBlock("sand");
/* 233 */   public static final Block gravel = getRegisteredBlock("gravel");
/* 234 */   public static final Block gold_ore = getRegisteredBlock("gold_ore");
/* 235 */   public static final Block iron_ore = getRegisteredBlock("iron_ore");
/* 236 */   public static final Block coal_ore = getRegisteredBlock("coal_ore");
/* 237 */   public static final Block log = getRegisteredBlock("log");
/* 238 */   public static final Block log2 = getRegisteredBlock("log2");
/* 239 */   public static final BlockLeaves leaves = (BlockLeaves)getRegisteredBlock("leaves");
/* 240 */   public static final BlockLeaves leaves2 = (BlockLeaves)getRegisteredBlock("leaves2");
/* 241 */   public static final Block sponge = getRegisteredBlock("sponge");
/* 242 */   public static final Block glass = getRegisteredBlock("glass");
/* 243 */   public static final Block lapis_ore = getRegisteredBlock("lapis_ore");
/* 244 */   public static final Block lapis_block = getRegisteredBlock("lapis_block");
/* 245 */   public static final Block dispenser = getRegisteredBlock("dispenser");
/* 246 */   public static final Block sandstone = getRegisteredBlock("sandstone");
/* 247 */   public static final Block noteblock = getRegisteredBlock("noteblock");
/* 248 */   public static final Block bed = getRegisteredBlock("bed");
/* 249 */   public static final Block golden_rail = getRegisteredBlock("golden_rail");
/* 250 */   public static final Block detector_rail = getRegisteredBlock("detector_rail");
/* 251 */   public static final BlockPistonBase sticky_piston = (BlockPistonBase)getRegisteredBlock("sticky_piston");
/* 252 */   public static final Block web = getRegisteredBlock("web");
/* 253 */   public static final BlockTallGrass tallgrass = (BlockTallGrass)getRegisteredBlock("tallgrass");
/* 254 */   public static final BlockDeadBush deadbush = (BlockDeadBush)getRegisteredBlock("deadbush");
/* 255 */   public static final BlockPistonBase piston = (BlockPistonBase)getRegisteredBlock("piston");
/* 256 */   public static final BlockPistonExtension piston_head = (BlockPistonExtension)getRegisteredBlock("piston_head");
/* 257 */   public static final Block wool = getRegisteredBlock("wool");
/* 258 */   public static final BlockPistonMoving piston_extension = (BlockPistonMoving)getRegisteredBlock("piston_extension");
/* 259 */   public static final BlockFlower yellow_flower = (BlockFlower)getRegisteredBlock("yellow_flower");
/* 260 */   public static final BlockFlower red_flower = (BlockFlower)getRegisteredBlock("red_flower");
/* 261 */   public static final BlockBush brown_mushroom = (BlockBush)getRegisteredBlock("brown_mushroom");
/* 262 */   public static final BlockBush red_mushroom = (BlockBush)getRegisteredBlock("red_mushroom");
/* 263 */   public static final Block gold_block = getRegisteredBlock("gold_block");
/* 264 */   public static final Block iron_block = getRegisteredBlock("iron_block");
/* 265 */   public static final BlockSlab double_stone_slab = (BlockSlab)getRegisteredBlock("double_stone_slab");
/* 266 */   public static final BlockSlab stone_slab = (BlockSlab)getRegisteredBlock("stone_slab");
/* 267 */   public static final Block brick_block = getRegisteredBlock("brick_block");
/* 268 */   public static final Block tnt = getRegisteredBlock("tnt");
/* 269 */   public static final Block bookshelf = getRegisteredBlock("bookshelf");
/* 270 */   public static final Block mossy_cobblestone = getRegisteredBlock("mossy_cobblestone");
/* 271 */   public static final Block obsidian = getRegisteredBlock("obsidian");
/* 272 */   public static final Block torch = getRegisteredBlock("torch");
/* 273 */   public static final BlockFire fire = (BlockFire)getRegisteredBlock("fire");
/* 274 */   public static final Block mob_spawner = getRegisteredBlock("mob_spawner");
/* 275 */   public static final Block oak_stairs = getRegisteredBlock("oak_stairs");
/* 276 */   public static final BlockChest chest = (BlockChest)getRegisteredBlock("chest");
/* 277 */   public static final BlockRedstoneWire redstone_wire = (BlockRedstoneWire)getRegisteredBlock("redstone_wire");
/* 278 */   public static final Block diamond_ore = getRegisteredBlock("diamond_ore");
/* 279 */   public static final Block diamond_block = getRegisteredBlock("diamond_block");
/* 280 */   public static final Block crafting_table = getRegisteredBlock("crafting_table");
/* 281 */   public static final Block wheat = getRegisteredBlock("wheat");
/* 282 */   public static final Block farmland = getRegisteredBlock("farmland");
/* 283 */   public static final Block furnace = getRegisteredBlock("furnace");
/* 284 */   public static final Block lit_furnace = getRegisteredBlock("lit_furnace");
/* 285 */   public static final Block standing_sign = getRegisteredBlock("standing_sign");
/* 286 */   public static final Block oak_door = getRegisteredBlock("wooden_door");
/* 287 */   public static final Block spruce_door = getRegisteredBlock("spruce_door");
/* 288 */   public static final Block birch_door = getRegisteredBlock("birch_door");
/* 289 */   public static final Block jungle_door = getRegisteredBlock("jungle_door");
/* 290 */   public static final Block acacia_door = getRegisteredBlock("acacia_door");
/* 291 */   public static final Block dark_oak_door = getRegisteredBlock("dark_oak_door");
/* 292 */   public static final Block ladder = getRegisteredBlock("ladder");
/* 293 */   public static final Block rail = getRegisteredBlock("rail");
/* 294 */   public static final Block stone_stairs = getRegisteredBlock("stone_stairs");
/* 295 */   public static final Block wall_sign = getRegisteredBlock("wall_sign");
/* 296 */   public static final Block lever = getRegisteredBlock("lever");
/* 297 */   public static final Block stone_pressure_plate = getRegisteredBlock("stone_pressure_plate");
/* 298 */   public static final Block iron_door = getRegisteredBlock("iron_door");
/* 299 */   public static final Block wooden_pressure_plate = getRegisteredBlock("wooden_pressure_plate");
/* 300 */   public static final Block redstone_ore = getRegisteredBlock("redstone_ore");
/* 301 */   public static final Block lit_redstone_ore = getRegisteredBlock("lit_redstone_ore");
/* 302 */   public static final Block unlit_redstone_torch = getRegisteredBlock("unlit_redstone_torch");
/* 303 */   public static final Block redstone_torch = getRegisteredBlock("redstone_torch");
/* 304 */   public static final Block stone_button = getRegisteredBlock("stone_button");
/* 305 */   public static final Block snow_layer = getRegisteredBlock("snow_layer");
/* 306 */   public static final Block ice = getRegisteredBlock("ice");
/* 307 */   public static final Block snow = getRegisteredBlock("snow");
/* 308 */   public static final BlockCactus cactus = (BlockCactus)getRegisteredBlock("cactus");
/* 309 */   public static final Block clay = getRegisteredBlock("clay");
/* 310 */   public static final BlockReed reeds = (BlockReed)getRegisteredBlock("reeds");
/* 311 */   public static final Block jukebox = getRegisteredBlock("jukebox");
/* 312 */   public static final Block oak_fence = getRegisteredBlock("fence");
/* 313 */   public static final Block spruce_fence = getRegisteredBlock("spruce_fence");
/* 314 */   public static final Block birch_fence = getRegisteredBlock("birch_fence");
/* 315 */   public static final Block jungle_fence = getRegisteredBlock("jungle_fence");
/* 316 */   public static final Block dark_oak_fence = getRegisteredBlock("dark_oak_fence");
/* 317 */   public static final Block acacia_fence = getRegisteredBlock("acacia_fence");
/* 318 */   public static final Block pumpkin = getRegisteredBlock("pumpkin");
/* 319 */   public static final Block netherrack = getRegisteredBlock("netherrack");
/* 320 */   public static final Block soul_sand = getRegisteredBlock("soul_sand");
/* 321 */   public static final Block glowstone = getRegisteredBlock("glowstone");
/* 322 */   public static final BlockPortal portal = (BlockPortal)getRegisteredBlock("portal");
/* 323 */   public static final Block lit_pumpkin = getRegisteredBlock("lit_pumpkin");
/* 324 */   public static final Block cake = getRegisteredBlock("cake");
/* 325 */   public static final BlockRedstoneRepeater unpowered_repeater = (BlockRedstoneRepeater)getRegisteredBlock("unpowered_repeater");
/* 326 */   public static final BlockRedstoneRepeater powered_repeater = (BlockRedstoneRepeater)getRegisteredBlock("powered_repeater");
/* 327 */   public static final Block trapdoor = getRegisteredBlock("trapdoor");
/* 328 */   public static final Block monster_egg = getRegisteredBlock("monster_egg");
/* 329 */   public static final Block stonebrick = getRegisteredBlock("stonebrick");
/* 330 */   public static final Block brown_mushroom_block = getRegisteredBlock("brown_mushroom_block");
/* 331 */   public static final Block red_mushroom_block = getRegisteredBlock("red_mushroom_block");
/* 332 */   public static final Block iron_bars = getRegisteredBlock("iron_bars");
/* 333 */   public static final Block glass_pane = getRegisteredBlock("glass_pane");
/* 334 */   public static final Block melon_block = getRegisteredBlock("melon_block");
/* 335 */   public static final Block pumpkin_stem = getRegisteredBlock("pumpkin_stem");
/* 336 */   public static final Block melon_stem = getRegisteredBlock("melon_stem");
/* 337 */   public static final Block vine = getRegisteredBlock("vine");
/* 338 */   public static final Block oak_fence_gate = getRegisteredBlock("fence_gate");
/* 339 */   public static final Block spruce_fence_gate = getRegisteredBlock("spruce_fence_gate");
/* 340 */   public static final Block birch_fence_gate = getRegisteredBlock("birch_fence_gate");
/* 341 */   public static final Block jungle_fence_gate = getRegisteredBlock("jungle_fence_gate");
/* 342 */   public static final Block dark_oak_fence_gate = getRegisteredBlock("dark_oak_fence_gate");
/* 343 */   public static final Block acacia_fence_gate = getRegisteredBlock("acacia_fence_gate");
/* 344 */   public static final Block brick_stairs = getRegisteredBlock("brick_stairs");
/* 345 */   public static final Block stone_brick_stairs = getRegisteredBlock("stone_brick_stairs");
/* 346 */   public static final BlockMycelium mycelium = (BlockMycelium)getRegisteredBlock("mycelium");
/* 347 */   public static final Block waterlily = getRegisteredBlock("waterlily");
/* 348 */   public static final Block nether_brick = getRegisteredBlock("nether_brick");
/* 349 */   public static final Block nether_brick_fence = getRegisteredBlock("nether_brick_fence");
/* 350 */   public static final Block nether_brick_stairs = getRegisteredBlock("nether_brick_stairs");
/* 351 */   public static final Block nether_wart = getRegisteredBlock("nether_wart");
/* 352 */   public static final Block enchanting_table = getRegisteredBlock("enchanting_table");
/* 353 */   public static final Block brewing_stand = getRegisteredBlock("brewing_stand");
/* 354 */   public static final BlockCauldron cauldron = (BlockCauldron)getRegisteredBlock("cauldron");
/* 355 */   public static final Block end_portal = getRegisteredBlock("end_portal");
/* 356 */   public static final Block end_portal_frame = getRegisteredBlock("end_portal_frame");
/* 357 */   public static final Block end_stone = getRegisteredBlock("end_stone");
/* 358 */   public static final Block dragon_egg = getRegisteredBlock("dragon_egg");
/* 359 */   public static final Block redstone_lamp = getRegisteredBlock("redstone_lamp");
/* 360 */   public static final Block lit_redstone_lamp = getRegisteredBlock("lit_redstone_lamp");
/* 361 */   public static final BlockSlab double_wooden_slab = (BlockSlab)getRegisteredBlock("double_wooden_slab");
/* 362 */   public static final BlockSlab wooden_slab = (BlockSlab)getRegisteredBlock("wooden_slab");
/* 363 */   public static final Block cocoa = getRegisteredBlock("cocoa");
/* 364 */   public static final Block sandstone_stairs = getRegisteredBlock("sandstone_stairs");
/* 365 */   public static final Block emerald_ore = getRegisteredBlock("emerald_ore");
/* 366 */   public static final Block ender_chest = getRegisteredBlock("ender_chest");
/* 367 */   public static final BlockTripWireHook tripwire_hook = (BlockTripWireHook)getRegisteredBlock("tripwire_hook");
/* 368 */   public static final Block tripwire = getRegisteredBlock("tripwire");
/* 369 */   public static final Block emerald_block = getRegisteredBlock("emerald_block");
/* 370 */   public static final Block spruce_stairs = getRegisteredBlock("spruce_stairs");
/* 371 */   public static final Block birch_stairs = getRegisteredBlock("birch_stairs");
/* 372 */   public static final Block jungle_stairs = getRegisteredBlock("jungle_stairs");
/* 373 */   public static final Block command_block = getRegisteredBlock("command_block");
/* 374 */   public static final BlockBeacon beacon = (BlockBeacon)getRegisteredBlock("beacon");
/* 375 */   public static final Block cobblestone_wall = getRegisteredBlock("cobblestone_wall");
/* 376 */   public static final Block flower_pot = getRegisteredBlock("flower_pot");
/* 377 */   public static final Block carrots = getRegisteredBlock("carrots");
/* 378 */   public static final Block potatoes = getRegisteredBlock("potatoes");
/* 379 */   public static final Block wooden_button = getRegisteredBlock("wooden_button");
/* 380 */   public static final BlockSkull skull = (BlockSkull)getRegisteredBlock("skull");
/* 381 */   public static final Block anvil = getRegisteredBlock("anvil");
/* 382 */   public static final Block trapped_chest = getRegisteredBlock("trapped_chest");
/* 383 */   public static final Block light_weighted_pressure_plate = getRegisteredBlock("light_weighted_pressure_plate");
/* 384 */   public static final Block heavy_weighted_pressure_plate = getRegisteredBlock("heavy_weighted_pressure_plate");
/* 385 */   public static final BlockRedstoneComparator unpowered_comparator = (BlockRedstoneComparator)getRegisteredBlock("unpowered_comparator");
/* 386 */   public static final BlockRedstoneComparator powered_comparator = (BlockRedstoneComparator)getRegisteredBlock("powered_comparator");
/* 387 */   public static final BlockDaylightDetector daylight_detector = (BlockDaylightDetector)getRegisteredBlock("daylight_detector");
/* 388 */   public static final BlockDaylightDetector daylight_detector_inverted = (BlockDaylightDetector)getRegisteredBlock("daylight_detector_inverted");
/* 389 */   public static final Block redstone_block = getRegisteredBlock("redstone_block");
/* 390 */   public static final Block quartz_ore = getRegisteredBlock("quartz_ore");
/* 391 */   public static final BlockHopper hopper = (BlockHopper)getRegisteredBlock("hopper");
/* 392 */   public static final Block quartz_block = getRegisteredBlock("quartz_block");
/* 393 */   public static final Block quartz_stairs = getRegisteredBlock("quartz_stairs");
/* 394 */   public static final Block activator_rail = getRegisteredBlock("activator_rail");
/* 395 */   public static final Block dropper = getRegisteredBlock("dropper");
/* 396 */   public static final Block stained_hardened_clay = getRegisteredBlock("stained_hardened_clay");
/* 397 */   public static final Block barrier = getRegisteredBlock("barrier");
/* 398 */   public static final Block iron_trapdoor = getRegisteredBlock("iron_trapdoor");
/* 399 */   public static final Block hay_block = getRegisteredBlock("hay_block");
/* 400 */   public static final Block carpet = getRegisteredBlock("carpet");
/* 401 */   public static final Block hardened_clay = getRegisteredBlock("hardened_clay");
/* 402 */   public static final Block coal_block = getRegisteredBlock("coal_block");
/* 403 */   public static final Block packed_ice = getRegisteredBlock("packed_ice");
/* 404 */   public static final Block acacia_stairs = getRegisteredBlock("acacia_stairs");
/* 405 */   public static final Block dark_oak_stairs = getRegisteredBlock("dark_oak_stairs");
/* 406 */   public static final Block slime_block = getRegisteredBlock("slime");
/* 407 */   public static final BlockDoublePlant double_plant = (BlockDoublePlant)getRegisteredBlock("double_plant");
/* 408 */   public static final BlockStainedGlass stained_glass = (BlockStainedGlass)getRegisteredBlock("stained_glass");
/* 409 */   public static final BlockStainedGlassPane stained_glass_pane = (BlockStainedGlassPane)getRegisteredBlock("stained_glass_pane");
/* 410 */   public static final Block prismarine = getRegisteredBlock("prismarine");
/* 411 */   public static final Block sea_lantern = getRegisteredBlock("sea_lantern");
/* 412 */   public static final Block standing_banner = getRegisteredBlock("standing_banner");
/* 413 */   public static final Block wall_banner = getRegisteredBlock("wall_banner");
/* 414 */   public static final Block red_sandstone = getRegisteredBlock("red_sandstone");
/* 415 */   public static final Block red_sandstone_stairs = getRegisteredBlock("red_sandstone_stairs");
/* 416 */   public static final BlockSlab double_stone_slab2 = (BlockSlab)getRegisteredBlock("double_stone_slab2");
/* 417 */   public static final BlockSlab stone_slab2 = (BlockSlab)getRegisteredBlock("stone_slab2");
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\init\Blocks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */