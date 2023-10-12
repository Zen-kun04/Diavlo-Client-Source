/*      */ package net.minecraft.command.server;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import net.minecraft.command.CommandBase;
/*      */ import net.minecraft.command.CommandException;
/*      */ import net.minecraft.command.CommandResultStats;
/*      */ import net.minecraft.command.ICommand;
/*      */ import net.minecraft.command.ICommandSender;
/*      */ import net.minecraft.command.SyntaxErrorException;
/*      */ import net.minecraft.command.WrongUsageException;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.nbt.JsonToNBT;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTException;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTUtil;
/*      */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*      */ import net.minecraft.scoreboard.Score;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ 
/*      */ public class CommandScoreboard extends CommandBase {
/*      */   public String getCommandName() {
/*   38 */     return "scoreboard";
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRequiredPermissionLevel() {
/*   43 */     return 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCommandUsage(ICommandSender sender) {
/*   48 */     return "commands.scoreboard.usage";
/*      */   }
/*      */ 
/*      */   
/*      */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*   53 */     if (!func_175780_b(sender, args)) {
/*      */       
/*   55 */       if (args.length < 1)
/*      */       {
/*   57 */         throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
/*      */       }
/*      */ 
/*      */       
/*   61 */       if (args[0].equalsIgnoreCase("objectives")) {
/*      */         
/*   63 */         if (args.length == 1)
/*      */         {
/*   65 */           throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
/*      */         }
/*      */         
/*   68 */         if (args[1].equalsIgnoreCase("list"))
/*      */         {
/*   70 */           listObjectives(sender);
/*      */         }
/*   72 */         else if (args[1].equalsIgnoreCase("add"))
/*      */         {
/*   74 */           if (args.length < 4)
/*      */           {
/*   76 */             throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
/*      */           }
/*      */           
/*   79 */           addObjective(sender, args, 2);
/*      */         }
/*   81 */         else if (args[1].equalsIgnoreCase("remove"))
/*      */         {
/*   83 */           if (args.length != 3)
/*      */           {
/*   85 */             throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
/*      */           }
/*      */           
/*   88 */           removeObjective(sender, args[2]);
/*      */         }
/*      */         else
/*      */         {
/*   92 */           if (!args[1].equalsIgnoreCase("setdisplay"))
/*      */           {
/*   94 */             throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
/*      */           }
/*      */           
/*   97 */           if (args.length != 3 && args.length != 4)
/*      */           {
/*   99 */             throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
/*      */           }
/*      */           
/*  102 */           setObjectiveDisplay(sender, args, 2);
/*      */         }
/*      */       
/*  105 */       } else if (args[0].equalsIgnoreCase("players")) {
/*      */         
/*  107 */         if (args.length == 1)
/*      */         {
/*  109 */           throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
/*      */         }
/*      */         
/*  112 */         if (args[1].equalsIgnoreCase("list"))
/*      */         {
/*  114 */           if (args.length > 3)
/*      */           {
/*  116 */             throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
/*      */           }
/*      */           
/*  119 */           listPlayers(sender, args, 2);
/*      */         }
/*  121 */         else if (args[1].equalsIgnoreCase("add"))
/*      */         {
/*  123 */           if (args.length < 5)
/*      */           {
/*  125 */             throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
/*      */           }
/*      */           
/*  128 */           setPlayer(sender, args, 2);
/*      */         }
/*  130 */         else if (args[1].equalsIgnoreCase("remove"))
/*      */         {
/*  132 */           if (args.length < 5)
/*      */           {
/*  134 */             throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
/*      */           }
/*      */           
/*  137 */           setPlayer(sender, args, 2);
/*      */         }
/*  139 */         else if (args[1].equalsIgnoreCase("set"))
/*      */         {
/*  141 */           if (args.length < 5)
/*      */           {
/*  143 */             throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
/*      */           }
/*      */           
/*  146 */           setPlayer(sender, args, 2);
/*      */         }
/*  148 */         else if (args[1].equalsIgnoreCase("reset"))
/*      */         {
/*  150 */           if (args.length != 3 && args.length != 4)
/*      */           {
/*  152 */             throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
/*      */           }
/*      */           
/*  155 */           resetPlayers(sender, args, 2);
/*      */         }
/*  157 */         else if (args[1].equalsIgnoreCase("enable"))
/*      */         {
/*  159 */           if (args.length != 4)
/*      */           {
/*  161 */             throw new WrongUsageException("commands.scoreboard.players.enable.usage", new Object[0]);
/*      */           }
/*      */           
/*  164 */           func_175779_n(sender, args, 2);
/*      */         }
/*  166 */         else if (args[1].equalsIgnoreCase("test"))
/*      */         {
/*  168 */           if (args.length != 5 && args.length != 6)
/*      */           {
/*  170 */             throw new WrongUsageException("commands.scoreboard.players.test.usage", new Object[0]);
/*      */           }
/*      */           
/*  173 */           func_175781_o(sender, args, 2);
/*      */         }
/*      */         else
/*      */         {
/*  177 */           if (!args[1].equalsIgnoreCase("operation"))
/*      */           {
/*  179 */             throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
/*      */           }
/*      */           
/*  182 */           if (args.length != 7)
/*      */           {
/*  184 */             throw new WrongUsageException("commands.scoreboard.players.operation.usage", new Object[0]);
/*      */           }
/*      */           
/*  187 */           func_175778_p(sender, args, 2);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  192 */         if (!args[0].equalsIgnoreCase("teams"))
/*      */         {
/*  194 */           throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
/*      */         }
/*      */         
/*  197 */         if (args.length == 1)
/*      */         {
/*  199 */           throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
/*      */         }
/*      */         
/*  202 */         if (args[1].equalsIgnoreCase("list")) {
/*      */           
/*  204 */           if (args.length > 3)
/*      */           {
/*  206 */             throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
/*      */           }
/*      */           
/*  209 */           listTeams(sender, args, 2);
/*      */         }
/*  211 */         else if (args[1].equalsIgnoreCase("add")) {
/*      */           
/*  213 */           if (args.length < 3)
/*      */           {
/*  215 */             throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
/*      */           }
/*      */           
/*  218 */           addTeam(sender, args, 2);
/*      */         }
/*  220 */         else if (args[1].equalsIgnoreCase("remove")) {
/*      */           
/*  222 */           if (args.length != 3)
/*      */           {
/*  224 */             throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
/*      */           }
/*      */           
/*  227 */           removeTeam(sender, args, 2);
/*      */         }
/*  229 */         else if (args[1].equalsIgnoreCase("empty")) {
/*      */           
/*  231 */           if (args.length != 3)
/*      */           {
/*  233 */             throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
/*      */           }
/*      */           
/*  236 */           emptyTeam(sender, args, 2);
/*      */         }
/*  238 */         else if (args[1].equalsIgnoreCase("join")) {
/*      */           
/*  240 */           if (args.length < 4 && (args.length != 3 || !(sender instanceof net.minecraft.entity.player.EntityPlayer)))
/*      */           {
/*  242 */             throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
/*      */           }
/*      */           
/*  245 */           joinTeam(sender, args, 2);
/*      */         }
/*  247 */         else if (args[1].equalsIgnoreCase("leave")) {
/*      */           
/*  249 */           if (args.length < 3 && !(sender instanceof net.minecraft.entity.player.EntityPlayer))
/*      */           {
/*  251 */             throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
/*      */           }
/*      */           
/*  254 */           leaveTeam(sender, args, 2);
/*      */         }
/*      */         else {
/*      */           
/*  258 */           if (!args[1].equalsIgnoreCase("option"))
/*      */           {
/*  260 */             throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
/*      */           }
/*      */           
/*  263 */           if (args.length != 4 && args.length != 5)
/*      */           {
/*  265 */             throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
/*      */           }
/*      */           
/*  268 */           setTeamOption(sender, args, 2);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean func_175780_b(ICommandSender p_175780_1_, String[] p_175780_2_) throws CommandException {
/*  277 */     int i = -1;
/*      */     
/*  279 */     for (int j = 0; j < p_175780_2_.length; j++) {
/*      */       
/*  281 */       if (isUsernameIndex(p_175780_2_, j) && "*".equals(p_175780_2_[j])) {
/*      */         
/*  283 */         if (i >= 0)
/*      */         {
/*  285 */           throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
/*      */         }
/*      */         
/*  288 */         i = j;
/*      */       } 
/*      */     } 
/*      */     
/*  292 */     if (i < 0)
/*      */     {
/*  294 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  298 */     List<String> list1 = Lists.newArrayList(getScoreboard().getObjectiveNames());
/*  299 */     String s = p_175780_2_[i];
/*  300 */     List<String> list = Lists.newArrayList();
/*      */     
/*  302 */     for (String s1 : list1) {
/*      */       
/*  304 */       p_175780_2_[i] = s1;
/*      */ 
/*      */       
/*      */       try {
/*  308 */         processCommand(p_175780_1_, p_175780_2_);
/*  309 */         list.add(s1);
/*      */       }
/*  311 */       catch (CommandException commandexception) {
/*      */         
/*  313 */         ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
/*  314 */         chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/*  315 */         p_175780_1_.addChatMessage((IChatComponent)chatcomponenttranslation);
/*      */       } 
/*      */     } 
/*      */     
/*  319 */     p_175780_2_[i] = s;
/*  320 */     p_175780_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
/*      */     
/*  322 */     if (list.size() == 0)
/*      */     {
/*  324 */       throw new WrongUsageException("commands.scoreboard.allMatchesFailed", new Object[0]);
/*      */     }
/*      */ 
/*      */     
/*  328 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Scoreboard getScoreboard() {
/*  335 */     return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
/*      */   }
/*      */ 
/*      */   
/*      */   protected ScoreObjective getObjective(String name, boolean edit) throws CommandException {
/*  340 */     Scoreboard scoreboard = getScoreboard();
/*  341 */     ScoreObjective scoreobjective = scoreboard.getObjective(name);
/*      */     
/*  343 */     if (scoreobjective == null)
/*      */     {
/*  345 */       throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] { name });
/*      */     }
/*  347 */     if (edit && scoreobjective.getCriteria().isReadOnly())
/*      */     {
/*  349 */       throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] { name });
/*      */     }
/*      */ 
/*      */     
/*  353 */     return scoreobjective;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected ScorePlayerTeam getTeam(String name) throws CommandException {
/*  359 */     Scoreboard scoreboard = getScoreboard();
/*  360 */     ScorePlayerTeam scoreplayerteam = scoreboard.getTeam(name);
/*      */     
/*  362 */     if (scoreplayerteam == null)
/*      */     {
/*  364 */       throw new CommandException("commands.scoreboard.teamNotFound", new Object[] { name });
/*      */     }
/*      */ 
/*      */     
/*  368 */     return scoreplayerteam;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addObjective(ICommandSender sender, String[] args, int index) throws CommandException {
/*  374 */     String s = args[index++];
/*  375 */     String s1 = args[index++];
/*  376 */     Scoreboard scoreboard = getScoreboard();
/*  377 */     IScoreObjectiveCriteria iscoreobjectivecriteria = (IScoreObjectiveCriteria)IScoreObjectiveCriteria.INSTANCES.get(s1);
/*      */     
/*  379 */     if (iscoreobjectivecriteria == null)
/*      */     {
/*  381 */       throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] { s1 });
/*      */     }
/*  383 */     if (scoreboard.getObjective(s) != null)
/*      */     {
/*  385 */       throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] { s });
/*      */     }
/*  387 */     if (s.length() > 16)
/*      */     {
/*  389 */       throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] { s, Integer.valueOf(16) });
/*      */     }
/*  391 */     if (s.length() == 0)
/*      */     {
/*  393 */       throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
/*      */     }
/*      */ 
/*      */     
/*  397 */     if (args.length > index) {
/*      */       
/*  399 */       String s2 = getChatComponentFromNthArg(sender, args, index).getUnformattedText();
/*      */       
/*  401 */       if (s2.length() > 32)
/*      */       {
/*  403 */         throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] { s2, Integer.valueOf(32) });
/*      */       }
/*      */       
/*  406 */       if (s2.length() > 0)
/*      */       {
/*  408 */         scoreboard.addScoreObjective(s, iscoreobjectivecriteria).setDisplayName(s2);
/*      */       }
/*      */       else
/*      */       {
/*  412 */         scoreboard.addScoreObjective(s, iscoreobjectivecriteria);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  417 */       scoreboard.addScoreObjective(s, iscoreobjectivecriteria);
/*      */     } 
/*      */     
/*  420 */     notifyOperators(sender, (ICommand)this, "commands.scoreboard.objectives.add.success", new Object[] { s });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addTeam(ICommandSender sender, String[] args, int index) throws CommandException {
/*  426 */     String s = args[index++];
/*  427 */     Scoreboard scoreboard = getScoreboard();
/*      */     
/*  429 */     if (scoreboard.getTeam(s) != null)
/*      */     {
/*  431 */       throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] { s });
/*      */     }
/*  433 */     if (s.length() > 16)
/*      */     {
/*  435 */       throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] { s, Integer.valueOf(16) });
/*      */     }
/*  437 */     if (s.length() == 0)
/*      */     {
/*  439 */       throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
/*      */     }
/*      */ 
/*      */     
/*  443 */     if (args.length > index) {
/*      */       
/*  445 */       String s1 = getChatComponentFromNthArg(sender, args, index).getUnformattedText();
/*      */       
/*  447 */       if (s1.length() > 32)
/*      */       {
/*  449 */         throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] { s1, Integer.valueOf(32) });
/*      */       }
/*      */       
/*  452 */       if (s1.length() > 0)
/*      */       {
/*  454 */         scoreboard.createTeam(s).setTeamName(s1);
/*      */       }
/*      */       else
/*      */       {
/*  458 */         scoreboard.createTeam(s);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  463 */       scoreboard.createTeam(s);
/*      */     } 
/*      */     
/*  466 */     notifyOperators(sender, (ICommand)this, "commands.scoreboard.teams.add.success", new Object[] { s });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setTeamOption(ICommandSender sender, String[] args, int index) throws CommandException {
/*  472 */     ScorePlayerTeam scoreplayerteam = getTeam(args[index++]);
/*      */     
/*  474 */     if (scoreplayerteam != null) {
/*      */       
/*  476 */       String s = args[index++].toLowerCase();
/*      */       
/*  478 */       if (!s.equalsIgnoreCase("color") && !s.equalsIgnoreCase("friendlyfire") && !s.equalsIgnoreCase("seeFriendlyInvisibles") && !s.equalsIgnoreCase("nametagVisibility") && !s.equalsIgnoreCase("deathMessageVisibility"))
/*      */       {
/*  480 */         throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
/*      */       }
/*  482 */       if (args.length == 4) {
/*      */         
/*  484 */         if (s.equalsIgnoreCase("color"))
/*      */         {
/*  486 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)) });
/*      */         }
/*  488 */         if (!s.equalsIgnoreCase("friendlyfire") && !s.equalsIgnoreCase("seeFriendlyInvisibles")) {
/*      */           
/*  490 */           if (!s.equalsIgnoreCase("nametagVisibility") && !s.equalsIgnoreCase("deathMessageVisibility"))
/*      */           {
/*  492 */             throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
/*      */           }
/*      */ 
/*      */           
/*  496 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString(Team.EnumVisible.func_178825_a()) });
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  501 */         throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(Arrays.asList(new String[] { "true", "false" })) });
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  506 */       String s1 = args[index];
/*      */       
/*  508 */       if (s.equalsIgnoreCase("color")) {
/*      */         
/*  510 */         EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s1);
/*      */         
/*  512 */         if (enumchatformatting == null || enumchatformatting.isFancyStyling())
/*      */         {
/*  514 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)) });
/*      */         }
/*      */         
/*  517 */         scoreplayerteam.setChatFormat(enumchatformatting);
/*  518 */         scoreplayerteam.setNamePrefix(enumchatformatting.toString());
/*  519 */         scoreplayerteam.setNameSuffix(EnumChatFormatting.RESET.toString());
/*      */       }
/*  521 */       else if (s.equalsIgnoreCase("friendlyfire")) {
/*      */         
/*  523 */         if (!s1.equalsIgnoreCase("true") && !s1.equalsIgnoreCase("false"))
/*      */         {
/*  525 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(Arrays.asList(new String[] { "true", "false" })) });
/*      */         }
/*      */         
/*  528 */         scoreplayerteam.setAllowFriendlyFire(s1.equalsIgnoreCase("true"));
/*      */       }
/*  530 */       else if (s.equalsIgnoreCase("seeFriendlyInvisibles")) {
/*      */         
/*  532 */         if (!s1.equalsIgnoreCase("true") && !s1.equalsIgnoreCase("false"))
/*      */         {
/*  534 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(Arrays.asList(new String[] { "true", "false" })) });
/*      */         }
/*      */         
/*  537 */         scoreplayerteam.setSeeFriendlyInvisiblesEnabled(s1.equalsIgnoreCase("true"));
/*      */       }
/*  539 */       else if (s.equalsIgnoreCase("nametagVisibility")) {
/*      */         
/*  541 */         Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(s1);
/*      */         
/*  543 */         if (team$enumvisible == null)
/*      */         {
/*  545 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString(Team.EnumVisible.func_178825_a()) });
/*      */         }
/*      */         
/*  548 */         scoreplayerteam.setNameTagVisibility(team$enumvisible);
/*      */       }
/*  550 */       else if (s.equalsIgnoreCase("deathMessageVisibility")) {
/*      */         
/*  552 */         Team.EnumVisible team$enumvisible1 = Team.EnumVisible.func_178824_a(s1);
/*      */         
/*  554 */         if (team$enumvisible1 == null)
/*      */         {
/*  556 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString(Team.EnumVisible.func_178825_a()) });
/*      */         }
/*      */         
/*  559 */         scoreplayerteam.setDeathMessageVisibility(team$enumvisible1);
/*      */       } 
/*      */       
/*  562 */       notifyOperators(sender, (ICommand)this, "commands.scoreboard.teams.option.success", new Object[] { s, scoreplayerteam.getRegisteredName(), s1 });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeTeam(ICommandSender p_147194_1_, String[] p_147194_2_, int p_147194_3_) throws CommandException {
/*  569 */     Scoreboard scoreboard = getScoreboard();
/*  570 */     ScorePlayerTeam scoreplayerteam = getTeam(p_147194_2_[p_147194_3_]);
/*      */     
/*  572 */     if (scoreplayerteam != null) {
/*      */       
/*  574 */       scoreboard.removeTeam(scoreplayerteam);
/*  575 */       notifyOperators(p_147194_1_, (ICommand)this, "commands.scoreboard.teams.remove.success", new Object[] { scoreplayerteam.getRegisteredName() });
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void listTeams(ICommandSender p_147186_1_, String[] p_147186_2_, int p_147186_3_) throws CommandException {
/*  581 */     Scoreboard scoreboard = getScoreboard();
/*      */     
/*  583 */     if (p_147186_2_.length > p_147186_3_) {
/*      */       
/*  585 */       ScorePlayerTeam scoreplayerteam = getTeam(p_147186_2_[p_147186_3_]);
/*      */       
/*  587 */       if (scoreplayerteam == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  592 */       Collection<String> collection = scoreplayerteam.getMembershipCollection();
/*  593 */       p_147186_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
/*      */       
/*  595 */       if (collection.size() <= 0)
/*      */       {
/*  597 */         throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] { scoreplayerteam.getRegisteredName() });
/*      */       }
/*      */       
/*  600 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.teams.list.player.count", new Object[] { Integer.valueOf(collection.size()), scoreplayerteam.getRegisteredName() });
/*  601 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  602 */       p_147186_1_.addChatMessage((IChatComponent)chatcomponenttranslation);
/*  603 */       p_147186_1_.addChatMessage((IChatComponent)new ChatComponentText(joinNiceString(collection.toArray())));
/*      */     }
/*      */     else {
/*      */       
/*  607 */       Collection<ScorePlayerTeam> collection1 = scoreboard.getTeams();
/*  608 */       p_147186_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection1.size());
/*      */       
/*  610 */       if (collection1.size() <= 0)
/*      */       {
/*  612 */         throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
/*      */       }
/*      */       
/*  615 */       ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.scoreboard.teams.list.count", new Object[] { Integer.valueOf(collection1.size()) });
/*  616 */       chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  617 */       p_147186_1_.addChatMessage((IChatComponent)chatcomponenttranslation1);
/*      */       
/*  619 */       for (ScorePlayerTeam scoreplayerteam1 : collection1) {
/*      */         
/*  621 */         p_147186_1_.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.scoreboard.teams.list.entry", new Object[] { scoreplayerteam1.getRegisteredName(), scoreplayerteam1.getTeamName(), Integer.valueOf(scoreplayerteam1.getMembershipCollection().size()) }));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void joinTeam(ICommandSender p_147190_1_, String[] p_147190_2_, int p_147190_3_) throws CommandException {
/*  628 */     Scoreboard scoreboard = getScoreboard();
/*  629 */     String s = p_147190_2_[p_147190_3_++];
/*  630 */     Set<String> set = Sets.newHashSet();
/*  631 */     Set<String> set1 = Sets.newHashSet();
/*      */     
/*  633 */     if (p_147190_1_ instanceof net.minecraft.entity.player.EntityPlayer && p_147190_3_ == p_147190_2_.length) {
/*      */       
/*  635 */       String s4 = getCommandSenderAsPlayer(p_147190_1_).getName();
/*      */       
/*  637 */       if (scoreboard.addPlayerToTeam(s4, s))
/*      */       {
/*  639 */         set.add(s4);
/*      */       }
/*      */       else
/*      */       {
/*  643 */         set1.add(s4);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  648 */       while (p_147190_3_ < p_147190_2_.length) {
/*      */         
/*  650 */         String s1 = p_147190_2_[p_147190_3_++];
/*      */         
/*  652 */         if (s1.startsWith("@")) {
/*      */           
/*  654 */           for (Entity entity : func_175763_c(p_147190_1_, s1)) {
/*      */             
/*  656 */             String s3 = getEntityName(p_147190_1_, entity.getUniqueID().toString());
/*      */             
/*  658 */             if (scoreboard.addPlayerToTeam(s3, s)) {
/*      */               
/*  660 */               set.add(s3);
/*      */               
/*      */               continue;
/*      */             } 
/*  664 */             set1.add(s3);
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  670 */         String s2 = getEntityName(p_147190_1_, s1);
/*      */         
/*  672 */         if (scoreboard.addPlayerToTeam(s2, s)) {
/*      */           
/*  674 */           set.add(s2);
/*      */           
/*      */           continue;
/*      */         } 
/*  678 */         set1.add(s2);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  684 */     if (!set.isEmpty()) {
/*      */       
/*  686 */       p_147190_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
/*  687 */       notifyOperators(p_147190_1_, (ICommand)this, "commands.scoreboard.teams.join.success", new Object[] { Integer.valueOf(set.size()), s, joinNiceString(set.toArray((Object[])new String[set.size()])) });
/*      */     } 
/*      */     
/*  690 */     if (!set1.isEmpty())
/*      */     {
/*  692 */       throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] { Integer.valueOf(set1.size()), s, joinNiceString(set1.toArray(new String[set1.size()])) });
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void leaveTeam(ICommandSender p_147199_1_, String[] p_147199_2_, int p_147199_3_) throws CommandException {
/*  698 */     Scoreboard scoreboard = getScoreboard();
/*  699 */     Set<String> set = Sets.newHashSet();
/*  700 */     Set<String> set1 = Sets.newHashSet();
/*      */     
/*  702 */     if (p_147199_1_ instanceof net.minecraft.entity.player.EntityPlayer && p_147199_3_ == p_147199_2_.length) {
/*      */       
/*  704 */       String s3 = getCommandSenderAsPlayer(p_147199_1_).getName();
/*      */       
/*  706 */       if (scoreboard.removePlayerFromTeams(s3))
/*      */       {
/*  708 */         set.add(s3);
/*      */       }
/*      */       else
/*      */       {
/*  712 */         set1.add(s3);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  717 */       while (p_147199_3_ < p_147199_2_.length) {
/*      */         
/*  719 */         String s = p_147199_2_[p_147199_3_++];
/*      */         
/*  721 */         if (s.startsWith("@")) {
/*      */           
/*  723 */           for (Entity entity : func_175763_c(p_147199_1_, s)) {
/*      */             
/*  725 */             String s2 = getEntityName(p_147199_1_, entity.getUniqueID().toString());
/*      */             
/*  727 */             if (scoreboard.removePlayerFromTeams(s2)) {
/*      */               
/*  729 */               set.add(s2);
/*      */               
/*      */               continue;
/*      */             } 
/*  733 */             set1.add(s2);
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  739 */         String s1 = getEntityName(p_147199_1_, s);
/*      */         
/*  741 */         if (scoreboard.removePlayerFromTeams(s1)) {
/*      */           
/*  743 */           set.add(s1);
/*      */           
/*      */           continue;
/*      */         } 
/*  747 */         set1.add(s1);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  753 */     if (!set.isEmpty()) {
/*      */       
/*  755 */       p_147199_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
/*  756 */       notifyOperators(p_147199_1_, (ICommand)this, "commands.scoreboard.teams.leave.success", new Object[] { Integer.valueOf(set.size()), joinNiceString(set.toArray((Object[])new String[set.size()])) });
/*      */     } 
/*      */     
/*  759 */     if (!set1.isEmpty())
/*      */     {
/*  761 */       throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] { Integer.valueOf(set1.size()), joinNiceString(set1.toArray(new String[set1.size()])) });
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void emptyTeam(ICommandSender p_147188_1_, String[] p_147188_2_, int p_147188_3_) throws CommandException {
/*  767 */     Scoreboard scoreboard = getScoreboard();
/*  768 */     ScorePlayerTeam scoreplayerteam = getTeam(p_147188_2_[p_147188_3_]);
/*      */     
/*  770 */     if (scoreplayerteam != null) {
/*      */       
/*  772 */       Collection<String> collection = Lists.newArrayList(scoreplayerteam.getMembershipCollection());
/*  773 */       p_147188_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, collection.size());
/*      */       
/*  775 */       if (collection.isEmpty())
/*      */       {
/*  777 */         throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] { scoreplayerteam.getRegisteredName() });
/*      */       }
/*      */ 
/*      */       
/*  781 */       for (String s : collection)
/*      */       {
/*  783 */         scoreboard.removePlayerFromTeam(s, scoreplayerteam);
/*      */       }
/*      */       
/*  786 */       notifyOperators(p_147188_1_, (ICommand)this, "commands.scoreboard.teams.empty.success", new Object[] { Integer.valueOf(collection.size()), scoreplayerteam.getRegisteredName() });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeObjective(ICommandSender p_147191_1_, String p_147191_2_) throws CommandException {
/*  793 */     Scoreboard scoreboard = getScoreboard();
/*  794 */     ScoreObjective scoreobjective = getObjective(p_147191_2_, false);
/*  795 */     scoreboard.removeObjective(scoreobjective);
/*  796 */     notifyOperators(p_147191_1_, (ICommand)this, "commands.scoreboard.objectives.remove.success", new Object[] { p_147191_2_ });
/*      */   }
/*      */ 
/*      */   
/*      */   protected void listObjectives(ICommandSender p_147196_1_) throws CommandException {
/*  801 */     Scoreboard scoreboard = getScoreboard();
/*  802 */     Collection<ScoreObjective> collection = scoreboard.getScoreObjectives();
/*      */     
/*  804 */     if (collection.size() <= 0)
/*      */     {
/*  806 */       throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
/*      */     }
/*      */ 
/*      */     
/*  810 */     ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.objectives.list.count", new Object[] { Integer.valueOf(collection.size()) });
/*  811 */     chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  812 */     p_147196_1_.addChatMessage((IChatComponent)chatcomponenttranslation);
/*      */     
/*  814 */     for (ScoreObjective scoreobjective : collection) {
/*      */       
/*  816 */       p_147196_1_.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.scoreboard.objectives.list.entry", new Object[] { scoreobjective.getName(), scoreobjective.getDisplayName(), scoreobjective.getCriteria().getName() }));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setObjectiveDisplay(ICommandSender p_147198_1_, String[] p_147198_2_, int p_147198_3_) throws CommandException {
/*  823 */     Scoreboard scoreboard = getScoreboard();
/*  824 */     String s = p_147198_2_[p_147198_3_++];
/*  825 */     int i = Scoreboard.getObjectiveDisplaySlotNumber(s);
/*  826 */     ScoreObjective scoreobjective = null;
/*      */     
/*  828 */     if (p_147198_2_.length == 4)
/*      */     {
/*  830 */       scoreobjective = getObjective(p_147198_2_[p_147198_3_], false);
/*      */     }
/*      */     
/*  833 */     if (i < 0)
/*      */     {
/*  835 */       throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] { s });
/*      */     }
/*      */ 
/*      */     
/*  839 */     scoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
/*      */     
/*  841 */     if (scoreobjective != null) {
/*      */       
/*  843 */       notifyOperators(p_147198_1_, (ICommand)this, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] { Scoreboard.getObjectiveDisplaySlot(i), scoreobjective.getName() });
/*      */     }
/*      */     else {
/*      */       
/*  847 */       notifyOperators(p_147198_1_, (ICommand)this, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] { Scoreboard.getObjectiveDisplaySlot(i) });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void listPlayers(ICommandSender p_147195_1_, String[] p_147195_2_, int p_147195_3_) throws CommandException {
/*  854 */     Scoreboard scoreboard = getScoreboard();
/*      */     
/*  856 */     if (p_147195_2_.length > p_147195_3_) {
/*      */       
/*  858 */       String s = getEntityName(p_147195_1_, p_147195_2_[p_147195_3_]);
/*  859 */       Map<ScoreObjective, Score> map = scoreboard.getObjectivesForEntity(s);
/*  860 */       p_147195_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, map.size());
/*      */       
/*  862 */       if (map.size() <= 0)
/*      */       {
/*  864 */         throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] { s });
/*      */       }
/*      */       
/*  867 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.players.list.player.count", new Object[] { Integer.valueOf(map.size()), s });
/*  868 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  869 */       p_147195_1_.addChatMessage((IChatComponent)chatcomponenttranslation);
/*      */       
/*  871 */       for (Score score : map.values())
/*      */       {
/*  873 */         p_147195_1_.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.scoreboard.players.list.player.entry", new Object[] { Integer.valueOf(score.getScorePoints()), score.getObjective().getDisplayName(), score.getObjective().getName() }));
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  878 */       Collection<String> collection = scoreboard.getObjectiveNames();
/*  879 */       p_147195_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
/*      */       
/*  881 */       if (collection.size() <= 0)
/*      */       {
/*  883 */         throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
/*      */       }
/*      */       
/*  886 */       ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.scoreboard.players.list.count", new Object[] { Integer.valueOf(collection.size()) });
/*  887 */       chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  888 */       p_147195_1_.addChatMessage((IChatComponent)chatcomponenttranslation1);
/*  889 */       p_147195_1_.addChatMessage((IChatComponent)new ChatComponentText(joinNiceString(collection.toArray())));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setPlayer(ICommandSender p_147197_1_, String[] p_147197_2_, int p_147197_3_) throws CommandException {
/*  895 */     String s = p_147197_2_[p_147197_3_ - 1];
/*  896 */     int i = p_147197_3_;
/*  897 */     String s1 = getEntityName(p_147197_1_, p_147197_2_[p_147197_3_++]);
/*      */     
/*  899 */     if (s1.length() > 40)
/*      */     {
/*  901 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s1, Integer.valueOf(40) });
/*      */     }
/*      */ 
/*      */     
/*  905 */     ScoreObjective scoreobjective = getObjective(p_147197_2_[p_147197_3_++], true);
/*  906 */     int j = s.equalsIgnoreCase("set") ? parseInt(p_147197_2_[p_147197_3_++]) : parseInt(p_147197_2_[p_147197_3_++], 0);
/*      */     
/*  908 */     if (p_147197_2_.length > p_147197_3_) {
/*      */       
/*  910 */       Entity entity = getEntity(p_147197_1_, p_147197_2_[i]);
/*      */ 
/*      */       
/*      */       try {
/*  914 */         NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(buildString(p_147197_2_, p_147197_3_));
/*  915 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  916 */         entity.writeToNBT(nbttagcompound1);
/*      */         
/*  918 */         if (!NBTUtil.func_181123_a((NBTBase)nbttagcompound, (NBTBase)nbttagcompound1, true))
/*      */         {
/*  920 */           throw new CommandException("commands.scoreboard.players.set.tagMismatch", new Object[] { s1 });
/*      */         }
/*      */       }
/*  923 */       catch (NBTException nbtexception) {
/*      */         
/*  925 */         throw new CommandException("commands.scoreboard.players.set.tagError", new Object[] { nbtexception.getMessage() });
/*      */       } 
/*      */     } 
/*      */     
/*  929 */     Scoreboard scoreboard = getScoreboard();
/*  930 */     Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
/*      */     
/*  932 */     if (s.equalsIgnoreCase("set")) {
/*      */       
/*  934 */       score.setScorePoints(j);
/*      */     }
/*  936 */     else if (s.equalsIgnoreCase("add")) {
/*      */       
/*  938 */       score.increseScore(j);
/*      */     }
/*      */     else {
/*      */       
/*  942 */       score.decreaseScore(j);
/*      */     } 
/*      */     
/*  945 */     notifyOperators(p_147197_1_, (ICommand)this, "commands.scoreboard.players.set.success", new Object[] { scoreobjective.getName(), s1, Integer.valueOf(score.getScorePoints()) });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void resetPlayers(ICommandSender p_147187_1_, String[] p_147187_2_, int p_147187_3_) throws CommandException {
/*  951 */     Scoreboard scoreboard = getScoreboard();
/*  952 */     String s = getEntityName(p_147187_1_, p_147187_2_[p_147187_3_++]);
/*      */     
/*  954 */     if (p_147187_2_.length > p_147187_3_) {
/*      */       
/*  956 */       ScoreObjective scoreobjective = getObjective(p_147187_2_[p_147187_3_++], false);
/*  957 */       scoreboard.removeObjectiveFromEntity(s, scoreobjective);
/*  958 */       notifyOperators(p_147187_1_, (ICommand)this, "commands.scoreboard.players.resetscore.success", new Object[] { scoreobjective.getName(), s });
/*      */     }
/*      */     else {
/*      */       
/*  962 */       scoreboard.removeObjectiveFromEntity(s, (ScoreObjective)null);
/*  963 */       notifyOperators(p_147187_1_, (ICommand)this, "commands.scoreboard.players.reset.success", new Object[] { s });
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void func_175779_n(ICommandSender p_175779_1_, String[] p_175779_2_, int p_175779_3_) throws CommandException {
/*  969 */     Scoreboard scoreboard = getScoreboard();
/*  970 */     String s = getPlayerName(p_175779_1_, p_175779_2_[p_175779_3_++]);
/*      */     
/*  972 */     if (s.length() > 40)
/*      */     {
/*  974 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40) });
/*      */     }
/*      */ 
/*      */     
/*  978 */     ScoreObjective scoreobjective = getObjective(p_175779_2_[p_175779_3_], false);
/*      */     
/*  980 */     if (scoreobjective.getCriteria() != IScoreObjectiveCriteria.TRIGGER)
/*      */     {
/*  982 */       throw new CommandException("commands.scoreboard.players.enable.noTrigger", new Object[] { scoreobjective.getName() });
/*      */     }
/*      */ 
/*      */     
/*  986 */     Score score = scoreboard.getValueFromObjective(s, scoreobjective);
/*  987 */     score.setLocked(false);
/*  988 */     notifyOperators(p_175779_1_, (ICommand)this, "commands.scoreboard.players.enable.success", new Object[] { scoreobjective.getName(), s });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void func_175781_o(ICommandSender p_175781_1_, String[] p_175781_2_, int p_175781_3_) throws CommandException {
/*  995 */     Scoreboard scoreboard = getScoreboard();
/*  996 */     String s = getEntityName(p_175781_1_, p_175781_2_[p_175781_3_++]);
/*      */     
/*  998 */     if (s.length() > 40)
/*      */     {
/* 1000 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40) });
/*      */     }
/*      */ 
/*      */     
/* 1004 */     ScoreObjective scoreobjective = getObjective(p_175781_2_[p_175781_3_++], false);
/*      */     
/* 1006 */     if (!scoreboard.entityHasObjective(s, scoreobjective))
/*      */     {
/* 1008 */       throw new CommandException("commands.scoreboard.players.test.notFound", new Object[] { scoreobjective.getName(), s });
/*      */     }
/*      */ 
/*      */     
/* 1012 */     int i = p_175781_2_[p_175781_3_].equals("*") ? Integer.MIN_VALUE : parseInt(p_175781_2_[p_175781_3_]);
/* 1013 */     p_175781_3_++;
/* 1014 */     int j = (p_175781_3_ < p_175781_2_.length && !p_175781_2_[p_175781_3_].equals("*")) ? parseInt(p_175781_2_[p_175781_3_], i) : Integer.MAX_VALUE;
/* 1015 */     Score score = scoreboard.getValueFromObjective(s, scoreobjective);
/*      */     
/* 1017 */     if (score.getScorePoints() >= i && score.getScorePoints() <= j) {
/*      */       
/* 1019 */       notifyOperators(p_175781_1_, (ICommand)this, "commands.scoreboard.players.test.success", new Object[] { Integer.valueOf(score.getScorePoints()), Integer.valueOf(i), Integer.valueOf(j) });
/*      */     }
/*      */     else {
/*      */       
/* 1023 */       throw new CommandException("commands.scoreboard.players.test.failed", new Object[] { Integer.valueOf(score.getScorePoints()), Integer.valueOf(i), Integer.valueOf(j) });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void func_175778_p(ICommandSender p_175778_1_, String[] p_175778_2_, int p_175778_3_) throws CommandException {
/* 1031 */     Scoreboard scoreboard = getScoreboard();
/* 1032 */     String s = getEntityName(p_175778_1_, p_175778_2_[p_175778_3_++]);
/* 1033 */     ScoreObjective scoreobjective = getObjective(p_175778_2_[p_175778_3_++], true);
/* 1034 */     String s1 = p_175778_2_[p_175778_3_++];
/* 1035 */     String s2 = getEntityName(p_175778_1_, p_175778_2_[p_175778_3_++]);
/* 1036 */     ScoreObjective scoreobjective1 = getObjective(p_175778_2_[p_175778_3_], false);
/*      */     
/* 1038 */     if (s.length() > 40)
/*      */     {
/* 1040 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40) });
/*      */     }
/* 1042 */     if (s2.length() > 40)
/*      */     {
/* 1044 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s2, Integer.valueOf(40) });
/*      */     }
/*      */ 
/*      */     
/* 1048 */     Score score = scoreboard.getValueFromObjective(s, scoreobjective);
/*      */     
/* 1050 */     if (!scoreboard.entityHasObjective(s2, scoreobjective1))
/*      */     {
/* 1052 */       throw new CommandException("commands.scoreboard.players.operation.notFound", new Object[] { scoreobjective1.getName(), s2 });
/*      */     }
/*      */ 
/*      */     
/* 1056 */     Score score1 = scoreboard.getValueFromObjective(s2, scoreobjective1);
/*      */     
/* 1058 */     if (s1.equals("+=")) {
/*      */       
/* 1060 */       score.setScorePoints(score.getScorePoints() + score1.getScorePoints());
/*      */     }
/* 1062 */     else if (s1.equals("-=")) {
/*      */       
/* 1064 */       score.setScorePoints(score.getScorePoints() - score1.getScorePoints());
/*      */     }
/* 1066 */     else if (s1.equals("*=")) {
/*      */       
/* 1068 */       score.setScorePoints(score.getScorePoints() * score1.getScorePoints());
/*      */     }
/* 1070 */     else if (s1.equals("/=")) {
/*      */       
/* 1072 */       if (score1.getScorePoints() != 0)
/*      */       {
/* 1074 */         score.setScorePoints(score.getScorePoints() / score1.getScorePoints());
/*      */       }
/*      */     }
/* 1077 */     else if (s1.equals("%=")) {
/*      */       
/* 1079 */       if (score1.getScorePoints() != 0)
/*      */       {
/* 1081 */         score.setScorePoints(score.getScorePoints() % score1.getScorePoints());
/*      */       }
/*      */     }
/* 1084 */     else if (s1.equals("=")) {
/*      */       
/* 1086 */       score.setScorePoints(score1.getScorePoints());
/*      */     }
/* 1088 */     else if (s1.equals("<")) {
/*      */       
/* 1090 */       score.setScorePoints(Math.min(score.getScorePoints(), score1.getScorePoints()));
/*      */     }
/* 1092 */     else if (s1.equals(">")) {
/*      */       
/* 1094 */       score.setScorePoints(Math.max(score.getScorePoints(), score1.getScorePoints()));
/*      */     }
/*      */     else {
/*      */       
/* 1098 */       if (!s1.equals("><"))
/*      */       {
/* 1100 */         throw new CommandException("commands.scoreboard.players.operation.invalidOperation", new Object[] { s1 });
/*      */       }
/*      */       
/* 1103 */       int i = score.getScorePoints();
/* 1104 */       score.setScorePoints(score1.getScorePoints());
/* 1105 */       score1.setScorePoints(i);
/*      */     } 
/*      */     
/* 1108 */     notifyOperators(p_175778_1_, (ICommand)this, "commands.scoreboard.players.operation.success", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 1115 */     if (args.length == 1)
/*      */     {
/* 1117 */       return getListOfStringsMatchingLastWord(args, new String[] { "objectives", "players", "teams" });
/*      */     }
/*      */ 
/*      */     
/* 1121 */     if (args[0].equalsIgnoreCase("objectives")) {
/*      */       
/* 1123 */       if (args.length == 2)
/*      */       {
/* 1125 */         return getListOfStringsMatchingLastWord(args, new String[] { "list", "add", "remove", "setdisplay" });
/*      */       }
/*      */       
/* 1128 */       if (args[1].equalsIgnoreCase("add")) {
/*      */         
/* 1130 */         if (args.length == 4)
/*      */         {
/* 1132 */           Set<String> set = IScoreObjectiveCriteria.INSTANCES.keySet();
/* 1133 */           return getListOfStringsMatchingLastWord(args, set);
/*      */         }
/*      */       
/* 1136 */       } else if (args[1].equalsIgnoreCase("remove")) {
/*      */         
/* 1138 */         if (args.length == 3)
/*      */         {
/* 1140 */           return getListOfStringsMatchingLastWord(args, func_147184_a(false));
/*      */         }
/*      */       }
/* 1143 */       else if (args[1].equalsIgnoreCase("setdisplay")) {
/*      */         
/* 1145 */         if (args.length == 3)
/*      */         {
/* 1147 */           return getListOfStringsMatchingLastWord(args, Scoreboard.getDisplaySlotStrings());
/*      */         }
/*      */         
/* 1150 */         if (args.length == 4)
/*      */         {
/* 1152 */           return getListOfStringsMatchingLastWord(args, func_147184_a(false));
/*      */         }
/*      */       }
/*      */     
/* 1156 */     } else if (args[0].equalsIgnoreCase("players")) {
/*      */       
/* 1158 */       if (args.length == 2)
/*      */       {
/* 1160 */         return getListOfStringsMatchingLastWord(args, new String[] { "set", "add", "remove", "reset", "list", "enable", "test", "operation" });
/*      */       }
/*      */       
/* 1163 */       if (!args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove") && !args[1].equalsIgnoreCase("reset")) {
/*      */         
/* 1165 */         if (args[1].equalsIgnoreCase("enable")) {
/*      */           
/* 1167 */           if (args.length == 3)
/*      */           {
/* 1169 */             return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */           }
/*      */           
/* 1172 */           if (args.length == 4)
/*      */           {
/* 1174 */             return getListOfStringsMatchingLastWord(args, func_175782_e());
/*      */           }
/*      */         }
/* 1177 */         else if (!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("test")) {
/*      */           
/* 1179 */           if (args[1].equalsIgnoreCase("operation"))
/*      */           {
/* 1181 */             if (args.length == 3)
/*      */             {
/* 1183 */               return getListOfStringsMatchingLastWord(args, getScoreboard().getObjectiveNames());
/*      */             }
/*      */             
/* 1186 */             if (args.length == 4)
/*      */             {
/* 1188 */               return getListOfStringsMatchingLastWord(args, func_147184_a(true));
/*      */             }
/*      */             
/* 1191 */             if (args.length == 5)
/*      */             {
/* 1193 */               return getListOfStringsMatchingLastWord(args, new String[] { "+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><" });
/*      */             }
/*      */             
/* 1196 */             if (args.length == 6)
/*      */             {
/* 1198 */               return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */             }
/*      */             
/* 1201 */             if (args.length == 7)
/*      */             {
/* 1203 */               return getListOfStringsMatchingLastWord(args, func_147184_a(false));
/*      */             }
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1209 */           if (args.length == 3)
/*      */           {
/* 1211 */             return getListOfStringsMatchingLastWord(args, getScoreboard().getObjectiveNames());
/*      */           }
/*      */           
/* 1214 */           if (args.length == 4 && args[1].equalsIgnoreCase("test"))
/*      */           {
/* 1216 */             return getListOfStringsMatchingLastWord(args, func_147184_a(false));
/*      */           }
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1222 */         if (args.length == 3)
/*      */         {
/* 1224 */           return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */         }
/*      */         
/* 1227 */         if (args.length == 4)
/*      */         {
/* 1229 */           return getListOfStringsMatchingLastWord(args, func_147184_a(true));
/*      */         }
/*      */       }
/*      */     
/* 1233 */     } else if (args[0].equalsIgnoreCase("teams")) {
/*      */       
/* 1235 */       if (args.length == 2)
/*      */       {
/* 1237 */         return getListOfStringsMatchingLastWord(args, new String[] { "add", "remove", "join", "leave", "empty", "list", "option" });
/*      */       }
/*      */       
/* 1240 */       if (args[1].equalsIgnoreCase("join")) {
/*      */         
/* 1242 */         if (args.length == 3)
/*      */         {
/* 1244 */           return getListOfStringsMatchingLastWord(args, getScoreboard().getTeamNames());
/*      */         }
/*      */         
/* 1247 */         if (args.length >= 4)
/*      */         {
/* 1249 */           return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1254 */         if (args[1].equalsIgnoreCase("leave"))
/*      */         {
/* 1256 */           return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */         }
/*      */         
/* 1259 */         if (!args[1].equalsIgnoreCase("empty") && !args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("remove")) {
/*      */           
/* 1261 */           if (args[1].equalsIgnoreCase("option")) {
/*      */             
/* 1263 */             if (args.length == 3)
/*      */             {
/* 1265 */               return getListOfStringsMatchingLastWord(args, getScoreboard().getTeamNames());
/*      */             }
/*      */             
/* 1268 */             if (args.length == 4)
/*      */             {
/* 1270 */               return getListOfStringsMatchingLastWord(args, new String[] { "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility" });
/*      */             }
/*      */             
/* 1273 */             if (args.length == 5)
/*      */             {
/* 1275 */               if (args[3].equalsIgnoreCase("color"))
/*      */               {
/* 1277 */                 return getListOfStringsMatchingLastWord(args, EnumChatFormatting.getValidValues(true, false));
/*      */               }
/*      */               
/* 1280 */               if (args[3].equalsIgnoreCase("nametagVisibility") || args[3].equalsIgnoreCase("deathMessageVisibility"))
/*      */               {
/* 1282 */                 return getListOfStringsMatchingLastWord(args, Team.EnumVisible.func_178825_a());
/*      */               }
/*      */               
/* 1285 */               if (args[3].equalsIgnoreCase("friendlyfire") || args[3].equalsIgnoreCase("seeFriendlyInvisibles"))
/*      */               {
/* 1287 */                 return getListOfStringsMatchingLastWord(args, new String[] { "true", "false" });
/*      */               }
/*      */             }
/*      */           
/*      */           } 
/* 1292 */         } else if (args.length == 3) {
/*      */           
/* 1294 */           return getListOfStringsMatchingLastWord(args, getScoreboard().getTeamNames());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1299 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<String> func_147184_a(boolean p_147184_1_) {
/* 1305 */     Collection<ScoreObjective> collection = getScoreboard().getScoreObjectives();
/* 1306 */     List<String> list = Lists.newArrayList();
/*      */     
/* 1308 */     for (ScoreObjective scoreobjective : collection) {
/*      */       
/* 1310 */       if (!p_147184_1_ || !scoreobjective.getCriteria().isReadOnly())
/*      */       {
/* 1312 */         list.add(scoreobjective.getName());
/*      */       }
/*      */     } 
/*      */     
/* 1316 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   protected List<String> func_175782_e() {
/* 1321 */     Collection<ScoreObjective> collection = getScoreboard().getScoreObjectives();
/* 1322 */     List<String> list = Lists.newArrayList();
/*      */     
/* 1324 */     for (ScoreObjective scoreobjective : collection) {
/*      */       
/* 1326 */       if (scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER)
/*      */       {
/* 1328 */         list.add(scoreobjective.getName());
/*      */       }
/*      */     } 
/*      */     
/* 1332 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isUsernameIndex(String[] args, int index) {
/* 1337 */     return !args[0].equalsIgnoreCase("players") ? (args[0].equalsIgnoreCase("teams") ? ((index == 2)) : false) : ((args.length > 1 && args[1].equalsIgnoreCase("operation")) ? ((index == 2 || index == 5)) : ((index == 2)));
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandScoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */