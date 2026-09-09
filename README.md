# task_management
Task Management Project

## Task Board (トレロ風タスク管理アプリ)

デスクトップのブラウザで使う、Trello風のタスク管理Webアプリ(演習)です。

詳細な要件は [docs/requirements.md](docs/requirements.md)(要件定義書)、前提となる依頼者ニーズは [docs/needs-analysis.md](docs/needs-analysis.md)(要求分析書)を参照してください。

### 機能
- リスト(列)の作成・リネーム・削除
- カードの作成・編集・削除
- カードへの予定日時(いつやるか)の任意設定(タスクごとにON/OFF可能。予定日時を過ぎている・24時間以内は色分け表示)
- ドラッグ&ドロップでカードをリスト内・リスト間で移動
- 背景のカスタマイズ(プリセットカラー、またはローカル画像のアップロード。ウィンドウサイズに追従してフィット表示)

> **状態**: `docs/requirements.md`(10. 技術構成)の方針に基づき、バックエンドをNode.js/ExpressからJava/Spring Bootへ移行中です。現在の `backend/` はSpring Bootの最小構成(ひな形)で、ヘルスチェック(`/api/health`)のみが実装済みです。リスト/カードのCRUD・ドラッグ&ドロップなど実際のAPIはまだ移植されていないため、**現時点では `frontend/` から `backend/` への実際のAPI呼び出しは動作しません**。上記の機能一覧は、要件定義書上の仕様および `docs/mockup.html`(プロトタイプ、バックエンド不要)で確認できます。

### 技術構成
| 層 | 技術 | 状態 |
|---|---|---|
| フロントエンド | React + Vite、ドラッグ&ドロップは [@dnd-kit](https://dndkit.com/) | 稼働中(JavaScript。TypeScript+Tailwind CSSへの移行は未着手) |
| バックエンド | Java + Spring Boot + Gradle | ひな形段階(ヘルスチェックのみ) |
| DB | 未接続(PostgreSQL予定) | 未着手。`/actuator/health` は正直に `DOWN` を返す |

### セットアップと起動

**バックエンド**(必要環境: JDK 25以上。Gradleは同梱のWrapperを使うため別途インストール不要)
```bash
cd task_management/backend
./gradlew.bat bootRun   # Windows。macOS/Linuxは ./gradlew bootRun
```
起動後、以下で動作確認できます(ポート8080)。
- `GET http://localhost:8080/api/health` → `{"status":"ok"}`(アプリ自体の起動確認)
- `GET http://localhost:8080/actuator/health` → `{"status":"DOWN", ...}`(HTTP 503)。DB(PostgreSQL)がまだ未接続であることを `database` コンポーネントが正直に示す

**フロントエンド**(必要環境: Node.js。動作確認はNode 24系)
```bash
cd task_management/frontend
npm install
npm run dev
```
ブラウザで http://localhost:5173 を開くとボードが表示されますが、前述の通りバックエンドAPIが未実装のため、リスト/カードの読み込みなどは動作しません。

**プロトタイプ**(バックエンド不要ですぐ試せる版): [docs/mockup.html](docs/mockup.html) をブラウザで直接開いてください。データはブラウザの `localStorage` に保存されます。

### データの保存先
- タスクデータ: 未着手。データベース(PostgreSQL予定)自体が未接続のため、現時点では何も保存されません
- 背景設定(色・画像): ブラウザのローカルストレージ(サーバー側DBには保存されません)
- プロトタイプ(`docs/mockup.html`)のデータ: ブラウザの `localStorage`(上記アプリ本体とは別管理)

### ディレクトリ構成
```
task_management/
├── docs/
│   ├── requirements.md   # 要件定義書
│   ├── needs-analysis.md # 要求分析書
│   └── mockup.html       # 動作するプロトタイプ(バックエンド不要)
├── backend/             # Spring Boot バックエンド(ひな形段階)
│   ├── build.gradle
│   ├── gradlew / gradlew.bat
│   └── src/main/java/com/taskmanagement/backend/
│       ├── BackendApplication.java
│       ├── HealthController.java
│       └── DatabaseHealthIndicator.java  # DB未接続を正直にDOWN表示
└── frontend/            # React (Vite) アプリ
    └── src/
        ├── App.jsx       # 状態管理・ドラッグ&ドロップ制御
        ├── api.js        # バックエンドAPIクライアント
        └── components/
            ├── Board.jsx
            ├── List.jsx
            ├── Card.jsx
            ├── AddListForm.jsx
            ├── AddCardForm.jsx
            ├── CardModal.jsx
            └── BackgroundPicker.jsx
```
