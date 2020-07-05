package com.example.batteryalarmclock.templates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.batteryalarmclock.model.AlarmData;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private String TABLE_NAME = "ALARMDATA";
    private String KEY_ID = "ID";
    private String UNIQUE_ID = "UNIQUE_ID" ;
    private String ALARM_TYPE = "ALARM_TYPE";
    private String CURRENT_DATE_TIME = "CURRENT_DATE_TIME";
    private String TARGET_PERCENTAGE = "TARGET_PERCENTAGE";
    private String CURRENT_PERCENTAGE = "CURRENT_PERCENTAGE";
    private String SELECTED_HOURS = "SELECTED_HOURS";
    private String SELECTED_MINUTE = "SELECTED_MINUTE";
    private String UNPLUGG_DATE_TIME = "UNPLUGG_DATE_TIME";
    private String UNPLUGG_PERCENTAGE = "UNPLUGG_PERCENTAGE";
    private String ALARM_STATUS = "ALARM_STATUS";

    private Constant constant = Constant.getInstance();

    public DBHelper(@Nullable Context context) {
        super(context, "batteryalarm", null, 1);
        //TODO table name and DB name change when give to testing
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARM_TABLE = "CREATE TABLE "
                + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UNIQUE_ID + " INTEGER DEFAULT 0 ,"
                + CURRENT_DATE_TIME + " INTEGER ,"
                + TARGET_PERCENTAGE + " INTEGER ,"
                + CURRENT_PERCENTAGE + " INTEGER ,"
                + SELECTED_HOURS + " INTEGER DEFAULT 0 ,"
                + SELECTED_MINUTE + " INTEGER DEFAULT 0 ,"
                + UNPLUGG_DATE_TIME + " INTEGER DEFAULT 0 , "
                + UNPLUGG_PERCENTAGE + " INTEGER DEFAULT 0 ,"
                + ALARM_STATUS + " TEXT  ,"
                + ALARM_TYPE + " TEXT "
                +")";


        db.execSQL(CREATE_ALARM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteAllData(){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }

    public void addAlarm(AlarmData alarmData) {
        Log.e("DBHelper" , "addAlarm : " + alarmData.toString());
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(UNIQUE_ID,alarmData.getUnique_id());
        cv.put(CURRENT_DATE_TIME,alarmData.getCurrent_date_time());
        cv.put(TARGET_PERCENTAGE,alarmData.getTarget_percentage());
        cv.put(CURRENT_PERCENTAGE,alarmData.getCurrent_percentage());
        cv.put(SELECTED_HOURS,alarmData.getSelected_hour());
        cv.put(SELECTED_MINUTE,alarmData.getSelected_minute());
        cv.put(ALARM_STATUS ,  alarmData.getAlarm_states());
        cv.put(ALARM_TYPE ,  alarmData.getAlarm_type());

        /*cv.put(UNPLUGG_DATE_TIME,alarmData.getUnplagg_date_time());
        cv.put(UNPLUGG_PERCENTAGE,alarmData.getUnplugg_percentage());
        cv.put(ALARM_STATUS,alarmData.getAlarm_states());
        cv.put(ALARM_TYPE , alarmData.getAlarm_type());*/

        long rowInsrted =  db.insert(TABLE_NAME,null, cv);
        constant.lastID = alarmData.getUnique_id() ;

        Log.e("DBHelper","ID IN DATABASE ::" +  constant.lastID);

        db.close();
    }

    public void updateAlarmStatus(String val, int alarmID) {
        Log.e("DBHelper","updateAlarmStatus " + alarmID );
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ALARM_STATUS , val);

        // updating row
        db.update(TABLE_NAME, cv, UNIQUE_ID + " = ?",
                new String[]{String.valueOf(alarmID)});
    }

    public void updateLastUnpluggData(int alarmID , AlarmData alarmData , boolean status_add) {
        Log.e("DBHelper","updateLastUnpluggData " + alarmID + " DATA" + alarmData.getUnplagg_date_time()  +"  kxjf  " + status_add +  " fhsfk" + alarmData.getAlarm_states()  );
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if (status_add) {
            cv.put(ALARM_STATUS, alarmData.getAlarm_states());
        }
        cv.put(UNPLUGG_DATE_TIME  , alarmData.getUnplagg_date_time());
        cv.put(UNPLUGG_PERCENTAGE , alarmData.getUnplugg_percentage());

        // updating row
        db.update(TABLE_NAME, cv, UNIQUE_ID + " = ?",
                new String[]{String.valueOf(alarmID)});
    }

    public AlarmData getPerticularAlarmData(String id) {
        List<AlarmData> alarmDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        AlarmData alarmData = new AlarmData();
        Cursor cursor  = db.rawQuery(" SELECT * FROM  "+ TABLE_NAME +"  WHERE  " + UNIQUE_ID + " = ? ", new String[]{id});
        try {
            if (cursor.moveToFirst()) {
                do {


                    alarmData.setUnique_id(cursor.getInt(cursor.getColumnIndex(UNIQUE_ID)));
                    alarmData.setCurrent_date_time(cursor.getString(cursor.getColumnIndex(CURRENT_DATE_TIME)));
                    alarmData.setCurrent_percentage(cursor.getInt(cursor.getColumnIndex(CURRENT_PERCENTAGE)));
                    alarmData.setTarget_percentage(cursor.getInt(cursor.getColumnIndex(TARGET_PERCENTAGE)));
                    alarmData.setSelected_hour(cursor.getInt(cursor.getColumnIndex(SELECTED_HOURS)));
                    alarmData.setSelected_minute(cursor.getInt(cursor.getColumnIndex(SELECTED_MINUTE)));
                    alarmData.setAlarm_states(cursor.getString(cursor.getColumnIndex(ALARM_STATUS)));
                    alarmData.setAlarm_type(cursor.getString(cursor.getColumnIndex(ALARM_TYPE)));

                   // alarmDataList.add(alarmData);

                } while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return alarmData;
    }

    public String getPerticularAlarmType(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String alarm_type = null;
        Cursor cursor  = db.rawQuery(" SELECT * FROM  "+ TABLE_NAME +"  WHERE " + UNIQUE_ID + " = ? ", new String[]{id});
        try {
            if (cursor.moveToFirst()) {
                do {
                    alarm_type = cursor.getString(cursor.getColumnIndex(ALARM_TYPE));
                } while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return alarm_type;
    }

    public List<AlarmData> getAllAlarmData() {
        List<AlarmData> alarmDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor  = db.rawQuery(" SELECT * FROM  "+ TABLE_NAME , null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    AlarmData alarmData = new AlarmData();

                    alarmData.setUnique_id(cursor.getInt(cursor.getColumnIndex(UNIQUE_ID)));
                    alarmData.setCurrent_date_time(cursor.getString(cursor.getColumnIndex(CURRENT_DATE_TIME)));
                    alarmData.setCurrent_percentage(cursor.getInt(cursor.getColumnIndex(CURRENT_PERCENTAGE)));
                    alarmData.setTarget_percentage(cursor.getInt(cursor.getColumnIndex(TARGET_PERCENTAGE)));
                    alarmData.setSelected_hour(cursor.getInt(cursor.getColumnIndex(SELECTED_HOURS)));
                    alarmData.setSelected_minute(cursor.getInt(cursor.getColumnIndex(SELECTED_MINUTE)));
                    alarmData.setAlarm_states(cursor.getString(cursor.getColumnIndex(ALARM_STATUS)));
                    alarmData.setAlarm_type(cursor.getString(cursor.getColumnIndex(ALARM_TYPE)));
                    alarmData.setUnplugg_percentage(cursor.getInt(cursor.getColumnIndex(UNPLUGG_PERCENTAGE)));
                    alarmData.setUnplagg_date_time(cursor.getString(cursor.getColumnIndex(UNPLUGG_DATE_TIME)));

                    alarmDataList.add(alarmData);

                    Log.e("All : " , alarmData.toString() );

                } while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return alarmDataList;
    }

    /*public List<AlarmData> getAlarmList() {
        List<AlarmData> alarmDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME +" ORDER BY "  + KEY_HOUR + " ASC " +"," + KEY_MINUTE + " ASC " ;
        Cursor cursor = db.rawQuery(query,null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    AlarmData alarmData = new AlarmData(cursor.getInt(cursor.getColumnIndex(KEY_ID)));

                    alarmData.setAlarmType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
                    alarmData.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    alarmData.setHour(cursor.getInt(cursor.getColumnIndex(KEY_HOUR)));
                    alarmData.setMinute(cursor.getInt(cursor.getColumnIndex(KEY_MINUTE)));
                    alarmData.setDays(cursor.getString(cursor.getColumnIndex(KEY_DAYS)));
                    alarmData.setMissionName(cursor.getString(cursor.getColumnIndex(KEY_MISSION_NAME)));
                    alarmData.setMissionValue(cursor.getInt(cursor.getColumnIndex(KEY_MISSION_VALUE)));
                    alarmData.setSoundName(cursor.getString(cursor.getColumnIndex(KEY_SOUND)));
                    alarmData.setSoundPath(cursor.getInt(cursor.getColumnIndex(KEY_SOUND_PATH)));
                    alarmData.setVolumn(cursor.getInt(cursor.getColumnIndex(KEY_VOLUMN)));
                    alarmData.setIsVibrate(cursor.getInt(cursor.getColumnIndex(KEY_ISVIBRATE)));
                    alarmData.setLabel(cursor.getString(cursor.getColumnIndex(KEY_LABEL)));
                    alarmData.setCurrent_time(cursor.getString(cursor.getColumnIndex(CURRENT_TIME)));
                    alarmData.setAddtime(cursor.getInt(cursor.getColumnIndex(ADD_TIME)));
                    alarmData.setIsRandomMisison(cursor.getInt(cursor.getColumnIndex(RANDOMMISSION)));
                    alarmData.setIsEnable(cursor.getInt(cursor.getColumnIndex(KEY_ISENABLE)));
                    alarmData.setSnoozeDuration(cursor.getLong(cursor.getColumnIndex(SNOOZEDURATION)));
                    alarmData.setNextalarmtime(cursor.getLong(cursor.getColumnIndex(NEXTALARMTIME)));


                    final boolean mon = cursor.getInt(cursor.getColumnIndex(COL_MON)) == 1;
                    final boolean tues = cursor.getInt(cursor.getColumnIndex(COL_TUES)) == 1;
                    final boolean wed = cursor.getInt(cursor.getColumnIndex(COL_WED)) == 1;
                    final boolean thurs = cursor.getInt(cursor.getColumnIndex(COL_THURS)) == 1;
                    final boolean fri = cursor.getInt(cursor.getColumnIndex(COL_FRI)) == 1;
                    final boolean sat = cursor.getInt(cursor.getColumnIndex(COL_SAT)) == 1;
                    final boolean sun = cursor.getInt(cursor.getColumnIndex(COL_SUN)) == 1;

                    alarmData.setDayA(AlarmData.MON, mon);
                    alarmData.setDayA(AlarmData.TUES, tues);
                    alarmData.setDayA(AlarmData.WED, wed);
                    alarmData.setDayA(AlarmData.THURS, thurs);
                    alarmData.setDayA(AlarmData.FRI, fri);
                    alarmData.setDayA(AlarmData.SAT, sat);
                    alarmData.setDayA(AlarmData.SUN, sun);

                    alarmData.setTime(cursor.getLong(cursor.getColumnIndex(KEY_TIME)));

                    alarmDataList.add(alarmData);

                } while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return alarmDataList;
    }

    public int updateNote(AlarmData alarmData ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(KEY_TYPE,alarmData.getAlarmType()); // quick  -- 0   standard  -- 1
        cv.put(KEY_HOUR,alarmData.getHour()); //
        cv.put(KEY_MINUTE,alarmData.getMinute()); //
        cv.put(KEY_DAYS,alarmData.getDays()); //  00
        cv.put(KEY_MISSION_NAME,alarmData.getMissionName());  // quick -- None
        cv.put(KEY_MISSION_VALUE,alarmData.getMissionValue()); // quick  -- 0
        cv.put(KEY_SOUND,alarmData.getSoundName()); // quick  = ""
        cv.put(KEY_SOUND_PATH,alarmData.getSoundPath()); // quick  -- sound ID
        cv.put(KEY_VOLUMN,alarmData.getVolumn()); // yes -- 15 no  --0
        cv.put(KEY_ISVIBRATE,alarmData.getIsVibrate()); // on -- 1  off -- 0
        cv.put(KEY_LABEL,alarmData.getLabel()); // quick -- None
        cv.put(KEY_ISENABLE,alarmData.getIsEnable()); // quick  -- 1
        cv.put(CURRENT_TIME , alarmData.getCurrentTime());
        cv.put(ADD_TIME , alarmData.getAddedTime());
        cv.put(RANDOMMISSION , alarmData.getIsRandomMisison());
        cv.put(SNOOZEDURATION , alarmData.getSnoozeDuration());
        cv.put(NEXTALARMTIME , alarmData.getNextalarmtime());
        // cv.put(MAXTAPSNOOZEBTN , alarmData.getMaxtapsnooze());

        final SparseBooleanArray days = alarmData.getDaysA();
        cv.put(COL_MON, days.get(AlarmData.MON) ? 1 : 0);
        cv.put(COL_TUES, days.get(AlarmData.TUES) ? 1 : 0);
        cv.put(COL_WED, days.get(AlarmData.WED) ? 1 : 0);
        cv.put(COL_THURS, days.get(AlarmData.THURS) ? 1 : 0);
        cv.put(COL_FRI, days.get(AlarmData.FRI) ? 1 : 0);
        cv.put(COL_SAT, days.get(AlarmData.SAT) ? 1 : 0);
        cv.put(COL_SUN, days.get(AlarmData.SUN) ? 1 : 0);
        cv.put(KEY_TIME,alarmData.getTime());

        // updating row
        return db.update(TABLE_NAME, cv, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmData.getId())});
    }

    public void deleteNote(AlarmData  alarmData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmData.getId())});
        db.close();
    }

    public String[] getSoundName(String valueOf) {
        Log.e("ALARMSNOOEID" , valueOf + " sound id getSoundName");
        Cursor cursor = null;
        String[] empName = new String[15];

        SQLiteDatabase db= this.getReadableDatabase();
        try {
            cursor = db.rawQuery(" SELECT * FROM  "+ TABLE_NAME +"  WHERE ID = ? ", new String[]{valueOf});
            if (cursor.moveToFirst()){
                do {
                    empName[0] = (String.valueOf(cursor.getString(cursor.getColumnIndex(KEY_MISSION_NAME))));
                    empName[1] = (String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_SOUND_PATH))));
                    empName[2] = (String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_VOLUMN))));
                    empName[3] = (String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ISVIBRATE))));
                    empName[4] = (String.valueOf(cursor.getString(cursor.getColumnIndex(KEY_LABEL))));
                    empName[5] = (String.valueOf(cursor.getString(cursor.getColumnIndex(KEY_MISSION_VALUE))));

                    // Log.e("MISSIONVAL", "Mission Value is : " + String.valueOf(cursor.getString(cursor.getColumnIndex(KEY_MISSION_VALUE))));
                    //  empName[5] = (String.valueOf(cursor.getInt(cursor.getColumnIndex(MAXTAPSNOOZEBTN)))) ;
                    Log.e("Snooze" , empName[2] + " dd ");
                }while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            db.close();
        }
        return empName;
    }

    //Update Single Alarm Value From  List screen for enable or disable Alarm
    public void updateEnableDisable(int val, int alarmID) {
        Log.e("DATAAA12","updateEnableDisable alarmID : " +alarmID + " val : " + val );
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ISENABLE,val);

        // updating row
        db.update(TABLE_NAME, cv, KEY_ID + " = ?", new String[]{String.valueOf(alarmID)});
    }

    //Update Next Alarm time
    public void updatNextAlarmTime(long val , int alarmID) {
        Log.e("Snooze ","updatNextAlarmTime  DONE :  --  " + alarmID );
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NEXTALARMTIME , val);

        // updating row
        db.update(TABLE_NAME, cv, KEY_ID + " = ?", new String[]{String.valueOf(alarmID)});
    }

    public ArrayList<Long> getNextAlarmTime() {
        Cursor cursor  = null;
        ArrayList<Long> nextAlarm = new ArrayList<>();

        SQLiteDatabase db= this.getReadableDatabase();

        String query =" SELECT " + NEXTALARMTIME + " FROM  "+ TABLE_NAME + " WHERE  " + NEXTALARMTIME  + "  <>  0  " +  " ORDER BY "  + NEXTALARMTIME + " ASC ";
        try {
            cursor = db.rawQuery(query , null);
            if (cursor.moveToFirst()){
                do {
                    nextAlarm.add(cursor.getLong(cursor.getColumnIndex(NEXTALARMTIME)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            db.close();
        }


        return nextAlarm;
    }

    // Apply Query for Upate All standard  List Value Using Vacation mode
    public int updateAllStandardAlarm(int val, int alarmType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ISENABLE,val);
        cv.put(SNOOZEDURATION,0);
        // updating row
        return db.update(TABLE_NAME, cv, KEY_TYPE + " = ?",
                new String[]{String.valueOf(alarmType)});
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    // Apply Query for Update Snooze Duration
    public int updateSnoozeDuration(long val, int alarmID) {
        Log.e("Snooze ","updateSnoozeDuration  DONE :  --  " + alarmID );
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SNOOZEDURATION,val);

        // updating row
        return db.update(TABLE_NAME, cv, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmID)});
    }

    public int updateMaxTapSnooze(long val, int alarmID) {
        Log.e("Snooze ","updateMaxTapSnooze  DONE :  --  " + alarmID );
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // cv.put(MAXTAPSNOOZEBTN,val);

        // updating row
        return db.update(TABLE_NAME, cv, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmID)});
    }

    public  List<Integer>  getQuickID(){
        List<Integer> integerList =  new ArrayList<>();
        Cursor cursor = null;
        String[] empName = new String[15];

        SQLiteDatabase db= this.getReadableDatabase();
        try {
            cursor = db.rawQuery(" SELECT ID FROM  "+ TABLE_NAME +"  WHERE TYPE = ? ", new String[]{"0"});
            if (cursor.moveToFirst()){
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    integerList.add(id);

                    Log.e("ALARMSERVICECRASH","ID FROM DATABASE CALLED : " + integerList);
                }while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            db.close();
        }
        return integerList;
    }*/
}