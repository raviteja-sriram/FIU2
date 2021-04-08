package com.repute.fiu2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView outputStr;
    TextView toConcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("FIU 2 Main Activity");

        outputStr = (TextView)findViewById(R.id.textView2);
        toConcat = (TextView)findViewById(R.id.editText);

        //outputStr.setText(printAllRunningActivities(getAllRunningActivities(getApplicationContext())));
        //outputStr.setText(getProcessInfo());
        outputStr.setText(getSharedUserId(getApplicationContext()));
    }

    public String getSharedUserId(Context context){
        try {
            return context.getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA).sharedUserId;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "NameNotFound";
        }
    }

    public ArrayList<ActivityInformation> getSDKResources(){
        ArrayList<ActivityInformation> activityInfos = new ArrayList<>();
        try {
            Context fiu1Context = this.createPackageContext("com.repute.mainappdummy", Context.CONTEXT_IGNORE_SECURITY);
            toConcat.setText(fiu1Context != null ? "not null" : "null");
            PackageInfo pi = fiu1Context.getPackageManager().getPackageInfo(fiu1Context.getPackageName(), PackageManager.GET_ACTIVITIES);
            ArrayList<ActivityInfo> activities = new ArrayList<>(Arrays.asList(pi.activities));
            for(ActivityInfo a : activities){
                ActivityInformation ai = new ActivityInformation(a.name, a.processName);
                activityInfos.add(ai);
            }
        } catch (PackageManager.NameNotFoundException e) {
            toConcat.setText(e.getMessage());
            e.printStackTrace();
        }
        return activityInfos;
    }

    private String getProcessInfo(){
        List<String> activePackages = new ArrayList<String>();

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> l =  am.getRunningAppProcesses();
        String result = "";
        for (ActivityManager.RunningAppProcessInfo processInfo : l) {
            if(processInfo.processName.contains("com.repute"))
                result += processInfo.pid + " " + processInfo.processName + "\n";
        }
        return result;
    }

    //Gets list of activities with process name - List<activityName, processName>
    public ArrayList<ActivityInformation> getAllRunningActivities(Context context) {
        ArrayList<ActivityInformation> activityInfos = new ArrayList<>();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
            ArrayList<ActivityInfo> activities = new ArrayList<>(Arrays.asList(pi.activities));
            for(ActivityInfo a : activities){
                ActivityInformation ai = new ActivityInformation(a.name, a.processName);
                activityInfos.add(ai);
            }
            return activityInfos;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Returns list of activity name and process name as string
    public String printAllRunningActivities(ArrayList<ActivityInformation> ai){
        String result = "";
        result += "Activity Name - Process Id \n\n";
        for(ActivityInformation a : ai){
            result += a.activityName + " - " + getProcessIdBasedOnName(a.processName) + "\n";
        }
        return result;
    }

    public String getProcessIdBasedOnName(String processName){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes =  am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
            if(processInfo.processName.equalsIgnoreCase(processName))
                return Integer.toString(processInfo.pid);
        }
        return "0";
    }
}
