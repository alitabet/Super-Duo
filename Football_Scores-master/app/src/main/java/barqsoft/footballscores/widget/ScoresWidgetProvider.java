package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import barqsoft.footballscores.service.myFetchService;

/**
 * Created by alitabet on 11/29/15.
 */
public class ScoresWidgetProvider extends AppWidgetProvider {
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        String homeTeam = "Arsenal";
//        String awayTeam = "Manchester United";
//        String matchTime = "10:00pm";
//        String score = Utility.getScores(0, 0);
//        int homeImageResource = Utility.getTeamCrestByTeamName("Arsenal London FC");
//        int awayImageResource = Utility.getTeamCrestByTeamName("Manchester United FC");
//
//        // Perform this loop procedure for each widget
//        for (int appWidgetId : appWidgetIds) {
//            int layoutId = R.layout.scores_app_widget;
//            RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
//
//            // Add the data to the RemoteViews
//            views.setImageViewResource(R.id.home_crest, homeImageResource);
//            views.setImageViewResource(R.id.away_crest, awayImageResource);
//
////            // Content Descriptions for RemoteViews were only added in ICS MR1
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
////                setRemoteContentDescription(views, description);
////            }
//            views.setTextViewText(R.id.home_name, homeTeam);
//            views.setTextViewText(R.id.away_name, awayTeam);
//            views.setTextViewText(R.id.score_textview, score);
//            views.setTextViewText(R.id.date_textview, matchTime);
//
//            // Create an Intent to launch MainActivity
//            Intent launchIntent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
//            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//
//            // Tell the AppWidgetManager to perform an update on the current app widget
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
//    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, ScoresWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, ScoresWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (myFetchService.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            context.startService(new Intent(context, ScoresWidgetIntentService.class));
        }
    }
}
