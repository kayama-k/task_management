-- GET /api/lists の動作確認用テストデータ。
-- 注意: requirements.md 4.1 の「初回起動時デフォルト3リスト」機能(未実装)とは別物。
-- あくまでこのAPIの読み取り確認のためのシードデータであり、固定ID(lists: 1001-1004,
-- cards: 2001-2004)に対して DELETE → INSERT するため、./gradlew bootRun を
-- 何度実行してもデータが重複しない(再実行安全)。

DELETE FROM cards WHERE id IN (2001, 2002, 2003, 2004);
DELETE FROM lists WHERE id IN (1001, 1002, 1003, 1004);

INSERT INTO lists (id, title, position) VALUES
    (1001, '[Seed] 今週のタスク', 0),
    (1002, '[Seed] 進行中', 1),
    (1003, '[Seed] 完了済み', 2),
    (1004, '[Seed] 空リスト', 3);

INSERT INTO cards (id, list_id, title, description, position, due_at, created_at) VALUES
    (2001, 1001, '買い物リストを作る', '', 0, NULL, now()),
    (2002, 1001, '資料を読む', '来週の会議用', 1, '2026-09-20 10:00:00', now()),
    (2003, 1002, '実装中のタスク', '', 0, NULL, now()),
    (2004, 1003, '完了済みタスクA', '', 0, NULL, now());

-- 将来の書き込みAPI実装後、アプリ生成のidがseed用の固定idと衝突しないようにする。
SELECT setval(pg_get_serial_sequence('lists', 'id'), COALESCE((SELECT MAX(id) FROM lists), 1));
SELECT setval(pg_get_serial_sequence('cards', 'id'), COALESCE((SELECT MAX(id) FROM cards), 1));
