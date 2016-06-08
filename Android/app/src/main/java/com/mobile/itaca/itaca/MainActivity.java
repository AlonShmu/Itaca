package com.mobile.itaca.itaca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;
    private int userCommit = 0;
    private int userSum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((EditText)findViewById(R.id.minuteseditText)).setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.minuteseditText)).getWindowToken(), 0);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        ((EditText)findViewById(R.id.minuteseditText)).setOnFocusChangeListener(new OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText)findViewById(R.id.minuteseditText)).setTextColor(Color.GRAY);
                    ((EditText)findViewById(R.id.minuteseditText)).setText("");
                }
            }
        });

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        if (preferences.getString("username", "").equals("") && preferences.getString("password", "").equals("")) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            String companyname = preferences.getString("companyname", "");
            if (!companyname.equals("")) {
                ImageView img= (ImageView) findViewById(R.id.companyimageView);
                Context context = img.getContext();
                int id = context.getResources().getIdentifier(companyname.toLowerCase(), "drawable", context.getPackageName());
                if (id > 0) {
                    img.setImageResource(id);
                }
            }
        }

        onFetchData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // set company image
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String companyname = preferences.getString("companyname", "");
        if (!companyname.equals("")) {
            ImageView img= (ImageView) findViewById(R.id.companyimageView);
            Context context = img.getContext();
            int id = context.getResources().getIdentifier(companyname.toLowerCase(), "drawable", context.getPackageName());
            if (id > 0) {
                img.setImageResource(id);
            } else {
                id = context.getResources().getIdentifier("itaca_logo_touching_lives", "drawable", context.getPackageName());
                if (id > 0) {
                    img.setImageResource(id);
                }
            }
        }
        onFetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Log.i("MainActivity", "onOptionsItemSelected - Logout Selected");
                logoutPressed();
                return true;
            case R.id.item2:
                Log.i("MainActivity", "onOptionsItemSelected - Refresh");
                onFetchData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutPressed() {
        //--REST Data
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", "");
        editor.putString("password", "");
        editor.putString("companyname", "");
        editor.putString("id", "");
        editor.commit();

        // reset charts
        userCommit = 0;
        userSum = 0;
        createPieChart(new ArrayList<Float>());
        createBarChart(new ArrayList<String>(), new ArrayList<Float>(), new ArrayList<Float>());

        ArrayList<String> teamsNames = new ArrayList<>();
        ArrayList<Float> teamsData = new ArrayList<>();
        ArrayList<Float> commits = new ArrayList<>();

        // present login screen
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onFetchData() {
        Log.i("MainActivity", "onFetchData");
        getUserActivityByWeek();
        getTeamActivityByWeek();
    }

    private void getUserActivityByWeek() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Log.i("MainActivity", "getUserActivityByWeek " + preferences.getString("id", ""));
        if (!preferences.getString("id", "").equals("")) {
            new UserActivityByWeek().execute("http://52.1.104.153/GetUserActionBuyWeek.php", preferences.getString("id", ""));
        }
    }

    private class UserActivityByWeek extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return postData(urls[0],urls[1]);
        }

        protected void onPostExecute(String data) {
            Log.i("MainActivity", "UserActivityByWeek-onPostExecute");
            ActivityByWeekData activityByWeekData = new Gson().fromJson(data, ActivityByWeekData.class);
            if (activityByWeekData.getSucess().equals("true")) {
                ArrayList<Float> values = new ArrayList<>();
                values.add(activityByWeekData.getWeekData().getSum());
                userSum = Math.round(activityByWeekData.getWeekData().getSum());
                userCommit = Math.round(activityByWeekData.getWeekData().getUserCommit());
                Float left = activityByWeekData.getWeekData().getUserCommit() - activityByWeekData.getWeekData().getSum();
                if (left > 0) {
                    values.add(left);
                }
                createPieChart(values);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PieChart pieChart = (PieChart) findViewById(R.id.chart);
                        pieChart.invalidate();
                    }
                });
                Log.i("MainActivity", "UserActivityByWeek-onPostExecute");
            } else {
                Log.i("MainActivity", "UserActivityByWeek-onPostExecute-Error");
            }
        }

        public String postData(String urlStr, String id) {
            try {
                URL url = new URL(urlStr);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "UserId=" + id;
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                Log.i("MainActivity", "UserActivityByWeek-postData code" + responseCode);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                Log.i("MainActivity", "UserActivityByWeek-postData output" + responseOutput.toString());
                return responseOutput.toString();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Communication Error ", Toast.LENGTH_LONG).show();
            }

            return null;
        }
    }

    private void getTeamActivityByWeek() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Log.i("MainActivity", "getTeamActivityByWeek " + preferences.getString("companyname", ""));
        if (!preferences.getString("companyname", "").equals("")) {
            new TeamActivityByWeek().execute("http://52.1.104.153/GetTeamsActionBuyWeek.php", preferences.getString("companyname", ""));
        }
    }

    private class TeamActivityByWeek extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return postData(urls[0],urls[1]);
        }

        protected void onPostExecute(String data) {
            Log.i("MainActivity", "TeamActivityByWeek-onPostExecute");
            TeamActivityByWeekData teamActivityByWeekData = new Gson().fromJson(data, TeamActivityByWeekData.class);
            if (teamActivityByWeekData.getSucess().equals("true")) {
                ArrayList<String> teamsNames = new ArrayList<>();
                ArrayList<Float> teamsData = new ArrayList<>();
                ArrayList<Float> commits = new ArrayList<>();

                teamsNames.add(teamActivityByWeekData.getWeekData0().getTeamname());
                teamsData.add(teamActivityByWeekData.getWeekData0().getSum());
                commits.add(teamActivityByWeekData.getWeekData0().getUserCommit());

                if (teamActivityByWeekData.getWeekData1() != null) {
                    teamsNames.add(teamActivityByWeekData.getWeekData1().getTeamname());
                    teamsData.add(teamActivityByWeekData.getWeekData1().getSum());
                    commits.add(teamActivityByWeekData.getWeekData1().getUserCommit());
                }

                if (teamActivityByWeekData.getWeekData2() != null) {
                    teamsNames.add(teamActivityByWeekData.getWeekData2().getTeamname());
                    teamsData.add(teamActivityByWeekData.getWeekData2().getSum());
                    commits.add(teamActivityByWeekData.getWeekData2().getUserCommit());
                }

                if (teamActivityByWeekData.getWeekData3() != null) {
                    teamsNames.add(teamActivityByWeekData.getWeekData3().getTeamname());
                    teamsData.add(teamActivityByWeekData.getWeekData3().getSum());
                    commits.add(teamActivityByWeekData.getWeekData3().getUserCommit());
                }

                if (teamActivityByWeekData.getWeekData4() != null) {
                    teamsNames.add(teamActivityByWeekData.getWeekData4().getTeamname());
                    teamsData.add(teamActivityByWeekData.getWeekData4().getSum());
                    commits.add(teamActivityByWeekData.getWeekData4().getUserCommit());
                }

                if (teamActivityByWeekData.getWeekData5() != null) {
                    teamsNames.add(teamActivityByWeekData.getWeekData5().getTeamname());
                    teamsData.add(teamActivityByWeekData.getWeekData5().getSum());
                    commits.add(teamActivityByWeekData.getWeekData5().getUserCommit());
                }

                if (teamActivityByWeekData.getWeekData6() != null) {
                    teamsNames.add(teamActivityByWeekData.getWeekData6().getTeamname());
                    teamsData.add(teamActivityByWeekData.getWeekData6().getSum());
                    commits.add(teamActivityByWeekData.getWeekData6().getUserCommit());
                }

                createBarChart(teamsNames, teamsData, commits);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BarChart barChar = (BarChart) findViewById(R.id.barchart);
                        barChar.invalidate();
                    }
                });
                Log.i("MainActivity", "UserActivityByWeek-onPostExecute");
            } else {
                Log.i("MainActivity", "UserActivityByWeek-onPostExecute-Error");
            }
        }

        public String postData(String urlStr, String companName) {
            try {
                URL url = new URL(urlStr);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "CompanName=" + companName;
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                Log.i("MainActivity", "TeamActivityByWeek-postData code" + responseCode);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                Log.i("MainActivity", "TeamActivityByWeek-postData output" + responseOutput.toString());
                return responseOutput.toString();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Communication Error ", Toast.LENGTH_LONG).show();
            }

            return null;
        }
    }

    private void updateUserActivity(String minutes) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Log.i("MainActivity", "updateActivity " + preferences.getString("id", ""));
        if (!preferences.getString("id", "").equals("")) {
            new UpdateActivity().execute("http://52.1.104.153/UpdateActivity.php", preferences.getString("id", ""), minutes);
        }
    }

    private class UpdateActivity extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return postData(urls[0], urls[1], urls[2]);
        }

        protected void onPostExecute(String data) {
            Log.i("MainActivity", "UpdateActivity-onPostExecute");
            ActivityByWeekData activityByWeekData = new Gson().fromJson(data, ActivityByWeekData.class);
            if (activityByWeekData.getSucess().equals("true")) {
                Toast.makeText(MainActivity.this, "Activity was updated successfully", Toast.LENGTH_LONG).show();
                ((EditText)findViewById(R.id.minuteseditText)).setText("");
                onFetchData();
            } else {
                Toast.makeText(MainActivity.this,  "Failed to update activity", Toast.LENGTH_LONG).show();
            }
        }

        public String postData(String urlStr, String id, String minutes) {
            try {
                URL url = new URL(urlStr);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "UserId=" + id + "&Minutes=" + minutes;;
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                Log.i("MainActivity", "UpdateActivity-postData code" + responseCode);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                Log.i("MainActivity", "UpdateActivity-postData output" + responseOutput.toString());
                return responseOutput.toString();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Communication Error ", Toast.LENGTH_LONG).show();
            }

            return null;
        }
    }

    private void createPieChart(ArrayList<Float> values) {
        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        pieChart.setDescription("");
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawSliceText(false);
        pieChart.setHighlightPerTapEnabled(false);

        // creating data values + labels
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new Entry(values.get(i), i));
            labels.add("");
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        PieData data = new PieData(labels, pieDataSet); // initialize Piedata
        pieChart.setData(data); // set data into chart

        // set pie colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.argb(255, 54, 168, 61));
        colors.add(Color.RED);
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(10);
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + ((int) value);
            }
        });

        pieChart.animateXY(10, 10);
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

        // check if the length is more then 6 chars
