/*     */ package net.minecraft.server.management;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerProfileCache
/*     */ {
/*  45 */   public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
/*  46 */   private final Map<String, ProfileEntry> usernameToProfileEntryMap = Maps.newHashMap();
/*  47 */   private final Map<UUID, ProfileEntry> uuidToProfileEntryMap = Maps.newHashMap();
/*  48 */   private final LinkedList<GameProfile> gameProfiles = Lists.newLinkedList();
/*     */   
/*     */   private final MinecraftServer mcServer;
/*     */   
/*  52 */   private static final ParameterizedType TYPE = new ParameterizedType()
/*     */     {
/*     */       public Type[] getActualTypeArguments()
/*     */       {
/*  56 */         return new Type[] { PlayerProfileCache.ProfileEntry.class };
/*     */       }
/*     */       
/*     */       public Type getRawType() {
/*  60 */         return List.class;
/*     */       }
/*     */       
/*     */       public Type getOwnerType() {
/*  64 */         return null;
/*     */       }
/*     */     };
/*     */   protected final Gson gson; private final File usercacheFile;
/*     */   
/*     */   public PlayerProfileCache(MinecraftServer server, File cacheFile) {
/*  70 */     this.mcServer = server;
/*  71 */     this.usercacheFile = cacheFile;
/*  72 */     GsonBuilder gsonbuilder = new GsonBuilder();
/*  73 */     gsonbuilder.registerTypeHierarchyAdapter(ProfileEntry.class, new Serializer());
/*  74 */     this.gson = gsonbuilder.create();
/*  75 */     load();
/*     */   }
/*     */ 
/*     */   
/*     */   private static GameProfile getGameProfile(MinecraftServer server, String username) {
/*  80 */     final GameProfile[] agameprofile = new GameProfile[1];
/*  81 */     ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
/*     */       {
/*     */         public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_)
/*     */         {
/*  85 */           agameprofile[0] = p_onProfileLookupSucceeded_1_;
/*     */         }
/*     */         
/*     */         public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
/*  89 */           agameprofile[0] = null;
/*     */         }
/*     */       };
/*  92 */     server.getGameProfileRepository().findProfilesByNames(new String[] { username }, Agent.MINECRAFT, profilelookupcallback);
/*     */     
/*  94 */     if (!server.isServerInOnlineMode() && agameprofile[0] == null) {
/*     */       
/*  96 */       UUID uuid = EntityPlayer.getUUID(new GameProfile((UUID)null, username));
/*  97 */       GameProfile gameprofile = new GameProfile(uuid, username);
/*  98 */       profilelookupcallback.onProfileLookupSucceeded(gameprofile);
/*     */     } 
/*     */     
/* 101 */     return agameprofile[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntry(GameProfile gameProfile) {
/* 106 */     addEntry(gameProfile, (Date)null);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addEntry(GameProfile gameProfile, Date expirationDate) {
/* 111 */     UUID uuid = gameProfile.getId();
/*     */     
/* 113 */     if (expirationDate == null) {
/*     */       
/* 115 */       Calendar calendar = Calendar.getInstance();
/* 116 */       calendar.setTime(new Date());
/* 117 */       calendar.add(2, 1);
/* 118 */       expirationDate = calendar.getTime();
/*     */     } 
/*     */     
/* 121 */     String s = gameProfile.getName().toLowerCase(Locale.ROOT);
/* 122 */     ProfileEntry playerprofilecache$profileentry = new ProfileEntry(gameProfile, expirationDate);
/*     */     
/* 124 */     if (this.uuidToProfileEntryMap.containsKey(uuid)) {
/*     */       
/* 126 */       ProfileEntry playerprofilecache$profileentry1 = this.uuidToProfileEntryMap.get(uuid);
/* 127 */       this.usernameToProfileEntryMap.remove(playerprofilecache$profileentry1.getGameProfile().getName().toLowerCase(Locale.ROOT));
/* 128 */       this.gameProfiles.remove(gameProfile);
/*     */     } 
/*     */     
/* 131 */     this.usernameToProfileEntryMap.put(gameProfile.getName().toLowerCase(Locale.ROOT), playerprofilecache$profileentry);
/* 132 */     this.uuidToProfileEntryMap.put(uuid, playerprofilecache$profileentry);
/* 133 */     this.gameProfiles.addFirst(gameProfile);
/* 134 */     save();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile getGameProfileForUsername(String username) {
/* 139 */     String s = username.toLowerCase(Locale.ROOT);
/* 140 */     ProfileEntry playerprofilecache$profileentry = this.usernameToProfileEntryMap.get(s);
/*     */     
/* 142 */     if (playerprofilecache$profileentry != null && (new Date()).getTime() >= playerprofilecache$profileentry.expirationDate.getTime()) {
/*     */       
/* 144 */       this.uuidToProfileEntryMap.remove(playerprofilecache$profileentry.getGameProfile().getId());
/* 145 */       this.usernameToProfileEntryMap.remove(playerprofilecache$profileentry.getGameProfile().getName().toLowerCase(Locale.ROOT));
/* 146 */       this.gameProfiles.remove(playerprofilecache$profileentry.getGameProfile());
/* 147 */       playerprofilecache$profileentry = null;
/*     */     } 
/*     */     
/* 150 */     if (playerprofilecache$profileentry != null) {
/*     */       
/* 152 */       GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
/* 153 */       this.gameProfiles.remove(gameprofile);
/* 154 */       this.gameProfiles.addFirst(gameprofile);
/*     */     }
/*     */     else {
/*     */       
/* 158 */       GameProfile gameprofile1 = getGameProfile(this.mcServer, s);
/*     */       
/* 160 */       if (gameprofile1 != null) {
/*     */         
/* 162 */         addEntry(gameprofile1);
/* 163 */         playerprofilecache$profileentry = this.usernameToProfileEntryMap.get(s);
/*     */       } 
/*     */     } 
/*     */     
/* 167 */     save();
/* 168 */     return (playerprofilecache$profileentry == null) ? null : playerprofilecache$profileentry.getGameProfile();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getUsernames() {
/* 173 */     List<String> list = Lists.newArrayList(this.usernameToProfileEntryMap.keySet());
/* 174 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile getProfileByUUID(UUID uuid) {
/* 179 */     ProfileEntry playerprofilecache$profileentry = this.uuidToProfileEntryMap.get(uuid);
/* 180 */     return (playerprofilecache$profileentry == null) ? null : playerprofilecache$profileentry.getGameProfile();
/*     */   }
/*     */ 
/*     */   
/*     */   private ProfileEntry getByUUID(UUID uuid) {
/* 185 */     ProfileEntry playerprofilecache$profileentry = this.uuidToProfileEntryMap.get(uuid);
/*     */     
/* 187 */     if (playerprofilecache$profileentry != null) {
/*     */       
/* 189 */       GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
/* 190 */       this.gameProfiles.remove(gameprofile);
/* 191 */       this.gameProfiles.addFirst(gameprofile);
/*     */     } 
/*     */     
/* 194 */     return playerprofilecache$profileentry;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load() {
/* 199 */     BufferedReader bufferedreader = null;
/*     */ 
/*     */     
/*     */     try {
/* 203 */       bufferedreader = Files.newReader(this.usercacheFile, Charsets.UTF_8);
/* 204 */       List<ProfileEntry> list = (List<ProfileEntry>)this.gson.fromJson(bufferedreader, TYPE);
/* 205 */       this.usernameToProfileEntryMap.clear();
/* 206 */       this.uuidToProfileEntryMap.clear();
/* 207 */       this.gameProfiles.clear();
/*     */       
/* 209 */       for (ProfileEntry playerprofilecache$profileentry : Lists.reverse(list))
/*     */       {
/* 211 */         if (playerprofilecache$profileentry != null)
/*     */         {
/* 213 */           addEntry(playerprofilecache$profileentry.getGameProfile(), playerprofilecache$profileentry.getExpirationDate());
/*     */         }
/*     */       }
/*     */     
/* 217 */     } catch (FileNotFoundException fileNotFoundException) {
/*     */ 
/*     */     
/*     */     }
/* 221 */     catch (JsonParseException jsonParseException) {
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 227 */       IOUtils.closeQuietly(bufferedreader);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void save() {
/* 233 */     String s = this.gson.toJson(getEntriesWithLimit(1000));
/* 234 */     BufferedWriter bufferedwriter = null;
/*     */ 
/*     */     
/*     */     try {
/* 238 */       bufferedwriter = Files.newWriter(this.usercacheFile, Charsets.UTF_8);
/* 239 */       bufferedwriter.write(s);
/*     */       
/*     */       return;
/* 242 */     } catch (FileNotFoundException fileNotFoundException) {
/*     */ 
/*     */     
/*     */     }
/* 246 */     catch (IOException var9) {
/*     */       
/*     */       return;
/*     */     }
/*     */     finally {
/*     */       
/* 252 */       IOUtils.closeQuietly(bufferedwriter);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private List<ProfileEntry> getEntriesWithLimit(int limitSize) {
/* 258 */     ArrayList<ProfileEntry> arraylist = Lists.newArrayList();
/*     */     
/* 260 */     for (GameProfile gameprofile : Lists.newArrayList(Iterators.limit(this.gameProfiles.iterator(), limitSize))) {
/*     */       
/* 262 */       ProfileEntry playerprofilecache$profileentry = getByUUID(gameprofile.getId());
/*     */       
/* 264 */       if (playerprofilecache$profileentry != null)
/*     */       {
/* 266 */         arraylist.add(playerprofilecache$profileentry);
/*     */       }
/*     */     } 
/*     */     
/* 270 */     return arraylist;
/*     */   }
/*     */ 
/*     */   
/*     */   class ProfileEntry
/*     */   {
/*     */     private final GameProfile gameProfile;
/*     */     private final Date expirationDate;
/*     */     
/*     */     private ProfileEntry(GameProfile gameProfileIn, Date expirationDateIn) {
/* 280 */       this.gameProfile = gameProfileIn;
/* 281 */       this.expirationDate = expirationDateIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public GameProfile getGameProfile() {
/* 286 */       return this.gameProfile;
/*     */     }
/*     */ 
/*     */     
/*     */     public Date getExpirationDate() {
/* 291 */       return this.expirationDate;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   class Serializer
/*     */     implements JsonDeserializer<ProfileEntry>, JsonSerializer<ProfileEntry>
/*     */   {
/*     */     private Serializer() {}
/*     */ 
/*     */     
/*     */     public JsonElement serialize(PlayerProfileCache.ProfileEntry p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
/* 303 */       JsonObject jsonobject = new JsonObject();
/* 304 */       jsonobject.addProperty("name", p_serialize_1_.getGameProfile().getName());
/* 305 */       UUID uuid = p_serialize_1_.getGameProfile().getId();
/* 306 */       jsonobject.addProperty("uuid", (uuid == null) ? "" : uuid.toString());
/* 307 */       jsonobject.addProperty("expiresOn", PlayerProfileCache.dateFormat.format(p_serialize_1_.getExpirationDate()));
/* 308 */       return (JsonElement)jsonobject;
/*     */     }
/*     */ 
/*     */     
/*     */     public PlayerProfileCache.ProfileEntry deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
/* 313 */       if (p_deserialize_1_.isJsonObject()) {
/*     */         
/* 315 */         JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 316 */         JsonElement jsonelement = jsonobject.get("name");
/* 317 */         JsonElement jsonelement1 = jsonobject.get("uuid");
/* 318 */         JsonElement jsonelement2 = jsonobject.get("expiresOn");
/*     */         
/* 320 */         if (jsonelement != null && jsonelement1 != null) {
/*     */           
/* 322 */           String s = jsonelement1.getAsString();
/* 323 */           String s1 = jsonelement.getAsString();
/* 324 */           Date date = null;
/*     */           
/* 326 */           if (jsonelement2 != null) {
/*     */             
/*     */             try {
/*     */               
/* 330 */               date = PlayerProfileCache.dateFormat.parse(jsonelement2.getAsString());
/*     */             }
/* 332 */             catch (ParseException var14) {
/*     */               
/* 334 */               date = null;
/*     */             } 
/*     */           }
/*     */           
/* 338 */           if (s1 != null && s != null) {
/*     */             UUID uuid;
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 344 */               uuid = UUID.fromString(s);
/*     */             }
/* 346 */             catch (Throwable var13) {
/*     */               
/* 348 */               return null;
/*     */             } 
/*     */             
/* 351 */             PlayerProfileCache.this.getClass(); PlayerProfileCache.ProfileEntry playerprofilecache$profileentry = new PlayerProfileCache.ProfileEntry(new GameProfile(uuid, s1), date);
/* 352 */             return playerprofilecache$profileentry;
/*     */           } 
/*     */ 
/*     */           
/* 356 */           return null;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 361 */         return null;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 366 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\management\PlayerProfileCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */