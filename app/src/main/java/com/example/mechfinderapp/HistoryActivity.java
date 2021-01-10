package com.example.mechfinderapp;
import android.annotation.SuppressLint;

import com.example.mechfinderapp.historyRecyclerView.HistoryAdapter;
import com.example.mechfinderapp.historyRecyclerView.HistoryObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class HistoryActivity extends AppCompatActivity {
    private String customerOrDriver,userId;

    private RecyclerView mHistoryRecyclerView;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;
    private TextView mBalance;
    private Double Balance1 = 0.0;
    private Double Balance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarlayout_toolbar);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);
        mBalance = (TextView) findViewById(R.id.balance);
        mHistoryRecyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        mHistoryRecyclerView.setNestedScrollingEnabled(false);
        mHistoryRecyclerView.setHasFixedSize(true);

        mHistoryLayoutManager = new LinearLayoutManager(HistoryActivity.this);

        mHistoryRecyclerView.setLayoutManager(mHistoryLayoutManager);
        mHistoryAdapter = new HistoryAdapter(getDataSetHistory(),HistoryActivity.this);
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        customerOrDriver = getIntent().getExtras().getString("customerOrMechanic");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserHistoryIds();
        if(customerOrDriver.equals("Mechanic")){
            mBalance.setVisibility(View.VISIBLE);
        }
       // HistoryObject obj = new HistoryObject("1234");

    }

    private void getUserHistoryIds() {
        DatabaseReference userHistoryDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(customerOrDriver).child(userId).child("history");
        userHistoryDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot history : dataSnapshot.getChildren()){
                        FetchRideInformation(history.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchRideInformation(final String ridekey) {
        DatabaseReference historyDatabaseRef = FirebaseDatabase.getInstance().getReference().child("history").child(ridekey);
        historyDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String rideId = dataSnapshot.getKey();
                    Long timestamp = 0L;
                    String distance="";
                    Double ridePrice=0.0;

                    /*
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        if(child.getKey().equals("timestamp")){
                            timestamp = Long.valueOf(child.getValue().toString());
                        }
                    }
                    */
                    if(dataSnapshot.child("timestamp")!=null){
                        timestamp = Long.valueOf(dataSnapshot.child("timestamp").getValue().toString());
                    }
                    if(dataSnapshot.child("customerPaid")!=null && dataSnapshot.child("MechanicPaidOut").getValue() == null){
                        if(dataSnapshot.child("jarak").getValue()!=null){
                            distance=dataSnapshot.child("jarak").getValue().toString();
                            ridePrice = (Double.valueOf(distance)*0.4);
                            Balance += ridePrice;
                            Balance1=1000.00;
                            mBalance.setText("Balance : RS "+ String.valueOf(Balance1));
                        }
                    }
                    HistoryObject obj = new HistoryObject(rideId,getDate(timestamp));

                    resultHistory.add(obj);
                     mHistoryAdapter.notifyDataSetChanged();
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getDate(Long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm",cal).toString();

        return date;
    }

    private ArrayList resultHistory = new ArrayList<HistoryObject>();
    private ArrayList<HistoryObject> getDataSetHistory() {
        return resultHistory;
    }


}