//        for (int i = 0; i < barDataPoints.size(); i++) {
//            if ((barDataPoints.get(i).length() > 6) && (barDataPoints.size() > 3)) {
//                barChart.getXAxis().setLabelRotationAngle(270.0f);
//                break;
//            }
//        }

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

    public void buttonOnClick(View v) {
        ((EditText) findViewById(R.id.minuteseditText)).clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.minuteseditText)).getWindowToken(), 0);
        String minutes = ((EditText)findViewById(R.id.minuteseditText)).getText().toString();
        if (minutes.length() > 0) {
            if (Integer.parseInt(minutes) > 120) {
                ((EditText) findViewById(R.id.minuteseditText)).setTextColor(Color.RED);
                ((EditText) findViewById(R.id.minuteseditText)).setText("up to 120 minutes");
                return;
            }

            String minutesReport = minutes;

            // we don't report more then (userCommit-userSum)
            if (((userCommit-userSum) < Integer.parseInt(minutes)) && (userCommit > 0)) {
                minutesReport = String.valueOf(userCommit-userSum);
                if (userCommit-userSum <= 0) {
                    ((EditText) findViewById(R.id.minuteseditText)).setText("");
                    return;
                }
            }

            updateUserActivity(minutesReport);
        }
    }

    public void smilyClicked(View v) {
        Log.i("MainActivity", "smilyClicked");
    }
}
