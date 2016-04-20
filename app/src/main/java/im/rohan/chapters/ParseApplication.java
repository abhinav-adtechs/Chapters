package im.rohan.chapters;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    private static final String APPLN_ID = "E1vlVGfAtYFPx4ydOTuBcI7QVOxx5ZijVJWkUuCn", CLIENT_KEY = "fuOwfooiVLDkFNJ8Nm1LSCBp7iaj4xteTcY6O8vg";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, APPLN_ID, CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

    }

}
