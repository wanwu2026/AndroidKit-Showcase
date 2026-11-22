package com.example.showcase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.cardkey.CardKey;
import com.example.crashkit.CrashKit;
import com.example.updatekit.UpdateKit;

/** 纯代码 UI：一屏演示四套能力（激活 / 更新 / 公告 / 崩溃）。 */
public class MainActivity extends Activity {
    private TextView log;
    private StringBuilder sb = new StringBuilder();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scroll = new ScrollView(this);
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = 24;
        box.setPadding(pad, pad, pad, pad);

        log = new TextView(this);
        log.setTextSize(15);
        box.addView(log);

        box.addView(button("1. 激活状态", new View.OnClickListener() {
            @Override public void onClick(View v) {
                append("已激活: " + CardKey.get().isActivated());
                append("设备码: " + CardKey.get().getDeviceId());
            }
        }));
        box.addView(button("2. 检查更新", new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    com.example.updatekit.model.UpdateInfo info = UpdateKit.get().checkUpdate(1);
                    append(info == null ? "暂无更新" : "有新版本: " + info.version);
                } catch (Exception e) {
                    append("检查失败: " + e.getMessage() + "（需配置真实 OpenHub 地址）");
                }
            }
        }));
        box.addView(button("3. 拉取公告", new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    java.util.List<com.example.updatekit.model.Notice> list = UpdateKit.get().fetchNotices();
                    append("未读公告: " + (list == null ? 0 : list.size()) + " 条");
                } catch (Exception e) {
                    append("拉取失败: " + e.getMessage());
                }
            }
        }));
        box.addView(button("4. 捕获一条崩溃", new View.OnClickListener() {
            @Override public void onClick(View v) {
                int n = CrashKit.get().capture(new RuntimeException("演示崩溃"));
                append("已入队，待上报: " + n + " 条");
            }
        }));
        box.addView(button("5. 立即上报崩溃", new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    int accepted = CrashKit.get().flush();
                    append("服务端接收: " + accepted + " 条");
                } catch (Exception e) {
                    append("上报失败: " + e.getMessage() + "（需配置真实 OpenHub 地址）");
                }
            }
        }));

        scroll.addView(box);
        setContentView(scroll);
        append("AndroidKit Showcase 就绪。三套 SDK 已初始化。");
    }

    private Button button(String text, View.OnClickListener l) {
        Button b = new Button(this);
        b.setText(text);
        b.setOnClickListener(l);
        return b;
    }

    private void append(String line) {
        sb.append(line).append('\n');
        log.setText(sb.toString());
    }
}
