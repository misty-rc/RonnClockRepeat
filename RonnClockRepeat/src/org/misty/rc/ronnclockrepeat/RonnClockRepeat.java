package org.misty.rc.ronnclockrepeat;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class RonnClockRepeat extends AppWidgetProvider {
	
	private static final long INTERVAL = 1000 * 3; // 3sec
	private static final String ACTION_REPEAT = "org.misty.rc.ronnclockrepeat.ACTION_REPEAT";
	private static final String TAG = "RonnClockRepeat";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		super.onReceive(context, intent);
		
		// 更新用のアクションの場合
		if(intent.getAction().equals(ACTION_REPEAT)) {
			ComponentName cn = new ComponentName(context, RonnClockRepeat.class);
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.keyguard);
			long now = System.currentTimeMillis();
			Date date = new Date(now);
			
			// TextViewに文字列をセット
			rv.setTextViewText(R.id.textView1, date.toString());
			
			// Widgetを更新
			AppWidgetManager.getInstance(context).updateAppWidget(cn, rv);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.d(TAG, "onUpdate");
		
		for(int i = 0; i < appWidgetIds.length; i++) {
			Intent intent = new Intent(context, RonnClockRepeat.class);
			intent.setAction(ACTION_REPEAT);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			
			PendingIntent operation = PendingIntent.getBroadcast(
					context, appWidgetIds[i], intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			
			am.setRepeating(AlarmManager.RTC, 0, INTERVAL, operation);
		}
	}
}
