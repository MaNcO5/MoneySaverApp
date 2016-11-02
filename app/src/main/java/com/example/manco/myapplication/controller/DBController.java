package com.example.manco.myapplication.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Marjan Sherdenkovski
 *
 * This class is managing the database
 */
public class DBController extends SQLiteOpenHelper {
    private static final String DB_NAME = "BadassDatabase";
    private static final int DB_VERSION = 1;
    SQLiteDatabase database;


    /**
     * The constructor
     * @param ctx
     */
    public DBController(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
    }


    /**
     * This method is opening the database
     */
    public void open(){
        database = getWritableDatabase();
    }


    /**
     * This method is closing the database
     */
    public void close(){
        database.close();
    }


    /**
     * This method is adding incomes in db
     * @param category
     * @param title
     * @param amount
     */
    public void saveIncome(String category, String title, int amount, String date){
        ContentValues values = new ContentValues();
        values.put("category",category);
        values.put("title", title);
        values.put("amount", amount);
        values.put("date", date);
        database.insert("incometable", null, values);
    }


    /**
     * This method is adding expenses in db
     * @param category
     * @param title
     * @param amount
     */
    public void saveExpense(String category, String title, int amount, String date){
        ContentValues values = new ContentValues();
        values.put("category",category);
        values.put("title", title);
        values.put("amount", amount);
        values.put("date", date);
        database.insert("expensetable", null, values);
    }


    /**
     * This method is getting all incomes
     * @return
     */
    public Cursor getAllIncome(){
        Cursor cursor = database.rawQuery("SELECT oid as _id, title, amount, category, date from incometable", new String[]{

        });
        return cursor;
    }


    /**
     * This method is getting all expenses
     * @return
     */
    public Cursor getAllExpense(){
        Cursor cursor = database.rawQuery("SELECT oid as _id, title, amount, category, date from expensetable", new String[]{

        });
        return cursor;
    }


    /**
     * This method is deleting an expense item in db
     * @param id
     */
    public void deleteExpenseItem(String id){
        database.delete("expensetable","oid = " + id ,null);
    }


    /**
     * This method is deleting an income item in db
     * @param id
     */
    public void deleteIncomeItem(String id){
        database.delete("incometable","oid = " + id ,null);
    }


    /**
     * This method is creates database if not exists
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE table if not exists incometable ( oid INTEGER PRIMARY KEY, category VARCHAR(255), title VARCHAR(255), amount INTEGER, date DATE);");
        sqLiteDatabase.execSQL("CREATE table if not exists expensetable ( oid INTEGER PRIMARY KEY, category VARCHAR(255), title VARCHAR(255), amount INTEGER, date DATE);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }


    /**
     * This method is getting all info from the db tables
     * @param table
     * @param id
     * @return
     */
    public Cursor getItemInfo(String table, long id) {
        Cursor cursor = database.rawQuery("select * from " + table + " where oid = " + Long.toString(id), null);
        return cursor;
    }

    /**
     * This method is getting all info from database between two dates
     * @param from
     * @param to
     * @param table -
     * @return cursor
     */
    public Cursor getSpecList(String from, String to, String table) {
        Cursor cursor = database.rawQuery("SELECT oid as _id, title, amount, category from " + table + " where date between '" + from + "' and '" + to + "'", new String[]{

        });
        return cursor;
    }

    /**
     * This method is calculating the total incomes
     * @return cursor
     */
    public String getTotInc() {
        Cursor cursor = database.rawQuery("select sum(amount) from incometable", new String[]{});
        int summa = 0;
        String sum;
        if(cursor.moveToFirst()){
            summa = cursor.getInt(0);
        }
        sum = String.valueOf(summa);
        return sum;
    }

    /**
     * This method is calulating the total expenses
     * @return cursor
     */
    public String getTotExp() {
        Cursor cursor = database.rawQuery("select sum(amount) from expensetable", new String[]{});
        int summa = 0;
        String sum;
        if(cursor.moveToFirst()){
            summa = cursor.getInt(0);
        }
        sum = String.valueOf(summa);
        return sum;
    }

    /**
     * This method is calculating the sum of incomes and expenses
     * @return res
     */
    public String getResult() {
        String expenses = getTotExp();
        String incomes = getTotInc();
        int exp = Integer.valueOf(expenses);
        int inc = Integer.valueOf(incomes);
        int result = inc-exp;
        String res = String.valueOf(result);

        return res;
    }

    /**
     * This method is getting the sum of incomes between two dates
     * @param from
     * @param to
     * @return
     */
    public String getSpecIncome(String from, String to) {
        Cursor cursor = database.rawQuery("select sum(amount) from incometable " + "where date between '" + from + "' and '" + to + "'", new String[]{});
        int summa = 0;
        String sum;
        if(cursor.moveToFirst()){
            summa = cursor.getInt(0);
        }
        sum = String.valueOf(summa);
        return sum;
    }

    /**
     * This method is getting the sum of expenses between two dates
     * @param from
     * @param to
     * @return
     */
    public String getSpecExpense(String from, String to) {
        Cursor cursor = database.rawQuery("select sum(amount) from expensetable " + "where date between '" + from + "' and '" + to + "'", new String[]{});
        int summa = 0;
        String sum;
        if(cursor.moveToFirst()){
            summa = cursor.getInt(0);
        }
        sum = String.valueOf(summa);
        return sum;
    }

    /**
     * This method is calculating the sum of incomes and expenses between two dates
     * @param from
     * @param to
     * @return res
     */
    public String getSpecResult(String from, String to) {
        String expenses = getSpecExpense(from, to);
        String incomes = getSpecIncome(from, to);
        int exp = Integer.valueOf(expenses);
        int inc = Integer.valueOf(incomes);
        int result = inc-exp;
        String res = String.valueOf(result);

        return res;
    }
}