package com.appsworld.xmlpullparserex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private List<Employee> employeeList;
    private CustomAdapter customAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Employee employee;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        employeeList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        GetXmlData();
    }

    private void GetXmlData() {
        try {
            InputStream istream = getAssets().open("employees.xml");
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(istream, null);
            String tag = "", text = "";
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("employee")) {
                            // create a new instance of employee
                            employee = new Employee();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = xmlPullParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("employee")) {
                            // add employee object to list
                            employeeList.add(employee);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            employee.setId(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("name")) {
                            employee.setName(text);
                        } else if (tagname.equalsIgnoreCase("salary")) {
                            employee.setSalary(Float.parseFloat(text));
                        }
                        break;

                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        customAdapter = new CustomAdapter(employeeList, this);
        recyclerView.setAdapter(customAdapter);
    }


}