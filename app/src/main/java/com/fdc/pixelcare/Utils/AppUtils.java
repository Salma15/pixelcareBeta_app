package com.fdc.pixelcare.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewBold;

/**
 * Created by SALMA on 19/12/18.
 */

public class AppUtils {

    public static final String TAG = "PixC:";

    public interface OnAlertClickListener {
        void onButtonClick();
    }

    public static void showCustomAlertMessage(Context context, String title, String description, String positiveButtonText, String response_msg, final OnAlertClickListener listener) {

        if (!((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_networkalert);

            CustomTextView dialogButton = (CustomTextView) dialog.findViewById(R.id.custom_network_ok);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public static void showCustomSuccessMessage(Context context, String title, String description, String positiveButtonText, String response_msg, final OnAlertClickListener listener) {

        if (!((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_success);

          /*  CustomTextViewBold title_text = (CustomTextViewBold) dialog.findViewById(R.id.custom_success_title);
            title_text.setText(title);
*/
            CustomTextView message_text = (CustomTextView) dialog.findViewById(R.id.text_success_dialog);
            message_text.setText(description);

            Button dialogButton = (Button) dialog.findViewById(R.id.text_success_submit);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public static void showCustomFailureMessage(Context context, String title, String description, String positiveButtonText, String response_msg, final OnAlertClickListener listener) {

        if (!((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_failure);

            CustomTextViewBold title_text = (CustomTextViewBold) dialog.findViewById(R.id.custom_failure_title);
            title_text.setText(title);

            CustomTextView message_text = (CustomTextView) dialog.findViewById(R.id.custom_failure_msg);
            message_text.setText(description);

            CustomTextView dialogButton = (CustomTextView) dialog.findViewById(R.id.custom_failure_ok);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
