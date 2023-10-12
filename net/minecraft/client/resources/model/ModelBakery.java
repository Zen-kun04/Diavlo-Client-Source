/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.renderer.BlockModelShapes;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.BlockPart;
/*     */ import net.minecraft.client.renderer.block.model.BlockPartFace;
/*     */ import net.minecraft.client.renderer.block.model.FaceBakery;
/*     */ import net.minecraft.client.renderer.block.model.ItemModelGenerator;
/*     */ import net.minecraft.client.renderer.block.model.ModelBlock;
/*     */ import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
/*     */ import net.minecraft.client.renderer.texture.IIconCreator;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.resources.IResource;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IRegistry;
/*     */ import net.minecraft.util.RegistrySimple;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.client.model.ITransformation;
/*     */ import net.minecraftforge.client.model.TRSRTransformation;
/*     */ import net.minecraftforge.fml.common.registry.RegistryDelegate;
/*     */ import net.optifine.CustomItems;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import net.optifine.util.StrUtils;
/*     */ import net.optifine.util.TextureUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelBakery
/*     */ {
/*  58 */   private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet((Object[])new ResourceLocation[] { new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/lava_flow"), new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"), new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"), new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"), new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"), new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9"), new ResourceLocation("items/empty_armor_slot_helmet"), new ResourceLocation("items/empty_armor_slot_chestplate"), new ResourceLocation("items/empty_armor_slot_leggings"), new ResourceLocation("items/empty_armor_slot_boots") });
/*  59 */   private static final Logger LOGGER = LogManager.getLogger();
/*  60 */   protected static final ModelResourceLocation MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
/*  61 */   private static final Map<String, String> BUILT_IN_MODELS = Maps.newHashMap();
/*  62 */   private static final Joiner JOINER = Joiner.on(" -> ");
/*     */   private final IResourceManager resourceManager;
/*  64 */   private final Map<ResourceLocation, TextureAtlasSprite> sprites = Maps.newHashMap();
/*  65 */   private final Map<ResourceLocation, ModelBlock> models = Maps.newLinkedHashMap();
/*  66 */   private final Map<ModelResourceLocation, ModelBlockDefinition.Variants> variants = Maps.newLinkedHashMap();
/*     */   private final TextureMap textureMap;
/*     */   private final BlockModelShapes blockModelShapes;
/*  69 */   private final FaceBakery faceBakery = new FaceBakery();
/*  70 */   private final ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
/*  71 */   private RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple();
/*  72 */   private static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
/*  73 */   private static final ModelBlock MODEL_COMPASS = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
/*  74 */   private static final ModelBlock MODEL_CLOCK = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
/*  75 */   private static final ModelBlock MODEL_ENTITY = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
/*  76 */   private Map<String, ResourceLocation> itemLocations = Maps.newLinkedHashMap();
/*  77 */   private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = Maps.newHashMap();
/*  78 */   private Map<Item, List<String>> variantNames = Maps.newIdentityHashMap();
/*  79 */   private static Map<RegistryDelegate<Item>, Set<String>> customVariantNames = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   public ModelBakery(IResourceManager p_i46085_1_, TextureMap p_i46085_2_, BlockModelShapes p_i46085_3_) {
/*  83 */     this.resourceManager = p_i46085_1_;
/*  84 */     this.textureMap = p_i46085_2_;
/*  85 */     this.blockModelShapes = p_i46085_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   public IRegistry<ModelResourceLocation, IBakedModel> setupModelRegistry() {
/*  90 */     loadVariantItemModels();
/*  91 */     loadModelsCheck();
/*  92 */     loadSprites();
/*  93 */     bakeItemModels();
/*  94 */     bakeBlockModels();
/*  95 */     return (IRegistry<ModelResourceLocation, IBakedModel>)this.bakedRegistry;
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadVariantItemModels() {
/* 100 */     loadVariants(this.blockModelShapes.getBlockStateMapper().putAllStateModelLocations().values());
/* 101 */     this.variants.put(MODEL_MISSING, new ModelBlockDefinition.Variants(MODEL_MISSING.getVariant(), Lists.newArrayList((Object[])new ModelBlockDefinition.Variant[] { new ModelBlockDefinition.Variant(new ResourceLocation(MODEL_MISSING.getResourcePath()), ModelRotation.X0_Y0, false, 1) })));
/* 102 */     ResourceLocation resourcelocation = new ResourceLocation("item_frame");
/* 103 */     ModelBlockDefinition modelblockdefinition = getModelBlockDefinition(resourcelocation);
/* 104 */     registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "normal"));
/* 105 */     registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "map"));
/* 106 */     loadVariantModels();
/* 107 */     loadItemModels();
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadVariants(Collection<ModelResourceLocation> p_177591_1_) {
/* 112 */     for (ModelResourceLocation modelresourcelocation : p_177591_1_) {
/*     */ 
/*     */       
/*     */       try {
/* 116 */         ModelBlockDefinition modelblockdefinition = getModelBlockDefinition(modelresourcelocation);
/*     */ 
/*     */         
/*     */         try {
/* 120 */           registerVariant(modelblockdefinition, modelresourcelocation);
/*     */         }
/* 122 */         catch (Exception exception) {
/*     */           
/* 124 */           LOGGER.warn("Unable to load variant: " + modelresourcelocation.getVariant() + " from " + modelresourcelocation, exception);
/*     */         }
/*     */       
/* 127 */       } catch (Exception exception1) {
/*     */         
/* 129 */         LOGGER.warn("Unable to load definition " + modelresourcelocation, exception1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerVariant(ModelBlockDefinition p_177569_1_, ModelResourceLocation p_177569_2_) {
/* 136 */     this.variants.put(p_177569_2_, p_177569_1_.getVariants(p_177569_2_.getVariant()));
/*     */   }
/*     */ 
/*     */   
/*     */   private ModelBlockDefinition getModelBlockDefinition(ResourceLocation p_177586_1_) {
/* 141 */     ResourceLocation resourcelocation = getBlockStateLocation(p_177586_1_);
/* 142 */     ModelBlockDefinition modelblockdefinition = this.blockDefinitions.get(resourcelocation);
/*     */     
/* 144 */     if (modelblockdefinition == null) {
/*     */       
/* 146 */       List<ModelBlockDefinition> list = Lists.newArrayList();
/*     */ 
/*     */       
/*     */       try {
/* 150 */         for (IResource iresource : this.resourceManager.getAllResources(resourcelocation)) {
/*     */           
/* 152 */           InputStream inputstream = null;
/*     */ 
/*     */           
/*     */           try {
/* 156 */             inputstream = iresource.getInputStream();
/* 157 */             ModelBlockDefinition modelblockdefinition1 = ModelBlockDefinition.parseFromReader(new InputStreamReader(inputstream, Charsets.UTF_8));
/* 158 */             list.add(modelblockdefinition1);
/*     */           }
/* 160 */           catch (Exception exception) {
/*     */             
/* 162 */             throw new RuntimeException("Encountered an exception when loading model definition of '" + p_177586_1_ + "' from: '" + iresource.getResourceLocation() + "' in resourcepack: '" + iresource.getResourcePackName() + "'", exception);
/*     */           }
/*     */           finally {
/*     */             
/* 166 */             IOUtils.closeQuietly(inputstream);
/*     */           }
/*     */         
/*     */         } 
/* 170 */       } catch (IOException ioexception) {
/*     */         
/* 172 */         throw new RuntimeException("Encountered an exception when loading model definition of model " + resourcelocation.toString(), ioexception);
/*     */       } 
/*     */       
/* 175 */       modelblockdefinition = new ModelBlockDefinition(list);
/* 176 */       this.blockDefinitions.put(resourcelocation, modelblockdefinition);
/*     */     } 
/*     */     
/* 179 */     return modelblockdefinition;
/*     */   }
/*     */ 
/*     */   
/*     */   private ResourceLocation getBlockStateLocation(ResourceLocation p_177584_1_) {
/* 184 */     return new ResourceLocation(p_177584_1_.getResourceDomain(), "blockstates/" + p_177584_1_.getResourcePath() + ".json");
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadVariantModels() {
/* 189 */     for (ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
/*     */       
/* 191 */       for (ModelBlockDefinition.Variant modelblockdefinition$variant : ((ModelBlockDefinition.Variants)this.variants.get(modelresourcelocation)).getVariants()) {
/*     */         
/* 193 */         ResourceLocation resourcelocation = modelblockdefinition$variant.getModelLocation();
/*     */         
/* 195 */         if (this.models.get(resourcelocation) == null)
/*     */           
/*     */           try {
/*     */             
/* 199 */             ModelBlock modelblock = loadModel(resourcelocation);
/* 200 */             this.models.put(resourcelocation, modelblock);
/*     */           }
/* 202 */           catch (Exception exception) {
/*     */             
/* 204 */             LOGGER.warn("Unable to load block model: '" + resourcelocation + "' for variant: '" + modelresourcelocation + "'", exception);
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private ModelBlock loadModel(ResourceLocation p_177594_1_) throws IOException {
/*     */     Reader reader;
/*     */     ModelBlock modelblock;
/* 213 */     String s = p_177594_1_.getResourcePath();
/*     */     
/* 215 */     if ("builtin/generated".equals(s))
/*     */     {
/* 217 */       return MODEL_GENERATED;
/*     */     }
/* 219 */     if ("builtin/compass".equals(s))
/*     */     {
/* 221 */       return MODEL_COMPASS;
/*     */     }
/* 223 */     if ("builtin/clock".equals(s))
/*     */     {
/* 225 */       return MODEL_CLOCK;
/*     */     }
/* 227 */     if ("builtin/entity".equals(s))
/*     */     {
/* 229 */       return MODEL_ENTITY;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     if (s.startsWith("builtin/")) {
/*     */       
/* 237 */       String s1 = s.substring("builtin/".length());
/* 238 */       String s2 = BUILT_IN_MODELS.get(s1);
/*     */       
/* 240 */       if (s2 == null)
/*     */       {
/* 242 */         throw new FileNotFoundException(p_177594_1_.toString());
/*     */       }
/*     */       
/* 245 */       reader = new StringReader(s2);
/*     */     }
/*     */     else {
/*     */       
/* 249 */       p_177594_1_ = getModelLocation(p_177594_1_);
/* 250 */       IResource iresource = this.resourceManager.getResource(p_177594_1_);
/* 251 */       reader = new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 258 */       ModelBlock modelblock1 = ModelBlock.deserialize(reader);
/* 259 */       modelblock1.name = p_177594_1_.toString();
/* 260 */       modelblock = modelblock1;
/* 261 */       String s3 = TextureUtils.getBasePath(p_177594_1_.getResourcePath());
/* 262 */       fixModelLocations(modelblock1, s3);
/*     */     }
/*     */     finally {
/*     */       
/* 266 */       reader.close();
/*     */     } 
/*     */     
/* 269 */     return modelblock;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceLocation getModelLocation(ResourceLocation p_177580_1_) {
/* 275 */     ResourceLocation resourcelocation = p_177580_1_;
/* 276 */     String s = p_177580_1_.getResourcePath();
/*     */     
/* 278 */     if (!s.startsWith("mcpatcher") && !s.startsWith("optifine"))
/*     */     {
/* 280 */       return new ResourceLocation(p_177580_1_.getResourceDomain(), "models/" + p_177580_1_.getResourcePath() + ".json");
/*     */     }
/*     */ 
/*     */     
/* 284 */     if (!s.endsWith(".json"))
/*     */     {
/* 286 */       resourcelocation = new ResourceLocation(p_177580_1_.getResourceDomain(), s + ".json");
/*     */     }
/*     */     
/* 289 */     return resourcelocation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadItemModels() {
/* 295 */     registerVariantNames();
/*     */     
/* 297 */     for (Item item : Item.itemRegistry) {
/*     */       
/* 299 */       for (String s : getVariantNames(item)) {
/*     */         
/* 301 */         ResourceLocation resourcelocation = getItemLocation(s);
/* 302 */         this.itemLocations.put(s, resourcelocation);
/*     */         
/* 304 */         if (this.models.get(resourcelocation) == null) {
/*     */           
/*     */           try {
/*     */             
/* 308 */             ModelBlock modelblock = loadModel(resourcelocation);
/* 309 */             this.models.put(resourcelocation, modelblock);
/*     */           }
/* 311 */           catch (Exception exception) {
/*     */             
/* 313 */             LOGGER.warn("Unable to load item model: '" + resourcelocation + "' for item: '" + Item.itemRegistry.getNameForObject(item) + "'", exception);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadItemModel(String p_loadItemModel_1_, ResourceLocation p_loadItemModel_2_, ResourceLocation p_loadItemModel_3_) {
/* 322 */     this.itemLocations.put(p_loadItemModel_1_, p_loadItemModel_2_);
/*     */     
/* 324 */     if (this.models.get(p_loadItemModel_2_) == null) {
/*     */       
/*     */       try {
/*     */         
/* 328 */         ModelBlock modelblock = loadModel(p_loadItemModel_2_);
/* 329 */         this.models.put(p_loadItemModel_2_, modelblock);
/*     */       }
/* 331 */       catch (Exception exception) {
/*     */         
/* 333 */         LOGGER.warn("Unable to load item model: '{}' for item: '{}'", new Object[] { p_loadItemModel_2_, p_loadItemModel_3_ });
/* 334 */         LOGGER.warn(exception.getClass().getName() + ": " + exception.getMessage());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerVariantNames() {
/* 341 */     this.variantNames.clear();
/* 342 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stone), Lists.newArrayList((Object[])new String[] { "stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth" }));
/* 343 */     this.variantNames.put(Item.getItemFromBlock(Blocks.dirt), Lists.newArrayList((Object[])new String[] { "dirt", "coarse_dirt", "podzol" }));
/* 344 */     this.variantNames.put(Item.getItemFromBlock(Blocks.planks), Lists.newArrayList((Object[])new String[] { "oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks" }));
/* 345 */     this.variantNames.put(Item.getItemFromBlock(Blocks.sapling), Lists.newArrayList((Object[])new String[] { "oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling" }));
/* 346 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.sand), Lists.newArrayList((Object[])new String[] { "sand", "red_sand" }));
/* 347 */     this.variantNames.put(Item.getItemFromBlock(Blocks.log), Lists.newArrayList((Object[])new String[] { "oak_log", "spruce_log", "birch_log", "jungle_log" }));
/* 348 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.leaves), Lists.newArrayList((Object[])new String[] { "oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves" }));
/* 349 */     this.variantNames.put(Item.getItemFromBlock(Blocks.sponge), Lists.newArrayList((Object[])new String[] { "sponge", "sponge_wet" }));
/* 350 */     this.variantNames.put(Item.getItemFromBlock(Blocks.sandstone), Lists.newArrayList((Object[])new String[] { "sandstone", "chiseled_sandstone", "smooth_sandstone" }));
/* 351 */     this.variantNames.put(Item.getItemFromBlock(Blocks.red_sandstone), Lists.newArrayList((Object[])new String[] { "red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone" }));
/* 352 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.tallgrass), Lists.newArrayList((Object[])new String[] { "dead_bush", "tall_grass", "fern" }));
/* 353 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.deadbush), Lists.newArrayList((Object[])new String[] { "dead_bush" }));
/* 354 */     this.variantNames.put(Item.getItemFromBlock(Blocks.wool), Lists.newArrayList((Object[])new String[] { "black_wool", "red_wool", "green_wool", "brown_wool", "blue_wool", "purple_wool", "cyan_wool", "silver_wool", "gray_wool", "pink_wool", "lime_wool", "yellow_wool", "light_blue_wool", "magenta_wool", "orange_wool", "white_wool" }));
/* 355 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.yellow_flower), Lists.newArrayList((Object[])new String[] { "dandelion" }));
/* 356 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.red_flower), Lists.newArrayList((Object[])new String[] { "poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy" }));
/* 357 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.stone_slab), Lists.newArrayList((Object[])new String[] { "stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab", "stone_brick_slab", "nether_brick_slab", "quartz_slab" }));
/* 358 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.stone_slab2), Lists.newArrayList((Object[])new String[] { "red_sandstone_slab" }));
/* 359 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.stained_glass), Lists.newArrayList((Object[])new String[] { "black_stained_glass", "red_stained_glass", "green_stained_glass", "brown_stained_glass", "blue_stained_glass", "purple_stained_glass", "cyan_stained_glass", "silver_stained_glass", "gray_stained_glass", "pink_stained_glass", "lime_stained_glass", "yellow_stained_glass", "light_blue_stained_glass", "magenta_stained_glass", "orange_stained_glass", "white_stained_glass" }));
/* 360 */     this.variantNames.put(Item.getItemFromBlock(Blocks.monster_egg), Lists.newArrayList((Object[])new String[] { "stone_monster_egg", "cobblestone_monster_egg", "stone_brick_monster_egg", "mossy_brick_monster_egg", "cracked_brick_monster_egg", "chiseled_brick_monster_egg" }));
/* 361 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stonebrick), Lists.newArrayList((Object[])new String[] { "stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick" }));
/* 362 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.wooden_slab), Lists.newArrayList((Object[])new String[] { "oak_slab", "spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab" }));
/* 363 */     this.variantNames.put(Item.getItemFromBlock(Blocks.cobblestone_wall), Lists.newArrayList((Object[])new String[] { "cobblestone_wall", "mossy_cobblestone_wall" }));
/* 364 */     this.variantNames.put(Item.getItemFromBlock(Blocks.anvil), Lists.newArrayList((Object[])new String[] { "anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged" }));
/* 365 */     this.variantNames.put(Item.getItemFromBlock(Blocks.quartz_block), Lists.newArrayList((Object[])new String[] { "quartz_block", "chiseled_quartz_block", "quartz_column" }));
/* 366 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stained_hardened_clay), Lists.newArrayList((Object[])new String[] { "black_stained_hardened_clay", "red_stained_hardened_clay", "green_stained_hardened_clay", "brown_stained_hardened_clay", "blue_stained_hardened_clay", "purple_stained_hardened_clay", "cyan_stained_hardened_clay", "silver_stained_hardened_clay", "gray_stained_hardened_clay", "pink_stained_hardened_clay", "lime_stained_hardened_clay", "yellow_stained_hardened_clay", "light_blue_stained_hardened_clay", "magenta_stained_hardened_clay", "orange_stained_hardened_clay", "white_stained_hardened_clay" }));
/* 367 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.stained_glass_pane), Lists.newArrayList((Object[])new String[] { "black_stained_glass_pane", "red_stained_glass_pane", "green_stained_glass_pane", "brown_stained_glass_pane", "blue_stained_glass_pane", "purple_stained_glass_pane", "cyan_stained_glass_pane", "silver_stained_glass_pane", "gray_stained_glass_pane", "pink_stained_glass_pane", "lime_stained_glass_pane", "yellow_stained_glass_pane", "light_blue_stained_glass_pane", "magenta_stained_glass_pane", "orange_stained_glass_pane", "white_stained_glass_pane" }));
/* 368 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.leaves2), Lists.newArrayList((Object[])new String[] { "acacia_leaves", "dark_oak_leaves" }));
/* 369 */     this.variantNames.put(Item.getItemFromBlock(Blocks.log2), Lists.newArrayList((Object[])new String[] { "acacia_log", "dark_oak_log" }));
/* 370 */     this.variantNames.put(Item.getItemFromBlock(Blocks.prismarine), Lists.newArrayList((Object[])new String[] { "prismarine", "prismarine_bricks", "dark_prismarine" }));
/* 371 */     this.variantNames.put(Item.getItemFromBlock(Blocks.carpet), Lists.newArrayList((Object[])new String[] { "black_carpet", "red_carpet", "green_carpet", "brown_carpet", "blue_carpet", "purple_carpet", "cyan_carpet", "silver_carpet", "gray_carpet", "pink_carpet", "lime_carpet", "yellow_carpet", "light_blue_carpet", "magenta_carpet", "orange_carpet", "white_carpet" }));
/* 372 */     this.variantNames.put(Item.getItemFromBlock((Block)Blocks.double_plant), Lists.newArrayList((Object[])new String[] { "sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia" }));
/* 373 */     this.variantNames.put(Items.bow, Lists.newArrayList((Object[])new String[] { "bow", "bow_pulling_0", "bow_pulling_1", "bow_pulling_2" }));
/* 374 */     this.variantNames.put(Items.coal, Lists.newArrayList((Object[])new String[] { "coal", "charcoal" }));
/* 375 */     this.variantNames.put(Items.fishing_rod, Lists.newArrayList((Object[])new String[] { "fishing_rod", "fishing_rod_cast" }));
/* 376 */     this.variantNames.put(Items.fish, Lists.newArrayList((Object[])new String[] { "cod", "salmon", "clownfish", "pufferfish" }));
/* 377 */     this.variantNames.put(Items.cooked_fish, Lists.newArrayList((Object[])new String[] { "cooked_cod", "cooked_salmon" }));
/* 378 */     this.variantNames.put(Items.dye, Lists.newArrayList((Object[])new String[] { "dye_black", "dye_red", "dye_green", "dye_brown", "dye_blue", "dye_purple", "dye_cyan", "dye_silver", "dye_gray", "dye_pink", "dye_lime", "dye_yellow", "dye_light_blue", "dye_magenta", "dye_orange", "dye_white" }));
/* 379 */     this.variantNames.put(Items.potionitem, Lists.newArrayList((Object[])new String[] { "bottle_drinkable", "bottle_splash" }));
/* 380 */     this.variantNames.put(Items.skull, Lists.newArrayList((Object[])new String[] { "skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper" }));
/* 381 */     this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence_gate), Lists.newArrayList((Object[])new String[] { "oak_fence_gate" }));
/* 382 */     this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence), Lists.newArrayList((Object[])new String[] { "oak_fence" }));
/* 383 */     this.variantNames.put(Items.oak_door, Lists.newArrayList((Object[])new String[] { "oak_door" }));
/*     */     
/* 385 */     for (Map.Entry<RegistryDelegate<Item>, Set<String>> entry : customVariantNames.entrySet())
/*     */     {
/* 387 */       this.variantNames.put((Item)((RegistryDelegate)entry.getKey()).get(), Lists.newArrayList(((Set)entry.getValue()).iterator()));
/*     */     }
/*     */     
/* 390 */     CustomItems.update();
/* 391 */     CustomItems.loadModels(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private List<String> getVariantNames(Item p_177596_1_) {
/* 396 */     List<String> list = this.variantNames.get(p_177596_1_);
/*     */     
/* 398 */     if (list == null)
/*     */     {
/* 400 */       list = Collections.singletonList(((ResourceLocation)Item.itemRegistry.getNameForObject(p_177596_1_)).toString());
/*     */     }
/*     */     
/* 403 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private ResourceLocation getItemLocation(String p_177583_1_) {
/* 408 */     ResourceLocation resourcelocation = new ResourceLocation(p_177583_1_);
/*     */     
/* 410 */     if (Reflector.ForgeHooksClient.exists())
/*     */     {
/* 412 */       resourcelocation = new ResourceLocation(p_177583_1_.replaceAll("#.*", ""));
/*     */     }
/*     */     
/* 415 */     return new ResourceLocation(resourcelocation.getResourceDomain(), "item/" + resourcelocation.getResourcePath());
/*     */   }
/*     */ 
/*     */   
/*     */   private void bakeBlockModels() {
/* 420 */     for (ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
/*     */       
/* 422 */       WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();
/* 423 */       int i = 0;
/*     */       
/* 425 */       for (ModelBlockDefinition.Variant modelblockdefinition$variant : ((ModelBlockDefinition.Variants)this.variants.get(modelresourcelocation)).getVariants()) {
/*     */         
/* 427 */         ModelBlock modelblock = this.models.get(modelblockdefinition$variant.getModelLocation());
/*     */         
/* 429 */         if (modelblock != null && modelblock.isResolved()) {
/*     */           
/* 431 */           i++;
/* 432 */           weightedbakedmodel$builder.add(bakeModel(modelblock, modelblockdefinition$variant.getRotation(), modelblockdefinition$variant.isUvLocked()), modelblockdefinition$variant.getWeight());
/*     */           
/*     */           continue;
/*     */         } 
/* 436 */         LOGGER.warn("Missing model for: " + modelresourcelocation);
/*     */       } 
/*     */ 
/*     */       
/* 440 */       if (i == 0) {
/*     */         
/* 442 */         LOGGER.warn("No weighted models for: " + modelresourcelocation); continue;
/*     */       } 
/* 444 */       if (i == 1) {
/*     */         
/* 446 */         this.bakedRegistry.putObject(modelresourcelocation, weightedbakedmodel$builder.first());
/*     */         
/*     */         continue;
/*     */       } 
/* 450 */       this.bakedRegistry.putObject(modelresourcelocation, weightedbakedmodel$builder.build());
/*     */     } 
/*     */ 
/*     */     
/* 454 */     for (Map.Entry<String, ResourceLocation> entry : this.itemLocations.entrySet()) {
/*     */       
/* 456 */       ResourceLocation resourcelocation = entry.getValue();
/* 457 */       ModelResourceLocation modelresourcelocation1 = new ModelResourceLocation(entry.getKey(), "inventory");
/*     */       
/* 459 */       if (Reflector.ModelLoader_getInventoryVariant.exists())
/*     */       {
/* 461 */         modelresourcelocation1 = (ModelResourceLocation)Reflector.call(Reflector.ModelLoader_getInventoryVariant, new Object[] { entry.getKey() });
/*     */       }
/*     */       
/* 464 */       ModelBlock modelblock1 = this.models.get(resourcelocation);
/*     */       
/* 466 */       if (modelblock1 != null && modelblock1.isResolved()) {
/*     */         
/* 468 */         if (isCustomRenderer(modelblock1)) {
/*     */           
/* 470 */           this.bakedRegistry.putObject(modelresourcelocation1, new BuiltInModel(modelblock1.getAllTransforms()));
/*     */           
/*     */           continue;
/*     */         } 
/* 474 */         this.bakedRegistry.putObject(modelresourcelocation1, bakeModel(modelblock1, ModelRotation.X0_Y0, false));
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 479 */       LOGGER.warn("Missing model for: " + resourcelocation);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<ResourceLocation> getVariantsTextureLocations() {
/* 486 */     Set<ResourceLocation> set = Sets.newHashSet();
/* 487 */     List<ModelResourceLocation> list = Lists.newArrayList(this.variants.keySet());
/* 488 */     Collections.sort(list, new Comparator<ModelResourceLocation>()
/*     */         {
/*     */           public int compare(ModelResourceLocation p_compare_1_, ModelResourceLocation p_compare_2_)
/*     */           {
/* 492 */             return p_compare_1_.toString().compareTo(p_compare_2_.toString());
/*     */           }
/*     */         });
/*     */     
/* 496 */     for (ModelResourceLocation modelresourcelocation : list) {
/*     */       
/* 498 */       ModelBlockDefinition.Variants modelblockdefinition$variants = this.variants.get(modelresourcelocation);
/*     */       
/* 500 */       for (ModelBlockDefinition.Variant modelblockdefinition$variant : modelblockdefinition$variants.getVariants()) {
/*     */         
/* 502 */         ModelBlock modelblock = this.models.get(modelblockdefinition$variant.getModelLocation());
/*     */         
/* 504 */         if (modelblock == null) {
/*     */           
/* 506 */           LOGGER.warn("Missing model for: " + modelresourcelocation);
/*     */           
/*     */           continue;
/*     */         } 
/* 510 */         set.addAll(getTextureLocations(modelblock));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 515 */     set.addAll(LOCATIONS_BUILTIN_TEXTURES);
/* 516 */     return set;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBakedModel bakeModel(ModelBlock modelBlockIn, ModelRotation modelRotationIn, boolean uvLocked) {
/* 521 */     return bakeModel(modelBlockIn, modelRotationIn, uvLocked);
/*     */   }
/*     */ 
/*     */   
/*     */   protected IBakedModel bakeModel(ModelBlock p_bakeModel_1_, ITransformation p_bakeModel_2_, boolean p_bakeModel_3_) {
/* 526 */     TextureAtlasSprite textureatlassprite = this.sprites.get(new ResourceLocation(p_bakeModel_1_.resolveTextureName("particle")));
/* 527 */     SimpleBakedModel.Builder simplebakedmodel$builder = (new SimpleBakedModel.Builder(p_bakeModel_1_)).setTexture(textureatlassprite);
/*     */     
/* 529 */     for (BlockPart blockpart : p_bakeModel_1_.getElements()) {
/*     */       
/* 531 */       for (EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
/*     */         
/* 533 */         BlockPartFace blockpartface = (BlockPartFace)blockpart.mapFaces.get(enumfacing);
/* 534 */         TextureAtlasSprite textureatlassprite1 = this.sprites.get(new ResourceLocation(p_bakeModel_1_.resolveTextureName(blockpartface.texture)));
/* 535 */         boolean flag = true;
/*     */         
/* 537 */         if (Reflector.ForgeHooksClient.exists())
/*     */         {
/* 539 */           flag = TRSRTransformation.isInteger(p_bakeModel_2_.getMatrix());
/*     */         }
/*     */         
/* 542 */         if (blockpartface.cullFace != null && flag) {
/*     */           
/* 544 */           simplebakedmodel$builder.addFaceQuad(p_bakeModel_2_.rotate(blockpartface.cullFace), makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, p_bakeModel_2_, p_bakeModel_3_));
/*     */           
/*     */           continue;
/*     */         } 
/* 548 */         simplebakedmodel$builder.addGeneralQuad(makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, p_bakeModel_2_, p_bakeModel_3_));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 553 */     return simplebakedmodel$builder.makeBakedModel();
/*     */   }
/*     */ 
/*     */   
/*     */   private BakedQuad makeBakedQuad(BlockPart p_177589_1_, BlockPartFace p_177589_2_, TextureAtlasSprite p_177589_3_, EnumFacing p_177589_4_, ModelRotation p_177589_5_, boolean p_177589_6_) {
/* 558 */     return Reflector.ForgeHooksClient.exists() ? makeBakedQuad(p_177589_1_, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_6_) : this.faceBakery.makeBakedQuad(p_177589_1_.positionFrom, p_177589_1_.positionTo, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.partRotation, p_177589_6_, p_177589_1_.shade);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BakedQuad makeBakedQuad(BlockPart p_makeBakedQuad_1_, BlockPartFace p_makeBakedQuad_2_, TextureAtlasSprite p_makeBakedQuad_3_, EnumFacing p_makeBakedQuad_4_, ITransformation p_makeBakedQuad_5_, boolean p_makeBakedQuad_6_) {
/* 563 */     return this.faceBakery.makeBakedQuad(p_makeBakedQuad_1_.positionFrom, p_makeBakedQuad_1_.positionTo, p_makeBakedQuad_2_, p_makeBakedQuad_3_, p_makeBakedQuad_4_, p_makeBakedQuad_5_, p_makeBakedQuad_1_.partRotation, p_makeBakedQuad_6_, p_makeBakedQuad_1_.shade);
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadModelsCheck() {
/* 568 */     loadModels();
/*     */     
/* 570 */     for (ModelBlock modelblock : this.models.values())
/*     */     {
/* 572 */       modelblock.getParentFromMap(this.models);
/*     */     }
/*     */     
/* 575 */     ModelBlock.checkModelHierarchy(this.models);
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadModels() {
/* 580 */     Deque<ResourceLocation> deque = Queues.newArrayDeque();
/* 581 */     Set<ResourceLocation> set = Sets.newHashSet();
/*     */     
/* 583 */     for (ResourceLocation resourcelocation : this.models.keySet()) {
/*     */       
/* 585 */       set.add(resourcelocation);
/* 586 */       ResourceLocation resourcelocation1 = ((ModelBlock)this.models.get(resourcelocation)).getParentLocation();
/*     */       
/* 588 */       if (resourcelocation1 != null)
/*     */       {
/* 590 */         deque.add(resourcelocation1);
/*     */       }
/*     */     } 
/*     */     
/* 594 */     while (!deque.isEmpty()) {
/*     */       
/* 596 */       ResourceLocation resourcelocation2 = deque.pop();
/*     */ 
/*     */       
/*     */       try {
/* 600 */         if (this.models.get(resourcelocation2) != null) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 605 */         ModelBlock modelblock = loadModel(resourcelocation2);
/* 606 */         this.models.put(resourcelocation2, modelblock);
/* 607 */         ResourceLocation resourcelocation3 = modelblock.getParentLocation();
/*     */         
/* 609 */         if (resourcelocation3 != null && !set.contains(resourcelocation3))
/*     */         {
/* 611 */           deque.add(resourcelocation3);
/*     */         }
/*     */       }
/* 614 */       catch (Exception var6) {
/*     */         
/* 616 */         LOGGER.warn("In parent chain: " + JOINER.join(getParentPath(resourcelocation2)) + "; unable to load model: '" + resourcelocation2 + "'");
/*     */       } 
/*     */       
/* 619 */       set.add(resourcelocation2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private List<ResourceLocation> getParentPath(ResourceLocation p_177573_1_) {
/* 625 */     List<ResourceLocation> list = Lists.newArrayList((Object[])new ResourceLocation[] { p_177573_1_ });
/* 626 */     ResourceLocation resourcelocation = p_177573_1_;
/*     */     
/* 628 */     while ((resourcelocation = getParentLocation(resourcelocation)) != null)
/*     */     {
/* 630 */       list.add(0, resourcelocation);
/*     */     }
/*     */     
/* 633 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private ResourceLocation getParentLocation(ResourceLocation p_177576_1_) {
/* 638 */     for (Map.Entry<ResourceLocation, ModelBlock> entry : this.models.entrySet()) {
/*     */       
/* 640 */       ModelBlock modelblock = entry.getValue();
/*     */       
/* 642 */       if (modelblock != null && p_177576_1_.equals(modelblock.getParentLocation()))
/*     */       {
/* 644 */         return entry.getKey();
/*     */       }
/*     */     } 
/*     */     
/* 648 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<ResourceLocation> getTextureLocations(ModelBlock p_177585_1_) {
/* 653 */     Set<ResourceLocation> set = Sets.newHashSet();
/*     */     
/* 655 */     for (BlockPart blockpart : p_177585_1_.getElements()) {
/*     */       
/* 657 */       for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
/*     */         
/* 659 */         ResourceLocation resourcelocation = new ResourceLocation(p_177585_1_.resolveTextureName(blockpartface.texture));
/* 660 */         set.add(resourcelocation);
/*     */       } 
/*     */     } 
/*     */     
/* 664 */     set.add(new ResourceLocation(p_177585_1_.resolveTextureName("particle")));
/* 665 */     return set;
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadSprites() {
/* 670 */     final Set<ResourceLocation> set = getVariantsTextureLocations();
/* 671 */     set.addAll(getItemsTextureLocations());
/* 672 */     set.remove(TextureMap.LOCATION_MISSING_TEXTURE);
/* 673 */     IIconCreator iiconcreator = new IIconCreator()
/*     */       {
/*     */         public void registerSprites(TextureMap iconRegistry)
/*     */         {
/* 677 */           for (ResourceLocation resourcelocation : set) {
/*     */             
/* 679 */             TextureAtlasSprite textureatlassprite = iconRegistry.registerSprite(resourcelocation);
/* 680 */             ModelBakery.this.sprites.put(resourcelocation, textureatlassprite);
/*     */           } 
/*     */         }
/*     */       };
/* 684 */     this.textureMap.loadSprites(this.resourceManager, iiconcreator);
/* 685 */     this.sprites.put(new ResourceLocation("missingno"), this.textureMap.getMissingSprite());
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<ResourceLocation> getItemsTextureLocations() {
/* 690 */     Set<ResourceLocation> set = Sets.newHashSet();
/*     */     
/* 692 */     for (ResourceLocation resourcelocation : this.itemLocations.values()) {
/*     */       
/* 694 */       ModelBlock modelblock = this.models.get(resourcelocation);
/*     */       
/* 696 */       if (modelblock != null) {
/*     */         
/* 698 */         set.add(new ResourceLocation(modelblock.resolveTextureName("particle")));
/*     */         
/* 700 */         if (hasItemModel(modelblock)) {
/*     */           
/* 702 */           for (String s : ItemModelGenerator.LAYERS) {
/*     */             
/* 704 */             ResourceLocation resourcelocation2 = new ResourceLocation(modelblock.resolveTextureName(s));
/*     */             
/* 706 */             if (modelblock.getRootModel() == MODEL_COMPASS && !TextureMap.LOCATION_MISSING_TEXTURE.equals(resourcelocation2)) {
/*     */               
/* 708 */               TextureAtlasSprite.setLocationNameCompass(resourcelocation2.toString());
/*     */             }
/* 710 */             else if (modelblock.getRootModel() == MODEL_CLOCK && !TextureMap.LOCATION_MISSING_TEXTURE.equals(resourcelocation2)) {
/*     */               
/* 712 */               TextureAtlasSprite.setLocationNameClock(resourcelocation2.toString());
/*     */             } 
/*     */             
/* 715 */             set.add(resourcelocation2);
/*     */           }  continue;
/*     */         } 
/* 718 */         if (!isCustomRenderer(modelblock))
/*     */         {
/* 720 */           for (BlockPart blockpart : modelblock.getElements()) {
/*     */             
/* 722 */             for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
/*     */               
/* 724 */               ResourceLocation resourcelocation1 = new ResourceLocation(modelblock.resolveTextureName(blockpartface.texture));
/* 725 */               set.add(resourcelocation1);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 732 */     return set;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasItemModel(ModelBlock p_177581_1_) {
/* 737 */     if (p_177581_1_ == null)
/*     */     {
/* 739 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 743 */     ModelBlock modelblock = p_177581_1_.getRootModel();
/* 744 */     return (modelblock == MODEL_GENERATED || modelblock == MODEL_COMPASS || modelblock == MODEL_CLOCK);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isCustomRenderer(ModelBlock p_177587_1_) {
/* 750 */     if (p_177587_1_ == null)
/*     */     {
/* 752 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 756 */     ModelBlock modelblock = p_177587_1_.getRootModel();
/* 757 */     return (modelblock == MODEL_ENTITY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void bakeItemModels() {
/* 763 */     for (ResourceLocation resourcelocation : this.itemLocations.values()) {
/*     */       
/* 765 */       ModelBlock modelblock = this.models.get(resourcelocation);
/*     */       
/* 767 */       if (hasItemModel(modelblock)) {
/*     */         
/* 769 */         ModelBlock modelblock1 = makeItemModel(modelblock);
/*     */         
/* 771 */         if (modelblock1 != null)
/*     */         {
/* 773 */           modelblock1.name = resourcelocation.toString();
/*     */         }
/*     */         
/* 776 */         this.models.put(resourcelocation, modelblock1); continue;
/*     */       } 
/* 778 */       if (isCustomRenderer(modelblock))
/*     */       {
/* 780 */         this.models.put(resourcelocation, modelblock);
/*     */       }
/*     */     } 
/*     */     
/* 784 */     for (TextureAtlasSprite textureatlassprite : this.sprites.values()) {
/*     */       
/* 786 */       if (!textureatlassprite.hasAnimationMetadata())
/*     */       {
/* 788 */         textureatlassprite.clearFramesTextureData();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ModelBlock makeItemModel(ModelBlock p_177582_1_) {
/* 795 */     return this.itemModelGenerator.makeItemModel(this.textureMap, p_177582_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelBlock getModelBlock(ResourceLocation p_getModelBlock_1_) {
/* 800 */     ModelBlock modelblock = this.models.get(p_getModelBlock_1_);
/* 801 */     return modelblock;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void fixModelLocations(ModelBlock p_fixModelLocations_0_, String p_fixModelLocations_1_) {
/* 806 */     ResourceLocation resourcelocation = fixModelLocation(p_fixModelLocations_0_.getParentLocation(), p_fixModelLocations_1_);
/*     */     
/* 808 */     if (resourcelocation != p_fixModelLocations_0_.getParentLocation())
/*     */     {
/* 810 */       Reflector.setFieldValue(p_fixModelLocations_0_, Reflector.ModelBlock_parentLocation, resourcelocation);
/*     */     }
/*     */     
/* 813 */     Map<String, String> map = (Map<String, String>)Reflector.getFieldValue(p_fixModelLocations_0_, Reflector.ModelBlock_textures);
/*     */     
/* 815 */     if (map != null)
/*     */     {
/* 817 */       for (Map.Entry<String, String> entry : map.entrySet()) {
/*     */         
/* 819 */         String s = entry.getValue();
/* 820 */         String s1 = fixResourcePath(s, p_fixModelLocations_1_);
/*     */         
/* 822 */         if (s1 != s)
/*     */         {
/* 824 */           entry.setValue(s1);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static ResourceLocation fixModelLocation(ResourceLocation p_fixModelLocation_0_, String p_fixModelLocation_1_) {
/* 832 */     if (p_fixModelLocation_0_ != null && p_fixModelLocation_1_ != null) {
/*     */       
/* 834 */       if (!p_fixModelLocation_0_.getResourceDomain().equals("minecraft"))
/*     */       {
/* 836 */         return p_fixModelLocation_0_;
/*     */       }
/*     */ 
/*     */       
/* 840 */       String s = p_fixModelLocation_0_.getResourcePath();
/* 841 */       String s1 = fixResourcePath(s, p_fixModelLocation_1_);
/*     */       
/* 843 */       if (s1 != s)
/*     */       {
/* 845 */         p_fixModelLocation_0_ = new ResourceLocation(p_fixModelLocation_0_.getResourceDomain(), s1);
/*     */       }
/*     */       
/* 848 */       return p_fixModelLocation_0_;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 853 */     return p_fixModelLocation_0_;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String fixResourcePath(String p_fixResourcePath_0_, String p_fixResourcePath_1_) {
/* 859 */     p_fixResourcePath_0_ = TextureUtils.fixResourcePath(p_fixResourcePath_0_, p_fixResourcePath_1_);
/* 860 */     p_fixResourcePath_0_ = StrUtils.removeSuffix(p_fixResourcePath_0_, ".json");
/* 861 */     p_fixResourcePath_0_ = StrUtils.removeSuffix(p_fixResourcePath_0_, ".png");
/* 862 */     return p_fixResourcePath_0_;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void addVariantName(Item p_addVariantName_0_, String... p_addVariantName_1_) {
/* 868 */     RegistryDelegate<Item> registrydelegate = (RegistryDelegate)Reflector.getFieldValue(p_addVariantName_0_, Reflector.ForgeItem_delegate);
/*     */     
/* 870 */     if (customVariantNames.containsKey(registrydelegate)) {
/*     */       
/* 872 */       ((Set)customVariantNames.get(registrydelegate)).addAll(Lists.newArrayList((Object[])p_addVariantName_1_));
/*     */     }
/*     */     else {
/*     */       
/* 876 */       customVariantNames.put(registrydelegate, Sets.newHashSet((Object[])p_addVariantName_1_));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends ResourceLocation> void registerItemVariants(Item p_registerItemVariants_0_, T... p_registerItemVariants_1_) {
/* 882 */     RegistryDelegate<Item> registrydelegate = (RegistryDelegate)Reflector.getFieldValue(p_registerItemVariants_0_, Reflector.ForgeItem_delegate);
/*     */     
/* 884 */     if (!customVariantNames.containsKey(registrydelegate))
/*     */     {
/* 886 */       customVariantNames.put(registrydelegate, Sets.newHashSet());
/*     */     }
/*     */     
/* 889 */     for (T t : p_registerItemVariants_1_)
/*     */     {
/* 891 */       ((Set<String>)customVariantNames.get(registrydelegate)).add(t.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 897 */     BUILT_IN_MODELS.put("missing", "{ \"textures\": {   \"particle\": \"missingno\",   \"missingno\": \"missingno\"}, \"elements\": [ {     \"from\": [ 0, 0, 0 ],     \"to\": [ 16, 16, 16 ],     \"faces\": {         \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"down\", \"texture\": \"#missingno\" },         \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"up\", \"texture\": \"#missingno\" },         \"north\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"north\", \"texture\": \"#missingno\" },         \"south\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"south\", \"texture\": \"#missingno\" },         \"west\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"west\", \"texture\": \"#missingno\" },         \"east\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"east\", \"texture\": \"#missingno\" }    }}]}");
/* 898 */     MODEL_GENERATED.name = "generation marker";
/* 899 */     MODEL_COMPASS.name = "compass generation marker";
/* 900 */     MODEL_CLOCK.name = "class generation marker";
/* 901 */     MODEL_ENTITY.name = "block entity marker";
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\model\ModelBakery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */