/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommand;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.stats.Achievement;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public class CommandAchievement extends CommandBase {
/*     */   public String getCommandName() {
/*  23 */     return "achievement";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  28 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  33 */     return "commands.achievement.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  38 */     if (args.length < 2)
/*     */     {
/*  40 */       throw new WrongUsageException("commands.achievement.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  44 */     final StatBase statbase = StatList.getOneShotStat(args[1]);
/*     */     
/*  46 */     if (statbase == null && !args[1].equals("*"))
/*     */     {
/*  48 */       throw new CommandException("commands.achievement.unknownAchievement", new Object[] { args[1] });
/*     */     }
/*     */ 
/*     */     
/*  52 */     final EntityPlayerMP entityplayermp = (args.length >= 3) ? getPlayer(sender, args[2]) : getCommandSenderAsPlayer(sender);
/*  53 */     boolean flag = args[0].equalsIgnoreCase("give");
/*  54 */     boolean flag1 = args[0].equalsIgnoreCase("take");
/*     */     
/*  56 */     if (flag || flag1)
/*     */     {
/*  58 */       if (statbase == null) {
/*     */         
/*  60 */         if (flag)
/*     */         {
/*  62 */           for (Achievement achievement4 : AchievementList.achievementList)
/*     */           {
/*  64 */             entityplayermp.triggerAchievement((StatBase)achievement4);
/*     */           }
/*     */           
/*  67 */           notifyOperators(sender, (ICommand)this, "commands.achievement.give.success.all", new Object[] { entityplayermp.getName() });
/*     */         }
/*  69 */         else if (flag1)
/*     */         {
/*  71 */           for (Achievement achievement5 : Lists.reverse(AchievementList.achievementList))
/*     */           {
/*  73 */             entityplayermp.func_175145_a((StatBase)achievement5);
/*     */           }
/*     */           
/*  76 */           notifyOperators(sender, (ICommand)this, "commands.achievement.take.success.all", new Object[] { entityplayermp.getName() });
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  81 */         if (statbase instanceof Achievement) {
/*     */           
/*  83 */           Achievement achievement = (Achievement)statbase;
/*     */           
/*  85 */           if (flag) {
/*     */             
/*  87 */             if (entityplayermp.getStatFile().hasAchievementUnlocked(achievement))
/*     */             {
/*  89 */               throw new CommandException("commands.achievement.alreadyHave", new Object[] { entityplayermp.getName(), statbase.createChatComponent() });
/*     */             }
/*     */             
/*     */             List<Achievement> list;
/*     */             
/*  94 */             for (list = Lists.newArrayList(); achievement.parentAchievement != null && !entityplayermp.getStatFile().hasAchievementUnlocked(achievement.parentAchievement); achievement = achievement.parentAchievement)
/*     */             {
/*  96 */               list.add(achievement.parentAchievement);
/*     */             }
/*     */             
/*  99 */             for (Achievement achievement1 : Lists.reverse(list))
/*     */             {
/* 101 */               entityplayermp.triggerAchievement((StatBase)achievement1);
/*     */             }
/*     */           }
/* 104 */           else if (flag1) {
/*     */             
/* 106 */             if (!entityplayermp.getStatFile().hasAchievementUnlocked(achievement))
/*     */             {
/* 108 */               throw new CommandException("commands.achievement.dontHave", new Object[] { entityplayermp.getName(), statbase.createChatComponent() });
/*     */             }
/*     */             
/* 111 */             List<Achievement> list1 = Lists.newArrayList((Iterator)Iterators.filter(AchievementList.achievementList.iterator(), new Predicate<Achievement>()
/*     */                   {
/*     */                     public boolean apply(Achievement p_apply_1_)
/*     */                     {
/* 115 */                       return (entityplayermp.getStatFile().hasAchievementUnlocked(p_apply_1_) && p_apply_1_ != statbase);
/*     */                     }
/*     */                   }));
/* 118 */             List<Achievement> list2 = Lists.newArrayList(list1);
/*     */             
/* 120 */             for (Achievement achievement2 : list1) {
/*     */               
/* 122 */               Achievement achievement3 = achievement2;
/*     */               
/*     */               boolean flag2;
/* 125 */               for (flag2 = false; achievement3 != null; achievement3 = achievement3.parentAchievement) {
/*     */                 
/* 127 */                 if (achievement3 == statbase)
/*     */                 {
/* 129 */                   flag2 = true;
/*     */                 }
/*     */               } 
/*     */               
/* 133 */               if (!flag2)
/*     */               {
/* 135 */                 for (achievement3 = achievement2; achievement3 != null; achievement3 = achievement3.parentAchievement)
/*     */                 {
/* 137 */                   list2.remove(achievement2);
/*     */                 }
/*     */               }
/*     */             } 
/*     */             
/* 142 */             for (Achievement achievement6 : list2)
/*     */             {
/* 144 */               entityplayermp.func_175145_a((StatBase)achievement6);
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 149 */         if (flag) {
/*     */           
/* 151 */           entityplayermp.triggerAchievement(statbase);
/* 152 */           notifyOperators(sender, (ICommand)this, "commands.achievement.give.success.one", new Object[] { entityplayermp.getName(), statbase.createChatComponent() });
/*     */         }
/* 154 */         else if (flag1) {
/*     */           
/* 156 */           entityplayermp.func_175145_a(statbase);
/* 157 */           notifyOperators(sender, (ICommand)this, "commands.achievement.take.success.one", new Object[] { statbase.createChatComponent(), entityplayermp.getName() });
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 167 */     if (args.length == 1)
/*     */     {
/* 169 */       return getListOfStringsMatchingLastWord(args, new String[] { "give", "take" });
/*     */     }
/* 171 */     if (args.length != 2)
/*     */     {
/* 173 */       return (args.length == 3) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*     */     }
/*     */ 
/*     */     
/* 177 */     List<String> list = Lists.newArrayList();
/*     */     
/* 179 */     for (StatBase statbase : StatList.allStats)
/*     */     {
/* 181 */       list.add(statbase.statId);
/*     */     }
/*     */     
/* 184 */     return getListOfStringsMatchingLastWord(args, list);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 190 */     return (index == 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandAchievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */