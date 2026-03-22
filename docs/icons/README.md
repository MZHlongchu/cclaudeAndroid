# CClaude 图标资源

## 图标预览

```
┌─────────────────────┐
│     ████████████    │
│   ███░░░░░░░░░░███  │
│  ██░░░░░░░░░░░░░░██ │
│ ██░░░░████████░░░░██│
│██░░░░██      ██░░░██│
│██░░░██   ██   █░░░██│
│██░░░██  ████  █░░░██│
│██░░░░██ ████ █░░░░██│
│ ██░░░░██████░░░░░██ │
│  ██░░░░░░░░░░░░░██  │
│   ███░░░░░░░░░███   │
│     ████████████    │
│            ●        │
└─────────────────────┘
    CClaude Logo
```

## 设计元素

- **主图形**: 白色字母 C
- **装饰**: AI 智能点 (右上角小圆点 + 连接线)
- **背景**: Claude 橙色渐变 `#E57035` → `#D86028`

## 文件说明

| 文件 | 描述 |
|------|------|
| `cclaude-icon.svg` | 矢量源文件 |
| `ic_launcher-playstore.png` | Play Store 512x512 |
| `ic_launcher_foreground.xml` | 自适应图标前景 |
| `ic_launcher_background.xml` | 自适应图标背景 |

## 使用方法

### 生成高质量图标

```bash
# 1. 安装依赖
pip install cairosvg

# 2. 生成所有尺寸
python3 generate-icons.py
```

### Android Studio 方式

1. 右键 `res` → New → Image Asset
2. Icon Type: Launcher Icons
3. Asset: 选择 `cclaude-icon.svg`
4. Background: `#E57035`
5. Finish

## 颜色规范

| 用途 | 颜色 | Hex |
|------|------|-----|
| 主色 | Claude Orange | `#E57035` |
| 深色 | Deep Orange | `#D86028` |
| 图标 | 白色 | `#FFFFFF` |
