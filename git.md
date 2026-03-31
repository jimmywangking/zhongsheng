## 1.项目根目录执行初始化git
```
cd /Users/helloworld/Downloads/CRMSchrdule
git init
git add .
git commit -m "Init CrmSchedule project"
```

## 2.Github 操作

1) 先创建 GitHub 仓库
在 GitHub 上新建一个空仓库（例如 crm-schedule），不要勾选“用 README 初始化”（可选，但建议避免冲突）。

拿到仓库地址，形如：
```
https://github.com/<你的用户名>/<你的仓库名>.git
```
2) 绑定 remote
在终端进入项目目录：
```
cd /Users/helloworld/Downloads/CRMSchrdule/CrmSchedule
```
设置远程仓库（把占位符替换成你的）：
```
git remote add origin https://github.com/<你的用户名>/<你的仓库名>.git
```
如果你之前已经加过 origin，改用：
```
git remote set-url origin https://github.com/<你的用户名>/<你的仓库名>.git
```
3) 推送到 GitHub
```
git push -u origin main
```
之后去 GitHub 页面刷新即可看到代码。

## 3.SSH配置

SSH（一次配置后更方便）
生成密钥：
```
ssh-keygen -t ed25519 -C "你的邮箱"
```
把公钥复制到剪贴板并粘到 GitHub：
```
cat ~/.ssh/id_ed25519.pub
```
GitHub：Settings → SSH and GPG keys → New SSH key → 粘贴
修改 remote：
```
git remote set-url origin git@github.com:jimmywangking/zhongsheng.git
```
再推：
```
git push -u origin main
```

## 4.代码提交
直接在当前项目里用 git 命令就可以，步骤是固定的三步：拉主分支 → 新建分支 → 提交并推送。

1. 确保在项目目录
```
cd /Users/helloworld/Downloads/CRMSchrdule/CrmSchedule
```
2. 基于最新 main 创建新分支
```
git checkout main          # 回到主分支（如果当前不在）
git pull origin main       # 同步远端最新代码（第一次可能提示设置 token）
git checkout -b feature-xxx   # 新分支名自己起，比如 feature-sync-log-ui
```
3. 提交你这次的修改
```
git status                 # 看看有哪些文件改了
git add .                  # 或者只 add 你想提交的文件
git commit -m "你的提交说明"   # 如：git commit -m "Optimize sync task logging"
```
4. 推送新分支到 GitHub
```
git push -u origin feature-xxx
```
之后你可以在 GitHub 仓库网页上用这个新分支发 PR。
