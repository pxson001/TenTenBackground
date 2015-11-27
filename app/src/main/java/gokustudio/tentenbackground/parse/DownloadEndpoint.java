/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gokustudio.tentenbackground.parse;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import gokustudio.tentenbackground.models.parse.Download;

/**
 *
 * @author Son Pham
 */
public class DownloadEndpoint {

    public static final String OBJECT_NAME = "download";
    public static final String OBJECT_ID = "objectId";
    public static final String DAY_ID = "dayId";
    public static final String WEEK_ID = "weekId";
    public static final String MONTH_ID = "monthId";
    public static final String QUARTER_ID = "quarterId";
    public static final String YEAR_ID = "yearId";
    public static final String COUNT = "count";

    public void add(Download download) throws ParseException {
        ParseObject downloadObject = toParseObject(download);
        downloadObject.save();
    }

    public List<Download> getDailyDownload(int skip, int limit) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.orderByDescending(DAY_ID);
        query.orderByDescending(COUNT);
        query.setSkip(skip);
        query.setLimit(limit);
        List<Download> listDownloads = new ArrayList<>();
        try {
            List<ParseObject> list = query.find();
            for (ParseObject object : list) {
                Download download = get(object);
                listDownloads.add(download);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return listDownloads;
    }

    public Download get(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject object = query.get(objectId);
            if (object != null) {
                Download download = get(object);
                return download;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Download getDownload(Download download) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(DAY_ID, download.getDayId()).whereEqualTo(YEAR_ID, download.getYearId()).whereEqualTo(WallpaperEndpoint.ID, download.getWallpaperId()).find();
            if (objects != null && objects.size() > 0) {
                Download downloadResult = get(objects.get(0));
                return downloadResult;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void update(Download download) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject parseObject = query.get(download.getObjectId());
            if (parseObject != null) {
                parseObject.increment(COUNT);
                parseObject.save();
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject parseObject = query.get(objectId);
            if (parseObject != null) {
                parseObject.increment(COUNT);
                parseObject.save();
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public static Download get(ParseObject downloadObject) throws ParseException {
        String objectId = downloadObject.getObjectId();
        int dayId = downloadObject.getInt(DAY_ID);
        int weekId = downloadObject.getInt(WEEK_ID);
        int mothId = downloadObject.getInt(MONTH_ID);
        int quarterId = downloadObject.getInt(QUARTER_ID);
        int yearId = downloadObject.getInt(YEAR_ID);
        String wallpaperId = downloadObject.getString(WallpaperEndpoint.ID);
        int count = downloadObject.getInt(COUNT);

        Download download = new Download();
        download.setObjectId(objectId);
        download.setDayId(dayId);
        download.setWeekId(weekId);
        download.setMonthId(mothId);
        download.setQuarterId(quarterId);
        download.setYearId(yearId);
        download.setWallpaperId(wallpaperId);
        download.setCount(count);
        return download;
    }

    public static ParseObject toParseObject(Download download) {
        ParseObject downloadObject = new ParseObject(OBJECT_NAME);

        downloadObject.put(DAY_ID, download.getDayId());
        downloadObject.put(WEEK_ID, download.getWeekId());
        downloadObject.put(MONTH_ID, download.getMonthId());
        downloadObject.put(QUARTER_ID, download.getQuarterId());
        downloadObject.put(YEAR_ID, download.getYearId());
        downloadObject.put(WallpaperEndpoint.ID, download.getWallpaperId());
        downloadObject.put(COUNT, 1);
        return downloadObject;
    }


}
