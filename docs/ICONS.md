# CClaude 应用图标

## 图标设计

### 概念
- **字母 C**: 代表 CClaude
- **AI 点缀**: 小圆点和连线代表 AI/神经网络
- **橙色主题**: `#E57035` 渐变到 `#D86028`

### 文件结构
```
app/src/main/res/
├── mipmap-anydpi-v26/          # 自适应图标 (Android 8.0+)
│   ├── ic_launcher.xml
│   └── ic_launcher_round.xml
├── mipmap-mdpi/                # 48x48
│   └── ic_launcher.png
├── mipmap-hdpi/                # 72x72
│   └── ic_launcher.png
├── mipmap-xhdpi/               # 96x96
│   └── ic_launcher.png
├── mipmap-xxhdpi/              # 144x144
│   └── ic_launcher.png
├── mipmap-xxxhdpi/             # 192x192
│   └── ic_launcher.png
└── drawable/
    ├── ic_launcher_background.xml   # 自适应图标背景
    └── ic_launcher_foreground.xml   # 自适应图标前景
```

## 生成 PNG 图标

### 方法 1: 使用 Python 脚本

```bash
# 安装依赖
pip install cairosvg

# 生成所有图标
python3 generate-icons.py
```

### 方法 2: 使用在线工具

1. 打开 `docs/icons/cclaude-icon.svg`
2. 访问 https://convertio.co/svg-png/
3. 转换并下载以下尺寸：
   - 48x48 (mdpi)
   - 72x72 (hdpi)
   - 96x96 (xhdpi)
   - 144x144 (xxhdpi)
   - 192x192 (xxxhdpi)
   - 512x512 (Play Store)

### 方法 3: 使用 Android Studio

1. 右键点击 `res` 文件夹
2. New → Image Asset
3. 选择 Icon Type: Launcher Icons (Adaptive and Legacy)
4. Foreground Layer: 选择 `docs/icons/cclaude-icon.svg`
5. Background Layer: 颜色 `#E57035`
6. 点击 Next → Finish

## Play Store 图标

- 尺寸: 512x512
- 格式: PNG (32-bit)
- 位置: `app/src/main/ic_launcher-playstore.png`

## 自适应图标预览

```xml
<!-- ic_launcher.xml -->
<adaptive-icon>
    <background android:drawable="@drawable/ic_launcher_background"/>
    <foreground android:drawable="@drawable/ic_launcher_foreground"/>
</adaptive-icon>
```

### 背景层
- 渐变橙色背景
- 108dp x 108dp 画布

### 前景层
- 白色字母 C
- AI 小圆点和连接线
- 安全区域: 66dp 直径圆形内

## 设计规范

| 属性 | 值 |
|------|-----|
| 主色调 | `#E57035` (Claude Orange) |
| 辅助色 | `#D86028` (Deep Orange) |
| 图标颜色 | 白色 `#FFFFFF` |
| 画布尺寸 | 108dp x 108dp |
| 安全区域 | 66dp 直径 |
| 圆角 | 自适应系统遮罩 |

## F-Droid 图标要求

- ✅ 无透明度问题
- ✅ 纯色背景
- ✅ 清晰可识别
- ✅ 支持自适应图标

## 更新图标

修改 `docs/icons/cclaude-icon.svg` 后重新运行生成脚本。
