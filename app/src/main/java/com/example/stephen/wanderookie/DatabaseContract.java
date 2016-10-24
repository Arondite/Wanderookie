package com.example.stephen.wanderookie;

/**
 * Created by zpanter on 2016-Aug-08.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "package com.example.stephen.wanderookie.SimpleContentProvider";
    public static final String DB_NAME = "wanderookie.db";
    public static final int DB_VERSION = 1;

    public interface HikesColumns {
        String TRAILID = BaseColumns._ID;
        String TRAILNAME = "TrailName";
        String TRAILDESCRIPTION = "TrailDescription";
        String TRAILCITY = "TrailCity";
        String TRAILCOUNTY = "TrailCounty";
        String TRAILSTATE = "TrailState";
        String TRAILONEWAYDISTANCE = "TrailOneWayDistance";
        String TRAILDIFFICULTY = "TrailDifficulty";
        String TRAILELEVATIONGAIN = "TrailElevationGain";
        String TRAILCOST  = "TrailCost";
        String TRAILPARKID = "ParkId";
    }

    public static final class HikesTable implements HikesColumns {
        public static final String TABLE_NAME = "hike";
        public static final String URI_PATH = "hike";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + URI_PATH);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_PATH;
        public static final String CONTENT_NOTE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + URI_PATH;

        private HikesTable() {
        }
    }
}
