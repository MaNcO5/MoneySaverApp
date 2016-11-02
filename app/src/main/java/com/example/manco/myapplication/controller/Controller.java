package com.example.manco.myapplication.controller;

import android.content.Context;
import android.widget.Toast;

import com.example.manco.myapplication.fragments.ExpenseFragment;
import com.example.manco.myapplication.fragments.IncomeFragment;
import com.example.manco.myapplication.fragments.SummaryFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marjan Sherdenkovski 2016-10-15
 * This class contains the logic between the fragments
 */
public class Controller {

 //  AlertDialog alertDialog;
//    SharedPreferences prefs;
//    SharedPreferences.Editor editor;
//    EditText etName,  etAdress,  etPhone;
//    String savedName, savedAdress, savedPhone;
 public SummaryFragment sumFrag;
    ExpenseFragment expFrag;
    IncomeFragment incFrag;
    public DBController dbController;
    Context activity;

    /**
     * The constructor
     */
    public Controller(Context activity, SummaryFragment summaryFragment, ExpenseFragment expenseFragment, IncomeFragment incomeFragment, DBController dbController){
//        initializeComponents(activity);
        this.activity = activity;
        this.sumFrag = summaryFragment;
        this.expFrag = expenseFragment;
        this.incFrag = incomeFragment;
        this.dbController = dbController;

        dbController.open();

        sumFrag.setController(this);
        expFrag.setController(this);
        incFrag.setController(this);
    }


    /**
     *
     * This method is allowing the database to save the incomes
     * @param title
     * @param amount
     * @param category
     */
    public void saveIncomeToDB(String title, int amount, String category, String date){
        dbController.saveIncome(category,title,amount,date);
        Toast.makeText(activity, title + " was added", Toast.LENGTH_LONG).show();
    }


    /**
     * This method is allowing the database to save the expenses
     * @param title
     * @param amount
     * @param category
     */
    public void saveExpenseToDB(String title, int amount, String category, String date){
        dbController.saveExpense(category, title, amount,date);
        Toast.makeText(activity, title + " was added", Toast.LENGTH_LONG).show();
    }


    /**
     * This method initialize components and creates share preferences
     * @param activity
     */
//    public void initializeComponents(Context activity){
//        prefs = activity.getSharedPreferences("MyPrefs", 0);
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        LayoutInflater factory = LayoutInflater.from(activity);
//        View dialog = factory.inflate(R.layout.my_dialog, null);
//
//        etName = (EditText)dialog.findViewById(R.id.etName);
//        etAdress = (EditText)dialog.findViewById(R.id.etAdress);
//        etPhone = (EditText)dialog.findViewById(R.id.etPhone);
//
//        userDialog(builder,dialog, activity);
//    }


    /**
     * This method show a dialog if the user has not wrote info
     * @param builder
     * @param dialog
     * @param activity -
     */
//    public void userDialog(AlertDialog.Builder builder, View dialog, final Context activity){
//
//        builder.setView(dialog);
//        builder.setTitle("Personal Information");
//
//        prefs = activity.getSharedPreferences("MyPrefs", 0);
//        editor = prefs.edit();
//
//        savedName = prefs.getString("name","fail");
//        savedAdress = prefs.getString("Adress","fail");
//        savedPhone = prefs.getString("Phone","fail");
//
//        if(!savedName.contains("fail"))
//            etName.setText(savedName);
//        if(!savedAdress.contains("fail"))
//            etAdress.setText(savedAdress);
//        if(!savedPhone.contains("fail"))
//            etPhone.setText(savedPhone);
//
//
//        builder.setPositiveButton("Save",new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                editor.putString("name", etName.getText().toString());
//                editor.putString("Adress", etAdress.getText().toString());
//                editor.putString("Phone", etPhone.getText().toString());
//                editor.apply();
//                Toast.makeText(activity, "Save succeeded", Toast.LENGTH_LONG).show();
//            }
//        });
//        alertDialog = builder.create();
//        if(savedName.contains("fail")||
//                savedAdress.contains("fail")||
//                savedPhone.contains("fail")){
//            Toast.makeText(activity,"Fill in all the fields please..", Toast.LENGTH_SHORT).show();
//            alertDialog.show();
//        }
//    }


    /**
     * This method is formatting the date strings
     * @param date
     * @return
     */
    public String formatDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd");
        String finalDate = timeFormat.format(myDate);

        return finalDate;
    }
}