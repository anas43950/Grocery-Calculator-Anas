package com.grocerycalculator.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.grocerycalculator.R;
import com.grocerycalculator.databinding.AddItemDialogBinding;
import com.grocerycalculator.databinding.NativeAdLayoutBinding;
import com.grocerycalculator.models.Item;
import com.grocerycalculator.utils.AdUtils;
import com.grocerycalculator.utils.Constants;

public class AddItemDialog extends BottomSheetDialog implements OnClickListener {
    AddItemDialogBinding binding;
    OnAddClickedListener listener;
    Context context;

    public AddItemDialog(@NonNull Context context, OnAddClickedListener listener) {
        super(context);
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddItemDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCommonListeners();
        if (Constants.USE_SYSTEM_KEYBOARD) {
            addTextChangedListener();
            binding.keyboardLayout.setVisibility(View.GONE);
            binding.priceEt.setOnEditorActionListener((v, actionId, event) -> {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    addItem();
                    return true;
                }
                return false;
            });
        } else {
            binding.priceEt.requestFocus();
            disableSystemKeyboard();
            setKeyboardListeners();
        }

        AdUtils.loadNativeAd(context, binding.nativeAd);
    }


    private void disableSystemKeyboard() {
        binding.priceEt.setShowSoftInputOnFocus(false);
        binding.quantityEt.setShowSoftInputOnFocus(false);
    }

    private void setCommonListeners() {
        binding.doneBtn.setOnClickListener(view -> dismiss());
        binding.increaseQuantityBtn.setOnClickListener(view -> {
            if (binding.quantityEt.getText().toString().trim().equals("")) {
                binding.quantityEt.setText("1");
            } else {
                int quantity = Integer.parseInt(binding.quantityEt.getText().toString().trim());
                binding.quantityEt.setText(String.valueOf(quantity + 1));
            }
        });
        binding.decreaseQuantityBtn.setOnClickListener(view -> {
            if (!binding.quantityEt.getText().toString().trim().equals("1")) {
                int quantity = Integer.parseInt(binding.quantityEt.getText().toString().trim());
                binding.quantityEt.setText(String.valueOf(quantity - 1));
            }
        });
    }

    private void setKeyboardListeners() {
        binding.quantityEt.setOnFocusChangeListener((view, b) -> binding.buttonDot.setClickable(!b));
        binding.buttonOne.setOnClickListener(this);
        binding.buttonTwo.setOnClickListener(this);
        binding.buttonThree.setOnClickListener(this);
        binding.buttonFour.setOnClickListener(this);
        binding.buttonFive.setOnClickListener(this);
        binding.buttonSix.setOnClickListener(this);
        binding.buttonSeven.setOnClickListener(this);
        binding.buttonEight.setOnClickListener(this);
        binding.buttonNine.setOnClickListener(this);
        binding.buttonZero.setOnClickListener(this);
        binding.buttonDot.setOnClickListener(this);
        binding.backspaceBtn.setOnClickListener(this);
        binding.clearBtn.setOnClickListener(this);
        binding.addBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        String input = "";
        if (binding.priceEt.hasFocus()) {
            if (binding.priceEt.getText().toString().contains(Constants.currentStore.getCurrencySymbol())) {
                input = binding.priceEt.getText().toString().replace(Constants.currentStore.getCurrencySymbol(), "");
            }
        } else input = binding.quantityEt.getText().toString();
        if (id != R.id.add_btn && id != R.id.clear_btn && id != R.id.backspace_btn) {
            String btnText = ((MaterialButton) v).getText().toString();
            if (btnText.equals(".") && (input.contains(".") || input.equals(""))) {  // Doing this so we don't end up entering more than one decimal
                return;
            }
            input = input + btnText;
        }

        if (id == R.id.backspace_btn) {
            if (!input.equals("")) {
                input = input.substring(0, input.length() - 1);
            }
        }
        if (binding.priceEt.hasFocus()) {
            if (input.equals("")) {
                binding.priceEt.setText(input);
            } else {
                binding.priceEt.setText(Constants.currentStore.getCurrencySymbol() + input);
                binding.priceEt.setSelection(binding.priceEt.getText().toString().length());
            }
        } else {
            binding.quantityEt.setText(input);
            binding.quantityEt.setSelection(binding.quantityEt.getText().toString().length());
        }
        if (id == R.id.clear_btn) {
            clearInputFields();
        }
        if (id == R.id.add_btn) {
            addItem();
        }
    }

    private void clearInputFields() {
        binding.priceEt.setText("");
        binding.quantityEt.setText("1");
    }

    private boolean isValidData() {
        if (binding.quantityEt.getText().toString().trim().equals("")) {
            binding.quantityEt.setError("Enter quantity");
            return false;
        }
        if (binding.priceEt.getText().toString().trim().equals("")) {
            binding.priceEt.setError("Enter price");
            return false;
        }
        return true;
    }

    private void addTextChangedListener() {
        binding.priceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.priceEt.removeTextChangedListener(this);
                String text = editable.toString();
                String currencySymbol = Constants.currentStore.getCurrencySymbol();
                // Add currency symbol if not present
                if (!text.startsWith(currencySymbol)) {
                    text = currencySymbol + text;
                    binding.priceEt.setText(text);
                }
                // If all digits are removed, remove currency symbol too
                if (text.equals(currencySymbol)) {
                    binding.priceEt.setText("");
                }
                binding.priceEt.setSelection(binding.priceEt.getText().toString().length());
                binding.priceEt.addTextChangedListener(this);
            }
        });


    }

    public interface OnAddClickedListener {
        void onItemAdded(Item item);
    }

    private void addItem() {
        if (isValidData()) {
            int quantity = Integer.parseInt(binding.quantityEt.getText().toString().trim());
            float price = Float.parseFloat(binding.priceEt.getText().toString().trim().replace(Constants.currentStore.getCurrencySymbol(), ""));
            Item item = new Item("", price, quantity, Constants.currentStore.getId());
            listener.onItemAdded(item);
            clearInputFields();
        }
    }
}
