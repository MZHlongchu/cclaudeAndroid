# GitHub 推送指南

## 1. 打开终端，进入项目目录

```bash
cd CClaudeAndroid
```

## 2. 添加所有文件并提交

```bash
git add .
git commit -m "feat: CClaude Android v1.0.0 - Privacy-first AI assistant with F-Droid support"
```

## 3. 推送到 GitHub

```bash
git push -u origin main
```

## 4. 验证推送

访问 https://github.com/MZHlongchu/cclaudeAndroid 查看你的仓库

## 完整命令序列

```bash
#!/bin/bash
cd CClaudeAndroid
git init
git add .
git commit -m "feat: CClaude Android v1.0.0"
git branch -M main
git remote add origin https://github.com/MZHlongchu/cclaudeAndroid.git
git push -u origin main
```

## 首次推送后的更新

```bash
git add .
git commit -m "your commit message"
git push
```
