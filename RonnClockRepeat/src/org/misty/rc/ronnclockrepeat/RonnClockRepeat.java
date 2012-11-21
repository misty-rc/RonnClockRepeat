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
		
		// アクション文字列が該当する場合
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
		
		for(int appWidgetId: appWidgetIds) {
			Intent intent = new Intent(context, RonnClockRepeat.class);
			// Action文字列をセット
			intent.setAction(ACTION_REPEAT);
			// 今回は使わないがServiceなどで一意のIDを取得したい場合はセットしておくと便利
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			
			// 一意のIDをrequestCodeに渡さないとWidgetを特定できない
			// referenceの"currently not used"はウソっぽい
			PendingIntent operation = PendingIntent.getBroadcast(
					context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC, 0, INTERVAL, operation);
		}
	}
}
