package com.lusa.jilowa.smsfilterapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.util.Arrays;

public class SMSReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    private static final String TAG = "SMSReceiver";

    // SmsManager class is responsible for all SMS related actions
    final SmsManager sms = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        final Classifier<String, String> bayes =
                new BayesClassifier<String, String>();



    /*
       * The classifier can learn from classifications that are handed over
       * to the learn methods. Imagin a tokenized text as follows. The tokens
       * are the text's features. The category of the text will either be
       * positive or negative.
       */
        final String[] positiveText = ("Bro, don't you think we should into our apps from the get go? " +
                "Came across some reading this night. Will call you tomorrow cards in yankee morning if you don't mind. " +
                "You still coming. Am still here. Registrations are fine," +
                " but some data under mailing list puts part of the school name under reacheable moved Apologies status " +
                "and it thus shifts the remaining, while for some the university u bt it wasnt reacheable moved already name shifts under status. " +
                "Could you kindly have a look at it as well Kk.heaven's gate of blessxs Lemme test Awesome." +
                " Thanks. Ok. Give me some 30 mins to upload and test fix. No wahala. " +
                " Well be expecting your call Hi boss, kindly let me call u back in like 30 how u dey." +
                " Any updates. Tried calling u but no pick up Hey. Can u confirm if ok now. Thanks Tnx sir. " +
                "Jst sent u d details. Did u get it. Pls keep me updated. Thanks bruv! Hi. 4 my silence, been." +
                " I will go thru d site thorougly 2mrw and get bak 2 u wid updates. Tnx. Got it. No p Happy new mth." +
                ".Dis Sept will be a month 2 rememba 4  gd things amen. " +
                "D mth of feb shall be d continuation of God's awesome implement some app analytics visit in ur life in dis new year." +
                " will be opened 2 u ." +
                " HNM I wish u dis year dat data to the left d Lord wil walk with u thruout dis year n l b ur pillar This comes to wish a" +
                " beautiful family a happy xmas and prosperous new year. Thanks for the love and care. My peace ve I given 2 u not like" +
                " any other. Go in dis might tied n conquer very down lately Nov.shallom. My mtn is dead, called  wld buzz u wen i get home. " +
                "com  a c# programmer told me to achieve that requires a php prog." +
                " the handshake of the payment platform which i will find out but if u know 1," +
                "dont mind.buttumline line is i want d site to be able to take both credit/debit i havent got ur feedback," +
                "no mail yet I have sent it bro. Check again My mtn is dead," +
                " called u bt it wasnt already wld buzz u wen i get home").split("\\s");
        bayes.learn("positive", Arrays.asList(positiveText));

        final String[] negativeText = ("your number? If yes, you just won 2 days of FREE subscription to football analysis service." +
                " Send now YES to 38255 FREE of charge!Easytunez Man U Vs Sunderland 4PM GMT! Get your Football Club Ring Back Tune for FREE." +
                " Text CLUB to 251 for a list of football club tunes or text 10 to 251 for Man U Easytunez Get your callers listening to Shoki by Orezi on Etisalat Easytunez." +
                " Text 82158 to 251 for Shoki or text NEW to 251 Y'ello, you have been successfully unsubscribed from  WEDDING PLANNING TIPS." +
                " To subscribe to WEDDING PLANNING TIPS again, send  WED to 33334 for a list of tunes. N50/tune Wednesday Sale!!! 4K Loafers,3K Clutch Bags," +
                "4K OfficeShoes,2K Polos,4K Bags & mor.2order visit www.taafoo.com /Plot cl2 Ikosi Rd,Oregun/call 01460999.Hurry Now! " +
                "Treat your callers to the latest songs on Airtel  Hellotunes! Dial 6060 (FREE for first 5 mins) to download a callertune now." +
                " Cost is for new users. LAST INVITATION FOR 070~! TODAY 23/08 'Nigeria Mega Jackpot' chose you to participate for CASH! ONLY A FEW DAYS LEFT! Send WIN to 35777 now! N100/SMS").split("\\s");
        bayes.learn("negative", Arrays.asList(negativeText));

        // Get the SMS message received
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    // This will create an SmsMessage object from the received pdu
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    // Get sender phone number
                    //Resolving the contact name from the contacts.
//                    Uri lookupUri = Uri.withAppendedPath(  ContactsContract.CommonDataKinds.Phone.CONTENT_URI, Uri.encode(i));
//                   Cursor c = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?"}, null, new String[]{id}, null);
//                    c.moveToFirst();
//
                    // Get sender phone number
                    String phoneNumber = sms.getDisplayOriginatingAddress();
//                    String sender = phoneNumber;
                    String sender = phoneNumber;
                    String message = sms.getDisplayMessageBody();



                        String formattedText = String.format(context.getResources().getString(R.string.sms_message), sender, message);

                    final String[] unknownText = formattedText.split("\\s");
                    String classfiedMessage =  bayes.classify(Arrays.asList(unknownText)).getCategory();
                    if (classfiedMessage=="positive"){
                        //Toast.makeText(context, formattedText, Toast.LENGTH_SHORT).show();
                        //this will update the UI with message
                        MainActivity inst = MainActivity.instance();
                        inst.updateList(formattedText);
                    }else{

                        //this will update the UI with message
                        SpamFilter inst = SpamFilter.instance();
                        inst.updateList(formattedText);
                    }



                }
            }
            abortBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}