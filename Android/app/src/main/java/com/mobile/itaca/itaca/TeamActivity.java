package com.mobile.itaca.itaca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class TeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String companyName = preferences.getString("companyname", "");
        if (!companyName.equals("")) {
            ImageView img= (ImageView) findViewById(R.id.companyimageView);
            Context context = img.getContext();
            int id = context.getResources().getIdentifier(companyName.toLowerCase(), "drawable", context.getPackageName());
            if (id > 0) {
                img.setImageResource(id);
            }
        }
        Intent intent = getIntent();
        String teamName = intent.getStringExtra("teamName");
        ((TextView) findViewById(R.id.teamName)).setText(teamName);
        getUsersFotTeam(teamName);
    }

    private void getUsersFotTeam(String teamName) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Log.i("TeamActivity", "getUsersFotTeam " + preferences.getString("companyname", "") + " " + teamName);
        if (!preferences.getString("companyname", "").equals("")) {
            new UsersForTeam().execute("http://52.1.104.153/GetUserfromTeam.php", preferences.getString("companyname", ""), teamName);
        }
    }

    private class UsersForTeam extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return postData(urls[0],urls[1],urls[2]);
        }

        protected void onPostExecute(String data) {
            Log.i("TeamActivity", "getUsersFotTeam-onPostExecute");
            TeamUsersData teamUsersData = new Gson().fromJson(data, TeamUsersData.class);
            if (teamUsersData.getSucess().equals("true")) {
                ArrayList<String> teamsNames = new ArrayList<>();
                ArrayList<Float> teamsData = new ArrayList<>();
                ArrayList<Float> commits = new ArrayList<>();

                teamUsersData.genetaeUsesData();
                for (int i = 0; i < teamUsersData.getUsersData().size(); i++) {
                    UserForTeamData userData = teamUsersData.getUsersData().get(i);
                    int index = userData.getUsername().lastIndexOf("@");
                    if (index > -1) {
                        teamsNames.add(userData.getUsername().substring(0, index));
                    } else {
                        teamsNames.add(userData.getUsername());
                    }
                    teamsData.add(userData.getSum());
                    commits.add(userData.getUserCommit());
                }

                createBarChart(teamsNames, teamsData, commits);
                TeamActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BarChart barChar = (BarChart) findViewById(R.id.barchart);
                        barChar.invalidate();
                    }
                });
                Log.i("TeamActivity", "getUsersFotTeam-onPostExecute");
            } else {
                Log.i("TeamActivity", "getUsersFotTeam-onPostExecute-Error");
            }
        }

        public String postData(String urlStr, String companName, String teamName) {
            try {
                URL url = new URL(urlStr);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "CompanName=" + companName + "&TeamName=" + teamName;
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                Log.i("TeamActivity", "getUsersFotTeam-postData code" + responseCode);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                Log.i("TeamActivity", "getUsersFotTeam-postData output" + responseOutput.toString());
                return responseOutput.toString();
            } catch (IOException e) {
                Toast.makeText(TeamActivity.this, "Communication Error ", Toast.LENGTH_LONG).show();
            }

            return null;
        }
    }

    private void createBarChart(ArrayList<String> barDataPoints, ArrayList<Float> barValues, ArrayList<Float> commits) {
        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        barChart.setDescription("");
        barChart.getLegend().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setGridColor(0);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setTextSize(11);
        barChart.getXAxis().setAxisLineColor(0);

        if (barDataPoints.size() > 5) {
            barChart.getXAxis().setTextSize(8);
        }

        if (barDataPoints.size() > 1) {
            barChart.getXAxis().setLabelRotationAngle(270.0f);
        }

        Calendar calendar = Calendar.getInstance();
        Integer day = calendar.get(Calendar.DAY_OF_WEEK);

        // creating data values
        ArrayList<BarEntry> barDataEntries = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<>();
        for (int i = 0; i < barValues.size(); i++) {
            barDataEntries.add(new BarEntry((barValues.get(i)/commits.get(i))*100, i));
            if (day <= 7 && day > 0) {
                Float commitPerDay = (commits.get(i) / 7);
                if (barValues.get(i) >= (commitPerDay * day)) {
                    barColors.add(Color.argb(255, 54, 168, 61));
                } else {
                    barColors.add(Color.RED);
                }
            } else {
                barColors.add(Color.argb(255, 54, 168, 61));
            }
        }

        BarDataSet dataset = new BarDataSet(barDataEntries, "");
        BarData data = new BarData(barDataPoints, dataset); // initialize Piedata
        barChart.setData(data); // set data into chart

        // set colors
        dataset.setColors(barColors);
        dataset.setValueTextSize(10);
        dataset.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value + " %";
            }
        });

        barChart.animateY(20);
    }
}
