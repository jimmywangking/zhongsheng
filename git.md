## 1.项目根目录执行初始化git

```bash
cd /Users/helloworld/Downloads/CRMSchrdule
git init
git add .
git commit -m "Init CrmSchedule project"
```

## 2.Github 操作

1. 先创建 GitHub 仓库
在 GitHub 上新建一个空仓库（例如 crm-schedule），不要勾选“用 README 初始化”（可选，但建议避免冲突）。

拿到仓库地址，形如：

```
https://github.com/<你的用户名>/<你的仓库名>.git
```

1. 绑定 remote
在终端进入项目目录：

```shell
cd /Users/helloworld/Downloads/CRMSchrdule/CrmSchedule
```

设置远程仓库（把占位符替换成你的）：

```bash
git remote add origin https://github.com/<你的用户名>/<你的仓库名>.git
```

如果你之前已经加过 origin，改用：

```bash
git remote set-url origin https://github.com/<你的用户名>/<你的仓库名>.git
```

1. 推送到 GitHub

```bash
git push -u origin main
```

之后去 GitHub 页面刷新即可看到代码。

## 3.SSH配置

SSH（一次配置后更方便）
生成密钥：

```bash
ssh-keygen -t ed25519 -C "你的邮箱"
```

把公钥复制到剪贴板并粘到 GitHub：

```bash
cat ~/.ssh/id_ed25519.pub
```

GitHub：Settings → SSH and GPG keys → New SSH key → 粘贴
修改 remote：

```bash
git remote set-url origin git@github.com:jimmywangking/zhongsheng.git
```

再推：

```bash
git push -u origin main
```

## 4.代码提交

直接在当前项目里用 git 命令就可以，步骤是固定的三步：拉主分支 → 新建分支 → 提交并推送。

1. 确保在项目目录

```bash
cd /Users/helloworld/Downloads/CRMSchrdule/CrmSchedule
```

1. 基于最新 main 创建新分支

```bash
git checkout main          # 回到主分支（如果当前不在）
git pull origin main       # 同步远端最新代码（第一次可能提示设置 token）
git checkout -b feature-xxx   # 新分支名自己起，比如 feature-sync-log-ui
```

1. 提交你这次的修改

```bash
git status                 # 看看有哪些文件改了
git add .                  # 或者只 add 你想提交的文件
git commit -m "你的提交说明"   # 如：git commit -m "Optimize sync task logging"
```

1. 推送新分支到 GitHub

```bash
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

```bash
git checkout main
git pull origin main
git checkout -b feature/某功能
```

# 写代码、自测

```bash
git add .
git commit -m "简短说明"
git push -u origin feature/某功能
```

在 GitHub 上开 Pull Request：feature/某功能 → main，代码审查通过后 Merge。

- 合并后本地清理：

```bash
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

```bash
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

## 6. 创建一个分支并提交回main

1. 用当前修改新建分支并提交

```bash
cd /path/to/你的仓库根目录   # 有 .git 的那一层
```

# 从当前 main 拉出分支（未提交的修改会跟过去）

```bash
git checkout -b feature/git.md
git add git.md
git commit -m "更新 git 说明文档"
git push -u origin feature/git.md
```

之后在 GitHub 上开 Pull Request：feature/git.md → main，审查后点 Merge（推荐，有记录）。

1. 合并回 main（本地合并，不经过 PR 时）

```bash
git checkout main
git pull origin main
git merge feature/git.md
git push origin main
```

合并完可删分支（可选）：

```bash
git branch -d feature/git.md
git push origin --delete feature/git.md
```

1. 若分支名想避免 .md（可选）

有的团队不用扩展名当分支名，可以改成：

```bash
git checkout -b feature/git-doc
```

逻辑一样。

1. 若你已经把 git.md 的修改 提交在 main 上了

先告诉我，或自己用「把最后一次提交挪到新分支」的方式：

```bash
git branch feature/git.md
git reset --hard HEAD~1        # main 退回上一提交（慎用，确认没别的提交）
git checkout feature/git.md
git push -u origin feature/git.md
```

HEAD~1 只在你「多出来的就是那一个提交」时安全；情况复杂时先 git log 看一眼再操作。

最短路径：git checkout -b feature/git.md → git add / commit → push → GitHub 上 PR 合并到 main（或本地 checkout main + merge + push）。

## 7.查看分支历史和回退

可以分两步：先看历史，再决定回退方式（个人分支 vs 已推送/多人协作，做法不同）。

1. 查看某个分支的历史

先切到该分支（或指定分支名看 log）：

```bash
git checkout 分支名
git log --oneline -20
```

更紧凑的一行一条：
```shell
git log 分支名 --oneline -30
```
带图形（可选）：
```bash
git log 分支名 --oneline --graph -20
```
需要看某次改动了哪些文件：
```bash
git show <commit的hash>
```
2. 回退到某一个版本（三种常见做法）
假设目标提交的 hash 是 abc1234（用 git log 里复制）。

方式 A：只在本机「回到过去」看一眼（不改动分支指针）
```bash
git checkout abc1234
```
会进入 detached HEAD，适合临时查看/编译。看完回到分支：
```bash
git checkout 你的分支名
```
方式 B：把当前分支直接指到旧提交（会改历史）
适合：还没 push，或确定只有你一个人用这条分支。
```bash
git checkout 你的分支名
git reset --hard abc1234
```
之后若需要同步到远程（会覆盖远程该分支，慎用）：
```bash
git push --force-with-lease origin 你的分支名
```
方式 C：在已合并到 main / 多人协作的线上历史：用「反做」而不是硬 reset
适合：已经 push 且别人可能基于你的提交，用 revert 更安全：
```bash
git revert <要撤销的那次提交的hash>
```
# 或连续多笔用 revert 范围，视情况处理
这样是在新提交里撤销改动，不强行改历史。

## 回退方式怎么选（简表）

| 情况 | 建议 |
|------|------|
| 只想看旧代码、不打算改分支 | `git checkout <hash>`，看完再 `git checkout <分支名>` |
| 分支只在你本机、可随便改 | `git reset --hard <hash>` |
| 已 push、别人在用 / 在 main 上 | 优先 `git revert`，避免 `reset` + `--force` |

如果你说一下是「feature 分支还没 push」还是「已经 push 到 GitHub」，我可以给你一条最稳妥的具体命令序列。

