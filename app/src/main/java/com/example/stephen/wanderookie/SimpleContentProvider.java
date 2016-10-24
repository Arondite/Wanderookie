
package com.example.stephen.wanderookie;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class SimpleContentProvider extends ContentProvider {
    // Provide a mechanism to identify all the incoming uri patterns.
    private static final int HIKES = 1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // /animal
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.HikesTable.URI_PATH, HIKES);
    }

    private DatabaseHelper dh;

    public boolean onCreate() {
        dh = new DatabaseHelper(getContext(),"USER_CREDENTIALS", null, 1);
        return true;
    }

    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setDistinct(true);
        switch (uriMatcher.match(uri)) {
            case HIKES:
                qb.setTables(DatabaseContract.HikesTable.TABLE_NAME);
                if (sortOrder == null)
                    sortOrder = DatabaseContract.HikesColumns.TRAILNAME + " ASC";
                c = qb.query(dh.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (getContext() != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (values != null) {
            switch (uriMatcher.match(uri)) {
                case HIKES:
                    SQLiteDatabase db = dh.getWritableDatabase();
                    long rowId = db.insert(DatabaseContract.HikesTable.TABLE_NAME, null, values);
                    if (rowId > 0) {
                        Uri insertedUri = ContentUris.withAppendedId(DatabaseContract.HikesTable.CONTENT_URI, rowId);
                        if (getContext() != null) {
                            getContext().getContentResolver().notifyChange(insertedUri, null);
                        }
                        return insertedUri;
                    }
                    throw new SQLException("Failed to insert row - " + uri);
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
        return null;
    }

    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dh.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case HIKES:
                count = db.update(DatabaseContract.HikesTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dh.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case HIKES:
                count = db.delete(DatabaseContract.HikesTable.TABLE_NAME,
                        DatabaseContract.HikesTable.TRAILID + "=" + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case HIKES:
                return DatabaseContract.HikesTable.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
}
