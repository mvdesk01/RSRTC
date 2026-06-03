package com.mtech.rsrtcsc.ui.activity.billdesk;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.billdesk.sdk.LibraryPaymentStatusProtocol;


public class BilldeskCallBack implements LibraryPaymentStatusProtocol, Parcelable
{
	String TAG = "Callback ::: > ";

	public BilldeskCallBack() {
		Log.v(TAG, "CallBack()....");
	}

	public BilldeskCallBack(Parcel in) {
		Log.v(TAG, "CallBack(Parcel in)....");
	}

	@Override
	public void paymentStatus(String status, Activity context) {
		Intent intent = new Intent(context, PaymentStatusActivity.class);
		intent.putExtra("status", status);
		context.startActivity(intent);
		context.finish();
	}
	@Override
	public void tryAgain() {
		Log.d(TAG, "tryAgain() called");
	}

	@Override
	public void onError(Exception e) {
		Log.d(TAG, "onError() called with: e = [" + e.getMessage() + "]");
		//Log.e("exception ",""+e.getMessage());
	}
  	@Override
	public void cancelTransaction() {
		Log.d(TAG, "cancelTransaction() called");
	}

	@Override
	public int describeContents() {
		Log.v(TAG, "describeContents()....");
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.v(TAG, "writeToParcel(Parcel dest, int flags)....");
	}

	@SuppressWarnings("rawtypes")
	public static final Creator CREATOR = new Creator() {
		String TAG = "Callback --- Parcelable.Creator ::: > ";
		@Override
		public BilldeskCallBack createFromParcel(Parcel in) {
			Log.v(TAG, "CallBackActivity createFromParcel(Parcel in)....");
			return new BilldeskCallBack(in);
		}

		@Override
		public Object[] newArray(int size) {
			Log.v(TAG, "Object[] newArray(int size)....");
			return new BilldeskCallBack[size];
		}
	};
}
