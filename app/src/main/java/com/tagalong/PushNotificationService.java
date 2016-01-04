package com.tagalong;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by christon on 1/3/16.
 */
public class PushNotificationService extends IntentService {

  private NotificationCompat.Builder mBuilder;
  // Should set the details of the push notification here: title, time, location

  public PushNotificationService() {
    super("PushNotificationService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Parse
    Intent acceptInviteIntent = new Intent(this, AcceptInvite.class);
    Intent rejectInviteIntent = new Intent(this, RejectInvite.class);
    Intent viewInviteIntent = new Intent(this, ViewInvite.class);
    PendingIntent pendingAcceptIntent = PendingIntent.getBroadcast(this, 0, acceptInviteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    PendingIntent pendingRejectIntent = PendingIntent.getBroadcast(this, 0, rejectInviteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    PendingIntent pendingViewIntent = PendingIntent.getBroadcast(this, 0, viewInviteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    //building the notification
    mBuilder = new NotificationCompat.Builder(this)
      .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
      .setContentTitle("New Tagalong Invitation")
      .setContentText("Event Details") // TODO: receive actual details from event
      .addAction(R.drawable.ic_checkmark_holo_light, "Accept", pendingAcceptIntent)
      .addAction(R.drawable.ic_menu_block, "Decline", pendingRejectIntent)
      .addAction(android.R.drawable.ic_menu_today, "View", pendingViewIntent);

    mBuilder.setContentIntent(pendingViewIntent);

    int notificationId = 001; // TODO: use eventCount and hostId/hostEmail to give a unique id
//    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//    mNotifyMgr.notify(mNotificationId, mBuilder.build());
    startForeground(notificationId, mBuilder.build());
  }
}
