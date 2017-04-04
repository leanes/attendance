package com.example.attendance.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 陈振聪 on 2017/2/8.
 */
public class UserDataManager {
    private static final String TAG = "UserDataManager";
    private static final String DB_NAME = "user_data.db";
    private static final String TABLE_NAME = "users";

    public static final String ID = "_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PWD = "user_password";
    public static final String SCHOOL_NAME = "school_name";
    public static final String PHONE_NUMBER = "phone_number";



    private static final int DB_VERSION = 2;
    private Context mContext = null;

    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key ,"
            +  USER_NAME + " text,"
            +  SCHOOL_NAME + " text,"
            +  PHONE_NUMBER + " varchar,"
            +  USER_PWD + " varchar"+")" ;

    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;

    private static class DataBaseManagementHelper extends SQLiteOpenHelper {

        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG,"db.getVersion()="+db.getVersion());
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            db.execSQL(DB_CREATE);
            Log.i(TAG, "db.execSQL(DB_CREATE)");
            Log.e(TAG, DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "DataBaseManagementHelper onUpgrade");
            onCreate(db);
        }
    }

    public UserDataManager(Context context) {
        mContext = context;
    }

    public void openDataBase() throws SQLException {

        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void closeDataBase() throws SQLException {

        mDatabaseHelper.close();
    }

    public long insertUserData(UserData userData) {

        String userName = userData.getUserName();
        String schoolName = userData.getSchoolName() ;
        String phoneNumber = userData.getPhoneNumber() ;
        String userPwd = userData.getUserPwd();


        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(SCHOOL_NAME , schoolName);
        values.put(PHONE_NUMBER , phoneNumber);
        values.put(USER_PWD, userPwd);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }

/*    public boolean updateUserData(UserData userData) {

        int id = userData.getUserId();
        String userName = userData.getUserName();
        String schoolName = userData.getSchoolName() ;
        String phoneNumber = userData.getPhoneNumber() ;
        String userPwd = userData.getUserPwd();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(SCHOOL_NAME , schoolName);
        values.put(PHONE_NUMBER , phoneNumber);
        values.put(USER_PWD, userPwd);
        return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }*/

    public Cursor fetchUserData(int id) throws SQLException {

        Cursor mCursor = mSQLiteDatabase.query(true, TABLE_NAME, null, ID
                + "=" + id, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

/*    public Cursor fetchAllUserDatas() {

        return mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null,
                null);
    }*/

  /*  public boolean deleteUserData(int id) {

        return mSQLiteDatabase.delete(TABLE_NAME, ID + "=" + id, null) > 0;
    }

    public boolean deleteAllUserDatas() {

        return mSQLiteDatabase.delete(TABLE_NAME, null, null) > 0;
    }


    public String getStringByColumnName(String columnName, int id) {
        Cursor mCursor = fetchUserData(id);
        int columnIndex = mCursor.getColumnIndex(columnName);
        String columnValue = mCursor.getString(columnIndex);
        mCursor.close();
        return columnValue;
    }


    public boolean updateUserDataById(String columnName, int id,
                                      String columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
        return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }*/

    public int findPhoneNumber(String phoneNumber){
        Log.i(TAG,"findPhoneNumber , phoneNumber =" + phoneNumber);
        int result=0;
        Cursor mCursor= mSQLiteDatabase.query(TABLE_NAME, null , "phone_number = " + phoneNumber , null , null , null , null);
        if(mCursor != null){
            result = mCursor.getCount();
            mCursor.close();
            Log.i(TAG,"findPhoneNumber , result =" + result);
        }
        return result;
    }

    public int findUserByPhoneAndPwd(String phoneNumber ,String pwd){
        Log.i(TAG,"findUserByPhoneAndPwd");
        int result=0;
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, "phone_number ="+ phoneNumber +  " and " + "user_password = "+ pwd ,
                null, null, null, null);
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
            Log.i(TAG,"findUserByPhoneAndPwd , result ="+ result);
        }
        return result;
    }

}

