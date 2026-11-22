# AndroidKit Showcase · 四合一集成示例

> 🤖 本项目由 AI 生成。

[中文](README.md) | [English](README_EN.md)

一个演示 App，把三套客户端 SDK 集成到同一个工程，一屏看到四种能力：

| 能力 | 来自 | SDK |
|---|---|---|
| 卡密激活 | OpenHub `cardkey` 插件 | [CardKeySDK](https://github.com/wanwu2026/CardKeySDK) |
| 版本更新 | OpenHub `update` 插件 | [UpdateKit](https://github.com/wanwu2026/UpdateKit) |
| 公告拉取 | OpenHub `announce` 插件 | [UpdateKit](https://github.com/wanwu2026/UpdateKit) |
| 崩溃上报 | OpenHub `crash` 插件 | [CrashKit](https://github.com/wanwu2026/CrashKit) |

三套 SDK 对接 [OpenHub](https://github.com/wanwu2026/OpenHub) 服务器。

## 依赖（JitPack）

三个 SDK 打 tag 后可从 JitPack 拉取。在 `settings.gradle` 加：

```gradle
repositories { maven { url 'https://jitpack.io' } }
```

`app/build.gradle`：

```gradle
dependencies {
    implementation 'com.github.wanwu2026:CardKeySDK:TAG'
    implementation 'com.github.wanwu2026:UpdateKit:TAG'
    implementation 'com.github.wanwu2026:CrashKit:TAG'
}
```

> TAG 换成实际版本（如 `v1.0.0`）。首次需在 https://jitpack.io 用 GitHub 账号登录并触发构建。

## 初始化（`ShowcaseApp`）

```java
CardKey.init(new CardKey.Config(this)
        .productId("my_app").publicKeyBase64(PUB).mode(CardKey.Mode.OFFLINE));
UpdateKit.init(new UpdateKit.Config(this)
        .baseUrl(OPENHUB_BASE).apiKey(KEY).product("my_app").publicKeyBase64(PUB));
CrashKit.init(new CrashKit.Config(this)
        .baseUrl(OPENHUB_BASE).apiKey(KEY).appId("my_app"));
CrashKit.get().install();   // 全局崩溃捕获
```

> `CardKey.init` 抛受检异常，必须捕获。私钥永远只在 OpenHub 服务器，客户端只嵌公钥。

## 演示界面（`MainActivity`）

五个按钮：激活状态 / 检查更新 / 拉取公告 / 捕获崩溃 / 立即上报。都是本地可看，联网功能需把 `ShowcaseApp` 里的占位地址与 Key 换成真实值。

## 验证方式

本机（aarch64，无完整 Gradle 链）用 javac + android.jar 把三套 SDK 源码与本 App 一起编译（含 stub R 类绕过 aapt），**72 个 class 编译通过**，证明所有 API 调用正确。真机 APK 由 GitHub Actions 云端编译。

## License

MIT —— 见 [LICENSE](LICENSE)。
