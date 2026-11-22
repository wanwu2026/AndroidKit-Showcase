package com.example.showcase;

import android.app.Application;

import com.example.cardkey.CardKey;
import com.example.updatekit.UpdateKit;
import com.example.crashkit.CrashKit;

/** 一次初始化三套 SDK：卡密激活 / 更新公告 / 崩溃上报。 */
public class ShowcaseApp extends Application {
    // 换成你自己的值（公钥只嵌公钥，私钥永远在 OpenHub 服务器）
    static final String PUBLIC_KEY_BASE64 = "REPLACE_WITH_YOUR_PUBLIC_KEY_BASE64";
    static final String OPENHUB_BASE = "https://your.openhub.example/api/v1";
    static final String API_KEY = "ohk_replace_me";

    @Override public void onCreate() {
        super.onCreate();

        // 1) 卡密激活
        try {
            CardKey.init(new CardKey.Config(this)
                    .productId("my_app")
                    .publicKeyBase64(PUBLIC_KEY_BASE64)
                    .mode(CardKey.Mode.OFFLINE));
        } catch (Exception e) {
            android.util.Log.e("Showcase", "CardKey init failed", e);
        }

        // 2) 更新 + 公告
        try {
            UpdateKit.init(new UpdateKit.Config(this)
                    .baseUrl(OPENHUB_BASE)
                    .apiKey(API_KEY)
                    .product("my_app")
                    .publicKeyBase64(PUBLIC_KEY_BASE64));
        } catch (Exception e) {
            android.util.Log.e("Showcase", "UpdateKit init failed", e);
        }

        // 3) 崩溃上报（含全局未捕获异常捕获）
        CrashKit.init(new CrashKit.Config(this)
                .baseUrl(OPENHUB_BASE)
                .apiKey(API_KEY)
                .appId("my_app"));
        CrashKit.get().install();
    }
}
