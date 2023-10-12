/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.WorldServer;
/*    */ import net.minecraft.world.storage.WorldInfo;
/*    */ 
/*    */ public class CommandWeather
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/* 14 */     return "weather";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 19 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 24 */     return "commands.weather.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 29 */     if (args.length >= 1 && args.length <= 2) {
/*    */       
/* 31 */       int i = (300 + (new Random()).nextInt(600)) * 20;
/*    */       
/* 33 */       if (args.length >= 2)
/*    */       {
/* 35 */         i = parseInt(args[1], 1, 1000000) * 20;
/*    */       }
/*    */       
/* 38 */       WorldServer worldServer = (MinecraftServer.getServer()).worldServers[0];
/* 39 */       WorldInfo worldinfo = worldServer.getWorldInfo();
/*    */       
/* 41 */       if ("clear".equalsIgnoreCase(args[0]))
/*    */       {
/* 43 */         worldinfo.setCleanWeatherTime(i);
/* 44 */         worldinfo.setRainTime(0);
/* 45 */         worldinfo.setThunderTime(0);
/* 46 */         worldinfo.setRaining(false);
/* 47 */         worldinfo.setThundering(false);
/* 48 */         notifyOperators(sender, this, "commands.weather.clear", new Object[0]);
/*    */       }
/* 50 */       else if ("rain".equalsIgnoreCase(args[0]))
/*    */       {
/* 52 */         worldinfo.setCleanWeatherTime(0);
/* 53 */         worldinfo.setRainTime(i);
/* 54 */         worldinfo.setThunderTime(i);
/* 55 */         worldinfo.setRaining(true);
/* 56 */         worldinfo.setThundering(false);
/* 57 */         notifyOperators(sender, this, "commands.weather.rain", new Object[0]);
/*    */       }
/*    */       else
/*    */       {
/* 61 */         if (!"thunder".equalsIgnoreCase(args[0]))
/*    */         {
/* 63 */           throw new WrongUsageException("commands.weather.usage", new Object[0]);
/*    */         }
/*    */         
/* 66 */         worldinfo.setCleanWeatherTime(0);
/* 67 */         worldinfo.setRainTime(i);
/* 68 */         worldinfo.setThunderTime(i);
/* 69 */         worldinfo.setRaining(true);
/* 70 */         worldinfo.setThundering(true);
/* 71 */         notifyOperators(sender, this, "commands.weather.thunder", new Object[0]);
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 76 */       throw new WrongUsageException("commands.weather.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 82 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "clear", "rain", "thunder" }) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandWeather.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */