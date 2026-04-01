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

## 5.多分支开发在做什么
一条主干（通常是 main）保持相对稳定；每个功能/修 bug 在独立分支里开发，改完再合并回 main。这样多人可以同时开发、互不把半成品推到主线上。

- 常见分支名约定（可按团队习惯调整）
分支类型	命名示例	用途
长期主干	main	可发布/集成基线
功能	feature/crm-sync-log	新功能
修复	fix/login-timeout	修线上/测试问题
发布（可选）	release/1.3.0	发版前冻结、只修 bug
单人日常流程（你从 main 拉分支）
```
git checkout main
git pull origin main
git checkout -b feature/某功能
```
# 写代码、自测
```
git add .
git commit -m "简短说明"
git push -u origin feature/某功能
```
在 GitHub 上开 Pull Request：feature/某功能 → main，代码审查通过后 Merge。

- 合并后本地清理：
```
git checkout main
git pull origin main
git branch -d feature/某功能        # 删本地分支（可选）
git push origin --delete feature/某功能   # 删远程分支（可选）
```
- 多人并行时怎么配合
每人自己的功能分支，不要长期在别人的分支上直接改。
main 经常同步：开始新功能前先 pull 最新 main。
分支寿命尽量短：几天内合回 main，减少冲突。
冲突在 PR 或合并前解决：把 main 合并进你的分支再推：
```
git checkout feature/某功能
git fetch origin
git merge origin/main    # 或 git rebase origin/main（团队习惯 rebase 时用）
```

- 解决冲突 → commit → push
和「当前主线」的关系
默认开发分支以远程为准：git branch -r 里带 origin/ 的那条，一般是 origin/main。
本地多开几个终端/IDE 窗口时，注意左下角当前分支，避免把提交错分支上。

# 小结
一个任务一个分支，从 main 拉出，做完 PR 合并 回 main。
并行开发 = 多人在不同 feature/* 上各自提交，最后用 PR 汇到 main。
如果你说一下是「只有你一个人」还是「多人 + 要不要 code review」，可以帮你缩成一套你们团队专用的最短命令清单。
