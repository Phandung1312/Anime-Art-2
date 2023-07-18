package com.anime.art.ai.common;

@dagger.hilt.android.HiltAndroidApp
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\f"}, d2 = {"Lcom/anime/art/ai/common/App;", "Landroid/app/Application;", "()V", "prefs", "Lcom/anime/art/ai/data/Preferences;", "getPrefs", "()Lcom/anime/art/ai/data/Preferences;", "setPrefs", "(Lcom/anime/art/ai/data/Preferences;)V", "onCreate", "", "Companion", "app_debug"})
public final class App extends android.app.Application {
    public static com.anime.art.ai.common.App app;
    @javax.inject.Inject
    public com.anime.art.ai.data.Preferences prefs;
    @org.jetbrains.annotations.NotNull
    public static final com.anime.art.ai.common.App.Companion Companion = null;
    
    public App() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.anime.art.ai.data.Preferences getPrefs() {
        return null;
    }
    
    public final void setPrefs(@org.jetbrains.annotations.NotNull
    com.anime.art.ai.data.Preferences p0) {
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2 = {"Lcom/anime/art/ai/common/App$Companion;", "", "()V", "app", "Lcom/anime/art/ai/common/App;", "getApp", "()Lcom/anime/art/ai/common/App;", "setApp", "(Lcom/anime/art/ai/common/App;)V", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.anime.art.ai.common.App getApp() {
            return null;
        }
        
        public final void setApp(@org.jetbrains.annotations.NotNull
        com.anime.art.ai.common.App p0) {
        }
    }
}