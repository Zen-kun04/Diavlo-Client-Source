/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.base.Functions;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public abstract class CommandBase
/*     */   implements ICommand
/*     */ {
/*     */   private static IAdminCommand theAdmin;
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  28 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getCommandAliases() {
/*  33 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCommandSenderUseCommand(ICommandSender sender) {
/*  38 */     return sender.canCommandSenderUseCommand(getRequiredPermissionLevel(), getCommandName());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/*  43 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int parseInt(String input) throws NumberInvalidException {
/*     */     try {
/*  50 */       return Integer.parseInt(input);
/*     */     }
/*  52 */     catch (NumberFormatException var2) {
/*     */       
/*  54 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int parseInt(String input, int min) throws NumberInvalidException {
/*  60 */     return parseInt(input, min, 2147483647);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int parseInt(String input, int min, int max) throws NumberInvalidException {
/*  65 */     int i = parseInt(input);
/*     */     
/*  67 */     if (i < min)
/*     */     {
/*  69 */       throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Integer.valueOf(i), Integer.valueOf(min) });
/*     */     }
/*  71 */     if (i > max)
/*     */     {
/*  73 */       throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Integer.valueOf(i), Integer.valueOf(max) });
/*     */     }
/*     */ 
/*     */     
/*  77 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long parseLong(String input) throws NumberInvalidException {
/*     */     try {
/*  85 */       return Long.parseLong(input);
/*     */     }
/*  87 */     catch (NumberFormatException var2) {
/*     */       
/*  89 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static long parseLong(String input, long min, long max) throws NumberInvalidException {
/*  95 */     long i = parseLong(input);
/*     */     
/*  97 */     if (i < min)
/*     */     {
/*  99 */       throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Long.valueOf(i), Long.valueOf(min) });
/*     */     }
/* 101 */     if (i > max)
/*     */     {
/* 103 */       throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Long.valueOf(i), Long.valueOf(max) });
/*     */     }
/*     */ 
/*     */     
/* 107 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BlockPos parseBlockPos(ICommandSender sender, String[] args, int startIndex, boolean centerBlock) throws NumberInvalidException {
/* 113 */     BlockPos blockpos = sender.getPosition();
/* 114 */     return new BlockPos(parseDouble(blockpos.getX(), args[startIndex], -30000000, 30000000, centerBlock), parseDouble(blockpos.getY(), args[startIndex + 1], 0, 256, false), parseDouble(blockpos.getZ(), args[startIndex + 2], -30000000, 30000000, centerBlock));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static double parseDouble(String input) throws NumberInvalidException {
/*     */     try {
/* 121 */       double d0 = Double.parseDouble(input);
/*     */       
/* 123 */       if (!Doubles.isFinite(d0))
/*     */       {
/* 125 */         throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
/*     */       }
/*     */ 
/*     */       
/* 129 */       return d0;
/*     */     
/*     */     }
/* 132 */     catch (NumberFormatException var3) {
/*     */       
/* 134 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static double parseDouble(String input, double min) throws NumberInvalidException {
/* 140 */     return parseDouble(input, min, Double.MAX_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static double parseDouble(String input, double min, double max) throws NumberInvalidException {
/* 145 */     double d0 = parseDouble(input);
/*     */     
/* 147 */     if (d0 < min)
/*     */     {
/* 149 */       throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] { Double.valueOf(d0), Double.valueOf(min) });
/*     */     }
/* 151 */     if (d0 > max)
/*     */     {
/* 153 */       throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] { Double.valueOf(d0), Double.valueOf(max) });
/*     */     }
/*     */ 
/*     */     
/* 157 */     return d0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean parseBoolean(String input) throws CommandException {
/* 163 */     if (!input.equals("true") && !input.equals("1")) {
/*     */       
/* 165 */       if (!input.equals("false") && !input.equals("0"))
/*     */       {
/* 167 */         throw new CommandException("commands.generic.boolean.invalid", new Object[] { input });
/*     */       }
/*     */ 
/*     */       
/* 171 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 176 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender sender) throws PlayerNotFoundException {
/* 182 */     if (sender instanceof EntityPlayerMP)
/*     */     {
/* 184 */       return (EntityPlayerMP)sender;
/*     */     }
/*     */ 
/*     */     
/* 188 */     throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static EntityPlayerMP getPlayer(ICommandSender sender, String username) throws PlayerNotFoundException {
/* 194 */     EntityPlayerMP entityplayermp = PlayerSelector.matchOnePlayer(sender, username);
/*     */     
/* 196 */     if (entityplayermp == null) {
/*     */       
/*     */       try {
/*     */         
/* 200 */         entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(UUID.fromString(username));
/*     */       }
/* 202 */       catch (IllegalArgumentException illegalArgumentException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     if (entityplayermp == null)
/*     */     {
/* 210 */       entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
/*     */     }
/*     */     
/* 213 */     if (entityplayermp == null)
/*     */     {
/* 215 */       throw new PlayerNotFoundException();
/*     */     }
/*     */ 
/*     */     
/* 219 */     return entityplayermp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Entity getEntity(ICommandSender p_175768_0_, String p_175768_1_) throws EntityNotFoundException {
/* 225 */     return getEntity(p_175768_0_, p_175768_1_, Entity.class);
/*     */   }
/*     */   
/*     */   public static <T extends Entity> T getEntity(ICommandSender commandSender, String p_175759_1_, Class<? extends T> p_175759_2_) throws EntityNotFoundException {
/*     */     EntityPlayerMP entityPlayerMP;
/* 230 */     Entity entity = PlayerSelector.matchOneEntity(commandSender, p_175759_1_, p_175759_2_);
/* 231 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/* 233 */     if (entity == null)
/*     */     {
/* 235 */       entityPlayerMP = minecraftserver.getConfigurationManager().getPlayerByUsername(p_175759_1_);
/*     */     }
/*     */     
/* 238 */     if (entityPlayerMP == null) {
/*     */       
/*     */       try {
/*     */         
/* 242 */         UUID uuid = UUID.fromString(p_175759_1_);
/* 243 */         Entity entity1 = minecraftserver.getEntityFromUuid(uuid);
/*     */         
/* 245 */         if (entity1 == null)
/*     */         {
/* 247 */           entityPlayerMP = minecraftserver.getConfigurationManager().getPlayerByUUID(uuid);
/*     */         }
/*     */       }
/* 250 */       catch (IllegalArgumentException var6) {
/*     */         
/* 252 */         throw new EntityNotFoundException("commands.generic.entity.invalidUuid", new Object[0]);
/*     */       } 
/*     */     }
/*     */     
/* 256 */     if (entityPlayerMP != null && p_175759_2_.isAssignableFrom(entityPlayerMP.getClass()))
/*     */     {
/* 258 */       return (T)entityPlayerMP;
/*     */     }
/*     */ 
/*     */     
/* 262 */     throw new EntityNotFoundException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Entity> func_175763_c(ICommandSender p_175763_0_, String p_175763_1_) throws EntityNotFoundException {
/* 268 */     return PlayerSelector.hasArguments(p_175763_1_) ? PlayerSelector.<Entity>matchEntities(p_175763_0_, p_175763_1_, Entity.class) : Lists.newArrayList((Object[])new Entity[] { getEntity(p_175763_0_, p_175763_1_) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPlayerName(ICommandSender sender, String query) throws PlayerNotFoundException {
/*     */     try {
/* 275 */       return getPlayer(sender, query).getName();
/*     */     }
/* 277 */     catch (PlayerNotFoundException playernotfoundexception) {
/*     */       
/* 279 */       if (PlayerSelector.hasArguments(query))
/*     */       {
/* 281 */         throw playernotfoundexception;
/*     */       }
/*     */ 
/*     */       
/* 285 */       return query;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getEntityName(ICommandSender p_175758_0_, String p_175758_1_) throws EntityNotFoundException {
/*     */     try {
/* 294 */       return getPlayer(p_175758_0_, p_175758_1_).getName();
/*     */     }
/* 296 */     catch (PlayerNotFoundException var5) {
/*     */ 
/*     */       
/*     */       try {
/* 300 */         return getEntity(p_175758_0_, p_175758_1_).getUniqueID().toString();
/*     */       }
/* 302 */       catch (EntityNotFoundException entitynotfoundexception) {
/*     */         
/* 304 */         if (PlayerSelector.hasArguments(p_175758_1_))
/*     */         {
/* 306 */           throw entitynotfoundexception;
/*     */         }
/*     */ 
/*     */         
/* 310 */         return p_175758_1_;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int p_147178_2_) throws CommandException, PlayerNotFoundException {
/* 318 */     return getChatComponentFromNthArg(sender, args, p_147178_2_, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int index, boolean p_147176_3_) throws PlayerNotFoundException {
/* 323 */     ChatComponentText chatComponentText = new ChatComponentText("");
/*     */     
/* 325 */     for (int i = index; i < args.length; i++) {
/*     */       IChatComponent iChatComponent;
/* 327 */       if (i > index)
/*     */       {
/* 329 */         chatComponentText.appendText(" ");
/*     */       }
/*     */       
/* 332 */       ChatComponentText chatComponentText1 = new ChatComponentText(args[i]);
/*     */       
/* 334 */       if (p_147176_3_) {
/*     */         
/* 336 */         IChatComponent ichatcomponent2 = PlayerSelector.matchEntitiesToChatComponent(sender, args[i]);
/*     */         
/* 338 */         if (ichatcomponent2 == null) {
/*     */           
/* 340 */           if (PlayerSelector.hasArguments(args[i]))
/*     */           {
/* 342 */             throw new PlayerNotFoundException();
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 347 */           iChatComponent = ichatcomponent2;
/*     */         } 
/*     */       } 
/*     */       
/* 351 */       chatComponentText.appendSibling(iChatComponent);
/*     */     } 
/*     */     
/* 354 */     return (IChatComponent)chatComponentText;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String buildString(String[] args, int startPos) {
/* 359 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/* 361 */     for (int i = startPos; i < args.length; i++) {
/*     */       
/* 363 */       if (i > startPos)
/*     */       {
/* 365 */         stringbuilder.append(" ");
/*     */       }
/*     */       
/* 368 */       String s = args[i];
/* 369 */       stringbuilder.append(s);
/*     */     } 
/*     */     
/* 372 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static CoordinateArg parseCoordinate(double base, String p_175770_2_, boolean centerBlock) throws NumberInvalidException {
/* 377 */     return parseCoordinate(base, p_175770_2_, -30000000, 30000000, centerBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public static CoordinateArg parseCoordinate(double p_175767_0_, String p_175767_2_, int min, int max, boolean centerBlock) throws NumberInvalidException {
/* 382 */     boolean flag = p_175767_2_.startsWith("~");
/*     */     
/* 384 */     if (flag && Double.isNaN(p_175767_0_))
/*     */     {
/* 386 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { Double.valueOf(p_175767_0_) });
/*     */     }
/*     */ 
/*     */     
/* 390 */     double d0 = 0.0D;
/*     */     
/* 392 */     if (!flag || p_175767_2_.length() > 1) {
/*     */       
/* 394 */       boolean flag1 = p_175767_2_.contains(".");
/*     */       
/* 396 */       if (flag)
/*     */       {
/* 398 */         p_175767_2_ = p_175767_2_.substring(1);
/*     */       }
/*     */       
/* 401 */       d0 += parseDouble(p_175767_2_);
/*     */       
/* 403 */       if (!flag1 && !flag && centerBlock)
/*     */       {
/* 405 */         d0 += 0.5D;
/*     */       }
/*     */     } 
/*     */     
/* 409 */     if (min != 0 || max != 0) {
/*     */       
/* 411 */       if (d0 < min)
/*     */       {
/* 413 */         throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] { Double.valueOf(d0), Integer.valueOf(min) });
/*     */       }
/*     */       
/* 416 */       if (d0 > max)
/*     */       {
/* 418 */         throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] { Double.valueOf(d0), Integer.valueOf(max) });
/*     */       }
/*     */     } 
/*     */     
/* 422 */     return new CoordinateArg(d0 + (flag ? p_175767_0_ : 0.0D), d0, flag);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static double parseDouble(double base, String input, boolean centerBlock) throws NumberInvalidException {
/* 428 */     return parseDouble(base, input, -30000000, 30000000, centerBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public static double parseDouble(double base, String input, int min, int max, boolean centerBlock) throws NumberInvalidException {
/* 433 */     boolean flag = input.startsWith("~");
/*     */     
/* 435 */     if (flag && Double.isNaN(base))
/*     */     {
/* 437 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { Double.valueOf(base) });
/*     */     }
/*     */ 
/*     */     
/* 441 */     double d0 = flag ? base : 0.0D;
/*     */     
/* 443 */     if (!flag || input.length() > 1) {
/*     */       
/* 445 */       boolean flag1 = input.contains(".");
/*     */       
/* 447 */       if (flag)
/*     */       {
/* 449 */         input = input.substring(1);
/*     */       }
/*     */       
/* 452 */       d0 += parseDouble(input);
/*     */       
/* 454 */       if (!flag1 && !flag && centerBlock)
/*     */       {
/* 456 */         d0 += 0.5D;
/*     */       }
/*     */     } 
/*     */     
/* 460 */     if (min != 0 || max != 0) {
/*     */       
/* 462 */       if (d0 < min)
/*     */       {
/* 464 */         throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] { Double.valueOf(d0), Integer.valueOf(min) });
/*     */       }
/*     */       
/* 467 */       if (d0 > max)
/*     */       {
/* 469 */         throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] { Double.valueOf(d0), Integer.valueOf(max) });
/*     */       }
/*     */     } 
/*     */     
/* 473 */     return d0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Item getItemByText(ICommandSender sender, String id) throws NumberInvalidException {
/* 479 */     ResourceLocation resourcelocation = new ResourceLocation(id);
/* 480 */     Item item = (Item)Item.itemRegistry.getObject(resourcelocation);
/*     */     
/* 482 */     if (item == null)
/*     */     {
/* 484 */       throw new NumberInvalidException("commands.give.item.notFound", new Object[] { resourcelocation });
/*     */     }
/*     */ 
/*     */     
/* 488 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Block getBlockByText(ICommandSender sender, String id) throws NumberInvalidException {
/* 494 */     ResourceLocation resourcelocation = new ResourceLocation(id);
/*     */     
/* 496 */     if (!Block.blockRegistry.containsKey(resourcelocation))
/*     */     {
/* 498 */       throw new NumberInvalidException("commands.give.block.notFound", new Object[] { resourcelocation });
/*     */     }
/*     */ 
/*     */     
/* 502 */     Block block = (Block)Block.blockRegistry.getObject(resourcelocation);
/*     */     
/* 504 */     if (block == null)
/*     */     {
/* 506 */       throw new NumberInvalidException("commands.give.block.notFound", new Object[] { resourcelocation });
/*     */     }
/*     */ 
/*     */     
/* 510 */     return block;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String joinNiceString(Object[] elements) {
/* 517 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/* 519 */     for (int i = 0; i < elements.length; i++) {
/*     */       
/* 521 */       String s = elements[i].toString();
/*     */       
/* 523 */       if (i > 0)
/*     */       {
/* 525 */         if (i == elements.length - 1) {
/*     */           
/* 527 */           stringbuilder.append(" and ");
/*     */         }
/*     */         else {
/*     */           
/* 531 */           stringbuilder.append(", ");
/*     */         } 
/*     */       }
/*     */       
/* 535 */       stringbuilder.append(s);
/*     */     } 
/*     */     
/* 538 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static IChatComponent join(List<IChatComponent> components) {
/* 543 */     ChatComponentText chatComponentText = new ChatComponentText("");
/*     */     
/* 545 */     for (int i = 0; i < components.size(); i++) {
/*     */       
/* 547 */       if (i > 0)
/*     */       {
/* 549 */         if (i == components.size() - 1) {
/*     */           
/* 551 */           chatComponentText.appendText(" and ");
/*     */         }
/* 553 */         else if (i > 0) {
/*     */           
/* 555 */           chatComponentText.appendText(", ");
/*     */         } 
/*     */       }
/*     */       
/* 559 */       chatComponentText.appendSibling(components.get(i));
/*     */     } 
/*     */     
/* 562 */     return (IChatComponent)chatComponentText;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String joinNiceStringFromCollection(Collection<String> strings) {
/* 567 */     return joinNiceString(strings.toArray((Object[])new String[strings.size()]));
/*     */   }
/*     */   
/*     */   public static List<String> func_175771_a(String[] p_175771_0_, int p_175771_1_, BlockPos p_175771_2_) {
/*     */     String s;
/* 572 */     if (p_175771_2_ == null)
/*     */     {
/* 574 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 578 */     int i = p_175771_0_.length - 1;
/*     */ 
/*     */     
/* 581 */     if (i == p_175771_1_) {
/*     */       
/* 583 */       s = Integer.toString(p_175771_2_.getX());
/*     */     }
/* 585 */     else if (i == p_175771_1_ + 1) {
/*     */       
/* 587 */       s = Integer.toString(p_175771_2_.getY());
/*     */     }
/*     */     else {
/*     */       
/* 591 */       if (i != p_175771_1_ + 2)
/*     */       {
/* 593 */         return null;
/*     */       }
/*     */       
/* 596 */       s = Integer.toString(p_175771_2_.getZ());
/*     */     } 
/*     */     
/* 599 */     return Lists.newArrayList((Object[])new String[] { s });
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<String> func_181043_b(String[] p_181043_0_, int p_181043_1_, BlockPos p_181043_2_) {
/*     */     String s;
/* 605 */     if (p_181043_2_ == null)
/*     */     {
/* 607 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 611 */     int i = p_181043_0_.length - 1;
/*     */ 
/*     */     
/* 614 */     if (i == p_181043_1_) {
/*     */       
/* 616 */       s = Integer.toString(p_181043_2_.getX());
/*     */     }
/*     */     else {
/*     */       
/* 620 */       if (i != p_181043_1_ + 1)
/*     */       {
/* 622 */         return null;
/*     */       }
/*     */       
/* 625 */       s = Integer.toString(p_181043_2_.getZ());
/*     */     } 
/*     */     
/* 628 */     return Lists.newArrayList((Object[])new String[] { s });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean doesStringStartWith(String original, String region) {
/* 634 */     return region.regionMatches(true, 0, original, 0, original.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<String> getListOfStringsMatchingLastWord(String[] args, String... possibilities) {
/* 639 */     return getListOfStringsMatchingLastWord(args, Arrays.asList((Object[])possibilities));
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<String> getListOfStringsMatchingLastWord(String[] p_175762_0_, Collection<?> p_175762_1_) {
/* 644 */     String s = p_175762_0_[p_175762_0_.length - 1];
/* 645 */     List<String> list = Lists.newArrayList();
/*     */     
/* 647 */     if (!p_175762_1_.isEmpty()) {
/*     */       
/* 649 */       for (String s1 : Iterables.transform(p_175762_1_, Functions.toStringFunction())) {
/*     */         
/* 651 */         if (doesStringStartWith(s, s1))
/*     */         {
/* 653 */           list.add(s1);
/*     */         }
/*     */       } 
/*     */       
/* 657 */       if (list.isEmpty())
/*     */       {
/* 659 */         for (Object object : p_175762_1_) {
/*     */           
/* 661 */           if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation)object).getResourcePath()))
/*     */           {
/* 663 */             list.add(String.valueOf(object));
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 669 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 674 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void notifyOperators(ICommandSender sender, ICommand command, String msgFormat, Object... msgParams) {
/* 679 */     notifyOperators(sender, command, 0, msgFormat, msgParams);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void notifyOperators(ICommandSender sender, ICommand command, int p_152374_2_, String msgFormat, Object... msgParams) {
/* 684 */     if (theAdmin != null)
/*     */     {
/* 686 */       theAdmin.notifyOperators(sender, command, p_152374_2_, msgFormat, msgParams);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setAdminCommander(IAdminCommand command) {
/* 692 */     theAdmin = command;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(ICommand p_compareTo_1_) {
/* 697 */     return getCommandName().compareTo(p_compareTo_1_.getCommandName());
/*     */   }
/*     */ 
/*     */   
/*     */   public static class CoordinateArg
/*     */   {
/*     */     private final double field_179633_a;
/*     */     private final double field_179631_b;
/*     */     private final boolean field_179632_c;
/*     */     
/*     */     protected CoordinateArg(double p_i46051_1_, double p_i46051_3_, boolean p_i46051_5_) {
/* 708 */       this.field_179633_a = p_i46051_1_;
/* 709 */       this.field_179631_b = p_i46051_3_;
/* 710 */       this.field_179632_c = p_i46051_5_;
/*     */     }
/*     */ 
/*     */     
/*     */     public double func_179628_a() {
/* 715 */       return this.field_179633_a;
/*     */     }
/*     */ 
/*     */     
/*     */     public double func_179629_b() {
/* 720 */       return this.field_179631_b;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean func_179630_c() {
/* 725 */       return this.field_179632_c;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */