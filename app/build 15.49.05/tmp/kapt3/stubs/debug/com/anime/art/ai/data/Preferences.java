package com.anime.art.ai.data;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\b\u0007\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001f\u0010\u0005\u001a\u0010\u0012\f\u0012\n \b*\u0004\u0018\u00010\u00070\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\n\u001a\u0010\u0012\f\u0012\n \b*\u0004\u0018\u00010\u000b0\u000b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\u000e"}, d2 = {"Lcom/anime/art/ai/data/Preferences;", "", "rxPrefs", "Lcom/f2prateek/rx/preferences2/RxSharedPreferences;", "(Lcom/f2prateek/rx/preferences2/RxSharedPreferences;)V", "isUpgraded", "Lcom/f2prateek/rx/preferences2/Preference;", "", "kotlin.jvm.PlatformType", "()Lcom/f2prateek/rx/preferences2/Preference;", "timeExpiredPremium", "", "getTimeExpiredPremium", "Companion", "app_debug"})
public final class Preferences {
    @org.jetbrains.annotations.NotNull
    private final com.f2prateek.rx.preferences2.RxSharedPreferences rxPrefs = null;
    @org.jetbrains.annotations.NotNull
    private final com.f2prateek.rx.preferences2.Preference<java.lang.Boolean> isUpgraded = null;
    @org.jetbrains.annotations.NotNull
    private final com.f2prateek.rx.preferences2.Preference<java.lang.Long> timeExpiredPremium = null;
    @org.jetbrains.annotations.NotNull
    public static final com.anime.art.ai.data.Preferences.Companion Companion = null;
    
    @javax.inject.Inject
    public Preferences(@org.jetbrains.annotations.NotNull
    com.f2prateek.rx.preferences2.RxSharedPreferences rxPrefs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.f2prateek.rx.preferences2.Preference<java.lang.Boolean> isUpgraded() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.f2prateek.rx.preferences2.Preference<java.lang.Long> getTimeExpiredPremium() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/anime/art/ai/data/Preferences$Companion;", "", "()V", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}