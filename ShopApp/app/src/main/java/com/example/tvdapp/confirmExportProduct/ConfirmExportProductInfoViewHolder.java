package com.example.tvdapp.confirmExportProduct;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.ConfirmOrderViewHolder;
import com.example.tvdapp.users.RegisterAdapter;
import com.example.tvdapp.users.SelectionItem;

import java.util.Arrays;

public class ConfirmExportProductInfoViewHolder extends ConfirmOrderViewHolder implements TextWatcher {
    private View addItemsView;
    private Spinner spinner;
    private EditText noteEditText;
    private Context context;
    private ConfirmExportProductInfoViewHolderEvent event;
    private SelectionItem exportTypes[];


    public ConfirmExportProductInfoViewHolder(@NonNull View itemView, Context context, ConfirmExportProductInfoViewHolderEvent event) {
        super(itemView);
        this.context = context;
        this.event = event;

        addItemsView = itemView.findViewById(R.id.confirm_export_product_add_items_view);
        spinner = itemView.findViewById(R.id.confirm_export_product_spinner);
        noteEditText = itemView.findViewById(R.id.confirm_export_product_note_edit_text);

        exportTypes = new SelectionItem[]{
                new SelectionItem(ExportType.export.getId(), context.getString(ExportType.export.getStringId())),
                new SelectionItem(ExportType.broken.getId(), context.getString(ExportType.broken.getStringId()))
        };

        RegisterAdapter adapter = new RegisterAdapter(context, exportTypes);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ExportType type = getExportType((int) l);
                if (event != null) {
                    event.selectExportType(type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addItemsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event != null) {
                    event.selectAddItems();
                }
            }
        });

        noteEditText.addTextChangedListener(this);
    }

    public void bindData(ConfirmExportProductInfoViewEntity entity) {
        noteEditText.removeTextChangedListener(this);
        noteEditText.setText(entity.note);
        noteEditText.addTextChangedListener(this);

        spinner.setSelection(getSelectionExportIndex(entity.exportId));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ExportType getExportType(int id) {
        return Arrays.stream(ExportType.values())
                .filter(type -> type.getId() == id)
                .findAny()
                .orElse(null);
    }

    private int getSelectionExportIndex(int exportId) {
        for (int i = 0; i < exportTypes.length; i++) {
            if (exportTypes[i].id == exportId) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (event != null) {
            event.noteDidChange(editable.toString());
        }
    }

    interface ConfirmExportProductInfoViewHolderEvent {
        void selectAddItems();
        void selectExportType(ExportType exportType);
        void noteDidChange(String note);
    }
}
