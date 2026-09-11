# タスク読み取りAPI実装計画

- 状態: **実装済み**
- 要件(何を作るか)は [requirements.md](requirements.md)(要件定義書)9.1、DBのテーブル・エンティティ設計は [database-design.md](database-design.md)(データベース設計書)を参照。

## Context

バックエンドにはEntity(`TaskList.java`、`Card.java`)は実装済みだったが、Repository/Service/Controller層が未実装。タスクの読み取り(READ)APIを実装し、テストデータを投入して動作確認する。

対象範囲は `requirements.md` 9.1に定義されている `GET /api/lists`(全リストを、所属カード込みでposition順に取得)のみとすることでユーザーと合意。キーワード検索・ステータス別取得・ID指定取得等の絞り込みAPIは今回の対象外。

## 実装手順

1. **GitHub Issue作成**
   - `gh issue create` でタスク読み取りAPI実装のIssueを作成する
   - [Issue #1](https://github.com/kayama-k/task_management/issues/1) として作成済み。この回から、Issue+ブランチ運用を開発プロセスに導入することとした。

2. **ブランチ作成**
   - `feature/#<issue番号>-task-read-api` 形式のブランチを作成する
   - `feature/#1-task-read-api` を作成し、以降の変更(手順3〜7)はこのブランチ上で行った(mainには直接コミットしない)。

3. **Repository層作成**
   - `backend/src/main/java/com/taskmanagement/backend/repository/TaskListRepository.java`
   - `JpaRepository<TaskList, Long>` を継承し、`findAllByOrderByPositionAsc()` を定義

4. **DTO作成**
   - `backend/src/main/java/com/taskmanagement/backend/dto/CardResponse.java` / `ListResponse.java`(record)
   - `TaskList`/`Card` エンティティを直接返すと、①`cards`が遅延ロードのため `LazyInitializationException`、②`TaskList.cards`⇔`Card.list`の双方向関連による無限再帰、が起きるため、読み取り専用のDTOに変換してから返す方針とした

5. **Service層作成**
   - `backend/src/main/java/com/taskmanagement/backend/service/TaskListQueryService.java`
   - 全リスト取得(カード込み、position順)の1メソッド `findAllLists()`。`@Transactional(readOnly = true)` により、取得とDTO変換(手順4)を1トランザクション内で行い、遅延ロードされる`cards`を安全に初期化する

6. **Controller層作成**
   - `backend/src/main/java/com/taskmanagement/backend/controller/TaskListController.java`
   - エンドポイント: `GET /api/lists` — 全リストをカード込み(position順)で取得

7. **テストデータ投入**
   - `backend/src/main/resources/data.sql` — Hibernate初期化後に自動実行(再実行安全: 固定ID(lists: 1001-1004、cards: 2001-2004)に対して`DELETE`→`INSERT`するため、`./gradlew bootRun`を何度実行してもデータが重複しない)
   - リスト4件(空リストを含む)・カード4件(`description`あり/なし、`due_at`あり/なし)を投入
   - `application.properties`(本プロジェクトは一貫してproperties形式。ymlではない)に `spring.sql.init.mode=always` と `spring.jpa.defer-datasource-initialization=true` を追加

## 動作確認結果

1. `./gradlew bootRun` でPostgreSQLコンテナ(`compose.yaml`)が自動起動し、アプリが8080番で起動することを確認。
2. `curl http://localhost:8080/api/lists` → リスト4件が `position` 昇順、各リストの `cards` も `position` 昇順でネスト、空リストは `"cards": []`、`dueAt` の有無も正しく返ることを確認。
3. アプリを再起動して再度確認 → 同じ4件・4カードのみが返り、重複が発生しないことを確認(`createdAt` のみ再投入時刻に更新される、想定通りの挙動)。
4. `docker exec backend-postgres-1 psql -U taskmanagement -d taskmanagement -c "..."` でDBの生データを直接確認し、APIレスポンスと完全に一致することを確認(`docs/database-design.md` §6の既存の確認方法を踏襲)。

## 既知の課題・スコープ外

- `data.sql` は現状「毎起動時にこの4件をリセットする」仕様。将来書き込みAPI(POST/PATCH/DELETE)を実装した際、API経由で編集した内容が起動のたびに上書きされる。専用profileでの無効化などは未対応(必要になった時点で対応)。
- `findAllByOrderByPositionAsc()` + 遅延ロードの `cards` により、リスト件数分の追加クエリ(N+1)が発生する。現状の規模では問題ないが、将来的に `JOIN FETCH` 最適化の余地あり。
- POST `/api/lists`、PATCH `/api/lists/:id`、DELETE `/api/lists/:id`、POST `/api/cards`、PATCH `/api/cards/:id`、PATCH `/api/cards/:id/move`、DELETE `/api/cards/:id` は本計画のスコープ外(未実装)。
