package com.example.manco.myapplication.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manco.myapplication.R;
import com.example.manco.myapplication.adapters.CustomListAdapter;
import com.example.manco.myapplication.controller.Controller;

import java.util.Calendar;

/**
 * Created by Marjan Sherdenkovski
 *
 * This class is controlling the summary fragment
 */
public class SummaryFragment extends Fragment {

    TextView tvTotalIncome, tvTotalExpense, tvResult;
    ListView listIncome, listExpense;
    final Calendar cal = Calendar.getInstance();
    TextView tvFromDate;
    TextView tvToDate;
    DatePicker fromDate;
    DatePicker toDate;
    Controller controller;
    Button btnUpdate;
    CustomListAdapter customListAdapter;
    LinearLayout linear;


    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        initializeComponents(view);
        return  view;
    }


    /**
     * This class is initialize the summary fragment components
     * @param
     */
    private void initializeComponents(View view) {
        tvTotalExpense = (TextView) view.findViewById(R.id.tvTotalExpense);
        tvTotalIncome = (TextView) view.findViewById(R.id.tvTotalIncome);
        tvResult = (TextView) view.findViewById(R.id.tvResult);
        listExpense = (ListView) view.findViewById(R.id.listExpense);
        listIncome = (ListView) view.findViewById(R.id.listIncome);
        tvFromDate = (TextView) view.findViewById(R.id.tvFromDate);
        tvToDate = (TextView) view.findViewById(R.id.tvToDate);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        linear = (LinearLayout)view.findViewById(R.id.pieChartFrame);
        fillExpenseList();
        fillIncomeList();
        setResultAndTotal();
        setPieChart();
        listExpense.setOnItemClickListener(new ExpenseItemClicked());
        listIncome.setOnItemClickListener(new IncomeItemClicked());
        tvFromDate.setOnClickListener(new tvFromDateClicked());
        tvToDate.setOnClickListener(new tvToDateClicked());
        btnUpdate.setOnClickListener(new btnUpdateClicked());
    }


    /**
     * This method is getting all expenses from database
     */
    public void fillExpenseList(){
        customListAdapter = new CustomListAdapter(getActivity(),
                R.layout.custom_list_row,
                controller.dbController.getAllExpense(),
                new String[]{"title","amount"},
                new int[]{R.id.customTvTitle, R.id.customTvAmount} );
        listExpense.setAdapter(customListAdapter);
    }


    /**
     * This method is getting all incomes from database
     */
    public void fillIncomeList(){
        customListAdapter = new CustomListAdapter(getActivity(),
                R.layout.custom_list_row,
                controller.dbController.getAllIncome(),
                new String[]{"title","amount"},
                new int[]{R.id.customTvTitle, R.id.customTvAmount} );
        listIncome.setAdapter(customListAdapter);
    }


    /**
     * This method is checking if there is a diagram and remove it
     * after that is creating a new diagram
     *
     */
    public void setPieChart(){
        linear.removeAllViewsInLayout();
        float[] pieChartArray = new float[]{};

        if(tvFromDate.getText().toString().equals("") || tvToDate.getText().toString().equals("")){
            float exp = Float.valueOf(controller.dbController.getTotExp());
            float inc = Float.valueOf(controller.dbController.getTotInc());
            float[] floatArray = new float[]{exp, inc};
            pieChartArray = calculateData(floatArray);
        }else{
            float exp = Float.valueOf(controller.dbController.getSpecExpense(tvFromDate.getText().toString(), tvToDate.getText().toString()));
            float inc = Float.valueOf(controller.dbController.getSpecIncome(tvFromDate.getText().toString(), tvToDate.getText().toString()));
            float[] floatArray = new float[]{exp, inc};
            pieChartArray = calculateData(floatArray);
        }
        linear.addView(new MyGraphview(getActivity(), pieChartArray));
    }


    /**
     * This method is setting the controller class in the fragment
     * @param
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }


    /**
     * This method is getting expenses from database between the date that the user has choose
     * @param
     * @param
     */
    public void setSpecExpenseList(String from , String to){
        customListAdapter = new CustomListAdapter(getActivity(),
                R.layout.custom_list_row,
                controller.dbController.getSpecList(from,to,"expensetable"),
                new String[]{"title","amount"},
                new int[]{R.id.customTvTitle, R.id.customTvAmount} );
        listExpense.setAdapter(customListAdapter);
        Toast.makeText(getActivity(),from + " , " + to, Toast.LENGTH_LONG).show();
    }


    /**
     * This method is getting incomes from database between the date that the user has choose
     * @param
     * @param
     */
    public void setSpecIncomeList(String from , String to){
        customListAdapter = new CustomListAdapter(getActivity(),
                R.layout.custom_list_row,
                controller.dbController.getSpecList(from,to,"incometable"),
                new String[]{"title","amount"},
                new int[]{R.id.customTvTitle, R.id.customTvAmount} );
        listIncome.setAdapter(customListAdapter);
        Toast.makeText(getActivity(),from + " , " + to, Toast.LENGTH_LONG).show();
    }


    /**
     * This method is updating the total result between the chosen dates
     * @param
     * @param
     */
    private void setSpecResultAndTotal(String from, String to) {
        tvTotalIncome.setText(controller.dbController.getSpecIncome(from, to));
        tvTotalExpense.setText(controller.dbController.getSpecExpense(from, to));
        tvResult.setText(controller.dbController.getSpecResult(from, to));
    }


    /**
     * This method is calculating the total result
     */
    public void setResultAndTotal(){
        tvTotalIncome.setText(controller.dbController.getTotInc());
        tvTotalExpense.setText(controller.dbController.getTotExp());
        tvResult.setText(controller.dbController.getResult());
    }

    /**
     * Button listeners
     */
    private class ExpenseItemClicked implements AdapterView.OnItemClickListener {

        String str;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Item info:");
            Cursor itemInfo = controller.dbController.getItemInfo("expensetable", id);

            if(itemInfo.moveToFirst()) {
                str = "Catagory: " + itemInfo.getString(1)
                        + "\n" + "Title: " + itemInfo.getString(2)
                        + "\n" + "Amount: " + itemInfo.getString(3)
                        + "\n" + "Date: " + itemInfo.getString(4);
            }
            builder.setMessage(str);

            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    controller.dbController.deleteExpenseItem(Long.toString(id));
                    fillExpenseList();
                    setPieChart();
                }
            });
            builder.create();
            builder.show();
        }
    }

    private class IncomeItemClicked implements AdapterView.OnItemClickListener {

        String str;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Item info:");
            Cursor itemInfo = controller.dbController.getItemInfo("incometable", id);

            if (itemInfo.moveToFirst()) {
                str = "Catagory: " + itemInfo.getString(1)
                        + "\n" + "Title: " + itemInfo.getString(2)
                        + "\n" + "Amount: " + itemInfo.getString(3)
                        + "\n" + "Date: " + itemInfo.getString(4);
            }
            builder.setMessage(str);

            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    controller.dbController.deleteIncomeItem(Long.toString(id));
                    fillIncomeList();
                    setPieChart();
                }
            });
            builder.create();
            builder.show();
        }
    }

    private class tvFromDateClicked implements View.OnClickListener {
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
            builder.setNegativeButton("Reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tvToDate.setText("");
                    tvFromDate.setText("");
                }
            });
            fromDate = (DatePicker)dateDialog.findViewById(R.id.datePicker);
            fromDate.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = controller.formatDate(String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth));
                    tvFromDate.setText(date);
                }
            });
            builder.create();
            builder.show();
        }
    }

    private class tvToDateClicked implements View.OnClickListener {
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
            builder.setNegativeButton("Reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tvToDate.setText("");
                    tvFromDate.setText("");
                }
            });
            toDate = (DatePicker)dateDialog.findViewById(R.id.datePicker);
            toDate.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = controller.formatDate(String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth));
                    tvToDate.setText(date);
                }
            });
            builder.create();
            builder.show();
        }
    }


    private class btnUpdateClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(tvFromDate.getText().toString() == "" || tvToDate.getText().toString() == ""){
                fillExpenseList();
                fillIncomeList();
                setResultAndTotal();
                setPieChart();
                linear.refreshDrawableState();
            }else{
                setSpecExpenseList(controller.formatDate(tvFromDate.getText().toString()), controller.formatDate(tvToDate.getText().toString()));
                setSpecIncomeList(controller.formatDate(tvFromDate.getText().toString()), controller.formatDate(tvToDate.getText().toString()));
                setSpecResultAndTotal(controller.formatDate(tvFromDate.getText().toString()), controller.formatDate(tvToDate.getText().toString()));
                setPieChart();
                linear.refreshDrawableState();
            }
        }
    }


    /**
     * This method is calculating the circle diagram
     * @param
     * @return
     */
    private float[] calculateData(float[] data) {
        // TODO Auto-generated method stub
        float total=0;
        for(int i=0;i<data.length;i++){
            total+=data[i];
        }
        for(int i=0;i<data.length;i++){
            data[i]=360*(data[i]/total);
        }
        return data;
    }


    /**
     * This method is creating the circle diagram
     */
    public class MyGraphview extends View {
        private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        private int[] COLORS={Color.BLUE, Color.GREEN, Color.GRAY, Color.CYAN, Color.RED};
        RectF rectf = new RectF(10, 10, 200, 200);
        float temp=0;
        public MyGraphview(Context context, float[] values) {

            super(context);
            value_degree=new float[values.length];
            for(int i=0;i<values.length;i++){
                value_degree[i]=values[i];
            }
        }


        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            temp = 0;
            for (int i = 0; i < value_degree.length; i++) {
                if (i == 0) {
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                } else {
                    temp += value_degree[i - 1];
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
            }
        }
    }
}