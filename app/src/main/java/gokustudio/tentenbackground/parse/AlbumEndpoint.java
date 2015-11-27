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

import gokustudio.tentenbackground.models.parse.Album;

/**
 *
 * @author son
 */
public class AlbumEndpoint {
    
    public static final String OBJECT_NAME = "album";
    public static final String OBJECT_ID = "objectId";
    public static final String ID = "albumId";
    public static final String NAME = "name";
    public static final String GOOGLE_PLAY_ID = "ggplayId";
    
    public static void saveAlbumToLocal(Album album) throws ParseException {
        ParseObject albumObject = toParseObject(album);
        albumObject.pin();
    }
    
    public List<Album> getAll() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        List<Album> listAlbums = new ArrayList<>();
        try {
            List<ParseObject> list = query.find();
            for (ParseObject object : list) {
                Album album = get(object);
                listAlbums.add(album);
            }
        } catch (ParseException ex) {
          ex.printStackTrace();
        }
        return listAlbums;
    }
    
    public Album getFromLocal(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.fromLocalDatastore();
        try {
            ParseObject object = query.get(objectId);
            if (object != null) {
                Album album = get(object);
                return album;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Album get(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject object = query.get(objectId);
            if (object != null) {
                Album album = get(object);
                return album;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public Album getAlbumByName(String name) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(NAME, name).find();
            if (objects != null &&  objects.size() > 0) {
                Album album = get(objects.get(0));
                return album;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public Album getAlbumById(String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(ID, id).find();
            if (objects != null &&  objects.size() > 0) {
                Album album = get(objects.get(0));
                return album;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static Album get(ParseObject albumObject) {
        String objectId = albumObject.getObjectId();
        String id = albumObject.getString(ID);
        String name = albumObject.getString(NAME);
        String ggplayId = albumObject.getString(GOOGLE_PLAY_ID);
        return new Album(objectId, id, name, ggplayId);
    }
    
    public static ParseObject toParseObject(Album album) {
        ParseObject albumParseObject = new ParseObject(OBJECT_NAME);
        if (album.getObjectId() != null) {
            albumParseObject.setObjectId(album.getObjectId());
        }
        if (album.getGooglePlayId() != null) {
            albumParseObject.put(GOOGLE_PLAY_ID, album.getGooglePlayId());
        }
        albumParseObject.put(ID, album.getId());
        albumParseObject.put(NAME, album.getName());
        return albumParseObject;
    }
}
