/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import net.minecraft.profiler.Profiler;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CommandDebug
/*     */   extends CommandBase {
/*  16 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   private long profileStartTime;
/*     */   private int profileStartTick;
/*     */   
/*     */   public String getCommandName() {
/*  22 */     return "debug";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  27 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  32 */     return "commands.debug.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  37 */     if (args.length < 1)
/*     */     {
/*  39 */       throw new WrongUsageException("commands.debug.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  43 */     if (args[0].equals("start")) {
/*     */       
/*  45 */       if (args.length != 1)
/*     */       {
/*  47 */         throw new WrongUsageException("commands.debug.usage", new Object[0]);
/*     */       }
/*     */       
/*  50 */       notifyOperators(sender, this, "commands.debug.start", new Object[0]);
/*  51 */       MinecraftServer.getServer().enableProfiling();
/*  52 */       this.profileStartTime = MinecraftServer.getCurrentTimeMillis();
/*  53 */       this.profileStartTick = MinecraftServer.getServer().getTickCounter();
/*     */     }
/*     */     else {
/*     */       
/*  57 */       if (!args[0].equals("stop"))
/*     */       {
/*  59 */         throw new WrongUsageException("commands.debug.usage", new Object[0]);
/*     */       }
/*     */       
/*  62 */       if (args.length != 1)
/*     */       {
/*  64 */         throw new WrongUsageException("commands.debug.usage", new Object[0]);
/*     */       }
/*     */       
/*  67 */       if (!(MinecraftServer.getServer()).theProfiler.profilingEnabled)
/*     */       {
/*  69 */         throw new CommandException("commands.debug.notStarted", new Object[0]);
/*     */       }
/*     */       
/*  72 */       long i = MinecraftServer.getCurrentTimeMillis();
/*  73 */       int j = MinecraftServer.getServer().getTickCounter();
/*  74 */       long k = i - this.profileStartTime;
/*  75 */       int l = j - this.profileStartTick;
/*  76 */       saveProfileResults(k, l);
/*  77 */       (MinecraftServer.getServer()).theProfiler.profilingEnabled = false;
/*  78 */       notifyOperators(sender, this, "commands.debug.stop", new Object[] { Float.valueOf((float)k / 1000.0F), Integer.valueOf(l) });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void saveProfileResults(long timeSpan, int tickSpan) {
/*  85 */     File file1 = new File(MinecraftServer.getServer().getFile("debug"), "profile-results-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");
/*  86 */     file1.getParentFile().mkdirs();
/*     */ 
/*     */     
/*     */     try {
/*  90 */       FileWriter filewriter = new FileWriter(file1);
/*  91 */       filewriter.write(getProfileResults(timeSpan, tickSpan));
/*  92 */       filewriter.close();
/*     */     }
/*  94 */     catch (Throwable throwable) {
/*     */       
/*  96 */       logger.error("Could not save profiler results to " + file1, throwable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String getProfileResults(long timeSpan, int tickSpan) {
/* 102 */     StringBuilder stringbuilder = new StringBuilder();
/* 103 */     stringbuilder.append("---- Minecraft Profiler Results ----\n");
/* 104 */     stringbuilder.append("// ");
/* 105 */     stringbuilder.append(getWittyComment());
/* 106 */     stringbuilder.append("\n\n");
/* 107 */     stringbuilder.append("Time span: ").append(timeSpan).append(" ms\n");
/* 108 */     stringbuilder.append("Tick span: ").append(tickSpan).append(" ticks\n");
/* 109 */     stringbuilder.append("// This is approximately ").append(String.format("%.2f", new Object[] { Float.valueOf(tickSpan / (float)timeSpan / 1000.0F) })).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
/* 110 */     stringbuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
/* 111 */     func_147202_a(0, "root", stringbuilder);
/* 112 */     stringbuilder.append("--- END PROFILE DUMP ---\n\n");
/* 113 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_147202_a(int p_147202_1_, String p_147202_2_, StringBuilder stringBuilder) {
/* 118 */     List<Profiler.Result> list = (MinecraftServer.getServer()).theProfiler.getProfilingData(p_147202_2_);
/*     */     
/* 120 */     if (list != null && list.size() >= 3)
/*     */     {
/* 122 */       for (int i = 1; i < list.size(); i++) {
/*     */         
/* 124 */         Profiler.Result profiler$result = list.get(i);
/* 125 */         stringBuilder.append(String.format("[%02d] ", new Object[] { Integer.valueOf(p_147202_1_) }));
/*     */         
/* 127 */         for (int j = 0; j < p_147202_1_; j++)
/*     */         {
/* 129 */           stringBuilder.append(" ");
/*     */         }
/*     */         
/* 132 */         stringBuilder.append(profiler$result.field_76331_c).append(" - ").append(String.format("%.2f", new Object[] { Double.valueOf(profiler$result.field_76332_a) })).append("%/").append(String.format("%.2f", new Object[] { Double.valueOf(profiler$result.field_76330_b) })).append("%\n");
/*     */         
/* 134 */         if (!profiler$result.field_76331_c.equals("unspecified")) {
/*     */           
/*     */           try {
/*     */             
/* 138 */             func_147202_a(p_147202_1_ + 1, p_147202_2_ + "." + profiler$result.field_76331_c, stringBuilder);
/*     */           }
/* 140 */           catch (Exception exception) {
/*     */             
/* 142 */             stringBuilder.append("[[ EXCEPTION ").append(exception).append(" ]]");
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getWittyComment() {
/* 151 */     String[] astring = { "Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server." };
/*     */ 
/*     */     
/*     */     try {
/* 155 */       return astring[(int)(System.nanoTime() % astring.length)];
/*     */     }
/* 157 */     catch (Throwable var2) {
/*     */       
/* 159 */       return "Witty comment unavailable :(";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 165 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "start", "stop" }) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandDebug.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */