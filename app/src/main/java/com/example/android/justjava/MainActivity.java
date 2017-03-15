package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.R.attr.name;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if (quantity != 100) {
            quantity = quantity + 1;
            displayQuantity(quantity);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.cant_order_100_cups,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (quantity != 1) {
            quantity = quantity - 1;
            displayQuantity(quantity);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.cant_order_less_than_one, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Find the user's name
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        // Figure out if the user wants whipped cream topping
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out if the user wants chocolate topping
        CheckBox choclateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = choclateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        emailOrder(createOrderSummary(price, hasWhippedCream, hasChocolate, name), name);
    }

    /**
     * Calculates the price of the order.
     *
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int coffeePrice = 5;
        int whippedCreamPrice = 1;
        int chocolatePrice = 2;
        int cupPrice = coffeePrice;

        if (addWhippedCream) { cupPrice += whippedCreamPrice; }
        if (addChocolate) { cupPrice += chocolatePrice; }

        return quantity * (cupPrice);
    }

    /**
     * Creates a summary of the entire order
     *
     * @param priceOfOrder the price of the entire order
     * @return message text summarising the order
     */
    private String createOrderSummary(int priceOfOrder, boolean addWhippedCream,
                                      boolean addChocolate, String customerName) {
        String message = getString(R.string.order_summary_name, customerName);
        message += "\n" + getString(R.string.add_whipped_cream) + addWhippedCream;
        message += "\n" + getString(R.string.add_chocolate) + addChocolate;
        message += "\n" + getString(R.string.quantity) + ": " + quantity;
        message += "\n"+ getString(R.string.total) + ": $" + priceOfOrder;
        message += "\n" + getString(R.string.thank_you);
        return message;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int displayQuantity) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + displayQuantity);
    }

    /**
     * This method sends an email for the order
     * @param message
     * @param name
     */
    private void emailOrder(String message, String name) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_line, name));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}