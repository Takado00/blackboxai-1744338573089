package com.example.recicapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationManager {
    private static final String CHANNEL_ID = "recicapp_notifications";
    private static final String CHANNEL_NAME = "RecicApp Notificaciones";
    private static final String CHANNEL_DESC = "Notificaciones de RecicApp para transacciones y materiales";
    private Context context;
    private static int notificationId = 1;

    public NotificationManager(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESC);
            
            android.app.NotificationManager notificationManager = 
                context.getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNewMaterialNotification(String materialName, String location) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Nuevo Material Disponible")
            .setContentText("Nuevo material: " + materialName + " en " + location)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId++, builder.build());
    }

    public void sendTransactionNotification(String transactionType, String materialName, String otherParty) {
        String title = "";
        String content = "";

        switch (transactionType) {
            case "new":
                title = "Nueva Solicitud de Transacción";
                content = otherParty + " está interesado en tu material: " + materialName;
                break;
            case "accepted":
                title = "Transacción Aceptada";
                content = otherParty + " aceptó la transacción para: " + materialName;
                break;
            case "completed":
                title = "Transacción Completada";
                content = "La transacción con " + otherParty + " para " + materialName + " fue completada";
                break;
            case "cancelled":
                title = "Transacción Cancelada";
                content = "La transacción con " + otherParty + " para " + materialName + " fue cancelada";
                break;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId++, builder.build());
    }

    public void sendRatingNotification(String materialName, float rating) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Nueva Valoración")
            .setContentText("Tu material " + materialName + " recibió una valoración de " + rating + " estrellas")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId++, builder.build());
    }

    public void sendLocationUpdateNotification(String materialName, String newLocation) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Actualización de Ubicación")
            .setContentText("La ubicación de " + materialName + " ha sido actualizada a: " + newLocation)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId++, builder.build());
    }
}
