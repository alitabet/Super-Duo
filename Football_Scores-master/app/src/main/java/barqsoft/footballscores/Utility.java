package barqsoft.footballscores;

import android.content.Context;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
                        + String.format(Locale.getDefault(), context.getString(R.string.text_matchday), matchDay);
//                        + String.format(context.getString(R.string.text_matchday), matchDay);//Matchday: 6";
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
            return String.format(Locale.getDefault(), context.getString(R.string.text_matchday), matchDay);
            //String.format(context.getString(R.string.text_matchday), matchDay);
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
            return "";
        }

        return String.format(Locale.getDefault(), "%d", homeGoals) + " - " + String.format(Locale.getDefault(), "%d", awayGoals);
    }

    /**
     * Helper method to format a string containing the content
     * description for the game score
     *
     * @param context Context to use for resource localization
     * @param homeGoals Home team score
     * @param awayGoals Away team score
     * @return String containing the appropriate content description
     */
    public static String getScoresContentDescription(Context context, int homeGoals, int awayGoals)
    {
        // if the game is not finished yet inform the user
        // that nos core is available

        if(homeGoals < 0 || awayGoals < 0)
        {
            return context.getString(R.string.no_score_description);
        }

        return String.format(Locale.getDefault(), context.getString(R.string.match_score_description), homeGoals, awayGoals);
    }

    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        }
        else if ( julianDay == currentJulianDay -1)
        {
            return context.getString(R.string.yesterday);
        }
        else
        {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
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
            // Premier League
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
            // Budnesliga
            case "FC Bayern München" : return R.drawable.bayern_munchen;
            case "Hamburger SV" : return R.drawable.hamburger;
            case "FC Augsburg" : return R.drawable.augsburg;
            case "Hertha BSC" : return R.drawable.hertha_berlin;
            case "Bayer Leverkusen" : return R.drawable.bayer_leverkusen;
            case "TSG 1899 Hoffenheim" : return R.drawable.hoffenheim;
            case "SV Darmstadt 98" : return R.drawable.darmstadt_98;
            case "Hannover 96" : return R.drawable.hannover_96;
            case "1. FSV Mainz 05" : return R.drawable.mainz_05;
            case "FC Ingolstadt 04" : return R.drawable.ingolstadt_04;
            case "Werder Bremen" : return R.drawable.werder_bremen;
            case "FC Schalke 04" : return R.drawable.schalke_04;
            case "Borussia Dortmund" : return R.drawable.borussia_dortmund;
            case "Bor. Mönchengladbach" : return R.drawable.borussia_munchengladbach;
            case "VfL Wolfsburg" : return R.drawable.wolfsburg;
            case "Eintracht Frankfurt" : return R.drawable.eintracht_frankfurt;
            case "VfB Stuttgart" : return R.drawable.vfb_stuttgart;
            case "1. FC Köln" : return R.drawable.fc_koln;
            case "MSV Duisburg" : return R.drawable.msv_duisburg;
            case "1. FC Kaiserslautern" : return R.drawable.fc_kaiserslautern;
            case "Eintracht Braunschweig" : return R.drawable.eintracht_braunschweig;
            case "SV Sandhausen" : return R.drawable.sv_sandhausen;
            case "SC Freiburg" : return R.drawable.sc_freiburg;
            case "1. FC Nürnberg" : return R.drawable.fc_nuremberg;
            case "FSV Frankfurt" : return R.drawable.fsv_frankfurt;
            case "Red Bull Leipzig" : return R.drawable.rasenballsport_leipzig;
            case "SpVgg Greuther Fürth" : return R.drawable.spvgg_greuther_forth;
            case "Karlsruher SC" : return R.drawable.karlsruher;
            case "1. FC Heidenheim 1846" : return R.drawable.fc_heidenheim_1846;
            case "TSV 1860 München" : return R.drawable.tsv_1860_munich;
            case "SC Paderborn 07" : return R.drawable.sc_paderborn;
            case "VfL Bochum" : return R.drawable.vfl_bochum;
            case "Arminia Bielefeld" : return R.drawable.arminia_bielefeld;
            case "1. FC Union Berlin" : return R.drawable.fc_union_berlin;
            case "Fortuna Düsseldorf" : return R.drawable.fortuna_dusseldorf;
            case "FC St. Pauli" : return R.drawable.fc_st_pauli;
            // La Ligua
            case "FC Barcelona" : return R.drawable.barcelona;
            case "Club Atlético de Madrid" : return R.drawable.atletico_madrid;
            case "Real Madrid CF" : return R.drawable.real_madrid;
            case "Valencia CF" : return R.drawable.valencia;
            default: return R.drawable.no_icon;
        }
    }
}
