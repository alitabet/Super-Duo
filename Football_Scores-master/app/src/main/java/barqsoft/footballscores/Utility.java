package barqsoft.footballscores;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utility
{
    /**
     * Helper method to get the name of a football league
     * given the league code from the API
     *
     * @param context Context to use for resource localization
     * @param leagueNum Unique API league code
     * @return The name of the league
     */
    public static String getLeague(Context context, int leagueNum)
    {
        // get the league codes and their corresponding
        // names from the app resources
        List<String> leagueCodes = new ArrayList<>(
                Arrays.asList(context.getResources().getStringArray(R.array.list_league_code_array)));

        List<String> leagueNames = new ArrayList<>(
                Arrays.asList(context.getResources().getStringArray(R.array.list_league_name_array)));

        // if the code exists in the database return corresponding name
        // else return unknown league string
        if (leagueCodes.contains(String.valueOf(leagueNum))) {
            int location = leagueCodes.indexOf(String.valueOf(leagueNum));
            return leagueNames.get(location);
        } else {
            return context.getString(R.string.unknown_league_text_response);
        }
    }

    /**
     * Helper methods to get the matchday string given the league code
     *
     * @param context Context to use for resource localization
     * @param matchDay Integer representing the matchday of the season
     * @param leagueNum Unique API league code
     * @return String formatted to display relevant matchday
     */
    public static String getMatchDay(Context context, int matchDay, int leagueNum)
    {
        // get the league name
        String leagueName = getLeague(context, leagueNum);

        // if we are looking at the Champions League then format the matchday
        // string based on the league stage (group stage, knockout, semifinal...)
        // otherwise just return Matchday: <em>matchDay<em>

        if(leagueName.equals(context.getString(R.string.league_name_champions_league)))
        {
            if (matchDay <= 6)
            {
                return context.getString(R.string.text_cl_group_stages)
                        + ", "
                        + String.format(context.getString(R.string.text_matchday), matchDay);//Matchday: 6";
            }
            else if(matchDay == 7 || matchDay == 8)
            {
                return context.getString(R.string.text_cl_first_knockout_round);
            }
            else if(matchDay == 9 || matchDay == 10)
            {
                return context.getString(R.string.text_cl_quarter_final);
            }
            else if(matchDay == 11 || matchDay == 12)
            {
                return context.getString(R.string.text_cl_semi_final);
            }
            else
            {
                return context.getString(R.string.text_cl_final);
            }
        }
        else
        {
            return String.format(context.getString(R.string.text_matchday), matchDay);
        }
    }

    /**
     * Helper method to format a string displaying the match results
     *
     * @param homeGoals Home team score
     * @param awayGoals Away team score
     * @return String displaying match score
     */
    public static String getScores(int homeGoals, int awayGoals)
    {
        // if the game is not finished yet just return blank results
        // else return the actual score

        if(homeGoals < 0 || awayGoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(homeGoals) + " - " + String.valueOf(awayGoals);
        }
    }

    /**
     * Helper method to retrieve the image resource for a given team
     *
     * @param teamName Name of team to retrieve image logo
     * @return Resource id from the team logo
     */
    public static int getTeamCrestByTeamName (String teamName)
    {
        if (teamName==null){return R.drawable.no_icon;}
        switch (teamName)
        {   //This is the set of icons that are currently in the app.
            // Feel free to find and add more as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            default: return R.drawable.no_icon;
        }
    }
}
