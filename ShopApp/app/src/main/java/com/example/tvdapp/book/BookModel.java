package com.example.tvdapp.book;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.book.model.BookViewEntity;
import com.example.tvdapp.confirmImportProduct.model.ImportProductResponse;
import com.example.tvdapp.orderMangager.model.OrderManagerResponse;
import com.example.tvdapp.utilities.Constant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookModel {
    private List<ImportProductResponse> importProductResponses = new ArrayList<>();
    private DatabaseReference mDatabase;
    private BoolModelEvent event;

    public void setEvent(BoolModelEvent event) {
        this.event = event;
    }

    public BookModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void fetchData() {
        ValueEventListener postListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                importProductResponses.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImportProductResponse response = snapshot.getValue(ImportProductResponse.class);
                    importProductResponses.add(response);
                }

                sortImportProductResponses();

                if (event != null) {
                    event.fetchDataSuccess(getBookViewEntities());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("import_product").addValueEventListener(postListener);
    }

    public void payment(String id) {
        mDatabase.child(String.format("import_product/%s/paymentStatus", id)).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (event != null) {
                    event.paymentSuccess();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sortImportProductResponses() {
        Comparator<ImportProductResponse> comp = (ImportProductResponse a, ImportProductResponse b) -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Constant.hh_mm_dd_MM_yyyy);
            LocalDateTime localDateTimeA = LocalDateTime.parse(a.time, dtf);
            LocalDateTime localDateTimeB = LocalDateTime.parse(b.time, dtf);
            return localDateTimeB.compareTo(localDateTimeA);
        };
        importProductResponses.sort(comp);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<BookViewEntity> getBookViewEntities() {
        return importProductResponses.stream()
                .map(item -> new BookViewEntity(
                        item.id,
                        item.supplierName,
                        item.time,
                        String.format("%,d", item.total),
                        item.paymentStatus
                )).collect(Collectors.toList());
    }

    interface BoolModelEvent {
        void fetchDataSuccess(List<BookViewEntity> bookViewEntityList);
        void paymentSuccess();
    }
}
