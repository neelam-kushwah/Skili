package com.securesurveillance.skili;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by adarsh on 3/23/2019.
 */

public class TermsAndConditionActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_terms);
        TextView tvTerms = (TextView) findViewById(R.id.tvTerms);
        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                setResult(RESULT_CANCELED, i);
                finish();
            }
        });
        tvTerms.setText("PURPOSE: \n" +
                "\n" +
                "Skili app(a product of Secure Surveillance) is only to serve as medium of contact and exchange of information for its users\n" +
                "who have a bona fide intention to contact and/or be contacted for the purposes related to genuine full time or part time job vacancies.\n" +
                "\n" +
                "\n" +
                "\n" +
                "USE TO BE IN CONFORMITY WITH THE PURPOSE: \n" +
                "\n" +
                "Skili app's subscription and use should meant for the bonafied Purpose and only the exclusive use of the subscriber/registered user.\n" +
                "\n" +
                "In the event you are found to be copying or misusing or transmitting or crawling any data or photographs or graphics or any information \n" +
                "available on Skili for any purpose other than that being a bonafide Purpose, we reserve the right to take such action that we deem fit \n" +
                "including stopping access and claiming damages.\n" +
                "\n" +
                "By using this app, you consent to the terms of our online Privacy Policy. \n" +
                "\n" +
                "The app is a public app with free access and Secure Surveillance assumes no liability for the quality and genuineness of information posted by users.\n" +
                "Secure Surveillance cannot monitor the data that a person may receive in response to information he/she has published on the site. \n" +
                "The individual/company would have to conduct its own background verification checks on the genuineness of all response/data. \n" +
                "\n" +
                "Secure Surveillance does not share personally identifiable data of any individual with other companies / entities without prior permission from the users.\n" +
                "Secure Surveillance shall share all such information that it has in its possession in response to legal process, such as a court order. \n" +
                "\n" +
                "The user shall not upload, post, transmit, publish, or distribute any material or information that is unlawful, \n" +
                "or which may potentially be perceived as being harmful, threatening, abusive, harassing, defamatory, libelous, vulgar, obscene, or\n" +
                "racially, ethnically, or otherwise objectionable. User must be held legal responsible for posting any such information. \n" +
                "Any such information will be removed from our platform as and when identified. However Secure Surveillance\n" +
                "can not control user from posting such information on the platform.  Secure Surveillance expressly disclaims any liability of the such data shared by user on the platform.\n" +
                "\n" +
                "The user expressly states that the information/ data being shared/added/inserted on into the Skili app by the user is correct and complete in \n" +
                "all respects and does not contain any false, distorted, manipulated, fraudulent or misleading facts.\n" +
                "\n" +
                "Note: The terms in this agreement may be changed by Secure Surveillance any time. Secure Surveillance is free to offer \n" +
                "its services to any client/prospective client without restriction.");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }
}
