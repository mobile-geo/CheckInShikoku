
1) Add your API key at "app/src/debug/res/values/google_maps_api.xml"
   <string name="google_maps_key" templateMergeStrategy="preserve">YOUR API KEY</string>


2) Create new project on Google Developers Console
   https://console.developers.google.com/project


3) Deploy the server side program to AppEngine by "Build > Deploy Module To App Engine…". 
   You need to select your own project ID by section 2).

4) Change the APR_ROOT_URL to your AppEngine URL in the MapsActivity.java
   private static final String API_ROOT_URL = "https://YOUR_PROJECT_URL_HERE/_ah/api/";

5) Rebuild and Run.