/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandSpreadPlayers extends CommandBase {
/*     */   public String getCommandName() {
/*  24 */     return "spreadplayers";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  29 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  34 */     return "commands.spreadplayers.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  39 */     if (args.length < 6)
/*     */     {
/*  41 */       throw new WrongUsageException("commands.spreadplayers.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  45 */     int i = 0;
/*  46 */     BlockPos blockpos = sender.getPosition();
/*  47 */     double d0 = parseDouble(blockpos.getX(), args[i++], true);
/*  48 */     double d1 = parseDouble(blockpos.getZ(), args[i++], true);
/*  49 */     double d2 = parseDouble(args[i++], 0.0D);
/*  50 */     double d3 = parseDouble(args[i++], d2 + 1.0D);
/*  51 */     boolean flag = parseBoolean(args[i++]);
/*  52 */     List<Entity> list = Lists.newArrayList();
/*     */     
/*  54 */     while (i < args.length) {
/*     */       
/*  56 */       String s = args[i++];
/*     */       
/*  58 */       if (PlayerSelector.hasArguments(s)) {
/*     */         
/*  60 */         List<Entity> list1 = PlayerSelector.matchEntities(sender, s, Entity.class);
/*     */         
/*  62 */         if (list1.size() == 0)
/*     */         {
/*  64 */           throw new EntityNotFoundException();
/*     */         }
/*     */         
/*  67 */         list.addAll(list1);
/*     */         
/*     */         continue;
/*     */       } 
/*  71 */       EntityPlayerMP entityPlayerMP = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(s);
/*     */       
/*  73 */       if (entityPlayerMP == null)
/*     */       {
/*  75 */         throw new PlayerNotFoundException();
/*     */       }
/*     */       
/*  78 */       list.add(entityPlayerMP);
/*     */     } 
/*     */ 
/*     */     
/*  82 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
/*     */     
/*  84 */     if (list.isEmpty())
/*     */     {
/*  86 */       throw new EntityNotFoundException();
/*     */     }
/*     */ 
/*     */     
/*  90 */     sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.spreadplayers.spreading." + (flag ? "teams" : "players"), new Object[] { Integer.valueOf(list.size()), Double.valueOf(d3), Double.valueOf(d0), Double.valueOf(d1), Double.valueOf(d2) }));
/*  91 */     func_110669_a(sender, list, new Position(d0, d1), d2, d3, ((Entity)list.get(0)).worldObj, flag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void func_110669_a(ICommandSender p_110669_1_, List<Entity> p_110669_2_, Position p_110669_3_, double p_110669_4_, double p_110669_6_, World worldIn, boolean p_110669_9_) throws CommandException {
/*  98 */     Random random = new Random();
/*  99 */     double d0 = p_110669_3_.field_111101_a - p_110669_6_;
/* 100 */     double d1 = p_110669_3_.field_111100_b - p_110669_6_;
/* 101 */     double d2 = p_110669_3_.field_111101_a + p_110669_6_;
/* 102 */     double d3 = p_110669_3_.field_111100_b + p_110669_6_;
/* 103 */     Position[] acommandspreadplayers$position = func_110670_a(random, p_110669_9_ ? func_110667_a(p_110669_2_) : p_110669_2_.size(), d0, d1, d2, d3);
/* 104 */     int i = func_110668_a(p_110669_3_, p_110669_4_, worldIn, random, d0, d1, d2, d3, acommandspreadplayers$position, p_110669_9_);
/* 105 */     double d4 = func_110671_a(p_110669_2_, worldIn, acommandspreadplayers$position, p_110669_9_);
/* 106 */     notifyOperators(p_110669_1_, this, "commands.spreadplayers.success." + (p_110669_9_ ? "teams" : "players"), new Object[] { Integer.valueOf(acommandspreadplayers$position.length), Double.valueOf(p_110669_3_.field_111101_a), Double.valueOf(p_110669_3_.field_111100_b) });
/*     */     
/* 108 */     if (acommandspreadplayers$position.length > 1)
/*     */     {
/* 110 */       p_110669_1_.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.spreadplayers.info." + (p_110669_9_ ? "teams" : "players"), new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d4) }), Integer.valueOf(i) }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private int func_110667_a(List<Entity> p_110667_1_) {
/* 116 */     Set<Team> set = Sets.newHashSet();
/*     */     
/* 118 */     for (Entity entity : p_110667_1_) {
/*     */       
/* 120 */       if (entity instanceof EntityPlayer) {
/*     */         
/* 122 */         set.add(((EntityPlayer)entity).getTeam());
/*     */         
/*     */         continue;
/*     */       } 
/* 126 */       set.add((Team)null);
/*     */     } 
/*     */ 
/*     */     
/* 130 */     return set.size();
/*     */   }
/*     */ 
/*     */   
/*     */   private int func_110668_a(Position p_110668_1_, double p_110668_2_, World worldIn, Random p_110668_5_, double p_110668_6_, double p_110668_8_, double p_110668_10_, double p_110668_12_, Position[] p_110668_14_, boolean p_110668_15_) throws CommandException {
/* 135 */     boolean flag = true;
/* 136 */     double d0 = 3.4028234663852886E38D;
/*     */     
/*     */     int i;
/* 139 */     for (i = 0; i < 10000 && flag; i++) {
/*     */       
/* 141 */       flag = false;
/* 142 */       d0 = 3.4028234663852886E38D;
/*     */       
/* 144 */       for (int j = 0; j < p_110668_14_.length; j++) {
/*     */         
/* 146 */         Position commandspreadplayers$position = p_110668_14_[j];
/* 147 */         int k = 0;
/* 148 */         Position commandspreadplayers$position1 = new Position();
/*     */         
/* 150 */         for (int l = 0; l < p_110668_14_.length; l++) {
/*     */           
/* 152 */           if (j != l) {
/*     */             
/* 154 */             Position commandspreadplayers$position2 = p_110668_14_[l];
/* 155 */             double d1 = commandspreadplayers$position.func_111099_a(commandspreadplayers$position2);
/* 156 */             d0 = Math.min(d1, d0);
/*     */             
/* 158 */             if (d1 < p_110668_2_) {
/*     */               
/* 160 */               k++;
/* 161 */               commandspreadplayers$position1.field_111101_a += commandspreadplayers$position2.field_111101_a - commandspreadplayers$position.field_111101_a;
/* 162 */               commandspreadplayers$position1.field_111100_b += commandspreadplayers$position2.field_111100_b - commandspreadplayers$position.field_111100_b;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 167 */         if (k > 0) {
/*     */           
/* 169 */           commandspreadplayers$position1.field_111101_a /= k;
/* 170 */           commandspreadplayers$position1.field_111100_b /= k;
/* 171 */           double d2 = commandspreadplayers$position1.func_111096_b();
/*     */           
/* 173 */           if (d2 > 0.0D) {
/*     */             
/* 175 */             commandspreadplayers$position1.func_111095_a();
/* 176 */             commandspreadplayers$position.func_111094_b(commandspreadplayers$position1);
/*     */           }
/*     */           else {
/*     */             
/* 180 */             commandspreadplayers$position.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
/*     */           } 
/*     */           
/* 183 */           flag = true;
/*     */         } 
/*     */         
/* 186 */         if (commandspreadplayers$position.func_111093_a(p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_))
/*     */         {
/* 188 */           flag = true;
/*     */         }
/*     */       } 
/*     */       
/* 192 */       if (!flag)
/*     */       {
/* 194 */         for (Position commandspreadplayers$position3 : p_110668_14_) {
/*     */           
/* 196 */           if (!commandspreadplayers$position3.func_111098_b(worldIn)) {
/*     */             
/* 198 */             commandspreadplayers$position3.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
/* 199 */             flag = true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 205 */     if (i >= 10000)
/*     */     {
/* 207 */       throw new CommandException("commands.spreadplayers.failure." + (p_110668_15_ ? "teams" : "players"), new Object[] { Integer.valueOf(p_110668_14_.length), Double.valueOf(p_110668_1_.field_111101_a), Double.valueOf(p_110668_1_.field_111100_b), String.format("%.2f", new Object[] { Double.valueOf(d0) }) });
/*     */     }
/*     */ 
/*     */     
/* 211 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private double func_110671_a(List<Entity> p_110671_1_, World worldIn, Position[] p_110671_3_, boolean p_110671_4_) {
/* 217 */     double d0 = 0.0D;
/* 218 */     int i = 0;
/* 219 */     Map<Team, Position> map = Maps.newHashMap();
/*     */     
/* 221 */     for (int j = 0; j < p_110671_1_.size(); j++) {
/*     */       Position commandspreadplayers$position;
/* 223 */       Entity entity = p_110671_1_.get(j);
/*     */ 
/*     */       
/* 226 */       if (p_110671_4_) {
/*     */         
/* 228 */         Team team = (entity instanceof EntityPlayer) ? ((EntityPlayer)entity).getTeam() : null;
/*     */         
/* 230 */         if (!map.containsKey(team))
/*     */         {
/* 232 */           map.put(team, p_110671_3_[i++]);
/*     */         }
/*     */         
/* 235 */         commandspreadplayers$position = map.get(team);
/*     */       }
/*     */       else {
/*     */         
/* 239 */         commandspreadplayers$position = p_110671_3_[i++];
/*     */       } 
/*     */       
/* 242 */       entity.setPositionAndUpdate((MathHelper.floor_double(commandspreadplayers$position.field_111101_a) + 0.5F), commandspreadplayers$position.func_111092_a(worldIn), MathHelper.floor_double(commandspreadplayers$position.field_111100_b) + 0.5D);
/* 243 */       double d2 = Double.MAX_VALUE;
/*     */       
/* 245 */       for (int k = 0; k < p_110671_3_.length; k++) {
/*     */         
/* 247 */         if (commandspreadplayers$position != p_110671_3_[k]) {
/*     */           
/* 249 */           double d1 = commandspreadplayers$position.func_111099_a(p_110671_3_[k]);
/* 250 */           d2 = Math.min(d1, d2);
/*     */         } 
/*     */       } 
/*     */       
/* 254 */       d0 += d2;
/*     */     } 
/*     */     
/* 257 */     d0 /= p_110671_1_.size();
/* 258 */     return d0;
/*     */   }
/*     */ 
/*     */   
/*     */   private Position[] func_110670_a(Random p_110670_1_, int p_110670_2_, double p_110670_3_, double p_110670_5_, double p_110670_7_, double p_110670_9_) {
/* 263 */     Position[] acommandspreadplayers$position = new Position[p_110670_2_];
/*     */     
/* 265 */     for (int i = 0; i < acommandspreadplayers$position.length; i++) {
/*     */       
/* 267 */       Position commandspreadplayers$position = new Position();
/* 268 */       commandspreadplayers$position.func_111097_a(p_110670_1_, p_110670_3_, p_110670_5_, p_110670_7_, p_110670_9_);
/* 269 */       acommandspreadplayers$position[i] = commandspreadplayers$position;
/*     */     } 
/*     */     
/* 272 */     return acommandspreadplayers$position;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 277 */     return (args.length >= 1 && args.length <= 2) ? func_181043_b(args, 0, pos) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   static class Position
/*     */   {
/*     */     double field_111101_a;
/*     */     
/*     */     double field_111100_b;
/*     */ 
/*     */     
/*     */     Position() {}
/*     */     
/*     */     Position(double p_i1358_1_, double p_i1358_3_) {
/* 291 */       this.field_111101_a = p_i1358_1_;
/* 292 */       this.field_111100_b = p_i1358_3_;
/*     */     }
/*     */ 
/*     */     
/*     */     double func_111099_a(Position p_111099_1_) {
/* 297 */       double d0 = this.field_111101_a - p_111099_1_.field_111101_a;
/* 298 */       double d1 = this.field_111100_b - p_111099_1_.field_111100_b;
/* 299 */       return Math.sqrt(d0 * d0 + d1 * d1);
/*     */     }
/*     */ 
/*     */     
/*     */     void func_111095_a() {
/* 304 */       double d0 = func_111096_b();
/* 305 */       this.field_111101_a /= d0;
/* 306 */       this.field_111100_b /= d0;
/*     */     }
/*     */ 
/*     */     
/*     */     float func_111096_b() {
/* 311 */       return MathHelper.sqrt_double(this.field_111101_a * this.field_111101_a + this.field_111100_b * this.field_111100_b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void func_111094_b(Position p_111094_1_) {
/* 316 */       this.field_111101_a -= p_111094_1_.field_111101_a;
/* 317 */       this.field_111100_b -= p_111094_1_.field_111100_b;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean func_111093_a(double p_111093_1_, double p_111093_3_, double p_111093_5_, double p_111093_7_) {
/* 322 */       boolean flag = false;
/*     */       
/* 324 */       if (this.field_111101_a < p_111093_1_) {
/*     */         
/* 326 */         this.field_111101_a = p_111093_1_;
/* 327 */         flag = true;
/*     */       }
/* 329 */       else if (this.field_111101_a > p_111093_5_) {
/*     */         
/* 331 */         this.field_111101_a = p_111093_5_;
/* 332 */         flag = true;
/*     */       } 
/*     */       
/* 335 */       if (this.field_111100_b < p_111093_3_) {
/*     */         
/* 337 */         this.field_111100_b = p_111093_3_;
/* 338 */         flag = true;
/*     */       }
/* 340 */       else if (this.field_111100_b > p_111093_7_) {
/*     */         
/* 342 */         this.field_111100_b = p_111093_7_;
/* 343 */         flag = true;
/*     */       } 
/*     */       
/* 346 */       return flag;
/*     */     }
/*     */ 
/*     */     
/*     */     public int func_111092_a(World worldIn) {
/* 351 */       BlockPos blockpos = new BlockPos(this.field_111101_a, 256.0D, this.field_111100_b);
/*     */       
/* 353 */       while (blockpos.getY() > 0) {
/*     */         
/* 355 */         blockpos = blockpos.down();
/*     */         
/* 357 */         if (worldIn.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
/*     */         {
/* 359 */           return blockpos.getY() + 1;
/*     */         }
/*     */       } 
/*     */       
/* 363 */       return 257;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean func_111098_b(World worldIn) {
/* 368 */       BlockPos blockpos = new BlockPos(this.field_111101_a, 256.0D, this.field_111100_b);
/*     */       
/* 370 */       while (blockpos.getY() > 0) {
/*     */         
/* 372 */         blockpos = blockpos.down();
/* 373 */         Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
/*     */         
/* 375 */         if (material != Material.air)
/*     */         {
/* 377 */           return (!material.isLiquid() && material != Material.fire);
/*     */         }
/*     */       } 
/*     */       
/* 381 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void func_111097_a(Random p_111097_1_, double p_111097_2_, double p_111097_4_, double p_111097_6_, double p_111097_8_) {
/* 386 */       this.field_111101_a = MathHelper.getRandomDoubleInRange(p_111097_1_, p_111097_2_, p_111097_6_);
/* 387 */       this.field_111100_b = MathHelper.getRandomDoubleInRange(p_111097_1_, p_111097_4_, p_111097_8_);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandSpreadPlayers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */