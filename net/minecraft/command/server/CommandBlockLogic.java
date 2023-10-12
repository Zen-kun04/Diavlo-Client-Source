/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.command.CommandResultStats;
/*     */ import net.minecraft.command.ICommandManager;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class CommandBlockLogic
/*     */   implements ICommandSender {
/*  22 */   private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");
/*     */   private int successCount;
/*     */   private boolean trackOutput = true;
/*  25 */   private IChatComponent lastOutput = null;
/*  26 */   private String commandStored = "";
/*  27 */   private String customName = "@";
/*  28 */   private final CommandResultStats resultStats = new CommandResultStats();
/*     */ 
/*     */   
/*     */   public int getSuccessCount() {
/*  32 */     return this.successCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getLastOutput() {
/*  37 */     return this.lastOutput;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeDataToNBT(NBTTagCompound tagCompound) {
/*  42 */     tagCompound.setString("Command", this.commandStored);
/*  43 */     tagCompound.setInteger("SuccessCount", this.successCount);
/*  44 */     tagCompound.setString("CustomName", this.customName);
/*  45 */     tagCompound.setBoolean("TrackOutput", this.trackOutput);
/*     */     
/*  47 */     if (this.lastOutput != null && this.trackOutput)
/*     */     {
/*  49 */       tagCompound.setString("LastOutput", IChatComponent.Serializer.componentToJson(this.lastOutput));
/*     */     }
/*     */     
/*  52 */     this.resultStats.writeStatsToNBT(tagCompound);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readDataFromNBT(NBTTagCompound nbt) {
/*  57 */     this.commandStored = nbt.getString("Command");
/*  58 */     this.successCount = nbt.getInteger("SuccessCount");
/*     */     
/*  60 */     if (nbt.hasKey("CustomName", 8))
/*     */     {
/*  62 */       this.customName = nbt.getString("CustomName");
/*     */     }
/*     */     
/*  65 */     if (nbt.hasKey("TrackOutput", 1))
/*     */     {
/*  67 */       this.trackOutput = nbt.getBoolean("TrackOutput");
/*     */     }
/*     */     
/*  70 */     if (nbt.hasKey("LastOutput", 8) && this.trackOutput)
/*     */     {
/*  72 */       this.lastOutput = IChatComponent.Serializer.jsonToComponent(nbt.getString("LastOutput"));
/*     */     }
/*     */     
/*  75 */     this.resultStats.readStatsFromNBT(nbt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/*  80 */     return (permLevel <= 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCommand(String command) {
/*  85 */     this.commandStored = command;
/*  86 */     this.successCount = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommand() {
/*  91 */     return this.commandStored;
/*     */   }
/*     */ 
/*     */   
/*     */   public void trigger(World worldIn) {
/*  96 */     if (worldIn.isRemote)
/*     */     {
/*  98 */       this.successCount = 0;
/*     */     }
/*     */     
/* 101 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/* 103 */     if (minecraftserver != null && minecraftserver.isAnvilFileSet() && minecraftserver.isCommandBlockEnabled()) {
/*     */       
/* 105 */       ICommandManager icommandmanager = minecraftserver.getCommandManager();
/*     */ 
/*     */       
/*     */       try {
/* 109 */         this.lastOutput = null;
/* 110 */         this.successCount = icommandmanager.executeCommand(this, this.commandStored);
/*     */       }
/* 112 */       catch (Throwable throwable) {
/*     */         
/* 114 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Executing command block");
/* 115 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Command to be executed");
/* 116 */         crashreportcategory.addCrashSectionCallable("Command", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/* 120 */                 return CommandBlockLogic.this.getCommand();
/*     */               }
/*     */             });
/* 123 */         crashreportcategory.addCrashSectionCallable("Name", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/* 127 */                 return CommandBlockLogic.this.getName();
/*     */               }
/*     */             });
/* 130 */         throw new ReportedException(crashreport);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 135 */       this.successCount = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 141 */     return this.customName;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getDisplayName() {
/* 146 */     return (IChatComponent)new ChatComponentText(getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(String p_145754_1_) {
/* 151 */     this.customName = p_145754_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChatMessage(IChatComponent component) {
/* 156 */     if (this.trackOutput && getEntityWorld() != null && !(getEntityWorld()).isRemote) {
/*     */       
/* 158 */       this.lastOutput = (new ChatComponentText("[" + timestampFormat.format(new Date()) + "] ")).appendSibling(component);
/* 159 */       updateCommand();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean sendCommandFeedback() {
/* 165 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 166 */     return (minecraftserver == null || !minecraftserver.isAnvilFileSet() || minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCommandStat(CommandResultStats.Type type, int amount) {
/* 171 */     this.resultStats.setCommandStatScore(this, type, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void updateCommand();
/*     */   
/*     */   public abstract int func_145751_f();
/*     */   
/*     */   public abstract void func_145757_a(ByteBuf paramByteBuf);
/*     */   
/*     */   public void setLastOutput(IChatComponent lastOutputMessage) {
/* 182 */     this.lastOutput = lastOutputMessage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTrackOutput(boolean shouldTrackOutput) {
/* 187 */     this.trackOutput = shouldTrackOutput;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldTrackOutput() {
/* 192 */     return this.trackOutput;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryOpenEditCommandBlock(EntityPlayer playerIn) {
/* 197 */     if (!playerIn.capabilities.isCreativeMode)
/*     */     {
/* 199 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 203 */     if ((playerIn.getEntityWorld()).isRemote)
/*     */     {
/* 205 */       playerIn.openEditCommandBlock(this);
/*     */     }
/*     */     
/* 208 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandResultStats getCommandResultStats() {
/* 214 */     return this.resultStats;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandBlockLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */