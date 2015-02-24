１） app/src/debug/res/values/google_maps_api.xml に作成した API key を追加してください

<string name="google_maps_key" templateMergeStrategy="preserve">YOUR API KEY</string>


２）Google Developers Console で新しいプロジェクトを作成します。
https://console.developers.google.com/project


３）Build/Deploy Module To App Engine… で、GAE にサーバサイドプログラムをデプロイします。その際、２）で作成したプロジェクトを選択します。作成したプロジェクトは https://appengine.google.com/ にて確認することができます。


４）MapsActivity.java の API_ROOT_URL を作成したプロジェクトの URL に変更します。

private static final String API_ROOT_URL = "https://YOUR_PROJECT_URL/_ah/api/";