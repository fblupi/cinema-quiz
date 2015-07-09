package com.hijosdevuctir.cinemaquiz;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class OtherGamesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_games_activity);

        WebView myWebView = (WebView) this.findViewById(R.id.webViewOtherGames);
        myWebView.loadUrl("http://html5games.com/");
    }

}
