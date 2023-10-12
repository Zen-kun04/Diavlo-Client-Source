/*      */ package net.optifine.config;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockDoublePlant;
/*      */ import net.minecraft.block.properties.IProperty;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.item.EnumDyeColor;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.src.Config;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumWorldBlockLayer;
/*      */ import net.minecraft.util.IStringSerializable;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ 
/*      */ public class ConnectedParser {
/*   24 */   private String context = null;
/*   25 */   public static final VillagerProfession[] PROFESSIONS_INVALID = new VillagerProfession[0];
/*   26 */   public static final EnumDyeColor[] DYE_COLORS_INVALID = new EnumDyeColor[0];
/*   27 */   private static final INameGetter<Enum> NAME_GETTER_ENUM = new INameGetter<Enum>()
/*      */     {
/*      */       public String getName(Enum en)
/*      */       {
/*   31 */         return en.name();
/*      */       }
/*      */     };
/*   34 */   private static final INameGetter<EnumDyeColor> NAME_GETTER_DYE_COLOR = new INameGetter<EnumDyeColor>()
/*      */     {
/*      */       public String getName(EnumDyeColor col)
/*      */       {
/*   38 */         return col.getName();
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*      */   public ConnectedParser(String context) {
/*   44 */     this.context = context;
/*      */   }
/*      */ 
/*      */   
/*      */   public String parseName(String path) {
/*   49 */     String s = path;
/*   50 */     int i = path.lastIndexOf('/');
/*      */     
/*   52 */     if (i >= 0)
/*      */     {
/*   54 */       s = path.substring(i + 1);
/*      */     }
/*      */     
/*   57 */     int j = s.lastIndexOf('.');
/*      */     
/*   59 */     if (j >= 0)
/*      */     {
/*   61 */       s = s.substring(0, j);
/*      */     }
/*      */     
/*   64 */     return s;
/*      */   }
/*      */ 
/*      */   
/*      */   public String parseBasePath(String path) {
/*   69 */     int i = path.lastIndexOf('/');
/*   70 */     return (i < 0) ? "" : path.substring(0, i);
/*      */   }
/*      */ 
/*      */   
/*      */   public MatchBlock[] parseMatchBlocks(String propMatchBlocks) {
/*   75 */     if (propMatchBlocks == null)
/*      */     {
/*   77 */       return null;
/*      */     }
/*      */ 
/*      */     
/*   81 */     List list = new ArrayList();
/*   82 */     String[] astring = Config.tokenize(propMatchBlocks, " ");
/*      */     
/*   84 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*   86 */       String s = astring[i];
/*   87 */       MatchBlock[] amatchblock = parseMatchBlock(s);
/*      */       
/*   89 */       if (amatchblock != null)
/*      */       {
/*   91 */         list.addAll(Arrays.asList(amatchblock));
/*      */       }
/*      */     } 
/*      */     
/*   95 */     MatchBlock[] amatchblock1 = (MatchBlock[])list.toArray((Object[])new MatchBlock[list.size()]);
/*   96 */     return amatchblock1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public IBlockState parseBlockState(String str, IBlockState def) {
/*  102 */     MatchBlock[] amatchblock = parseMatchBlock(str);
/*      */     
/*  104 */     if (amatchblock == null)
/*      */     {
/*  106 */       return def;
/*      */     }
/*  108 */     if (amatchblock.length != 1)
/*      */     {
/*  110 */       return def;
/*      */     }
/*      */ 
/*      */     
/*  114 */     MatchBlock matchblock = amatchblock[0];
/*  115 */     int i = matchblock.getBlockId();
/*  116 */     Block block = Block.getBlockById(i);
/*  117 */     return block.getDefaultState();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public MatchBlock[] parseMatchBlock(String blockStr) {
/*  123 */     if (blockStr == null)
/*      */     {
/*  125 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  129 */     blockStr = blockStr.trim();
/*      */     
/*  131 */     if (blockStr.length() <= 0)
/*      */     {
/*  133 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  137 */     String[] astring = Config.tokenize(blockStr, ":");
/*  138 */     String s = "minecraft";
/*  139 */     int i = 0;
/*      */     
/*  141 */     if (astring.length > 1 && isFullBlockName(astring)) {
/*      */       
/*  143 */       s = astring[0];
/*  144 */       i = 1;
/*      */     }
/*      */     else {
/*      */       
/*  148 */       s = "minecraft";
/*  149 */       i = 0;
/*      */     } 
/*      */     
/*  152 */     String s1 = astring[i];
/*  153 */     String[] astring1 = Arrays.<String>copyOfRange(astring, i + 1, astring.length);
/*  154 */     Block[] ablock = parseBlockPart(s, s1);
/*      */     
/*  156 */     if (ablock == null)
/*      */     {
/*  158 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  162 */     MatchBlock[] amatchblock = new MatchBlock[ablock.length];
/*      */     
/*  164 */     for (int j = 0; j < ablock.length; j++) {
/*      */       
/*  166 */       Block block = ablock[j];
/*  167 */       int k = Block.getIdFromBlock(block);
/*  168 */       int[] aint = null;
/*      */       
/*  170 */       if (astring1.length > 0) {
/*      */         
/*  172 */         aint = parseBlockMetadatas(block, astring1);
/*      */         
/*  174 */         if (aint == null)
/*      */         {
/*  176 */           return null;
/*      */         }
/*      */       } 
/*      */       
/*  180 */       MatchBlock matchblock = new MatchBlock(k, aint);
/*  181 */       amatchblock[j] = matchblock;
/*      */     } 
/*      */     
/*  184 */     return amatchblock;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFullBlockName(String[] parts) {
/*  192 */     if (parts.length < 2)
/*      */     {
/*  194 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  198 */     String s = parts[1];
/*  199 */     return (s.length() < 1) ? false : (startsWithDigit(s) ? false : (!s.contains("=")));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean startsWithDigit(String str) {
/*  205 */     if (str == null)
/*      */     {
/*  207 */       return false;
/*      */     }
/*  209 */     if (str.length() < 1)
/*      */     {
/*  211 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  215 */     char c0 = str.charAt(0);
/*  216 */     return Character.isDigit(c0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Block[] parseBlockPart(String domain, String blockPart) {
/*  222 */     if (startsWithDigit(blockPart)) {
/*      */       
/*  224 */       int[] aint = parseIntList(blockPart);
/*      */       
/*  226 */       if (aint == null)
/*      */       {
/*  228 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  232 */       Block[] ablock1 = new Block[aint.length];
/*      */       
/*  234 */       for (int j = 0; j < aint.length; j++) {
/*      */         
/*  236 */         int i = aint[j];
/*  237 */         Block block1 = Block.getBlockById(i);
/*      */         
/*  239 */         if (block1 == null) {
/*      */           
/*  241 */           warn("Block not found for id: " + i);
/*  242 */           return null;
/*      */         } 
/*      */         
/*  245 */         ablock1[j] = block1;
/*      */       } 
/*      */       
/*  248 */       return ablock1;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  253 */     String s = domain + ":" + blockPart;
/*  254 */     Block block = Block.getBlockFromName(s);
/*      */     
/*  256 */     if (block == null) {
/*      */       
/*  258 */       warn("Block not found for name: " + s);
/*  259 */       return null;
/*      */     } 
/*      */ 
/*      */     
/*  263 */     Block[] ablock = { block };
/*  264 */     return ablock;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] parseBlockMetadatas(Block block, String[] params) {
/*  271 */     if (params.length <= 0)
/*      */     {
/*  273 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  277 */     String s = params[0];
/*      */     
/*  279 */     if (startsWithDigit(s)) {
/*      */       
/*  281 */       int[] aint = parseIntList(s);
/*  282 */       return aint;
/*      */     } 
/*      */ 
/*      */     
/*  286 */     IBlockState iblockstate = block.getDefaultState();
/*  287 */     Collection collection = iblockstate.getPropertyNames();
/*  288 */     Map<IProperty, List<Comparable>> map = new HashMap<>();
/*      */     
/*  290 */     for (int i = 0; i < params.length; i++) {
/*      */       
/*  292 */       String s1 = params[i];
/*      */       
/*  294 */       if (s1.length() > 0) {
/*      */         
/*  296 */         String[] astring = Config.tokenize(s1, "=");
/*      */         
/*  298 */         if (astring.length != 2) {
/*      */           
/*  300 */           warn("Invalid block property: " + s1);
/*  301 */           return null;
/*      */         } 
/*      */         
/*  304 */         String s2 = astring[0];
/*  305 */         String s3 = astring[1];
/*  306 */         IProperty iproperty = ConnectedProperties.getProperty(s2, collection);
/*      */         
/*  308 */         if (iproperty == null) {
/*      */           
/*  310 */           warn("Property not found: " + s2 + ", block: " + block);
/*  311 */           return null;
/*      */         } 
/*      */         
/*  314 */         List<Comparable> list = map.get(s2);
/*      */         
/*  316 */         if (list == null) {
/*      */           
/*  318 */           list = new ArrayList<>();
/*  319 */           map.put(iproperty, list);
/*      */         } 
/*      */         
/*  322 */         String[] astring1 = Config.tokenize(s3, ",");
/*      */         
/*  324 */         for (int j = 0; j < astring1.length; j++) {
/*      */           
/*  326 */           String s4 = astring1[j];
/*  327 */           Comparable comparable = parsePropertyValue(iproperty, s4);
/*      */           
/*  329 */           if (comparable == null) {
/*      */             
/*  331 */             warn("Property value not found: " + s4 + ", property: " + s2 + ", block: " + block);
/*  332 */             return null;
/*      */           } 
/*      */           
/*  335 */           list.add(comparable);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  340 */     if (map.isEmpty())
/*      */     {
/*  342 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  346 */     List<Integer> list1 = new ArrayList<>();
/*      */     
/*  348 */     for (int k = 0; k < 16; k++) {
/*      */       
/*  350 */       int l = k;
/*      */ 
/*      */       
/*      */       try {
/*  354 */         IBlockState iblockstate1 = getStateFromMeta(block, l);
/*      */         
/*  356 */         if (matchState(iblockstate1, map))
/*      */         {
/*  358 */           list1.add(Integer.valueOf(l));
/*      */         }
/*      */       }
/*  361 */       catch (IllegalArgumentException illegalArgumentException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  367 */     if (list1.size() == 16)
/*      */     {
/*  369 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  373 */     int[] aint1 = new int[list1.size()];
/*      */     
/*  375 */     for (int i1 = 0; i1 < aint1.length; i1++)
/*      */     {
/*  377 */       aint1[i1] = ((Integer)list1.get(i1)).intValue();
/*      */     }
/*      */     
/*  380 */     return aint1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IBlockState getStateFromMeta(Block block, int md) {
/*      */     try {
/*  391 */       IBlockState iblockstate = block.getStateFromMeta(md);
/*      */       
/*  393 */       if (block == Blocks.double_plant && md > 7) {
/*      */         
/*  395 */         IBlockState iblockstate1 = block.getStateFromMeta(md & 0x7);
/*  396 */         iblockstate = iblockstate.withProperty((IProperty)BlockDoublePlant.VARIANT, iblockstate1.getValue((IProperty)BlockDoublePlant.VARIANT));
/*      */       } 
/*      */       
/*  399 */       return iblockstate;
/*      */     }
/*  401 */     catch (IllegalArgumentException var5) {
/*      */       
/*  403 */       return block.getDefaultState();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static Comparable parsePropertyValue(IProperty prop, String valStr) {
/*  409 */     Class<?> oclass = prop.getValueClass();
/*  410 */     Comparable comparable = parseValue(valStr, oclass);
/*      */     
/*  412 */     if (comparable == null) {
/*      */       
/*  414 */       Collection collection = prop.getAllowedValues();
/*  415 */       comparable = getPropertyValue(valStr, collection);
/*      */     } 
/*      */     
/*  418 */     return comparable;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Comparable getPropertyValue(String value, Collection propertyValues) {
/*  423 */     for (Object o : propertyValues) {
/*      */       
/*  425 */       Comparable comparable = (Comparable)o;
/*  426 */       if (getValueName(comparable).equals(value))
/*      */       {
/*  428 */         return comparable;
/*      */       }
/*      */     } 
/*      */     
/*  432 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static Object getValueName(Comparable obj) {
/*  437 */     if (obj instanceof IStringSerializable) {
/*      */       
/*  439 */       IStringSerializable istringserializable = (IStringSerializable)obj;
/*  440 */       return istringserializable.getName();
/*      */     } 
/*      */ 
/*      */     
/*  444 */     return obj.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Comparable parseValue(String str, Class<?> cls) {
/*  450 */     if (cls == String.class) {
/*  451 */       return str;
/*      */     }
/*  453 */     if (cls == Boolean.class) {
/*  454 */       return Boolean.valueOf(str);
/*      */     }
/*  456 */     if (cls == Float.class) {
/*  457 */       return Float.valueOf(str);
/*      */     }
/*  459 */     if (cls == Double.class) {
/*  460 */       return Double.valueOf(str);
/*      */     }
/*  462 */     if (cls == Integer.class) {
/*  463 */       return Integer.valueOf(str);
/*      */     }
/*  465 */     if (cls == Long.class) {
/*  466 */       return Long.valueOf(str);
/*      */     }
/*  468 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchState(IBlockState bs, Map<IProperty, List<Comparable>> mapPropValues) {
/*  474 */     for (IProperty iproperty : mapPropValues.keySet()) {
/*      */       
/*  476 */       List<Comparable> list = mapPropValues.get(iproperty);
/*  477 */       Comparable comparable = bs.getValue(iproperty);
/*      */       
/*  479 */       if (comparable == null)
/*      */       {
/*  481 */         return false;
/*      */       }
/*      */       
/*  484 */       if (!list.contains(comparable))
/*      */       {
/*  486 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  490 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public BiomeGenBase[] parseBiomes(String str) {
/*  495 */     if (str == null)
/*      */     {
/*  497 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  501 */     str = str.trim();
/*  502 */     boolean flag = false;
/*      */     
/*  504 */     if (str.startsWith("!")) {
/*      */       
/*  506 */       flag = true;
/*  507 */       str = str.substring(1);
/*      */     } 
/*      */     
/*  510 */     String[] astring = Config.tokenize(str, " ");
/*  511 */     List<BiomeGenBase> list = new ArrayList();
/*      */     
/*  513 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*  515 */       String s = astring[i];
/*  516 */       BiomeGenBase biomegenbase = findBiome(s);
/*      */       
/*  518 */       if (biomegenbase == null) {
/*      */         
/*  520 */         warn("Biome not found: " + s);
/*      */       }
/*      */       else {
/*      */         
/*  524 */         list.add(biomegenbase);
/*      */       } 
/*      */     } 
/*      */     
/*  528 */     if (flag) {
/*      */       
/*  530 */       List<BiomeGenBase> list1 = new ArrayList<>(Arrays.asList(BiomeGenBase.getBiomeGenArray()));
/*  531 */       list1.removeAll(list);
/*  532 */       list = list1;
/*      */     } 
/*      */     
/*  535 */     BiomeGenBase[] abiomegenbase = list.<BiomeGenBase>toArray(new BiomeGenBase[list.size()]);
/*  536 */     return abiomegenbase;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public BiomeGenBase findBiome(String biomeName) {
/*  542 */     biomeName = biomeName.toLowerCase();
/*      */     
/*  544 */     if (biomeName.equals("nether"))
/*      */     {
/*  546 */       return BiomeGenBase.hell;
/*      */     }
/*      */ 
/*      */     
/*  550 */     BiomeGenBase[] abiomegenbase = BiomeGenBase.getBiomeGenArray();
/*      */     
/*  552 */     for (int i = 0; i < abiomegenbase.length; i++) {
/*      */       
/*  554 */       BiomeGenBase biomegenbase = abiomegenbase[i];
/*      */       
/*  556 */       if (biomegenbase != null) {
/*      */         
/*  558 */         String s = biomegenbase.biomeName.replace(" ", "").toLowerCase();
/*      */         
/*  560 */         if (s.equals(biomeName))
/*      */         {
/*  562 */           return biomegenbase;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  567 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int parseInt(String str, int defVal) {
/*  573 */     if (str == null)
/*      */     {
/*  575 */       return defVal;
/*      */     }
/*      */ 
/*      */     
/*  579 */     str = str.trim();
/*  580 */     int i = Config.parseInt(str, -1);
/*      */     
/*  582 */     if (i < 0) {
/*      */       
/*  584 */       warn("Invalid number: " + str);
/*  585 */       return defVal;
/*      */     } 
/*      */ 
/*      */     
/*  589 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] parseIntList(String str) {
/*  596 */     if (str == null)
/*      */     {
/*  598 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  602 */     List<Integer> list = new ArrayList<>();
/*  603 */     String[] astring = Config.tokenize(str, " ,");
/*      */     
/*  605 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*  607 */       String s = astring[i];
/*      */       
/*  609 */       if (s.contains("-")) {
/*      */         
/*  611 */         String[] astring1 = Config.tokenize(s, "-");
/*      */         
/*  613 */         if (astring1.length != 2) {
/*      */           
/*  615 */           warn("Invalid interval: " + s + ", when parsing: " + str);
/*      */         }
/*      */         else {
/*      */           
/*  619 */           int k = Config.parseInt(astring1[0], -1);
/*  620 */           int l = Config.parseInt(astring1[1], -1);
/*      */           
/*  622 */           if (k >= 0 && l >= 0 && k <= l)
/*      */           {
/*  624 */             for (int i1 = k; i1 <= l; i1++)
/*      */             {
/*  626 */               list.add(Integer.valueOf(i1));
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  631 */             warn("Invalid interval: " + s + ", when parsing: " + str);
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/*  637 */         int j = Config.parseInt(s, -1);
/*      */         
/*  639 */         if (j < 0) {
/*      */           
/*  641 */           warn("Invalid number: " + s + ", when parsing: " + str);
/*      */         }
/*      */         else {
/*      */           
/*  645 */           list.add(Integer.valueOf(j));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  650 */     int[] aint = new int[list.size()];
/*      */     
/*  652 */     for (int j1 = 0; j1 < aint.length; j1++)
/*      */     {
/*  654 */       aint[j1] = ((Integer)list.get(j1)).intValue();
/*      */     }
/*      */     
/*  657 */     return aint;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean[] parseFaces(String str, boolean[] defVal) {
/*  663 */     if (str == null)
/*      */     {
/*  665 */       return defVal;
/*      */     }
/*      */ 
/*      */     
/*  669 */     EnumSet<EnumFacing> enumset = EnumSet.allOf(EnumFacing.class);
/*  670 */     String[] astring = Config.tokenize(str, " ,");
/*      */     
/*  672 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*  674 */       String s = astring[i];
/*      */       
/*  676 */       if (s.equals("sides")) {
/*      */         
/*  678 */         enumset.add(EnumFacing.NORTH);
/*  679 */         enumset.add(EnumFacing.SOUTH);
/*  680 */         enumset.add(EnumFacing.WEST);
/*  681 */         enumset.add(EnumFacing.EAST);
/*      */       }
/*  683 */       else if (s.equals("all")) {
/*      */         
/*  685 */         enumset.addAll(Arrays.asList(EnumFacing.VALUES));
/*      */       }
/*      */       else {
/*      */         
/*  689 */         EnumFacing enumfacing = parseFace(s);
/*      */         
/*  691 */         if (enumfacing != null)
/*      */         {
/*  693 */           enumset.add(enumfacing);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  698 */     boolean[] aboolean = new boolean[EnumFacing.VALUES.length];
/*      */     
/*  700 */     for (int j = 0; j < aboolean.length; j++)
/*      */     {
/*  702 */       aboolean[j] = enumset.contains(EnumFacing.VALUES[j]);
/*      */     }
/*      */     
/*  705 */     return aboolean;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public EnumFacing parseFace(String str) {
/*  711 */     str = str.toLowerCase();
/*      */     
/*  713 */     if (!str.equals("bottom") && !str.equals("down")) {
/*      */       
/*  715 */       if (!str.equals("top") && !str.equals("up")) {
/*      */         
/*  717 */         if (str.equals("north"))
/*      */         {
/*  719 */           return EnumFacing.NORTH;
/*      */         }
/*  721 */         if (str.equals("south"))
/*      */         {
/*  723 */           return EnumFacing.SOUTH;
/*      */         }
/*  725 */         if (str.equals("east"))
/*      */         {
/*  727 */           return EnumFacing.EAST;
/*      */         }
/*  729 */         if (str.equals("west"))
/*      */         {
/*  731 */           return EnumFacing.WEST;
/*      */         }
/*      */ 
/*      */         
/*  735 */         Config.warn("Unknown face: " + str);
/*  736 */         return null;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  741 */       return EnumFacing.UP;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  746 */     return EnumFacing.DOWN;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void dbg(String str) {
/*  752 */     Config.dbg("" + this.context + ": " + str);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String str) {
/*  757 */     Config.warn("" + this.context + ": " + str);
/*      */   }
/*      */ 
/*      */   
/*      */   public RangeListInt parseRangeListInt(String str) {
/*  762 */     if (str == null)
/*      */     {
/*  764 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  768 */     RangeListInt rangelistint = new RangeListInt();
/*  769 */     String[] astring = Config.tokenize(str, " ,");
/*      */     
/*  771 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*  773 */       String s = astring[i];
/*  774 */       RangeInt rangeint = parseRangeInt(s);
/*      */       
/*  776 */       if (rangeint == null)
/*      */       {
/*  778 */         return null;
/*      */       }
/*      */       
/*  781 */       rangelistint.addRange(rangeint);
/*      */     } 
/*      */     
/*  784 */     return rangelistint;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private RangeInt parseRangeInt(String str) {
/*  790 */     if (str == null)
/*      */     {
/*  792 */       return null;
/*      */     }
/*  794 */     if (str.indexOf('-') >= 0) {
/*      */       
/*  796 */       String[] astring = Config.tokenize(str, "-");
/*      */       
/*  798 */       if (astring.length != 2) {
/*      */         
/*  800 */         warn("Invalid range: " + str);
/*  801 */         return null;
/*      */       } 
/*      */ 
/*      */       
/*  805 */       int j = Config.parseInt(astring[0], -1);
/*  806 */       int k = Config.parseInt(astring[1], -1);
/*      */       
/*  808 */       if (j >= 0 && k >= 0)
/*      */       {
/*  810 */         return new RangeInt(j, k);
/*      */       }
/*      */ 
/*      */       
/*  814 */       warn("Invalid range: " + str);
/*  815 */       return null;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  821 */     int i = Config.parseInt(str, -1);
/*      */     
/*  823 */     if (i < 0) {
/*      */       
/*  825 */       warn("Invalid integer: " + str);
/*  826 */       return null;
/*      */     } 
/*      */ 
/*      */     
/*  830 */     return new RangeInt(i, i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean parseBoolean(String str, boolean defVal) {
/*  837 */     if (str == null)
/*      */     {
/*  839 */       return defVal;
/*      */     }
/*      */ 
/*      */     
/*  843 */     String s = str.toLowerCase().trim();
/*      */     
/*  845 */     if (s.equals("true"))
/*      */     {
/*  847 */       return true;
/*      */     }
/*  849 */     if (s.equals("false"))
/*      */     {
/*  851 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  855 */     warn("Invalid boolean: " + str);
/*  856 */     return defVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean parseBooleanObject(String str) {
/*  863 */     if (str == null)
/*      */     {
/*  865 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  869 */     String s = str.toLowerCase().trim();
/*      */     
/*  871 */     if (s.equals("true"))
/*      */     {
/*  873 */       return Boolean.TRUE;
/*      */     }
/*  875 */     if (s.equals("false"))
/*      */     {
/*  877 */       return Boolean.FALSE;
/*      */     }
/*      */ 
/*      */     
/*  881 */     warn("Invalid boolean: " + str);
/*  882 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int parseColor(String str, int defVal) {
/*  889 */     if (str == null)
/*      */     {
/*  891 */       return defVal;
/*      */     }
/*      */ 
/*      */     
/*  895 */     str = str.trim();
/*      */ 
/*      */     
/*      */     try {
/*  899 */       int i = Integer.parseInt(str, 16) & 0xFFFFFF;
/*  900 */       return i;
/*      */     }
/*  902 */     catch (NumberFormatException var3) {
/*      */       
/*  904 */       return defVal;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int parseColor4(String str, int defVal) {
/*  911 */     if (str == null)
/*      */     {
/*  913 */       return defVal;
/*      */     }
/*      */ 
/*      */     
/*  917 */     str = str.trim();
/*      */ 
/*      */     
/*      */     try {
/*  921 */       int i = (int)(Long.parseLong(str, 16) & 0xFFFFFFFFFFFFFFFFL);
/*  922 */       return i;
/*      */     }
/*  924 */     catch (NumberFormatException var3) {
/*      */       
/*  926 */       return defVal;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public EnumWorldBlockLayer parseBlockRenderLayer(String str, EnumWorldBlockLayer def) {
/*  933 */     if (str == null)
/*      */     {
/*  935 */       return def;
/*      */     }
/*      */ 
/*      */     
/*  939 */     str = str.toLowerCase().trim();
/*  940 */     EnumWorldBlockLayer[] aenumworldblocklayer = EnumWorldBlockLayer.values();
/*      */     
/*  942 */     for (int i = 0; i < aenumworldblocklayer.length; i++) {
/*      */       
/*  944 */       EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[i];
/*      */       
/*  946 */       if (str.equals(enumworldblocklayer.name().toLowerCase()))
/*      */       {
/*  948 */         return enumworldblocklayer;
/*      */       }
/*      */     } 
/*      */     
/*  952 */     return def;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T parseObject(String str, T[] objs, INameGetter<T> nameGetter, String property) {
/*  958 */     if (str == null)
/*      */     {
/*  960 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  964 */     String s = str.toLowerCase().trim();
/*      */     
/*  966 */     for (int i = 0; i < objs.length; i++) {
/*      */       
/*  968 */       T t = objs[i];
/*  969 */       String s1 = nameGetter.getName(t);
/*      */       
/*  971 */       if (s1 != null && s1.toLowerCase().equals(s))
/*      */       {
/*  973 */         return t;
/*      */       }
/*      */     } 
/*      */     
/*  977 */     warn("Invalid " + property + ": " + str);
/*  978 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T[] parseObjects(String str, T[] objs, INameGetter nameGetter, String property, T[] errValue) {
/*  984 */     if (str == null)
/*      */     {
/*  986 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  990 */     str = str.toLowerCase().trim();
/*  991 */     String[] astring = Config.tokenize(str, " ");
/*  992 */     T[] at = (T[])Array.newInstance(objs.getClass().getComponentType(), astring.length);
/*      */     
/*  994 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*  996 */       String s = astring[i];
/*  997 */       T t = parseObject(s, objs, nameGetter, property);
/*      */       
/*  999 */       if (t == null)
/*      */       {
/* 1001 */         return errValue;
/*      */       }
/*      */       
/* 1004 */       at[i] = t;
/*      */     } 
/*      */     
/* 1007 */     return at;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Enum parseEnum(String str, Enum[] enums, String property) {
/* 1013 */     return parseObject(str, enums, NAME_GETTER_ENUM, property);
/*      */   }
/*      */ 
/*      */   
/*      */   public Enum[] parseEnums(String str, Enum[] enums, String property, Enum[] errValue) {
/* 1018 */     return parseObjects(str, enums, NAME_GETTER_ENUM, property, errValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public EnumDyeColor[] parseDyeColors(String str, String property, EnumDyeColor[] errValue) {
/* 1023 */     return parseObjects(str, EnumDyeColor.values(), NAME_GETTER_DYE_COLOR, property, errValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public Weather[] parseWeather(String str, String property, Weather[] errValue) {
/* 1028 */     return parseObjects(str, Weather.values(), NAME_GETTER_ENUM, property, errValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public NbtTagValue parseNbtTagValue(String path, String value) {
/* 1033 */     return (path != null && value != null) ? new NbtTagValue(path, value) : null;
/*      */   }
/*      */ 
/*      */   
/*      */   public VillagerProfession[] parseProfessions(String profStr) {
/* 1038 */     if (profStr == null)
/*      */     {
/* 1040 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1044 */     List<VillagerProfession> list = new ArrayList<>();
/* 1045 */     String[] astring = Config.tokenize(profStr, " ");
/*      */     
/* 1047 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/* 1049 */       String s = astring[i];
/* 1050 */       VillagerProfession villagerprofession = parseProfession(s);
/*      */       
/* 1052 */       if (villagerprofession == null) {
/*      */         
/* 1054 */         warn("Invalid profession: " + s);
/* 1055 */         return PROFESSIONS_INVALID;
/*      */       } 
/*      */       
/* 1058 */       list.add(villagerprofession);
/*      */     } 
/*      */     
/* 1061 */     if (list.isEmpty())
/*      */     {
/* 1063 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1067 */     VillagerProfession[] avillagerprofession = list.<VillagerProfession>toArray(new VillagerProfession[list.size()]);
/* 1068 */     return avillagerprofession;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private VillagerProfession parseProfession(String str) {
/* 1075 */     str = str.toLowerCase();
/* 1076 */     String[] astring = Config.tokenize(str, ":");
/*      */     
/* 1078 */     if (astring.length > 2)
/*      */     {
/* 1080 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1084 */     String s = astring[0];
/* 1085 */     String s1 = null;
/*      */     
/* 1087 */     if (astring.length > 1)
/*      */     {
/* 1089 */       s1 = astring[1];
/*      */     }
/*      */     
/* 1092 */     int i = parseProfessionId(s);
/*      */     
/* 1094 */     if (i < 0)
/*      */     {
/* 1096 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1100 */     int[] aint = null;
/*      */     
/* 1102 */     if (s1 != null) {
/*      */       
/* 1104 */       aint = parseCareerIds(i, s1);
/*      */       
/* 1106 */       if (aint == null)
/*      */       {
/* 1108 */         return null;
/*      */       }
/*      */     } 
/*      */     
/* 1112 */     return new VillagerProfession(i, aint);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int parseProfessionId(String str) {
/* 1119 */     int i = Config.parseInt(str, -1);
/* 1120 */     return (i >= 0) ? i : (str.equals("farmer") ? 0 : (str.equals("librarian") ? 1 : (str.equals("priest") ? 2 : (str.equals("blacksmith") ? 3 : (str.equals("butcher") ? 4 : (str.equals("nitwit") ? 5 : -1))))));
/*      */   }
/*      */ 
/*      */   
/*      */   private static int[] parseCareerIds(int prof, String str) {
/* 1125 */     Set<Integer> set = new HashSet<>();
/* 1126 */     String[] astring = Config.tokenize(str, ",");
/*      */     
/* 1128 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/* 1130 */       String s = astring[i];
/* 1131 */       int j = parseCareerId(prof, s);
/*      */       
/* 1133 */       if (j < 0)
/*      */       {
/* 1135 */         return null;
/*      */       }
/*      */       
/* 1138 */       set.add(Integer.valueOf(j));
/*      */     } 
/*      */     
/* 1141 */     Integer[] ainteger = set.<Integer>toArray(new Integer[set.size()]);
/* 1142 */     int[] aint = new int[ainteger.length];
/*      */     
/* 1144 */     for (int k = 0; k < aint.length; k++)
/*      */     {
/* 1146 */       aint[k] = ainteger[k].intValue();
/*      */     }
/*      */     
/* 1149 */     return aint;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int parseCareerId(int prof, String str) {
/* 1154 */     int i = Config.parseInt(str, -1);
/*      */     
/* 1156 */     if (i >= 0)
/*      */     {
/* 1158 */       return i;
/*      */     }
/*      */ 
/*      */     
/* 1162 */     if (prof == 0) {
/*      */       
/* 1164 */       if (str.equals("farmer"))
/*      */       {
/* 1166 */         return 1;
/*      */       }
/*      */       
/* 1169 */       if (str.equals("fisherman"))
/*      */       {
/* 1171 */         return 2;
/*      */       }
/*      */       
/* 1174 */       if (str.equals("shepherd"))
/*      */       {
/* 1176 */         return 3;
/*      */       }
/*      */       
/* 1179 */       if (str.equals("fletcher"))
/*      */       {
/* 1181 */         return 4;
/*      */       }
/*      */     } 
/*      */     
/* 1185 */     if (prof == 1) {
/*      */       
/* 1187 */       if (str.equals("librarian"))
/*      */       {
/* 1189 */         return 1;
/*      */       }
/*      */       
/* 1192 */       if (str.equals("cartographer"))
/*      */       {
/* 1194 */         return 2;
/*      */       }
/*      */     } 
/*      */     
/* 1198 */     if (prof == 2 && str.equals("cleric"))
/*      */     {
/* 1200 */       return 1;
/*      */     }
/*      */ 
/*      */     
/* 1204 */     if (prof == 3) {
/*      */       
/* 1206 */       if (str.equals("armor"))
/*      */       {
/* 1208 */         return 1;
/*      */       }
/*      */       
/* 1211 */       if (str.equals("weapon"))
/*      */       {
/* 1213 */         return 2;
/*      */       }
/*      */       
/* 1216 */       if (str.equals("tool"))
/*      */       {
/* 1218 */         return 3;
/*      */       }
/*      */     } 
/*      */     
/* 1222 */     if (prof == 4) {
/*      */       
/* 1224 */       if (str.equals("butcher"))
/*      */       {
/* 1226 */         return 1;
/*      */       }
/*      */       
/* 1229 */       if (str.equals("leather"))
/*      */       {
/* 1231 */         return 2;
/*      */       }
/*      */     } 
/*      */     
/* 1235 */     return (prof == 5 && str.equals("nitwit")) ? 1 : -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] parseItems(String str) {
/* 1242 */     str = str.trim();
/* 1243 */     Set<Integer> set = new TreeSet<>();
/* 1244 */     String[] astring = Config.tokenize(str, " ");
/*      */     
/* 1246 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/* 1248 */       String s = astring[i];
/* 1249 */       ResourceLocation resourcelocation = new ResourceLocation(s);
/* 1250 */       Item item = (Item)Item.itemRegistry.getObject(resourcelocation);
/*      */       
/* 1252 */       if (item == null) {
/*      */         
/* 1254 */         warn("Item not found: " + s);
/*      */       }
/*      */       else {
/*      */         
/* 1258 */         int j = Item.getIdFromItem(item);
/*      */         
/* 1260 */         if (j < 0) {
/*      */           
/* 1262 */           warn("Item has no ID: " + item + ", name: " + s);
/*      */         }
/*      */         else {
/*      */           
/* 1266 */           set.add(new Integer(j));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1271 */     Integer[] ainteger = set.<Integer>toArray(new Integer[set.size()]);
/* 1272 */     int[] aint = Config.toPrimitive(ainteger);
/* 1273 */     return aint;
/*      */   }
/*      */ 
/*      */   
/*      */   public int[] parseEntities(String str) {
/* 1278 */     str = str.trim();
/* 1279 */     Set<Integer> set = new TreeSet<>();
/* 1280 */     String[] astring = Config.tokenize(str, " ");
/*      */     
/* 1282 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/* 1284 */       String s = astring[i];
/* 1285 */       int j = EntityUtils.getEntityIdByName(s);
/*      */       
/* 1287 */       if (j < 0) {
/*      */         
/* 1289 */         warn("Entity not found: " + s);
/*      */       }
/*      */       else {
/*      */         
/* 1293 */         set.add(new Integer(j));
/*      */       } 
/*      */     } 
/*      */     
/* 1297 */     Integer[] ainteger = set.<Integer>toArray(new Integer[set.size()]);
/* 1298 */     int[] aint = Config.toPrimitive(ainteger);
/* 1299 */     return aint;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\config\ConnectedParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */