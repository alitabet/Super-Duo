package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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

    public static final int COL_DATE = 1;
    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_LEAGUE = 5;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_ID = 8;
    public static final int COL_MATCHDAY = 9;

    public ScoresWidgetIntentService() {
        super("ScoresWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                ScoresWidgetProvider.class));

        Date fragmentdate = new Date(System.currentTimeMillis() - 86400000);
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

        String date[] = new String[1];
        date[0] = mformat.format(fragmentdate);

        Cursor data = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, null);
//                .query(DatabaseContract.scores_table.buildScoreWithDate(),
//                null, null, date, null);

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
//            int layoutId = R.layout.scores_app_widget;
            // Find the correct layout based on the widget's width
            int widgetWidth = getWidgetWidth(appWidgetManager, appWidgetId);
            int defaultWidth = getResources().getDimensionPixelSize(R.dimen.widget_score_default_width);
            int layoutId;
           if (widgetWidth >= defaultWidth) {
                layoutId = R.layout.scores_app_widget;
            } else {
                layoutId = R.layout.scores_app_widget_small;
            }
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

            String description = Utility.getGameDescription(
                    this, homeTeam, awayTeam,
                    data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS),
                    data.getString(COL_DATE), matchTime,
                    data.getInt(COL_LEAGUE), data.getInt(COL_MATCHDAY));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, description);
            }

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget, description);
    }

    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_score_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return  getResources().getDimensionPixelSize(R.dimen.widget_score_default_width);
    }
}
