/*     */ package us.myles.ViaVersion.api.protocol;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.viaversion.viaversion.api.protocol.version.VersionRange;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ProtocolVersion
/*     */ {
/*  42 */   private static final Int2ObjectMap<ProtocolVersion> versions = (Int2ObjectMap<ProtocolVersion>)new Int2ObjectOpenHashMap();
/*  43 */   private static final List<ProtocolVersion> versionList = new ArrayList<>();
/*     */   
/*  45 */   public static final ProtocolVersion v1_4_6 = register(51, "1.4.6/7", new VersionRange("1.4", 6, 7));
/*  46 */   public static final ProtocolVersion v1_5_1 = register(60, "1.5.1");
/*  47 */   public static final ProtocolVersion v1_5_2 = register(61, "1.5.2");
/*  48 */   public static final ProtocolVersion v_1_6_1 = register(73, "1.6.1");
/*  49 */   public static final ProtocolVersion v_1_6_2 = register(74, "1.6.2");
/*  50 */   public static final ProtocolVersion v_1_6_3 = register(77, "1.6.3");
/*  51 */   public static final ProtocolVersion v_1_6_4 = register(78, "1.6.4");
/*  52 */   public static final ProtocolVersion v1_7_1 = register(4, "1.7-1.7.5", new VersionRange("1.7", 0, 5));
/*  53 */   public static final ProtocolVersion v1_7_6 = register(5, "1.7.6-1.7.10", new VersionRange("1.7", 6, 10));
/*  54 */   public static final ProtocolVersion v1_8 = register(47, "1.8.x");
/*  55 */   public static final ProtocolVersion v1_9 = register(107, "1.9");
/*  56 */   public static final ProtocolVersion v1_9_1 = register(108, "1.9.1");
/*  57 */   public static final ProtocolVersion v1_9_2 = register(109, "1.9.2");
/*  58 */   public static final ProtocolVersion v1_9_3 = register(110, "1.9.3/4", new VersionRange("1.9", 3, 4));
/*  59 */   public static final ProtocolVersion v1_10 = register(210, "1.10.x");
/*  60 */   public static final ProtocolVersion v1_11 = register(315, "1.11");
/*  61 */   public static final ProtocolVersion v1_11_1 = register(316, "1.11.1/2", new VersionRange("1.11", 1, 2));
/*  62 */   public static final ProtocolVersion v1_12 = register(335, "1.12");
/*  63 */   public static final ProtocolVersion v1_12_1 = register(338, "1.12.1");
/*  64 */   public static final ProtocolVersion v1_12_2 = register(340, "1.12.2");
/*  65 */   public static final ProtocolVersion v1_13 = register(393, "1.13");
/*  66 */   public static final ProtocolVersion v1_13_1 = register(401, "1.13.1");
/*  67 */   public static final ProtocolVersion v1_13_2 = register(404, "1.13.2");
/*  68 */   public static final ProtocolVersion v1_14 = register(477, "1.14");
/*  69 */   public static final ProtocolVersion v1_14_1 = register(480, "1.14.1");
/*  70 */   public static final ProtocolVersion v1_14_2 = register(485, "1.14.2");
/*  71 */   public static final ProtocolVersion v1_14_3 = register(490, "1.14.3");
/*  72 */   public static final ProtocolVersion v1_14_4 = register(498, "1.14.4");
/*  73 */   public static final ProtocolVersion v1_15 = register(573, "1.15");
/*  74 */   public static final ProtocolVersion v1_15_1 = register(575, "1.15.1");
/*  75 */   public static final ProtocolVersion v1_15_2 = register(578, "1.15.2");
/*  76 */   public static final ProtocolVersion v1_16 = register(735, "1.16");
/*  77 */   public static final ProtocolVersion v1_16_1 = register(736, "1.16.1");
/*  78 */   public static final ProtocolVersion v1_16_2 = register(751, "1.16.2");
/*  79 */   public static final ProtocolVersion v1_16_3 = register(753, "1.16.3");
/*  80 */   public static final ProtocolVersion v1_16_4 = register(754, "1.16.4/5", new VersionRange("1.16", 4, 5));
/*  81 */   public static final ProtocolVersion v1_17 = register(755, "1.17");
/*  82 */   public static final ProtocolVersion v1_17_1 = register(756, "1.17.1");
/*  83 */   public static final ProtocolVersion v1_18 = register(757, "1.18/1.18.1", new VersionRange("1.18", 0, 1));
/*  84 */   public static final ProtocolVersion v1_18_2 = register(758, "1.18.2");
/*  85 */   public static final ProtocolVersion v1_19 = register(759, "1.19");
/*  86 */   public static final ProtocolVersion v1_19_1 = register(760, "1.19.1/2", new VersionRange("1.19", 1, 2));
/*  87 */   public static final ProtocolVersion v1_19_3 = register(761, "1.19.3");
/*  88 */   public static final ProtocolVersion v1_19_4 = register(762, "1.19.4");
/*  89 */   public static final ProtocolVersion v1_20 = register(763, "1.20/1.20.1", new VersionRange("1.20", 0, 1));
/*  90 */   public static final ProtocolVersion v1_20_2 = register(764, "1.20.2");
/*  91 */   public static final ProtocolVersion unknown = register(-1, "UNKNOWN"); private final int version; private final int snapshotVersion;
/*     */   
/*     */   public static ProtocolVersion register(int version, String name) {
/*  94 */     return register(version, -1, name);
/*     */   }
/*     */   private final String name; private final boolean versionWildcard; private final Set<String> includedVersions;
/*     */   public static ProtocolVersion register(int version, int snapshotVersion, String name) {
/*  98 */     return register(version, snapshotVersion, name, null);
/*     */   }
/*     */   
/*     */   public static ProtocolVersion register(int version, String name, VersionRange versionRange) {
/* 102 */     return register(version, -1, name, versionRange);
/*     */   }
/*     */   
/*     */   public static ProtocolVersion register(int version, int snapshotVersion, String name, VersionRange versionRange) {
/* 106 */     ProtocolVersion protocol = new ProtocolVersion(version, snapshotVersion, name, versionRange);
/* 107 */     versionList.add(protocol);
/* 108 */     versions.put(protocol.getVersion(), protocol);
/* 109 */     if (protocol.isSnapshot()) {
/* 110 */       versions.put(protocol.getFullSnapshotVersion(), protocol);
/*     */     }
/* 112 */     return protocol;
/*     */   }
/*     */   
/*     */   public static boolean isRegistered(int id) {
/* 116 */     return versions.containsKey(id);
/*     */   }
/*     */   
/*     */   public static ProtocolVersion getProtocol(int id) {
/* 120 */     ProtocolVersion protocolVersion = (ProtocolVersion)versions.get(id);
/* 121 */     if (protocolVersion != null) {
/* 122 */       return protocolVersion;
/*     */     }
/* 124 */     return new ProtocolVersion(id, "Unknown (" + id + ")");
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getIndex(ProtocolVersion version) {
/* 129 */     return versionList.indexOf(version);
/*     */   }
/*     */   
/*     */   public static List<ProtocolVersion> getProtocols() {
/* 133 */     return Collections.unmodifiableList(new ArrayList<>((Collection<? extends ProtocolVersion>)versions.values()));
/*     */   }
/*     */   
/*     */   public static ProtocolVersion getClosest(String protocol) {
/* 137 */     for (ObjectIterator<ProtocolVersion> objectIterator = versions.values().iterator(); objectIterator.hasNext(); ) { ProtocolVersion version = objectIterator.next();
/* 138 */       String name = version.getName();
/* 139 */       if (name.equals(protocol)) {
/* 140 */         return version;
/*     */       }
/*     */       
/* 143 */       if (version.isVersionWildcard()) {
/*     */         
/* 145 */         String majorVersion = name.substring(0, name.length() - 2);
/* 146 */         if (majorVersion.equals(protocol) || protocol.startsWith(name.substring(0, name.length() - 1)))
/* 147 */           return version;  continue;
/*     */       } 
/* 149 */       if (version.isRange() && 
/* 150 */         version.getIncludedVersions().contains(protocol)) {
/* 151 */         return version;
/*     */       } }
/*     */ 
/*     */     
/* 155 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion(int version, String name) {
/* 165 */     this(version, -1, name, null);
/*     */   }
/*     */   
/*     */   public ProtocolVersion(int version, int snapshotVersion, String name, VersionRange versionRange) {
/* 169 */     this.version = version;
/* 170 */     this.snapshotVersion = snapshotVersion;
/* 171 */     this.name = name;
/* 172 */     this.versionWildcard = name.endsWith(".x");
/*     */     
/* 174 */     Preconditions.checkArgument((!this.versionWildcard || versionRange == null), "A version cannot be a wildcard and a range at the same time!");
/* 175 */     if (versionRange != null) {
/* 176 */       this.includedVersions = new LinkedHashSet<>();
/* 177 */       for (int i = versionRange.rangeFrom(); i <= versionRange.rangeTo(); i++) {
/* 178 */         if (i == 0) {
/* 179 */           this.includedVersions.add(versionRange.baseVersion());
/*     */         }
/*     */         
/* 182 */         this.includedVersions.add(versionRange.baseVersion() + "." + i);
/*     */       } 
/*     */     } else {
/* 185 */       this.includedVersions = Collections.singleton(name);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getVersion() {
/* 190 */     return this.version;
/*     */   }
/*     */   
/*     */   public int getSnapshotVersion() {
/* 194 */     Preconditions.checkArgument(isSnapshot());
/* 195 */     return this.snapshotVersion;
/*     */   }
/*     */   
/*     */   public int getFullSnapshotVersion() {
/* 199 */     Preconditions.checkArgument(isSnapshot());
/* 200 */     return 0x40000000 | this.snapshotVersion;
/*     */   }
/*     */   
/*     */   public int getOriginalVersion() {
/* 204 */     return (this.snapshotVersion == -1) ? this.version : (0x40000000 | this.snapshotVersion);
/*     */   }
/*     */   
/*     */   public boolean isKnown() {
/* 208 */     return (this.version != -1);
/*     */   }
/*     */   
/*     */   public boolean isRange() {
/* 212 */     return (this.includedVersions.size() != 1);
/*     */   }
/*     */   
/*     */   public Set<String> getIncludedVersions() {
/* 216 */     return Collections.unmodifiableSet(this.includedVersions);
/*     */   }
/*     */   
/*     */   public boolean isVersionWildcard() {
/* 220 */     return this.versionWildcard;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 224 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isSnapshot() {
/* 228 */     return (this.snapshotVersion != -1);
/*     */   }
/*     */   
/*     */   public int getId() {
/* 232 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 237 */     if (this == o) return true; 
/* 238 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 240 */     ProtocolVersion that = (ProtocolVersion)o;
/* 241 */     return (this.version == that.version);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 246 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 251 */     return String.format("%s (%d)", new Object[] { this.name, Integer.valueOf(this.version) });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar\\us\myles\ViaVersion\api\protocol\ProtocolVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */