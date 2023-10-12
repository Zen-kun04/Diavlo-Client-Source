/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityCommandBlock;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandStats
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName() {
/*  19 */     return "stats";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  24 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  29 */     return "commands.stats.usage";
/*     */   } public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*     */     boolean flag;
/*     */     int i;
/*     */     CommandResultStats commandresultstats;
/*  34 */     if (args.length < 1)
/*     */     {
/*  36 */       throw new WrongUsageException("commands.stats.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  42 */     if (args[0].equals("entity")) {
/*     */       
/*  44 */       flag = false;
/*     */     }
/*     */     else {
/*     */       
/*  48 */       if (!args[0].equals("block"))
/*     */       {
/*  50 */         throw new WrongUsageException("commands.stats.usage", new Object[0]);
/*     */       }
/*     */       
/*  53 */       flag = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  58 */     if (flag) {
/*     */       
/*  60 */       if (args.length < 5)
/*     */       {
/*  62 */         throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
/*     */       }
/*     */       
/*  65 */       i = 4;
/*     */     }
/*     */     else {
/*     */       
/*  69 */       if (args.length < 3)
/*     */       {
/*  71 */         throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
/*     */       }
/*     */       
/*  74 */       i = 2;
/*     */     } 
/*     */     
/*  77 */     String s = args[i++];
/*     */     
/*  79 */     if ("set".equals(s)) {
/*     */       
/*  81 */       if (args.length < i + 3)
/*     */       {
/*  83 */         if (i == 5)
/*     */         {
/*  85 */           throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
/*     */         }
/*     */         
/*  88 */         throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  93 */       if (!"clear".equals(s))
/*     */       {
/*  95 */         throw new WrongUsageException("commands.stats.usage", new Object[0]);
/*     */       }
/*     */       
/*  98 */       if (args.length < i + 1) {
/*     */         
/* 100 */         if (i == 5)
/*     */         {
/* 102 */           throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
/*     */         }
/*     */         
/* 105 */         throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     CommandResultStats.Type commandresultstats$type = CommandResultStats.Type.getTypeByName(args[i++]);
/*     */     
/* 111 */     if (commandresultstats$type == null)
/*     */     {
/* 113 */       throw new CommandException("commands.stats.failed", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 117 */     World world = sender.getEntityWorld();
/*     */ 
/*     */     
/* 120 */     if (flag) {
/*     */       
/* 122 */       BlockPos blockpos = parseBlockPos(sender, args, 1, false);
/* 123 */       TileEntity tileentity = world.getTileEntity(blockpos);
/*     */       
/* 125 */       if (tileentity == null)
/*     */       {
/* 127 */         throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */       }
/*     */       
/* 130 */       if (tileentity instanceof TileEntityCommandBlock)
/*     */       {
/* 132 */         commandresultstats = ((TileEntityCommandBlock)tileentity).getCommandResultStats();
/*     */       }
/*     */       else
/*     */       {
/* 136 */         if (!(tileentity instanceof TileEntitySign))
/*     */         {
/* 138 */           throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */         }
/*     */         
/* 141 */         commandresultstats = ((TileEntitySign)tileentity).getStats();
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 146 */       Entity entity = getEntity(sender, args[1]);
/* 147 */       commandresultstats = entity.getCommandStats();
/*     */     } 
/*     */     
/* 150 */     if ("set".equals(s)) {
/*     */       
/* 152 */       String s1 = args[i++];
/* 153 */       String s2 = args[i];
/*     */       
/* 155 */       if (s1.length() == 0 || s2.length() == 0)
/*     */       {
/* 157 */         throw new CommandException("commands.stats.failed", new Object[0]);
/*     */       }
/*     */       
/* 160 */       CommandResultStats.setScoreBoardStat(commandresultstats, commandresultstats$type, s1, s2);
/* 161 */       notifyOperators(sender, this, "commands.stats.success", new Object[] { commandresultstats$type.getTypeName(), s2, s1 });
/*     */     }
/* 163 */     else if ("clear".equals(s)) {
/*     */       
/* 165 */       CommandResultStats.setScoreBoardStat(commandresultstats, commandresultstats$type, (String)null, (String)null);
/* 166 */       notifyOperators(sender, this, "commands.stats.cleared", new Object[] { commandresultstats$type.getTypeName() });
/*     */     } 
/*     */     
/* 169 */     if (flag) {
/*     */       
/* 171 */       BlockPos blockpos1 = parseBlockPos(sender, args, 1, false);
/* 172 */       TileEntity tileentity1 = world.getTileEntity(blockpos1);
/* 173 */       tileentity1.markDirty();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 181 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "entity", "block" }) : ((args.length == 2 && args[0].equals("entity")) ? getListOfStringsMatchingLastWord(args, func_175776_d()) : ((args.length >= 2 && args.length <= 4 && args[0].equals("block")) ? func_175771_a(args, 1, pos) : (((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block"))) ? (((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block"))) ? (((args.length != 6 || !args[0].equals("entity")) && (args.length != 8 || !args[0].equals("block"))) ? null : getListOfStringsMatchingLastWord(args, func_175777_e())) : getListOfStringsMatchingLastWord(args, CommandResultStats.Type.getTypeNames())) : getListOfStringsMatchingLastWord(args, new String[] { "set", "clear" }))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String[] func_175776_d() {
/* 186 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<String> func_175777_e() {
/* 191 */     Collection<ScoreObjective> collection = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
/* 192 */     List<String> list = Lists.newArrayList();
/*     */     
/* 194 */     for (ScoreObjective scoreobjective : collection) {
/*     */       
/* 196 */       if (!scoreobjective.getCriteria().isReadOnly())
/*     */       {
/* 198 */         list.add(scoreobjective.getName());
/*     */       }
/*     */     } 
/*     */     
/* 202 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 207 */     return (args.length > 0 && args[0].equals("entity") && index == 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */