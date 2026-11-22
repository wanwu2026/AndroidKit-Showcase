#!/usr/bin/env bash
# 集成验证：把三套 SDK 源码与本 App 一起用 javac 编译，确认 showcase 的 API 调用正确。
# 依赖：JDK（$JAVA_HOME）、android.jar（$ANDROID_JAR）。
# 三个 SDK 源码目录由调用方通过 SDK_ROOT 提供（CI 里 checkout 到 ../sdk-sources/）。
set -euo pipefail
SDK_ROOT="${SDK_ROOT:-../sdk-sources}"
ANDROID_JAR="${ANDROID_JAR:-$ANDROID_HOME/platforms/android-34/android.jar}"
[ -f "$ANDROID_JAR" ] || { echo "缺 android.jar: $ANDROID_JAR" >&2; exit 1; }

SRC=$(mktemp -d)
trap 'rm -rf "$SRC"' EXIT
mkdir -p "$SRC/java"

# 合并三套 SDK 的 com 包
cp -r "$SDK_ROOT/CardKeySDK/sdk/src/main/java/com" "$SRC/java/"
cp -r "$SDK_ROOT/UpdateKit/sdk/src/main/java/com" "$SRC/java/"
cp -r "$SDK_ROOT/CrashKit/sdk/src/main/java/com" "$SRC/java/"
# 本 App
cp -r app/src/main/java/com/example/showcase "$SRC/java/com/example/"

# stub R（CardKey 的 UI 类引用；真机由 aapt 生成）
mkdir -p "$SRC/java/com/example/cardkey"
cat > "$SRC/java/com/example/cardkey/R.java" <<'EOF'
package com.example.cardkey;
public final class R {
    public static final class layout { public static final int ck_dialog_activation = 1; }
    public static final class id {
        public static final int ck_code_input=1, ck_message=2, ck_progress=3,
            ck_btn_activate=4, ck_btn_cancel=5, ck_device_id=6;
    }
}
EOF

OUT="$SRC/out"; mkdir -p "$OUT"
cd "$SRC/java"
"$JAVA_HOME/bin/javac" -source 8 -target 8 -Xlint:-options \
  -cp "$ANDROID_JAR" -d "$OUT" $(find . -name '*.java')
COUNT=$(find "$OUT" -name '*.class' | wc -l)
echo "集成编译通过：$COUNT 个 class，showcase 的 API 调用全部正确。"
