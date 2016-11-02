package com.example.manco.myapplication.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manco.myapplication.MainActivity;
import com.example.manco.myapplication.R;
import com.example.manco.myapplication.controller.Controller;

import java.util.Calendar;

/**
 * Created by Marjan Sherdnekovski
 *
 * This class is controlling the expense fragment
 */
public class ExpenseFragment extends Fragment {

    EditText etTitle, etAmount;
    Button btnSave;
    Spinner category;
    TextView btnDatePicker;
    Controller controller;
    final Calendar cal = Calendar.getInstance();
    DatePicker datePicker;


    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        initializeComponents(view);
        return view;
    }


    /**
     * This method is initialize the fragment components
     * @param
     */
    private void initializeComponents(View view) {
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etAmount = (EditText) view.findViewById(R.id.etAmount);
        btnSave = (Button) view.findViewById(R.id.btnSaveExpense);
        category = (Spinner) view.findViewById(R.id.spinnerCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_choises, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        btnDatePicker = (TextView) view.findViewById(R.id.btnExpenseDatePicker);
        btnSave.setOnClickListener(new btnSaveClicked());
        btnDatePicker.setOnClickListener(new btnDatePickerClicked());
    }

    /**
     * This method is setting the control class
     * @param controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * The button listeners
     */
    private class btnSaveClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(etTitle.getText().toString().equals(null) || etTitle.getText().toString().equals("") ||
                    etAmount.getText().toString().equals(null) || etAmount.getText().toString().equals("") ||
                    btnDatePicker.getText().toString().equals(null) || btnDatePicker.getText().toString().equals("")){

                Toast.makeText(getActivity(),"Fill all the fields please..", Toast.LENGTH_SHORT).show();
            }else{
                int amount = Integer.parseInt(etAmount.getText().toString());
                controller.saveExpenseToDB(etTitle.getText().toString(), amount, category.getSelectedItem().toString(), btnDatePicker.getText().toString());
                controller.sumFrag.fillExpenseList();
                controller.sumFrag.setPieChart();
                Intent myIntent = new Intent(ExpenseFragment.this.getActivity(), MainActivity.class);
                ExpenseFragment.this.startActivity(myIntent);
            }
        }
    }


    private class btnDatePickerClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater factory = LayoutInflater.from(getActivity());

            View dateDialog = factory.inflate(R.layout.fragment_my_date_dialog,null);
            builder.setView(dateDialog);
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            datePicker = (DatePicker)dateDialog.findViewById(R.id.datePicker);
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = controller.formatDate(String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth));
                    btnDatePicker.setText(date);
                }
            });
            builder.create();
            builder.show();
        }
    }
}
