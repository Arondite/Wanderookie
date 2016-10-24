package com.example.stephen.wanderookie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;

    public DatabaseHelper(Context context)
    {
        super(context, DatabaseContract.DB_NAME, null, DatabaseContract.DB_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public SQLiteDatabase open(){
        database = getWritableDatabase();
        return database;
    }

    public void close(){
        if(database != null){
            database.close();
        }
    }

    private int getUserId(String username)
    {
        int UserId;
        String[] params = new String[1];
        params[0] = "" + username;
        int userID = 0;
        Cursor cursor;
        if(open() != null)
        {
            cursor = database.rawQuery("SELECT UserId FROM USER WHERE UserName = ?", params);
            if(cursor != null && cursor.moveToFirst())
            {
                userID = cursor.getInt(cursor.getColumnIndex("UserId"));
            }

        }

        UserId = userID;

        return UserId;
    }

    public boolean UserExists(String username)
    {
        String[] params = new String[1];
        params[0] = "" + username;
        int userID = 0;
        Cursor cursor = null;
        close();
        //onCreate(database);
        if(open() != null)
        {
            cursor = database.rawQuery("SELECT UserId FROM USER WHERE UserName = ?", params);
            if(cursor != null && cursor.moveToFirst())
            {
                userID = cursor.getInt(cursor.getColumnIndex("UserId"));
            }
        }

        if(userID >= 1)
        {
            return true;
        }
        else
            return false;
    }

    public UserCredentials testUser(String username)
    {
        UserCredentials user = new UserCredentials();
        int userID = 0;
        String userPassword = "";

        String[] params = new String[1];
        params[0] = "" + username;
        Cursor cursor = null;
        if(open() != null)
        {
            cursor = database.rawQuery("SELECT UserId FROM USER WHERE UserName = ?", params);
            if(cursor != null && cursor.moveToFirst())
            {
                userID = cursor.getInt(cursor.getColumnIndex("UserId"));
            }
        }

        params[0] = "" + userID;
        if(open() != null)
        {
            cursor = database.rawQuery("SELECT UserPassword FROM USER_CREDENTIAL WHERE UserId = ?", params);
            if(cursor != null && cursor.moveToFirst()) {
                userPassword = cursor.getString(cursor.getColumnIndex("UserPassword"));
            }
        }


        user.SetUsername(username);
        user.SetPassword(userPassword);
        return user;

    }

    public int updateTotalMilesHiked(int milesToAdd, String username)
    {
        int logMilesHiked = 0;
        int newTotal;
        int logId = 0;
        String logDay = "";
        String logGoalMet = "";
        String logDescription = "";
        int logElevationGained = 0;
        String logTime = "";

        int UserId;
        UserId = getUserId(username);

        String[] params = new String[1];
        params[0] = "" + UserId;
        Cursor cursor;
        Cursor full;
        if(open() != null)
        {
            cursor = database.rawQuery("SELECT LogMilesHiked FROM USER_LOG WHERE UserId = ?", params);
            full = database.rawQuery("SELECT * FROM USER_LOG WHERE UserId = ?", params);
            if(cursor != null && cursor.moveToFirst())
            {
                logMilesHiked = cursor.getInt(cursor.getColumnIndex("LogMilesHiked"));
            }
            if(full != null && full.moveToFirst())
            {
                logId = full.getInt(full.getColumnIndex("LogId"));
                logDay = full.getString(full.getColumnIndex("LogDay"));
                logGoalMet = full.getString(full.getColumnIndex("LogGoalMet"));
                logDescription = full.getString(full.getColumnIndex("LogDescription"));
                logElevationGained = full.getInt(full.getColumnIndex("LogElevationGained"));
                logTime = full.getString(full.getColumnIndex("LogTime"));
            }
        }


        newTotal = logMilesHiked + milesToAdd;

        ContentValues editUserLog = new ContentValues();
        editUserLog.put("LogId", logId);
        editUserLog.put("LogDay", logDay);
        editUserLog.put("LogGoalMet", logGoalMet);
        editUserLog.put("LogMilesHiked", newTotal);
        editUserLog.put("LogDescription", logDescription);
        editUserLog.put("LogElevationGained", logElevationGained);
        editUserLog.put("LogTime", logTime);

        if(open() != null)
        {
            database.update("USER_LOG", editUserLog, "UserId=" + UserId, null);
        }

        return newTotal;
    }

    public void addNewBucketListItem(String username, int trailId)
    {
        String bucketComplete = "false";
        ContentValues insertBucketList = new ContentValues();

        int UserId;
        UserId = getUserId(username);

        insertBucketList.put("BucketComplete", bucketComplete);
        insertBucketList.put("TrailId", trailId);
        insertBucketList.put("UserId", UserId);

        if(open() != null)
        {
            database.insert("BUCKET_LIST",null,insertBucketList);
        }

    }

    public boolean signUpInsert(String userName, String userEmail, String userZip, String userCity, String userState, String userPassword)
    {
        ContentValues insertNewUser = new ContentValues();
        ContentValues insertNewUserCredentials = new ContentValues();
        String nullString = null;

        insertNewUser.put("UserId", nullString);
        insertNewUser.put("UserName", userName);
        insertNewUser.put("UserEmail", userEmail);
        insertNewUser.put("UserZip", userZip);
        insertNewUser.put("UserCity", userCity);
        insertNewUser.put("UserState", userState);

        if(open() != null)
        {
            database.insert("USER",null,insertNewUser);
        }

        String[] params = new String[1];
        params[0] = "" + userName;
        int UserId = 0;
        Cursor cursor;
        close();
        if(open() != null)
        {
            cursor = database.rawQuery("SELECT UserId FROM USER WHERE UserName = ?", params);
            if(cursor != null && cursor.moveToFirst())
            {
                UserId = cursor.getInt(cursor.getColumnIndex("UserId"));
            }
        }

        insertNewUserCredentials.put("UserId", UserId);
        insertNewUserCredentials.put("UserPassword", userPassword);

        if(open() != null)
        {
            database.insert("USER_CREDENTIAL",null,insertNewUserCredentials);
        }

        if(UserExists(userName))
            return true;
        else
            return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        long rowID = -1;

        String dropQuery = "DROP TABLE IF EXISTS USER";
        db.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS TRAIL";
        db.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS AMENITY";
        db.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS BUCKET_LIST";
        db.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS COORDINATE";
        db.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS PARK";
        db.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS TRAIL_AMENITY";
        db.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS TRAIL_LOG";
        db.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS USER_CREDENTIAL";
        db.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS USER_LOG";
        db.execSQL(dropQuery);


        String createQuery = "CREATE TABLE USER (" +
                "UserId integer primary key autoincrement," +
                "UserName TEXT," +
                "UserEmail TEXT," +
                "UserZip TEXT," +
                "UserCity TEXT," +
                "UserState TEXT" +
                ");";
        db.execSQL(createQuery);

        createQuery = "CREATE TABLE TRAIL (" +
                "TrailId INTEGER," +
                "TrailCity TEXT," +
                "TrailZip TEXT," +
                "TrailState BLOB," +
                "TrailDistance INTEGER," +
                "TrailElevation INTEGER," +
                "TrailHead TEXT," +
                "TrailCost TEXT," +
                "TrailDescription TEXT," +
                "ParkId INTEGER," +
                "TrailDifficulty TEXT," +
                "PRIMARY KEY(TrailId)," +
                "FOREIGN KEY(`ParkId`) REFERENCES PARK(ParkId)" +
                ");";
        db.execSQL(createQuery);
        //Hikes
//        createQuery = "CREATE TABLE " + DatabaseContract.HikesTable.TABLE_NAME + " (" +
//                DatabaseContract.HikesTable.TRAILID + "INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
//                DatabaseContract.HikesTable.TABLE_NAME + " TEXT," +
//                DatabaseContract.HikesTable.TRAILDESCRIPTION + " TEXT," +
//                DatabaseContract.HikesTable.TRAILCITY + " TEXT," +
//                DatabaseContract.HikesTable.TRAILCOUNTY + " TEXT," +
//                DatabaseContract.HikesTable.TRAILSTATE + " TEXT," +
//                DatabaseContract.HikesTable.TRAILONEWAYDISTANCE + " INTEGER," +
//                DatabaseContract.HikesTable.TRAILDIFFICULTY + " TEXT," +
//                DatabaseContract.HikesTable.TRAILELEVATIONGAIN + " INTEGER," +
//                DatabaseContract.HikesTable.TRAILCOST + " INTEGER," +
//                DatabaseContract.HikesTable.TRAILPARKID + " INTEGER," +
//
//                "PRIMARY KEY(TrailId)," +
//                "FOREIGN KEY(`ParkId`) REFERENCES PARK(ParkId)" +
//                ");";
//        db.execSQL(createQuery);


        createQuery = "CREATE TABLE AMENITY (" +
                "AmentityId INTEGER," +
                "AmentityDescription TEXT," +
                "PRIMARY KEY(AmentityId)" +
                ");";
        db.execSQL(createQuery);

        createQuery = "CREATE TABLE BUCKET_LIST (" +
                "BucketId INTEGER," +
                "BucketComplete TEXT," +
                "TrailId INTEGER," +
                "UserId INTEGER," +
                "PRIMARY KEY(BucketId)," +
                "FOREIGN KEY(TrailId) REFERENCES TRAIL(TrailId)," +
                "FOREIGN KEY(UserId) REFERENCES User(UserId)" +
                ");";
        db.execSQL(createQuery);

        createQuery = "CREATE TABLE COORDINATE (" +
                "coordinateId INTEGER," +
                "CoordinateName TEXT," +
                "Latitude INTEGER," +
                "Longitude INTEGER," +
                "Altitude INTEGER," +
                "TrailId INTEGER," +
                "PRIMARY KEY(coordinateId)," +
                "FOREIGN KEY(TrailId) REFERENCES TRAIL(TrailId)" +
                ");";
        db.execSQL(createQuery);

        createQuery = "CREATE TABLE PARK (" +
                "ParkId INTEGER," +
                "ParkName TEXT," +
                "ParkShortCode TEXT," +
                "PeakSeasonStart TEXT," +
                "PeakSeasonEnd TEXT," +
                "PRIMARY KEY(ParkId)" +
                ");";
        db.execSQL(createQuery);

        createQuery = "CREATE TABLE TRAIL_AMENITY (" +
                "TrailId INTEGER," +
                "AmentityId INTEGER," +
                "AmentityStartDate TEXT," +
                "AmentityEndDate TEXT," +
                "PRIMARY KEY(TrailId,AmentityId)," +
                "FOREIGN KEY(TrailId) REFERENCES TRAIL(TrailId)," +
                "FOREIGN KEY(AmentityId) REFERENCES AMENTITIES(AmentityId)" +
                ");";
        db.execSQL(createQuery);

        createQuery = "CREATE TABLE TRAIL_LOG (" +
                "LogId INTEGER," +
                "RatingScore INTEGER," +
                "RatingComment TEXT," +
                "UserId INTEGER," +
                "TrailId INTEGER," +
                "PRIMARY KEY(LogId)," +
                "FOREIGN KEY(UserId) REFERENCES User(UserId)," +
                "FOREIGN KEY(TrailId) REFERENCES TRAIL(TrailId)" +
                ");";
        db.execSQL(createQuery);

        createQuery = "CREATE TABLE USER_CREDENTIAL (" +
                "UserCredentialid INTEGER," +
                "UserId INTEGER," +
                "UserPassword TEXT," +
                "PRIMARY KEY(UserCredentialid)," +
                "FOREIGN KEY(UserId) REFERENCES User(UserId)" +
                ");";
        db.execSQL(createQuery);

        createQuery = "CREATE TABLE USER_LOG (" +
                "LogId INTEGER," +
                "UserId INTEGER," +
                "Logday TEXT," +
                "LogGoalMet TEXT," +
                "LogMilesHiked TEXT," +
                "LogDescription TEXT," +
                "LogElevationGained INTEGER," +
                "LogTime TEXT," +
                "PRIMARY KEY(LogId)," +
                "FOREIGN KEY(UserId) REFERENCES User(UserId)" +
                ");";
        db.execSQL(createQuery);

        //insertHikesData(db);
        insertSample(db);


    }

    public void insertSample(SQLiteDatabase dbLite)
    {
        dbLite.execSQL("INSERT INTO USER VALUES(1, 'guest', 'guest', 'guestZip', 'Guest', 'Guest');");
        dbLite.execSQL("INSERT INTO USER_CREDENTIAL VALUES(1,1,'guest');");

        //End of Andy's insert. All problems from here is Stephen's issues
        dbLite.execSQL("INSERT INTO PARK VALUES(1,'Antelope Island State Park',10.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(2,'Arches National Park',10.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(3,'Ashley National Forest',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(4,'Bear Lake State Park',10.00,'2016-05-01','2016-10-31');");
        dbLite.execSQL("INSERT INTO PARK VALUES(5,'Bryce Canyon National Park',30.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(6,'Canyonlands National Park',10,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(7,'Capitol Reef National Park',10.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(8,'Cedar Breaks National Monument',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(9,'Coral Pink Sand Dunes State Park',8.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(10,'Dead Horse Point State Park',10.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(11,'Dinosaur National Monument',20.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(12,'Dixie National Forest',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(13,'Fishlake National Forest',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(14,'Flaming Gorge NRA',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(15,'Four Corners Area',5.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(16,'Glen Canyon/Lake Powell',25.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(17,'Golden Spike State Park',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(18,'Grand Staircase-Escalante',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(19,'Hovenweep State Park',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(20,'Manti-La Sal National Forest',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(21,'Monument Valley Navajo Tribal Park',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(22,'Natural Bridges National Monument',10.00,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(23,'Rainbow Bridge National Monument',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(24,'Snow Canyon State Park',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(25,'Timpanogos Cave National Monument',8.00,'2016-05-15','2016-10-31');");
        dbLite.execSQL("INSERT INTO PARK VALUES(26,'Uinta-Wasatch-Cache National Forest',null,null,null);");
        dbLite.execSQL("INSERT INTO PARK VALUES(27,'Zion National Park',30.00,null,null);");

//********************************************************************************************************************************************************************************************************************************************************************************************
//        //--insert into TRAIL values(null, TrailName, TrailDescription, TrailCity, TrailCounty, TrailState, TrailOneWayDistance, TrailDifficulty, TrailElevationGain, TrailCost, ParkId);
        dbLite.execSQL("INSERT INTO TRAIL VALUES(1, 'Hidden Falls','The Hidden Falls trail up Big Cottonwood canyon is a short adventure good for a quick escape.','Big Cottonwood Canyon','Salt Lake County','UT', 0.1,'EASY', 173, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(2, 'Red Ledges Picnic Area Trails','Red Ledges is the mini Moab you never knew existed.','Diamond Fork','Utah','UT', 0.1,'EASY/MODERATE', 227, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(3, 'Lisa Falls','Lisa Falls is a quirky little waterfall that cascades at an angle.','Little Cottonwood Canyon','Salt Lake','UT',0.3,'EASY',125,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(4, 'Grotto Falls','If you like the idea of crossing a rushing river via narrow tree trunks and a thundering waterfall finale - this trail is for you.','Payson/Nebo Scenic Loop','Utah','UT',0.3,'EASY',186,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(5, 'Canyon Overlook Trail','This trail starts immediately after the Mount Carmel tunnel, and gives a stunning view of Zion National Park.','Zion National Park','Washington','UT',0.5,'EASY',141,30.00,27);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(6, 'Rocky Mouth Waterfall','A short, rising hike that starts in a Sandy neighborhood and leads to a lovely waterfall.','Sandy','Salt Lake','UT',0.4,'EASY',324,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(7, 'Bridal Veil Falls','This is a the tallest waterfall in the state. At Bridal Veil you can grill, camp, hike bike, feed fish or splash around in the bottom of the falls!','Provo Canyon','Utah','UT', 0.4,'EASY', 80, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(8, 'Battle Creek Falls','Battle Creek Falls near Springville is a quick hike to a decent sized waterfall. The trail follows a steady incline the whole way.','Springville','Utah','UT', 0.5,'Easy/Moderate', 436, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(9, 'Big Springs Hollow','Big Springs Hollow near Provo offers scenic mountain views, aspen forests and lots of wooden bridges crossing babbling brooks. ','Provo Canyon - South Fork','Utah','UT', 0.6,'DIFFICULTY', 257, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(10,'Emerald Pools','These picturesque pools contrast nicely with the red rock of Zion.','Zion National Park','Washington','UT', 0.8,'Moderate', 360, 30.00, 27);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(11,'Riverside Walk','This paved trail follows the Virgin River and provides great views of the canyon walls, river-access and leads to the Narrows.','Zion National Park','Washington','UT', 1,'Easy', 69, 30.00, 27);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(12,'Timp Falls','Accessible from the Aspen Grove Trailhead near Sundance Ski Resort, this cascade is a nice alternative when Stewart Falls is busy.','Provo Canyon','Utah','UT', 1,'Easy/Moderate', 729, 6.00, 26);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(13,'Scout Falls','Scout Falls is a great way to experience a portion of the beautiful Timpooneke trail without actually trekking all the way up Timp.','Provo Canyon','Utah','UT', 1.1,'MODERATE', 752, 6.00, 26);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(14,'Lagoon Trail','This trails follows Farmington Creek through along the east end of Lagoon, and takes you through desne growth.','Farmington','Davis','UT', 1.3,'EASY', 15, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(15,'Timp Caves Hike','You don''t need a ticket to do this great hike, but be sure to get some for the neat cave tours at the end!','American Fork Canyon','Utah','UT', 1.5,'Moderate/Strenuous', 1500, 8.00, 26);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(16,'Parus Trail','This paved trail starts at the Zion Visitor Center and crosses over beautiful bridges.','Zion National Park','Washington','UT', 1.7,'Easy', 95, 30.00, 27);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(17,'Stewart Falls','This popular family friendly hike ends in a 200 ft. waterfall near Sundance Ski Resort.','Provo Canyon','Utah','UT', 1.8,'Moderate', 18, 8.00, 26);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(18,'Do(ugh)nut Falls','This is one of the most popular hikes up Big Cottonwood Canyon that leads to a small but steep waterfall.','Big Cottonwood Canyon','Salt Lake','UT', 1.8,'Easy', 428, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(19,'Malan''s Peak','A great canyon hike near Ogden Utah that''s very reminiscent of Riverdale.','Ogden','Weber','UT', 1.8,'Moderate', 2086, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(20,'Adams Canyon Waterfall','Starting with initial switchbacks, this trail leads through lots of shade and some rugged terrain to one of the most popular places in Layton!','','Davis','UT', 1362,'Easy/Moderate', 1362, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(21,'Little Wildhorse Slot Canyon','This slot canyon north of Goblin Valley State Park is great for families, and allows non-technical hikers a slot canyon experience.','San Rafael Swell','Emery','UT', 1.9,'Easy/Moderate', 268, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(22,'Bell''s Canyon Waterfall','Leading to both a reservoir and waterfall, this popular hike is good for families wanting more of a challenge.','Salt Lake City','Salt Lake','UT', 2,'Moderate/Strenuous', 2204, null,null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(23,'Fifth Water Hot Springs','The Fifth Water trail leads to natural hot springs in the middle of Diamond Fork Canyon. There are natural hot springs pools fed by a lovely waterfall.','Spanish Fork','Utah','UT', 2.1,'Easy/Moderate', 598, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(24,'Angel''s Landing','This quintessential Zion hike rises up tight switchbacks to vertigo-inducing ridges over the Virgin River','Zion National Park','Washington','UT', 2.5,'Strenuous', 1496, 30.00, 27);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(25,'Calf Creek Lower Falls','This desert waterfall attracts visitors from across the globe due to its beautiful colors, impressive height and unique location.','Grand Staircase/Escalante','Garfield','UT', 2.7,'Moderate', 356, null, 18);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(26,'Y Mountain','This popular Utah county hike starts with switchbacks, but leads into the picturesque Slide Canyon up to the Y Mountain summit.','Provo','Utah','UT', 2.9,'Moderate', 3345, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(27,'The Subway (from the Bottom Up)','This route is a way to enjoy the stunning beauty of the Subway without technical equipment.','Zion National Park','Washington','UT', 3.2,'Strenuous', 273, 30.00, 27);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(28,'Squaw Peak','Otherwise known as Pride Rock, Squaw Peak up Rock Canyon in Provo takes you past tall mountains and winding forest paths up to a great viewpoint.','Provo','Utah','UT', 3.4,'Moderate', 2708, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(29,'Timpanogos Summit (Timpooneke)','The tallest mountain in the Wasatch provides stunning vistas, colorful wildflowers, glacial lakes and a lofty summit to boot!','Alpine','Utah','UT', 5.5,'Strenuous', 4385, null, 26);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(30,'Red Reef Trail (Red Cliffs Recreation Area)','Tucked away in the west end of the Red Cliffs Recreation area, the Red Reef trail has a natural waterslide and beautiful red rock.','Leeds','Washington','UT', 4,'DIFFICULTY', 45, 10, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(31,'Timpanogos Summit (Aspen Grove)','An alternative (albeit steeper) route to the summit of Timpanogos. Wildflowers, mountain goats, glacial lakes - it is beautiful!','Provo Canyon','Utah','UT', 5,'Strenuous', 4857, 10, 26);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(32,'Ogden River Parkway','Starting at Rainbow Gardens near Ogden Canyon, the Ogden Parkway follows the Ogden River in a winding, peaceful paved path to Fort Bueniventura.','Ogden','Weber','UT', 15,'Easy/Moderate', 117, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(33,'Provo River Parkway','The paved parkway follows the Provo River from Vivian Park up Provo Canyon all the way down to Utah Lake State Park.','Provo Canyon','Utah','UT', 15,'Easy/Moderate', 702, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(34,'Kanarraville Falls','This is a river hike up a the lovely Kanarra Creek up into a slot canyon with a waterfall.','Cedar City (Kanarraville)','Washington','UT', 1.26,'Moderate', 629, 10, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(35,'Buffalo Peak','Buffalo Peak is a great peak to summit if you''ve enjoyed the view at Squeak Peak Overlook and want more of an adventure!','Provo Canyon','Utah','UT', 4,'Moderate', 1328, null, null);");
        dbLite.execSQL("INSERT INTO TRAIL VALUES(36,'The Narrows','This river hike passes through a giant slot canyon with waterfalls, greenery and adventure. Make sure to bring your dry-bags and trekking poles!','Zion National Park','Washington','UT', 3,'DIFFICULTY', 20, 30, 27);");

//******************************************************************************************************************************************************************************************************************************************************************************************************
//        //--insert into COORDINATE values(null, 'CoordinateName', Latitude, Longitude, Elevation);
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(1,'Hidden Falls Parking Area',40.63422,-111.72418,6214);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(2,'Hidden Falls TH',40.63454,-111.72374,6272);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(3,'Hidden Falls Turnoff',40.63467,-111.7235,6318);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(4,'Hidden Falls', 40.63521, -111.72319, 6387);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(5,'Red Ledges Parking Area', 40.07983,-111.40338,5305);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(6,'Red Ledges Arch', 40.07996,-111.402405,5532);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(7,'Lisa Falls TH', 40.57263,-111.72673,6512);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(8,'Lisa Falls',40.57386, -111.72758,6637);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(9,'Grotto Falls TH',	39.951443,	-111.675769,	6357);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(10,'Grotto Falls',	39.948588,	-111.677673,	6543);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(11,'Zion Canyon Overlook TH',	37.21337,	-112.94576,	5121);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(12,'Zion Canyon Overlook',	37.213456,	-112.94576,	5262);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(13,'Rocky Mouth TH',	40.547131,	-111.806519,	5121);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(14,'Rocky Mouth Waterfall',	40.543072,	-111.803564,	5445);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(15,'Bridal Veil Falls',	40.340226,	-111.60306,	5169);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(16,'Bridal Veil Parking Area',	40.341229,	-111.603564,	5089);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(17,'Bridal Veil Extra Parking',	40.337804,	-111.60918);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(18,'Battle Creek TH',	40.362986,	-111.700841,	5247);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(19,'Battle Creek Falls',	40.367392,	-111.692891,	5683);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(20,'Big Springs Hollow TH',	40.3327,	-111.52521,	5770);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(21,'Big Springs Hollow'	40.32476,	-111.53204,	6027);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(22,'The Grotto (Emerald Pools TH)',	37.253357,	-112.956084,	4271);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(23,'Emerald Lower Pool (Zion)',	37.257025,	-112.96262,	4303);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(24,'Emerald Middle Pool (Zion)',	37.257466,	-112.963488,	4435);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(25,'Emerald Upper Pool (Zion)',	37.257847,	-112.966867,	4631);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(26,'Riverside Walk TH',	37.285645,	-112.947552,	4440);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(27,'Riverside Walk End, Narrows Start',	37.296671,	-112.94822,	4509);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(28,'TH (Aspen Grove)',	40.404272,	-111.605276,	6890);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(29,'Timp Falls',	40.403863,	-111.62131,	7619);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(30,'TH (Timpooneke)',	40.43126,	-111.63914,	7364);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(31,'Scout Falls',	40.431267,	-111.639144,	7619);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(32,'Lagoon Trail (North TH)',	40.987548,	-111.896405,	4295);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(33,'Lagoon Trail (South TH)',	40.981132,	-111.896405,	4280);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(34,'Timp Cave TH',	40.443587,	-111.705502,	5647);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(35,'Timp Cave Entrance',	40.43901,	-111.708749,	7147);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(36,'Parus Trail Canyon Junction (End)',	37.217803,	-112.974491,	4032);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(37,'Parus TH',	37.201898,	-112.986391,	3937);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(38,'Stewart Falls',	40.38624,	-111.603902,	6909);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(39,'Donut TH',	40.629722,	-111.654722,	7475);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(40,'Donut Falls',	40.631108,	-111.654516,	7903);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(41,'Ogden 29th Street TH',	41.21115,	-111.931913,	4772);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(42,'Malans Summit',	41.207149,	-111.919573,	6858);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(43,'Adams TH',	41.06634,	-111.909934,	4825);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(44,'Adams Lower Falls',	41.06516,	-111.904036,	6187);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(45,'Adams Upper Falls',	41.067356,	-111.885193,6208	);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(46,'Little Wild Horse TH',	38.582768,	-110.802884,	4960);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(47,'Little Wild Horse North End',	38.600424,	-110.799075,	5228);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(48,'Bells Canyon TH',	40.5653,	-111.80371,	5153);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(49,'Bells Resevoir',	40.565265,	-111.795802,5571);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(50,'Bells Turnoff',	40.56046,	-111.77059,6724	);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(51,'Bells Falls',	40.56138,	-111.77055,	7357);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(52,'Fifth Water TH',	40.084307,	-111.354818,	5548);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(53,'Fifth Water Falls, Hot Springs',	40.082825,	-111.318276,	6146);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(54,'Angels Landing TH (The Grotto)',	37.259294,	-112.951441,	4292);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(55,'Angels Landing Switchbacks (Walters Wiggles)',	37.275815,	-112.951607,5141);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(56,'Angels Landing Viewpoint',	37.268305,	-112.94833,	5788);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(57,'Calf Creek TH',	37.799412,	-111.411898,	5375);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(58,'Calf Creek Lower Falls',	37.829107,	-111.419911,	5731);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(59,'Calf Creek Upper Falls',	37.855077,	-111.451987,	5971);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(60,'Y Mountain TH',	40.244845,	-111.627381,	5151);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(61,'The 'Y'',	40.248211,	-111.620089,	6222);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(62,'Slide Canyon - Bear Flat',	40.245116,	-111.610701, 7009);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(63,'Y Mountain Summit',	40.256775,	-111.606767,	8496);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(64,'Y Mountain Overlook',	40.254587,	-111.611132, 8487);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(65,'Subway Trail Start',	37.28922,	-113.08492, 4671);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(66,'Subway Parking Lot',	37.28483,	-113.09576, 5088);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(67,'Kolob Terrace Road Turnoff',	37.20373,	-113.18594, 3561);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(68,'The Subway',	37.30951,	-113.05178,	5361);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(69,'Rock Canyon TH',	40.264529,	-111.629792,	5136);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(70,'Rock Canyon Left Fork',	40.269446,	-111.602567, 6181	);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(71,'Squaw Peak 2nd Meadow',	40.278368,	-111.618096, 7282	);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(72,'Squaw Peak Summit',	40.27182,	-111.616465,	7844);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(73,'(Timp) Emerald Lake',	40.39269,	-111.64017, 10356	);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(74,'Timp Saddle',	40.396111,	-111.654722, 11054	);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(75,'Timp Summit',	40.39084,	-111.64593,	11749);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(76,'Waterslide',	37.228271,	-113.410542,	3222);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(77,'Red Reef Trail TH',	37.222539,	-113.403411,	3267);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(78,'Rainbow Gardens (Ogden River Parkway)',	41.236472,	-111.9294,	4419);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(79,'Lorin Farr Pool (Ogden)',	41.236704,	-111.957985,	4313);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(80,'Dinosaur Park (Ogden)',	41.237612,	-111.937807, 4381	);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(81,'Fort Buenventura (Ogden)', 41.218708, -111.988426,	4302);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(82,'Vivian Park (Provo River Parkway)',	40.355256,	-111.574133,	5201);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(83,'Utah Lake State Park (Provo River Parkway)',	40.237931,	-111.732391,	4499);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(84,'Kanarraville TH',	37.537438,	-113.175622,	5624);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(85,'Kanarraville Lower Falls',	37.537536,	-113.152238,	6253);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(86,'Kanarraville Upper Falls',	37.53995,	-113.147628,	6297);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(87,'Mystery Falls (Narrows)',	37.299407,	-112.944414,	4519);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(88,'Squaw Peak Overlook Front Trail (Buffalo Peak)',	40.301222,	-111.625316,	6688);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(89,'Little Rock Canyon Overlook (Buffalo Peak)',	40.290868,	-111.61346,	7260);");
//        dbLite.execSQL("INSERT INTO COORDINATE VALUES(90,'Buffalo Peak Summit',	40.282567,	-111.612822,	8016);");

//        //--insert into COORDINATE_TRAIL values(CoordinateId, TrailId,Order);
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(1,1,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(2,1,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(3,1,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(4,1,4);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(5,2,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(6,2,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(7,3,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(8,3,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(9,4,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(10,4,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(11,5,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(12,5,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(13,6,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(14,6,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(15,7,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(16,7,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(17,7,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(18,8,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(19,8,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(20,9,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(21,9,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(22,10,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(23,10,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(24,10,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(25,10,4);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(26,11,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(27,11,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(28,12,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(29,12,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(30,13,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(31,13,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(32,14,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(33,14,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(34,15,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(35,15,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(36,16,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(37,16,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(28,17,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(38,17,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(39,18,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(40,18,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(41,19,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(42,19,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(43,20,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(44,20,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(45,20,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(46,21,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(47,21,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(48,22,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(49,22,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(50,22,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(51,22,4);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(52,23,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(53,23,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(54,24,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(55,24,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(56,24,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(57,25,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(58,25,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(59,25,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(60,26,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(61,26,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(62,26,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(63,26,4);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(64,26,5);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(65,27,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(66,27,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(67,27,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(68,27,4);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(69,28,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(70,28,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(71,28,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(72,28,4);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(30,29,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(31,29,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(73,29,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(74,29,4);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(75,29,5);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(76,30,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(77,30,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(28,31,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(29,31,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(73,31,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(74,31,4);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(75,31,5);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(78,32,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(79,32,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(80,32,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(81,32,4);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(82,33,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(83,33,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(84,34,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(85,34,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(86,34,3);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(27,36,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(87,36,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(88,35,1);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(89,35,2);");
//        dbLite.execSQL("INSERT INTO COORDINATE_TRAIL VALUES(90,35,3);");

        //--insert into BUCKET_LIST values(null, BucketComplete, TrailId, UserId);
        dbLite.execSQL("INSERT INTO BUCKET_LIST VALUES(1,'TRUE',1,1);");

        //--insert into AMENITY values(null, AmenityId);
        dbLite.execSQL("INSERT INTO AMENITY VALUES(1,'Trailhead Map');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(2,'Portapotty');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(3,'Vault Toilet');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(4,'Plumbed Toilet');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(5,'Water Fountain');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(6,'Water Access (for Purifiers)');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(7,'Summit Trail');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(8,'Waterfall');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(9,'Lake/Pond/Pools');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(10,'River');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(11,'River Hiking');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(12,'Wheelchair Accessibility');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(13,'Dog Friendly');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(14,'Picnic Tables');");
        dbLite.execSQL("INSERT INTO AMENITY VALUES(15,'Slot Canyon');");

        //--insert into TRAIL_AMENITY values(TrailId, AmenityId, AmenityStartDate, AmenityEndDate);
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(1,6,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(1,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(1,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(2,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(2,14,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(3,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(3,6,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(3,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(4,6,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(4,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(4,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(4,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(5,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(6,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(7,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(8,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(8,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(8,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(9,6,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(9,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(10,9,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(10,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(10,1,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(11,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(11,12,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(12,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(12,6,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(12,3,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(12,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(12,5,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(13,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(13,6,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(13,3,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(13,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(14,12,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(15,4,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(15,5,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(16,12,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(16,4,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(10,12,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(17,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(17,5,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(17,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(18,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(10,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(19,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(19,7,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(20,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(20,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(20,6,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(21,15,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(22,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(23,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(23,9,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(23,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(23,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(24,7,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(25,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(25,9,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(26,7,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(26,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(27,3,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(27,11,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(28,7,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(28,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(29,7,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(29,6,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(29,9,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(30,6,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(31,7,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(31,9,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(31,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(31,5,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(32,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(32,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(32,12,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(33,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(33,12,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(34,8,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(34,10,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(34,11,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(35,7,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(35,13,null,null);");
        dbLite.execSQL("INSERT INTO TRAIL_AMENITY VALUES(36,8,null,null);");

        //--insert into USER values(null, 'UserUserName', 'UserEmail', UserZip, 'UserCity', UserState);
        dbLite.execSQL("INSERT INTO USER VALUES(5, 'astettler', 'williamstettler@mail.weber.edu', 11111, 'Davis', 'UT');");
        dbLite.execSQL("INSERT INTO USER VALUES(2, 'cchiang', 'chachiacheng@mail.weber.edu', 11111, 'Davis', 'UT');");
        dbLite.execSQL("INSERT INTO USER VALUES(3, 'sknotek', 'stephenknotek@mail.weber.edu', 11111, 'Davis', 'UT');");
        dbLite.execSQL("INSERT INTO USER VALUES(4, 'zpanter', 'zacharypanter@mail.weber.edu', 84403, 'Provo', 'UT');");

        //--insert into TRAIL_LOG values(null, RatingScore, RatingComment, UserId, TrailId);
        dbLite.execSQL("INSERT INTO TRAIL_LOG VALUES(1,5,'This hike was sooooo great!',1,1);");

        //--insert into USER_LOG values(null,UserId, LogDay, LogGoalMet, LogMilesHiked, LogDescription, LogElevationGained, LogTime);
        dbLite.execSQL("INSERT INTO USER_LOG VALUES(1,1,'2016-04-15','TRUE',10,'A great hiking day.',186,2.5);");
    }

    public void insertHikesData(SQLiteDatabase dbLite){
        dbLite.execSQL("insert into TRAIL values(null,'Hidden Falls','The Hidden Falls trail up Big Cottonwood canyon is a short adventure good for a quick escape.','Big Cottonwood Canyon','Salt Lake County','UT', 0.1,'EASY', 173, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Red Ledges Picnic Area Trails','Red Ledges is the mini Moab you never knew existed.','Diamond Fork','Utah','UT', 0.1,'EASY/MODERATE', 227, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Lisa Falls','Lisa Falls is a quirky little waterfall that cascades at an angle.','Little Cottonwood Canyon','Salt Lake','UT',0.3,'EASY',125,null,null);");
        dbLite.execSQL("insert into TRAIL values(null, 'Grotto Falls','If you like the idea of crossing a rushing river via narrow tree trunks and a thundering waterfall finale - this trail is for you.','Payson/Nebo Scenic Loop','Utah','UT',0.3,'EASY',186,null,null);");
        dbLite.execSQL("insert into TRAIL values(null, 'Canyon Overlook Trail','This trail starts immediately after the Mount Carmel tunnel, and gives a stunning view of Zion National Park.','Zion National Park','Washington','UT',0.5,'EASY',141,30.00,27);");
        dbLite.execSQL("insert into TRAIL values(null, 'Rocky Mouth Waterfall','A short, rising hike that starts in a Sandy neighborhood and leads to a lovely waterfall.','Sandy','Salt Lake','UT',0.4,'EASY',324,null,null);");
        dbLite.execSQL("insert into TRAIL values(null,'Bridal Veil Falls','This is a the tallest waterfall in the state. At Bridal Veil you can grill, camp, hike bike, feed fish or splash around in the bottom of the falls!','Provo Canyon','Utah','UT', 0.4,'EASY', 80, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Battle Creek Falls','Battle Creek Falls near Springville is a quick hike to a decent sized waterfall. The trail follows a steady incline the whole way.','Springville','Utah','UT', 0.5,'Easy/Moderate', 436, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Big Springs Hollow','Big Springs Hollow near Provo offers scenic mountain views, aspen forests and lots of wooden bridges crossing babbling brooks. ','Provo Canyon - South Fork','Utah','UT', 0.6,'DIFFICULTY', 257, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Emerald Pools','These picturesque pools contrast nicely with the red rock of Zion.','Zion National Park','Washington','UT', 0.8,'Moderate', 360, 30.00, 27);");
        dbLite.execSQL("insert into TRAIL values(null,'Riverside Walk','This paved trail follows the Virgin River and provides great views of the canyon walls, river-access and leads to the Narrows.','Zion National Park','Washington','UT', 1,'Easy', 69, 30.00, 27);");
        dbLite.execSQL("insert into TRAIL values(null,'Timp Falls','Accessible from the Aspen Grove Trailhead near Sundance Ski Resort, this cascade is a nice alternative when Stewart Falls is busy.','Provo Canyon','Utah','UT', 1,'Easy/Moderate', 729, 6.00, 26);");
        dbLite.execSQL("insert into TRAIL values(null,'Scout Falls','Scout Falls is a great way to experience a portion of the beautiful Timpooneke trail without actually trekking all the way up Timp.','Provo Canyon','Utah','UT', 1.1,'MODERATE', 752, 6.00, 26);");
        dbLite.execSQL("insert into TRAIL values(null,'Lagoon Trail','This trails follows Farmington Creek through along the east end of Lagoon, and takes you through desne growth.','Farmington','Davis','UT', 1.3,'EASY', 15, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Timp Caves Hike','You don't need a ticket to do this great hike, but be sure to get some for the neat cave tours at the end!','American Fork Canyon','Utah','UT', 1.5,'Moderate/Strenuous', 1500, 8.00, 26);");
        dbLite.execSQL("insert into TRAIL values(null,'Parus Trail','This paved trail starts at the Zion Visitor Center and crosses over beautiful bridges.','Zion National Park','Washington','UT', 1.7,'Easy', 95, 30.00, 27);");
        dbLite.execSQL("insert into TRAIL values(null,'Stewart Falls','This popular family friendly hike ends in a 200 ft. waterfall near Sundance Ski Resort.','Provo Canyon','Utah','UT', 1.8,'Moderate', 18, 8.00, 26);");
        dbLite.execSQL("insert into TRAIL values(null,'Do(ugh)nut Falls','This is one of the most popular hikes up Big Cottonwood Canyon that leads to a small but steep waterfall.','Big Cottonwood Canyon','Salt Lake','UT', 1,8,'Easy', 428, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Malans Peak','A great canyon hike near Ogden Utah that's very reminiscent of Rivendell.','Ogden','Weber','UT', 1.8,'Moderate', 2086, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Adams Canyon Waterfall','Starting with initial switchbacks, this trail leads through lots of shade and some rugged terrain to one of the most popular places in Layton!','','Davis','UT', 1362,'Easy/Moderate', 1362, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Little Wildhorse Slot Canyon','This slot canyon north of Goblin Valley State Park is great for families, and allows non-technical hikers a slot canyon experience.','San Rafael Swell','Emery','UT', 1.9,'Easy/Moderate', 268, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Bells Canyon Waterfall','Leading to both a reservoir and waterfall, this popular hike is good for families wanting more of a challenge.','Salt Lake City','Salt Lake','UT', 2,'Moderate/Strenuous', 2204, null,null);");
        dbLite.execSQL("insert into TRAIL values(null,'Fifth Water Hot Springs','The Fifth Water trail leads to natural hot springs in the middle of Diamond Fork Canyon. There are natural hot springs pools fed by a lovely waterfall.','Spanish Fork','Utah','UT', 2.1,'Easy/Moderate', 598, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Angels Landing','This quintessential Zion hike rises up tight switchbacks to vertigo-inducing ridges over the Virgin River','Zion National Park','Washington','UT', 2.5,'Strenuous', 1496, 30.00, 27);");
        dbLite.execSQL("insert into TRAIL values(null,'Calf Creek Lower Falls','This desert waterfall attracts visitors from across the globe due to its beautiful colors, impressive height and unique location.','Grand Staircase/Escalante','Garfield','UT', 2.7,'Moderate', 356, null, 18);");
        dbLite.execSQL("insert into TRAIL values(null,'Y Mountain','This popular Utah county hike starts with switchbacks, but leads into the picturesque Slide Canyon up to the Y Mountain summit.','Provo','Utah','UT', 2.9,'Moderate', 3345, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'The Subway (from the Bottom Up)','This route is a way to enjoy the stunning beauty of the Subway without technical equipment.','Zion National Park','Washington','UT', 3.2,'Strenuous', 273, 30.00, 27);");
        dbLite.execSQL("insert into TRAIL values(null,'Squaw Peak','Otherwise known as Pride Rock, Squaw Peak up Rock Canyon in Provo takes you past tall mountains and winding forest paths up to a great viewpoint.','Provo','Utah','UT', 3.4,'Moderate', 2708, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Timpanogos Summit (Timpooneke)','The tallest mountain in the Wasatch provides stunning vistas, colorful wildflowers, glacial lakes and a lofty summit to boot!','Alpine','Utah','UT', 5.5,'Strenuous', 4385, null, 26);");
        dbLite.execSQL("insert into TRAIL values(null,'Red Reef Trail (Red Cliffs Recreation Area)','Tucked away in the west end of the Red Cliffs Recreation area, the Red Reef trail has a natural waterslide and beautiful red rock.','Leeds','Washington','UT', 4,'DIFFICULTY', 45, 10, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Timpanogos Summit (Aspen Grove)','An alternative (albeit steeper) route to the summit of Timpanogos. Wildflowers, mountain goats, glacial lakes - it is beautiful!','Provo Canyon','Utah','UT', 5,'Strenuous', 4857, 10, 26);");
        dbLite.execSQL("insert into TRAIL values(null,'Ogden River Parkway','Starting at Rainbow Gardens near Ogden Canyon, the Ogden Parkway follows the Ogden River in a winding, peaceful paved path to Fort Bueniventura.','Ogden','Weber','UT', 15,'Easy/Moderate', 117, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Provo River Parkway','The paved parkway follows the Provo River from Vivian Park up Provo Canyon all the way down to Utah Lake State Park.','Provo Canyon','Utah','UT', 15,'Easy/Moderate', 702, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Kanarraville Falls','This is a river hike up a the lovely Kanarra Creek up into a slot canyon with a waterfall.','Cedar City (Kanarraville)','Washington','UT', 1.26,'Moderate', 629, 10, null);");
        dbLite.execSQL("insert into TRAIL values(null,'Buffalo Peak','Buffalo Peak is a great peak to summit if you have enjoyed the view at Squeak Peak Overlook and want more of an adventure!','Provo Canyon','Utah','UT', 4,'Moderate', 1328, null, null);");
        dbLite.execSQL("insert into TRAIL values(null,'The Narrows','This river hike passes through a giant slot canyon with waterfalls, greenery and adventure. Make sure to bring your dry-bags and trekking poles!','Zion National Park','Washington','UT', 3,'DIFFICULTY', 20, 30, 27);");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
