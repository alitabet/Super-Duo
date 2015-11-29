package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utility;

/**
 * Created by alitabet on 11/29/15.
 */
public class ScoresWidgetIntentService extends IntentService {

    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;

    public ScoresWidgetIntentService() {
        super("ScoresWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                ScoresWidgetProvider.class));

        Date fragmentdate = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

        String date[] = new String[1];
        date[0] = mformat.format(fragmentdate);

        Cursor data = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, date, null);

        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        String homeTeam = data.getString(COL_HOME);
        String awayTeam = data.getString(COL_AWAY);
        String matchTime = data.getString(COL_MATCHTIME);
        String score = Utility.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS));
        int homeImageResource = Utility.getTeamCrestByTeamName(
                data.getString(COL_HOME));
        int awayImageResource = Utility.getTeamCrestByTeamName(
                data.getString(COL_AWAY));
        data.close();

//        String homeTeam = "Arsenal";
//        String awayTeam = "Manchester United";
//        String matchTime = "10:00pm";
//        String score = Utility.getScores(0, 0);
//        int homeImageResource = Utility.getTeamCrestByTeamName("Arsenal London FC");
//        int awayImageResource = Utility.getTeamCrestByTeamName("Manchester United FC");

        // Perform this loop procedure for each widget
        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.scores_app_widget;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setImageViewResource(R.id.home_crest, homeImageResource);
            views.setImageViewResource(R.id.away_crest, awayImageResource);

//            // Content Descriptions for RemoteViews were only added in ICS MR1
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                setRemoteContentDescription(views, description);
//            }
            views.setTextViewText(R.id.home_name, homeTeam);
            views.setTextViewText(R.id.away_name, awayTeam);
            views.setTextViewText(R.id.score_textview, score);
            views.setTextViewText(R.id.date_textview, matchTime);

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
