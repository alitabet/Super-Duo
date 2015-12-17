package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utility;

/**
 * RemoteViewsService controlling the data being shown in the scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = DetailWidgetRemoteViewsService.class.getSimpleName();

    public static final int COL_DATE = 1;
    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_LEAGUE = 5;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_ID = 8;
    public static final int COL_MATCHDAY = 9;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_list_item);

                String homeTeam = data.getString(COL_HOME);
                String awayTeam = data.getString(COL_AWAY);
                String matchTime = data.getString(COL_MATCHTIME);
                String date = data.getString(COL_DATE);
                String score = Utility.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS));
                int homeImageResource = Utility.getTeamCrestByTeamName(
                        data.getString(COL_HOME));
                int awayImageResource = Utility.getTeamCrestByTeamName(
                        data.getString(COL_AWAY));

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
                views.setTextViewText(R.id.date_textview, date);
                views.setTextViewText(R.id.time_textview, matchTime);

                String description = Utility.getGameDescription(
                        DetailWidgetRemoteViewsService.this, homeTeam, awayTeam,
                        data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS), date, matchTime,
                        data.getInt(COL_LEAGUE), data.getInt(COL_MATCHDAY));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views, description);
                }
//                views.setContentDescription(position, Utility.getGameDescription(
//                        getApplicationContext(), homeTeam, awayTeam,
//                        data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS),
//                        date, matchTime,
//                        data.getInt(COL_LEAGUE), data.getInt(COL_MATCHDAY)));
                final Intent fillInIntent = new Intent();
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_detail_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return Long.parseLong(data.getString(COL_ID));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
