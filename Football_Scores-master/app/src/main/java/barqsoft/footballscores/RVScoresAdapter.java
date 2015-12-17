package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by alitabet on 11/28/15.
 */
public class RVScoresAdapter extends RecyclerView.Adapter<RVScoresAdapter.ViewHolder> {

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;
//    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    private Cursor mCursor;
    final private Context mContext;
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;

    public RVScoresAdapter(Context context) {
        mContext = context;
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView home_name;
        public TextView away_name;
        public TextView score;
        public TextView date;
        public ImageView home_crest;
        public ImageView away_crest;
        public double match_id;
        public ViewGroup container;
        public LinearLayout linearLayout;
        public View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            home_name = (TextView) view.findViewById(R.id.home_name);
            away_name = (TextView) view.findViewById(R.id.away_name);
            score     = (TextView) view.findViewById(R.id.score_textview);
            date      = (TextView) view.findViewById(R.id.date_textview);
            home_crest = (ImageView) view.findViewById(R.id.home_crest);
            away_crest = (ImageView) view.findViewById(R.id.away_crest);
            container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
            linearLayout = (LinearLayout) view.findViewById(R.id.score_linear_layout);
        }
    }

    @Override
    public RVScoresAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scores_list_item, viewGroup, false);
        view.setFocusable(true);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        final ViewHolder mHolder = holder;

        // We will set all the appropriate text and images for
        // each view, and finally set a content description string
        // for the whole view that summarizes the game details
        mHolder.home_name.setText(mCursor.getString(COL_HOME));
        ViewCompat.setImportantForAccessibility(
                mHolder.home_name,
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
        mHolder.away_name.setText(mCursor.getString(COL_AWAY));
        ViewCompat.setImportantForAccessibility(
                mHolder.away_name,
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);

        // Get the match time and adjust content description
        // to inform user the full date and time of the game
        mHolder.date.setText(mCursor.getString(COL_MATCHTIME));
        ViewCompat.setImportantForAccessibility(
                mHolder.date,
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);

        // Get the game scores and set the appropriate content descriptions
        mHolder.score.setText(Utility.getScores(mCursor.getInt(COL_HOME_GOALS), mCursor.getInt(COL_AWAY_GOALS)));
        ViewCompat.setImportantForAccessibility(
                mHolder.score,
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);

        mHolder.match_id = mCursor.getDouble(COL_ID);
        mHolder.home_crest.setImageResource(Utility.getTeamCrestByTeamName(
                mCursor.getString(COL_HOME)));
        mHolder.away_crest.setImageResource(Utility.getTeamCrestByTeamName(
                mCursor.getString(COL_AWAY)));

        // Set a content description for the whole view
        mHolder.linearLayout.setContentDescription(Utility.getGameDescription(
                mContext, mCursor.getString(COL_HOME), mCursor.getString(COL_AWAY),
                mCursor.getInt(COL_HOME_GOALS), mCursor.getInt(COL_AWAY_GOALS),
                mCursor.getString(COL_DATE), mCursor.getString(COL_MATCHTIME),
                mCursor.getInt(COL_LEAGUE), mCursor.getInt(COL_MATCHDAY)));

        mHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail_match_id = mHolder.match_id;
                MainActivity.selected_match_id = (int) mHolder.match_id;
                notifyDataSetChanged();
            }
        });

        //Log.v(FetchScoreTask.LOG_TAG,mHolder.home_name.getText() + " Vs. " + mHolder.away_name.getText() +" id " + String.valueOf(mHolder.match_id));
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(detail_match_id));
        LayoutInflater vi = (LayoutInflater) mContext.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        if(mHolder.match_id == detail_match_id)
        {
            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");

            mHolder.container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            ViewCompat.setImportantForAccessibility(
                    match_day,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
            match_day.setText(Utility.getMatchDay(mContext, mCursor.getInt(COL_MATCHDAY),
                    mCursor.getInt(COL_LEAGUE)));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            ViewCompat.setImportantForAccessibility(
                    league,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
            league.setText(Utility.getLeague(mContext, mCursor.getInt(COL_LEAGUE)));
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //add Share Action
                    mContext.startActivity(createShareForecastIntent(mHolder.home_name.getText() + " "
                            + mHolder.score.getText() + " " + mHolder.away_name.getText() + " "));
                }
            });
        }
        else
        {
            mHolder.container.removeAllViews();
        }

    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + mContext.getString(R.string.sharing_hashtag));
        return shareIntent;
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
//        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }
}
