/*     */ package com.viaversion.viaversion.api.protocol.version;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProtocolVersion
/*     */ {
/*  37 */   private static final Int2ObjectMap<ProtocolVersion> VERSIONS = (Int2ObjectMap<ProtocolVersion>)new Int2ObjectOpenHashMap();
/*  38 */   private static final List<ProtocolVersion> VERSION_LIST = new ArrayList<>();
/*     */ 
/*     */   
/*  41 */   public static final ProtocolVersion v1_4_6 = register(51, "1.4.6/7", new VersionRange("1.4", 6, 7));
/*  42 */   public static final ProtocolVersion v1_5_1 = register(60, "1.5/1.5.1", new VersionRange("1.5", 0, 1));
/*  43 */   public static final ProtocolVersion v1_5_2 = register(61, "1.5.2");
/*  44 */   public static final ProtocolVersion v_1_6_1 = register(73, "1.6.1");
/*  45 */   public static final ProtocolVersion v_1_6_2 = register(74, "1.6.2");
/*  46 */   public static final ProtocolVersion v_1_6_3 = register(77, "1.6.3");
/*  47 */   public static final ProtocolVersion v_1_6_4 = register(78, "1.6.4");
/*     */   
/*  49 */   public static final ProtocolVersion v1_7_1 = register(4, "1.7.2-1.7.5", new VersionRange("1.7", 2, 5));
/*  50 */   public static final ProtocolVersion v1_7_6 = register(5, "1.7.6-1.7.10", new VersionRange("1.7", 6, 10));
/*  51 */   public static final ProtocolVersion v1_8 = register(47, "1.8.x");
/*  52 */   public static final ProtocolVersion v1_9 = register(107, "1.9");
/*  53 */   public static final ProtocolVersion v1_9_1 = register(108, "1.9.1");
/*  54 */   public static final ProtocolVersion v1_9_2 = register(109, "1.9.2");
/*  55 */   public static final ProtocolVersion v1_9_3 = register(110, "1.9.3/4", new VersionRange("1.9", 3, 4));
/*  56 */   public static final ProtocolVersion v1_10 = register(210, "1.10.x");
/*  57 */   public static final ProtocolVersion v1_11 = register(315, "1.11");
/*  58 */   public static final ProtocolVersion v1_11_1 = register(316, "1.11.1/2", new VersionRange("1.11", 1, 2));
/*  59 */   public static final ProtocolVersion v1_12 = register(335, "1.12");
/*  60 */   public static final ProtocolVersion v1_12_1 = register(338, "1.12.1");
/*  61 */   public static final ProtocolVersion v1_12_2 = register(340, "1.12.2");
/*  62 */   public static final ProtocolVersion v1_13 = register(393, "1.13");
/*  63 */   public static final ProtocolVersion v1_13_1 = register(401, "1.13.1");
/*  64 */   public static final ProtocolVersion v1_13_2 = register(404, "1.13.2");
/*  65 */   public static final ProtocolVersion v1_14 = register(477, "1.14");
/*  66 */   public static final ProtocolVersion v1_14_1 = register(480, "1.14.1");
/*  67 */   public static final ProtocolVersion v1_14_2 = register(485, "1.14.2");
/*  68 */   public static final ProtocolVersion v1_14_3 = register(490, "1.14.3");
/*  69 */   public static final ProtocolVersion v1_14_4 = register(498, "1.14.4");
/*  70 */   public static final ProtocolVersion v1_15 = register(573, "1.15");
/*  71 */   public static final ProtocolVersion v1_15_1 = register(575, "1.15.1");
/*  72 */   public static final ProtocolVersion v1_15_2 = register(578, "1.15.2");
/*  73 */   public static final ProtocolVersion v1_16 = register(735, "1.16");
/*  74 */   public static final ProtocolVersion v1_16_1 = register(736, "1.16.1");
/*  75 */   public static final ProtocolVersion v1_16_2 = register(751, "1.16.2");
/*  76 */   public static final ProtocolVersion v1_16_3 = register(753, "1.16.3");
/*  77 */   public static final ProtocolVersion v1_16_4 = register(754, "1.16.4/5", new VersionRange("1.16", 4, 5));
/*  78 */   public static final ProtocolVersion v1_17 = register(755, "1.17");
/*  79 */   public static final ProtocolVersion v1_17_1 = register(756, "1.17.1");
/*  80 */   public static final ProtocolVersion v1_18 = register(757, "1.18/1.18.1", new VersionRange("1.18", 0, 1));
/*  81 */   public static final ProtocolVersion v1_18_2 = register(758, "1.18.2");
/*  82 */   public static final ProtocolVersion v1_19 = register(759, "1.19");
/*  83 */   public static final ProtocolVersion v1_19_1 = register(760, "1.19.1/2", new VersionRange("1.19", 1, 2));
/*  84 */   public static final ProtocolVersion v1_19_3 = register(761, "1.19.3");
/*  85 */   public static final ProtocolVersion v1_19_4 = register(762, "1.19.4");
/*  86 */   public static final ProtocolVersion v1_20 = register(763, "1.20/1.20.1", new VersionRange("1.20", 0, 1));
/*  87 */   public static final ProtocolVersion v1_20_2 = register(764, "1.20.2");
/*  88 */   public static final ProtocolVersion unknown = register(-1, "UNKNOWN"); private final int version; private final int snapshotVersion;
/*     */   
/*     */   public static ProtocolVersion register(int version, String name) {
/*  91 */     return register(version, -1, name);
/*     */   }
/*     */   private final String name; private final boolean versionWildcard; private final Set<String> includedVersions;
/*     */   public static ProtocolVersion register(int version, int snapshotVersion, String name) {
/*  95 */     return register(version, snapshotVersion, name, null);
/*     */   }
/*     */   
/*     */   public static ProtocolVersion register(int version, String name, VersionRange versionRange) {
/*  99 */     return register(version, -1, name, versionRange);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProtocolVersion register(int version, int snapshotVersion, String name, VersionRange versionRange) {
/* 112 */     ProtocolVersion protocol = new ProtocolVersion(version, snapshotVersion, name, versionRange);
/* 113 */     VERSION_LIST.add(protocol);
/* 114 */     VERSIONS.put(protocol.getVersion(), protocol);
/* 115 */     if (protocol.isSnapshot()) {
/* 116 */       VERSIONS.put(protocol.getFullSnapshotVersion(), protocol);
/*     */     }
/* 118 */     return protocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isRegistered(int version) {
/* 128 */     return VERSIONS.containsKey(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProtocolVersion getProtocol(int version) {
/* 139 */     ProtocolVersion protocolVersion = (ProtocolVersion)VERSIONS.get(version);
/* 140 */     if (protocolVersion != null) {
/* 141 */       return protocolVersion;
/*     */     }
/* 143 */     return new ProtocolVersion(version, "Unknown (" + version + ")");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getIndex(ProtocolVersion version) {
/* 154 */     return VERSION_LIST.indexOf(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<ProtocolVersion> getProtocols() {
/* 163 */     return Collections.unmodifiableList(VERSION_LIST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProtocolVersion getClosest(String protocol) {
/* 175 */     for (ObjectIterator<ProtocolVersion> objectIterator = VERSIONS.values().iterator(); objectIterator.hasNext(); ) { ProtocolVersion version = objectIterator.next();
/* 176 */       String name = version.getName();
/* 177 */       if (name.equals(protocol)) {
/* 178 */         return version;
/*     */       }
/*     */       
/* 181 */       if (version.isVersionWildcard()) {
/*     */         
/* 183 */         String majorVersion = name.substring(0, name.length() - 2);
/* 184 */         if (majorVersion.equals(protocol) || protocol.startsWith(name.substring(0, name.length() - 1)))
/* 185 */           return version;  continue;
/*     */       } 
/* 187 */       if (version.isRange() && 
/* 188 */         version.getIncludedVersions().contains(protocol)) {
/* 189 */         return version;
/*     */       } }
/*     */ 
/*     */     
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion(int version, String name) {
/* 207 */     this(version, -1, name, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion(int version, int snapshotVersion, String name, VersionRange versionRange) {
/* 217 */     this.version = version;
/* 218 */     this.snapshotVersion = snapshotVersion;
/* 219 */     this.name = name;
/* 220 */     this.versionWildcard = name.endsWith(".x");
/*     */     
/* 222 */     Preconditions.checkArgument((!this.versionWildcard || versionRange == null), "A version cannot be a wildcard and a range at the same time!");
/* 223 */     if (versionRange != null) {
/* 224 */       this.includedVersions = new LinkedHashSet<>();
/* 225 */       for (int i = versionRange.rangeFrom(); i <= versionRange.rangeTo(); i++) {
/* 226 */         if (i == 0) {
/* 227 */           this.includedVersions.add(versionRange.baseVersion());
/*     */         }
/*     */         
/* 230 */         this.includedVersions.add(versionRange.baseVersion() + "." + i);
/*     */       } 
/*     */     } else {
/* 233 */       this.includedVersions = Collections.singleton(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 243 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSnapshotVersion() {
/* 254 */     Preconditions.checkArgument(isSnapshot());
/* 255 */     return this.snapshotVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFullSnapshotVersion() {
/* 266 */     Preconditions.checkArgument(isSnapshot());
/* 267 */     return 0x40000000 | this.snapshotVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOriginalVersion() {
/* 276 */     return (this.snapshotVersion == -1) ? this.version : (0x40000000 | this.snapshotVersion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isKnown() {
/* 285 */     return (this.version != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRange() {
/* 295 */     return (this.includedVersions.size() != 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getIncludedVersions() {
/* 307 */     return Collections.unmodifiableSet(this.includedVersions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVersionWildcard() {
/* 316 */     return this.versionWildcard;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 325 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSnapshot() {
/* 334 */     return (this.snapshotVersion != -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 339 */     if (this == o) return true; 
/* 340 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 342 */     ProtocolVersion that = (ProtocolVersion)o;
/* 343 */     return (this.version == that.version);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 348 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 353 */     return String.format("%s (%d)", new Object[] { this.name, Integer.valueOf(this.version) });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\protocol\version\ProtocolVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */