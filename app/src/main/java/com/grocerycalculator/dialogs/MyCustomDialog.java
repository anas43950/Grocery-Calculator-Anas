package com.grocerycalculator.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import androidx.annotation.NonNull;

import com.grocerycalculator.R;
import com.grocerycalculator.databinding.MyCustomDialogBinding;
import com.grocerycalculator.enums.DialogTask;
import com.grocerycalculator.utils.Constants;

public class MyCustomDialog extends Dialog {
    OnPositiveClickedListener onPositiveClickedListener;
    OnNegativeClickedListener onNegativeClickedListener;
    DialogTask dialogTask;
    MyCustomDialogBinding binding;

    public MyCustomDialog(@NonNull Context context, OnPositiveClickedListener onPositiveClickedListener, DialogTask dialogTask) {
        super(context);
        this.onPositiveClickedListener = onPositiveClickedListener;
        this.dialogTask = dialogTask;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MyCustomDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCancelable(false);

        InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 45);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(inset);

        adjustDialogViews();
        setListeners();
    }

    public interface OnPositiveClickedListener {
        void onPositiveClicked(String input, DialogTask dialogTask);
    }

    public interface OnNegativeClickedListener {
        void onNegativeClicked();
    }

    public void setOnNegativeClickedListener(OnNegativeClickedListener onNegativeClickedListener) {
        this.onNegativeClickedListener = onNegativeClickedListener;
    }

    private void adjustDialogViews() {
        switch (dialogTask) {
            case TAX:
                if (Constants.currentStore.getTax() != 0) {
                    setText(Constants.currentStore.getTax() + "");
                }
                binding.inputEt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_percentage, 0);
                changeEtInputTypeToNumberDecimal();
                break;
            case BUDGET:
                if (Constants.currentStore.getBudget() != 0) {
                    setText(Constants.currentStore.getBudget() + "");
                }
                binding.currencySymbolTv.setText(Constants.currentStore.getCurrencySymbol());
                binding.currencySymbolTv.setVisibility(View.VISIBLE);
                changeEtInputTypeToNumberDecimal();
                break;
            case DISCOUNT:
                if (Constants.currentStore.getDiscount() != 0) {
                    setText(Constants.currentStore.getDiscount() + "");
                }
                binding.inputEt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_subtract, 0, R.drawable.ic_percentage, 0);
                changeEtInputTypeToNumberDecimal();
                break;
            case ADD_STORE:
                binding.inputEt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                setCancelable(true);
                binding.cancelBtn.setText("Skip");
                break;
            case ITEM_QUANTITY:
                setText(Constants.currentItem.getQuantity() + "");
                binding.inputEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case EDIT_STORE_NAME:
                setText(Constants.currentStore.getName());
                break;
            case ITEM_NAME:
                setText(Constants.currentItem.getName());
                break;
            case ITEM_PRICE:
                setText(Constants.currentItem.getPrice() + "");
                break;
            default:
                break;
        }
        binding.titleTv.setText(dialogTask.getTitle());

    }

    private void setListeners() {
        binding.cancelBtn.setOnClickListener(view -> {
            if (onNegativeClickedListener != null) {
                onNegativeClickedListener.onNegativeClicked();
            }
            dismiss();
        });

        binding.doneBtn.setOnClickListener(view -> {
            String input = binding.inputEt.getText().toString().trim();
            if (input.equals("")) {
                binding.inputEt.setError("This field can't be empty");
                return;
            }
            onPositiveClickedListener.onPositiveClicked(input, dialogTask);
            dismiss();
        });
    }


    private void changeEtInputTypeToNumberDecimal() {
        InputFilter decimalDigitsFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                String currentText = dest.toString();
                int dotPosition = currentText.indexOf(".");
                if (dotPosition >= 0 && dend - dotPosition > 2) {
                    // If the user is trying to enter more than 2 digits after the decimal point, reject the input
                    return "";
                }
                return null;
            }
        };
        binding.inputEt.setFilters(new InputFilter[]{decimalDigitsFilter});
        binding.inputEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    private void setText(String text) {
        binding.inputEt.setText(text);
    }
}


