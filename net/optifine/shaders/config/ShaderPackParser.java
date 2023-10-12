/*      */ package net.optifine.shaders.config;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.CharArrayWriter;
/*      */ import java.io.IOException;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import net.minecraft.src.Config;
/*      */ import net.optifine.expr.IExpression;
/*      */ import net.optifine.expr.IExpressionBool;
/*      */ import net.optifine.shaders.IShaderPack;
/*      */ import net.optifine.shaders.Program;
/*      */ import net.optifine.shaders.SMCLog;
/*      */ import net.optifine.shaders.uniform.CustomUniform;
/*      */ import net.optifine.shaders.uniform.UniformType;
/*      */ 
/*      */ public class ShaderPackParser {
/*   21 */   private static final Pattern PATTERN_VERSION = Pattern.compile("^\\s*#version\\s+.*$");
/*   22 */   private static final Pattern PATTERN_INCLUDE = Pattern.compile("^\\s*#include\\s+\"([A-Za-z0-9_/\\.]+)\".*$");
/*   23 */   private static final Set<String> setConstNames = makeSetConstNames();
/*   24 */   private static final Map<String, Integer> mapAlphaFuncs = makeMapAlphaFuncs();
/*   25 */   private static final Map<String, Integer> mapBlendFactors = makeMapBlendFactors();
/*      */ 
/*      */   
/*      */   public static ShaderOption[] parseShaderPackOptions(IShaderPack shaderPack, String[] programNames, List<Integer> listDimensions) {
/*   29 */     if (shaderPack == null)
/*      */     {
/*   31 */       return new ShaderOption[0];
/*      */     }
/*      */ 
/*      */     
/*   35 */     Map<String, ShaderOption> map = new HashMap<>();
/*   36 */     collectShaderOptions(shaderPack, "/shaders", programNames, map);
/*   37 */     Iterator<Integer> iterator = listDimensions.iterator();
/*      */     
/*   39 */     while (iterator.hasNext()) {
/*      */       
/*   41 */       int i = ((Integer)iterator.next()).intValue();
/*   42 */       String s = "/shaders/world" + i;
/*   43 */       collectShaderOptions(shaderPack, s, programNames, map);
/*      */     } 
/*      */     
/*   46 */     Collection<ShaderOption> collection = map.values();
/*   47 */     ShaderOption[] ashaderoption = collection.<ShaderOption>toArray(new ShaderOption[collection.size()]);
/*   48 */     Comparator<ShaderOption> comparator = new Comparator<ShaderOption>()
/*      */       {
/*      */         public int compare(ShaderOption o1, ShaderOption o2)
/*      */         {
/*   52 */           return o1.getName().compareToIgnoreCase(o2.getName());
/*      */         }
/*      */       };
/*   55 */     Arrays.sort(ashaderoption, comparator);
/*   56 */     return ashaderoption;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void collectShaderOptions(IShaderPack shaderPack, String dir, String[] programNames, Map<String, ShaderOption> mapOptions) {
/*   62 */     for (int i = 0; i < programNames.length; i++) {
/*      */       
/*   64 */       String s = programNames[i];
/*      */       
/*   66 */       if (!s.equals("")) {
/*      */         
/*   68 */         String s1 = dir + "/" + s + ".vsh";
/*   69 */         String s2 = dir + "/" + s + ".fsh";
/*   70 */         collectShaderOptions(shaderPack, s1, mapOptions);
/*   71 */         collectShaderOptions(shaderPack, s2, mapOptions);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void collectShaderOptions(IShaderPack sp, String path, Map<String, ShaderOption> mapOptions) {
/*   78 */     String[] astring = getLines(sp, path);
/*      */     
/*   80 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*   82 */       String s = astring[i];
/*   83 */       ShaderOption shaderoption = getShaderOption(s, path);
/*      */       
/*   85 */       if (shaderoption != null && !shaderoption.getName().startsWith(ShaderMacros.getPrefixMacro()) && (!shaderoption.checkUsed() || isOptionUsed(shaderoption, astring))) {
/*      */         
/*   87 */         String s1 = shaderoption.getName();
/*   88 */         ShaderOption shaderoption1 = mapOptions.get(s1);
/*      */         
/*   90 */         if (shaderoption1 != null) {
/*      */           
/*   92 */           if (!Config.equals(shaderoption1.getValueDefault(), shaderoption.getValueDefault())) {
/*      */             
/*   94 */             Config.warn("Ambiguous shader option: " + shaderoption.getName());
/*   95 */             Config.warn(" - in " + Config.arrayToString((Object[])shaderoption1.getPaths()) + ": " + shaderoption1.getValueDefault());
/*   96 */             Config.warn(" - in " + Config.arrayToString((Object[])shaderoption.getPaths()) + ": " + shaderoption.getValueDefault());
/*   97 */             shaderoption1.setEnabled(false);
/*      */           } 
/*      */           
/*  100 */           if (shaderoption1.getDescription() == null || shaderoption1.getDescription().length() <= 0)
/*      */           {
/*  102 */             shaderoption1.setDescription(shaderoption.getDescription());
/*      */           }
/*      */           
/*  105 */           shaderoption1.addPaths(shaderoption.getPaths());
/*      */         }
/*      */         else {
/*      */           
/*  109 */           mapOptions.put(s1, shaderoption);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isOptionUsed(ShaderOption so, String[] lines) {
/*  117 */     for (int i = 0; i < lines.length; i++) {
/*      */       
/*  119 */       String s = lines[i];
/*      */       
/*  121 */       if (so.isUsedInLine(s))
/*      */       {
/*  123 */         return true;
/*      */       }
/*      */     } 
/*      */     
/*  127 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String[] getLines(IShaderPack sp, String path) {
/*      */     try {
/*  134 */       List<String> list = new ArrayList<>();
/*  135 */       String s = loadFile(path, sp, 0, list, 0);
/*      */       
/*  137 */       if (s == null)
/*      */       {
/*  139 */         return new String[0];
/*      */       }
/*      */ 
/*      */       
/*  143 */       ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s.getBytes());
/*  144 */       String[] astring = Config.readLines(bytearrayinputstream);
/*  145 */       return astring;
/*      */     
/*      */     }
/*  148 */     catch (IOException ioexception) {
/*      */       
/*  150 */       Config.dbg(ioexception.getClass().getName() + ": " + ioexception.getMessage());
/*  151 */       return new String[0];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static ShaderOption getShaderOption(String line, String path) {
/*  157 */     ShaderOption shaderoption = null;
/*      */     
/*  159 */     if (shaderoption == null)
/*      */     {
/*  161 */       shaderoption = ShaderOptionSwitch.parseOption(line, path);
/*      */     }
/*      */     
/*  164 */     if (shaderoption == null)
/*      */     {
/*  166 */       shaderoption = ShaderOptionVariable.parseOption(line, path);
/*      */     }
/*      */     
/*  169 */     if (shaderoption != null)
/*      */     {
/*  171 */       return shaderoption;
/*      */     }
/*      */ 
/*      */     
/*  175 */     if (shaderoption == null)
/*      */     {
/*  177 */       shaderoption = ShaderOptionSwitchConst.parseOption(line, path);
/*      */     }
/*      */     
/*  180 */     if (shaderoption == null)
/*      */     {
/*  182 */       shaderoption = ShaderOptionVariableConst.parseOption(line, path);
/*      */     }
/*      */     
/*  185 */     return (shaderoption != null && setConstNames.contains(shaderoption.getName())) ? shaderoption : null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Set<String> makeSetConstNames() {
/*  191 */     Set<String> set = new HashSet<>();
/*  192 */     set.add("shadowMapResolution");
/*  193 */     set.add("shadowMapFov");
/*  194 */     set.add("shadowDistance");
/*  195 */     set.add("shadowDistanceRenderMul");
/*  196 */     set.add("shadowIntervalSize");
/*  197 */     set.add("generateShadowMipmap");
/*  198 */     set.add("generateShadowColorMipmap");
/*  199 */     set.add("shadowHardwareFiltering");
/*  200 */     set.add("shadowHardwareFiltering0");
/*  201 */     set.add("shadowHardwareFiltering1");
/*  202 */     set.add("shadowtex0Mipmap");
/*  203 */     set.add("shadowtexMipmap");
/*  204 */     set.add("shadowtex1Mipmap");
/*  205 */     set.add("shadowcolor0Mipmap");
/*  206 */     set.add("shadowColor0Mipmap");
/*  207 */     set.add("shadowcolor1Mipmap");
/*  208 */     set.add("shadowColor1Mipmap");
/*  209 */     set.add("shadowtex0Nearest");
/*  210 */     set.add("shadowtexNearest");
/*  211 */     set.add("shadow0MinMagNearest");
/*  212 */     set.add("shadowtex1Nearest");
/*  213 */     set.add("shadow1MinMagNearest");
/*  214 */     set.add("shadowcolor0Nearest");
/*  215 */     set.add("shadowColor0Nearest");
/*  216 */     set.add("shadowColor0MinMagNearest");
/*  217 */     set.add("shadowcolor1Nearest");
/*  218 */     set.add("shadowColor1Nearest");
/*  219 */     set.add("shadowColor1MinMagNearest");
/*  220 */     set.add("wetnessHalflife");
/*  221 */     set.add("drynessHalflife");
/*  222 */     set.add("eyeBrightnessHalflife");
/*  223 */     set.add("centerDepthHalflife");
/*  224 */     set.add("sunPathRotation");
/*  225 */     set.add("ambientOcclusionLevel");
/*  226 */     set.add("superSamplingLevel");
/*  227 */     set.add("noiseTextureResolution");
/*  228 */     return set;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ShaderProfile[] parseProfiles(Properties props, ShaderOption[] shaderOptions) {
/*  233 */     String s = "profile.";
/*  234 */     List<ShaderProfile> list = new ArrayList<>();
/*      */     
/*  236 */     for (Object o : props.keySet()) {
/*      */       
/*  238 */       String s1 = (String)o;
/*  239 */       if (s1.startsWith(s)) {
/*      */         
/*  241 */         String s2 = s1.substring(s.length());
/*  242 */         props.getProperty(s1);
/*  243 */         Set<String> set = new HashSet<>();
/*  244 */         ShaderProfile shaderprofile = parseProfile(s2, props, set, shaderOptions);
/*      */         
/*  246 */         if (shaderprofile != null)
/*      */         {
/*  248 */           list.add(shaderprofile);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  253 */     if (list.size() <= 0)
/*      */     {
/*  255 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  259 */     ShaderProfile[] ashaderprofile = list.<ShaderProfile>toArray(new ShaderProfile[list.size()]);
/*  260 */     return ashaderprofile;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, IExpressionBool> parseProgramConditions(Properties props, ShaderOption[] shaderOptions) {
/*  266 */     String s = "program.";
/*  267 */     Pattern pattern = Pattern.compile("program\\.([^.]+)\\.enabled");
/*  268 */     Map<String, IExpressionBool> map = new HashMap<>();
/*      */     
/*  270 */     for (Object o : props.keySet()) {
/*      */       
/*  272 */       String s1 = (String)o;
/*  273 */       Matcher matcher = pattern.matcher(s1);
/*      */       
/*  275 */       if (matcher.matches()) {
/*      */         
/*  277 */         String s2 = matcher.group(1);
/*  278 */         String s3 = props.getProperty(s1).trim();
/*  279 */         IExpressionBool iexpressionbool = parseOptionExpression(s3, shaderOptions);
/*      */         
/*  281 */         if (iexpressionbool == null) {
/*      */           
/*  283 */           SMCLog.severe("Error parsing program condition: " + s1);
/*      */           
/*      */           continue;
/*      */         } 
/*  287 */         map.put(s2, iexpressionbool);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  292 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static IExpressionBool parseOptionExpression(String val, ShaderOption[] shaderOptions) {
/*      */     try {
/*  299 */       ShaderOptionResolver shaderoptionresolver = new ShaderOptionResolver(shaderOptions);
/*  300 */       ExpressionParser expressionparser = new ExpressionParser(shaderoptionresolver);
/*  301 */       IExpressionBool iexpressionbool = expressionparser.parseBool(val);
/*  302 */       return iexpressionbool;
/*      */     }
/*  304 */     catch (ParseException parseexception) {
/*      */       
/*  306 */       SMCLog.warning(parseexception.getClass().getName() + ": " + parseexception.getMessage());
/*  307 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static Set<String> parseOptionSliders(Properties props, ShaderOption[] shaderOptions) {
/*  313 */     Set<String> set = new HashSet<>();
/*  314 */     String s = props.getProperty("sliders");
/*      */     
/*  316 */     if (s == null)
/*      */     {
/*  318 */       return set;
/*      */     }
/*      */ 
/*      */     
/*  322 */     String[] astring = Config.tokenize(s, " ");
/*      */     
/*  324 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*  326 */       String s1 = astring[i];
/*  327 */       ShaderOption shaderoption = ShaderUtils.getShaderOption(s1, shaderOptions);
/*      */       
/*  329 */       if (shaderoption == null) {
/*      */         
/*  331 */         Config.warn("Invalid shader option: " + s1);
/*      */       }
/*      */       else {
/*      */         
/*  335 */         set.add(s1);
/*      */       } 
/*      */     } 
/*      */     
/*  339 */     return set;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ShaderProfile parseProfile(String name, Properties props, Set<String> parsedProfiles, ShaderOption[] shaderOptions) {
/*  345 */     String s = "profile.";
/*  346 */     String s1 = s + name;
/*      */     
/*  348 */     if (parsedProfiles.contains(s1)) {
/*      */       
/*  350 */       Config.warn("[Shaders] Profile already parsed: " + name);
/*  351 */       return null;
/*      */     } 
/*      */ 
/*      */     
/*  355 */     parsedProfiles.add(name);
/*  356 */     ShaderProfile shaderprofile = new ShaderProfile(name);
/*  357 */     String s2 = props.getProperty(s1);
/*  358 */     String[] astring = Config.tokenize(s2, " ");
/*      */     
/*  360 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*  362 */       String s3 = astring[i];
/*      */       
/*  364 */       if (s3.startsWith(s)) {
/*      */         
/*  366 */         String s4 = s3.substring(s.length());
/*  367 */         ShaderProfile shaderprofile1 = parseProfile(s4, props, parsedProfiles, shaderOptions);
/*      */         
/*  369 */         if (shaderprofile != null)
/*      */         {
/*  371 */           shaderprofile.addOptionValues(shaderprofile1);
/*  372 */           shaderprofile.addDisabledPrograms(shaderprofile1.getDisabledPrograms());
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  377 */         String[] astring1 = Config.tokenize(s3, ":=");
/*      */         
/*  379 */         if (astring1.length == 1) {
/*      */           
/*  381 */           String s7 = astring1[0];
/*  382 */           boolean flag = true;
/*      */           
/*  384 */           if (s7.startsWith("!")) {
/*      */             
/*  386 */             flag = false;
/*  387 */             s7 = s7.substring(1);
/*      */           } 
/*      */           
/*  390 */           String s5 = "program.";
/*      */           
/*  392 */           if (s7.startsWith(s5)) {
/*      */             
/*  394 */             String s6 = s7.substring(s5.length());
/*      */             
/*  396 */             if (!Shaders.isProgramPath(s6))
/*      */             {
/*  398 */               Config.warn("Invalid program: " + s6 + " in profile: " + shaderprofile.getName());
/*      */             }
/*  400 */             else if (flag)
/*      */             {
/*  402 */               shaderprofile.removeDisabledProgram(s6);
/*      */             }
/*      */             else
/*      */             {
/*  406 */               shaderprofile.addDisabledProgram(s6);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  411 */             ShaderOption shaderoption1 = ShaderUtils.getShaderOption(s7, shaderOptions);
/*      */             
/*  413 */             if (!(shaderoption1 instanceof ShaderOptionSwitch))
/*      */             {
/*  415 */               Config.warn("[Shaders] Invalid option: " + s7);
/*      */             }
/*      */             else
/*      */             {
/*  419 */               shaderprofile.addOptionValue(s7, String.valueOf(flag));
/*  420 */               shaderoption1.setVisible(true);
/*      */             }
/*      */           
/*      */           } 
/*  424 */         } else if (astring1.length != 2) {
/*      */           
/*  426 */           Config.warn("[Shaders] Invalid option value: " + s3);
/*      */         }
/*      */         else {
/*      */           
/*  430 */           String s8 = astring1[0];
/*  431 */           String s9 = astring1[1];
/*  432 */           ShaderOption shaderoption = ShaderUtils.getShaderOption(s8, shaderOptions);
/*      */           
/*  434 */           if (shaderoption == null) {
/*      */             
/*  436 */             Config.warn("[Shaders] Invalid option: " + s3);
/*      */           }
/*  438 */           else if (!shaderoption.isValidValue(s9)) {
/*      */             
/*  440 */             Config.warn("[Shaders] Invalid value: " + s3);
/*      */           }
/*      */           else {
/*      */             
/*  444 */             shaderoption.setVisible(true);
/*  445 */             shaderprofile.addOptionValue(s8, s9);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  451 */     return shaderprofile;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, ScreenShaderOptions> parseGuiScreens(Properties props, ShaderProfile[] shaderProfiles, ShaderOption[] shaderOptions) {
/*  457 */     Map<String, ScreenShaderOptions> map = new HashMap<>();
/*  458 */     parseGuiScreen("screen", props, map, shaderProfiles, shaderOptions);
/*  459 */     return map.isEmpty() ? null : map;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean parseGuiScreen(String key, Properties props, Map<String, ScreenShaderOptions> map, ShaderProfile[] shaderProfiles, ShaderOption[] shaderOptions) {
/*  464 */     String s = props.getProperty(key);
/*      */     
/*  466 */     if (s == null)
/*      */     {
/*  468 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  472 */     List<ShaderOption> list = new ArrayList<>();
/*  473 */     Set<String> set = new HashSet<>();
/*  474 */     String[] astring = Config.tokenize(s, " ");
/*      */     
/*  476 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/*  478 */       String s1 = astring[i];
/*      */       
/*  480 */       if (s1.equals("<empty>")) {
/*      */         
/*  482 */         list.add((ShaderOption)null);
/*      */       }
/*  484 */       else if (set.contains(s1)) {
/*      */         
/*  486 */         Config.warn("[Shaders] Duplicate option: " + s1 + ", key: " + key);
/*      */       }
/*      */       else {
/*      */         
/*  490 */         set.add(s1);
/*      */         
/*  492 */         if (s1.equals("<profile>")) {
/*      */           
/*  494 */           if (shaderProfiles == null)
/*      */           {
/*  496 */             Config.warn("[Shaders] Option profile can not be used, no profiles defined: " + s1 + ", key: " + key);
/*      */           }
/*      */           else
/*      */           {
/*  500 */             ShaderOptionProfile shaderoptionprofile = new ShaderOptionProfile(shaderProfiles, shaderOptions);
/*  501 */             list.add(shaderoptionprofile);
/*      */           }
/*      */         
/*  504 */         } else if (s1.equals("*")) {
/*      */           
/*  506 */           ShaderOption shaderoption1 = new ShaderOptionRest("<rest>");
/*  507 */           list.add(shaderoption1);
/*      */         }
/*  509 */         else if (s1.startsWith("[") && s1.endsWith("]")) {
/*      */           
/*  511 */           String s3 = StrUtils.removePrefixSuffix(s1, "[", "]");
/*      */           
/*  513 */           if (!s3.matches("^[a-zA-Z0-9_]+$"))
/*      */           {
/*  515 */             Config.warn("[Shaders] Invalid screen: " + s1 + ", key: " + key);
/*      */           }
/*  517 */           else if (!parseGuiScreen("screen." + s3, props, map, shaderProfiles, shaderOptions))
/*      */           {
/*  519 */             Config.warn("[Shaders] Invalid screen: " + s1 + ", key: " + key);
/*      */           }
/*      */           else
/*      */           {
/*  523 */             ShaderOptionScreen shaderoptionscreen = new ShaderOptionScreen(s3);
/*  524 */             list.add(shaderoptionscreen);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  529 */           ShaderOption shaderoption = ShaderUtils.getShaderOption(s1, shaderOptions);
/*      */           
/*  531 */           if (shaderoption == null) {
/*      */             
/*  533 */             Config.warn("[Shaders] Invalid option: " + s1 + ", key: " + key);
/*  534 */             list.add((ShaderOption)null);
/*      */           }
/*      */           else {
/*      */             
/*  538 */             shaderoption.setVisible(true);
/*  539 */             list.add(shaderoption);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  545 */     ShaderOption[] ashaderoption = list.<ShaderOption>toArray(new ShaderOption[list.size()]);
/*  546 */     String s2 = props.getProperty(key + ".columns");
/*  547 */     int j = Config.parseInt(s2, 2);
/*  548 */     ScreenShaderOptions screenshaderoptions = new ScreenShaderOptions(key, ashaderoption, j);
/*  549 */     map.put(key, screenshaderoptions);
/*  550 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedReader resolveIncludes(BufferedReader reader, String filePath, IShaderPack shaderPack, int fileIndex, List<String> listFiles, int includeLevel) throws IOException {
/*  556 */     String s = "/";
/*  557 */     int i = filePath.lastIndexOf("/");
/*      */     
/*  559 */     if (i >= 0)
/*      */     {
/*  561 */       s = filePath.substring(0, i);
/*      */     }
/*      */     
/*  564 */     CharArrayWriter chararraywriter = new CharArrayWriter();
/*  565 */     int j = -1;
/*  566 */     Set<ShaderMacro> set = new LinkedHashSet<>();
/*  567 */     int k = 1;
/*      */ 
/*      */     
/*      */     while (true) {
/*  571 */       String s1 = reader.readLine();
/*      */       
/*  573 */       if (s1 == null) {
/*      */         
/*  575 */         char[] achar = chararraywriter.toCharArray();
/*      */         
/*  577 */         if (j >= 0 && set.size() > 0) {
/*      */           
/*  579 */           StringBuilder stringbuilder = new StringBuilder();
/*      */           
/*  581 */           for (ShaderMacro shadermacro : set) {
/*      */             
/*  583 */             stringbuilder.append("#define ");
/*  584 */             stringbuilder.append(shadermacro.getName());
/*  585 */             stringbuilder.append(" ");
/*  586 */             stringbuilder.append(shadermacro.getValue());
/*  587 */             stringbuilder.append("\n");
/*      */           } 
/*      */           
/*  590 */           String s7 = stringbuilder.toString();
/*  591 */           StringBuilder stringbuilder1 = new StringBuilder(new String(achar));
/*  592 */           stringbuilder1.insert(j, s7);
/*  593 */           String s9 = stringbuilder1.toString();
/*  594 */           achar = s9.toCharArray();
/*      */         } 
/*      */         
/*  597 */         CharArrayReader chararrayreader = new CharArrayReader(achar);
/*  598 */         return new BufferedReader(chararrayreader);
/*      */       } 
/*      */       
/*  601 */       if (j < 0) {
/*      */         
/*  603 */         Matcher matcher = PATTERN_VERSION.matcher(s1);
/*      */         
/*  605 */         if (matcher.matches()) {
/*      */           
/*  607 */           String s2 = ShaderMacros.getFixedMacroLines() + ShaderMacros.getOptionMacroLines();
/*  608 */           String s3 = s1 + "\n" + s2;
/*  609 */           String s4 = "#line " + (k + 1) + " " + fileIndex;
/*  610 */           s1 = s3 + s4;
/*  611 */           j = chararraywriter.size() + s3.length();
/*      */         } 
/*      */       } 
/*      */       
/*  615 */       Matcher matcher1 = PATTERN_INCLUDE.matcher(s1);
/*      */       
/*  617 */       if (matcher1.matches()) {
/*      */         
/*  619 */         String s6 = matcher1.group(1);
/*  620 */         boolean flag = s6.startsWith("/");
/*  621 */         String s8 = flag ? ("/shaders" + s6) : (s + "/" + s6);
/*      */         
/*  623 */         if (!listFiles.contains(s8))
/*      */         {
/*  625 */           listFiles.add(s8);
/*      */         }
/*      */         
/*  628 */         int l = listFiles.indexOf(s8) + 1;
/*  629 */         s1 = loadFile(s8, shaderPack, l, listFiles, includeLevel);
/*      */         
/*  631 */         if (s1 == null)
/*      */         {
/*  633 */           throw new IOException("Included file not found: " + filePath);
/*      */         }
/*      */         
/*  636 */         if (s1.endsWith("\n"))
/*      */         {
/*  638 */           s1 = s1.substring(0, s1.length() - 1);
/*      */         }
/*      */         
/*  641 */         String s5 = "#line 1 " + l + "\n";
/*      */         
/*  643 */         if (s1.startsWith("#version "))
/*      */         {
/*  645 */           s5 = "";
/*      */         }
/*      */         
/*  648 */         s1 = s5 + s1 + "\n#line " + (k + 1) + " " + fileIndex;
/*      */       } 
/*      */       
/*  651 */       if (j >= 0 && s1.contains(ShaderMacros.getPrefixMacro())) {
/*      */         
/*  653 */         ShaderMacro[] ashadermacro = findMacros(s1, ShaderMacros.getExtensions());
/*      */         
/*  655 */         for (int i1 = 0; i1 < ashadermacro.length; i1++) {
/*      */           
/*  657 */           ShaderMacro shadermacro1 = ashadermacro[i1];
/*  658 */           set.add(shadermacro1);
/*      */         } 
/*      */       } 
/*      */       
/*  662 */       chararraywriter.write(s1);
/*  663 */       chararraywriter.write("\n");
/*  664 */       k++;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static ShaderMacro[] findMacros(String line, ShaderMacro[] macros) {
/*  670 */     List<ShaderMacro> list = new ArrayList<>();
/*      */     
/*  672 */     for (int i = 0; i < macros.length; i++) {
/*      */       
/*  674 */       ShaderMacro shadermacro = macros[i];
/*      */       
/*  676 */       if (line.contains(shadermacro.getName()))
/*      */       {
/*  678 */         list.add(shadermacro);
/*      */       }
/*      */     } 
/*      */     
/*  682 */     ShaderMacro[] ashadermacro = list.<ShaderMacro>toArray(new ShaderMacro[list.size()]);
/*  683 */     return ashadermacro;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String loadFile(String filePath, IShaderPack shaderPack, int fileIndex, List<String> listFiles, int includeLevel) throws IOException {
/*  688 */     if (includeLevel >= 10)
/*      */     {
/*  690 */       throw new IOException("#include depth exceeded: " + includeLevel + ", file: " + filePath);
/*      */     }
/*      */ 
/*      */     
/*  694 */     includeLevel++;
/*  695 */     InputStream inputstream = shaderPack.getResourceAsStream(filePath);
/*      */     
/*  697 */     if (inputstream == null)
/*      */     {
/*  699 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  703 */     InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "ASCII");
/*  704 */     BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
/*  705 */     bufferedreader = resolveIncludes(bufferedreader, filePath, shaderPack, fileIndex, listFiles, includeLevel);
/*  706 */     CharArrayWriter chararraywriter = new CharArrayWriter();
/*      */ 
/*      */     
/*      */     while (true) {
/*  710 */       String s = bufferedreader.readLine();
/*      */       
/*  712 */       if (s == null)
/*      */       {
/*  714 */         return chararraywriter.toString();
/*      */       }
/*      */       
/*  717 */       chararraywriter.write(s);
/*  718 */       chararraywriter.write("\n");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CustomUniforms parseCustomUniforms(Properties props) {
/*  726 */     String s = "uniform";
/*  727 */     String s1 = "variable";
/*  728 */     String s2 = s + ".";
/*  729 */     String s3 = s1 + ".";
/*  730 */     Map<String, IExpression> map = new HashMap<>();
/*  731 */     List<CustomUniform> list = new ArrayList<>();
/*      */     
/*  733 */     for (Object o : props.keySet()) {
/*      */       
/*  735 */       String s4 = (String)o;
/*  736 */       String[] astring = Config.tokenize(s4, ".");
/*      */       
/*  738 */       if (astring.length == 3) {
/*      */         
/*  740 */         String s5 = astring[0];
/*  741 */         String s6 = astring[1];
/*  742 */         String s7 = astring[2];
/*  743 */         String s8 = props.getProperty(s4).trim();
/*      */         
/*  745 */         if (map.containsKey(s7)) {
/*      */           
/*  747 */           SMCLog.warning("Expression already defined: " + s7); continue;
/*      */         } 
/*  749 */         if (s5.equals(s) || s5.equals(s1)) {
/*      */           
/*  751 */           SMCLog.info("Custom " + s5 + ": " + s7);
/*  752 */           CustomUniform customuniform = parseCustomUniform(s5, s7, s6, s8, map);
/*      */           
/*  754 */           if (customuniform != null) {
/*      */             
/*  756 */             map.put(s7, customuniform.getExpression());
/*      */             
/*  758 */             if (!s5.equals(s1))
/*      */             {
/*  760 */               list.add(customuniform);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  767 */     if (list.size() <= 0)
/*      */     {
/*  769 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  773 */     CustomUniform[] acustomuniform = list.<CustomUniform>toArray(new CustomUniform[list.size()]);
/*  774 */     CustomUniforms customuniforms = new CustomUniforms(acustomuniform, map);
/*  775 */     return customuniforms;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static CustomUniform parseCustomUniform(String kind, String name, String type, String src, Map<String, IExpression> mapExpressions) {
/*      */     try {
/*  783 */       UniformType uniformtype = UniformType.parse(type);
/*      */       
/*  785 */       if (uniformtype == null) {
/*      */         
/*  787 */         SMCLog.warning("Unknown " + kind + " type: " + uniformtype);
/*  788 */         return null;
/*      */       } 
/*      */ 
/*      */       
/*  792 */       ShaderExpressionResolver shaderexpressionresolver = new ShaderExpressionResolver(mapExpressions);
/*  793 */       ExpressionParser expressionparser = new ExpressionParser((IExpressionResolver)shaderexpressionresolver);
/*  794 */       IExpression iexpression = expressionparser.parse(src);
/*  795 */       ExpressionType expressiontype = iexpression.getExpressionType();
/*      */       
/*  797 */       if (!uniformtype.matchesExpressionType(expressiontype)) {
/*      */         
/*  799 */         SMCLog.warning("Expression type does not match " + kind + " type, expression: " + expressiontype + ", " + kind + ": " + uniformtype + " " + name);
/*  800 */         return null;
/*      */       } 
/*      */ 
/*      */       
/*  804 */       iexpression = makeExpressionCached(iexpression);
/*  805 */       CustomUniform customuniform = new CustomUniform(name, uniformtype, iexpression);
/*  806 */       return customuniform;
/*      */ 
/*      */     
/*      */     }
/*  810 */     catch (ParseException parseexception) {
/*      */       
/*  812 */       SMCLog.warning(parseexception.getClass().getName() + ": " + parseexception.getMessage());
/*  813 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static IExpression makeExpressionCached(IExpression expr) {
/*  819 */     return (expr instanceof IExpressionFloat) ? (IExpression)new ExpressionFloatCached((IExpressionFloat)expr) : ((expr instanceof IExpressionFloatArray) ? (IExpression)new ExpressionFloatArrayCached((IExpressionFloatArray)expr) : expr);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void parseAlphaStates(Properties props) {
/*  824 */     for (Object o : props.keySet()) {
/*      */       
/*  826 */       String s = (String)o;
/*  827 */       String[] astring = Config.tokenize(s, ".");
/*      */       
/*  829 */       if (astring.length == 2) {
/*      */         
/*  831 */         String s1 = astring[0];
/*  832 */         String s2 = astring[1];
/*      */         
/*  834 */         if (s1.equals("alphaTest")) {
/*      */           
/*  836 */           Program program = Shaders.getProgram(s2);
/*      */           
/*  838 */           if (program == null) {
/*      */             
/*  840 */             SMCLog.severe("Invalid program name: " + s2);
/*      */             
/*      */             continue;
/*      */           } 
/*  844 */           String s3 = props.getProperty(s).trim();
/*  845 */           GlAlphaState glalphastate = parseAlphaState(s3);
/*      */           
/*  847 */           if (glalphastate != null)
/*      */           {
/*  849 */             program.setAlphaState(glalphastate);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static GlAlphaState parseAlphaState(String str) {
/*  859 */     String[] astring = Config.tokenize(str, " ");
/*      */     
/*  861 */     if (astring.length == 1) {
/*      */       
/*  863 */       String s = astring[0];
/*      */       
/*  865 */       if (s.equals("off") || s.equals("false"))
/*      */       {
/*  867 */         return new GlAlphaState(false);
/*      */       }
/*      */     }
/*  870 */     else if (astring.length == 2) {
/*      */       
/*  872 */       String s2 = astring[0];
/*  873 */       String s1 = astring[1];
/*  874 */       Integer integer = mapAlphaFuncs.get(s2);
/*  875 */       float f = Config.parseFloat(s1, -1.0F);
/*      */       
/*  877 */       if (integer != null && f >= 0.0F)
/*      */       {
/*  879 */         return new GlAlphaState(true, integer.intValue(), f);
/*      */       }
/*      */     } 
/*      */     
/*  883 */     SMCLog.severe("Invalid alpha test: " + str);
/*  884 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void parseBlendStates(Properties props) {
/*  889 */     for (Object o : props.keySet()) {
/*      */       
/*  891 */       String s = (String)o;
/*  892 */       String[] astring = Config.tokenize(s, ".");
/*      */       
/*  894 */       if (astring.length == 2) {
/*      */         
/*  896 */         String s1 = astring[0];
/*  897 */         String s2 = astring[1];
/*      */         
/*  899 */         if (s1.equals("blend")) {
/*      */           
/*  901 */           Program program = Shaders.getProgram(s2);
/*      */           
/*  903 */           if (program == null) {
/*      */             
/*  905 */             SMCLog.severe("Invalid program name: " + s2);
/*      */             
/*      */             continue;
/*      */           } 
/*  909 */           String s3 = props.getProperty(s).trim();
/*  910 */           GlBlendState glblendstate = parseBlendState(s3);
/*      */           
/*  912 */           if (glblendstate != null)
/*      */           {
/*  914 */             program.setBlendState(glblendstate);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static GlBlendState parseBlendState(String str) {
/*  924 */     String[] astring = Config.tokenize(str, " ");
/*      */     
/*  926 */     if (astring.length == 1) {
/*      */       
/*  928 */       String s = astring[0];
/*      */       
/*  930 */       if (s.equals("off") || s.equals("false"))
/*      */       {
/*  932 */         return new GlBlendState(false);
/*      */       }
/*      */     }
/*  935 */     else if (astring.length == 2 || astring.length == 4) {
/*      */       
/*  937 */       String s4 = astring[0];
/*  938 */       String s1 = astring[1];
/*  939 */       String s2 = s4;
/*  940 */       String s3 = s1;
/*      */       
/*  942 */       if (astring.length == 4) {
/*      */         
/*  944 */         s2 = astring[2];
/*  945 */         s3 = astring[3];
/*      */       } 
/*      */       
/*  948 */       Integer integer = mapBlendFactors.get(s4);
/*  949 */       Integer integer1 = mapBlendFactors.get(s1);
/*  950 */       Integer integer2 = mapBlendFactors.get(s2);
/*  951 */       Integer integer3 = mapBlendFactors.get(s3);
/*      */       
/*  953 */       if (integer != null && integer1 != null && integer2 != null && integer3 != null)
/*      */       {
/*  955 */         return new GlBlendState(true, integer.intValue(), integer1.intValue(), integer2.intValue(), integer3.intValue());
/*      */       }
/*      */     } 
/*      */     
/*  959 */     SMCLog.severe("Invalid blend mode: " + str);
/*  960 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void parseRenderScales(Properties props) {
/*  965 */     for (Object o : props.keySet()) {
/*      */       
/*  967 */       String s = (String)o;
/*  968 */       String[] astring = Config.tokenize(s, ".");
/*      */       
/*  970 */       if (astring.length == 2) {
/*      */         
/*  972 */         String s1 = astring[0];
/*  973 */         String s2 = astring[1];
/*      */         
/*  975 */         if (s1.equals("scale")) {
/*      */           
/*  977 */           Program program = Shaders.getProgram(s2);
/*      */           
/*  979 */           if (program == null) {
/*      */             
/*  981 */             SMCLog.severe("Invalid program name: " + s2);
/*      */             
/*      */             continue;
/*      */           } 
/*  985 */           String s3 = props.getProperty(s).trim();
/*  986 */           RenderScale renderscale = parseRenderScale(s3);
/*      */           
/*  988 */           if (renderscale != null)
/*      */           {
/*  990 */             program.setRenderScale(renderscale);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static RenderScale parseRenderScale(String str) {
/* 1000 */     String[] astring = Config.tokenize(str, " ");
/* 1001 */     float f = Config.parseFloat(astring[0], -1.0F);
/* 1002 */     float f1 = 0.0F;
/* 1003 */     float f2 = 0.0F;
/*      */     
/* 1005 */     if (astring.length > 1) {
/*      */       
/* 1007 */       if (astring.length != 3) {
/*      */         
/* 1009 */         SMCLog.severe("Invalid render scale: " + str);
/* 1010 */         return null;
/*      */       } 
/*      */       
/* 1013 */       f1 = Config.parseFloat(astring[1], -1.0F);
/* 1014 */       f2 = Config.parseFloat(astring[2], -1.0F);
/*      */     } 
/*      */     
/* 1017 */     if (Config.between(f, 0.0F, 1.0F) && Config.between(f1, 0.0F, 1.0F) && Config.between(f2, 0.0F, 1.0F))
/*      */     {
/* 1019 */       return new RenderScale(f, f1, f2);
/*      */     }
/*      */ 
/*      */     
/* 1023 */     SMCLog.severe("Invalid render scale: " + str);
/* 1024 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parseBuffersFlip(Properties props) {
/* 1030 */     for (Object o : props.keySet()) {
/*      */       
/* 1032 */       String s = (String)o;
/* 1033 */       String[] astring = Config.tokenize(s, ".");
/*      */       
/* 1035 */       if (astring.length == 3) {
/*      */         
/* 1037 */         String s1 = astring[0];
/* 1038 */         String s2 = astring[1];
/* 1039 */         String s3 = astring[2];
/*      */         
/* 1041 */         if (s1.equals("flip")) {
/*      */           
/* 1043 */           Program program = Shaders.getProgram(s2);
/*      */           
/* 1045 */           if (program == null) {
/*      */             
/* 1047 */             SMCLog.severe("Invalid program name: " + s2);
/*      */             
/*      */             continue;
/*      */           } 
/* 1051 */           Boolean[] aboolean = program.getBuffersFlip();
/* 1052 */           int i = Shaders.getBufferIndexFromString(s3);
/*      */           
/* 1054 */           if (i >= 0 && i < aboolean.length) {
/*      */             
/* 1056 */             String s4 = props.getProperty(s).trim();
/* 1057 */             Boolean obool = Config.parseBoolean(s4, (Boolean)null);
/*      */             
/* 1059 */             if (obool == null) {
/*      */               
/* 1061 */               SMCLog.severe("Invalid boolean value: " + s4);
/*      */               
/*      */               continue;
/*      */             } 
/* 1065 */             aboolean[i] = obool;
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/* 1070 */           SMCLog.severe("Invalid buffer name: " + s3);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Map<String, Integer> makeMapAlphaFuncs() {
/* 1080 */     Map<String, Integer> map = new HashMap<>();
/* 1081 */     map.put("NEVER", new Integer(512));
/* 1082 */     map.put("LESS", new Integer(513));
/* 1083 */     map.put("EQUAL", new Integer(514));
/* 1084 */     map.put("LEQUAL", new Integer(515));
/* 1085 */     map.put("GREATER", new Integer(516));
/* 1086 */     map.put("NOTEQUAL", new Integer(517));
/* 1087 */     map.put("GEQUAL", new Integer(518));
/* 1088 */     map.put("ALWAYS", new Integer(519));
/* 1089 */     return Collections.unmodifiableMap(map);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Map<String, Integer> makeMapBlendFactors() {
/* 1094 */     Map<String, Integer> map = new HashMap<>();
/* 1095 */     map.put("ZERO", new Integer(0));
/* 1096 */     map.put("ONE", new Integer(1));
/* 1097 */     map.put("SRC_COLOR", new Integer(768));
/* 1098 */     map.put("ONE_MINUS_SRC_COLOR", new Integer(769));
/* 1099 */     map.put("DST_COLOR", new Integer(774));
/* 1100 */     map.put("ONE_MINUS_DST_COLOR", new Integer(775));
/* 1101 */     map.put("SRC_ALPHA", new Integer(770));
/* 1102 */     map.put("ONE_MINUS_SRC_ALPHA", new Integer(771));
/* 1103 */     map.put("DST_ALPHA", new Integer(772));
/* 1104 */     map.put("ONE_MINUS_DST_ALPHA", new Integer(773));
/* 1105 */     map.put("CONSTANT_COLOR", new Integer(32769));
/* 1106 */     map.put("ONE_MINUS_CONSTANT_COLOR", new Integer(32770));
/* 1107 */     map.put("CONSTANT_ALPHA", new Integer(32771));
/* 1108 */     map.put("ONE_MINUS_CONSTANT_ALPHA", new Integer(32772));
/* 1109 */     map.put("SRC_ALPHA_SATURATE", new Integer(776));
/* 1110 */     return Collections.unmodifiableMap(map);
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\config\ShaderPackParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */