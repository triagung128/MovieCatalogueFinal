package com.triagung.moviecataloguefinal;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.triagung.moviecataloguefinal.activity.DetailActivity;
import com.triagung.moviecataloguefinal.model.Movie;

import static com.triagung.moviecataloguefinal.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class FavoriteWidget extends AppWidgetProvider {
    private static final String DETAIL_ACTION = "com.triagung.moviecataloguefinal.DETAIL_ACTION";
    public static final String EXTRA_ITEM = "com.triagung.moviecataloguefinal.EXTRA_ITEM";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent detailActivityIntent = new Intent(context, FavoriteWidget.class);
        detailActivityIntent.setAction(FavoriteWidget.DETAIL_ACTION);
        detailActivityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent detailActivityPendingIntent = PendingIntent.getBroadcast(context, 0, detailActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, detailActivityPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction() != null) {
            if (intent.getAction().equals(DETAIL_ACTION)) {
                Movie movie = intent.getParcelableExtra(EXTRA_ITEM);
                Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + movie.getId());
                Intent moveDetail = new Intent(context, DetailActivity.class);
                moveDetail.setData(uri);
                moveDetail.putExtra(DetailActivity.EXTRA_TYPE, "movie");
                moveDetail.putExtra(DetailActivity.EXTRA_ID, movie.getId());
                context.startActivity(moveDetail);
            }
        }
    }
}

