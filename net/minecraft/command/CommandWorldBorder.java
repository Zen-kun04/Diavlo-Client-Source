/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ 
/*     */ public class CommandWorldBorder
/*     */   extends CommandBase {
/*     */   public String getCommandName() {
/*  14 */     return "worldborder";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  19 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  24 */     return "commands.worldborder.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  29 */     if (args.length < 1)
/*     */     {
/*  31 */       throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  35 */     WorldBorder worldborder = getWorldBorder();
/*     */     
/*  37 */     if (args[0].equals("set")) {
/*     */       
/*  39 */       if (args.length != 2 && args.length != 3)
/*     */       {
/*  41 */         throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
/*     */       }
/*     */       
/*  44 */       double d0 = worldborder.getTargetSize();
/*  45 */       double d2 = parseDouble(args[1], 1.0D, 6.0E7D);
/*  46 */       long i = (args.length > 2) ? (parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L;
/*     */       
/*  48 */       if (i > 0L) {
/*     */         
/*  50 */         worldborder.setTransition(d0, d2, i);
/*     */         
/*  52 */         if (d0 > d2)
/*     */         {
/*  54 */           notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d2) }), String.format("%.1f", new Object[] { Double.valueOf(d0) }), Long.toString(i / 1000L) });
/*     */         }
/*     */         else
/*     */         {
/*  58 */           notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d2) }), String.format("%.1f", new Object[] { Double.valueOf(d0) }), Long.toString(i / 1000L) });
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  63 */         worldborder.setTransition(d2);
/*  64 */         notifyOperators(sender, this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d2) }), String.format("%.1f", new Object[] { Double.valueOf(d0) }) });
/*     */       }
/*     */     
/*  67 */     } else if (args[0].equals("add")) {
/*     */       
/*  69 */       if (args.length != 2 && args.length != 3)
/*     */       {
/*  71 */         throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
/*     */       }
/*     */       
/*  74 */       double d4 = worldborder.getDiameter();
/*  75 */       double d8 = d4 + parseDouble(args[1], -d4, 6.0E7D - d4);
/*  76 */       long i1 = worldborder.getTimeUntilTarget() + ((args.length > 2) ? (parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L);
/*     */       
/*  78 */       if (i1 > 0L) {
/*     */         
/*  80 */         worldborder.setTransition(d4, d8, i1);
/*     */         
/*  82 */         if (d4 > d8)
/*     */         {
/*  84 */           notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d8) }), String.format("%.1f", new Object[] { Double.valueOf(d4) }), Long.toString(i1 / 1000L) });
/*     */         }
/*     */         else
/*     */         {
/*  88 */           notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d8) }), String.format("%.1f", new Object[] { Double.valueOf(d4) }), Long.toString(i1 / 1000L) });
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  93 */         worldborder.setTransition(d8);
/*  94 */         notifyOperators(sender, this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d8) }), String.format("%.1f", new Object[] { Double.valueOf(d4) }) });
/*     */       }
/*     */     
/*  97 */     } else if (args[0].equals("center")) {
/*     */       
/*  99 */       if (args.length != 3)
/*     */       {
/* 101 */         throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
/*     */       }
/*     */       
/* 104 */       BlockPos blockpos = sender.getPosition();
/* 105 */       double d1 = parseDouble(blockpos.getX() + 0.5D, args[1], true);
/* 106 */       double d3 = parseDouble(blockpos.getZ() + 0.5D, args[2], true);
/* 107 */       worldborder.setCenter(d1, d3);
/* 108 */       notifyOperators(sender, this, "commands.worldborder.center.success", new Object[] { Double.valueOf(d1), Double.valueOf(d3) });
/*     */     }
/* 110 */     else if (args[0].equals("damage")) {
/*     */       
/* 112 */       if (args.length < 2)
/*     */       {
/* 114 */         throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
/*     */       }
/*     */       
/* 117 */       if (args[1].equals("buffer"))
/*     */       {
/* 119 */         if (args.length != 3)
/*     */         {
/* 121 */           throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
/*     */         }
/*     */         
/* 124 */         double d5 = parseDouble(args[2], 0.0D);
/* 125 */         double d9 = worldborder.getDamageBuffer();
/* 126 */         worldborder.setDamageBuffer(d5);
/* 127 */         notifyOperators(sender, this, "commands.worldborder.damage.buffer.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d5) }), String.format("%.1f", new Object[] { Double.valueOf(d9) }) });
/*     */       }
/* 129 */       else if (args[1].equals("amount"))
/*     */       {
/* 131 */         if (args.length != 3)
/*     */         {
/* 133 */           throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
/*     */         }
/*     */         
/* 136 */         double d6 = parseDouble(args[2], 0.0D);
/* 137 */         double d10 = worldborder.getDamageAmount();
/* 138 */         worldborder.setDamageAmount(d6);
/* 139 */         notifyOperators(sender, this, "commands.worldborder.damage.amount.success", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d6) }), String.format("%.2f", new Object[] { Double.valueOf(d10) }) });
/*     */       }
/*     */     
/* 142 */     } else if (args[0].equals("warning")) {
/*     */       
/* 144 */       if (args.length < 2)
/*     */       {
/* 146 */         throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
/*     */       }
/*     */       
/* 149 */       int j = parseInt(args[2], 0);
/*     */       
/* 151 */       if (args[1].equals("time"))
/*     */       {
/* 153 */         if (args.length != 3)
/*     */         {
/* 155 */           throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
/*     */         }
/*     */         
/* 158 */         int k = worldborder.getWarningTime();
/* 159 */         worldborder.setWarningTime(j);
/* 160 */         notifyOperators(sender, this, "commands.worldborder.warning.time.success", new Object[] { Integer.valueOf(j), Integer.valueOf(k) });
/*     */       }
/* 162 */       else if (args[1].equals("distance"))
/*     */       {
/* 164 */         if (args.length != 3)
/*     */         {
/* 166 */           throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
/*     */         }
/*     */         
/* 169 */         int l = worldborder.getWarningDistance();
/* 170 */         worldborder.setWarningDistance(j);
/* 171 */         notifyOperators(sender, this, "commands.worldborder.warning.distance.success", new Object[] { Integer.valueOf(j), Integer.valueOf(l) });
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 176 */       if (!args[0].equals("get"))
/*     */       {
/* 178 */         throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
/*     */       }
/*     */       
/* 181 */       double d7 = worldborder.getDiameter();
/* 182 */       sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor_double(d7 + 0.5D));
/* 183 */       sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.worldborder.get.success", new Object[] { String.format("%.0f", new Object[] { Double.valueOf(d7) }) }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected WorldBorder getWorldBorder() {
/* 190 */     return (MinecraftServer.getServer()).worldServers[0].getWorldBorder();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 195 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "set", "center", "damage", "warning", "add", "get" }) : ((args.length == 2 && args[0].equals("damage")) ? getListOfStringsMatchingLastWord(args, new String[] { "buffer", "amount" }) : ((args.length >= 2 && args.length <= 3 && args[0].equals("center")) ? func_181043_b(args, 1, pos) : ((args.length == 2 && args[0].equals("warning")) ? getListOfStringsMatchingLastWord(args, new String[] { "time", "distance" }) : null)));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandWorldBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */