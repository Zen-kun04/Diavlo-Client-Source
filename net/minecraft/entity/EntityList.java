/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.ai.EntityMinecartMobSpawner;
/*     */ import net.minecraft.entity.boss.EntityDragon;
/*     */ import net.minecraft.entity.boss.EntityWither;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.entity.item.EntityEnderEye;
/*     */ import net.minecraft.entity.item.EntityEnderPearl;
/*     */ import net.minecraft.entity.item.EntityExpBottle;
/*     */ import net.minecraft.entity.item.EntityFallingBlock;
/*     */ import net.minecraft.entity.item.EntityFireworkRocket;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.item.EntityMinecartChest;
/*     */ import net.minecraft.entity.item.EntityMinecartEmpty;
/*     */ import net.minecraft.entity.item.EntityMinecartFurnace;
/*     */ import net.minecraft.entity.item.EntityMinecartHopper;
/*     */ import net.minecraft.entity.item.EntityMinecartTNT;
/*     */ import net.minecraft.entity.item.EntityPainting;
/*     */ import net.minecraft.entity.item.EntityTNTPrimed;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.monster.EntityBlaze;
/*     */ import net.minecraft.entity.monster.EntityCaveSpider;
/*     */ import net.minecraft.entity.monster.EntityCreeper;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntityEndermite;
/*     */ import net.minecraft.entity.monster.EntityGhast;
/*     */ import net.minecraft.entity.monster.EntityGiantZombie;
/*     */ import net.minecraft.entity.monster.EntityGuardian;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
/*     */ import net.minecraft.entity.monster.EntityMagmaCube;
/*     */ import net.minecraft.entity.monster.EntityMob;
/*     */ import net.minecraft.entity.monster.EntityPigZombie;
/*     */ import net.minecraft.entity.monster.EntitySilverfish;
/*     */ import net.minecraft.entity.monster.EntitySkeleton;
/*     */ import net.minecraft.entity.monster.EntitySlime;
/*     */ import net.minecraft.entity.monster.EntitySnowman;
/*     */ import net.minecraft.entity.monster.EntitySpider;
/*     */ import net.minecraft.entity.monster.EntityWitch;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntityBat;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityCow;
/*     */ import net.minecraft.entity.passive.EntityHorse;
/*     */ import net.minecraft.entity.passive.EntityMooshroom;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.entity.passive.EntityPig;
/*     */ import net.minecraft.entity.passive.EntityRabbit;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityEgg;
/*     */ import net.minecraft.entity.projectile.EntityLargeFireball;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.entity.projectile.EntitySmallFireball;
/*     */ import net.minecraft.entity.projectile.EntitySnowball;
/*     */ import net.minecraft.entity.projectile.EntityWitherSkull;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityList
/*     */ {
/*  79 */   private static final Logger logger = LogManager.getLogger();
/*  80 */   private static final Map<String, Class<? extends Entity>> stringToClassMapping = Maps.newHashMap();
/*  81 */   private static final Map<Class<? extends Entity>, String> classToStringMapping = Maps.newHashMap();
/*  82 */   private static final Map<Integer, Class<? extends Entity>> idToClassMapping = Maps.newHashMap();
/*  83 */   private static final Map<Class<? extends Entity>, Integer> classToIDMapping = Maps.newHashMap();
/*  84 */   private static final Map<String, Integer> stringToIDMapping = Maps.newHashMap();
/*  85 */   public static final Map<Integer, EntityEggInfo> entityEggs = Maps.newLinkedHashMap();
/*     */ 
/*     */   
/*     */   private static void addMapping(Class<? extends Entity> entityClass, String entityName, int id) {
/*  89 */     if (stringToClassMapping.containsKey(entityName))
/*     */     {
/*  91 */       throw new IllegalArgumentException("ID is already registered: " + entityName);
/*     */     }
/*  93 */     if (idToClassMapping.containsKey(Integer.valueOf(id)))
/*     */     {
/*  95 */       throw new IllegalArgumentException("ID is already registered: " + id);
/*     */     }
/*  97 */     if (id == 0)
/*     */     {
/*  99 */       throw new IllegalArgumentException("Cannot register to reserved id: " + id);
/*     */     }
/* 101 */     if (entityClass == null)
/*     */     {
/* 103 */       throw new IllegalArgumentException("Cannot register null clazz for id: " + id);
/*     */     }
/*     */ 
/*     */     
/* 107 */     stringToClassMapping.put(entityName, entityClass);
/* 108 */     classToStringMapping.put(entityClass, entityName);
/* 109 */     idToClassMapping.put(Integer.valueOf(id), entityClass);
/* 110 */     classToIDMapping.put(entityClass, Integer.valueOf(id));
/* 111 */     stringToIDMapping.put(entityName, Integer.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addMapping(Class<? extends Entity> entityClass, String entityName, int entityID, int baseColor, int spotColor) {
/* 117 */     addMapping(entityClass, entityName, entityID);
/* 118 */     entityEggs.put(Integer.valueOf(entityID), new EntityEggInfo(entityID, baseColor, spotColor));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Entity createEntityByName(String entityName, World worldIn) {
/* 123 */     Entity entity = null;
/*     */ 
/*     */     
/*     */     try {
/* 127 */       Class<? extends Entity> oclass = stringToClassMapping.get(entityName);
/*     */       
/* 129 */       if (oclass != null)
/*     */       {
/* 131 */         entity = oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
/*     */       }
/*     */     }
/* 134 */     catch (Exception exception) {
/*     */       
/* 136 */       exception.printStackTrace();
/*     */     } 
/*     */     
/* 139 */     return entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Entity createEntityFromNBT(NBTTagCompound nbt, World worldIn) {
/* 144 */     Entity entity = null;
/*     */     
/* 146 */     if ("Minecart".equals(nbt.getString("id"))) {
/*     */       
/* 148 */       nbt.setString("id", EntityMinecart.EnumMinecartType.byNetworkID(nbt.getInteger("Type")).getName());
/* 149 */       nbt.removeTag("Type");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 154 */       Class<? extends Entity> oclass = stringToClassMapping.get(nbt.getString("id"));
/*     */       
/* 156 */       if (oclass != null)
/*     */       {
/* 158 */         entity = oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
/*     */       }
/*     */     }
/* 161 */     catch (Exception exception) {
/*     */       
/* 163 */       exception.printStackTrace();
/*     */     } 
/*     */     
/* 166 */     if (entity != null) {
/*     */       
/* 168 */       entity.readFromNBT(nbt);
/*     */     }
/*     */     else {
/*     */       
/* 172 */       logger.warn("Skipping Entity with id " + nbt.getString("id"));
/*     */     } 
/*     */     
/* 175 */     return entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Entity createEntityByID(int entityID, World worldIn) {
/* 180 */     Entity entity = null;
/*     */ 
/*     */     
/*     */     try {
/* 184 */       Class<? extends Entity> oclass = getClassFromID(entityID);
/*     */       
/* 186 */       if (oclass != null)
/*     */       {
/* 188 */         entity = oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
/*     */       }
/*     */     }
/* 191 */     catch (Exception exception) {
/*     */       
/* 193 */       exception.printStackTrace();
/*     */     } 
/*     */     
/* 196 */     if (entity == null)
/*     */     {
/* 198 */       logger.warn("Skipping Entity with id " + entityID);
/*     */     }
/*     */     
/* 201 */     return entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getEntityID(Entity entityIn) {
/* 206 */     Integer integer = classToIDMapping.get(entityIn.getClass());
/* 207 */     return (integer == null) ? 0 : integer.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class<? extends Entity> getClassFromID(int entityID) {
/* 212 */     return idToClassMapping.get(Integer.valueOf(entityID));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getEntityString(Entity entityIn) {
/* 217 */     return classToStringMapping.get(entityIn.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getIDFromString(String entityName) {
/* 222 */     Integer integer = stringToIDMapping.get(entityName);
/* 223 */     return (integer == null) ? 90 : integer.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getStringFromID(int entityID) {
/* 228 */     return classToStringMapping.get(getClassFromID(entityID));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void func_151514_a() {}
/*     */ 
/*     */   
/*     */   public static List<String> getEntityNameList() {
/* 237 */     Set<String> set = stringToClassMapping.keySet();
/* 238 */     List<String> list = Lists.newArrayList();
/*     */     
/* 240 */     for (String s : set) {
/*     */       
/* 242 */       Class<? extends Entity> oclass = stringToClassMapping.get(s);
/*     */       
/* 244 */       if ((oclass.getModifiers() & 0x400) != 1024)
/*     */       {
/* 246 */         list.add(s);
/*     */       }
/*     */     } 
/*     */     
/* 250 */     list.add("LightningBolt");
/* 251 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isStringEntityName(Entity entityIn, String entityName) {
/* 256 */     String s = getEntityString(entityIn);
/*     */     
/* 258 */     if (s == null && entityIn instanceof net.minecraft.entity.player.EntityPlayer) {
/*     */       
/* 260 */       s = "Player";
/*     */     }
/* 262 */     else if (s == null && entityIn instanceof net.minecraft.entity.effect.EntityLightningBolt) {
/*     */       
/* 264 */       s = "LightningBolt";
/*     */     } 
/*     */     
/* 267 */     return entityName.equals(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isStringValidEntityName(String entityName) {
/* 272 */     return ("Player".equals(entityName) || getEntityNameList().contains(entityName));
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 277 */     addMapping((Class)EntityItem.class, "Item", 1);
/* 278 */     addMapping((Class)EntityXPOrb.class, "XPOrb", 2);
/* 279 */     addMapping((Class)EntityEgg.class, "ThrownEgg", 7);
/* 280 */     addMapping((Class)EntityLeashKnot.class, "LeashKnot", 8);
/* 281 */     addMapping((Class)EntityPainting.class, "Painting", 9);
/* 282 */     addMapping((Class)EntityArrow.class, "Arrow", 10);
/* 283 */     addMapping((Class)EntitySnowball.class, "Snowball", 11);
/* 284 */     addMapping((Class)EntityLargeFireball.class, "Fireball", 12);
/* 285 */     addMapping((Class)EntitySmallFireball.class, "SmallFireball", 13);
/* 286 */     addMapping((Class)EntityEnderPearl.class, "ThrownEnderpearl", 14);
/* 287 */     addMapping((Class)EntityEnderEye.class, "EyeOfEnderSignal", 15);
/* 288 */     addMapping((Class)EntityPotion.class, "ThrownPotion", 16);
/* 289 */     addMapping((Class)EntityExpBottle.class, "ThrownExpBottle", 17);
/* 290 */     addMapping((Class)EntityItemFrame.class, "ItemFrame", 18);
/* 291 */     addMapping((Class)EntityWitherSkull.class, "WitherSkull", 19);
/* 292 */     addMapping((Class)EntityTNTPrimed.class, "PrimedTnt", 20);
/* 293 */     addMapping((Class)EntityFallingBlock.class, "FallingSand", 21);
/* 294 */     addMapping((Class)EntityFireworkRocket.class, "FireworksRocketEntity", 22);
/* 295 */     addMapping((Class)EntityArmorStand.class, "ArmorStand", 30);
/* 296 */     addMapping((Class)EntityBoat.class, "Boat", 41);
/* 297 */     addMapping((Class)EntityMinecartEmpty.class, EntityMinecart.EnumMinecartType.RIDEABLE.getName(), 42);
/* 298 */     addMapping((Class)EntityMinecartChest.class, EntityMinecart.EnumMinecartType.CHEST.getName(), 43);
/* 299 */     addMapping((Class)EntityMinecartFurnace.class, EntityMinecart.EnumMinecartType.FURNACE.getName(), 44);
/* 300 */     addMapping((Class)EntityMinecartTNT.class, EntityMinecart.EnumMinecartType.TNT.getName(), 45);
/* 301 */     addMapping((Class)EntityMinecartHopper.class, EntityMinecart.EnumMinecartType.HOPPER.getName(), 46);
/* 302 */     addMapping((Class)EntityMinecartMobSpawner.class, EntityMinecart.EnumMinecartType.SPAWNER.getName(), 47);
/* 303 */     addMapping((Class)EntityMinecartCommandBlock.class, EntityMinecart.EnumMinecartType.COMMAND_BLOCK.getName(), 40);
/* 304 */     addMapping((Class)EntityLiving.class, "Mob", 48);
/* 305 */     addMapping((Class)EntityMob.class, "Monster", 49);
/* 306 */     addMapping((Class)EntityCreeper.class, "Creeper", 50, 894731, 0);
/* 307 */     addMapping((Class)EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
/* 308 */     addMapping((Class)EntitySpider.class, "Spider", 52, 3419431, 11013646);
/* 309 */     addMapping((Class)EntityGiantZombie.class, "Giant", 53);
/* 310 */     addMapping((Class)EntityZombie.class, "Zombie", 54, 44975, 7969893);
/* 311 */     addMapping((Class)EntitySlime.class, "Slime", 55, 5349438, 8306542);
/* 312 */     addMapping((Class)EntityGhast.class, "Ghast", 56, 16382457, 12369084);
/* 313 */     addMapping((Class)EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
/* 314 */     addMapping((Class)EntityEnderman.class, "Enderman", 58, 1447446, 0);
/* 315 */     addMapping((Class)EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
/* 316 */     addMapping((Class)EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
/* 317 */     addMapping((Class)EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
/* 318 */     addMapping((Class)EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
/* 319 */     addMapping((Class)EntityDragon.class, "EnderDragon", 63);
/* 320 */     addMapping((Class)EntityWither.class, "WitherBoss", 64);
/* 321 */     addMapping((Class)EntityBat.class, "Bat", 65, 4996656, 986895);
/* 322 */     addMapping((Class)EntityWitch.class, "Witch", 66, 3407872, 5349438);
/* 323 */     addMapping((Class)EntityEndermite.class, "Endermite", 67, 1447446, 7237230);
/* 324 */     addMapping((Class)EntityGuardian.class, "Guardian", 68, 5931634, 15826224);
/* 325 */     addMapping((Class)EntityPig.class, "Pig", 90, 15771042, 14377823);
/* 326 */     addMapping((Class)EntitySheep.class, "Sheep", 91, 15198183, 16758197);
/* 327 */     addMapping((Class)EntityCow.class, "Cow", 92, 4470310, 10592673);
/* 328 */     addMapping((Class)EntityChicken.class, "Chicken", 93, 10592673, 16711680);
/* 329 */     addMapping((Class)EntitySquid.class, "Squid", 94, 2243405, 7375001);
/* 330 */     addMapping((Class)EntityWolf.class, "Wolf", 95, 14144467, 13545366);
/* 331 */     addMapping((Class)EntityMooshroom.class, "MushroomCow", 96, 10489616, 12040119);
/* 332 */     addMapping((Class)EntitySnowman.class, "SnowMan", 97);
/* 333 */     addMapping((Class)EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
/* 334 */     addMapping((Class)EntityIronGolem.class, "VillagerGolem", 99);
/* 335 */     addMapping((Class)EntityHorse.class, "EntityHorse", 100, 12623485, 15656192);
/* 336 */     addMapping((Class)EntityRabbit.class, "Rabbit", 101, 10051392, 7555121);
/* 337 */     addMapping((Class)EntityVillager.class, "Villager", 120, 5651507, 12422002);
/* 338 */     addMapping((Class)EntityEnderCrystal.class, "EnderCrystal", 200);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class EntityEggInfo
/*     */   {
/*     */     public final int spawnedID;
/*     */     public final int primaryColor;
/*     */     public final int secondaryColor;
/*     */     public final StatBase field_151512_d;
/*     */     public final StatBase field_151513_e;
/*     */     
/*     */     public EntityEggInfo(int id, int baseColor, int spotColor) {
/* 351 */       this.spawnedID = id;
/* 352 */       this.primaryColor = baseColor;
/* 353 */       this.secondaryColor = spotColor;
/* 354 */       this.field_151512_d = StatList.getStatKillEntity(this);
/* 355 */       this.field_151513_e = StatList.getStatEntityKilledBy(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */