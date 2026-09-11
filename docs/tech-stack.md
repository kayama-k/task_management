# 技術構成書

Trello風タスク管理アプリ(架空案件)の技術スタック(採用技術・選定理由)をまとめた資料。機能要件・API仕様は [requirements.md](requirements.md)(要件定義書)、データベースの詳細設計は [database-design.md](database-design.md)(データベース設計書)を参照。

> **注記**: 以下は今後移行予定の技術スタックである。バックエンドは `backend/` をSpring Boot版に一本化済み(旧Node.js版は削除)だが、まだ最小構成(ひな形、REST APIエンドポイント未実装)の段階。DB(PostgreSQL)はDocker Compose経由で接続確認済みで、`lists`/`cards`のテーブル・JPAエンティティも作成済み(詳細は[database-design.md](database-design.md))。CRUD APIの実装はまだ。フロントエンド(`frontend/`)はまだ移行前(JavaScript、素のCSS)のまま。詳細は requirements.md の「12. 検討・変更の経緯」を参照。

## 1. フロントエンド

| 項目 | 技術 |
|---|---|
| フレームワーク | React |
| 言語 | TypeScript |
| ビルドツール | Vite |
| ドラッグ&ドロップ | dnd-kit |
| スタイリング | Tailwind CSS |
| パッケージ管理 | npm |

## 2. バックエンド

| 項目 | 技術 |
|---|---|
| 言語 | Java |
| フレームワーク | Spring Boot |
| ビルドツール | Gradle |
| API形式 | REST API |

## 3. データベース

| 項目 | 技術 |
|---|---|
| RDBMS | PostgreSQL |
| 実行環境(開発時) | Docker Compose(`backend/compose.yaml`。Spring Bootのdocker-compose連携により `bootRun` 時に自動起動・停止) |

> Spring Data JPAでの接続確認、および `lists`/`cards` のテーブル・JPAエンティティ作成は完了している。テーブル定義・ER図などの詳細は [database-design.md](database-design.md)(データベース設計書)を参照。CRUD APIエンドポイントの実装はまだ(次段階)。

## 4. 開発ツール

| 項目 | 技術 |
|---|---|
| バージョン管理 | Git + GitHub |

## 5. 移行前(現行実装)からの変更点

| 項目 | 移行前(現行実装) | 移行後(採用) |
|---|---|---|
| フロントエンド言語 | JavaScript | TypeScript |
| スタイリング | 素のCSS | Tailwind CSS |
| バックエンド | Node.js + Express | Java + Spring Boot(Gradle) |
| データベース | SQLite(`node:sqlite`) | PostgreSQL |
| ドラッグ&ドロップ | `@dnd-kit`(変更なし) | `dnd-kit`(変更なし) |
